package com.rupeng.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rupeng.pojo.AdminUserRole;
import com.rupeng.pojo.Role;
import com.rupeng.service.AdminUserRoleService;
import com.rupeng.service.RolesService;
import com.rupeng.util.AjaxResult;

@Controller
@RequestMapping("/page/adminUserRole")
public class AdminUserRoleCotroller {

	@Autowired
	private AdminUserRoleService adminUserRoleService;
	@Autowired
	private RolesService rolesService;
	@RequestMapping("update.do")
	public String updatedo(Long id,Model model){
		List<Role> selectList = rolesService.selectList();
		AdminUserRole aur=new AdminUserRole();
		aur.setAdminuserid(id);
		List<AdminUserRole> selectList2 = adminUserRoleService.selectList(aur);
		model.addAttribute("adminUserRoleList", selectList2);
		model.addAttribute("roleList", selectList);
		model.addAttribute("id", id);
		return "adminUserRole/update";
	}
	
	@RequestMapping("update")
	@ResponseBody
	public AjaxResult update(Long[] roleIds,Long adminUserId){
		adminUserRoleService.deleteByFirstId(adminUserId);
		for(Long id:roleIds){
			AdminUserRole adminUserRole=new AdminUserRole();
			adminUserRole.setAdminuserid(adminUserId);
			adminUserRole.setRoleId(id);
			adminUserRoleService.insert(adminUserRole);
		}
		return AjaxResult.successInstance("角色分配成功！");
	}
}
