package com.web.cobra.xp.controller;


import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.cobra.xp.entity.User;


@RestController
@RequestMapping("/redis")
public class RedisTestController {

	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Resource 
	private RedisTemplate<String, User> redisTemplate;

	@GetMapping("/get/{key}")
	public String getRedis(@PathVariable(name = "key") String key) {
		return stringRedisTemplate.opsForValue().get(key);
	}

	@GetMapping("/set/{key}/{value}")
	public String setRedis(@PathVariable(name = "key") String key, @PathVariable(name = "value") String value) {
		stringRedisTemplate.opsForValue().set(key, value);
		return "SUCCESS";
	}

	@GetMapping("/setentity")
	public String postEntity() {
		User user = new User();
		user.setName("yangxiaoping");
		user.setAddress("四川省巴中市");
		redisTemplate.opsForValue().set(user.getName(), user);
		return "SUCCESS";
	}

	@GetMapping("/getentity/{key}")
	public Object getEntity(@PathVariable(name = "key") String key) {
		return redisTemplate.opsForValue().get(key);
	}

}
