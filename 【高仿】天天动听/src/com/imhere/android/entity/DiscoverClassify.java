package com.imhere.android.entity;

import java.util.List;

public class DiscoverClassify {

	String itemName;

	List<DiscoverBriefUnit> gridViewItems;


	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public List<DiscoverBriefUnit> getGridViewItems() {
		return gridViewItems;
	}

	public void setGridViewItems(List<DiscoverBriefUnit> gridViewItems) {
		this.gridViewItems = gridViewItems;
	}

}
