package com.ssafy.study.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.study.user.model.User;
import com.ssafy.study.user.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public User loadUserByUserId(long userId) {
		return userRepository.findByUserId(userId);
	}

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	public void deleteById(long id) {
		userRepository.deleteById(id);
	}
	
}
