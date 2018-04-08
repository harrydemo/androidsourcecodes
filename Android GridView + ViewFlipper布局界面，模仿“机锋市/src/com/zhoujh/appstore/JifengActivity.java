package com.zhoujh.appstore;

import com.zhoujh.appstore.animation.WindowAnimation;

import android.app.ActivityGroup;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class JifengActivity extends ActivityGroup {

	
	private ViewFlipper mViewFlipper;
	private GridView menuGridView;
	//底部菜单的名字
	private String []menuGridViewNames = null;
	//未选择时 显示的图片资源id
	private int []menuGridViewUnSelectedImgs = null;
	//选择时 显示的图片资源id
	private int []menuGridViewSelectedImgs = null;
	//上次点击的位置，当前点击的位置
	private int lastClickPosition,clickPosition;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.main);
		initRes();
        findViews();
        setValues();
        setListeners();
	}
	
	 private void findViews() {
		 menuGridView = (GridView)this.findViewById(R.id.gridview);
	    	mViewFlipper = (ViewFlipper)this.findViewById(R.id.content);
	    }

	 private void setValues() {
	    	menuGridView.setAdapter(new BottomMenuGridView(this));
	    	menuGridView.setNumColumns(4);
	    	menuGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
	    	menuGridView.setGravity(Gravity.CENTER);
	    	menuGridView.setBackgroundResource(R.drawable.tab_bg);
	    	
	    	
	    	switchActivity(AppConstants.INDEX_ACTIVITY_INDEX);
	    	
	    }
	 private void initRes() {
	    	menuGridViewNames = new String[4];
	    	menuGridViewNames[0] = this.getString(R.string.tab_index);
	    	menuGridViewNames[1] = this.getString(R.string.tab_type);
	    	menuGridViewNames[2] = this.getString(R.string.app_rank);
	    	menuGridViewNames[3] = this.getString(R.string.app_manager);
	    	
	    	menuGridViewUnSelectedImgs = new int[4];
	    	menuGridViewUnSelectedImgs[0] = R.drawable.tab_home_normal;
	    	menuGridViewUnSelectedImgs[1] = R.drawable.tab_rank_normal;
	    	menuGridViewUnSelectedImgs[2] = R.drawable.tab_category_normal;
	    	menuGridViewUnSelectedImgs[3] = R.drawable.tab_app_unselect;
	    	
	    	menuGridViewSelectedImgs = new int[4];
	    	menuGridViewSelectedImgs[0] = R.drawable.tab_home_selected;
	    	menuGridViewSelectedImgs[1] = R.drawable.tab_rank_selected;
	    	menuGridViewSelectedImgs[2] = R.drawable.tab_category_selected;
	    	menuGridViewSelectedImgs[3] = R.drawable.tab_app_select;
	    }
	    
	 private void setListeners() {
	    	menuGridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					//Log.i(TAG, "arg2="+arg2+"; arg3="+arg3);
					lastClickPosition = clickPosition;
					clickPosition = arg2;
					if(lastClickPosition == clickPosition) return; //防止重复点击同一个菜单
					
					RelativeLayout temp = (RelativeLayout)menuGridView.getChildAt(arg2);
					temp.setBackgroundResource(R.drawable.product_title_bg);
					temp.getChildAt(0).setBackgroundResource(menuGridViewSelectedImgs[arg2]);
					
					for(int i=0; i<menuGridViewNames.length; i++) {
						if(i != arg2) {
							RelativeLayout temp2 = (RelativeLayout)menuGridView.getChildAt(i);
							temp2.setBackgroundDrawable(null);
							temp2.getChildAt(0).setBackgroundResource(menuGridViewUnSelectedImgs[i]);
						}
					}
					
					switchActivity(arg2);
					
				}
	    		
			});
	    	
	  
	    }
	 
	 private void switchActivity(int index) {
		 String activityId = null;
		 Intent intent = null;
		 
		 switch(index) {
		 case AppConstants.INDEX_ACTIVITY_INDEX:
			 intent = new Intent(this,IndexActivity.class);
			 activityId = AppConstants.INDEX_ACTIVITY;
			 break;
			 
		 case AppConstants.TYPE_ACTIVITY_INDEX:
			 intent = new Intent(this,TypeActivity.class);
			 activityId = AppConstants.TYPE_ACTIVITY;
			 break;
			 
		 case AppConstants.RANK_ACTIVITY_INDEX:
			 break;
		 case AppConstants.LOCAL_MANAGER_ACTIVITY_INDEX:
			 break;
		 }
		 
		 if(activityId != null) {
			 toActivity(activityId, intent);
		 }
		
	 }
	 
	 /**
	  *  切换到指定activity
	  * @param activityId
	  * @param intent
	  */
	 public void toActivity(String activityId,Intent intent) {
	    	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    	View view = getLocalActivityManager().startActivity(activityId, intent).getDecorView();
	    	//切换activity时显示的动画效果
	    	view.setAnimation(new WindowAnimation(500));
	    	mViewFlipper.removeAllViews();
	    	mViewFlipper.addView(view);
	    	mViewFlipper.showNext();
	    }
	 
	  private class BottomMenuGridView extends BaseAdapter {

	    	private LayoutInflater mInflater;
	    	
	    	public BottomMenuGridView(Context ctx) {
	    		this.mInflater = LayoutInflater.from(ctx);
	    	}
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return menuGridViewUnSelectedImgs.length;
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				MenuViewHolder viewHolder;
				if(convertView == null) {
					convertView = mInflater.inflate(R.layout.item_tab, null);
					viewHolder = new MenuViewHolder();
					viewHolder.imageView = (ImageView)convertView.findViewById(R.id.image_item);
					viewHolder.textView = (TextView)convertView.findViewById(R.id.text_item);
					convertView.setTag(viewHolder);
				}else {
					viewHolder = (MenuViewHolder)convertView.getTag();
				}
				viewHolder.imageView.setBackgroundResource(menuGridViewUnSelectedImgs[position]);
				viewHolder.textView.setText(menuGridViewNames[position]);
				
				return convertView;
			}
	    	
	    }
	    
	    private final class MenuViewHolder {
	    	public ImageView imageView;
	    	public TextView textView;
	    }
	    
	 
}
