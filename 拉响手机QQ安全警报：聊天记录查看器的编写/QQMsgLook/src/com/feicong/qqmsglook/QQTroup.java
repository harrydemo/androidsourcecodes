package com.feicong.qqmsglook;

public class QQTroup
{	
	public QQTroup(String name, String uin, String memo)
	{
		super();
		this.troopName = name;
		this.troopUin = uin;
		this.troopMemo = memo;
	}
	
	@Override
	public String toString()
	{
		return "QQTroup [memo=" + troopMemo + ", name=" + troopName + ", uin=" + troopUin
				+ ", getMemo()=" + getMemo() + ", getName()=" + getName()
				+ ", getUin()=" + getUin() + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}
	private String troopMemo;
	private String troopName;
	private String troopUin;	
	
	public String getMemo()
	{
		return troopMemo;
	}
	public void setMemo(String memo)
	{
		this.troopMemo = memo;
	}
	public String getName()
	{
		return troopName;
	}
	public void setName(String name)
	{
		this.troopName = name;
	}
	public String getUin()
	{
		return troopUin;
	}
	public void setUin(String uin)
	{
		this.troopUin = uin;
	}	
}
