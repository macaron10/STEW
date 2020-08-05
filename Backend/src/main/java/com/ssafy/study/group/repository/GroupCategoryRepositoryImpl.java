package com.ssafy.study.group.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.ssafy.study.group.model.entity.GroupCategory;

@Repository
public class GroupCategoryRepositoryImpl implements GroupCategoryRepositoryCustom {

	@PersistenceContext
	private EntityManager em;

//	@Override
//	public List<GroupCategory> selectBoxLgGroupCategory() {
//		String jpql = "select cat from GroupCategory cat where cat.gpCatMd = null";
//		TypedQuery<GroupCategory> query = em.createQuery(jpql, GroupCategory.class);
//
//		return query.getResultList();
//	}
//
//	@Override
//	public List<GroupCategory> selectBoxMdGroupCategory(String lg) {
//		String jpql = "select cat from GroupCategory cat where cat.gpCatLg = :lg and cat.gpCatSm = null";
//
//		TypedQuery<GroupCategory> query = em.createQuery(jpql, GroupCategory.class);
//		query.setParameter("lg", lg);
//		return query.getResultList();
//	}
//
//	@Override
//	public List<GroupCategory> selectBoxSmGroupCategory(String lg, String md) {
//		String jpql = "select cat from GroupCategory cat where cat.gpCatLg = :lg and cat.gpCatMd = :md";
//
//		TypedQuery<GroupCategory> query = em.createQuery(jpql, GroupCategory.class);
//		query.setParameter("lg", lg);
//		query.setParameter("md", md);
//		return query.getResultList();
//	}

}
