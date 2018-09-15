package com.imooc.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.imooc.pojo.Videos;
import com.imooc.pojo.VideosVo;
import com.imooc.utils.MyMapper;

public interface VideosVoMapper extends MyMapper<Videos> {
	
	public List<VideosVo> queryAllVideos(@Param("VideoDesc") String  videoDesc); 
	public List<VideosVo> queryAllVideos();

}