package com.rupengadmin;

import org.junit.Test;

import com.rupeng.pojo.Segment;
import com.rupeng.util.AjaxResult;
import com.rupeng.util.HttpUtils;
import com.rupeng.web.controller.SegmentController;

public class rupengTest {

	/*@Test
	public void CardTest() {
		
		try {
			HttpUtils.get("http://localhost:8080/rupeng-admin/page/card/add.do");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
	@Test
	public void SegmentTest(){
		SegmentController sc =new SegmentController();
		Segment segment=new Segment();
		segment.setName("test");
		segment.setSeqNum(1);
		segment.setDescription("test");
		segment.setVideoCode("xxx");
		segment.setChapterId(9L);
		segment.setIsDeleted(false);
		segment.setId(11L);
		AjaxResult add = sc.add(segment);
		if(add.getStatus().equals("200")){
			System.out.println("success");
		}
		
	}

}
