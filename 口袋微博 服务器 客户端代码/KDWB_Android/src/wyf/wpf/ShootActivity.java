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
	Camera camera = null;			//����Camera��������
	SurfaceView sv;					//SurfaceView��������
	byte [] photoData = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 	//����ȫ��
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,  
		              WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.shoot);			//���õ�ǰ��Ļ
		sv = (SurfaceView)findViewById(R.id.svCamera);
		sv.getHolder().addCallback(this);
		sv.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);		//��������
		ImageButton ib = (ImageButton)findViewById(R.id.ibShoot);
		ib.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {					//�������հ�ť
				camera.takePicture(myShutterCallback, myRawCallback, myjpegCallback);
			}
		});
		Button btnUploadPhoto = (Button)findViewById(R.id.btnUploadPhoto);		//���Button����
		btnUploadPhoto.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {									//����ȷ���ϴ���ť
				if(photoData == null){
					Toast.makeText(ShootActivity.this, "��������һ����Ƭ��", Toast.LENGTH_LONG).show();
					return;
				}
				Intent intent = ShootActivity.this.getIntent();
				intent.setClass(ShootActivity.this,UploadActivity.class);		//����Intent
				intent.putExtra("data", photoData);			//��ͼƬ��������ΪExtra
				startActivity(intent);						//�����ϴ�ͼƬActivity
			}
		});
		Button btnReShoot = (Button)findViewById(R.id.btnShootBack);			//���ذ�ť
		btnReShoot.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ShootActivity.this.finish();
			}
		});
	}
	public void initCamera(){
		if(!isShown){		//��û�г�ʼ��
			camera = Camera.open();
		}
		if(camera != null && !isShown){
			try{
				Camera.Parameters params = camera.getParameters();
				params.setPictureFormat(PixelFormat.JPEG);		//����ͼƬ��ʽ
				params.setPreviewSize(CAMERA_WIDTH,CAMERA_HEIGHT);	//���óߴ�
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
		initCamera();				//��ʼ�����
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		shutdownCamera();		//�ر����
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
			photoData = data;			//�����Ƭ����
			ImageView iv = (ImageView)findViewById(R.id.ivCamera);		//���ImageView��������
			Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
			iv.setImageBitmap(bmp);
			shutdownCamera();			//�ر������
			initCamera();//��ʼ�����
		}
	};
}