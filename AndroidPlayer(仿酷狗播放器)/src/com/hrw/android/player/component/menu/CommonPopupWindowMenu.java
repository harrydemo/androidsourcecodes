package com.hrw.android.player.component.menu;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.PopupWindow;

import com.hrw.android.player.R;

public class CommonPopupWindowMenu extends PopupWindow {
	private Context context;
	private View parent;

	private View child;
	ImageButton tab_menu;

	public CommonPopupWindowMenu(Context context, View parent, View child) {
		super(context);
		tab_menu = (ImageButton) parent.findViewById(R.id.tab_menu);
		this.parent = parent;
		this.context = context;
		this.child = child;
		this.child.setFocusableInTouchMode(true);
		this.child.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((keyCode == KeyEvent.KEYCODE_MENU)
						&& (event.getAction() == KeyEvent.ACTION_DOWN)
						&& (isShowing())) {
					dismiss();
					return true;
				}
				return false;
			}
		});
		setContentView(child);
		setWidth(LayoutParams.FILL_PARENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		setBackgroundDrawable(new BitmapDrawable());
		setFocusable(true);

		setAnimationStyle(R.style.PopupAnimation);
		update();
	}

	public void show() {
		if ((this != null) && (!this.isShowing())) {
			tab_menu.setBackgroundResource(R.drawable.tab_menu_open_up);
			this.showAtLocation(parent, Gravity.BOTTOM, 0, 55);
		}
	}

	@Override
	public void dismiss() {
		tab_menu.setSelected(false);
		tab_menu.setBackgroundResource(R.drawable.tab_menu_default);
		super.dismiss();
	}
}
