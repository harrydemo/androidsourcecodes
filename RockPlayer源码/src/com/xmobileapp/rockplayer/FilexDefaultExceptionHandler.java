/*
 * [程序名称] Android 音乐播放器
 * [参考资料] http://code.google.com/p/rockon-android/ 
 * [开源协议] Apache License, Version 2.0 (http://www.apache.org/licenses/LICENSE-2.0)
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

package com.xmobileapp.rockplayer;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.util.Log;

	class FilexDefaultExceptionHandler implements Thread.UncaughtExceptionHandler {

		private UncaughtExceptionHandler oldDefaultExceptionHandler;
		private final String TAG = "FilexDefaultExceptionHandler";
		private Context context;
	
		FilexDefaultExceptionHandler(Context context) {
			Log.d(TAG, "Default Exception Handler=" + Thread.getDefaultUncaughtExceptionHandler());
			oldDefaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
			
			Thread.setDefaultUncaughtExceptionHandler(this);
			
			this.context = context; 
		}

		public void destroy(){
			this.context = null;
			Thread.setDefaultUncaughtExceptionHandler(null);
		}
		
		public void uncaughtException(Thread t, Throwable e) {
			Log.e("RockOn", "Caught the following exception: ", e);

			StringBuilder message = new StringBuilder(
				"Sorry!\n\nRockOn "+
				"v1.3.2 "+
				"crashed! Please send this mail, so that we " +
				"can analyze/fix the issue.\n");
			message.append(String.format("-- Android Version: sdk=%s, release=%s, inc=%s\n",
				Build.VERSION.SDK, Build.VERSION.RELEASE, Build.VERSION.INCREMENTAL));
			
			Runtime rt = Runtime.getRuntime();
			message.append(String.format("-- Memory free: %4.2fMB total: %4.2fMB max: %4.2fMB",
					rt.freeMemory() / 1024 / 1024.0, 
					rt.totalMemory() / 1024 / 1024.0,
					rt.maxMemory() / 1024 / 1024.0));
			message.append(String.format("-- Thread State: %s\n", t.getState()));
			
//			EntryManager entryManager = null;
//
//	try {
//
//	entryManager = EntryManager.getInstance(NewsRob.this);
//
//	} catch (Throwable throwable) {
//
//	// ignored
//
//	Log
//
//	.e(
//
//	TAG,
//
//	"Oh, Throwable during creation of EntryManager when trying to create a bug report ;-(.",
//
//	throwable);
//
//	}
//
//	if (entryManager != null) {
//
//	message.append(String.format("-- NewsRob Version: %s/%s\n", entryManager.getMyVersionName(),
//
//	entryManager.getMyVersionCode()));
//
//
//	Job j = entryManager.getCurrentRunningJob();
//
//	if (j != null)
//
//	message.append(String.format("-- Job: %s\n", j.getJobDescription()));
//
//
//	if (entryManager.runningThread != null)
//
//	message.append(String.format("-- Running Thread: %s\n", entryManager.runningThread));
//
//
//	}

			// Add stacktrace

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			
			e.printStackTrace(pw);
			pw.close();
			
			message.append("-- Stacktrace:\n");
			message.append(sw.getBuffer());
			
			String messageBody = message.toString();
			
			// ignore certain exceptions
//			if (Pattern.compile("CacheManager.java:391").matcher(messageBody).find())
//				return;

			// Prepare Mail
	
			final Intent sendIntent = new Intent(Intent.ACTION_SEND);
			sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			sendIntent.setType("message/rfc822");
			sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "filipe.abrantes@gmail.com" });
			sendIntent.putExtra(Intent.EXTRA_SUBJECT, "BugReport: " + e.getClass().getSimpleName() + ": "
					+ e.getMessage());
			sendIntent.putExtra(Intent.EXTRA_TEXT, messageBody);
			Log.e(TAG, "Exception handled. Email activity should be initiated now.");
	
			// Send Mail
	
			new Thread(new Runnable() {
				public void run() {
					//NewsRob.this.startActivity(sendIntent);
					context.startActivity(sendIntent);
					//sendBroadcast(sendIntent);
				}
			}).start();
	
			Log.e(TAG, "Exception handled. Email should be sent by now.");
	
			// Use default exception mechanism
	
			if (oldDefaultExceptionHandler != null)
				oldDefaultExceptionHandler.uncaughtException(t, e);
		}

	}