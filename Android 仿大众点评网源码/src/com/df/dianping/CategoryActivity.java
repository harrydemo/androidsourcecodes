package com.df.dianping;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CategoryActivity extends Activity {
	
	private List<Map<String, Object>> mData;
	View view;
	
	Handler handler = new Handler() 
	{
		public void handleMessage(Message paramMessage) 
		{
			if(paramMessage.what == KeyEvent.KEYCODE_BACK)
			{
				finish();
			}
		}
	};
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        view = this.getLayoutInflater().inflate(R.layout.category, null);
        
        setContentView(view);
        
        mData = CategoryData.getData();
        
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout header = (LinearLayout)inflater.inflate(R.layout.categoryheader, null);
        
        ListView list = (ListView)findViewById(R.id.categorylist);
        list.addHeaderView(header);
        list.setOnItemClickListener(mOnClickListener);
        ListAdapter adapter = new MyAdapter(this);
        list.setAdapter(adapter);
        
//        Animation mAnimationScale =new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f,
//				Animation.RELATIVE_TO_SELF, 0.5f, 
//				Animation.RELATIVE_TO_SELF, 0.5f);
//        
//        mAnimationScale.setDuration(600);
//        view.startAnimation(mAnimationScale);

    }
    
    private AdapterView.OnItemClickListener mOnClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View v, int position, long id)
        {
            Intent intent = new Intent();
            intent.setClass(CategoryActivity.this, ResultActivity.class);
            startActivity(intent);
        }
    };
    
    
    public final class ViewHolder {
		public ImageView img;
		public TextView title;
	}
    
    public class MyAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public MyAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mData.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;
			if (convertView == null) 
			{
				holder = new ViewHolder();

				convertView = mInflater.inflate(R.layout.categoryitem, null);
				convertView.setMinimumHeight(100);
				holder.img = (ImageView) convertView.findViewById(R.id.category_icon);
				holder.title = (TextView) convertView.findViewById(R.id.category_name);

				convertView.setTag(holder);

			} 
			else 
			{

				holder = (ViewHolder) convertView.getTag();
			}
			holder.img.setBackgroundResource((Integer) mData.get(position).get("img"));
			holder.title.setText((String) mData.get(position).get("title"));
			
			return convertView;
		}

	}
    
    boolean isBack;
    public void onPause()
    {
    	if (isBack)
    	{
    		isBack = false;
    		overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
    	}
    	super.onPause();
    }
    
    public boolean onKeyUp(int keyCode, KeyEvent event) 
    {
    	if(keyCode == KeyEvent.KEYCODE_BACK)
    	{         
    		isBack = true;
//            Animation mAnimationScale =new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f,
//    				Animation.RELATIVE_TO_SELF, 0.5f, 
//    				Animation.RELATIVE_TO_SELF, 0.5f);
//      
//            mAnimationScale.setDuration(800);
//            view.startAnimation(mAnimationScale);
//    		  
//    		//delay finish the activity so as to show animation
//			new Thread() 
//			{
//				public void run() 
//				{
//					try 
//					{
//						Thread.sleep(600);
//					} 
//					catch (InterruptedException e) 
//					{
//						e.printStackTrace();
//					}
//					
//					Message msg = new Message();
//					msg.what = KeyEvent.KEYCODE_BACK;
//					DianpingActivity.this.handler.sendMessage(msg);
//				}
//			}.start();
//    		 
//    		 return true;
    	}
    	
    	return super.onKeyUp(keyCode, event);
    }
}