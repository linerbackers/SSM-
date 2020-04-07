package com.rupeng.web.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;

import com.rupeng.pojo.AdminUser;
import com.rupeng.service.AdminUserService;
import com.rupeng.web.redis.JedisClient;

public class KeepOnlineInterceptor extends HandlerInterceptorAdapter{

	@Autowired
	private JedisClient jedisClient;
	@Autowired
	private AdminUserService adminUserService;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//先判断当前session中有没有adminUser对象
		AdminUser adminUser = (AdminUser) request.getSession().getAttribute("adminUser");
	
		//如果没有adminUser , 从cookie中取出原来的sessionId
		if(adminUser==null){
			Cookie sessionCookie = WebUtils.getCookie(request, "JSESSIONID");
			if(sessionCookie==null){
				return true;
			}
			
			String oldSessionId = sessionCookie.getValue();
			
			//从redis服务器中拿到对应的userId
			String userId = jedisClient.get("keepOnline_"+oldSessionId);
			if(userId==null){
				return true;
			}
			
			//从数据库中根据userid把adminUser查询出来，设置到session里面
			adminUser = adminUserService.selectOne(Long.parseLong(userId));
			if(adminUser==null){
				return true;
			}
			
			request.getSession().setAttribute("adminUser", adminUser);
		}
		
		//把当前sessionid和userid存放到redis服务器
		if(adminUser!=null){
			jedisClient.setex("keepOnline_"+request.getSession().getId(),24*24*60,adminUser.getId()+"");
		}
		
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
	}

}
