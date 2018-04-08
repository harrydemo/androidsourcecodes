package com.sordid.particly;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class LevelSelect extends Activity {
	private int mChapter = 0;
	private int mLevels = 0;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        mChapter = savedInstanceState != null ? savedInstanceState.getInt("chapter"):0;	
		if(mChapter == 0) {
			mChapter = extras != null ? extras.getInt("chapter") : 1;
		}
        mLevels = savedInstanceState != null ? savedInstanceState.getInt("levels"):0;	
		if(mLevels == 0) {
			mLevels = extras != null ? extras.getInt("levels") : 0;
		}		
        
        setContentView(R.layout.levelselect);
        setupLevels();
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
    	super.onNewIntent(intent);   	
    	Bundle extras = intent.getExtras();
    	mChapter = extras != null ? extras.getInt("chapter") : 1;
    	mLevels = extras != null ? extras.getInt("levels") : 0;
    	setupLevels();
    }  
       
    @Override
    public void onBackPressed() {
    	Intent intent = new Intent(getBaseContext(), ChapterSelect.class);
        startActivity(intent);
        this.finish();
    }
    
    public void setupLevels() {
        if(mChapter == 0) {
    		Intent nintent = new Intent(getBaseContext(), LoadingScreen.class);
            startActivity(nintent);
    	} else {
    		final String chapterFile = "stats_data";
    		String extra = "";
    		if(mChapter > 1) {
    			extra = "_chapter"+mChapter;
    		}
        	Stats temp = new Stats(this, chapterFile, extra);
    		Stats read = temp.readObject(this);
    		if(read != null) {
    			ParticlyActivity.sStats = read;
    			ParticlyActivity.sStats.setFile(chapterFile, extra);
    		} else {
    			ParticlyActivity.sStats = new Stats(this, chapterFile, extra);
    		}
    		
    		SharedPreferences settings = getSharedPreferences(Stats.PREFS_NAME, 0);
    		int maxLevel = settings.getInt("currentLevel"+extra, 1);
    		ParticlyActivity.sStats.setMaxLevel(maxLevel);
			SharedPreferences.Editor editor = settings.edit();
			editor.putInt("currentLevel"+extra, ParticlyActivity.sStats.getMaxLevel());
			editor.commit();    		
    		
	    	LinearLayout groups = (LinearLayout)findViewById(R.id.levels);
	    	groups.removeAllViews();
	        
	        final double perRow = 5;
	        final int totalIterations = (int) Math.ceil((double)mLevels/perRow);
	        int num = 0;
	        for(int i=0;i<totalIterations;i++) {
	            LinearLayout row = new LinearLayout(this);
	            row.setOrientation(LinearLayout.HORIZONTAL);
	            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT);
	            lp.weight = 1.0f;
	            lp.topMargin = 7;
	            lp.bottomMargin = 3;
	            lp.leftMargin = 10;
	            for (int c = 0; c < perRow; ++c)
	            {
	            	if(num >= mLevels) {
	            		break;
	            	}
	                final LevelView gv = new LevelView(this, ++num, ParticlyActivity.sStats.getMaxLevel(), ParticlyActivity.sStats.getLevel(num));
	                gv.setOnClickListener(new View.OnClickListener() {
	                    public void onClick(View v) {
	                    	if(gv.isSelectable() || ParticlyActivity.DEBUG) {
		                     	Intent intent = new Intent(getBaseContext(), ParticlyActivity.class);
		                     	intent.putExtra("chapter",mChapter);
		                     	intent.putExtra("levels",mLevels);
		                     	intent.putExtra("level",gv.getLevel());
		       					startActivity(intent);
	                    	}
	                    }
	                }); 
	                row.addView(gv, lp);
	            }
	            groups.addView(row, lp);        	
	        }
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.CENTER;
	        lp.leftMargin = 10;    
	        ImageView b = new ImageView(getBaseContext());
	        b.setImageResource(R.drawable.button_back);
	        b.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                 	Intent intent = new Intent(getBaseContext(), ChapterSelect.class);
   					startActivity(intent);
                }
            }); 
	        LinearLayout.LayoutParams blp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	        blp.gravity = Gravity.CENTER;
	        blp.topMargin = 20;
	        blp.bottomMargin = 20;
	        blp.leftMargin = 20;
	        row.addView(b, blp);
	        groups.addView(row, lp); 
    	}
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
      // ignore orientation/keyboard change
      super.onConfigurationChanged(newConfig);
    }
}