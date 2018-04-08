package com.parabola.service.impl;

import java.util.List;

import com.parabola.entity.User;
 
/** 
 * @作者 song shi chao 
 * @QQ   421271944
 * @创建日期 2011-8-22 
 * @版本 V 1.0 
 */

public interface IUserServcie {
	  public void add(User user);
	    public boolean exists(User user);
	    public User loadById(int id);
	    public void delete(int id);
	    public void update(User user);
	    public List<User> getUsers(int currentPage,int page);
	    public Integer getTotalRecord();
}
