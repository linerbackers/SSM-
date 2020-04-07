package com.rupeng.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rupeng.mapper.CardMapper;
import com.rupeng.pojo.Card;
import com.rupeng.pojo.Chapter;
import com.rupeng.pojo.Segment;

@Service
public class CardService extends BaseService<Card>{
	
	@Autowired 
	private ChaptersSevice chapterSevice;
	@Autowired
	private SegmentsService segmentsService;
	
	public Map<Chapter, List<Segment>> selectSegmentsByChapterId(Long cardId) {
		//此处用LinkedHashMap,保证顺序，否则页面显示的章节就随机了
		Map<Chapter, List<Segment>> map= new LinkedHashMap<>();
		//先查出这张学习卡的所有chapter
		Chapter chapter=new Chapter();
		chapter.setCardId(cardId);
		List<Chapter> chaptersList = chapterSevice.selectList(chapter, " seqNum asc");
		//查出chapter的所有segment
		for(Chapter chapter2:chaptersList){
			Segment segment=new Segment();
			segment.setChapterId(chapter2.getId());
			List<Segment> segmentList = segmentsService.selectList(segment, "seqNum asc");
			map.put(chapter2, segmentList);
		}
		return map;
	}

}
