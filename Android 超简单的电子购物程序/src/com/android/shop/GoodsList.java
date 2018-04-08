package com.android.shop;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class GoodsList extends Activity implements OnClickListener {
	private CheckBox mCheckBox01,mCheckBox02,mCheckBox03;
	private ImageButton mImageButton01,mImageButton02,mImageButton03;
	private Button mButton01,mButton02;
	private TextView mTextView01,mTextView02;
	private TextView mExitTextView;
	
	public static  boolean flag=false;
	ShopDbAdapter sDb;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_list);
        showList();
        sDb=new ShopDbAdapter(this);
        sDb.open();
        mButton01.setOnClickListener(this);
        mButton02.setOnClickListener(this);
        
        mTextView01.setOnClickListener(this);
        mTextView02.setOnClickListener(this);
        
        mExitTextView.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
//				sDb.drop_Dababase();
				finish();
				
			}
		});
        
        
    }
    public void showList(){
    	mExitTextView=(TextView) findViewById(R.id.exit);
    	mImageButton01=(ImageButton) findViewById(R.id.image_01);
    	mImageButton01.setBackgroundDrawable(getResources().getDrawable(R.drawable.android));
    	mImageButton02=(ImageButton) findViewById(R.id.image_02);
    	mImageButton02.setBackgroundDrawable(getResources().getDrawable(R.drawable.java));
    	mImageButton03=(ImageButton) findViewById(R.id.image_03);
    	mImageButton03.setBackgroundDrawable(getResources().getDrawable(R.drawable.c));
    	mCheckBox01=(CheckBox) findViewById(R.id.check_01);
    	mCheckBox02=(CheckBox) findViewById(R.id.check_02);
    	mCheckBox03=(CheckBox) findViewById(R.id.check_03);
    	mButton01=(Button) findViewById(R.id.shop_button);
    	mButton02=(Button) findViewById(R.id.collect_button);
    	mTextView01=(TextView) findViewById(R.id.text_view01);
    	mTextView02=(TextView) findViewById(R.id.text_view02);
    }
	public void onClick(View v) {
		if (v==mButton01) {

		 if (mCheckBox01.isChecked()) {
			sDb.store_createNote("android开发入门");
		}
		 if (mCheckBox02.isChecked()) {
				sDb.store_createNote("java编程思想");
			}
		 if (mCheckBox03.isChecked()) {
				sDb.store_createNote("C#程序设计");
			}
		}
		if (v==mButton02) {
//			del02();
			 if (mCheckBox01.isChecked()) {
					sDb.collection_createNote("android开发入门");
				}
				 if (mCheckBox02.isChecked()) {
						sDb.collection_createNote("java编程思想");
					}
				 if (mCheckBox03.isChecked()) {
						sDb.collection_createNote("C#程序设计");
					}
		}
		if (v==mTextView01) {
			flag=true;	
			Intent i=new Intent();
			i.setClass(this, Shop_Store.class);
			Log.i("flag===", String.valueOf(flag));
		
			startActivity(i);
		}
		if (v==mTextView02) {
			flag=false;
			Intent i=new Intent(this, Shop_Store.class);
			startActivity(i);
		}
	}

	
}