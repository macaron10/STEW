package com.ssafy.study.group.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.ssafy.study.group.model.Group;

public class GroupRepositoryImpl implements CustomGroupRepository {

	@PersistenceContext
	private EntityManager em;

	public List<Group> example() {
		String username = "";
		String jpql = "SELECT m FROM Member m where m.username = :username";
		TypedQuery<Group> query = em.createQuery(jpql, Group.class);
		query.setParameter("username", username);

		jpql = "SELECT m FROM Member m where m.username = ?1";
		query = em.createQuery(jpql, Group.class);
		query.setParameter(1, username);

		return query.getResultList();
	}
}
