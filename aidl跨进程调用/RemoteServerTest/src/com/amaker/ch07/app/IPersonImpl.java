package com.amaker.ch07.app;

import com.amaker.ch07.app.IPerson;

import android.os.RemoteException;
/**
 * 
 * @author ����־
 * IPerson�ӿ�ʵ����
 */
public class IPersonImpl extends IPerson.Stub{
	// ������������
	private int age;
	private String name;
	@Override
	// ��ʾname��age
	public String display() throws RemoteException {
		return "name:zsh;age=30";
	}
	@Override
	// ����age
	public void setAge(int age) throws RemoteException {
		this.age = age;
	}
	@Override
	// ����name
	public void setName(String name) throws RemoteException {
		this.name = name;
	}
}
