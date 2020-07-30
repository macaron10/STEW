package com.ssafy.study.group.model.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.ssafy.study.user.model.User;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "gp_req", uniqueConstraints = { @UniqueConstraint(columnNames = { "gp_no", "user_id" }) })
public class GroupReq {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long gpReqNo;// 요청 아이디

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "gp_no")
	private Group gp;
//	private int gpNo;// 스터디 아이디

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
//	private String userId;// 유저아이디

	public GroupReq(long userId, long gpNo) {
		gp = new Group(gpNo);
		user = new User(userId);
	}

}
