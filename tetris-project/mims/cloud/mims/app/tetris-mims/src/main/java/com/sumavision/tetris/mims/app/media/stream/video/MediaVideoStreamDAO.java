package com.sumavision.tetris.mims.app.media.stream.video;

import java.util.Collection;
import java.util.List;
import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.tetris.mims.app.media.UploadStatus;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = MediaVideoStreamPO.class, idClass = Long.class)
public interface MediaVideoStreamDAO extends BaseDAO<MediaVideoStreamPO>{

	/**
	 * 获取文件夹下特定上传状态的视频流媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月28日 上午10:33:08
	 * @param Long folderId 文件夹id
	 * @param UploadStatus uploadStatus 
	 * @return List<MediaVideoStreamPO> 视频流媒资
	 */
	public List<MediaVideoStreamPO> findByFolderIdAndUploadStatusOrderByName(Long folderId, UploadStatus uploadStatus);
	
	/**
	 * 获取文件夹下特定上传状态的视频流媒资（批量）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月29日 下午3:33:50
	 * @param Collection<Long> folderIds 文件夹id列表
	 * @param UploadStatus status 上传状态
	 * @return List<MediaVideoStreamPO> 媒资视频流列表
	 */
	public List<MediaVideoStreamPO> findByFolderIdInAndUploadStatus(Collection<Long> folderIds, UploadStatus status);
	
	/**
	 * 获取文件夹下的视频流媒资（批量）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月28日 下午3:21:18
	 * @param Long folderId 文件夹id
	 * @return List<MediaVideoStreamPO> 视频流媒资列表
	 */
	public List<MediaVideoStreamPO> findByFolderIdIn(Collection<Long> folderId);
	
}
