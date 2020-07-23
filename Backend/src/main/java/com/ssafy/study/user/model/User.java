package com.ssafy.study.user.model;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Data
@Entity
public class User implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private long userId;
	
	@Column(nullable = false, length = 15)
	private String userNm;
	
	@Column(nullable = false, unique = true, length = 128)
	private String userEmail;
	
	@Column(nullable = false, length = 128)
	private String userPw;
	
	@Column(nullable = false, length = 50)
	private String userPhone;
	
	@Column(nullable = false)
	private char userGender;
	
	@Column(nullable = false)
	private String roles;
	
	private String permissions;
	
	@Column(length = 200)
	private String userIntro;
	
	@Column(length = 100)
	private String userImg;
	
	private int userGoalHr;
	
	private Date userRegDate;
	
	@Builder
	public User(String userEmail, String userPw) {
		this.userEmail = userEmail;
		this.userPw = userPw;
	}
	
	@Setter
	@Transient
	private boolean isEnable = true;
	
	public List<String> getRoleList(){
		if(this.roles.length() > 0) {
			return Arrays.asList(this.roles.split(","));
		}
		
		return new ArrayList<>();
	}
	
	public List<String> getPermissionList(){
		if(this.permissions.length() > 0) {
			return Arrays.asList(this.permissions.split(","));
		}
		
		return new ArrayList<>();
	}
	
}