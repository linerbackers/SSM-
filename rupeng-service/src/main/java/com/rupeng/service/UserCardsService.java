package com.rupeng.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rupeng.mapper.ClassesUsersMapper;
import com.rupeng.mapper.SettingsMapper;
import com.rupeng.pojo.Card;
import com.rupeng.pojo.Settings;
import com.rupeng.pojo.User;
import com.rupeng.pojo.UserCard;
import com.rupeng.util.AjaxResult;
import com.rupeng.util.CommonUtils;

@Service
public class UserCardsService extends ManyToManyBaseService<User, UserCard, Card> {
	@Autowired
	private ClassesUsersMapper classesUsersMapper;

	@Autowired
	private SettingService settingService;
	public AjaxResult activateFirstCard(Long classesId, Long cardId) {

		// 该班级下的所有成员
		List<User> userList = classesUsersMapper.selectSecondListByFirstId(classesId);
		// 有可能没有成员
		if (CommonUtils.isEmpty(userList)) {
			return AjaxResult.errorInstance("该班级下还没有成员！");
		}

		// 遍历判断是否有老师
		for (User user : userList) {
			if (user.getIsTeacher()) {
				continue;// 跳过执行下一个循环
			}
			// 判断学生是否已经拥有第一张学习卡
			UserCard uc = new UserCard();
			uc.setUserId(user.getId());
			uc.setCardId(cardId);
			if (isExisted(uc)) {
				continue;//给下一个发放学习卡
			}
			
			Settings setting = new Settings();
	        setting.setName("card_valid_days");
	        setting = settingService.selectOne(setting);

	        int validDays = Integer.parseInt(setting.getValue());
			uc.setCreateTime(new Date());

            Date endTime = new Date(uc.getCreateTime().getTime() + validDays * 1000 * 60 * 60 * 24);
            uc.setEndTime(endTime);

            insert(uc);
		}
		
		return AjaxResult.successInstance("学习卡已经发放");
	}

}
