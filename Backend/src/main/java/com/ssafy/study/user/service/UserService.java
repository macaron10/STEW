package com.ssafy.study.user.service;

import java.util.List;

import com.ssafy.study.user.model.User;

public interface UserService {
	
	User loadUserByUserId(long userId);
	List<User> findAll(); 
	void deleteById(long id);
	
}
