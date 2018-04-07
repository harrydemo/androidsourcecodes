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

public class RegisterActivity extends CommonActivity implements OnClickListener {
	/**
	 * �ʺ�
	 */
	private EditText usernameEditText = null ; 
	/**
	 * ����
	 */
	private EditText passwordEditText = null ; 
	/**
	 * ȷ������
	 */
	private EditText verifyEditText = null ;
	/**
	 * �ύ
	 */
	private TextView submitTextView = null ;
      @Override
    protected void onCreate(Bundle savedInstanceState) {
    	  super.onCreate(savedInstanceState);
  		// ȥ���ֻ�����Ĭ�ϱ���
  		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
  		setContentView(R.layout.letao_myletao_register);
  		// �ֻ������������
  		super.textViewTitle = (TextView) findViewById(R.id.title);
  		super.textViewTitle.setText(R.string.register_submit);
  		
  		usernameEditText = (EditText)findViewById(R.id.register_mobile_phone);
  		passwordEditText = (EditText)findViewById(R.id.register_password);
  		verifyEditText = (EditText)findViewById(R.id.register_repassword);
  		submitTextView = (TextView)findViewById(R.id.register);
  		submitTextView.setOnClickListener(this);
  		this.bottomMenuOnClick();
    }
	@Override
	public void onClick(View v) {
		
			if(usernameEditText.getText().toString().length() < 11 || usernameEditText.getText().toString() == null || "".equals(usernameEditText.getText().toString())){
				Toast toast = Toast.makeText(this, "�ʺŸ�ʽ����", Toast.LENGTH_SHORT);
		    	toast.setGravity(Gravity.CENTER, 0, 0);
		    	toast.show();
		}
			else	if(passwordEditText.getText().toString() == null || "".equals(passwordEditText.getText().toString())){
				Toast toast = Toast.makeText(this, "�����ʽ����", Toast.LENGTH_SHORT);
		    	toast.setGravity(Gravity.CENTER, 0, 0);
		    	toast.show();
		} 		else if(verifyEditText.getText().toString() == null || "".equals(verifyEditText.getText().toString())){
			Toast toast = Toast.makeText(this, "ȷ�������ʽ����", Toast.LENGTH_SHORT);
		    	toast.setGravity(Gravity.CENTER, 0, 0);
		    	toast.show();
		} else if ( passwordEditText.getText().toString().equals(verifyEditText.getText().toString()) ){
			Toast toast = Toast.makeText(this, "ȷ����������벻һ��", Toast.LENGTH_SHORT);
	    	toast.setGravity(Gravity.CENTER, 0, 0);
	    	toast.show();
		}else {
			Toast toast = Toast.makeText(this, "ע��ɹ�", Toast.LENGTH_SHORT);
	    	toast.setGravity(Gravity.CENTER, 0, 0);
	    	toast.show();
	    	startActivity(new Intent(this,SecondActivity.class));
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
