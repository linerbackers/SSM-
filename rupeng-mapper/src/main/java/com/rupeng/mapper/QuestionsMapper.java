package com.rupeng.mapper;

import java.util.List;

import com.rupeng.pojo.Question;

public interface QuestionsMapper extends IMapper<Question> {

	public List<Question> selectUnResolvedQuestionByTeacherId(Long id);
	
	public List<Question> selectUnResolvedQuestionByStudentId(Long id);
   
}