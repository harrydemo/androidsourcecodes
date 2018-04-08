package com.feicong.qqmsglook;

public class QQMsg
{
	private String uin;	
	private String message;	
	
	public QQMsg(String uin, String message)
	{
		super();
		this.uin = uin;
		this.message = message;
	}

	@Override
	public String toString()
	{
		return "QQMsg [uin=" + uin + ", message=" + message + "]";
	}

	public String getUin()
	{
		return uin;
	}
	public void setUin(String uin)
	{
		this.uin = uin;
	}
	public String getMessage()
	{
		return message;
	}
	public void setMessage(String message)
	{
		this.message = message;
	}
}
