package com.rupeng.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class testController {
	
	@RequestMapping("test")
	public String test(){
		int i=1/0;
		return "test";
	}
}
