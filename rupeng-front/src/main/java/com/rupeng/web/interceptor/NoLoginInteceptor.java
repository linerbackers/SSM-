package com.rupeng.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.rupeng.pojo.User;
import com.rupeng.service.Properties;

public class NoLoginInteceptor implements HandlerInterceptor{
	@Autowired
	private Properties properties;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		User user = (User) request.getSession().getAttribute("frontUser");
		if(user==null){
			//因为mvc子容器无法访问spring父容器的属性（.properties文件是spring扫描的）
			//所以只能访问service层获取属性,该service层只能在同一个工程下
			//跳转到登录页面，把用户请求的url(request.getRequestURL())作为参数传递给登录页面。
			response.sendRedirect(properties.FRONT_BASE_URL+properties.FRONT_USER_LOGIN+
					"?redirect=" + request.getRequestURL());//request.getRequestURL() 返回全路径，例如 http://localhost:8080/jqueryLearn/resources/request.jsp 
			return false;
		}
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
