package com.ssafy.study.group.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ssafy.study.group.model.dto.GroupDto;
import com.ssafy.study.group.model.entity.Group;

public interface GroupRepository extends JpaRepository<Group, Long>, GroupRepositoryCustom {

	@Transactional
	@Modifying
	void deleteByGpNo(long gpNo);

	@Transactional
	@Modifying
	@Query("update Group gp set gp.gpCurNum = gp.gpCurNum + 1 where gp.gpNo = :gpNo")
	void increaseMemberCnt(@Param("gpNo") long gpNo);

	@Transactional
	@Modifying
	@Query("update Group gp set gp.gpCurNum = gp.gpCurNum - 1 where gp.gpNo = :gpNo")
	void decreaseMemberCnt(@Param("gpNo") long gpNo);

	Group findByGpNo(long gpNo);

}
