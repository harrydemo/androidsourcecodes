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

import android.content.Context;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.KeyEvent;
import android.os.Handler;
import android.os.Message;





public class MediaButtonIntentReceiver extends BroadcastReceiver {

	/***********************************
	 * 
	 * MISSING KEY CODES IN THIS SDK
	 * 
	 ***********************************/
	public final int KEYCODE_PLAYPAUSE       = 85;
	public final int KEYCODE_STOP            = 86;
	public final int KEYCODE_NEXTSONG        = 87;
	public final int KEYCODE_PREVIOUSSONG    = 88;
	public final int KEYCODE_REWIND          = 89;
	public final int KEYCODE_FORWARD         = 90;
	

    private static final String SERVICECMD = "org.abrantes.filex.playerservicecmd";
    private static final String CMDNAME = "command";
    private static final String CMDTOGGLEPAUSE = "togglepause";
    public final String CMDPAUSE = "pause";
    public final String CMDPREVIOUS = "previous";
    public final String CMDNEXT = "next";
    
    	private static final int MSG_LONGPRESS_TIMEOUT = 1;
    	private static final int LONG_PRESS_DELAY = 1500;
    	private static final int MSG_SINGLEPRESS_TIMEOUT = 2;
    	private static final int SINGLE_PRESS_DELAY = 1000;
    	private static final int MSG_DOUBLEPRESS_TIMEOUT = 3;
    	private static final int DOUBLE_PRESS_DELAY = 1000;

        private static long mLastClickTime = 0;
        private static boolean mDown = false;
        private static boolean mFirst = false;
        private static boolean mLaunched = false;

        private static Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_LONGPRESS_TIMEOUT:
                    	Log.i("HDL", "trying to launch rock on");
                        if (!mLaunched && mDown) {
                        	//if(true) return;
                        	Log.i("HDL", "launching rockon");
                                Context context = (Context)msg.obj;
                            Intent i = new Intent();
                            i.putExtra("autoshuffle", "true");
                            i.setClass(context, RockPlayer.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(i);
                            mLaunched = true;
                        }
                        break;
                    case MSG_SINGLEPRESS_TIMEOUT:
                    	Log.i("HDL", "trying to toggle pause");
                        if (mLaunched && !mDown) {
                        	Log.i("HDL", "toggling pause");                            
                            Context context = (Context)msg.obj;
                        	Intent i = new Intent(context, PlayerService.class);
                            i.setAction(SERVICECMD);
                            i.putExtra(CMDNAME, CMDTOGGLEPAUSE);
                            //context.bindService(i, null, 0);	
                            context.startService(i);
                            mHandler.removeMessages(MSG_LONGPRESS_TIMEOUT);
                            //context.sendBroadcast(i);
                        }
                        break;
                }
            }
        };
        
        @Override
        public void onReceive(Context context, Intent intent) {
            KeyEvent event = (KeyEvent)
                    intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            
            Log.i("MDBUTTON", "Received button");
            
            if (event == null) {
            	abortBroadcast();
                return;
            }

            Log.i("MDBUTTON", "Event not null");
            
            
            int keycode = event.getKeyCode();
            int action = event.getAction();
            long eventtime = event.getEventTime();
            
            /*
             * 'First' press
             */
            if(event.getEventTime() - mLastClickTime > SINGLE_PRESS_DELAY){
            	mFirst = false;
            	mLaunched = false;
            }
            
            mLastClickTime = event.getEventTime();

            // single quick press: pause/resume. 
            // double press: next track
            // long press: start auto-shuffle mode.
            
            Log.i("MEDIABTN", "received event "+keycode);
            String command = null;
            switch (keycode) {
                case KeyEvent.KEYCODE_HEADSETHOOK:
                	command = CMDNAME; // bogus - does nothing
                	break;
//                case KEYCODE_PLAYPAUSE:
//                    command = CMDTOGGLEPAUSE;
//                    break;
//                case KEYCODE_NEXTSONG:
//                    command = CMDNEXT;
//                    break;
//                case KEYCODE_PREVIOUSSONG:
//                    command = CMDPREVIOUS;
//                    break;
                default:
                	command = CMDNAME;
                	break;
            }

            if (command != null) {
                if (action == KeyEvent.ACTION_DOWN) {
                	Log.i("HDL", "BTN DOWN");
                    
                	if(mDown){
                		abortBroadcast();
                		return;
                	}
                	mDown = true;
                    if (!mFirst) {
                    	Log.i("HDL", "First button press");
                        
                    	mFirst = true;
                    	
                    	Log.i("HDL", "schedule toggle play");
                        
                    	/*
                    	 * Schedule app play/pause
                    	 */
                    	mHandler.sendMessageDelayed(
                                mHandler.obtainMessage(MSG_SINGLEPRESS_TIMEOUT, context),
                                SINGLE_PRESS_DELAY);
                    	
                    	Log.i("HDL", "schedule app start");
                        
                    	/*
                    	 * Schedule app start-up
                    	 */
                    	mHandler.sendMessageDelayed(
                                mHandler.obtainMessage(MSG_LONGPRESS_TIMEOUT, context),
                                LONG_PRESS_DELAY);
                    	
                    } else {
                    	Log.i("HDL", "second button press");
                    	
                    	/* Second Click */
                    	
                       	mHandler.removeMessages(MSG_SINGLEPRESS_TIMEOUT);   
                       	mHandler.removeMessages(MSG_LONGPRESS_TIMEOUT);   
                                           	
                    	command = CMDNEXT;
                    	
                    	Log.i("HDL", "sending intent for next song");
                        
                    	Intent i = new Intent(context, PlayerService.class);
                        i.setAction(SERVICECMD);
                        i.putExtra(CMDNAME, CMDNEXT);
                        context.startService(i);
                        //context.sendBroadcast(i);
                        //context.startService(i);
                        //mLastClickTime = 0;
                        
                     
                    }
//                        
//                    	// only if this isn't a repeat event
//                        
//                        if (PlayerService.CMDTOGGLEPAUSE.equals(command)) {
//                            // We're not using the original time of the event as the
//                            // base here, because in some cases it can take more than
//                            // one second for us to receive the event, in which case
//                            // we would go immediately to auto shuffle mode, even if
//                            // the user didn't long press.
//                            mHandler.sendMessageDelayed(
//                                    mHandler.obtainMessage(MSG_LONGPRESS_TIMEOUT, context),
//                                    LONG_PRESS_DELAY);
//                        }
//                        
//                        SharedPreferences pref = context.getSharedPreferences("Music", 
//                                Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
//                        String q = pref.getString("queue", "");
//                        // The service may or may not be running, but we need to send it
//                        // a command.
//                        Intent i = new Intent(context, PlayerService.class);
//                        i.setAction(PlayerService.SERVICECMD);
//                        if (keycode == KeyEvent.KEYCODE_HEADSETHOOK && eventtime - mLastClickTime < 300) {
//                            i.putExtra(PlayerService.CMDNAME, PlayerService.CMDNEXT);
//                            context.startService(i);
//                            mLastClickTime = 0;
//                        } else {
//                            i.putExtra(PlayerService.CMDNAME, command);
//                            context.startService(i);
//                            mLastClickTime = eventtime;
//                        }
//
//                        mLaunched = false;
//                        mDown = true;
//                    }
                } else  if(action == KeyEvent.ACTION_UP){
                	Log.i("HDL", "BTN UP");
                    
                	mDown = false;
                	mHandler.removeMessages(MSG_LONGPRESS_TIMEOUT);
                }
                abortBroadcast();
            }
        }
}