package com.parabola.dao.impl;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.parabola.entity.User;

/** 
 * @作者 song shi chao 
 * @QQ   421271944
 * @创建日期 2011-8-22 
 * @版本 V 1.0 
 */

public class UserDaoImpl extends HibernateDaoSupport implements IUserDao {

	/* (non-Javadoc)
	 * @see com.parabola.dao.impl.IUserDao#LoadById(int)
	 */
	public User LoadById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.parabola.dao.impl.IUserDao#checkUserExistsWithName(java.lang.String)
	 */
	public boolean checkUserExistsWithName(String username) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.parabola.dao.impl.IUserDao#deleteById(int)
	 */
	public void deleteById(int id) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.parabola.dao.impl.IUserDao#getTotalRecord()
	 */
	public Integer getTotalRecord() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.parabola.dao.impl.IUserDao#getUsers(int, int)
	 */
	public List<User> getUsers(int currentPage, int page) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 添加用户
	 */
	public void save(User user) {
		this.getHibernateTemplate().save(user);
	}

	/* (non-Javadoc)
	 * @see com.parabola.dao.impl.IUserDao#updateById(com.parabola.entity.User)
	 */
	public void updateById(User user) {
		// TODO Auto-generated method stub
		
	}

}
