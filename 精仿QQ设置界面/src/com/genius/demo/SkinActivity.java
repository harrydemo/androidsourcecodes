package com.genius.demo;

import java.util.ArrayList;
import java.util.List;

import com.genius.demo.adapter.SkinListAdapter;
import com.genius.demo.adapter.SkinListItemData;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class SkinActivity extends Activity{
	
	private final int skinIconID[] = {R.drawable.skinpic_blue,
										R.drawable.skinpic_green,
										R.drawable.skinpic_pink,
										R.drawable.skinpic_snow};
	
	private final String text[] = {"蓝水静溢", "绿雾晨光", "粉色花语", "银装素裹"};
	
	private final int uncheckIcon = R.drawable.themeradio;
	
	private ListView mListView;
	
	private SkinListAdapter mSkinListAdapter;
	
	private TextView mTitleTextView;
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Load the preferences from an XML resource
        setContentView(R.layout.skin_main_layout);
        
        init();
    }
	
	
	
	private void init()
	{
		mListView = (ListView) findViewById(R.id.themelist);
		
		mSkinListAdapter = new SkinListAdapter(this, getItemList());
		
		mListView.setAdapter(mSkinListAdapter);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				mSkinListAdapter.setSelect(position);
				mTitleTextView.setBackgroundResource(skinIconID[position]);
			}
		});
		

		mTitleTextView = (TextView) findViewById(R.id.skinTitle);
	
		
	}
	
	
	private List<SkinListItemData> getItemList()
	{
		List<SkinListItemData> list = new ArrayList<SkinListItemData>();
		
		for(int i = 0; i < 4; i++)
		{
			SkinListItemData data = new SkinListItemData();
			data.mImageViewLeftID = skinIconID[i];
			data.mTextView = text[i];
			data.mImageViewRightID = uncheckIcon;
			list.add(data);
		}
		
		
		return list;
	}
}
