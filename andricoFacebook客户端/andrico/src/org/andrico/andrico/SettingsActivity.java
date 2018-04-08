 /*************************************************************************** 
 *              Copyright (C) 2009 Andrico Team                             * 
 *              http://code.google.com/p/andrico/                           *
 *                             												*
 * Licensed under the Apache License, Version 2.0 (the "License");			*
 * you may not use this file except in compliance with the License.			*
 * 																			*	
 * You may obtain a copy of the License at 									*
 * http://www.apache.org/licenses/LICENSE-2.0								*
 *																			*
 * Unless required by applicable law or agreed to in writing, software		*
 * distributed under the License is distributed on an "AS IS" BASIS,		*
 *																			*
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.	*
 * See the License for the specific language governing permissions and		*
 * limitations under the License.											*
 ****************************************************************************/

package org.andrico.andrico;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import org.andrico.andrico.content.Contact;
import org.andrico.andrico.content.DBContact;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.google.gdata.data.Feed;
import com.google.gdata.util.ServiceException;




public class SettingsActivity extends Activity 
{
	final static String TAG = "Settings";
	private SharedPreferences SharedPreferences;
	
	
		@Override
	    public void onCreate(Bundle savedInstanceState)
		{
	        super.onCreate(savedInstanceState);
	        Window  w = getWindow(); 
	        w.requestFeature(Window.FEATURE_LEFT_ICON);   
	        setContentView(R.layout.settings);
	        w.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_andrico);
	        Intent i = this.getIntent();
	        
	        CheckBox delBox = (CheckBox)this.findViewById(R.id.DeleteContacts);
	        CheckBox picBox = (CheckBox)this.findViewById(R.id.DownloadPic);
	        
	        SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);  
	        
	        if (SharedPreferences.getString(Preferences.DELETE_CONTACTS, "no").equals("no"))
	        {
	        	delBox.setChecked(true);
	        }
	        else
	        {
	        	delBox.setChecked(false);
	        }
	        
	        
	        if (SharedPreferences.getString(Preferences.SYNCH_PHOTOS, "yes").equals("yes"))
	        {
	        	picBox.setChecked(true);
	        }
	        else
	        {
	        	picBox.setChecked(false);
	        }
	        
	        
	        delBox.setOnCheckedChangeListener(new OnCheckedChangeListener()
	        {	    
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) 
				{
					SharedPreferences.Editor editor = SharedPreferences.edit();
					if (SharedPreferences.getString(Preferences.DELETE_CONTACTS, "no").equals("no"))
					{
						editor.putString(Preferences.DELETE_CONTACTS, "yes");
					}
					else
					{
						editor.putString(Preferences.DELETE_CONTACTS, "no");
					}
					
					editor.commit();
				}
			}); 
	        
	        
	        picBox.setOnCheckedChangeListener(new OnCheckedChangeListener()
	        {	    
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) 
				{
					SharedPreferences.Editor editor = SharedPreferences.edit();
					if (SharedPreferences.getString(Preferences.SYNCH_PHOTOS, "yes").equals("yes"))
					{
						editor.putString(Preferences.SYNCH_PHOTOS, "no");
					}
					else
					{
						editor.putString(Preferences.SYNCH_PHOTOS, "yes");
					}
					
					editor.commit();
				}
			});

	        
	        this.findViewById(R.id.BackToMenu).setOnClickListener(new OnClickListener()
	        {
				public void onClick(View v)
				{
					Intent i = new Intent(SettingsActivity.this,MainActivity.class);
	                startActivity(i);
	                finish();
	       		}
			});
	    }
		
		public boolean onKeyDown(int keyCode, KeyEvent event) 
	    { 
	    	if(keyCode==KeyEvent.KEYCODE_BACK)
	    	{
	    		Intent i = new Intent(SettingsActivity.this,MainActivity.class);
	    		startActivity(i);
	            finish();
	            return true;
	    	}
			return false; 
		}
}