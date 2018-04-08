package com.sky_dreaming.ACDsee;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.sky_dreaming.ACDsee.gallery_view.ImageAdapter;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery.LayoutParams;

/**
 * ****************************************************************
 * 文件名称	: ImageActivity.java
 * 创建时间	: 2010-11-2 下午05:30:04
 * 文件描述	: 图片浏览器
 *****************************************************************
 */
public class ACDsee extends Activity implements
		AdapterView.OnItemSelectedListener, ViewSwitcher.ViewFactory {
	/** Called when the activity is first created. */

	/**
	 * 手机设备中图像列表
	 */
	private List<String> ImageList;

	private ImageSwitcher mSwitcher;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image);

		ImageList = getImagesFromSD();

		/**
		 * xml获取Switcher 
		 */
		mSwitcher = (ImageSwitcher) findViewById(R.id.switcher);
		mSwitcher.setFactory(this);

		mSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,
				android.R.anim.slide_in_left));

		mSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,
				android.R.anim.slide_out_right));

		/**
		 * xml获取Gallery
		 */
		Gallery g = (Gallery) findViewById(R.id.mygallery);

		
		if(ImageList == null || ImageList.size() == 0)
			return;
		
		/**
		 * 创建适配器，注册适配器
		 */
		g.setAdapter(new ImageAdapter(this, ImageList));

		g.setOnItemSelectedListener(this);

		/**
		 * 点击事件监听器
		 */
		g.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				
			}
		});
	}

	/**
	 * @return
	 */
	private List<String> getImagesFromSD() {
		List<String> imageList = new ArrayList<String>();

		File f = new File("/sdcard/");
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			f = new File(Environment.getExternalStorageDirectory().toString());
		}
		else
		{
			Toast.makeText(ACDsee.this, R.string.sdcarderror, 1).show();
			return imageList;
		}

		File[] files = f.listFiles();

		if(files == null || files.length == 0)
			return imageList;
		/**
		 * 将所有图像文件的路径存入ArrayList列表
		 */
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			if (isImageFile(file.getPath()))
				imageList.add(file.getPath());
		}
		return imageList;
	}

	/**
	 * @param fName
	 * @return
	 */
	private boolean isImageFile(String fName) {
		boolean re;
		String end = fName
				.substring(fName.lastIndexOf(".") + 1, fName.length())
				.toLowerCase();

		/**
		 * 依据文件扩展名判断是否为图像文件
		 */
		if (end.equals("jpg") || end.equals("gif") || end.equals("png")
				|| end.equals("jpeg") || end.equals("bmp")) {
			re = true;
		} else {
			re = false;
		}
		return re;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		/**
		 * 获取当前要显示的Image的路径
		 */
		String photoURL = ImageList.get(position);
		Log.i("A", String.valueOf(position));
		/**
		 * 设置当前要显示的Image的路径	 
		 */
		mSwitcher.setImageURI(Uri.parse(photoURL));
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}

	@Override
	public View makeView() {

		ImageView i = new ImageView(this);

		i.setBackgroundColor(0xFF000000);

		i.setScaleType(ImageView.ScaleType.FIT_XY);

		i.setLayoutParams(new ImageSwitcher.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		return i;
	}
}