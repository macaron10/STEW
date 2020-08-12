package com.ssafy.study.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.study.user.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	User findByUserEmailAndUserPw(String userEmail, String userPw);
	
	User findByUserEmailAndType(String userEmail, String type);
	
	User findByUserId(long userId);
	
	List<User> findAllByUserEmail(String userEmail);
	
	Optional<User> findByUserEmail(String userEmail);	
	
	
	
}
