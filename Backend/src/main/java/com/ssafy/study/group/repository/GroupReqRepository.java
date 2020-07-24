package com.ssafy.study.group.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.study.group.model.GroupReq;

public interface GroupReqRepository extends JpaRepository<GroupReq, Long>, GroupReqRepositoryCustom {

	public GroupReq findByGpReqNo(long reqNo);

	public void deleteByGpReqNo(long reqNo);
}
