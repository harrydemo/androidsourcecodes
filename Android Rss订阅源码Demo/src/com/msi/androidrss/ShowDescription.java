package com.msi.androidrss;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import android.view.*;

public class ShowDescription extends Activity 
{
    public void onCreate(Bundle icicle) 
    {
        super.onCreate(icicle);
        setContentView(R.layout.showdescription);
        
        String theStory = null;
        
        
        Intent startingIntent = getIntent();
        
        if (startingIntent != null)
        {
        	Bundle b = startingIntent.getBundleExtra("android.intent.extra.INTENT");
        	if (b == null)
        	{
        		theStory = "bad bundle?";
        	}
        	else
    		{
        		theStory = b.getString("title") + "\n\n" + b.getString("pubdate") + "\n\n" + b.getString("description").replace('\n',' ') + "\n\nMore information:\n" + b.getString("link");
    		}
        }
        else
        {
        	theStory = "Information Not Found.";
        
        }
        
        TextView db= (TextView) findViewById(R.id.storybox);
        db.setText(theStory);
        
        Button backbutton = (Button) findViewById(R.id.back);
        
        backbutton.setOnClickListener(new Button.OnClickListener() 
        {
            public void onClick(View v) 
            {
            	finish();
            }
        });        
    }
}
