package com.sordid.particly;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ChapterView extends LinearLayout {
	private final int mChapter;
	private final int mLevels;
	
	public ChapterView(Context context, int chapter, int levels, boolean hasCompleted, String pTitle) {
		super(context);
		
		mChapter = chapter;
		mLevels = levels;
		Drawable completed = getResources().getDrawable(R.drawable.rounded_rectangle_complete);
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.chapterview, this);
		
		RelativeLayout container = (RelativeLayout)findViewById(R.id.chapter_container);
		if(hasCompleted) {
			container.setBackgroundDrawable(completed);
		}
		
        TextView level_number = (TextView)findViewById(R.id.chapter_text);
		level_number.setText("Chapter "+chapter);
		
		TextView title = (TextView)findViewById(R.id.chapter_title);
		title.setText(pTitle);
		
		String extra = "";
		if(mChapter > 1) {
			extra = "_chapter"+mChapter;
		}

		SharedPreferences settings = context.getSharedPreferences(Stats.PREFS_NAME, 0);
		Integer maxLevel = settings.getInt("currentLevel"+extra, 1);
		
		TextView stats = (TextView)findViewById(R.id.chapter_stats);
		int maxLevelDisplay = maxLevel > levels ? levels : maxLevel-1;
		stats.setText(maxLevelDisplay+"/"+levels+" levels completed");
		if(maxLevel > levels) {
			ImageView image = (ImageView)findViewById(R.id.chapter_image);
			image.setImageResource(R.drawable.large_star_glow);
		}
	}
	
	public int getChapter() {
		return mChapter;
	}

	public int getLevels() {
		return mLevels;
	}
}