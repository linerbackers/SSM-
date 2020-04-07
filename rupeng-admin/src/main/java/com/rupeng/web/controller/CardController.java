package com.rupeng.web.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rupeng.pojo.Card;
import com.rupeng.pojo.CardSubject;
import com.rupeng.pojo.Subject;
import com.rupeng.service.CardService;
import com.rupeng.service.CardSubjectService;
import com.rupeng.service.SubjectService;
import com.rupeng.util.AjaxResult;

@Controller
@RequestMapping("/page/card")
public class CardController {

	@Autowired
	private CardService cardService;
	@Autowired
	private SubjectService subjectService;
	@Autowired
	private CardSubjectService cardSubjectService;
	@RequestMapping(value="/list.do",method=RequestMethod.GET)
	public String listdo(Model model){
		List<Card> selectList = cardService.selectList();
		model.addAttribute("cardList", selectList);
		return "card/list";
	}
	
	@RequestMapping(value="/add.do",method=RequestMethod.GET)
	public String adddo(Model model){
		List<Subject> selectList = subjectService.selectList();
		model.addAttribute("subjectList", selectList);
		return "card/add";
	}
	
	@RequestMapping(value="/add.do",method=RequestMethod.POST)
	@ResponseBody
	public AjaxResult adddo(Card card,Long[] subjectIds){
		//非空校验
		//唯一性检查
		//插入
		card.setCreateTime(new Date());
		int insert = cardService.insert(card);
		if(insert>0){
			Card selectOne = cardService.selectOne(card);
			Integer i=0;
			for(Long id:subjectIds){
				CardSubject cs=new CardSubject();
				cs.setCardId(selectOne.getId());
				cs.setSubjectId(id);
				cs.setSeqNum(i++);
				cardSubjectService.insert(cs);
			}
		}
		return AjaxResult.successInstance("学习卡添加成功");
	}
	
	@RequestMapping(value="/delete.do",method=RequestMethod.POST)
	@ResponseBody
	public AjaxResult delete(Long id){
		cardService.delete(id);
		return AjaxResult.successInstance("删除成功");
	}
	
	@RequestMapping(value="/update.do",method=RequestMethod.GET)
	public String updatedo(Long id,Model model){
		Card selectOne = cardService.selectOne(id);
		model.addAttribute("card", selectOne);
		List<Subject> selectList = subjectService.selectList();
		model.addAttribute("subjectList", selectList);
		
		CardSubject cs=new CardSubject();
		cs.setCardId(id);
		List<CardSubject> selectSecondListByFirstId = cardSubjectService.selectList(cs);
		model.addAttribute("cardSubjectList", selectSecondListByFirstId);
		return "/card/update";
	}
	@RequestMapping(value="/update.do",method=RequestMethod.POST)
	@ResponseBody
	public AjaxResult update(Card card,Long[] subjectIds){
		card.setCreateTime(new Date());
		cardService.update(card);
		cardSubjectService.deleteByFirstId(card.getId());
		for(Long id:subjectIds){
			CardSubject cs=new CardSubject();
			cs.setCardId(card.getId());
			cs.setSubjectId(id);
			cardSubjectService.insert(cs);
		}
		return AjaxResult.successInstance("更新成功");
	}
}
