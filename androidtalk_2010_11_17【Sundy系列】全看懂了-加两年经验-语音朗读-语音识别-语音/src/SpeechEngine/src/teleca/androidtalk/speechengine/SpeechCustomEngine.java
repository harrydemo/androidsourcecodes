/**
 * @description not available in version 0.1
 * @version 1.0
 * @author sundy
 * @date 2010-11-5
 */
package teleca.androidtalk.speechengine;

import android.content.Context;

/**
 * @author Administrator
 *
 */
public class SpeechCustomEngine implements ISpeechEngine,
		ISystemCommandsAnalysis {

	
	/* (non-Javadoc)
	 * @see teleca.androidtalk.speechengine.ISpeechEngine#speechRecognition(android.content.Context)
	 */
	@Override
	public SpeechCommandResult speechRecognition(Context curContext) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see teleca.androidtalk.speechengine.ISpeechEngine#speechToText(android.content.Context)
	 */
	@Override
	public SpeechCommandResult speechToText(Context curContext) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see teleca.androidtalk.speechengine.ISpeechEngine#textToSpeech(android.content.Context, java.lang.String)
	 */
	@Override
	public void textToSpeech(Context curContext, String textContent) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see teleca.androidtalk.speechengine.ISystemCommandsAnalysis#analysisSystemCommand(java.lang.String)
	 */
	@Override
	public SpeechCommandResult analysisSystemCommand(String speakCommand) {
		// TODO Auto-generated method stub
		return null;
	}

}
