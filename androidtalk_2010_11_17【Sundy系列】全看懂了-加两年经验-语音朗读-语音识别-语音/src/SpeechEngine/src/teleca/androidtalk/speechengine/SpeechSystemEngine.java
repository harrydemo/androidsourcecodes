/**
 * @description SpeechEngine key class .
 * @version 1.0
 * @author sundy
 * @date 2010-11-5
 */

package teleca.androidtalk.speechengine;

import teleca.androidtalk.speechengine.kernel.TTSKernel;

import android.content.Context;
import android.util.Log;

public class SpeechSystemEngine implements ISpeechEngine,
		ISystemCommandsAnalysis {
	
	@Override
	public SpeechCommandResult speechRecognition(Context curContext) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SpeechCommandResult speechToText(Context curContext) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void textToSpeech(Context curContext, String textContent) {
		// TODO Auto-generated method stub
		if(textContent == null || curContext == null)
			return ;
		try
		{
			@SuppressWarnings("unused")
			TTSKernel ttsKernel = new TTSKernel(curContext,textContent) ;
		}catch(Exception ex)
		{
			Log.e("androidtalk", ex.getMessage())  ;
		}
			
	}

	@Override
	public SpeechCommandResult analysisSystemCommand(String speakCommand) {
		// TODO Auto-generated method stub
		return SpeechEngineTool.analysisSystemCommand(speakCommand) ;
	}

}
