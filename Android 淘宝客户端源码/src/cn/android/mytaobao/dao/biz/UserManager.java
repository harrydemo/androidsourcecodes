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
	 * ��¼���ɹ����ص�¼ �ɹ����û���ʵ��,ʧ�ܷ���null
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
	 * ע���û�,�ɹ�����ע����û�ʵ�壬ʧ�ܷ���null;
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
