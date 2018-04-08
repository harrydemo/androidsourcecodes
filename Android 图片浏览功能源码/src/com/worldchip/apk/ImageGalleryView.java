package com.worldchip.apk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Display;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

public class ImageGalleryView extends Activity{
	
	private static final String TAG = "ImageGalleryView";
    private static final String PREF_SLIDESHOW_REPEAT =
        "pref_gallery_slideshow_repeat_key";
    
	Gallery gallery=null;
	Animation anim=null;
	String[] paths=null;
	List<String> mImagePathList;
	private ImageGetter mGetter;
	private final Random mRandom = new Random(System.currentTimeMillis());
	
	boolean mPaused = true;
	private boolean mSlideShowLoop = false;
    private int mSlideShowInterval=1000;
    private int mLastSlideShowImage;
	int mCurrentPosition = 0;
	
    // represents which style animation to use
    private int mAnimationIndex;
    private Animation [] mSlideShowInAnimation;
    private Animation [] mSlideShowOutAnimation;
    
    private SharedPreferences mPrefs;
    
    // Choices for what adjacents to load.
    private static final int[] sOrderAdjacents = new int[] {0, 1, -1};
    private static final int[] sOrderSlideshow = new int[] {0};
    
    GestureDetector mGestureDetector;
	final GetterHandler mHandler = new GetterHandler();	
    static final int MODE_NORMAL = 1;
    static final int MODE_SLIDESHOW = 2;
    private int mMode = MODE_NORMAL;
    // The image view displayed for normal mode.
    private ImageViewTouch mCurrentImageView;
    private Button mSaveConfirm;
    private Button mSaveCancel;
    // This is the cache for thumbnail bitmaps.
    private BitmapCache mCache;
	
	String path=null;
	GalleryAdapter galleryAdapter=null;
    private float scaleWidth=1, scaleHeight=1;
    
    private int mSlideShowImageCurrent = 0;
    private final ImageViewTouchBase [] mSlideShowImageViews =
            new ImageViewTouchBase[2];
    
    private Bitmap currentBitmap;
    private Bitmap effectsBitmap;
    private String currentPath;
    private int currentPosition;
    private int displayWidth, displayHeight;
    //FLING PARMS
    public static final int FLING_MIN_DISTANCE=100;
    public static final int FLING_MIN_VELOCITY=200;
    //Menu
    public static final int ANIMATION = Menu.FIRST;  
    public static final int ROTATE = Menu.FIRST+1; 
    public static final int THUMBNAIL = Menu.FIRST+2;
    public static final int SETUPFOR = Menu.FIRST+5;
    public static final int DELETE = Menu.FIRST+6;  
    public static final int DETAILS = Menu.FIRST+3;  
    public static final int QUITE = Menu.FIRST+4; 
    public static final int EFFECTS = Menu.FIRST+7; 
    
    private void setupOnTouchListeners(View rootView) {
        mGestureDetector = new GestureDetector(this, new MyGestureListener());

        OnTouchListener rootListener = new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                mGestureDetector.onTouchEvent(event);

                // We do not use the return value of
                // mGestureDetector.onTouchEvent because we will not receive
                // the "up" event if we return false for the "down" event.
                return true;
            }
        };
        rootView.setOnTouchListener(rootListener);
    }

    private class MyGestureListener extends
            GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                float distanceX, float distanceY) {
            //if (mPaused) return false;
            ImageViewTouch imageView = mCurrentImageView;
            if (imageView.getScale() > 1F) {
                imageView.postTranslateCenter(-distanceX, -distanceY);
            }
            return true;
        }

        @Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// TODO Auto-generated method stub
        	if((e1.getX()-e2.getX()>FLING_MIN_DISTANCE)&&
        			Math.abs(velocityX)>FLING_MIN_VELOCITY)
        	{
        		moveNextOrPrevious(1);
        	}
        	else if((e2.getX()-e1.getX()>FLING_MIN_DISTANCE)&&
        			Math.abs(velocityX)>FLING_MIN_VELOCITY)
			{
        		moveNextOrPrevious(-1);
			}
			return super.onFling(e1, e2, velocityX, velocityY);
		}

		@Override
        public boolean onSingleTapUp(MotionEvent e) {
            //if (mPaused) return false;
            //setMode(MODE_NORMAL);
            //openOptionsMenu();
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            //if (mPaused) return false;
            //showOnScreenControls();
            //scheduleDismissOnScreenControls();
        	setMode(MODE_NORMAL);
            openOptionsMenu();
            return true;
        }

        @Override
		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			return super.onDown(e);
		}

		@Override
        public boolean onDoubleTap(MotionEvent e) {
            //if (mPaused) return false;
            ImageViewTouch imageView = mCurrentImageView;

            // Switch between the original scale and 3x scale.
            if (imageView.getScale() > 2F) {
                mCurrentImageView.zoomTo(1f);
            } else {
                mCurrentImageView.zoomToPoint(3f, e.getX(), e.getY());
            }
            return true;
        }
    }
    
    private Animation makeInAnimation(int id) {
        Animation inAnimation = AnimationUtils.loadAnimation(this, id);
        return inAnimation;
    }

    private Animation makeOutAnimation(int id) {
        Animation outAnimation = AnimationUtils.loadAnimation(this, id);
        return outAnimation;
    }
    
    private static int getPreferencesInteger(
            SharedPreferences prefs, String key, int defaultValue) {
        String value = prefs.getString(key, null);
        try {
            return value == null ? defaultValue : Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            Log.e(TAG, "couldn't parse preference: " + value, ex);
            return defaultValue;
        }
    }
    void setMode(int mode) {
        if (mMode == mode) {
            return;
        }
        View slideshowPanel = findViewById(R.id.slideShowContainer);
        View normalPanel = findViewById(R.id.abs);

        Window win = getWindow();
        mMode = mode;
        if (mode == MODE_SLIDESHOW) {
        	slideshowPanel.setVisibility(View.VISIBLE);
            normalPanel.setVisibility(View.GONE);

            win.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                    | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            mCurrentImageView.clear();
            slideshowPanel.getRootView().requestLayout();

            // The preferences we want to read:
            //   mUseShuffleOrder
            //   mSlideShowLoop
            //   mAnimationIndex
            //   mSlideShowInterval
            mSlideShowLoop = mPrefs.getBoolean(PREF_SLIDESHOW_REPEAT, false);
          //  mAnimationIndex = getPreferencesInteger(
          //          mPrefs, "pref_gallery_slideshow_transition_key", -1);
            Log.i(TAG, "mAnimationIndex="+mAnimationIndex);
            mSlideShowInterval = getPreferencesInteger(
                    mPrefs, "pref_gallery_slideshow_interval_key", 3) * 1000;
        } else {
            slideshowPanel.setVisibility(View.GONE);
            normalPanel.setVisibility(View.VISIBLE);

            win.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);            
            win.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

            if (mGetter != null) {
                mGetter.cancelCurrent();
            }
            for (ImageViewTouchBase ivt : mSlideShowImageViews) {
                ivt.clear();
            }
            // mGetter null is a proxy for being paused
            if (mGetter != null) {
                setImage(mCurrentPosition, true);
            }
        }
    }

/**
 * 加载幻灯片播放图片
 * @param requestedPos
 * @param delay
 * @param firstCall
 */
    private void loadNextImage(final int requestedPos, final long delay,
                               final boolean firstCall) {

        final long targetDisplayTime = System.currentTimeMillis() + delay;

        ImageGetterCallback cb = new ImageGetterCallback() {
            public void completed() {
            }

            public boolean wantsThumbnail(int pos, int offset) {
                return true;
            }

            public boolean wantsFullImage(int pos, int offset) {
                return false;
            }

            public int [] loadOrder() {
                return sOrderSlideshow;
            }

            public int fullImageSizeToUse(int pos, int offset) {
                return 480; // TODO compute this
            }
            
			public void imageLoaded(int pos, int offset, Bitmap bitmap,
					boolean isThumb) {
                long timeRemaining = Math.max(0,
                        targetDisplayTime - System.currentTimeMillis());
                final Bitmap bm=bitmap;
                mHandler.postDelayedGetterCallback(new Runnable() {
                    public void run() {
                        if (mMode == MODE_NORMAL) {
                            return;
                        }

                        ImageViewTouchBase oldView =
                                mSlideShowImageViews[mSlideShowImageCurrent];

                        if (++mSlideShowImageCurrent
                                == mSlideShowImageViews.length) {
                            mSlideShowImageCurrent = 0;
                        }

                        ImageViewTouchBase newView =
                                mSlideShowImageViews[mSlideShowImageCurrent];
                        newView.setVisibility(View.VISIBLE);
                        newView.setImageRotateBitmapResetBase(new RotateBitmap(bm,0), true);
                        newView.bringToFront();
                        currentBitmap=bm;

                        int animation = 0;

                        if (mAnimationIndex == -1) {
                            int n = mRandom.nextInt(
                                    mSlideShowInAnimation.length);
                            animation = n;
                        } else {
                            animation = mAnimationIndex;
                        }

                        Animation aIn = mSlideShowInAnimation[animation];
                        newView.startAnimation(aIn);
                        newView.setVisibility(View.VISIBLE);

                        Animation aOut = mSlideShowOutAnimation[animation];
                        oldView.setVisibility(View.INVISIBLE);
                        oldView.startAnimation(aOut);

                        mCurrentPosition = requestedPos;

                        if (mCurrentPosition == mLastSlideShowImage
                                && !firstCall) {
                            if (!mSlideShowLoop) {
                                setMode(MODE_NORMAL);
                                return;
                            }
                        }

                        loadNextImage(
                                (mCurrentPosition + 1) % mImagePathList.size(),
                                mSlideShowInterval, false);
                    }
                }, timeRemaining);
            }

        };
        // Could be null if we're stopping a slide show in the course of pausing
        if (mGetter != null) {
            int pos = requestedPos;
            mGetter.setPosition(pos, cb, mImagePathList, mHandler);
        }
    }
    /**
     * 加载上一张下一张图片
     * @param delta
     */
    private void moveNextOrPrevious(int delta) {
        int nextImagePos = mCurrentPosition + delta;
        if ((0 <= nextImagePos) && (nextImagePos < mImagePathList.size())) {
            setImage(nextImagePos, true);
        }
    }
    
    void setImage(int pos, boolean showControls) {
        mCurrentPosition = pos;

        Bitmap b = mCache.getBitmap(pos);
        if (b != null) {           
            mCurrentImageView.setImageRotateBitmapResetBase(
                    new RotateBitmap(b, 0), true);             
            currentBitmap=b;
        }

        ImageGetterCallback cb = new ImageGetterCallback() {
            public void completed() {
            }

            public boolean wantsThumbnail(int pos, int offset) {
                return !mCache.hasBitmap(pos + offset);
            }

            public boolean wantsFullImage(int pos, int offset) {
                return offset == 0;
            }

            public int fullImageSizeToUse(int pos, int offset) {
                // this number should be bigger so that we can zoom.  we may
                // need to get fancier and read in the fuller size image as the
                // user starts to zoom.
                // Originally the value is set to 480 in order to avoid OOM.
                // Now we set it to 2048 because of using
                // native memory allocation for Bitmaps.
                final int imageViewSize = 2048;
                return imageViewSize;
            }

            public int [] loadOrder() {
                return sOrderAdjacents;
            }
			
			public void imageLoaded(int pos, int offset, Bitmap bitmap,
					boolean isThumb) {
				// TODO Auto-generated method stub
                // We may get a result from a previous request. Ignore it.
                if (pos != mCurrentPosition) {
                    bitmap.recycle();
                    return;
                }

                if (isThumb) {
                    mCache.put(pos + offset, bitmap);
                }
                if (offset == 0) {
                    // isThumb: We always load thumb bitmap first, so we will
                    // reset the supp matrix for then thumb bitmap, and keep
                    // the supp matrix when the full bitmap is loaded.
                    mCurrentImageView.setImageRotateBitmapResetBase(new RotateBitmap(bitmap,0), isThumb);
                    currentBitmap=bitmap;
                }
				
			}
        };

        // Could be null if we're stopping a slide show in the course of pausing
        if (mGetter != null) {
            mGetter.setPosition(pos, cb, mImagePathList, mHandler);
        }
    }

	@Override  
	 public void onCreate(Bundle savedInstanceState) {  
	    super.onCreate(savedInstanceState);  
	       
	    requestWindowFeature(Window.FEATURE_NO_TITLE);    
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,      
	                      WindowManager.LayoutParams.FLAG_FULLSCREEN);    
	       
	    setContentView(R.layout.gallery_view_bk);  
	    
        mCurrentImageView = (ImageViewTouch) findViewById(R.id.image);
        mCurrentImageView.setEnableTrackballScroll(true);
        mCache = new BitmapCache(3);
        mCurrentImageView.setRecycler(mCache);
        
        mSaveConfirm=(Button)findViewById(R.id.save_confirm);
        mSaveCancel=(Button)findViewById(R.id.save_cancel);
        mSaveConfirm.setOnClickListener((android.view.View.OnClickListener) onSaveConfirmListener);
        mSaveCancel.setOnClickListener((android.view.View.OnClickListener) onSaveCancelListener);
	    makeGetter();
        mAnimationIndex = -1;

        mSlideShowInAnimation = new Animation[] {
        	makeInAnimation(R.anim.erase_translate_in),
        	makeInAnimation(R.anim.bounce_in),
        	makeInAnimation(R.anim.alpha_scale_translate_rotate_in),
        	makeInAnimation(R.anim.alpha_scale_translate_in),
        	makeInAnimation(R.anim.alpha_rotate_in),
            makeInAnimation(R.anim.transition_in),
            makeInAnimation(R.anim.slide_in),
            makeInAnimation(R.anim.slide_in_vertical),
        };

        mSlideShowOutAnimation = new Animation[] {
        	makeInAnimation(R.anim.erase_translate_out),
        	makeInAnimation(R.anim.bounce_out),
        	makeInAnimation(R.anim.alpha_scale_translate_rotate_out),
        	makeInAnimation(R.anim.alpha_scale_translate_out),
        	makeInAnimation(R.anim.alpha_rotate_out),
            makeOutAnimation(R.anim.transition_out),
            makeOutAnimation(R.anim.slide_out),
            makeOutAnimation(R.anim.slide_out_vertical),
        };

        mSlideShowImageViews[0] =
                (ImageViewTouchBase) findViewById(R.id.image1_slideShow);
        mSlideShowImageViews[1] =
                (ImageViewTouchBase) findViewById(R.id.image2_slideShow);
        for (ImageViewTouchBase v : mSlideShowImageViews) {
            v.setVisibility(View.INVISIBLE);
            v.setRecycler(mCache);
        }
	    mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		/*Display display = getWindowManager().getDefaultDisplay();
		displayWidth=display.getWidth();
		displayHeight=display.getHeight();
		Log.i("view_Display" , "height:" +displayWidth);
		Log.i("view_Display" , "width:" +displayHeight);*/
          
		 // DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		 // Log.i("view_DisplayMetrics" , "height" +displayMetrics.heightPixels);
		 // Log.i("view_DisplayMetrics" , "width" +displayMetrics.widthPixels);
	    /*Animation load_anim=AnimationUtils.loadAnimation(ImageGalleryView.this,R.anim.load_photo);
	    anim=AnimationUtils.loadAnimation(ImageGalleryView.this,R.anim.to_large); 
	    //gallery = (Gallery)this.findViewById(R.id.galleryView);
	    mCurrentImageView.startAnimation(load_anim);*/
	    Intent intent = getIntent();      
	    paths = intent.getStringArrayExtra("data");
        if(paths==null)
        {
        	Log.i(TAG, "pathArray=null");
        	return;
        }
	    path=intent.getStringExtra("path");
        int position=intent.getIntExtra("id", 0);
        mCurrentPosition=position;
        mLastSlideShowImage = mCurrentPosition;
        Log.i("ImageGalleryView_onCreate", "position="+position+"; path="+path);
        mImagePathList=new ArrayList<String>();
        for(String str: paths)
	    {
        	mImagePathList.add(str);		    
	    }
        
        setupOnTouchListeners(findViewById(R.id.rootLayout));
        //galleryAdapter=new GalleryAdapter(this,paths, displayWidth, displayHeight);
        //gallery.setAdapter(galleryAdapter);
        
        // currentPosition=position;
		//currentPath=(String) galleryAdapter.getItem(position);
		Log.i(TAG, "currentPostion="+currentPosition+"; currentPath="+currentPath);
		//mCurrentImageView = galleryAdapter.getCurrentView();
		//currentBitmap=galleryAdapter.getBitmap();	

	 }
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
        int count = mImagePathList.size();
        if (count == 0) {
            finish();
            return;
        } else if (count <= mCurrentPosition) {
            mCurrentPosition = count - 1;
        }
        if (mGetter == null) {
            makeGetter();
        }
        setImage(mCurrentPosition, false);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
        // mGetter could be null if we call finish() and leave early in
        // onStart().
        if (mGetter != null) {
            mGetter.cancelCurrent();
            mGetter.stop();
            mGetter = null;
        }
        // removing all callback in the message queue
        mHandler.removeAllGetterCallbacks();
        
        mCurrentImageView.clear();
        mCache.clear();
	}

	private void makeGetter() {
	    mGetter = new ImageGetter(getContentResolver());
	}
	 public void startAnimation()
	 {
		 mCurrentImageView.startAnimation(anim);
	 }	 

     public void rotate(int r){
    	 
    	if(currentBitmap==null)
		{
			 Log.i(TAG, "oh, nonono~~~, currentBitmap=null!");
			 return;
		}
    	
    	Log.i(TAG, "the currentPosition="+currentPosition);
    	
        int bmWidth = currentBitmap.getWidth();
        int bmHeight = currentBitmap.getHeight();        
        Matrix matrix = new Matrix();
        
        try
        {
           matrix.postRotate(r); 
           Bitmap resizeBmp = Bitmap.createBitmap(currentBitmap,0,0,bmWidth,bmHeight,matrix,true);
        
           mCurrentImageView.setImageBitmap(resizeBmp);

           currentBitmap=resizeBmp;
        }
        catch(Exception err)
        {
        	err.printStackTrace();
        	Toast.makeText(ImageGalleryView.this, R.string.no_memory,Toast.LENGTH_SHORT).show();
        	return;
        }
    }

    Handler handler = new Handler(){  
           public void handleMessage(Message msg) {  
    	          switch (msg.what) {      
    	            case 1:
    	            	currentPosition++;
    	            	int animationPosition=currentPosition%paths.length;
    	            	Log.i(TAG, "currentPosition="+currentPosition+"; animationPosition="+animationPosition);
    	            	gallery.setSelection(animationPosition);
    	                break;      
    	            }      
    	            super.handleMessage(msg);  
    	   }       
     };  
    
     private Runnable runnable = new Runnable() {  
    	    public void run() {  
    	       // update();  
    	    	Message message = new Message();      
                message.what = 1;      
                handler.sendMessage(message); 
    	        handler.postDelayed(this, 3000);  
    	    }  
    	 
     };
    	
     TimerTask task = new TimerTask(){  
    	 public void run() {  
    	    Message message = new Message();      
            message.what = 1;      
            handler.sendMessage(message);    
         }            
     }; 
    	 
	 @Override
	 public boolean onCreateOptionsMenu(Menu menu) {
		   super.onCreateOptionsMenu(menu);
		   if(mMode==MODE_NORMAL)
		   {
			   menu.add(0, ANIMATION, 1, R.string.animation); 
			   menu.getItem(0).setIcon(R.drawable.start);
		   }	
		   else
		   {
			   menu.add(0, ANIMATION, 1, R.string.stop_anim);
			   menu.getItem(0).setIcon(R.drawable.pause);
		   }		    
		   menu.add(1, ROTATE, 2, R.string.retate);
		   menu.add(0, THUMBNAIL, 3, R.string.thumbnail);
		   menu.add(0, DETAILS, 4, R.string.details);
		   menu.add(0, QUITE, 5, R.string.quite);
		   menu.add(0, SETUPFOR, 6, R.string.setupfor);
		   menu.add(0, DELETE, 7, R.string.delete);
		//   menu.add(0, EFFECTS, 8, R.string.set_effects);
		   
		   
		   menu.getItem(1).setIcon(R.drawable.rotate2);
		   menu.getItem(2).setIcon(R.drawable.thumb);
		   menu.getItem(3).setIcon(R.drawable.details);
		   menu.getItem(4).setIcon(R.drawable.backto);
		   menu.getItem(5).setIcon(R.drawable.setupfor);
		   menu.getItem(6).setIcon(R.drawable.delete);
		   
		   return true;
	 }
	 
	 @Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onPrepareOptionsMenu(menu);
		   if(mMode==MODE_NORMAL)
		   {			   
			   menu.getItem(0).setTitle(R.string.animation);
			   menu.getItem(0).setIcon(R.drawable.start);
		   }	
		   else
		   {			   
			   menu.getItem(0).setTitle(R.string.stop_anim);
			   menu.getItem(0).setIcon(R.drawable.pause);
		   }	
		   return true;
	}

	//閸掓稑缂撴稉濠佺瑓閺傚洩褰嶉崡锟�
	 @Override  
     public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {  
		 
         menu.setHeaderIcon(R.drawable.rotate2);
         menu.setHeaderTitle(R.string.retate);
		 menu.add(1, 0, 0, R.string.retate_left); 
		 menu.add(1, 1, 1, R.string.retate_right);
		 menu.add(1, 2, 2, R.string.retate_back);
		 
		 //閺嗗倹妞傛稉宥堝厴娴ｈ法鏁ら崶鐐垼
		 //menu.getItem(0).setIcon(R.drawable.rotate);
		 //menu.getItem(1).setIcon(R.drawable.rotateright);
		   
		 super.onCreateContextMenu(menu, v, menuInfo);  
	 }

	 @Override  
	 public boolean onOptionsItemSelected(MenuItem item) {  
		 
	      switch(item.getItemId()){  
	           case ANIMATION: //閹绢厽鏂侀崝銊ф暰
	        	   showAnimation(item);
	        	   break;  
	           case ROTATE:
	        	   showRotate();
	        	   break; 
	           case SETUPFOR: //鐠佹儳鐣炬稉鎭掞拷閵嗗倶锟�
	        	   showSetupFor();
	        	   break;  
	           case THUMBNAIL: //缂傗晜鏂�
	        	   showThumbnail();
	        	   break;   
	           case DELETE: //閸掔娀娅�
	        	   showDelete();
	        	   break;
	           case DETAILS: //鐠囷妇绮忔穱鈩冧紖
	        	   showDetails();
	        	   break;
	           case QUITE: //鏉╂柨娲�
	        	   ImageGalleryView.this.finish();
	        	   break;
	           case EFFECTS: //设置图片特效
	        	   setEffects();
	        	   break;
	        	   
	      }  
	          
	      return super.onOptionsItemSelected(item);  
	 }  
	 
	 private void showThumbnail() {
		// TODO Auto-generated method stub
		 String[] items={(String) getText(R.string.tolarger),
				 (String) getText(R.string.tosmaller)}; 
         AlertDialog dialog = new AlertDialog.Builder(ImageGalleryView.this)  
         .setIcon(R.drawable.thumb)
         .setTitle(R.string.thumbnail)
         .setItems(items, onShowThumbnailSelect).create();  
         dialog.show(); 
	}

	private void showAnimation(MenuItem item) {
		// TODO Auto-generated method stub
		 //if(item.getTitle().equals((String) getText(R.string.animation)))
		 if(mMode==MODE_NORMAL)
		 {
		    item.setIcon(R.drawable.pause);
  	        item.setTitle(R.string.stop_anim);
  	        try
	  	    {
  	        	//handler.postDelayed(runnable, 1000);
  	        	//timer.schedule(task, 2000, 5000);
                setMode(MODE_SLIDESHOW);
                mLastSlideShowImage = mCurrentPosition;
                loadNextImage(mCurrentPosition, 0, true);
	  	    }
	  	    catch(Exception err)
	  	    {
	  	    	 err.printStackTrace();
	  	    	 return;
	  	    }
		 }
		 else
		 {
			 item.setIcon(R.drawable.start);
	  	     item.setTitle(R.string.animation);
	  	     try
	  	     {
	  	    	//handler.removeCallbacks(runnable);  
	  	        //timer.cancel();
	  	    	setMode(MODE_NORMAL);
	  	     }
	  	     catch(Exception err)
	  	     {
	  	    	 err.printStackTrace();
	  	    	 return;
	  	     }
		 }
	}

	private void showRotate() {
		// TODO Auto-generated method stub
		 String[] items={(String) getText(R.string.retate_left),
				 (String) getText(R.string.retate_right),
				 (String) getText(R.string.retate_back)}; 

          AlertDialog dialog = new AlertDialog.Builder(ImageGalleryView.this)  
         .setIcon(R.drawable.rotate2)  
         .setTitle(R.string.retate)
         .setItems(items, onShowRotateSelect).create();  
         dialog.show();  
	}

	private void showSetupFor() {
		 String[] items={(String) getText(R.string.setup_wallpaper),
				 (String) getText(R.string.setup_favarete)}; 
		  AlertDialog dialog = new AlertDialog.Builder(ImageGalleryView.this)  
		 .setIcon(R.drawable.setupfor)
	     .setTitle(R.string.setupfor)
         .setItems(items, onShowSetUpSelect).create();  
		 dialog.show(); 
	}

	private void showDelete() {
		// TODO Auto-generated method stub
	   currentPath=mImagePathList.get(mCurrentPosition);
	   final File file=new File(currentPath);
  	   if(file.exists())
  	   {
  			new AlertDialog.Builder(ImageGalleryView.this)
  	        .setIcon(R.drawable.delete)
  	        .setTitle(R.string.delete_redue)
  	        .setMessage((String) getText(R.string.delete_redue_question_a)+" "+file.getName()
  	        		+" "+(String) getText(R.string.delete_redue_question_b))
  	        .setPositiveButton(R.string.ok, 
  	                new DialogInterface.OnClickListener() {
  	                    public void onClick(DialogInterface dialog,
  	                            int whichButton) {
  	                       
  	                    	ArrayList<String> list=new ArrayList<String>();
  	                    	for(String str:paths)
  	                    	{
  	                    		if(str.equals(currentPath))
  	                    			continue;
  	                    		list.add(str);
  	                    	}
  	                    	Log.v(TAG, "remove before n="+mImagePathList.size());
  	                    	mImagePathList.remove(currentPath);
  	                    	Log.v(TAG, "remove before n="+mImagePathList.size());
  	                    	try
  	                    	{
  	                    	    /*String[] newPaths=(String[])list.toArray(new String[list.size()]);
  	                    	    galleryAdapter=new GalleryAdapter(ImageGalleryView.this, newPaths, displayWidth, displayHeight);
  	                            gallery.setAdapter(galleryAdapter);
  	                        
  	                    	    //gallery.removeView(mCurrentImageView); //.removeViewAt(currentPosition);
  	                    	    galleryAdapter.notifyDataSetChanged();
  	                    	    gallery.invalidate();
  	                    	    gallery.setSelection(currentPosition%newPaths.length);*/  	                           
  	                    	    file.delete();
  	                    	    Toast.makeText(ImageGalleryView.this,
  	                    	    		(String) getText(R.string.picture_file)+" "+file.getName()
  	                    	    		+" "+(String) getText(R.string.delete_success),Toast.LENGTH_SHORT).show();
  	                    	}
  	                    	catch(Exception err)
  	                    	{
  	                    		err.printStackTrace();
  	                    		Toast.makeText(ImageGalleryView.this, R.string.unkonw_err,Toast.LENGTH_SHORT).show();
  	                    		return;
  	                    	}
  	                    }
  	                })
  	        .setNegativeButton(R.string.cancel,
  	                new DialogInterface.OnClickListener() {
  	                    public void onClick(DialogInterface dialog,
  	                            int whichButton) {

  	                        /* User clicked OK so do some stuff */
  	                    }
  	                })
  	        .show();
  	   }
	}

	private void showDetails() {
		// TODO Auto-generated method stub
		currentPath=mImagePathList.get(mCurrentPosition);
		final File file=new File(currentPath);
		 String[] items={(String) getText(R.string.picture_name)+": "+file.getName(),
				 (String) getText(R.string.picture_path)+": "+file.getParentFile().getAbsolutePath(),
				 (String) getText(R.string.picture_size)+": "+Common.getFileSize(Common.getFileSize(file)), 
				 (String) getText(R.string.picture_modified_date)+": "+Common.getFileDateTime(file)}; 
		 
		                  //閸栧懎鎯堟径姘嚋闁銆嶉惃鍕嚠鐠囨繃顢� 
		 AlertDialog dialog = new AlertDialog.Builder(ImageGalleryView.this)  
		           .setIcon(R.drawable.details)  
		           .setTitle(R.string.details)
		           .setItems(items, onShowDetailsSelect).create();  
		 dialog.show();  
	}
	
	private void setEffects() {
		// TODO Auto-generated method stub
		 String[] items={"底片效果",
				 "浮雕效果", 
				 "黑白效果",
				 "柔化效果",
				 "锐化效果",}; 
		 
		                  //閸栧懎鎯堟径姘嚋闁銆嶉惃鍕嚠鐠囨繃顢� 
		 AlertDialog dialog = new AlertDialog.Builder(ImageGalleryView.this)  
		           .setIcon(R.drawable.details)  
		           .setTitle(R.string.set_effects)
		           .setItems(items, onSetEffectsSelect).create();  
		 dialog.show();  
	}
	
	OnClickListener onShowRotateSelect = new OnClickListener() {  
	    public void onClick(DialogInterface dialog, int which) {  
	         // TODO Auto-generated method stub  
	      switch(which)
	      {
	        case 0:
	        	rotate(-90);
	    		break;
	        case 1:
	        	rotate(90);
	    		break;
	        case 2:
	        	rotate(180);
	    		break;
	      }
	   }
    };

	OnClickListener onShowDetailsSelect = new OnClickListener() {  
		    public void onClick(DialogInterface dialog, int which) {  
		         // TODO Auto-generated method stub  
		    	switch(which)
		    	{
		           default:
		    	      break;
		    	}
		   }
	};
	
	OnClickListener onShowThumbnailSelect = new OnClickListener() {  
	    public void onClick(DialogInterface dialog, int which) {  
	         // TODO Auto-generated method stub  
	      switch(which)
	      {
	        case 0:	
	        	try {	    			
	        		mCurrentImageView.zoomIn();	        		
	    		} catch (Exception e) {
	    			Log.i(TAG, "nonono, had err when thumbnail picture");
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    			Toast.makeText(ImageGalleryView.this, R.string.no_memory,Toast.LENGTH_SHORT).show();  
	    		}
	    		break;
	        case 1:
	        	try {	    			 
	        		mCurrentImageView.zoomOut();
	    		} catch (Exception e) {
	    			// TODO Auto-generated catch block
	    			Log.i(TAG, "nononono, had err when thumbnail picture");
	    			e.printStackTrace();
	    			Toast.makeText(ImageGalleryView.this, R.string.no_memory,Toast.LENGTH_SHORT).show();  
	    		}
	    		break;
	      }
	   }
};
	
	OnClickListener onShowSetUpSelect = new OnClickListener() {  
	    public void onClick(DialogInterface dialog, int which) {  
	         // TODO Auto-generated method stub  
	    	switch(which)
		    {
		        case 0:
		        	try {
		    			 setWallpaper(currentBitmap);
		    			 Toast.makeText(ImageGalleryView.this, R.string.setup_wallpaper_success,Toast.LENGTH_SHORT).show();
		    		} catch (IOException e) {
		    			// TODO Auto-generated catch block
		    			e.printStackTrace();
		    			Toast.makeText(ImageGalleryView.this, R.string.unkonw_err,Toast.LENGTH_SHORT).show();  
		    		}
		    		break;
		    }
	   }
    };
    private android.view.View.OnClickListener onSaveConfirmListener = new android.view.View.OnClickListener(){
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			// TODO Auto-generated method stub
			File file = new File(mImagePathList.get(mCurrentPosition));
	          FileOutputStream out;
	          try{
	                out = new FileOutputStream(file);
	                if(effectsBitmap.compress(Bitmap.CompressFormat.PNG, 100, out))
	                {
	                    out.flush();
	                    out.close();
	                    return ;
	                }
	          }
	          catch (FileNotFoundException e)
	          {
	        	   Log.i(TAG, "saveBitmap has err! FileNotFoundException!");
	               e.printStackTrace();
	          }
	          catch (IOException e)
	          {
	        	  Log.i(TAG, "saveBitmap has err! IOException!");
	               e.printStackTrace();
	          }
			   mSaveConfirm.setVisibility(View.GONE);
			   mSaveCancel.setVisibility(View.GONE);
		}
    	
    };
    private android.view.View.OnClickListener onSaveCancelListener = new android.view.View.OnClickListener(){

		public void onClick(View v) {
			// TODO Auto-generated method stub
			 mCurrentImageView.setImageBitmap(currentBitmap);	
			   mSaveConfirm.setVisibility(View.GONE);
			   mSaveCancel.setVisibility(View.GONE);
		}
    	
    };
	OnClickListener onSetEffectsSelect = new OnClickListener() {  
	    public void onClick(DialogInterface dialog, int which) {  
	          
	      switch(which)
	      {
	        case 0:
	        	try{	
	        		Log.v(TAG, "Set Effects!");
	    			int Width = currentBitmap.getWidth();
	    			int Height = currentBitmap.getHeight();
	    			Log.v(TAG, "Set Effects!w="+Width+"h="+Height);
	    			int [] pixelBuf = new int[Width * Height];	
	    			Log.v(TAG, "Set Effects!pixelBuf.length="+pixelBuf.length);
	    			effectsBitmap=Bitmap.createBitmap(Width, Height, Config.ARGB_8888);
					//Bitmap newBitmap=Bitmap.createBitmap(currentBitmap);
	    			currentBitmap.getPixels(pixelBuf, 0, Width, 0, 0, Width, Height);
	    			Log.v(TAG, "Set Effects!w="+Width+"h="+Height);
	        		for(int x=0;x<Width;x++)
	        			for(int y=0;y<Height;y++)
	        			{
	        				int r;
	        				int g;
	        				int b;	
	        				int index = y*Width+x;
	        				r=255-(pixelBuf[index]>>16)&0xff;
	        				g=255-(pixelBuf[index]>>8)&0xff;
	        				b=255-(pixelBuf[index])&0xff;
	        				pixelBuf[index]=0xFF000000|(r<<16)|(g<<8)|b;
	        			}
	        		Log.v(TAG, "Set Effects!");	        		
	        		effectsBitmap.setPixels(pixelBuf, 0, Width, 0, 0, Width, Height);
	        	   mCurrentImageView.setImageBitmap(effectsBitmap);	 
	    		   mSaveConfirm.setVisibility(View.VISIBLE);
	    		   mSaveCancel.setVisibility(View.VISIBLE);
	        	   //currentBitmap=newBitmap;
	        	   //mCurrentImageView.setImageRotateBitmapResetBase(new RotateBitmap(newBitmap,0), false);
	        	   
	        	}
	        	catch(Exception e)
	        	{
	        		Log.v(TAG, "Set Effects Fails!");
	        	}
	    		break;
	        case 1:
	        	rotate(90);
	    		break;
	        case 2:
	        	rotate(180);
	    		break;
	      }
	   }
    };
	@Override
	 public boolean onContextItemSelected(MenuItem item) { 
		 
		 switch (item.getItemId()) {  
		   case 0:
			   rotate(-90);
			   return true;
		   case 1:
			   rotate(90);
			   return true;
		   case 2:
			   rotate(180);
			   return true;
		   default:  
			   return super.onContextItemSelected(item);  
	     }
	 }
}
class ImageViewTouch extends ImageViewTouchBase {
    private final ImageGalleryView mViewImage;
    private boolean mEnableTrackballScroll;

    public ImageViewTouch(Context context) {
        super(context);
        mViewImage = (ImageGalleryView) context;
    }

    public ImageViewTouch(Context context, AttributeSet attrs) {
        super(context, attrs);
        mViewImage = (ImageGalleryView) context;
    }

    public void setEnableTrackballScroll(boolean enable) {
        mEnableTrackballScroll = enable;
    }

    protected void postTranslateCenter(float dx, float dy) {
        super.postTranslate(dx, dy);
        center(true, true);
    }

    private static final float PAN_RATE = 20;

    // This is the time we allow the dpad to change the image position again.
    private long mNextChangePositionTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mViewImage.mPaused) return false;

        // Don't respond to arrow keys if trackball scrolling is not enabled
        if (!mEnableTrackballScroll) {
            if ((keyCode >= KeyEvent.KEYCODE_DPAD_UP)
                    && (keyCode <= KeyEvent.KEYCODE_DPAD_RIGHT)) {
                return super.onKeyDown(keyCode, event);
            }
        }

        int current = mViewImage.mCurrentPosition;

        int nextImagePos = -2; // default no next image
        try {
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_LEFT: {
                    if (getScale() <= 1F && event.getEventTime()
                            >= mNextChangePositionTime) {
                        nextImagePos = current - 1;
                        mNextChangePositionTime = event.getEventTime() + 500;
                    } else {
                        panBy(PAN_RATE, 0);
                        center(true, false);
                    }
                    return true;
                }
                case KeyEvent.KEYCODE_DPAD_RIGHT: {
                    if (getScale() <= 1F && event.getEventTime()
                            >= mNextChangePositionTime) {
                        nextImagePos = current + 1;
                        mNextChangePositionTime = event.getEventTime() + 500;
                    } else {
                        panBy(-PAN_RATE, 0);
                        center(true, false);
                    }
                    return true;
                }
                case KeyEvent.KEYCODE_DPAD_UP: {
                    panBy(0, PAN_RATE);
                    center(false, true);
                    return true;
                }
                case KeyEvent.KEYCODE_DPAD_DOWN: {
                    panBy(0, -PAN_RATE);
                    center(false, true);
                    return true;
                }
                /*case KeyEvent.KEYCODE_DEL:
                    MenuHelper.deletePhoto(
                            mViewImage, mViewImage.mDeletePhotoRunnable);
                    break;*/
            }
        } finally {
            if (nextImagePos >= 0
                    && nextImagePos < mViewImage.mImagePathList.size()) {
                synchronized (mViewImage) {
                    //mViewImage.setMode(ImageGalleryView.MODE_NORMAL);
                    mViewImage.setImage(nextImagePos, true);
                }
           } else if (nextImagePos != -2) {
               center(true, true);
           }
        }

        return super.onKeyDown(keyCode, event);
    }
}

// This is a cache for Bitmap displayed in ViewImage (normal mode, thumb only).
class BitmapCache implements ImageViewTouchBase.Recycler {
    public static class Entry {
        int mPos;
        Bitmap mBitmap;
        public Entry() {
            clear();
        }
        public void clear() {
            mPos = -1;
            mBitmap = null;
        }
    }

    private final Entry[] mCache;

    public BitmapCache(int size) {
        mCache = new Entry[size];
        for (int i = 0; i < mCache.length; i++) {
            mCache[i] = new Entry();
        }
    }

    // Given the position, find the associated entry. Returns null if there is
    // no such entry.
    private Entry findEntry(int pos) {
        for (Entry e : mCache) {
            if (pos == e.mPos) {
                return e;
            }
        }
        return null;
    }

    // Returns the thumb bitmap if we have it, otherwise return null.
    public synchronized Bitmap getBitmap(int pos) {
        Entry e = findEntry(pos);
        if (e != null) {
            return e.mBitmap;
        }
        return null;
    }

    public synchronized void put(int pos, Bitmap bitmap) {
        // First see if we already have this entry.
        if (findEntry(pos) != null) {
            return;
        }

        // Find the best entry we should replace.
        // See if there is any empty entry.
        // Otherwise assuming sequential access, kick out the entry with the
        // greatest distance.
        Entry best = null;
        int maxDist = -1;
        for (Entry e : mCache) {
            if (e.mPos == -1) {
                best = e;
                break;
            } else {
                int dist = Math.abs(pos - e.mPos);
                if (dist > maxDist) {
                    maxDist = dist;
                    best = e;
                }
            }
        }

        // Recycle the image being kicked out.
        // This only works because our current usage is sequential, so we
        // do not happen to recycle the image being displayed.
        if (best.mBitmap != null) {
            best.mBitmap.recycle();
        }

        best.mPos = pos;
        best.mBitmap = bitmap;
    }

    // Recycle all bitmaps in the cache and clear the cache.
    public synchronized void clear() {
        for (Entry e : mCache) {
            if (e.mBitmap != null) {
                e.mBitmap.recycle();
            }
            e.clear();
        }
    }

    // Returns whether the bitmap is in the cache.
    public synchronized boolean hasBitmap(int pos) {
        Entry e = findEntry(pos);
        return (e != null);
    }

    // Recycle the bitmap if it's not in the cache.
    // The input must be non-null.
    public synchronized void recycle(Bitmap b) {
        for (Entry e : mCache) {
            if (e.mPos != -1) {
                if (e.mBitmap == b) {
                    return;
                }
            }
        }
        b.recycle();
    }
}
