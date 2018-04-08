package org.loon.framework.android.game.utils;

import java.lang.reflect.Method;

import android.view.MotionEvent;

/**
 * 
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
 * @version 0.1.0
 */
public class MultitouchUtils {

	public static final int ACTION_POINTER_1_DOWN = 0x00000005;
	public static final int ACTION_POINTER_1_UP = 0x00000006;
	public static final int ACTION_POINTER_2_DOWN = 0x00000105;
	public static final int ACTION_POINTER_2_UP = 0x00000106;
	public static final int ACTION_POINTER_3_DOWN = 0x00000205;
	public static final int ACTION_POINTER_3_UP = 0x00000206;
	public static final int ACTION_POINTER_4_DOWN = 0x00000305;
	public static final int ACTION_POINTER_4_UP = 0x00000306;
	
	private static Method motionEvent_GetPointerCount;
	private static Method motionEvent_GetPointerId;
	private static Method motionEvent_FindPointerIndex;
	private static Method motionEvent_GetX;
	private static Method motionEvent_GetY;

	private static boolean isMultitouch;

	private static Object[] emptyObjectArray = new Object[] {};

	static {
		try {
			motionEvent_GetPointerCount = MotionEvent.class.getMethod(
					"getPointerCount", new Class[] {});
			motionEvent_GetPointerId = MotionEvent.class.getMethod(
					"getPointerId", new Class[] { int.class });
			motionEvent_FindPointerIndex = MotionEvent.class.getMethod(
					"findPointerIndex", new Class[] { int.class });
			motionEvent_GetX = MotionEvent.class.getMethod("getX",
					new Class[] { int.class });
			motionEvent_GetY = MotionEvent.class.getMethod("getY",
					new Class[] { int.class });
			isMultitouch = true;
		} catch (NoSuchMethodException ex) {
			isMultitouch = false;
		}
	};

	/**
	 * 判断当前系统是否支持多点触摸
	 * 
	 * @return
	 */
	public static boolean isMultitouch() {
		return isMultitouch;
	}

	/**
	 * 获得同一时间内屏幕触摸次数
	 * 
	 * @param e
	 * @return
	 */
	public static int getPointerCount(MotionEvent e) {
		if (!isMultitouch) {
			return 1;
		}
		try {
			int pointerCount = (Integer) motionEvent_GetPointerCount.invoke(e,
					emptyObjectArray);
			return pointerCount;
		} catch (Exception ex) {
			throw new RuntimeException("Reflected multitouch method failed!",
					ex);
		}
	}

	/**
	 * 查找对应触摸点索引
	 * 
	 * @param e
	 * @param id
	 * @return
	 */
	public static int findPointerIndex(MotionEvent e, int id) {
		if (!isMultitouch) {
			return 0;
		}
		try {
			int pointerIndex = (Integer) motionEvent_FindPointerIndex.invoke(e,
					new Object[] { id });
			return pointerIndex;
		} catch (Exception ex) {
			throw new RuntimeException("Reflected multitouch method failed!",
					ex);
		}
	}

	/**
	 * 获得对应触摸点的ID
	 * 
	 * @param e
	 * @param pointerIndex
	 * @return
	 */
	public static int getPointerId(MotionEvent e, int pointerIndex) {
		if (!isMultitouch) {
			return 0;
		}
		try {
			int pointerCount = (Integer) motionEvent_GetPointerId.invoke(e,
					new Object[] { pointerIndex });
			return pointerCount;
		} catch (Exception ex) {
			throw new RuntimeException("Reflected multitouch method failed!",
					ex);
		}
	}

	/**
	 * 获得多点触摸下的X轴
	 * 
	 * @param e
	 * @param pointerIndex
	 * @return
	 */
	public static float getX(MotionEvent e, int pointerIndex) {
		if (!isMultitouch) {
			return e.getY();
		}
		try {
			float pointerCount = (Float) motionEvent_GetX.invoke(e,
					new Object[] { pointerIndex });
			return pointerCount;
		} catch (Exception ex) {
			throw new RuntimeException("Reflected multitouch method failed!",
					ex);
		}
	}

	/**
	 * 获得多点触摸下的Y轴
	 * 
	 * @param e
	 * @param pointerIndex
	 * @return
	 */
	public static float getY(MotionEvent e, int pointerIndex) {
		if (!isMultitouch) {
			return e.getY();
		}
		try {
			float pointerCount = (Float) motionEvent_GetY.invoke(e,
					new Object[] { pointerIndex });
			return pointerCount;
		} catch (Exception ex) {
			throw new RuntimeException("Reflected multitouch method failed!",
					ex);
		}
	}

}
