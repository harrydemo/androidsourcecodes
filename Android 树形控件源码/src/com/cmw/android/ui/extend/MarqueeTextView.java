package com.cmw.android.ui.extend;

import android.content.Context;
import android.widget.TextView;
/**
 * 自定滚动 TextView 类
 * @author chengmingwei
 *
 */
public class MarqueeTextView extends TextView {

	public MarqueeTextView(Context context) {
		super(context);
	}

	@Override
	public boolean isFocused() {
		return true;
	}
}
