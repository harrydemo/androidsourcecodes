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

import com.xmobileapp.rockplayer.R;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;


/**
 * Simple widget to show currently playing album art along
 * with play/pause and next track buttons.  
 */
public class RockOnAppWidgetProvider extends AppWidgetProvider {
    static final String TAG = "RockOnAppWidgetProvider";
    
    static final String	FILEX_SMALL_ALBUM_ART_PATH = "/sdcard/albumthumbs/RockOn/small/";
    
    Bitmap albumUndefinedCoverBitmap = null;

    public static final String CMDAPPWIDGETUPDATE = "appwidgetupdate";
    
    static final ComponentName THIS_APPWIDGET =
        new ComponentName("org.abrantes.filex",
                "org.abrantes.filex.RockOnAppWidgetProvider");
    
    private static RockOnAppWidgetProvider sInstance;
     
    static synchronized RockOnAppWidgetProvider getInstance() {
        if (sInstance == null) {
            sInstance = new RockOnAppWidgetProvider();
        }
        return sInstance;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        defaultAppWidget(context, appWidgetIds);
        
        // Send broadcast intent to any running MediaPlaybackService so it can
        // wrap around with an immediate update.
        Intent updateIntent = new Intent(PlayerService.SERVICECMD);
        updateIntent.putExtra(PlayerService.CMDNAME,
                PlayerService.CMDAPPWIDGETUPDATE);
        updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        updateIntent.addFlags(Intent.FLAG_RECEIVER_REGISTERED_ONLY);
        context.sendBroadcast(updateIntent);
    }
    
    /**
     * Initialize given widgets to default state, where we launch Music on default click
     * and hide actions if service not running.
     */
    private void defaultAppWidget(Context context, int[] appWidgetIds) {
        final Resources res = context.getResources();
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.album_appwidget);
        
        views.setViewVisibility(R.id.title, View.GONE);
        views.setTextViewText(R.id.artist, res.getText(R.string.emptyplaylist));

        linkButtons(context, views, false /* not playing */);
        pushUpdate(context, appWidgetIds, views);
    }
    
    private void pushUpdate(Context context, int[] appWidgetIds, RemoteViews views) {
        // Update specific list of appWidgetIds if given, otherwise default to all
        final AppWidgetManager gm = AppWidgetManager.getInstance(context);
        if (appWidgetIds != null) {
            gm.updateAppWidget(appWidgetIds, views);
        } else {
            gm.updateAppWidget(THIS_APPWIDGET, views);
        }
    }
    
    /**
     * Check against {@link AppWidgetManager} if there are any instances of this widget.
     */
    private boolean hasInstances(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(THIS_APPWIDGET);
        Log.i("WIDGET", "widget count "+appWidgetIds.length);
        return (appWidgetIds.length > 0);
    }

    /**
     * Handle a change notification coming over from {@link MediaPlaybackService}
     */
    void notifyChange(PlayerService service, String what) {
    	Log.i("WIDGET", "updating widget");

    	// what is not being used right now...
        if (hasInstances(service)) {
        	performUpdate(service, null);
        }
    }
    
    /**
     * Update all active widget instances by pushing changes 
     */
    void performUpdate(PlayerService service, int[] appWidgetIds) {
        final Resources res = service.getResources();
        final RemoteViews views = new RemoteViews(service.getPackageName(), R.layout.album_appwidget);
        
		Log.i("WIDGET", "updating widget");

        
//        final int track = service.getQueuePosition() + 1;
        CharSequence titleName = service.getSongName();
        CharSequence artistName = service.getArtistName();
        CharSequence albumName = service.getAlbumName();
        CharSequence errorState = null;
        
        // Format title string with track number, or show SD card message
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_SHARED) ||
                status.equals(Environment.MEDIA_UNMOUNTED)) {
            errorState = res.getText(R.string.sdcard_busy_title);
        } else if (status.equals(Environment.MEDIA_REMOVED)) {
            errorState = res.getText(R.string.sdcard_missing_title);
        } else if (titleName == null) {
            errorState = res.getText(R.string.emptyplaylist);
        }
        
        if (errorState != null) {
            // Show error state to user
            views.setViewVisibility(R.id.title, View.GONE);
            views.setTextViewText(R.id.artist, errorState);
            
        } else {
            // No error, so show normal titles
            views.setViewVisibility(R.id.title, View.VISIBLE);
            views.setTextViewText(R.id.title, titleName);
            views.setTextViewText(R.id.artist, artistName);
         
            // show album cover
            Bitmap albumCoverCrude = BitmapFactory.decodeFile(
            		FILEX_SMALL_ALBUM_ART_PATH + 
            		validateFileName((String) artistName) + 
            		" - " +
            		validateFileName((String) albumName));
            if(albumCoverCrude == null)
            	albumCoverCrude = BitmapFactory.decodeResource(res, R.drawable.albumart_mp_unknown);
            Bitmap albumCover = createFancyAlbumCover(
            		albumCoverCrude,
            		50,
            		55);
            
            Log.i("WIDGET", FILEX_SMALL_ALBUM_ART_PATH + 
            		artistName + 
            		" - " +
            		albumName);
            
            views.setImageViewBitmap(R.id.album_cover, albumCover);
            albumCoverCrude.recycle();   
        }
        
        // Set correct drawable for pause state
        final boolean playing = service.isPlaying();
//        if (playing) {
//            views.setImageViewResource(R.id.control_play, R.drawable.appwidget_pause);
//        } else {
//            views.setImageViewResource(R.id.control_play, R.drawable.appwidget_play);
//        }
//
        // Link actions buttons to intents
        linkButtons(service, views, playing);
//        
        pushUpdate(service, appWidgetIds, views);
    }

    /**
     * Link up various button actions using {@link PendingIntents}.
     * 
     * @param playerActive True if player is active in background, which means
     *            widget click will launch {@link MediaPlaybackActivity},
     *            otherwise we launch {@link MusicBrowserActivity}.
     */
    private void linkButtons(Context context, RemoteViews views, boolean playerActive) {
        // Connect up various buttons and touch events
        Intent intent;
        PendingIntent pendingIntent;
        
        final ComponentName serviceName = new ComponentName(context, PlayerService.class);
        
        if (playerActive) {
            intent = new Intent(context, RockPlayer.class);
            pendingIntent = PendingIntent.getActivity(context,
                    0 /* no requestCode */, intent, 0 /* no flags */);
            views.setOnClickPendingIntent(R.id.album_appwidget, pendingIntent);
        } else {
            intent = new Intent(context, RockPlayer.class);
            pendingIntent = PendingIntent.getActivity(context,
                    0 /* no requestCode */, intent, 0 /* no flags */);
            views.setOnClickPendingIntent(R.id.album_appwidget, pendingIntent);
        }
//        
           
////        intent = new Intent(PlayerService.TOGGLEPAUSE_ACTION);

////        intent = new Intent(MediaPlaybackService.NEXT_ACTION);
        Intent intentNext = new Intent(PlayerService.SERVICECMD);
        intentNext.putExtra(PlayerService.CMDNAME,
        		PlayerService.CMDNEXT);
        intent.replaceExtras(intentNext);
        intent.setComponent(serviceName);
        PendingIntent pendingIntentNext = PendingIntent.getService(context,
                0 /* no requestCode */, intent, 0 /* no flags */);
        views.setOnClickPendingIntent(R.id.control_next, pendingIntentNext);

        
		intent = new Intent(PlayerService.SERVICECMD);
		intent.putExtra(PlayerService.CMDNAME,
			PlayerService.CMDTOGGLEPAUSE);
		intent.setComponent(serviceName);
		pendingIntent = PendingIntent.getService(context,
		    0 /* no requestCode */, intent, 0 /* no flags */);
		views.setOnClickPendingIntent(R.id.album_cover, pendingIntent);
		
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
//	        if(showFrame){
//	        	canvas.drawRoundRect(rect, width/40.0f, height/40.0f, paint);
//	        } else {
	        	canvas.drawRoundRect(rect, width/30.0f, height/30.0f, paint);
//	        }
	        	
	        return cBitmap;
    	} catch (Exception e) {
    		e.printStackTrace();
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