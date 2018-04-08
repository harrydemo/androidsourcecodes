package teleca.androidtalk.facadetest;

import junit.framework.TestResult;
import teleca.androidtalk.facade.CommandItemAdapter;
import teleca.androidtalk.speechservice.ISpeechServiceProxy;
import teleca.androidtalk.speechservice.ServiceAction;
import teleca.androidtalk.speechservice.SpeechServiceProxy;
import android.test.AndroidTestCase;
import android.test.mock.MockContext;

public class CommandItemAdapterTest extends AndroidTestCase
{
	ISpeechServiceProxy issp  ;
	CommandItemAdapter cia = new CommandItemAdapter(mContext, 0, null);
	public void textToSpeechValidTest()
	{
		issp = new SpeechServiceProxy() ;
		MockContext context = new MockContext() ;
		issp.textToSpeech(ServiceAction.TTS_ACTION, context, "hello everyone") ;
		cia.getView(0, null, null);
	}
	@Override
	public TestResult run() 
	{
		// TODO Auto-generated method stub		
		textToSpeechValidTest();
		return super.run();
	}

}
