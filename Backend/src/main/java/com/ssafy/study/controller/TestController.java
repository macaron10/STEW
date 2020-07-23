package com.ssafy.study.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.study.common.model.BasicResponse;
import com.ssafy.study.user.model.User;
import com.ssafy.study.user.repository.UserRepository;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api")
@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
		@ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
		@ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
		@ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })
public class TestController {
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/test")
	@ApiOperation("아무나 요청 가능")
	public String test1() {
		return "API Test 1 ";
	}
	
//	only manager
	@GetMapping("/manager")
	@ApiOperation("매니저만")
	public String reports() {
		return "API Test 2";
	}
	
//	only admin
	@GetMapping("/admin")
	@ApiOperation("어드민만")
	public List<User> allUsers(){
		return this.userRepository.findAll();
	}
}
