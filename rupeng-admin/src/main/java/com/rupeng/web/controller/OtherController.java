package com.rupeng.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rupeng.pojo.AdminUser;
import com.rupeng.util.ImageCodeUtils;


@Controller
public class OtherController {

	@RequestMapping("/index")
	public String index(HttpServletRequest request){
		   AdminUser adminUser = (AdminUser) request.getSession().getAttribute("adminUser");
		   if(adminUser==null){
			   return "adminUser/login";
		   }
		return "index";
	}
	
	@RequestMapping("/welcome")
	public String welcome(){
		return "welcome";
	}
	
	@RequestMapping("imageCode.do")
	public void imageCode(HttpServletRequest request,HttpServletResponse response){
		ImageCodeUtils.sendImageCode(request.getSession(), response);
	}
}
