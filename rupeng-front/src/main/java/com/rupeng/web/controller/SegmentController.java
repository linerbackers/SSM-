package com.rupeng.web.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rupeng.pojo.Card;
import com.rupeng.pojo.Chapter;
import com.rupeng.pojo.Segment;
import com.rupeng.pojo.User;
import com.rupeng.pojo.UserSegment;
import com.rupeng.service.CardService;
import com.rupeng.service.ChaptersSevice;
import com.rupeng.service.SegmentsService;
import com.rupeng.service.UserSegmentsService;

@Controller
@RequestMapping("segment")
public class SegmentController {

	private static final Logger logger=LogManager.getLogger(SegmentController.class);
	@Autowired
	private SegmentsService segmentsService;
	@Autowired
	private CardService cardService;
	@Autowired
	private ChaptersSevice chapterService;
	@Autowired
	private UserSegmentsService userSegmentsService;
	@RequestMapping("/detail.do")
	public String detaildo(Long id,Model model,HttpServletRequest request){
		Segment segment = segmentsService.selectOne(id);
		model.addAttribute("segment", segment);
		
		Chapter chapter = chapterService.selectOne(segment.getChapterId());
		model.addAttribute("chapter", chapter);
		
		Card card = cardService.selectOne(chapter.getCardId());
		model.addAttribute("card", card);
		
		User user = (User) request.getSession().getAttribute("frontUser");
		//默认当学生点击进入这个页面的时候就添加一条学生的最新的学习记录
		
		model.addAttribute("user",user );
		
		UserSegment us=new UserSegment();
		us.setCreateTime(new Date());
		us.setSegmentId(id);
		us.setUserId(user.getId());
		userSegmentsService.insert(us);
		return "segment/detail";
	}

	@RequestMapping("nextSegment.do")
	public String nextSegment(Long segmentId,Long chapterId,Model model,HttpServletRequest request){
		//获取chapterId
		Segment segment = segmentsService.selectOne(segmentId);
		Long chId = segment.getChapterId();
		
		if(chId.equals(chapterId)){//确保是在这张章节下的segment
			//查出该chapterId下的segments
			Segment segmentTWO=new Segment();
			segmentTWO.setChapterId(chapterId);
			List<Segment> selectList = segmentsService.selectList(segmentTWO);
			int i=0;
			for(;i<selectList.size();i++){
				if(selectList.get(i).getId().equals(segmentId++)){//当前chapter有下节课程
					return detaildo(segmentId++, model, request);
				}
			}
			//如果当前chapter 已经没有下一节课，我们需要找到下一个篇章的课显示给他
			segmentTWO.setChapterId(chapterId++);
			selectList=segmentsService.selectList(segmentTWO,"seqNum asc");//需要排序
			//如果没有下一个chapter
			if(selectList.size()==0){
				model.addAttribute("message", "本张学习卡已经学完");
				return "message";
			}
				return detaildo(selectList.get(0).getId(), model, request);
		}
		model.addAttribute("message", "");
		return "message";
	}
}
