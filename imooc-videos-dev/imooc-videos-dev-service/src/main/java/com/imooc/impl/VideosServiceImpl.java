package com.imooc.impl;

import java.util.List;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.mapper.SearchRecordsMapper;
import com.imooc.mapper.VideosMapper;
import com.imooc.mapper.VideosVoMapper;
import com.imooc.pojo.PageResult;
import com.imooc.pojo.SearchRecords;
import com.imooc.pojo.Videos;
import com.imooc.pojo.VideosVo;
import com.imooc.service.VideoService;

/**
 * 
 * @author 2016wlw2 徐塬峰 创建时间：2018年7月6日 下午3:57:02
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS)

public class VideosServiceImpl implements VideoService {
	@Autowired
	private Sid sid;// 生成唯一主键
	@Autowired
	private VideosMapper videosMapper;
	@Autowired
	private VideosVoMapper videosVoMapper;
	@Autowired
	private SearchRecordsMapper searchRecordsMapper;
   
	
	
	
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public String saveVideo(Videos video) {

		String id = sid.nextShort();
		video.setId(id);
		videosMapper.insertSelective(video);
		return id;

	}

	/**
	 * 修改視頻的封面
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateVideo(String id, String coverPath) {
		Videos video = new Videos();
		video.setId(id);
		video.setCoverPath(coverPath);
		videosMapper.updateByPrimaryKeySelective(video);
	}

	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public PageResult getAllVideos(Videos videos,Integer isSaveRecord,Integer page, Integer pageSize) {
 
		//保存热搜词
		String desc=videos.getVideoDesc();
		if(isSaveRecord!=null&&isSaveRecord==1)
		{
			SearchRecords record=new SearchRecords();
			record.setContent(desc);
			String recordId=sid.nextShort();
			record.setId(recordId);
			searchRecordsMapper.insert(record);
		}
		//对查询进行优化
		PageHelper.startPage(page, pageSize);
		List<VideosVo> list = videosVoMapper.queryAllVideos(desc);
		PageInfo<VideosVo> pageList = new PageInfo<>(list);
		PageResult pageResult = new PageResult();
		pageResult.setPage(page);
		pageResult.setTotal(pageList.getPages());
		pageResult.setRows(list);
		pageResult.setRecords(pageList.getTotal());

		return pageResult;
	}

	@Override
	public PageResult getAllVideos(Integer page, Integer pageSize) {
		//对查询进行优化
		PageHelper.startPage(page, pageSize);
		List<VideosVo> list = videosVoMapper.queryAllVideos();
		PageInfo<VideosVo> pageList = new PageInfo<>(list);
		PageResult pageResult = new PageResult();
		pageResult.setPage(page);
		pageResult.setTotal(pageList.getPages());
		pageResult.setRows(list);
		pageResult.setRecords(pageList.getTotal());
		return pageResult;
	}

}
