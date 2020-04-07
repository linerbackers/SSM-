package com.rupeng.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.rupeng.pojo.Card;
import com.rupeng.pojo.Chapter;
import com.rupeng.pojo.Classes;
import com.rupeng.pojo.Question;
import com.rupeng.pojo.QuestionAnswer;
import com.rupeng.pojo.Segment;
import com.rupeng.pojo.User;
import com.rupeng.service.CardSubjectService;
import com.rupeng.service.ChaptersSevice;
import com.rupeng.service.ClassesUsersService;
import com.rupeng.service.QuestionAnswersService;
import com.rupeng.service.QuestionsService;
import com.rupeng.service.SegmentsService;
import com.rupeng.service.UserSegmentsService;
import com.rupeng.util.AjaxResult;
import com.rupeng.util.CommonUtils;
import com.rupeng.util.GetUserInfo;
import com.rupeng.util.JedisUtils;
import com.rupeng.util.JsonUtils;

import redis.clients.jedis.Jedis;

@Controller
@RequestMapping("question")
public class QuestionController {

	@Autowired
	private UserSegmentsService userSegmentsService;
	@Autowired
	private SegmentsService segmentService;
	@Autowired
	private ClassesUsersService classesUsersService;
	@Autowired
	private CardSubjectService cardSubjectService;
	@Autowired
	private ChaptersSevice chapterSevice;
	@Autowired
	private QuestionsService questionService;
	@Autowired
	private QuestionAnswersService questionAnswersService;
	@RequestMapping("/ask")
	public String askdo(HttpServletRequest request,Model model){
		
		//当前用户最近学习的segmentId
		Long latestSegmentId=null;
		//当前用户最近学习的chapterId
		Long latestChapterId=null;
		//当前用户最近学习的cardId
		Long latestCardId=null;
		
		List<Segment> segmentList=null;
		List<Chapter> chapterList=null;
		List<Card> cardList=null;
		
		User user = (User) GetUserInfo.getUserAttribute(request);
		
		//当前用户的班级所属的学科的所有的学习卡
		Classes classes = classesUsersService.selectFirstOneBySecondId(user.getId());
		cardList=cardSubjectService.selectFirstListBySecondId(classes.getSubjectId(),"seqNum asc");
		
		latestSegmentId=userSegmentsService.selectLastSegmentTimeByUserId(user.getId());
		if(latestSegmentId!=null){
			//获取segmentList
			Segment segment = segmentService.selectOne(latestSegmentId);
			latestChapterId=segment.getChapterId();
			Segment param=new Segment();
			param.setChapterId(latestChapterId);
			segmentList=segmentService.selectList(param,"seqNum asc");	
			
			//获取chapterList
			latestCardId=chapterSevice.selectOne(latestChapterId).getCardId();
			Chapter chapter=new Chapter();
			chapter.setCardId(latestCardId);
			chapterList = chapterSevice.selectList(chapter,"seqNum asc");
		}else{
			//默认显示第一张学习卡，及chapterList ,segmentList
			Card card = cardSubjectService.selectFirstOneBySecondId(classes.getSubjectId());
			Chapter chapter=new Chapter();
			chapter.setCardId(card.getId());
			chapterList=chapterSevice.selectList(chapter, "seqNum asc");
			latestChapterId=chapterList.get(0).getId();
			
			if(CommonUtils.isNotEmpty(chapterList)){
				Segment segment=new Segment();
				segment.setChapterId(chapterList.get(0).getId());
				segmentList = segmentService.selectList(segment,"seqNum asc");
				latestSegmentId=segmentList.get(0).getId();
			}
		}
		
		model.addAttribute("cardList", cardList);
		model.addAttribute("chapterList", chapterList);
		model.addAttribute("segmentList", segmentList);
		model.addAttribute("latestSegmentId", latestSegmentId);
		model.addAttribute("latestChapterId", latestChapterId);
		model.addAttribute("latestCardId", latestCardId);
		model.addAttribute("user", user);
		return "question/ask";
	}
	
	
	@RequestMapping("cardList")
	@ResponseBody
	public AjaxResult cardList(Long cardId){
		Chapter chapter=new Chapter();
		chapter.setCardId(cardId);
		List<Chapter> selectList = chapterSevice.selectList(chapter,"seqNum asc");
		return AjaxResult.successInstance(selectList);
	}
	
	@RequestMapping("segmentList")
	@ResponseBody
	public AjaxResult segmentList(Long chapterId){
		Segment segment=new Segment();
		segment.setChapterId(chapterId);
		List<Segment> selectList = segmentService.selectList(segment,"seqNum asc");
		return AjaxResult.successInstance(selectList);
	}
	
	@RequestMapping("formsubmit")
	@ResponseBody
	public AjaxResult formsubmit(Long segmentId,String errorInfo,String errorCode,String description,HttpServletRequest request){
		User user = (User) GetUserInfo.getUserAttribute(request);
		//省略非空校验，唯一性检查
		Question question=new Question();
		question.setUserId(user.getId());
		question.setErrorInfo(errorInfo);
		question.setErrorCode(errorCode);
		question.setDescription(description);
		question.setUsername(user.getName());
		question.setSegmentId(segmentId);
		
		StringBuilder builder=new StringBuilder();
		builder.append(user.getId()+">>"+user.getName()+">>"+segmentId+">>"+"description");
		question.setCourseInfo(builder.toString());
		question.setCreateTime(new Date());
		questionService.insert(question);
		
		//创建消息对象
				Map<String, Object> notification=new HashMap<>();
				Question param=new Question();
				param.setUserId(user.getId());
				question=questionService.page(1, 1, param, "createTime desc").getList().get(0);
				notification.put("questionId", question.getId());
				notification.put("content", "学生提问了新的问题");
				
				//保存到redis服务器
				//key设计为notification_{teacherId}
				//value 使用set结构
				//思路：先找个这个成员所在的班级，遍历班级找到老师
			Classes	classes= classesUsersService.selectFirstOneBySecondId(user.getId());
				List<User> userList = classesUsersService.selectSecondListByFirstId(classes.getId());
				for(User users:userList){
					if(users.getIsTeacher()!=null||users.getIsTeacher()){
						JedisUtils.sadd("notification_"+users.getId(),JsonUtils.toJson( notification));
					}
				}
		return AjaxResult.successInstance("提交成功");
	}

	
	@RequestMapping("list")
	public String condition(Integer pageNum ,String condition,HttpServletRequest request,Model model){
		User user = (User) GetUserInfo.getUserAttribute(request);
		PageInfo<Question> page=null;
		if(pageNum==null){
			pageNum=1;
		}
		
		if(CommonUtils.isEmpty(condition)){
			condition="myAsked";
		}
		
		if("allUnresolved".equals(condition)){
			//全部解决中的问题
			Question params=new Question();
			params.setIsResolved(false);
			page = questionService.page(pageNum, 10, params);
		}
		
		if("allResolved".equals(condition)){
			
		}
		
		if("myAsked".equals(condition)){
			Question question=new Question();
			question.setUserId(user.getId());
			question.setUsername(user.getName());
			page = questionService.page(pageNum, 10, question, "isResolved asc");
			
		}
		
		if("myAnswered".equals(condition)){
			
		}
		model.addAttribute("pageList", page);
		model.addAttribute("condition", condition);
		model.addAttribute("user", user);
		return "question/list";
	}

	@RequestMapping("detail")
	public String detail(Model model,Long id,HttpServletRequest request){
		User user = (User) GetUserInfo.getUserAttribute(request);
		Classes classes = classesUsersService.selectFirstOneBySecondId(user.getId());
		Question selectOne = questionService.selectOne(id);
		
		//查询此问题有关的所有的答案
		QuestionAnswer qu=new QuestionAnswer();
		qu.setQuestionId(id);
		List<QuestionAnswer> allAnswerList = questionAnswersService.selectList(qu, "createTime asc");
		//处理与此问题相关的所有答案，使得答案之间有层级关系
		//1、先找到所有的顶层答案
		if(CommonUtils.isNotEmpty(allAnswerList)){
			for(QuestionAnswer qa:allAnswerList){
				if(qa.getParentId()==null){
					allAnswerList.add(qa);
				}
			}
		}
		
		//2、给每个答案找到所有的直接子答案
		if(CommonUtils.isNotEmpty(allAnswerList)){
			for(QuestionAnswer qa:allAnswerList){
				//找到当前遍历到的答案的所有的直接子答案
				List<QuestionAnswer> childrenAnswerList=new ArrayList<>();
				for(QuestionAnswer questionAnswer3:allAnswerList){
					if(questionAnswer3.getParentId()==qa.getId()){
						childrenAnswerList.add(questionAnswer3);
					}
				}
				qa.setChildrenAnswerList(childrenAnswerList);
			}
		}
		model.addAttribute("question", selectOne);
		model.addAttribute("classes", classes);
		model.addAttribute("user",user);
		model.addAttribute("rootAnswerList", allAnswerList);
		return "question/detail";
	}

	@RequestMapping("answer")
	@ResponseBody
	public AjaxResult answer(Long questionId,String content,HttpServletRequest request,Long parentId){
		User user = (User) GetUserInfo.getUserAttribute(request);
		if(CommonUtils.isEmpty(content)){
			return AjaxResult.errorInstance("答案不能为空");
		}
		
		QuestionAnswer qa=new QuestionAnswer();
		qa.setContent(content);
		qa.setCreateTime(new Date());
		qa.setParentId(parentId);
		qa.setQuestionId(questionId);
		qa.setUsername(user.getName());
		qa.setUserId(user.getId());
		questionAnswersService.insert(qa);
		
		return AjaxResult.successInstance("提交成功");
	}
	@RequestMapping("/adopt")
	@ResponseBody
	public AjaxResult adopt(Long questionAnswerId,HttpServletRequest request ){
		User user = (User) GetUserInfo.getUserAttribute(request);
		//当前用户是提问者和老师的时候才可以采纳。
		QuestionAnswer questionAnswer = questionAnswersService.selectOne(questionAnswerId);
		Question question = questionService.selectOne(questionAnswer.getQuestionId());
		if(!user.getId().equals(question.getUserId())&&user.getIsTeacher()==null||user.getIsTeacher()==false){
			return AjaxResult.errorInstance("您不是老师也不是问题提问者，无法采纳");
		}
		
		questionAnswer.setIsAdopted(true);
		question.setIsResolved(true);
		question.setResolvedTime(new Date());
		questionService.adopt(question,questionAnswer);
		return AjaxResult.successInstance("采纳成功");
	}
}
