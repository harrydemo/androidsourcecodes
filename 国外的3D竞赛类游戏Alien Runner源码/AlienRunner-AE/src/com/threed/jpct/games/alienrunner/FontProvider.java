package com.threed.jpct.games.alienrunner;

import java.util.HashMap;
import java.util.Map;

import android.content.res.AssetManager;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Logger;
import com.threed.jpct.games.alienrunner.glfont.GLFont;

public class FontProvider {

	private static Map<Integer, GLFont> fonts = new HashMap<Integer, GLFont>();

	private static Typeface baseFont=null;
	
	public static void inject(AssetManager assets) {
		baseFont=Typeface.createFromAsset(assets, "Action Man Bold.ttf");
	}
	
	public static GLFont getFont(int size, FrameBuffer buffer, boolean bold) {
		float mul = buffer.getHeight() / GameConfig.defaultHeight;
		size *= mul;
		int hashSize = size;
		if (bold) {
			hashSize += 100000;
		}

		GLFont font = fonts.get(hashSize);
		if (font == null) {
			Paint paint = new Paint();
			paint.setAntiAlias(true);
			
			paint.setTypeface(Typeface.create(baseFont, (bold ? Typeface.BOLD : Typeface.NORMAL)));
			paint.setTextSize(size);
			font = new GLFont(paint);
			fonts.put(hashSize, font);
			Logger.log("FontProvider created font of size " + size + " with key " + hashSize);
		}

		return font;
	}

}
