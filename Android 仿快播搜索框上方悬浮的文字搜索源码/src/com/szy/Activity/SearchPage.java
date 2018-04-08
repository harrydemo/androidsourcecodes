package com.szy.Activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.szy.Activity.R;
import com.way.chat.activity.KeywordsView;

public class SearchPage extends Activity implements View.OnClickListener {
	private String[] totalKeys = null;
	private  String[] key_words=new String[15];
//	= { "QQ", "单机", "联网", "游戏", "美女",
//			"冒险", "uc", "手机", "app", "谷歌" };
	protected static final String TAG = "SearchPage";
	
	private KeywordsView showKeywords = null;
	private LinearLayout searchLayout = null;
	
	/**
	 * 搜索栏
	 */

	private GestureDetector mggd;
	/**
	 * 判断是在外页面还是内页面
	 */
	private boolean isOutter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.searchpage);
		searchLayout = (LinearLayout) this.findViewById(R.id.searchContent);
		
		showKeywords = (KeywordsView) this.findViewById(R.id.word);
		showKeywords.setDuration(2000l);
		showKeywords.setOnClickListener(this);
		this.mggd =new GestureDetector(new Mygdlinseter());
		showKeywords.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
			    return mggd.onTouchEvent(event); //注册点击事件
			}
		});
		isOutter = true;
		
		handler.sendEmptyMessage(Msg_Start_Load);

		
		
	}
	private String[]getRandomArray(){
		if (totalKeys != null && totalKeys.length > 0) {
			String[] keys = new String[15];
			List<String> ks = new ArrayList<String>();
			for (int i = 0; i < totalKeys.length; i++) {
				ks.add(totalKeys[i]);
			}
			for (int i = 0; i < keys.length; i++) {
				int k = (int) (ks.size() * Math.random());
				keys[i] = ks.remove(k);
				if(keys[i] == null)
					System.out.println("nulnulnulnulnul");	
			}
			System.out.println("result's length = "+keys.length);
			return keys;
		}
		return 	new String[]{ "QQ", "单机", "联网", "游戏", "美女",
				"冒险", "uc", "安卓", "app", "谷歌","多多米","财迷","快播","YY","MSN" };
	}

	private static final int Msg_Start_Load = 0x0102;
	private static final int Msg_Load_End = 0x0203;
	
	private LoadKeywordsTask task = null;
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case Msg_Start_Load:

				
					task = new LoadKeywordsTask();
					new Thread(task).start();
				
				break;
			case Msg_Load_End:
				showKeywords.rubKeywords();
				feedKeywordsFlow(showKeywords, key_words);
				showKeywords.go2Shwo(KeywordsView.ANIMATION_IN);
				break;
			}
			
		}
	};
	private class LoadKeywordsTask implements Runnable{
		@Override
		public void run() {
			try {
				
				key_words = getRandomArray();
				if(key_words.length>0)
					handler.sendEmptyMessage(Msg_Load_End);
			} catch (Exception e) {
			}
		}
	}
	private void feedKeywordsFlow(KeywordsView keyworldFlow, String[] arr) {
		for (int i = 0; i < KeywordsView.MAX; i++) {
			String tmp = arr[i];
			keyworldFlow.feedKeyword(tmp);
		}
	}

	

	class Mygdlinseter implements OnGestureListener {
		@Override
		public boolean onDown(MotionEvent e) {
			return true;
		}
		@Override
		public void onShowPress(MotionEvent e) {
		}
		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			return false;
		}
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
				float distanceY) {
			return false;
		}
		@Override
		public void onLongPress(MotionEvent e) {
		}
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if (e2.getX() - e1.getX() > 100) { //右滑
				key_words = getRandomArray();
				showKeywords.rubKeywords();
				feedKeywordsFlow(showKeywords, key_words);
				showKeywords.go2Shwo(KeywordsView.ANIMATION_OUT);
				return true;
			}
			if (e2.getX() - e1.getX() < -100) {//左滑
				key_words = getRandomArray();
				showKeywords.rubKeywords();
				feedKeywordsFlow(showKeywords, key_words);
				showKeywords.go2Shwo(KeywordsView.ANIMATION_IN);
				return true;
			}
			if (e2.getY() - e1.getY() < -100) {//上滑
				key_words = getRandomArray();
				showKeywords.rubKeywords();
				feedKeywordsFlow(showKeywords, key_words);
				showKeywords.go2Shwo(KeywordsView.ANIMATION_IN);
				return true;
			}
			if (e2.getY() - e1.getY() > 100) {//下滑
				key_words = getRandomArray();
				showKeywords.rubKeywords();
				feedKeywordsFlow(showKeywords, key_words);
				showKeywords.go2Shwo(KeywordsView.ANIMATION_OUT);
				return true;
			}
			return false;
		}
	}
 

	@Override
	public void onClick(View v) {
		System.out.println("V"+v);
		// TODO Auto-generated method stub
		if(isOutter){
			isOutter = false;
		
			String kw = ((TextView) v).getText().toString();
			Log.i(TAG, "keywords = "+kw);
			if (!kw.trim().equals("")) {			
				searchLayout.removeAllViews();

			}
			Toast.makeText(this, "选中的内容是：" + ((TextView) v).getText().toString(), 1)
			.show();
		}
		
	}

	
	
	/**
	 * 处理返回按键事件
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		switch(keyCode){
		case KeyEvent.KEYCODE_BACK:
			
			if(!isOutter){
				isOutter = true;
				searchLayout.removeAllViews();
				searchLayout.addView(showKeywords);
				/**
				 * 将自身设为不可动作
				 */
			
				/**
				 * 将搜索栏清空
				 */
				
			}else{
				SearchPage.this.finish();
			/**
			 * 执行返回按键操作
			 */
			
			}
			
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}

}
