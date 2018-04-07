package com.jclt.activity.myletao;

import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jclt.activity.CommonActivity;
import com.jclt.activity.R;

public class LetaoCollectActivity extends CommonActivity {
	
	/**
	 * 背景图
	 */
	private static LinearLayout backgroundLinearLayout = null ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除手机界面默认标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.letao_myletao_collect);
		// 手机界面标题设置
		super.textViewTitle = (TextView) findViewById(R.id.title);
		super.textViewTitle.setText(R.string.title_my_favorite);
		backgroundLinearLayout = (LinearLayout)findViewById(R.id.myfavorite_productlist_layout);
		backgroundLinearLayout.setBackgroundResource(R.drawable.my_favorite_nodata);
		this.bottomMenuOnClick();
	}
	
	
	/**
	 * 底部菜单监听器
	 */
	private void bottomMenuOnClick() {
		imageViewIndex = (ImageView) findViewById(R.id.menu_home_img);
		imageViewIndex.setOnTouchListener(viewIndex);
		imageViewIndex.setImageResource(R.drawable.menu_home_released);
		imageViewType = (ImageView) findViewById(R.id.menu_brand_img);
		imageViewType.setOnTouchListener(viewType);
		imageViewType.setImageResource(R.drawable.menu_brand_released);
		imageViewShooping = (ImageView) findViewById(R.id.menu_shopping_cart_img);
		imageViewShooping.setOnTouchListener(viewShooping);
		imageViewShooping.setImageResource(R.drawable.menu_shopping_cart_released);
		imageViewMyLetao = (ImageView) findViewById(R.id.menu_my_letao_img);
		imageViewMyLetao.setOnTouchListener(viewMyLetao);
		imageViewMyLetao.setImageResource(R.drawable.menu_my_letao_pressed);
		imageViewMore = (ImageView) findViewById(R.id.menu_more_img);
		imageViewMore.setOnTouchListener(viewMore);
		imageViewMore.setImageResource(R.drawable.menu_more_released);
	}
}
