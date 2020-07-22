package com.ssafy.study.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.study.common.model.BasicResponse;
import com.ssafy.study.user.model.User;
import com.ssafy.study.user.service.UserServiceImpl;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/user")
@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
		@ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
		@ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
		@ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })
public class UserController {
	
	@Autowired
	private UserServiceImpl userService;
	
	@PostMapping("/signin")
	@ApiOperation("로그인")
	public ResponseEntity<BasicResponse> signIn(@RequestParam String userEmail, @RequestParam String userPw) {
		
		BasicResponse result = new BasicResponse();
		
		result.object = userService.findUserByUserEmail(userEmail);
		
		return new ResponseEntity<>(result, HttpStatus.OK);
		
	}
	
//	테스트용
	@PostMapping("/signup")
	@ApiOperation("회원가입")
	public ResponseEntity<BasicResponse> signUp(@RequestBody User user){
		
		String password = user.getUserPw();
		
		user.setUserPw(new BCryptPasswordEncoder().encode(password));
		
		BasicResponse result = new BasicResponse();
		
		result.object = user;
		
		userService.createUser(user);
		
		return new ResponseEntity<>(result, HttpStatus.OK);
		
	}
	
}
