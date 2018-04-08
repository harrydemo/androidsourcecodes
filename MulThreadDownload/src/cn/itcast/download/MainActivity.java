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
    //����������Ϣ���з�����Ϣ����Handler������ʱ���Զ��󶨵�Handler������ʱ���ڵ��߳����󶨵���Ϣ����
    private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				downloadbar.setProgress(msg.getData().getInt("size"));//�ѵ�ǰ�Ѿ����ص����ݳ�������Ϊ�������ĵ�ǰ�̶�
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
    
    //��UI�ؼ��ĸ���ֻ�������߳�(UI�߳�)����,�������UI�̸߳��¿ؼ������º��ֵ���ᱻ�ػ浽��Ļ��
	private void download(final String path, final File saveDir) {
		new Thread(new Runnable() {			
			@Override
			public void run() {
				try {
					FileDownloader loader = new FileDownloader(MainActivity.this, path, saveDir, 3);
					downloadbar.setMax(loader.getFileSize());//���ý����������̶�Ϊ�ļ��Ĵ�С
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