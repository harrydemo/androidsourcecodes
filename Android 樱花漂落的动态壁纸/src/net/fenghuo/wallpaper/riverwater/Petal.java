package net.fenghuo.wallpaper.riverwater;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Petal extends Layer {
	Bitmap sourceImage;

	private int numberFrames; // ��֡��
	private Rect[] frameRects; // ÿһ֡��Ӧ�ľ�������
	private int frameSequenceIndex; // ��ǰ֡���

	public Petal(Bitmap bitmap) {
		sourceImage = bitmap;
		setWidth(35);
		setHeight(35);
		numberFrames = 45;

		frameRects = new Rect[numberFrames];
		// ����ÿһ֡��Ӧ�ľ�������
		int left, top, right, bottom;
		int index = 0;
		for (int row = 0; row < 7; row++) {
			for (int los = 0; los < 7 && index < numberFrames; los++, index++) {
				left = width * los;
				top = height * row;
				right = left + width;
				bottom = top + height;
				frameRects[index] = new Rect(left, top, right, bottom);
			}
		}
	}

	@Override
	public void draw(Canvas canvas, int referencex, int referencey) {
		int left = getX();
		int top = getY();

		Rect destRect = new Rect(left, top, left + this.width, top + this.height);

		Rect sourceRect = this.frameRects[this.frameSequenceIndex];

		canvas.drawBitmap(this.sourceImage, sourceRect, destRect, null);
		
		nextFrame();
	}

	public void nextFrame() {
		y += 1;
		frameSequenceIndex++;
		if(frameSequenceIndex>=numberFrames){
			frameSequenceIndex = 0;
		}
	}
}
