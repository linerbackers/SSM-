package com.rupeng.mapper;

import com.rupeng.pojo.Classes;
import com.rupeng.pojo.ClassesUser;
import com.rupeng.pojo.User;

public interface ClassesUsersMapper extends IManyToManyMapper<Classes, ClassesUser, User> {
	
	public Integer count(ClassesUser classesUser);
   
}