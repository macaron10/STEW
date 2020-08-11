package com.ssafy.study.user.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ssafy.study.common.model.TimeEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "userEmail", "type" }))
public class User extends TimeEntity {

	@Builder
	public User(String userNm, String userEmail, String userPw,
			String userIntro, int userGoalHr, String type) {
		this.userNm = userNm;
		this.userEmail = userEmail;
		this.userPw = userPw;
		this.userIntro = userIntro;
		this.userGoalHr = userGoalHr;
		this.type = type;
	}

	public User(long userId) {
		this.userId = userId;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private long userId;

	@Column(nullable = false, length = 15)
	private String userNm;

	@Column(nullable = false, length = 128)
	private String userEmail;

	@Setter
	@Column(nullable = false, length = 128)
	private String userPw;

	@Setter
	@Column(nullable = false, length = 128)
	private String roles = "USER";
	
	@Setter
	@Column(nullable = false, length = 128)
	private String type = "stew";
	
	@Setter
	@Column(length = 200)
	private String userIntro;

	@Setter
	@Column(length = 100)
	private String userImg;

	@Setter
	private int userGoalHr;

	
	public void update(UserModify modifyInfo) {
		
		if(!isEmptyString(modifyInfo.getUserNewPw())) {
			this.userPw = new BCryptPasswordEncoder().encode(modifyInfo.getUserNewPw());
		}else {
		
			if(!isEmptyString(modifyInfo.getUserIntro())) {
				this.userIntro = modifyInfo.getUserIntro();
			}
			
			if(!isEmptyString(modifyInfo.getUserNm())) {
				this.userNm = modifyInfo.getUserNm();
			}
			
			if(modifyInfo.getUserGoalHr() != 0) {
				this.userGoalHr = modifyInfo.getUserGoalHr();
			}
			
		}
		
	}
	
	public boolean isEmptyString(String str) {
		if (str == null || str.equals(""))
			return true;
		return false;
	}

}