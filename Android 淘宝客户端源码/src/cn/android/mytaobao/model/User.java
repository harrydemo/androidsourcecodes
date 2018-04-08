package cn.android.mytaobao.model;

public class User {
	private String userId;
	private String passWord;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	/**
	 * @param userId
	 * @param passWord
	 */
	public User(String userId, String passWord) {
		super();
		this.userId = userId;
		this.passWord = passWord;
	}
	
	public User(){}
}
