package com.ssafy.study.user.service;

import com.ssafy.study.user.model.User;

public interface UserService {
	User login(User user);
	User createUser(User user);
	User findUserByUserEmail(String userEmail);
}
