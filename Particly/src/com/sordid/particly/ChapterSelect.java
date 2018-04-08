package com.sordid.particly;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class ChapterSelect extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chapterselect);
                       
        setupChapters();
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
    	super.onNewIntent(intent);
    	
    	setupChapters();
    }  
    
    public void setupChapters() {
    	LinearLayout groups = (LinearLayout)findViewById(R.id.chapters);
    	groups.removeAllViews();
    	
		Resources res = getResources();
		final int[] chapters = res.getIntArray(R.array.chapters);
		final String[] titles = res.getStringArray(R.array.chapters_titles);
        
        for(int i=0;i<chapters.length;i++) {
            final ChapterView gv = new ChapterView(this, i+1, chapters[i], false, titles[i]);
            gv.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                  	Intent intent = new Intent(getBaseContext(), LevelSelect.class);
   					intent.putExtra("chapter",gv.getChapter());
   					intent.putExtra("levels",gv.getLevels());
 					startActivity(intent);
                }
            }); 
            groups.addView(gv);        	
        }     
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
      // ignore orientation/keyboard change
      super.onConfigurationChanged(newConfig);
    }
    
    @Override
    public void onBackPressed() {
    	//moveTaskToBack(true);
    	this.finish();
    }
}