package com.cmw.android.ui.extend;

import android.content.Context;
import android.widget.TextView;
/**
 * �Զ����� TextView ��
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
