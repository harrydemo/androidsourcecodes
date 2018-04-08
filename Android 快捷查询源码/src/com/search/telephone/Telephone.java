package com.search.telephone;

import java.io.Serializable;

/**
 * 封装查询结果信息
 * 为了在activity之间传递，要实现Serializable接口
 * @author Administrator
 *
 */
public class Telephone implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String mobile; //手机号码
	
	private String queryResult; //true或者false
	
	private String province; //省
	
	private String city; //市
	
	private String areaCode; //区号
	
	private String postCode; //邮编
	
	private String corp; //
	
	private String card; //卡类型

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getQueryResult() {
		return queryResult;
	}

	public void setQueryResult(String queryResult) {
		this.queryResult = queryResult;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getCorp() {
		return corp;
	}

	public void setCorp(String corp) {
		this.corp = corp;
	}

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}
	
	

}
