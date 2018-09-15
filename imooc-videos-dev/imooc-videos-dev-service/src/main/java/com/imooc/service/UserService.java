package com.imooc.service;

import com.imooc.pojo.Users;
public interface UserService 
{
	//判断用户是否存在
	public boolean queryUsernameIsExist(String username);
	//保存用户名
	public void saveUser(Users user);
	//查询一个用户
	public Users getUser(String user);
	//修改用户信息
	public void updateUserInfo(Users user);
	//查询用户的信息
	public Users queryUserInfo(String userid);
	
}
