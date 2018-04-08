package com.np.data;

public class Info {
	private String mobile;
	private boolean queryResult = false;
	private String province;
	private String city;
	private String areaCode;
	private String postCode;
	private String corp;
	private String card;
	
	public String getMobile(){
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public boolean getQueryResult() {
		return queryResult;
	}
	public void setQueryResult(String queryResult) {
		if(queryResult.equals("true") || queryResult.equals("True"))
			this.queryResult = true;
		else
			this.queryResult = false;
	}
	public String getProvince(){
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
	
	
	public String[] getInfo(){
		String str = mobile+"&"+province+"&"+city+"&"+areaCode+"&"+postCode+"&"+corp+"&"+card;
//		return str;
		return str.split("&");
	}
}