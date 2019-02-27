package com.sumavision.tetris.mims.app.media.picture;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.alibaba.fastjson.JSONObject;

@FeignClient(name = "tetris-mims")
public interface MediaPictureFeign {

	/**
	 * 查询文件夹下的文件夹以及图片媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月26日 下午5:46:46
	 * @param Long folderId 当前文件夹
	 * @return JSONObject 图片媒资列表以及面包屑
	 */
	@RequestMapping(value = "/media/picture/feign/load")
	public JSONObject load(@RequestParam("folderId") Long folderId) throws Exception;
	
}