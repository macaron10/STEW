package com.ssafy.study.group.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "gp_tag")
public class GroupTag {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long gpTagNo;
	
	@Column(length = 50, nullable = false)
	private String gpTagNm;
}
