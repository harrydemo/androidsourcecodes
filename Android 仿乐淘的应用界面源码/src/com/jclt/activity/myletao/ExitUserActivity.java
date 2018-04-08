package com.jclt.activity.myletao;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jclt.activity.CommonActivity;
import com.jclt.activity.MyLetaoInforActivity;
import com.jclt.activity.R;


public class ExitUserActivity extends CommonActivity implements OnClickListener {
	/**
	 * ��¼
	 */
	private TextView loginTextView = null;
	/**
	 * ע��
	 */
	private TextView registerTextView = null; 
	/**
	 * ��������
	 */
	private TextView forgetPasswordTextView = null; 
	/**
	 * �˺�
	 */
	private EditText cellPhoneEditText = null; 
	/**
	 * ����
	 */
	private EditText passWordEditText = null; 
           @Override
        protected void onCreate(Bundle savedInstanceState) {
        	// ȥ���ֻ�����Ĭ�ϱ���
       		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
       		setContentView(R.layout.letao_myletao_login);
       		// �ֻ������������
       		super.textViewTitle = (TextView) findViewById(R.id.title);
       		super.textViewTitle.setText(R.string.fristlogin);
        	super.onCreate(savedInstanceState);
        	
        	cellPhoneEditText = (EditText) findViewById(R.id.cell_phone);
        	passWordEditText = (EditText) findViewById(R.id.pwd);
    		loginTextView = (TextView) findViewById(R.id.login);
    		registerTextView = (TextView) findViewById(R.id.register);
    		forgetPasswordTextView = (TextView) findViewById(R.id.forget_password);
    		loginTextView.setOnClickListener(this);
    		registerTextView.setOnClickListener(this);
    		forgetPasswordTextView.setOnClickListener(this);
    		this.bottomMenuOnClick();
        	
        }
		@Override
		public void onClick(View v) {
			if (v == loginTextView) {
				// ���������
				InputMethodManager imManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				IBinder mBinder = getCurrentFocus().getWindowToken();
				imManager.hideSoftInputFromWindow(mBinder,
						InputMethodManager.HIDE_NOT_ALWAYS);
				if(cellPhoneEditText.getText().toString() == null || "".equals(cellPhoneEditText.getText().toString())){
				  Toast toast =  Toast.makeText(this, "�û�������Ϊ��,�����û�������", Toast.LENGTH_SHORT);
				  toast.setGravity(Gravity.CENTER, 0, 0);
				  toast.show();
				}else if(passWordEditText.getText().toString() == null || "".equals(passWordEditText.getText().toString())){
					Toast toast = Toast.makeText(this, "���벻��Ϊ��,�����������", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}else{
					startActivity(new Intent(this,MyLetaoInforActivity.class));
				}
				

			} else if (v == registerTextView) {
				startActivity(new Intent(this, RegisterActivity.class));
			} else if (v == forgetPasswordTextView) {
				startActivity(new Intent(this, ReturnPasswordActivity.class));
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
