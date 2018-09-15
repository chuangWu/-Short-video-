package com.imooc.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.imooc.pojo.Bgm;
import com.imooc.pojo.PageResult;
import com.imooc.pojo.VideoStatusEnum;
import com.imooc.pojo.Videos;
import com.imooc.service.BgmService;
import com.imooc.service.VideoService;
import com.imooc.utils.FetchVideo;
import com.imooc.utils.IMoocJSONResult;
import com.imooc.utils.MergeVideo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 
 * @author 2016wlw2 徐塬峰 创建时间：2018年6月28日 下午12:07:38
 */
@RestController
@Api(value = "视频相关的业务数据接口", tags = { "视频上传" })
@RequestMapping("/video")
public class VideoController extends BasicController {

	@Autowired
	private BgmService bgmService;
	@Autowired
	private VideoService videoService;

	@ApiOperation(value = "用户上传视频", notes = "用户上传视频接口")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String", paramType = "form"),
			@ApiImplicitParam(name = "bgmId", value = "背景音乐id", required = false, dataType = "String", paramType = "form"), //// 可以不指定
			@ApiImplicitParam(name = "videoSeconds", value = "视频时间", required = true, dataType = "double", paramType = "form"), // 参数类型为form
			@ApiImplicitParam(name = "videoWidth", value = "视频宽度", required = true, dataType = "int", paramType = "form"),
			@ApiImplicitParam(name = "videoHeight", value = "视频高度", required = true, dataType = "int", paramType = "form"),
			@ApiImplicitParam(name = "desc", value = "视频描述", required = false, dataType = "String", paramType = "query")// 可以不指定

	})
	@PostMapping(value = "/upload", headers = "content-type=multipart/form-data")
	public IMoocJSONResult uploadFace(String userId, String bgmId, double duration, int tmpWidth, int tmpHeight,
			String desc, @ApiParam(value = "短视频", required = true) MultipartFile file) throws IOException {

		if (StringUtils.isBlank(userId)) {
			return IMoocJSONResult.errorException("用户id不能为空...");
		}
		// 文件保存命名空间

		// 保存到数据库路径 相对路径
		String uploadPathDB = "/" + userId + "/video";
		String coverPath = "/" + userId + "/video";

		String finalVideoPath = "";
		if (file != null) {

			FileOutputStream fileOutputStream = null;
			InputStream inputStream = null;
			try {
				String fileName = file.getOriginalFilename();
				String fileNamePrefix = fileName.split("\\.")[0];

				if (StringUtils.isNotBlank(fileName)) {
					// 文件上传的最终保存路径
					finalVideoPath = FILe_SPACE + "/" + uploadPathDB + "/" + fileName;
					// 数据库保存路径
					uploadPathDB += ("/" + fileName);
					coverPath = coverPath + "/" + fileNamePrefix + ".jpg";// 相对路径放到数据库里

					File outFile = new File(finalVideoPath);
					//
					if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
						// 创建父亲文件夹
						outFile.getParentFile().mkdirs();
					}

					fileOutputStream = new FileOutputStream(outFile);
					inputStream = file.getInputStream();

					IOUtils.copy(inputStream, fileOutputStream);
					// 调用接口

				} else {// 增加校验防止入侵攻击
					return IMoocJSONResult.errorException("上传失败了");

				}
			} catch (IOException e) {
				e.printStackTrace();
				return IMoocJSONResult.errorException("上传失败了");
			} finally {
				// 如果不为空
				// 则flush 关闭
				try {
					fileOutputStream.flush();
					IOUtils.closeQuietly(fileOutputStream);
					IOUtils.closeQuietly(inputStream);

				} catch (IOException e) {
					e.printStackTrace();
				}
				// 判断bgmid是否为空，如果不为空 则需要查询信息，并且合并视频生成新的视频
				if (StringUtils.isNotBlank(bgmId)) {
					Bgm bgm = bgmService.queryBgmById(bgmId);
					System.out.println(bgm.getId());
					String mp3InputPath = FILe_SPACE + bgm.getPath();// 得到路径
																		// 并与上传的视频相整合
					// ffmpeg所在地址
					// String
					// ffmpegPath="G:\\ffmepg视频处理方案\\ffmpeg-20180704-3b10bb8-win64-static\\bin\\ffmpeg.exe";
					MergeVideo tool = new MergeVideo(FFMPEGEXE);
					String videoInputPath = finalVideoPath;

					String outPathName = UUID.randomUUID().toString() + ".mp4";
					uploadPathDB = "/" + userId + "/video" + "/" + outPathName;
					String outputStream = FILe_SPACE + "/" + uploadPathDB;

					tool.convertor(mp3InputPath, videoInputPath, duration, outputStream);

				}
			}
		}

		// ffmpeg.convertor(finalVideoPath,FILe_SPACE+coverPath);
		System.out.println("上传成功");
		FetchVideo videoInfo=new FetchVideo(FFMPEGEXE);
		videoInfo.getcover(finalVideoPath, FILe_SPACE+coverPath);
		
		
		// 保存视频到数据库
		Videos video = new Videos();
		video.setAudioId(bgmId);
		video.setUserId(userId);
		video.setVideoSeconds((float) duration);
		video.setVideoHeight(tmpHeight);
		video.setVideoWidth(tmpWidth);
		video.setVideoDesc(desc);
		video.setCreateTime(new Date());
		video.setVideoPath(uploadPathDB);
		video.setCoverPath(coverPath);
		video.setStatus(VideoStatusEnum.Success.value);
		String videoId = videoService.saveVideo(video);
		System.out.println("上传数据库成功" + videoId);
		return IMoocJSONResult.ok(videoId);// 返回200

	}

	@ApiOperation(value="上传封面", notes="上传封面的接口")
	@ApiImplicitParams({
		@ApiImplicitParam(name="userId", value="用户id", required=true, 
				dataType="String", paramType="form"),
		@ApiImplicitParam(name="videoId", value="视频主键id", required=true, 
				dataType="String", paramType="form")
	})
	@PostMapping(value="/uploadCover", headers="content-type=multipart/form-data")
	public IMoocJSONResult uploadCover(String userId,
				String videoId,
				@ApiParam(value="视频封面", required=true)
				MultipartFile file) throws Exception {
		
		if (StringUtils.isBlank(videoId) || StringUtils.isBlank(userId)) {
			return IMoocJSONResult.errorMsg("视频主键id和用户id不能为空...");
		}
		
		// 文件保存的命名空间
//		String fileSpace = "C:/imooc_videos_dev";
		// 保存到数据库中的相对路径
		String uploadPathDB = "/" + userId + "/video";
		
		FileOutputStream fileOutputStream = null;
		InputStream inputStream = null;
		// 文件上传的最终保存路径
		String finalCoverPath = "";
		try {
			if (file != null) {
				
				String fileName = file.getOriginalFilename();
				if (StringUtils.isNotBlank(fileName)) {
					
					finalCoverPath = FILe_SPACE + uploadPathDB + "/" + fileName;
					// 设置数据库保存的路径
					uploadPathDB += ("/" + fileName);
					
					File outFile = new File(finalCoverPath);
					if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
						// 创建父文件夹
						outFile.getParentFile().mkdirs();
					}
					
					fileOutputStream = new FileOutputStream(outFile);
					inputStream = file.getInputStream();
					IOUtils.copy(inputStream, fileOutputStream);
				}
				
			} else {
				return IMoocJSONResult.errorMsg("上传出错...");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return IMoocJSONResult.errorMsg("上传出错...");
		} finally {
			if (fileOutputStream != null) {
				fileOutputStream.flush();
				fileOutputStream.close();
			}
		}
		
		videoService.updateVideo(videoId, uploadPathDB);
		
		return IMoocJSONResult.ok();
	}
	
	/**
	 * 分页和搜索查询列表
	 * issave=1 需要保存
	 * issave=0 不需要保存  或者为空的时候
	 * 
	 * @param video
	 * @param isSaveRecord
	 * @param page
	 * @return
	 */
	/*
	@PostMapping(value = "/showAll2")//isSaveRecord 是否保存记录
	public IMoocJSONResult showAll2(@RequestBody Videos video,Integer isSaveRecord,Integer page) 
	{
		if(page==null)
		{
			page=1;
		}
		PageResult pageResult=videoService.getAllVideos(video,isSaveRecord,page, PAGE_SIZE);
		return IMoocJSONResult.ok(pageResult);
	}*/
	
	@PostMapping(value = "/showAll")//isSaveRecord 是否保存记录
	public IMoocJSONResult showAll(Integer page) 
	{
		if(page==null)
		{
			page=1;
		}
		PageResult pageResult=videoService.getAllVideos(page, PAGE_SIZE);
		return IMoocJSONResult.ok(pageResult);
	}
	
	
	
	/*
	@PostMapping(value = "/hot")
	public IMoocJSONResult hot(@RequestBody Videos video,Integer isSaveRecord,Integer page) 
	{
		return IMoocJSONResult.ok();
	}*/
}
