package com.rupeng.web.redis;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class redistest {

	   @Test
	    public void testSpringJedis(){
	        ClassPathXmlApplicationContext classPathXmlApplicationContext = 
	        new ClassPathXmlApplicationContext("classpath:beans.xml");
	        JedisPool bean = (JedisPool) classPathXmlApplicationContext.getBean("jedisClientSingle");
	        Jedis resource = bean.getResource();
	        resource.set("20180718", "xxxxxx");
	        System.out.println(resource.get("20180718"));
	        classPathXmlApplicationContext.close();
	    }
}
