
package com.imooc.service;

import java.util.List;

import com.imooc.pojo.PageResult;
import com.imooc.pojo.Videos;

/**
 * 
 * @author 2016wlw2 徐塬峰 创建时间：2018年7月6日 下午3:53:21
 */
public interface VideoService {

	/**
	 * 保存
	 * 
	 * @return
	 */
	public String saveVideo(Videos video);

	/**
	 * 更新
	 * 
	 * @return
	 */
	public void updateVideo(String id,String CoverPath);

    public PageResult getAllVideos( Videos video,Integer isSaveRecord,Integer page,Integer pageSize);
    
    public PageResult getAllVideos(Integer page,Integer pageSize);

}
