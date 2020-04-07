package com.rupeng.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rupeng.mapper.QuestionsMapper;
import com.rupeng.pojo.Question;
import com.rupeng.pojo.QuestionAnswer;
@Service
public class QuestionsService extends BaseService<Question>{

	@Autowired
	private QuestionAnswersService  questionAnswersService;
	@Autowired
	private QuestionsMapper questionMapper;
	public void adopt(Question question, QuestionAnswer questionAnswer) {

		update(question);
		questionAnswersService.update(questionAnswer);
	}
	
	//查询出当前老师所在的所有班级的所有学生所提问的未解决的问题
	public List<Question> selectUnResolvedQuestionByTeacherId(Long id) {
		List<Question> questionList=questionMapper.selectUnResolvedQuestionByTeacherId(id);
		return questionList;
	}
	
	public List<Question> selectUnResolvedQuestionByStudentId(Long id){
		List<Question> selectUnResolvedQuestionByStudentId = questionMapper.selectUnResolvedQuestionByStudentId(id);
		return selectUnResolvedQuestionByStudentId;
	}

}
