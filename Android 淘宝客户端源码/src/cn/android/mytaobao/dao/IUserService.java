package cn.android.mytaobao.dao;

import cn.android.mytaobao.model.User;

public interface IUserService {
  public void insert(User user);
  public User getUserById(String userId);
}
