package com.ssafy.study.controller;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.study.common.model.BasicResponse;
import com.ssafy.study.user.model.User;
import com.ssafy.study.user.model.UserPrincipal;
import com.ssafy.study.user.model.UserSignIn;
import com.ssafy.study.user.service.UserService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
		@ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
		@ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
		@ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })
public class TestController {

	@Autowired
	private UserService userService;

	@PostMapping("/user/signin")
	@ApiOperation("로그인 테스트용")
	public String login(@RequestBody UserSignIn signinRequest) {
		return "";
	}

	@GetMapping("/test")
	@ApiOperation("아무나 요청 가능")
	public String test1(HttpServletRequest request, HttpServletResponse response) {
		for(Cookie c : request.getCookies()) {
			System.out.println(c.getName() + " " + c.getValue());
		}
		
		return "API Test 1 ";
	}
	
	@GetMapping("/user/test")
	@ApiOperation("아무나 요청 가능")
	public String test4(HttpServletRequest request, HttpServletResponse response) {
		for(Cookie c : request.getCookies()) {
			System.out.println(c.getName() + " " + c.getValue());
		}
		
		return "API Test 1 ";
	}
	
	@GetMapping("/user/tokeninit")
	@ApiOperation("토큰 지워보기")
	public String test2(HttpServletRequest request, HttpServletResponse response) {
		for(Cookie c : request.getCookies()) {
			if(c.getName().equals("accessToken")) {
				System.out.println("이놈인데");
				System.out.println(c.getName() + " " + c.getValue());
				System.out.println(c.getPath() + " " + c.isHttpOnly() + " " + c.getSecure());
				
				Cookie token = new Cookie(c.getName(), null);
				token.setPath("/api");
				token.setMaxAge(0);
					
				response.addCookie(token);
			}
		}
		
		return "지워짐??";
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
	public List<User> allUsers(UserPrincipal userPrincipal) {
//		System.out.println("INFO : " + userPrincipal.getAuthorities());
		
		return this.userService.findAll();
	}

}
