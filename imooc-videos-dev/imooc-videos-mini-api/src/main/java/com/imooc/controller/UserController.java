package com.imooc.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.immoc.vo.UsersVo;
import com.imooc.pojo.Users;
import com.imooc.service.UserService;
import com.imooc.utils.IMoocJSONResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import sun.rmi.rmic.iiop.ImplementationType;

@RestController
@Api(value = "用户业务接口 ", tags = { "头像上传" })
@RequestMapping("/user")
/**
 * 
 * @author 2016wlw2 徐塬峰 创建时间：2018年6月11日 下午4:14:49
 */
public class UserController extends BasicController {
	@Autowired
	private UserService userService;

	/**
	 * logout 登出
	 * 
	 * @param user
	 * @return
	 */
	@ApiOperation(value = "用户上传头像", notes = "用户上传头像接口")
	@ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String", paramType = "query")
	@PostMapping("/uploadFace")
	public IMoocJSONResult uploadFace(String userId, @RequestParam("file") MultipartFile[] files) {

		
		if(StringUtils.isBlank(userId))
		{
			return IMoocJSONResult.errorException("用户id不能为空...");
		}
		
		// 文件保存命名空间
//		String fileSpace = "G://imooc_videos_dev";
		// 保存到数据库路径 相对路径
		String uploadPathDB = "/" + userId + "/face";

		if (files != null && files.length > 0) {

			FileOutputStream fileOutputStream = null;
			InputStream inputStream = null;
			try {
				String fileName = files[0].getOriginalFilename();
				if (StringUtils.isNotBlank(fileName)) {
					// 文件上传的最终保存路径
					String finalFacePath = FILe_SPACE + "/" + uploadPathDB + "/" + fileName;
					// 数据库保存路径
					uploadPathDB += ("/" + fileName);

					File outFile = new File(finalFacePath);
					//
					if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
						// 创建父亲文件夹
						outFile.getParentFile().mkdirs();
					}

					fileOutputStream = new FileOutputStream(outFile);
					inputStream = files[0].getInputStream();

					IOUtils.copy(inputStream, fileOutputStream);
					 //调用接口
					
				}
				else
				{//增加校验防止入侵攻击
					return IMoocJSONResult.errorException("上传失败了");
					
				}
			} catch (IOException e) {
				e.printStackTrace();
				return IMoocJSONResult.errorException("上传失败了");
			}
			finally
			{
                //如果不为空
				//则flush 关闭
				try {
					fileOutputStream.flush();
					IOUtils.closeQuietly(fileOutputStream);
					IOUtils.closeQuietly(inputStream);

				} catch (IOException e) {
					
					e.printStackTrace();
				}

				
				
			}
		}
		
		Users user=new Users();
		user.setId(userId);
		user.setFaceImage(uploadPathDB);
		userService.updateUserInfo(user);
		System.out.println(user.toString());
		return IMoocJSONResult.ok(user);
      
	}
	/**
	 * 根据userid去查询用户相关的信息
	 * @param userId
	 * @return
	 */
	@ApiOperation(value = "查询信息接口", notes = "查询用户信息接口")
	@ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String", paramType = "query")
	@PostMapping("/query")
	public IMoocJSONResult uploadFace(String userId)
	{
		if(StringUtils.isBlank(userId))
		{
	       return IMoocJSONResult.errorMsg("用户id不能为空");
		}
	   Users userInfo=userService.queryUserInfo(userId);
	   UsersVo userVO=new UsersVo();
	   BeanUtils.copyProperties(userInfo, userVO);
       return IMoocJSONResult.ok(userVO);		
	}
	

}
