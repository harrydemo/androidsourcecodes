package cn.com.karl.anim;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

	private ImageView image;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		image=(ImageView) this.findViewById(R.id.icon);
		image.setBackgroundResource(R.drawable.bk);
	    image.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(MainActivity.this,PathButtonActivity.class);
				startActivity(intent);
				//第一个参数为启动时动画效果，第二个参数为退出时动画效果
				overridePendingTransition(R.anim.fade, R.anim.hold);
			}
		});
	}
	  @Override
	  protected void onStop() {
	  	// TODO Auto-generated method stub
	  	super.onStop();
	  	finish();
	  }
}
