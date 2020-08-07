package com.ssafy.study.timeAcml.model.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ssafy.study.group.model.entity.Group;
import com.ssafy.study.timeAcml.model.dto.addTimeAcmlDto;
import com.ssafy.study.user.model.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "tm_acml"/*
						 * , uniqueConstraints = {
						 * 
						 * @UniqueConstraint(columnNames = { "gp_no", "user_id", "tm_acml_date" }) }
						 */)
public class TimeAcml {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long tmAcmlId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "gp_no", nullable = false)
	private Group gp;

	@Column(columnDefinition = "DATE DEFAULT NOW()", nullable = false)
	private LocalDateTime tmAcmlDate;

	private long tmAcmlTime;

	public TimeAcml(long userId, long gpNo) {
		this.user = new User(userId);
		this.gp = new Group(gpNo);

		tmAcmlDate = LocalDateTime.now();
	}

	public void update(addTimeAcmlDto time) {
		tmAcmlTime += time.getTmAcmlTime();
	}

}
