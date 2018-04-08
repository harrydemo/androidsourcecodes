package com.lequ.Base;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lequ.Blackjack.R;

public class Tools
{
	public static void popToast(Context paramContext, CharSequence paramCharSequence, int paramInt)
	{
		Toast localToast = Toast.makeText(paramContext, paramCharSequence, paramInt);
		LinearLayout localLinearLayout = new LinearLayout(paramContext);
		View localView = localToast.getView();
		localLinearLayout.addView(localView);
		localToast.setView(localLinearLayout);
		localToast.setGravity(17, 0, 30);
		localToast.show();
	}

	public static final class styleable{
		public static final int[] CardView = { R.attr.topSpace, R.attr.leftSpace };
		public static final int CardView_leftSpace = 1;
		public static final int CardView_topSpace = 0;
		public static final int[] net_youmi_android_AdView = { R.attr.backgroundColor, R.attr.textColor, R.attr.backgroundTransparent };
		public static final int net_youmi_android_AdView_backgroundColor = 0;
		public static final int net_youmi_android_AdView_backgroundTransparent = 2;
		public static final int net_youmi_android_AdView_textColor = 1;
	}
	
	/**Get size of Screen
	 * @param manager getWindowManager()
	 * @return arrays of length 2: heightPixels,widthPixels
	 */
	public static int[] getScreenSize(WindowManager manager) {
		DisplayMetrics dm = new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(dm);
		int size[]= {dm.heightPixels,dm.widthPixels};
		return size;
	}
	
	
	/**Inflate a menu hierarchy from the specified XML resource
	 * @param inflater getMenuInflater()
	 * @param menu
	 * @param menuID
	 */
	public static void inflateMenu(MenuInflater inflater,Menu menu,int menuID ) {
		 inflater.inflate(menuID, menu);
	}
}