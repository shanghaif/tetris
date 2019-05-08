package com.sumavision.tetris.cs.menu;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.mims.app.media.video.MediaVideoQuery;
import com.sumavision.tetris.mims.app.media.video.MediaVideoVO;

@Component
public class CsResourceQuery {
	@Autowired
	CsResourceDAO csSourceDao;

	@Autowired
	MediaVideoQuery mediaVideoQuery;

	public List<CsResourceVO> queryMenuResources(Long menuId) throws Exception {
		List<CsResourcePO> resources = csSourceDao.findByParentId(menuId);

		List<CsResourceVO> menuResources = new ArrayList<CsResourceVO>();
		if (resources != null && resources.size() > 0) {
			for (CsResourcePO item:resources) {
				menuResources.add(new CsResourceVO().set(item));
			}
		}
		return menuResources;
	}

	public CsResourceVO queryResourceById(Long resourceId) throws Exception {
		CsResourcePO resource = csSourceDao.findOne(resourceId);

		return new CsResourceVO().set(resource);
	}

	public List<CsResourceVO> getResourcesFromChannelId(Long channelId) throws Exception {
		List<CsResourcePO> resourcePOList = csSourceDao.findResourceByChannelId(channelId);
		List<CsResourceVO> returnList = new ArrayList<CsResourceVO>();
		if (resourcePOList != null && resourcePOList.size() > 0) {
			for (CsResourcePO item : resourcePOList) {
				returnList.add(new CsResourceVO().set(item));
			}
		}
		return returnList;
	}

	public List<MediaVideoVO> getMIMSResources(Long id) throws Exception {

		List<MediaVideoVO> mimsVideoList = mediaVideoQuery.loadAll();

		return mimsVideoList;
	}
}