package com.shinylife.smalltools;

import java.util.ArrayList;
import java.util.List;

import com.jackrex.R;
import com.shinylife.smalltools.BaseActivity;
import com.shinylife.smalltools.ItemAdapter;
import com.shinylife.smalltools.QueryAddress;
import com.shinylife.smalltools.QueryIDCard;
import com.shinylife.smalltools.QueryPhone;
import com.shinylife.smalltools.entity.NumberItem;
import com.shinylife.smalltools.helper.InternetHelper;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class SmallToolsActivity extends BaseActivity {

	private List<NumberItem> list;
	private ListView number_list;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setView(R.layout.main);
		SetTitle(getString(R.string.app_name));
		setTitleBar(0, "", 0, "");
		InitList();
		findViews();
		ItemAdapter adapter = new ItemAdapter(this, list);
		number_list.setAdapter(adapter);
		setListener();

		if (!new InternetHelper(this).getNetworkIsAvailable()) {
			showToast(getString(R.string.no_internet));
		}

	}

	private void findViews() {
		number_list = (ListView) findViewById(R.id.number_list);
	}

	private void setListener() {
		number_list.setOnItemClickListener(new OnItemClickListener() {

			
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent it = list.get(arg2).getIntent();
				if (it != null) {
					startActivity(it);
				}
			}
		});
	}

	private void InitList() {
		list = new ArrayList<NumberItem>();
		list.add(new NumberItem(R.drawable.number_query,
				getString(R.string.number_mobile_info), new Intent(this,
						QueryPhone.class)));
		list.add(new NumberItem(R.drawable.number_operator,
				getString(R.string.number_id_info), new Intent(this,
						QueryIDCard.class)));
		list.add(new NumberItem(R.drawable.number_ticket,
				getString(R.string.number_add_info), new Intent(this,
						QueryAddress.class)));
	}

	@Override
	protected void HandleTitleBarEvent(int buttonId) {

	}

	

	
	
}