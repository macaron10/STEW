package com.ssafy.study.common.model;

import java.time.LocalDateTime;
import java.util.TimeZone;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class TimeEntity {

	@CreatedDate
	private LocalDateTime regDate;

	public TimeEntity() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul")); 
	}
}
