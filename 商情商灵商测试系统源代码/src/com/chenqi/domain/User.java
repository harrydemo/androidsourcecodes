package com.chenqi.domain;


public class User {
	
	public User(String username, String pwd, String sex, Integer age, String telephone)
	{

		this.username = username;
		this.pwd = pwd;
		this.sex = sex;
		this.age = age;
		this.telephone = telephone;
	}
	private Integer userid;
	private String username;
	private String pwd;
	private String sex;
	private Integer age;
	private String telephone;
	public Integer getUserid()
	{
		return userid;
	}
	public void setUserid(Integer userid)
	{
		this.userid = userid;
	}
	public String getUsername()
	{
		return username;
	}
	public void setUsername(String username)
	{
		this.username = username;
	}
	public String getPwd()
	{
		return pwd;
	}
	public void setPwd(String pwd)
	{
		this.pwd = pwd;
	}
	public String getSex()
	{
		return sex;
	}
	public void setSex(String sex)
	{
		this.sex = sex;
	}
	public Integer getAge()
	{
		return age;
	}
	public void setAge(Integer age)
	{
		this.age = age;
	}
	public String getTelephone()
	{
		return telephone;
	}
	public void setTelephone(String telephone)
	{
		this.telephone = telephone;
	}
	@Override
	public String toString()
	{
		return "User [age=" + age + ", pwd=" + pwd + ", sex=" + sex + ", telephone=" + telephone + ", userid=" + userid + ", username=" + username + "]";
	}
	
		
}
