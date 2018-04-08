package org.mingjiang.ticket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

public class MainApplication extends Activity {
	protected boolean _active = true;
	protected int _splashTime = 2000;
	ProgressBar progressBarHorizontal;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		progressBarHorizontal = (ProgressBar) this.findViewById(R.id.progressBarHorizontal);
		progressBarHorizontal.setMax(_splashTime);
		//progressBarHorizontal.setProgress(30);  
		//progressBarHorizontal.setSecondaryProgress(70); 
		
		final Thread splashThread = new Thread() {
			@Override
			public void run() {
				try {
	                int waited = 0;
	                while(_active && (waited < _splashTime)) {
	                    sleep(100);
	                    if(_active) {
	                        waited += 100;
	                        progressBarHorizontal.setProgress(waited); 
	                    }
	                }
	            } catch(InterruptedException e) {
	                // do nothing
	            } finally {
	                finish();
	                Intent i = new Intent(MainApplication.this,
	            			RemainSearcher.class);
	            	MainApplication.this.startActivity(i);
	                stop();
	            }
			}
		};
		splashThread.start();
	}

}






/*try {
	if (HttpHelper.isNetworkAvailable(MainApplication.this)) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				MainApplication.this).setCancelable(true)
				.setTitle("友情提示……").setMessage(
						"网络连接不可用，请设置网络连接。");
		DialogInterface.OnClickListener l = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog,
					int which) {
				// TODO Auto-generated method stub
				if (which == DialogInterface.BUTTON_POSITIVE) {
					Intent intent = new Intent(
							Settings.ACTION_WIRELESS_SETTINGS);
					startActivity(intent);
					finish();
				} else if (which == DialogInterface.BUTTON_NEGATIVE) {
					dialog.dismiss();
				}
			}
		};
		builder.setPositiveButton("设置", l);
		builder.setNegativeButton(android.R.string.cancel, l);
		builder.show();
	}
} catch (Exception e) {

	e.printStackTrace();
} finally {
	
	Intent i = new Intent(MainApplication.this,
			RemainSearcher.class);
	MainApplication.this.startActivity(i);
}*/