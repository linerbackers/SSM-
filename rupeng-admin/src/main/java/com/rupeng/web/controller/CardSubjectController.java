package com.rupeng.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rupeng.pojo.AdminUser;
import com.rupeng.pojo.Card;
import com.rupeng.pojo.CardSubject;
import com.rupeng.pojo.Subject;
import com.rupeng.service.CardSubjectService;
import com.rupeng.service.SubjectService;
import com.rupeng.util.AjaxResult;

@Controller
@RequestMapping("page/cardSubject")
public class CardSubjectController {

	@Autowired
	private CardSubjectService cardSubjectService;
	@Autowired
	private SubjectService subjectService;
	@RequestMapping(value="/order.do",method=RequestMethod.GET)
	public String order(Model model,Long subjectId){
		if(subjectId==null){
			subjectId=(Long)1L;
		}
		//查询所有学科
		List<Subject> selectList = subjectService.selectList();
		model.addAttribute("subjectList", selectList);
		//默认查出java的关联关系（学习卡名称和原序号）
		List<Card> selectFirstListBySecondId = cardSubjectService.selectFirstListBySecondId(subjectId);
		model.addAttribute("cardList", selectFirstListBySecondId);
		CardSubject cs=new CardSubject();
		cs.setSubjectId(subjectId);
		List<CardSubject> selectList2 = cardSubjectService.selectList(cs,"seqNum asc");
		model.addAttribute("cardSubjectList", selectList2);
		//如果不把subjectId传过去，选择其他学科，前端始终显示java学科。
		model.addAttribute("subjectId",subjectId);
		return "cardSubject/order";
	}
	
	@RequestMapping(value="/order.do",method=RequestMethod.POST)
	@ResponseBody
	public AjaxResult orderSubmit(Long[] cardSubjectIds,Integer[] seqNums ){
        cardSubjectService.order(cardSubjectIds, seqNums);
        return AjaxResult.successInstance("排序成功！");

	}
}
