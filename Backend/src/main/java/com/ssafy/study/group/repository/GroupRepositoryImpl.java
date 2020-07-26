package com.ssafy.study.group.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.ssafy.study.group.model.Group;
import com.ssafy.study.group.model.GroupSearch;

@Repository
public class GroupRepositoryImpl /* extends QuerydslRepositorySupport */ implements GroupRepositoryCustom {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<Group> findMyJoinGroup(long userId) {
		String jpql = "select g from Group g where g.gpNo in (select gj.gpNo from GroupJoin gj where gj.userId = :userId)";
		TypedQuery<Group> query = em.createQuery(jpql, Group.class);
		query.setParameter("userId", userId);

		return query.getResultList();
	}

	@Override
	public List<Group> searchGroup(GroupSearch search) {
		String jpql = "select g from Group g where 1 = 1 ";

		if (!isEmpty(search.getGpNm()))
			jpql += "and g.gpNm like concat('%', '" + search.getGpNm() + "', '%') ";

		if (!isEmpty(search.getGpCatLg())) {
			jpql += "and g.gpCat.gpCatLg = '" + search.getGpCatLg() + "' ";

			if (!isEmpty(search.getGpCatMd())) {
				jpql += "and g.gpCat.gpCatMd = '" + search.getGpCatMd() + "' ";

				if (!isEmpty(search.getGpCatSm())) {
					jpql += "and g.gpCat.gpCatSm = '" + search.getGpCatSm() + "' ";
				}
			}
		}

		if (search.getGpStTm() != -1)
			jpql += "and g.gpStTm >= " + search.getGpStTm() + " ";
		if (search.getGpEndTm() != -1)
			jpql += "and g.gpEndTm >= " + search.getGpEndTm() + " ";

		if (search.isGpPrivate())
			jpql += "and g.gpPublic = " + false;

		TypedQuery<Group> query = em.createQuery(jpql, Group.class);

		return query.getResultList();
	}

	public boolean isEmpty(String str) {
		if (str == null || str == "")
			return true;
		return false;
	}

}
