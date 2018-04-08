package cn.itcreator.android.reader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class IndexActivity extends Activity {

	
	private final static int DEVING  = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.indexactivity);
		
		Button localFileButton = (Button) findViewById(R.id.localBooks);
		
		localFileButton.setOnTouchListener(new View.OnTouchListener(){

			public boolean onTouch(View v, MotionEvent event) {
				
				Intent i = new Intent(getApplicationContext(),FileBrowser.class);
				startActivity(i);
				return true;
			}
		});
		localFileButton.setOnClickListener(new View.OnClickListener(){

			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(),FileBrowser.class);
				startActivity(i);
			}
		});
		
//		Button sinaBooks = (Button) findViewById(R.id.sinaBooks);
//		
//		sinaBooks.setOnTouchListener(new View.OnTouchListener(){
//
//			public boolean onTouch(View v, MotionEvent event) {
//				
//				showDialog(DEVING);
//				return true;
//			}
//		});
//		sinaBooks.setOnClickListener(new View.OnClickListener(){
//
//			public void onClick(View v) {
//				showDialog(DEVING);
//			}
//		});
		
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		
		switch (id) {
		case DEVING:
			return new AlertDialog.Builder(this)
			.setMessage(getString(R.string.dev))
			.setNegativeButton(getString(R.string.sure), new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int which) {
					dismissDialog(DEVING);
				}
				
			})
			.create();

		default:
			break;
		}
		return super.onCreateDialog(id);
	}
	
	
	
}
