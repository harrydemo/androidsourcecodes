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
import java.util.List;

import org.andrico.andrico.content.Contact;
import org.andrico.andrico.content.DBContact;
import org.andrico.andrico.facebook.AuthorizationActivity;
import org.andrico.andrico.facebook.LoginActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gdata.data.Feed;
import com.google.gdata.util.ServiceException;




public class ContactInfo extends Activity
{
    /** Called when the activity is first created. */
    private static String fbid = "";
	
	final static String LOG = "ContactInfo";
	public static Feed resultFeed = null;
	int viewStatus = 0;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	    Window  w = getWindow(); 
	    w.requestFeature(Window.FEATURE_LEFT_ICON);   
	    setContentView(R.layout.contact_info);
	    w.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_andrico);
	        
	    Intent i = this.getIntent();
	    
	    if(i.hasExtra("fbid"))
	    {
	        fbid = i.getStringExtra("fbid");
	    }
	    else
	    {
	    	AlertDialog dialog = new AlertDialog.Builder(ContactInfo.this)
			.setTitle("FAILED")
			.setMessage("UNKNOWN ERROR OCCURED")
			.setPositiveButton("OK", 
				new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int whichButton)
					{
						dialog.dismiss();
						Intent j = new Intent(ContactInfo.this, ContactList.class);
						startActivity(j);
						finish();
					}
				}).create(); 
	        	
	    	dialog.show(); 
	    }
	        
	    DBContact db = new DBContact();    
	    Contact contact = db.getContactByFBid(ContactInfo.this, fbid); 
	    
	    if (contact == null)
	    {
	    	AlertDialog dialog = new AlertDialog.Builder(ContactInfo.this)
			.setTitle("FAILED")
			.setMessage("CAN'T FIND THE CONTACT")
			.setPositiveButton("OK", 
						new DialogInterface.OnClickListener() 
						{
							public void onClick(DialogInterface dialog, int whichButton)
							{
								dialog.dismiss();
								Intent j = new Intent(ContactInfo.this, ContactList.class);
								startActivity(j);
								finish();
							}
						}).create(); 
	        	
	    	dialog.show(); 
	    } 
	    else
	    {
	    	((TextView) this.findViewById(R.id.name)).setText(contact.getName() + " " + contact.getSecondName());
	    	((TextView) this.findViewById(R.id.url)).setText(contact.getPage());
	    	
	    	if (!contact.getDateOfBirth().equals(""))
	    	{
	    		((TextView) this.findViewById(R.id.date)).setText(contact.getDateOfBirth());
	    	}
	    	else
	    	{
	    		((TextView) this.findViewById(R.id.date)).setText("date of birth isn't avaliable");
	    	}
	    	if (!contact.getAdress().equals(""))
	    	{
	    		this.findViewById(R.id.goToAdress).setEnabled(true);
	    		((TextView) this.findViewById(R.id.adress)).setText(contact.getAdress());
	    	}
	    	else
	    	{
	    		this.findViewById(R.id.goToAdress).setEnabled(false);
	    		((TextView) this.findViewById(R.id.adress)).setText("no adress avaliable");
	    	}
	    	
	    	((Button) this.findViewById(R.id.page)).setText("GO TO FACEBOOK PAGE");
	    	((Button) this.findViewById(R.id.goToAdress)).setText("SHOW ADDRESS ON MAP");
	    	
	    	if (contact.getPhoto()!=null)
	    	{
	    		Bitmap bm = BitmapFactory.decodeByteArray(contact.getPhoto(), 0, contact.getPhoto().length);
	    		
	    		if (bm != null)
	    		{
	    			((ImageView) this.findViewById(R.id.photo)).setImageBitmap(bm);
	    		}
	    	}
	    	
	    	this.findViewById(R.id.mainLayout).setVisibility(View.VISIBLE);    	
	    }
	    
	    this.findViewById(R.id.page).setOnClickListener(new OnClickListener()
	        {
	        	public void onClick(View v)
				{  		
	        		String path = (String)((TextView) ContactInfo.this.findViewById(R.id.url)).getText();
	        		
	        		Intent i = new Intent(ContactInfo.this, WebActivity.class);
	        		i.putExtra("url", path);
	        		Log.d(LOG, "loadUrl: " + path.toString());
	        		startActivity(i); 
	       		}
			});
	    
	    this.findViewById(R.id.goToAdress).setOnClickListener(new OnClickListener()
        	{
        		public void onClick(View v)
        		{  		
        			String address = (String)((TextView) ContactInfo.this.findViewById(R.id.adress)).getText();
        			String path = "http://maps.google.com/?q=" + address + "&output=html&zoom=7";
        			
        			Intent i = new Intent(ContactInfo.this, WebActivity.class);
	        		i.putExtra("url", path);
	        		Log.d(LOG, "loadUrl: " + path.toString());
	        		startActivity(i);
        		
	        	/*	Geocoder geo = new Geocoder(ContactInfo.this);
        			final Integer MAX_RESULTS = 1;
        		
        			try 
        			{
        				List<Address> results = geo.getFromLocationName(address, MAX_RESULTS);
        				
        				
        				
					} 
        			catch (IOException e) 
        			{
						e.printStackTrace();
						AlertDialog dialog = new AlertDialog.Builder(ContactInfo.this)
										.setTitle("FAILED")
										.setMessage("can't find the location")
										.setPositiveButton("OK", 
												new DialogInterface.OnClickListener() 
										{
											public void onClick(DialogInterface dialog, int whichButton)
											{
												dialog.dismiss();
											}
										}).create(); 
						dialog.show(); 
					}*/
        		}
		});
	}
		
	public boolean onKeyDown(int keyCode, KeyEvent event) 
    { 
    	if(keyCode==KeyEvent.KEYCODE_BACK)
    	{
    		Intent i = new Intent(ContactInfo.this,ContactList.class);
    		startActivity(i);
            finish();
            return true;
    	}
		return false; 
	}
}