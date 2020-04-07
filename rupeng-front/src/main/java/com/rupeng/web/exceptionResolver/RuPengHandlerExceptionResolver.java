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

@Component
public class RuPengHandlerExceptionResolver implements HandlerExceptionResolver{

	private static final Logger logger=LogManager.getLogger(RuPengHandlerExceptionResolver.class);
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		
		if(request.getHeader("X-Requested-With")!=null){
			//是ajax请求
			AjaxResult result=new AjaxResult("服务器出错了!");
			try {
				logger.error(ex);
				response.getWriter().println(JsonUtils.toJson(result));
			} catch (IOException e) {
				e.printStackTrace();
			}
			//此处返回空参数的modelandview，异常处理器就认为我们自己已经打印异常
			return new ModelAndView();
		}else{
			//普通请求
			logger.error(ex);
			return new ModelAndView("message","message","服务器出错了");
		}
	}

}
