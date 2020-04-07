package com.rupeng.pojo;

public class AdminUserRole {
    private Long id;

    private Long adminUserId;

    private Long roleId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAdminuserid() {
        return adminUserId;
    }

    public void setAdminuserid(Long adminuserid) {
        this.adminUserId = adminuserid;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleid) {
        this.roleId = roleid;
    }
}