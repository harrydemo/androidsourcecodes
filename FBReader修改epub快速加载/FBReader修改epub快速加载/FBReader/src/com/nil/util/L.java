package com.nil.util;

import java.util.Hashtable;

public class L {
	public static void l(String info) {
		final StackTraceElement[] stack = new Throwable().getStackTrace();
		final int i = 1;
		// for(int id = 0; id < stack.length; id++){
		final StackTraceElement ste = stack[i];
		// android.util.Log.e(SharpParam.LOG_TAG,
		// String.format("[%s][%s]%s[%s]", ste.getFileName(),
		// ste.getMethodName(), ste.getLineNumber(), info));
		android.util.Log.e(
				"-----nil------",
				String.format("[%s][%s]%s[%s]", ste.getFileName(),
						ste.getMethodName(), ste.getLineNumber(), info));
		// }
	}

	private static boolean epubFlag = false;

	public static void setEpubFlag(boolean tag) {
		epubFlag = tag;
	}

	public static boolean getEpubFlag() {
		return epubFlag;
	}

	static Hashtable<String, Long> startTimeHashtable = new Hashtable<String, Long>();

	/**
	 * 打印开始的name
	 * 
	 * @param name
	 */
	public static void startTime(String name) {
		if (!startTimeHashtable.containsKey(name)) {
			startTimeHashtable.put(name, System.currentTimeMillis());
		}
	}

	/**
	 * @param name
	 *            打印的标识符
	 */
	public static void endTime(String name) {
		if (startTimeHashtable.containsKey(name)) {
			float wasteTime = ((float) (System.currentTimeMillis() - startTimeHashtable
					.get(name)) / 1000);
			android.util.Log.e("==(_*|*_)====" + name + "===wasteTime:",
					wasteTime + " (s)");
			startTimeHashtable.remove(name);
		} else {
			android.util.Log.e("==(_*|*_)====" + name + "===error", "========");
		}
	}

}