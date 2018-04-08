package com.shinylife.smalltools.entity;

import android.content.Intent;

public class NumberItem {
	private int ICOId;
	private String name;
	private Intent intent;

	public NumberItem() {

	}

	public NumberItem(int iCOId, String name, Intent intent) {
		this.ICOId = iCOId;
		this.name = name;
		this.intent = intent;
	}

	public int getICOId() {
		return ICOId;
	}

	public String getName() {
		return name;
	}

	public Intent getIntent() {
		return intent;
	}

	public void setICOId(int iCOId) {
		ICOId = iCOId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setIntent(Intent intent) {
		this.intent = intent;
	}
}
