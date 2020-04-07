package com.rupeng.web.interceptor;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.rupeng.pojo.AdminUser;
import com.rupeng.service.AdminUserRoleService;
import com.rupeng.util.CommonUtils;
import com.rupeng.util.JsonUtils;

public class PermissionInterceptor extends HandlerInterceptorAdapter{

	@Autowired
	private AdminUserRoleService adminUserRoleService;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//1、检查用户是否登录
		AdminUser adminUser = (AdminUser) request.getSession().getAttribute("adminUser");
		if(adminUser==null){
			//判断是哪种请求
			if(CommonUtils.isEmpty(request.getHeader("X-Request-With"))){
				//ajax请求
				response.getWriter().print(JsonUtils.toJson("您还没有登录，请先登录！"));
			}else{
				//普通请求
				response.getWriter().print("您还没有登录，请先登录！");
			}
			return false;
		}
		//2、检查权限
		//请求路径  为了开发方便，把拦截权限的功能关闭
		String servletPath = request.getServletPath();
		HashMap<String, Object> map=new HashMap<>();
		map.put("adminUserId", adminUser.getId());
		map.put("path",servletPath);
		boolean checkPermission = adminUserRoleService.checkPermission(map);
		if(!checkPermission){
			//判断是哪种请求
			if(CommonUtils.isEmpty(request.getHeader("X-Request-With"))){
				//ajax请求
				response.getWriter().print(JsonUtils.toJson("您还没有权限！"));
			}else{
				//普通请求
				response.getWriter().print("您还没有权限！");
			}
			return false;
		}
		return true;
	}
}
