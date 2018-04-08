package com.ly.control;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.ly.control.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class PhotoActivity extends Activity {
	private Button bt1,bt2;
	private SurfaceView sv;
	private SurfaceHolder sh;
	private Camera camera;
	private boolean isPreview=false;
	private ImageView iv;
	private VideoView videoView;
	private String filename="";
	private String name="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo);
	
		bt1=(Button)findViewById(R.id.Button01);
		bt2=(Button)findViewById(R.id.Button02);
		
		iv = (ImageView) findViewById(R.id.ImageView01);
		
	//	videoView = (VideoView) findViewById(R.id.videoview);
		
		sv=(SurfaceView)findViewById(R.id.SurfaceView01);
		sh=sv.getHolder();
		sh.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		reset();
		bt1.setOnClickListener(l);
		bt2.setOnClickListener(l);
		
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		
		if (requestCode == 1)
		{
			if (resultCode == Activity.RESULT_OK)
			{
				Uri uri = data.getData();
				Cursor cursor = this.getContentResolver().query(uri, null,
						null, null, null);
				if (cursor.moveToFirst())
				{

					String videoPath = cursor.getString(cursor
							.getColumnIndex("_data"));
					videoView.setVideoURI(Uri.parse(videoPath));
					// 设置视频控制器
					videoView.setMediaController(new MediaController(this));
					// 开始播放视频
					videoView.start();

				}

			}

		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	

	public void stop(){
		if(camera!=null&&isPreview){
			camera.stopPreview();
			camera.release();
			isPreview=false;
		}
	}
	public void reset(){
		if(!isPreview){
			camera = Camera.open();
		}if(camera!=null&&!isPreview){
			Parameters p = camera.getParameters();
			p.setPictureFormat(PixelFormat.JPEG);
			p.setPictureSize(320, 200);
			p.setPreviewSize(320, 200);
			camera.setParameters(p);
			try {
				camera.setPreviewDisplay(sh);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			camera.startPreview();
			isPreview = true;
		}
	}
	 private OnClickListener l=new OnClickListener(){
	    	
	    	public void onClick(View v) {
	    	if(v==bt1){	
	    		camera.takePicture(shutter, pic, pic1);
	    	  }
	    	
	    	if(v==bt2){
	    	
	    		Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
	    		startActivityForResult(intent, 1);
	    		
	    	}    	
	    }
    };
    ShutterCallback shutter = new ShutterCallback(){
    	public void onShutter() {
    		// TODO Auto-generated method stub  		
    	}
    	
    };
	    
	
	    PictureCallback pic1=new PictureCallback() {
			
	    	
			public void onPictureTaken(byte[] data, Camera camera) {
				// TODO Auto-generated method stub
				Bitmap bit= BitmapFactory.decodeByteArray(data,0,data.length);
				iv.setImageBitmap(bit);
				
				stop();
				
				FileOutputStream out = null;
				name = System.currentTimeMillis()+".jpg";
				filename = "/sdcard/"+name;
				
				try {
					out = new FileOutputStream(filename);
					out.write(data);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				reset();
				
				/*//***图片写入内存卡
				FileOutputStream b = null;
				File file = new File("/sdcard/myImage/");
				file.mkdirs();// 创建文件夹
				String fileName = "/sdcard/myImage/1.jpg";
				try {
					b = new FileOutputStream(fileName);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				bit.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
				//****
	*/			
			}
		};
	    PictureCallback pic=new PictureCallback(){

			public void onPictureTaken(byte[] data, Camera camera) {
				// TODO Auto-generated method stub				
			}
	    	
	    };
		protected void onDestroy() {
			super.onDestroy();
			stop();
		}
}
