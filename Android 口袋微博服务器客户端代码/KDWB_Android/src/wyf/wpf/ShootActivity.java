package wyf.wpf;
import static wyf.wpf.ConstantUtil.CAMERA_HEIGHT;
import static wyf.wpf.ConstantUtil.CAMERA_WIDTH;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class ShootActivity extends Activity implements SurfaceHolder.Callback{
	boolean isShown = false;
	Camera camera = null;			//声明Camera对象引用
	SurfaceView sv;					//SurfaceView对象引用
	byte [] photoData = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 	//设置全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,  
		              WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.shoot);			//设置当前屏幕
		sv = (SurfaceView)findViewById(R.id.svCamera);
		sv.getHolder().addCallback(this);
		sv.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);		//设置类型
		ImageButton ib = (ImageButton)findViewById(R.id.ibShoot);
		ib.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {					//按下拍照按钮
				camera.takePicture(myShutterCallback, myRawCallback, myjpegCallback);
			}
		});
		Button btnUploadPhoto = (Button)findViewById(R.id.btnUploadPhoto);		//获得Button对象
		btnUploadPhoto.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {									//按下确定上传按钮
				if(photoData == null){
					Toast.makeText(ShootActivity.this, "请先拍摄一张照片！", Toast.LENGTH_LONG).show();
					return;
				}
				Intent intent = ShootActivity.this.getIntent();
				intent.setClass(ShootActivity.this,UploadActivity.class);		//创建Intent
				intent.putExtra("data", photoData);			//将图片数据设置为Extra
				startActivity(intent);						//启动上传图片Activity
			}
		});
		Button btnReShoot = (Button)findViewById(R.id.btnShootBack);			//返回按钮
		btnReShoot.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ShootActivity.this.finish();
			}
		});
	}
	public void initCamera(){
		if(!isShown){		//还没有初始化
			camera = Camera.open();
		}
		if(camera != null && !isShown){
			try{
				Camera.Parameters params = camera.getParameters();
				params.setPictureFormat(PixelFormat.JPEG);		//设置图片格式
				params.setPreviewSize(CAMERA_WIDTH,CAMERA_HEIGHT);	//设置尺寸
				camera.setParameters(params);
				camera.setPreviewDisplay(sv.getHolder());
				camera.startPreview();
			}catch(Exception e){
				e.printStackTrace();
			}
			isShown = true;
		}
	}
	public void shutdownCamera(){
		isShown = false;
		camera.stopPreview();
		camera.release();
		camera = null;
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {}
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		initCamera();				//初始化相机
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		shutdownCamera();		//关闭相机
	}
	ShutterCallback myShutterCallback = new ShutterCallback(){
		@Override
		public void onShutter(){}
	};
	PictureCallback myRawCallback = new PictureCallback(){
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {}
	};	
	PictureCallback myjpegCallback = new PictureCallback(){
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			photoData = data;			//获得相片数据
			ImageView iv = (ImageView)findViewById(R.id.ivCamera);		//获得ImageView对象引用
			Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
			iv.setImageBitmap(bmp);
			shutdownCamera();			//关闭照相机
			initCamera();//初始化相机
		}
	};
}