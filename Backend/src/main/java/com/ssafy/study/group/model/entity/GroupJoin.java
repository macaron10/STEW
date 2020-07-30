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
@Table(name = "gp_join", uniqueConstraints = { @UniqueConstraint(columnNames = { "gp_no", "user_id" }) })
public class GroupJoin {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long gpJoinNo;// 소속 아이디

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "gp_no")
	private Group gp;
//	private int gpNo;// 스터디 아이디

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
//	private String userId;// 유저아이디

	public GroupJoin(long gpNo, long userId) {
		gp = new Group(gpNo);
		user = new User(userId);
	}

}
