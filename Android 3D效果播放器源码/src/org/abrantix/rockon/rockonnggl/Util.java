package org.abrantix.rockon.rockonnggl;

import java.io.File;
import java.io.IOException;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class Util {
	
	static int oDonationAppsInstalled=0;
	static public boolean hasDonated(Context ctx, boolean ignoreTouchesOnly) {
		oDonationAppsInstalled = 0;
    	if(ctx.getResources().getBoolean(R.bool.config_isMarketVersion)) {    			
    		File f = new File(Constants.ROCKON_DONATION_PATH);
    		if(!f.exists() || ignoreTouchesOnly)
    		{
            	try{
            		ComponentName cName = 
        				new ComponentName(
    						Constants.DONATION_APP_PKG_1, 
    						Constants.DONATION_APP_MAIN_ACTIVITY_1);
        			ctx.getPackageManager().
        				getActivityInfo(
        						cName,
        						0);
        			oDonationAppsInstalled++;
        		} catch(NameNotFoundException e) {
        		}
        		try{
            		ComponentName cName = 
        				new ComponentName(
        						Constants.DONATION_APP_PKG_2, 
        						Constants.DONATION_APP_MAIN_ACTIVITY_2);
        			ctx.getPackageManager().
        				getActivityInfo(
        						cName,
        						0);
        			oDonationAppsInstalled++;
        		} catch(NameNotFoundException e) {
        		}
        		try{
            		ComponentName cName = 
        				new ComponentName(
        						Constants.DONATION_APP_PKG_3, 
        						Constants.DONATION_APP_MAIN_ACTIVITY_3);
        			ctx.getPackageManager().
        				getActivityInfo(
        						cName,
        						0);
        			oDonationAppsInstalled++;
        		} catch(NameNotFoundException e) {
        		}
        		try{
            		ComponentName cName = 
        				new ComponentName(
        						Constants.DONATION_APP_PKG_4, 
        						Constants.DONATION_APP_MAIN_ACTIVITY_4);
        			ctx.getPackageManager().
        				getActivityInfo(
        						cName,
        						0);
        			oDonationAppsInstalled++;
        		} catch(NameNotFoundException e) {
        		}
            	
        		Log.i(null, "DonationApps installed: "+oDonationAppsInstalled);
        		
        		if(oDonationAppsInstalled <= 0) {
    		    	return false;
        		} else {
        			return true;
        		}
        	} else {
        		Log.i(null, "Has clicked donation option");
        		return true;
        	}
        } else {
        	Log.i(null, "It is not market version");
        	return true;
        }
	}
}
