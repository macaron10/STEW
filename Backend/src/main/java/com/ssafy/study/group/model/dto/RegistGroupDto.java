package com.ssafy.study.group.model.dto;

import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import com.ssafy.study.group.model.entity.Group;
import com.ssafy.study.group.model.entity.GroupCategory;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegistGroupDto {
	@ApiModelProperty(required = true)
	@NotNull
	private int gpCatNo;// 타입 아이디
	@ApiModelProperty(required = true)
	@NotNull
	private String gpNm;// 스터디 이름
	private String gpIntro;// 소개
	private int gpStTm;// 선호 시작시간
	private int gpEndTm;// 선호 종료시간

	@ApiModelProperty(required = true)
	@NotNull
	private boolean gpPublic;// 공개여부

	private MultipartFile gpImg;
	
	private String gpTag;// 태그

	public Group toEntity() {
		return Group.builder().gpCat(new GroupCategory(gpCatNo)).gpNm(gpNm).gpIntro(gpIntro).gpStTm(gpStTm)
				.gpEndTm(gpEndTm).gpCurNum(0).gpMaxNum(5).gpPublic(gpPublic).build();
	}
}
