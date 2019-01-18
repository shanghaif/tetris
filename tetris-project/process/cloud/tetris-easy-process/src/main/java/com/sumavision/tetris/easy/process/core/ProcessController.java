package com.sumavision.tetris.easy.process.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.easy.process.access.point.AccessPointDAO;
import com.sumavision.tetris.easy.process.access.point.AccessPointPO;
import com.sumavision.tetris.easy.process.access.point.AccessPointScope;
import com.sumavision.tetris.easy.process.access.point.exception.AccessPointNotExistException;
import com.sumavision.tetris.easy.process.access.service.ServiceType;
import com.sumavision.tetris.easy.process.access.service.rest.RestServiceDAO;
import com.sumavision.tetris.easy.process.access.service.rest.RestServicePO;
import com.sumavision.tetris.easy.process.core.exception.ProcessIdAlreadyExistException;
import com.sumavision.tetris.easy.process.core.exception.ProcessNotExistException;
import com.sumavision.tetris.easy.process.core.exception.UserHasNoPermissionForProcessQueryException;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserClassify;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/process")
public class ProcessController {

	@Autowired
	private UserQuery userTool;
	
	@Autowired
	private ProcessDAO processDao;
	
	@Autowired
	private ProcessQuery processTool;
	
	@Autowired
	private ProcessService processService;
	
	@Autowired
	private RestServiceDAO restServiceDao;
	
	@Autowired
	private AccessPointDAO accessPointDao;
	
	/**
	 * 分页查询流程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月24日 下午5:31:49
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return long total 总数据量
	 * @return List<ProcessVO> 流程列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list")
	public Object list(
			int currentPage,
			int pageSize,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userTool.current();
		
		if(!UserClassify.MAINTENANCE.equals(UserClassify.valueOf(user.getClassify()))){
			throw new UserHasNoPermissionForProcessQueryException(user.getUuid(), "基础流程分页查询");
		}
		
		long total = processDao.count();
		
		List<ProcessPO> entities = processTool.findAll(currentPage, pageSize);
		
		List<ProcessVO> rows = ProcessVO.getConverter(ProcessVO.class).convert(entities, ProcessVO.class);
		
		Map<String, Object> result = new HashMapWrapper<String, Object>().put("total", total)
																		 .put("rows", rows)
																		 .getMap();
		
		return result;
	}
	
	/**
	 * 添加一个流程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月24日 下午5:36:40
	 * @param String processId 用户自定义流程id
	 * @param String name 流程名称
	 * @param String remarks 流程说明
	 * @return ProcessVO 流程数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			String type,
			String processId,
			String name,
			String remarks,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userTool.current();
		
		if(!UserClassify.MAINTENANCE.equals(UserClassify.valueOf(user.getClassify()))){
			throw new UserHasNoPermissionForProcessQueryException(user.getUuid(), "添加基础流程");
		}
		
		ProcessPO process = processDao.findByProcessId(processId);

		if(process != null){
			throw new ProcessIdAlreadyExistException(processId);
		}
		
		Date updateTime = new Date();
		
		process = new ProcessPO();
		process.setType(ProcessType.fromName(type));
		process.setProcessId(processId);
		process.setName(name);
		process.setRemarks(remarks);
		process.setPath(new StringBufferWrapper().append("tmp")
												 .append(File.separator)
												 .append("processes")
												 .append(File.separator)
												 .append(user.getUuid())
												 .append("-")
												 .append(processId)
												 .append("-")
												 .append(updateTime.getTime())
												 .append(".bpmn")
												 .toString());
		process.setUpdateTime(updateTime);
		processDao.save(process);
		
		return new ProcessVO().set(process);
	}
	
	/**
	 * 修改流程数据<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月24日 下午5:49:56
	 * @param @PathVariable Long id 流程id
	 * @param String processId 自定义流程id
	 * @param String name 流程名称
	 * @param String remarks 流程备注
	 * @return ProcessVO 流程数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit/{id}")
	public Object edit(
			@PathVariable Long id,
			String name,
			String remarks,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userTool.current();
		
		if(!UserClassify.MAINTENANCE.equals(UserClassify.valueOf(user.getClassify()))){
			throw new UserHasNoPermissionForProcessQueryException(user.getUuid(), "修改基础流程");
		}
		
		ProcessPO process = processDao.findOne(id);
		
		if(process == null){
			throw new ProcessNotExistException(id);
		}
		
		process.setName(name);
		process.setRemarks(remarks);
		processDao.save(process);
		
		return new ProcessVO().set(process);
	}
	
	/**
	 * 删除流程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月25日 上午8:44:26
	 * @param @PathVariable Long id 流程id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete/{id}")
	public Object delete(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userTool.current();
		
		if(!UserClassify.MAINTENANCE.equals(UserClassify.valueOf(user.getClassify()))){
			throw new UserHasNoPermissionForProcessQueryException(user.getUuid(), "删除基础流程");
		}
		
		ProcessPO process = processDao.findOne(id);
		
		if(process != null){
			processService.delete(process);
		}
		
		return null;
	}
	
	/**
	 * 查询流程类型<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月26日 上午9:52:43
	 * @return Set<String> 类型名称列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/types")
	public Object queryTypes(HttpServletRequest request) throws Exception{
		
		UserVO user = userTool.current();
		
		if(!UserClassify.MAINTENANCE.equals(UserClassify.valueOf(user.getClassify()))){
			throw new UserHasNoPermissionForProcessQueryException(user.getUuid(), "查询流程类型");
		}
		
		Set<String> processTypes = new HashSet<String>();
		ProcessType[] types = ProcessType.values();
		for(ProcessType type:types){
			processTypes.add(type.getName());
		}
		
		return processTypes;
	}
	
	/**
	 * 查询流程bpmn内容和可配置的接入点列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月26日 上午10:38:52
	 * @param Long id 流程id
	 * @return String bpmn 配置文件内容
	 * @return String processId 流程主键
	 * @return String uuid 流程uuid
	 * @return List<GroupEntryVO> groupEntries 可配置接入点列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/bpmn/and/entries")
	public Object queryBpmnAndEntries(
			Long id,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userTool.current();
		
		if(!UserClassify.MAINTENANCE.equals(UserClassify.valueOf(user.getClassify()))){
			throw new UserHasNoPermissionForProcessQueryException(user.getUuid(), "查询流程bpmn内容和可配置的接入点列表");
		}
		
		ProcessPO process = processDao.findOne(id);
		
		if(process == null){
			throw new ProcessNotExistException(id);
		}
		
		List<GroupEntryVO> groupEntries = new ArrayList<GroupEntryVO>();
		
		List<AccessPointPO> accessPoints = accessPointDao.findByScope(AccessPointScope.SYSTEMSCOPE);
		if(accessPoints!=null && accessPoints.size()>0){
			Set<Long> restIds = new HashSet<Long>();
			for(AccessPointPO accessPoint:accessPoints){
				if(ServiceType.REST.equals(accessPoint.getServiceType())){
					restIds.add(accessPoint.getServiceId());
				}
			}
			List<RestServicePO> restEntities = restServiceDao.findAll(restIds);
			for(RestServicePO entry:restEntities){
				GroupEntryVO groupEntry = new GroupEntryVO().set(entry).setEntries(new ArrayList<EntryVO>());
				groupEntries.add(groupEntry);
				for(AccessPointPO accessPoint:accessPoints){
					if(ServiceType.REST.equals(accessPoint.getServiceType()) && 
							accessPoint.getServiceId().equals(entry.getId())){
						groupEntry.getEntries().add(new EntryVO().set(accessPoint, true));
					}
				}
			}
		}
		
		Map<String, Object> result = new HashMapWrapper<String, Object>().put("bpmn", process.getBpmn())
																		 .put("processId", process.getProcessId())
																		 .put("uuid", process.getUuid())
																		 .put("groupEntries", groupEntries)
																		 .getMap();
		
		return result;
	}
	
	/**
	 * 保存流程bpmn配置<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月28日 上午10:14:55
	 * @param @PathVariable Long id 流程id
	 * @param String bpmn bpmn配置
	 * @param JSONArray accessPointIds 配置的接入点id数组
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/save/bpmn/{id}")
	public Object saveBpmn(
			@PathVariable Long id, 
			String bpmn,
			String accessPointIds,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userTool.current();
		
		if(!UserClassify.MAINTENANCE.equals(UserClassify.valueOf(user.getClassify()))){
			throw new UserHasNoPermissionForProcessQueryException(user.getUuid(), "查询流程bpmn内容和可配置的接入点列表");
		}
		
		ProcessPO process = processDao.findOne(id);
		
		if(process == null){
			throw new ProcessNotExistException(id);
		}
		
		List<Long> accessPointIdList = JSON.parseArray(accessPointIds, Long.class);
		
		List<AccessPointPO> accessPoints = accessPointDao.findAll(accessPointIdList);
		
		if(accessPointIdList.size() != (accessPoints==null?0:accessPoints.size())){
			if(accessPoints!=null && accessPoints.size()>0){
				for(Long accessPointId:accessPointIdList){
					boolean finded = false;
					for(AccessPointPO accessPoint:accessPoints){
						if(accessPoint.getId().equals(accessPointId)){
							finded = true;
							break;
						}
					}
					if(!finded){
						throw new AccessPointNotExistException(accessPointId);
					}
				}
			}
		}
		
		processService.saveBpmn(process, bpmn, accessPoints);
		
		return null;
	}
	
}