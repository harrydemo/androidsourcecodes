package com.parabola.dao.impl;

import java.util.List;

import com.parabola.entity.User;
 
/** 
 * @���� song shi chao 
 * @QQ   421271944
 * @�������� 2011-8-22 
 * @�汾 V 1.0 
 */

public interface IUserDao {
	public void save(User user);
    public boolean checkUserExistsWithName(String username);
    public User LoadById(int id);
    public void deleteById(int id);
    public void updateById(User user);
    public List<User> getUsers(int currentPage,int page);
    public Integer getTotalRecord();
}
