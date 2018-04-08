/**
 * @description Speech engine service kernel class .
 * @version 1.0
 * @author sundy
 * @date 2010-11-5
 */
package teleca.androidtalk.speechservice;

import teleca.androidtalk.speechengine.ISpeechEngine;
import teleca.androidtalk.speechengine.SpeechSystemEngine;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
/**
 * @description Speech engine service kernel class .
 * @version 1.0
 * @author sundy
 * @date 2010-11-5
 */
public class SpeechService extends Service{

	/**
	 * Description Clear the static variables in ServiceConst class .
	 */
	private void clearServiceConst()
	{
		ServiceConst.TTS_COMMAND_DATA = null ;
		ServiceConst.TTS_CONTEXT = null ;
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		String actionName  ;
		if(intent != null)
		{
			ISpeechEngine se = new SpeechSystemEngine()  ;
			actionName = intent.getAction() ;
			//if do tts operation
			if(actionName.equals(ServiceAction.TTS_ACTION))
			{
				//get current context 
				if(ServiceConst.TTS_CONTEXT != null)
				{
					se.textToSpeech(ServiceConst.TTS_CONTEXT,ServiceConst.TTS_COMMAND_DATA) ;
					this.clearServiceConst() ;
				}
				
			}else if(actionName == ServiceAction.SR_ACTION)
			{
				
			}else if(actionName==ServiceAction.STT_ACTION)
			{
				
			}
		}


}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}