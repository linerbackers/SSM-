package com.rupeng.mapper;

import com.rupeng.pojo.Permission;
import com.rupeng.pojo.Role;
import com.rupeng.pojo.RolePermission;

public interface RolePermissionsMapper extends IManyToManyMapper<Role, RolePermission, Permission> {
    
}