package com.ly.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.ly.control.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

public class LYGalleryActivity extends Activity {
	
	//public int[] pics = new int[]{R.drawable.bg1,R.drawable.bg2,R.drawable.bg3,R.drawable.bg4,R.drawable.bg5,
			                  //    R.drawable.bg6,R.drawable.bg7,R.drawable.bg8,R.drawable.bg9,R.drawable.bg10};
//	private ImageView iv;
	private Gallery gallery;
	private int background;
	private String image[]={"t1.jpg","t2.jpg","t3.jpg","t4.jpg","t5.jpg","t6.jpg","t7.jpg"};
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery);	

        gallery = (Gallery)findViewById(R.id.Gallery01);
      //  iv = (ImageView)findViewById(R.id.ImageView01);
        gallery.setAdapter(new ImageAdapter(this,image));
        gallery.setOnItemClickListener((OnItemClickListener) listener);
 //       TypedArray imageBack= obtainStyledAttributes(R.styleable.gallery);
 //      background = imageBack.getResourceId(R.styleable.gallery_android_galleryItemBackground, 0);
    }
	
	private OnItemClickListener listener = new OnItemClickListener(){

		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			String url = image[arg2];
			Intent intent = new Intent();
			intent.putExtra("url", url);
			setResult(3,intent);
			finish();
		}
	};

	private class ImageAdapter extends BaseAdapter{

		private Context ctx;
		private String image[];
		
		public ImageAdapter (Context c,String image[]){
			
			ctx = c;
			this.image = image;
		}
		public int getCount() {
			// TODO Auto-generated method stub
			return image.length;
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return image[position];
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ImageView img = new ImageView(ctx);
			Bitmap bm = BitmapFactory.decodeFile("/sdcard/"+image[position]);
			img.setImageBitmap(bm);
			img.setLayoutParams(new Gallery.LayoutParams(80,108));//图片宽和高
			img.setScaleType(ImageView.ScaleType.FIT_XY);//图片如何在imageview中显示
			img.setBackgroundResource(background);
			return img;
		}
		
	}

}














