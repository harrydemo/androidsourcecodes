package grimbo.android.demo.slidingmenu;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;

public class ClickListenerForScrolling implements OnClickListener {
	HorizontalScrollView scrollView;
	View menu;

	boolean menuOut = false;

	public ClickListenerForScrolling(HorizontalScrollView scrollView, View menu) {
		super();
		this.scrollView = scrollView;
		this.menu = menu;
	}

	@Override
	public void onClick(View v) {
		System.out.println("你点击的是menu"+menu);
		int menuWidth = menu.getMeasuredWidth();
		System.out.println("你点击的是menuWidth"+menuWidth);
		menu.setVisibility(View.VISIBLE);

		if (!menuOut) {
			int left = 0;
//			从什么地方滚动到什么地方
			scrollView.smoothScrollTo(left, 0);
			
		} else {
			int left = menuWidth;
			scrollView.smoothScrollTo(left, 0);
		}
		menuOut = !menuOut;
	}

}
