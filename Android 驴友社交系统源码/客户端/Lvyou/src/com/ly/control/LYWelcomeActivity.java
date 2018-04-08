/**
 *  Copyright 2011 ChinaSoft International Ltd. All rights reserved.
 */

package com.ly.control;

import com.amo.demo.wheelview.LogoView;

import android.app.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

/**
 * <p>
 * Title: LogoActivity
 * </p>
 * <p>
 * Description: 应用程序启动时显示的LOGO界面
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company: ChinaSoft International Ltd.
 * </p>
 * 
 * @author etc
 * @version 1.0
 */

public class LYWelcomeActivity extends Activity {
	/**
	 * 用于显示Logo的自定义视图
	 * */
	LogoView lv;

	@Override
	/**
	 * 创建Activity时自动回调
	 * */
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//System.out.println("Logo onCreate");
		super.onCreate(savedInstanceState);

		// 实例化自定义视图
		lv = new LogoView(this);
		// 将自定义视图显示在本界面
		setContentView(lv);
		// 创建修改Logo透明度的异步任务
		LogoTask task = new LogoTask();
		// 执行异步任务
		task.execute();
	}

	/**
	 * 用于修改Logo界面图片透明度的异步任务
	 * */
	private class LogoTask extends AsyncTask<Object, Integer, String> {

		// 当前的透明度取值
		int alpha = 0;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			//System.out.println("a"+System.currentTimeMillis());
		}
		/**
		 * 异步任务执行成功后，自动跳转至应用程序主界面
		 * */
		@Override
		protected void onPostExecute(String result) {
			 //System.out.println("d"+System.currentTimeMillis());
			// TODO Auto-generated method stub
			Intent intent=new Intent(LYWelcomeActivity .this, LYTabHostActivity.class);
			startActivity(intent);
		}
		/**
		 * 修改Logo自定义视图图片透明度，并重绘
		 * */
		@Override
		protected void onProgressUpdate(Integer... values) {
			 //System.out.println("c"+System.currentTimeMillis());
			// TODO Auto-generated method stub
			int temp = values[0].intValue();
			lv.repaint(temp);
		}

		/**
		 * 异步任务，循环改变透明度取值
		 * */
		@Override
		protected String doInBackground(Object... arg0) {
			// TODO Auto-generated method stub
          // System.out.println("b"+System.currentTimeMillis());
			while (alpha < 255) {

				try {
					Thread.sleep(100);
					publishProgress(new Integer(alpha));
					alpha += 5;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			return null;
		}

	}

}
