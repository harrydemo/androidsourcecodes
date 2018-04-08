package com.jclt.activity.more;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jclt.activity.CommonActivity;
import com.jclt.activity.R;

public class LetaoServceActivity extends CommonActivity {
	private ImageView servceImageView = null ; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		// ȥ���ֻ�����Ĭ�ϱ���
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.letao_more_servce);

		// �ֻ������������
		super.textViewTitle = (TextView) findViewById(R.id.title);
		super.textViewTitle.setText(R.string.servce);
        super.listViewAll = (ListView)findViewById(android.R.id.list);
		// ͨ���߳���ѭ�����ý�����
		super.progressDialog = ProgressDialog.show(this, "��ͨ����", "���ݻ�ȡ��....",true);
		super.progressDialog.show();
		super.handler.post(this);
		//��������
		servceImageView = (ImageView)findViewById(R.id.servceImage);
		bottomMenuOnClick();
	}
	
	 /**
     * �ײ��˵�������
     */
    private void bottomMenuOnClick(){
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
		imageViewMyLetao.setImageResource(R.drawable.menu_my_letao_released);
		imageViewMore = (ImageView) findViewById(R.id.menu_more_img);
		imageViewMore.setOnTouchListener(viewMore);
		imageViewMore.setImageResource(R.drawable.menu_more_pressed);
    }
}
