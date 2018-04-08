package net.fenghuo.wallpaper.riverwater;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

public class Flower extends Layer {
	Bitmap sourceImage;

	int angle; // 当前旋转的相对于原始位图选转的角度

	Matrix matrix;

	public Flower(Bitmap bitmap) {
		sourceImage = bitmap;
		setWidth(bitmap.getWidth());
		setHeight(bitmap.getHeight());
		matrix = new Matrix();
	}

	public void draw(Canvas canvas,int referencex,int referencey) {
		Bitmap bitmap = Bitmap.createBitmap(sourceImage, 0, 0, sourceImage.getWidth(), sourceImage.getHeight(), matrix, true);
		if (bitmap != null) {
			canvas.drawBitmap(bitmap, x+referencex, y+referencey, null);
		}
		nextFrame();
	}

	public void nextFrame() {
		y += 2;
		angle = (angle + 1) % 360;
		matrix.reset();
		matrix.setRotate(angle, width / 2, height / 2);
	}
}
