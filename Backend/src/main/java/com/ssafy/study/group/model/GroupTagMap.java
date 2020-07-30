package com.ssafy.study.group.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "gp_tag_map", uniqueConstraints = { @UniqueConstraint(columnNames = { "gp_no", "gp_tag_no" }) })
public class GroupTagMap {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long tagMapNo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "gp_no", nullable = false)
	private Group gp;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "gp_tag_no", nullable = false)
	private GroupTag gpTag;
}
