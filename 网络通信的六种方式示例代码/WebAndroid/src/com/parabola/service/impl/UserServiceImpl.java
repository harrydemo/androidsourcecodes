package com.parabola.service.impl;

import java.util.List;

import com.parabola.dao.impl.IUserDao;
import com.parabola.entity.User;

/** 
 * @���� song shi chao 
 * @QQ   421271944
 * @�������� 2011-8-22 
 * @�汾 V 1.0 
 */

public class UserServiceImpl implements IUserServcie {
	private IUserDao userDao;
	
	public IUserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(IUserDao userDao) {
		this.userDao = userDao;
	}

	/**
	 * ����û�
	 */
	public void add(User user) {
		userDao.save(user);
	}
 
	public void delete(int id) {
	 
	}

	 
	public boolean exists(User user) {
		 
		return false;
	}

 
	public Integer getTotalRecord() {
		 
		return null;
	}

	 
	public List<User> getUsers(int currentPage, int page) {
	 
		return null;
	}

	 
	public User loadById(int id) {
		 
		return null;
	}

	 
	public void update(User user) {
		 
		
	}

}
