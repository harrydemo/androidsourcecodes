package teleca.androidtalk.facadetest;

import junit.framework.TestResult;
import teleca.androidtalk.facade.MainActivity;
import teleca.androidtalk.speechengine.SpeechCommandResult;
import teleca.androidtalk.speechservice.ISpeechServiceProxy;
import teleca.androidtalk.speechservice.ServiceAction;
import teleca.androidtalk.speechservice.SpeechServiceProxy;
import android.test.AndroidTestCase;
import android.test.mock.MockContext;

public class MainActivityTest extends AndroidTestCase 
{
	ISpeechServiceProxy issp  ;
	public void textToSpeechValidTest()
	{
		MainActivity ma = new MainActivity();
		issp = new SpeechServiceProxy() ;
		MockContext context = new MockContext() ;
		issp.textToSpeech(ServiceAction.TTS_ACTION, context, "hello everyone") ;
		ma.getVoiceCommand(new SpeechCommandResult());
	}
	@Override
	public TestResult run() 
	{
		// TODO Auto-generated method stub		
		textToSpeechValidTest();
		return super.run();
	}
}
