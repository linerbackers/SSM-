package com.rupeng.mapper;


import org.apache.ibatis.annotations.Param;

import com.rupeng.pojo.Settings;

public interface SettingsMapper extends IMapper<Settings> {

	public String selectOneByName(@Param("name") String name);
}