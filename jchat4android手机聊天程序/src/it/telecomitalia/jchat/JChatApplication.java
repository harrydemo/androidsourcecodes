package it.telecomitalia.jchat;

import java.util.Random;

import android.app.Application;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * This class is accessibile directly by all activities and it is used to get stored preferences
 * and useful data throughout the code
 * 
 * @author Stefano Semeria
 * @author Tiziana Trucco
 *
 */
public class JChatApplication extends Application {
	
	private static String SHARED_PREFERENCES_NAME= "properties";
	
	/**
	 * Constant used for retrieving the last host for JADE main container
	 */
	public static final String JADE_DEFAULT_HOST="it.telecomitalia.jchat.JADE_DEFAULT_HOST";
	
	/**
	 * Constant used for retrieving the last host for JADE main container
	 */
	public static final String JADE_DEFAULT_PORT="it.telecomitalia.jchat.JADE_DEFAULT_PORT";
	/**
	 * Constant used for the retrieving the last location provider
	 */
	public static final String LOCATION_PROVIDER="it.telecomitalia.jchat.LOCATION_PROVIDER";
	/**
	 * Key for retrieving the phone number
	 */
	public static final String PREFERENCE_PHONE_NUMBER="PREFERENCE_PHONE_NUMBER";
	private static final String EMULATOR_IMEI= "000000000000000";
	
	public void onCreate() {
		super.onCreate();
		initializeParameters();
	}

	public void onTerminate() {
		super.onTerminate();	
	}
	
	private void initializeParameters(){
		SharedPreferences prefs = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = prefs.edit();
		
		String host = prefs.getString(JADE_DEFAULT_HOST, getString(R.string.jade_platform_host));
		prefEditor.putString(JADE_DEFAULT_HOST, host);
		String port = prefs.getString(JADE_DEFAULT_PORT, getString(R.string.jade_platform_port));
		prefEditor.putString(JADE_DEFAULT_PORT, port);
		String provider = prefs.getString(LOCATION_PROVIDER, getString(R.string.location_provider_name));
		prefEditor.putString(LOCATION_PROVIDER, provider);
		
		String phoneNumber = prefs.getString(PREFERENCE_PHONE_NUMBER, "");
		
		if (phoneNumber.equals("")){
			//Get the phone number of my contact
			TelephonyManager telMgr = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
			//get the phone number
			StringBuffer numTel = new StringBuffer(telMgr.getLine1Number());
		    String IMEI = telMgr.getDeviceId();
		    if(EMULATOR_IMEI.equals(IMEI)){
		    	numTel.append(getRandomNumber());
		    	Log.v("JChatApplication", "Phone Number generated randomly and stored in shared preferences! Value is " +  phoneNumber.toString());
			}
			phoneNumber = numTel.toString();
			prefEditor.putString(PREFERENCE_PHONE_NUMBER, phoneNumber);
		}
		prefEditor.commit();
	}
	
	/**
	 * Retrieves a string as a replacement for the phone number if the phone number is not available 
	 * (no <code>/data/local.prop</code> on emulator).
	 * 
	 * @return String formatted as "RND&ltRandom Number&gt"
	 */
	private String getRandomNumber() {
		Random rnd = new Random();
		int randInt = rnd.nextInt(1000);
		return String.valueOf(randInt);
	}
	
	/**
	 * Returns the last stored value for a property (uses Android persistent store)
	 * @param key name of the property to be retrieved
	 * @return value of the property from the persistent store
	 */
	public String getProperty(String key){
		SharedPreferences prefs = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
		return prefs.getString(key, "");
	}

	/**
	 * Saves a property on persistent store (uses Android persistent store)
	 * @param key name of the property to be saved
	 * @param value value of the property to be saved
	 */
	public void setProperty(String key, String value){
		SharedPreferences prefs = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = prefs.edit();
		prefEditor.putString(key, value);
		prefEditor.commit();
	}
}
