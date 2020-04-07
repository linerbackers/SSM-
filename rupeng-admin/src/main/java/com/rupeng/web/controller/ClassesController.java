package com.rupeng.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rupeng.pojo.Classes;
import com.rupeng.pojo.Subject;
import com.rupeng.service.ClassesService;
import com.rupeng.service.SubjectService;
import com.rupeng.util.AjaxResult;

@Controller
@RequestMapping("/page/classes")
public class ClassesController {

	@Autowired
	private ClassesService classesService;
	@Autowired
	private SubjectService subjectService;
	
	@RequestMapping("/list.do")
	public String listdo(Model model){
		List<Classes> selectList = classesService.selectList();
		model.addAttribute("classesList", selectList);
		
		List<Subject> selectList2 = subjectService.selectList();
		model.addAttribute("subjectList", selectList2);
		return "classes/list";
	}
	
	
	@RequestMapping(value="/add.do",method=RequestMethod.GET)
	public String adddo(Model model){
		List<Subject> selectList = subjectService.selectList();
		model.addAttribute("subjectList", selectList);
		return "classes/add";
	}
	
	@RequestMapping(value="/add.do",method=RequestMethod.POST)
	@ResponseBody
	public AjaxResult add(String name,Long subjectId){
		Classes classes=new Classes();
		classes.setName(name);
		classes.setSubjectId(subjectId);
		classesService.insert(classes);
		return AjaxResult.successInstance("添加成功");
	}
	
	@RequestMapping(value="/delete.do",method=RequestMethod.POST)
	@ResponseBody
	public AjaxResult delete(Long id){
		classesService.delete(id);
		return AjaxResult.successInstance("删除成功");
	}
	

	@RequestMapping(value="/update.do",method=RequestMethod.GET)
	public String updatedo(Long id,Model model){
		List<Subject> selectList = subjectService.selectList();
		model.addAttribute("subjectList", selectList);
		Classes selectOne = classesService.selectOne(id);
		model.addAttribute("classes", selectOne);
		return "classes/add";
	}
	
	
	@RequestMapping(value="/update.do",method=RequestMethod.POST)
	@ResponseBody
	public AjaxResult update(Classes classes){
		classesService.update(classes);
		return AjaxResult.successInstance("修改成功");
	}
}
