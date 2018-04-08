package com.jclt.activity.myletao;

import android.content.Intent;
import android.os.Bundle;
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

public class ReturnPasswordActivity extends CommonActivity implements OnClickListener{
	/**
	 * 找回密码
	 */
	  private TextView retrieveTextView = null ;
	  private EditText passwordEditText = null ;
      @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// 去除手机界面默认标题
     		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
     		setContentView(R.layout.letao_myletao_retrieve_password);
     		// 手机界面标题设置
     		super.textViewTitle = (TextView) findViewById(R.id.title);
     		super.textViewTitle.setText(R.string.fristlogin);
     		super.onCreate(savedInstanceState);
        passwordEditText = (EditText)findViewById(R.id.return_password_mobile_phone);
     	retrieveTextView = (TextView)findViewById(R.id.return_password);
     	retrieveTextView.setOnClickListener(this);
     	this.bottomMenuOnClick();
    }
	@Override
	public void onClick(View v) {
	    if(passwordEditText.getText().toString().length() < 11 || passwordEditText.getText().toString() == null || "".equals(passwordEditText.getText().toString())){
	    	Toast toast = Toast.makeText(this, "帐号格式不对", Toast.LENGTH_SHORT);
	    	toast.setGravity(Gravity.CENTER, 0, 0);
	    	toast.show();
	    }else{
	    	Toast toast = Toast.makeText(this, "信息已经发送到您的手机上面,请注意查收!", Toast.LENGTH_SHORT);
	    	toast.setGravity(Gravity.CENTER, 0, 0);
	    	toast.show();
	    	startActivity(new Intent(this,SecondActivity.class));
	    }
	}
	
	
	/**
	 * 底部菜单监听器
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
