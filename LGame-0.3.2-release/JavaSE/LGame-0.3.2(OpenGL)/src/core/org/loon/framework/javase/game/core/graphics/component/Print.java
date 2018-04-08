package org.loon.framework.javase.game.core.graphics.component;

import java.util.ArrayList;

import org.loon.framework.javase.game.core.LRelease;
import org.loon.framework.javase.game.core.LSystem;
import org.loon.framework.javase.game.core.geom.Vector2f;
import org.loon.framework.javase.game.core.graphics.LFont;
import org.loon.framework.javase.game.core.graphics.LImage;
import org.loon.framework.javase.game.core.graphics.opengl.GLColor;
import org.loon.framework.javase.game.core.graphics.opengl.GLEx;
import org.loon.framework.javase.game.core.graphics.opengl.LSTRFont;
import org.loon.framework.javase.game.core.graphics.opengl.LSTRTexture;
import org.loon.framework.javase.game.core.graphics.opengl.LTexture;
import org.loon.framework.javase.game.core.graphics.opengl.LTexture.Format;
import org.loon.framework.javase.game.utils.StringUtils;

/**
 * Copyright 2008 - 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * @project loonframework
 * @author chenpeng
 * @email ceponline@yahoo.com.cn
 * @version 0.1.1
 */
public class Print implements LRelease {

	private char[] showMessages;

	private int iconWidth;

	private GLColor fontColor = GLColor.white;

	private int interceptMaxString;

	private int interceptCount;

	private int messageLength = 10;

	private String messages;

	private boolean onComplete, newLine, visible, isClose;

	private StringBuffer messageBuffer = new StringBuffer(messageLength);

	private int width, height, leftOffset, topOffset, next, messageCount;

	private float alpha, move_x, move_y;

	private int size, wait, tmp_left, left, fontSize, fontHeight;

	private Vector2f vector;

	private LTexture creeseIcon;

	private LTexture cacheTexture;

	private boolean isUpdate;

	private boolean isEnglish, isLeft, isWait, isCache;

	private float iconX, iconY;

	private LFont deffont;

	private ArrayList<Integer> copyCacheTexturePoints = new ArrayList<Integer>(
			100);

	private final static ArrayList<String> lazyString = new ArrayList<String>(
			30);

	public Print(Vector2f vector, LFont font, int width, int height) {
		this("", font, vector, width, height);
	}

	public Print(String context, LFont font, Vector2f vector, int width,
			int height) {
		this.setMessage(context, font);
		this.deffont = font;
		this.vector = vector;
		this.width = width;
		this.height = height;
		this.wait = 0;
		this.isCache = false;
		this.isWait = false;
	}

	private boolean isSupportCache() {
		return isCache && !GLEx.isVbo() && (width < 1024 && height < 1024);
	}

	public void setMessage(String context, LFont font) {
		setMessage(context, font, false);
	}

	public void setMessage(String context, LFont font, boolean isComplete) {
		synchronized (lazyString) {
			this.isClose = true;
			if (isSupportCache()) {
				if (copyCacheTexturePoints != null) {
					copyCacheTexturePoints.clear();
				}
				if (cacheTexture != null) {
					cacheTexture.destroy();
					cacheTexture = null;
				}
				LSystem.gc();
			} else {
				if (lazyString.size() > 10) {
					dispose();
				}
				if (context != null && context.length() > 0) {
					LSTRTexture.bindStringLazy(font, context);
					lazyString.add(context);
				}
			}
			this.wait = 0;
			this.deffont = font;
			this.visible = false;
			this.showMessages = new char[] { '\0' };
			this.interceptMaxString = 0;
			this.next = 0;
			this.messageCount = 0;
			this.interceptCount = 0;
			this.size = 0;
			this.tmp_left = 0;
			this.left = 0;
			this.fontSize = 0;
			this.fontHeight = 0;
			this.messages = context;
			this.next = context.length();
			this.onComplete = false;
			this.newLine = false;
			this.messageCount = 0;
			this.messageBuffer.delete(0, messageBuffer.length());
			if (isComplete) {
				this.complete();
			}
			this.isClose = false;
			this.visible = true;
		}
	}

	public String getMessage() {
		return messages;
	}

	private GLColor getColor(char flagName) {
		if ('r' == flagName || 'R' == flagName) {
			return GLColor.red;
		}
		if ('b' == flagName || 'B' == flagName) {
			return GLColor.black;
		}
		if ('l' == flagName || 'L' == flagName) {
			return GLColor.blue;
		}
		if ('g' == flagName || 'G' == flagName) {
			return GLColor.green;
		}
		if ('o' == flagName || 'O' == flagName) {
			return GLColor.orange;
		}
		if ('y' == flagName || 'Y' == flagName) {
			return GLColor.yellow;
		} else {
			return null;
		}
	}

	public void draw(GLEx g) {
		draw(g, GLColor.white);
	}

	private void drawMessage(GLEx g, GLColor old) {
		if (!visible) {
			return;
		}
		if (isClose) {
			return;
		}
		if (isSupportCache()) {
			$draw_cache(g, old);
		} else {
			$draw_not_cache(g, old);
		}
	}

	private void $draw_cache(GLEx g, GLColor old) {
		if (cacheTexture != null) {
			g.drawTexture(cacheTexture, move_x, move_y);
			if (!newLine && !onComplete) {
				g.drawTexture(creeseIcon, iconX, iconY);
			}
		}
		synchronized (showMessages) {
			float ascent = deffont.getAscent();
			if (cacheTexture == null) {
				cacheTexture = new LTexture(width, height, true, Format.FONT);
			}
			int hashCode = 0;
			int index = 0, offset = 0, font = 0, tmp_font = 0;
			int x = 0;
			int y = 0;
			if (isUpdate) {
				isUpdate = false;
			} else {
				this.size = showMessages.length;
				this.fontSize = isEnglish ? deffont.getSize() / 2 : g.getFont()
						.getSize();
				this.fontHeight = deffont.getHeight();
				this.tmp_left = isLeft ? 0
						: (width - (fontSize * messageLength)) / 2
								- (int) (fontSize * 1.5);
				this.left = tmp_left;
				int fontSizeDouble = fontSize * 2;
				char charString;
				fontColor = old;
				for (int i = 0; i < size; i++) {
					charString = showMessages[i];
					if (interceptCount < interceptMaxString) {
						interceptCount++;
						continue;
					} else {
						interceptMaxString = 0;
						interceptCount = 0;
					}
					if (showMessages[i] == 'n'
							&& showMessages[i > 0 ? i - 1 : 0] == '\\') {
						index = 0;
						left = tmp_left;
						offset++;
						continue;
					} else if (charString == '\n') {
						index = 0;
						left = tmp_left;
						offset++;
						continue;
					} else if (charString == '<') {
						GLColor color = getColor(showMessages[i < size - 1 ? i + 1
								: i]);
						if (color != null) {
							interceptMaxString = 1;
							fontColor = color;
						}
						continue;
					} else if (showMessages[i > 0 ? i - 1 : i] == '<'
							&& getColor(charString) != null) {
						continue;
					} else if (charString == '/') {
						if (showMessages[i < size - 1 ? i + 1 : i] == '>') {
							interceptMaxString = 1;
							fontColor = old;
						}
						continue;
					} else if (index > messageLength) {
						index = 0;
						left = tmp_left;
						offset++;
						newLine = false;
					} else if (charString == '\\') {
						continue;
					}
					String mes = String.valueOf(charString);
					tmp_font = deffont.charWidth(charString);
					if (Character.isLetter(charString)) {
						if (tmp_font < fontSize) {
							font = fontSize;
						} else {
							font = tmp_font;
						}
					} else {
						font = fontSize;
					}
					left += font;
					if (font <= 10 && StringUtils.isSingle(charString)) {
						left += 12;
					}
					if (i != size - 1) {
						x = left + leftOffset;
						y = (offset * fontHeight) + fontSizeDouble + topOffset;
						hashCode = 0;
						hashCode = LSystem.unite(hashCode, x);
						hashCode = LSystem.unite(hashCode, y);
						hashCode = LSystem.unite(hashCode, mes.hashCode());
						if (!copyCacheTexturePoints.contains(hashCode)) {
							LImage temp = LSTRFont.createFontImage(deffont,
									fontColor, mes);
							g.copyImageToTexture(cacheTexture, temp, x, y,
									false, false);
							if (temp != null) {
								temp.dispose();
								temp = null;
							}
							copyCacheTexturePoints.add(hashCode);
						}
					} else if (!newLine && !onComplete) {
						iconX = vector.x + left + leftOffset + iconWidth;
						iconY = (offset * fontHeight) + vector.y + fontSize
								+ topOffset + ascent;
					}
					index++;
				}
				move_x = vector.x;
				move_y = vector.y - ascent;
				isUpdate = true;
				if (messageCount == next) {
					onComplete = true;
				}
			}
		}
	}

	private void $draw_not_cache(GLEx g, GLColor old) {
		synchronized (showMessages) {
			this.size = showMessages.length;
			this.fontSize = isEnglish ? deffont.getSize() / 2 : g.getFont()
					.getSize();
			this.fontHeight = deffont.getHeight();
			this.tmp_left = isLeft ? 0 : (width - (fontSize * messageLength))
					/ 2 - (int) (fontSize * 1.5);
			this.left = tmp_left;
			int index = 0, offset = 0, font = 0, tmp_font = 0;
			int fontSizeDouble = fontSize * 2;
			char charString;
			g.setColor(old);
			for (int i = 0; i < size; i++) {
				charString = showMessages[i];
				if (interceptCount < interceptMaxString) {
					interceptCount++;
					g.setColor(fontColor);
					continue;
				} else {
					interceptMaxString = 0;
					interceptCount = 0;
				}
				if (showMessages[i] == 'n'
						&& showMessages[i > 0 ? i - 1 : 0] == '\\') {
					index = 0;
					left = tmp_left;
					offset++;
					continue;
				} else if (charString == '\n') {
					index = 0;
					left = tmp_left;
					offset++;
					continue;
				} else if (charString == '<') {
					GLColor color = getColor(showMessages[i < size - 1 ? i + 1
							: i]);
					if (color != null) {
						interceptMaxString = 1;
						fontColor = color;
					}
					continue;
				} else if (showMessages[i > 0 ? i - 1 : i] == '<'
						&& getColor(charString) != null) {
					continue;
				} else if (charString == '/') {
					if (showMessages[i < size - 1 ? i + 1 : i] == '>') {
						interceptMaxString = 1;
						fontColor = old;
					}
					continue;
				} else if (index > messageLength) {
					index = 0;
					left = tmp_left;
					offset++;
					newLine = false;
				} else if (charString == '\\') {
					continue;
				}
				String mes = String.valueOf(charString);
				tmp_font = deffont.charWidth(charString);
				if (Character.isLetter(charString)) {
					if (tmp_font < fontSize) {
						font = fontSize;
					} else {
						font = tmp_font;
					}
				} else {
					font = fontSize;
				}
				left += font;
				if (font <= 10 && StringUtils.isSingle(charString)) {
					left += 12;
				}
				if (i != size - 1) {
					g.drawEastString(mes, vector.x + left + leftOffset,
							(offset * fontHeight) + vector.y + fontSizeDouble
									+ topOffset);
				} else if (!newLine && !onComplete) {
					g.resetColor();
					g.drawTexture(creeseIcon, vector.x + left + leftOffset
							+ iconWidth, (offset * fontHeight) + vector.y
							+ fontSize + topOffset + deffont.getAscent());
				}
				index++;
			}
			if (messageCount == next) {
				onComplete = true;
			}
		}
	}

	public void draw(GLEx g, GLColor old) {
		if (!visible) {
			return;
		}
		alpha = g.getAlpha();
		if (alpha > 0 && alpha < 1) {
			g.setAlpha(1.0f);
		}
		drawMessage(g, old);
		if (alpha > 0 && alpha < 1) {
			g.setAlpha(alpha);
		}
	}

	public void setX(int x) {
		vector.setX(x);
	}

	public void setY(int y) {
		vector.setY(y);
	}

	public int getX() {
		return vector.x();
	}

	public int getY() {
		return vector.y();
	}

	public void complete() {
		synchronized (showMessages) {
			this.onComplete = true;
			this.messageCount = messages.length();
			this.next = messageCount;
			this.showMessages = (messages + "_").toCharArray();
			this.size = showMessages.length;
		}
	}

	public boolean isComplete() {
		if (isWait) {
			if (onComplete) {
				wait++;
			}
			return onComplete && wait > 100;
		}
		return onComplete;
	}

	public boolean next() {
		synchronized (messageBuffer) {
			if (!onComplete) {
				if (messageCount == next) {
					onComplete = true;
					return false;
				}
				if (messageBuffer.length() > 0) {
					messageBuffer.delete(messageBuffer.length() - 1,
							messageBuffer.length());
				}
				this.messageBuffer.append(messages.charAt(messageCount));
				this.messageBuffer.append("_");
				this.showMessages = messageBuffer.toString().toCharArray();
				this.size = showMessages.length;
				this.messageCount++;
			} else {
				return false;
			}
			return true;
		}
	}

	public LTexture getCreeseIcon() {
		return creeseIcon;
	}

	public void setCreeseIcon(LTexture icon) {
		if (this.creeseIcon != null) {
			creeseIcon.destroy();
			creeseIcon = null;
		}
		this.creeseIcon = icon;
		if (icon == null) {
			return;
		}
		this.iconWidth = icon.getWidth();
	}

	public int getMessageLength() {
		return messageLength;
	}

	public void setMessageLength(int messageLength) {
		this.messageLength = messageLength;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getLeftOffset() {
		return leftOffset;
	}

	public void setLeftOffset(int leftOffset) {
		this.leftOffset = leftOffset;
	}

	public int getTopOffset() {
		return topOffset;
	}

	public void setTopOffset(int topOffset) {
		this.topOffset = topOffset;
	}

	public boolean isEnglish() {
		return isEnglish;
	}

	public void setEnglish(boolean isEnglish) {
		this.isEnglish = isEnglish;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isLeft() {
		return isLeft;
	}

	public void setLeft(boolean isLeft) {
		this.isLeft = isLeft;
	}

	public boolean isWait() {
		return isWait;
	}

	public void setWait(boolean isWait) {
		this.isWait = isWait;
	}

	public boolean isTextCache() {
		return isCache;
	}

	public void setTextCache(boolean isCache) {
		this.isCache = isCache;
	}

	public boolean isClose() {
		return this.isClose;
	}

	public void dispose() {
		isClose = true;
		if (lazyString.size() > 0) {
			for (String string : lazyString) {
				LSTRTexture.unloadStringLazy(deffont, string);
			}
			lazyString.clear();
		}
		if (cacheTexture != null) {
			cacheTexture.destroy();
			cacheTexture = null;
		}
	}

}
