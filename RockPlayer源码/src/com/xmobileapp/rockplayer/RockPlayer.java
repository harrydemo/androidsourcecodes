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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

import com.xmobileapp.rockplayer.PlayerServiceInterface;
import com.xmobileapp.rockplayer.R;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.xml.sax.SAXException;

import com.xmobileapp.rockplayer.utils.Constants;
import com.xmobileapp.rockplayer.utils.RockOnPreferenceManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.preference.PreferenceScreen;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class RockPlayer extends Activity {

	/**********************************************
	 * 
	 * Some Definitions
	 * 
	 **********************************************/
    String[] ALBUM_COLS = new String[] {
    	MediaStore.Audio.Albums._ID,
        MediaStore.Audio.Albums.ALBUM,
        MediaStore.Audio.Albums.ALBUM_ART,
        MediaStore.Audio.Albums.ALBUM_KEY,
        MediaStore.Audio.Albums.ARTIST,
        MediaStore.Audio.Albums.FIRST_YEAR,
        MediaStore.Audio.Albums.LAST_YEAR,
        MediaStore.Audio.Albums.NUMBER_OF_SONGS
    };
    String[] SONG_COLS = new String[] {
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
    String[] ARTIST_COLS = new String[] {
        	MediaStore.Audio.Artists._ID,
            MediaStore.Audio.Artists.ARTIST
    };
    String[] PLAYLIST_COLS = new String[] {
    		MediaStore.Audio.Playlists._ID,
        	MediaStore.Audio.Playlists.DATA,
            MediaStore.Audio.Playlists.DATE_ADDED,
            MediaStore.Audio.Playlists.DATE_MODIFIED,
            MediaStore.Audio.Playlists.NAME
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
    
    double 				CURRENT_PLAY_SCREEN_FRACTION = 0.66;
    double 				CURRENT_PLAY_SCREEN_FRACTION_LANDSCAPE = 0.75;
    double 				NAVIGATOR_SCREEN_FRACTION = 1 - CURRENT_PLAY_SCREEN_FRACTION;
    double 				NAVIGATOR_SCREEN_FRACTION_LANDSCAPE = 1 - CURRENT_PLAY_SCREEN_FRACTION_LANDSCAPE;
    String				FILEX_PATH = "/sdcard/RockOn/";
    String				FILEX_ALBUM_ART_PATH = "/sdcard/albumthumbs/RockOn/";
    String				FILEX_SMALL_ALBUM_ART_PATH = "/sdcard/albumthumbs/RockOn/small/";
    static String		FILEX_BACKGROUND_PATH = "/sdcard/albumthumbs/RockOn/backgrounds/";
    String				FILEX_CONCERT_PATH = "/sdcard/RockOn/concert/";
    String				FILEX_PREFERENCES_PATH = "/sdcard/RockOn/preferences/";
    String				FILEX_FILENAME_EXTENSION = "";
    long				VERSION = (long) 3;
    long				ART_IMPORT_INTVL = 2*24*60*60*1000;
    double				CONCERT_RADIUS_DEFAULT = 750000;
    double				CONCERT_RADIUS_INCREMENT = 50000;
    Constants			constants = new Constants();
    static String		PREFS_NAME = "RockOnPreferences";
    static String		PREFS_SHOW_ICON = "show_icon";
    static String		PREFS_SCROBBLE_DROID = "scrobble_droid";
    static String		PREFS_SHOW_ART_WHILE_SCROLLING = "show_art_while_scrolling";
    static String		PREFS_SHOW_FRAME = "show_frame";
    static String		PREFS_ALWAYS_LANDSCAPE = "always_landscape";
    static String		PREFS_AUTO_ROTATE = "auto_rotate";
    static String		PREFS_BACKGROUND_BLUR = "background_blur";
    static String		PREFS_CUSTOM_BACKGROUND = "custom_background";
    static String		PREFS_CUSTOM_BACKGROUND_PORTRAIT = "custom_background_portrait";
    static String		PREFS_CUSTOM_BACKGROUND_LANDSCAPE = "custom_background_landscape";
    
    static int			BITMAP_SIZE_SMALL = 0;
    static int			BITMAP_SIZE_NORMAL = 1;
    static int			BITMAP_SIZE_FULLSCREEN = 2;
    
    
    static final int	NORMAL_VIEW = 0;
    static final int	LIST_EXPANDED_VIEW = 1;
    static final int	FULLSCREEN_VIEW = 2;
    
    static int			VIEW_STATE = FULLSCREEN_VIEW;//NORMAL_VIEW;
    
    static boolean		GRATIS = false;
    
    
    /*********************************************
     * 
     * Service intent stuff
     * 
     *********************************************/
    protected IntentFilter 					musicChangedIntentFilter = new IntentFilter(new Constants().MUSIC_CHANGED);  
    protected IntentFilter 					albumChangedIntentFilter = new IntentFilter(new Constants().ALBUM_CHANGED); 
    protected IntentFilter 					mediaButtonPauseIntentFilter = new IntentFilter(new Constants().MEDIA_BUTTON_PAUSE);  
    protected IntentFilter 					mediaButtonPlayIntentFilter = new IntentFilter(new Constants().MEDIA_BUTTON_PLAY); 
    private MusicChangedIntentReceiver		musicChangedIntentReceiver= null;  
    private AlbumChangedIntentReceiver		albumChangedIntentReceiver= null;
    private MediaButtonPauseIntentReceiver	mediaButtonPauseIntentReceiver= null;  
    private MediaButtonPlayIntentReceiver	mediaButtonPlayIntentReceiver= null;
    
    /*********************************************
     * 
     * Application global variables
     * 
     *********************************************/
    private Context						context = null;
    public	ContentResolver				contentResolver;
    public	Cursor						albumCursor = null;
    public	Cursor						songCursor = null;
    public	Cursor						artistCursor = null;
    public	int							albumCursorPositionPlaying = 0;
    public 	boolean						SHUFFLE = false;
    public	PlayerServiceConnection		playerServiceConn = null;
    public	PlayerServiceInterface		playerServiceIface = null;
    public	boolean						calledByService = false;
    private double						songDuration = 0;
    private double						songCurrentPosition = 0;
    public	boolean						albumListIsScrolling = false;
    private int							accumulatedProgress = 0;
    private LastFmEventImporter 		lastFmEventImporter = null;
    public	double						concertRadius;
    private AlbumCursorAdapter			albumAdapter = null;
    public	long					playlist = new Constants().PLAYLIST_NONE;
    public	static int					recentPlaylistPeriod;
    private FilexDefaultExceptionHandler fdHandler;
    public	static	boolean				scrobbleDroid;
    public	static	boolean				showArtWhileScrolling;
    public	static	boolean				showFrame;
    public	static	boolean				alwaysLandscape;
    public	static	boolean				autoRotate;
    int albumNavigatorItemLongClickIndex;

    
    /*********************************************
     * 
     * UI animations
     * 
     *********************************************/
    private	Rotate3dAnimation		perspectiveLeft = null;
    private	Rotate3dAnimation		perspectiveRight = null;
    private	Rotate3dAnimationY		hideLeft = null;
    private Rotate3dAnimationY		showLeft = null;
    private Rotate3dAnimationY		hideRight = null;
    private Rotate3dAnimationY		showRight = null;
    public	AlphaAnimation			fadeAlbumOut = null;
    public	AlphaAnimation			fadeAlbumIn = null;
    
    /*********************************************
     * 
     * UI components
     * 
     *********************************************/
    public	Display					display = null;
    public	MediaPlayer				mediaPlayer = null;
    private	ViewGroup				mainUIContainer = null;
    private ImageView				currentAlbumPlayingImageView = null;
    private ImageView				currentAlbumPlayingOverlayImageView = null;
    private ViewGroup				containerLayout = null;
    public	ViewGroup				currentPlayingLayout = null;
    private	ViewGroup				currentAlbumPlayingLayout = null;
    public	ViewGroup				currentAlbumPlayingLayoutOuter = null;
    public	ViewGroup				albumNavigatorLayoutOuter = null;
    public	ViewGroup				albumNavigatorLayout = null;
    public	ListView				albumNavigatorList = null;
    private ViewGroup				currentPlayingSongContainer = null;
    private TextView				artistNameText = null;
    private TextView				albumNameText = null;
    private TextView				songNameText = null;
    private TextView				songDurationText = null;
    private TextView				songDurationOngoingText = null;
    public	ViewGroup				eventListViewContainer = null;
    public	ImageButton				eventListDecreaseRadius = null;
    public	ImageButton				eventListIncreaseRadius = null;
    public	EditText				eventListRadius = null;
    public	TextView				eventListRadiusMetric = null;
    public	ListView				eventListView = null;
    public	ProgressBar				songProgressBar = null;
    public	ImageView 				forwardImage = null;
    public	ImageView 				rewindImage = null;
    public	ImageView 				nextImage = null;
    public	ImageView 				playPauseImage = null;
    
    public	WebView					webView	= null;
    public	ViewGroup				songSearchContainer = null;
    public	ViewGroup				helpView = null;
    public	ImageView				helpImageView = null;
    public	AutoCompleteTextView	songSearchTextView = null;
    public	ProgressDialog 			albumReloadProgressDialog = null;
    public	ProgressDialog 			concertAnalysisProgressDialog = null;
    
	/**********************************************
	 * 
	 * Timer stuff
	 * 
	 **********************************************/
    private Timer		removeAlbumLabelsTimer = null;
    //private TimerTask	removeAlbumLabelsTimerTask; // defined below
    private Timer		songProgressTimer = null;
    //private TimerTask	songProgressTimerTask; // defined below 
    private Timer		reloadEventListTimer = null;
    //private TimerTask	reloadEventListTimerTask; // defined below
    private Timer		albumListSelectedAlbumTimer = null;
    //TimerTask			albumListSelectedAlbumTimerTask; // defined below

    /**********************************************
     * 
     * External function from MusicUtils.java of 
     * Android's Music App
     * 
     * --- still needs to be tested
     *
     **********************************************/
    public boolean isMediaScannerScanning(Context context, ContentResolver contentResolver) {
        boolean result = false;
        Cursor cursor = contentResolver.query(MediaStore.getMediaScannerUri(), 
                				new String [] { MediaStore.MEDIA_SCANNER_VOLUME }, 
                				null, 
                				null, 
                				null);
        if (cursor != null) {
            if (cursor.getCount() == 1) {
                cursor.moveToFirst();
                result = "external".equals(cursor.getString(0));
            }
            cursor.close(); 
        } 

        return result;
    }
    
	/**********************************************
	 * 
	 *  Called when the activity is first created
	 *  
	 **********************************************/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;        	
        System.gc();
        
        //Log.i("PRFMC", "1");
        /*
    	 * Window Properties
    	 */
    	//requestWindowFeature(Window.FEATURE_PROGRESS);
    	//requestWindowFeature(Window.PROGRESS_VISIBILITY_ON);
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	
    	
    	/*
    	 * Blur&Dim the BG
    	 */
        // Have the system blur any windows behind this one.
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
//                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
////        getWindow().setFlags(WindowManager.LayoutParams.FLAG_DITHER, 
////        		WindowManager.LayoutParams.FLAG_DITHER);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND, 
//        		WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//        WindowManager.LayoutParams params = getWindow().getAttributes();
//        params.dimAmount = 0.625f;
//        getWindow().setAttributes(params);
        
//        Resources.Theme theme = getTheme();
//        theme.dump(arg0, arg1, arg2)
        
    	//setContentView(R.layout.songfest_main);
    	//Log.i("PRFMC", "2");

    	/*
    	 * Initialize Display
    	 */
    	WindowManager windowManager 		= (WindowManager) 
												getSystemService(Context.WINDOW_SERVICE);
    	this.display 						= windowManager.getDefaultDisplay();
    	//Log.i("PRFMC", "3");

   	
    	/*
    	 * Set Bug Report Handler
    	 */
    	fdHandler = new FilexDefaultExceptionHandler(this);

        /*
         * Check if Album Art Directory exists
         */
        checkAlbumArtDirectory();
        checkConcertDirectory();
        checkPreferencesDirectory();
        checkBackgroundDirectory();
        //Log.i("PRFMC", "7");
    	
    	/*
    	 * Get Preferences
    	 */
    	readPreferences();
    	
    	/*
    	 * Force landscape or auto-rotate orientation if user requested
    	 */
		if(alwaysLandscape)
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		else if(autoRotate)
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    	
    	/*
         * Create UI and initialize UI vars
         */
    	setContentView(R.layout.songfest_main);

    	//getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    	//if (true) return;
    	
    	//if(true)
    	//	return;
    	//Log.i("PRFMC", "4");
        initializeAnimations();
        //Log.i("PRFMC", "5");
        initializeUiVariables();
        //Log.i("PRFMC", "6");

    	/*
    	 * Set Background
    	 */
        setBackground();
    	        
        /*
         * Set Current View
         */
        switch(VIEW_STATE){
        case LIST_EXPANDED_VIEW:
        	setListExpandedView();
        	break;
        case FULLSCREEN_VIEW:
        	setFullScreenView();
        	break;
        default:
        	setNormalView();
        	break;
        }
        
//        AlphaAnimation aAnim = new AlphaAnimation(0.0f, 0.92f);
//        aAnim.setFillAfter(true);
//        aAnim.setDuration(200);
//        mainUIContainer.startAnimation(aAnim);
        
        /*
         * Check for SD Card
         */
        if (!android.os.Environment.getExternalStorageState().equals(
        		android.os.Environment.MEDIA_MOUNTED)){
        	Dialog noSDDialog = new Dialog(this);
        	noSDDialog.setTitle("SD Card Error!");
        	noSDDialog.setContentView(R.layout.no_sd_alert_layout);
        	noSDDialog.show();
        	return;
        }
        
        
        /*
         * Get a Content Resolver to browse
         * the phone audio database
         */
        contentResolver = getContentResolver();

        /*
         * Get Preferences
         */
        //SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        RockOnPreferenceManager settings = new RockOnPreferenceManager(FILEX_PREFERENCES_PATH);
        settings = new RockOnPreferenceManager(this.FILEX_PREFERENCES_PATH);
        // Shuffle
        boolean shuffle = settings.getBoolean("Shuffle", false);
   	   	this.SHUFFLE = shuffle;
   	   	//Playlist
   	   	if(playlist == constants.PLAYLIST_NONE)
   	   		playlist = settings.getLong(constants.PREF_KEY_PLAYLIST, constants.PLAYLIST_ALL);
   	   	Log.i("PLAYLIST PREF", constants.PREF_KEY_PLAYLIST+" "+constants.PLAYLIST_ALL+" "+playlist);
   	   	
   	   	
        
        /*
         * Check if the MediaScanner is scanning
         */
//        ProgressDialog pD = null;
//        while(isMediaScannerScanning(this, contentResolver) == true){
//        	if(pD == null){
//        		pD = new ProgressDialog(this);
//        		pD.setTitle("Scanning Media");
//        		pD.setMessage("Wait please...");
//        		pD.show();
//        	}
//        	try {
//				wait(2000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//        }
//        if(pD != null)
//        	pD.dismiss();
        
        
        /*
         * Initialize mediaPlayer
         */
        //this.mediaPlayer = new MediaPlayer();
        //this.mediaPlayer.setOnCompletionListener(songCompletedListener);

        
    	/*
    	 * Initialize (or connect to) BG Service
    	 *  - when connected it will call the method getCurrentPlaying()
    	 *  	and will cause the Service to reset its albumCursor
    	 */
    	initializeService();
    	//Log.i("PRFMC", "8");
    	musicChangedIntentReceiver= new MusicChangedIntentReceiver();  
        albumChangedIntentReceiver= new AlbumChangedIntentReceiver();
        mediaButtonPauseIntentReceiver = new MediaButtonPauseIntentReceiver();
        mediaButtonPlayIntentReceiver = new MediaButtonPlayIntentReceiver();
        registerReceiver(musicChangedIntentReceiver, musicChangedIntentFilter);
        //Log.i("PRFMC", "9");
        registerReceiver(albumChangedIntentReceiver, albumChangedIntentFilter);
        //Log.i("PRFMC", "10");
        registerReceiver(mediaButtonPauseIntentReceiver, mediaButtonPauseIntentFilter);
        registerReceiver(mediaButtonPlayIntentReceiver, mediaButtonPlayIntentFilter);
        // calls also getCurrentPlaying() upon connection
    	
        /*
         * Register Media Button Receiver
         */
//        mediaButtonIntentReceiver = new MediaButtonIntentReceiver();
//        registerReceiver(mediaButtonIntentReceiver, new IntentFilter("android.intent.action.MEDIA_BUTTON"));
        
    	/*
         * Get album information on a new
         * thread
         */
        //new Thread(){
        //	public void run(){
        		getAlbums(false);
        //Log.i("PRFMC", "11");
        //	}
        //}.start();
        	     
     
   	   	/*
   	   	 * Check for first time run ----------
   	   	 * 
   	   	 * Save Software Version
   	   	 * 	&
   	   	 * Show Help Screen
   	   	 */
//   	   	if(!settings.contains("Version")){
//   	   		Editor settingsEditor = settings.edit();
//   	   		settingsEditor.putLong("Version", VERSION);
//   	   		settingsEditor.commit();
//   	   		this.hideMainUI();
//   	   		this.showHelpUI();
//   	   	}
        Log.i("DBG", "Version - " + (new RockOnPreferenceManager(this.FILEX_PREFERENCES_PATH)).getLong("Version", 0) +" - "+ VERSION);		
   	   	if(!(new RockOnPreferenceManager(this.FILEX_PREFERENCES_PATH)).contains("Version")
   	   			|| (new RockOnPreferenceManager(this.FILEX_PREFERENCES_PATH)).getLong("Version", 0) < VERSION){
   	   	
   	   		(new RockOnPreferenceManager(this.FILEX_PREFERENCES_PATH)).putLong("Version", VERSION);
   	   		
   	   		/*
   	   		 * Clear previous Album Art
   	   		 */
   	   		Builder aD = new AlertDialog.Builder(context);
   	   		aD.setTitle("New Version");
   	   		aD.setMessage("The new version of RockOn supports album art download from higher quality sources. Do you want to download art now? " +
   	   				"Every 10 albums will take aproximately 1 minute to download. " +
   	   				"(You can always do this later by choosing the 'Get Art' menu option)");
   	   		aD.setPositiveButton("Yes", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
		   	   		//(new RockOnPreferenceManager(FILEX_PREFERENCES_PATH)).putLong("artImportDate", 0);
		   	   		try{
			   	   		File albumArtDir = new File(FILEX_ALBUM_ART_PATH);
			   	   		String[] fileList = albumArtDir.list();
			   	   		File albumArtFile;
			   	   		for(int i = 0; i< fileList.length; i++){
			   	   			albumArtFile = new File(albumArtDir.getAbsolutePath()+"/"+fileList[i]);
			   	   			albumArtFile.delete();
			   	   		}
			   	   		checkAlbumArtDirectory();
			   	   		triggerAlbumArtFetching();
		   	   		}catch(Exception e){
		   	   			e.printStackTrace();
		   	   		}
				}
   	   			
   	   		});
   	   		aD.setNegativeButton("No", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
				
				}
   	   			
   	   		});
   	   		aD.show();
   	   		
   	   		/*
   	   		 * Version 2 specific default preference changes
   	   		 */
   	   		// version 2 specific
   	   	   	if(albumCursor.getCount() > 100)
   	   	   		(new RockOnPreferenceManager(this.FILEX_PREFERENCES_PATH)).putBoolean(PREFS_SHOW_ART_WHILE_SCROLLING, false);
   	   	   	else
   	   	   		(new RockOnPreferenceManager(this.FILEX_PREFERENCES_PATH)).putBoolean(PREFS_SHOW_ART_WHILE_SCROLLING, true);
   	   		
   	   		readPreferences();
   	   		albumAdapter.showArtWhileScrolling = showArtWhileScrolling;
   	   		albumAdapter.showFrame = showFrame;
   			//
   	   		
   	   		/*
   	   		 * Show help screen
   	   		 */
   	   		this.hideMainUI();
   	   		this.showHelpUI();
   	   	} else {
   	        /*
   	         * Run albumArt getter in background
   	         */
   	   		long lastAlbumArtImportDate = settings.getLong("artImportDate", 0);
   	   		Log.i("SYNCTIME", lastAlbumArtImportDate+" + "+this.ART_IMPORT_INTVL+" < "+System.currentTimeMillis());
   	   		if(lastAlbumArtImportDate + this.ART_IMPORT_INTVL < System.currentTimeMillis()){
   	   			triggerAlbumArtFetching();
   	   		}
   	   		//Log.i("PRFMC", "13");
   	   	}
    }
    
    public void triggerAlbumArtFetching(){
    	albumReloadProgressDialog = new ProgressDialog(this);
    	albumReloadProgressDialog.setIcon(R.drawable.ic_menu_music_library);
    	albumReloadProgressDialog.setTitle("Loading Album Art");
    	albumReloadProgressDialog.setMessage("Waiting for connection");
    	// TODO: set powered by Last.FM
    	albumReloadProgressDialog.show();
        Thread albumArtThread = new Thread(){
        	public void run(){
        		try{
	        		LastFmAlbumArtImporter lastFmArtImporter = 
	        			new LastFmAlbumArtImporter(context);
	        		lastFmArtImporter.getAlbumArt();
        		} catch(Exception e){
        			e.printStackTrace();
        		}
        	}
        };
        //albumArtThread.setUncaughtExceptionHandler(albumArtUncaughtExceptionHandler);
        albumArtThread.start();
    }
     
    /*******************************************
     * 
     * On Resume
     * 
     *******************************************/
    public void onResume(){
    	super.onResume();
    	Log.i("RESUME", "RockOn Resuming...");
    }
    
    /*******************************************
     * 
     * On Destroy
     * 
     *******************************************/
    public void onDestroy(){
    	super.onDestroy();
    	
    	this.getWindow().setCallback(null);
    	//this.getWindow().getDecorView().getRootView().
    	
    	if(fdHandler != null){
    		fdHandler.destroy();
    		fdHandler = null;
    	}

    	/*
    	 * Context
    	 */
    	context = null;
    	
    	/*
    	 * Intent Receivers
    	 */
	    try{	
	    	if(this.albumChangedIntentReceiver != null)
	    		unregisterReceiver(albumChangedIntentReceiver);
	    	if(this.musicChangedIntentReceiver != null)
	    		unregisterReceiver(musicChangedIntentReceiver);
	    	if(this.mediaButtonPauseIntentReceiver != null)
	    		unregisterReceiver(this.mediaButtonPauseIntentReceiver);
	    	if(this.mediaButtonPlayIntentReceiver != null)
	    		unregisterReceiver(this.mediaButtonPlayIntentReceiver);
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
    	/*
    	 * Service Connection
    	 */
    	if(playerServiceConn != null){
    		unbindService(playerServiceConn);
    		playerServiceConn.filex = null;
    		playerServiceConn = null;
    	}
    	
    	/*
    	 * Service Iface
    	 */
    	playerServiceIface = null;
    	
    	/*
    	 * Current Playing Album Art
    	 */
    	if(currentAlbumPlayingImageView != null){
	    	if(this.currentAlbumPlayingImageView.getDrawable() != null)
	    		this.currentAlbumPlayingImageView.getDrawable().setCallback(null);
	    	this.currentAlbumPlayingImageView.setImageDrawable(null);
    	}
	    	
    	
    	/*
    	 * Navigator Album Art
    	 */
    	if(this.albumNavigatorList != null){
	    	this.albumNavigatorList.setAdapter(null);
	    	this.albumNavigatorList.removeAllViewsInLayout();
    	}
	    if(albumAdapter != null){
		   albumAdapter.context = null;
	    	if(albumAdapter.albumImage != null && albumAdapter.albumImageOverlay != null){
			   	if(albumAdapter.albumImage.getDrawable() != null)
		    		albumAdapter.albumImage.getDrawable().setCallback(null);
		    	if(albumAdapter.albumImageOverlay.getDrawable() != null)
		    		albumAdapter.albumImageOverlay.getDrawable().setCallback(null);
	    	}
	    	albumAdapter.albumImage = null;
	    	albumAdapter.albumImageOverlay = null;
	    }
	    
    	/*
    	 * LastFM clean up
    	 */
    	if(lastFmEventImporter != null){
    		this.lastFmEventImporter.eventLinkedListAdapter.clear();
    		lastFmEventImporter = null;
    	}
    	
    	/*
    	 * Stop Timers
    	 */
    	if(songProgressTimer != null)
    		songProgressTimer.cancel();
    	
    	/*
    	 * Clean up all global vars
    	 */
    	cleanUpGlobals();
    }
    
    /*******************************************
     * 
     * OnActivityResult
     * 
     *******************************************/
    @Override
    protected void onActivityResult(int requestCode,
    								int resultCode, 
    								Intent data){
    	try{
			Log.i("BACKFROMPREFERENCES", "Activity Result Processing...");

	    	/*
	    	 * Coming back from preferences - check if stuff changed
	    	 */
	    	if(requestCode == new Constants().PREFERENCES_REQUEST){
				Log.i("BACKFROMPREFERENCES", "...From Preferences Activity...");

				RockOnPreferenceManager settings = new RockOnPreferenceManager(FILEX_PREFERENCES_PATH);
	    		/*
	    		 * Check for the recently added playlist period change
	    		 */
//				if(this.recentPlaylistPeriod != getSharedPreferences(PREFS_NAME, 0)
				if(this.recentPlaylistPeriod != settings
	    				.getInt(new Constants().PREF_KEY_RECENT_PERIOD, new Constants().RECENT_PERIOD_DEFAULT_IN_DAYS)){

	    			Log.i("BACKFROMPREFERENCES", "Recent Playlist Period was changed...");

//	    			this.recentPlaylistPeriod = getSharedPreferences(PREFS_NAME, 0)
	    			this.recentPlaylistPeriod = settings
						.getInt(new Constants().PREF_KEY_RECENT_PERIOD, new Constants().RECENT_PERIOD_DEFAULT_IN_DAYS);
	    			
	    			/*
	    			 * Update value of the service component
	    			 */
	    			this.playerServiceIface.setRecentPeriod(this.recentPlaylistPeriod);
	    			
	    			/*
	    			 * Update the UI if we are playing the recently added playlist
	    			 */
	    			if(this.playlist == new Constants().PLAYLIST_RECENT){
	    				Log.i("BACKFROMPREFERENCES", "Reloading album navigator");
	    				/*
	    				 * Reinitialize the album and song cursors
	    				 */
	    				initializeAlbumCursor();
	    				albumCursor.moveToFirst();
	    				songCursor = initializeSongCursor(
	    									albumCursor.getString(
	    										albumCursor.getColumnIndexOrThrow(
	    												MediaStore.Audio.Albums.ALBUM)));
	    				
	    				/*
	    				 * Reload Navigator List
	    				 */
	    				recycleNavigatorList();
	    				albumAdapter = null;
	    				getAlbums(true);
	    			}
	    		}
	    		
	    		/*
	    		 * Reload preferences that require modification
	    		 */
	    		readPreferences();
	    		albumAdapter.showArtWhileScrolling = showArtWhileScrolling;
	    		albumAdapter.showFrame = showFrame;
	    		
	    		/*
	    		 * Reload Orientation stuff
	    		 */
	    		if(alwaysLandscape)
	    			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	    		else if(autoRotate)
	    			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
	    		else
	    			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
	    		
	    		/*
    			 * Update background
    			 */
    			if(VIEW_STATE == NORMAL_VIEW)
    				setBackground();
	    	}
    	} catch (Exception e) {
    		
    	}
    	
    }
    
    /*******************************************
     * 
     * Lite version popup
     * 
     *******************************************/
    public void showLitePopup(){
    	AlertDialog alertDialog = new AlertDialog.Builder(this)
    		.setIcon(R.drawable.icon)
    		.setTitle("Lite Version")
    		.setMessage("This option is available in the full version of RockOn. You can get it for free from the RockOn website," +
    				" but if you like the concept and wish to support further development, please consider buying it from the " +
    				"Android Market.")
    		.setPositiveButton("Get it!", liteClickListener)
    		.create();
    	alertDialog.show();
    }
    
    DialogInterface.OnClickListener liteClickListener = new DialogInterface.OnClickListener(){

		@Override
		public void onClick(DialogInterface dialog, int which) {
			Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pname:org.abrantes.filex"));
			startActivity(i);
		}
    	
    };
    
    /*******************************************
     * 
     * Create Menu
     * 
     *******************************************/
    public boolean onPrepareOptionsMenu(Menu menu) {
    	/*
    	 * Initialize clean menu
    	 */
        super.onPrepareOptionsMenu(menu);
        menu.clear();
        
        /*
         *  Add menu items
         */
        if(this.SHUFFLE)
        	menu.add(0, 0, 0, "Suffle Off");
        else
        	menu.add(0, 0, 0, "Shuffle On");
        menu.add(0, 1, 1, "Search");
        menu.add(0, 5, 5, "Share");
        menu.add(0, 3, 3, "Get Art");
        menu.add(0, 4, 4, "Concerts");
        menu.add(0, 6, 6, "Playlists");
        menu.add(0, 7, 7, "Preferences");
        menu.add(0, 8, 8, "Help");
        menu.add(0, 2, 2, "Exit");
        
        /*
         * Add menu icons
         */
        (menu.findItem(0)).setIcon(R.drawable.ic_menu_shuffle);
        (menu.findItem(3)).setIcon(R.drawable.ic_menu_music_library);
        (menu.findItem(5)).setIcon(android.R.drawable.ic_menu_share);
        (menu.findItem(1)).setIcon(android.R.drawable.ic_menu_search);
      //(menu.findItem(3)).setIcon(android.R.drawable.ic_menu_slideshow);
        (menu.findItem(4)).setIcon(android.R.drawable.ic_menu_today);
        (menu.findItem(6)).setIcon(R.drawable.ic_mp_current_playlist_btn);
        (menu.findItem(7)).setIcon(android.R.drawable.ic_menu_preferences);
        (menu.findItem(8)).setIcon(android.R.drawable.ic_menu_help);
        (menu.findItem(2)).setIcon(android.R.drawable.ic_menu_close_clear_cancel);
        //(menu.findItem(6)).setIcon(android.R.drawable.ic_menu_preferences);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
        /*
         * Shuffle
         */
        case 0:
        	if(this.SHUFFLE)
        		this.SHUFFLE = false;
        	else
        		this.SHUFFLE = true;
        	try {
					playerServiceIface.setShuffle(this.SHUFFLE);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			RockOnPreferenceManager settings = new RockOnPreferenceManager(FILEX_PREFERENCES_PATH);
			settings.putBoolean("Shuffle", this.SHUFFLE);
            return true;
        /*
         * Search
         */
        case 1:
        	if(GRATIS){
        		showLitePopup();
        		return true;
        	}
        	
        	//this.hideMainUI();
        	this.showSongSearch();
        	this.songSearchTextView.requestFocus();
        	Cursor allSongsCursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
					SONG_COLS, // we should minimize the number of columns
					null,	// all songs
					null,   // parameters to the previous parameter - which is null also 
					null	// sort order, SQLite-like
					);
        	SimpleCursorAdapter songAdapter = new SimpleCursorAdapter(this,
        																R.layout.simple_dropdown_item_2line,
        																allSongsCursor,
        																new String[] {MediaStore.Audio.Media.TITLE,
        																					MediaStore.Audio.Media.ARTIST},
        																new int[] {R.id.text1, R.id.text2});       	
			FilterQueryProvider songSearchFilterProvider = new FilterQueryProvider(){
				@Override
				public Cursor runQuery(CharSequence constraint) {
					String whereClause = MediaStore.Audio.Media.TITLE+" LIKE '%"+constraint+"%'"
											+" OR "+
											MediaStore.Audio.Media.ARTIST+" LIKE '%"+constraint+"%'";
					Log.i("SEARCH", whereClause);
					//whereClause = null;
					Cursor songsCursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
							SONG_COLS, // we should minimize the number of columns
							whereClause, // songs where the title or artist name matches
							null,   // parameters to the previous parameter - which is null also 
							null	// sort order, SQLite-like
							);
					return songsCursor;
				}					
			};
			songAdapter.setFilterQueryProvider(songSearchFilterProvider);
        	this.songSearchTextView.setAdapter(songAdapter);
        	songAdapter.setStringConversionColumn(allSongsCursor.getColumnIndexOrThrow(
        											MediaStore.Audio.Media.TITLE));
        	//this.songSearchTextView.setOnKeyListener(songSearchKeyListener);
        	return true;
        /*
         * Share
         */
        case 5:
        	if(GRATIS){
        		showLitePopup();
        		return true;
        	}
        	
        	ShareSong shareSong = new ShareSong(context, songCursor);
        	shareSong.shareByMail();
            return true;
        /*
         * Get Art
         */
        case 3:
//        	if(GRATIS){
//        		showLitePopup();
//        		return true;
//        	}
        	albumReloadProgressDialog = new ProgressDialog(this);
        	albumReloadProgressDialog.setIcon(R.drawable.ic_menu_music_library);
        	albumReloadProgressDialog.setTitle("Loading Album Art");
        	albumReloadProgressDialog.setMessage("Waiting for Last.FM connection");
        	// TODO: set powered by Last.FM
        	albumReloadProgressDialog.show();
        	Thread albumArtThread = new Thread(){
	        	public void run(){
	        		try{
		        		LastFmAlbumArtImporter lastFmArtImporter = 
		        			new LastFmAlbumArtImporter(context);
		        		lastFmArtImporter.getAlbumArt();
	        		}catch(Exception e){
	        			e.printStackTrace();
	        		}
	        	}
	        };
	        //albumArtThread.setUncaughtExceptionHandler(albumArtUncaughtExceptionHandler);
	        albumArtThread.start();
        	//containerLayout.addView(albumReloadProgressDialog);
            return true;
        /*
         * Concerts
         */
        case 4:
        	if(GRATIS){
        		showLitePopup();
        		return true;
        	}
        	
        	this.hideMainUI();
        	this.mainUIContainer.setVisibility(View.GONE);
        	//this.hideHelpUI();
        	this.showEventUI();
        	
        	/*
        	 * Concert Radius Thing 
        	 */
        	// TODO: need a metric
//			SharedPreferences prefs = getSharedPreferences(PREFS_NAME, 0);
			RockOnPreferenceManager prefs = new RockOnPreferenceManager(FILEX_PREFERENCES_PATH);
        	concertRadius = prefs.getLong("ConcertRadius", (long) (this.CONCERT_RADIUS_DEFAULT));
			this.eventListRadius.setText(String.valueOf(Math.round((float)concertRadius/1000)));
        	
			/*
			 * Show a dialog to give some nice feedback to the user
			 */
        	concertAnalysisProgressDialog = new ProgressDialog(this);
        	concertAnalysisProgressDialog.setIcon(android.R.drawable.ic_menu_today);
        	concertAnalysisProgressDialog.setTitle("Analysing concert information");
        	concertAnalysisProgressDialog.setMessage("Waiting for Last.FM connection");
        	concertAnalysisProgressDialog.show();
        	
        	/*
        	 * Analyze concert info
        	 */
        	lastFmEventImporter = new LastFmEventImporter(context);
        	new Thread(){
        		public void run(){
		        	try {
							lastFmEventImporter.getArtistEvents();
						} catch (SAXException e) {
							e.printStackTrace();
						} catch (ParserConfigurationException e) {
							e.printStackTrace();
						}
        		}
        	}.start();
        	return true;
        /*
         * Playlists
         */
        case 6:
        	if(GRATIS){
        		showLitePopup();
        		return true;
        	}
        	
        	/*
        	 * Get the views and make them visible
        	 */
        	String sortOrder = MediaStore.Audio.Playlists.NAME+" ASC";
        	Cursor playlistAllCursor = contentResolver.query(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI,
        														this.PLAYLIST_COLS, 
        														null, 
        														null, 
        														sortOrder);
    		
        	/*
        	 * Create Array with custom playlist + system playlists
        	 */
        	Playlist playlistTemp;
        	ArrayList<Playlist> playlistArray = new ArrayList<Playlist>();
        	/* ALL Playlist*/
        	playlistTemp = new Playlist();
        	playlistTemp.id = constants.PLAYLIST_ALL;
        	playlistTemp.name = "All songs";
        	playlistArray.add(playlistTemp);
        	/* Recently Added */ 
        	playlistTemp = new Playlist();
        	playlistTemp.id = constants.PLAYLIST_RECENT;
        	playlistTemp.name = "Recently Added";
        	playlistArray.add(playlistTemp);
        	/* ... other system playlists ... */
        	
        	/* add every playlist in the media store */
        	while(playlistAllCursor.moveToNext()){
        		playlistTemp = new Playlist();
        		playlistTemp.id = playlistAllCursor.getLong(playlistAllCursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists._ID));
        		playlistTemp.name = playlistAllCursor.getString(playlistAllCursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.NAME));
        		playlistArray.add(playlistTemp);
        		Log.i("PLAYLIST MENU", playlistTemp.id+" "+playlistTemp.name);
        	}
        	
//        	String[] fieldsFrom = new String[1];
//    		int[]	fieldsTo = new int[1];
//        	fieldsFrom[0] = MediaStore.Audio.Playlists.NAME;
//    		fieldsTo[0] = R.id.playlist_name;
//        	PlaylistCursorAdapter playlistAllAdapter = new PlaylistCursorAdapter(this.getApplicationContext(),
//        																			R.layout.playlist_item, 
//        																			playlistAllCursor, 
//        																			fieldsFrom, 
//        																			fieldsTo);
        	PlaylistArrayAdapter playlistAllAdapter = new PlaylistArrayAdapter(getApplicationContext(),
        																		R.layout.playlist_item,
        																		playlistArray);
        	
    		/*
    		 * Create Dialog
    		 */
    		AlertDialog.Builder aD = new AlertDialog.Builder(context);
    		aD.create();
    		aD.setTitle("Select Playlist");
    		aD.setAdapter(playlistAllAdapter, playlistDialogClickListener);
    		aD.show();
        	
//        	playlistView.setAdapter(playlistAllAdapter);
//        	
//        	playlistView.setOnItemClickListener(playlistItemClickListener);
        	
//        	/*
//        	 * Animate scroll up of the listview
//        	 */
//        	TranslateAnimation slideUp = new TranslateAnimation(0,0,display.getHeight(),0);
//        	slideUp.setFillAfter(true);
//        	slideUp.setDuration(250);
//        	playlistContainer.startAnimation(slideUp);
//        	
        	//this.mainUIContainer.addView(playlistAllSpinner);
        	return true;
        /*
         * Preferences
         */
        case 7:
        	if(GRATIS){
        		showLitePopup();
        		return true;
        	}
        	
        	Intent i = new Intent();
        	i.setClass(getApplicationContext(), RockOnSettings.class);
//           	this.recentPlaylistPeriod = getSharedPreferences(PREFS_NAME, 0)
           	(new RockOnPreferenceManager(FILEX_PREFERENCES_PATH))
        		.getInt(new Constants().PREF_KEY_RECENT_PERIOD, new Constants().RECENT_PERIOD_DEFAULT_IN_DAYS);
        	startActivityForResult(i,	new Constants().PREFERENCES_REQUEST);
        	return true;
        /*
         * Help
         */
        case 8:
        	this.hideMainUI();
        	//this.mainUIContainer.setVisibility(View.GONE);
        	this.showHelpUI();
        	return true;
        /*
         * Exit
         */
        case 2:
        	try {
					this.playerServiceIface.destroy();
		        	this.finish();
			} catch (Exception e) {
					e.printStackTrace();
			}
			return true;
        }
        return false;
    }

    /*******************************************
     * 
     * readPreferences
     * 
     *******************************************/
    public void readPreferences(){
//    	scrobbleDroid = getSharedPreferences(Filex.PREFS_NAME, 0)
//			.getBoolean(Filex.PREFS_SCROBBLE_DROID, false);
//		showArtWhileScrolling = getSharedPreferences(Filex.PREFS_NAME, 0)
//			.getBoolean(Filex.PREFS_SHOW_ART_WHILE_SCROLLING, false);
//		showFrame = getSharedPreferences(Filex.PREFS_NAME, 0)
//			.getBoolean(Filex.PREFS_SHOW_FRAME, false);
//		alwaysLandscape = getSharedPreferences(Filex.PREFS_NAME, 0)
//			.getBoolean(Filex.PREFS_ALWAYS_LANDSCAPE, false);
    	RockOnPreferenceManager prefs = new RockOnPreferenceManager(FILEX_PREFERENCES_PATH);
		scrobbleDroid = prefs
			.getBoolean(RockPlayer.PREFS_SCROBBLE_DROID, false);
		showArtWhileScrolling = prefs
			.getBoolean(RockPlayer.PREFS_SHOW_ART_WHILE_SCROLLING, false);
		showFrame = prefs
			.getBoolean(RockPlayer.PREFS_SHOW_FRAME, false);
		alwaysLandscape = prefs
			.getBoolean(RockPlayer.PREFS_ALWAYS_LANDSCAPE, false);
		autoRotate = prefs
			.getBoolean(RockPlayer.PREFS_AUTO_ROTATE, false);
    }
    
    /*******************************************
     * 
     * NEEDS TO BE MOVED TO THE LISTENERS SECTION
     * 
     *******************************************/
    /*
    OnKeyListener songSearchKeyListener = new OnKeyListener(){

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if(event.getAction() == KeyEvent.ACTION_DOWN){
				String currentSearchText = ((TextView) v).getText().toString();
				
				SimpleCursorAdapter songSearchCursor = (SimpleCursorAdapter) 
															songSearchTextView.getAdapter();
			}
			return false;
		}
    	
    };
    */
    
    /*******************************************
     * 
     * Initialize Service
     * 
     *******************************************/
    private void initializeService(){
    	this.playerServiceConn = new PlayerServiceConnection((RockPlayer) this);
    	//this.playerServiceIface = new PlayerServiceInterface();
    	this.startService(new Intent(context, PlayerService.class));
    	this.bindService((new Intent(context, PlayerService.class)), this.playerServiceConn, 0);
    }
    
    /*******************************************
     * 
     * initializeAnimations
     * 
     *******************************************/
    private void initializeAnimations(){
    	/*
    	 * Puts the left panel in perspective
    	 */
    	if(this.display.getOrientation() == 0)
	    	perspectiveLeft = new Rotate3dAnimation(
							0,0, 	// X-axis rotation
							30,30, 	// Y-axis rotation
							0,0,  	// Z-axis rotation
							100, 100, // rotation center
							0.0f, // Z-depth
							false); //reverse movement
    	else
    		perspectiveLeft = new Rotate3dAnimation(
					0,0, 	// X-axis rotation
					15,15, 	// Y-axis rotation
					0,0,  	// Z-axis rotation
					100, 100, // rotation center
					0.0f, // Z-depth
					false); //reverse movement
    	perspectiveLeft.setDuration(1);
		perspectiveLeft.setFillAfter(true);
		
		/*
		 * Puts the right panel in perspective
		 */
    	if(this.display.getOrientation() == 0)
		    perspectiveRight = new Rotate3dAnimation(
						0,0, 	// X-axis rotation
						-30,-30, 	// Y-axis rotation
						0,0,  	// Z-axis rotation
						100, 100, // rotation center
						0.0f, // Z-depth
						false); //reverse movement
		else
			perspectiveRight = new Rotate3dAnimation(
					0,0, 	// X-axis rotation
					-15,-15, 	// Y-axis rotation
					0,0,  	// Z-axis rotation
					100, 100, // rotation center
					0.0f, // Z-depth
					false); //reverse movement
		perspectiveRight.setDuration(1);
		perspectiveRight.setFillAfter(true);
		//perspectiveLeftAnim.setDuration(500);
		//perspectiveLeftAnim.setInterpolator(new AccelerateInterpolator());
		
		/*
		 * Hides a panel left
		 */
    	hideLeft = new Rotate3dAnimationY(
						30,90, 	// Y-axis rotation
						100, 100, // rotation center
						0.0f, // Z-depth
						false); //reverse movement
		hideLeft.setDuration(300);
		hideLeft.setFillAfter(true);
		hideLeft.setInterpolator(new DecelerateInterpolator());
		hideLeft.setAnimationListener(this.hideAlbumAnimationListener);

		/*
		 * Shows a panel hidden to the left
		 */
    	showLeft = new Rotate3dAnimationY(
						270,390, 	// Y-axis rotation
						100, 100, // rotation center
						0.0f, // Z-depth
						false); //reverse movement
		showLeft.setDuration(600); // double of the angle
		showLeft.setFillAfter(true);
		showLeft.setInterpolator(new AccelerateInterpolator());
		showLeft.setAnimationListener(this.showAlbumAnimationListener);
		
		/*
		 * Fades an Album out
		 */
		/*fadeAlbumOut = new AlphaAnimation((float) 1.0, 0);
		fadeAlbumOut.setDuration(250);
		fadeAlbumOut.setFillAfter(true);
		fadeAlbumOut.setAnimationListener(this.hideAlbumAnimationListener);    	
		*/
//		fadeAlbumOut = new TranslateAnimation(-display.getWidth(),-display.getWidth(), 0, 0);
		fadeAlbumOut = new AlphaAnimation(1.0f, 0.0f);
		fadeAlbumOut.setDuration(200);
		fadeAlbumOut.setFillAfter(true);
		//fadeAlbumOut.setInterpolator(new AccelerateInterpolator());
		fadeAlbumOut.setAnimationListener(this.hideAlbumAnimationListener);    	
		
		/*
		 * Fades an Album in
		 */
		/*fadeAlbumIn = new AlphaAnimation(0, 1);
		fadeAlbumIn.setDuration(250);
		fadeAlbumIn.setFillAfter(true);
		fadeAlbumIn.setAnimationListener(this.showAlbumAnimationListener);
		*/
//		fadeAlbumIn = new TranslateAnimation(-display.getWidth(),0, 0, 0);
		fadeAlbumIn = new AlphaAnimation(0.0f, 1.0f);
		fadeAlbumIn.setDuration(200);
		fadeAlbumIn.setFillAfter(true);
		//fadeAlbumIn.setInterpolator(new AccelerateInterpolator());
		fadeAlbumIn.setAnimationListener(this.showAlbumAnimationListener);
    }
    
    /*******************************************
     * 
     * initializeUiVariables
     * 
     *******************************************/
    private void initializeUiVariables() {
    	
    	/*
    	 *  Retrieve components from layout
    	 */
    	this.currentAlbumPlayingImageView 	= (ImageView) 
    											findViewById(R.id.current_album_image);
    	this.currentAlbumPlayingOverlayImageView = (ImageView)
    											findViewById(R.id.current_album_image_overlay);
    	this.mainUIContainer				= (ViewGroup)
    											findViewById(R.id.songfest_main_ui_container);
    	this.containerLayout 				= (ViewGroup)
    											findViewById(R.id.songfest_container);
    	this.currentPlayingLayout 			= (ViewGroup)
												findViewById(R.id.songfest_current_playing_container);
    	this.currentAlbumPlayingLayout 		= (ViewGroup)
												findViewById(R.id.songfest_current_album_playing_container);
    	this.currentAlbumPlayingLayoutOuter	= (ViewGroup)
												findViewById(R.id.songfest_current_album_playing_container_global);
    	this.albumNavigatorLayout			= (ViewGroup)
												findViewById(R.id.songfest_navigator_container);
    	this.albumNavigatorLayoutOuter		= (ViewGroup)
												findViewById(R.id.songfest_navigator_container_outer);
    	this.albumNavigatorList				= (ListView)
    											findViewById(R.id.navigator_listview);
//    	this.albumNavigatorTextContainer	= (ViewGroup)
//    											findViewById(R.id.navigator_album_text_container);
//    	this.navigatorInitialTextView		= (TextView)
//    											findViewById(R.id.navigator_initial);
    	this.currentPlayingSongContainer	= (ViewGroup)
    											findViewById(R.id.current_playing_song_container);
    	this.artistNameText					= (TextView)
    											findViewById(R.id.songfest_current_playing_artist);
    	this.albumNameText					= (TextView)
    											findViewById(R.id.songfest_current_playing_album_name);
    	this.songNameText					= (TextView)
    											findViewById(R.id.songfest_current_playing_song_name);
    	this.songDurationText				= (TextView)
    											findViewById(R.id.songfest_current_playing_song_progress);
    	this.songDurationOngoingText		= (TextView)
												findViewById(R.id.songfest_current_playing_song_progress_ongoing);
//    	this.songListContainer				= (ViewGroup)
//    											findViewById(R.id.song_listview_container);
//    	this.songListHint					= (TextView)
//    											findViewById(R.id.song_listhint);
//    	this.songListView					= (ListView)
//    											findViewById(R.id.song_listview);
    	this.eventListViewContainer			= (ViewGroup)
    											findViewById(R.id.event_listview_container);
    	this.eventListDecreaseRadius		= (ImageButton)
    											findViewById(R.id.event_decrease_radius);
    	this.eventListIncreaseRadius		= (ImageButton)
    											findViewById(R.id.event_increase_radius);
    	this.eventListRadius				= (EditText)
    											findViewById(R.id.event_radius);
    	this.eventListRadiusMetric			= (TextView)
    											findViewById(R.id.event_radius_metric);
    	this.eventListView					= (ListView)
    											findViewById(R.id.event_listview);
    	this.songProgressBar				= (ProgressBar)
    											findViewById(R.id.songfest_current_playing_song_progressbar);
//    	this.webView						= (WebView)
//    											findViewById(R.id.web_view);
    	this.songSearchContainer			= (ViewGroup)
    											findViewById(R.id.songsearch_container);
    	this.songSearchTextView				= (AutoCompleteTextView)
    											findViewById(R.id.songsearch_textview);
    	this.helpImageView					= (ImageView)
    											findViewById(R.id.help_image);
    	this.helpView						= (ViewGroup)
    											findViewById(R.id.help_view);
    	
    	/*
    	 * Control buttons and button overlays
    	 */
    	this.playPauseImage					= (ImageView)
												findViewById(R.id.current_album_image_play_pause);
    	//playPauseImage.setImageResource(android.R.drawable.ic_media_play);
//    	this.playPauseImageOverlay			= (ImageView)
//												findViewById(R.id.current_album_image_play_pause_overlay);
//    	playPauseImageOverlay.setVisibility(View.GONE);
//    	playPauseImageOverlay.setImageResource(R.drawable.button_big_highlight_gradient);
//    	
//    	this.rewindImage					= (ImageView)
//    											findViewById(R.id.rewind_image);
//    	rewindImage.setImageResource(android.R.drawable.ic_media_rew);
//		this.rewindImageOverlay				= (ImageView)
//    											findViewById(R.id.rewind_image_overlay);
//    	rewindImageOverlay.setVisibility(View.GONE);
//		rewindImageOverlay.setImageResource(R.drawable.button_small_highlight_gradient);
//    	
//		this.forwardImage					= (ImageView)
//    											findViewById(R.id.forward_image);
//    	forwardImage.setImageResource(android.R.drawable.ic_media_ff);
//    	this.forwardImageOverlay			= (ImageView)
//    											findViewById(R.id.forward_image_overlay);
//    	forwardImageOverlay.setVisibility(View.GONE);
//    	forwardImageOverlay.setImageResource(R.drawable.button_small_highlight_gradient);
//    	
//    	this.nextImage						= (ImageView)
//    											findViewById(R.id.next_image);
//    	nextImage.setImageResource(android.R.drawable.ic_media_next);
   	//this.nextImageOverlay				= (ImageView)
    	//										findViewById(R.id.skipsong_image_overlay);
    	//nextImageOverlay.setImageResource(android.R.drawable.ic_media_next);
//    	this.songSearchButton				= (Button)
//    											findViewById(R.id.songsearch_button);
//    	this.songProgressTotalView			= (ImageView)
//												findViewById(R.id.songfest_current_playing_song_progress_total);
//		this.songProgressView				= (ImageView)
//												findViewById(R.id.songfest_current_playing_song_progress_current);
//    	this.preferenceScreen				= (PreferenceScreen)
//    											findViewById(R.id.preference_screen);
    	
    	/*
    	 *  Put views in perspective
    	 */
    	//this.currentAlbumPlayingLayout.startAnimation(perspectiveLeft);
    	this.currentPlayingLayout.startAnimation(perspectiveLeft);
		this.albumNavigatorList.startAnimation(perspectiveRight);
    	
    	/*
    	 *  Setup interaction listeners
    	 */
    	this.albumNavigatorList.setOnItemClickListener(this.albumNavigatorListItemClickListener);
    	this.albumNavigatorList.setOnItemLongClickListener(this.albumNavigatorListItemLongClickListener);
    	this.albumNavigatorList.setOnScrollListener(this.albumNavigatorScrollListener);
    	this.albumNavigatorList.setOnTouchListener(this.mainUIContainerTouchListener);
    	
    	this.currentAlbumPlayingImageView.setOnTouchListener(this.mainUIContainerTouchListener);
    	    	
    	this.currentAlbumPlayingImageView.setOnClickListener(currentAlbumClickListener);
    	
    	this.currentPlayingSongContainer.setOnClickListener(this.songNameClickListener);
    	this.currentPlayingSongContainer.setOnTouchListener(this.songTouchListener);
    	
//    	this.songNameText.setOnClickListener(this.songNameClickListener);
//    	this.songNameText.setOnTouchListener(this.songTouchListener);
//    	this.songDurationText.setOnClickListener(this.songNameClickListener);
//    	this.songDurationText.setOnTouchL	istener(this.songTouchListener);
//    	this.songProgressBar.setOnClickListener(this.songNameClickListener);
//    	this.songProgressBar.setOnTouchListener(this.songTouchListener);
    	//this.songListView.setOnKeyListener(this.songListKeyListener);
    	//this.songListContainer.setOnKeyListener(this.songListKeyListener);
    	//this.containerLayout.setOnKeyListener(this.songListKeyListener);
//    	this.songListView.setOnItemClickListener(this.songListItemClickListener);
//    	this.songListView.setOnItemLongClickListener(this.songListItemLongClickListener);
    	// Temporary change - for now
    	//this.currentAlbumPlayingImageView.setOnLongClickListener(this.songNameLongClickListener);
    	//this.songNameText.setOnLongClickListener(this.songNameLongClickListener);
    	//this.songDurationText.setOnLongClickListener(this.songNameLongClickListener);
    	this.artistNameText.setOnClickListener(this.artistNameClickListener);
    	this.albumNameText.setOnClickListener(this.artistNameClickListener);
    	//this.webView.setOnKeyListener(this.webViewKeyListener);
    	this.songSearchTextView.setOnItemClickListener(this.songSearchTextItemClickListener);
    	this.eventListIncreaseRadius.setOnClickListener(this.eventListIncreaseRadiusClickListener);
    	this.eventListDecreaseRadius.setOnClickListener(this.eventListDecreaseRadiusClickListener);
    	this.helpImageView.setOnClickListener(this.helpImageClickListener);
    	
    	

    	
    	/*
    	 *  Fix layout stuff
    	 */
    	// TODO:
    	//  set the height of the top panel to be ~66% of the screen height (LANDSCAPE)
    	
    	//	set the width of the left panel to be ~66% of the screen width	(PORTRAIT)
    	RelativeLayout.LayoutParams params = null;
    	/* Don't know the code of Portrait */
    	if(this.display.getOrientation() == 0){
    		params = new RelativeLayout.LayoutParams(
    				(int) Math.round(
    					this.display.getWidth()*CURRENT_PLAY_SCREEN_FRACTION),
    				LayoutParams.FILL_PARENT);
        	this.currentPlayingLayout.setLayoutParams(params);

    	} else {
    		params = new RelativeLayout.LayoutParams(
					(int) Math.round(
						this.display.getWidth()*CURRENT_PLAY_SCREEN_FRACTION_LANDSCAPE),
					LayoutParams.FILL_PARENT);
        	this.currentPlayingLayout.setLayoutParams(params);

    		/* 
    		 * Special Positioning in Landscape Mode
    		 * ------- can go into a different function 
    		 * ------- or maybe into the XML...
    		 */
        	//getWindow().setBackgroundDrawableResource(R.drawable.bg_landscape);
    		RelativeLayout.LayoutParams p = null;
        	/* album image */
    		p = new RelativeLayout.LayoutParams(
					(int) Math.round(
							this.display.getWidth()*CURRENT_PLAY_SCREEN_FRACTION_LANDSCAPE/2),
					(int) Math.round(
							this.display.getWidth()*CURRENT_PLAY_SCREEN_FRACTION_LANDSCAPE/2));
    		p.topMargin = 12;
    		p.leftMargin = 12;
    		this.currentAlbumPlayingLayoutOuter.setLayoutParams(p);
        	
    		/*
    		 * Current Artist&Album container to the right of the album
    		 */
    		p = (LayoutParams) findViewById(R.id.current_playing_artist_album_container).getLayoutParams();
    		p.addRule(RelativeLayout.RIGHT_OF, R.id.songfest_current_album_playing_container_global);
    		p.addRule(RelativeLayout.ABOVE, R.id.current_playing_song_container);
    		findViewById(R.id.current_playing_artist_album_container).setLayoutParams(p);
    		/* artist name */
//    		p = (LayoutParams) this.artistNameText.getLayoutParams();
//    		p.addRule(RelativeLayout.RIGHT_OF, R.id.songfest_current_album_playing_container_global);
//    		this.artistNameText.setLayoutParams(p);
    		/* album name */
//    		p = (LayoutParams) this.albumNameText.getLayoutParams();
//    		p.addRule(RelativeLayout.RIGHT_OF, R.id.songfest_current_album_playing_container_global);
//    		this.albumNameText.setLayoutParams(p);
    		/* song name */
//    		p = (LayoutParams) this.songNameText.getLayoutParams();
//    		p.addRule(RelativeLayout.BELOW, R.id.songfest_current_album_playing_container_global);
//    		this.songNameText.setLayoutParams(p);
    		this.songNameText.setSingleLine();
    		/* song progress */
    		//p = (LayoutParams) this.songProgressBar.getLayoutParams();
    		//p.addRule(RelativeLayout.RIGHT_OF, R.id.songfest_current_album_playing_container_global);
    		//this.songProgressBar.setLayoutParams(p);
    	}
    	
/*    	RelativeLayout.LayoutParams rParams = new RelativeLayout.LayoutParams(
												(int) Math.round(
															this.display.getWidth()*CURRENT_PLAY_SCREEN_FRACTION),
												LayoutParams.FILL_PARENT);
    	this.currentAlbumPlayingLayout.setLayoutParams(rParams);*/
    	

		
		/*
		 * Create Album Labels (TextViews)
		 */
		/*
		int listHeight = this.display.getHeight()-20;
		int numberOfVisibleAlbums = (int) Math.floor(listHeight/(this.display.getWidth()*NAVIGATOR_SCREEN_FRACTION))+1;
		for(int i = 0; i < numberOfVisibleAlbums; i++){
			TextView albumLabel = new TextView(this);
			LinearLayout.LayoutParams tParams = new LinearLayout.LayoutParams(
													(int) (this.display.getWidth()*NAVIGATOR_SCREEN_FRACTION*1.75),
													(int) (this.display.getWidth()*NAVIGATOR_SCREEN_FRACTION*0.5));
			tParams.bottomMargin = (int) (this.display.getWidth()*NAVIGATOR_SCREEN_FRACTION*0.25);
			tParams.topMargin = (int) (this.display.getWidth()*NAVIGATOR_SCREEN_FRACTION*0.25);
			albumLabel.setLayoutParams(tParams);
			albumLabel.setTextColor(Color.argb(200, 255, 255, 255));
			albumLabel.setTextSize(20);
			albumLabel.setMaxLines(4);
			albumLabel.setBackgroundResource(R.drawable.album_text_bg);
			this.albumNavigatorTextContainer.addView(albumLabel);
		}
		this.albumNavigatorTextContainer.setVisibility(View.GONE);
		*/
	}

    /*********************************************
     * 
     * Set Preference
     * 
     *********************************************/
    void setBooleanPreference(String prefName, boolean preference){
    	RockOnPreferenceManager prefs = new RockOnPreferenceManager(FILEX_PREFERENCES_PATH);
    	prefs.putBoolean(prefName, preference);
//    	
//    	SharedPreferences 			settings = getSharedPreferences(PREFS_NAME, 0);
//        SharedPreferences.Editor 	editor 	 = settings.edit();
//        editor.putBoolean(prefName, preference);
//        editor.commit();
    }
    
    /*********************************************
     * 
     * getCurrentPlaying
     * 	get what the player service is currently
     * 	playing and update UI
     *
     *********************************************/
    public void getCurrentPlaying(){
    	//if(true) return;
    	
    	/*
    	 * ask the player service if/what he is playing
    	 */
    	int albumCursorPosition = 0;
    	int songCursorPosition = 0;
    	try {
			albumCursorPosition = this.playerServiceIface.getAlbumCursorPosition();
	    	songCursorPosition	= this.playerServiceIface.getSongCursorPosition();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		if(albumCursorPosition == -1 || songCursorPosition == -1){
//			Log.i("GETCURPLAY", "BOTH CURSORS ARE -1");
//			try{
//				Random rand = new Random();
//				if(albumCursor != null && rand != null)
//					albumCursorPosition = rand.nextInt(albumCursor.getCount()-1);
//				else
//					albumCursorPosition = 1;
//				albumCursorPositionPlaying = albumCursorPosition;
//				songCursorPosition = 0;
//			} catch (Exception e) {
//				try{
//					if(albumCursor != null)
//						Log.i("EXCP", ""+albumCursor.getCount());
//				} catch (Exception ee){
//					ee.printStackTrace();
//				}
//				e.printStackTrace();
//				albumCursorPosition = 0;
//				albumCursorPositionPlaying = albumCursorPosition;
//				songCursorPosition = 0;
//			}
//			try {
//				playerServiceIface.setAlbumCursorPosition(albumCursorPosition);
//				playerServiceIface.setSongCursorPosition(songCursorPosition);
//				playerServiceIface.play(albumCursorPosition, songCursorPosition);
//				playerServiceIface.pause();
//				// HACKzzzzzzzzz we need a paused event from the service
//				this.stopSongProgress();
//			} catch (RemoteException e) {
//				e.printStackTrace();
//			}
    	} else {
    		try {
				if(playerServiceIface.isPlaying()){
					TransitionDrawable playPauseTDrawable = (TransitionDrawable) playPauseImage.getDrawable();
					playPauseTDrawable.setCrossFadeEnabled(true);
					playPauseTDrawable.startTransition(1);
					playPauseTDrawable.invalidateSelf();
//	    			this.playPauseImage.setImageResource(android.R.drawable.ic_media_pause);    		}
				} else {
					albumCursorPosition = playerServiceIface.getAlbumCursorPosition();
					songCursorPosition = playerServiceIface.getSongCursorPosition();
					albumCursorPositionPlaying = albumCursorPosition;

//					Log.i("INIT", albumCursorPosition+" "+songCursorPosition);
//					playerServiceIface.play(albumCursorPositionPlaying,
//											songCursorPosition);
//					playerServiceIface.pause();
					this.stopSongProgress();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
		// TODO: ask if he is playing
		/*
		 * If media Library is empty just show a popup
		 */
		if(albumCursor.getCount() == 0){
			Dialog noMediaDialog = new Dialog(this);
			noMediaDialog.setTitle("No Media Available");
			//TextView noMediaText = new TextView(this);
			//noMediaText.setText("Please add some Music to your SD Card");
			//noMediaDialog.setContentView(noMediaText);
			noMediaDialog.show();
			return;
		}
		
		/*
		 * Go to the current playing Media
		 */
		Log.i("GETCURPLAY", "MOVING CURSORS");

		try{
			albumCursor.moveToPosition(albumCursorPosition);
			albumCursorPositionPlaying = albumCursorPosition;
			songCursor = initializeSongCursor(albumCursor.getString(
										albumCursor.getColumnIndexOrThrow(
												MediaStore.Audio.Albums.ALBUM)));
			songCursor.moveToPosition(songCursorPosition);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Log.i("GETCURPLAY", "GET ALBUM ART");

		try{
//			/*
//			 * get albumArt
//			 */
//			String albumCoverPath = albumCursor.getString(
//										albumCursor.getColumnIndexOrThrow(
//											MediaStore.Audio.Albums.ALBUM_ART));
//	    	// if it does not exist in database look for our dir
//	    	if(albumCoverPath == null){
//	    		String artistName = albumCursor.getString(
//										albumCursor.getColumnIndexOrThrow(
//												MediaStore.Audio.Albums.ARTIST));
//	    		String albumName = albumCursor.getString(
//										albumCursor.getColumnIndexOrThrow(
//												MediaStore.Audio.Albums.ALBUM));
//	    		String path = this.FILEX_ALBUM_ART_PATH+
//	    						validateFileName(artistName)+
//								" - "+
//								validateFileName(albumName)+
//								FILEX_FILENAME_EXTENSION;
//	    		File albumCoverFilePath = new File(path);
//				if(albumCoverFilePath.exists() && albumCoverFilePath.length() > 0){
//					albumCoverPath = path;
//				}
//	    	}
//	    	
//	    	Log.i("GETCURPLAY", "UPDATING UI COMPONENT");
//
// 	    	/*
//	    	 * Update currentPlaying albumArt UI component
//	    	 */
//			if(albumCoverPath != null){
//				// TODO:
//				// adjust sample size
//				Options opts = new Options();
//				opts.inSampleSize = 1;
//				Bitmap albumCoverBitmap = BitmapFactory.decodeFile(albumCoverPath, opts);
//				this.currentAlbumPlayingImageView.setImageBitmap(albumCoverBitmap);
//			} else {
//				// TODO:
//				// adjust sample size dynamically
//				Options opts = new Options();
//				opts.inSampleSize = 1;
//				Bitmap albumCoverBitmap = BitmapFactory.decodeResource(this.context.getResources(),
//																R.drawable.albumart_mp_unknown, opts);
//				if(albumCoverBitmap != null)
//					this.currentAlbumPlayingImageView.setImageBitmap(albumCoverBitmap);
//			}
			
			if(VIEW_STATE == FULLSCREEN_VIEW)
				this.currentAlbumPlayingImageView.setImageBitmap(albumAdapter.getAlbumBitmap(albumCursor.getPosition(), BITMAP_SIZE_FULLSCREEN));
			else
				this.currentAlbumPlayingImageView.setImageBitmap(albumAdapter.getAlbumBitmap(albumCursor.getPosition(), BITMAP_SIZE_NORMAL));
			
			if(showFrame)
				this.currentAlbumPlayingOverlayImageView.setVisibility(View.VISIBLE);
			else
				this.currentAlbumPlayingOverlayImageView.setVisibility(View.GONE);
			
			Log.i("GETCURPLAY", "UPDATING REST OF UI");
	
			/*
			 * Update currentPlaying artist UI component
			 */
			this.updateArtistTextUI();
			/*
			 * Update currentPlaying song UI component
			 */
			this.updateSongTextUI();
			/*
			 * Update Song Progress
			 */
			this.updateSongProgress();
			
			Log.i("GETCURPLAY", "CENTER ALBUMLIST");
	
			/*
			 * Center Album List
			 */
			albumNavigatorList.setSelectionFromTop(albumCursorPositionPlaying, 
														(int) Math.round(
																(display.getHeight()-20)/2.0 - 
																(display.getWidth()*(1-CURRENT_PLAY_SCREEN_FRACTION_LANDSCAPE))
															)
														);
			this.albumNavigatorScrollListener.onScrollStateChanged(this.albumNavigatorList, OnScrollListener.SCROLL_STATE_IDLE);

    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
	/*********************************************
	 * 
	 * get Album info
	 *  
	 *********************************************/
    static Bitmap[] albumImages = null;
    public void getAlbums(boolean force){
    	/*
    	 * Query the phone database for albums
    	 */
		initializeAlbumCursor();
		
		/*
		 * Create Album Navigator
		 */
		String[] fieldsFrom = new String[1];
		int[] fieldsTo		= new int[1];
		fieldsFrom[0] = MediaStore.Audio.Albums.ALBUM_ART;
		fieldsTo[0] = R.id.navigator_albumart_image;
		
		if(albumAdapter == null || force){
			Log.i("NOCACHE", "Album List was not cached");
			albumAdapter = new AlbumCursorAdapter(
													this.getApplicationContext(),
													R.layout.navigator_item,
													albumCursor,
													fieldsFrom,
													fieldsTo,
													albumImages,
													showArtWhileScrolling,
													showFrame);
		} else {
			albumAdapter.context = this.getApplicationContext();
			albumAdapter.reloadNavigatorWidth();
		}
		
		//albumAdapter.showArtWhileScrolling = showArtWhileScrolling;
		albumAdapter.showFrame = showFrame;
		
		/*
		 * initialize preloaded images array
		 */
		if(albumImages == null || force){
			if(albumCursor.getCount() < albumAdapter.AVOID_PRELOAD_THRESHOLD){
				try{
					for(int i=0; i<albumCursor.getCount(); i++){
						if(albumImages[i] != null)
							albumImages[i].recycle();
					}
				}catch (Exception e){
					e.printStackTrace();
				}
				albumImages = new Bitmap[albumCursor.getCount()];
				for(int i=0; i<albumCursor.getCount(); i++){
					albumImages[i]=albumAdapter.getAlbumBitmap(
							i, 
							albumAdapter.BITMAP_SIZE_XSMALL);
				}
				albumAdapter.albumImages = albumImages;
				albumNavigatorList.setSelection(
						albumNavigatorList.getSelectedItemPosition());
			}
		}
		
//		if(albumImages == null || force){
////			if(albumCursor.getCount() < albumAdapter.AVOID_PRELOAD_THRESHOLD){
//				try{
//					for(int i=0; i<albumCursor.getCount(); i++){
//						if(albumImages[i] != null)
//							albumImages[i].recycle();
//					}
//				}catch (Exception e){
//					e.printStackTrace();
//				}
//				albumImages = new Bitmap[30];
//				for(int i=0; i<30; i++){
//					albumImages[i]=albumAdapter.getAlbumBitmap(
//							i, 
//							albumAdapter.BITMAP_SIZE_XSMALL);
//				}
//				albumAdapter.albumImages = albumImages;
//				albumNavigatorList.setSelection(
//						albumNavigatorList.getSelectedItemPosition());
////			}
//		}
		
		this.albumNavigatorList.setAdapter(albumAdapter);
		this.albumNavigatorScrollListener.onScrollStateChanged(this.albumNavigatorList, OnScrollListener.SCROLL_STATE_IDLE);
		
		// No albums
		if(albumCursor == null)
			return;
		
		/*
		 * Initialize the song and artist cursors
		 */
		songCursor = initializeSongCursor(null);
		initializeArtistCursor();
    }
    
    /*
     * Cursor Initialization
     */
    public void	initializeAlbumCursor(){
		try{
	    	Log.i("DBG", "Initializing Album Cursor - playlist - "+playlist);
	//		playlist = this.getSharedPreferences(this.PREFS_NAME, 0).getLong(constants.PREF_KEY_PLAYLIST, constants.PLAYLIST_ALL);
	    	if(playlist == constants.PLAYLIST_ALL){
		    	String sortClause = MediaStore.Audio.Albums.ARTIST+" ASC";
		    	albumCursor = contentResolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
						ALBUM_COLS, // we should minimize the number of columns
						null,	// all albums 
						null,   // parameters to the previous parameter - which is null also 
						sortClause	// sort order, SQLite-like
						);
	    	} else if(playlist == constants.PLAYLIST_RECENT){
	        	RockOnPreferenceManager prefs = new RockOnPreferenceManager(FILEX_PREFERENCES_PATH);
	//    		double period = getSharedPreferences(PREFS_NAME, 0)
	        	double period = prefs
	    							.getInt(constants.PREF_KEY_RECENT_PERIOD, constants.RECENT_PERIOD_DEFAULT_IN_DAYS) * 24 * 60 * 60;
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
		    	String sortClause = MediaStore.Audio.Playlists.Members.ALBUM+" ASC";
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
		    			Log.i("DBG", "Album Repeated - "+playlistCursor.getString(playlistCursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.Members.ALBUM_KEY)));
		    			Log.i("DBG", "Album Repeated - "+playlistCursor.getString(playlistCursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.Members.ALBUM)));
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
	
		    		Log.i("DBG", "Album - "+playlistCursor.getString(playlistCursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.Members.ALBUM_KEY)));
		   
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
		}catch(Exception e){
			e.printStackTrace();
		}
//    	/*************DBG********************************/
//    	initializeAlbumCursorFromPlaylist();
    }
    public Cursor	initializeSongCursor(String albumName){
//    	//playlist=1;
//    	if(albumName == null){
//	    	songCursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
//					SONG_COLS, // we should minimize the number of columns
//					null,	// all songs
//					null,   // parameters to the previous parameter - which is null also 
//					null	// sort order, SQLite-like
//					);
//    	} else {
    	try{
			String whereClause = null;
			if(playlist == constants.PLAYLIST_ALL){
	    		if(albumName != null)
	    			whereClause = MediaStore.Audio.Media.ALBUM+"=\""+
	    								albumName.replaceAll("\"", "\"\"")+
	    								"\"";
		    	String sortClause = MediaStore.Audio.Media.TRACK+" ASC";
	    		Log.i("SONGSEARCH", ""+whereClause);
	    		return contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
						SONG_COLS, 		// we should minimize the number of columns
						whereClause,	// all songs from a certain album
						null,   		// parameters to the previous parameter - which is null also 
						sortClause		// sort order, SQLite-like
						);
			} else if (playlist == constants.PLAYLIST_RECENT){
				if(albumName != null)
	    			whereClause = MediaStore.Audio.Media.ALBUM+"=\""+
	    								albumName.replaceAll("\"", "\"\"")+"\"";
		    	String sortClause = MediaStore.Audio.Media.TRACK+" ASC";
	    		Log.i("SONGSEARCH", ""+whereClause);
	    		return contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
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
		    		whereClause = MediaStore.Audio.Playlists.Members.ALBUM + "=\""+
		    			albumName.replaceAll("\"", "\"\"") +
		    			"\"";
				Cursor playlistCursor = contentResolver.query(MediaStore.Audio.Playlists.Members.getContentUri("external", playlist),
						PLAYLIST_SONG_COLS, // we should minimize the number of columns
						whereClause,	// all albums 
						null,   // parameters to the previous parameter - which is null also 
						null	// sort order, SQLite-like
						);
		    	whereClause = MediaStore.Audio.Media.ALBUM + "=\""+
		    		albumName.replaceAll("\"", "\"\"") + "\"";
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
	
		    		Log.i("DBG", "Album - "+playlistCursor.getString(playlistCursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.Members.TITLE_KEY)));
		    	}
		    	
		    	String sortClause = MediaStore.Audio.Media.TRACK+" ASC";
		    	//String sortClause = null;
		    	Log.i("SONGSEARCH", whereClause);
		    	return contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
		    						SONG_COLS, 		// we should minimize the number of columns
		    						whereClause,	// all songs from a certain album
		    						null,   		// parameters to the previous parameter - which is null also 
		    						sortClause		// sort order, SQLite-like
		    						);
		    		
			}
    	}catch(Exception e){
    		e.printStackTrace();
    		return songCursor;
    	}
//    	}
    }
    public void	initializeArtistCursor(){
    	
    }
    
    public void initializeAlbumCursorFromPlaylist(){
//    	Cursor	playlistCursor;
//    	playlistCursor = contentResolver.query(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI,
//    											PLAYLIST_COLS,
//    											null,
//    											null,
//    											MediaStore.Audio.Playlists.DEFAULT_SORT_ORDER);
//    	while(playlistCursor.moveToNext()){
//    		Log.i("PLAYLIST", MediaStore.Audio.Playlists.DATA + playlistCursor.getString(playlistCursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.DATA)) +
//    				MediaStore.Audio.Playlists.DATE_ADDED + playlistCursor.getString(playlistCursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.DATE_ADDED))+
//    				MediaStore.Audio.Playlists.DATE_MODIFIED + playlistCursor.getString(playlistCursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.DATE_MODIFIED))+
//    				MediaStore.Audio.Playlists.NAME + playlistCursor.getString(playlistCursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.NAME)));
//    		
//    	}
    }

    Handler	invalidateCurrentSongLayout = new Handler(){
    	public void handleMessage(Message msg){
    		if(currentPlayingSongContainer != null)
    			currentPlayingSongContainer.invalidate();
    	}
    };
    
    Handler	invalidateCurrentPlayingImageView = new Handler(){
    	public void handleMessage(Message msg){
    		if(currentPlayingLayout != null)
    			currentPlayingLayout.invalidate();
    	}
    };
    
    /*************************************
     * 
     * playPauseClickListenerHelper
     * 
     *************************************/
    public void playPauseClickListenerHelper(){
		try {
			// TODO: UI related updates----
			//   these are just mockups
			if(playerServiceIface.isPlaying()){
				playerServiceIface.pause();
//				AlphaAnimation albumPlayingFadeOut = new AlphaAnimation((float)1.0, (float)0.66);
//				albumPlayingFadeOut.setFillAfter(true);
//				albumPlayingFadeOut.setDuration(200);
//				currentAlbumPlayingLayout.startAnimation(albumPlayingFadeOut);
				//playPauseImage.setImageResource(android.R.drawable.ic_media_play);
				TransitionDrawable playPauseTDrawable = (TransitionDrawable) playPauseImage.getDrawable();
				playPauseTDrawable.setCrossFadeEnabled(true);
				playPauseTDrawable.reverseTransition(300);
				playPauseTDrawable.invalidateSelf();
				invalidateCurrentSongLayout.sendEmptyMessageDelayed(0, 50);
				invalidateCurrentSongLayout.sendEmptyMessageDelayed(0, 100);
				invalidateCurrentSongLayout.sendEmptyMessageDelayed(0, 150);
				invalidateCurrentSongLayout.sendEmptyMessageDelayed(0, 200);
				invalidateCurrentSongLayout.sendEmptyMessageDelayed(0, 250);
//				TransitionDrawable playPauseBgTDrawable = (TransitionDrawable) playPauseImage.getBackground();
//				playPauseBgTDrawable.startTransition(500);
//				playPauseBgTDrawable.invalidateSelf();
//				playPauseOverlayHandler.sendEmptyMessageDelayed(0, 500);
//				currentAlbumPlayingLayout.invalidate();
				//TODO: reverse transition with an handler
//				
//				playPauseImageOverlay.setVisibility(View.VISIBLE);
//				AlphaAnimation playPauseImageOverlayFadeIn = new AlphaAnimation(0.0f, 1.0f);
//				playPauseImageOverlayFadeIn.setFillAfter(true);
//				playPauseImageOverlayFadeIn.setDuration(200);
//				playPauseImageOverlayFadeIn.setAnimationListener(playPauseOverlayFadeInAnimationListener);
//				playPauseImageOverlay.startAnimation(playPauseImageOverlayFadeIn);
				//currentAlbumPlayingLayout.setBackgroundColor(Color.argb(128, 255, 255, 255));
				if(songProgressTimer != null)
					songProgressTimer.cancel();
			} else {
				//playerServiceIface.resume(); - use a delayed timer to not interfere with the button animations
				Message msg = new Message();
				msg.what = 0;
				playerServiceResumeHandler.sendMessageDelayed(new Message(), 900);
//				AlphaAnimation albumPlayingFadeIn = new AlphaAnimation((float)0.66, (float)1.0);
//				albumPlayingFadeIn.setFillAfter(true);
//				albumPlayingFadeIn.setDuration(200);
//				currentAlbumPlayingLayout.startAnimation(albumPlayingFadeIn);
				//playPauseImage.setImageResource(android.R.drawable.ic_media_pause);
				TransitionDrawable playPauseTDrawable = (TransitionDrawable) playPauseImage.getDrawable();
				playPauseTDrawable.setCrossFadeEnabled(true);
				playPauseTDrawable.startTransition(500);
				playPauseTDrawable.invalidateSelf();
				invalidateCurrentSongLayout.sendEmptyMessageDelayed(0, 150);
				invalidateCurrentSongLayout.sendEmptyMessageDelayed(0, 300);
				invalidateCurrentSongLayout.sendEmptyMessageDelayed(0, 450);
				invalidateCurrentSongLayout.sendEmptyMessageDelayed(0, 600);
				invalidateCurrentSongLayout.sendEmptyMessageDelayed(0, 750);
//				TransitionDrawable playPauseBgTDrawable = (TransitionDrawable) playPauseImage.getBackground();
//				playPauseBgTDrawable.startTransition(500);
//				playPauseBgTDrawable.invalidateSelf();
//				playPauseOverlayHandler.sendEmptyMessageDelayed(0, 500);
//				currentAlbumPlayingLayout.invalidate();
				//TODO: reverse transition with an handler
//				
//				playPauseImageOverlay.setVisibility(View.VISIBLE);
//				AlphaAnimation playPauseImageOverlayFadeIn = new AlphaAnimation(0.0f, 1.0f);
//				playPauseImageOverlayFadeIn.setFillAfter(true);
//				playPauseImageOverlayFadeIn.setDuration(250);
//				playPauseImageOverlayFadeIn.setAnimationListener(playPauseOverlayFadeInAnimationListener);
//				playPauseImageOverlay.startAnimation(playPauseImageOverlayFadeIn);
				//currentAlbumPlayingLayout.setBackgroundColor(Color.argb(0, 255, 255, 255));
				//Log.i("RES", "1");
//				songProgressTimer = new Timer();
//				Log.i("RES", "7");
//				songProgressTimer.scheduleAtFixedRate(new SongProgressTimerTask(), 100, 1000);
//				Log.i("RES", "8");
				updateSongTextUI(); // starts the progress timer
				//triggerSongProgress();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    /**********************************
     * 
     * NextSongClickListenerHelper
     * 
     **********************************/
    public void nextSongClickListenerHelper(){
		try {
		/* In case the album is paused */
		//AlphaAnimation	fadeIn = new AlphaAnimation((float)1.0,(float)1.0);
		//fadeIn.setFillAfter(true);
		//fadeIn.setDuration(1);
		//currentAlbumPlayingLayout.startAnimation(fadeIn);
		
		/* Play next song */
		playerServiceIface.playNext();
	} catch (RemoteException e) {
		e.printStackTrace();
	}	
    }
    
    /**************************************
     * 
     * TOUCH/CLICK/COMPLETION LISTENERS
     * 
     **************************************/
    OnClickListener currentAlbumClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			try{
				showSongList();
				//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			} catch (Exception e){
				e.printStackTrace();
			}
		}
    };
        
    AdapterView.OnItemLongClickListener albumNavigatorListItemLongClickListener = new OnItemLongClickListener(){

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View arg1,
				int position, long arg3) {
			Log.i("LONGCLICK", "NAVIGATOR");
			
    		/*
    		 * Get albumCursor
    		 */
    		Cursor albumCursorTmp = ((AlbumCursorAdapter) parent.getAdapter()).getCursor();
			
    		/*
    		 * Save item chosen index
    		 */
    		albumNavigatorItemLongClickIndex = position;
    		
    		/*
    		 * Set Options adapter
    		 */
    		ArrayAdapter optionsAdapter = new ArrayAdapter(
    				getApplicationContext(),
    				android.R.layout.select_dialog_item,
    				android.R.id.text1,
    				getResources().getStringArray(R.array.album_options));
    		
    		
			AlertDialog.Builder aD = new AlertDialog.Builder(context);
			
			aD.create();
			aD.setTitle(
					albumCursorTmp.getString(albumCursorTmp.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST))
					+"\n"+
					albumCursorTmp.getString(albumCursorTmp.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM)));
			aD.setAdapter(optionsAdapter, albumOptionsDialogClickListener);
			aD.show();
			
			return true;
		}
    
    };
        
    DialogInterface.OnClickListener albumOptionsDialogClickListener = new DialogInterface.OnClickListener(){

		@Override
		public void onClick(DialogInterface dialog, int which) {
			
			AlertDialog.Builder aD = new AlertDialog.Builder(context);
			
			aD.create();
			albumCursor.moveToPosition(albumNavigatorItemLongClickIndex);
			aD.setTitle(
					albumCursor.getString(albumCursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST))
					+"\n"+
					albumCursor.getString(albumCursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM)));
			
			/*
			 * Show Song List
			 */
			if(which == 0){
				
				Cursor songCursorTmp = initializeSongCursor(
						albumCursor.getString(albumCursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM)));
				
				/*
				 * populate song list
				 */		
				//Log.i("POPULATE", "the song list");
				String[] fieldsFrom = new String[1];
				int[] fieldsTo		= new int[1];
				fieldsFrom[0] = MediaStore.Audio.Media.TITLE;
				fieldsTo[0] = R.id.songlist_item_song_name;
				SongCursorAdapter	songAdapter = new SongCursorAdapter(
														getApplicationContext(),
														R.layout.songlist_dialog_item,
														songCursorTmp,
														fieldsFrom,
														fieldsTo);
				aD.setAdapter(songAdapter, songListDialogClickListener);
					
			} 
			/*
			 * Change Album Art
			 */
			else if(which == 1) {
				//aD.setAdapter(optionsAdapter, albumOptionsDialogClickListener);
				try{
					
					/*
					 * Inflate View to show on dialog (image + next/previous buttons)
					 */
					albumArtChooserLayout = ((LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE))
						.inflate(R.layout.album_art_change_dialog, null);
					lastFmAlbumArtImporterDialog = new LastFmAlbumArtImporter(context);
					
					/*
					 * Preload image with the current used image (if it does not exist - try the embedded
					 */
					String albumCoverPath = lastFmAlbumArtImporterDialog.checkAlbumArtPathCustom(
							albumCursor.getString(albumCursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST)),
							albumCursor.getString(albumCursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM)));
					if(albumCoverPath == null){
						albumCoverPath = lastFmAlbumArtImporterDialog.checkAlbumArtEmbeddedSize(
								albumCursor.getString(albumCursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_ART)));
						albumArtSearchDialogIndex = -1;
					} else {
						albumArtSearchDialogIndex = -2;
					}
					
					Options opts = new BitmapFactory.Options();
					Bitmap albumCoverBitmap = BitmapFactory.decodeFile(albumCoverPath, opts);
					((ImageView)albumArtChooserLayout.findViewById(R.id.album_art)).setImageBitmap(albumCoverBitmap);
					((TextView)albumArtChooserLayout.findViewById(R.id.art_res)).setText(
							opts.outWidth +
							"x"+
							opts.outHeight);					
					
					/*
					 * Set Dialog Interaction Listeners
					 */
					((ImageButton)albumArtChooserLayout.findViewById(R.id.next_art))
						.setOnClickListener(nextArtDialogClickListener);
					((ImageButton)albumArtChooserLayout.findViewById(R.id.previous_art))
						.setOnClickListener(previousArtDialogClickListener);

					aD.setPositiveButton("Set", albumArtChooserPositiveListener);			
					aD.setNegativeButton("Cancel", albumArtChooserNegativeListener);			
					aD.setOnCancelListener(albumArtChooserCancelListener);
					
					/*
					 * Set Dialog view
					 */
					aD.setView(albumArtChooserLayout);
					
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
			
			aD.show();	
			
		}
    	
    };
    
    View albumArtChooserLayout = null;
    LastFmAlbumArtImporter lastFmAlbumArtImporterDialog = null;
    int	albumArtSearchDialogIndex = -2;
    GetImageThread getImageThread = null;
    Bitmap	newAlbumArt;
    //HttpEntity	hE = null;
    
    DialogInterface.OnClickListener albumArtChooserPositiveListener = new DialogInterface.OnClickListener(){
		@Override
		public void onClick(DialogInterface dialog, int which) {
			/*
			 * Set Album Art 
			 */
			try{
				/*
				 * create files
				 */
				albumCursor.moveToPosition(albumNavigatorItemLongClickIndex);
				lastFmAlbumArtImporterDialog.createAlbumArt(
						albumCursor.getString(albumCursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST)),
						albumCursor.getString(albumCursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM)),
						newAlbumArt);
				lastFmAlbumArtImporterDialog.createSmallAlbumArt(
						albumCursor.getString(albumCursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST)),
						albumCursor.getString(albumCursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM)),
						true);
				
				/*
				 * reload preloaded images
				 */
				albumAdapter.albumImages[albumNavigatorItemLongClickIndex] = 
					albumAdapter.getAlbumBitmap(albumNavigatorItemLongClickIndex, BITMAP_SIZE_SMALL);
			}catch(Exception e){
				e.printStackTrace();
			}
			
			/*
			 * Clear global vars
			 */
			albumArtChooserCleanVars();
		}
    };
    DialogInterface.OnClickListener albumArtChooserNegativeListener = new DialogInterface.OnClickListener(){
		@Override
		public void onClick(DialogInterface dialog, int which) {
			/*
			 * Clear global vars
			 */
			albumArtChooserCleanVars();
		}
    };
    DialogInterface.OnCancelListener albumArtChooserCancelListener = new DialogInterface.OnCancelListener(){
		@Override
		public void onCancel(DialogInterface dialog) {
			/*
			 * Clear global vars
			 */
			albumArtChooserCleanVars();
		}
    };
    public void albumArtChooserCleanVars(){
    	try{
    		albumArtChooserLayout = null;
    		lastFmAlbumArtImporterDialog = null;
    		albumArtSearchDialogIndex = -2;
    		newAlbumArt.recycle();
    		newAlbumArt = null;
    		if(getImageThread != null){
    			getImageThread.interrupt();
    			getImageThread = null;
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    OnClickListener nextArtDialogClickListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			((ImageView) albumArtChooserLayout.findViewById(R.id.album_art))
				.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.albumart_mp_unknown));
			((TextView) albumArtChooserLayout.findViewById(R.id.art_res))
				.setText("Loading...");
			if(albumArtSearchDialogIndex > 19)
				albumArtSearchDialogIndex = 0;
			else
				albumArtSearchDialogIndex ++;
			getArtDialogAction();
		}
    };
    OnClickListener previousArtDialogClickListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			((ImageView) albumArtChooserLayout.findViewById(R.id.album_art))
				.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.albumart_mp_unknown));
			((TextView) albumArtChooserLayout.findViewById(R.id.art_res))
				.setText("Loading...");
			if(albumArtSearchDialogIndex <= 0)
				albumArtSearchDialogIndex = 19;
			else
				albumArtSearchDialogIndex --;
			getArtDialogAction();
		}
    };
    public void getArtDialogAction(){
    	if(getImageThread != null){
			getImageThread.interrupt();
			getImageThread.stop();
		}
		getImageThread = new GetImageThread();
		
		albumCursor.moveToPosition(albumNavigatorItemLongClickIndex);
		getImageThread.albumArtPath = albumCursor.getString(
				albumCursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_ART));
		getImageThread.artistName = albumCursor.getString(
				albumCursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST));
		getImageThread.albumName = albumCursor.getString(
				albumCursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM));
		
		getImageThread.start();
    }

	public class GetImageThread extends Thread {
		// TODO: Needs to be assigned
		public String artistName = null;
		public String albumName = null;
		public String albumArtPath = null;

		@Override
		public void run(){
			try{
				Bitmap bm = null;
				albumCursor.moveToPosition(albumNavigatorItemLongClickIndex);			
				
				if(albumArtSearchDialogIndex == -1){
					/*
					 * Open embedded art
					 */
					String albumCoverPath = lastFmAlbumArtImporterDialog.checkAlbumArtEmbeddedSize(albumArtPath);
					if(albumCoverPath != null)
						bm = BitmapFactory.decodeFile(albumCoverPath);
					else
						albumArtSearchDialogIndex++;
				}
				if(albumArtSearchDialogIndex >= 0){
					// get Google Search result index albumArtSearchDialogIndex
					HttpEntity	hE = lastFmAlbumArtImporterDialog.getGoogleSearchResponse(artistName, albumName);
					BufferedReader bR = new BufferedReader(
						new InputStreamReader(hE.getContent()));
					String artUrl = lastFmAlbumArtImporterDialog
						.parseGoogleSearchResponse(bR, albumArtSearchDialogIndex);
					/*
					 * open Bitmap URL	
					 */
					BasicHttpParams params = new BasicHttpParams();
					HttpConnectionParams.setConnectionTimeout(params, 10000);
					HttpConnectionParams.setSoTimeout(params, 10000);
					DefaultHttpClient httpClient = new DefaultHttpClient(params);
			        HttpGet httpGet = new HttpGet(artUrl);
			        InputStream albumArtURLStream = null;
			        HttpResponse response = httpClient.execute(httpGet);
			        HttpEntity entity = response.getEntity();
			        albumArtURLStream = entity.getContent();
			        bm = BitmapFactory.decodeStream(albumArtURLStream);
			        
			        hE.consumeContent();
			        entity.consumeContent();
				}
				Message msg = new Message();
				msg.obj = bm;
				albumArtChooserHandler.sendMessage(msg);
			}catch(Exception e){
				e.printStackTrace();
				albumArtChooserHandler.sendMessage(new Message());
			}
		}
		
		@Override
		public void interrupt(){
			try {
				this.finalize();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}
    
	Handler albumArtChooserHandler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			try{
				newAlbumArt = Bitmap.createBitmap((Bitmap)msg.obj);
			
				((ImageView)albumArtChooserLayout.findViewById(R.id.album_art)).setImageBitmap(newAlbumArt);
				((TextView)albumArtChooserLayout.findViewById(R.id.art_res)).setText(
						newAlbumArt.getWidth() +
						"x"+
						newAlbumArt.getHeight());
			} catch(Exception e) {
				e.printStackTrace();
				try{
					((TextView)albumArtChooserLayout.findViewById(R.id.art_res)).setText(
						"Download failed!");
				}catch(Exception ee){
					ee.printStackTrace();
				}
				
			}
		}
	};
	
    AdapterView.OnItemClickListener albumNavigatorListItemClickListener = new OnItemClickListener(){
    	
    	@Override
    	public void	onItemClick(AdapterView<?> parent, View view, int position, long id){
    		/*
    		 * Check if it is not the current Album
    		 */
    		// TODO
    		
    		/* set icon to pause */
    		try {
				if(!playerServiceIface.isPlaying()){
					TransitionDrawable playPauseTDrawable = (TransitionDrawable) playPauseImage.getDrawable();
					playPauseTDrawable.setCrossFadeEnabled(true);
					playPauseTDrawable.startTransition(1);
					playPauseTDrawable.invalidateSelf();
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
    		
    		/*
    		 * Stop current song
    		 */
    		try {
				if(playerServiceIface.isPlaying()){
					playerServiceIface.stop();
				}
				songProgressHandler.sendEmptyMessage(0);
				if(songProgressTimer != null)
					songProgressTimer.cancel();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
    					
    		/*
    		 * Save albumCursor
    		 */
    		albumCursor = ((AlbumCursorAdapter) parent.getAdapter()).getCursor();

    		/*
    		 * Hide the Current Album
    		 *  - the listener of the animation will change the art
    		 *  	and go to the next song
    		 */
    		currentAlbumPlayingLayoutOuter.startAnimation(fadeAlbumOut);
    		invalidateCurrentPlayingImageView.sendEmptyMessageDelayed(0, 50);
    		invalidateCurrentPlayingImageView.sendEmptyMessageDelayed(0, 100);
    		invalidateCurrentPlayingImageView.sendEmptyMessageDelayed(0, 150);
    		invalidateCurrentPlayingImageView.sendEmptyMessageDelayed(0, 200);
    		invalidateCurrentPlayingImageView.sendEmptyMessageDelayed(0, 250);
    		invalidateCurrentPlayingImageView.sendEmptyMessageDelayed(0, 300);
    		invalidateCurrentPlayingImageView.sendEmptyMessageDelayed(0, 350);
    	}
    		
    };

    DialogInterface.OnClickListener	songListDialogClickListener = new DialogInterface.OnClickListener(){

		@Override
		public void onClick(DialogInterface dialog, int position) {
//			// TODO Auto-generated method stub
//			
//		}
//		@Override
//    	public void	onItemClick(AdapterView<?> parent, View view, int position, long id){
			Log.i("LIST", "SONG CLICK");
			
			/* 
			 * Check song position 
			 */
			albumCursor.moveToPosition(albumNavigatorItemLongClickIndex);
			songCursor = initializeSongCursor(albumCursor.getString(albumCursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM)));
			songCursor.moveToPosition(position);
			    		
			/*
			 * UpdateUI
			 */
			try{
				((RockPlayer) context).songProgressBar.setProgress(0);
				((RockPlayer) context).songProgressBar.setMax((int) songCursor.getDouble(
																songCursor.getColumnIndexOrThrow(
																		MediaStore.Audio.Media.DURATION)));
			}catch(Exception e){
				e.printStackTrace();
			}
				
			/* In case the player is paused */
			try {
				if(!playerServiceIface.isPlaying()){
					TransitionDrawable playPauseTDrawable = (TransitionDrawable) playPauseImage.getDrawable();
					playPauseTDrawable.setCrossFadeEnabled(true);
					playPauseTDrawable.startTransition(1);
					playPauseTDrawable.invalidateSelf();
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			// TODO: use slide down animation 
			// Might not be necessary
			//((Filex) context).hideSongListUI();
			//((Filex) context).showMainUI();
			
			/*
			 * Play song
			 */
			try
			{
//				((Filex) context).playerServiceIface.play(((Filex) context).albumCursorPositionPlaying,
//						songCursor.getPosition());	
				playerServiceIface.play(albumNavigatorItemLongClickIndex,
								position);
				
				/*
				 * Update some vars
				 */
				albumCursorPositionPlaying = albumNavigatorItemLongClickIndex;
			} catch (RemoteException e) {
				e.printStackTrace();
			}
    		
			/*
			 * Some more UI
			 */
			updateSongTextUI();
			
			/*
			 * Start tracking song progress
			 */
			triggerSongProgress();

			
		}
    };

    AdapterView.OnItemClickListener songSearchTextItemClickListener = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
			
			try{
				TextView songName = (TextView) view.findViewById(R.id.text1);
				songSearchTextView.setText(songName.getText());
				
				/*
				 * Get the album name
				 */
				String whereClause = MediaStore.Audio.Media.TITLE + " = \"" + 
					songName.getText().toString().replaceAll("\"", "\"\"") +
					"\"";
				Log.i("DBG", (String) songName.getText());
				Cursor songSearchCursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
							SONG_COLS, // we should minimize the number of columns
							whereClause,	// all songs
							null,   // parameters to the previous parameter - which is null also 
							null	// sort order, SQLite-like
							);
				songSearchCursor.moveToFirst();
				String albumName = songSearchCursor.getString(
						songSearchCursor.getColumnIndexOrThrow(
								MediaStore.Audio.Media.ALBUM));
				Log.i("DBG", albumName);
				
				/*			
				 * Get the album index
				 */
		    	String sortClause = MediaStore.Audio.Albums.ARTIST+" ASC";
				songSearchCursor = contentResolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
						ALBUM_COLS, // we should minimize the number of columns
						null,	// all songs
						null,   // parameters to the previous parameter - which is null also 
						sortClause	// sort order, SQLite-like
						);
				int albumIndex = 0;
				while(songSearchCursor.moveToNext()){
					if(albumName.equals(songSearchCursor.getString(
										songSearchCursor.getColumnIndexOrThrow(
											MediaStore.Audio.Albums.ALBUM)))){
						albumIndex = songSearchCursor.getPosition();
						break;
					}
				}
			
				/*
				 * Get the song index
				 */
				int songIndex = 0;
				//String songPath = null;;
				whereClause = MediaStore.Audio.Media.ALBUM+"=\""+
					albumName.replaceAll("\"", "\"\"")+
					"\"";
				songSearchCursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
						SONG_COLS, // we should minimize the number of columns
						whereClause,	// all songs
						null,   // parameters to the previous parameter - which is null also 
						null	// sort order, SQLite-like
						);
				while(songSearchCursor.moveToNext()){
					//Log.i("DBG", songName.getText().toString() + "=?" + songCursor.getString(songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));
					if(songName.getText().toString().equals(songSearchCursor.getString(
																songSearchCursor.getColumnIndexOrThrow(
																	MediaStore.Audio.Media.TITLE)))){
						songIndex = songSearchCursor.getPosition();
						//songPath = songCursor.getString(songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
						//Log.i("DBG", "Song found "+ songPath);
						break;
					}
				}
				
				/*
				 * hideSearchUI
				 */
				hideSongSearch();
				showMainUI();
				songSearchTextView.setText("");
				
				/*
				 * Update Cursor and Start Album Animation
				 */
				albumCursor.moveToPosition(albumIndex);
				albumCursorPositionPlaying = albumIndex;
	
				songCursor = initializeSongCursor(albumName);
				songCursor.moveToPosition(songIndex);
	
				songProgressBar.setProgress(0);
				songProgressBar.setMax((int) songCursor.getDouble(
													songCursor.getColumnIndexOrThrow(
															MediaStore.Audio.Media.DURATION)));
				
				calledByService = true;
	    		currentAlbumPlayingLayoutOuter.startAnimation(fadeAlbumOut);
	    		invalidateCurrentPlayingImageView.sendEmptyMessageDelayed(0, 50);
	    		invalidateCurrentPlayingImageView.sendEmptyMessageDelayed(0, 100);
	    		invalidateCurrentPlayingImageView.sendEmptyMessageDelayed(0, 150);
	    		invalidateCurrentPlayingImageView.sendEmptyMessageDelayed(0, 200);
	    		invalidateCurrentPlayingImageView.sendEmptyMessageDelayed(0, 250);
	    		invalidateCurrentPlayingImageView.sendEmptyMessageDelayed(0, 300);
	    		invalidateCurrentPlayingImageView.sendEmptyMessageDelayed(0, 350);
				
				/*
				 * Play Song
				 */
				try {
					playerServiceIface.play(albumIndex, songIndex);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				
				/*
				 * Change Album and song text
				 */
				// It should be the service triggering this stuff
				/********** TODO ******************/
				
				//albumChangedIntentReceiver.onReceive(context, null);
				//musicChangedIntentReceiver.onReceive(context, null);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
    };
    
    DialogInterface.OnClickListener playlistDialogClickListener = new DialogInterface.OnClickListener(){
		@Override
		public void onClick(DialogInterface dialog, int which) {
			try{
				String sortOrder = MediaStore.Audio.Playlists.NAME+" ASC";
	        	Cursor playlistAllCursor = contentResolver.query(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI,
	        														PLAYLIST_COLS, 
	        														null, 
	        														null, 
	        														sortOrder);
	    		
	        	/*
	        	 * Create Array with custom playlist + system playlists
	        	 */
	        	Playlist playlistTemp;
	        	ArrayList<Playlist> playlistArray = new ArrayList<Playlist>();
	        	/* ALL Playlist*/
	        	playlistTemp = new Playlist();
	        	playlistTemp.id = constants.PLAYLIST_ALL;
	        	playlistTemp.name = "All songs";
	        	playlistArray.add(playlistTemp);
	        	/* Recently Added */ 
	        	playlistTemp = new Playlist();
	        	playlistTemp.id = constants.PLAYLIST_RECENT;
	        	playlistTemp.name = "Recently Added";
	        	playlistArray.add(playlistTemp);
	        	/* ... other system playlists ... */
	        	
	        	/* add every playlist in the media store */
	        	while(playlistAllCursor.moveToNext()){
	        		playlistTemp = new Playlist();
	        		playlistTemp.id = playlistAllCursor.getLong(playlistAllCursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists._ID));
	        		playlistTemp.name = playlistAllCursor.getString(playlistAllCursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.NAME));
	        		playlistArray.add(playlistTemp);
	        		Log.i("PLAYLIST MENU", playlistTemp.id+" "+playlistTemp.name);
	        	}	
	        	/* INIT END */
	        	
	        	
	        	
	        	long playlistId = playlistArray.get(which).id;
				
				/*
				 * Save the playlist as the preferred one
				 */
	        	RockOnPreferenceManager prefs = new RockOnPreferenceManager(FILEX_PREFERENCES_PATH);
	        	prefs.putLong(constants.PREF_KEY_PLAYLIST, playlistId);
	//			SharedPreferences prefs = getSharedPreferences(PREFS_NAME, 0);
	//			prefs.edit().putLong(constants.PREF_KEY_PLAYLIST, playlistId);
	//			prefs.edit().commit();
				
				/*
				 * Reinitialize the album and song cursors
				 */
				playlist = playlistId;
				initializeAlbumCursor();
				albumCursor.moveToFirst();
				songCursor = initializeSongCursor(albumCursor.getString(
										albumCursor.getColumnIndexOrThrow(
												MediaStore.Audio.Albums.ALBUM)));
				
				/*
				 * Reload Navigator List
				 */
				recycleNavigatorList();
				albumAdapter = null;
				getAlbums(true);
	
				/*
				 * Back to main UI
				 */
	//			hidePlaylistUI();
	//			showMainUI();
				
				
	//			String[] fieldsFrom = new String[1];
	//			int[] fieldsTo		= new int[1];
	//			fieldsFrom[0] = MediaStore.Audio.Albums.ALBUM_ART;
	//			fieldsTo[0] = R.id.navigator_albumart_image;				
	//			albumAdapter = new AlbumCursorAdapter(getApplicationContext(),
	//													R.layout.navigator_item, 
	//													albumCursor, 
	//													fieldsFrom, 
	//													fieldsTo);
	//			
				
				/*
				 * Sync with playerService
				 */
	//			playerServiceIface.setRecentPeriod(getSharedPreferences(PREFS_NAME, 0)
				playerServiceIface.setRecentPeriod(prefs
		    							.getInt(constants.PREF_KEY_RECENT_PERIOD, constants.RECENT_PERIOD_DEFAULT_IN_DAYS));
				playerServiceIface.setPlaylist(playlist);
				//playerServiceIface.reloadCursors();
				
	
				/*
				 * Play First Song??
				 */
				
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
    };
    
    
    OnScrollListener albumNavigatorScrollListener = new OnScrollListener(){

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if(scrollState == OnScrollListener.SCROLL_STATE_IDLE){
				System.gc();
				
				albumAdapter.isScrolling = false;
				albumListIsScrolling = false;

				if(true || !showArtWhileScrolling){
					int i = view.getFirstVisiblePosition();
					int count = view.getChildCount();
					for(int j = 0; j < count; j++){
						View v = view.getChildAt(j);
	
						/*
						 * Reload ImageView
						 */
						ImageView albumImage = (ImageView)
							v.findViewById(R.id.navigator_albumart_image);
						if(albumImage != null){
							albumImage.setImageBitmap(albumAdapter.getAlbumBitmap(j+i, BITMAP_SIZE_SMALL));
						}
												
						/*
						 * Hide (or show) Text Hint
						 */
						TextView albumText = (TextView)
							v.findViewById(R.id.navigator_albumart_alternative);
						if(albumAdapter.albumCoverPath != null){
							Log.i("SCROLL IDLE", "album cover exists");	
							if(albumText != null)
								albumText.setVisibility(View.GONE);
						} else {
							Log.i("SCROLL IDLE", "album cover FAIL");	
							if(albumText != null)
								albumText.setVisibility(View.VISIBLE);
						}
	
					}
				}
				if(true)
					return;
			}
			if(scrollState == OnScrollListener.SCROLL_STATE_FLING){
				albumAdapter.isScrolling = true;
				albumListIsScrolling = true;
			}
			if(scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
				albumAdapter.isScrolling = true;
				albumListIsScrolling = true;
				if(albumListSelectedAlbumTimer != null)
					albumListSelectedAlbumTimer.cancel();
				if(true)
					return;
			}
		}
		
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {}
    	
    };
    
	OnCompletionListener songCompletedListener = new OnCompletionListener(){

		@Override
		public void onCompletion(MediaPlayer mp) {
			if(!SHUFFLE){
				if(!songCursor.isLast()){
					songCursor.moveToNext();
		    		String songPath = songCursor.getString(
										songCursor.getColumnIndexOrThrow(
												MediaStore.Audio.Media.DATA));
		    		try {
		    			mediaPlayer.reset();
						mediaPlayer.setDataSource(songPath);
			    		mediaPlayer.prepare();
			    		mediaPlayer.start();
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
				} else {
					// TODO: needs to get next album
					//			and change the album cover
				}
			} else {
				//TODO: Implement Shuffle
			}
		}
	};
	
	double lastPlayPauseClickTimestamp = 0;
	OnClickListener songNameClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			try{
				if(System.currentTimeMillis() - lastPlayPauseClickTimestamp > 
					new Constants().DOUBLE_CLICK_INTERVAL){
//					if(!playerServiceIface.isPlaying())
						playPauseHandler.sendEmptyMessageDelayed(0, 10);
//					else
//						playPauseHandler.sendEmptyMessageDelayed(0, (long) new Constants().DOUBLE_CLICK_INTERVAL + 50);
				} else {
//					playPauseHandler.removeMessages(0);
//					if(!playerServiceIface.isPlaying()){
//						TransitionDrawable playPauseTDrawable = (TransitionDrawable) playPauseImage.getDrawable();
//						playPauseTDrawable.setCrossFadeEnabled(true);
//						playPauseTDrawable.startTransition(500);
//						playPauseTDrawable.invalidateSelf();
//						
//						invalidateCurrentSongLayout.sendEmptyMessageDelayed(0, 150);
//						invalidateCurrentSongLayout.sendEmptyMessageDelayed(0, 300);
//						invalidateCurrentSongLayout.sendEmptyMessageDelayed(0, 450);
//						invalidateCurrentSongLayout.sendEmptyMessageDelayed(0, 600);
//						invalidateCurrentSongLayout.sendEmptyMessageDelayed(0, 750);
//						
//						triggerSongProgress();
//					}
//
//					nextSongClickListenerHelper();
				}
				lastPlayPauseClickTimestamp = System.currentTimeMillis();
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		
	};
	
	Handler playPauseHandler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			playPauseClickListenerHelper();
		}
	};
		
	OnLongClickListener songNameLongClickListener = new OnLongClickListener(){
		@Override
		public boolean onLongClick(View v) {
			showSongProgressDialog();
			return true;
		}
	};
	
	OnClickListener	artistNameClickListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			Log.i("WebV","0");
			if (true)
				return;

			Log.i("WebV","2");
			webView.setWebViewClient(
	        		new WebViewClient() {
	        			@Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
	        				return false;
	        			}
	        		});
			Log.i("WebV","3");
			String LAST_FM_BASE_URL = "http://www.last.fm/music/";
			webView.loadUrl(LAST_FM_BASE_URL+URLEncoder.encode((String) artistNameText.getText()));
			Log.i("WebV","4");
			
			System.gc();
			showWebUI();
			hideMainUI();
			containerLayout.bringChildToFront(webView);			
			Log.i("WebV","5");
		}
		
	};
	
	OnClickListener eventListIncreaseRadiusClickListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			/*
			 * New Radius
			 */
			EditText radiusEditText = ((RockPlayer) context).eventListRadius;
			double radius = Double.valueOf(radiusEditText.getText().toString()) + ((RockPlayer) context).CONCERT_RADIUS_INCREMENT/1000;
			radiusEditText.setText(String.valueOf(radius));
			// TODO: set radius SharedPreference
			/*
			 * Set new radius preference
			 */
//			SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
//			Editor	settingsEdit = settings.edit();
//			settingsEdit.putLong("ConcertRadius", (long) radius * 1000);
//			settingsEdit.commit();
			RockOnPreferenceManager prefs = new RockOnPreferenceManager(FILEX_PREFERENCES_PATH);
			prefs.putLong("ConcertRadius", (long) radius * 1000);
			
			/*
			 * Schedule new concert search
			 */
			if(reloadEventListTimer != null)
				reloadEventListTimer.cancel();
			reloadEventListTimer = new Timer();
			reloadEventListTimer.schedule(new ReloadEventListTimerTask(), 2000);
		}
	};	
	
	OnClickListener eventListDecreaseRadiusClickListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			/*
			 * New Radius
			 */
			EditText radiusEditText = ((RockPlayer) context).eventListRadius;
			double radius = Double.valueOf(radiusEditText.getText().toString()) - ((RockPlayer) context).CONCERT_RADIUS_INCREMENT/1000;
			radiusEditText.setText(String.valueOf(radius));
			/*
			 * Set new radius preference
			 */
//			SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
//			Editor	settingsEdit = settings.edit();
//			settingsEdit.putLong("ConcertRadius", (long) radius * 1000);
//			settingsEdit.commit();
			RockOnPreferenceManager prefs = new RockOnPreferenceManager(FILEX_PREFERENCES_PATH);
			prefs.putLong("ConcertRadius", (long) radius * 1000);
			
			/*
			 * Schedule new concert search
			 */
			if(reloadEventListTimer != null)
				reloadEventListTimer.cancel();
			reloadEventListTimer = new Timer();
			reloadEventListTimer.schedule(new ReloadEventListTimerTask(), 2000);
		}
	};
	
	OnClickListener helpImageClickListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			hideHelpUI();
			showMainUI();
		}
	};
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent msg){
		try{
//			ViewGroup playlistContainer = (ViewGroup) findViewById(R.id.playlist_listview_container);
			
			switch (keyCode){
			case KeyEvent.KEYCODE_BACK:
					if(this.containerLayout == null){
						this.finish();
						return true;
					}
					if(this.songSearchContainer.getVisibility() == View.VISIBLE){
						this.hideSongSearch();
						this.showMainUI();
						return true;
					}
					if(this.eventListViewContainer.getVisibility() == View.VISIBLE){
						this.hideEventUI();
						this.showMainUI();
						return true;
					}
//					if(this.songListContainer.getVisibility() == View.VISIBLE){
//						songCursor.moveToPosition(((SongCursorAdapter) songListView.getAdapter()).songInitialPosition);
//						this.hideSongListUI();
//						this.showMainUI();
//						return true;
//					}
//					if(playlistContainer.getVisibility() == View.VISIBLE){
//						this.hidePlaylistUI();
//						//this.showMainUI(); // done onAnimationEnd
//						return true;
//					}
					if(this.helpView.getVisibility() == View.VISIBLE){
						this.hideHelpUI();
						this.showMainUI();
						return true;
					}
	//				if(this.webView.getVisibility() == View.VISIBLE){
	//					this.hideWebUI();
	//					this.showMainUI();
	//					return true;
	//				}
					this.finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
		
	double songTouchStartX 	= 0;
	double songTouchStartY 	= 0;
	double touchStartTime	= 0;
	double LONG_TOUCH_INTVL = 800;
	int MIN_MOV_SIZE 		= 50;
	boolean readSideMovement = true;
	
	OnTouchListener songTouchListener = new OnTouchListener(){
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			try{
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					songTouchStartX = event.getX();
					songTouchStartY = event.getY();
					touchStartTime = System.currentTimeMillis();
					readSideMovement = true;
					return true;
				}
				if(event.getAction() == MotionEvent.ACTION_MOVE){
					if(System.currentTimeMillis() - touchStartTime > LONG_TOUCH_INTVL){
						/*
						 * Check if movement is sideways
						 */
						double xDelta = event.getX() - songTouchStartX;
						if(Math.abs(xDelta) > MIN_MOV_SIZE){
							if(xDelta > 0){
								try {
									playPauseHandler.removeMessages(0);
									if(!playerServiceIface.isPlaying()){
										TransitionDrawable playPauseTDrawable = (TransitionDrawable) playPauseImage.getDrawable();
										playPauseTDrawable.setCrossFadeEnabled(true);
										playPauseTDrawable.startTransition(500);
										playPauseTDrawable.invalidateSelf();
										
										invalidateCurrentSongLayout.sendEmptyMessageDelayed(0, 50);
										invalidateCurrentSongLayout.sendEmptyMessageDelayed(0, 100);
										invalidateCurrentSongLayout.sendEmptyMessageDelayed(0, 150);
										invalidateCurrentSongLayout.sendEmptyMessageDelayed(0, 200);
										invalidateCurrentSongLayout.sendEmptyMessageDelayed(0, 250);
										
										triggerSongProgress();
									}

									nextSongClickListenerHelper();
									
									currentPlayingLayout.invalidate();
								} catch (RemoteException e) {
									e.printStackTrace();
								}
							}
						/*
						 * Movement is not sideways
						 */
						}else{
							readSideMovement = false;
							//showSongList();
							showSongProgressDialog();
						}
						return true;
					}				
				}
				if(event.getAction() == MotionEvent.ACTION_UP){
					if(!readSideMovement)
						return true;
					double xDelta = event.getX() - songTouchStartX;
					if(Math.abs(xDelta) > MIN_MOV_SIZE){
						if(xDelta > 0){
							try {
								playPauseHandler.removeMessages(0);
								if(!playerServiceIface.isPlaying()){
									TransitionDrawable playPauseTDrawable = (TransitionDrawable) playPauseImage.getDrawable();
									playPauseTDrawable.setCrossFadeEnabled(true);
									playPauseTDrawable.startTransition(500);
									playPauseTDrawable.invalidateSelf();
									
									invalidateCurrentSongLayout.sendEmptyMessageDelayed(0, 50);
									invalidateCurrentSongLayout.sendEmptyMessageDelayed(0, 100);
									invalidateCurrentSongLayout.sendEmptyMessageDelayed(0, 150);
									invalidateCurrentSongLayout.sendEmptyMessageDelayed(0, 200);
									invalidateCurrentSongLayout.sendEmptyMessageDelayed(0, 250);
									
									triggerSongProgress();
								}

								nextSongClickListenerHelper();
								
								currentPlayingLayout.invalidate();
								
							} catch (RemoteException e) {
								e.printStackTrace();
							}
						}
						return true;
					}
				}		
				return false;
			
			} catch (Exception e){
				e.printStackTrace();
				return false;
			}
		}
	};
	
	double mainUITouchStartX 		= 0;
	double mainUITouchStartY 		= 0;
	double mainUITouchStartTime		= 0;
	double MAIN_UI_MOVEMENT_INTVL 	= 1000;
	int MAIN_UI_MIN_MOV_SIZE 		= 100;
	boolean mainUIReadSideMovement = true;
	OnTouchListener mainUIContainerTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				mainUITouchStartX = event.getX();
				mainUITouchStartY = event.getY();
				mainUITouchStartTime = event.getEventTime();
			}
			if(event.getAction() == MotionEvent.ACTION_UP){
				if(event.getEventTime() - mainUITouchStartTime > MAIN_UI_MOVEMENT_INTVL)
					return false;
				if(Math.abs(event.getX() - mainUITouchStartX) < MAIN_UI_MIN_MOV_SIZE)
					return false;
				
				
				if(event.getX() - mainUITouchStartX < 0){
					
					if(VIEW_STATE == NORMAL_VIEW){
						setListExpandedView();
						return true;
					} else if(VIEW_STATE == LIST_EXPANDED_VIEW) {
						return false;
					} else if(VIEW_STATE == FULLSCREEN_VIEW){
						setNormalView();
						return true;
					}
					
				} else {					
					if(VIEW_STATE == NORMAL_VIEW){
						setFullScreenView();
						return true;
					} else if(VIEW_STATE == LIST_EXPANDED_VIEW) {
						setNormalView();
						return true;
					} else if(VIEW_STATE == FULLSCREEN_VIEW){
						return false;
					}
					

				}
			}
			return false;
		}
		
	};
	
	
	
	/*************************************
	 * 
	 * AnimationListeners
	 * 
	 *************************************/
	private AnimationListener slideRightAnimationListener = new AnimationListener(){
		@Override
		public void onAnimationEnd(Animation animation) {

			Rotate3dAnimation perspectiveBackLeft;
			
			if(display.getOrientation() == 0){
				perspectiveBackLeft = new Rotate3dAnimation(
						0,0, 	// X-axis rotation
						90,30,	// Y-axis rotation
						0,0,  	// Z-axis rotation
						100, 100, // rotation center
						0.0f, // Z-depth
						false); //reverse movement
			} else {
				perspectiveBackLeft = new Rotate3dAnimation(
						0,0, 	// X-axis rotation
						90,15,	// Y-axis rotation
						0,0,  	// Z-axis rotation
						100, 100, // rotation center
						0.0f, // Z-depth
						false); //reverse movement				
			}
	    	perspectiveBackLeft.setFillAfter(true);
	    	perspectiveBackLeft.setDuration(300);
			currentPlayingLayout.startAnimation(perspectiveBackLeft);
			
//			RelativeLayout.LayoutParams params = (LayoutParams) albumNavigatorLayoutOuter.getLayoutParams();
//			params.addRule(RelativeLayout.RIGHT_OF, R.id.songfest_current_playing_container);
//			albumNavigatorLayoutOuter.setLayoutParams(params);

		}
		@Override
		public void onAnimationRepeat(Animation animation) {
			
		}
		@Override
		public void onAnimationStart(Animation animation) {
			
		}
	};
	
	private AnimationListener hideAlbumAnimationListener = new AnimationListener(){

		@Override
		public void onAnimationEnd(Animation animation) {
    					
			/*
			 * Hide this right away, we will unhide before 
			 * start showing the next album
			 */
			//currentAlbumPlayingLayout.setVisibility(View.GONE);
			
			/* 
    		 * Get the ID of the new album
    		 */
			String artistName = null;
			String albumName = null;
			String albumArt = null;
			try {
				if(playerServiceIface.isPlaying())
					albumCursor.moveToPosition(playerServiceIface.getAlbumCursorPosition());
				artistName = albumCursor.getString(
										albumCursor.getColumnIndexOrThrow(
											MediaStore.Audio.Albums.ARTIST));
				albumName = albumCursor.getString(
										albumCursor.getColumnIndexOrThrow(
											MediaStore.Audio.Albums.ALBUM));
				albumArt = albumCursor.getString(
										albumCursor.getColumnIndexOrThrow(
											MediaStore.Audio.Albums.ALBUM_ART));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try{
				Bitmap albumArtBitmap;
				
				if(VIEW_STATE == FULLSCREEN_VIEW)
					albumArtBitmap = albumAdapter.getAlbumBitmap(albumCursor.getPosition(), BITMAP_SIZE_FULLSCREEN);
				else
					albumArtBitmap = albumAdapter.getAlbumBitmap(albumCursor.getPosition(), BITMAP_SIZE_NORMAL);
				
				if(showFrame)
					currentAlbumPlayingOverlayImageView.setVisibility(View.VISIBLE);
				else
					currentAlbumPlayingOverlayImageView.setVisibility(View.GONE);
	    		    	
	    		/* change album bitmap */
	    		currentAlbumPlayingImageView.setImageBitmap(albumArtBitmap);
	    		
	    		/* In case the album is paused */
	    		AlphaAnimation	fadeIn = new AlphaAnimation((float)1.0,(float)1.0);
	    		fadeIn.setFillAfter(true);
	    		fadeIn.setDuration(1);
	    		currentAlbumPlayingLayout.startAnimation(fadeIn);
	
	    		
	    		/*
	    		 * If this was called by service the song cursor was already initialized
	    		 * 	Probaly we should move this to the place where 
	    		 * 	the animation is called the first time
	    		 */
	    		if(!calledByService){
		    		/*
		    		 * Initialize the song cursor for the new album
		    		 */
		    		songCursor = initializeSongCursor(albumName);
		    		
		    		/*
		    		 * Get first song of the new album to play
		    		 */
		    		songCursor.moveToFirst();
	    		} else {
	    			triggerSongProgress();
	    		}
	    		
	    		/*
	    		 * Update UI components with song name and duration
	    		 */
	    		updateArtistTextUI();
	    		updateSongTextUI();
	    		
	    		/*
				 * Unhide the album layout
				 */
				//currentAlbumPlayingLayout.setVisibility(View.VISIBLE);
				
				/*
				 * Show next Album
				 */
				if(animation.equals(hideLeft))
					currentAlbumPlayingLayout.startAnimation(showLeft);
				if(animation.equals(fadeAlbumOut)){
					currentAlbumPlayingLayoutOuter.startAnimation(fadeAlbumIn);
		    		invalidateCurrentPlayingImageView.sendEmptyMessageDelayed(0, 50);
		    		invalidateCurrentPlayingImageView.sendEmptyMessageDelayed(0, 100);
		    		invalidateCurrentPlayingImageView.sendEmptyMessageDelayed(0, 150);
		    		invalidateCurrentPlayingImageView.sendEmptyMessageDelayed(0, 200);
		    		invalidateCurrentPlayingImageView.sendEmptyMessageDelayed(0, 250);
		    		invalidateCurrentPlayingImageView.sendEmptyMessageDelayed(0, 300);
		    		invalidateCurrentPlayingImageView.sendEmptyMessageDelayed(0, 350);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	public AnimationListener showAlbumAnimationListener= new AnimationListener(){

		@Override
		public void onAnimationEnd(Animation animation) {
    		/*
    		 * Play the first song of the new album
    		 */

			if(!calledByService)
				playAlbum();
			else
				calledByService = false;
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub
			
		}
		
	};
		
	Timer playAlbumTimer = null;
	/*************************************
	 * 
	 * playAlbum
	 *
	 *************************************/
	private void playAlbum(){
		/*
		 * To give a little room for the animation to finish 
		 * we delay the song start
		 */
		if(playAlbumTimer != null)
			playAlbumTimer.cancel();
		playAlbumTimer = new Timer();
		playAlbumTimer.schedule(new PlayAlbumTimerTask(), 300);
	}
	/****
	 * 
	 * PlayAlbumTimerTask
	 *
	 ****/
	private class PlayAlbumTimerTask extends TimerTask{

		@Override
		public void run() {

			/*
			 * Send order to service to play the first song of the requested album
			 */
			try {
				songCursor.moveToFirst();
				albumCursorPositionPlaying = albumCursor.getPosition();

				playerServiceIface.play(albumCursorPositionPlaying, songCursor.getPosition());

				if (songProgressTimer != null)
					songProgressTimer.cancel();
				songDuration = songCursor.getDouble(
									songCursor.getColumnIndexOrThrow(
											MediaStore.Audio.Media.DURATION));
				songCurrentPosition = 0;
				songProgressBar.setProgress(0);
				songProgressBar.setMax((int) songDuration);
				runOnUiThread(new Runnable(){
					@Override
					public void run() {
						currentPlayingLayout.invalidate();
						triggerSongProgress();
					}						
				});
			} catch (Exception e1) {
				e1.printStackTrace();
			}			
		}
		
	}

	/*************************************
	 * 
	 * Update Song UI components
	 * 
	 *************************************/
	public void updateSongTextUI(){
		
		try{
			Log.i("UI", "Updating songTextUI");
			/*
			 * Check if the artist has changed
			 */
			if(songCursor.getString(
					songCursor.getColumnIndexOrThrow(
						MediaStore.Audio.Media.ARTIST))
							.compareTo((String) artistNameText.getText()) != 0){
				
				Log.i("UI", "Artist Changed!");
				
				artistNameText.setText(songCursor.getString(
					songCursor.getColumnIndexOrThrow(
						MediaStore.Audio.Media.ARTIST)));
				
			}
			/*
			 * Check if the album has changed (might happen if, for instance, changed playlist)
			 */
			if(songCursor.getString(
					songCursor.getColumnIndexOrThrow(
						MediaStore.Audio.Media.ALBUM))
							.compareTo((String) albumNameText.getText()) != 0){
				Log.i("UI", "Album Changed!");
				albumChangedIntentReceiver.onReceive(this, null);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try{
			/*
			 *  Song Name
			 */
			songNameText.setText(songCursor.getString(
									songCursor.getColumnIndexOrThrow(
											MediaStore.Audio.Media.TITLE)));
			
			/*
			 *  Song Duration 
			 */
			int songDuration = new Integer(songCursor.getString(
											songCursor.getColumnIndexOrThrow(
													MediaStore.Audio.Media.DURATION)));
			int minutes = (int) Math.floor(songDuration/1000/60);
			int seconds = (int) Math.floor(songDuration/1000%60);
			String duration = null;
			if(seconds > 9)
				duration = String.valueOf(minutes)+"'"+String.valueOf(seconds);
			else
				duration = String.valueOf(minutes)+"'0"+String.valueOf(seconds);
			songDurationText.setText(duration);

			/* 
			 * Song Progress
			 */
			int songProgress = playerServiceIface.getPlayingPosition();
			minutes = (int) Math.floor(songProgress/1000/60);
			seconds = (int) Math.floor(songProgress/1000%60);
			duration = null;
			if(seconds > 9)
				duration = String.valueOf(minutes)+"'"+String.valueOf(seconds);
			else
				duration = String.valueOf(minutes)+"'0"+String.valueOf(seconds);
			songDurationOngoingText.setText(duration);

			/* 
			 * Progress Bar
			 */
			songProgressBar.setMax(songDuration);
			songProgressBar.setProgress(songProgress);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	

		
		containerLayout.invalidate();
	}

	/*************************************
	 * 
	 * Update Artist/Album Text UI components
	 * 
	 *************************************/
	void updateArtistTextUI(){
		try{
			/*
			 * Check if artist + album text will overlap the album art
			 */
			if(albumCursor.getString(
					albumCursor.getColumnIndexOrThrow(
						MediaStore.Audio.Albums.ARTIST)).length() 
				> 16
				&&
				albumCursor.getString(
						albumCursor.getColumnIndexOrThrow(
							MediaStore.Audio.Albums.ALBUM)).length() 
				> 22
				)
			{
				albumNameText.setMaxLines(1);
				albumNameText.setTextScaleX((float) 0.6);
			} else {
				albumNameText.setTextScaleX((float) 1.0);
				albumNameText.setMaxLines(2);
			}
			/*
			 * Set the artist and album text
			 */
			artistNameText.setText(albumCursor.getString(
									albumCursor.getColumnIndexOrThrow(
										MediaStore.Audio.Albums.ARTIST)));
			albumNameText.setText(albumCursor.getString(
									albumCursor.getColumnIndexOrThrow(
											MediaStore.Audio.Albums.ALBUM)));
			containerLayout.invalidate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*************************************
	 * 
	 * Update Song Progress
	 * 
	 *************************************/
	private void updateSongProgress(){
		try {
			
			this.songProgressBar.setMax((int) this.songCursor.getDouble(
					this.songCursor.getColumnIndexOrThrow(
							MediaStore.Audio.Media.DURATION)));
			this.songProgressBar.setProgress(playerServiceIface.getPlayingPosition());

			if(playerServiceIface.isPlaying()){
				if(songProgressTimer != null)
					songProgressTimer.cancel();
				this.songProgressTimer = new Timer();
				this.songProgressTimer.scheduleAtFixedRate(new SongProgressTimerTask(), 100, 1000);
				
			}
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	/*************************************
	 * 
	 * Trigger Song Progress
	 * 
	 *************************************/
	public void triggerSongProgress(){
		if(songProgressTimer != null)
			songProgressTimer.cancel();
	
		songProgressTimer = new Timer();
		songProgressTimer.scheduleAtFixedRate(new SongProgressTimerTask(), 100, 1000);
	}
	/*************************************
	 * 
	 * Trigger Song Progress
	 * 
	 *************************************/
	public void stopSongProgress(){
		if(songProgressTimer != null)
			songProgressTimer.cancel();
	
//		songProgressTimer = new Timer();
//		songProgressTimer.scheduleAtFixedRate(new SongProgressTimerTask(), 100, 1000);
	}
	/*************************************
	 * 
	 * showSongList
	 * 
	 *************************************/
	private void showSongList(){
	
		/*
		 * Move album Cursor
		 */
		try {
			albumCursor.moveToPosition(playerServiceIface.getAlbumCursorPosition());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		albumNavigatorItemLongClickIndex = albumCursor.getPosition();
		
		/*
		 * getSongCursor
		 */
		songCursor = initializeSongCursor(albumCursor.getString(albumCursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM)));
		
		
		/*
		 * populate song list
		 */		
		String[] fieldsFrom = new String[1];
		int[] fieldsTo		= new int[1];
		fieldsFrom[0] = MediaStore.Audio.Media.TITLE;
		fieldsTo[0] = R.id.songlist_item_song_name;
		SongCursorAdapter	songAdapter = new SongCursorAdapter(
												this,
												R.layout.songlist_dialog_item,
												songCursor,
												fieldsFrom,
												fieldsTo);
		
		/*
		 * Create Dialog
		 */
		AlertDialog.Builder aD = new AlertDialog.Builder(context);
		aD.create();
		aD.setTitle(
				albumCursor.getString(albumCursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM))
				+"\n"+
				albumCursor.getString(albumCursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST)));
		aD.setAdapter(songAdapter, songListDialogClickListener);
		aD.show();		
	}
	
	int songProgressDialogId = R.layout.song_progress_dialog;
	AlertDialog.Builder songProgressAlertDialog = null;
	View songProgressView = null;
	
	/*************************************
	 * 
	 * ShowSongProgressDialog
	 * 
	 *************************************/
	public void showSongProgressDialog(){

		/*
		 * Check if Dialog was created already
		 */
		if(songProgressAlertDialog != null)
			return;

		
		this.showDialog(songProgressDialogId);

	}
	
	
	@Override
	protected Dialog onCreateDialog(int dialogId){

		//this.removeDialog(R.layout.song_progress_dialog);
		
		switch(dialogId){
		case R.layout.song_progress_dialog:
			double start = System.currentTimeMillis();
			/*
			 * Move album Cursor
			 */
			try {
				albumCursor.moveToPosition(playerServiceIface.getAlbumCursorPosition());
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			//albumNavigatorItemLongClickIndex = albumCursor.getPosition();
				
			/*
			 * getSongCursor and move it
			 */
			songCursor = initializeSongCursor(albumCursor.getString(albumCursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM)));
			try{
				songCursor.moveToPosition(playerServiceIface.getSongCursorPosition());
			} catch(Exception e) {
				e.printStackTrace();
				return null;
			}
	
			Log.i("PRFRMC", "took "+(System.currentTimeMillis()-start)+"ms to reload cursors");
	
			
			/*
			 * Create Dialog
			 */
			try{
				songProgressAlertDialog = new AlertDialog.Builder(context);
				songProgressAlertDialog.setTitle(
					songCursor.getString(
						songCursor.getColumnIndexOrThrow(
							MediaStore.Audio.Media.TITLE))
					+"\n"+
					songCursor.getString(
						songCursor.getColumnIndexOrThrow(
							MediaStore.Audio.Media.ARTIST)));
					
				songProgressView = inflateSongProgressView();
				//((ViewGroup)songProgressView.getParent()).removeView(songProgressView);
			}catch(Exception e){
				e.printStackTrace();
				return null;
			}
			
			
			try {
				
				int duration=1;
				duration = playerServiceIface.getDuration()/1000;
				int durMins = duration/60;
				int durSecs = duration%60;
				
				int curPos=0;
				curPos = playerServiceIface.getPlayingPosition()/1000;
				int curMins = curPos/60;
				int curSecs = curPos%60;
			
				((SeekBar)songProgressView.findViewById(R.id.song_progress_dialog_seekbar))
					.setOnSeekBarChangeListener(songProgressDialogOnSeek);
				((SeekBar)songProgressView.findViewById(R.id.song_progress_dialog_seekbar))
					.setMax(duration*1000);
				((SeekBar)songProgressView.findViewById(R.id.song_progress_dialog_seekbar))
					.setProgress(curPos*1000);
				
				if(curSecs > 9)
					((TextView)songProgressView.findViewById(R.id.song_progress_dialog_current_time))
						.setText(curMins+"'"+curSecs);
				else
					((TextView)songProgressView.findViewById(R.id.song_progress_dialog_current_time))
						.setText(curMins+"'0"+curSecs);
					
				if(durSecs > 9)
					((TextView)songProgressView.findViewById(R.id.song_progress_dialog_total_time))
						.setText(durMins+"'"+durSecs);
				else
					((TextView)songProgressView.findViewById(R.id.song_progress_dialog_total_time))
						.setText(durMins+"'0"+durSecs);
					
			} catch (RemoteException e) {
				e.printStackTrace();
			}
	
			songProgressAlertDialog.setView(songProgressView);
			songProgressAlertDialog.setPositiveButton("Back", songProgressDialogOnPositiveClickListener);
			songProgressAlertDialog.setOnCancelListener(songProgressDialogOnCancelListener);
			return songProgressAlertDialog.create();
			
		default:
			return null;
		
		}
	}
	
	DialogInterface.OnClickListener songProgressDialogOnPositiveClickListener = 
		new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				songProgressAlertDialog = null;
				songProgressView = null;
				removeDialog(R.layout.song_progress_dialog);
			}
	};
	DialogInterface.OnCancelListener songProgressDialogOnCancelListener = 
		new DialogInterface.OnCancelListener(){
			@Override
			public void onCancel(DialogInterface dialog) {
				songProgressAlertDialog = null;
				songProgressView = null;
				removeDialog(R.layout.song_progress_dialog);
			}
	};
//	DialogInterface.OnDismissListener songProgressOnDismissListener = 
	
	OnSeekBarChangeListener songProgressDialogOnSeek = new OnSeekBarChangeListener(){

		int	progressCache = 0;
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {			
			try{
				int curMins = progress/60000;
				int curSecs = (progress/1000)%60;
				
				if(curSecs > 9)
					((TextView)songProgressView.findViewById(R.id.song_progress_dialog_current_time))
						.setText(curMins+"'"+curSecs);
				else
					((TextView)songProgressView.findViewById(R.id.song_progress_dialog_current_time))
						.setText(curMins+"'0"+curSecs);
			
				progressCache = progress;	
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			try{
				playerServiceIface.seekTo(progressCache);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {}
	};
	
	/*
	 * InflateSongProgressView
	 */
	public View inflateSongProgressView(){
		LayoutInflater lInflaterService = (LayoutInflater) 
			getSystemService(LAYOUT_INFLATER_SERVICE);
		View songProgressLayout = lInflaterService.inflate(R.layout.song_progress_dialog, null);
		SeekBar songDialogSeekBar = (SeekBar) songProgressLayout.findViewById(R.id.song_progress_dialog_seekbar);
		//Pass song duration and progress to function
		songDialogSeekBar.setMax(100000);
		return songProgressLayout;
	}
	
	/*************************************
	 * 
	 * checkAlbumArtDirectory
	 * 
	 *************************************/
	private void checkAlbumArtDirectory(){
        File albumArtDirectory = new File(this.FILEX_ALBUM_ART_PATH);
        	albumArtDirectory.mkdirs();
		
        File albumSmallArtDirectory = new File(this.FILEX_SMALL_ALBUM_ART_PATH);
        //if(!albumArtDirectory.exists()){
			albumSmallArtDirectory.mkdirs();
        //}
			Log.i("DIR",this.FILEX_ALBUM_ART_PATH);
			Log.i("DIR",this.FILEX_SMALL_ALBUM_ART_PATH);
	}
	/*************************************
	 * 
	 * Check Concert Directory
	 * 
	 *************************************/
	private void checkConcertDirectory(){
		File concertDirectory= new File(this.FILEX_CONCERT_PATH);
		concertDirectory.mkdirs();
	}
	/*************************************
	 * 
	 * Check Preferences Directory
	 * 
	 *************************************/
	private void checkPreferencesDirectory(){
		File preferencesDirectory= new File(this.FILEX_PREFERENCES_PATH);
		preferencesDirectory.mkdirs();
	}
	/*************************************
	 * 
	 * Check Backgrounds Directory
	 * 
	 *************************************/
	private void checkBackgroundDirectory(){
		File bgDirectory= new File(this.FILEX_BACKGROUND_PATH);
		bgDirectory.mkdirs();
	}
	
	/*************************************
	 * 
	 * TimerTasks 
	 * 
	 *************************************/
	
	/*
	 * songProgressTimerTask
	 */
	class	SongProgressTimerTask extends TimerTask{

		@Override
		public void run() {
			if(albumListIsScrolling){
				accumulatedProgress += 1000;
				return;
			}
			songProgressHandler.sendEmptyMessage(0);
		}	
	}
	
	/*
	 * reloadEventListTimerTask
	 */
	class ReloadEventListTimerTask extends TimerTask{
		@Override
		public void run() {
			/*
			 * Show a dialog to give some nice feedback to the user
			 */
			runOnUiThread(new Runnable(){
				@Override
				public void run() {
		        	concertAnalysisProgressDialog = new ProgressDialog(context);
		        	concertAnalysisProgressDialog.setIcon(android.R.drawable.ic_menu_today);
		        	concertAnalysisProgressDialog.setTitle("Analysing concert information");
		        	concertAnalysisProgressDialog.setMessage("Waiting for Last.FM connection");
		        	concertAnalysisProgressDialog.show();					
				}
			});

        	/*
        	 * Create Thread to get concert info
        	 */
			new Thread(){
				public void run(){
					try {
						// maybe use the global var that has already been created
						lastFmEventImporter = new LastFmEventImporter(context);
						lastFmEventImporter.getArtistEvents();
					} catch (SAXException e) {
						e.printStackTrace();
					} catch (ParserConfigurationException e) {
						e.printStackTrace();
					}	
				}
			}.start();
		}
	}
	
	/*
	 * albumListSelectedAlbumTimerTask
	 */
	class AlbumListSelectedAlbumTimerTask extends TimerTask{
		@Override
		public void run() {
			Log.i("LISTSCROLL", "checking...");

			if(albumCursorPositionPlaying <	albumNavigatorList.getFirstVisiblePosition() ||
					albumCursorPositionPlaying > albumNavigatorList.getLastVisiblePosition())
				Log.i("LISTSCROLL", "scrolling list to position "+albumCursorPositionPlaying);
				runOnUiThread(new Runnable(){
					public void run(){
						//albumNavigatorList.setSelection(albumCursorPositionPlaying);
						//albumNavigatorList.setSelectionFromTop(albumCursorPositionPlaying, (int) Math.round((display.getHeight()/2 - (display.getWidth()*CURRENT_PLAY_SCREEN_FRACTION_LANDSCAPE))));
						albumNavigatorList.setSelectionFromTop(albumCursorPositionPlaying, 
																(int) Math.round(
																		(display.getHeight()-20)/2.0 - 
																			(display.getWidth()*(1-CURRENT_PLAY_SCREEN_FRACTION_LANDSCAPE))
																		)
																);
						//albumNavigatorList.scrollBy(0, 460/2-100/2);
					}
				});
			//albumNavigatorList.getN
		}
	}
	
	/*************************************
	 * 
	 * Handlers
	 * 
	 *************************************/
	
	/*
	 * songProgressHandler
	 */
	private Handler songProgressHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			int totalSeconds;
			try {
				if(playerServiceIface == null)
					return;
				totalSeconds = playerServiceIface.getPlayingPosition();
				if(songProgressBar != null)
					songProgressBar.setProgress(totalSeconds);
				else {
					if(songProgressTimer != null)
						songProgressTimer.cancel();
					return;
				}
				String currentTime;
				int seconds = (int) Math.floor(totalSeconds/1000%60);
				int minutes = (int) Math.floor(totalSeconds/60000);
				if(seconds < 10)
					currentTime = minutes+"'0"+seconds;
				else
					currentTime = minutes+"'"+seconds;

				songDurationOngoingText.setText(currentTime);
				if(songProgressAlertDialog != null){
					if(!((SeekBar)songProgressView.findViewById(R.id.song_progress_dialog_seekbar)).isPressed()){
						((TextView)songProgressView.findViewById(R.id.song_progress_dialog_current_time))
							.setText(currentTime);
						((SeekBar)songProgressView.findViewById(R.id.song_progress_dialog_seekbar))
							.setProgress(totalSeconds);
					}
				}
				accumulatedProgress = 0;
				if(currentPlayingLayout != null)
					currentPlayingLayout.invalidate();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	
	/*
	 * getAlbumArtHandler
	 */
	public Handler getAlbumArtHandler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			try{
				Bundle data = msg.getData();
				String info = data.getString("info");
				albumReloadProgressDialog.setMessage(info);
				if(info == "Done!" || info == "No Internet Connection"){
					albumReloadProgressDialog.dismiss();
					getAlbums(true);
				}
			} catch (Exception e){
				e.printStackTrace();
			}
			
			/*
			 * Reload the cursor Positions
			 */
			try {
				initializeAlbumCursor();
				if(playerServiceIface.getAlbumCursorPosition() >= 0 && 
						playerServiceIface.getSongCursorPosition() >= 0){		
					albumCursor.moveToPosition(playerServiceIface.getAlbumCursorPosition());
					songCursor = initializeSongCursor(albumCursor.getString(albumCursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM)));
					songCursor.moveToPosition(playerServiceIface.getSongCursorPosition());
				}
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}		
	};
	
	/*
	 * updateEventListHandler
	 */
	public Handler updateEventListHandler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			Log.i("SET","Set up eventLinkedListAdapter");
			eventListView.setAdapter(lastFmEventImporter.eventLinkedListAdapter);
		}
	};
	
	/*
	 * analyseConcertInfoHandler
	 */
	public Handler analyseConcertInfoHandler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			Bundle data = msg.getData();
			String info = data.getString("info");
			concertAnalysisProgressDialog.setMessage(info);
			if(info == "Done!" || info == "No Internet Connection"){
				concertAnalysisProgressDialog.dismiss();
			}
		}
	};
	
	/*
	 * Resume Playing
	 */
    Handler playerServiceResumeHandler = new Handler(){
    	@Override
    	public void handleMessage(Message msg){
    		try {
				playerServiceIface.resume();
				triggerSongProgress();
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    };
    	
	/********************************
	 * 
	 * set Background
	 * 
	 ********************************/
	public void setBackground(){
		try{
	    	String bgPath = null;
	    	RockOnPreferenceManager settings = new RockOnPreferenceManager(FILEX_PREFERENCES_PATH);
	    	
    		Log.i("BG","BG");
	    	
	    	
	    	if(settings.getBoolean(PREFS_CUSTOM_BACKGROUND, false)){
	    		Log.i("DBG","Setting up background");

	    		if(display.getOrientation() == 0)
		    		bgPath = FILEX_BACKGROUND_PATH +
		    			PREFS_CUSTOM_BACKGROUND_PORTRAIT;
		    	else
		    		bgPath = FILEX_BACKGROUND_PATH + 
		    			PREFS_CUSTOM_BACKGROUND_LANDSCAPE;
		    	
		    	Bitmap bgBitmap = BitmapFactory.decodeStream(
		    			new FileInputStream(
		    					new File(bgPath)));
		    	
		    	findViewById(R.id.songfest_container)
					.setBackgroundDrawable(new BitmapDrawable(bgBitmap));
	    	} else if(settings.getBoolean(PREFS_BACKGROUND_BLUR, false)){
	    		Log.i("DBG","Blurring Background");
	    		System.gc();
	    		/*
		    	 * Blur&Dim the BG
		    	 */
//		        getTheme().applyStyle(arg0, arg1)
	    		
	    		getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(0, 0, 0, 0)));
	    		// Have the system blur any windows behind this one.
		        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
		                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
//		        getWindow().setFlags(WindowManager.LayoutParams.FLAG_DITHER, 
//		        		WindowManager.LayoutParams.FLAG_DITHER);
		        getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND, 
		        		WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		        WindowManager.LayoutParams params = getWindow().getAttributes();
		        params.dimAmount = 0.625f;
		        getWindow().setAttributes(params);
		        
	    	} else {
	    		Log.i("DBG","Clearing out background");
	    		System.gc();
	    		findViewById(R.id.songfest_container)
					.setBackgroundDrawable(null);
	    		getWindow().setBackgroundDrawable(null);
	    		// Do something
	    	}
	    	
	    	
	    	
	    		    	
	    	
//	    	BitmapFactory.Options opts = new BitmapFactory.Options();
//	    	opts.inJustDecodeBounds = true;
//	    	BitmapFactory.decodeFile(bgPath, opts);
//	    	int sampling = 1; // TODO:::::
//	    	opts.inJustDecodeBounds = false;
//	    	Bitmap bgBitmap = null;
//	    	if(opts.outHeight/display.getHeight() > opts.outWidth/display.getWidth()){
//				int newHeight = (int)Math.round(opts.outHeight*(display.getWidth()/opts.outWidth)); 
//	    		Bitmap tmpBitmap = Bitmap.createScaledBitmap(
//	    				BitmapFactory.decodeStream(new FileInputStream(bgPath), null, opts), 
//	    				display.getWidth(), 
//	    				newHeight,
//	    				false);
//	    		bgBitmap = Bitmap.createBitmap(
//	    				tmpBitmap,
//	    				0,
//	    				(newHeight-display.getHeight())/2,
//	    				display.getWidth(),
//	    				display.getHeight(),
//	    				null,
//	    				false);
//	    	} else {
//				int newWidth = (int)Math.round(opts.outWidth*(display.getHeight()/opts.outHeight)); 
//	    		Bitmap tmpBitmap = Bitmap.createScaledBitmap(
//	    				BitmapFactory.decodeStream(new FileInputStream(bgPath), null, opts), 
//	    				newWidth, 
//	    				display.getHeight(),
//	    				false);
//	    		bgBitmap = Bitmap.createBitmap(
//	    				tmpBitmap,
//	    				(newWidth-display.getWidth())/2,
//	    				0,
//	    				display.getWidth(),
//	    				display.getHeight(),
//	    				null,
//	    				false);
//	    	}
//    		findViewById(R.id.songfest_container)
//    			.setBackgroundDrawable(new BitmapDrawable(bgBitmap));

    	} catch(Exception e){
    		e.printStackTrace();
    	}
	}
	
	/********************************
	 * 
	 * hideBackground()
	 * 
	 ********************************/
	public void hideBackground(){
    	try{
    		findViewById(R.id.songfest_container)
    			.getBackground().setCallback(null);
    	}catch(Exception e){
    		e.printStackTrace();	
    	}

    	findViewById(R.id.songfest_container)
			.setBackgroundDrawable(null);

	}
	
	/********************************
	 * 
	 * switch between different UI modes
	 * 
	 ********************************/
	public void setNormalView(){
		if(VIEW_STATE == NORMAL_VIEW){
			
		} else if(VIEW_STATE == LIST_EXPANDED_VIEW) {
			/*
			 * Animate fading of the current playing layout
			 */
			
			/*
			 * Put album navigator full screen (this will happen in the end of the animation)
			 */
			RelativeLayout.LayoutParams params = (LayoutParams) albumNavigatorLayoutOuter.getLayoutParams();
			params.addRule(RelativeLayout.RIGHT_OF, R.id.songfest_current_playing_container);
			albumNavigatorLayoutOuter.setLayoutParams(params);
			
			/*
			 * Animate growth of the album navigator
			 */
			int slideAmount = currentPlayingLayout.getWidth();
			albumNavigatorLayoutOuter.bringToFront();
			TranslateAnimation slideRight= new TranslateAnimation(-slideAmount, 0, 0, 0);
			slideRight.setFillAfter(true);
			slideRight.setDuration(250);
			slideRight.setAnimationListener(slideRightAnimationListener);
			albumNavigatorLayoutOuter.startAnimation(slideRight);
			
			
		} else if(VIEW_STATE == FULLSCREEN_VIEW){
		
			setBackground();
			
			AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
			fadeOut.setFillAfter(true);
			fadeOut.setDuration(300);
			this.mainUIContainer.startAnimation(fadeOut);
			
			showFullScreenHandler.sendEmptyMessageDelayed(VIEW_STATE, 300);
			
			VIEW_STATE = FULLSCREEN_VIEW;
			
		}
		
		VIEW_STATE = NORMAL_VIEW;
		return;
	}
	
	public void setListExpandedView(){

			try{
				/*
				 * Animate fading of the current playing layout
				 */
				Rotate3dAnimation perspectiveFullLeft = new Rotate3dAnimation(
						0,0, 	// X-axis rotation
						90,90, 	// Y-axis rotation
						0,0,  	// Z-axis rotation
						100, 100, // rotation center
						0.0f, // Z-depth
						false); //reverse movement
		    	perspectiveFullLeft.setFillAfter(true);
		    	perspectiveFullLeft.setDuration(1);
				currentPlayingLayout.startAnimation(perspectiveFullLeft);
				
				// hide parts of mainUI
				
				/*
				 * Animate growth of the album navigator
				 */
				int slideAmount = display.getWidth() - albumNavigatorList.getWidth();
				TranslateAnimation slideLeft= new TranslateAnimation(slideAmount, 0, 0, 0);
				slideLeft.setFillAfter(true);
				slideLeft.setDuration(400);
				albumNavigatorLayoutOuter.startAnimation(slideLeft);
				
				/*
				 * Put album navigator full screen
				 */
				RelativeLayout.LayoutParams params = (LayoutParams) albumNavigatorLayoutOuter.getLayoutParams();
				params.addRule(RelativeLayout.RIGHT_OF, 0);
				albumNavigatorLayoutOuter.setLayoutParams(params);
				//albumNavigatorLayout.setBackgroundColor(Color.WHITE);
	//				
	//				LayoutParams paramsList = (LayoutParams) albumNavigatorList.getLayoutParams();
	//				paramsList.width = display.getWidth();
	//				albumNavigatorList.setLayoutParams(paramsList);
	
	//			currentPlayingLayout.setVisibility(View.GONE);
			
				VIEW_STATE = LIST_EXPANDED_VIEW;
			}catch(Exception e){
				e.printStackTrace();
			}
			
			return;
			
	}
	
	GradientDrawable gradDrawable = new GradientDrawable();
	
	Handler showFullScreenHandler = new Handler(){
		
		@Override
		public void handleMessage(Message msg){
		
			Log.i("GOGO", "FULL SCREEN - PART II");
			
			try {
				albumCursor.moveToPosition(playerServiceIface.getAlbumCursorPosition());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try{
				if(msg.what == NORMAL_VIEW){
					/*
					 * Hide album Navigator
					 */
					albumNavigatorList.setVisibility(View.GONE);
					
					/*
					 * CurrentPlaying
					 */
					RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) 
						currentPlayingLayout.getLayoutParams();
					params.width = RelativeLayout.LayoutParams.FILL_PARENT;
					currentPlayingLayout.setLayoutParams(params);
					
					/* straighten currentplaying view */
					Rotate3dAnimation perspectiveStraight = new Rotate3dAnimation(
							0,0, 	// X-axis rotation
							0,0, 	// Y-axis rotation
							0,0,  	// Z-axis rotation
							0, 0, // rotation center
							0.0f, // Z-depth
							false); //reverse movement
			    	perspectiveStraight.setFillAfter(true);
			    	perspectiveStraight.setDuration(1);
					currentPlayingLayout.startAnimation(perspectiveStraight);
					
					/* set album in fullscreen */
					params = (RelativeLayout.LayoutParams)
						currentAlbumPlayingLayoutOuter.getLayoutParams();
					params.width = RelativeLayout.LayoutParams.FILL_PARENT;
					params.height = RelativeLayout.LayoutParams.FILL_PARENT;
					if(display.getOrientation() != 0){
						params.topMargin = 0; // was 12
						params.leftMargin = 0; // was 12
					}
//					currentAlbumPlayingImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//					currentAlbumPlayingOverlayImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
					
					/* use large album bitmap */
					currentAlbumPlayingImageView.setImageBitmap(albumAdapter.getAlbumBitmap(albumCursor.getPosition(), BITMAP_SIZE_FULLSCREEN));
					currentAlbumPlayingImageView.setScaleType(ScaleType.CENTER_INSIDE);
					
					/* remove album frame and album margins */
					params = (RelativeLayout.LayoutParams)
						currentAlbumPlayingImageView.getLayoutParams();
					params.bottomMargin = 0; // was 8
					params.topMargin = 0; // was 8
					params.leftMargin = 0; // was 8
					params.rightMargin = 0; // was 8
					currentAlbumPlayingOverlayImageView.setVisibility(View.GONE);
					
					/* Remove relative positioning of the artist 
					 * and song name text containers
					 */
					RelativeLayout artistTextContainer = (RelativeLayout) 
						findViewById(R.id.current_playing_artist_album_container);
					params = (LayoutParams)	artistTextContainer.getLayoutParams();
					params.addRule(RelativeLayout.ABOVE, 0);
					params.addRule(RelativeLayout.RIGHT_OF, 0);
					params.topMargin = 0; // was 12
					//params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
					params.height = 90;
					artistTextContainer.setPadding(12, 12, 12, 12);
//					artistTextContainer.setBackgroundResource(R.drawable.fullscreen_artist_overlay);
					if(display.getOrientation() == 0){
						artistTextContainer.setBackgroundColor(Color.argb(255, 25, 25, 25));
//						artistTextContainer.setBackgroundResource(R.drawable.full_screen_overlay_top);
					}
					else
						artistTextContainer.setBackgroundColor(Color.argb(133, 25, 25, 25));
					artistNameText.setMaxLines(1);
					albumNameText.setMaxLines(1);
					
					
		//			params = (LayoutParams) playPauseImage.getLayoutParams();
		//			params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
		//
		//			params = (LayoutParams) songNameText.getLayoutParams();
		//			params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
					
					RelativeLayout songNameContainer = (RelativeLayout)
						findViewById(R.id.songname_container);
					params = (LayoutParams) songNameContainer.getLayoutParams();
					params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
					
					
					RelativeLayout songInfoContainer = (RelativeLayout) 
						findViewById(R.id.current_playing_song_container);
					params = (LayoutParams)	songInfoContainer.getLayoutParams();
					params.addRule(RelativeLayout.BELOW, 0);
					params.addRule(RelativeLayout.RIGHT_OF, 0);
					params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);
					//params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
					// TODO: FIX THIS
					params.height = 90;
					songInfoContainer.setPadding(12, 0, 12, 12); // was 0,0,0,24
					if(display.getOrientation() == 0){
						songInfoContainer.setBackgroundColor(Color.argb(255, 25, 25, 25));
//						songInfoContainer.setBackgroundResource(
//								R.drawable.full_screen_overlay_bottom);
					}
					else{
						songInfoContainer.setBackgroundColor(Color.argb(133, 25, 25, 25));
					}
					songNameText.setMaxLines(1);
					/*
					 * Unhide the separators
					 */
					ImageView im = (ImageView) findViewById(R.id.fullscreen_artist_separator);
					im.setVisibility(View.VISIBLE);
					im = (ImageView) findViewById(R.id.fullscreen_song_separator);
					im.setVisibility(View.VISIBLE);					
					
					/* Set BG of the artist and song containers */
					//TODO
					
					
	
				} else { // FULLSCREEN to NORMAL
					/*
					 * Hide album Navigator
					 */
					albumNavigatorList.setVisibility(View.VISIBLE);
					
					/*
					 * CurrentPlaying
					 */
					RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) 
						currentPlayingLayout.getLayoutParams();
					if(display.getOrientation() == 0){
						params.width = (int) (display.getWidth() * CURRENT_PLAY_SCREEN_FRACTION); 
					} else { //LANDSCAPE
						params.width = (int) (display.getWidth() * CURRENT_PLAY_SCREEN_FRACTION_LANDSCAPE);
					}
					currentPlayingLayout.setLayoutParams(params);
					
					/* put currentplaying view back in perspective*/
					int angle;
					if(display.getOrientation() == 0)
						angle = 30;
					else
						angle = 15;
					Rotate3dAnimation perspectiveStraight = new Rotate3dAnimation(
							0,0, 	// X-axis rotation
							angle,angle, 	// Y-axis rotation
							0,0,  	// Z-axis rotation
							100, 100, // rotation center
							0.0f, // Z-depth
							false); //reverse movement
			    	perspectiveStraight.setFillAfter(true);
			    	perspectiveStraight.setDuration(1);
					currentPlayingLayout.startAnimation(perspectiveStraight);
					
					
					/* set album back in the mode */
					int dimension;
					params = (RelativeLayout.LayoutParams)
						currentAlbumPlayingLayoutOuter.getLayoutParams();
					if(display.getOrientation() == 0)
						dimension = (int) (display.getWidth() * CURRENT_PLAY_SCREEN_FRACTION);
					else{
						dimension = (int) (display.getWidth() * CURRENT_PLAY_SCREEN_FRACTION_LANDSCAPE / 2);
						params.topMargin = 12; // was 12
						params.leftMargin = 12; // was 12
					}
					params.width = dimension;
					params.height = dimension;
					currentAlbumPlayingImageView.setScaleType(ImageView.ScaleType.FIT_XY);
					currentAlbumPlayingOverlayImageView.setScaleType(ImageView.ScaleType.FIT_XY);
					
					/* use small Bitmap */
					currentAlbumPlayingImageView.setImageBitmap(albumAdapter.getAlbumBitmap(albumCursor.getPosition(), BITMAP_SIZE_NORMAL));
					
					
					/* remove album frame and album margins */
					params = (RelativeLayout.LayoutParams)
						currentAlbumPlayingImageView.getLayoutParams();
					params.bottomMargin = 8; // was 8
					params.topMargin = 8; // was 8
					params.leftMargin = 8; // was 8
					params.rightMargin = 8; // was 8
					if(showFrame)
						currentAlbumPlayingOverlayImageView.setVisibility(View.VISIBLE);
					
					/* Remove relative positioning of the artist 
					 * and song name text containers
					 */
					RelativeLayout artistTextContainer = (RelativeLayout) 
						findViewById(R.id.current_playing_artist_album_container);
					params = (LayoutParams)	artistTextContainer.getLayoutParams();
					if(display.getOrientation() == 0)
						params.addRule(RelativeLayout.ABOVE, R.id.songfest_current_album_playing_container_global);
					else{
						params.addRule(RelativeLayout.ABOVE, R.id.current_playing_song_container);
						params.addRule(RelativeLayout.RIGHT_OF, R.id.songfest_current_album_playing_container_global);
					}
					params.topMargin = 12; // was 12
					params.height = RelativeLayout.LayoutParams.FILL_PARENT;
					artistTextContainer.setPadding(0, 0, 0, 0);
					artistTextContainer.setBackgroundColor(Color.argb(0, 0, 0, 0));
					
					artistNameText.setMaxLines(2);
					albumNameText.setMaxLines(2);
					
		//			params = (LayoutParams) playPauseImage.getLayoutParams();
		//			params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
		//
		//			params = (LayoutParams) songNameText.getLayoutParams();
		//			params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
					
					RelativeLayout songNameContainer = (RelativeLayout)
						findViewById(R.id.songname_container);
					params = (LayoutParams) songNameContainer.getLayoutParams();
					params.height = RelativeLayout.LayoutParams.FILL_PARENT;
					
					RelativeLayout songInfoContainer = (RelativeLayout) 
						findViewById(R.id.current_playing_song_container);
					params = (LayoutParams)	songInfoContainer.getLayoutParams();
					params.addRule(RelativeLayout.BELOW, R.id.songfest_current_album_playing_container_global);
					//params.addRule(RelativeLayout.RIGHT_OF, 0);
					params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);
					//params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
					// TODO: FIX THIS
					params.height = RelativeLayout.LayoutParams.FILL_PARENT;
					songInfoContainer.setPadding(0, 0, 0, 24); // was 0,0,0,24
					songInfoContainer.setBackgroundColor(Color.argb(0, 0, 0, 0));
				
					songNameText.setMaxLines(2);
					
					/*
					 * Hide the separators
					 */
					ImageView im = (ImageView) findViewById(R.id.fullscreen_artist_separator);
					im.setVisibility(View.GONE);
					im = (ImageView) findViewById(R.id.fullscreen_song_separator);
					im.setVisibility(View.GONE);
					
					/* Set BG of the artist and song containers */
					//TODO
					
					
				}
			}catch (Exception e){
				e.printStackTrace();
			}
			
			/*
			 * Bring back main UI
			 */
			AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
			fadeIn.setFillAfter(true);
			fadeIn.setDuration(400);
			try{
				mainUIContainer.startAnimation(fadeIn);
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		
	};
	
	public void setFullScreenView(){
		
		Log.i("GOGO", "FULL SCREEN");
		
		hideBackground();
		
		AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
		fadeOut.setFillAfter(true);
		fadeOut.setDuration(300);
		this.mainUIContainer.startAnimation(fadeOut);
		
		showFullScreenHandler.sendEmptyMessageDelayed(NORMAL_VIEW, 300);
		
		VIEW_STATE = FULLSCREEN_VIEW;
		
		if(true)
			return;
		
		/*
		 * Animate fading of the album navigator
		 */
		Rotate3dAnimation perspectiveFullLeft = new Rotate3dAnimation(
				0,0, 	// X-axis rotation
				90,90, 	// Y-axis rotation
				0,0,  	// Z-axis rotation
				100, 100, // rotation center
				0.0f, // Z-depth
				false); //reverse movement
    	perspectiveFullLeft.setFillAfter(true);
    	perspectiveFullLeft.setDuration(1);
		albumNavigatorList.startAnimation(perspectiveFullLeft);
//    	currentPlayingLayout.startAnimation(perspectiveFullLeft);
		
		
		/*
		 * Put album navigator full screen
		 */
		RelativeLayout.LayoutParams params = (LayoutParams) currentPlayingLayout.getLayoutParams();
		params.width = RelativeLayout.LayoutParams.FILL_PARENT;
		currentPlayingLayout.setLayoutParams(params);
		
		/*
		 * Animate growth of the current playing layout
		 */
//		int fullWidth = display.getWidth();
////		int slideAmount = display.getWidth() - albumNavigatorList.getWidth();
////		TranslateAnimation slideLeft= new TranslateAnimation(slideAmount, 0, 0, 0);
//		ScaleAnimation scaleAnim = new ScaleAnimation(0.66f, 1.0f, 1.0f, 1.0f);
//		scaleAnim.setFillAfter(true);
//		scaleAnim.setDuration(400);
//		currentPlayingLayout.startAnimation(scaleAnim);
		
		//albumNavigatorLayout.setBackgroundColor(Color.WHITE);
//			
//			LayoutParams paramsList = (LayoutParams) albumNavigatorList.getLayoutParams();
//			paramsList.width = display.getWidth();
//			albumNavigatorList.setLayoutParams(paramsList);

//		currentPlayingLayout.setVisibility(View.GONE);
		
//		if(VIEW_STATE == NORMAL_VIEW){
//			
//			
//			
//		} else if(VIEW_STATE == LIST_EXPANDED_VIEW) {
//			
//		} else if(VIEW_STATE == FULLSCREEN_VIEW){
//			
//		}
		
		VIEW_STATE = FULLSCREEN_VIEW;
	}
	
	/*******************************
	 * 
	 * hide and show UI components
	 * 
	 *******************************/
	public void hideMainUI(){
		
		/*
		 * Hide main UI components
		 */
		this.albumNavigatorLayout.setVisibility(View.GONE);
		this.currentPlayingLayout.setVisibility(View.GONE);
		this.currentAlbumPlayingImageView.setVisibility(View.GONE);
		this.currentAlbumPlayingLayout.setVisibility(View.GONE);
		this.currentAlbumPlayingLayoutOuter.setVisibility(View.GONE);
		this.albumNavigatorList.setVisibility(View.GONE);
//		this.albumNavigatorTextContainer.setVisibility(View.GONE);
		this.artistNameText.setVisibility(View.GONE);
		this.albumNameText.setVisibility(View.GONE);
		this.songNameText.setVisibility(View.GONE);
		this.songProgressBar.setVisibility(View.GONE);
		this.songDurationText.setVisibility(View.GONE);
		this.songDurationOngoingText.setVisibility(View.GONE);
		findViewById(R.id.forward_image).setVisibility(View.GONE);
		findViewById(R.id.rewind_image).setVisibility(View.GONE);
		findViewById(R.id.current_album_image_play_pause).setVisibility(View.GONE);
		
		ImageView albumImageOverlay = (ImageView) findViewById(R.id.current_album_image_overlay);
		albumImageOverlay.setVisibility(View.GONE);
	}
	public void showMainUI(){
		/*
		 * Remove the background of the main container
		 */
		//this.mainUIContainer.setBackgroundColor(Color.TRANSPARENT);
		//this.mainUIContainer.setBackgroundDrawable(null);
		
		this.mainUIContainer.setVisibility(View.VISIBLE);
		this.albumNavigatorLayout.setVisibility(View.VISIBLE);
		this.currentPlayingLayout.setVisibility(View.VISIBLE);
		this.currentAlbumPlayingImageView.setVisibility(View.VISIBLE);
		this.currentAlbumPlayingLayout.setVisibility(View.VISIBLE);
		this.currentAlbumPlayingLayoutOuter.setVisibility(View.VISIBLE);
		this.albumNavigatorList.setVisibility(View.VISIBLE);
		//this.albumNavigatorTextContainer.setVisibility(View.VISIBLE);
		this.artistNameText.setVisibility(View.VISIBLE);
		this.albumNameText.setVisibility(View.VISIBLE);
		this.songNameText.setVisibility(View.VISIBLE);
		this.songProgressBar.setVisibility(View.VISIBLE);
		this.songDurationText.setVisibility(View.VISIBLE);
		this.songDurationOngoingText.setVisibility(View.VISIBLE);
		findViewById(R.id.forward_image).setVisibility(View.VISIBLE);
		findViewById(R.id.rewind_image).setVisibility(View.VISIBLE);
		findViewById(R.id.current_album_image_play_pause).setVisibility(View.VISIBLE);
		
		if(showFrame)
			this.currentAlbumPlayingOverlayImageView.setVisibility(View.VISIBLE);
	}
	
	public void hideWebUI(){
		this.webView.setVisibility(View.GONE);
	}
	public void showWebUI(){
		if(this.songSearchContainer.getVisibility() == View.VISIBLE)
			this.hideSongSearch();
		if(this.eventListViewContainer.getVisibility() == View.VISIBLE)
			this.hideEventUI();
		
		this.webView.setVisibility(View.VISIBLE);
	}
	
	public void hideSongSearch(){
		this.songSearchContainer.setVisibility(View.GONE);
		this.songSearchTextView.setVisibility(View.GONE);
	}
	public void showSongSearch(){
		if(this.eventListViewContainer.getVisibility() == View.VISIBLE)
			this.hideEventUI();
		this.songSearchContainer.setVisibility(View.VISIBLE);
		this.songSearchTextView.setVisibility(View.VISIBLE);	
	}
	
	public void hideHelpUI(){
		this.helpView.setVisibility(View.GONE);
		this.helpImageView.setVisibility(View.GONE);
	}
	public void showHelpUI(){
		this.helpView.setVisibility(View.VISIBLE);
		this.helpImageView.setVisibility(View.VISIBLE);
	}
	
	public void hideEventUI(){
		this.eventListViewContainer.setVisibility(View.GONE);
		this.eventListView.setVisibility(View.GONE);
		this.eventListDecreaseRadius.setVisibility(View.GONE);
		this.eventListIncreaseRadius.setVisibility(View.GONE);
		this.eventListRadius.setVisibility(View.GONE);
		this.eventListRadiusMetric.setVisibility(View.GONE);
	}
	public void showEventUI(){
		if(this.songSearchContainer.getVisibility() == View.VISIBLE)
			this.hideSongSearch();
		
		this.eventListViewContainer.setVisibility(View.VISIBLE);
		this.eventListView.setVisibility(View.VISIBLE);
		this.eventListDecreaseRadius.setVisibility(View.VISIBLE);
		this.eventListIncreaseRadius.setVisibility(View.VISIBLE);
		this.eventListRadius.setVisibility(View.VISIBLE);
		this.eventListRadiusMetric.setVisibility(View.VISIBLE);	
	}
	
	public void recycleNavigatorList(){
    	/*
    	 * Navigator Album Art
    	 */
    	try{
			for(int i = 0; i < this.albumAdapter.albumImages.length; i++){
	    		if(this.albumAdapter.albumImages[i] != null){
	    			this.albumAdapter.albumImages[i].recycle();
	    			this.albumAdapter.albumImages[i] = null;
	    		}
	    	}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
	}
	
	public void cleanUpGlobals(){
		ALBUM_COLS = null;
	    SONG_COLS = null;
	    ARTIST_COLS = null;    
//	    double 				CURRENT_PLAY_SCREEN_FRACTION = 0.66;
//	    double 				CURRENT_PLAY_SCREEN_FRACTION_LANDSCAPE = 0.75;
//	    double 				NAVIGATOR_SCREEN_FRACTION = 1 - CURRENT_PLAY_SCREEN_FRACTION;
//	    double 				NAVIGATOR_SCREEN_FRACTION_LANDSCAPE = 1 - CURRENT_PLAY_SCREEN_FRACTION_LANDSCAPE;
	    FILEX_PATH = null;
	    FILEX_ALBUM_ART_PATH = null;
	    FILEX_CONCERT_PATH = null;
	    FILEX_SMALL_ALBUM_ART_PATH = null;
	    PREFS_NAME = null;
//	    long				VERSION = (long) 1.0;
//	    long				ART_IMPORT_INTVL = 2*24*60*60*1000;
//	    double				CONCERT_RADIUS_DEFAULT = 750000;
//	    double				CONCERT_RADIUS_INCREMENT = 50000;
	    
	    musicChangedIntentFilter = null;  
	    albumChangedIntentFilter = null; 
	    musicChangedIntentReceiver = null;  
	    albumChangedIntentReceiver= null;  
	   
	    context = null;
	    contentResolver = null;
	    albumCursor = null;
	    songCursor = null;
	    artistCursor = null;
//	    public	int						albumCursorPositionPlaying = 0;
//	    public 	boolean					SHUFFLE = false;
	    playerServiceConn = null;
	    playerServiceIface = null;
//	    public	boolean					calledByService = false;
//	    private double					songDuration = 0;
//	    private double					songCurrentPosition = 0;
//	    private boolean					albumListIsScrolling = false;
//	    private int						accumulatedProgress = 0;
	    lastFmEventImporter = null;
//	    public	double					concertRadius;
//	    albumAdapter = null;
	    
	    perspectiveLeft = null;
	    perspectiveRight = null;
	    hideLeft = null;
	    showLeft = null;
	    hideRight = null;
	    showRight = null;
	    //private AlphaAnimation		fadeAlbumOut = null;
	    //private AlphaAnimation		fadeAlbumIn = null;
	    fadeAlbumOut = null;
	    fadeAlbumIn = null;
	    
	    display = null;
	    mediaPlayer = null;
	    mainUIContainer = null;
	    currentAlbumPlayingImageView = null;
	    containerLayout = null;
	    currentPlayingLayout = null;
	    currentAlbumPlayingLayout = null;
	    currentAlbumPlayingLayoutOuter = null;
	    albumNavigatorLayout = null;
	    albumNavigatorList = null;
//	    albumNavigatorTextContainer = null;
	    artistNameText = null;
	    albumNameText = null;
	    songNameText = null;
	    songDurationText = null;
//	    songListContainer = null;
//	    songListHint = null;
//	    songListView = null;
	    eventListViewContainer = null;
	    eventListDecreaseRadius = null;
	    eventListIncreaseRadius = null;
	    eventListRadius = null;
	    eventListRadiusMetric = null;
	    eventListView = null;
	    songProgressBar = null;
	    webView	= null;
	    songSearchContainer = null;
	    helpView = null;
	    helpImageView = null;
	    //public	Button					songSearchButton = null;
	    songSearchTextView = null;
	    albumReloadProgressDialog = null;
	    concertAnalysisProgressDialog = null;
	    //private	ImageView		songProgressTotalView = null;
	    //private	ImageView		songProgressView = null;
	    
		removeAlbumLabelsTimer = null;
	    //private TimerTask	removeAlbumLabelsTimerTask; // defined below
	    songProgressTimer = null;
	    //private TimerTask	songProgressTimerTask; // defined below 
	    reloadEventListTimer = null;
	    //private TimerTask	reloadEventListTimerTask; // defined below
	    
	    //ViewGroup bigV = (ViewGroup) findViewById(R.id.songfest_container);
	    //bigV = (ViewGroup) bigV.getRootView();
	    //bigV.removeAllViews();
	    //bigV.removeAllViewsInLayout();
	    //this.currentAlbumPlayingImageView.getDrawable().setCallback(null);
	}
	
}
