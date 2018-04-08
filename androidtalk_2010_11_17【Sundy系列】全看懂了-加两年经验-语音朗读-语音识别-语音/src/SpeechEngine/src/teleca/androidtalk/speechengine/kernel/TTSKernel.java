/**
 * @description Kernel support with text to speech .
 * @version 1.0
 * @author sundy
 * @date 2010-11-5
 */
package teleca.androidtalk.speechengine.kernel;
import java.util.Locale;
import android.content.Context;  
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
/**
 * @description Kernel support with text to speech .
 * @version 1.0
 * @author sundy
 * @date 2010-11-5
 */
public class TTSKernel {

	private Context curContext   ;
	private String command  ;
	private TextToSpeech txtSpeech  ;
	public Context getCurContext() {
		return curContext;
	}
	public void setCurContext(Context curContext) {
		this.curContext = curContext;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public TTSKernel()
	{
		
	}
	public TTSKernel(Context curContext,String command)
	{
		this.setCurContext(curContext) ;
		this.setCommand(command) ;
		this.speechToText() ;
	}
	
	private void speechToText()  
	{
		if(command == null)
			return ;
			
		if(curContext != null)
		{
			try
			{
				txtSpeech = new TextToSpeech(curContext,new OnInitListener(){
					@Override
					public void onInit(int arg0) {
						// TODO Auto-generated method stub
						if(arg0 == TextToSpeech.SUCCESS)
						{
							int result = txtSpeech.setLanguage(Locale.ENGLISH)  ;
							if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
							{
								Log.i("androidtalk", "language not support")  ;
							}else
							{
								txtSpeech.speak(command, TextToSpeech.QUEUE_FLUSH, null) ;
							}
						}
					}
						
					}
					
					) ;
			}catch(Exception ex)
			{
				Log.e("androidtalk", ex.getMessage())  ;
			}
			
		}
	}
}
