package com.pop;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class PopWin {
	public View view;
	Activity mpthis;
	PopupWindow mPopupWindow;
	int mheight;

	public PopWin(Activity pthis) {
		mpthis = pthis;
		mPopupWindow = null;
	}

	public void setY(int y) {
		mheight = y;
		Log.d("PopWin", "setY:" + mheight);
	}

	public void dismiss() {
		Log.d("PopWin", "dismiss");
		if (mPopupWindow != null) {
			mPopupWindow.dismiss();
			mPopupWindow = null;
			Log.d("PopWin", "dismiss ok");
		}
	}

	TextView tv_showText = null;
	ImageView in1 = null;
	ImageView in2 = null;
	TextView tv = null;
	TextView tv1 = null;

	public void ShowWin(OnClickListener oc) {
		dismiss();
		View foot_popunwindwow = null;

		LayoutInflater LayoutInflater = (LayoutInflater) mpthis
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		foot_popunwindwow = LayoutInflater
				.inflate(R.layout.foot_map_view, null);

		mPopupWindow = new PopupWindow(foot_popunwindwow,
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		Log.d("PopWin", "" + mheight);
		mPopupWindow.showAtLocation(mpthis.findViewById(R.id.layout),
				Gravity.LEFT | Gravity.TOP, 100, 200);
		mPopupWindow.update();

		in1 = (ImageView) foot_popunwindwow.findViewById(R.id.in1);
		in2 = (ImageView) foot_popunwindwow.findViewById(R.id.in2);
		tv = (TextView) foot_popunwindwow.findViewById(R.id.text1);

		tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(mpthis, "text1", Toast.LENGTH_SHORT).show();
			}
		});
		in1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(mpthis, "in1", Toast.LENGTH_SHORT).show();
			}
		});
		in2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(mpthis, "in2", Toast.LENGTH_SHORT).show();
			}
		});
	}

}
