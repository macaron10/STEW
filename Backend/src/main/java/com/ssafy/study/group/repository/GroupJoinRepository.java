package com.ssafy.study.group.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ssafy.study.group.model.GroupJoin;

import io.lettuce.core.dynamic.annotation.Param;

public interface GroupJoinRepository extends JpaRepository<GroupJoin, Long>, GroupJoinRepositoryCustom {

	public GroupJoin findByGpJoinNo(long joinNo);

	@Transactional
	@Modifying
	public void deleteByGpJoinNo(long joinNo);

	@Transactional
	@Modifying
	@Query("delete from GroupJoin gj where gj.gp.gpNo = :gpNo and gj.user.userId = :userId")
	public void deleteByGpNoAndUserId(@Param("gpNo") long gpNo, @Param("userId") long userId);

	@Transactional
	@Modifying
	@Query("delete from GroupJoin gj where gj.gp.gpNo = :gpNo")
	public void deleteByGpNo(@Param("gpNo") long gpNo);
}
