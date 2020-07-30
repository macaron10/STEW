package com.ssafy.study.group.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class GroupTagMapRepositoryImpl implements GroupTagMapRepositoryCustom {
	@PersistenceContext
	private EntityManager em;
}
