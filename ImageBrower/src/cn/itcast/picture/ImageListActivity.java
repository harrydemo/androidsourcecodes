package cn.itcast.picture;

import java.io.File;

import cn.itcast.service.ImageServiceBean;
import android.app.Activity;
import android.os.Bundle;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ImageListActivity extends Activity {
	private ImageSwitcher imageSwitcher;
	private Gallery gallery;
	private int index = 1;//��¼��ǰ�����ͼƬ����
	private ImageServiceBean imageService;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		imageService = new ImageServiceBean(this);
		
		imageSwitcher = (ImageSwitcher) findViewById(R.id.switcher);
		imageSwitcher.setFactory(new ImageSwitcher.ViewFactory() {
			@Override
			public View makeView() {
				 ImageView i = new ImageView(ImageListActivity.this);
				 i.setImageURI(imageService.getBigBitmap(index));//Ĭ����ʾ�ڶ���ͼ
			     i.setBackgroundColor(0xFF000000);
			     i.setScaleType(ImageView.ScaleType.FIT_CENTER);
			     i.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			     return i;
			}
		});
		imageSwitcher.setOnClickListener(new View.OnClickListener() {//���ȫ����ʾͼƬ			
			@Override
			public void onClick(View v) {
				showPicture();
			}
		});
		
		gallery = (Gallery) findViewById(R.id.gallery);
		gallery.setAdapter(new ImageAdapter(this));//������		
		gallery.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				imageSwitcher.setImageURI(imageService.getBigBitmap(position));
				index = position;
			}
		});
		gallery.setSelection(index);
		
		TextView quit = (TextView) findViewById(R.id.quit);
		quit.setOnClickListener(new View.OnClickListener() {//�˳�Ӧ��		
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	//��ȫ����ʾͼƬ
	private void showPicture() {
		Intent intent = new Intent(ImageListActivity.this, ShowActivity.class);
		intent.putExtra("imageuri", imageService.getBigBitmap(index).toString());
		startActivity(intent);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) { //�������¼���ע��Gallery�Ѿ����������Ҽ��ƶ�ͼƬ,����ֻ�账��ȷ�ϼ���ȫͼ
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_CENTER://ȷ�ϼ�
			index = gallery.getSelectedItemPosition();//��ȡ��ǰͼƬ������
			imageSwitcher.setImageURI(imageService.getBigBitmap(index));//��ʾ��Ӧ����ͼƬ
			showPicture();
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(this.getCacheDir().exists()){//�������ͼƬ
			for(File file : this.getCacheDir().listFiles()){
				file.delete();
			}
		}
		android.os.Process.killProcess(android.os.Process.myPid());//ɱ�����̣��ر�Ӧ��
	}

	private final class ImageAdapter extends BaseAdapter {
		int mGalleryItemBackground;
		private Context mContext;
		public ImageAdapter(Context c) {
			mContext = c;
			//ʹ����res/values/attrs.xml�е�<declare-styleable>�Զ��� ��Gallery����.
			TypedArray a = obtainStyledAttributes(R.styleable.Gallery);
			/* ��Gallery�����л�ȡandroid:galleryItemBackground��ָ�����Դid��
			 * ����Դ���������ڿؼ��������ѡ�е�һϵ���¼��иı䱳��ͼ,����ͼʹ�õ���NicePath��ʽ
			 * ��Դ��androidԴ�����λ�ã�core\res\res\drawable\gallery_item_background.xml */
			mGalleryItemBackground = a.getResourceId(R.styleable.Gallery_android_galleryItemBackground, 0);
			/* �ö����styleable�����ܹ�����ʹ�� */
			a.recycle();
		}

		/* ���ǵķ���getCount,����ͼƬ��Ŀ */
		public int getCount() {
			return imageService.getLength();
		}

		/* ���ǵķ���getItemId,����ͼ�������id */
		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}
		/* ���ǵķ���getView,����һView���� */
		public View getView(int position, View convertView, ViewGroup parent) {			
			ImageView i = new ImageView(mContext);
			/* ����ͼƬ��imageView���� */
			i.setImageURI(imageService.getSmallBitmap(position));
			/* ��������ͼƬ�Ŀ�� */
			i.setScaleType(ImageView.ScaleType.FIT_XY);	
			/* ��������Layout�Ŀ�� */
			i.setLayoutParams(new Gallery.LayoutParams(70, 60));
			/* ����Gallery����ͼ */
			i.setBackgroundResource(mGalleryItemBackground);
			return i;
		}
	}
}
