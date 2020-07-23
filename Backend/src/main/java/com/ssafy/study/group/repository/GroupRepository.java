package com.ssafy.study.group.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.study.group.model.Group;

public interface GroupRepository extends JpaRepository<Group, Long>, GroupRepositoryCustom {

	List<Group> findByGpMgrId(String userId);

	Optional<Group> findByGpNo(long gpNo);
}
