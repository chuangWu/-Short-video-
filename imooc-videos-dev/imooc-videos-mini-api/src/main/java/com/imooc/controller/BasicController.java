package com.imooc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.imooc.utils.RedisOperator;
/**
 * 
 * @author 2016wlw2 徐塬峰
 * 创建时间：2018年6月11日 下午4:03:26
 */
@RestController
public class BasicController {
    @Autowired
	public RedisOperator redis;
    
    public static final Integer PAGE_SIZE=5;
    public static final String User_REDIS_SESSION="user-redis-session";
    public static final String FILe_SPACE="C://imooc_videos_dev";//上传目录
	public static final String FFMPEGEXE="C:\\ffmepg视频处理方案\\ffmpeg-20180704-3b10bb8-win64-static\\bin\\ffmpeg.exe";
	
}
