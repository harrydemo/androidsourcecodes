package com.qsa;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class QSA extends Activity {
	private Animation myAnimation_Alpha;
	protected static final int GOMENUS = 0x101;
	public static boolean isRun = true;
	NewView newView = null;
	Intent i = null;
	Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			newView.getReturn = -1;
			switch (msg.what) {

			case 0x101:
				
				break;
				
			case 0x102:

				break;
				
			case 0x103:

				i = new Intent(getApplicationContext(), ShowPic.class);
				startActivity(i);
				
				break;
				
			case 0x104:

				break;
				
			case 0x105:

				break;
				
			case 0x106:

				break;
				
			case 0x107:

				i = new Intent(getApplicationContext(), RunLed.class);
				startActivity(i);

				break;

			case 0x108:

				break;
				
			case 0x109:
				

				break;
			}

			super.handleMessage(msg);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		newView = new NewView(getApplicationContext(), 300, 200, 150);

		setContentView(newView);

		new Thread(new myThread()).start();

	}

	class myThread implements Runnable {
		public void run() {
			while (!Thread.currentThread().isInterrupted()) {

				Message message = new Message();

				switch (newView.getReturn()) {
				case 1:
					message.what = 0x101;
					break;
				case 2:
					message.what = 0x102;
					break;
				case 3:
					message.what = 0x103;
					break;
				case 4:
					message.what = 0x104;
					break;
				case 5:
					message.what = 0x105;
					break;
				case 6:
					message.what = 0x106;
					break;
				case 7:
					message.what = 0x107;
					break;
				case 8:
					message.what = 0x108;
					break;
				case 9:
					message.what = 0x109;
					break;

				}
				QSA.this.myHandler.sendMessage(message);
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}
	}

}
