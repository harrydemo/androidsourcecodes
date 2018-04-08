package teleca.androidtalk.facadetest;

import junit.framework.TestResult;
import teleca.androidtalk.facade.PopActivity;
import teleca.androidtalk.speechservice.ISpeechServiceProxy;
import teleca.androidtalk.speechservice.ServiceAction;
import teleca.androidtalk.speechservice.SpeechServiceProxy;
import android.test.AndroidTestCase;
import android.test.mock.MockContext;

public class PopActivityTest extends AndroidTestCase
{
	ISpeechServiceProxy issp  ;
	public void textToSpeechValidTest()
	{
		issp = new SpeechServiceProxy() ;
		MockContext context = new MockContext() ;
		issp.textToSpeech(ServiceAction.TTS_ACTION, context, "hello everyone") ;
		PopActivity.getInstalledApps(new PopActivity());
	}
	@Override
	public TestResult run() 
	{
		// TODO Auto-generated method stub		
		textToSpeechValidTest();
		return super.run();
	}

}
