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
	private int index = 1;//记录当前浏览的图片索引
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
				 i.setImageURI(imageService.getBigBitmap(index));//默认显示第二张图
			     i.setBackgroundColor(0xFF000000);
			     i.setScaleType(ImageView.ScaleType.FIT_CENTER);
			     i.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			     return i;
			}
		});
		imageSwitcher.setOnClickListener(new View.OnClickListener() {//点击全屏显示图片			
			@Override
			public void onClick(View v) {
				showPicture();
			}
		});
		
		gallery = (Gallery) findViewById(R.id.gallery);
		gallery.setAdapter(new ImageAdapter(this));//绑定数据		
		gallery.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				imageSwitcher.setImageURI(imageService.getBigBitmap(position));
				index = position;
			}
		});
		gallery.setSelection(index);
		
		TextView quit = (TextView) findViewById(R.id.quit);
		quit.setOnClickListener(new View.OnClickListener() {//退出应用		
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	//打开全屏显示图片
	private void showPicture() {
		Intent intent = new Intent(ImageListActivity.this, ShowActivity.class);
		intent.putExtra("imageuri", imageService.getBigBitmap(index).toString());
		startActivity(intent);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) { //处理按键事件，注：Gallery已经处理了左右键移动图片,我们只需处理确认键打开全图
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_CENTER://确认键
			index = gallery.getSelectedItemPosition();//获取当前图片的索引
			imageSwitcher.setImageURI(imageService.getBigBitmap(index));//显示对应索引图片
			showPicture();
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(this.getCacheDir().exists()){//清除缓存图片
			for(File file : this.getCacheDir().listFiles()){
				file.delete();
			}
		}
		android.os.Process.killProcess(android.os.Process.myPid());//杀死进程，关闭应用
	}

	private final class ImageAdapter extends BaseAdapter {
		int mGalleryItemBackground;
		private Context mContext;
		public ImageAdapter(Context c) {
			mContext = c;
			//使用在res/values/attrs.xml中的<declare-styleable>自定义 的Gallery属性.
			TypedArray a = obtainStyledAttributes(R.styleable.Gallery);
			/* 从Gallery属性中获取android:galleryItemBackground所指向的资源id，
			 * 该资源的作用是在控件被点击或选中等一系列事件中改变背景图,背景图使用的是NicePath格式
			 * 资源在android源代码的位置：core\res\res\drawable\gallery_item_background.xml */
			mGalleryItemBackground = a.getResourceId(R.styleable.Gallery_android_galleryItemBackground, 0);
			/* 让对象的styleable属性能够反复使用 */
			a.recycle();
		}

		/* 覆盖的方法getCount,返回图片数目 */
		public int getCount() {
			return imageService.getLength();
		}

		/* 覆盖的方法getItemId,返回图像的数组id */
		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}
		/* 覆盖的方法getView,返回一View对象 */
		public View getView(int position, View convertView, ViewGroup parent) {			
			ImageView i = new ImageView(mContext);
			/* 设置图片给imageView对象 */
			i.setImageURI(imageService.getSmallBitmap(position));
			/* 重新设置图片的宽高 */
			i.setScaleType(ImageView.ScaleType.FIT_XY);	
			/* 重新设置Layout的宽高 */
			i.setLayoutParams(new Gallery.LayoutParams(70, 60));
			/* 设置Gallery背景图 */
			i.setBackgroundResource(mGalleryItemBackground);
			return i;
		}
	}
}
