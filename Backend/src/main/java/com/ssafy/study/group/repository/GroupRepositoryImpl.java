package com.ssafy.study.group.repository;

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
		String jpql = "SELECT new com.ssafy.study.group.model.dto.GroupDto(gp,  group_concat(gt.gpTagNm) as gpTag)"
				+ "FROM Group gp, GroupTagMap gm, GroupTag gt "
				+ "WHERE gp.gpNo = gm.gp.gpNo AND gm.gpTag.gpTagNo = gt.gpTagNo group by gp.gpNo";

		return em.createQuery(jpql, GroupDto.class).getResultList();
	}

	@Override
	public List<GroupDto> findMyJoinGroup(long userId) {
		String jpql = "SELECT new com.ssafy.study.group.model.dto.GroupDto(gp,  group_concat(gt.gpTagNm) as gpTag) "
				+ "FROM Group gp, GroupTagMap gm, GroupTag gt "
				+ "WHERE gp.gpNo = gm.gp.gpNo AND gm.gpTag.gpTagNo = gt.gpTagNo "
				+ "and gp.gpNo in (select gj.gp.gpNo from GroupJoin gj where gj.user.userId = :userId)  "
				+ "group by gp.gpNo";

		TypedQuery<GroupDto> query = em.createQuery(jpql, GroupDto.class);
		query.setParameter("userId", userId);

		return query.getResultList();
	}

	@Override
	public GroupDto selectByGpNo(long gpNo) {
		String jpql = "SELECT new com.ssafy.study.group.model.dto.GroupDto(gp,  group_concat(gt.gpTagNm) as gpTag) "
				+ "FROM Group gp, GroupTagMap gm, GroupTag gt "
				+ "WHERE gp.gpNo = gm.gp.gpNo AND gm.gpTag.gpTagNo = gt.gpTagNo "
				+ "and gp.gpNo = :gpNo "
				+ "group by gp.gpNo";

		TypedQuery<GroupDto> query = em.createQuery(jpql, GroupDto.class);
		query.setParameter("gpNo", gpNo);

		return query.getSingleResult();
	}

	@Override
	public List<GroupDto> searchGroup(GroupSearchDto search) {
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
			jpql += "and g.gpPublic = " + false + " ";

		TypedQuery<Group> query = em.createQuery(jpql, Group.class);

		return query.getResultStream().map(g -> new GroupDto(g)).collect(Collectors.toList());
	}

	public boolean isEmpty(String str) {
		if (str == null || str == "")
			return true;
		return false;
	}

	@Override
	public boolean isGroupFull(long gpNo) {
		String jpql = "select gp.gpCurNum = gp.gpMaxNum from Group gp where gp.gpNo = :gpNo";
		TypedQuery<Boolean> query = em.createQuery(jpql, Boolean.class);
		query.setParameter("gpNo", gpNo);

		return query.getSingleResult();
	}

}
