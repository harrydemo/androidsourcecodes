package com.geolo.android.list;

import java.util.ArrayList;
import java.util.List;

import com.geolo.android.R;


import android.R.color;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class myListView extends Activity implements OnItemClickListener, OnItemLongClickListener{
	ListView mListView = null;
	List<MyItem> list = null;
	MyItemAdapter myAdapter  = null;
	View m_selectView = null;
	PopupWindow pw = null;
	int m_seclctViewPosi = 0;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_list);
		mListView = (ListView) findViewById(R.id.users);
		list = new ArrayList<MyItem>();
		initData(list);
		myAdapter = new MyItemAdapter(this, list);
		myAdapter.notifyDataSetChanged();
		mListView.setAdapter(myAdapter);
		//        mListView.setItemsCanFocus(true);
		//        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		mListView.setOnItemClickListener(this);
		mListView.setOnItemLongClickListener(this);
		mListView.setOnCreateContextMenuListener(this);     
	}
	public void initData(List<MyItem> list)
	{
		list.add(new MyItem(0,"name","10","abbadajdsljasldjasljd",R.drawable.icon,R.layout.user_item_list));
		list.add(new MyItem(1,"name2","10","abbadajdsljasldjasljd",R.drawable.icon,R.layout.user_item_list));
		list.add(new MyItem(2,"name3","10","abbadajdsljasldjasljd",R.drawable.icon,R.layout.user_item_list));
		list.add(new MyItem(3,"name4","10","abbadajdsljasldjasljd",R.drawable.icon,R.layout.user_item_list));
		list.add(new MyItem(4,"name5","10","abbadajdsljasldjasljd",R.drawable.icon,R.layout.user_item_list));
	}
	int y = 0;
	public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
		LayoutInflater factory = LayoutInflater.from(myListView.this);
		final View textEntryView = factory.inflate(R.layout.paopao_alert_dialog, null);
		Object obj = getSystemService(Context.WINDOW_SERVICE);   
		WindowManager wm = (WindowManager) obj;   
		int envWidth = wm.getDefaultDisplay().getWidth();   
		int envHeight = wm.getDefaultDisplay().getHeight();  
		if(pw == null)
		{
			pw = new PopupWindow(textEntryView); 
			pw.showAtLocation(v, Gravity.LEFT,0,0); 
		}else if(!pw.isShowing()){
			pw = new PopupWindow(textEntryView); 
			pw.showAtLocation(v, Gravity.LEFT,0,0); 
		}
		int pwH = 70;
		int pwW = 166;
		y = -envHeight/2+v.getTop() + pwH;
		pw.update(0,y,pwW,pwH); 



		textEntryView.findViewById(R.id.phone).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pw.dismiss();
			}

		});


		//		      new AlertDialog.Builder(myListView.this)
		////          .setIcon(R.drawable.alert_dialog_icon)
		////          .setTitle("试验一下")
		//          .setView(textEntryView)
		//          .show();


		//		mListView.setBackgroundColor(Color.TRANSPARENT);
		//		setTitleColor(Color.RED);
		//		setTitle("你选择的是"+position+"条"+list.get(position).getAge());
		////		v.setBackgroundColor(0x77172177);
		//		   v.setBackgroundResource(R.drawable.list_item_bg_over);
		//		if(m_selectView != null)
		//		{
		////			m_selectView.setBackgroundColor(Color.TRANSPARENT);
		//			m_selectView.setBackgroundResource(R.drawable.green);
		//		}
		//		m_selectView = v;
		//		m_seclctViewPosi = position;
		//		ImageView mImageView = (ImageView) v.findViewById(R.id.img);
		//		mImageView.setImageResource(R.drawable.p01);
		//		list.get(position+1).setIcon(R.drawable.p02);

		//		View view = myAdapter.getView(position-1, null, null);
		//		view.setBackgroundColor(Color.TRANSPARENT);
		//		v.setBackgroundColor(0xff000000);
	}

	OnClickListener btnOphoneClick = new OnClickListener()
	{
		public void onClick(View v) {
			new AlertDialog.Builder(myListView.this)
			.setMessage("v.getId()="+v.getId()+"R.id.phone="+R.id.phone)
			.show();


			//			if(v.getId() == R.id.phone)
			//			{
			//				if(pw.isShowing()) pw.dismiss();
			//			}
		}
	};



	public boolean onItemLongClick(AdapterView parent, View view, int position,long id) {
		Toast t = Toast.makeText(this, "你选择的是"+position+"条",  Toast.LENGTH_SHORT);
		t.show();
		//			  Log.e("CallLogActivity", view.toString() + "position=" + position);
		//			  CharSequence number = ((TextView) view).getText();
		//			  Toast t = Toast.makeText(this, number + " is long clicked",
		//			    Toast.LENGTH_LONG);
		//			  t.show();
		return true;
	}
	//	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
	//			  // TODO Auto-generated method stub
	//			  menu.add(0, 1, 0,list.get(m_seclctViewPosi).getName());
	//			  menu.add(0, 2, 0, list.get(m_seclctViewPosi).getName());
	//			 }
	//	 public boolean onContextItemSelected(MenuItem item) {
	//		 return super.onContextItemSelected(item);
	//	 }
	public boolean onCreateOptionsMenu(Menu menu)
	{
		//	    	System.out.println("********************create menu");
		//	    	menu.add(0, 0, 0, R.string.appp_abount);
		//	    	menu.add(0, 0, 0,"选择1");
		//	    	menu.add(0, 0, 0,"选择2");
		//	    	menu.add(0, 0, 0,"选择3");
		//	    	menu.add(0, 1, 1, R.string.app_exit);
		//	    	return super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.menu1, menu);  
		return super.onCreateOptionsMenu(menu);
	}
	public boolean onOptionsItemSelected(MenuItem item)
	{
		super.onOptionsItemSelected(item);
		Toast t = Toast.makeText(this, "你选择的是"+item.getTitle(),  Toast.LENGTH_SHORT);
		t.show();
		return true;
	}
}


