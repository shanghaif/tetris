package com.suma.venus.resource.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.suma.application.ldap.contants.LdapContants;
import com.suma.venus.resource.base.bo.BundlePrivilegeBO;
import com.suma.venus.resource.base.bo.ResourceIdListBO;
import com.suma.venus.resource.base.bo.ResultBO;
import com.suma.venus.resource.base.bo.RoleAndResourceIdBO;
import com.suma.venus.resource.base.bo.UnbindResouceBO;
import com.suma.venus.resource.base.bo.UnbindRolePrivilegeBO;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.base.bo.UserresPrivilegeBO;
import com.suma.venus.resource.bo.PrivilegeStatusBO;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.dao.PrivilegeDAO;
import com.suma.venus.resource.dao.RolePrivilegeMapDAO;
import com.suma.venus.resource.feign.UserQueryFeign;
import com.suma.venus.resource.lianwang.auth.AuthNotifyXml;
import com.suma.venus.resource.lianwang.auth.AuthXmlUtil;
import com.suma.venus.resource.lianwang.auth.DevAuthXml;
import com.suma.venus.resource.lianwang.auth.UserAuthXml;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.PrivilegePO;
import com.suma.venus.resource.pojo.PrivilegePO.EPrivilegeType;
import com.suma.venus.resource.pojo.RolePrivilegeMap;
import com.suma.venus.resource.pojo.VirtualResourcePO;
import com.suma.venus.resource.service.BundleService;
import com.suma.venus.resource.service.UserQueryService;
import com.suma.venus.resource.service.VirtualResourceService;
import com.suma.venus.resource.util.XMLBeanUtils;

@Controller
@RequestMapping("/resource")
public class BindResourceController extends ControllerBase {

	private static final Logger LOGGER = LoggerFactory.getLogger(BindResourceController.class);

	private final String BIND_TYPE_BINDED = "binded";
	private final String BIND_TYPE_UNBINDED = "unbinded";

	@Autowired
	private BundleService bundleService;

	@Autowired
	private VirtualResourceService virtualResourceService;

	@Autowired
	private UserQueryFeign userFeign;

	@Autowired
	private AuthXmlUtil authXmlUtil;

	@Autowired
	private BundleDao bundleDao;
	
	@Autowired
	private PrivilegeDAO privilegeDao;
	
	@Autowired
	private RolePrivilegeMapDAO rolePrivilegeMapDao;

	// 联网中心消息服务注册ID
	@Value("${connectCenterLayerID}")
	private String connectCenterLayerID;
	
	@Autowired
	private UserQueryService userQueryService;

	@RequestMapping(value = "/getAllUser", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getAllUser() {
		Map<String, Object> data = makeAjaxData();
		try {
			List<UserBO> users = new ArrayList<UserBO>();
			users = userQueryService.queryAllUserBaseInfo();
			data.put("users", users);
		} catch (Exception e) {
			LOGGER.error(e.toString());
			data.put(ERRMSG, "查询用户错误");
		}

		return data;
	}

	@RequestMapping(value = "/queryBundlesOfRole", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryBundles(Long roleId, String bindType, String deviceModel, String keyword, int pageNum, int countPerPage) {
		try {
			if (BIND_TYPE_UNBINDED.equals(bindType)) {// 只查未绑定的资源
				List<BundlePrivilegeBO> bundlePrivileges = getUnbindedBundles(queryUnbindedBundleIds(roleId, deviceModel, keyword));
				return bundlePageResponse(pageNum, countPerPage, bundlePrivileges);
			} else if (BIND_TYPE_BINDED.equals(bindType)) {// 只查已绑定的资源
				List<BundlePrivilegeBO> bundlePrivileges = getBindedBundles(roleId, deviceModel, keyword);
				return bundlePageResponse(pageNum, countPerPage, bundlePrivileges);
			} else {// 绑定和未绑定都查
				List<BundlePrivilegeBO> bundlePrivileges = getBundles(roleId, deviceModel, keyword);
				return bundlePageResponse(pageNum, countPerPage, bundlePrivileges);
			}
		} catch (Exception e) {
			LOGGER.error(e.toString());
			Map<String, Object> data = makeAjaxData();
			data.put(ERRMSG, "查询错误");
			return data;
		}
	}

	@RequestMapping(value = "/queryUsersOfRole", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryUsersOfRole(Long roleId, String keyword, int pageNum, int countPerPage) {
		try {
			return userPageResponse(pageNum, countPerPage, getUsers(roleId, keyword));
		} catch (Exception e) {
			LOGGER.error(e.toString());
			Map<String, Object> data = makeAjaxData();
			data.put(ERRMSG, "查询错误");
			return data;
		}
	}

	private Map<String, Object> bundlePageResponse(int pageNum, int countPerPage, List<BundlePrivilegeBO> bundlePrivileges) {
		Map<String, Object> data = makeAjaxData();
		if (null != bundlePrivileges) {
			// 排序
			Collections.sort(bundlePrivileges, new Comparator<BundlePrivilegeBO>() {
				@Override
				public int compare(BundlePrivilegeBO o1, BundlePrivilegeBO o2) {
					return (int) (o2.getId() - o1.getId());
				}
			});
			int from = (pageNum - 1) * countPerPage;
			int to = (pageNum * countPerPage > bundlePrivileges.size()) ? bundlePrivileges.size() : pageNum * countPerPage;
			data.put("resources", bundlePrivileges.subList(from, to));
			data.put("total", bundlePrivileges.size());
			return data;
		} else {
			data.put("resources", new ArrayList<BundlePrivilegeBO>());
			data.put("total", 0);
			return data;
		}
	}

	private Map<String, Object> userPageResponse(int pageNum, int countPerPage, List<UserresPrivilegeBO> userresPrivileges) {
		Map<String, Object> data = makeAjaxData();
		if (null != userresPrivileges) {
			// 排序
			Collections.sort(userresPrivileges, new Comparator<UserresPrivilegeBO>() {
				@Override
				public int compare(UserresPrivilegeBO o1, UserresPrivilegeBO o2) {
					return (int) (o2.getId() - o1.getId());
				}
			});
			int from = (pageNum - 1) * countPerPage;
			int to = (pageNum * countPerPage > userresPrivileges.size()) ? userresPrivileges.size() : pageNum * countPerPage;
			data.put("users", userresPrivileges.subList(from, to));
			data.put("total", userresPrivileges.size());
			return data;
		} else {
			data.put("users", new ArrayList<BundlePrivilegeBO>());
			data.put("total", 0);
			return data;
		}
	}

	private List<BundlePrivilegeBO> getUnbindedBundles(Set<String> bundleIds) throws Exception {
		List<BundlePrivilegeBO> bundlePrivileges = new ArrayList<BundlePrivilegeBO>();
		if (null != bundleIds) {
			for (String bundleId : bundleIds) {
				BundlePO po = bundleService.findByBundleId(bundleId);
				bundlePrivileges.add(getBundlePrivilegefromPO(po));
			}
		}

		return bundlePrivileges;
	}

	private Set<String> queryUnbindedBundleIds(Long roleId, String deviceModel, String keyword) throws Exception {
		Set<String> bundleIds = bundleService.queryBundleIdSetByMultiParams(deviceModel, null, keyword, null);
		if (bundleIds.isEmpty()) {
			return null;
		}

//		ResourceIdListBO bindResourceIdBO = userFeign.queryResourceByRoleId(roleId);
		ResourceIdListBO bindResourceIdBO = queryResourceByRoleId(roleId);
		if (null != bindResourceIdBO && null != bindResourceIdBO.getResourceCodes() && !bindResourceIdBO.getResourceCodes().isEmpty()) {
			List<String> bindResourceCodes = filterBindedBundleIds(bindResourceIdBO.getResourceCodes());
			bundleIds.removeAll(bindResourceCodes);
		}
		return bundleIds;
	}

	// 过滤出已绑定的bundleId
	private List<String> filterBindedBundleIds(List<String> bindResourceCodes) {
		List<String> resultList = new ArrayList<String>();
		for (String bindResourceCode : bindResourceCodes) {
			if (bindResourceCode.endsWith("-r") || bindResourceCode.endsWith("-w")) {
				resultList.add(bindResourceCode.substring(0, bindResourceCode.length() - 2));
			}
		}
		return resultList;
	}

	/** 查询角色具有权限的符合查询条件的bundle资源 */
	private List<BundlePrivilegeBO> getBindedBundles(Long roleId, String deviceModel, String keyword) throws Exception {
		List<BundlePrivilegeBO> bundlePrivileges = new ArrayList<BundlePrivilegeBO>();
		Set<String> bundleIds = bundleService.queryBundleIdSetByMultiParams(deviceModel, null, keyword, null);
		if (bundleIds.isEmpty()) {
			return bundlePrivileges;
		}

//		ResourceIdListBO bindResourceIdBO = userFeign.queryResourceByRoleId(roleId);
		ResourceIdListBO bindResourceIdBO = queryResourceByRoleId(roleId);
		if (null != bindResourceIdBO && null != bindResourceIdBO.getResourceCodes() && !bindResourceIdBO.getResourceCodes().isEmpty()) {
			Set<String> privilegeCodes = new HashSet<String>(bindResourceIdBO.getResourceCodes());
			for (String bundleId : bundleIds) {
				boolean hasReadPrivilege = privilegeCodes.contains(bundleId + "-r");
				boolean hasWritePrivilege = privilegeCodes.contains(bundleId + "-w");
				if (!hasReadPrivilege && !hasWritePrivilege) {
					continue;
				}
				BundlePO po = bundleService.findByBundleId(bundleId);
				BundlePrivilegeBO bundlePrivilege = getBundlePrivilegefromPO(po);
				if (hasReadPrivilege) {
					bundlePrivilege.setHasReadPrivilege(true);
				}
				if (hasWritePrivilege) {
					bundlePrivilege.setHasWritePrivilege(true);
				}
				bundlePrivileges.add(bundlePrivilege);
			}
		} else {
			return new ArrayList<BundlePrivilegeBO>();
		}

		return bundlePrivileges;
	}

	/** 查询所有资源，并标记其中有权限的资源 */
	private List<BundlePrivilegeBO> getBundles(Long roleId, String deviceModel, String keyword) throws Exception{
		List<BundlePrivilegeBO> bundlePrivileges = new ArrayList<BundlePrivilegeBO>();
		Set<String> bundleIds = bundleService.queryBundleIdSetByMultiParams(deviceModel, null, keyword, null);
		if (bundleIds.isEmpty()) {
			return bundlePrivileges;
		}

//		ResourceIdListBO bindResourceIdBO = userFeign.queryResourceByRoleId(roleId);
		ResourceIdListBO bindResourceIdBO = queryResourceByRoleId(roleId);
		if (null != bindResourceIdBO && null != bindResourceIdBO.getResourceCodes() && !bindResourceIdBO.getResourceCodes().isEmpty()) {
			Set<String> privilegeCodes = new HashSet<String>(bindResourceIdBO.getResourceCodes());
			for (String bundleId : bundleIds) {
				boolean hasReadPrivilege = privilegeCodes.contains(bundleId + "-r");
				boolean hasWritePrivilege = privilegeCodes.contains(bundleId + "-w");
				BundlePO po = bundleService.findByBundleId(bundleId);
				BundlePrivilegeBO bundlePrivilege = getBundlePrivilegefromPO(po);
				if (hasReadPrivilege) {
					bundlePrivilege.setHasReadPrivilege(true);
				}
				if (hasWritePrivilege) {
					bundlePrivilege.setHasWritePrivilege(true);
				}
				bundlePrivileges.add(bundlePrivilege);
			}
		} else {
			for (String bundleId : bundleIds) {
				BundlePO po = bundleService.findByBundleId(bundleId);
				BundlePrivilegeBO bundlePrivilege = getBundlePrivilegefromPO(po);
				bundlePrivileges.add(bundlePrivilege);
			}
		}

		return bundlePrivileges;
	}
	
	private ResourceIdListBO queryResourceByRoleId(Long roleId) throws Exception{
		
		List<PrivilegePO> privileges = privilegeDao.findByRoleId(roleId);
		
		ResourceIdListBO bo = new ResourceIdListBO();
		bo.setResourceCodes(new ArrayList<String>());
		for(PrivilegePO privilege: privileges){
			bo.getResourceCodes().add(privilege.getResourceIndentity());
		}
		
		return bo;
		
	}
	
	/** 查询所有用户，并标记其中有权限的用户 */
	private List<UserresPrivilegeBO> getUsers(Long roleId, String keyword) throws Exception{
		List<UserresPrivilegeBO> userresPrivilegeBOs = new ArrayList<>();
		List<UserBO> allUsers = userQueryService.queryUserLikeUserName(keyword);
//		Map<String, List<UserBO>> resultMap = userFeign.queryUsersByNameLike(keyword)/* userFeign.queryUsers() */;
//		if (null != resultMap && null != resultMap.get("users")) {
//			allUsers = resultMap.get("users");
//		}
		if (null == allUsers || allUsers.isEmpty()) {
			return userresPrivilegeBOs;
		}

//		ResourceIdListBO bindResourceIdBO = userFeign.queryResourceByRoleId(roleId);
		ResourceIdListBO bindResourceIdBO = queryResourceByRoleId(roleId);
		if (null != bindResourceIdBO && null != bindResourceIdBO.getResourceCodes() && !bindResourceIdBO.getResourceCodes().isEmpty()) {
			Set<String> privilegeCodes = new HashSet<String>(bindResourceIdBO.getResourceCodes());
			for (UserBO userBO : allUsers) {
				boolean hasReadPrivilege = privilegeCodes.contains(userBO.getUserNo() + "-r");
				boolean hasWritePrivilege = privilegeCodes.contains(userBO.getUserNo() + "-w");
				boolean hasHJPrivilege = privilegeCodes.contains(userBO.getUserNo() + "-hj");
				UserresPrivilegeBO userresPrivilege = getUserresPrivilegeFromUserBO(userBO);
				if (hasReadPrivilege) {
					userresPrivilege.setHasReadPrivilege(true);
				}
				if (hasWritePrivilege) {
					userresPrivilege.setHasWritePrivilege(true);
				}
				if (hasHJPrivilege) {
					userresPrivilege.setHasHJPrivilege(true);
				}
				userresPrivilegeBOs.add(userresPrivilege);
			}
		} else {
			for (UserBO userBO : allUsers) {
				userresPrivilegeBOs.add(getUserresPrivilegeFromUserBO(userBO));
			}
		}

		return userresPrivilegeBOs;
	}

	@RequestMapping(value = "/queryVirtualResourcesOfRole", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryVirtualResources(Long roleId, String bindType, String keyword, int pageNum, int countPerPage) {
		try {
			List<JSONObject> resourcePrivileges = null;
			if (BIND_TYPE_UNBINDED.equals(bindType)) {// 只查未绑定的资源
				resourcePrivileges = getUnbindedVirtualResources(roleId, keyword);
			} else if (BIND_TYPE_BINDED.equals(bindType)) {// 只查已绑定的资源
				resourcePrivileges = getBindedVirtualResources(roleId, keyword);
			} else {// 绑定和未绑定都查
				resourcePrivileges = getVirtualResources(roleId, keyword);
			}
			return virtualResourcePageResponse(pageNum, countPerPage, resourcePrivileges);
		} catch (Exception e) {
			LOGGER.error(e.toString());
			Map<String, Object> data = makeAjaxData();
			data.put(ERRMSG, "查询错误");
			return data;
		}
	}

	private List<JSONObject> getVirtualResources(Long roleId, String keyword) throws Exception {
		List<JSONObject> virtualResourcePrivileges = new ArrayList<JSONObject>();
		Set<String> resourceIds = virtualResourceService.queryResourceIdByKeyword(keyword);
		if (resourceIds.isEmpty()) {
			return virtualResourcePrivileges;
		}

//		ResourceIdListBO bindResourceIdBO = userFeign.queryResourceByRoleId(roleId);
		ResourceIdListBO bindResourceIdBO = queryResourceByRoleId(roleId);
		if (null != bindResourceIdBO && null != bindResourceIdBO.getResourceCodes() && !bindResourceIdBO.getResourceCodes().isEmpty()) {
			Set<String> privilegeCodes = new HashSet<String>(bindResourceIdBO.getResourceCodes());
			for (String resourceId : resourceIds) {
				JSONObject resourceJson = new JSONObject();
				resourceJson.put("resourceId", resourceId);
				List<VirtualResourcePO> pos = virtualResourceService.findByResourceId(resourceId);
				for (VirtualResourcePO po : pos) {
					resourceJson.put(po.getAttrName(), po.getAttrValue());
				}
				if (privilegeCodes.contains(resourceId)) {
					resourceJson.put("hasPrivilege", true);
				} else {
					resourceJson.put("hasPrivilege", false);
				}
				virtualResourcePrivileges.add(resourceJson);
			}
		} else {
			for (String resourceId : resourceIds) {
				JSONObject resourceJson = new JSONObject();
				resourceJson.put("resourceId", resourceId);
				List<VirtualResourcePO> pos = virtualResourceService.findByResourceId(resourceId);
				for (VirtualResourcePO po : pos) {
					resourceJson.put(po.getAttrName(), po.getAttrValue());
				}
				resourceJson.put("hasPrivilege", false);
				virtualResourcePrivileges.add(resourceJson);
			}
		}

		return virtualResourcePrivileges;
	}

	/** 查询角色权限范围内的虚拟资源 */
	private List<JSONObject> getBindedVirtualResources(Long roleId, String keyword) throws Exception{
		List<JSONObject> virtualResourcePrivileges = new ArrayList<JSONObject>();
		Set<String> resourceIds = virtualResourceService.queryResourceIdByKeyword(keyword);
		if (resourceIds.isEmpty()) {
			return virtualResourcePrivileges;
		}

//		ResourceIdListBO bindResourceIdBO = userFeign.queryResourceByRoleId(roleId);
		ResourceIdListBO bindResourceIdBO = queryResourceByRoleId(roleId);
		if (null != bindResourceIdBO && null != bindResourceIdBO.getResourceCodes() && !bindResourceIdBO.getResourceCodes().isEmpty()) {
			Set<String> privilegeCodes = new HashSet<String>(bindResourceIdBO.getResourceCodes());
			for (String resourceId : resourceIds) {
				if (privilegeCodes.contains(resourceId)) {
					JSONObject resourceJson = new JSONObject();
					resourceJson.put("resourceId", resourceId);
					List<VirtualResourcePO> pos = virtualResourceService.findByResourceId(resourceId);
					for (VirtualResourcePO po : pos) {
						resourceJson.put(po.getAttrName(), po.getAttrValue());
					}
					resourceJson.put("hasPrivilege", true);
					virtualResourcePrivileges.add(resourceJson);
				}
			}
		}

		return virtualResourcePrivileges;
	}

	/** 查询角色权限未绑定的虚拟资源 */
	private List<JSONObject> getUnbindedVirtualResources(Long roleId, String keyword) throws Exception{
		List<JSONObject> virtualResourcePrivileges = new ArrayList<JSONObject>();
		Set<String> resourceIds = virtualResourceService.queryResourceIdByKeyword(keyword);
		if (resourceIds.isEmpty()) {
			return virtualResourcePrivileges;
		}

//		ResourceIdListBO bindResourceIdBO = userFeign.queryResourceByRoleId(roleId);
		ResourceIdListBO bindResourceIdBO = queryResourceByRoleId(roleId);
		if (null != bindResourceIdBO && null != bindResourceIdBO.getResourceCodes() && !bindResourceIdBO.getResourceCodes().isEmpty()) {
			Set<String> privilegeCodes = new HashSet<String>(bindResourceIdBO.getResourceCodes());
			resourceIds.removeAll(privilegeCodes);
		}

		for (String resourceId : resourceIds) {
			JSONObject resourceJson = new JSONObject();
			resourceJson.put("resourceId", resourceId);
			List<VirtualResourcePO> pos = virtualResourceService.findByResourceId(resourceId);
			for (VirtualResourcePO po : pos) {
				resourceJson.put(po.getAttrName(), po.getAttrValue());
			}
			resourceJson.put("hasPrivilege", false);
			virtualResourcePrivileges.add(resourceJson);
		}

		return virtualResourcePrivileges;
	}

	private Map<String, Object> virtualResourcePageResponse(int pageNum, int countPerPage, List<JSONObject> virtualResourcePrivileges) {
		Map<String, Object> data = makeAjaxData();
		if (null != virtualResourcePrivileges) {
			// 排序
			Collections.sort(virtualResourcePrivileges, new Comparator<JSONObject>() {
				@Override
				public int compare(JSONObject o1, JSONObject o2) {
					return o2.getString("resourceId").compareTo(o1.getString("resourceId"));
				}
			});
			int from = (pageNum - 1) * countPerPage;
			int to = (pageNum * countPerPage > virtualResourcePrivileges.size()) ? virtualResourcePrivileges.size() : pageNum * countPerPage;
			data.put("resources", virtualResourcePrivileges.subList(from, to));
			data.put("total", virtualResourcePrivileges.size());
			return data;
		} else {
			data.put("resources", new ArrayList<BundlePrivilegeBO>());
			data.put("total", 0);
			return data;
		}
	}

	/** 查询虚拟资源信息 */
	@RequestMapping(value = "/queryVirtualResourceInfo", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryVirtualResourceInfo(@RequestParam(value = "resourceId") String resourceId) {
		Map<String, Object> data = makeAjaxData();
		try {
			List<VirtualResourcePO> infos = virtualResourceService.findByResourceId(resourceId);
			data.put("infos", infos);
		} catch (Exception e) {
			LOGGER.error(e.toString());
			data.put(ERRMSG, "查询错误");
		}

		return data;
	}

	/** 提交bundle权限绑定请求 */
	@RequestMapping(value = "/submitBundlePrivilege", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> submitBundlePrivilege(@RequestParam(value = "roleId") Long roleId, @RequestParam(value = "prevReadChecks") String prevReadChecks,
			@RequestParam(value = "prevWriteChecks") String prevWriteChecks, @RequestParam(value = "readChecks") String readChecks,
			@RequestParam(value = "writeChecks") String writeChecks, Principal principal) {
		Map<String, Object> data = makeAjaxData();

		try {
			List<String> preReadCheckList = new ArrayList<String>();
			List<String> prevWriteCheckList = new ArrayList<String>();
			List<String> readCheckList = new ArrayList<String>();
			List<String> writeCheckList = new ArrayList<String>();
			if (null != prevReadChecks && !prevReadChecks.isEmpty()) {
				preReadCheckList = Arrays.asList(prevReadChecks.split(","));
			}
			if (null != prevWriteChecks && !prevWriteChecks.isEmpty()) {
				prevWriteCheckList = Arrays.asList(prevWriteChecks.split(","));
			}
			if (null != readChecks && !readChecks.isEmpty()) {
				readCheckList = Arrays.asList(readChecks.split(","));
			}
			if (null != writeChecks && !writeChecks.isEmpty()) {
				writeCheckList = Arrays.asList(writeChecks.split(","));
			}

			List<String> toBindReadCheckList = getToBindPrivileges(preReadCheckList, readCheckList);
			List<String> toBindWriteCheckList = getToBindPrivileges(prevWriteCheckList, writeCheckList);
			List<String> toBindChecks = new ArrayList<String>();
			for (String readCheck : toBindReadCheckList) {
				toBindChecks.add(readCheck + "-r");
			}
			for (String writeCheck : toBindWriteCheckList) {
				toBindChecks.add(writeCheck + "-w");
			}
			if (!bindResourceCodes(roleId, toBindChecks)) {
				data.put(ERRMSG, "绑定失败");
				return data;
			}

			List<String> toUnbindReadCheckList = getToUnbindPrivileges(preReadCheckList, readCheckList);
			List<String> toUnbindWriteCheList = getToUnbindPrivileges(prevWriteCheckList, writeCheckList);
			List<String> toUnbindChecks = new ArrayList<String>();
			for (String readCheck : toUnbindReadCheckList) {
				toUnbindChecks.add(readCheck + "-r");
			}
			for (String writeCheck : toUnbindWriteCheList) {
				toUnbindChecks.add(writeCheck + "-w");
			}
			if (!unbindResourceCodes(roleId, toUnbindChecks)) {
				data.put(ERRMSG, "解绑失败");
				return data;
			}

			// 发送授权信息通知
			try {
//				List<UserBO> userBOs = userFeign.queryUsersByRole(roleId).get("users");
				List<UserBO> userBOs = userQueryService.queryUsersByRole(roleId);
				if (null != userBOs) {
					// 当前登录（操作）用户, TODO: 需要改
//					String oprusername = principal.getName();
//					UserBO oprUserBO = userFeign.queryUserInfo(oprusername).get("user");
					UserBO oprUserBO = userQueryService.current();
					Map<String, PrivilegeStatusBO> privilegeStatusMap = getPrivilegeStatusMap(preReadCheckList, prevWriteCheckList, new ArrayList<String>(), readCheckList,
							writeCheckList, new ArrayList<String>());
					for (UserBO userBO : userBOs) {
						if ("ldap".equals(userBO.getCreater())) {
							for (Entry<String, PrivilegeStatusBO> entry : privilegeStatusMap.entrySet()) {
								BundlePO bundle = bundleDao.findByBundleId(entry.getKey());
								if (null == bundle) {
									continue;
								}
								AuthNotifyXml authNotifyXml = new AuthNotifyXml();
								authNotifyXml.setAuthnodeid(LdapContants.DEFAULT_NODE_UUID);
								authNotifyXml.setAuthuserid(oprUserBO.getUserNo());
								authNotifyXml.setUserid(userBO.getUserNo());
								authNotifyXml.setOperation(entry.getValue().getDevOprType());
								String authCode = null;
								if (!"remove".equalsIgnoreCase(authNotifyXml.getOperation())) {
									authCode = entry.getValue().getDevAuthCode();
								}
								authNotifyXml.getDevlist().add(new DevAuthXml(bundle.getUsername(), authCode));
								// 发送消息
								JSONObject msgJson = authXmlUtil.createWholeAuthNotifyMessage(XMLBeanUtils.beanToXml(AuthNotifyXml.class, authNotifyXml), connectCenterLayerID);
								authXmlUtil.sendAuthNotifyXmlMsg(connectCenterLayerID, msgJson.toJSONString());
							}
						}
					}

				}
			} catch (Exception e) {
				LOGGER.error("", e);
			}

		} catch (Exception e) {
			LOGGER.error(e.toString());
			data.put(ERRMSG, "请求失败");
		}

		return data;
	}

	/** 提交用户权限绑定请求 */
	@RequestMapping(value = "/submitUserresPrivilege", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> submitUserresPrivilege(@RequestParam(value = "roleId") Long roleId, @RequestParam(value = "prevReadChecks") String prevReadChecks,
			@RequestParam(value = "prevWriteChecks") String prevWriteChecks, @RequestParam(value = "prevHJChecks") String prevHJChecks,
			@RequestParam(value = "readChecks") String readChecks, @RequestParam(value = "writeChecks") String writeChecks, @RequestParam(value = "hjChecks") String hjChecks,
			Principal principal) {
		Map<String, Object> data = makeAjaxData();

		try {
			List<String> preReadCheckList = new ArrayList<String>();
			List<String> prevWriteCheckList = new ArrayList<String>();
			List<String> prevHJCheckeList = new ArrayList<String>();
			List<String> readCheckList = new ArrayList<String>();
			List<String> writeCheckList = new ArrayList<String>();
			List<String> hjCheckList = new ArrayList<String>();
			if (null != prevReadChecks && !prevReadChecks.isEmpty()) {
				preReadCheckList = Arrays.asList(prevReadChecks.split(","));
			}
			if (null != prevWriteChecks && !prevWriteChecks.isEmpty()) {
				prevWriteCheckList = Arrays.asList(prevWriteChecks.split(","));
			}
			if (null != prevHJChecks && !prevHJChecks.isEmpty()) {
				prevHJCheckeList = Arrays.asList(prevHJChecks.split(","));
			}
			if (null != readChecks && !readChecks.isEmpty()) {
				readCheckList = Arrays.asList(readChecks.split(","));
			}
			if (null != writeChecks && !writeChecks.isEmpty()) {
				writeCheckList = Arrays.asList(writeChecks.split(","));
			}
			if (null != hjChecks && !hjChecks.isEmpty()) {
				hjCheckList = Arrays.asList(hjChecks.split(","));
			}

			List<String> toBindReadCheckList = getToBindPrivileges(preReadCheckList, readCheckList);
			List<String> toBindWriteCheckList = getToBindPrivileges(prevWriteCheckList, writeCheckList);
			List<String> toBindHJCheckList = getToBindPrivileges(prevHJCheckeList, hjCheckList);
			List<String> toBindChecks = new ArrayList<String>();
			for (String readCheck : toBindReadCheckList) {
				toBindChecks.add(readCheck + "-r");
			}
			for (String writeCheck : toBindWriteCheckList) {
				toBindChecks.add(writeCheck + "-w");
			}
			for (String hjCheck : toBindHJCheckList) {
				toBindChecks.add(hjCheck + "-hj");
			}
			if (!bindResourceCodes(roleId, toBindChecks)) {
				data.put(ERRMSG, "绑定失败");
				return data;
			}

			List<String> toUnbindReadCheckList = getToUnbindPrivileges(preReadCheckList, readCheckList);
			List<String> toUnbindWriteCheList = getToUnbindPrivileges(prevWriteCheckList, writeCheckList);
			List<String> toUnbindHJCheckList = getToUnbindPrivileges(prevHJCheckeList, hjCheckList);
			List<String> toUnbindChecks = new ArrayList<String>();
			for (String readCheck : toUnbindReadCheckList) {
				toUnbindChecks.add(readCheck + "-r");
			}
			for (String writeCheck : toUnbindWriteCheList) {
				toUnbindChecks.add(writeCheck + "-w");
			}
			for (String hjCheck : toUnbindHJCheckList) {
				toUnbindChecks.add(hjCheck + "-hj");
			}
			if (!unbindResourceCodes(roleId, toUnbindChecks)) {
				data.put(ERRMSG, "解绑失败");
				return data;
			}

			// 发送授权信息通知
			try {
//				List<UserBO> userBOs = userFeign.queryUsersByRole(roleId).get("users");
				List<UserBO> userBOs = userQueryService.queryUsersByRole(roleId);
				if (null != userBOs) {
					// 当前登录（操作）用户
//					String oprusername = principal.getName();
//					UserBO oprUserBO = userFeign.queryUserInfo(oprusername).get("user");
					UserBO oprUserBO = userQueryService.current();
					Map<String, PrivilegeStatusBO> privilegeStatusMap = getPrivilegeStatusMap(preReadCheckList, prevWriteCheckList, prevHJCheckeList, readCheckList, writeCheckList,
							hjCheckList);
					for (UserBO userBO : userBOs) {
						if ("ldap".equals(userBO.getCreater())) {
							for (Entry<String, PrivilegeStatusBO> entry : privilegeStatusMap.entrySet()) {
								AuthNotifyXml authNotifyXml = new AuthNotifyXml();
								authNotifyXml.setAuthnodeid(LdapContants.DEFAULT_NODE_UUID);
								authNotifyXml.setAuthuserid(oprUserBO.getUserNo());
								authNotifyXml.setUserid(userBO.getUserNo());
								authNotifyXml.setOperation(entry.getValue().getUserOprType());
								String authCode = null;
								if (!"remove".equalsIgnoreCase(authNotifyXml.getOperation())) {
									authCode = entry.getValue().getUserAuthCode();
								}
								authNotifyXml.getUserlist().add(new UserAuthXml(entry.getKey(), authCode));
								// 发送消息
								JSONObject msgJson = authXmlUtil.createWholeAuthNotifyMessage(XMLBeanUtils.beanToXml(AuthNotifyXml.class, authNotifyXml), connectCenterLayerID);
								authXmlUtil.sendAuthNotifyXmlMsg(connectCenterLayerID, msgJson.toJSONString());
							}
						}
					}

				}
			} catch (Exception e) {
				LOGGER.error("", e);
			}

		} catch (Exception e) {
			LOGGER.error(e.toString());
			data.put(ERRMSG, "请求失败");
		}

		return data;
	}

	private Map<String, PrivilegeStatusBO> getPrivilegeStatusMap(List<String> preReadCheckList, List<String> prevWriteCheckList, List<String> prevHJCheckeList,
			List<String> readCheckList, List<String> writeCheckList, List<String> hjCheckList) {
		Map<String, PrivilegeStatusBO> privilegeStatusMap = createPrivilegeStatusMap(preReadCheckList, prevWriteCheckList, prevHJCheckeList, readCheckList, writeCheckList,
				hjCheckList);
		for (Entry<String, PrivilegeStatusBO> entry : privilegeStatusMap.entrySet()) {
			if (preReadCheckList.contains(entry.getKey())) {
				entry.getValue().setPrevCanRead(true);
			}
			if (prevWriteCheckList.contains(entry.getKey())) {
				entry.getValue().setPrevCanWrite(true);
			}
			if (prevHJCheckeList.contains(entry.getKey())) {
				entry.getValue().setPrevCanHJ(true);
			}
			if (readCheckList.contains(entry.getKey())) {
				entry.getValue().setNowCanRead(true);
			}
			if (writeCheckList.contains(entry.getKey())) {
				entry.getValue().setNowCanWrite(true);
			}
			if (hjCheckList.contains(entry.getKey())) {
				entry.getValue().setNowCanHJ(true);
			}
		}
		return privilegeStatusMap;
	}

	private Map<String, PrivilegeStatusBO> createPrivilegeStatusMap(List<String> preReadCheckList, List<String> prevWriteCheckList, List<String> prevHJCheckeList,
			List<String> readCheckList, List<String> writeCheckList, List<String> hjCheckList) {
		Map<String, PrivilegeStatusBO> privilegeStatusMap = new HashMap<>();
		for (String code : preReadCheckList) {
			privilegeStatusMap.put(code, new PrivilegeStatusBO(code));
		}
		for (String code : prevWriteCheckList) {
			privilegeStatusMap.put(code, new PrivilegeStatusBO(code));
		}
		for (String code : prevHJCheckeList) {
			privilegeStatusMap.put(code, new PrivilegeStatusBO(code));
		}
		for (String code : readCheckList) {
			privilegeStatusMap.put(code, new PrivilegeStatusBO(code));
		}
		for (String code : writeCheckList) {
			privilegeStatusMap.put(code, new PrivilegeStatusBO(code));
		}
		for (String code : hjCheckList) {
			privilegeStatusMap.put(code, new PrivilegeStatusBO(code));
		}
		return privilegeStatusMap;
	}

	/** 提交虚拟资源权限绑定请求 */
	@RequestMapping(value = "/submitVirtualResourcePrivilege", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> submitVirtualResourcePrivilege(@RequestParam(value = "roleId") Long roleId, @RequestParam(value = "prevChecks") String prevChecks,
			@RequestParam(value = "checks") String checks) {
		Map<String, Object> data = makeAjaxData();

		try {
			List<String> preCheckList = prevChecks.isEmpty() ? new ArrayList<String>() : Arrays.asList(prevChecks.split(","));
			List<String> checkList = checks.isEmpty() ? new ArrayList<String>() : Arrays.asList(checks.split(","));

			List<String> toBindCheckList = getToBindPrivileges(preCheckList, checkList);
			if (!toBindCheckList.isEmpty()) {
				if (!bindResourceCodes(roleId, toBindCheckList)) {
					data.put(ERRMSG, "绑定失败");
					return data;
				}
			}

			List<String> toUnbindCheckList = getToUnbindPrivileges(preCheckList, checkList);
			if (!toUnbindCheckList.isEmpty()) {
				if (!unbindResourceCodes(roleId, toUnbindCheckList)) {
					data.put(ERRMSG, "解绑失败");
					return data;
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.toString());
			data.put(ERRMSG, "请求失败");
		}

		return data;
	}

	private boolean bindResourceCodes(Long roleId, List<String> toBindChecks) throws Exception {
		if (!toBindChecks.isEmpty()) {
			RoleAndResourceIdBO bo = new RoleAndResourceIdBO();
			bo.setRoleId(roleId);
			bo.setResourceCodes(toBindChecks);
			
			return bindRolePrivilege(bo);
//			ResultBO result = userFeign.bindRolePrivilege(bo);
//			if (null == result || !result.isResult()) {
//				return false;
//			}
		}
		return true;
	}
	
	/**
	 * 角色绑定权限<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月26日 下午2:00:49
	 * @param RoleAndResourceIdBO param
	 * @return boolean
	 */
	private boolean bindRolePrivilege(RoleAndResourceIdBO param) throws Exception{
		
		LOGGER.info("=================/bindRolePrivilege===========BindRolePrivilegeBO is " + ((null == param) ? "null" : JSONObject.toJSON(param).toString()));

		if (null == param.getRoleId() || param.getRoleId() == 0l) {
			return false;
		}

		if (null == param.getResourceCodes() || param.getResourceCodes().isEmpty()) {
			return false;
		}

		//TODO: feign中加
//		RolePO role = roleService.findById(param.getRoleId());
//		if (null == role) {
//			return false;
//		}
		List<String> resources = new ArrayList<String>();
		for (String resource : param.getResourceCodes()) {
			resources.add(resource);
		}
		
		List<PrivilegePO> privileges = privilegeDao.findByResourceIndentityIn(resources);
		
		// 先保存权限
		List<RolePrivilegeMap> maps = new ArrayList<RolePrivilegeMap>();
		for (String resource : param.getResourceCodes()) {
			PrivilegePO privilege = null;
			for(PrivilegePO _privilege: privileges){
				if(_privilege.getResourceIndentity().equals(resource)){
					privilege = _privilege;
					break;
				}
			}
			if (null == privilege) {
				PrivilegePO p = new PrivilegePO();
				p.setbEdit(false);
				p.setName("roleId-" + param.getRoleId() + "-privilege-" + resource);
				p.setpCode("customcode");
				p.setPrivilegeType(EPrivilegeType.CUSTOM);
				p.setResourceIndentity(resource);
				privilegeDao.save(p);
				privilege = p;
				// 进行绑定
			}
			RolePrivilegeMap mapR = new RolePrivilegeMap();
			mapR.setPrivilegeId(privilege.getId());
			mapR.setRoleId(param.getRoleId());
			maps.add(mapR);
		}
		
		rolePrivilegeMapDao.save(maps);
		
		return true;
	}

	private boolean unbindResourceCodes(Long roleId, List<String> toUnbindCheckList) throws Exception{
		if (!toUnbindCheckList.isEmpty()) {
			UnbindRolePrivilegeBO bo = new UnbindRolePrivilegeBO();
			bo.setRoleId(roleId);
			bo.setUnbindPrivilege(new ArrayList<UnbindResouceBO>());
			for (String toUnbindCheck : toUnbindCheckList) {
				UnbindResouceBO unbind = new UnbindResouceBO();
				unbind.setResourceCode(toUnbindCheck);
				bo.getUnbindPrivilege().add(unbind);
			}
			
			return unbindRolePrivilege(bo);
//			ResultBO result = userFeign.unbindRolePrivilege(bo);
//			if (null == result || !result.isResult()) {
//				return false;
//			}
		}
		return true;
	}
	
	/**
	 * 角色解绑权限<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月26日 下午2:02:39
	 */
	private boolean unbindRolePrivilege(UnbindRolePrivilegeBO param) throws Exception{
		
		if (null == param.getRoleId() || param.getRoleId() == 0l) {
			return false;
		}

		if (null == param.getUnbindPrivilege() || param.getUnbindPrivilege().isEmpty()) {
			return false;
		}

		//TODO: 新feign里面写
//		RolePO role = roleService.findById(param.getRoleId());
//		if (null == role) {
//			data.put(ERRMSG, "用户角色为空");
//			data.put("result", false);
//			return data;
//		}
		List<String> resources = new ArrayList<String>();
		for (UnbindResouceBO resource : param.getUnbindPrivilege()) {
			resources.add(resource.getResourceCode());
		}
		List<PrivilegePO> privileges = privilegeDao.findByResourceIndentityIn(resources);
		List<Long> privilegeIds = new ArrayList<Long>();
		for(PrivilegePO po: privileges){
			privilegeIds.add(po.getId());
		}
		
		List<RolePrivilegeMap> maps = rolePrivilegeMapDao.findByRoleIdAndPrivilegeIdIn(param.getRoleId(), privilegeIds);
		if(maps != null && maps.size()>0){
			rolePrivilegeMapDao.delete(maps);
		}
		
		List<PrivilegePO> needDeletePrivileges = new ArrayList<PrivilegePO>();
		for (UnbindResouceBO resource : param.getUnbindPrivilege()) {
			for(PrivilegePO po: privileges){
				if(po.getResourceIndentity().equals(resource.getResourceCode()) && resource.isbDelete()){
					needDeletePrivileges.add(po);
					break;
				}
			}
		}
		if(needDeletePrivileges.size() > 0){
			privilegeDao.delete(needDeletePrivileges);
		}
		
		return true;
	}

	/** 获取要绑定的资源ID集合 */
	private List<String> getToBindPrivileges(List<String> prevCheckList, List<String> checkList) {
		if (prevCheckList.isEmpty()) {
			return checkList;
		}

		List<String> toBindList = new ArrayList<String>();
		for (String checkId : checkList) {
			if (!prevCheckList.contains(checkId)) {
				toBindList.add(checkId);
			}
		}
		return toBindList;
	}

	/** 获取要解绑的资源ID集合 */
	private List<String> getToUnbindPrivileges(List<String> prevCheckList, List<String> checkList) {
		if (checkList.isEmpty()) {
			return prevCheckList;
		}

		List<String> toUnbindList = new ArrayList<String>();
		for (String prevCheckId : prevCheckList) {
			if (!checkList.contains(prevCheckId)) {
				toUnbindList.add(prevCheckId);
			}
		}
		return toUnbindList;
	}

	private BundlePrivilegeBO getBundlePrivilegefromPO(BundlePO po) {
		BundlePrivilegeBO bundlePrivilege = new BundlePrivilegeBO();
		bundlePrivilege.setId(po.getId());
		bundlePrivilege.setBundleId(po.getBundleId());
		bundlePrivilege.setDeviceModel(po.getDeviceModel());
		bundlePrivilege.setName(po.getBundleName());
		bundlePrivilege.setUsername(po.getUsername());
		return bundlePrivilege;
	}

	private UserresPrivilegeBO getUserresPrivilegeFromUserBO(UserBO userBO) {
		UserresPrivilegeBO userresPrivilege = new UserresPrivilegeBO();
		userresPrivilege.setId(userBO.getId());
		userresPrivilege.setName(userBO.getName());
		userresPrivilege.setUserNo(userBO.getUserNo());
		return userresPrivilege;
	}
}