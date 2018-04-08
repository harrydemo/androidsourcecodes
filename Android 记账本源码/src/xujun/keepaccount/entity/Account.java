/*
 * 系统名称：
 * 模块名称：
 * 描述：
 * 作者：徐骏
 * version 1.0
 * time  2010-11-12 下午03:08:08
 * copyright Anymusic Ltd.
 */
package xujun.keepaccount.entity;

import java.util.Date;



/**
 * @author 徐骏
 * @data   2010-11-12
 */
public class Account
{
	private int accountId;
	private AccountEnum type;
	private float money;
	private String date;
	private boolean selected;
	
	public int getAccountId()
	{
		return accountId;
	}
	public void setAccountId(int accountId)
	{
		this.accountId = accountId;
	}
	public AccountEnum getType()
	{
		return type;
	}
	public void setType(AccountEnum type)
	{
		this.type = type;
	}
	public float getMoney()
	{
		return money;
	}
	public void setMoney(float money)
	{
		this.money = money;
	}
	public String getDate()
	{
		return date;
	}
	public void setDate(String date)
	{
		this.date = date;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
