package com.hrw.android.player.utils;

import java.lang.reflect.Constructor;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;

public class MenuUtil {
	public static Menu inflate(Activity activity, int menu) {
		try {
			Class<?> menuBuilderClass = Class
					.forName("com.android.internal.view.menu.MenuBuilder");
			Constructor<?> menuBuilderConstructor = menuBuilderClass
					.getConstructor(Context.class);
			Menu localMenu = (Menu) menuBuilderConstructor
					.newInstance(activity);
			activity.getMenuInflater().inflate(menu, localMenu);
			return localMenu;
		} catch (Exception e) {
			Log.e("MenuBuilder", "can't builder the:" + menu);
			return null;
		}

	}

}
