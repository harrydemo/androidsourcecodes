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
	 * ȥ���    
	 */
	private TextView shoppingTextView = null  ; 
	    
	
        @Override
        protected void onCreate(Bundle savedInstanceState) {
        	super.onCreate(savedInstanceState);
    		// ȥ���ֻ�����Ĭ�ϱ���
    		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    		setContentView(R.layout.letao_shopping_shoppcar);
    		// �ֻ������������
    		super.textViewTitle = (TextView) findViewById(R.id.title);
    		super.textViewTitle.setText(R.string.title_letao_shopping_car);
    		
    		// ����ý���ʱ,ģ�´ӷ�������������ʱ�����������
    		super.progressDialog = ProgressDialog.show(this, "��ͨ����", "���ݻ�ȡ��....",
    				true);
    		super.progressDialog.show();
    		// ͨ���߳���ѭ�����ý�����
    		super.handler.post(this);
        	this.shoppingTextView = (TextView)findViewById(R.id.go_shopping);
        	this.shoppingTextView.setOnClickListener(this);
        	this.bottomMenuOnClick();
        }
        
        
        /**
    	 * �ײ��˵�������
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
