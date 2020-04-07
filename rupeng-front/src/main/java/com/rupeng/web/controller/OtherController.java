package com.rupeng.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rupeng.pojo.User;
import com.rupeng.service.SettingService;
import com.rupeng.service.UsersService;
import com.rupeng.util.AjaxResult;
import com.rupeng.util.CommonUtils;
import com.rupeng.util.EmailUtils;
import com.rupeng.util.GetUserInfo;
import com.rupeng.util.ImageCodeUtils;
import com.rupeng.util.JedisUtils;
import com.rupeng.util.JsonUtils;
import com.rupeng.util.SMSUtils;

@Controller
public class OtherController {

	@Autowired
	private SettingService settingService;
	@Autowired
	private UsersService userService;
	@RequestMapping("index")
	public String index(HttpServletRequest request,Model model){
		User frontUser = (User) request.getSession().getAttribute("frontUser");
		if(frontUser!=null){
			User selectOne = userService.selectOne(frontUser.getId());
			model.addAttribute("userName", selectOne.getName());
		}
		return "index";
		}
	
	@RequestMapping("emailCode.do")
	@ResponseBody
	public AjaxResult emailCodedo(String email,HttpServletRequest request,HttpServletResponse response){
		//判断邮箱是否为空
		if(CommonUtils.isEmpty(email)){
			return AjaxResult.errorInstance("邮箱不能为空");
		}
		
		//判断邮箱格式是否正确
		if(!CommonUtils.isEmail(email)){
			return AjaxResult.errorInstance("邮箱格式不正确");
		}
		
		// 发件人邮箱的 SMTP 服务器地址, 必须准确, 不同邮件服务器地址不同, 一般(只是一般, 绝非绝对)格式为: smtp.xxx.com
		// 网易163邮箱的 SMTP 服务器地址为: smtp.163.com    腾讯: smtp.qq.com 新浪：smtp.sina.com
		String smtpServer = settingService.selectOneByName("email_smtp_host");//腾讯邮箱服务器
		
		// 发件人的 邮箱 和 密码（替换为自己的邮箱和密码）
	    // PS: 某些邮箱服务器为了增加邮箱本身密码的安全性，给 SMTP 客户端设置了独立密码（有的邮箱称为“授权码”）, 
	    // 对于开启了独立密码的邮箱, 这里的邮箱密码必需使用这个独立密码（授权码）。
		String username = settingService.selectOneByName("email_username");//发件人的邮箱
		String password = settingService.selectOneByName("email_password");//发件人的密码
		String from = settingService.selectOneByName("email_from");//谁发送的
		//发送邮箱获取验证码
		EmailUtils.sendEmailCode(request.getSession(), smtpServer, username, password, from, email);
		return AjaxResult.successInstance("发送成功");
	}
	
	//用户登录验证码
	@RequestMapping("imageCode.do")
	public void imageCodedo(HttpServletRequest request,HttpServletResponse response){
		ImageCodeUtils.sendImageCode(request.getSession(), response);
	}
	
	//短信验证码
	@RequestMapping("smsCode.do")
	public void smsCodedo(String phone,HttpServletRequest request,HttpServletResponse response){
		String url = settingService.selectOneByName("sms_url");
		String username = settingService.selectOneByName("sms_username");
		String appKey = settingService.selectOneByName("sms_app_key");
		String template = settingService.selectOneByName("sms_code_template");
		SMSUtils.sendSMSCode(request.getSession(), url, username, appKey, template, phone);
	}
	
	@RequestMapping("/notification")
	@ResponseBody
	public AjaxResult notification(HttpServletRequest request){
		User user = (User) GetUserInfo.getUserAttribute(request);
		if(user==null){
			return AjaxResult.errorInstance("还没有登录");
		}
		
		//获得当前登录用户所有的通知消息，并且把通知消息从redis里面删除掉
		Set<String> datas = JedisUtils.smembersAndDel("notification_"+user.getId());
		if(CommonUtils.isEmpty(datas)){
			return AjaxResult.successInstance(null);
		}
		
		List<Object> list=new ArrayList<>();
		for(String string:datas){
			Object bean = JsonUtils.toBean(string, Object.class);
			list.add(bean);
		}
		
		return AjaxResult.successInstance(list);
	}
	
}
