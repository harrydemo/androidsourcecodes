package wzhg.dao;

import wzhg.entity.User;

public interface UserDao {
	public User login(String username,String password);
}
