package com.ssafy.study.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.ssafy.study.common.model.BasicResponse;
import com.ssafy.study.user.model.User;
import com.ssafy.study.user.model.UserPrincipal;
import com.ssafy.study.user.model.UserSignUp;
import com.ssafy.study.user.service.UserService;
import com.ssafy.study.util.JwtProperties;
import com.ssafy.study.util.JwtUtil;

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
	private RedisTemplate<String, Object> redisTemplate;
	@Autowired
	private UserService userService;
	
//	테스트용
	@PostMapping("/signup")
	@ApiOperation("회원가입 테스트")
	public ResponseEntity<BasicResponse> signUp(@RequestBody UserSignUp signUpInfo){
		
		User user = signUpInfo.toEntity();
		
		userService.save(user);
		
		BasicResponse result = new BasicResponse();
		
		result.status = true;
		result.msg = "success";
		result.object = user;
		
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	
	@GetMapping("/{userId}")
	@ApiOperation("회원 정보")
	public ResponseEntity<BasicResponse> userDetail(@PathVariable long userId){
		
		User user = userService.loadUserByUserId(userId);
		
		BasicResponse result = new BasicResponse();
		
		result.status = true;
		result.msg = "success";
		result.object = user;
		
		return new ResponseEntity<>(result, HttpStatus.OK);
		
	}
	
	@DeleteMapping("/{userId}")
	@ApiOperation("회원 탈퇴")
	public ResponseEntity<BasicResponse> signOut(@PathVariable long userId){
		
		userService.deleteById(userId);
		
		BasicResponse result = new BasicResponse();
		
		result.status = true;
		result.msg = "success";
		result.object = true;
		
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@PutMapping
	@ApiOperation("회원 수정")
	public ResponseEntity<BasicResponse> modify(@RequestBody User user){
		
		BasicResponse result = new BasicResponse();
		
		User modifiedUser = userService.save(user);
		
		result.status = true;
		result.msg = "success";
		result.object = modifiedUser;
		
		return new ResponseEntity<>(result, HttpStatus.OK);
		
	}
	
	@GetMapping("/check")
	@ApiOperation("중복 확인")
	public ResponseEntity<BasicResponse> checkDuplicated(String userEmail){
		
		BasicResponse result = new BasicResponse();
		
		result.status = true;
		result.msg = "success";
		result.object = userService.findByUserEmail(userEmail);
		
		return new ResponseEntity<>(result, HttpStatus.OK);
		
	}
	
	@GetMapping("/refresh")
	@ApiOperation("토큰 갱신")
	public ResponseEntity<BasicResponse> refreshToken(HttpServletRequest request, HttpServletResponse response){
		
		BasicResponse result = new BasicResponse();
		
		String refreshToken = request.getHeader(JwtProperties.HEADER_STRING);
		
//		리프레쉬 토큰 문제가 이씀
		if(refreshToken == null);
		try {
			JwtUtil.verify(refreshToken);
		} catch (TokenExpiredException e) {
			result.msg = "expired";
			
		} catch (Exception e) {
			result.msg = "Unhandled Exception";
			e.printStackTrace();
		}

		String accessToken = null;
		if(request.getCookies() != null) {
			for(Cookie c : request.getCookies()) {
				if(c.getName().equals("accessToken")) accessToken = c.getValue();
			}
		}
		
		if(accessToken == null) {
			result.msg = "accessToken not found";
		}
		
		String userEmail = JwtUtil.getUsernameFromToken(accessToken);
		
		if(refreshToken.equals(redisTemplate.opsForValue().get(userEmail))){
			UserPrincipal userPrincipal = new UserPrincipal(userService.findByUserEmail(userEmail));
			Cookie c = new Cookie("accessToken", JwtUtil.generateAccessToken(userPrincipal));
			c.setHttpOnly(true);
			c.setPath("/api");
			
			response.addCookie(c);
			
			result.status = true;
			result.msg = "success";
		}
		
		return new ResponseEntity<>(result, result.status ? HttpStatus.OK : HttpStatus.FORBIDDEN);
	}
	
}
