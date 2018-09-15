package com.imooc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imooc.service.BgmService;
import com.imooc.utils.IMoocJSONResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/bgm")
@Api(value = "背景音乐", tags = { "背景音乐业务controller" })

public class BgmController {
	
	@Autowired
	private BgmService bgmService;
	

	
	@ApiOperation(value = "列表", notes = "获取背景音乐列表")
	@PostMapping("/list")
	public IMoocJSONResult Hello() {
		
//测试代码		System.out.println(bgmService.queryBgmList().toString());
		return IMoocJSONResult.ok(bgmService.queryBgmList());
	}
	
}
