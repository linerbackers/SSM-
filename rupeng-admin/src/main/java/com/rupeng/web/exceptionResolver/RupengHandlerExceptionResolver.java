package com.rupeng.web.exceptionResolver;


import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.rupeng.util.AjaxResult;
import com.rupeng.util.JsonUtils;
//注解在spring中注册该bean
@Component
public class RupengHandlerExceptionResolver implements HandlerExceptionResolver{

	private final static Logger log=LogManager.getLogger(RupengHandlerExceptionResolver.class);

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		
		if(request.getHeader("X-Requested-With")!=null){
			//是ajax请求
			try {
				ex.printStackTrace();
				AjaxResult ajaxResult = AjaxResult.errorInstance(ex.getStackTrace());
				response.getWriter().println(JsonUtils.toJson(ajaxResult));
			} catch (IOException e) {
				e.printStackTrace();
				log.error(e);
			}

			//此处返回空参数的modelandview，异常处理器就认为我们自己已经打印异常
			return new ModelAndView();
		}else{
			//普通请求
			return new ModelAndView("500");
		}
	}

}
