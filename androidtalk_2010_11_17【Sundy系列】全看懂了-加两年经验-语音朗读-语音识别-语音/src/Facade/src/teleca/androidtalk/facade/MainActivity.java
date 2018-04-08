/**
 * @description : MainActivity of program .
 * @version 1.0
 * @author Alex
 * @date 2010-11-10
 */

package teleca.androidtalk.facade;


import teleca.androidtalk.facade.util.Helper;
import teleca.androidtalk.operation.IOperation;
import teleca.androidtalk.operation.OperationFactory;
import teleca.androidtalk.operation.OperationType;
import teleca.androidtalk.speechengine.SpeechCommandResult;
import teleca.androidtalk.speechservice.BaseActivity;
import teleca.androidtalk.speechservice.ISpeechServiceProxy;
import teleca.androidtalk.speechservice.SpeechServiceProxy;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends BaseActivity implements OnClickListener
{
	private Button btn_rec,btn_Ban;
	private Button btn1,btn2,btn3;
	 	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	//refer to the drawable resources
    	Drawable dw1 = getResources().getDrawable(R.drawable.btn_ground);
    	Drawable dw2 = this.getResources().getDrawable(R.drawable.back);
    	Drawable dw3 = this.getResources().getDrawable(R.drawable.txt);
    	Drawable dw4 = this.getResources().getDrawable(R.drawable.command);
    	Drawable dw5 = this.getResources().getDrawable(R.drawable.help);
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
       
        //ImpressionAdView.show(this, btn_Ban, 30,235 , Color.WHITE, true,30);
        Log.v("TAG", "@@@@@@@@@@");
        btn1 = (Button)findViewById(R.id.ibtn_read);
        btn1.setBackgroundDrawable(dw3);       
        btn1.setOnClickListener(this);  
        
        btn2 = (Button)findViewById(R.id.ibtn_command);
        btn2.setBackgroundDrawable(dw4);
        btn2.setOnClickListener(this);
        
        btn3 = (Button)findViewById(R.id.ibtn_help);
        btn3.setBackgroundDrawable(dw5);
        btn3.setOnClickListener(this);
        
        btn_rec = (Button)findViewById(R.id.btn_speak);        
        btn_rec.setBackgroundDrawable(dw1);
        btn_rec.setOnClickListener(this);
        
        btn_Ban = (Button)findViewById(R.id.btn_Ban);
        btn_Ban.setBackgroundDrawable(dw2);
    }
	@Override
	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
		if(v == btn_rec)
		{
			//call the speech engine to achieve speech recognition
			ISpeechServiceProxy iss = new SpeechServiceProxy() ;		
			iss.speechRecognition(MainActivity.this) ;
		}
		if(v == btn1)
		{
			Intent r_intent = new Intent();
			r_intent.setClass(MainActivity.this, ReadTextActivity.class);
			startActivity(r_intent);
		}
		if(v == btn2)
		{
			Intent c_intent = new Intent();
			c_intent.setClass(MainActivity.this, CommandListActivity.class);
			startActivity(c_intent);
		}
		else if(v == btn3)
		{
			Intent h_intent = new Intent();
			h_intent.setClass(MainActivity.this, HelpActivity.class);
			startActivity(h_intent);
		}
	}
	/**
	 *  get the voiceCommand
	 *  
	 * @param SpeechCommandResult
	 * @return 
	 */

	public void getVoiceCommand(SpeechCommandResult spCommand) 
	{
		if (spCommand != null) 
		{
			Log.v("TAG", spCommand.getOpType().toString() + ":"	+ spCommand.getOpData());
			OperationFactory oFactory = new OperationFactory();
			OperationType type = null;
			if(spCommand.getOpType().equals(teleca.androidtalk.speechengine.OperationType.Unknow))
			{
				spCommand = Helper.getCommand(spCommand.getOpData(), this);
			}
			switch (spCommand.getOpType()) 
			{
			case Dialing:
				type = OperationType.Dialing;
				break;
			case SearchGoogle:
				type = OperationType.SearchGoogle;
				break;
			case StartApp:
				type = OperationType.StartApp;
				break;
			case GotoWeb:
				type = OperationType.GotoWeb;
				break;
			default:
				break;
			}
			if (type != null) 
			{
				IOperation iOperation = oFactory.createOperation(type);
				iOperation.doOperation(MainActivity.this, spCommand.getOpData());
			}
		}
	}
}