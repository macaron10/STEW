package com.ssafy.study.group.repository;

import java.util.List;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.impl.JPAQuery;
import com.ssafy.study.group.model.Group;

@Repository
public class GroupRepositoryImpl /* extends QuerydslRepositorySupport */ implements GroupRepositoryCustom {

//	public GroupRepositoryImpl() {
//		super(Group.class);
//	}

//	private BooleanExpression ckGpNm(String nm) {
//		if (nm.equals("") || nm == null)
//			return null;
//		return group.gpNm.contains(nm);
//	}

}
