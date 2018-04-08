package cn.itcast.picture;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.ImageView;

public class ShowActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show);
		
		String uri = getIntent().getStringExtra("imageuri");
		ImageView imageView = (ImageView) findViewById(R.id.imageView);
		imageView.setImageURI(Uri.parse(uri));
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {//µã»÷ÆÁÄ»¹Ø±Õ
		if(event.getAction()==MotionEvent.ACTION_UP){
			finish();
		}
		return super.onTouchEvent(event);
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		finish();
		return super.onKeyDown(keyCode, event);
	}
}
