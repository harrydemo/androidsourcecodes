package com.qiaozi.activity;


import java.util.Timer;
import java.util.TimerTask;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.Android.PlanarYUVLuminanceSource;
import com.google.zxing.common.HybridBinarizer;


import android.app.Activity;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
public class MainActivity extends Activity {
	/** Called when the activity is first created. */
	private SurfaceView sfvCamera;
	private SFHCamera sfhCamera;
	private ImageView imgView;
	private View centerView;
	private TextView txtScanResult;
	private Timer mTimer;
	private MyTimerTask mTimerTask;
	private Button bt;
	// ���ձ�׼HVGA
	final static int width = 480;
	final static int height = 320;
	int dstLeft, dstTop, dstWidth, dstHeight;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		this.setTitle("Android����/��ά��ʶ��Demo-----hellogv");
		imgView = (ImageView) this.findViewById(R.id.ImageView01);
		centerView = (View) this.findViewById(R.id.centerView);
		sfvCamera = (SurfaceView) this.findViewById(R.id.sfvCamera);
		sfhCamera = new SFHCamera(sfvCamera.getHolder(), width, height,
				previewCallback);
		txtScanResult=(TextView)this.findViewById(R.id.txtScanResult);
		bt=(Button) this.findViewById(R.id.bt);
		bt.setOnClickListener(btclick);
		// ��ʼ����ʱ��
		//mTimer = new Timer();
		//mTimerTask = new MyTimerTask();
		//mTimer.schedule(mTimerTask, 0, 80);
	}
	
    OnClickListener btclick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			

			if (dstLeft == 0) {//ֻ��ֵһ��
				dstLeft = centerView.getLeft() * width
						/ getWindowManager().getDefaultDisplay().getWidth();
				dstTop = centerView.getTop() * height
						/ getWindowManager().getDefaultDisplay().getHeight();
				dstWidth = (centerView.getRight() - centerView.getLeft())* width
						/ getWindowManager().getDefaultDisplay().getWidth();
				dstHeight = (centerView.getBottom() - centerView.getTop())* height
						/ getWindowManager().getDefaultDisplay().getHeight();
			}
			sfhCamera.AutoFocusAndPreviewCallback();
		
			
			
			//**************************
		}
       	
       };
	
	class MyTimerTask extends TimerTask {
		@Override
		public void run() {
			if (dstLeft == 0) {//ֻ��ֵһ��
				dstLeft = centerView.getLeft() * width
						/ getWindowManager().getDefaultDisplay().getWidth();
				dstTop = centerView.getTop() * height
						/ getWindowManager().getDefaultDisplay().getHeight();
				dstWidth = (centerView.getRight() - centerView.getLeft())* width
						/ getWindowManager().getDefaultDisplay().getWidth();
				dstHeight = (centerView.getBottom() - centerView.getTop())* height
						/ getWindowManager().getDefaultDisplay().getHeight();
			}
			sfhCamera.AutoFocusAndPreviewCallback();
		}
	}
	/**
	 *  �Զ��Խ������ͼƬ
	 */
	private Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {
		@Override
		public void onPreviewFrame(byte[] data, Camera arg1) {
			//ȡ��ָ����Χ��֡������
			PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(
					data, width, height, dstLeft, dstTop, dstWidth, dstHeight);
			//ȡ�ûҶ�ͼ
			Bitmap mBitmap = source.renderCroppedGreyscaleBitmap();
			//��ʾ�Ҷ�ͼ
			imgView.setImageBitmap(mBitmap);
			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
			MultiFormatReader reader = new MultiFormatReader();
			try {
				Result result = reader.decode(bitmap);
				String strResult = "BarcodeFormat:"
						+ result.getBarcodeFormat().toString() + "  text:"
						+ result.getText();
				txtScanResult.setText(strResult);
			} catch (Exception e) {
				txtScanResult.setText("Scanning");
			}
		}
	};
}