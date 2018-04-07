package com.feicong.qqmsglook;

public class QQFriend
{
	private String memberLevel;
	private String name;
	private String qquin;
	
	public QQFriend(String memberLevel, String name, String qquin)
	{
		super();
		this.memberLevel = memberLevel;
		this.name = name;
		this.qquin = qquin;
	}

	@Override
	public String toString()
	{
		return "QQFriend [memberLevel=" + memberLevel + ", name=" + name
				+ ", qquin=" + qquin + "]";
	}

	public String getMemberLevel()
	{
		return memberLevel;
	}
	public void setMemberLevel(String memberLevel)
	{
		this.memberLevel = memberLevel;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getqqUin()
	{
		return qquin;
	}
	public void setqqUin(String qquin)
	{
		this.qquin = qquin;
	}	
}
