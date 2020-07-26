package com.ssafy.study.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.study.user.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	User findByUserEmailAndUserPw(String userEmail, String userPw);
	
	User findByUserId(long userId);
	
	Optional<User> findByUserEmail(String userEmail);
	
}
