package com.ssafy.study.group.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.study.group.model.entity.GroupTag;

public interface GroupTagRepository extends JpaRepository<GroupTag, Long>, GroupTagRepositoryCustom {

}
