package gl.test;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import gl.test.util.CropImage;
import gl.test.util.PaintView;
import gl.test.util.ZoomImage;
import gl.test.view.MenuView;
import gl.test.view.ToneMenuView;
import gl.test.view.ToneView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class Myjob2Activity extends Activity implements OnSeekBarChangeListener {

	private Button save, cancle;
	private Button zoom, rotate, scrawl, light, crap;
	private ImageView mimg;
	public String imagePath;
	public String filename;

	public Bitmap mBitmap;
	private ToneMenuView mTonMenuVie;
	private ToneView mToneView;

	private MenuView menuView;

	private static final int IMAGE_CUT = 1;

	private Matrix matrix = new Matrix();

	private LinearLayout linearLayout;

	/**
	 * ��ʱBitmap�ļ�
	 * */
	public Bitmap mTmpBmp;
	public String pictureName;
	public String data;
	private Paint mPaint;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		imagePath = "http://192.168.5.195:8080/MyjobServer/image/patient.jpg";
		filename = "test/jpg";
		initWidget();
	}

	private void initWidget() {
		// TODO Auto-generated method stub
		save = (Button) findViewById(R.id.save);
		save.setOnClickListener(new SaveOnclickListener());
		cancle = (Button) findViewById(R.id.cancle);
		cancle.setOnClickListener(new CancleOnclickListener());
		zoom = (Button) findViewById(R.id.zoom);// ����
		zoom.setOnClickListener(new ZoomOnclickListener());
		rotate = (Button) findViewById(R.id.rotate);// ��ת
		rotate.setOnClickListener(new RotateOnclickListener());
		scrawl = (Button) findViewById(R.id.scrawl);// Ϳѻ
		scrawl.setOnClickListener(new ScrawlOnclickListener());
		light = (Button) findViewById(R.id.light);// ����
		light.setOnClickListener(new LightOnclickListener());
		crap = (Button) findViewById(R.id.crap);// �ü�
		crap.setOnClickListener(new CrapOnclickListener());
		mimg = (ImageView) findViewById(R.id.img);
		mBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.patient);
		mTmpBmp = mBitmap.copy(Bitmap.Config.ARGB_8888, true);
		mimg.setImageBitmap(mBitmap);

		/*
		 * try { byte[] data = gl.test.util.ImageService.getImage(imagePath);
		 * 
		 * mBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
		 * 
		 * mTmpBmp = mBitmap.copy(Bitmap.Config.ARGB_8888, true);
		 * 
		 * mimg.setImageBitmap(mBitmap);
		 * 
		 * 
		 * } catch (Exception e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */

		// �ڳ�ʼ����ͬʱ����ͼƬ����ı����ļ�����

	}

	// ����ͼƬ
	public void saveFile(Bitmap bmp) {
		String sdStatue = Environment.getExternalStorageState();

		if (!sdStatue.endsWith(Environment.MEDIA_MOUNTED)) {

			System.out.println("��ǰSD������ʹ��---------------");
			return;
		}

		FileOutputStream b = null;
		File dirFile = new File("/sdcard/myImage");
		dirFile.mkdir();
		SimpleDateFormat sDattFormat = new SimpleDateFormat("yyyMMddhhmmss");
		data = sDattFormat.format(new Date());

		pictureName = "/sdcard/myImage/" + data + ".jpg";
		try {
			b = new FileOutputStream(pictureName);
			mTmpBmp.compress(Bitmap.CompressFormat.JPEG, 100, b);// ��λͼѹ����ΪJPEG��ʽ

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				b.flush();
				b.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	// �������̣߳�����ͼƬ
	private Runnable saveFileRunnable = new Runnable() {

		@Override
		public void run() {
			saveFile(mBitmap);

		}
	};
	public PaintView paintView;

	// �����޸Ĺ���ͼƬ
	private class SaveOnclickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			new Thread(saveFileRunnable).start();
			System.out.println("��ǰ��·�� =      ����     ======" + pictureName);

		}

	}

	/**
	 * ȡ������
	 * */

	private class CancleOnclickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			setResult(RESULT_CANCELED);
			finish();
			return;
		}

	}

	/**
	 * ����ͼƬ
	 * */

	private class ZoomOnclickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			initZoom();
			/*
			 * mTmpBmp = ZoomImage.zoomImg(mBitmap, (float) 0.5); reset();
			 */

		}

	}

	/**
	 * ��תͼƬ
	 * */
	private class RotateOnclickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			initRotate();
		}

	}

	// ��ʼ����תͼƬ�ؼ�
	private void initRotate() {

		// TODO Auto-generated method stub
		if (null == mTonMenuVie) {
			mTonMenuVie = new ToneMenuView(Myjob2Activity.this);
		}
		mTonMenuVie.show();
		mToneView = mTonMenuVie.getToneView();
		mToneView.setTag(3);
		mTonMenuVie.setSaturationBarListener(this);
	}

	/**
	 * ��ͼƬ����Ϳѻ
	 * 
	 * */
	private class ScrawlOnclickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mPaint = new Paint();
			mPaint.setAntiAlias(true);
			mPaint.setStyle(Paint.Style.STROKE);
			mPaint.setStrokeWidth(4);
			mPaint.setColor(0xFFFF0000);

			linearLayout = (LinearLayout) findViewById(R.id.imgLinearLayout);

			paintView = new PaintView(Myjob2Activity.this, mTmpBmp, mPaint);

			mBitmap = paintView.getBitmap();

		}

	}

	/**
	 * �ı�ͼƬ����
	 * */
	private class LightOnclickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			initTone();

		}

	}

	private void initTone() {
		// TODO Auto-generated method stub
		if (null == mTonMenuVie) {
			mTonMenuVie = new ToneMenuView(Myjob2Activity.this);
		}
		mTonMenuVie.show();

		mToneView = mTonMenuVie.getToneView();
		mToneView.setTag(1);

		mTonMenuVie.setSaturationBarListener(this);

	}

	private void initZoom() {
		// TODO Auto-generated method stub
		if (null == mTonMenuVie) {
			mTonMenuVie = new ToneMenuView(Myjob2Activity.this);
		}
		mTonMenuVie.show();
		mToneView = mTonMenuVie.getToneView();
		mToneView.setTag(2);
		mTonMenuVie.setSaturationBarListener(this);
		// mTonMenuView.setOnzoomBarListener(this);
	}

	/**
	 * ����ͼƬ
	 * */

	private class CrapOnclickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			saveFile(mBitmap);
			System.out.println("��ǰ��·�� =       ����     ======" + pictureName);
			Intent intent = CropImage.getImageClipIntent(pictureName);

			startActivityForResult(intent, IMAGE_CUT);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == IMAGE_CUT && data != null) {
			mTmpBmp = data.getParcelableExtra("data");
			reset();
			/*
			 * mimg.setImageBitmap(mTmpBmp); mBitmap = mTmpBmp;
			 */
		}

		else {
			mimg.setImageBitmap(mTmpBmp);
		}
	}

	/**
	 * ��������ͼƬ
	 * */

	private void reset() {

		mBitmap = mTmpBmp;
		mimg.setImageBitmap(mBitmap);
		mimg.invalidate();

	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		int flag = -1;
		System.out.println("��ǰ��seekbar" + seekBar.getTag());
		switch ((Integer) seekBar.getTag()) {

		case 1:

			flag = 1;
			mToneView.setLight(progress);
			System.out.println("����" + progress);
			mTmpBmp = mToneView.handleImage(mTmpBmp, flag);
			mimg.setImageBitmap(mTmpBmp);

			break;

		case 2:
			float scale = (float) progress / (float) 127;

			System.out.println("��ǰ�Ŀ��" + progress);
			System.out.println("��ǰ���ű���" + scale);
			System.out.println("��ǰλͼ�Ŀ�" + mBitmap.getWidth());

			mBitmap = ZoomImage.zoomImg(mTmpBmp, scale);
			mimg.setImageBitmap(mBitmap);

			break;

		case 3:
			// ����ͼ�����ת�Ƕ�
			matrix.setRotate(progress);
			// ��תͼ�񣬲������µ�Bitmap����
			System.out.println("��ǰ����ת�Ƕ�" + progress);
			mTmpBmp = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(),
					mBitmap.getHeight(), matrix, true);
			mimg.setImageBitmap(mTmpBmp);

		}

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

}