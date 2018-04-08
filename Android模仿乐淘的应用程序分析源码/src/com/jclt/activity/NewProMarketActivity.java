package com.jclt.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import com.jclt.activity.R;
import com.jclt.activity.type.CommondityInforLetaoActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class NewProMarketActivity extends CommonActivity implements OnItemClickListener , OnClickListener {
	/**
	 * ��Ʒ
	 */
	private TextView NewTextView = null ;
	/**
	 * �۸�
	 */
	private TextView ProTextView = null ;
	/**
	 * ����
	 */
	private TextView HotTextView = null ;
	private GridView ImagegridView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ȥ���ֻ�����Ĭ�ϱ���
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.letao_new_pro_market);
		// �ֻ������������
		super.textViewTitle = (TextView) findViewById(R.id.title);
		super.textViewTitle.setText(R.string.app_name);
		
		
		
		ImagegridView = (GridView) findViewById(R.id.grid1);
		ImagegridView.setOnItemClickListener(this);
		//��Ʒ
		NewTextView =(TextView)findViewById(R.id.commoditysort_product_lefttab_btn);
		NewTextView.setOnClickListener(this);
		//�۸�
		ProTextView =(TextView)findViewById(R.id.commoditysort_product_righttab_btn);
		ProTextView.setOnClickListener(this);
		//����
		HotTextView = (TextView)findViewById(R.id.commoditysort_product_centertab_btn);
		HotTextView.setOnClickListener(this);
		this.NewOrProAndHot(R.drawable.about_sina_log,"��Ь",9);
		this.ProgressDialog();
		this.bottomMenuOnClick();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		intent.setClass(getApplicationContext(), CommondityInforLetaoActivity.class);
		startActivity(intent);
	}

	@Override
	public void onClick(View view) {
		if (view == NewTextView) {
			this.ProgressDialog();
			this.NewOrProAndHot(R.drawable.about_sina_log,"��Ь",9);
		} else if (view == ProTextView) {
			this.ProgressDialog();
			this.NewOrProAndHot(R.drawable.about_logo,"�˶�Ь",10);
		} else if (view == HotTextView) {
			this.ProgressDialog();
			this.NewOrProAndHot(R.drawable.product_specialprice_image,"����Ь",12);
		}
	}
	
	/**
	 * ���������
	 */
	private void ProgressDialog(){
		// ����ý���ʱ,ģ�´ӷ�������������ʱ�����������
		super.progressDialog = android.app.ProgressDialog.show(this, "��ͨ����", "���ݻ�ȡ��....",
				true);
		super.progressDialog.show();
		// ͨ���߳���ѭ�����ý�����
		super.handler.post(this);
	}
	
	private void NewOrProAndHot(Object object,String type , int price){
		List<HashMap<String, Object>> mapsList = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < 60; i++) {
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			hashMap.put("Image", object);
			hashMap.put("text", type+i);
			hashMap.put("pro", "$"+price+i);
			mapsList.add(hashMap);
		}
		SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(),
				mapsList, R.layout.common_gridview_text, new String[] {
						"Image", "text","pro" }, new int[] {R.id.ItemImage , R.id.ItemText , R.id.proText});
		ImagegridView.setAdapter(adapter);
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
}
