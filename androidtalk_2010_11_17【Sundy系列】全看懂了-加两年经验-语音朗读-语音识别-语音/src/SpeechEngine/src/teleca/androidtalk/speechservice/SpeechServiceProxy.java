package teleca.androidtalk.speechservice;

import android.content.Context;
import android.content.Intent;

public class SpeechServiceProxy implements ISpeechServiceProxy{

	/**
	 * Description: text to speech method 
	 * @author sundy
	 * @param String serviceAction define the action variable of Service 
	 * @param Context curContext activation current context .
	 */
	@Override
	public void textToSpeech(String serviceAction, Context curContext,
			String textContent) {
		Intent _intent = new Intent(curContext, SpeechService.class)  ;
		_intent.setAction(serviceAction) ;
		ServiceConst.TTS_CONTEXT = curContext  ;
		ServiceConst.TTS_COMMAND_DATA = textContent ;
		curContext.startService(_intent) ;		
		
	}

	/**
	 * Description:speech recognition method  .
	 * @author sundy
	 * @param BaseActivity srActivity define current activity . 
	 */
	@Override
	public void speechRecognition(BaseActivity srActivity) {
		// TODO Auto-generated method stub
        srActivity.startVoiceRecognitionActivity()  ;
	}

}
