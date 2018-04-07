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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.andrico.andrico.content.Contact;
import org.andrico.andrico.content.DBContact;
import org.andrico.andrico.facebook.FB;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnKeyListener;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;


public class Synchronize extends Activity 
{
	private static final String TAG = "Synch Activity";
    
    private Context mContext;
    private FB mFacebook;
    private SharedPreferences SharedPreferences;
    
    public static final int DIALOG_SYNCHRONIZE = 0;
    private static final int FACEBOOK_LOGIN_REQUEST_CODE = 3;
    private static final int MESSAGE_FRIEND_CONTACTS_UPDATE = 0;
    private UiHandler mHandler;
    private Handler mBackgroundHandler;
    private static final String FRIENDS_CONTACTS_UPDATES_FQL = "SELECT uid, name, first_name, last_name, " +
    					"current_location, birthday, birthday_date, profile_url, pic FROM user WHERE uid IN " +
    					"(SELECT uid2 FROM friend WHERE uid1=";
    private List<Bundle> newList;
    public static String tokenMessage;
    private boolean beClicked;
    
    
    
    
    
    // -------------------------------------------------------------------------
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	super.onCreate(savedInstanceState);
    	
    	Window  w = getWindow(); 
        w.requestFeature(Window.FEATURE_LEFT_ICON);   
        setContentView(R.layout.synchronize);
        w.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_andrico);
        
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        mContext = this;
        beClicked = true;
        
        SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
       
        mFacebook = new FB(getString(R.string.facebook_api_key),
                getString(R.string.facebook_secret_key));
        
        mFacebook.setSession(
        		SharedPreferences.getString(Preferences.FACEBOOK_CRED_SESSION_KEY, "facebook_cred_session_key"),
        		SharedPreferences.getString(Preferences.FACEBOOK_CRED_SECRET, "facebook_cred_secret"), 
        		SharedPreferences.getString(Preferences.FACEBOOK_CRED_UID, "facebook_cred_uid"));
        
        mHandler = new AndricoHandler(/*mContext*/ getApplicationContext(), mFacebook);
        
        if (SharedPreferences.getString(Preferences.FACEBOOK_CRED_SESSION_KEY, "facebook_cred_session_key") != "facebook_cred_session_key")
        {
        	this.findViewById(R.id.Synch).setEnabled(true);
        }
        else
        {
        	this.findViewById(R.id.Synch).setEnabled(false);
        }
        
        DBContact db = new DBContact();
        LinkedList <Contact> conts = db.getContactList(Synchronize.this);
        if (conts == null)
        {
        	this.findViewById(R.id.Clear).setEnabled(false);
        }
        else
        {
        	this.findViewById(R.id.Clear).setEnabled(true);
        }
      
        
        this.findViewById(R.id.Clear).setOnClickListener(new OnClickListener()
        {
			public void onClick(View v)
			{
				AlertDialog dialog = new AlertDialog.Builder(Synchronize.this)
				.setTitle("ARE YOU SURE")
				.setMessage("DO YOU REALLY WANT TO CLEAR YOUR CONTACT LIST?")
				.setPositiveButton("YES", 
						new DialogInterface.OnClickListener() 
						{
							public void onClick(DialogInterface dialog, int whichButton)
							{
								DBContact db = new DBContact();
								db.deleteContacts(Synchronize.this);
								Synchronize.this.findViewById(R.id.Clear).setEnabled(false);
								dialog.dismiss();
							}
						})
				.setNegativeButton("NO", 
						new DialogInterface.OnClickListener() 
						{
							public void onClick(DialogInterface dialog, int whichButton)
							{
								dialog.dismiss();
							}
						}).create(); 
	        	dialog.show();
       		}
		});
        
        this.findViewById(R.id.BackToMenu).setOnClickListener(new OnClickListener()
        {
			public void onClick(View v)
			{
				Intent i = new Intent(Synchronize.this,MainActivity.class);
				startActivity(i);
                finish();
       		}
		}); 
        
        this.findViewById(R.id.LogIn).setOnClickListener(new OnClickListener()
        {
        	public void onClick(View v)
			{
        		tokenMessage = "TOKEN WASN'T CREATED";
				startActivityForResult(mFacebook.createLoginActivityIntent(mContext), FACEBOOK_LOGIN_REQUEST_CODE);
        	}
		});
        
        this.findViewById(R.id.Synch).setOnClickListener(new OnClickListener()
        {
			public void onClick(View v)
			{
				if (beClicked)
				{
					beClicked =false;
					showDialog(DIALOG_SYNCHRONIZE);
					buildBackgroundHandler();
					postToBackgroundHandler(new FbExecuteGetAllDataRunnable(mHandler, mFacebook));
				}
        	}
		});
    }
    
	private void setPrefsFromFacebookSession() 
	{
        SharedPreferences.Editor editor = SharedPreferences.edit();

        if (mFacebook.getSession() != null) 
        {
            Log.d(TAG, "Saving session to preferences.");
            FB.Session session = mFacebook.getSession();
            editor.putString(Preferences.FACEBOOK_CRED_SESSION_KEY, session.getSession());
            editor.putString(Preferences.FACEBOOK_CRED_SECRET, session.getSecret());
            editor.putString(Preferences.FACEBOOK_CRED_UID, session.getUid());
        } 
        else 
        {
            editor.remove(Preferences.FACEBOOK_CRED_SESSION_KEY);
            editor.remove(Preferences.FACEBOOK_CRED_SECRET);
            editor.remove(Preferences.FACEBOOK_CRED_UID);
        }
        editor.commit();
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) 
    {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult");
        if (mFacebook.handleLoginActivityResult(this, resultCode, data)) 
        {
        	AlertDialog dialog = new AlertDialog.Builder(Synchronize.this)
			.setTitle("LOGGED IN")
			.setMessage(tokenMessage)
			.setPositiveButton("OK", 
					new DialogInterface.OnClickListener() 
					{
						public void onClick(DialogInterface dialog, int whichButton)
						{
							dialog.dismiss();
						}
					}).create(); 
        	dialog.show();
        	setPrefsFromFacebookSession();
        	this.findViewById(R.id.Synch).setEnabled(true);                	
        } 
        else 
        {
        	AlertDialog dialog = new AlertDialog.Builder(Synchronize.this)
			.setTitle("FAILED")
			.setMessage(tokenMessage)
			.setPositiveButton("OK", 
					new DialogInterface.OnClickListener() 
					{
						public void onClick(DialogInterface dialog, int whichButton)
						{
							dialog.dismiss();
						}
					}).create(); 
        	dialog.show();
            
        	this.findViewById(R.id.Synch).setEnabled(false);
            
        	//Unset the user session.
            mFacebook.unsetSession();
            SharedPreferences.Editor editor = SharedPreferences.edit();
            editor.remove(Preferences.FACEBOOK_CRED_SESSION_KEY);
            editor.remove(Preferences.FACEBOOK_CRED_SECRET);
            editor.remove(Preferences.FACEBOOK_CRED_UID);
            editor.commit();
        }            
    }				
    
    public boolean onKeyDown(int keyCode, KeyEvent event) 
    { 
    	if(keyCode==KeyEvent.KEYCODE_BACK)
    	{
    		Intent i = new Intent(Synchronize.this,MainActivity.class);
    		startActivity(i);
            finish();
            return true;
    	}
		return false; 
	}

    
    
    
    
    
    
    // Creating ProgressBar Dialog
    
    @Override
    public Dialog onCreateDialog(int id) 
    {        
        ProgressDialog dialog = new ProgressDialog(this);
        switch (id) 
        {
            case DIALOG_SYNCHRONIZE:
                dialog.setTitle("SYNCHRONIZING");
                dialog.setIndeterminate(true);
                dialog.setOnKeyListener(new OnKeyListener()
                {
					public boolean onKey(DialogInterface arg0, int keyCode,
							KeyEvent event) 
					{
						if(keyCode==KeyEvent.KEYCODE_BACK)
				    	{
							//lock it
							return true;
				    	}
						return false;
					}
        		});
                
                return dialog;    
        }
        return null;
    }

    @Override
    public void onPrepareDialog(int id, Dialog dialog) 
    {
        switch (id) 
        {
            case DIALOG_SYNCHRONIZE:
            	            	               
            	((ProgressDialog)dialog).setMessage("LOADING FRIENDS INFO");
                        
                break;    
        }
    }
    
    
    String createAdress(String zip, String country, String state, String city)
    {
    	String adr = "";
    	
    	if (zip != "" && zip != null)
    	{
    		adr = zip;
    	}
    	
    	if (country != "" && country != null)
    	{
    		if (adr != "")
    		{
    			adr = adr + ", " + country;
    		}
    		else
    		{
    			adr = country;
    		}
    	}
    	
    	if (state != "" && state != null)
    	{
    		if (adr != "")
    		{
    			adr = adr + ", " + state;
    		}
    		else
    		{
    			adr = state;
    		}
    	}
    	
    	if (city != "" && city != null)
    	{
    		if (adr != "")
    		{
    			adr = adr + ", " + city;
    		}
    		else
    		{
    			adr = city;
    		}
    	}
    	
    	return adr;
    }
    
    
    //---------------------------------------------------------------------------------------
    private void postToBackgroundHandler(Runnable runnable) 
    {
        postToBackgroundHandler(runnable, 0);
    }

    /**
     * @param r
     * @param delayMillis if -1, post at front of queue, for values > 0 post
     *            with a delay.
     */
    private void postToBackgroundHandler(final Runnable r, final int delayMillis) 
    {
        if (mFacebook.getSession() == null) 
        {
            return;
        }
      
        if (delayMillis > 0) {
            mBackgroundHandler.postDelayed(new Runnable() 
            {
            	public void run() 
            	{
                    mBackgroundHandler.postDelayed(r, delayMillis);
                }
            }, delayMillis);
        } 
        else if (delayMillis == -1) 
        {
            mBackgroundHandler.postAtFrontOfQueue(r);
        } 
        else 
        {
            mBackgroundHandler.post(r);
        }
    }
    
    private void buildBackgroundHandler() 
    {
        // Start up the thread running fb requests. Note that we create a
        // separate thread because we don't want to block.
        HandlerThread thread = new HandlerThread(TAG, Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        Looper fbLooper = thread.getLooper();
        mBackgroundHandler = new Handler(fbLooper);
    }
    
    //---------------------------------------------------------------------------
    private static abstract class FbRunnable implements Runnable 
    {
        final WeakReference<FB> mFacebookWeakRef;
        final WeakReference<UiHandler> mUiHandlerWeakRef;

        private FbRunnable(UiHandler handler, FB facebook) 
        {
            this.mUiHandlerWeakRef = new WeakReference<UiHandler>(handler);
            this.mFacebookWeakRef = new WeakReference<FB>(facebook);
        }

        String getGetFriendsStatusUpdatesFql() 
        {
            FB facebook = mFacebookWeakRef.get();
            if (facebook == null) 
            {
                return null;
            }
            StringBuffer friendQuery = new StringBuffer();
            friendQuery.append(FRIENDS_CONTACTS_UPDATES_FQL);
            friendQuery.append(facebook.getSession().getUid());
            friendQuery.append(")");
            return friendQuery.toString();
        }
    }

    private static class FbExecuteGetAllDataRunnable extends FbRunnable 
    {
        public FbExecuteGetAllDataRunnable(UiHandler handler, FB facebook) 
        {
            super(handler, facebook);
        }

        public void run() 
        {
            UiHandler handler = mUiHandlerWeakRef.get();
            FB facebook = mFacebookWeakRef.get();
            if (facebook == null || handler == null) 
            {
                return;
            }
            handler.executeMethodForMessage(facebook.fql_query(getGetFriendsStatusUpdatesFql()),
                    MESSAGE_FRIEND_CONTACTS_UPDATE);
        }
    }
     
    private class AndricoHandler extends UiHandler 
    {
       // private final Random mRandom = new Random();

        AndricoHandler(Context context, FB facebook) 
        {
            super(context, facebook);
        }

        void handleGetFriendsStatusUpdatesMessage(Message msg) 
        {
            String result = msg.getData().getString("result");
            Integer errorCode = JsonParser.parseForErrorCode(result);
         //   ProgressDialog dialogPic;
            if (errorCode != null && errorCode == -1) 
            {
                try 
                {
                    Log.d(TAG, "Setting status list from json result:" + result);
                    JSONArray jsonResult = new JSONArray(result);
                    
                    newList = new Vector<Bundle>();
                    for (int i = 0; i < result.length(); i++) {
                        try 
                        {
                            JSONObject obj = jsonResult.getJSONObject(i);
                            
                            Bundle update = new Bundle();
                                
                            update.putString("name", obj.optString("name"));
                            update.putString("birthday", obj.optString("birthday"));
                            update.putString("birthdayDate", obj.optString("birthday_date"));
                            update.putString("profileUrl", obj.optString("profile_url"));
                            update.putString("pic", obj.optString("pic"));
                            update.putString("firstName",obj.optString("first_name"));
                            update.putString("lastName", obj.optString("last_name"));
                            update.putString("location_zip", (String)obj.optJSONObject("current_location").get("zip"));
                            
                            if (obj.optJSONObject("current_location").has("country"))
                            {
                            	update.putString("location_country", (String)obj.optJSONObject("current_location").get("country"));
                            }
                                
                            if (obj.optJSONObject("current_location").has("state"))
                            {
                                update.putString("location_state", (String)obj.optJSONObject("current_location").get("state"));
                            }
                                
                            if (obj.optJSONObject("current_location").has("city"))
                            {
                                update.putString("location_city", (String)obj.optJSONObject("current_location").get("city"));
                            }
                                
                            update.putString("uid", obj.optString("uid"));
                            newList.add(update);
                        } 
                        catch (NullPointerException e) 
                        {
                        } 
                        catch (JSONException e) 
                        {
                        }
                    }
                    
                                        
                    LinkedList <Contact> friends = new LinkedList<Contact>();
    				DBContact db = new DBContact();
    				try
    				{
    					int size = newList.size();
    				
    					for(int i = 0; i<size; i++)
    					{
    						Bundle bundContact = null;
    						bundContact = newList.get(i);
    						String adress = createAdress(bundContact.getString("location_zip"), 
    								bundContact.getString("location_country"), 
    								bundContact.getString("location_state"),
    								bundContact.getString("location_city"));
    								
    						Contact contact = new Contact();
    					
    						contact.setAdress(adress);
    						contact.setDateOfBirth(bundContact.getString("birthday"));
    						contact.setFBid(bundContact.getString("uid"));
    						contact.setName(bundContact.getString("firstName"));
    						contact.setSecondName(bundContact.getString("lastName"));
    						contact.setPage(bundContact.getString("profileUrl"));
    						contact.setPic(bundContact.getString("pic"));
    						
    						friends.add(contact);
    					}
    				
    					if (SharedPreferences.getString(Preferences.DELETE_CONTACTS, "no").equals("no"))
    					{
    						Log.d(TAG, "Starting synch");
    						db.synchronize(Synchronize.this, friends);
    					}
    					else    					
    					{
    						Log.d(TAG, "Starting del synch");
    						db.synchronizeDel(Synchronize.this, friends);
    					}
    					
    					
                        Log.d(TAG, "Contacts synchronized");
    					
                        Log.d(TAG, "Starting synchronize photos");
    			        
            			Integer picturesToSynch = 0;
    					Integer picturesSynched = 0;
    					
    					if (SharedPreferences.getString(Preferences.SYNCH_PHOTOS, "yes").equals("yes"))
    					{
    						for(int i = 0; i<size; i++)
    						{
    							Contact newContact = friends.get(i);
    							Contact contact = db.getContactByFBid(Synchronize.this, newContact.getFBid());
    							if (contact != null)
    							{
    								if (!contact.getPic().equals(newContact.getPic())&& 
    																		!newContact.getPic().equals(""))
    								{
    									picturesToSynch++; 
    									try 
    									{ 
    										URL picPath = new URL(newContact.getPic()); 
    										URLConnection con = picPath.openConnection();
    										con.setConnectTimeout(2500);
    										con.connect(); 
    					                    
    										InputStream is = con.getInputStream(); 
    										BufferedInputStream bis = new BufferedInputStream(is); 
    					                    
    										Bitmap pic = BitmapFactory.decodeStream(bis); 
    					                    
    										bis.close(); 
    										is.close(); 
    					                    
    										db.synchImage (Synchronize.this, newContact.getFBid(),
    					                    									newContact.getPic(), pic);
    										picturesSynched++;
    									}
    									catch (IOException e) 
    									{ 
    										Log.e(TAG, "Remtoe Image Exception", e);
    									}
    								}
    							}
    						}
    					}	
    					dismissDialog(DIALOG_SYNCHRONIZE);
    					
    					
    					AlertDialog dialog = new AlertDialog.Builder(Synchronize.this)
												.setTitle("SYNCHRONIZED")
												.setMessage(Integer.toString(size) + " friends processed")
												.setPositiveButton("OK", 
							new DialogInterface.OnClickListener() 
												{
													public void onClick(DialogInterface dialog, int whichButton)
													{
														dialog.dismiss();
														Intent i = new Intent(Synchronize.this,MainActivity.class);
														startActivity(i);
														finish();
													}
												}).create(); 
    					dialog.show(); 
    					
    					if (picturesSynched < picturesToSynch)
    					{
    						AlertDialog dialogErrPic = new AlertDialog.Builder(Synchronize.this)
									.setTitle("SORRY")
									.setMessage(Integer.toString(picturesSynched) + " OF REQUIRED " + 
											Integer.toString(picturesToSynch) + " PHOTOS WERE UPDATED. "+
											"CHECK FOR INTERNET CONNECTION, PLEASE.")
									.setPositiveButton("OK", 
									new DialogInterface.OnClickListener() 
									{
										public void onClick(DialogInterface dialog, int whichButton)
										{
											dialog.dismiss();
											
										}
									}).create(); 
    						dialogErrPic.show();
    					}
    					
    					return;
    				}
    				catch (NullPointerException e)
    				{
    					Log.e(TAG,"Failed to synch");
    					errorCode = 123;
    				}
                    // -------------------- end of parsing -------------------------------
                } 
                catch (JSONException jsonArrayConversionException) 
                {
                    errorCode = 0; 
                }
            }

            // We're checking for errorCode -1 because if we got an exception
            // above then the errorCode would indicate a success even though we
            // had a failure of some sort.
           
            dismissDialog(DIALOG_SYNCHRONIZE);
            
            String message = "";
            if (errorCode == null) 
            {
                Log.d(TAG, "handleErrorCode was handed null");  
                message = "PLEASE, CHECK FOR INTERNET CONNECTION";
            } 
            else if (errorCode == 102) 
            {
                message = "SESSION IS INVALID, PLEASE, LOGIN AGAIN";
                mFacebook.unsetSession();
                
                Editor editor = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
                editor.remove(Preferences.FACEBOOK_CRED_SESSION_KEY);
                editor.remove(Preferences.FACEBOOK_CRED_SECRET);
                editor.remove(Preferences.FACEBOOK_CRED_UID);
                editor.commit();
            } 
            else if (errorCode == 0)
            {
            	message = "NO FRIENDS FOUND";
            }
            else 	
            {
                message = "UNKNOWN ERROR OCCURED";
            }
            
            
            AlertDialog dialog = new AlertDialog.Builder(Synchronize.this)
									.setTitle("FAILED")
									.setMessage(message)
									.setPositiveButton("OK", 
											new DialogInterface.OnClickListener() 
											{
												public void onClick(DialogInterface dialog, int whichButton)
												{
													dialog.dismiss();
													beClicked = true;
												}
											}).create(); 
            dialog.show(); 
       }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                 case MESSAGE_FRIEND_CONTACTS_UPDATE:
                    handleGetFriendsStatusUpdatesMessage(msg);
                    break;
                default:
                    break;
            }
        }
        
    }    
}
