package com.sly.android.huangcun.ui;

import java.io.Serializable;
import java.security.Identity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.youmi.android.AdManager;
import net.youmi.android.AdView;
import com.sly.android.huangcun.entrey.ListViewItems;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MyListAct extends ListActivity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*文件列表*/
		protected void onCreate(Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
			AdManager.init(MyListAct.this,"0f34f539f9d030b4","612c3c56a1fab5c1",50,false); 
			String[]options=new String[]{"bookico","bookname"};
			int[]ico=new int[]{R.id.imageico,R.id.textappname};
			List<Map<String,Object>> items=new ArrayList<Map<String,Object>>();
			for(int i=0;i<ListViewItems.READ_NAME.length;i++){
				Map<String,Object>item=new HashMap<String,Object>();
				item.put("bookico", ListViewItems.READ_ICO[i]);
				item.put("bookname", ListViewItems.READ_NAME[i]);
				items.add(item);
			}
			SimpleAdapter adapter=new SimpleAdapter(this,items,R.layout.listview,options,ico);
			setListAdapter(adapter);
			AdView adView = new AdView(this); 
	         
			FrameLayout.LayoutParams params = new 
			FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, 
			FrameLayout.LayoutParams.WRAP_CONTENT); 
			params.gravity=Gravity.BOTTOM|Gravity.RIGHT;  
			//Activity  
			addContentView(adView, params);  
	}
		@Override
		protected void onListItemClick(ListView l, View v, int position, long id) {
			super.onListItemClick(l, v, position, id);
			Intent intent = new Intent();
			intent.putExtra("id", position);
			Log.i("传送id的值",id+"");
			System.out.println("小说名字==========="+ListViewItems.READ_NAME[position]);
			intent.setClass(MyListAct.this, StaringAct.class);
			startActivity(intent);
		}
		
}
