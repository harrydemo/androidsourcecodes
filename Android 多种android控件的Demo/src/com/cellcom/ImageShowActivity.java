package com.cellcom;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;


//��ʾ����Ч��
public class ImageShowActivity extends Activity implements ViewSwitcher.ViewFactory{

	private ImageSwitcher mSwitcher;
	private Gallery gallery;
	private Context mContext;

    private Integer[] mImageIds = {
            R.drawable.sample_0, R.drawable.sample_1, R.drawable.sample_2,
            R.drawable.sample_3, R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7,R.drawable.sample_8,
            R.drawable.sample_9,R.drawable.sample_10,R.drawable.sample_11};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_show);
		setTitle("���ͼƬ");
		mSwitcher=(ImageSwitcher)findViewById(R.id.switcher);
		gallery=(Gallery)findViewById(R.id.gallery);
		
		mSwitcher.setFactory(this);
		mSwitcher.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
		mSwitcher.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));
		
		gallery.setAdapter(new ImageAdapter(this));
		gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> adapter, View view,
					int position, long id) {
				mSwitcher.setImageResource(mImageIds[position]);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
			
		});
	}
	
	public class ImageAdapter extends BaseAdapter{

		public ImageAdapter(Context context){
			mContext=context;
		}
		
		@Override
		public int getCount() {
			return mImageIds.length;
		}

		@Override
		public Object getItem(int position) {
			
			return position;
		}

		@Override
		public long getItemId(int position) {
			
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView i=new ImageView(mContext);
			i.setImageResource(mImageIds[position]);
			i.setAdjustViewBounds(true);
			i.setLayoutParams(new Gallery.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
			i.setBackgroundResource(R.drawable.picture_frame);
			return i;
		}
		
	}

	@Override
	public View makeView() {
		ImageView i=new ImageView(this);
		i.setBackgroundColor(0xFF000000);
		i.setScaleType(ImageView.ScaleType.FIT_CENTER);
		i.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
		return i;
	}
}
