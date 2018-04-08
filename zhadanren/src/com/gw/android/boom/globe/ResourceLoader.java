package com.gw.android.boom.globe;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ResourceLoader {
	private Context context;

	public ResourceLoader(Context ct) {
		this.context = ct;
	}

	public Bitmap[] getBitmap(int[] id) {
		Resources res = context.getResources();
		Bitmap[] bitmap = new Bitmap[id.length];
		for (int i = 0; i < id.length; i++) {
			bitmap[i] = BitmapFactory.decodeResource(res, id[i]);
		}
		res = null;
		return bitmap;
	}

	public Bitmap getBitmap(int id) {
		Resources res = context.getResources();
		Bitmap bitmap = null;
		bitmap = BitmapFactory.decodeResource(res, id);
		res = null;
		return bitmap;
	}
}
