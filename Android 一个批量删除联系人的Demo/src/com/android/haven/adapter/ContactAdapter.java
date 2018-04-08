package com.android.haven.adapter;

import java.util.List;

import com.android.haven.contact.ContactManagerActvity;
import com.android.haven.contact.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

/**
 * @auther hujh
 * @version 2010-10-27 ÏÂÎç09:51:38
 */

public class ContactAdapter extends BaseAdapter {

	private List<ContactEntity> list;
	private ContactManagerActvity activity;
	
	public ContactAdapter(ContactManagerActvity activity,List<ContactEntity> list){
		this.activity = activity;
		this.list = list;
	}
	
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater li = LayoutInflater.from(activity);
		View view = li.inflate(R.layout.contact_row, null);
		((TextView)view.findViewById(R.id.name)).setText(list.get(position).getName());
		((TextView)view.findViewById(R.id.num)).setText(list.get(position).getPhoneNum());
		final CheckBox checkBox = (CheckBox)view.findViewById(R.id.check);
		checkBox.setChecked(list.get(position).isChecked());
		checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				list.get(position).setChecked(isChecked);
			}
		});
		checkBox.setFocusable(false);
		return view;
	}

}
