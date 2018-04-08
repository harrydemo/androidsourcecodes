package com.jclt.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jclt.activity.MoreInforActivity.listViewLitongOnclickListener;
import com.jclt.activity.myletao.ExitUserActivity;
import com.jclt.activity.myletao.LetaoAddressActivity;
import com.jclt.activity.myletao.LetaoCollectActivity;
import com.jclt.activity.myletao.LetaoFavorableActivity;
import com.jclt.activity.myletao.LetaoIndentActivity;
import com.jclt.activity.myletao.LetaoPasswordActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MyLetaoInforActivity extends CommonActivity {
	/**
	 * �˳���½
	 */
	private ImageView exitImageView = null ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// ȥ���ֻ�����Ĭ�ϱ���
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.myletaoinfor);

		// �ֻ������������
		super.textViewTitle = (TextView) findViewById(R.id.title);
		super.textViewTitle.setText(R.string.myletao);
		super.listViewAll = (ListView) findViewById(android.R.id.list);
		super.listViewAll.setOnItemClickListener(new listViewMyletaoOnclickListener());
		setListAdapter(new SimpleAdapter(this, getDate(),R.layout.common_listview_text, new String[] { "img", "text","img_pre" }, new int[] { R.id.img, R.id.text,R.id.img_pre }));
		// ����ý���ʱ,ģ�´ӷ�������������ʱ�����������
		super.progressDialog = ProgressDialog.show(this, "��ͨ����", "���ݻ�ȡ��....",
				true);
		super.progressDialog.show();
		// ͨ���߳���ѭ�����ý�����
		super.handler.post(this);
		super.onCreate(savedInstanceState);
		bottomMenuOnClick();
		
		//�˳���½
		exitImageView = (ImageView)findViewById(R.id.exitlogin);
		exitImageView.setOnClickListener( new exitImageViewOnClickListener());
	}
	
	/**
	 * �˳��¼���Ӧ
	 * @author Administrator
	 *
	 */
     class exitImageViewOnClickListener implements android.view.View.OnClickListener{

		@Override
		public void onClick(View v) {
			exitImageView.setImageResource(R.drawable.exitlogin_onclick);
			intent.setClass(getApplicationContext(), ExitUserActivity.class);
			startActivity(intent);
		}

			
    	 
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
		imageViewShooping.setImageResource(R.drawable.menu_shopping_cart_released);
		imageViewMyLetao = (ImageView) findViewById(R.id.menu_my_letao_img);
		imageViewMyLetao.setOnTouchListener(viewMyLetao);
		imageViewMyLetao.setImageResource(R.drawable.menu_my_letao_pressed);
		imageViewMore = (ImageView) findViewById(R.id.menu_more_img);
		imageViewMore.setOnTouchListener(viewMore);
		imageViewMore.setImageResource(R.drawable.menu_more_released);
	}

	/**
	 * 
	 * @return
	 */
	private List<Map<String, Object>> getDate() {
		List<Map<String, Object>> listmaps = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < MYLETAO.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("text", MYLETAO[i]);
			map.put("img", R.drawable.toright_mark);
			map.put("img_pre", R.drawable.paopao);
			listmaps.add(map);
		}
		return listmaps;
	}

	static final String[] MYLETAO = { "�ҵĶ���", "�ҵ��ղ�", "�ҵ��Ż�ȯ", "�ҵĵ�ַ��", "�޸�����" };
	
	
	private static final int ADDRESS = 3 ;
	private static final int COLLECT = 1 ; 
	private static final int FOVAORABLE = 2 ; 
	private static final int INDENT = 0 ;
	private static final int PASSWORD = 4 ;
	class listViewMyletaoOnclickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> list, View view, int position,
				long id) {
			if (position == INDENT) {
                 startActivity(new Intent(getApplicationContext(),LetaoIndentActivity.class));
			} else if (position == COLLECT) {
				startActivity(new Intent(getApplicationContext(),LetaoCollectActivity.class));
			} else if (position == FOVAORABLE) {
				startActivity(new Intent(getApplicationContext(),LetaoFavorableActivity.class));
			} else if (position == ADDRESS) {
				startActivity(new Intent(getApplicationContext(),LetaoAddressActivity.class));
			} else if (position == PASSWORD) {
				startActivity(new Intent(getApplicationContext(),LetaoPasswordActivity.class));
			}
		}
	}

}