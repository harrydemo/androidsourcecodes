package com.example.dairyclient;

import java.util.ArrayList;

import android.R.integer;
import android.R.raw;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class DairyAdapter extends BaseAdapter {
	
	Context context;
	ArrayList<Dairy> dataList=new ArrayList<Dairy>();
	public DairyAdapter(Context context,ArrayList<Dairy> inputDataList)
	{
		this.context=context;
		dataList.clear();
		for(int i=0;i<inputDataList.size();i++)
		{
			dataList.add(inputDataList.get(i));
		}
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		View v=convertView;
		final Dairy DairyUnit=dataList.get(position);
		if(v==null)
		{
    		LayoutInflater vi=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v=vi.inflate(R.layout.mylist, null);
			v.setClickable(true);
		}
    	TextView dairyNum=(TextView)v.findViewById(R.id.Text02);
    	final TextView dairyTitle=(TextView)v.findViewById(R.id.Text01);
    	TextView dairyText=(TextView)v.findViewById(R.id.Text03);
    	Button editButton=(Button)v.findViewById(R.id.revise);
    	
    	dairyNum.setText(DairyUnit.dairynum);
    	dairyTitle.setText(DairyUnit.dairytitle);
    	int end;
    	if(DairyUnit.dairytext.length()>10)
    	{
    		end=10;		
    	}
    	else
    	{
    		end=DairyUnit.dairytext.length();
    	}
    	dairyText.setText(DairyUnit.dairytext.substring(0, end));
    	
    	editButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent reviseIntent=new Intent(context,EditDairy.class);
				reviseIntent.putExtra("num", DairyUnit.dairynum);
				reviseIntent.putExtra("title",dairyTitle.getText().toString());
				reviseIntent.putExtra("text",DairyUnit.dairytext);
				context.startActivity(reviseIntent);
			}
		});
    	
		return v;
	}
}