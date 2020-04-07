package com.rupeng.mapper;

import com.rupeng.pojo.Card;
import com.rupeng.pojo.User;
import com.rupeng.pojo.UserCard;

public interface UserCardsMapper extends IManyToManyMapper<User, UserCard, Card>{
   
}