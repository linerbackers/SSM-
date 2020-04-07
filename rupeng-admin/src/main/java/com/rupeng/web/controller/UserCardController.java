package com.rupeng.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rupeng.pojo.CardSubject;
import com.rupeng.pojo.Classes;
import com.rupeng.service.CardSubjectService;
import com.rupeng.service.ClassesService;
import com.rupeng.service.ClassesUsersService;
import com.rupeng.service.UserCardsService;
import com.rupeng.util.AjaxResult;
import com.rupeng.util.CommonUtils;

@Controller
@RequestMapping("/page/userCard")
public class UserCardController {

	@Autowired
	private UserCardsService userCardsService;
	@Autowired
	private ClassesService classesService;
	@Autowired
	private CardSubjectService cardSubjectService;
	@RequestMapping("activateFirstCard.do")
	@ResponseBody
	public AjaxResult activateFirstCard(Long classesId) {
		 //根据指定的classesId查询出所属的学科
		Classes classes = classesService.selectOne(classesId);
		Long subjectId = classes.getSubjectId();

		if (subjectId == null) {
			return AjaxResult.errorInstance("此班级还没有指定学科");
		}

		// 进而查询出这个学科的第一张学习卡
		CardSubject cardSubject = new CardSubject();
		cardSubject.setSubjectId(subjectId);
		List<CardSubject> cardSubjectList = cardSubjectService.selectList(cardSubject, "seqNum asc");

		if (CommonUtils.isEmpty(cardSubjectList)) {
			return AjaxResult.errorInstance("此班级所属的学科还没有指定学习卡");
		}
		// 此学科第一张学习卡的id
		Long cardId = cardSubjectList.get(0).getCardId();

		// 给这个班级里面的所有的学生发放学习卡
		AjaxResult result = userCardsService.activateFirstCard(classesId, cardId);
		return result;
	}
}
