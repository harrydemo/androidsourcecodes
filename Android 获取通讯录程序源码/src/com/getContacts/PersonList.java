package com.getContacts;

import java.util.List;

import com.getContacts.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PersonList extends BaseAdapter{
	private List<Person> list;
	private Context context;
	public PersonList(Context context,List<Person> list) {
		this.context=context;
		this.list=list;
	}
	public int getCount()//implements android.widget.Adapter.getCount 
	{
		return list.size();
	}

	public Object getItem(int position)//implements android.widget.Adapter.getItem
	{
		return position;
	}

	public long getItemId(int position)//implements android.widget.Adapter.getItemId
	{
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent)//implements android.widget.Adapter.getView
	{//Get a View that displays the data at the specified position in the data set. 
		LayoutInflater inflater=LayoutInflater.from(context);
		LinearLayout layout=(LinearLayout) inflater.inflate(R.layout.main, null);
		Person person=list.get(position);
		TextView textName=(TextView) layout.findViewById(R.id.textName);
		textName.setText(person.getName());
		TextView textPhone=(TextView) layout.findViewById(R.id.textPhone);
		for(String phone:person.getPhone()){
			textPhone.setText(textPhone.getText()+phone+",");
    	}
		TextView textEmail=(TextView) layout.findViewById(R.id.textEmail);
		textEmail.setText(person.getEmail());
		TextView textAddress=(TextView) layout.findViewById(R.id.textAddress);
		textAddress.setText(person.getAddress()==null?"":person.getAddress());
		return layout;
	}
}
