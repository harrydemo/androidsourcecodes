package com.song.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.song.Constant;
import com.song.ImageManager;
import com.song.ImageShowActivity;
import com.song.R;
import com.song.model.Bucket;
import com.song.model.Images;


/**
 * 拼图选择界面
 * @author admin
 *
 */
public class BucketDetailActivity extends Activity{
	
	private Bucket bucket;
	private List<Images> imageChose = new ArrayList<Images>();
	private TextView back;
	private TextView start;
	private TextView choseCount;
	private GridView gridview;
	private GridViewAdapter gridViewAdapter;
	
	private GridView gallery;
	private GalleryAdapter galleryAdapter;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.bucketdetail);
		 Intent intent = getIntent();
		 if (intent.hasExtra("bundle")){
			 Bundle bd = intent.getBundleExtra("bundle");
			 bucket = (Bucket) bd.getSerializable("bucket");
		 }
		 initUiData();
		 initListener();
	}
	
	public void initUiData(){
		
		back = (TextView) findViewById(R.id.back);
		start = (TextView) findViewById(R.id.start);
		choseCount = (TextView) findViewById(R.id.chose_count);
		gridview = (GridView)findViewById(R.id.chose_picture_grid);
		gridViewAdapter = new GridViewAdapter(this,bucket.getImages());
		gridview.setAdapter(gridViewAdapter);
		gallery = (GridView)findViewById(R.id.Gallery);
		galleryAdapter = new GalleryAdapter(this,imageChose);
		gallery.setAdapter(galleryAdapter);
		
	}
	public void initListener(){
		
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {				
				Intent intent = new Intent();
				intent.setClass(BucketDetailActivity.this, BucketListActivity.class);
				startActivity(intent);
			}
		});
		
		start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {				
				Intent intent = new Intent();
				intent.setClass(BucketDetailActivity.this, ResultActivity.class);
				intent.putExtra("imageChose", (Serializable)imageChose);
				startActivity(intent);
			}
		});
		
		gridview.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Images image = bucket.getImages().get(position);
				if(imageChose.size()<Constant.max)
				{
					imageChose.add(image);
					choseCount.setText("当前选中"+imageChose.size()+"张"+"(最多"+Constant.max+"张)");
					galleryAdapter.notifyDataSetChanged();
				}
				
				
			}
			
		});
		
		gallery.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Images image = imageChose.get(position);
				imageChose.remove(image);
				choseCount.setText("当前选中"+imageChose.size()+"张"+"(最多"+Constant.max+"张)");
				galleryAdapter.notifyDataSetChanged();
				
			}
			
		});
		
	}
	
	

}
