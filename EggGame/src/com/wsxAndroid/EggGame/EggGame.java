package com.wsxAndroid.EggGame;



import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class EggGame extends Activity {
    /** Called when the activity is first created. */
	private Button btn1;
	private Button btn2;
	private Button btn3;
	private final static String TAG="EggGame";
	private final static int REQUEST_CODE = 1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        btn1 = (Button) findViewById(R.id.btn1);
		btn2 = (Button) findViewById(R.id.btn2);
		btn3 = (Button) findViewById(R.id.btn3);
		btn1.setOnClickListener(listener);
		btn2.setOnClickListener(listener2);
		btn3.setOnClickListener(listener3);
    }
    private OnClickListener listener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent=new Intent(EggGame.this,EggActivity.class);
			startActivityForResult(intent, REQUEST_CODE);
		}
	};
    private OnClickListener listener2=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String str="作者：Wsxian QQ：857119258";
			Toast.makeText(EggGame.this, str, Toast.LENGTH_LONG).show();
		}
	};
	private OnClickListener listener3=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			System.exit(0);
		}
	};
    
    @Override
	public void onStart(){
		super.onStart();
		Log.i(TAG, "EggGame-->onStart");
	}
	@Override
	public void onResume(){
		super.onResume();
		Log.i(TAG, "EggGame-->onResume");
	}
	@Override
	public void onPause(){
		//不能进行比较耗时的操作
		//对数据进行保存，持久化操作
		super.onPause();
		Log.i(TAG, "EggGame-->onPause");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	@Override
	public void onStop(){
		super.onStop();
		Log.i(TAG, "EggGame-->onStop");
	}
	@Override
	public void onRestart(){
		super.onRestart();
		Log.i(TAG, "EggGame-->onRestart");
	}
	@Override
	public void onDestroy(){
		super.onDestroy();
		Log.i(TAG, "EggGame-->onDestroy");
	}
}