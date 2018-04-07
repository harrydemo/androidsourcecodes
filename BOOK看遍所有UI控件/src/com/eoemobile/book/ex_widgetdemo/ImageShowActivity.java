package com.eoemobile.book.ex_widgetdemo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import android.widget.Gallery.LayoutParams;

public class ImageShowActivity extends Activity implements AdapterView.OnItemSelectedListener, ViewSwitcher.ViewFactory
{
	CheckBox plain_cb;
	CheckBox serif_cb;
	CheckBox italic_cb;
	CheckBox bold_cb;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.image_show);
		setTitle("ImageShowActivity");

		mSwitcher = (ImageSwitcher) findViewById(R.id.switcher);
		mSwitcher.setFactory(this);
		mSwitcher.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
		mSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));

		Gallery g = (Gallery) findViewById(R.id.gallery);
		g.setAdapter(new ImageAdapter(this));
		g.setOnItemSelectedListener(this);
	}

	public void onItemSelected(AdapterView parent, View v, int position, long id)
	{
		mSwitcher.setImageResource(mImageIds[position]);
	}

	public void onNothingSelected(AdapterView parent)
	{
	}

	public View makeView()
	{
		ImageView i = new ImageView(this);
		i.setBackgroundColor(0xFF000000);
		i.setScaleType(ImageView.ScaleType.FIT_CENTER);
		i.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		return i;
	}

	private ImageSwitcher mSwitcher;

	public class ImageAdapter extends BaseAdapter
	{
		public ImageAdapter(Context c)
		{
			mContext = c;
		}

		public int getCount()
		{
			return mThumbIds.length;
		}

		public Object getItem(int position)
		{
			return position;
		}

		public long getItemId(int position)
		{
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent)
		{
			ImageView i = new ImageView(mContext);

			i.setImageResource(mThumbIds[position]);
			i.setAdjustViewBounds(true);
			i.setLayoutParams(new Gallery.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			i.setBackgroundResource(R.drawable.picture_frame);
			return i;
		}

		private Context mContext;

	}

	private Integer[] mThumbIds =
	{ R.drawable.sample_thumb_0, R.drawable.sample_thumb_1, R.drawable.sample_thumb_2, R.drawable.sample_thumb_3, R.drawable.sample_thumb_4, R.drawable.sample_thumb_5, R.drawable.sample_thumb_6, R.drawable.sample_thumb_7 };

	private Integer[] mImageIds =
	{ R.drawable.sample_0, R.drawable.sample_1, R.drawable.sample_2, R.drawable.sample_3, R.drawable.sample_4, R.drawable.sample_5, R.drawable.sample_6, R.drawable.sample_7 };

}