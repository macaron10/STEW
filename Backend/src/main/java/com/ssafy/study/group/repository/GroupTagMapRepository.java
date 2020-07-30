package com.ssafy.study.group.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.study.group.model.GroupTagMap;

public interface GroupTagMapRepository extends JpaRepository<GroupTagMap, Long>, GroupTagMapRepositoryCustom {

}
