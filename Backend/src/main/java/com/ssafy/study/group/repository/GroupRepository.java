package com.ssafy.study.group.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.study.group.model.Group;

public interface GroupRepository extends JpaRepository<Group, Long> {
	

}
