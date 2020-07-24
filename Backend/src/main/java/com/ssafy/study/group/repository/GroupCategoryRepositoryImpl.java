package com.ssafy.study.group.repository;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.ssafy.study.group.model.GroupCategory;

@Repository
public class GroupCategoryRepositoryImpl implements GroupCategoryRepositoryCustom {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<GroupCategory> selectBoxLgGroupCategory() {
		String jpql = "select distinct cat.gpCatLg from GroupCategory cat";
		Query query = em.createQuery(jpql);

		query.getResultList().stream().map(s -> new GroupCategory((String) s)).forEach(s -> System.out.println(s));
		return null;
	}

	@Override
	public List<GroupCategory> selectBoxMdGroupCategory(String lg) {
		String jpql = "select distinct gp_cat_lg, gp_cat_md from gp_cat where gp_cat_lg = :lg";

		TypedQuery<GroupCategory> query = em.createQuery(jpql, GroupCategory.class);
		query.setParameter("lg", lg);
		return query.getResultList();
	}

	@Override
	public List<GroupCategory> selectBoxSmGroupCategory(String lg, String md) {
		String jpql = "select distinct gp_cat_lg, gp_cat_md from gp_cat where gp_cat_lg = :lg and gp_cat_md = :md";

		TypedQuery<GroupCategory> query = em.createQuery(jpql, GroupCategory.class);
		query.setParameter("lg", lg);
		query.setParameter("md", md);
		return query.getResultList();
	}

}
