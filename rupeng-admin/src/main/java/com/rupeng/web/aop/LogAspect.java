package com.rupeng.web.aop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.rupeng.pojo.AdminUser;
import com.rupeng.util.JsonUtils;


//定义切面
@Aspect
public class LogAspect {
	private final static Logger log=LogManager.getLogger(LogAspect.class);
	
	//切点定义在标有requestMapping注解的
	@Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
	public void controller(){
	}
	
	//定义通知
	@Before("controller()")
	public void log(JoinPoint joinPoint){//joinPoint 能获取被增强的方法名和方法参数
		if(!log.isDebugEnabled()){
			return;
		}
		
		//获取request
	   HttpServletRequest request=((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
	  //获取登录用户
	   AdminUser adminUser = (AdminUser) request.getSession().getAttribute("adminUser");
	   Long userId=null;
	   if(adminUser!=null){
		  userId = adminUser.getId();
	   }
	   
	   //获取参数
	   Object[] args = joinPoint.getArgs();
	   for(int i=0;i<args.length;i++){
		   if(args[i] instanceof HttpServletRequest){
			   args[i]=args[i]+"是request对象";
		   }
		   if(args[i] instanceof HttpServletResponse){
			   args[i]=args[i]+"是response对象";
		   }
		    if (args[i] instanceof MultipartFile) {
               args[i] = args[i]+"MultipartFilet对象";
           } 
		    if (args[i] instanceof BindingResult) {
               args[i] = args[i]+"BindingResult对象";
           }
	   }
	   
	   log.info("用户id：{}，方法签名：{}，方法的参数列表：{}", userId, joinPoint.getSignature(), JsonUtils.toJson(args));
	}
	

}
