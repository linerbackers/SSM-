package com.rupeng.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rupeng.pojo.Segment;
import com.rupeng.service.SegmentsService;
import com.rupeng.util.AjaxResult;

@Controller
@RequestMapping("/page/segment")
public class SegmentController {
	
	@Autowired
	private SegmentsService sgementService;

	@RequestMapping("list.do")
	public String listdo(Long id,Model model){
		Segment segment=new Segment();
		segment.setChapterId(id);
		List<Segment> selectList = sgementService.selectList(segment);
		model.addAttribute("segmentList", selectList);
		model.addAttribute("chapterId", id);
		return "segment/list";
	}
	
	@RequestMapping(value="add.do",method=RequestMethod.GET)
	public String adddo(Long chapterId,Model model){
		model.addAttribute("chapterId", chapterId);
		return "segment/add";
	}
	
	@RequestMapping(value="add.do",method=RequestMethod.POST)
	@ResponseBody
	public AjaxResult add(Segment segment){
		//唯一性检查和非空校验省略
		sgementService.insert(segment);
		return AjaxResult.successInstance("添加成功");
	}
	
	@RequestMapping(value="delete.do",method=RequestMethod.POST)
	@ResponseBody
	public AjaxResult delete(Long id){
		sgementService.delete(id);
		return AjaxResult.successInstance("删除成功");
	}
	
	@RequestMapping(value="update.do",method=RequestMethod.GET)
	public String updatedo(Long id, Model model){
		Segment selectOne = sgementService.selectOne(id);
		model.addAttribute("segment", selectOne);
		return "segment/update";
	}
	
	@RequestMapping(value="update.do",method=RequestMethod.POST)
	@ResponseBody
	public AjaxResult update(Segment segment){
		int update = sgementService.update(segment);
		return AjaxResult.successInstance("修改成功！");
	}
}
