package com.zyq.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.view.SurfaceHolder.Callback;


public class MainActivity extends Activity {
    private SurfaceView surfaceView;
    private Camera camera;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
    	requestWindowFeature(Window.FEATURE_NO_TITLE);//没有标题
    	window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
    			WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
    	window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//高亮

        setContentView(R.layout.main);
        
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceView.getHolder().setFixedSize(512, 512);	//设置分辨率
        surfaceView.getHolder().addCallback(new SurfaceCallback());
    }
    
    private class SurfaceCallback implements Callback{
    	private boolean preview;
    	
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
		}

		public void surfaceCreated(SurfaceHolder holder) {
			try {
				WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
				Display display = wm.getDefaultDisplay();
				camera = Camera.open();
				Camera.Parameters parameters = camera.getParameters();
				parameters.setPreviewSize(display.getWidth(), display.getHeight());//设置预览照片的大小
				parameters.setPreviewFrameRate(5);//每秒5帧
				parameters.setPictureFormat(PixelFormat.JPEG);//设置照片的输出格式
				parameters.set("jpeg-quality", 85);//照片质量
				parameters.setPictureSize(display.getWidth(), display.getHeight());//设置照片的大小
				camera.setParameters(parameters);
				camera.setPreviewDisplay(surfaceView.getHolder());//通过SurfaceView显示取景画面
				camera.startPreview();//开始预览
				preview = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
			if(camera!=null){
				if(preview) camera.stopPreview();
				camera.release();
			}
		}
    	
    }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(camera!=null && event.getRepeatCount()==0){
			switch (keyCode) {
			case KeyEvent.KEYCODE_SEARCH:
				camera.autoFocus(null);//自动对焦
				return true;

			case KeyEvent.KEYCODE_CAMERA:
			case KeyEvent.KEYCODE_DPAD_CENTER:
				camera.takePicture(null, null, new TakePictureCallback());
				return true;
			case KeyEvent.KEYCODE_VOLUME_DOWN:
				camera.takePicture(null, null, new TakePictureCallback());
			return true;
			case KeyEvent.KEYCODE_VOLUME_UP:
				camera.takePicture(null, null, new TakePictureCallback());
			return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private class TakePictureCallback implements PictureCallback{

		public void onPictureTaken(byte[] data, Camera camera) {
			try {
//				Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
//				File file = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis()+ ".jpg");
//				FileOutputStream outStream = new FileOutputStream(file);
//				bitmap.compress(CompressFormat.JPEG, 100, outStream);
//				outStream.close();
//				camera.startPreview();
				Intent intent = new Intent(MainActivity.this,CropImage.class);
				Bundle bundle = new Bundle();
				bundle.putByteArray("picture", data);
				intent.putExtras(bundle);
				startActivity(intent);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	
		
	}
    
    
}