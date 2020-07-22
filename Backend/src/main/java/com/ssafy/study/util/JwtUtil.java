package com.ssafy.study.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.ssafy.study.user.model.UserDetail;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil implements Serializable{
//	AccessToken 만료 시간
	public static final long JWT_ACCESS_TOKEN_VALIDITY = 10 * 60;
//	RefreshToken
	public static final long JWT_REFRESH_TOKEN_VALIDITY = 24 * 60 * 60 * 7;
	
	@Value("${jwt.secret}")
	private String secret;
	
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}
	
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}
	
	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		
		return claimsResolver.apply(claims);
	}
	
	public Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}
	
//	유저 정보 Map 
	public Map<String, Object> getUserParseinfo(String token){
		Claims parseInfo = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		
		Map<String, Object> result = new HashMap<>();
		
		result.put("username", parseInfo.getSubject());
		result.put("role", parseInfo.get("role", List.class));
		
		return result;
	}
	
	public boolean isExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		
		return expiration.before(new Date());
	}
	
	public String generateAccessToken(UserDetail userDetail) {
		Map<String, Object> claims = new HashMap<>();
		
		List<String> li = new ArrayList<>();
		
		for(GrantedAuthority ga : userDetail.getAuthorities()) {
			li.add(ga.getAuthority());
		}
		
		claims.put("role", li);
		
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(userDetail.getUserEmail())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_ACCESS_TOKEN_VALIDITY * 1000))
				.signWith(SignatureAlgorithm.HS512, secret)
				.compact();
	}
	
	public String generateRefreshToken(String username) {
        return Jwts.builder()
        		.setSubject(username)
        		.setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_REFRESH_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
	
}
