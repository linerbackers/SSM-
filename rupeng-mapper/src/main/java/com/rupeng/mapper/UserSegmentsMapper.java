package com.rupeng.mapper;

import com.rupeng.pojo.Segment;
import com.rupeng.pojo.User;
import com.rupeng.pojo.UserSegment;

public interface UserSegmentsMapper extends IManyToManyMapper<User, UserSegment,Segment>{
   
}