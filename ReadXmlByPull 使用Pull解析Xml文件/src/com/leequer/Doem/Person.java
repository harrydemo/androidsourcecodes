package com.leequer.Doem;
/**
 * xml文件对应的bean
 * 2010-04-26
 * @author leequer
 *
 */
public class Person {
	private String id ;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	private String name ;
	private String age;
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.id+",name  "+this.name+"  age  "+this.age+"\n";
	}
	
}
