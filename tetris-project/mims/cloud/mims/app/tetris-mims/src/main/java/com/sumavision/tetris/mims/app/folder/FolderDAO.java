package com.sumavision.tetris.mims.app.folder;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = FolderPO.class, idClass = Long.class)
public interface FolderDAO extends BaseDAO<FolderPO>{

	/**
	 * 获取用户素材库根文件夹<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月22日 上午11:42:50
	 * @param String userId 用户id
	 * @return FolderPO 素材库根文件夹
	 */
	@Query(value = "SELECT folder.id, folder.uuid, folder.update_time, folder.name, folder.parent_id, folder.parent_path, folder.type, folder.depth, folder.author_id, folder.author_name FROM mims_folder folder LEFT JOIN mims_folder_user_permission permission ON folder.id=permission.folder_id WHERE folder.parent_id IS NULL AND folder.type='PERSONAL' AND permission.user_id=?1", nativeQuery = true)
	public FolderPO findMaterialRootByUserId(String userId);
	
	/**
	 * 查询素材库文件夹树（完整库）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月25日 上午11:34:51
	 * @param Long userId 用户id
	 * @return List<FolderPO> 文件夹列表
	 */
	@Query(value = "SELECT folder.id, folder.uuid, folder.update_time, folder.name, folder.parent_id, folder.parent_path, folder.type, folder.depth, folder.author_id, folder.author_name FROM mims_folder folder LEFT JOIN mims_folder_user_permission permission ON folder.id=permission.folder_id WHERE permission.user_id=?1", nativeQuery = true)
	public List<FolderPO> findMaterialTreeByUserId(String userId);
	
	/**
	 * 查询素材库文件夹树（完整库带层级）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月25日 上午11:34:51
	 * @param Long userId 用户id
	 * @param Integer depth 深度
	 * @return List<FolderPO> 文件夹列表
	 */
	@Query(value = "SELECT folder.id, folder.uuid, folder.update_time, folder.name, folder.parent_id, folder.parent_path, folder.type, folder.depth, folder.author_id, folder.author_name FROM mims_folder folder LEFT JOIN mims_folder_user_permission permission ON folder.id=permission.folder_id WHERE permission.user_id=?1 AND folder.depth<=?4", nativeQuery = true)
	public List<FolderPO> findMaterialTreeByUserIdWithDepth(String userId, Integer depth);
	
	/**
	 * 查询素材库文件夹树（带例外）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月25日 上午11:34:51
	 * @param Long userId 用户id
	 * @param Long except 例外文件夹id
	 * @param String exceptReg 例外文件夹的子文件夹匹配规则
	 * @return List<FolderPO> 文件夹列表
	 */
	@Query(value = "SELECT folder.id, folder.uuid, folder.update_time, folder.name, folder.parent_id, folder.parent_path, folder.type, folder.depth, folder.author_id, folder.author_name FROM mims_folder folder LEFT JOIN mims_folder_user_permission permission ON folder.id=permission.folder_id WHERE permission.user_id=?1 AND folder.id<>?2 AND (folder.parent_path IS NULL OR folder.parent_path NOT LIKE ?3)", nativeQuery = true)
	public List<FolderPO> findMaterialTreeByUserIdWithExcept(String userId, Long except, String exceptReg);
	
	/**
	 * 查询素材库文件夹树（带例外，带深度）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月25日 上午11:31:45
	 * @param Long userId 用户id
	 * @param Long except 例外文件夹id
	 * @param String exceptReg 例外文件夹的子文件夹匹配规则
	 * @param Integer depth 查询深度
	 * @return List<FolderPO> 文件夹列表
	 */
	@Query(value = "SELECT folder.id, folder.uuid, folder.update_time, folder.name, folder.parent_id, folder.parent_path, folder.type, folder.depth, folder.author_id, folder.author_name FROM mims_folder folder LEFT JOIN mims_folder_user_permission permission ON folder.id=permission.folder_id WHERE permission.user_id=?1 AND folder.id<>?2 AND (folder.parent_path IS NULL OR folder.parent_path NOT LIKE ?3) AND folder.depth<=?4", nativeQuery = true)
	public List<FolderPO> findMaterialTreeByUserIdWithExceptAndDepth(String userId, Long except, String exceptReg, Integer depth);
	
	/**
	 * 获取文件夹下所有的子文件夹<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月22日 上午11:44:09
	 * @param Long parentId 父文件夹
	 * @return List<FolderPO> 子文件夹列表
	 */
	public List<FolderPO> findByParentIdOrderByNameAsc(Long parentId);
	
	/**
	 * 获取文件夹下的所有子文件夹<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月23日 下午3:17:16
	 * @param String parentPathReg 文件夹查询规则
	 * @return List<FolderPO> 子文件夹列表
	 */
	@Query(value = "SELECT folder.id, folder.uuid, folder.update_time, folder.name, folder.parent_id, folder.parent_path, folder.type, folder.depth, folder.author_id, folder.author_name FROM mims_folder folder WHERE folder.parent_path LIKE ?1", nativeQuery = true)
	public List<FolderPO> findSubFolders(String parentPathReg);
	
	/**
	 * 获取文件夹下的所有子文件夹（带例外文件夹）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月25日 上午11:46:55
	 * @param String parentPathReg 文件夹查询规则
	 * @param Long except 例外文件夹id
	 * @param String exceptReg 例外文件夹子文件夹规则
	 * @return List<FolderPO> 子文件夹
	 */
	@Query(value = "SELECT folder.id, folder.uuid, folder.update_time, folder.name, folder.parent_id, folder.parent_path, folder.type, folder.depth, folder.author_id, folder.author_name  FROM mims_folder folder WHERE folder.parent_path LIKE ?1 AND folder.id<>?2 AND (folder.parent_path IS NULL OR folder.parent_path NOT LIKE ?3)", nativeQuery = true)
	public List<FolderPO> findSubFoldersWithExcept(String parentPathReg, Long except, String exceptReg);
	
	/**
	 * 获取企业文件夹树<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月8日 下午3:01:44
	 * @param String groupId 群组id
	 * @return List<FolderPO> 文件夹树
	 */
	@Query(value = "SELECT folder.id, folder.uuid, folder.update_time, folder.name, folder.parent_id, folder.parent_path, folder.type, folder.depth, folder.author_id, folder.author_name  FROM mims_folder folder LEFT JOIN mims_folder_group_permission permission ON folder.id=permission.folder_id WHERE  permission.group_id=?1", nativeQuery = true)
	public List<FolderPO> findCompanyTreeByGroupId(String groupId);
	
	/**
	 * 获取用户有权限的企业根文件夹（区分文件夹类型）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月12日 下午1:20:22
	 * @param String userId 用户id
	 * @param FolderType type 文件夹类型
	 * @return List<FolderPO> 根文件夹列表
	 */
	@Query(value = "SELECT folder.id, folder.uuid, folder.update_time, folder.name, folder.parent_id, folder.parent_path, folder.type, folder.depth, folder.author_id, folder.author_name "+
			       "FROM mims_folder folder "+
				   "LEFT JOIN mims_folder_role_permission permission0 ON folder.id=permission0.folder_id "+
				   "LEFT JOIN mims_role role ON permission0.role_id=role.id "+
				   "LEFT JOIN mims_role_user_permission permission1 ON role.id=permission1.role_id "+
				   "WHERE permission1.user_id=?1 "+
				   "AND folder.type=?2 "+
				   "AND folder.parent_id NOT IN( "+
					   "SELECT folder.id FROM mims_folder folder "+
					   "LEFT JOIN mims_folder_role_permission permission0 ON folder.id=permission0.folder_id "+
					   "LEFT JOIN mims_role role ON permission0.role_id=role.id "+
					   "LEFT JOIN mims_role_user_permission permission1 ON role.id=permission1.role_id "+
					   "WHERE permission1.user_id=?1 "+
					   "AND folder.type=?2 "+ 
				   ")", nativeQuery = true)
	public List<FolderPO> findPermissionCompanyRootFolder(String userId, FolderType type);
	
	/**
	 * 获取企业文件夹下的有权限的子文件夹<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月12日 下午2:05:59
	 * @param String userId 用户id
	 * @param Long parentId 父文件夹
	 * @return List<FolderPO> 文件夹列表
	 */
	@Query(value = "SELECT folder.id, folder.uuid, folder.update_time, folder.name, folder.parent_id, folder.parent_path, folder.type, folder.depth, folder.author_id, folder.author_name "+ 
				   "FROM mims_folder folder "+
				   "LEFT JOIN mims_folder_role_permission permission0 ON folder.id=permission0.folder_id "+
				   "LEFT JOIN mims_role role ON permission0.role_id=role.id "+
				   "LEFT JOIN mims_role_user_permission permission1 ON role.id=permission1.role_id "+
				   "WHERE permission1.user_id=?1 "+
				   "AND folder.parent_id=?2 "+
				   "AND folder.type=?3", nativeQuery = true)
	public List<FolderPO> findPermissionCompanyFoldersByParentId(String userId, Long parentId, FolderType type);
	
	/**
	 * 方法概述<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月12日 下午4:01:05
	 * @param groupId
	 * @param type
	 * @return
	 */
	@Query(value = "SELECT folder.id, folder.uuid, folder.update_time, folder.name, folder.parent_id, folder.parent_path, folder.type, folder.depth, folder.author_id, folder.author_name "+
				   "FROM mims_folder folder "+
				   "LEFT JOIN mims_folder_group_permission permission0 ON folder.id=permission0.folder_id "+ 
				   "WHERE permission0.group_id=?1 "+
				   "AND folder.type=?2 "+
				   "AND folder.parent_id=( "+
					   "SELECT folder.id FROM mims_folder folder "+
					   "LEFT JOIN mims_folder_group_permission permission0 ON folder.id=permission0.folder_id "+ 
					   "WHERE permission0.group_id=?1 AND folder.type='COMPANY' "+
				   ")", nativeQuery = true)
	public FolderPO findCompanyRootFolderByType(String groupId, FolderType type);
}