package com.ssafy.study.group.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ssafy.study.group.model.dto.GroupReqDto;

import io.lettuce.core.dynamic.annotation.Param;

public interface GroupReqRepositoryCustom {

	List<GroupReqDto> findAllMgrsGpReq(long userId);

	List<GroupReqDto> findMgrsGpReq(long gpNo);
}
