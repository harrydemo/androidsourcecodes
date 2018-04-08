package com.jclt.activity;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Scroller;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class BoyShoesActivity extends CommonActivity implements OnItemClickListener{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//ȥ���ֻ�����Ĭ�ϱ���
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.boyshoes);
		//�ֻ������������
		super.textViewTitle = (TextView)findViewById(R.id.title);
		super.textViewTitle.setText(R.string.boyshoes);
		
		// �ײ��˵�������¼�Ч������
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
		setListAdapter(new SimpleAdapter(BoyShoesActivity.this, getDate(), R.layout.common_listview_text, new String[]{"img","text","img_pre"}, new int[]{R.id.img,R.id.text,R.id.img_pre}));
		super.listViewAll.setOnItemClickListener(this);
		//����ý���ʱ,ģ�´ӷ�������������ʱ�����������
		super.progressDialog = ProgressDialog.show(this, "��ͨ����", "���ݻ�ȡ��....",true);
		super.progressDialog.show();
		//ͨ���߳���ѭ�����ý�����
		super.handler.post(this);
	}
    
	
	private List<Map<String,Object>> getDate(){
		List<Map<String,Object>> listBoy = new ArrayList<Map<String,Object>>();
		for (int i = 0; i < BOYTYPE.length; i++) {
			Map<String,Object> mapBoy = new HashMap<String, Object>();
			mapBoy.put("text",BOYTYPE[i]);
			mapBoy.put("img",R.drawable.toright_mark);
			mapBoy.put("img_pre",R.drawable.paopao);
			listBoy.add(mapBoy);
		}
		return listBoy;
	}
	static final String[] BOYTYPE = {"��Ʒ","�˶�Ь","ƤЬ","����Ь","��Ь","��Ь","��Ь","����Ь","����Ь","�ܲ�Ь","����Ь","ѵ��Ь","����Ь","����Ь","����ƤЬ","����ƤЬ","ѥ��","��Ь"};
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		intent.setClass(this, NewProMarketActivity.class);
		startActivity(intent);
	}
	
}
