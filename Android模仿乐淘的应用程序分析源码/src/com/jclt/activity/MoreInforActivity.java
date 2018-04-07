package com.jclt.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jclt.activity.SecondActivity.listViewLitongOnclickListener;
import com.jclt.activity.more.LetaoAboutActivity;
import com.jclt.activity.more.LetaoHistoryActivity;
import com.jclt.activity.more.LetaoHotLineActivity;
import com.jclt.activity.more.LetaoMicroBlogActivity;
import com.jclt.activity.more.LetaoServceActivity;
import com.jclt.activity.more.LetaoSuggestActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MoreInforActivity extends CommonActivity { 
	/**
	 * 乐淘服务
	 */
	private static final int SERVCE = 0;
	/**
	 * 关于乐淘
	 */
	private static final int ABOUT = 1;
	/**
	 * 历史记录
	 */
	private static final int HISTORY = 2;
	/**
	 * 乐淘热线
	 */
	private static final int HOTLINE = 3;
	/**
	 * 乐淘建议
	 */
	private static final int SUGGEST = 4;
	/**
	 * 微博
	 */
	private static final int MICROBLOG = 5;



	      @Override
	    protected void onCreate(Bundle savedInstanceState) {
	    	//去除手机界面默认标题
	  		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	  		setContentView(R.layout.moreinfor);
	  		
	  	//手机界面标题设置
			super.textViewTitle = (TextView)findViewById(R.id.title);
			super.textViewTitle.setText(R.string.more);
			// 底部菜单栏点击事件效果设置
			imageViewIndex = (ImageView) findViewById(R.id.menu_home_img);
			imageViewIndex.setOnTouchListener(viewIndex);
            imageViewIndex.setImageResource(R.drawable.menu_home_released);
            
			imageViewType = (ImageView) findViewById(R.id.menu_brand_img);
			imageViewType.setOnTouchListener(viewType);

			imageViewShooping = (ImageView) findViewById(R.id.menu_shopping_cart_img);
			imageViewShooping.setOnTouchListener(viewShooping);

			imageViewMyLetao = (ImageView) findViewById(R.id.menu_my_letao_img);
			imageViewMyLetao.setOnTouchListener(viewMyLetao);
			
			imageViewMore = (ImageView) findViewById(R.id.menu_more_img);
			imageViewMore.setOnTouchListener(viewMore);
			imageViewMore.setImageResource(R.drawable.menu_more_pressed);
			
			super.listViewAll = (ListView)findViewById(android.R.id.list);
			super.listViewAll.setOnItemClickListener(new listViewLitongOnclickListener());
			setListAdapter(new SimpleAdapter(this, getDate(), R.layout.common_listview_text, new String[]{"img","text","img_pre"}, new int [] {R.id.img,R.id.text,R.id.img_pre}));
			//进入该界面时,模仿从服务器加载数据时的虚拟进度条
			super.progressDialog = ProgressDialog.show(this, "历通购物", "数据获取中....",true);
			super.progressDialog.show();
			//通过线程来循环调用进度条
			super.handler.post(this);
	    	super.onCreate(savedInstanceState);
	    }
	      
	      private List<Map<String, Object>> getDate(){
	  		List<Map<String, Object>> listMore = new ArrayList<Map<String,Object>>();
	  		for (int i = 0; i < MORETYPE.length; i++) {
	  			Map<String, Object> mapMore = new HashMap<String, Object>();
	  			mapMore.put("text", MORETYPE[i]);
	  			mapMore.put("img", R.drawable.toright_mark);
	  			mapMore.put("img_pre",R.drawable.paopao);
	  			listMore.add(mapMore);
	  		}
	  		return listMore;
	  	}
	  	static final String [] MORETYPE = {"乐淘服务承诺","关于","浏览记录","乐淘服务热线","建议反馈","手机乐淘微博"};
	  	
	  	//底部菜单模块<更多>ListView监听器
	  	class listViewLitongOnclickListener implements OnItemClickListener{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
			if (position == SERVCE) {
				//le tao fu wu cheng nuo
				intent.setClass(MoreInforActivity.this, LetaoServceActivity.class);
				startActivity(intent);
			} else if (position == ABOUT) {
				//guan yu ban ben hao ma(Android 2.2)
				intent.setClass(MoreInforActivity.this, LetaoAboutActivity.class);
				startActivity(intent);
			} else if (position == HISTORY) {
				//liu lan ji lu
				intent.setClass(MoreInforActivity.this, LetaoHistoryActivity.class);
				startActivity(intent);
			} else if (position == HOTLINE) {
				//le tao fu wu re xian
				intent.setClass(MoreInforActivity.this, LetaoHotLineActivity.class);
				startActivity(intent);
			} else if (position == SUGGEST) {
				//jian yi fan kui
				intent.setClass(MoreInforActivity.this, LetaoSuggestActivity.class);
				startActivity(intent);
			} else if (position == MICROBLOG) {
				//le tao shou ji bo ke 
				intent.setClass(MoreInforActivity.this, LetaoMicroBlogActivity.class);
				startActivity(intent);
			}
			}
	  		
	  	}
}
