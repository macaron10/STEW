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
@Table(name = "gp_req")
public class GroupReq {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long gpRegNo;// 요청 아이디

	@ManyToOne
	@JoinColumn(name = "gp_no")
	private Group gp;
//	private int gpNo;// 스터디 아이디

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
//	private String userId;// 유저아이디

}
