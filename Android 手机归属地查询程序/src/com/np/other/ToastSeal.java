package com.np.other;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

public class ToastSeal {
	private static Toast toast;
	private static long DELAY = 10;
	private static long PERIOD = 1000;
	private static boolean TYPE_VIEW = false;
	
	private static Context context;
	private static String text;
	private static View view;
	private static float showTime;
	
	public static void toast(Context context,String text,final float showTime){
		ToastSeal.context = context;
		ToastSeal.text = text;
		ToastSeal.showTime = showTime * 1000;
		TYPE_VIEW = false;
		show();
	}
	public static void toast(Context context,View view,final float showTime){
		ToastSeal.context = context;
		ToastSeal.view = view;
		ToastSeal.showTime = showTime;
		TYPE_VIEW = true;
		show();
	}
	private static void show(){
		if(toast == null)
			toast = Toast.makeText(context, null, Toast.LENGTH_SHORT);
		if(TYPE_VIEW && view != null){
			toast.setView(view);
		}else{
			toast.setText(text);
		}
		toast.show();
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			int t;
			@Override
			public void run() {
				if(t > showTime)
					timer.cancel();
				toast.show();
				t += PERIOD;
			}
		}, DELAY, PERIOD);
	}
}
