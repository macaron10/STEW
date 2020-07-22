package com.ssafy.study.group.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ssafy.study.user.model.User;

import lombok.Data;

@Data
@Entity
@Table(name = "gp_join")
public class GroupJoin {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long gpJoinNo;// 소속 아이디

	@ManyToOne
	@JoinColumn(name = "gp_no")
	private Group gp;
//	private int gpNo;// 스터디 아이디

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
//	private String userId;// 유저아이디

}
