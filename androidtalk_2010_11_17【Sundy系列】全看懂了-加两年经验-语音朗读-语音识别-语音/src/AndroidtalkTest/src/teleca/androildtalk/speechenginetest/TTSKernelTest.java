package teleca.androildtalk.speechenginetest;

import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.test.AndroidTestCase;
import android.test.mock.MockContext;
import teleca.androidtalk.speechengine.kernel.TTSKernel;
import teleca.androidtalk.speechservice.BaseActivity;

public class TTSKernelTest extends AndroidTestCase{

	public void testConstructr()
	{
		BaseActivity mc = new BaseActivity() ;
		TTSKernel ttsKernel = new TTSKernel(mc.getBaseContext(),"Hello Sundy") ;
		this.assertNotNull(ttsKernel.getCurContext()) ;
		//this.assertEquals("Hello Sundy", ttsKernel.getCommand())  ;
	}
	
	public void testConstructr2()
	{
		BaseActivity mc = new BaseActivity() ;
		TTSKernel ttsKernel = new TTSKernel(mc.getBaseContext(),"Hello Sundy") ;
		this.assertEquals("Hello Sundy",ttsKernel.getCommand()) ;
	}
	
	public void testTextToSpeechEnvironment()
	{
		MockContext mc = new MockContext() ;
		//TTSKernel ttsKernel = new TTSKernel(mc,"Hello Sundy") ;
		
		TextToSpeech txtSpeech = new TextToSpeech(mc.getApplicationContext(),new OnInitListener(){
			@Override
			public void onInit(int arg0) {
				// TODO Auto-generated method stub
				assertEquals(arg0,TextToSpeech.SUCCESS)  ;
			}
		}) ;
	}

}
