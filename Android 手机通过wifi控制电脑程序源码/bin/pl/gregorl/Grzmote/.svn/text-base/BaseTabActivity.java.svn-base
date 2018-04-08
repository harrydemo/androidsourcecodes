package pl.gregorl.Grzmote;

import android.app.TabActivity;
import android.os.Bundle;

public class BaseTabActivity extends TabActivity {
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
