package pl.gregorl.Grzmote;

import android.app.Activity;
import android.os.Bundle;


public class BaseActivity extends Activity {

	protected static Settings settings;
	
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
	    	super.onCreate(savedInstanceState);
	    	if(settings==null){
	    		settings = new Settings(getSharedPreferences(Settings.settingsPath,0));
	    	}
	    	settings.Load();
	 }
}
