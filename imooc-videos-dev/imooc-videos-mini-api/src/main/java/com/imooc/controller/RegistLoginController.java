package com.imooc.controller;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.immoc.vo.UsersVo;
import com.imooc.pojo.Users;
import com.imooc.service.UserService;
import com.imooc.utils.IMoocJSONResult;
import com.imooc.utils.MD5Utils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "用户注册的接口 ", tags = { "注册和登陆的controller" })
public class RegistLoginController extends BasicController {
	@Autowired
	private UserService userService;

	/**
	 * http://localhost:8080/swagger-ui.html 用户登陆接口
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "登陆", notes = "用户注册的接口")
	@PostMapping("/login")
	public IMoocJSONResult login(@RequestBody Users user) throws Exception {
		// 假设登陆的人特别多
		Thread.sleep(1000);
		// 1.先判断传入的用户的账号密码是否为空
		if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())) {
			return IMoocJSONResult.errorMsg("小主,账号密码不能为空哦");
		}
		// 判断该账号是否在数据库里 如果存在 则继续校验密码
		if (userService.queryUsernameIsExist(user.getUsername())) {

			Users userSource = userService.getUser(user.getUsername());
			// System.out.println(userSource.toString());
			// 校验数据库的账号密码是否正确
			if (userSource.getPassword().equalsIgnoreCase(MD5Utils.getMD5Str(user.getPassword()))) {
				user.setPassword("");
				UsersVo userVo = setUserRedisSessionToken(userSource);
				// System.out.println(userVo.toString());
				return IMoocJSONResult.ok(userVo);
			} else {
				return IMoocJSONResult.errorMsg("小主,你的账号密码错误");
			}

		} else {
			return IMoocJSONResult.errorMsg("小主,你的账号密码错误");

		}

	}

	/**
	 * logout
	 * 
	 * @param user
	 * @return
	 * @throws InterruptedException
	 */
	@ApiOperation(value = "用户注销", notes = "用户注销接口")
	@ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String", paramType = "query")
	@PostMapping("/logout")
	public IMoocJSONResult logout(String userId) throws InterruptedException {
		//

		// System.out.println("注销操作");
		redis.del(User_REDIS_SESSION + ":" + userId);// redis删除对应的id
		Thread.sleep(1000);
		return IMoocJSONResult.ok();
	}

	/**
	 * 用户注册接口
	 * 
	 * @param user
	 * @return
	 */
	@ApiOperation(value = "注册", notes = "用户注册的接口")
	@ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String", paramType = "query")
	@PostMapping("/regist")
	public IMoocJSONResult regist(@RequestBody Users user) {
		// 1.用户和密码是否为空 不能为空
		if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())) {
			return IMoocJSONResult.errorMsg("小主,用户名和密码不能为空哦");
		}
		// 2.用户名是否存在数据库里 ，如果存在 则抛出
		boolean usernameIsExist = userService.queryUsernameIsExist(user.getUsername());
		// 3.保存用户，注册信息
		if (!usernameIsExist) {
			try {
				user.setNickname(user.getUsername());
				user.setPassword(MD5Utils.getMD5Str(user.getPassword()));
				user.setReceiveLikeCounts(0);
				user.setFansCounts(0);
				user.setFollowCounts(0);
				user.setFaceImage(null);
				userService.saveUser(user);// 调用插入数据库

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			return IMoocJSONResult.errorMsg("用户已经存在,换一下吧 (●'◡'●)");
		}
		user.setPassword("");
		String token = UUID.randomUUID().toString();
		redis.set(User_REDIS_SESSION + ":" + user.getId(), token, 60 * 1000 * 30);
		UsersVo userVo = new UsersVo();
		BeanUtils.copyProperties(user, userVo);
		userVo.setUserToken(token);
		return IMoocJSONResult.ok(userVo);
	}

	public UsersVo setUserRedisSessionToken(Users userModel) {
		String token = UUID.randomUUID().toString();
		redis.set(User_REDIS_SESSION + ":" + userModel.getId(), token, 60 * 1000 * 30);
		UsersVo userVo = new UsersVo();
		BeanUtils.copyProperties(userModel, userVo);
		userVo.setUserToken(token);
		return userVo;
	}

}
