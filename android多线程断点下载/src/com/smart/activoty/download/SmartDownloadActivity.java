package com.smart.activoty.download;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.smart.impl.SmartDownloadProgressListener;
import com.smart.impl.SmartFileDownloader;

public class SmartDownloadActivity extends Activity {
    private ProgressBar downloadbar;
    private EditText pathText;
    private TextView resultView;
    private Handler handler = new Handler(){

		@Override//信息
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				int size = msg.getData().getInt("size");
				downloadbar.setProgress(size);
				float result = (float)downloadbar.getProgress()/ (float)downloadbar.getMax();
				int p = (int)(result*100);
				resultView.setText(p+"%");
				if(downloadbar.getProgress()==downloadbar.getMax())
					Toast.makeText(SmartDownloadActivity.this, R.string.success, 1).show();
				break;

			case -1:
				Toast.makeText(SmartDownloadActivity.this, R.string.error, 1).show();
				break;
			}
			
		}    	
    };
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button button = (Button)this.findViewById(R.id.button);
        downloadbar = (ProgressBar)this.findViewById(R.id.downloadbar);
        pathText = (EditText)this.findViewById(R.id.path);
        resultView = (TextView)this.findViewById(R.id.result);
        button.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				String path = pathText.getText().toString();
				if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
					File dir = Environment.getExternalStorageDirectory();//文件保存目录
					download(path, dir);
				}else{
					Toast.makeText(SmartDownloadActivity.this, R.string.sdcarderror, 1).show();
				}
			}
		});
    }
    //对于UI控件的更新只能由主线程(UI线程)负责，如果在非UI线程更新UI控件，更新的结果不会反映在屏幕上，某些控件还会出错
    private void download(final String path, final File dir){
    	new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					SmartFileDownloader loader = new SmartFileDownloader(SmartDownloadActivity.this, path, dir, 3);
					int length = loader.getFileSize();//获取文件的长度
					downloadbar.setMax(length);
					loader.download(new SmartDownloadProgressListener(){
						@Override
						public void onDownloadSize(int size) {//可以实时得到文件下载的长度
							Message msg = new Message();
							msg.what = 1;
							msg.getData().putInt("size", size);							
							handler.sendMessage(msg);
						}});
				} catch (Exception e) {
					Message msg = new Message();//信息提示
					msg.what = -1;
					msg.getData().putString("error", "下载失败");//如果下载错误，显示提示失败！
					handler.sendMessage(msg);
				}
			}
		}).start();//开始
    	
    }
}