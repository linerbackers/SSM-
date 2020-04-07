package com.rupeng.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rupeng.pojo.Subject;
import com.rupeng.service.SubjectService;
import com.rupeng.util.AjaxResult;
/**
 * 处理subject的增删改查
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/page/subject")
public class SubjectController {

	@Autowired
	private SubjectService subjectService;
	
	@RequestMapping("/list.do")
	public String showList(Model model){
		Subject subject=new Subject();
		List<Subject> selectList = subjectService.selectList(subject);
		model.addAttribute("subjectList", selectList);
		return "subject/list";
	}
	
	@RequestMapping("/add.do")
	public String redirectAdd(){
		return "subject/add";
	}
	
	@RequestMapping("/add")
	@ResponseBody
	public AjaxResult add(Subject subject){
		//有效性检查
		if(subject.getName()==null){
			return AjaxResult.errorInstance("添加的学科不能为空！");
		}
		
		//唯一性校验
		List<Subject> selectList = subjectService.selectList(subject);
		if(selectList.size()>0){
			return AjaxResult.errorInstance("已经存在该学科，请重新输入！");
		}
		
		//插入数据
		subjectService.insert(subject);
		return AjaxResult.successInstance("添加学科成功！");
	}
	
	@RequestMapping("/delete.do")
	@ResponseBody
	public AjaxResult delete(@RequestParam Long id){
		subjectService.delete(id);
		return AjaxResult.successInstance("删除成功！");
	}
	
	@RequestMapping("update.do")
	public String update(Long id,Model model){
		Subject subject = subjectService.selectOne(id);
		model.addAttribute("subject", subject);
		return "subject/update";
	}
	
	@RequestMapping("update")
	@ResponseBody
	public AjaxResult updateSubmit(Subject subject){
		subjectService.update(subject);
		return AjaxResult.successInstance("修改成功！");
	}
}
