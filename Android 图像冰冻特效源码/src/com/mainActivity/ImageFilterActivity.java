package com.mainActivity;

import com.imageFilters.IceFilter;
import com.util.ImageCache;
import com.util.ImageUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class ImageFilterActivity extends Activity {

	ImageView imageView1, imageView2;
	Drawable mDrawable;
	Bitmap mBitmap;

	Context mContext;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image);
		mContext = this;
		findView();

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		int pos = bundle.getInt("position", 0);
		Bitmap tmpBitmap;
		switch (pos) {
		case 0:
			if (ImageCache.get("IceFilter") != null) {
				tmpBitmap = ImageCache.get("IceFilter");
				imageView2.setImageBitmap(tmpBitmap);
				break;
			}
			tmpBitmap = new IceFilter(mBitmap).imageProcess().getDstBitmap();
			imageView2.setImageBitmap(tmpBitmap);
			ImageCache.put("IceFilter", tmpBitmap);
			break;
		default:
			imageView2.setImageBitmap(mBitmap);
			break;
		}
	}

	private void findView() {
		// TODO Auto-generated method stub
		imageView1 = (ImageView) findViewById(R.id.imageView1);
		imageView2 = (ImageView) findViewById(R.id.imageView2);
		mDrawable = imageView1.getDrawable();
		mBitmap = ImageUtil.readBitMap(mContext, R.drawable.image);
		imageView2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				imageView1.setVisibility(View.GONE);
			}
		});
	}
}
