package com.example.demo.model;

import java.io.Serializable;

public class Navigation implements Serializable {
	public final static int TYPE_0 = 0;
	public final static int TYPE_1 = 1;
	public final static int TYPE_2 = 2;
	public final static int TYPE_3 = 3;
	public final static int TYPE_4 = 4;
	public final static int TYPE_5 = 5;
	private int type;
	private String url;
	private String title;

	public Navigation(int type, String url, String title) {
		this.type = type;
		this.url = url;
		this.title = title;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "Navigation [type=" + type + ", url=" + url + ", title=" + title
				+ "]";
	}

}
