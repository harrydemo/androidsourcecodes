package com.tyj.onepiece;

import android.app.Activity;
import android.util.AttributeSet;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

public class OnePieceGame extends Activity {
	/** Called when the activity is first created. */
	private ProgressBar pb;
	private TextView show_RemainTime;
	private CtrlView cv;
	public static final int START_ID = Menu.FIRST;
	public static final int REARRARY_ID = Menu.FIRST + 1;
	public static final int END_ID = REARRARY_ID + 1;
	private int dormant = 1000;
	private boolean isCancel=true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		findViews();
		mRedrawHandler.sleep(dormant);

	}

	private RefreshHandler mRedrawHandler = new RefreshHandler();

	class RefreshHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			if(isCancel){
				run();
			}else{}						
		}

		public void sleep(long delayMillis) {
			this.removeMessages(0);// 移除信息队列中最顶部的信息（从顶部取出信息）
			sendMessageDelayed(obtainMessage(0), delayMillis);// 获得顶部信息并延时发送
		}
	};

	public void run() {
		if (cv.PROCESS_VALUE > 0 && cv.much != 0) {
			cv.PROCESS_VALUE--;
			pb.setProgress(cv.PROCESS_VALUE);
			show_RemainTime.setText(String.valueOf(cv.PROCESS_VALUE));
			mRedrawHandler.sleep(dormant);
		} else if (cv.PROCESS_VALUE == 0 && cv.much != 0) {
			cv.setEnabled(false);
			dialogForFail().show();
		} else if (cv.PROCESS_VALUE != 0 && cv.much == 0) {
			cv.setEnabled(false);
			dialogForSucceed().show();
		}
	}

	private void findViews() {
		pb = (ProgressBar) findViewById(R.id.pb);
		show_RemainTime = (TextView) findViewById(R.id.show_remainTime);
		cv = (CtrlView) findViewById(R.id.cv);
		pb.setMax(cv.GAMETIME);
		pb.incrementProgressBy(-1);
		pb.setProgress(cv.PROCESS_VALUE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, START_ID, 0, R.string.newgame);
		menu.add(0, REARRARY_ID, 0, R.string.rearrage);
		menu.add(0, END_ID, 0, R.string.exit);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case START_ID:
			newPlay();
			break;
		case REARRARY_ID:
			cv.rearrange();
			cv.PROCESS_VALUE = cv.PROCESS_VALUE - 5;
			pb.setProgress(cv.PROCESS_VALUE);
			break;
		case END_ID:
			isCancel=false;
			finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onStop() {
		isCancel=false;
		pb = null;
		cv = null;
		super.onStop();
	}
	
	@Override
	protected void onDestroy(){
		isCancel=false;
		super.onDestroy();
	}
	
	@Override
	protected void onStart(){
		isCancel=false;
		newPlay();
		isCancel=true;
		super.onStart();
		
	}
//	@Override
//	protected void onRestart(){
//		cv.reset();
//		super.onRestart();
//	}
//	

	public void newPlay() {
		cv.reset();
		pb.setProgress(cv.GAMETIME);
		cv.PROCESS_VALUE = cv.GAMETIME;
		mRedrawHandler.sleep(dormant);
		cv.setEnabled(true);
	}

	public AlertDialog dialogForSucceed() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.icon).setMessage(R.string.succeedInfo)
				.setPositiveButton(R.string.next,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dormant = dormant - 300;
								newPlay();
							}
						}).setNeutralButton(R.string.again_challenge,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								newPlay();
							}
						});
		return builder.create();
	}

	public AlertDialog dialogForFail() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.icon).setMessage(R.string.failInfo)
				.setPositiveButton(R.string.again_challenge,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								newPlay();
							}
						}).setNegativeButton(R.string.exit,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								isCancel=false;
								finish();
							}
						});
		return builder.create();
	}

}