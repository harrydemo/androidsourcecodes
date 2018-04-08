package com.song.ui;

import java.util.ArrayList;
import java.util.List;

import com.song.R;
import com.song.model.Bucket;
import com.song.model.Images;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;

public class ResultActivity extends Activity{
	
	private List<Images> imageChose = new ArrayList<Images>(); 
	private GridView gridview;
	private GridViewAdapter gridViewAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result);
		 Intent intent = getIntent();
		 if (intent.hasExtra("imageChose")){
			 
			 imageChose = (List<Images>) intent.getSerializableExtra("imageChose");
		 }
		 initUiData();		 
	}
	
	public void initUiData(){
		
		gridview = (GridView)findViewById(R.id.chose_picture_grid);
		gridViewAdapter = new GridViewAdapter(this,imageChose);
		gridview.setAdapter(gridViewAdapter);
		
	}
	
	
	
	
	
	

}
