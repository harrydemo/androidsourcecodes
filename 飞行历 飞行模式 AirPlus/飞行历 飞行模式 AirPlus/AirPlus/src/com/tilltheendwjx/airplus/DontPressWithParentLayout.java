/**
 * 
 */
package com.tilltheendwjx.airplus;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * @author wjx
 * 
 */
public class DontPressWithParentLayout extends LinearLayout {
	public DontPressWithParentLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void setPressed(boolean pressed) {
		// If the parent is pressed, do not set to pressed.
		if (pressed && ((View) getParent()).isPressed()) {
			return;
		}
		super.setPressed(pressed);
	}
}
