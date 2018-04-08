package com.example.mynewanimation;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public class MyViewGroup extends ViewGroup{

	private static MyViewGroup myViewGroup;
	AnimationView animationView;
	BackgroundView backgroundView;
	
	static MyViewGroup getInstance(Context context){
		if(myViewGroup == null){
			myViewGroup = new MyViewGroup(context);
		}
		return myViewGroup;	
	}
	
	public MyViewGroup(Context context) {
		super(context);
		animationView = AnimationView.getInstance(context);
		backgroundView = BackgroundView.getInstance(context);
		this.addView(backgroundView);
		this.addView(animationView);
	}
	
	public void switchBack(){
		backgroundView.switchBack();
		postInvalidate();
	}

	@Override
	protected void onLayout(boolean arg0, int l, int t, int r, int b) {
		int count = this.getChildCount();
		for(int i=0;i<count;i++){
			View v = getChildAt(i);
			v.layout(l, t, r, b);
		}		
	}
	
}
