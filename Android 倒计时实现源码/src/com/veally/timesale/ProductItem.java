 /*
  * Copyright (C) 2012 The TimeSale Project
  * All right reserved.
  * Version 1.00 2012-11-25
  * Author veally@foxmail.com
  */
package com.veally.timesale;

/**
 * Product entity
 * @author veally@foxmail.com
 */
public class ProductItem {

	private long id;
	private String imageUrl;
	private long remainTime;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public long getRemainTime() {
		return remainTime;
	}
	public void setRemainTime(long remainTime) {
		this.remainTime = remainTime;
	}
	
}
