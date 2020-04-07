package com.rupeng.web.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.rupeng.pojo.Card;
import com.rupeng.pojo.CardSubject;
import com.rupeng.pojo.Chapter;
import com.rupeng.pojo.Classes;
import com.rupeng.pojo.Segment;
import com.rupeng.pojo.User;
import com.rupeng.pojo.UserCard;
import com.rupeng.pojo.UserSegment;
import com.rupeng.service.CardService;
import com.rupeng.service.CardSubjectService;
import com.rupeng.service.ChaptersSevice;
import com.rupeng.service.ClassesUsersService;
import com.rupeng.service.SegmentsService;
import com.rupeng.service.SettingService;
import com.rupeng.service.UserCardsService;
import com.rupeng.service.UserSegmentsService;
import com.rupeng.util.AjaxResult;
import com.rupeng.util.CommonUtils;
import com.rupeng.util.GetUserInfo;

@Controller
@RequestMapping("/card")
public class CardController {

	@Autowired
	private CardService cardService;
	@Autowired
	private UserCardsService userCardService;
	@Autowired
	private UserSegmentsService userSegmentsService;
	@Autowired
	private SegmentsService segmentService;
	@Autowired
	private ChaptersSevice chaptersSevice;
	@Autowired
	private ClassesUsersService classesUsersService;
	@Autowired
	private CardSubjectService cardSubjectService;
	@Autowired
	private SettingService settingService;
	@RequestMapping("detail.do")
	public String detaildo(Long cardId, Long userId, HttpServletRequest request, Model model) {
		// 检查当前用户有没有这张学习卡，这张学习卡有没有过期
		UserCard uc = new UserCard();
		uc.setUserId(userId);
		uc.setCardId(cardId);
		uc = userCardService.selectOne(uc);
		if (uc.getId() == null) {
			model.addAttribute("message", "您还没有这张学习卡");
			return "message";
		}

		if (uc.getEndTime().getTime() < System.currentTimeMillis()) {
			model.addAttribute("message", "亲，学习卡已经过期");
			return "message";
		}
		// 查出此学习卡的信息
		Card card = cardService.selectOne(cardId);
		long calculateApartDays = CommonUtils.calculateApartDays(new Date(), uc.getEndTime());
		model.addAttribute("calculateApartDays", calculateApartDays);
		model.addAttribute("card", card);
		// 查出此学习卡的所有课程信息
		// 一个篇章对应多个段落
		Map<Chapter, List<Segment>> map = cardService.selectSegmentsByChapterId(cardId);
		model.addAttribute("map", map);
		model.addAttribute("user", request.getSession().getAttribute("frontUser"));

		//上次学到这里功能,根据用户Id,查询到最新一次看segment的时间
		Long userSegmentId = userSegmentsService.selectLastSegmentTimeByUserId(userId);
		model.addAttribute("userSegmentId", userSegmentId);
		return "card/detail";
	}
	
	@RequestMapping("latest.do")
	public String latestdo(HttpServletRequest request,Model model){
		//查询当前用户最后一次操作的segmentId
		User user = (User) GetUserInfo.getUserAttribute(request);
		Long segmentId = userSegmentsService.selectLastSegmentTimeByUserId(user.getId());
		Segment segment = segmentService.selectOne(segmentId);
		Chapter chapter = chaptersSevice.selectOne(segment.getChapterId());
		Card card = cardService.selectOne(chapter.getCardId());
	return	detaildo(card.getId(), user.getId(), request, model);
	}

	
	@RequestMapping("applyNext")
	@ResponseBody
	public AjaxResult applyNext(HttpServletRequest request){
		User user = (User) GetUserInfo.getUserAttribute(request);
		UserCard uc=new UserCard();
		uc.setUserId(user.getId());
		//拿到用户现有的最后一张学习卡的id
		PageInfo<UserCard> selectList =userCardService.page(1, 1, uc, "createTime desc");
		Long latestCardId=null;
		if(!CommonUtils.isEmpty(selectList.getList())){
			latestCardId=selectList.getList().get(0).getCardId();
		}
		//所属班级
		Classes classes = classesUsersService.selectFirstOneBySecondId(user.getId());
		if(classes==null){
			return AjaxResult.errorInstance("您还没加入任何班级");
		}
		
		//从班级中获得学科
		Long subjectId = classes.getSubjectId();
		if(subjectId==null){
			return AjaxResult.errorInstance("该班级不属于任何学科");
		}
		
		//拿到此学科的所有学习卡--排序
		CardSubject cs=new CardSubject();
		cs.setSubjectId(subjectId);
		List<CardSubject> cardList = cardSubjectService.selectList(cs, "seqNum asc");
		if(cardList==null||CommonUtils.isEmpty(cardList)){
			return AjaxResult.errorInstance("该学科还未添加任何学习卡");
		}
		 
		Long nextCardId=null;
		if(latestCardId==null){
			//说明该用户没有一张学习卡，则发放第一张学习卡
			nextCardId = cardList.get(0).getCardId();
		}else{

			//找到下一张学习卡的id
			int i=0;
			for(;i<cardList.size();i++){
				if(latestCardId.equals(cardList.get(i).getCardId())){
					break;
				}
			}
			//是最后一张学习卡
			if(i==cardList.size()-1){
				return AjaxResult.errorInstance("您已学完全部课程");
			}else{
				//下一张学习卡id
				nextCardId=cardList.get(i+1).getCardId();
			}
		
		}
		int cardValidDays =Integer.parseInt(settingService.selectOneByName("card_valid_days"));
		//执行发放学习卡
		UserCard userCard=new UserCard();
		userCard.setCardId(nextCardId);
		userCard.setCreateTime(new Date());
		userCard.setUserId(user.getId());
		userCard.setEndTime(new Date(userCard.getCreateTime().getTime()+1000*60*60*24*cardValidDays));
		userCardService.insert(userCard);
		//这是自己写的方法，不够全面
		//查询当前用户拥有的学习卡
//		UserCard uc=new UserCard();
//		uc.setUserId(user.getId());
//		List<UserCard> selectList = userCardService.selectList(uc,"cardId desc");
//		
//		uc.setCardId(selectList.get(0).getCardId()+1L);
//		uc.setCreateTime(new Date());
//		//一年后
//		Calendar curr = Calendar.getInstance();
//		curr.set(Calendar.YEAR,curr.get(Calendar.YEAR)+1);
//		Date date=curr.getTime();
//		uc.setEndTime(date);
//		userCardService.insert(uc);
		return AjaxResult.successInstance("新学习卡申请成功");
	}
}
