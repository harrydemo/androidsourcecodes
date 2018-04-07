package teleca.androidtalk.facadetest;

import junit.framework.TestResult;
import teleca.androidtalk.facade.CommandListActivity;
import teleca.androidtalk.speechservice.ISpeechServiceProxy;
import teleca.androidtalk.speechservice.ServiceAction;
import teleca.androidtalk.speechservice.SpeechServiceProxy;
import android.test.AndroidTestCase;
import android.test.mock.MockContext;

public class CommandListActivityTest extends AndroidTestCase
{
	ISpeechServiceProxy issp  ;
	CommandListActivity cla;
	public void textToSpeechValidTest()
	{
		issp = new SpeechServiceProxy() ;
		MockContext context = new MockContext() ;
		issp.textToSpeech(ServiceAction.TTS_ACTION, context, "hello everyone") ;
		cla.getArrayList();
	}
	@Override
	public TestResult run() 
	{
		// TODO Auto-generated method stub		
		textToSpeechValidTest();
		return super.run();
	}
}
