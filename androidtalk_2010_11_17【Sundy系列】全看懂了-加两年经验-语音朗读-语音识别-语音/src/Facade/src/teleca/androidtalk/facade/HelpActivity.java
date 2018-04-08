/**
 * @description : Activity that shows help information .
 * @version 1.0
 * @author Alex
 * @date 2010-11-08
 */
package teleca.androidtalk.facade;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class HelpActivity extends Activity
{
	private EditText inputText;
	private Button back_btn;
	private ListView lv;
	private ArrayAdapter<String> adapter;
	private String[] list = { "Dial phone No./contact name", "Goto <url>", "Search<keyword>","Start<APP>"};
	private static final String helpText = "Androidtalk is a application that can speak your input and .txt	file.You can touch the TapSpeak button to trigger the speech recognition function.You can touch the ReadText button to let the app read what you input and your.txt file.The CommandList shows the default system command for the speech recognition,you can define your personal command to recognize the speech.For anything unclear,please contact us,we are readying to help you."; 
	@Override
	public void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);
		setContentView(R.layout.help);

		Drawable dw3 = this.getResources().getDrawable(R.drawable.btn_image);
		inputText = (EditText)findViewById(R.id.edit);
		inputText.setText(helpText);
		inputText.setVerticalScrollBarEnabled(true);
		
		lv = (ListView) findViewById(android.R.id.list);
		adapter = new ArrayAdapter<String>(this,R.layout.hinfo, list);
		lv.setAdapter(adapter);
		lv.setItemsCanFocus(false);
		
		back_btn = (Button)findViewById(R.id.btn);
		back_btn.setBackgroundDrawable(dw3);
		back_btn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				Intent back_intent = new Intent();
				back_intent.setClass(HelpActivity.this, MainActivity.class);
				startActivity(back_intent);
			}			
		});
	}
}
