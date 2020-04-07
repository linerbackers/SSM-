package com.rupeng.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rupeng.mapper.SettingsMapper;
import com.rupeng.pojo.Settings;

@Service
public class SettingService extends BaseService<Settings> {

	@Autowired
	private SettingsMapper settingsMapper;
	public String selectOneByName(String name){
		return settingsMapper.selectOneByName(name);
	}
}
