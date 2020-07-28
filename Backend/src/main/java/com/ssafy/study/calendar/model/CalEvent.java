package com.ssafy.study.calendar.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cal_evt")
public class CalEvent {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long cNo;

//	@Column(columnDefinition = "char(1) not null check (c_type in ('U', 'G'))")
	private char cType;
	private long cOwn;
	@Column(columnDefinition = "timestamp default current_timestamp()")
	private Timestamp cStTm;
	private Timestamp cEndTm;

	@Column(length = 100)
	private String cEvtNm;
	@Column(length = 300)
	private String cEvtDsc;

}
