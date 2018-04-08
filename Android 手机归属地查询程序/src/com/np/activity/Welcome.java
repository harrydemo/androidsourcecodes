package com.np.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.wb.np.R;

public class Welcome extends Activity{
	private ProgressDialog p;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.welcome);
		
		 p = ProgressDialog.show(this, "ÇëÄúÉÔµÈ", "ÕýÔÚµÇÂ¼...");
		
		
		
		
		
		ImageView img = (ImageView)this.findViewById(R.id.img);
		img.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("http://shouji.360.cn/partner/html/101089.web.html")));
			}
		});
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				if(p.isShowing())
					p.dismiss();
				Intent intent = new Intent();
				intent.setClass(Welcome.this, Main.class);
				startActivity(intent);
				finish();
			}
		},3000);
	}

	
}
