package com.jclt.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class MoreShoesActivity extends CommonActivity implements OnItemClickListener{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除手机界面默认标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.moreshoes);
		//手机界面标题设置
		super.textViewTitle = (TextView)findViewById(R.id.title);
		super.textViewTitle.setText(R.string.moreshoes);
		
		// 底部菜单栏点击事件效果设置
		imageViewIndex = (ImageView) findViewById(R.id.menu_home_img);
		imageViewIndex.setOnTouchListener(viewIndex);
		
		imageViewType = (ImageView) findViewById(R.id.menu_brand_img);
		imageViewType.setOnTouchListener(viewType);
		imageViewType.setImageResource(R.drawable.menu_brand_pressed);
		
		imageViewShooping = (ImageView) findViewById(R.id.menu_shopping_cart_img);
		imageViewShooping.setOnTouchListener(viewShooping);

		imageViewMyLetao = (ImageView) findViewById(R.id.menu_my_letao_img);
		imageViewMyLetao.setOnTouchListener(viewMyLetao);
		
		imageViewMore = (ImageView) findViewById(R.id.menu_more_img);
		imageViewMore.setOnTouchListener(viewMore);
		
		super.listViewAll = (ListView)findViewById(android.R.id.list);
		setListAdapter(new SimpleAdapter(this, getDate(), R.layout.common_listview_text, new String[]{"img","text","img_pre"}, new int [] {R.id.img,R.id.text,R.id.img_pre}));
		super.listViewAll.setOnItemClickListener(this);
		//进入该界面时,模仿从服务器加载数据时的虚拟进度条
		super.progressDialog = ProgressDialog.show(this, "历通购物", "数据获取中....",true);
		super.progressDialog.show();
		//通过线程来循环调用进度条
		super.handler.post(this);
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
	static final String [] MORETYPE = {"童鞋","布鞋","运动鞋","皮鞋","户外鞋","凉鞋","拖鞋","单鞋","高跟鞋","坡跟鞋","平跟鞋","板鞋","休闲鞋","帆布鞋","跑步鞋","篮球鞋","训练鞋","网球鞋","足球鞋","商务皮鞋","休闲皮鞋","靴子"};
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		intent.setClass(getApplicationContext(), NewProMarketActivity.class);
		startActivity(intent);
	}
}
