package cn.android.mytaobao.dao.biz;

import cn.android.mytaobao.dao.IUserService;
import cn.android.mytaobao.daompl.UserService;
import cn.android.mytaobao.model.User;

public class UserManager {
	private IUserService dao;

	public UserManager() {
		dao = new UserService();
	}

	/**
	 * 登录，成功返回登录 成功的用户的实体,失败返回null
	 * 
	 * @param userId
	 * @param passWord
	 * @return
	 */
	public User Login(String userId, String passWord) {
		User user = dao.getUserById(userId);
		if (user != null) {
			return user.getPassWord().equals(passWord) ? user : null;
		}

		return null;
	}

	/**
	 * 注册用户,成功返回注册的用户实体，失败返回null;
	 * @param user
	 * @return
	 */
	public User Register(User user) {
		try {
			this.dao.insert(user);
			return user;
		} catch (Exception ex) {
			return null;
		}
	}
}
