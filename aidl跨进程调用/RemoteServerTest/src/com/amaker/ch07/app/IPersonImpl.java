package com.amaker.ch07.app;

import com.amaker.ch07.app.IPerson;

import android.os.RemoteException;
/**
 * 
 * @author 郭宏志
 * IPerson接口实现类
 */
public class IPersonImpl extends IPerson.Stub{
	// 声明两个变量
	private int age;
	private String name;
	@Override
	// 显示name和age
	public String display() throws RemoteException {
		return "name:zsh;age=30";
	}
	@Override
	// 设置age
	public void setAge(int age) throws RemoteException {
		this.age = age;
	}
	@Override
	// 设置name
	public void setName(String name) throws RemoteException {
		this.name = name;
	}
}
