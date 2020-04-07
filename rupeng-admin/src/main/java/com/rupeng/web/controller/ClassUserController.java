package com.rupeng.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.rupeng.pojo.ClassesUser;
import com.rupeng.pojo.User;
import com.rupeng.service.ClassesUsersService;
import com.rupeng.service.UsersService;
import com.rupeng.util.AjaxResult;

@Controller
@RequestMapping("/page/classesUser")
public class ClassUserController {

	@Autowired
	private ClassesUsersService classesUsersService;
	
	@Autowired
	private UsersService usersService;
	
	@RequestMapping(value="add.do")
	public String searchdo(Model model,Integer curr,String param,Long classesId,HttpServletRequest request){
		if(curr==null){
			curr=1;
		}
		
		Map<String, Object> params=new HashMap<String, Object>();
		if (param != null && param.length() > 0) {
            params.put("param", "%" + param + "%");
        }else{
        	param=null;
        }
		PageInfo<User> search = usersService.search(curr, 10, params);
		Long classId = (Long) request.getSession().getAttribute("classesId");
		if(classId!=null){
			model.addAttribute("classesId", classId);
		}else{
			model.addAttribute("classesId", classesId);
		}
		model.addAttribute("pageInfo", search);
		model.addAttribute("param", params);
		return "classesUser/add";
	}
	
	@RequestMapping(value="add",method=RequestMethod.POST)
	@ResponseBody
	public AjaxResult add(Long classesId,Long userId){
		//非空校验省略
		
		ClassesUser cu=new ClassesUser();
		cu.setClassesId(classesId);
		cu.setUserId(userId);
		//唯一性检查
		Integer count = classesUsersService.count(cu);
		if(count<=0){
			return AjaxResult.errorInstance("添加失败,已经存在该班级中");
		}
		classesUsersService.insert(cu);
		return AjaxResult.successInstance("添加成功");
	}
	
	//为何要把classesId 放到session中， 因为 在添加成员的页面（add.jsp）classesId是从update.jsp页面传过去的，
	//add页面 在模糊查询重新加载页面后，classesId为null, 这种情况下从session中重新取一遍是比较好的方法。
	@RequestMapping("/update.do")
	public String updatedo(Long classesId,Model model,HttpServletRequest request){
		List<User> selectSecondListByFirstId = classesUsersService.selectSecondListByFirstId(classesId);
		request.getSession().setAttribute("classesId", classesId);
		model.addAttribute("userList", selectSecondListByFirstId);
		model.addAttribute("classesId", classesId);
		return "classesUser/update";
	}
	
	
	@RequestMapping("delete.do")
		@ResponseBody
		public AjaxResult delete(Long id){
			classesUsersService.deleteBySecondId(id);
			return AjaxResult.successInstance("删除成功！");
		}
}
