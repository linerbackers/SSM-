package com.rupeng.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rupeng.mapper.ClassesUsersMapper;
import com.rupeng.pojo.Classes;
import com.rupeng.pojo.ClassesUser;
import com.rupeng.pojo.User;
import com.rupeng.pojo.UserCard;
import com.rupeng.util.AjaxResult;
import com.rupeng.util.CommonUtils;

@Service
public class ClassesUsersService extends ManyToManyBaseService<Classes,ClassesUser, User>{

	@Autowired
	private ClassesUsersMapper classesUsersMapper;
	@Autowired
	private ClassesService classesService;
	public Integer count(ClassesUser classesUser){
		Integer count = classesUsersMapper.count(classesUser);
		return count;
	}
	
}
