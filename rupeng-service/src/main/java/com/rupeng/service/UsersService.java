package com.rupeng.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rupeng.mapper.UsersMapper;
import com.rupeng.pojo.User;

@Service
public class UsersService extends BaseService<User>{

	@Autowired
	private UsersMapper usersMapper;
	
	public PageInfo<User> search(Integer currPage, int pageSize, Map<String, Object> params) {
		PageHelper.startPage(currPage, pageSize);
		List<User> search = usersMapper.search(params);
		return new PageInfo<User>(search);
		
	}
	
	public int count(User user){
		return usersMapper.count(user);
	}

}
