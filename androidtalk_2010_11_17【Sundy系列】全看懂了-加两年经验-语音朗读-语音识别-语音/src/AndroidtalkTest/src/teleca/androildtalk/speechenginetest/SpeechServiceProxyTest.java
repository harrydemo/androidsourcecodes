/**
 * description The test of speech service proxy .
 * version 1.0
 * author sundy
 * date 2010-11-12
 */
package teleca.androildtalk.speechenginetest;


import teleca.androidtalk.speechservice.ISpeechServiceProxy;
import teleca.androidtalk.speechservice.ServiceAction;
import teleca.androidtalk.speechservice.SpeechServiceProxy;
import android.test.AndroidTestCase;
import android.test.mock.* ;

public class SpeechServiceProxyTest extends AndroidTestCase {

	ISpeechServiceProxy issp  ;
	public void textToSpeechValidTest()
	{
		issp = new SpeechServiceProxy() ;
		MockContext context = new MockContext() ;
		issp.textToSpeech(ServiceAction.TTS_ACTION, context.getApplicationContext(), "hello everyone") ;
		assertNotNull(issp) ;
		
	}
	
	@Override
	public void testAndroidTestCaseSetupProperly() {
		// TODO Auto-generated method stub
		super.testAndroidTestCaseSetupProperly();
		issp = new SpeechServiceProxy() ;
		MockContext context = new MockContext() ;
	}

}
