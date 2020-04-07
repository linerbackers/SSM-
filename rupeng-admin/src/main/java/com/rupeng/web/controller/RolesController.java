package com.rupeng.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rupeng.pojo.Permission;
import com.rupeng.pojo.Role;
import com.rupeng.pojo.RolePermission;
import com.rupeng.service.PermissionsService;
import com.rupeng.service.RolePermissionsService;
import com.rupeng.service.RolesService;
import com.rupeng.util.AjaxResult;


//角色管理
@Controller
@RequestMapping("/page/role")
public class RolesController {
	
	@Autowired
	private RolesService rolesService;
	@Autowired
	private RolePermissionsService rolePermissionsService;
	@Autowired
	private PermissionsService permissionService;
	@RequestMapping("list")
	public String toList(Model model){
		List<Role> selectList = rolesService.selectList();
		model.addAttribute("roleList", selectList);
		return "role/list";
	}
	
	@RequestMapping("add.do")
	public String toAdd(Model model){
		List<Permission> selectList = permissionService.selectList();
		model.addAttribute("permissionList",selectList);
		return "role/add";
	}
	
	@RequestMapping("add")
	@ResponseBody
	public AjaxResult addRole(Role role,@RequestParam(value="permissionIds" ) Long[] ids){
		
		//非空判断省略
		//唯一性检查
		List<Role> selectList = rolesService.selectList(role);
		if(selectList.size()>=1){
			return AjaxResult.errorInstance("已存在该角色！");
		}
		
		int insert = rolesService.insert(role);
		Role selectOne=null;
		if(insert==1){
			selectOne= rolesService.selectOne(role);
			for(int i=0;i<ids.length;i++){
				RolePermission rp=new RolePermission();
				rp.setRoleId(selectOne.getId());
				rp.setPermissionId(ids[i]);
				rolePermissionsService.insert(rp);
			}
		}
		return AjaxResult.successInstance("添加角色成功！");
	} 
	
	@RequestMapping("delete")
	@ResponseBody
	public AjaxResult delete(Long id){
		rolesService.delete(id);
		return AjaxResult.successInstance("删除成功！");
	}
	
	@RequestMapping("update.do")
	public String updateTo(Long id,Model model){
		Role selectOne = rolesService.selectOne(id);
		model.addAttribute("role",selectOne);
		return "role/update";
	}

	@RequestMapping("update")
	@ResponseBody
	public AjaxResult update(Role role){
		//非空校验省略
		rolesService.update(role);
		return AjaxResult.successInstance("修改成功！");
	}
}
