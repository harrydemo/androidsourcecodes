/**
 * name BaseActivity
 * description The super class of Activities in Facade subsystem.
 * version 1.0
 * author sundy
 * date 2010-11-10
 */
package teleca.androidtalk.speechservice;

import java.util.ArrayList;
import java.util.List;

import teleca.androidtalk.speechengine.SpeechCommandResult;
import teleca.androidtalk.speechengine.SpeechEngineTool;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.widget.Toast;

public class BaseActivity extends Activity{
	private static final int VOICE_RECOGNITION_REQUEST_CODE = 6666; 
	protected boolean recognizerPresent = false ;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// Check to see if a recognition activity is present  
        PackageManager pm = getPackageManager();  
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);  
        if (activities.size() != 0)  
        {  
        	this.recognizerPresent = true ;
        }  
        else  
        {  
        	this.recognizerPresent = false ;
        	Toast.makeText(this, "Recognizer not present", 8).show()   ;
        }  
	}
	
	
	protected void getVoiceCommand(SpeechCommandResult spCommand)
	{
		if(spCommand != null)
		{
			Log.i("androidtalk", spCommand.getOpType().toString()+":"+spCommand.getOpData())  ;
		}
	}

	/**
	 * Description: start recognition operation first .
	 */
	protected void startVoiceRecognitionActivity()  
    {  
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);  
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);  
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech recognition");  
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);  
    }  
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK)  
        {  
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);  
            this.getVoiceCommand(SpeechEngineTool.analysisSystemCommand(matches.toString()))  ;
            Toast.makeText(this, matches.toString(), 8).show()   ;
        }  
  
        super.onActivityResult(requestCode, resultCode, data);  
	}

}
