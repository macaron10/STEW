package com.ssafy.study.group.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "gp_req")
public class GroupReq {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long gpRegNo;// 요청 아이디

	private int gpNo;// 스터디 아이디
	private String userId;// 유저아이디

}
