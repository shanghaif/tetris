package com.sumavision.bvc.control.device.command.group.system;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.device.command.system.AllForwardBO;
import com.sumavision.bvc.device.command.system.CommandSystemServiceImpl;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/command/system")
public class CommandSystemController {

	@Autowired
	private CommandSystemServiceImpl commandSystemServiceImpl;

	@Autowired
	private UserUtils userUtils;
	
	/**
	 * 查询指挥系统内的所有业务转发<br/>
	 * <p>包括指挥、指挥转发、点播、通话</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月15日 下午1:57:43
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/all/forward")
	public Object findCommandForwardDemand(
//			int currentPage,
//			int pageSize,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		List<AllForwardBO> allForwards = commandSystemServiceImpl.obtainAllForwards(userId);
		
//		Pageable page = new PageRequest(currentPage-1, pageSize);
		
		return JSON.toJSONString(new HashMapWrapper<String, Object>()
				.put("total", allForwards.size())
				.put("rows", allForwards)
				.getMap());
	}
	
}
