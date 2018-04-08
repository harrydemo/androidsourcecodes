package com.zhoujh.appstore;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class IndexActivity extends Activity {
	
	private final String TAG = "ZhuoDanActivity";
	
	private Gallery mGallery;
	private ListView mListView;

	
	private int []galleryRes = null;

	private int []listViewRes = null;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
         this.requestWindowFeature(Window.FEATURE_NO_TITLE);
         
        setContentView(R.layout.index);
        
        initRes();
        findViews();
        setValues();
        setListeners();
    }
    
    
    private void findViews() {
    	mGallery = (Gallery)this.findViewById(R.id.gallery);
    	mListView = (ListView)this.findViewById(R.id.listview);
    }
    
    private void setValues() {
    	mGallery.setAdapter(new GalleryAdapter(this));
    	mGallery.setSelection(1);
    	
    	
    	mListView.setAdapter(new ListViewAdapter(this));
    	
    }
    
    private void setListeners() {
    	
  
    }
    
    private void initRes() {
    	galleryRes = new int[5];
    	galleryRes[0] = R.drawable.gallery5;
    	galleryRes[1] = R.drawable.gallery6;
    	galleryRes[2] = R.drawable.gallery5;
    	galleryRes[3] = R.drawable.gallery6;
    	galleryRes[4] = R.drawable.gallery5;
    	
    	listViewRes = new int[100];
    	for(int i=0; i<100; i++) {
    		listViewRes[i] =  R.drawable.icon_360;
    	}
    	
    }
    
    private class GalleryAdapter extends BaseAdapter {

    	private Context mContext;
    	
    	public GalleryAdapter(Context ctx) {
    		this.mContext = ctx;
    	}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return galleryRes.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView;
			if(convertView == null) {
				imageView = new ImageView(mContext);
				imageView.setAdjustViewBounds(true);
				imageView.setPadding(8, 1, 8, 1);
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			}else {
				imageView = (ImageView)convertView;
			}
			
			imageView.setImageResource(galleryRes[position]);
			
			return imageView;
		}
    	
    }
    
    private final class ListViewHolder {
    	public ImageView imageViewIcon;
    	public TextView appName;
    	public TextView appType;
    	public TextView appSize;
    	public ImageView imgSplit;
    	public ImageView imgDownLoad;
    	public TextView appPriceFlag;
    }
    
    private class ListViewAdapter extends BaseAdapter {

    	private LayoutInflater mInflater;
    	private Context mContext;
    	
    	public ListViewAdapter(Context ctx) {
    		this.mContext = ctx;
    		this.mInflater = LayoutInflater.from(ctx);
    	}
    	
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listViewRes.length;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			ListViewHolder viewHolder;
			if(convertView == null) {
				convertView = mInflater.inflate(R.layout.index_item_listview, null);
				viewHolder = new ListViewHolder();
				viewHolder.imageViewIcon = (ImageView)convertView.findViewById(R.id.image_item_1);
				viewHolder.appName = (TextView)convertView.findViewById(R.id.text_item_1);
				viewHolder.appType = (TextView)convertView.findViewById(R.id.text_item_2);
				viewHolder.appSize = (TextView)convertView.findViewById(R.id.text_item_3);
				viewHolder.imgSplit = (ImageView)convertView.findViewById(R.id.image_item_2);
				viewHolder.imgDownLoad = (ImageView)convertView.findViewById(R.id.image_item_3);
				viewHolder.appPriceFlag = (TextView)convertView.findViewById(R.id.text_item_4);
				convertView.setTag(viewHolder);
			}else {
				viewHolder = (ListViewHolder)convertView.getTag();
			}
			
			if(position % 2 == 0) {
				viewHolder.imgDownLoad.setBackgroundResource(R.drawable.down_btn_10);
			}else {
				viewHolder.imgDownLoad.setBackgroundResource(R.drawable.down_btn_0);
			}
			return convertView;
		}
    	
    }
}