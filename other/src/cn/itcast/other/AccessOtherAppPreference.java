package cn.itcast.other;

import android.content.Context;
import android.content.SharedPreferences;
import android.test.AndroidTestCase;
import android.util.Log;

public class AccessOtherAppPreference extends AndroidTestCase {
	private static final String TAG = "AccessOtherAppPreference";
	
	public void testAccessPreference() throws Throwable{
		/*String path = "/data/data/cn.itcast.set/shared_prefs/itcast.xml";
		File file = new File(path);
		FileInputStream inStream = new FileInputStream(file);
		*/
		Context otherContext = this.getContext().createPackageContext("cn.itcast.set",
				Context.CONTEXT_IGNORE_SECURITY);
		
		SharedPreferences preferences = otherContext.getSharedPreferences("itcast", Context.MODE_WORLD_READABLE);
		Log.i(TAG, "name="+ preferences.getString("name", ""));
		Log.i(TAG, "age="+ String.valueOf(preferences.getInt("age", 20)));
	}
}
