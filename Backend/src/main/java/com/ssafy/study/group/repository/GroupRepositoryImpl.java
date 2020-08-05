package com.ssafy.study.group.repository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.ssafy.study.group.model.dto.GroupSearchDto;
import com.ssafy.study.group.model.dto.GroupDto;
import com.ssafy.study.group.model.entity.Group;

@Repository
public class GroupRepositoryImpl /* extends QuerydslRepositorySupport */ implements GroupRepositoryCustom {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<GroupDto> selectAllGroups() {
		String jpql = "SELECT new com.ssafy.study.group.model.dto.GroupDto(gp) " + "FROM Group gp ";

		return em.createQuery(jpql, GroupDto.class).getResultList();
	}

	@Override
	public List<GroupDto> findMyJoinGroup(long userId) {
		String jpql = "SELECT new com.ssafy.study.group.model.dto.GroupDto(gp) " + "FROM Group gp "
				+ "where gp.gpNo in (select gj.gp.gpNo from GroupJoin gj where gj.user.userId = :userId)  ";

		TypedQuery<GroupDto> query = em.createQuery(jpql, GroupDto.class);
		query.setParameter("userId", userId);

		return query.getResultList();
	}

	@Override
	public GroupDto selectByGpNo(long gpNo) {
		String jpql = "SELECT new com.ssafy.study.group.model.dto.GroupDto(gp) " + "FROM Group gp "
				+ "WHERE gp.gpNo = :gpNo ";

		TypedQuery<GroupDto> query = em.createQuery(jpql, GroupDto.class);
		query.setParameter("gpNo", gpNo);

		return query.getSingleResult();
	}

	@Override
	public List<GroupDto> searchGroup(GroupSearchDto search) {
		StringBuilder jpql = new StringBuilder();
		jpql.append("SELECT new com.ssafy.study.group.model.dto.GroupDto(gp) ");
		jpql.append("FROM Group gp ");

		jpql.append(" where 1 = 1 ");

		if (!isEmpty(search.getGpNm()))
			jpql.append(" and gp.gpNm like concat('%', '" + search.getGpNm() + "', '%') ");

//		if (!isEmpty(search.getGpCatLg())) {
//			jpql.append("and gp.gpCat.gpCatLg = '" + search.getGpCatLg() + "' ");
//
//			if (!isEmpty(search.getGpCatMd())) {
//				jpql.append("and gp.gpCat.gpCatMd = '" + search.getGpCatMd() + "' ");
//
//				if (!isEmpty(search.getGpCatSm())) {
//					jpql.append("and gp.gpCat.gpCatSm = '" + search.getGpCatSm() + "' ");
//				}
//			}
//		}
		if (search.getGpTag() != null && search.getGpTag().size() != 0) {
			search.getGpTag().stream().forEach(t -> {
				jpql.append(" and gp.gpTag like concat('%', '").append(t).append("', '%') ");
			});
		}

		if (search.getGpCatNo() != 0)
			jpql.append(" and gp.gpCat.gpCatNo = " + search.getGpCatNo() + " ");

		if (search.getGpStTm() != -1)
			jpql.append(" and gp.gpStTm >= ").append(search.getGpStTm()).append(" ");
		if (search.getGpEndTm() != -1)
			jpql.append("and gp.gpEndTm >= ").append(search.getGpEndTm()).append(" ");

		if (search.isGpPrivate())
			jpql.append(" and gp.gpPublic = ").append(false).append(" ");

		TypedQuery<GroupDto> query = em.createQuery(jpql.toString(), GroupDto.class);

		return query.getResultList();
	}

	@Override
	public boolean isGroupFull(long gpNo) {
		String jpql = "select gp.gpCurNum = gp.gpMaxNum from Group gp where gp.gpNo = :gpNo";
		TypedQuery<Boolean> query = em.createQuery(jpql, Boolean.class);
		query.setParameter("gpNo", gpNo);

		return query.getSingleResult();
	}

	public boolean isEmpty(String str) {
		if (str == null || str == "")
			return true;
		return false;
	}
}
