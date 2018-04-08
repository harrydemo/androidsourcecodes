package com.milifan.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {
	SharedPreferences sp;
	SharedPreferences.Editor editor;

	Context context;

	public SharedPreferencesHelper(Context c, String name) {
		context = c;
		sp = context.getSharedPreferences(name, 0);
		editor = sp.edit();
	}

	public void putValue(String key, int value) {
		editor = sp.edit();
		editor.putInt(key, value);
		editor.commit();
		//Common.month_total = value;
	}

	public int getValue(String key) {
		int value = sp.getInt(key, 240);
		//Common.month_total = value;
		return value;
	}

}
