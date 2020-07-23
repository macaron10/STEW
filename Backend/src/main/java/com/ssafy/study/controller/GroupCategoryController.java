package com.ssafy.study.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.study.group.service.GroupCategoryService;

@RestController
public class GroupCategoryController {

	@Autowired
	private GroupCategoryService categoryService;
	
	
	
}
