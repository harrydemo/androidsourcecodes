package org.loon.framework.javase.game.utils;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;

import org.loon.framework.javase.game.core.graphics.opengl.GLColor;

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
public class BufferUtils {

	public static final int SIZEOF_BYTE = 1;

	public static final int SIZEOF_SHORT = 2;

	public static final int SIZEOF_FLOAT = 4;

	public static final int SIZEOF_INT = SIZEOF_FLOAT;

	public static final int SIZEOF_DOUBLE = 8;

	public static final int SIZEOF_LONG = SIZEOF_DOUBLE;

	public static ByteBuffer createByteBuffer(byte... bytes) {
		ByteBuffer bb = createByteBuffer(bytes.length).put(bytes);
		bb.position(0);
		return bb;
	}

	public static FloatBuffer replaceFloats(FloatBuffer dest, float... src) {
		dest.clear();
		dest.put(src);
		dest.position(0);
		return dest;
	}

	public static FloatBuffer replaceFloats(int destPos, FloatBuffer dest,
			int srcPos, float... src) {
		dest.position(destPos);
		dest.put(src, srcPos, src.length - srcPos);
		dest.position(0);
		return dest;
	}

	public static FloatBuffer createFloatBuffer(FloatBuffer buf) {
		FloatBuffer dest = createFloatBuffer(buf.capacity());
		dest.clear();
		dest.put(buf);
		dest.position(0);
		return dest;
	}

	public static FloatBuffer createFloatBuffer(FloatBuffer buf, int start,
			int end) {
		final float[] inds = new float[buf.limit()];
		for (int x = start - 1; x < end - 1; x++) {
			inds[x] = buf.get();
		}
		return createFloatBuffer(inds);
	}

	public static int getElementSizeExponent(Buffer buf) {
		if (buf instanceof ByteBuffer) {
			return 0;
		} else if (buf instanceof ShortBuffer || buf instanceof CharBuffer) {
			return 1;
		} else if (buf instanceof FloatBuffer || buf instanceof IntBuffer) {
			return 2;
		} else if (buf instanceof LongBuffer || buf instanceof DoubleBuffer) {
			return 3;
		} else {
			throw new IllegalStateException("Unsupported buf type: " + buf);
		}
	}

	public static int getOffset(Buffer buf) {
		return buf.position() << getElementSizeExponent(buf);
	}

	public static FloatBuffer createColorBuffer(final int colors) {
		final FloatBuffer colorBuff = createFloatBuffer(SIZEOF_FLOAT * colors);
		return colorBuff;
	}

	public static void setInBuffer(final GLColor color, final FloatBuffer buf,
			final int index) {
		buf.position(index * SIZEOF_FLOAT);
		buf.put(color.getRed());
		buf.put(color.getGreen());
		buf.put(color.getBlue());
		buf.put(color.getAlpha());
	}

	public static void populateFromBuffer(final GLColor store,
			final FloatBuffer buf, final int index) {
		store.r = (buf.get(index * SIZEOF_FLOAT));
		store.g = (buf.get(index * SIZEOF_FLOAT + 1));
		store.b = (buf.get(index * SIZEOF_FLOAT + 2));
		store.a = (buf.get(index * SIZEOF_FLOAT + 3));
	}

	public static GLColor[] getColorArray(final FloatBuffer buff) {
		buff.rewind();
		final GLColor[] colors = new GLColor[buff.limit() >> 2];
		for (int x = 0; x < colors.length; x++) {
			final GLColor c = new GLColor(buff.get(), buff.get(), buff.get(),
					buff.get());
			colors[x] = c;
		}
		return colors;
	}

	public static void copyInternalColor(final FloatBuffer buf,
			final int fromPos, final int toPos) {
		copyInternal(buf, fromPos * SIZEOF_FLOAT, toPos * SIZEOF_FLOAT,
				SIZEOF_FLOAT);
	}

	public static boolean equals(final GLColor check, final FloatBuffer buf,
			final int index) {
		final GLColor temp = new GLColor();
		populateFromBuffer(temp, buf, index);
		return temp.equals(check);
	}

	public static FloatBuffer createVector4Buffer(final int vertices) {
		final FloatBuffer vBuff = createFloatBuffer(SIZEOF_FLOAT * vertices);
		return vBuff;
	}

	public static FloatBuffer createVector4Buffer(final FloatBuffer buf,
			final int vertices) {
		if (buf != null && buf.limit() == SIZEOF_FLOAT * vertices) {
			buf.rewind();
			return buf;
		}
		return createFloatBuffer(SIZEOF_FLOAT * vertices);
	}

	public static FloatBuffer createVector3Buffer(final int vertices) {
		final FloatBuffer vBuff = createFloatBuffer(3 * vertices);
		return vBuff;
	}

	public static FloatBuffer createVector3Buffer(final FloatBuffer buf,
			final int vertices) {
		if (buf != null && buf.limit() == 3 * vertices) {
			buf.rewind();
			return buf;
		}

		return createFloatBuffer(3 * vertices);
	}

	public static void copyInternalVector3(final FloatBuffer buf,
			final int fromPos, final int toPos) {
		copyInternal(buf, fromPos * 3, toPos * 3, 3);
	}

	public static FloatBuffer createVector2Buffer(final int vertices) {
		final FloatBuffer vBuff = createFloatBuffer(2 * vertices);
		return vBuff;
	}

	public static FloatBuffer createVector2Buffer(final FloatBuffer buf,
			final int vertices) {
		if (buf != null && buf.limit() == 2 * vertices) {
			buf.rewind();
			return buf;
		}

		return createFloatBuffer(2 * vertices);
	}

	public static void copyInternalVector2(final FloatBuffer buf,
			final int fromPos, final int toPos) {
		copyInternal(buf, fromPos * 2, toPos * 2, 2);
	}

	public static IntBuffer createIntBuffer(final int... data) {
		if (data == null) {
			return null;
		}
		final IntBuffer buff = createIntBuffer(data.length);
		buff.clear();
		buff.put(data);
		buff.flip();
		return buff;
	}

	public static int[] getIntArray(final IntBuffer buff) {
		if (buff == null) {
			return null;
		}
		buff.rewind();
		final int[] inds = new int[buff.limit()];
		for (int x = 0; x < inds.length; x++) {
			inds[x] = buff.get();
		}
		return inds;
	}

	public static float[] getFloatArray(final FloatBuffer buff) {
		if (buff == null) {
			return null;
		}
		buff.clear();
		final float[] inds = new float[buff.limit()];
		for (int x = 0; x < inds.length; x++) {
			inds[x] = buff.get();
		}
		return inds;
	}

	public static byte[] getByteArray(final ByteBuffer buff) {
		if (buff == null) {
			return null;
		}
		buff.clear();
		final byte[] inds = new byte[buff.limit()];
		for (int x = 0; x < inds.length; x++) {
			inds[x] = buff.get();
		}
		return inds;
	}

	public static DoubleBuffer createDoubleBufferOnHeap(final int size) {
		final DoubleBuffer buf = ByteBuffer.allocate(SIZEOF_DOUBLE * size).order(
				ByteOrder.nativeOrder()).asDoubleBuffer();
		buf.clear();
		return buf;
	}

	public static DoubleBuffer createDoubleBuffer(final int size) {
		final DoubleBuffer buf = ByteBuffer.allocateDirect(SIZEOF_DOUBLE * size).order(
				ByteOrder.nativeOrder()).asDoubleBuffer();
		buf.clear();
		return buf;
	}

	public static DoubleBuffer createDoubleBuffer(DoubleBuffer buf,
			final int size) {
		if (buf != null && buf.limit() == size) {
			buf.rewind();
			return buf;
		}

		buf = createDoubleBuffer(size);
		return buf;
	}

	public static DoubleBuffer clone(final DoubleBuffer buf) {
		if (buf == null) {
			return null;
		}
		buf.rewind();
		final DoubleBuffer copy;
		if (buf.isDirect()) {
			copy = createDoubleBuffer(buf.limit());
		} else {
			copy = createDoubleBufferOnHeap(buf.limit());
		}
		copy.put(buf);

		return copy;
	}

	public static FloatBuffer createFloatBuffer(final int size) {
		final FloatBuffer buf = ByteBuffer.allocateDirect(SIZEOF_FLOAT * size)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		buf.clear();
		return buf;
	}

	public static FloatBuffer createFloatBufferOnHeap(final int size) {
		final FloatBuffer buf = ByteBuffer.allocate(SIZEOF_FLOAT * size).order(
				ByteOrder.nativeOrder()).asFloatBuffer();
		buf.clear();
		return buf;
	}

	public static FloatBuffer createFloatBuffer(float... floats) {
		FloatBuffer fb = createFloatBuffer(floats.length);
		fb.put(floats);
		fb.position(0);
		return fb;
	}

	public static FloatBuffer createFloatBuffer(final FloatBuffer reuseStore,
			final float... data) {
		if (data == null) {
			return null;
		}
		final FloatBuffer buff;
		if (reuseStore == null || reuseStore.capacity() != data.length) {
			buff = createFloatBuffer(data.length);
		} else {
			buff = reuseStore;
			buff.clear();
		}
		buff.clear();
		buff.put(data);
		buff.flip();
		return buff;
	}

	public static void copy(final float[] data, final FloatBuffer buf,
			final int fromPos, final int toPos) {
		buf.position(fromPos);
		buf.get(data);
		buf.position(toPos);
		buf.put(data);
	}

	public static void copyInternal(final FloatBuffer buf, final int fromPos,
			final int toPos, final int length) {
		final float[] data = new float[length];
		buf.position(fromPos);
		buf.get(data);
		buf.position(toPos);
		buf.put(data);
	}

	public static FloatBuffer clone(final FloatBuffer buf) {
		if (buf == null) {
			return null;
		}
		buf.rewind();
		final FloatBuffer copy;
		if (buf.isDirect()) {
			copy = createFloatBuffer(buf.limit());
		} else {
			copy = createFloatBufferOnHeap(buf.limit());
		}
		copy.put(buf);

		return copy;
	}

	public static IntBuffer createIntBufferOnHeap(final int size) {
		final IntBuffer buf = ByteBuffer.allocate(SIZEOF_FLOAT * size).order(
				ByteOrder.nativeOrder()).asIntBuffer();
		buf.clear();
		return buf;
	}

	public static IntBuffer createIntBuffer(final int size) {
		final IntBuffer buf = ByteBuffer.allocateDirect(SIZEOF_FLOAT * size)
				.order(ByteOrder.nativeOrder()).asIntBuffer();
		buf.clear();
		return buf;
	}

	public static IntBuffer createIntBuffer(IntBuffer buf, final int size) {
		if (buf != null && buf.limit() == size) {
			buf.rewind();
			return buf;
		}

		buf = createIntBuffer(size);
		return buf;
	}

	public static IntBuffer clone(final IntBuffer buf) {
		if (buf == null) {
			return null;
		}
		buf.rewind();

		final IntBuffer copy;
		if (buf.isDirect()) {
			copy = createIntBuffer(buf.limit());
		} else {
			copy = createIntBufferOnHeap(buf.limit());
		}
		copy.put(buf);

		return copy;
	}

	public static ByteBuffer createByteBuffer(final int size) {
		final ByteBuffer buf = ByteBuffer.allocateDirect(size).order(
				ByteOrder.nativeOrder());
		buf.clear();
		return buf;
	}

	public static ByteBuffer createByteBuffer(ByteBuffer buf, final int size) {
		if (buf != null && buf.limit() == size) {
			buf.rewind();
			return buf;
		}

		buf = createByteBuffer(size);
		return buf;
	}

	public static ByteBuffer clone(final ByteBuffer buf) {
		if (buf == null) {
			return null;
		}
		buf.rewind();

		final ByteBuffer copy;
		if (buf.isDirect()) {
			copy = createByteBuffer(buf.limit());
		} else {
			copy = createByteBufferOnHeap(buf.limit());
		}
		copy.put(buf);

		return copy;
	}

	public static ShortBuffer createShortBufferOnHeap(final int size) {
		final ShortBuffer buf = ByteBuffer.allocate(SIZEOF_SHORT * size).order(
				ByteOrder.nativeOrder()).asShortBuffer();
		buf.clear();
		return buf;
	}

	public static ShortBuffer createShortBuffer(final int size) {
		final ShortBuffer buf = ByteBuffer.allocateDirect(SIZEOF_SHORT * size).order(
				ByteOrder.nativeOrder()).asShortBuffer();
		buf.clear();
		return buf;
	}

	public static ShortBuffer createShortBuffer(final short... data) {
		if (data == null) {
			return null;
		}
		final ShortBuffer buff = createShortBuffer(data.length);
		buff.clear();
		buff.put(data);
		buff.flip();
		return buff;
	}

	public static ShortBuffer createShortBuffer(ShortBuffer buf, final int size) {
		if (buf != null && buf.limit() == size) {
			buf.rewind();
			return buf;
		}

		buf = createShortBuffer(size);
		return buf;
	}

	public static ShortBuffer clone(final ShortBuffer buf) {
		if (buf == null) {
			return null;
		}
		buf.rewind();

		final ShortBuffer copy;
		if (buf.isDirect()) {
			copy = createShortBuffer(buf.limit());
		} else {
			copy = createShortBufferOnHeap(buf.limit());
		}
		copy.put(buf);

		return copy;
	}

	public static FloatBuffer ensureLargeEnough(FloatBuffer buf,
			final int required) {
		if (buf == null || (buf.remaining() < required)) {
			final int position = (buf != null ? buf.position() : 0);
			final FloatBuffer newVerts = createFloatBuffer(position + required);
			if (buf != null) {
				buf.rewind();
				newVerts.put(buf);
				newVerts.position(position);
			}
			buf = newVerts;
		}
		return buf;
	}

	public static ByteBuffer createByteBufferOnHeap(final int size) {
		final ByteBuffer buf = ByteBuffer.allocate(size).order(
				ByteOrder.nativeOrder());
		buf.clear();
		return buf;
	}

	public static ByteBuffer createByteBufferOnHeap(ByteBuffer buf,
			final int size) {
		if (buf != null && buf.limit() == size) {
			buf.rewind();
			return buf;
		}

		buf = createByteBufferOnHeap(size);
		return buf;
	}

	public static ByteBuffer cloneOnHeap(final ByteBuffer buf) {
		if (buf == null) {
			return null;
		}
		buf.rewind();

		final ByteBuffer copy = createByteBufferOnHeap(buf.limit());
		copy.put(buf);

		return copy;
	}

	public static ByteBuffer newByteBuffer(int numElements) {
		ByteBuffer bb = ByteBuffer.allocateDirect(numElements);
		bb.order(ByteOrder.nativeOrder());
		return bb;
	}

	public static DoubleBuffer newDoubleBuffer(int numElements) {
		ByteBuffer bb = newByteBuffer(numElements * SIZEOF_DOUBLE);
		return bb.asDoubleBuffer();
	}

	public static FloatBuffer newFloatBuffer(int numElements) {
		ByteBuffer bb = newByteBuffer(numElements * SIZEOF_FLOAT);
		return bb.asFloatBuffer();
	}

	public static IntBuffer newIntBuffer(int numElements) {
		ByteBuffer bb = newByteBuffer(numElements * SIZEOF_INT);
		return bb.asIntBuffer();
	}

	public static LongBuffer newLongBuffer(int numElements) {
		ByteBuffer bb = newByteBuffer(numElements * SIZEOF_LONG);
		return bb.asLongBuffer();
	}

	public static ShortBuffer newShortBuffer(int numElements) {
		ByteBuffer bb = newByteBuffer(numElements * SIZEOF_SHORT);
		return bb.asShortBuffer();
	}

	public static ByteBuffer copyByteBuffer(ByteBuffer buf) {
		ByteBuffer dest = newByteBuffer(buf.remaining());
		buf.mark();
		dest.put(buf);
		buf.reset();
		dest.rewind();
		return dest;
	}

	public static DoubleBuffer copyDoubleBuffer(DoubleBuffer buf) {
		return copyDoubleBufferAsByteBuffer(buf).asDoubleBuffer();
	}

	public static FloatBuffer copyFloatBuffer(FloatBuffer buf) {
		return copyFloatBufferAsByteBuffer(buf).asFloatBuffer();
	}

	public static IntBuffer copyIntBuffer(IntBuffer buf) {
		return copyIntBufferAsByteBuffer(buf).asIntBuffer();
	}

	public static LongBuffer copyLongBuffer(LongBuffer buf) {
		return copyLongBufferAsByteBuffer(buf).asLongBuffer();
	}

	public static ShortBuffer copyShortBuffer(ShortBuffer buf) {
		return copyShortBufferAsByteBuffer(buf).asShortBuffer();
	}

	public static ByteBuffer copyDoubleBufferAsByteBuffer(DoubleBuffer buf) {
		ByteBuffer dest = newByteBuffer(buf.remaining() * SIZEOF_DOUBLE);
		buf.mark();
		dest.asDoubleBuffer().put(buf);
		buf.reset();
		dest.rewind();
		return dest;
	}

	public static ByteBuffer copyFloatBufferAsByteBuffer(FloatBuffer buf) {
		ByteBuffer dest = newByteBuffer(buf.remaining() * SIZEOF_FLOAT);
		buf.mark();
		dest.asFloatBuffer().put(buf);
		buf.reset();
		dest.rewind();
		return dest;
	}

	public static ByteBuffer copyIntBufferAsByteBuffer(IntBuffer buf) {
		ByteBuffer dest = newByteBuffer(buf.remaining() * SIZEOF_INT);
		buf.mark();
		dest.asIntBuffer().put(buf);
		buf.reset();
		dest.rewind();
		return dest;
	}

	public static ByteBuffer copyLongBufferAsByteBuffer(LongBuffer buf) {
		ByteBuffer dest = newByteBuffer(buf.remaining() * SIZEOF_LONG);
		buf.mark();
		dest.asLongBuffer().put(buf);
		buf.reset();
		dest.rewind();
		return dest;
	}

	public static ByteBuffer copyShortBufferAsByteBuffer(ShortBuffer buf) {
		ByteBuffer dest = newByteBuffer(buf.remaining() * SIZEOF_SHORT);
		buf.mark();
		dest.asShortBuffer().put(buf);
		buf.reset();
		dest.rewind();
		return dest;
	}

}
