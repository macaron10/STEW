package com.ssafy.study.group.model.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ssafy.study.group.model.entity.Group;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Valid
@NoArgsConstructor
public class ModifyGroupDto {
	private long gpNo; // 스터디 아이디

	@ApiModelProperty(required = true)
	@NotNull
	private int gpCatNo;// 타입 아이디
	private String gpNm;// 스터디 이름
	private String gpIntro;// 소개
	private int gpStTm;// 선호 시작시간
	private int gpEndTm;// 선호 종료시간

	@ApiModelProperty(required = true)
	@NotNull
	private boolean gpPublic;// 공개여부

	private MultipartFile gpImg;

	@JsonIgnore
	private String gpImgPath;
	@ApiModelProperty(required = true)
	@NotNull
	private boolean updateGpImg;
	
	private List<String> gpTag;

	public Group toEntity() {
		return Group.builder().gpNo(gpNo).gpNm(gpNm).gpIntro(gpIntro).gpStTm(gpStTm).gpImg(gpImgPath).gpEndTm(gpEndTm)
				.gpPublic(gpPublic).gpTag(String.join(",", gpTag)).build();
	}
}
