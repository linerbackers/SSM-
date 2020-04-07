package com.rupeng.util;

import javax.servlet.http.HttpServletRequest;

public class GetUserInfo {

	public static Object getUserAttribute(HttpServletRequest request){
		return request.getSession().getAttribute("frontUser");
	}
}
