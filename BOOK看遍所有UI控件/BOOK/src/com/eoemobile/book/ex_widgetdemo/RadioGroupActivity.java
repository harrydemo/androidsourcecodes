/* 
 * Copyright (C) 2007 Google Inc.
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

package com.eoemobile.book.ex_widgetdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

public class RadioGroupActivity extends Activity implements View.OnClickListener
{
	private RadioGroup mRadioGroup;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.radio_group);
		setTitle("RadioGroupActivity");
		mRadioGroup = (RadioGroup) findViewById(R.id.menu);
		Button clearButton = (Button) findViewById(R.id.clear);
		clearButton.setOnClickListener(this);
	}

	public void onClick(View v)
	{
		mRadioGroup.clearCheck();
	}
}
