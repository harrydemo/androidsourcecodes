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

import java.io.IOException;
import java.util.Random;

import com.xmobileapp.rockplayer.PlayerServiceInterface;
import com.xmobileapp.rockplayer.R;

import com.xmobileapp.rockplayer.utils.Constants;
import com.xmobileapp.rockplayer.utils.RockOnPreferenceManager;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RemoteViews;

public class PlayerService extends Service{

	/**********************************************
	 * 
	 * Some Definitions
	 * 
	 **********************************************/
    final String[] ALBUM_COLS = new String[] {
    	MediaStore.Audio.Albums._ID,
        MediaStore.Audio.Albums.ALBUM,
        MediaStore.Audio.Albums.ALBUM_ART,
        MediaStore.Audio.Albums.ALBUM_KEY,
        MediaStore.Audio.Albums.ARTIST,
        MediaStore.Audio.Albums.FIRST_YEAR,
        MediaStore.Audio.Albums.LAST_YEAR,
        MediaStore.Audio.Albums.NUMBER_OF_SONGS
    };
    final String[] SONG_COLS = new String[] {
    	MediaStore.Audio.Media._ID,
    	MediaStore.Audio.Media.TITLE,
    	MediaStore.Audio.Media.TITLE_KEY,
    	MediaStore.Audio.Media.DATA,
    	MediaStore.Audio.Media.DISPLAY_NAME,
    	MediaStore.Audio.Media.TRACK,
    	MediaStore.Audio.Media.DURATION,
    	MediaStore.Audio.Media.IS_MUSIC,
    	MediaStore.Audio.Media.ALBUM,
    	MediaStore.Audio.Media.ALBUM_KEY,
    	MediaStore.Audio.Media.ARTIST,
    	MediaStore.Audio.Media.ARTIST_KEY,
    	MediaStore.Audio.Media.DATE_ADDED
    };
    String[] PLAYLIST_SONG_COLS = new String[] {
    		MediaStore.Audio.Playlists.Members._ID,
    		MediaStore.Audio.Playlists.Members.DATA,
    		MediaStore.Audio.Playlists.Members.DISPLAY_NAME,
    		MediaStore.Audio.Playlists.Members.DURATION,
            MediaStore.Audio.Playlists.Members.ALBUM_KEY,
            MediaStore.Audio.Playlists.Members.ALBUM,
            //MediaStore.Audio.Playlists.Members.ALBUM_ART,
            MediaStore.Audio.Playlists.Members.ARTIST,
            MediaStore.Audio.Playlists.Members.TRACK,
            MediaStore.Audio.Playlists.Members.PLAY_ORDER,
            MediaStore.Audio.Playlists.Members.TITLE,
            MediaStore.Audio.Playlists.Members.TITLE_KEY
    };
    
    final String		PREFS_NAME = "RockOnPreferences";
    final String		FILEX_PREFERENCES_PATH = "/sdcard/RockOn/preferences/";
    final String	 	PREFS_SHOW_ICON = "show_icon";
    private final int 	SONG_NOTIFICATION_ID = R.layout.songlist_item;
    
    /***************************************
     * 
     * Global Variables
     * 
     ***************************************/
    ContentResolver			contentResolver = null;
    Cursor					songCursor = null;
    Cursor					albumCursor = null;
    boolean					SHUFFLE = false;
    MediaPlayer				mediaPlayer = null;
    boolean					waitingOnCall = false;
    NotificationManager 	notificationManager = null;
    Notification			notification = null;
    RemoteViews 			contentView = null;
    //SharedPreferences		settings = null;
    Constants				constants = new Constants();
    long					playlist = constants.PLAYLIST_ALL;
    int						recentPeriod = constants.RECENT_PERIOD_DEFAULT_IN_DAYS;
    boolean					isPaused = true;
    static boolean			scrobbleDroid;	
    
    
    /*********************************************
     * 
     * Service intent stuff
     * 
     *********************************************/
    Intent	musicChangedIntent = new Intent(new Constants().MUSIC_CHANGED);
    Intent	albumChangedIntent = new Intent(new Constants().ALBUM_CHANGED);
    Intent	mediaButtonPauseIntent = new Intent(new Constants().MEDIA_BUTTON_PAUSE);
    Intent	mediaButtonPlayIntent = new Intent(new Constants().MEDIA_BUTTON_PLAY);
    
    private MediaButtonIntentReceiver 	mediaButtonIntentReceiver = null;

    public static final String SERVICECMD = "org.abrantes.filex.playerservicecmd";
    public static final String CMDNAME = "command";
    public static final String CMDTOGGLEPAUSE = "togglepause";
    public static final String CMDPAUSE = "pause";
    public static final String CMDPREVIOUS = "previous";
    public static final String CMDNEXT = "next";
    
    public static final String CMDAPPWIDGETUPDATE = "widget_update";
    
    /***************************************
     * 
     * AppWdiget
     * 
     ***************************************/
    // cant be declared for compatibility reasons
    //private RockOnAppWidgetProvider mAppWidgetProvider;
         
    private static boolean widgetAvailable;
    
    /* establish whether the "new" class is available to us */
    static {
        try {
        	Log.i("WIDGET-COMPAT","Checking widget compatibility");
            RockOnAppWidgetProviderWrapper.checkAvailable();
            widgetAvailable = true;
        } catch (Throwable t) {
        	Log.i("WIDGET-COMPAT","No Widgets");
            widgetAvailable = false;
        }
    }

    
    /***************************************
     * 
     * Called when the service is first created
     * 
     ***************************************/
    @Override
    public void onCreate(){
    	
    	/*
    	 * AppWidget
    	 */
//    	if(Integer.decode(Build.VERSION.SDK) > 2){
//    		mAppWidgetProvider = RockOnAppWidgetProvider.getInstance();
//    	}
    	
        /*
         * Get Preferences
         */
       // Shuffle
       RockOnPreferenceManager settings = new RockOnPreferenceManager(FILEX_PREFERENCES_PATH);
       boolean shuffle = settings.getBoolean("Shuffle", false);
   	   this.SHUFFLE = shuffle;
   	   Log.i("SVC", "Shuffle is "+shuffle);
   	   //Playlist
   	   //this.playlist = getSharedPreferences(PREFS_NAME, 0).getLong(constants.PREF_KEY_PLAYLIST, constants.PLAYLIST_ALL);
 
   	   /*
   	 	* Initialize mediaPlayer
   	 	*/
   	   mediaPlayer = new MediaPlayer();
       mediaPlayer.setOnCompletionListener(songCompletedListener);
     
    	/*
    	 * Initialize cursors to browse library
    	 */
    	try{
    		contentResolver = this.getContentResolver();
        	initializeAlbumCursor();
        	if(albumCursor.getCount() <= 0){
				try {
					this.finalize();
					return;
				} catch (Throwable e) {
					e.printStackTrace();
				}
        	}
        	moveAlbumCursorToLastPlayingPosition();
        	initializeSongCursor(
        			albumCursor.getString(
        					albumCursor.getColumnIndexOrThrow(
        							MediaStore.Audio.Albums.ALBUM)));
        	moveSongCursorToLastPlayingPosition();
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
       
       /*
    	 * Initialize mediaPlayer position
    	 */
    	try {
			mBinder.play(albumCursor.getPosition(), songCursor.getPosition());
			
			mBinder.seekTo((int) (new RockOnPreferenceManager(FILEX_PREFERENCES_PATH))
					.getLong(new Constants().KEY_PREFERENCE_LAST_SONG_POSITION, 0));
			mBinder.pause();
    	} catch (RemoteException e) {
			e.printStackTrace();
		}
    	
    	

        /*
         * Intent Listener (for mediabutton and appwidget)
         */
//        IntentFilter commandFilter = new IntentFilter();
//        commandFilter.addAction(SERVICECMD);
//        commandFilter.addAction(TOGGLEPAUSE_ACTION);
//        commandFilter.addAction(PAUSE_ACTION);
//        commandFilter.addAction(NEXT_ACTION);
//        commandFilter.addAction(PREVIOUS_ACTION);
        registerReceiver(intentReceiver, new IntentFilter(SERVICECMD));
        

   	   /*
   	    * Phone state reader
   	    */
   	   RockOnPhoneStateListener rockonPhoneStateListener = new RockOnPhoneStateListener();
	   TelephonyManager tManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
   	   tManager.listen(rockonPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
   	   
       /*
        * Register Media Button Receiver
        */
       mediaButtonIntentReceiver = new MediaButtonIntentReceiver();
       registerReceiver(mediaButtonIntentReceiver, new IntentFilter("android.intent.action.MEDIA_BUTTON"));

   	   /*
   	    * Notification Manager
   	    */
   	   	notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notification = new Notification(R.drawable.stat_notify_musicplayer,
													null,
													System.currentTimeMillis());
		if((new RockOnPreferenceManager(FILEX_PREFERENCES_PATH))
				.getBoolean(PREFS_SHOW_ICON, false) == true){
			notification.flags |= Notification.FLAG_ONGOING_EVENT;
		} else {
			notification.flags = Notification.FLAG_AUTO_CANCEL;
		}
		contentView = new RemoteViews(getPackageName(), R.layout.newsong_notification);
		
		contentView.setImageViewResource(R.id.icon, R.drawable.icon_smallest);
		contentView.setTextViewText(R.id.trackNameNotification, "RockOn is ON...");
		notification.contentView = contentView;
		
		notification.contentIntent = PendingIntent.getActivity(this, 0,
																new Intent("org.abrantes.filex").
																putExtra("org.abrantes.filex", 1)
															.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT|
																		Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		//    try {
		//            contentIntent.send();
		//    } catch (CanceledException e) {
		//            e.printStackTrace();
		//    }
		
		notificationManager.notify(SONG_NOTIFICATION_ID, notification);
		
    }

    /***************************************
     * 
     * onStart
     * 
     ***************************************/
    @Override
    public void onStart(Intent intent, int startId) {    	
    	Log.i("SVC", "Player Started");
    	String cmd  = intent.getStringExtra("command");
		try {
	    	
			if(cmd!=null){
				Log.i("SVC", cmd);

				if(CMDNEXT.equals(cmd)){
					Log.i("SVC", "Received NEXT command");
					if(!mediaPlayer.isPlaying()){
    					sendBroadcast(this.mediaButtonPlayIntent);
					}
					mBinder.playNext();
				}
				else if (CMDTOGGLEPAUSE.equals(cmd)){
					Log.i("SVC", "Received TOGGLEPAUSE command");
	    			if(mediaPlayer.isPlaying()){
	    				mBinder.pause();
	    				sendBroadcast(this.mediaButtonPauseIntent);
	    				isPaused = true;
	    			}
	    			else{
	    				try{
	    					mBinder.resume();
	    					sendBroadcast(this.mediaButtonPlayIntent);
	    					//mBinder.playNext();
	    					isPaused = false;
	    				} catch (Exception e) {
	    					e.printStackTrace();
	    					mBinder.playNext();
	    				}
	    			}
		    	}
				else if(CMDAPPWIDGETUPDATE.equals(cmd)){
					//else if (MediaAppWidgetProvider.CMDAPPWIDGETUPDATE.equals(cmd)) {
		                // Someone asked us to refresh a set of specific widgets, probably
		                // because they were just added.
						if(widgetAvailable){
							int[] appWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
			                (new RockOnAppWidgetProviderWrapper()).performUpdate(PlayerService.this, appWidgetIds);
						}
		            //}
					
				}
	    	}
			else{
				Log.i("SVCSTART", "CMD is NULL");
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
    	
    }
    
//    /*
//     * BroadcastReceiver
//     */
//    private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            String cmd = intent.getStringExtra("command");
////            if (CMDNEXT.equals(cmd) || NEXT_ACTION.equals(action)) {
////                next(true);
////            } else if (CMDPREVIOUS.equals(cmd) || PREVIOUS_ACTION.equals(action)) {
////                prev();
////            } else if (CMDTOGGLEPAUSE.equals(cmd) || TOGGLEPAUSE_ACTION.equals(action)) {
////                if (isPlaying()) {
////                    pause();
////                } else {
////                    play();
////                }
////            } else if (CMDPAUSE.equals(cmd) || PAUSE_ACTION.equals(action)) {
////                pause();
////            } else if (CMDSTOP.equals(cmd)) {
////                pause();
////                seek(0);
////            } else 
////            if (CMDAPPWIDGETUPDATE.equals(cmd)) {
////                // Someone asked us to refresh a set of specific widgets, probably
////                // because they were just added.
////                int[] appWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
////                mAppWidgetProvider.performUpdate(PlayerService.this, appWidgetIds);
////            }
//        }
//    };

    /***************************************
     * 
     * on Destroy
     * 
     ***************************************/
    @Override
	public void onDestroy() {
    	super.onDestroy();
        Log.i("SVC", "destroying");
        
		/*
		 * Save last song position
		 */
        try{
//			Editor edit = getSharedPreferences(PREFS_NAME, 0).edit();
//			edit.putLong(Constants.KEY_PREFERENCE_LAST_SONG_POSITION, mediaPlayer.getCurrentPosition()); 
//			edit.commit();
			(new RockOnPreferenceManager(FILEX_PREFERENCES_PATH))
				.putLong(new Constants().KEY_PREFERENCE_LAST_SONG_POSITION, mediaPlayer.getCurrentPosition()); 
        } catch (Exception e){
        	e.printStackTrace();
        }
        
        try{
	    	mediaPlayer.stop();
	        mediaPlayer.release();
	        notificationManager.cancel(SONG_NOTIFICATION_ID);
	        notificationManager.cancelAll();
	        
	        if(this.intentReceiver != null)
	        	unregisterReceiver(intentReceiver);
	        
	    	if(this.mediaButtonIntentReceiver != null)
	    		unregisterReceiver(mediaButtonIntentReceiver);
        } catch (Exception e) {
        	e.printStackTrace();
        }


    }
    

    
	/***************************************
	 * 
	 * Interface Binding
	 * 
	 ***************************************/
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	
    /**************************************
     * 
     * Phone State Listener
     * 
     **************************************/
	public class RockOnPhoneStateListener extends PhoneStateListener { 
		public RockOnPhoneStateListener(){
		}
		
		public void onCallStateChanged(int state, String incomingNumber){
            super.onCallStateChanged(state, incomingNumber); 
            
            Log.i("PHONESTATE", "Changed to " + state);
            
            try{
	    		if(state == TelephonyManager.CALL_STATE_IDLE){
					if(waitingOnCall){
						if(mediaPlayer != null && mediaPlayer.getCurrentPosition() != -1){
							mediaPlayer.start();
							isPaused = false;
							waitingOnCall = false;
							scrobblePlay();
						}
					}
				} else if(state == TelephonyManager.CALL_STATE_OFFHOOK){
					if(mediaPlayer != null && mediaPlayer.isPlaying()){
						waitingOnCall = true;
						mediaPlayer.pause();
						isPaused = true;
	    				scrobblePause();
					}
				} else if(state == TelephonyManager.CALL_STATE_RINGING){
					if(mediaPlayer != null && mediaPlayer.isPlaying()){
						waitingOnCall = true;
						mediaPlayer.pause();
						isPaused = true;
	    				scrobblePause();			    		  		
					}
				}
            } catch (Exception e) {
            	e.printStackTrace();
            }
    	}
    };
    
    /***************************************
     * 
     * Intent Receiver (for mediabutton)
     * 
     ***************************************/
    private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
	            String cmd = intent.getStringExtra("command");
	            if (CMDNEXT.equals(cmd)) {
	            	Log.i("SVC", "Received Broadcast for NEXT");
	 					mBinder.playNext();
	            } else if (CMDPREVIOUS.equals(cmd)) {
	                //prev();
	            } else if (CMDTOGGLEPAUSE.equals(cmd)) {
	            	Log.i("SVC", "Received Broadcast for TOGGLEPAUSE");

	                if (mediaPlayer.isPlaying()) {
	                    mBinder.pause();
	                    isPaused = true;
	                } else {
	                    mBinder.resume();
	                    isPaused = false;
	                }
	            } else if (CMDPAUSE.equals(cmd)) {
	                mBinder.pause();
	                isPaused = true;
	            } else if (CMDAPPWIDGETUPDATE.equals(cmd)) {
	                // Someone asked us to refresh a set of specific widgets, probably
	                // because they were just added.
//	            	if(Integer.decode(Build.VERSION.SDK) > 2){
	            	if(widgetAvailable){
	            		int[] appWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
		        		(new RockOnAppWidgetProviderWrapper()).performUpdate(PlayerService.this, appWidgetIds);
	            	}
//	            	}
	            }
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    };
    
	/***************************************
	 * 
	 * Service Interface Stub declaration
	 * 
	 ****************************************/
	private final PlayerServiceInterface.Stub mBinder = 
									new PlayerServiceInterface.Stub()
	{
		/*
		 * play
		 * (non-Javadoc)
		 * @see org.abrantes.filex.PlayerServiceInterface#play(int, int)
		 */
		public void	play(int albumCursorPosition, int songCursorPosition){

			try{
	//			if(albumCursor.getPosition() != albumCursorPosition){
	//				albumCursor.moveToPosition(albumCursorPosition);
	//				sendBroadcast(albumChangedIntent);
	//			} else {
					albumCursor.moveToPosition(albumCursorPosition);
	//			}
				initializeSongCursor(albumCursor.getString(
										albumCursor.getColumnIndexOrThrow(
												MediaStore.Audio.Albums.ALBUM)));
				songCursor.moveToPosition(songCursorPosition);
	//			sendBroadcast(musicChangedIntent);
				String songPath = songCursor.getString(
										songCursor.getColumnIndexOrThrow(
												MediaStore.Audio.Media.DATA));
				playSong(songPath);
				
			} catch (CursorIndexOutOfBoundsException e){
				e.printStackTrace();
				try {
					reloadCursors();
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
				return;
			}
			
			/*
			 * Save last song (and album) key
			 */
//			Editor edit = getSharedPreferences(PREFS_NAME, 0).edit();
//			edit.putString(Constants.KEY_PREFERENCE_LAST_ALBUM, albumCursor.getString(
//					albumCursor.getColumnIndexOrThrow(
//							MediaStore.Audio.Albums.ALBUM_KEY)));
//			edit.commit();
//			edit.putString(Constants.KEY_PREFERENCE_LAST_SONG, songCursor.getString(
//					songCursor.getColumnIndexOrThrow(
//							MediaStore.Audio.Media.TITLE_KEY)));
//			edit.commit();
			RockOnPreferenceManager prefs = new RockOnPreferenceManager(FILEX_PREFERENCES_PATH); 
			prefs.putString(new Constants().KEY_PREFERENCE_LAST_ALBUM, albumCursor.getString(
					albumCursor.getColumnIndexOrThrow(
							MediaStore.Audio.Albums.ALBUM_KEY)));
			prefs.putString(new Constants().KEY_PREFERENCE_LAST_SONG, songCursor.getString(
					songCursor.getColumnIndexOrThrow(
							MediaStore.Audio.Media.TITLE_KEY)));
			
			Log.i("SVC","playing "+albumCursorPosition+" "+songCursorPosition);
			
			/*
			 * show notification
			 */
			if(mediaPlayer.isPlaying()){
	    		/*
	    		 * Show notification
	    		 */
				if(notificationManager != null){
					//TODO: takes this initialization into a sharedpreferences listener
//					notificationManager.cancelAll();
//					notification = new Notification(R.drawable.stat_notify_musicplayer,
//							null,
//							System.currentTimeMillis());
//					notification.contentIntent = PendingIntent.getActivity(PlayerService.this, 0,
//									new Intent("org.abrantes.filex").
//										putExtra("org.abrantes.filex", 1)
//											.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT|
//													Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
					//
//					notification.tickerText = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
//		    									+ " - "
//		    									+ songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
		    		
		    		contentView.setTextViewText(R.id.trackNameNotification, songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
		    		contentView.setTextViewText(R.id.albumNameNotification, songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
		    		contentView.setTextViewText(R.id.artistNameNotification, songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
		    		notification.contentView = contentView;
		    		Log.i("SVC", "Notification persistence is "+ prefs.getBoolean(PREFS_SHOW_ICON, false));
//		    		if(getSharedPreferences(PREFS_NAME, 0).getBoolean(PREFS_SHOW_ICON, false) == true){
		    		if(prefs.getBoolean(PREFS_SHOW_ICON, false) == true){
		    			notification.flags |= Notification.FLAG_ONGOING_EVENT;
		    		} else {
		    			notification.flags = Notification.FLAG_AUTO_CANCEL;
		    		}
		    		notificationManager.notify(SONG_NOTIFICATION_ID, notification);
		    		Log.i("NEWSONG", "Notification presented");
				}
			}
		}
		
		/*
		 * playNext
		 * (non-Javadoc)
		 * @see org.abrantes.filex.PlayerServiceInterface#playNext()
		 */
		public void playNext(){
			playNextSong();
		}
		
		/*
		 * stop player
		 * (non-Javadoc)
		 * @see org.abrantes.filex.PlayerServiceInterface#stop()
		 */
		@Override
		public void stop() throws RemoteException {
			if(mediaPlayer.isPlaying())
				mediaPlayer.stop();
			mediaPlayer.reset();
		}
		
		/*
		 * setShuffle
		 * (non-Javadoc)
		 * @see org.abrantes.filex.PlayerServiceInterface#setShuffle(boolean)
		 */
		public void	setShuffle(boolean shuffle){
			SHUFFLE = shuffle;
		}

		/*
		 * getAlbumCursorPosition
		 * (non-Javadoc)
		 * @see org.abrantes.filex.PlayerServiceInterface#getAlbumCursorPosition()
		 */
		@Override
		public int getAlbumCursorPosition() throws RemoteException {
			if(albumCursor != null)
				return albumCursor.getPosition();
			else
				return -1;
		}

		/*
		 * getSongCursorPosition
		 * (non-Javadoc)
		 * @see org.abrantes.filex.PlayerServiceInterface#getSongCursorPosition()
		 */
		@Override
		public int getSongCursorPosition() throws RemoteException {
			if(songCursor != null)
				return songCursor.getPosition();
			else
				return -1;
		}

		/*
		 * isPlaying
		 * (non-Javadoc)
		 * @see org.abrantes.filex.PlayerServiceInterface#isPlaying()
		 */
		@Override
		public boolean isPlaying() throws RemoteException {
			return mediaPlayer.isPlaying();
		}

		/*
		 * resume()
		 * (non-Javadoc)
		 * @see org.abrantes.filex.PlayerServiceInterface#resume()
		 */
		@Override
		public void resume() throws RemoteException {
			try{
				
				if(mediaPlayer != null && mediaPlayer.getCurrentPosition() != -1){
					mediaPlayer.start();
					isPaused = false;
					scrobblePlay();
				}
				
				Log.i("SVC", "Done resuming");
			} catch (Exception e) {
				Log.i("SVC", "Exception on media player");
				this.play(albumCursor.getPosition(), songCursor.getPosition());
			}
		}

		/*
		 * Pause
		 * (non-Javadoc)
		 * @see org.abrantes.filex.PlayerServiceInterface#pause()
		 */
		@Override
		public void pause() throws RemoteException {
			if(mediaPlayer != null && mediaPlayer.isPlaying()){
				mediaPlayer.pause();
				isPaused = true;
				scrobblePause();
				
			}
		}

		/*
		 * getPlayingPosition
		 * (non-Javadoc)
		 * @see org.abrantes.filex.PlayerServiceInterface#getPlayingPosition()
		 */
		@Override
		public int getPlayingPosition() throws RemoteException {
			try{
				if(mediaPlayer != null){
					return mediaPlayer.getCurrentPosition();
				} else {
					return 0;
				}
			} catch (Exception e) {
				return 0;
			}
		}

		/*
		 * getDuration
		 * (non-Javadoc)
		 * @see org.abrantes.filex.PlayerServiceInterface#getPlayingPosition()
		 */
		@Override
		public int getDuration() throws RemoteException {
			if(mediaPlayer != null){
				return mediaPlayer.getDuration();
			} else {
				return 0;
			}
		}

		int DEFAULT_FWD_INTERVAL = 30000; // 30 secs forward
		/*
		 * forward
		 * (non-Javadoc)
		 * @see org.abrantes.filex.PlayerServiceInterface#forward()
		 */
		@Override
		public void forward() throws RemoteException {
			if(mediaPlayer != null)
				mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() +
									DEFAULT_FWD_INTERVAL);
		}

		/*
		 * reverse
		 * (non-Javadoc)
		 * @see org.abrantes.filex.PlayerServiceInterface#reverse()
		 */
		@Override
		public void reverse() throws RemoteException {
			if(mediaPlayer != null)
				mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() -
									DEFAULT_FWD_INTERVAL);			
		}

		/*
		 * seekTo
		 * (non-Javadoc)
		 * @see org.abrantes.filex.PlayerServiceInterface#seekTo()
		 */
		@Override
		public void seekTo(int msec) throws RemoteException {
			if(mediaPlayer != null)
				mediaPlayer.seekTo(msec);
		}

		/*
		 * resetAlbumCursor
		 * (non-Javadoc)
		 * @see org.abrantes.filex.PlayerServiceInterface#resetAlbumCursor()
		 */
		@Override
		public void resetAlbumCursor() throws RemoteException {
			try{
				if(albumCursor.getPosition() != -1){
					Log.i("SVC", "resetAlbumCursor - remembering playing position - "+albumCursor.getPosition());
					String albumKey = albumCursor.getString(albumCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_KEY));
					initializeAlbumCursor();
					while(albumCursor.moveToNext()){
//						Log.i("RESETCURSOR", albumCursor.getString(albumCursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM)));
//						Log.i("RESETCURSOR", albumCursor.getString(albumCursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_KEY)));
//						Log.i("RESETCURSOR", ""+albumKey);
						if(albumKey.compareTo(albumCursor.getString(albumCursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_KEY))) 
								== 0)
							break;
					}
//					String sortClause = MediaStore.Audio.Albums.ARTIST+" ASC";
//			    	Cursor albumCursorNew = contentResolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
//							ALBUM_COLS, // we should minimize the number of columns
//							null,	// all albums 
//							null,   // parameters to the previous parameter - which is null also 
//							sortClause	// sort order, SQLite-like
//							);
//					while(albumCursorNew.moveToNext()){
//						Log.i("RESETCURSOR", albumCursorNew.getString(albumCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM)) + "==" + albumName);
//						if(albumCursorNew.getString(albumCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM)).compareTo(albumName) == 0)
//							break;
//					}
//					albumCursor = albumCursorNew;
				}
			} catch (Exception e){
				e.printStackTrace();
			}
		}

		@Override
		public void reloadCursors() throws RemoteException {
		   	//playlist = getSharedPreferences(PREFS_NAME, 0).getLong(constants.PREF_KEY_PLAYLIST, constants.PLAYLIST_ALL);
		   	try{
				initializeAlbumCursor();
				albumCursor.moveToFirst();
				initializeSongCursor(
						albumCursor.getString(
								albumCursor.getColumnIndexOrThrow(
										MediaStore.Audio.Albums.ALBUM)));
				songCursor.moveToFirst();
		   	}catch(Exception e){
		   		e.printStackTrace();
		   	}
		}
		
		/*
		 * Kill the service
		 * (non-Javadoc)
		 * @see org.abrantes.filex.PlayerServiceInterface#destroy()
		 */
		@Override
		public void destroy() throws RemoteException {
			stopSelf();
		}

		@Override
		public boolean setAlbumCursorPosition(int position)
				throws RemoteException {
			try{
				if(position >= albumCursor.getCount())
					return false;
				albumCursor.moveToPosition(position);
				initializeSongCursor(albumCursor.getString(albumCursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM)));
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

		@Override
		public boolean setSongCursorPosition(int position)
				throws RemoteException {
			songCursor.moveToPosition(position);
			return false;
		}

		@Override
		public void setPlaylist(long playlistId) throws RemoteException {
			boolean diff = false;
			if(playlistId != playlist){
				diff = true;
				Log.i("SVC", "setPlaylist *NEW* playlist "+playlistId);
			}
			playlist = playlistId;
			if(diff){
				Log.i("SVC", "Reloading Cursors "+playlistId);
				reloadCursors();
			}
				
		}

		@Override
		public void setRecentPeriod(int period) throws RemoteException {
			if(recentPeriod != period){
				Log.i("SVC", "New period - "+recentPeriod+" "+period);
				recentPeriod = period;
				if(playlist == new Constants().PLAYLIST_RECENT)
					reloadCursors();
			}
			
		}

		@Override
		public boolean isPaused() throws RemoteException {
			return isPaused;
		}

		@Override
		public void setScrobbleDroid(boolean val) throws RemoteException {
			scrobbleDroid = val;
		}

	};
	
	/**************************************
	 * 
	 * Initialize Album and Song Cursors
	 * 
	 **************************************/
    public void	initializeAlbumCursor(){
		Log.i("DBG", "Initializing Album Cursor");
    	if(playlist == constants.PLAYLIST_ALL){
	    	String sortClause = MediaStore.Audio.Albums.ARTIST+" ASC";
	    	albumCursor = contentResolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
					ALBUM_COLS, // we should minimize the number of columns
					null,	// all albums 
					null,   // parameters to the previous parameter - which is null also 
					sortClause	// sort order, SQLite-like
					);
    	} else if(playlist == constants.PLAYLIST_RECENT){
    		double period = recentPeriod * 24 * 60 * 60;
			String whereClause = MediaStore.Audio.Media.DATE_ADDED +">" + (System.currentTimeMillis()/1000 - period);
			String sortOrder = MediaStore.Audio.Albums.ALBUM+" ASC";
			Cursor songCursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
					SONG_COLS, 
					whereClause,
					null,
					sortOrder);
			whereClause = "";
			String lastAlbumKey = "";
			while(songCursor.moveToNext()){
				if(songCursor.getString(songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_KEY)).equals(lastAlbumKey)){
					lastAlbumKey = songCursor.getString(songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_KEY));
					//Log.i("DBG", "Album Repeated - "+songCursor.getString(songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_KEY)));
					//Log.i("DBG", "Album Repeated - "+songCursor.getString(songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)));
					continue;
				}
				if(whereClause != "")
					whereClause += " OR ";
				whereClause += MediaStore.Audio.Albums.ALBUM_KEY+"=\""+
							songCursor.getString(
									songCursor.getColumnIndexOrThrow(
											MediaStore.Audio.Media.ALBUM_KEY))
									.replaceAll("\"", "\"\"")+
							"\"";
				
				lastAlbumKey = songCursor.getString(songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_KEY));
				//Log.i("PLAYLIST_RECENT", songCursor.getDouble(songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED))+" > "+ (System.currentTimeMillis()/1000 - period));
			}
			sortOrder = MediaStore.Audio.Albums.ARTIST+" ASC";
			albumCursor = contentResolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
					ALBUM_COLS, // we should minimize the number of columns
					whereClause,	// all albums 
					null,   // parameters to the previous parameter - which is null also 
					sortOrder	// sort order, SQLite-like
					);
			Log.i("DBG", "query length = "+albumCursor.getCount());
			// more pre defined playlists
    	} else {
    		/*
    		 * A previously chosen playlist
    		 */
	    	String sortClause = MediaStore.Audio.Playlists.Members.ARTIST+" ASC";
	    	Cursor playlistCursor = contentResolver.query(MediaStore.Audio.Playlists.Members.getContentUri("external", playlist),
					PLAYLIST_SONG_COLS, // we should minimize the number of columns
					null,	// all albums 
					null,   // parameters to the previous parameter - which is null also 
					sortClause	// sort order, SQLite-like
					);
	    	String whereClause = "";
	    	String lastAlbumKey = "";
	    	while(playlistCursor.moveToNext()){
	    		if(playlistCursor.getString(playlistCursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.Members.ALBUM_KEY)).equals(lastAlbumKey)){
	    			lastAlbumKey = playlistCursor.getString(playlistCursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.Members.ALBUM_KEY));
//	    			Log.i("DBG", "Album Repeated - "+playlistCursor.getString(playlistCursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.Members.ALBUM_KEY)));
//	    			Log.i("DBG", "Album Repeated - "+playlistCursor.getString(playlistCursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.Members.ALBUM)));
	    			continue;
	    		}
	    		if(whereClause != "")
	    			whereClause += " OR ";
	    		whereClause += MediaStore.Audio.Albums.ALBUM_KEY+"=\""+
								playlistCursor.getString(
										playlistCursor.getColumnIndexOrThrow(
												MediaStore.Audio.Playlists.Members.ALBUM_KEY))
										.replaceAll("\"", "\"\"")+
								"\"";

	    		lastAlbumKey = playlistCursor.getString(playlistCursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.Members.ALBUM_KEY));

//	    		Log.i("DBG", "Album - "+playlistCursor.getString(playlistCursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.Members.ALBUM_KEY)));
	   
	    	}
	    	
	    	Log.i("DBG", whereClause);
	    	sortClause = MediaStore.Audio.Albums.ARTIST+" ASC";
	    	albumCursor = contentResolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
					ALBUM_COLS, // we should minimize the number of columns
					whereClause,	// all albums 
					null,   // parameters to the previous parameter - which is null also 
					sortClause	// sort order, SQLite-like
					);
	    	Log.i("DBG", "query length = "+albumCursor.getCount());
    	}
//    	/*************DBG********************************/
//    	initializeAlbumCursorFromPlaylist();

//    	if(playlist == constants.PLAYLIST_ALL){
//	    	String sortClause = MediaStore.Audio.Albums.ARTIST+" ASC";
//	    	albumCursor = contentResolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
//					ALBUM_COLS, // we should minimize the number of columns
//					null,	// all albums 
//					null,   // parameters to the previous parameter - which is null also 
//					sortClause	// sort order, SQLite-like
//					);
//    	}
    }
    public void	initializeSongCursor(String albumName){
//    	
//    	if(albumName == null){
//	    	songCursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
//					SONG_COLS, // we should minimize the number of columns
//					null,	// all songs
//					null,   // parameters to the previous parameter - which is null also 
//					null	// sort order, SQLite-like
//					);
//    	} else {
//    		String whereClause = MediaStore.Audio.Media.ALBUM+"=\""+
//    								albumName+"\"";
//    		Log.i("SONGSEARCH", whereClause);
//    		songCursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
//					SONG_COLS, 		// we should minimize the number of columns
//					whereClause,	// all songs from a certain album
//					null,   		// parameters to the previous parameter - which is null also 
//					null			// sort order, SQLite-like
//					);
//    	}
    	
		String whereClause = null;
		if(playlist == constants.PLAYLIST_ALL){
    		if(albumName != null)
    			whereClause = MediaStore.Audio.Media.ALBUM+"=\""+
    								albumName.replaceAll("\"", "\"\"")+
    								"\"";
	    	String sortClause = MediaStore.Audio.Media.TRACK+" ASC";
    		Log.i("SONGSEARCH", ""+whereClause);
    		songCursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
					SONG_COLS, 		// we should minimize the number of columns
					whereClause,	// all songs from a certain album
					null,   		// parameters to the previous parameter - which is null also 
					sortClause		// sort order, SQLite-like
					);
		} else if (playlist == constants.PLAYLIST_RECENT){
			if(albumName != null)
    			whereClause = MediaStore.Audio.Media.ALBUM+
    								"=\""+
    								albumName.replaceAll("\"", "\"\"")+
    								"\"";
	    	String sortClause = MediaStore.Audio.Media.TRACK+" ASC";
    		Log.i("SONGSEARCH", ""+whereClause);
    		songCursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
					SONG_COLS, 		// we should minimize the number of columns
					whereClause,	// all songs from a certain album
					null,   		// parameters to the previous parameter - which is null also 
					sortClause		// sort order, SQLite-like
					);
			//another dynamic playlist
		} else {
			/*
    		 * A previously chosen playlist
    		 */
	    	if(albumName != null)
	    		whereClause = MediaStore.Audio.Playlists.Members.ALBUM + 
	    							"=\""+
	    							albumName.replaceAll("\"", "\"\"") +
	    							"\"";
			Cursor playlistCursor = contentResolver.query(MediaStore.Audio.Playlists.Members.getContentUri("external", playlist),
					PLAYLIST_SONG_COLS, // we should minimize the number of columns
					whereClause,	// all albums 
					null,   // parameters to the previous parameter - which is null also 
					null	// sort order, SQLite-like
					);
	    	whereClause = MediaStore.Audio.Media.ALBUM + 
	    					"=\""+
							albumName.replaceAll("\"", "\"\"") + 
							"\"";
//    	    	String lastAlbumKey = "";
	    	while(playlistCursor.moveToNext()){
//    	    		if(playlistCursor.getString(playlistCursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.Members.ALBUM_KEY)).equals(lastAlbumKey)){
//    	    			lastAlbumKey = playlistCursor.getString(playlistCursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.Members.ALBUM_KEY));
//    	    			Log.i("DBG", "Album Repeated - "+playlistCursor.getString(playlistCursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.Members.ALBUM_KEY)));
//    	    			Log.i("DBG", "Album Repeated - "+playlistCursor.getString(playlistCursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.Members.ALBUM)));
//    	    			continue;
//    	    		}
	    		if(whereClause != "")
	    			whereClause += " OR ";
	    		whereClause += MediaStore.Audio.Media.TITLE_KEY+"=\""+
								playlistCursor.getString(
										playlistCursor.getColumnIndexOrThrow(
												MediaStore.Audio.Playlists.Members.TITLE_KEY))
										.replaceAll("\"", "\"\"")+
								"\"";

//    	    		lastAlbumKey = playlistCursor.getString(playlistCursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.Members.ALBUM_KEY));

//	    		Log.i("DBG", "Album - "+playlistCursor.getString(playlistCursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.Members.TITLE_KEY)));
	    	}
	    	
	    	String sortClause = MediaStore.Audio.Media.TRACK+" ASC";
	    	//String sortClause = null;
	    	Log.i("SONGSEARCH", whereClause);
	    	songCursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
	    						SONG_COLS, 		// we should minimize the number of columns
	    						whereClause,	// all songs from a certain album
	    						null,   		// parameters to the previous parameter - which is null also 
	    						sortClause		// sort order, SQLite-like
	    						);
	    		
		}
//    	}    	
    	
    }
    
    /***************************************
     * 
     * moveAlbumCursorToLastPlayingPosition
     * 
     ***************************************/
    public void moveAlbumCursorToLastPlayingPosition(){
    	try{
//    		String albumKey = getSharedPreferences(PREFS_NAME, 0).getString(Constants.KEY_PREFERENCE_LAST_ALBUM, "");
    		String albumKey = (new RockOnPreferenceManager(FILEX_PREFERENCES_PATH))
    			.getString(new Constants().KEY_PREFERENCE_LAST_ALBUM, "");
	    	albumCursor.moveToFirst();
	    	while(!albumCursor.isAfterLast()){
	    		if(albumCursor.getString(
	    						albumCursor.getColumnIndexOrThrow(
	    								MediaStore.Audio.Albums.ALBUM_KEY)).compareTo(albumKey)
	    			== 0){
	    			break;
	    		}
	    		albumCursor.moveToNext();
	    	}
	    	if(albumCursor.isAfterLast())
	    		albumCursor.moveToFirst();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    /***************************************
     * 
     * moveSongCursorToLastPlayingPosition
     * 
     ***************************************/
    public void moveSongCursorToLastPlayingPosition(){
    	try{
//    		String songKey = getSharedPreferences(PREFS_NAME, 0).getString(Constants.KEY_PREFERENCE_LAST_SONG, "");
    		String songKey = (new RockOnPreferenceManager(FILEX_PREFERENCES_PATH))
    			.getString(new Constants().KEY_PREFERENCE_LAST_SONG, "");
	    	songCursor.moveToFirst();
	    	while(!songCursor.isAfterLast()){
	    		if(songCursor.getString(
	    						songCursor.getColumnIndexOrThrow(
	    								MediaStore.Audio.Media.TITLE_KEY)).compareTo(songKey)
	    			== 0){
	    			break;
	    		}
	    		songCursor.moveToNext();
	    	}
	    	if(songCursor.isAfterLast())
	    		songCursor.moveToFirst();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    /***************************************
     * 
     * getSongName
     * 
     ***************************************/
    String getSongName(){
    	if(songCursor != null)
    		return songCursor.getString(songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
    	else
    		return null;
    }
    
    /***************************************
     * 
     * getArtistName
     * 
     ***************************************/
    String getArtistName(){
    	if(songCursor != null)
    		return songCursor.getString(songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
    	else
    		return null;
    }
    
    /***************************************
     * 
     * getAlbumName
     * 
     ***************************************/
    String getAlbumName(){
    	if(songCursor != null)
    		return songCursor.getString(songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
    	else
    		return null;
    }
   
    /***************************************
     * 
     * isPlaying
     * 
     ***************************************/
    boolean isPlaying(){
    	try{
    		if(mediaPlayer.isPlaying())
    			return true;
    		else
    			return false;
    	}catch (Exception e){
    		e.printStackTrace();
    		return false;
    	}
    }
    
    /***************************************
     * 
     * PlaySong
     * 
     ***************************************/
	public void playSong(String songPath){
		Message msg = new Message();
		msg.obj = songPath;
//		playSongHandler.sendMessageDelayed(msg, 1000);
		
		try {
			if(mediaPlayer.isPlaying()){
				mediaPlayer.stop();
			}
			mediaPlayer.reset();
			mediaPlayer.setDataSource(songPath);
    		mediaPlayer.prepare();
    		mediaPlayer.start();
    		scrobblePlay();
    		updateWidget();
    		isPaused = false;
    		
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	
	/*************************************
	 * 
	 * playNextSong
	 *
	 *************************************/
	private void playNextSong(){
		String songPath = null;		
		try{
			if (this.SHUFFLE){
				/* 
				 * Get another album 
				 */
				Random rand = new Random();
				albumCursor.moveToPosition(rand.nextInt(albumCursor.getCount())); // cursor starts at 0?
				initializeSongCursor(albumCursor.getString(
										albumCursor.getColumnIndexOrThrow(
												MediaStore.Audio.Albums.ALBUM)));
				
				/*
				 * Get a song of the album
				 */
				songCursor.moveToPosition(rand.nextInt(songCursor.getCount()));
				songPath = songCursor.getString(
								songCursor.getColumnIndexOrThrow(
										MediaStore.Audio.Media.DATA));
				
				sendBroadcast(albumChangedIntent);
			} else if (songCursor.isLast()){
				/*
				 * Get next Album
				 */
				if(albumCursor.isLast())
					albumCursor.moveToFirst();
				else
					albumCursor.moveToNext();
				initializeSongCursor(albumCursor.getString(
										albumCursor.getColumnIndexOrThrow(
												MediaStore.Audio.Albums.ALBUM)));
				/*
				 * Get first song of this album
				 */
				songCursor.moveToFirst();
				songPath = songCursor.getString(
								songCursor.getColumnIndexOrThrow(
										MediaStore.Audio.Media.DATA));
				sendBroadcast(albumChangedIntent);
			} else {
				/*
				 * Get next song of the current album to play
				 */
				songCursor.moveToNext();
				songPath = songCursor.getString(
										songCursor.getColumnIndexOrThrow(
											MediaStore.Audio.Media.DATA));
				
				sendBroadcast(musicChangedIntent);
				/*
				 * Update UI components with song name and duration
				 */
				//updateSongTextUI();		
			}
			//TODO: start tracking song duration
			/*
			 * Play song 
			 */
			playSong(songPath);
			
			/*
			 * Save last song (and album) key
			 */
//			Editor edit = getSharedPreferences(PREFS_NAME, 0).edit();
//			edit.putString(Constants.KEY_PREFERENCE_LAST_ALBUM, albumCursor.getString(
//					albumCursor.getColumnIndexOrThrow(
//							MediaStore.Audio.Albums.ALBUM_KEY)));
//			edit.commit();
//			edit.putString(Constants.KEY_PREFERENCE_LAST_SONG, songCursor.getString(
//					songCursor.getColumnIndexOrThrow(
//							MediaStore.Audio.Media.TITLE_KEY)));
//			edit.commit();
			RockOnPreferenceManager prefs = new RockOnPreferenceManager(FILEX_PREFERENCES_PATH);
			prefs.putString(new Constants().KEY_PREFERENCE_LAST_ALBUM, albumCursor.getString(
					albumCursor.getColumnIndexOrThrow(
							MediaStore.Audio.Albums.ALBUM_KEY)));
			prefs.putString(new Constants().KEY_PREFERENCE_LAST_SONG, songCursor.getString(
					songCursor.getColumnIndexOrThrow(
							MediaStore.Audio.Media.TITLE_KEY)));
			
			/*
			 * show notification
			 */
			if(mediaPlayer.isPlaying()){
	    		/*
	    		 * Show notification
	    		 */
				// TODO: take this out to a sharedpref listener
//				notificationManager.cancelAll();
//				notification = new Notification(R.drawable.stat_notify_musicplayer,
//						null,
//						System.currentTimeMillis());
//				notification.contentIntent = PendingIntent.getActivity(PlayerService.this, 0,
//						new Intent("org.abrantes.filex").
//							putExtra("org.abrantes.filex", 1)
//								.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT|
//										Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
				//
//				notification.tickerText = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
//	    									+ " - "
//	    									+ songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
//				if(getSharedPreferences(PREFS_NAME, 0).getBoolean(PREFS_SHOW_ICON, false) == true){
				if(prefs.getBoolean(PREFS_SHOW_ICON, false) == true){
			    			notification.flags |= Notification.FLAG_ONGOING_EVENT;
	    		} else {
	    			notification.flags = Notification.FLAG_AUTO_CANCEL;
	    		}
	    		contentView.setTextViewText(R.id.trackNameNotification, songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
	    		contentView.setTextViewText(R.id.albumNameNotification, songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
	    		contentView.setTextViewText(R.id.artistNameNotification, songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
	    		
	    		notification.contentView = contentView;
	    		
	            notificationManager.notify(SONG_NOTIFICATION_ID, notification);
	    		Log.i("NEWSONG", "Notification presented");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	Handler playSongHandler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			String songPath = (String) msg.obj;
			
			try {
				if(mediaPlayer.isPlaying()){
					mediaPlayer.stop();
				}
				mediaPlayer.reset();
				mediaPlayer.setDataSource(songPath);
	    		mediaPlayer.prepare();
				mediaPlayer.start();
	    		scrobblePlay();
	    		isPaused = false;
	    		
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	};
	
	/*****************************************
	 * 
	 * songCompletedListener 
	 * 
	 *****************************************/
	OnCompletionListener songCompletedListener = new OnCompletionListener(){

		@Override
		public void onCompletion(MediaPlayer mp) {
			playNextSong();
		}
	};
	
	/************************************
	 * 
	 * updateWidget
	 * 
	 ************************************/
	static String PAUSE = "pause";
	static String PLAY = "play";
	public void updateWidget(){
		try{
			Log.i("SVC", "check if we have widgets");
//			if(Integer.decode(Build.VERSION.SDK) > 2){
	    	if(widgetAvailable){
				Log.i("SVC", "notifying widget");
//				RockOnAppWidgetProviderWrapper mAppWidgetProvider = new RockOnAppWidgetProviderWrapper();
				if(mediaPlayer.isPlaying())
					(new RockOnAppWidgetProviderWrapper()).notifyChange(this, PLAY);
				else
					(new RockOnAppWidgetProviderWrapper()).notifyChange(this, PAUSE);
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	/****************************************
	 * 
	 * SrobbleSong
	 * 
	 ****************************************/
	public void scrobblePlay(){
		try{
			if(scrobbleDroid){
    			Intent i = new Intent("net.jjc1138.android.scrobbler.action.MUSIC_STATUS");

    			i.putExtra("playing", true);
    			i.putExtra("id", songCursor.getInt(
    					songCursor.getColumnIndexOrThrow(
    							MediaStore.Audio.Media._ID)));
//    			i.putExtra("track", "Does Anybody Know");
//    			i.putExtra("secs", 120);

    			sendBroadcast(i);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/***************************************
	 * 
	 * ScrobblePause
	 *
	 ***************************************/
	public void scrobblePause(){
		try{
			if(scrobbleDroid){
				Intent i = new Intent("net.jjc1138.android.scrobbler.action.MUSIC_STATUS");

				i.putExtra("playing", false);
				i.putExtra("id", songCursor.getInt(
						songCursor.getColumnIndexOrThrow(
								MediaStore.Audio.Media._ID)));
//				i.putExtra("track", "Does Anybody Know");
//				i.putExtra("secs", 120);

				sendBroadcast(i);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}	
}

