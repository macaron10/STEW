package com.ssafy.study.group.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.study.group.model.Group;
import com.ssafy.study.group.model.GroupCategory;

public interface GroupCategoryRepository extends JpaRepository<GroupCategory, Long>, GroupCategoryRepositoryCustom {

	public GroupCategory findByGpCatNo(int catNo);
}
