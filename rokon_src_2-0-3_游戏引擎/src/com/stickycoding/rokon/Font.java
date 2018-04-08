package com.stickycoding.rokon;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.graphics.Typeface;

/**
 * Font.java
 * Loads a TTF font, for creating TextTextures
 * 
 * @author Richard
 */

public class Font {
	
	/**
	 * The maximum numebr of lines when producing a TextTexture
	 */
	public static final int MAX_LINES = 64;
	
	/**
	 * The default font size to use when producing TextTexture
	 */
	public static final float DEFAULT_FONT_SIZE = 64f;

	protected Rect bounds;
	protected FontMetrics fontMetrics;
	protected Typeface typeface;
	
	/**
	 * The Paint object used to draw text, this can be manipulated to produce the desired colours / styles
	 */
	public Paint paint;
	
	/**
	 * Creates a Font from a Typeface object
	 * 
	 * @param typeface valid Typeface object
	 */
	public Font(Typeface typeface) {
		this.typeface = typeface;
		init();
	}
	
	/**
	 * Creates a Font from a TTF font in assets
	 * 
	 * @param assetPath path to a valid .TTF file in assets
	 */
	public Font(String assetPath) {
		this.typeface = Typeface.createFromAsset(Rokon.currentActivity.getAssets(), assetPath);
		init();
	}
	
	private void init() {
		paint = new Paint();
		paint.setTypeface(typeface);
		paint.setTextSize(DEFAULT_FONT_SIZE);
		fontMetrics = paint.getFontMetrics();
		bounds = new Rect();
	}
	
	/**
	 * Sets the font size to be used when creating TextTextures, otherwise DEFAULT_FONT_SIZE will be used.
	 * 
	 * @param fontSize valid float, > 0
	 */
	public void setFontSize(float fontSize) {
		paint.setTextSize(fontSize);
		fontMetrics = paint.getFontMetrics();
	}
	
	/**
	 * Creates a TextTexture without wrapping
	 * 
	 * @param text valid String
	 * @return valid TextTexture, NULL if error in processing
	 */
	public TextTexture createTexture(String text) {
		int bitmapWidth, bitmapHeight;
		paint.getTextBounds(text, 0, text.length(), bounds);
		bitmapWidth = bounds.right - bounds.left;
		bitmapHeight = bounds.bottom - bounds.top;
		Bitmap bmp = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bmp);
		canvas.drawText(text, 0 - bounds.left, 0 - bounds.top, paint);
		return new TextTexture(bmp);
	}
	
	/**
	 * Creates a TextTexture with wrapping
	 * 
	 * @param text valid String
	 * @param width an integer width, in pixels, for the TextTexture produced
	 * @return valid TextTexture, NULL if error in processing
	 */
	public TextTexture createTexture(String text, int width) {
		int bitmapWidth, bitmapHeight;
		bitmapWidth = width;
		
		String[] lines = new String[MAX_LINES];
		int lineIndexStart = 0;
		int lineCount = 1;
		int currentLineWidth = 1;
		Rect testRect = new Rect();
		
		for(int index = 0; index < text.length(); index++) {
			paint.getTextBounds(text, index, index + 1, testRect);
			currentLineWidth += testRect.right;
			if(currentLineWidth + testRect.right > width) {
				currentLineWidth = testRect.right;
				lines[lineCount] = text.substring(lineIndexStart, index);
				lineIndexStart = index;
				lineCount++;
			} else {
				currentLineWidth += testRect.right;
			}
			if(index == text.length() - 1) {
				lines[lineCount] = text.substring(lineIndexStart, index);
			}
		}

		bitmapHeight = (int) (fontMetrics.bottom - fontMetrics.top) * lineCount;
		
		Bitmap bmp = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bmp);
		
		for(int i = 0; i <= lineCount; i++) {
			if(lines[i] != null) {
				canvas.drawText(lines[i], 0, (int) (fontMetrics.bottom - fontMetrics.top) * i, paint);
			}
		}
		Debug.print("Created texture, " + bitmapWidth + "x" + bitmapHeight);
		return new TextTexture(bmp);
	}

}
