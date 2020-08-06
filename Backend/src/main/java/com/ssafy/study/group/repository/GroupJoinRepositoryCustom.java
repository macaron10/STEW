package com.ssafy.study.group.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ssafy.study.group.model.dto.GroupJoinDto;

import io.lettuce.core.dynamic.annotation.Param;

public interface GroupJoinRepositoryCustom {

	public boolean ckGroupJoin(long gpNo, long userId);

	List<GroupJoinDto> findGpJoinMemeber(long gpNo);



//	@Transactional
//	@Modifying
//	@Query("delete from GroupJoin gj where gj.gp.gpNo = :gpNo")
//	public void deleteByGpNo(@Param("gpNo") long gpNo);
}
