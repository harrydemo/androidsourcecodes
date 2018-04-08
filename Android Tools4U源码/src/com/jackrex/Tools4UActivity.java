package com.jackrex;






import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;



import cn.domob.android.ads.DomobAdListener;
import cn.domob.android.ads.DomobAdView;

import com.jackrex.account.Grid_bills;
import com.shinylife.smalltools.SmallToolsActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;


public class Tools4UActivity extends Activity {
    public static final String PUBLISHER_ID = "56OJznKouMl64+HdXf";
	/** Called when the activity is first created. */
    RelativeLayout mAdContainer;
	DomobAdView mAdview320x50;
	private ImageView runImage;
    ImageView compass,calender,clock,light,weather,account;
    TranslateAnimation left, right;
	 private ViewPager myViewPager;
	private MyPagerAdapter myAdapter;
	private LayoutInflater mInflater;
	private List<View> mListViews;
	private View layout1 = null;
	private View layout2 = null;
	private View layout3 = null;
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewflipper);
        
        	
        	myAdapter = new MyPagerAdapter();
			myViewPager = (ViewPager) findViewById(R.id.viewpagerLayout);
			myViewPager.setAdapter(myAdapter);
	        
	        mListViews = new ArrayList<View>();
	        mInflater = getLayoutInflater();
	        layout1 = mInflater.inflate(R.layout.layout1, null);
	        layout2 = mInflater.inflate(R.layout.layout2, null);
	        layout3 = mInflater.inflate(R.layout.layout3, null);
	       
	        mListViews.add(layout1);
	        mListViews.add(layout2);
	        mListViews.add(layout3);
	        
	        //初始化当前显示的view
	        myViewPager.setCurrentItem(1);
	        
	        
	        ads();
	        ads_1();
	        ads_2();
	       
	        runImage = (ImageView)layout2.findViewById(R.id.run_image);
	    	runAnimation();
	      
			
			    
			    myViewPager.setOnPageChangeListener(new OnPageChangeListener() {
				
				public void onPageSelected(int arg0) {
					Log.d("k", "onPageSelected - " + arg0);
					//activity从1到2滑动，2被加载后掉用此方法
				
					
				}
				
				public void onPageScrolled(int arg0, float arg1, int arg2) {
					Log.d("k", "onPageScrolled - " + arg0);
					//从1到2滑动，在1滑动前调用
				}
				
				public void onPageScrollStateChanged(int arg0) {
					Log.d("k", "onPageScrollStateChanged - " + arg0);
					//状态有三个0空闲，1是增在滑行中，2目标加载完毕
					/**
				     * Indicates that the pager is in an idle, settled state. The current page
				     * is fully in view and no animation is in progress.
				     */
				    //public static final int SCROLL_STATE_IDLE = 0;
				    /**
				     * Indicates that the pager is currently being dragged by the user.
				     */
				    //public static final int SCROLL_STATE_DRAGGING = 1;
				    /**
				     * Indicates that the pager is in the process of settling to a final position.
				     */
				    //public static final int SCROLL_STATE_SETTLING = 2;

				}
			});
   
			  weather=(ImageView) layout1.findViewById(R.id.weather);
		        weather.setOnClickListener(new ImageView.OnClickListener() {
					
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
					
					
							Intent intent=new Intent();
							intent.setClass(Tools4UActivity.this, SmallToolsActivity.class);
							startActivity(intent);
							overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
						
							
					
					}
				});
		        
		        
		        account=(ImageView) layout3.findViewById(R.id.account);
		        account.setOnClickListener(new ImageView.OnClickListener() {
					
				
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent=new Intent();
						intent.setClass(Tools4UActivity.this, Grid_bills.class);
						startActivity(intent);
						overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
					}
				});
			    
    	
        compass=(ImageView) layout2.findViewById(R.id.compass);
        compass.setOnClickListener(new ImageView.OnClickListener() {
			
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(Tools4UActivity.this, compass.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
			}
		});
        
        calender=(ImageView) layout2.findViewById(R.id.calender);
        calender.setOnClickListener(new ImageView.OnClickListener() {
			
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(Tools4UActivity.this, calender.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
			}
		});
        
        
        clock=(ImageView) layout2.findViewById(R.id.Clock);
        clock.setOnClickListener(new ImageView.OnClickListener() {
			
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(Tools4UActivity.this, clock.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
			}
		});
        
        
        
        light=(ImageView) layout2.findViewById(R.id.light);
        light.setOnClickListener(new ImageView.OnClickListener() {
			
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(Tools4UActivity.this, light.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
			}
        });
    }

    
    private void ads_2() {
		// TODO Auto-generated method stub
    	mAdContainer = (RelativeLayout)layout3.findViewById(R.id.ad_2);
		//创建一个320x50的广告View
		mAdview320x50 = new DomobAdView(this, Tools4UActivity.PUBLISHER_ID, DomobAdView.INLINE_SIZE_320X50);
		mAdview320x50.setKeyword("game");
		mAdview320x50.setUserGender("male");
		mAdview320x50.setUserBirthdayStr("2000-08-08");
		mAdview320x50.setUserPostcode("123456");
		
		//设置广告view的监听器。
		mAdview320x50.setOnAdListener(new DomobAdListener() {
			
			public void onReceivedFreshAd(DomobAdView adview) {
				Log.i("DomobSDKDemo", "onReceivedFreshAd");
			}
		
			public void onFailedToReceiveFreshAd(DomobAdView adview) {
				Log.i("DomobSDKDemo", "onFailedToReceiveFreshAd");
			}
			
			public void onLandingPageOpening() {
				Log.i("DomobSDKDemo", "onLandingPageOpening");
			}
		
			public void onLandingPageClose() {
				Log.i("DomobSDKDemo", "onLandingPageClose");
			}
		});
		//将广告View增加到视图中。
		mAdContainer.addView(mAdview320x50);
	}


	private void ads_1() {
		// TODO Auto-generated method stub
		mAdContainer = (RelativeLayout)layout1.findViewById(R.id.ad_1);
		//创建一个320x50的广告View
		mAdview320x50 = new DomobAdView(this, Tools4UActivity.PUBLISHER_ID, DomobAdView.INLINE_SIZE_320X50);
		mAdview320x50.setKeyword("game");
		mAdview320x50.setUserGender("male");
		mAdview320x50.setUserBirthdayStr("2000-08-08");
		mAdview320x50.setUserPostcode("123456");
		
		//设置广告view的监听器。
		mAdview320x50.setOnAdListener(new DomobAdListener() {
			
			public void onReceivedFreshAd(DomobAdView adview) {
				Log.i("DomobSDKDemo", "onReceivedFreshAd");
			}
			
			public void onFailedToReceiveFreshAd(DomobAdView adview) {
				Log.i("DomobSDKDemo", "onFailedToReceiveFreshAd");
			}
			
			public void onLandingPageOpening() {
				Log.i("DomobSDKDemo", "onLandingPageOpening");
			}
		
			public void onLandingPageClose() {
				Log.i("DomobSDKDemo", "onLandingPageClose");
			}
		});
		//将广告View增加到视图中。
		mAdContainer.addView(mAdview320x50);
	}


	private void ads() {
		// TODO Auto-generated method stub
    	mAdContainer = (RelativeLayout)layout2.findViewById(R.id.adcontainer);
		//创建一个320x50的广告View
		mAdview320x50 = new DomobAdView(this, Tools4UActivity.PUBLISHER_ID, DomobAdView.INLINE_SIZE_320X50);
		mAdview320x50.setKeyword("game");
		mAdview320x50.setUserGender("male");
		mAdview320x50.setUserBirthdayStr("2000-08-08");
		mAdview320x50.setUserPostcode("123456");
		
		//设置广告view的监听器。
		mAdview320x50.setOnAdListener(new DomobAdListener() {
			
			public void onReceivedFreshAd(DomobAdView adview) {
				Log.i("DomobSDKDemo", "onReceivedFreshAd");
			}
			
			public void onFailedToReceiveFreshAd(DomobAdView adview) {
				Log.i("DomobSDKDemo", "onFailedToReceiveFreshAd");
			}
			
			public void onLandingPageOpening() {
				Log.i("DomobSDKDemo", "onLandingPageOpening");
			}
			
			public void onLandingPageClose() {
				Log.i("DomobSDKDemo", "onLandingPageClose");
			}
		});
		//将广告View增加到视图中。
		mAdContainer.addView(mAdview320x50);
	}


	public void runAnimation() {
		right = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0f,
				Animation.RELATIVE_TO_PARENT, -1f,
				Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT,
				0f);
		left = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, -1f,
				Animation.RELATIVE_TO_PARENT, 0f,
				Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT,
				0f);
		right.setDuration(25000);
		left.setDuration(25000);
		right.setFillAfter(true);
		left.setFillAfter(true);

		right.setAnimationListener(new Animation.AnimationListener() {
			
			public void onAnimationStart(Animation animation) {
			}

			
			public void onAnimationRepeat(Animation animation) {
			}

		
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				runImage.startAnimation(left);
			}
		});
		left.setAnimationListener(new Animation.AnimationListener() {
		
			public void onAnimationStart(Animation animation) {
			}

		
			public void onAnimationRepeat(Animation animation) {
			}

			
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				runImage.startAnimation(right);
			}
		});
		runImage.startAnimation(right);
	}
   
    /**
     * 关联菜单
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
    	super.onCreateOptionsMenu(menu);
    	getMenuInflater().inflate(R.menu.mainmenu, menu);
    	//menu.findItem(R.id.about).setEnabled(false);
    	return true;
    }
    
    /**
     * 捕捉菜单事件
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    switch(item.getItemId())
    {
  
    case R.id.seteffer:
    	
    	dialog();
    	return true;
    case R.id.about:
    	about();
    	return true;
 
    
    }
    return false;
    
    }
    
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {  
           dialog();  
            return true;  
        }  
       return super.onKeyDown(keyCode, event);
    }  

     
     
     
     
			private void dialog() {
		// TODO Auto-generated method stub
		   AlertDialog.Builder builder = new Builder(Tools4UActivity.this);  
	        builder.setMessage("确定要退出吗?");  
	        builder.setTitle("提示");  
	        builder.setPositiveButton("确认",  
	        new android.content.DialogInterface.OnClickListener() {  
	            public void onClick(DialogInterface dialog, int which) {  
	                dialog.dismiss();  
	                finish();
	                android.os.Process.killProcess(android.os.Process.myPid());  
	              
	            }  
	        });  
	        builder.setNegativeButton("取消",  
	        new android.content.DialogInterface.OnClickListener() {  
	            public void onClick(DialogInterface dialog, int which) {  
	                dialog.dismiss();  
	            }  
	        });  
	        builder.create().show();  
	}
    
		    /**
		     * 显示关于我们
		     */
		    public void about(){
		    	new AlertDialog.Builder(Tools4UActivity.this).setTitle("关于作者")
						.setMessage("欢迎您使用Tools4U作者：jackrex\n邮件：\njackrex1993@gmail.com").setIcon(R.drawable.icon)
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								// finish();
							}
						}).setNegativeButton("返回",
								new DialogInterface.OnClickListener() {

								
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub

									}

								}).show();
		    }
		    
		    	
		    private class MyPagerAdapter extends PagerAdapter{

				
				@Override
				public void destroyItem(View arg0, int arg1, Object arg2) {
					Log.d("k", "destroyItem");
					((ViewPager) arg0).removeView(mListViews.get(arg1));
				}

				
				@Override
				public void finishUpdate(View arg0) {
					Log.d("k", "finishUpdate");
				}

				
				@Override
				public int getCount() {
					Log.d("k", "getCount");
					return mListViews.size();
				}

				@Override
				public Object instantiateItem(View arg0, int arg1) {
					Log.d("k", "instantiateItem");
					((ViewPager) arg0).addView(mListViews.get(arg1),0);
					return mListViews.get(arg1);
				}

				
				@Override
				public boolean isViewFromObject(View arg0, Object arg1) {
					Log.d("k", "isViewFromObject");
					return arg0==(arg1);
				}

				
				@Override
				public void restoreState(Parcelable arg0, ClassLoader arg1) {
					Log.d("k", "restoreState");
				}

				
				@Override
				public Parcelable saveState() {
					Log.d("k", "saveState");
					return null;
				}

				
				@Override
				public void startUpdate(View arg0) {
					Log.d("k", "startUpdate");
				}
		    	
		    }
		    
		    
		    
		    public class ImageAdapter extends BaseAdapter 
		    {
		      int mGalleryItemBackground;
		      private Context mContext;
		      private Integer[] myImageIds; 
		      public ImageAdapter(Context c, Integer[] aid) 
		      {
		        mContext = c; myImageIds = aid;
		        TypedArray a = obtainStyledAttributes(R.styleable.Gallery);
		        mGalleryItemBackground = a.getResourceId( R.styleable.Gallery_android_galleryItemBackground, 0);
		        a.recycle(); 
		        } 
		      public int getCount()
		      {
		        // TODO Auto-generated method stub 
		        return myImageIds.length; 
		        } 
		      public Object getItem(int position)
		      { 
		        // TODO Auto-generated method stub
		        return null; 
		        } 
		      public long getItemId(int position)
		      {
		        // TODO Auto-generated method stub 
		        return position; 
		        } 
		      public View getView(int position, View convertView, ViewGroup parent) 
		      { 
		        // TODO Auto-generated method stub 
		        /*产生ImageView对象*/
		        ImageView i = new ImageView(mContext);
		        /*设定图片给imageView对象*/ 
		        i.setImageResource(myImageIds[position]); 
		        /*重新设定图片的宽高*/
		        i.setScaleType(ImageView.ScaleType.FIT_XY); 
		        /*重新设定Layout的宽高*/
		        i.setLayoutParams(new Gallery.LayoutParams(138, 108)); 
		        /*设定Gallery背景图*/
		        i.setBackgroundResource(mGalleryItemBackground);
		        /*传回imageView对象*/
		        return i;
		        } 
		      }
		    @Override
		    public void setWallpaper(InputStream data) throws IOException  { 
		        // TODO Auto-generated method stub 
		        super.setWallpaper(data);
		        }
		    
		    

		    	

}