package com.milifan.contact;

import android.os.Bundle;

import com.milifan.R;
import com.milifan.contact.base.BaseContactList;

public class OutgoingContactList extends BaseContactList {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_layout);
		setListAdapter("type=2 and DURATION!=0", null);
	}
}
