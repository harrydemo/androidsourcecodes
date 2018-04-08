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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class TypeActivity extends Activity {
	
	private final String TAG = "ZhuoDanActivity";
	private ListView mListView;

	private int []listViewRes = null;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
         this.requestWindowFeature(Window.FEATURE_NO_TITLE);
         
        setContentView(R.layout.type);
        
        initRes();
        findViews();
        setValues();
        setListeners();
    }
    
    
    private void findViews() {
    	mListView = (ListView)this.findViewById(R.id.listview);
    }
    
    private void setValues() {
	
    	mListView.setAdapter(new ListViewAdapter(this));
    	
    }
    
    private void setListeners() {
    	
  
    }
    
    private void initRes() {
    	
    	listViewRes = new int[20];
    	for(int i=0; i<20; i++) {
    		listViewRes[i] =  R.drawable.icon_360;
    	}
    	
    }

    
    private final class ListViewHolder {
    	public ImageView imageViewIcon;
    	public TextView appType;
    	public TextView appDesc;
    	public TextView appCount;
  
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
				convertView = mInflater.inflate(R.layout.type_item_listview, null);
				viewHolder = new ListViewHolder();
				viewHolder.imageViewIcon = (ImageView)convertView.findViewById(R.id.image_item_1);
				viewHolder.appType = (TextView)convertView.findViewById(R.id.text_item_1);
				viewHolder.appDesc = (TextView)convertView.findViewById(R.id.text_item_2);
				viewHolder.appCount = (TextView)convertView.findViewById(R.id.text_item_3);
				
				convertView.setTag(viewHolder);
			}else {
				viewHolder = (ListViewHolder)convertView.getTag();
			}
			
	
			return convertView;
		}
    	
    }
}