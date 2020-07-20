package com.ssafy.study.group.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "gp_join")
public class GroupJoin {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long gpJoinNo;// 소속 아이디

	private String userId;// 유저아이디
	private int gpNo;// 스터디아이디 test
	

}
