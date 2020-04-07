package com.rupeng.mapper;

import java.util.List;
import java.util.Map;

import com.rupeng.pojo.Card;
import com.rupeng.pojo.Chapter;
import com.rupeng.pojo.Segment;

public interface CardMapper extends IMapper<Card>{

	Map<Chapter, List<Segment>> selectSegmentsByChapterId(Long cardId);

}
