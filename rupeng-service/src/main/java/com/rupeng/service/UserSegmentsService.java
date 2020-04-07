package com.rupeng.service;



import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rupeng.mapper.UserSegmentsMapper;
import com.rupeng.pojo.Segment;
import com.rupeng.pojo.User;
import com.rupeng.pojo.UserSegment;

@Service
public class UserSegmentsService extends ManyToManyBaseService<User, UserSegment, Segment>{

	private static final Logger logger=LogManager.getLogger(UserSegmentsService.class);
	public Long selectLastSegmentTimeByUserId(Long userId) {
		UserSegment us=new UserSegment();
		us.setUserId(userId);
		//返回最后一次观看数据
		 PageInfo<UserSegment> page = page(1, 1, us, "createTime desc");
		 if(page.getSize()==0||page==null){
			 logger.debug("查询到数据为空");
		 }
		
		return page.getList().get(0).getSegmentId();
	}

}
