package com.rupeng.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rupeng.pojo.Permission;
import com.rupeng.pojo.RolePermission;
import com.rupeng.service.PermissionsService;
import com.rupeng.service.RolePermissionsService;
import com.rupeng.util.AjaxResult;

@Controller
@RequestMapping("/page/rolePermission")
public class RolePermissionController {
	
	@Autowired
	private RolePermissionsService rolePermissionService;
	@Autowired
	private PermissionsService permissionsService;
	@RequestMapping("update.do")
	public String updatedo(@RequestParam("roleId")Long id, Model model){
		List<Permission> selectp = permissionsService.selectList();
		RolePermission rp=new RolePermission();
		rp.setRoleId(id);
		List<RolePermission> selectList = rolePermissionService.selectList(rp);
		model.addAttribute("rolePermissionList",selectList);
		model.addAttribute("permissionList", selectp);
		model.addAttribute("roleId", id);
		return "rolePermission/update";
	}
	
	@RequestMapping("update")
	@ResponseBody
	public AjaxResult update(Long[] permissionIds,Long roleId){
		int deleteByFirstId = rolePermissionService.deleteByFirstId(roleId);
		if(deleteByFirstId>0){
			for(int i=0;i<permissionIds.length;i++){
				RolePermission rp=new RolePermission();
				rp.setRoleId(roleId);
				rp.setPermissionId(permissionIds[i]);
				rolePermissionService.insert(rp);
			}
		}
		return AjaxResult.successInstance("分配权限成功！");
	}

}
