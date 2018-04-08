package com.smart.domain;

public class Person {

	@Override
	public String toString() {
		
		return "personid="+personid+",name="+name+",age="+age;
	}

	public int personid;
	public String name;
	public Short age;

	public int getPersonid() {
		return personid;
	}

	public void setPersonid(int personid) {
		this.personid = personid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// ����һ��������
	public Person(int personid, String name, Short age) {
		super();
		this.personid = personid;
		this.name = name;
		this.age = age;
	}
	//����������
	public Person(String name, short age) {
		this.name = name;
		this.age = age;
	
	}

	public Short getAge() {
		return age;
	}

	public void setAge(Short age) {
		this.age = age;
	}

}
