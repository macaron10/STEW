package com.ssafy.study.group.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.study.group.model.entity.GroupCategory;

public interface GroupCategoryRepository extends JpaRepository<GroupCategory, Long>, GroupCategoryRepositoryCustom {

	public GroupCategory findByGpCatNo(int catNo);
}
