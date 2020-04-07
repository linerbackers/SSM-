package com.rupeng.mapper;

import java.util.HashMap;

import com.rupeng.pojo.AdminUser;
import com.rupeng.pojo.AdminUserRole;
import com.rupeng.pojo.Role;

public interface AdminUserRolesMapper extends IManyToManyMapper<AdminUser, AdminUserRole,Role> {
    public int checkPermission(HashMap<String, Object> map);
}