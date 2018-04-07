package com.myandroid.test;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class MyMenu extends Activity {
	private List<String> titles;	//标题栏
	private List<List<String>> item_names;	//选项名称
	private List<List<Integer>> item_images;	//选项图标
	private MyDefinedMenu myMenu;	//弹出菜单
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //弹出菜单标题栏
        titles = addItems(new String[]{"菜单一", "菜单二", "菜单三"});
        //选项图标
        item_images = new ArrayList<List<Integer>>();
        item_images.add(addItems(new Integer[]{R.drawable.bag,
        	R.drawable.bluetooth, R.drawable.earth, R.drawable.email}));
        item_images.add(addItems(new Integer[]{R.drawable.map,
            	R.drawable.news, R.drawable.reader, R.drawable.sound, R.drawable.tape}));
        item_images.add( addItems(new Integer[]{R.drawable.telephone,
            	R.drawable.bluetooth, R.drawable.earth, R.drawable.email}));
        //选项名称
        item_names = new ArrayList<List<String>>();
        item_names.add(addItems(new String[]{"购物", "蓝牙", "游览器", "邮件"}));
        item_names.add(addItems(new String[]{"地图", "新闻", "阅读器", "音箱", "录音"}));
        item_names.add(addItems(new String[]{"电话", "蓝牙", "阅读器", "邮箱"}));
        //创建弹出菜单对象
		myMenu = new MyDefinedMenu(this, titles, item_names, 
				item_images, new ItemClickEvent());
        
    }
    
    /**
     * 转换为List<String>
     * @param values
     * @return
     */
    private List<String> addItems(String[] values) {
    	
    	List<String> list = new ArrayList<String>();
    	for (String var : values) {
			list.add(var);
		}
    	
    	return list;
    }
    
    /**
     * 转换为List<Integer>
     * @param values
     * @return
     */
    private List<Integer> addItems(Integer[] values) {
    	
    	List<Integer> list = new ArrayList<Integer>();
    	for (Integer var : values) {
			list.add(var);
		}
    	
    	return list;
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		menu.add("menu");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		
		if(0 == myMenu.currentState && myMenu.isShowing()) {	
			Log.e("isShowing:", "" + myMenu.isShowing());
			myMenu.dismiss();			//对话框消失
			myMenu.currentState = 1;	//标记状态，已消失
			Log.e("MenuState:", "dismissing");
		} else {
			myMenu.showAtLocation(findViewById(R.id.layout), Gravity.BOTTOM, 0,0);
			Log.e("isShowing:", "" + myMenu.isShowing()); 
			myMenu.currentState = 0;	//标记状态，显示中
			Log.e("MenuState:", "showing");
		}
		
		return false;	// true--显示系统自带菜单；false--不显示。
	}
	
	@Override
	public void closeOptionsMenu() {
		// TODO Auto-generated method stub
		super.closeOptionsMenu();
		Log.e("MenuState:", "closeOptionsMenu");
	}

	@Override
	public void onOptionsMenuClosed(Menu menu) {
		// TODO Auto-generated method stub
		super.onOptionsMenuClosed(menu);
		Log.e("MenuState:", "onOptionsMenuClosed");
	}


	/**
	 * 菜单选项点击事件
	 * @author Kobi
	 *
	 */
	class ItemClickEvent implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			//显示点击的是哪个菜单哪个选项。
			Toast.makeText(MyMenu.this, "Menu: " + 
					titles.get(myMenu.getTitleIndex()) + 
					" Item: " + item_names.get(myMenu.getTitleIndex()).get(arg2),
					Toast.LENGTH_SHORT).show();
			myMenu.dismiss();	//菜单消失
			myMenu.currentState = 1;	//标记状态，已消失
			Log.e("MenuState:", "dismissing");
		}
		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.e("Activity:", "onPause");
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.e("Activity:", "onRestart");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.e("Activity:", "onResume");
	}
	
	
	
	
	
	
	

    
    
}