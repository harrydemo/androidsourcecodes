package cn.android.mytaobao.daompl;

import java.util.HashMap;
import java.util.Map;

import cn.android.mytaobao.dao.IUserService;
import cn.android.mytaobao.model.User;

public class UserService implements IUserService{

	private Map<String,User> users = null;
	public UserService(){
		users = new HashMap<String, User>();
		User user = new User("admin","admin");
		this.insert(user);
	}
	@Override
	public void insert(User user) {
	   users.put(user.getUserId(), user)	;
	}

	@Override
	public User getUserById(String userId) {
		return users.get(userId);
	}

}
