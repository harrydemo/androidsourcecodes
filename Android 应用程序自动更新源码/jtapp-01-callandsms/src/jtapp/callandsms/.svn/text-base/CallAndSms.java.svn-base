package jtapp.callandsms;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class CallAndSms extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
       
        setComponent();
    }
    
	private void setComponent() {
		Button bt1 = (Button) findViewById(R.id.Button01);
		bt1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(
						Intent.ACTION_CALL, Uri.parse("tel:10010"));
				startActivity(intent);
			}
		});
		Button bt2 = (Button) findViewById(R.id.Button02);
		bt2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String smsContent = "102";
				// note: SMS must be divided before being sent  
				SmsManager sms = SmsManager.getDefault();
				List<String> texts = sms.divideMessage(smsContent);
				for (String text : texts) {
					sms.sendTextMessage("10010", null, text, null, null);
				}
				// note: not checked success or failure yet
				Toast.makeText(
						CallAndSms.this, 
						"sms send 成功",
						Toast.LENGTH_SHORT ).show();
			}
		});
		
		Button bt3 = (Button) findViewById(R.id.Button03);
		bt3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(
						Intent.ACTION_DIAL, Uri.parse("tel:10010"));
				startActivity(intent);
			}
		});
		Button bt4 = (Button) findViewById(R.id.Button04);
		bt4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Uri uri = Uri.parse("smsto:10010");        
				Intent it = new Intent(Intent.ACTION_SENDTO, uri);        
				it.putExtra("sms_body", "102");        
				startActivity(it); 
			}
		});
		
		//Email
		Button bt5 = (Button) findViewById(R.id.Button05);
		bt5.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Setup the recipient in a String array
				String[] mailto = { "noam@gmail.com" };
				// Create a new Intent to send messages
				Intent sendIntent = new Intent(Intent.ACTION_SEND);
				// Write the body of theEmail
				String emailBody = "You're password is: ";
				// Add attributes to the intent
				//sendIntent.setType("text/plain"); // use this line for testing
													// in the emulator
				sendIntent.setType("message/rfc822"); // use this line for testing 
														// on the real phone
				sendIntent.putExtra(Intent.EXTRA_EMAIL, mailto);
				sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Your Password");
				sendIntent.putExtra(Intent.EXTRA_TEXT, emailBody);
				startActivity(sendIntent);
			}
		});
	}
}