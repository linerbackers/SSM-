package com.rupeng.web.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.rupeng.pojo.User;
import com.rupeng.service.UsersService;
import com.rupeng.web.redis.JedisClientSingle;

public class AutoLoginInteceptor implements HandlerInterceptor{

	@Autowired
	private UsersService userService;
	@Autowired
	private JedisClientSingle jedisClientSingle;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		User frontUser =  (User) request.getSession().getAttribute("frontUser");
		if(frontUser==null){
			//从cookie中取出sessionId
			Cookie cookie = WebUtils.getCookie(request, "JSESSIONID");
			if(cookie==null){
				return true;
			}
			String oldSessionId = cookie.getValue();
			//去redis数据库中查用户信息
			String string = jedisClientSingle.get("AutoLogin_"+oldSessionId);
			if(string==null){
				return true;
			}
			//查数据库
			frontUser = userService.selectOne(Long.parseLong(string));
			if(frontUser==null){
				return true;
			}
			request.getSession().setAttribute("frontUser",frontUser);
		}
		//如果用户不为空，则重新把sessionid和用户id存放到redis数据库中
		jedisClientSingle.setex("AutoLogin_"+request.getSession().getId(), 24*24*60, frontUser.getId()+"");
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
