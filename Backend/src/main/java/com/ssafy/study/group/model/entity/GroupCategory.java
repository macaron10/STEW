package com.ssafy.study.group.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "gp_cat")
@NoArgsConstructor
public class GroupCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int gpCatNo;// 타입 아이디

	private String gpCatNm; //카테고리

	public GroupCategory(int gpCatNo) {
		this.gpCatNo = gpCatNo;
	}

}
