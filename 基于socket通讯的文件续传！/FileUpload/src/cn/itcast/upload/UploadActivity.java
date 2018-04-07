package cn.itcast.upload;

import java.io.File;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.io.RandomAccessFile;
import java.net.Socket;

import cn.itcast.service.FileService;
import cn.itcast.utils.StreamTool;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class UploadActivity extends Activity {
	private static final String TAG = "UploadActivity";
    private EditText filenameText;
    private TextView resultVew;
    private ProgressBar progressBar;
    private FileService fileService;
    private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			int size = msg.getData().getInt("size");
			progressBar.setProgress(size);
			float result = (float)progressBar.getProgress()/ (float)progressBar.getMax();
			int p = (int)(result*100);
			resultVew.setText(p+"%");
			if(progressBar.getProgress()==progressBar.getMax()){
				Toast.makeText(UploadActivity.this, R.string.success, 1).show();				
			}
		}
    };
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        fileService = new FileService(this);
        
        filenameText = (EditText) this.findViewById(R.id.filename);
        resultVew = (TextView) this.findViewById(R.id.result);
        progressBar = (ProgressBar) this.findViewById(R.id.uploadbar);
        Button button = (Button) this.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				String filename = filenameText.getText().toString();
				if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
					File uploadFile = new File(Environment.getExternalStorageDirectory(), filename);
					uploadfile(uploadFile);
				}else{
					Toast.makeText(UploadActivity.this, R.string.sdcarderror, 1).show();
				}
			}
		});
    }
    
    private void uploadfile(final File file){
    	new Thread(new Runnable() {			
			@Override
			public void run() {
				try {
					progressBar.setMax((int)file.length());
					String sourceid = fileService.find(file);
					Socket socket = new Socket("192.168.1.157", 7878);
		            OutputStream outStream = socket.getOutputStream(); 
		            String head = "Content-Length="+ file.length() + ";filename="+ file.getName() + ";sourceid="+
		            		(sourceid==null? "" : sourceid)+"\r\n";
		            outStream.write(head.getBytes());
		            
		            PushbackInputStream inStream = new PushbackInputStream(socket.getInputStream());	
					String response = StreamTool.readLine(inStream);
		            System.out.println(response);
		            String[] items = response.split(";");
		            String id = items[0].substring(items[0].indexOf("=")+1);//服务返回绑定该文件的资源id
					String position = items[1].substring(items[1].indexOf("=")+1);
					if(sourceid==null) fileService.save(file, id);
					
					RandomAccessFile fileOutStream = new RandomAccessFile(file, "r");
					fileOutStream.seek(Integer.valueOf(position));
					byte[] buffer = new byte[1024];
					int len = -1;
					int length = Integer.valueOf(position);
					while( (len = fileOutStream.read(buffer)) != -1){
						outStream.write(buffer, 0, len);
						length += len;
						Message msg = new Message();
						msg.getData().putInt("size", length);
						handler.sendMessage(msg);
					}
					fileOutStream.close();
					outStream.close();
		            inStream.close();
		            socket.close();
		            if(length==file.length()) fileService.delete(file);
		        } catch (Exception e) {                    
		            Log.e(TAG, e.toString());
		        }
			}
		}).start();
    }
}