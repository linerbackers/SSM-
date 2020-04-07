package com.rupeng.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Properties {

	@Value("${FRONT_BASE_URL}")
	public String FRONT_BASE_URL;
	@Value("${FRONT_USER_LOGIN}")
	public String FRONT_USER_LOGIN;
}
