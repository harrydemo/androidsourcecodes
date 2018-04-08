/**
 * @description the interface published to invoke service . use this class , you can invoke SpeechEngine easily
 * @version 1.0
 * @author sundy
 * @date 2010-11-5
 */
package teleca.androidtalk.speechservice;

import android.content.Context;

public interface ISpeechServiceProxy {

	/**
	 * text to speech service .
	 * @param serviceAction Service action is type .
	 * @param curContext translate the context instance  .
	 * @param textContent translate the context text 
	 */
	public void textToSpeech(String serviceAction, Context curContext,String textContent)  ;
	/**
	 * speech recognition function 
	 * @param act current activity instance based on BaseActivity
	 */
	public void speechRecognition(BaseActivity act) ;
}
