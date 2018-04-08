package org.loon.framework.javase.game;

import java.nio.IntBuffer;

import org.loon.framework.javase.game.utils.BufferUtils;
import org.lwjgl.input.Cursor;

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
public class GameCursor extends Cursor {

	public static Cursor cursor;

	private static IntBuffer bufferByte;

	private static int size = 32;

	static {
		bufferByte = BufferUtils.createIntBuffer(size * size);

		int row = 0;
		bufferByte.put(row * size + 8, 0xFF000000);
		bufferByte.put(row * size + 9, 0xFF000000);

		row++;
		bufferByte.put(row * size + 7, 0xFF000000);
		bufferByte.put(row * size + 8, 0xFFFFFFFF);
		bufferByte.put(row * size + 9, 0xFFFFFFFF);
		bufferByte.put(row * size + 10, 0xFF000000);

		row++;
		bufferByte.put(row * size + 7, 0xFF000000);
		bufferByte.put(row * size + 8, 0xFFFFFFFF);
		bufferByte.put(row * size + 9, 0xFFFFFFFF);
		bufferByte.put(row * size + 10, 0xFF000000);

		row++;
		bufferByte.put(row * size + 6, 0xFF000000);
		bufferByte.put(row * size + 7, 0xFFFFFFFF);
		bufferByte.put(row * size + 8, 0xFFFFFFFF);
		bufferByte.put(row * size + 9, 0xFF000000);

		row++;
		bufferByte.put(row * size, 0xFF000000);
		bufferByte.put(row * size + 6, 0xFF000000);
		bufferByte.put(row * size + 7, 0xFFFFFFFF);
		bufferByte.put(row * size + 8, 0xFFFFFFFF);
		bufferByte.put(row * size + 9, 0xFF000000);

		row++;
		bufferByte.put(row * size, 0xFF000000);
		bufferByte.put(row * size + 1, 0xFF000000);
		bufferByte.put(row * size + 5, 0xFF000000);
		bufferByte.put(row * size + 6, 0xFFFFFFFF);
		bufferByte.put(row * size + 7, 0xFFFFFFFF);
		bufferByte.put(row * size + 8, 0xFF000000);

		row++;
		bufferByte.put(row * size, 0xFF000000);
		bufferByte.put(row * size + 1, 0xFFFFFFFF);
		bufferByte.put(row * size + 2, 0xFF000000);
		bufferByte.put(row * size + 5, 0xFF000000);
		bufferByte.put(row * size + 6, 0xFFFFFFFF);
		bufferByte.put(row * size + 7, 0xFFFFFFFF);
		bufferByte.put(row * size + 8, 0xFF000000);

		row++;
		bufferByte.put(row * size, 0xFF000000);
		bufferByte.put(row * size + 1, 0xFFFFFFFF);
		bufferByte.put(row * size + 2, 0xFFFFFFFF);
		bufferByte.put(row * size + 3, 0xFF000000);
		bufferByte.put(row * size + 4, 0xFF000000);
		bufferByte.put(row * size + 5, 0xFFFFFFFF);
		bufferByte.put(row * size + 6, 0xFFFFFFFF);
		bufferByte.put(row * size + 7, 0xFF000000);

		row++;
		bufferByte.put(row * size, 0xFF000000);
		bufferByte.put(row * size + 1, 0xFFFFFFFF);
		bufferByte.put(row * size + 2, 0xFFFFFFFF);
		bufferByte.put(row * size + 3, 0xFFFFFFFF);
		bufferByte.put(row * size + 4, 0xFF000000);
		bufferByte.put(row * size + 5, 0xFFFFFFFF);
		bufferByte.put(row * size + 6, 0xFFFFFFFF);
		bufferByte.put(row * size + 7, 0xFF000000);

		row++;
		bufferByte.put(row * size, 0xFF000000);
		bufferByte.put(row * size + 1, 0xFFFFFFFF);
		bufferByte.put(row * size + 2, 0xFFFFFFFF);
		bufferByte.put(row * size + 3, 0xFFFFFFFF);
		bufferByte.put(row * size + 4, 0xFFFFFFFF);
		bufferByte.put(row * size + 5, 0xFFFFFFFF);
		bufferByte.put(row * size + 6, 0xFFFFFFFF);
		bufferByte.put(row * size + 7, 0xFF000000);
		bufferByte.put(row * size + 8, 0xFF000000);
		bufferByte.put(row * size + 9, 0xFF000000);
		bufferByte.put(row * size + 10, 0xFF000000);
		bufferByte.put(row * size + 11, 0xFF000000);

		row++;
		bufferByte.put(row * size, 0xFF000000);
		bufferByte.put(row * size + 1, 0xFFFFFFFF);
		bufferByte.put(row * size + 2, 0xFFFFFFFF);
		bufferByte.put(row * size + 3, 0xFFFFFFFF);
		bufferByte.put(row * size + 4, 0xFFFFFFFF);
		bufferByte.put(row * size + 5, 0xFFFFFFFF);
		bufferByte.put(row * size + 6, 0xFFFFFFFF);
		bufferByte.put(row * size + 7, 0xFFFFFFFF);
		bufferByte.put(row * size + 8, 0xFFFFFFFF);
		bufferByte.put(row * size + 9, 0xFFFFFFFF);
		bufferByte.put(row * size + 10, 0xFF000000);

		row++;
		bufferByte.put(row * size, 0xFF000000);
		bufferByte.put(row * size + 1, 0xFFFFFFFF);
		bufferByte.put(row * size + 2, 0xFFFFFFFF);
		bufferByte.put(row * size + 3, 0xFFFFFFFF);
		bufferByte.put(row * size + 4, 0xFFFFFFFF);
		bufferByte.put(row * size + 5, 0xFFFFFFFF);
		bufferByte.put(row * size + 6, 0xFFFFFFFF);
		bufferByte.put(row * size + 7, 0xFFFFFFFF);
		bufferByte.put(row * size + 8, 0xFFFFFFFF);
		bufferByte.put(row * size + 9, 0xFF000000);

		row++;
		bufferByte.put(row * size, 0xFF000000);
		bufferByte.put(row * size + 1, 0xFFFFFFFF);
		bufferByte.put(row * size + 2, 0xFFFFFFFF);
		bufferByte.put(row * size + 3, 0xFFFFFFFF);
		bufferByte.put(row * size + 4, 0xFFFFFFFF);
		bufferByte.put(row * size + 5, 0xFFFFFFFF);
		bufferByte.put(row * size + 6, 0xFFFFFFFF);
		bufferByte.put(row * size + 7, 0xFFFFFFFF);
		bufferByte.put(row * size + 8, 0xFF000000);

		row++;
		bufferByte.put(row * size, 0xFF000000);
		bufferByte.put(row * size + 1, 0xFFFFFFFF);
		bufferByte.put(row * size + 2, 0xFFFFFFFF);
		bufferByte.put(row * size + 3, 0xFFFFFFFF);
		bufferByte.put(row * size + 4, 0xFFFFFFFF);
		bufferByte.put(row * size + 5, 0xFFFFFFFF);
		bufferByte.put(row * size + 6, 0xFFFFFFFF);
		bufferByte.put(row * size + 7, 0xFF000000);

		row++;
		bufferByte.put(row * size, 0xFF000000);
		bufferByte.put(row * size + 1, 0xFFFFFFFF);
		bufferByte.put(row * size + 2, 0xFFFFFFFF);
		bufferByte.put(row * size + 3, 0xFFFFFFFF);
		bufferByte.put(row * size + 4, 0xFFFFFFFF);
		bufferByte.put(row * size + 5, 0xFFFFFFFF);
		bufferByte.put(row * size + 6, 0xFF000000);

		row++;
		bufferByte.put(row * size, 0xFF000000);
		bufferByte.put(row * size + 1, 0xFFFFFFFF);
		bufferByte.put(row * size + 2, 0xFFFFFFFF);
		bufferByte.put(row * size + 3, 0xFFFFFFFF);
		bufferByte.put(row * size + 4, 0xFFFFFFFF);
		bufferByte.put(row * size + 5, 0xFF000000);

		row++;
		bufferByte.put(row * size, 0xFF000000);
		bufferByte.put(row * size + 1, 0xFFFFFFFF);
		bufferByte.put(row * size + 2, 0xFFFFFFFF);
		bufferByte.put(row * size + 3, 0xFFFFFFFF);
		bufferByte.put(row * size + 4, 0xFF000000);

		row++;
		bufferByte.put(row * size, 0xFF000000);
		bufferByte.put(row * size + 1, 0xFFFFFFFF);
		bufferByte.put(row * size + 2, 0xFFFFFFFF);
		bufferByte.put(row * size + 3, 0xFF000000);

		row++;
		bufferByte.put(row * size, 0xFF000000);
		bufferByte.put(row * size + 1, 0xFFFFFFFF);
		bufferByte.put(row * size + 2, 0xFF000000);

		row++;
		bufferByte.put(row * size, 0xFF000000);
		bufferByte.put(row * size + 1, 0xFF000000);

		row++;
		bufferByte.put(row * size, 0xFF000000);

		try {
			cursor = new GameCursor();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public GameCursor() throws Exception {
		super(size, size, 0, 19, 1, bufferByte, null);
	}

}
