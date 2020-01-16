package com.sumavision.bvc.control.welcome;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.feign.UserQueryFeign;
import com.suma.venus.resource.service.UserQueryService;
//import com.suma.venus.resource.feign.UserFeign;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "")
public class IndexController {

//	@Autowired
//	private UserFeign userFeign;
	
	@Autowired
	private UserQueryFeign userQueryFeign;
	
	@Autowired
	private UserQueryService userQueryService;
	
	@Autowired
	private UserUtils userUtils;
	
	@RequestMapping(value = "/index")
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		ModelAndView mv = new ModelAndView("web/bvc/index");
		
		return mv;
	}
	
	@ResponseBody
	@JsonBody
	@RequestMapping("/prepare/app")
	public Object prepareApp(HttpServletRequest request) throws Exception{
		
		//这段代码待测
		//Object userName = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		UserBO userBO = userQueryService.current();
		
		//包装一下
		UserVO user = new UserVO().set(userBO);
		
		//缓存用户信息
		userUtils.setUserToSession(request, user);
		
		return user;
	}
	
	/**
	 * 心跳检测<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月29日 下午3:46:47
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/heart/beat")
	public Object heartBeat(HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		userQueryFeign.userHeartbeat(user.getName());
		
		return null;
	}
	
}
