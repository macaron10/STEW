package com.ssafy.study.group.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ssafy.study.group.model.entity.GroupReq;

import io.lettuce.core.dynamic.annotation.Param;

public interface GroupReqRepository extends JpaRepository<GroupReq, Long>, GroupReqRepositoryCustom {

	public GroupReq findByGpReqNo(long reqNo);

	@Transactional
	@Modifying
	public void deleteByGpReqNo(long reqNo);
	
	@Transactional
	@Modifying
	@Query("delete from GroupReq gr where gr.gp.gpNo = :gpNo")
	public void deleteByGpNo(@Param("gpNo") long gpNo);
}
