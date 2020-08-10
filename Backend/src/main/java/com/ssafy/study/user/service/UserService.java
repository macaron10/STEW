package com.ssafy.study.user.service;

import java.util.List;

import com.ssafy.study.user.model.User;

public interface UserService {
	
	User save(User user);
	User loadUserByUserId(long userId);
	List<User> findAll(); 
	void deleteById(long id);
	User findByUserEmail(String userEmail);
	User findByUserEmailAndType(String userEmail, String type);
	
}
