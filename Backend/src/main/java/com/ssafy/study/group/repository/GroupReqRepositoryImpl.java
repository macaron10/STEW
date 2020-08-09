package com.ssafy.study.group.repository;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.ssafy.study.group.model.dto.GroupReqDto;
import com.ssafy.study.group.model.entity.GroupReq;

public class GroupReqRepositoryImpl implements GroupReqRepositoryCustom {

	@PersistenceContext
	private EntityManager em;

//	@Override
//	public void deleteByGpNo(long gpNo) {
//		String jpql = "delete from GroupReq gr where gr.gp.gpNo = :gpNo";
//		Query query = em.createQuery(jpql);
//
//		query.setParameter("gpNo", gpNo);
//		query.getSingleResult();
//	}

	@Override
	public List<GroupReqDto> findAllMgrsGpReq(long userId) {
		String jpql = "select gr from GroupReq gr where gr.gp.gpNo in (select gp.gpNo from Group gp where gp.gpMgrId = :userId)";
		TypedQuery<GroupReq> query = em.createQuery(jpql, GroupReq.class);

		query.setParameter("userId", userId);

		return query.getResultStream().map(r -> new GroupReqDto(r)).collect(Collectors.toList());
	}

	@Override
	public List<GroupReqDto> findUsersReq(long userId) {
		String jpql = "select gr from GroupReq gr where gr.gp.gpNo in (select gj.gp.gpNo from GroupJoin gj where gj.user.userId = :userId)";
		TypedQuery<GroupReq> query = em.createQuery(jpql, GroupReq.class);

		query.setParameter("userId", userId);

		return query.getResultStream().map(r -> new GroupReqDto(r)).collect(Collectors.toList());
	}

	@Override
	public GroupReqDto findGpReqByReqNo(long reqNo) {
		String jpql = "select new com.ssafy.study.group.model.dto.GroupReqDto(gr, gp, u) from GroupReq gr join Group gp on gr.gp.gpNo = gp.gpNo join User u on gr.user.userId = u.userId where gr.gpReqNo = :reqNo";
		TypedQuery<GroupReqDto> query = em.createQuery(jpql, GroupReqDto.class);

		query.setParameter("reqNo", reqNo);

		return query.getSingleResult();
	}

	@Override
	public List<GroupReqDto> findMgrsGpReq(long gpNo) {
		String jpql = "select gr from GroupReq gr where gr.gp.gpNo in (select gp.gpNo from Group gp where gp.gpNo = :gpNo)";
		TypedQuery<GroupReq> query = em.createQuery(jpql, GroupReq.class);

		query.setParameter("gpNo", gpNo);

		return query.getResultStream().map(r -> new GroupReqDto(r)).collect(Collectors.toList());
	}

	@Override
	public boolean ckGroupReq(long gpNo, long userId) {
		String jpql = "select count(gr) from GroupReq gr where gr.gp.gpNo = :gpNo and gr.user.userId = :userId";
		TypedQuery<Long> query = em.createQuery(jpql, Long.class);

		query.setParameter("gpNo", gpNo);
		query.setParameter("userId", userId);

		return query.getSingleResult() > 0 ? true : false;
	}

}
