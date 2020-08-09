package com.ssafy.study.timeAcml.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.study.timeAcml.model.dto.TimeAcmlDto;
import com.ssafy.study.timeAcml.model.entity.TimeAcml;

public interface TimeAcmlRepository extends JpaRepository<TimeAcml, Long>, TimeAcmlRepositoryCustom {

	
}
