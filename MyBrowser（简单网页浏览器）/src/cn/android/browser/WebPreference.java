package cn.android.browser;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.widget.Toast;

public class WebPreference extends PreferenceActivity {

	SharedPreferences sp;
	public static Context con;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
        con = this;
        
        sp = PreferenceManager.getDefaultSharedPreferences(this);  
        
        sp.registerOnSharedPreferenceChangeListener(new OnSharedPreferenceChangeListener(){   
        	  
            @Override   
            //sharedPreferences:¾ä±ú   
            //key: ¸Ä±ä ¼üÖµ   
            public void onSharedPreferenceChanged(   
                    SharedPreferences sharedPreferences, String key) 
            {   
                // TODO Auto-generated method stub   
                   
                if(key.equals("checkbox_preference_1"))
                {   
                    Boolean flag = sharedPreferences.getBoolean("checkbox_preference_1", false);   
                    Main.instance.setBlockImage(flag);
                    if(flag){               	
                    	Toast.makeText(WebPreference.this, "×èÈûÍ¼Ïñ", Toast.LENGTH_LONG).show();  
                    }   
                    else {   
                    	
                    }   
                }  
                else if(key.equals("checkbox_preference_2"))
                {               	               	
                	Boolean flag = sharedPreferences.getBoolean("checkbox_preference_2", false);   
                	Main.instance.setCacheMode(flag);
                	if(flag){   
                    	Toast.makeText(WebPreference.this, "Æô¶¯»º´æ", Toast.LENGTH_LONG).show();  
                    }   
                    else {   
                    }   
                }
                else if(key.equals("checkbox_preference_3"))
                {            	
                	Boolean flag = sharedPreferences.getBoolean("checkbox_preference_3", false);   
                	Main.instance.setJavaScript(flag);
                	if(flag){                 	
                    	Toast.makeText(WebPreference.this, "Æô¶¯javascript", Toast.LENGTH_LONG).show();  
                    }   
                    else {   
                    }   
                } 
                
            }   
               
        });  
    }
    
    @Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,Preference preference) 
    {
		if(preference.getKey()!=null)
		{
			if(preference.getKey().equals("checkbox_preference_1"))
			{
				boolean flag = preference.getSharedPreferences().getBoolean(preference.getKey(), false);
				Main.instance.setBlockImage(flag);
			}
			else if(preference.getKey().equals("checkbox_preference_2"))
			{
				boolean flag = preference.getSharedPreferences().getBoolean(preference.getKey(), false);
				Main.instance.setCacheMode(flag);
			}
			else if(preference.getKey().equals("checkbox_preference_3"))
			{
				boolean flag = preference.getSharedPreferences().getBoolean(preference.getKey(), false);
				Main.instance.setJavaScript(flag);
			}
		}

		return super.onPreferenceTreeClick(preferenceScreen, preference);

	}



}
