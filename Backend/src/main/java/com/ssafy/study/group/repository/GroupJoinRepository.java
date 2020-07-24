package com.ssafy.study.group.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.study.group.model.GroupJoin;

public interface GroupJoinRepository extends JpaRepository<GroupJoin, Long>, GroupJoinRepositoryCustom {

	public GroupJoin findByGpJoinNo(long joinNo);

	public void deleteByGpJoinNo(long joinNo);
}
