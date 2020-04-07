package com.rupeng.web.controller;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rupeng.pojo.AdminUser;
import com.rupeng.pojo.AdminUserRole;
import com.rupeng.pojo.Role;
import com.rupeng.service.AdminUserService;
import com.rupeng.service.RolesService;
import com.rupeng.util.AjaxResult;
import com.rupeng.util.CommonUtils;
import com.rupeng.util.ImageCodeUtils;

@Controller
@RequestMapping("/page/adminUser")
public class AdminUserController {

	@Autowired
	private AdminUserService adminUserService;
	@Autowired
	private RolesService rolesService;
	@RequestMapping("list.do")
	public String listdo(Model model){
		List<AdminUser> selectList = adminUserService.selectList();
		model.addAttribute("adminUserList", selectList);
		return "adminUser/list";
	}
	
	@RequestMapping("add.do")
	public String adddo(Model model){
		List<Role> selectList = rolesService.selectList();
		model.addAttribute("roleList", selectList);
		return "adminUser/add";
	}
	
	@RequestMapping("add")
	@ResponseBody
	public AjaxResult add(AdminUser adminUser,Long[] roleIds){
		//非空校验省略
		//唯一性检查
		if(!adminUserService.isExisted(adminUser)){
			return AjaxResult.errorInstance("已经存在该用户！");
		}
		if (!CommonUtils.isLengthEnough(adminUser.getPassword(), 6)) {
	            return AjaxResult.errorInstance("密码长度至少6位");
	    }
		String salt=UUID.randomUUID().toString();
		adminUser.setPasswordSalt(salt);
		String calculateMD5 = CommonUtils.calculateMD5(adminUser.getPassword()+salt);
		adminUser.setPassword(calculateMD5);
		adminUserService.insert(adminUser);
		AdminUser selectOne = adminUserService.selectOne(adminUser);
		for(Long id:roleIds){
			AdminUserRole aur=new AdminUserRole();
			aur.setRoleId(id);
			aur.setAdminuserid(selectOne.getId());
		}
		return AjaxResult.successInstance("管理员添加成功！");
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public AjaxResult delete(Long id){
		adminUserService.delete(id);
		return AjaxResult.successInstance("删除成功!");
	}
	
	@RequestMapping("resetPassword.do")
	public String resetPassworddo(Long id,Model model){
		AdminUser selectOne = adminUserService.selectOne(id);
		model.addAttribute("adminUser", selectOne);
		return "adminUser/resetPassword";
	}
	
	@RequestMapping("resetPassword")
	@ResponseBody
	public AjaxResult resetPassword(AdminUser adminUser){
		AdminUser selectOne = adminUserService.selectOne(adminUser.getId());
		String passwordSalt=UUID.randomUUID().toString();
		adminUser.setPasswordSalt(passwordSalt);
		adminUser.setPassword(CommonUtils.calculateMD5(adminUser.getPassword()+passwordSalt));
		adminUser.setAccount(selectOne.getAccount());
		adminUser.setIsDisabled(true);
		adminUserService.update(adminUser);
		return AjaxResult.successInstance("密码重置成功！");
	}
	
	@RequestMapping("isDesabled")
	@ResponseBody
	public AjaxResult isDesabled(Long id){
		AdminUser selectOne = adminUserService.selectOne(id);
		Boolean isDisabled = selectOne.getIsDisabled();
		selectOne.setIsDisabled(!isDisabled);
		adminUserService.update(selectOne);
		return new AjaxResult("success");
	}
	
	@RequestMapping("login.do")
	public String loginDo(){
		return "adminUser/login";
	}
	
	
	@RequestMapping("login")
	public String loginSubmit(String imageCode,String account, String password,HttpServletRequest request,Model model){
		//验证码校验
		if(!ImageCodeUtils.checkImageCode(request.getSession(), imageCode)){
			model.addAttribute("message", "验证码错误！");
			return "adminUser/login";
		}
		
		//用户账户和密码校验
		AdminUser au=new AdminUser();
		au.setAccount(account);
		int count = adminUserService.count(au);//此count值取到的是查询出的数据条数的值
		if(count<=0){
			model.addAttribute("message", "账户或密码错误");
			return "adminUser/login";
		}
		AdminUser selectOne = adminUserService.selectOne(au);
		if(!(selectOne.getPassword()).equalsIgnoreCase(CommonUtils.calculateMD5(password+selectOne.getPasswordSalt()))){
			//密码不一致
			model.addAttribute("message", "账户或密码错误");
			return "adminUser/login";
		}
		
		//该用户是否禁用
		if(selectOne.getIsDisabled()){
			model.addAttribute("message", "该用户已经禁用");
			return "adminUser/login";
		}
		
		//登陆成功
		request.getSession().setAttribute("adminUser", selectOne);
		model.addAttribute("adminUser", selectOne);
		return "redirect:/index";	
	}
	
	@RequestMapping("updatePassword.do")
	public String updatePassworddo(Long id,Model model){
		model.addAttribute("id", id);
		return "adminUser/updatePassword";
	}
	
	@RequestMapping("update")
	@ResponseBody
	public AjaxResult updatePassword(Long id,String password,String newpassword,String renewpassword){
		AdminUser selectOne = adminUserService.selectOne(id);
		if(!selectOne.getPassword().equalsIgnoreCase(CommonUtils.calculateMD5(password+selectOne.getPasswordSalt()))){
			return AjaxResult.errorInstance("原始密码错误!");
		}
		if(!CommonUtils.isLengthEnough(newpassword, 6)){
			return AjaxResult.errorInstance("密码长度不能少于6位");
		}
		
		if(!newpassword.equals(renewpassword)){
			return AjaxResult.errorInstance("前后密码不一致！");
		}
		
		AdminUser adminUser=new AdminUser();
		String passwordSalt = UUID.randomUUID().toString();
		adminUser.setPassword(CommonUtils.calculateMD5(password+passwordSalt));
		adminUser.setPasswordSalt(passwordSalt);
		adminUser.setId(id);
		adminUser.setAccount(selectOne.getAccount());
		adminUser.setIsDisabled(true);
		adminUserService.update(adminUser);
		return AjaxResult.successInstance("密码修改成功！");
	}
	
	@RequestMapping("loginout")
	public String loginout(HttpServletRequest request){
		//销毁session
		request.getSession().invalidate();
		return "redirect:/page/adminUser/login.do";
	}
}
