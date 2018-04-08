package com.myandroid.test;

import java.util.List;

import com.myandroid.test.MyMenu.ItemClickEvent;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;



public class MyDefinedMenu extends PopupWindow { 
	
	private LinearLayout layout;	//�ܵĲ���
	private GridView gv_title;		//�˵���
	private GridView gv_body;		//ѡ����ͼ
	private BodyAdatper[] bodyAdapter;	//ѡ��������
	private TitleAdatper titleAdapter;	//����������
	private Context context;			//������
	private int titleIndex;				//�˵����
	public int currentState;			//�Ի���״̬��0--��ʾ�С�1--����ʧ��2--ʧȥ����
	
	
	
	public MyDefinedMenu(Context context, List<String> titles, 
			List<List<String>> item_names, List<List<Integer>> item_images,
			ItemClickEvent itemClickEvent) {
		
		super(context);
		this.context = context;
		currentState = 1;
		
		//���ֿ��
		layout = new LinearLayout(context);		
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setLayoutParams(new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		
		//�˵���
	    titleIndex = 0;
		gv_title = new GridView(context);
		titleAdapter = new TitleAdatper(context, titles);
		gv_title.setAdapter(titleAdapter);
		gv_title.setLayoutParams(new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		gv_title.setNumColumns(titles.size());	//�˵�����
		gv_title.setBackgroundColor(Color.WHITE);
		
		//ѡ����ͼ
		bodyAdapter = new BodyAdatper[item_names.size()];	//������ͼ������
		for (int i = 0; i < item_names.size(); i++) {
			bodyAdapter[i] = new BodyAdatper(context, item_names.get(i), item_images.get(i));
		}
		gv_body = new GridView(context);
		gv_body.setNumColumns(4);	//ÿ����ʾ4��ѡ��
		gv_body.setBackgroundColor(Color.TRANSPARENT);
		gv_body.setAdapter(bodyAdapter[0]);	//����������
		
		//�˵����л�
		gv_title.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				titleIndex = arg2;	//��¼��ǰѡ�в˵������
				titleAdapter.setFocus(arg2);
				gv_body.setAdapter(bodyAdapter[arg2]);	//�ı�ѡ����ͼ

			}
		});
		
		//����ѡ�����¼�
		gv_body.setOnItemClickListener(itemClickEvent);
		
		//��ӱ�������ѡ��
		layout.addView(gv_title);
		layout.addView(gv_body);
		
		// ��Ӳ˵���ͼ
		this.setContentView(layout);
		this.setWidth(LayoutParams.FILL_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);// menu�˵���ý��� ���û�л�ý���menu�˵��еĿؼ��¼��޷���Ӧ  
		
	}
	
	/**
	 * ��ȡ��ǰѡ�в˵���
	 * @return	�˵������
	 */
	public int getTitleIndex() {
		
		return titleIndex;
	}
	
	

	
}
