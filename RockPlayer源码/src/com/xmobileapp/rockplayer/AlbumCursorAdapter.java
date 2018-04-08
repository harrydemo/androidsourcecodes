/*
 * [程序名称] Android 音乐播放器
 * [参考资料] http://code.google.com/p/rockon-android/ 
 * [开源协议] Apache License, Version 2.0 (http://www.apache.org/licenses/LICENSE-2.0)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xmobileapp.rockplayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.xmobileapp.rockplayer.R;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.BitmapFactory.Options;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.GradientDrawable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout.LayoutParams;


public class AlbumCursorAdapter extends SimpleCursorAdapter{
	String						FILEX_FILENAME_EXTENSION = "";
    String						FILEX_ALBUM_ART_PATH = "/sdcard/albumthumbs/RockOn/";
    String						FILEX_SMALL_ALBUM_ART_PATH = "/sdcard/albumthumbs/RockOn/small/";
    String						FILEX_CONCERT_PATH = "/sdcard/RockOn/concert/";
    double 						CURRENT_PLAY_SCREEN_FRACTION = 0.66;
    double 						CURRENT_PLAY_SCREEN_FRACTION_LANDSCAPE = 0.75;
    double 						NAVIGATOR_SCREEN_FRACTION = 1 - CURRENT_PLAY_SCREEN_FRACTION;
    double		 				NAVIGATOR_SCREEN_FRACTION_LANDSCAPE = 1 - CURRENT_PLAY_SCREEN_FRACTION_LANDSCAPE;
    
    int							BITMAP_SIZE_SMALL = 0;
    int							BITMAP_SIZE_NORMAL = 1;
    int							BITMAP_SIZE_FULLSCREEN = 2;
    int							BITMAP_SIZE_XSMALL = 3;
    
    private final int 			NO_COVER_SAMPLING_INTERVAL = 1;
    private Cursor 				cursor;
    public Context	 			context;
    public int					viewWidth;
    public int					viewWidthNormal;
    public int					viewWidthBig;
    public int 					viewHeightBig;
    private LayoutParams 		params = null;
    private GradientDrawable	overlayGradient = null;
    public Bitmap				albumCoverBitmap;
    public BitmapFactory		bitmapDecoder = new BitmapFactory();
    public ImageView 			albumImage = null;
    public ImageView 			albumImageOverlay = null;
    public TextView				albumImageAlternative = null;
    public TextView				albumNameTextView = null;
    public TextView				albumArtistTextView = null;
	public String 				albumCoverPath = null;    
	public String 				artistName = null;
	public String 				albumName = null;
	public String 				path = null;
	public File 				albumCoverFilePath = null;
	public Options 				opts = null;
	public Bitmap[] 			albumImages;
	public Bitmap				albumUndefinedCoverBitmap;
	public Bitmap				albumUndefinedCoverBigBitmap;
	public	boolean				PRELOAD = false;
	public boolean				isScrolling = false;
	public int					AVOID_PRELOAD_THRESHOLD = 100;
	public boolean				showArtWhileScrolling = false;
	public boolean				showFrame = false;

    public AlbumCursorAdapter(Context context, 
    							int layout, 
    							Cursor c,
    							String[] from,
    							int[] to,
    							Bitmap[] albumImages,
    							boolean showArtWhileScrolling,
    							boolean showFrame){
        super(context, layout, c, from, to);
        this.cursor = c;
        this.context = context;
        this.showArtWhileScrolling = showArtWhileScrolling;
        this.showFrame = showFrame;
        
        /*
         * Read the width of the Navigator
         */
//    	if(((Filex) context).display.getOrientation() == 0)
//	        viewWidth = (int) Math.floor(
//	        				((Filex) context).display.getWidth() * 
//	        				((Filex) context).NAVIGATOR_SCREEN_FRACTION
//	        				);
//    	else    
//    		viewWidth = (int) Math.floor(
//    				((Filex) context).display.getWidth() * 
//    				((Filex) context).NAVIGATOR_SCREEN_FRACTION_LANDSCAPE
//    				);
        reloadNavigatorWidth();
        
    	//params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 1);
        
        /*
         * Create the Overlay Gradient
         */
//        overlayGradient = new GradientDrawable(GradientDrawable.Orientation.TL_BR,
//                								new int[] { 0x00000000, 
//        													0x22FFFFFF,
//        													0x00000000 });
//        overlayGradient.setGradientCenter((float) Math.floor(viewWidth * 0.75),
//        									(float) Math.floor(viewWidth * 0.75));
//        //overlayGradient.setGradientRadius((float) (viewWidth*0.75));
//        //overlayGradient.setGradientType(GradientDrawable.RECTANGLE);
//        overlayGradient.setShape(GradientDrawable.RECTANGLE);
        
        /*
         * Preload the undefined album image
         */
		// TODO:
		// adjust sample size dynamically
		opts = new Options();
		opts.inSampleSize = NO_COVER_SAMPLING_INTERVAL;
		//Bitmap albumCoverBitmap = BitmapFactory.decodeFile(albumCoverPath, opts);
//		albumUndefinedCoverBitmap = bitmapDecoder.decodeResource(this.context.getResources(),
//														R.drawable.albumart_mp_unknown, opts);
		albumUndefinedCoverBitmap = this.createFancyAlbumCoverFromResource(
				R.drawable.albumart_mp_unknown,
				viewWidth,
				viewWidth);
    	
//		albumUndefinedCoverNormalBitmap = this.createFancyAlbumCoverFromResource(
//				R.drawable.albumart_mp_unknown,
//				viewWidthNormal,
//				viewWidthNormal);
		
		albumUndefinedCoverBigBitmap = this.createFancyAlbumCoverFromResource(
				R.drawable.albumart_mp_unknown,
				viewWidthBig,
				viewHeightBig);
    	
    	/*
         * Preload album Images into memory to avoid dynamic loading
         */
        if(c.getCount() < AVOID_PRELOAD_THRESHOLD){
        	this.albumImages = albumImages; 
//        	albumImages = new Bitmap[c.getCount()];
//        	final AlbumCursorAdapter adapter = this;
//        	new Thread(){
//        		@Override
//        		public void run(){
//        			adapter.preloadAlbumImages(albumImages);
//        		}
//        	}.start();
        	//preloadAlbumImages(albumImages);
        	PRELOAD = true;
        }
        

    }

    /*
     * reloadNavigatorWidth
     */
    public void reloadNavigatorWidth(){

        WindowManager windowManager 		= (WindowManager) 
			context.getSystemService(Context.WINDOW_SERVICE);

        Display display						= windowManager.getDefaultDisplay();
		
		if(display.getOrientation() == 0){
			viewWidth = (int) Math.floor(
				display.getWidth() * 
				NAVIGATOR_SCREEN_FRACTION
				);
		
			viewWidthNormal = (int) Math.floor(
				display.getWidth() * 
				(1-NAVIGATOR_SCREEN_FRACTION)
				);
			viewWidthBig = (int) Math.min(
				display.getWidth(),
				display.getHeight()
				);
		}else{    
			viewWidth = (int) Math.floor(
				display.getWidth() * 
				NAVIGATOR_SCREEN_FRACTION_LANDSCAPE
				);
			viewWidthNormal = (int) Math.floor(
				display.getWidth() * 
				(1-NAVIGATOR_SCREEN_FRACTION_LANDSCAPE) / 2
				);
			viewWidthBig = (int) Math.min(
				display.getWidth(),
				display.getHeight()
				);
		}
		viewWidthBig = display.getWidth() - 30;
		viewHeightBig = display.getHeight() - 60;
		
		viewWidthBig = 320;
		viewHeightBig = 320;
		
		params = new LayoutParams(viewWidth, viewWidth);
    }
    
    /* (non-Javadoc)
     * This is where you actually create the item view of the list
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
    	try{
	    	/*
	    	 * Get the item list image component set its height right
	    	 */
	    	albumImage = (ImageView)
	    		view.findViewById(R.id.navigator_albumart_image);
	    	albumImage.setLayoutParams(params);
	    	
	    	albumImageOverlay = (ImageView)
	    		view.findViewById(R.id.navigator_albumart_overlay);
	    	//albumImageOverlay.setImageDrawable(overlayGradient);
	    	albumImageOverlay.setLayoutParams(params);
	    	if(this.showFrame){
	    		albumImageOverlay.setVisibility(View.VISIBLE);
	    	} else {
	    		albumImageOverlay.setVisibility(View.GONE);
	    	}

	    	// TODO: needs a if(albums in full screen)
	    	albumNameTextView = (TextView)
	    									view.findViewById(R.id.navigator_albumname);
	    	albumArtistTextView = (TextView)
	    									view.findViewById(R.id.navigator_albumartist);
	    	albumNameTextView.setText(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM)));
	    	albumArtistTextView.setText(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST)));
	    	
	    	
	    	if(isScrolling && !showArtWhileScrolling){
		    	albumImageAlternative = (TextView)
		    		view.findViewById(R.id.navigator_albumart_alternative);
		    	albumImageAlternative.setLayoutParams(params);
		    	albumImageAlternative.setVisibility(View.VISIBLE);
		    	albumImageAlternative.setText(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST)));
		    	albumImage.setImageBitmap(albumUndefinedCoverBitmap);
	    	} else {
		    	/*
		    	 * if preload is in use, get the preloaded bitmap, otherwise go get it
		    	 */
		    	if(PRELOAD){
		    		if(albumImage != null && cursor != null){
		    			if(albumImages[cursor.getPosition()] != null)
		    				albumImage.setImageBitmap(albumImages[cursor.getPosition()]);
		    			else
		    				albumImage.setImageBitmap(albumUndefinedCoverBitmap);
		    		}
		    		return;
		    	} else {
		    		albumImage.setImageBitmap(getAlbumBitmap(cursor.getPosition(), BITMAP_SIZE_SMALL));
		    		return;
		    	}
	    	}
    	} catch(Exception e) {
    		//albumImage.setImageBitmap(albumUndefinedCoverBitmap);
    		e.printStackTrace();
    		return;
    	}
    	
//    	/*
//    	 * Get Album Art pointer
//    	 */
//    	albumCoverPath = null;
////    	String albumCoverPath = cursor.getString(
////    								cursor.getColumnIndex(
////    									MediaStore.Audio.Albums.ALBUM_ART));
////    	// if it does not exist in database look for our dir
////    	if(albumCoverPath == null){
//    		artistName = cursor.getString(
//									cursor.getColumnIndex(
//											MediaStore.Audio.Albums.ARTIST));
//    		albumName = cursor.getString(
//									cursor.getColumnIndex(
//											MediaStore.Audio.Albums.ALBUM));
//    		path = FILEX_SMALL_ALBUM_ART_PATH+
//    						validateFileName(artistName)+
//							" - "+
//							validateFileName(albumName)+
//							FILEX_FILENAME_EXTENSION;
//    		Log.i("LOAD_ART", path);
//    		albumCoverFilePath = new File(path);
//			if(albumCoverFilePath.exists() && albumCoverFilePath.length() > 0){
//				albumCoverPath = path;
//			}
////    	}
//    	
//    	/*
//		 * If the album art exists put it in the listView, otherwise
//		 * just use the default image
//		 */
//		if(albumCoverPath != null){
//			/*
//			 * First check the albumThumbsize and then get the album art just
//			 * with the resolution that is strictly required
//			 */
////			Options opts = new Options();
////			opts.inJustDecodeBounds = true;
////			Bitmap albumCoverBitmap = BitmapFactory.decodeFile(albumCoverPath, opts);
////
////			opts.inJustDecodeBounds = false;
////			opts.inSampleSize = (int) Math.max(1, 
////												Math.floor(opts.outWidth/viewWidth)
////												);
////			Log.i("ALBUMADAPTER", ""+opts.inSampleSize);
//			//Optimization time
//			//opts.inSampleSize = 3;
////			albumCoverBitmap = BitmapFactory.decodeFile(albumCoverPath, opts);
//			albumCoverBitmap = bitmapDecoder.decodeFile(albumCoverPath);
//			
//			//albumCoverBitmap = Bitmap.createScaledBitmap(albumCoverBitmap,
//			//												30,
//			//												30,
//			//												false);
//			
//			if(albumCoverBitmap != null)
//				albumImage.setImageBitmap(albumCoverBitmap);
//		} else {
//			// TODO:
//			// adjust sample size dynamically
//			opts = new Options();
//			opts.inSampleSize = NO_COVER_SAMPLING_INTERVAL;
//			//Bitmap albumCoverBitmap = BitmapFactory.decodeFile(albumCoverPath, opts);
//			albumCoverBitmap = bitmapDecoder.decodeResource(this.context.getResources(),
//															R.drawable.albumart_mp_unknown, opts);
//			if(albumCoverBitmap != null)
//				albumImage.setImageBitmap(albumCoverBitmap);
//		}
    }
    
    /*********************************
     * 
     * PreloadAlbumImages
     * 
     *********************************/
    public void	preloadAlbumImages(Bitmap[] albumImages){
//    	cursor.moveToFirst();
    	for(int i=0; i< cursor.getCount(); i++){
//    	while(!cursor.isAfterLast()){
    		cursor.moveToPosition(i);
    		Log.i("PRELOAD", ""+cursor.getPosition());
    		albumImages[cursor.getPosition()] = getAlbumBitmap(cursor.getPosition(), BITMAP_SIZE_XSMALL);
//    		cursor.moveToNext();
    	}
    }
    
    /*********************************
     * 
     * albumArtExists
     * 
     *********************************/
    public boolean albumArtExists(int position){
    	cursor.moveToPosition(position);
    	
    	/*
    	 * Get the path to the album art
    	 */
    	albumCoverPath = null;
		artistName = cursor.getString(
								cursor.getColumnIndexOrThrow(
										MediaStore.Audio.Albums.ARTIST));
		albumName = cursor.getString(
								cursor.getColumnIndexOrThrow(
										MediaStore.Audio.Albums.ALBUM));
		path = FILEX_SMALL_ALBUM_ART_PATH+
						validateFileName(artistName)+
						" - "+
						validateFileName(albumName)+
						FILEX_FILENAME_EXTENSION;
		albumCoverFilePath = new File(path);
		if(albumCoverFilePath.exists() && albumCoverFilePath.length() > 0){
			albumCoverPath = path;
			return true;
		} else {
			return false;
		}
    }
    
    /*********************************
     * 
     * getAlbumBitmap
     *
     *********************************/
    int dim = 1;
    public Bitmap getAlbumBitmap(int position, int bitmapFuzzySize){
    	if(position == -1){
    		return albumUndefinedCoverBitmap;
    	}
    	
    	cursor.moveToPosition(position);
    		
    	/*
    	 * Get the path to the album art
    	 */
    	albumCoverPath = null;
		artistName = cursor.getString(
								cursor.getColumnIndexOrThrow(
										MediaStore.Audio.Albums.ARTIST));
		albumName = cursor.getString(
								cursor.getColumnIndexOrThrow(
										MediaStore.Audio.Albums.ALBUM));
		if(bitmapFuzzySize == BITMAP_SIZE_SMALL){
			path = FILEX_SMALL_ALBUM_ART_PATH+
				validateFileName(artistName)+
				" - "+
				validateFileName(albumName)+
				FILEX_FILENAME_EXTENSION;
    	}else{
    		path = cursor.getString(
    					cursor.getColumnIndexOrThrow(
    							MediaStore.Audio.Albums.ALBUM_ART));
    		
        	/* check if the embedded mp3 is valid (or big enough)*/
    		if(path != null){
    			FileInputStream pathStream = null;
				try {
					pathStream = new FileInputStream(path);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
	    		BitmapFactory.Options opts = new BitmapFactory.Options();
	    		opts.inJustDecodeBounds = true;
	    		Bitmap bmTmp = BitmapFactory.decodeStream(pathStream, null, opts);
	    		if(opts == null || opts.outHeight < 320 || opts.outWidth < 320)
	    			path = null;
	    		if(bmTmp != null)
	    			bmTmp.recycle();
	    		try {
					pathStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
    		}
    		
    		if(path == null){
	    		path = FILEX_ALBUM_ART_PATH+
					validateFileName(artistName)+
					" - "+
					validateFileName(albumName)+
					FILEX_FILENAME_EXTENSION;
    		}
    	}
    
		albumCoverFilePath = new File(path);
		if(albumCoverFilePath.exists() && albumCoverFilePath.length() > 0){
			albumCoverPath = path;
		}
		
		try{
			/*
			 * If the album art exists put it in the preloaded array, otherwise
			 * just use the default image
			 */
			if(albumCoverPath != null){
				if(bitmapFuzzySize == BITMAP_SIZE_XSMALL){
//					if(cursor.getCount() > 1 && cursor.getCount() < AVOID_PRELOAD_THRESHOLD){
//					dim = viewWidth*4;
					dim = Math.min(
							Math.max(Math.round(viewWidth * (30.0f/cursor.getCount())), Math.round(viewWidth/1.8f)),
							viewWidth);
						//Log.i("performance", ""+dim);
						albumCoverBitmap = createFancyAlbumCoverFromFilePath(albumCoverPath, dim, dim);
//					} else {
//						albumCoverBitmap = createFancyAlbumCoverFromFilePath(albumCoverPath, viewWidth, viewWidth);
//					}
				}
				else if(bitmapFuzzySize == BITMAP_SIZE_SMALL)
					albumCoverBitmap = createFancyAlbumCoverFromFilePath(albumCoverPath, viewWidth, viewWidth);
				else if(bitmapFuzzySize == BITMAP_SIZE_NORMAL)
					albumCoverBitmap = createFancyAlbumCoverFromFilePath(albumCoverPath, viewWidthNormal, viewWidthNormal);
				else if(bitmapFuzzySize == BITMAP_SIZE_FULLSCREEN)
					albumCoverBitmap = createFancyAlbumCoverFromFilePath(albumCoverPath, viewWidthBig, viewHeightBig);
				return albumCoverBitmap;
			} else {
				// TODO:
				// adjust sample size dynamically
//				opts = new Options();
//				opts.inSampleSize = NO_COVER_SAMPLING_INTERVAL;
//				albumCoverBitmap = bitmapDecoder.decodeResource(this.context.getResources(),
//																R.drawable.albumart_mp_unknown, opts);
//				albumCoverBitmap = createFancyAlbumCoverFromResource(R.drawable.albumart_mp_unknown, viewWidth, viewWidth);
				if(bitmapFuzzySize == BITMAP_SIZE_SMALL)
					albumCoverBitmap = createFancyAlbumCover(albumUndefinedCoverBitmap, viewWidth, viewWidth);
				else if(bitmapFuzzySize == BITMAP_SIZE_NORMAL)
					albumCoverBitmap = createFancyAlbumCover(albumUndefinedCoverBigBitmap, viewWidthNormal, viewWidthNormal);
				else if(bitmapFuzzySize == BITMAP_SIZE_FULLSCREEN)
					albumCoverBitmap = createFancyAlbumCover(albumUndefinedCoverBigBitmap, viewWidthBig, viewWidthBig);
				return albumCoverBitmap;
			}
		} catch(Exception e) {
			e.printStackTrace();
			if(bitmapFuzzySize == BITMAP_SIZE_XSMALL)
				return albumUndefinedCoverBitmap;
			else if(bitmapFuzzySize == BITMAP_SIZE_SMALL)
				return albumUndefinedCoverBitmap;
			else if(bitmapFuzzySize == BITMAP_SIZE_NORMAL)
				return albumUndefinedCoverBigBitmap;
			else if(bitmapFuzzySize == BITMAP_SIZE_FULLSCREEN)
				return albumUndefinedCoverBigBitmap;
			else
				return albumUndefinedCoverBigBitmap;
		}
    }
    
    
    Shader shader;
    RectF	rect = new RectF();
    Paint	paint= new Paint();

    
    /*********************************
     * 
     * createFancyAlbumCover
     * 
     *********************************/
//    Bitmap cBitmap = Bitmap.createBitmap(460, 460, Bitmap.Config.ARGB_8888);
    public Bitmap createFancyAlbumCover(Bitmap bitmap, int width, int height){

    	try{

    		/*
    		 * Adjust the aspect ratio of the incoming bitmap if needed
    		 */
    		float aspectRatio = (float)width/(float)height; 
    		if(Math.abs(aspectRatio - ((float)bitmap.getWidth()/(float)bitmap.getHeight())) > 0.1){
        		
    			if(aspectRatio > 1){ // width is larger
	    			bitmap = Bitmap.createBitmap(
		        				bitmap,
		        				0,
		        				(int)(bitmap.getHeight()-(bitmap.getHeight()/aspectRatio))/2,
		        				bitmap.getWidth(),
		        				(int)(bitmap.getHeight()/aspectRatio));
    			} else {
					bitmap = Bitmap.createBitmap(
	        				bitmap,
	        				(int)(bitmap.getWidth()-(bitmap.getWidth()*aspectRatio))/2,
	        				0,
	        				(int)(bitmap.getWidth()*aspectRatio),
	        				bitmap.getHeight());
    			}
    		}
    		
	//    	Bitmap tBitmap;
	    	paint.setAntiAlias(true);
	    	paint.setDither(true);
	    	//BlurMaskFilter blurFilter = new BlurMaskFilter(viewWidth/20.0f, BlurMaskFilter.Blur.INNER);
	    	
	        Bitmap cBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
	        Canvas canvas = new Canvas();
	        canvas.setBitmap(cBitmap); 
	        
	//        //paint.setXfermode(new PorterDuffXfermode(Mode.));
	//        paint.setMaskFilter(blurFilter);
	//	  	//paint.setAlpha(100);
	//	  	tBitmap = Bitmap.createScaledBitmap(bitmapDecoder.decodeFile(path),
	//	  		width, height, false);
	//	  	canvas.drawBitmap(tBitmap, 0, 0, paint);
	//	
	//	    
	//		paint.setXfermode(new PorterDuffXfermode(Mode.LIGHTEN)); 
	//		paint.setAlpha(150);
	//		tBitmap = Bitmap.createScaledBitmap(bitmapDecoder.decodeFile(path),
	//				width, height, false);
	//		canvas.drawBitmap(tBitmap, 0, 0, paint);
	
			//paint.setXfermode(new PorterDuffXfermode(Mode.DARKEN));
	        
	        paint.setXfermode(null);
	        if(bitmap != null){
		        Shader bmpShader = new BitmapShader(
		        		Bitmap.createScaledBitmap(
		        				bitmap,
		        				width, 
		        				height, 
		        				false),
	//	        		bitmap,
		        		TileMode.CLAMP,
		        		TileMode.CLAMP);
		        paint.setShader(bmpShader);
	        }
	        
	//        rect = new RectF();
	        rect.left = 1.0f;
	        rect.top = 1.0f;
	        rect.right = width - 1.0f;
	        rect.bottom = height - 1.0f; 
	        if(showFrame){
	        	canvas.drawRoundRect(rect, width/40.0f, height/40.0f, paint);
	        } else {
	        	canvas.drawRoundRect(rect, width/30.0f, height/30.0f, paint);
	        }
	
	        
	//        int[] gradColors = {0x44222222, 0x44EEEEEE, 0x44FFFFFF};
	//        float[] gradColorPositions = {0, 0.75f, 1.0f};
	//		//paint.setXfermode(new PorterDuffXfermode(Mode.LIGHTEN)); 
	//        Shader gradientShader = new LinearGradient(
	//        		0,0,
	//                0,(int) (height/3),
	//                gradColors, 
	//                gradColorPositions,
	//                TileMode.CLAMP);
	        
	
	//      int[] gradColors = {0x33333333, 0x55333333, 0x33333333};
	//      float[] gradColorPositions = {0, 0.85f, 1.0f};
	//      paint.setXfermode(new PorterDuffXfermode(Mode.MULTIPLY)); 
	//      Shader gradientShader = new LinearGradient(
	//      		0,0,
	//              0,(int) (height/3),
	//              gradColors, 
	//              gradColorPositions,
	//              TileMode.CLAMP);
	      
	        
	//        paint.setShader(gradientShader);
	// 
	////        rect = new RectF();
	//        rect.left = (float) -width;
	//        rect.top = (float) -height/2;
	//        rect.right = (float) 2*width;
	//        rect.bottom = (float) height/3; 
	//        canvas.drawOval(rect, paint);
	        
	//        paint  = new Paint();
	//        paint.setShadowLayer(6.0f, 0, 0, Color.WHITE);
	//        paint.setStyle(Paint.Style.STROKE);
	//        paint.setStrokeWidth(2.0f);
	//        paint.setColor(Color.WHITE);
	//        canvas.drawRoundRect(rect, 128.0f, 12.0f, paint);
	
	//        paint.setXfermode(new PorterDuffXfermode(Mode.MULTIPLY));
	//        //paint.setMaskFilter(new BlurMaskFilter(viewWidth/2.0f, BlurMaskFilter.Blur.NORMAL));
	//        //paint.setAlpha(100);
	//        
	//        tBitmap = Bitmap.createScaledBitmap(bitmapDecoder.decodeFile(path),
	//        		width, height, false);
	//        canvas.drawBitmap(tBitmap, 0, 0, paint);
    	
	        return cBitmap;
    	} catch (Exception e) {
    		e.printStackTrace();
    		return albumUndefinedCoverBitmap;
    	} catch (Error e) {
    		e.printStackTrace();
    		return albumUndefinedCoverBigBitmap;
    	}
    	
    }
    
    
    BitmapFactory.Options bitOpts = new BitmapFactory.Options();
    byte[] tmpBitmapAlloc = new byte[1024*8];
    //byte[] tmpBitmapAlloc2 = new byte[1024*64];
    /*********************************
     * 
     * createFancyAlbumCoverFromFilePath
     * 
     *********************************/
    public Bitmap createFancyAlbumCoverFromFilePath(String path, int width, int height){
    	bitOpts.inTempStorage = tmpBitmapAlloc; 
    	
      	if (false)
    		return bitmapDecoder.decodeFile(path, bitOpts);
    	
    	
    	try{
    		FileInputStream pathStream = new FileInputStream(path);
    		Bitmap tmpBm = bitmapDecoder.decodeStream(pathStream, null, bitOpts);
    		Bitmap bm = createFancyAlbumCover(tmpBm, width,height);
    		tmpBm.recycle();
    		pathStream.close();
    		return bm;
    	} catch(Exception e) {
    		return createFancyAlbumCover(albumUndefinedCoverBitmap, width,height);
    	}catch (Error err){
    		err.printStackTrace();
    		return albumUndefinedCoverBitmap;
    	}
    	
    }
    
    /*********************************
     * 
     * createFancyAlbumCoverFromResource
     * 
     *********************************/
    public Bitmap createFancyAlbumCoverFromResource(int res, int width, int height){
    	bitOpts.inTempStorage = tmpBitmapAlloc; 

    	if (false)
    		return bitmapDecoder.decodeResource(context.getResources(), res, bitOpts);
    	try{
	    	Bitmap bmpTmp = bitmapDecoder.decodeResource(context.getResources(), res, bitOpts);
	    	Bitmap bm = createFancyAlbumCover(bmpTmp, width,height);
	    	bmpTmp.recycle();
	    	return bm;
    	}catch(Error err){
    		err.printStackTrace();
    		return albumUndefinedCoverBitmap;
    	}
    }
    
	/*********************************
	 * 
	 * ValidateFilename
	 * 
	 *********************************/
	private String validateFileName(String fileName) {
		if(fileName == null)
			return "";
		fileName = fileName.replace('/', '_');
		fileName = fileName.replace('<', '_');
		fileName = fileName.replace('>', '_');
		fileName = fileName.replace(':', '_');
		fileName = fileName.replace('\'', '_');
		fileName = fileName.replace('?', '_');
		fileName = fileName.replace('"', '_');
		fileName = fileName.replace('|', '_');
		return fileName;
	}
}