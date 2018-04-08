package com.android.caigang.view;

import com.android.caigang.R;
import com.android.caigang.util.ImageUtil;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

public class ImageActivity extends Activity {
	private ImageView iv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.head_photo);
		iv = (ImageView)findViewById(R.id.heab_photo);
		try {
			Drawable drawable = ImageUtil.getDrawableFromUrl("http://app.qlogo.cn/mbloghead/bb97fdbda7913d1a6174/100");
			iv.setImageDrawable(drawable);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
