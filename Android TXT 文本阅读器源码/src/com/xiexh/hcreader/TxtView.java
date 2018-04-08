package com.xiexh.hcreader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.http.util.EncodingUtils;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class TxtView extends Activity {
	
	private final String TAG = "[HcReader]";
	private String fileName;
	/**
	 * 
	 */
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.txtview);
		Log.d(TAG, "TxtViewOnCreate");
		
		fileName = this.getIntent().getStringExtra("file");
		updateView( fileName,"GB2312" );
		
		OnClickListener listener = new OnClickListener(){
			public void onClick(View v){
				switch(v.getId()){
					case R.id.gb2312:
						updateView( fileName,"GB2312" );
						break;
					case R.id.utf8:
						updateView( fileName,"UTF8" );
						break;
				}
			}
		};
		findViewById(R.id.gb2312).setOnClickListener(listener);
		findViewById(R.id.utf8).setOnClickListener(listener);
	}
	public void updateView( String fileName,String encoding ){
		byte[] data = openFile(fileName);
		String display = EncodingUtils.getString(data, encoding);
		TextView tv = (TextView)findViewById(R.id.txtView);
		tv.setText(display);
	}
	public byte[] openFile(String fileName){
		try {
			File file = new File(fileName);
			FileInputStream in = new FileInputStream(file);
			int length = (int)file.length();
			byte[] temp = new byte[length];
			in.read(temp, 0, length);
			return temp;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(TAG, e.toString());
			return null;
		}
	}
}
