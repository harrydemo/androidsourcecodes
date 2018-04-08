package com.jclt.activity.shopping;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.jclt.activity.CommonActivity;
import com.jclt.activity.R;
import com.jclt.activity.type.TypeLetaoActivity;

public class ShoppingCarActivity extends CommonActivity implements OnClickListener {
	/**
	 * 去逛逛    
	 */
	private TextView shoppingTextView = null  ; 
	    
	
        @Override
        protected void onCreate(Bundle savedInstanceState) {
        	super.onCreate(savedInstanceState);
    		// 去除手机界面默认标题
    		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    		setContentView(R.layout.letao_shopping_shoppcar);
    		// 手机界面标题设置
    		super.textViewTitle = (TextView) findViewById(R.id.title);
    		super.textViewTitle.setText(R.string.title_letao_shopping_car);
    		
    		// 进入该界面时,模仿从服务器加载数据时的虚拟进度条
    		super.progressDialog = ProgressDialog.show(this, "历通购物", "数据获取中....",
    				true);
    		super.progressDialog.show();
    		// 通过线程来循环调用进度条
    		super.handler.post(this);
        	this.shoppingTextView = (TextView)findViewById(R.id.go_shopping);
        	this.shoppingTextView.setOnClickListener(this);
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
    		imageViewShooping.setImageResource(R.drawable.menu_shopping_cart_pressed);
    		imageViewMyLetao = (ImageView) findViewById(R.id.menu_my_letao_img);
    		imageViewMyLetao.setOnTouchListener(viewMyLetao);
    		imageViewMyLetao.setImageResource(R.drawable.menu_my_letao_released);
    		imageViewMore = (ImageView) findViewById(R.id.menu_more_img);
    		imageViewMore.setOnTouchListener(viewMore);
    		imageViewMore.setImageResource(R.drawable.menu_more_released);
    	}


		@Override
		public void onClick(View v) {
                     startActivity(new Intent(getApplicationContext() , TypeLetaoActivity.class));
		}
}
