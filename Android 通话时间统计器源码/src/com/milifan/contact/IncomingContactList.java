package com.milifan.contact;

import android.os.Bundle;
import android.widget.TextView;

import com.milifan.R;
import com.milifan.contact.base.BaseContactList;

public class IncomingContactList extends BaseContactList {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_layout);
		setListAdapter("type=1  and DURATION!=0", null);
		//TextView total=(TextView) this.findViewById(R.id.total);
		//total.setText("jhkl");
	}
}
