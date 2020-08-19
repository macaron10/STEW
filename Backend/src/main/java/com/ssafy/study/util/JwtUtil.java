package com.ssafy.study.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ssafy.study.user.model.UserDto;
import com.ssafy.study.user.model.UserPrincipal;

@Component
public class JwtUtil implements Serializable{
	
	public static String getUsernameFromToken(String token) {
		return JWT.decode(sliceBearer(token)).getSubject();
	}
	
	public static long getUserIdFromToken(String token) {
		return JWT.decode(sliceBearer(token)).getClaim("userId").asLong();
	}

	public static UserDto getUserFromToken(String token) {
		
		DecodedJWT decodedJwt = JWT.decode(sliceBearer(token));
		
		return UserDto.builder()
				.userId(decodedJwt.getClaim("userId").asLong())
				.userNm(decodedJwt.getClaim("userNm").asString())
				.userImg(decodedJwt.getClaim("userImg").asString())
				.type(decodedJwt.getClaim("type").asString())
				.userEmail(decodedJwt.getSubject())
				.build();
		
	}
	
	public static String getUserTypeFromToken(String token) {
		return JWT.decode(sliceBearer(token)).getClaim("type").asString();
	}
	
	public static Collection<? extends GrantedAuthority> getAuthoritiesFromToken(String token) {
		String[] roles = JWT.decode(sliceBearer(token)).getClaim("role").asArray(String.class);
		
		List<GrantedAuthority> authorities = new ArrayList<>();
		
		for(int i=1;i<roles.length;i++) {
			authorities.add(new SimpleGrantedAuthority(roles[i]));
		}
		
		return authorities;
	}
	
	public static void verify(String token, HttpServletRequest request) {
		JWT.require(Algorithm.HMAC512(JwtProperties.SECRET.getBytes()))
		.build()
		.verify(sliceBearer(token));
	}
	
	public static boolean verify(String token) {
		try {
			
			JWT.require(Algorithm.HMAC512(JwtProperties.SECRET.getBytes()))
			.build()
			.verify(sliceBearer(token));
			
			return true;
			
		} catch (AlgorithmMismatchException e) {
		} catch (SignatureVerificationException e) {
		} catch (TokenExpiredException e) {
		} catch (InvalidClaimException e) {
		} catch (JWTVerificationException e) {
		}
		
		return false;
	}
	
	public static String generateAccessToken(UserPrincipal userPrincipal) {
		ArrayList<String> authorities = new ArrayList<>();
		for(GrantedAuthority p : userPrincipal.getAuthorities()) {
			authorities.add(p.getAuthority());
		}
		
		return 
				JWT.create()
				.withArrayClaim("role", authorities.toArray(new String[authorities.size()]))
				.withClaim("userId", userPrincipal.getUserId())
				.withClaim("userNm", userPrincipal.getUserNm())
				.withClaim("userImg", userPrincipal.getUserImg())
				.withClaim("type", userPrincipal.getType())
				.withSubject(userPrincipal.getUsername())
				.withIssuedAt(new Date(System.currentTimeMillis()))
				.withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME_ACCESS))
				.sign(Algorithm.HMAC512(JwtProperties.SECRET.getBytes()));
		
	}
	
	public static String generateAccessTokenExpireIn(UserPrincipal userPrincipal, long millis) {
		ArrayList<String> authorities = new ArrayList<>();
		for(GrantedAuthority p : userPrincipal.getAuthorities()) {
			authorities.add(p.getAuthority());
		}
		
		return 
				JWT.create()
				.withArrayClaim("role", authorities.toArray(new String[authorities.size()]))
				.withClaim("userId", userPrincipal.getUserId())
				.withClaim("userNm", userPrincipal.getUserNm())
				.withClaim("userImg", userPrincipal.getUserImg())
				.withClaim("type", userPrincipal.getType())
				.withSubject(userPrincipal.getUsername())
				.withIssuedAt(new Date(System.currentTimeMillis()))
				.withExpiresAt(new Date(System.currentTimeMillis() + millis))
				.sign(Algorithm.HMAC512(JwtProperties.SECRET.getBytes()));
		
	}
	
	public static String generateRefreshToken() {
		
		return
				JWT.create()
				.withIssuedAt(new Date(System.currentTimeMillis()))
				.withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME_REFRESH))
				.sign(Algorithm.HMAC512(JwtProperties.SECRET.getBytes()));
		
	}
	
	public static Long getExpiringTime(String token) {
		return JWT.decode(sliceBearer(token)).getExpiresAt().getTime();
	}
	
	public static String getRefreshKey(String accessToken) {
		
		return JwtUtil.getUsernameFromToken(accessToken) + "#" + JwtUtil.getUserTypeFromToken(accessToken);
		
	}
	
	private static String sliceBearer(String token) {
		if(token.startsWith(JwtProperties.TOKEN_PREFIX)) {
			token = token.replace(JwtProperties.TOKEN_PREFIX, "");
		}
		
		return token;
	}
	
}

