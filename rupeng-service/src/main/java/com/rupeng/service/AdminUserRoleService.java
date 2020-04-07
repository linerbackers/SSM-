package com.rupeng.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rupeng.mapper.AdminUserRolesMapper;
import com.rupeng.pojo.AdminUser;
import com.rupeng.pojo.AdminUserRole;
import com.rupeng.pojo.Role;

@Service
public class AdminUserRoleService extends ManyToManyBaseService<AdminUser,AdminUserRole,Role>{
	@Autowired
	private AdminUserRolesMapper adminUserRolesMapper;
	public boolean checkPermission(HashMap<String, Object> map){
			int checkPermission = adminUserRolesMapper.checkPermission(map);
			return checkPermission>0;
	}
}
