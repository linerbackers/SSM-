package com.rupeng.mapper;

import com.rupeng.pojo.AdminUser;

public interface AdminUsersMapper extends IMapper<AdminUser>{
	
	public int count(AdminUser adminUser);
}