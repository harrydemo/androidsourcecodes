/**
 * @description speechengine interface definition .
 * @version 1.0
 * @author sundy
 * @date 2010-11-5
 */
package teleca.androidtalk.speechengine;

import android.content.Context;

/**
 * @description speechengine interface definition .
 * @version 1.0
 * @author sundy
 * @date 2010-11-5
 */
public interface ISpeechEngine {
	public void textToSpeech(Context curContext,String textContent)  ;
	public SpeechCommandResult speechRecognition(Context curContext)  ;
	public SpeechCommandResult speechToText(Context curContext)  ;
	
}
