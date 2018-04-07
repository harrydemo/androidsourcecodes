package com.jclt.activity.type;

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

import com.jclt.activity.BoyShoesActivity;
import com.jclt.activity.CommonActivity;
import com.jclt.activity.GirlShoesActivity;
import com.jclt.activity.MoreShoesActivity;
import com.jclt.activity.R;
import com.jclt.activity.SecondActivity;

public class TypeLetaoActivity extends CommonActivity implements OnItemClickListener {
	
	private int BOYSHOES = 0;
	private int GIRLSHOES = 1;
	private int MORESHOES = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除手机界面默认标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.letao_type_sreach);
		// 手机界面标题设置
		super.textViewTitle = (TextView) findViewById(R.id.title);
		super.textViewTitle.setText(R.string.title_myletao_type);
		super.listViewAll = (ListView)findViewById(android.R.id.list);
		setListAdapter(new SimpleAdapter(TypeLetaoActivity.this, getDate(), R.layout.common_listview_text, new String[]{"img","text","img_pre"}, new int[]{R.id.img,R.id.text,R.id.img_pre}));
		super.listViewAll.setOnItemClickListener(this);
		// 进入该界面时,模仿从服务器加载数据时的虚拟进度条
		super.progressDialog = ProgressDialog.show(this, "历通购物", "数据获取中....",
				true);
		super.progressDialog.show();
		// 通过线程来循环调用进度条
		super.handler.post(this);
		this.bottomMenuOnClick();
	}
	
	/**
     * 获取ListView的数据
     * @return
     */
	private List<Map<String, Object>> getDate() {
		List<Map<String, Object>> listLitong = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < TYPE.length; i++) {
			Map<String, Object> maplitong = new HashMap<String, Object>();
			maplitong.put("text", TYPE[i]);
			maplitong.put("img", R.drawable.toright_mark);
			maplitong.put("img_pre", R.drawable.paopao);
			listLitong.add(maplitong);
		}

		return listLitong;
	}
    
	/**
	 * ListView数组
	 */
	static final String[] TYPE = { "男鞋", "女鞋", "更多分类"};
	
	
	/**
	 * 底部菜单监听器
	 */
	private void bottomMenuOnClick() {
		imageViewIndex = (ImageView) findViewById(R.id.menu_home_img);
		imageViewIndex.setOnTouchListener(viewIndex);
		imageViewIndex.setImageResource(R.drawable.menu_home_released);
		imageViewType = (ImageView) findViewById(R.id.menu_brand_img);
		imageViewType.setOnTouchListener(viewType);
		imageViewType.setImageResource(R.drawable.menu_brand_pressed);
		imageViewShooping = (ImageView) findViewById(R.id.menu_shopping_cart_img);
		imageViewShooping.setOnTouchListener(viewShooping);
		imageViewShooping.setImageResource(R.drawable.menu_shopping_cart_released);
		imageViewMyLetao = (ImageView) findViewById(R.id.menu_my_letao_img);
		imageViewMyLetao.setOnTouchListener(viewMyLetao);
		imageViewMyLetao.setImageResource(R.drawable.menu_my_letao_released);
		imageViewMore = (ImageView) findViewById(R.id.menu_more_img);
		imageViewMore.setOnTouchListener(viewMore);
		imageViewMore.setImageResource(R.drawable.menu_more_released);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
		if (position == BOYSHOES) {
			intent.setClass(TypeLetaoActivity.this, BoyShoesActivity.class);
			startActivity(intent);
		} else if (position == GIRLSHOES) {
			intent.setClass(TypeLetaoActivity.this, GirlShoesActivity.class);
			startActivity(intent);
		} else if (position == MORESHOES) {
			intent.setClass(TypeLetaoActivity.this, MoreShoesActivity.class);
			startActivity(intent);
		}
		
	}     
}
