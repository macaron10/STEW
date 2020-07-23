package com.ssafy.study.group.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.ssafy.study.group.model.GroupCategory;

@Repository
public class GroupCategoryRepositoryImpl implements GroupCategoryRepositoryCustom {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<GroupCategory> selectBoxGroupCategory(int idx, int catNo) {
		String select = "select distinct gp_cat_no";
		String where = "from gp_cat where ";
		select += "gp_cat_lg";
		if (idx <= 2) {
			select += ", gp_cat_md";
		}
		if (idx <= 3) {
			select += ", gp_cat_sm";
		}

		TypedQuery<GroupCategory> query = em.createQuery(select + where, GroupCategory.class);
		return query.getResultList();
	}

}
