package cn.itcast.domain;

public class Contact {
	private Integer id;
	private String name;
	private String mobile;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Contact(Integer id, String name, String mobile) {
		this.id = id;
		this.name = name;
		this.mobile = mobile;
	}
	public Contact(){}
}
