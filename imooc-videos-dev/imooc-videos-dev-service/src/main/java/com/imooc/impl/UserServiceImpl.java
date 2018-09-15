package com.imooc.impl;

import java.util.ArrayList;
import java.util.List;

import org.n3r.idworker.Sid;
import org.omg.CORBA.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.mapper.UsersMapper;
import com.imooc.pojo.Users;
import com.imooc.service.UserService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
@Service
public class UserServiceImpl implements UserService {
@Autowired
private UsersMapper userMapper;
@Autowired
private Sid sid;
@Transactional(propagation = Propagation.SUPPORTS)	
	/**
	 * 判断用户是否存在
	 * Boolean
	 */
	@Override
	public boolean queryUsernameIsExist(String username) {
		Users user=new Users();
		user.setUsername(username);
		Users result=userMapper.selectOne(user);
		return result==null?false:true;
	}
    /**
     * 保存账号
     * 插入数据库
     */
    @Transactional(propagation=Propagation.REQUIRED)//创建一个事务
	@Override
	public void saveUser(Users user) {
		 String userId = sid.nextShort();
         user.setId(userId);
         userMapper.insert(user);
	}
	@Transactional(propagation = Propagation.SUPPORTS)	
	@Override
	public Users getUser(String username) {
		
		 Users user=new Users();
		 user.setUsername(username);
		 Users result=userMapper.selectOne(user);
         return result==null?null:result;
	
	}
	@Override
	public void updateUserInfo(Users user) {

		Example userExample=new Example(Users.class);
		Criteria criteria = userExample.createCriteria();
		criteria.andEqualTo("id",user.getId());
		userMapper.updateByExampleSelective(user, userExample);//字段中不为null也更新
		
		
	}
	@Override
	public Users queryUserInfo(String userId) {
		
		Example userExample=new Example(Users.class);
		Criteria criteria = userExample.createCriteria();
		criteria.andEqualTo("id",userId);
 		Users user=userMapper.selectOneByExample(userExample);
		return user;
	}

}
