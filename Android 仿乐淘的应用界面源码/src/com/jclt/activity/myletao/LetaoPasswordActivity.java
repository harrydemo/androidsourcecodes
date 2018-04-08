package com.jclt.activity.myletao;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jclt.activity.CommonActivity;
import com.jclt.activity.R;
import com.jclt.activity.SecondActivity;

public class LetaoPasswordActivity extends CommonActivity implements OnClickListener {
	/**
	 * ������
	 */
	private EditText oldPasswordEditText = null ;
	/**
	 * ������
	 */
	private EditText newPasswordEditText = null ;
	/**
	 * ȷ������
	 */
	private EditText verifyPasswordEditText = null ;
	/**
	 * �ύ�޸�����
	 */
	private TextView submitTextView = null ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// ȥ���ֻ�����Ĭ�ϱ���
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.letao_myletao_password);
       super.onCreate(savedInstanceState);
		// �ֻ������������
		super.textViewTitle = (TextView) findViewById(R.id.title);
		super.textViewTitle.setText(R.string.myletao_modifypassword_str);
		oldPasswordEditText = (EditText)findViewById(R.id.changepassword_originalpassword_edit);
		newPasswordEditText = (EditText)findViewById(R.id.changepassword_newpassword_edit);
		verifyPasswordEditText = (EditText)findViewById(R.id.changepassword_newpasswordagain_edit);
		submitTextView = (TextView)findViewById(R.id.changepassword_confirmchange_btn);
		submitTextView.setOnClickListener(this);
		this.bottomMenuOnClick();
	}
	@Override
	public void onClick(View v) {
		if(oldPasswordEditText.getText().toString() == null || "".equals(oldPasswordEditText.getText().toString())){
			Toast toast = Toast.makeText(getApplicationContext(), "�����ʽ�������ԭ�������", Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}else if(newPasswordEditText.getText().toString() == null || "".equals(newPasswordEditText.getText().toString())){
			Toast toast = Toast.makeText(getApplicationContext(), "�������ʽ������߲���Ϊ��", Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}else if(verifyPasswordEditText.getText().toString().equals(newPasswordEditText.getText().toString())== false){
			Toast toast = Toast.makeText(getApplicationContext(), "�������ȷ�����벻һ�£�", Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}else{
			Toast toast = Toast.makeText(getApplicationContext(), "�޸ĳɹ���", Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			startActivity(new Intent(getApplicationContext() , SecondActivity.class));
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
