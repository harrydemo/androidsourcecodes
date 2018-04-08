package com.song.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.song.ImageManager;
import com.song.ImageShowActivity;
import com.song.R;
import com.song.model.Bucket;


/**
 * 图片列表界面
 * @author admin
 *
 */
public class BucketListActivity extends Activity{

	private ListView bucktList;
	private BucketListAdapter bucketListAdapter;
	private TextView back;
	private TextView flash;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.bucketlist);
		 initUiData();
		 initListener();
	}
	
	public void initUiData(){		
		bucktList = (ListView)findViewById(R.id.bucket_list);
		bucketListAdapter = new BucketListAdapter(this);
		bucktList.setAdapter(bucketListAdapter);
		back = (TextView)findViewById(R.id.back);
		flash = (TextView)findViewById(R.id.flash);
	}
	
	public void initListener(){
		
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {				
				Intent intent = new Intent();
				intent.setClass(BucketListActivity.this, ImageShowActivity.class);
				startActivity(intent);
			}
		});
		
		bucktList.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Bucket bucket = ImageManager.bucketList.get(position);
				Intent intent = new Intent();
				intent.setClass(BucketListActivity.this, BucketDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("bucket", bucket);
				intent.putExtra("bundle", bundle);
				startActivity(intent);
			}
			
		});
		
	}
	
	

}
