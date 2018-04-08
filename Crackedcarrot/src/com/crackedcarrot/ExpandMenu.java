package com.crackedcarrot;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

/**
 * Class that functions as the expandable menu in the game. It expands the
 * LinearLayout in the way that it switches between visibility and non-
 * visibility with a slide animation. Object of type TranslateAnimation the
 * position of an object.
 */
public class ExpandMenu extends LinearLayout {
	private int duration = 300;
	private DisplayMetrics dm;
	private float height;
	private boolean active = false;

	public ExpandMenu(Context ctx) {
		super(ctx);
		this.dm = new DisplayMetrics();
		this.height = (float) dm.heightPixels / 3;
	}

	public ExpandMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.dm = new DisplayMetrics();
		this.height = (float) dm.heightPixels / 3;
	}

	/**
	 * This method checks whether the expanded menu is activated or not, and
	 * makes the appropriate switch.
	 * 
	 * @param boolean onoff true = activate menu, false = deactivate menu
	 */
	public void switchMenu(boolean onoff) {
		TranslateAnimation animation = null;

		if (onoff) {
			setVisibility(View.VISIBLE);
			animation = new TranslateAnimation(0.0f, 0.0f, this.height, 0.0f);
			this.active = true;
		} else {
			animation = new TranslateAnimation(0.0f, 0.0f, this.height, 0.0f);
			animation.setAnimationListener(animListener);
			this.active = false;
		}
		/** Amount of time (in milliseconds) for the animation to run */
		animation.setDuration(this.duration);
		/**
		 * Defines the interpolator used to smooth the animation movement in
		 * time. An AccelerateInterpolator where the rate of change starts out
		 * slowly and and then accelerates.
		 */
		animation.setInterpolator(new AccelerateInterpolator(1.0f));
		startAnimation(animation);
	}

	/**
	 * This method checks whether the expanded menu is activated or not, and
	 * makes the appropriate switch.
	 */
	public void switchMenu() {
		TranslateAnimation animation = null;

		if (!active) {
			setVisibility(View.VISIBLE);
			animation = new TranslateAnimation(0.0f, 0.0f, this.height, 0.0f);
			this.active = true;
		} else {
			animation = new TranslateAnimation(0.0f, 0.0f, this.height, 0.0f);
			animation.setAnimationListener(animListener2);
			this.active = false;
		}
		animation.setDuration(this.duration);
		animation.setInterpolator(new AccelerateInterpolator(1.0f));
		startAnimation(animation);
	}

	/**
	 * The animationListener receives notifications from the animation. When the
	 * animation is done, it sets this view to be non-visible
	 */
	Animation.AnimationListener animListener = new Animation.AnimationListener() {
		public void onAnimationEnd(Animation animation) {
			setVisibility(View.GONE);
		}

		/** Must be implemented or AnimationListener will not function */
		public void onAnimationRepeat(Animation animation) {
			// Do nothing here
		}

		/** Must be implemented or AnimationListener will not function */
		public void onAnimationStart(Animation animation) {
			// Do nothing here
		}
	};

	Animation.AnimationListener animListener2 = new Animation.AnimationListener() {
		public void onAnimationEnd(Animation animation) {
			setVisibility(View.GONE);
		}

		/** Must be implemented or AnimationListener will not function */
		public void onAnimationRepeat(Animation animation) {
			// Do nothing here
		}

		/** Must be implemented or AnimationListener will not function */
		public void onAnimationStart(Animation animation) {
			// Do nothing here
		}
	};

}