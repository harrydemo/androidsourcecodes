/*
 * Wongxming May ,2009
 * mwongxming@gmail.com
 */
package cn.wongxming.ringer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;

public class AlarmBroadcastReceiver extends BroadcastReceiver{

	public static final String VIBRATE_CHANGED = "cn.wongxming.ringer.VIBRATE_CHANGED";
	public static final String SILENT_CHANGED = "cn.wongxming.ringer.SILENT_CHANGED";
	public static final String RV_CHANGED = "cn.wongxming.ringer.RV_CHANGED";
	public static final String RING_CHANGED = "cn.wongxming.ringer.RING_CHANGED";
	
	public static final int REQUEST_CODE = 0;
	
	String TAG = "AlarmBroadcastReceiver";
	@Override
	public void onReceive(Context context, Intent intent) {

			AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
	        
			int checkedId = intent.getIntExtra("checkedId",0);

			Log.e(TAG,checkedId + intent.getAction());
			//ÇÐ»»Çé¾°Ä£Ê½
			switch (checkedId) {
	            case R.id.ring_and_vibrate: ringAndVibrate(audio); break;
	            case R.id.vibrate: vibrate(audio); break;
	            case R.id.silent: silent(audio); break;
	            default: ring(audio); break;
			}

	}
	
	//ÁåÉùºÍÕð¶¯
    protected void ringAndVibrate(AudioManager audio) {
    	audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_ON);
        audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION, AudioManager.VIBRATE_SETTING_ON);
    }
  //ÁåÉù
    protected void ring(AudioManager audio) {
    	audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_OFF);
        audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION, AudioManager.VIBRATE_SETTING_OFF);
        
    }
  //Õð¶¯
    protected void vibrate(AudioManager audio) {
    	audio.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_ON);
        audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION, AudioManager.VIBRATE_SETTING_ON);   
        
    }
  //¾²Òô
    protected void silent(AudioManager audio) {
    	audio.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_OFF);
        audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION, AudioManager.VIBRATE_SETTING_OFF);
          
    }

}
