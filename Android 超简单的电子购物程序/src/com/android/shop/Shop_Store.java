package com.android.shop;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class Shop_Store extends ListActivity {
	private TextView mTextView;
	private Button mButton;
	ShopDbAdapter sDb;
	ShopOnline sonline;
	
//	GoodsList gl;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.store_list);
		sDb=new ShopDbAdapter(this);
		sonline=new ShopOnline();

	
		sDb.open();
		
		mButton=(Button) findViewById(R.id.sure_button);
		mTextView=(TextView) findViewById(R.id.user_text);
		
		mButton.setOnClickListener(new Button.OnClickListener() {
			
			public void onClick(View v) {
//				Intent i=new Intent(Shop_Store.this, GoodsList.class);
//				startActivity(i);
				
				finish();
			}
		});
		
		Log.i("onCreate=====", "onCreate====");
		renderList();
	}
	public void renderList(){

		if (GoodsList.flag) {
			mTextView.setText("欢迎您，admin! 您的购物车内有以下物品：");
			Log.i("flag======", String.valueOf(GoodsList.flag));
		   Cursor c=sDb.store_fetchAllNotes();
		   startManagingCursor(c);
			   SimpleCursorAdapter notes=new SimpleCursorAdapter(this,	R.layout.store_row , c, 
					   new String[]{ShopDbAdapter.KEY_NAME,ShopDbAdapter.COLLECTION_ROWID},new int[] {R.id.text1,R.id.text2});
			   setListAdapter(notes);		
	}
        if (!GoodsList.flag) {
        	mTextView.setText("欢迎您，admin! 您的收藏夹内有以下物品：");
        	Log.i("flag======", String.valueOf(GoodsList.flag));
        	Cursor c=sDb.collection_fetchAllNotes();
 		   startManagingCursor(c);
 			   SimpleCursorAdapter notes=new SimpleCursorAdapter(this,	R.layout.store_row , c, 
 					   new String[]{ShopDbAdapter.KEY_NAME,ShopDbAdapter.COLLECTION_ROWID},new int[] {R.id.text1,R.id.text2});
 			   setListAdapter(notes);
		}

	}
}
