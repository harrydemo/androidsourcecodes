package cn.com.ldci.plants;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.BitmapFactory;

public class BadestZomebie extends Zomebie {
	Bitmap[] BadestZomebieBitmap;
	
	BadestZomebie(float y) {
		currentY = y ;
		eatlifes = 10;
		moveLength = 1;
	}

	
	@Override
	protected void eat() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void hit() {
		// TODO Auto-generated method stub

	}
}
