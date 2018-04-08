package com.threed.jpct.games.alienrunner;

import java.util.ArrayList;
import java.util.List;

import android.content.res.Resources;

import com.threed.jpct.FrameBuffer;
import com.threed.jpct.RGBColor;
import com.threed.jpct.Texture;
import com.threed.jpct.games.alienrunner.glfont.GLFont;
import com.threed.jpct.games.alienrunner.glfont.Rectangle;

public class Rotator {

	private List<Texture> levelThumbs = new ArrayList<Texture>();
	private int level = 1;
	private int target = 1;
	private int xPos = -999999;
	private int dxPos = 0;
	private GLFont font = null;
	private Rectangle size = null;
	private String text = "Select track!";

	public Rotator(Resources res) {
		levelThumbs.add(new Texture(res.openRawResource(R.raw.level_thumb_1)));
		levelThumbs.add(new Texture(res.openRawResource(R.raw.level_thumb_2)));
		levelThumbs.add(new Texture(res.openRawResource(R.raw.level_thumb_3)));
		
		for (Texture t:levelThumbs) {
			t.setMipmap(false);
		}
	}

	public int getLevel() {
		if (target == level) {
			return level;
		}
		return -1;
	}

	public void scroll(boolean left) {
		if (target == level) {
			target = level + (left ? -1 : 1);
			SoundManager.getInstance().play(SoundManager.TURN);
		}
	}

	public void paint(long ticks, FrameBuffer buffer) {

		if (font == null) {
			font = FontProvider.getFont(35, buffer, true);
			size=font.getStringBounds(text, null);
		}

		Texture tex = levelThumbs.get(level - 1);

		float difX = buffer.getWidth() / GameConfig.defaultWidth;
		float difY = buffer.getHeight() / GameConfig.defaultHeight;

		if (xPos == -999999) {
			xPos = buffer.getWidth() / 2;
			xPos -= 150 * difX;
			dxPos = xPos;
		}

		int yPos = buffer.getHeight() / 2;
		yPos -= 100 * difY;

		int yPosFont = (int) yPos - size.height/2;
		int xPosFont = (buffer.getWidth() / 2) - (size.width / 2);

		font.blitString(buffer, text, xPosFont, yPosFont, 50, RGBColor.WHITE);

		Texture nTex = null;
		if (target != level) {
			int tp = target - 1;
			if (tp < 0) {
				tp = levelThumbs.size() - 1;
			}
			if (tp + 1 > levelThumbs.size()) {
				tp = 0;
			}
			nTex = levelThumbs.get(tp);
		}

		buffer.blit(tex, 0, 0, xPos, yPos, 128, 128, (int) (300 * difX), (int) (200* difY), -1, false, null);

		if (nTex != null) {
			float nxPos = 400 * difX;

			if (target > level) {
				nxPos = xPos + nxPos;
			} else {
				nxPos = xPos - nxPos;
			}

			if (target > level) {
				if (nxPos <= dxPos) {
					nxPos = dxPos;
					xPos = dxPos;
					level = target;
				}
			} else {
				if (nxPos >= dxPos) {
					nxPos = dxPos;
					xPos = dxPos;
					level = target;
				}
			}

			if (level == target) {
				if (level < 1) {
					level = levelThumbs.size();
					target = level;
				}
				if (level > levelThumbs.size()) {
					level = 1;
					target = level;
				}
			}

			buffer.blit(nTex, 0, 0, (int) nxPos, yPos, 128, 128, (int) (300 * difX), (int) (200 * difY), -1, false, null);
		}

		if (level != target) {
			if (target > level) {
				xPos -= ticks * 20;
			} else {
				xPos += ticks * 20;
			}
		}
	}
}
