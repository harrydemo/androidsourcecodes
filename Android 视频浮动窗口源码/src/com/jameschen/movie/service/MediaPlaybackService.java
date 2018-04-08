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

package com.jameschen.movie.service;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.view.View;
import android.view.ViewGroup;

import com.jameschen.movie.MyFloatView;
import com.jameschen.movie.R;

/**
 * Provides "background" audio playback capabilities, allowing the user to
 * switch between activities without stopping playback.
 */
public class MediaPlaybackService extends Service {



	@Override
	public void onCreate() {
		super.onCreate();

	}




	public ViewGroup fView;
	MyFloatView sFloatView;

	private void createView(Context context) {
		if (fView != null) {
			return;
		}

		fView = (ViewGroup) View.inflate(context, R.layout.main, null);
		// 显示myFloatView图像
		sFloatView = new MyFloatView(fView);
		sFloatView.bindViewListener();
		sFloatView.showLayoutView();

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null) {
			String action = intent.getAction();

			String cmd = intent.getStringExtra("command");

			if ("createUI".equals(action)) {
				createView(this);
			} else if ("removeUI".equals(action)) {

				fView = null;
				sFloatView = null;
			}
		}

		return START_STICKY;
	}

	boolean iPlayState = false;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}





}
