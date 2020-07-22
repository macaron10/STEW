package com.ssafy.study.user.service;

import org.springframework.stereotype.Service;

import com.ssafy.study.user.model.User;
import com.ssafy.study.user.repository.UserRepository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{
	
	@NonNull
	private UserRepository userRepository;

	@Override
	public User login(User user) {
		return userRepository.findByUserEmailAndUserPw(user.getUserEmail(), user.getUserPw());
	}

	@Override
	public User createUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public User findUserByUserEmail(String userEmail) {
		return userRepository.findByUserEmail(userEmail).get();
	}
	
}
