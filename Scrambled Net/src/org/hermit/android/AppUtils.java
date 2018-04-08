
/**
 * org.hermit.utils: useful Android utility classes.
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License version 2
 *   as published by the Free Software Foundation (see COPYING).
 * 
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 */


package org.hermit.android;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;


/**
 * This class provides some simple application-related utilities.
 */
public class AppUtils
{

	// ******************************************************************** //
	// Public Classes.
	// ******************************************************************** //

	/**
	 * Version info detail level.
	 */
	public enum Detail {
		NONE,				// Do not display.
		SIMPLE,				// Show basic name and version.
		DEBUG;				// Show debug-level detail.
	}
	
	
	// ******************************************************************** //
	// Constructor.
	// ******************************************************************** //

	/**
	 * Set up an app utils instance for the given activity.
	 * 
	 * @param	parent			Activity for which we want information.
	 */
	public AppUtils(Activity parent) {
		parentApp = parent;
		resources = parent.getResources();
	}


	// ******************************************************************** //
	// Version.
	// ******************************************************************** //
    
    /**
     * Get a string containing the name and version info for the current
     * app's package.
     * 
     * @param detail		How much detail we want.
     * @return				Descriptive name / version string.
     */
    public String getVersionString(Detail detail) {
    	String res = null;
    	
    	// Get the package manager.
    	PackageManager pm = parentApp.getPackageManager();
    	
    	// Get our package name and use it to get our package info.  We
    	// don't need the optional info.
    	String pname = parentApp.getPackageName();
    	try {
			PackageInfo pinfo = pm.getPackageInfo(pname, 0);
		 	int vcode = pinfo.versionCode;
		 	String vname = pinfo.versionName;
		 	if (vname == null)
		 		vname = "?.?";
		 	
		 	// Get the pretty name of the app.
		 	CharSequence appName = "?";
		 	ApplicationInfo ainfo = pinfo.applicationInfo;
		 	if (ainfo != null) {
		 		int alabel = ainfo.labelRes;
		 		if (alabel != 0)
		 			appName = resources.getText(alabel);
		 	}
		 	
		 	if (detail == Detail.DEBUG)
		 		res = String.format("%s (%s) %s (%d)",
		 							appName, pname, vname, vcode);
		 	else {
		 		// TODO: the "version" should really come from a resource,
		 		// but I don't want a separate resources file in this package.
		 		res = String.format("%s version %s", appName, vname);
		 	}
		} catch (NameNotFoundException e) {
			res = String.format("%s (no info)", pname);
		}
		
		return res;
    }
    
   
	// ******************************************************************** //
	// Private Data.
	// ******************************************************************** //
 
	// Parent application context.
	private Activity parentApp;
	
	// App's resources.
	private Resources resources;
	
}

