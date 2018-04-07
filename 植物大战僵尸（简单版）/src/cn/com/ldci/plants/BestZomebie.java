package cn.com.ldci.plants;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

public class BestZomebie extends Zomebie {

	Bitmap[] BestZomebieBitmap;
	
	BestZomebie(float y) {
		eatlifes = 10;
		moveLength = 1;
		currentX = 400;
		currentY = y ;
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
