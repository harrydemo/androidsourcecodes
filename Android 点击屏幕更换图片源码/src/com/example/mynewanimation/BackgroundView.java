package com.example.mynewanimation;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;

public class BackgroundView extends View{

	private static BackgroundView backgroundView;
	String[] nameList = Utils.nameList;
	AssetManager assetManager;
	Bitmap bitmap;
	String imagePathString;
	
	static BackgroundView getInstance(Context context){
		if(backgroundView == null){
			backgroundView = new BackgroundView(context);
		}
		return backgroundView;		
	}
	
	public BackgroundView(Context context) {
		super(context);
		initBackImage();
	}
	
	public void switchBack(){
		imagePathString = null;
		initBackImage();
		postInvalidate();
	}
	
	private void initBackImage() {
		
		String imagePathString = nameSwitch()+".jpg";
		try {
			assetManager = this.getResources().getAssets();
			InputStream inputStream = assetManager.open(imagePathString);
			bitmap = BitmapFactory.decodeStream(inputStream);
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String nameSwitch(){
		String name = "";
		double r = Math.random()*10;
		if(r<nameList.length){
			int i = (int)(r);
			name = nameList[i];
		}
		return name;
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawBitmap(bitmap, 0,0,null);
	}

}

