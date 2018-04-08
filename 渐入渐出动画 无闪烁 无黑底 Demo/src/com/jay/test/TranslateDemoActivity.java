package com.jay.test;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
/**
 * 
 * @author jayzhou215@163.com
 * @time 2012/5/25
 */
public class TranslateDemoActivity extends Activity {

	private View head;
	private View content;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        head = findViewById(R.id.head);
        content = findViewById(R.id.content);
        content.setOnClickListener(listener);
        
        
    }
    private boolean k = true;
    private OnClickListener listener = new OnClickListener() {
		

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (k) {
				showHead(head, content);
				setTitle("ÏÔÊ¾head");
			} else {
				hideHead(head, content);
				setTitle("Òþ²Øhead");
			}
			k = !k;
			
		}
	};
	
	private void showHead(View head, View content) {
    	head.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_out_top));
    	content.startAnimation(AnimationUtils.loadAnimation(this, R.anim.content_down));
    	content.setPadding(0, 200, 0, 0);
    }
    
	private void hideHead(View head, View content) {
		content.setPadding(0, 0, 0, 0);
		head.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_top));
		content.startAnimation(AnimationUtils.loadAnimation(this, R.anim.content_up));
    }
}