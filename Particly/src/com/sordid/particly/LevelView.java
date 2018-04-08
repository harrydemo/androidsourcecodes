package com.sordid.particly;

import com.sordid.particly.Stats.StatsLevel;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LevelView extends LinearLayout {
	private final int mLevel;
	private boolean mSelectable = true;
	
	public LevelView(Context context, int level, int max, StatsLevel pLevel) {
		super(context);
		
		mLevel = level;
		Drawable disabled = getResources().getDrawable(R.drawable.rounded_rectangle_disabled);
		Drawable completed = getResources().getDrawable(R.drawable.rounded_rectangle_complete);
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.levelview, this);
		
		RelativeLayout container = (RelativeLayout)findViewById(R.id.level_container);
		if(level > max) {
			mSelectable = false;
			container.setBackgroundDrawable(disabled);
		} else if(level < max) {
			container.setBackgroundDrawable(completed);
		}
		
        TextView level_number = (TextView)findViewById(R.id.level_number);
		level_number.setText(""+mLevel);
		if(level > max) {
			level_number.setTextColor(getResources().getColor(R.color.disabled));
		} else if(level < max) {
			level_number.setTextColor(getResources().getColor(R.color.done));
		}
		
		if(pLevel != null) {
			int attempts = pLevel.getAttemps();
			if(attempts == 1) {
				ImageView star3 = (ImageView)findViewById(R.id.level_star_3);
				star3.setImageResource(R.drawable.star_glow);
			}
			if(attempts <= 2) {
				ImageView star2 = (ImageView)findViewById(R.id.level_star_2);
				star2.setImageResource(R.drawable.star_glow);
			}
			ImageView star1 = (ImageView)findViewById(R.id.level_star_1);
			star1.setImageResource(R.drawable.star_glow);
		}
	}
	
	public int getLevel() {
		return mLevel;
	}
	
	public boolean isSelectable() {
		return mSelectable;
	}
}