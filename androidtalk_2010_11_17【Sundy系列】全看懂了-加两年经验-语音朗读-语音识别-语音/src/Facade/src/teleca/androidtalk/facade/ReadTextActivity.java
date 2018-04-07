package teleca.androidtalk.facade;

import teleca.androidtalk.speechservice.ISpeechServiceProxy;
import teleca.androidtalk.speechservice.ServiceAction;
import teleca.androidtalk.speechservice.SpeechServiceProxy;


import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ReadTextActivity extends Activity  
{
	private Button btn, btn2;
	private EditText inputText;
	private Intent intent;
	private Bundle bd;
 
	@Override
	public void onCreate(Bundle icicle) 
	{
		super.onCreate(icicle);
		setContentView(R.layout.read);
		Drawable dw3 = this.getResources().getDrawable(R.drawable.btn_image);
		inputText = (EditText) findViewById(R.id.inputText);
		intent = getIntent();
		if (intent.getExtras() != null) 
		{
			bd = intent.getExtras();
			inputText.setText(bd.getString("FileContent"));
			Log.v("TAG", "t-34");
		}
		
		btn = (Button) findViewById(R.id.speakBtn);
		btn.setBackgroundDrawable(dw3);
		btn.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				ISpeechServiceProxy iss = new SpeechServiceProxy() ;
				iss.textToSpeech(ServiceAction.TTS_ACTION, ReadTextActivity.this, inputText.getText().toString()) ;
			}
		});
		btn2 = (Button) findViewById(R.id.readBtn);
		btn2.setBackgroundDrawable(dw3);
		btn2.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				Intent file_intent = new Intent();
				file_intent.setClass(ReadTextActivity.this, FileReadActivity.class);
				startActivity(file_intent);
			}
		});
	}
}