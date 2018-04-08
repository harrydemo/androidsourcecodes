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
import java.io.FileOutputStream;

import com.xmobileapp.rockplayer.R;

import com.xmobileapp.rockplayer.utils.Constants;
import com.xmobileapp.rockplayer.utils.RockOnPreferenceManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class RockOnSettings extends PreferenceActivity {

	private final Constants constants = new Constants();
    private final String KEY_TOGGLE_SHOW_ICON = RockPlayer.PREFS_SHOW_ICON;
    private final String KEY_TOGGLE_SCROBBLE_DROID = RockPlayer.PREFS_SCROBBLE_DROID;
    private final String KEY_TOGGLE_SHOW_ART_WHILE_SCROLLING = RockPlayer.PREFS_SHOW_ART_WHILE_SCROLLING;
    private final String KEY_TOGGLE_SHOW_FRAME = RockPlayer.PREFS_SHOW_FRAME;
    private final String KEY_TOGGLE_ALWAYS_LANDSCAPE = RockPlayer.PREFS_ALWAYS_LANDSCAPE;
    private final String KEY_TOGGLE_AUTO_ROTATE = RockPlayer.PREFS_AUTO_ROTATE;
    private final String KEY_TOGGLE_CUSTOM_BACKGROUND = RockPlayer.PREFS_CUSTOM_BACKGROUND;
    private final String KEY_TOGGLE_CUSTOM_BACKGROUND_PORTRAIT =
    	RockPlayer.PREFS_CUSTOM_BACKGROUND_PORTRAIT;
    private final String KEY_TOGGLE_CUSTOM_BACKGROUND_LANDSCAPE =
    	RockPlayer.PREFS_CUSTOM_BACKGROUND_LANDSCAPE;
    
    private	final String FILEX_PREFERENCES_PATH = "/sdcard/RockOn/preferences/";
    private final String FILEX_BACKGROUND_PATH = RockPlayer.FILEX_BACKGROUND_PATH;

    
    //private static final String KEY_CALL_SETTINGS = "call_settings";
    //private static final String KEY_SYNC_SETTINGS = "sync_settings";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    	/*
    	 * Blur&Dim the BG
    	 */
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_DITHER, 
//        		WindowManager.LayoutParams.FLAG_DITHER);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND, 
        		WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.dimAmount = 0.625f;
        getWindow().setAttributes(params);
        
        /*
         * Get preferences layout from xml
         */
        addPreferencesFromResource(R.xml.rockonsettings);
        
        //PreferenceGroup parent = (PreferenceGroup) findPreference(KEY_PARENT);
        //Utils.updatePreferenceToSpecificActivityOrRemove(this, parent, KEY_SYNC_SETTINGS, 0);
        
        /*
         * Initialize settings
         */
        initSettings();

    }
    
    public void initSettings(){
//        SharedPreferences settings = getSharedPreferences(Filex.PREFS_NAME, 0);
        RockOnPreferenceManager settings = new RockOnPreferenceManager(FILEX_PREFERENCES_PATH);
        /*
         * Show Icon Preference
         */
        ((CheckBoxPreference) findPreference(KEY_TOGGLE_SHOW_ICON))
        						.setChecked(settings.getBoolean(KEY_TOGGLE_SHOW_ICON, false));
        ((CheckBoxPreference) findPreference(KEY_TOGGLE_SHOW_ICON))
        						.setOnPreferenceChangeListener(new ShowIconChangeListener());

        /*
         * Recent Period Preference
         */
        ((Preference) findPreference(constants.PREF_KEY_RECENT_PERIOD)).setTitle(
        		getResources().getString(R.string.settings_recent_period) + " - " +
        						settings.getInt(new Constants().PREF_KEY_RECENT_PERIOD, 
        								new Constants().RECENT_PERIOD_DEFAULT_IN_DAYS) + " days");   
        ((Preference) findPreference(constants.PREF_KEY_RECENT_PERIOD)).setSummary(
        		getResources().getString(R.string.settings_recent_period_summary) + " - "
        						+settings.getInt(new Constants().PREF_KEY_RECENT_PERIOD, 
        								new Constants().RECENT_PERIOD_DEFAULT_IN_DAYS) + " days");
        ((Preference) findPreference(constants.PREF_KEY_RECENT_PERIOD))
        						.setOnPreferenceClickListener(new RecentPeriodChangeListener());
        
        /*
         * Last.FM Scrobble Droid
         */
        ((CheckBoxPreference) findPreference(KEY_TOGGLE_SCROBBLE_DROID))
			.setChecked(settings.getBoolean(KEY_TOGGLE_SCROBBLE_DROID, false));
        ((CheckBoxPreference) findPreference(KEY_TOGGLE_SCROBBLE_DROID))
			.setOnPreferenceChangeListener(new ScrobbleDroidChangeListener());
        
        /*
         * Look and Feel
         */
        ((CheckBoxPreference) findPreference(KEY_TOGGLE_SHOW_ART_WHILE_SCROLLING))
			.setChecked(settings.getBoolean(KEY_TOGGLE_SHOW_ART_WHILE_SCROLLING, false));
        ((CheckBoxPreference) findPreference(KEY_TOGGLE_SHOW_ART_WHILE_SCROLLING))
			.setOnPreferenceChangeListener(new ShowArtWhileScrollingChangeListener());
        
        ((CheckBoxPreference) findPreference(KEY_TOGGLE_SHOW_FRAME))
			.setChecked(settings.getBoolean(KEY_TOGGLE_SHOW_FRAME, false));
        ((CheckBoxPreference) findPreference(KEY_TOGGLE_SHOW_FRAME))
			.setOnPreferenceChangeListener(new ShowFrameChangeListener());
    
    	((CheckBoxPreference) findPreference(KEY_TOGGLE_ALWAYS_LANDSCAPE))
			.setChecked(settings.getBoolean(KEY_TOGGLE_ALWAYS_LANDSCAPE, false));
		((CheckBoxPreference) findPreference(KEY_TOGGLE_ALWAYS_LANDSCAPE))
			.setOnPreferenceChangeListener(new AlwaysLandscapeChangeListener());
	
		((CheckBoxPreference) findPreference(KEY_TOGGLE_AUTO_ROTATE))
			.setChecked(settings.getBoolean(KEY_TOGGLE_AUTO_ROTATE, false));
		((CheckBoxPreference) findPreference(KEY_TOGGLE_AUTO_ROTATE))
			.setOnPreferenceChangeListener(new AutoRotateChangeListener());
		
		((CheckBoxPreference) findPreference(KEY_TOGGLE_CUSTOM_BACKGROUND))
			.setChecked(settings.getBoolean(KEY_TOGGLE_CUSTOM_BACKGROUND, false));
		((CheckBoxPreference) findPreference(KEY_TOGGLE_CUSTOM_BACKGROUND))
			.setOnPreferenceChangeListener(new CustomBackgroundChangeListener());

		((Preference) findPreference(KEY_TOGGLE_CUSTOM_BACKGROUND_PORTRAIT))
			.setOnPreferenceClickListener(new CustomBackgroundChooserClickListener(KEY_TOGGLE_CUSTOM_BACKGROUND_PORTRAIT));

		((Preference) findPreference(KEY_TOGGLE_CUSTOM_BACKGROUND_LANDSCAPE))
			.setOnPreferenceClickListener(new CustomBackgroundChooserClickListener(KEY_TOGGLE_CUSTOM_BACKGROUND_LANDSCAPE));

    }
    
    @Override
    protected void onResume() {
        //findPreference(KEY_CALL_SETTINGS).setEnabled(!isAirplaneMode());
        super.onResume();
    }
    
    protected void onDestroy(){
    	super.onDestroy();
    	this.setResult(Activity.RESULT_OK);
    }
    
    protected void onPause(){
    	super.onPause();
    	this.setResult(Activity.RESULT_OK);
    }
    
    
    /*************************************
     * 
     * ShowIcon Listener
     * 
     *************************************/
    public class ShowIconChangeListener implements OnPreferenceChangeListener {

		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			Log.i("SETTINGS", "Changed show_icon to "+ newValue);
//			SharedPreferences settings = getSharedPreferences(Filex.PREFS_NAME, 0);
//			Editor edit = settings.edit();
//			edit.putBoolean(KEY_TOGGLE_SHOW_ICON, (Boolean) newValue);
//			edit.commit();
//			
	        RockOnPreferenceManager settings = new RockOnPreferenceManager(FILEX_PREFERENCES_PATH);
	        settings.putBoolean(KEY_TOGGLE_SHOW_ICON, (Boolean) newValue);
	        return true;
		}
    	
    }
        
    /************************************
     * 
     * Recent Period Listener
     * 
     ************************************/
    public class RecentPeriodChangeListener implements OnPreferenceClickListener {

    	int period = new Constants().RECENT_PERIOD_DEFAULT_IN_DAYS;
		
		@Override
		public boolean onPreferenceClick(Preference preference) {

			//AlertDialog.Builder daysDialogBuilder = AlertDialog.Builder((Context) RockOnSettings.this);
			
			/*
			 * Create the dialog
			 */
			AlertDialog.Builder daysDialogBuilder = new AlertDialog.Builder(preference.getContext());
			/*
			 * Create inner view of the dialog
			 */
			RelativeLayout recentDaysPicker = (RelativeLayout) 
				((LayoutInflater) (preference.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)))
					.inflate(R.layout.recent_period_setter, null);
			
			period = (new RockOnPreferenceManager(FILEX_PREFERENCES_PATH))
					.getInt(new Constants().PREF_KEY_RECENT_PERIOD, 
							new Constants().RECENT_PERIOD_DEFAULT_IN_DAYS);
			((TextView) recentDaysPicker.findViewById(R.id.recent_period))
					.setText(Integer.toString(period));
			((View) recentDaysPicker.findViewById(R.id.recent_period_increase))
					.setOnClickListener(increaseRecentIntervalOnClickListener);
			((View) recentDaysPicker.findViewById(R.id.recent_period_decrease))
					.setOnClickListener(decreaseRecentIntervalOnClickListener);
			daysDialogBuilder.setView(recentDaysPicker);
			/*
			 * Dialog settings
			 */
			daysDialogBuilder.setTitle("What is recent?");
			daysDialogBuilder.setIcon(android.R.drawable.ic_menu_month);
			daysDialogBuilder.setPositiveButton("Change", recentPeriodChangeOnClickListener);
			daysDialogBuilder.setNegativeButton("Cancel", null);
			
			AlertDialog daysDialog = daysDialogBuilder.create();
//			RelativeLayout.LayoutParams params = 
//				new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
//			params.topMargin = 20;
//			params.bottomMargin = 20;
			//params.addRule(RelativeLayout.CENTER_HORIZONTAL);
			//params.addRule(RelativeLayout.CENTER_IN_PARENT);
//			daysDialog.addContentView(
//					recentDaysPicker,
//					params
//					);
//			daysDialog.setTitle(R.string.settings_recent_period);
			//daysDialog.setIcon(android.R.drawable.ic_menu_month);
			daysDialog.show();
			return true;
		}
		
		DialogInterface.OnClickListener recentPeriodChangeOnClickListener 
			= new DialogInterface.OnClickListener(){
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
//				Editor editor = getSharedPreferences(Filex.PREFS_NAME, 0).edit();
//				editor.putInt(Constants.PREF_KEY_RECENT_PERIOD, period);
//				editor.commit();
//				
				RockOnPreferenceManager settings = new RockOnPreferenceManager(FILEX_PREFERENCES_PATH);
				settings.putInt(new Constants().PREF_KEY_RECENT_PERIOD, period);
				
				// TODO: could be optimized - reload just the 'recent period' option
				RockOnSettings.this.initSettings();

			}
			
		};
		
		View.OnClickListener increaseRecentIntervalOnClickListener
			= new View.OnClickListener(){

				@Override
				public void onClick(View v) {
					period += 5;
					View parentView = (View) v.getParent();
					((EditText) parentView.findViewById(R.id.recent_period)).setText(Integer.toString(period));
				}

			
		};
		
		View.OnClickListener decreaseRecentIntervalOnClickListener
			= new View.OnClickListener(){
	
				@Override
				public void onClick(View v) {
					period -= 5;
					period = Math.max(0, period);
					View parentView = (View) v.getParent();
					((EditText) parentView.findViewById(R.id.recent_period)).setText(Integer.toString(period));
				}
	
			
		};
		
    }
    
    /*************************************
     * 
     * ScrobbleDroid Listener
     * 
     *************************************/
    public class ScrobbleDroidChangeListener implements OnPreferenceChangeListener {

		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			Log.i("SETTINGS", "Changed scrobble_droid to "+ newValue);
//			SharedPreferences settings = getSharedPreferences(Filex.PREFS_NAME, 0);
//			Editor edit = settings.edit();
//			edit.putBoolean(KEY_TOGGLE_SCROBBLE_DROID, (Boolean) newValue);
//			edit.commit();
//			
			RockOnPreferenceManager settings = new RockOnPreferenceManager(FILEX_PREFERENCES_PATH);
			settings.putBoolean(KEY_TOGGLE_SCROBBLE_DROID, (Boolean) newValue);

			if(((Boolean) newValue) == Boolean.TRUE){
				Intent i = new Intent(Intent.ACTION_VIEW, 
				Uri.parse("market://search?q=pname:net.jjc1138.android.scrobbler"));
				startActivity(i);
			}
			
	        return true;
	        

		}
    	
    }
    
//    OnLongClickListener scrobbleDroidLongClickListener = new OnLongClickListener(){
//
//		@Override
//		public boolean onLongClick(View v) {
//			Intent i = new Intent(Intent.ACTION_VIEW, 
//					Uri.parse("market://search?q=pname:net.jjc1138.android.scrobbler"));
//			startActivity(i);
//			return true;
//		}
//    	
//    };

	
    /*************************************
     * 
     * ShowArtWhileScrolling Listener
     * 
     *************************************/
    public class ShowArtWhileScrollingChangeListener implements OnPreferenceChangeListener {

		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {
//			Log.i("SETTINGS", "Changed show_art_while_scrobbling to "+ newValue);
//			SharedPreferences settings = getSharedPreferences(Filex.PREFS_NAME, 0);
//			Editor edit = settings.edit();
//			edit.putBoolean(KEY_TOGGLE_SHOW_ART_WHILE_SCROLLING, (Boolean) newValue);
//			edit.commit();
			
			RockOnPreferenceManager settings = new RockOnPreferenceManager(FILEX_PREFERENCES_PATH);
			settings.putBoolean(KEY_TOGGLE_SHOW_ART_WHILE_SCROLLING, (Boolean) newValue);

	        return true;
		}
    	
    }

    /*************************************
     * 
     * ShowFrame Listener
     * 
     *************************************/
    public class ShowFrameChangeListener implements OnPreferenceChangeListener {

		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			Log.i("SETTINGS", "Changed show frame to "+ newValue);
//			SharedPreferences settings = getSharedPreferences(Filex.PREFS_NAME, 0);
//			Editor edit = settings.edit();
//			edit.putBoolean(KEY_TOGGLE_SHOW_FRAME, (Boolean) newValue);
//			edit.commit();
//			
			RockOnPreferenceManager settings = new RockOnPreferenceManager(FILEX_PREFERENCES_PATH);
			settings.putBoolean(KEY_TOGGLE_SHOW_FRAME, (Boolean) newValue);

	        return true;
		}
    	
    }
    
    /*************************************
     * 
     * AlwaysLandscape Listener
     * 
     *************************************/
    public class AlwaysLandscapeChangeListener implements OnPreferenceChangeListener {

		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			Log.i("SETTINGS", "Changed show frame to "+ newValue);
//			SharedPreferences settings = getSharedPreferences(Filex.PREFS_NAME, 0);
//			Editor edit = settings.edit();
//			edit.putBoolean(KEY_TOGGLE_ALWAYS_LANDSCAPE, (Boolean) newValue);
//			edit.commit();
			
			RockOnPreferenceManager settings = new RockOnPreferenceManager(FILEX_PREFERENCES_PATH);
			settings.putBoolean(KEY_TOGGLE_ALWAYS_LANDSCAPE, (Boolean) newValue);
			
			/*
			 * uncheck landscape if this is set
			 */
			if((Boolean) newValue == true){
		    	((CheckBoxPreference) findPreference(KEY_TOGGLE_AUTO_ROTATE))
					.setChecked(false);
				settings.putBoolean(KEY_TOGGLE_AUTO_ROTATE, false);
			}

			
	        return true;
		}
    	
    }

    /*************************************
     * 
     * AutoRotate Listener
     * 
     *************************************/
    public class AutoRotateChangeListener implements OnPreferenceChangeListener {

		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			Log.i("SETTINGS", "Changed show frame to "+ newValue);
//			SharedPreferences settings = getSharedPreferences(Filex.PREFS_NAME, 0);
//			Editor edit = settings.edit();
//			edit.putBoolean(KEY_TOGGLE_ALWAYS_LANDSCAPE, (Boolean) newValue);
//			edit.commit();
			
			RockOnPreferenceManager settings = new RockOnPreferenceManager(FILEX_PREFERENCES_PATH);
			settings.putBoolean(KEY_TOGGLE_AUTO_ROTATE, (Boolean) newValue);
			
			/*
			 * uncheck landscape if this is set
			 */
			if((Boolean) newValue == true){
		    	((CheckBoxPreference) findPreference(KEY_TOGGLE_ALWAYS_LANDSCAPE))
					.setChecked(false);
//				((CheckBoxPreference) findPreference(KEY_TOGGLE_ALWAYS_LANDSCAPE))
//					.setOnPreferenceChangeListener(new AlwaysLandscapeChangeListener());
		    	settings.putBoolean(KEY_TOGGLE_ALWAYS_LANDSCAPE, false);
			}

			
	        return true;
		}
    	
    }
    
    /*************************************
     * 
     * CustomBackground Listener
     * 
     *************************************/
    public class CustomBackgroundChangeListener implements OnPreferenceChangeListener {

		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			Log.i("SETTINGS", "Changed custom background to "+ newValue);
//			SharedPreferences settings = getSharedPreferences(Filex.PREFS_NAME, 0);
//			Editor edit = settings.edit();
//			edit.putBoolean(KEY_TOGGLE_CUSTOM_BACKGROUND, (Boolean) newValue);
//			edit.commit();
			
			RockOnPreferenceManager settings = new RockOnPreferenceManager(FILEX_PREFERENCES_PATH);
			settings.putBoolean(KEY_TOGGLE_CUSTOM_BACKGROUND, (Boolean) newValue);
						
	        return true;
		}
    	
    }
    
    
    String[] IMAGE_COLS = {
    		MediaStore.Images.Thumbnails._ID,
    		MediaStore.Images.Thumbnails.IMAGE_ID,
    		MediaStore.Images.Thumbnails.DATA,
    		MediaStore.Images.Thumbnails.WIDTH
		};
    String[] BIG_IMAGE_COLS = {
    		MediaStore.Images.Media._ID,
    		MediaStore.Images.Media.DATA
		};
    String[] cursorFields = {
    		MediaStore.Images.Thumbnails.DATA,
    		MediaStore.Images.Thumbnails.DATA
    };
    int[]	layoutIds = {
    		R.id.image,
    		R.id.image_title
    };
    
    /************************************
     * 
     * CustomBackground Portrait
     * 
     ************************************/
    public class CustomBackgroundChooserClickListener implements OnPreferenceClickListener {

		DialogInterface.OnClickListener imageDialogClickListener = new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.i("DGB", ""+which);
				RockOnPreferenceManager settiings = new RockOnPreferenceManager(FILEX_PREFERENCES_PATH);
				imageCursor.moveToPosition(which);
				
				/*
				 * Get the big picture not the Thumbnail
				 */
//				String whereClause = 
//					MediaStore.Images.Media._ID + 
//					"=" + 
//					imageCursor.getString(
//    					imageCursor.getColumnIndexOrThrow(
//    							MediaStore.Images.Thumbnails._ID)); 
//				Cursor bigImageCursor = getContentResolver().query(
//						MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//						BIG_IMAGE_COLS,
//						whereClause,
//						null,
//						null);
//				bigImageCursor.moveToFirst();
//				String bigImagePath = bigImageCursor.getString(
//						bigImageCursor.getColumnIndexOrThrow(
//								MediaStore.Images.Media.DATA));
//				Log.i("DBG", bigImagePath);
				
				try{
					/*
					 * Load screen dimensions
					 */
					WindowManager windowManager 		= (WindowManager) 
						getSystemService(Context.WINDOW_SERVICE);
					Display display						= windowManager.getDefaultDisplay();
					
					int width=1;
					int height=1;
					if(orientation.equals(KEY_TOGGLE_CUSTOM_BACKGROUND_PORTRAIT)){
						if(display.getOrientation() == 0){
							width = display.getWidth();
							height = display.getHeight() - 20;
						} else {
							width = display.getHeight();
							height = display.getWidth() - 20;
						}
					} else if (orientation.equals(KEY_TOGGLE_CUSTOM_BACKGROUND_LANDSCAPE)){
						if(display.getOrientation() == 0){
							width = display.getHeight();
							height = display.getWidth() - 20;
						} else {
							width = display.getWidth();
							height = display.getHeight() - 20;
						}
					}
					
					
					/* 
					 * Load the Bitmap and save it to the disk with right dimensions 
					 */
			    				    			    	
			    	BitmapFactory.Options opts = new BitmapFactory.Options();
			    	opts.inJustDecodeBounds = true;
			    	BitmapFactory.decodeFile(
			    			imageCursor.getString(
			    					imageCursor.getColumnIndexOrThrow(
			    							MediaStore.Images.Thumbnails.DATA)),
//			    			bigImagePath,
			    			opts);
			    	int sampling = Math.max(
			    			1, 
			    			(int)Math.min(
			    					(float)opts.outWidth/width, 
			    					(float)opts.outHeight/height)); // TODO:::::
			    	opts.inJustDecodeBounds = false;
			    	Bitmap bgBitmap = null;
			    	
			    	if((float)opts.outWidth/(float)width < (float)opts.outHeight/(float)height){
					
			    		int newHeight = (int)Math.round((float)opts.outHeight*((float)width/(float)opts.outWidth));
			    		Bitmap tmpBitmap;
			    		tmpBitmap = BitmapFactory.decodeStream(
		    						new FileInputStream(
		    								imageCursor.getString(
		    									imageCursor.getColumnIndexOrThrow(
		    										MediaStore.Images.Thumbnails.DATA))),
//		    								bigImagePath),
					    					null, 
					    					opts);
			    		tmpBitmap = Bitmap.createScaledBitmap(
			    				tmpBitmap, 
			    				width, 
			    				newHeight,
			    				false);
			    		bgBitmap = Bitmap.createBitmap(
			    				tmpBitmap,
			    				0,
			    				(newHeight-height)/2,
			    				width,
			    				height,
			    				null,
			    				false);
			    	
			    	} else {
						
			    		int newWidth = (int)Math.round((float)opts.outWidth*((float)height/(float)opts.outHeight));
						Log.i("DBG", "newWidth="+newWidth+"outWidth="+opts.outWidth+
								"height="+height+"outHeight"+opts.outHeight);
			    		Bitmap tmpBitmap = BitmapFactory.decodeStream(
	    						new FileInputStream(
	    								imageCursor.getString(
	    									imageCursor.getColumnIndexOrThrow(
	    										MediaStore.Images.Thumbnails.DATA))),
//	    								bigImagePath),
				    					null, 
				    					opts);
			    		tmpBitmap = Bitmap.createScaledBitmap(
			    				tmpBitmap, 
			    				newWidth, 
			    				height,
			    				false);
			    		bgBitmap = Bitmap.createBitmap(
			    				tmpBitmap,
			    				(int)Math.max(
			    						0,
			    						(newWidth-width)/2),			    						
			    				0,
			    				(int)Math.min(
			    						width,
			    						newWidth),
			    				height,
			    				null,
			    				false);
			    	}
		    	
			    	/*
			    	 * Write bitmap to file
			    	 */
			    	String bgPath = null;
					if(orientation.equals(KEY_TOGGLE_CUSTOM_BACKGROUND_PORTRAIT)){
						bgPath = FILEX_BACKGROUND_PATH + KEY_TOGGLE_CUSTOM_BACKGROUND_PORTRAIT;
	//					settings.putString(KEY_TOGGLE_CUSTOM_BACKGROUND_PORTRAIT, 
	//							imageCursor.getString(imageCursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA)));
					} else {
						bgPath = FILEX_BACKGROUND_PATH + KEY_TOGGLE_CUSTOM_BACKGROUND_LANDSCAPE;
	//					settings.putString(KEY_TOGGLE_CUSTOM_BACKGROUND_LANDSCAPE, 
	//							imageCursor.getString(imageCursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA)));
					}

			    	File outputFile = new File(bgPath);
			    	outputFile.createNewFile();
			    	FileOutputStream outputFileStream = new FileOutputStream(outputFile);
			    	
			    	bgBitmap.compress(CompressFormat.JPEG,
			    			95,
			    			new FileOutputStream(outputFile));
			    	outputFileStream.close();
			    	
			    	
//			    	findViewById(R.id.songfest_container)
//		    			.setBackgroundDrawable(new BitmapDrawable(bgBitmap));
				} catch(Exception e){
					e.printStackTrace();
				}
				

				
				
	    		
//	    		if(display.getOrientation() == 0)
//		    		bgPath = settings.getString(PREFS_CUSTOM_BACKGROUND_PORTRAIT, null);
//		    	else
//		    		bgPath = settings.getString(PREFS_CUSTOM_BACKGROUND_LANDSCAPE, null);
//	    		
//	    		RockOnPreferenceManager settings = new RockOnPreferenceManager(FILEX_PREFERENCES_PATH);

	    		
	    		
				imageCursor.close();
			}
		};
		
		String orientation;
		
		public CustomBackgroundChooserClickListener(String orientation){
			this.orientation = orientation;
		}
		
		Cursor imageCursor;
    	
		@Override
		public boolean onPreferenceClick(Preference preference) {
			
			//ListView imageList = new ListView(RockOnSettings.this.getApplicationContext());
			//imageList.setOnItemClickListener(imageListClickListener);
			imageCursor = RockOnSettings.this.getContentResolver().query(
					MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
					IMAGE_COLS,
					null,
					null, 
					null);
			ImageCursorAdapter imageAdapter = new ImageCursorAdapter(
					RockOnSettings.this,
					R.layout.image_list_item,
					imageCursor,
					cursorFields,
					layoutIds);
			
			AlertDialog.Builder imageDialog = new AlertDialog.Builder(RockOnSettings.this);
			imageDialog.setAdapter(imageAdapter, imageDialogClickListener);
//			imageDialog.setSingleChoiceItems(imageAdapter, 2, imageDialogClickListener);
			
			imageDialog.show();
			
			// new pop up window with adapter with images...
			
			return true;
    
		}
    }
}

