package com.ssafy.study.group.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.ssafy.study.group.model.dto.GroupDto;
import com.ssafy.study.group.model.dto.GroupSearchDto;

@Repository
public class GroupRepositoryImpl /* extends QuerydslRepositorySupport */ implements GroupRepositoryCustom {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<GroupDto> selectAllGroups() {
		String jpql = "SELECT new com.ssafy.study.group.model.dto.GroupDto(gp, gc.gpCatNm) "
				+ "FROM Group gp join GroupCategory gc on gp.gpCat.gpCatNo = gc.gpCatNo";

		return em.createQuery(jpql, GroupDto.class).getResultList();
	}

	@Override
	public List<GroupDto> findMyJoinGroup(long userId) {
		String jpql = "SELECT new com.ssafy.study.group.model.dto.GroupDto(gp, gc.gpCatNm) "
				+ "FROM Group gp join GroupCategory gc on gp.gpCat.gpCatNo = gc.gpCatNo "
				+ "where gp.gpNo in (select gj.gp.gpNo from GroupJoin gj where gj.user.userId = :userId)  ";

		TypedQuery<GroupDto> query = em.createQuery(jpql, GroupDto.class);
		query.setParameter("userId", userId);

		return query.getResultList();
	}

	@Override
	public GroupDto selectByGpNo(long gpNo) {
		String jpql = "SELECT new com.ssafy.study.group.model.dto.GroupDto(gp, gc.gpCatNm) "
				+ "FROM Group gp join GroupCategory gc on gp.gpCat.gpCatNo = gc.gpCatNo " + "WHERE gp.gpNo = :gpNo ";

		TypedQuery<GroupDto> query = em.createQuery(jpql, GroupDto.class);
		query.setParameter("gpNo", gpNo);

		return query.getSingleResult();
	}

	@Override
	public List<GroupDto> searchGroup(GroupSearchDto search) {
		StringBuilder jpql = new StringBuilder();
		jpql.append("SELECT new com.ssafy.study.group.model.dto.GroupDto(gp, gc.gpCatNm) ");
		jpql.append("FROM Group gp join GroupCategory gc on gp.gpCat.gpCatNo = gc.gpCatNo ");
		jpql.append(" where 1 = 1 ");

		if (search.getGpNm().size() > 0)
			search.getGpNm().stream().forEach(nm -> jpql.append(" and gp.gpNm like concat('%', '" + nm + "', '%') "));

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
		if (search.getGpTag().size() > 0) {
			search.getGpTag().stream().forEach(t -> {
				jpql.append(" and gp.gpTag like concat('%', '").append(t).append("', '%') ");
			});
		}

		if (search.getGpCatNo() != -1)
			jpql.append(" and gp.gpCat.gpCatNo = " + search.getGpCatNo() + " ");

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
	
	@Override
	public List<GroupDto> rankGroupStudyTime(){
		String jpql="select new com.ssafy.study.group.model.dto.GroupDto(gp, sum(tm.tmAcmlTime) as sumTime, sum(tm.tmAcmlTime) / gp.gpCurNum as avgTime) "
				+ "from Group gp join TimeAcml tm on gp.gpNo = tm.gp.gpNo "
				+ "where substring(tm.tmAcmlDate, 1, 7) = substring(CURRENT_DATE, 1, 7) "
				+ "and 2 <> 0 "
				+ "group by gp.gpNo "
				+ "order by avgTime desc, gp.gpCurNum desc ";
		
		TypedQuery<GroupDto> query = em.createQuery(jpql, GroupDto.class);

		query.setMaxResults(5);
		return query.getResultList();
	}

	public boolean isEmpty(String str) {
		if (str == null || str == "")
			return true;
		return false;
	}
}
