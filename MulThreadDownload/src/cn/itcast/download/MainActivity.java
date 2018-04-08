package cn.itcast.download;

import java.io.File;

import cn.itcast.net.download.DownloadProgressListener;
import cn.itcast.net.download.FileDownloader;

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

public class MainActivity extends Activity {
    private EditText pathText;
    private ProgressBar downloadbar;
    private TextView resultView;
    //它用于往消息队列发送消息，当Handler被创建时会自动绑定到Handler被创建时所在的线程所绑定的消息队列
    private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				downloadbar.setProgress(msg.getData().getInt("size"));//把当前已经下载的数据长度设置为进度条的当前刻度
				float num = (float)downloadbar.getProgress() / (float)downloadbar.getMax();
				int result = (int)(num * 100);
				resultView.setText(result + "%");
				if(downloadbar.getProgress() == downloadbar.getMax()){
					Toast.makeText(MainActivity.this, R.string.suceess, 1).show();
				}
				break;

			case -1:
				Toast.makeText(MainActivity.this, R.string.error, 1).show();
				break;
			}
		}    	
    };
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        pathText = (EditText) findViewById(R.id.path);
        downloadbar = (ProgressBar) findViewById(R.id.downloadbar);
        resultView = (TextView) findViewById(R.id.result);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
					String path = pathText.getText().toString();
					download(path, Environment.getExternalStorageDirectory());
				}else{
					Toast.makeText(MainActivity.this, R.string.sdcarderror, 1).show();
				}
			}
		});
    }
    
    //对UI控件的更新只能由主线程(UI线程)负责,如果不在UI线程更新控件，更新后的值不会被重绘到屏幕上
	private void download(final String path, final File saveDir) {
		new Thread(new Runnable() {			
			@Override
			public void run() {
				try {
					FileDownloader loader = new FileDownloader(MainActivity.this, path, saveDir, 3);
					downloadbar.setMax(loader.getFileSize());//设置进度条的最大刻度为文件的大小
					loader.download(new DownloadProgressListener(){
						@Override
						public void onDownloadSize(int size) {
							Message msg = new Message();
							msg.what = 1;
							msg.getData().putInt("size", size);
							handler.sendMessage(msg);
							//msg.target = handler;							
						}});
				} catch (Exception e) {
					Message msg = new Message();
					msg.what = -1;
					handler.sendMessage(msg);
				}
			}
		}).start();
	}
}