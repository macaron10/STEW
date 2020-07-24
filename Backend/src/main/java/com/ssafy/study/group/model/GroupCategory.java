package com.ssafy.study.group.model;

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

	private String gpCatLg;// 대분류
	private String gpCatMd;// 중분류
	private String gpCatSm;// 소분류

	public GroupCategory(int gpCatNo) {
		this.gpCatNo = gpCatNo;
	}

	public GroupCategory(String gpCatLg) {
		this.gpCatLg = gpCatLg;
	}

	public GroupCategory(String gpCatLg, String gpCatMd) {
		this.gpCatLg = gpCatLg;
		this.gpCatMd = gpCatMd;
	}

	public GroupCategory(String gpCatLg, String gpCatMd, String gpCatSm) {
		this.gpCatLg = gpCatLg;
		this.gpCatMd = gpCatMd;
		this.gpCatSm = gpCatSm;
	}

}
