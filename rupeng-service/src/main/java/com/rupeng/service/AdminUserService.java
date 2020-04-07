package com.rupeng.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rupeng.mapper.AdminUsersMapper;
import com.rupeng.pojo.AdminUser;

@Service
public class AdminUserService  extends BaseService<AdminUser>{

	@Autowired
	private AdminUsersMapper adminUsersMapper;
	
	public int count(AdminUser adminUser){
		int count = adminUsersMapper.count(adminUser);
		return count;
		
	}
}
