package org.loon.framework.javase.game.core.input;

import org.loon.framework.javase.game.core.input.LInputFactory.Touch;
import org.loon.framework.javase.game.utils.collection.ArrayByte;

/**
 * Copyright 2008 - 2011
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
 * @email：ceponline@yahoo.com.cn
 * @version 0.1
 */
public class LTouch {

	int type;

	float x, y;

	int button;

	int pointer;

	public LTouch(byte[] out) {
		in(out);
	}

	LTouch() {

	}

	LTouch(LTouch touch) {
		this.type = touch.type;
		this.x = touch.x;
		this.y = touch.y;
		this.button = touch.button;
		this.pointer = touch.pointer;
	}

	public boolean equals(LTouch e) {
		if (e == null) {
			return false;
		}
		if (e == this) {
			return true;
		}
		if (e.type == type && e.x == x && e.y == y && e.button == button
				&& e.pointer == pointer) {
			return true;
		}
		return false;
	}

	public int getButton() {
		return button;
	}

	public int getPointer() {
		return pointer;
	}

	public int getType() {
		return type;
	}

	public int x() {
		return (int) x;
	}

	public int y() {
		return (int) y;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public boolean isDown() {
		return type == Touch.TOUCH_DOWN;
	}

	public boolean isUp() {
		return type == Touch.TOUCH_UP;
	}

	public boolean isMove() {
		return type == Touch.TOUCH_MOVE;
	}

	public boolean isDrag() {
		return LInputFactory.isDraging;
	}

	public boolean isLeft() {
		return button == Touch.LEFT;
	}

	public boolean isMiddle() {
		return button == Touch.MIDDLE;
	}

	public boolean isRight() {
		return button == Touch.RIGHT;
	}

	public byte[] out() {
		ArrayByte touchByte = new ArrayByte();
		touchByte.writeInt(x());
		touchByte.writeInt(y());
		touchByte.writeInt(getButton());
		touchByte.writeInt(getPointer());
		touchByte.writeInt(getType());
		return touchByte.getData();
	}

	public void in(byte[] out) {
		ArrayByte touchByte = new ArrayByte(out);
		x = touchByte.readInt();
		y = touchByte.readInt();
		button = touchByte.readInt();
		pointer = touchByte.readInt();
		type = touchByte.readInt();
	}

}
