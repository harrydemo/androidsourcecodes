package com.jclt.activity.myletao.subclass;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jclt.activity.CommonActivity;
import com.jclt.activity.R;
import com.jclt.activity.myletao.LetaoAddressActivity;

public class LetaoNewAddressActivity extends CommonActivity implements OnClickListener {
	/**
	 * �ύ
	 */
	private TextView submitTextView = null ; 
	/**
	 * ȡ��
	 */
	private TextView cancelTextView = null ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ȥ���ֻ�����Ĭ�ϱ���
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.letao_myletao_newaddress);
		// �ֻ������������
		super.textViewTitle = (TextView) findViewById(R.id.title);
		super.textViewTitle.setText(R.string.title_add_address);
		submitTextView = (TextView)findViewById(R.id.save_address_button);
		cancelTextView = (TextView)findViewById(R.id.cancel_address_button);
		submitTextView.setOnClickListener(this);
		cancelTextView.setOnClickListener(this);
		this.bottomMenuOnClick();
	}

	@Override
	public void onClick(View v) {
		if (v == submitTextView) {
			Toast toast = Toast.makeText(getApplicationContext(), "���ʧ��,���Ǽٵ�!", Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		} else if (v == cancelTextView) {
			startActivity(new Intent(getApplicationContext(),LetaoAddressActivity.class));
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
}
