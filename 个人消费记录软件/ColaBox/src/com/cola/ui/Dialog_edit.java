/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cola.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


public class Dialog_edit extends AlertDialog implements OnClickListener {
	private String text = "";
	private EditText edit;
	private OnDateSetListener mCallback;

	private LinearLayout layout;

	public interface OnDateSetListener {

		void onDateSet(String text);
	}

	protected Dialog_edit(Context context, String title, String value,
			OnDateSetListener Callback) {
		super(context);
		mCallback = Callback;
		TextView label = new TextView(context);
		label.setText("hint");
		// setView(label);
		edit = new EditText(context);
		edit.setText(value);
		layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		// LinearLayout.LayoutParams param =
		// new LinearLayout.LayoutParams(100, 40);
		// layout.addView(label, param);
		LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(200,
				50);
		layout.addView(edit, param2);
		setView(layout);
		setTitle(title);
		setButton("确定", this);
		setButton2("取消", (OnClickListener) null);

	}

	public void onClick(DialogInterface dialog, int which) {
		// Log.v("cola","U click which="+which);
		text = edit.getText().toString();
		Log.v("cola", "U click text=" + text);
		if (mCallback != null)
			mCallback.onDateSet(text);

	}

}
