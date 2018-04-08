package com.ostrichmyself.txtReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
/**
 * 纯文本浏览器
 * @author Administrator
 *
 */
public class TxtReader extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
       
        Button fileOpenBtn = (Button)findViewById(R.id.openFIleBtn);
        fileOpenBtn.setOnClickListener(new OpenFileAction());

    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = this.getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		menu.removeItem(R.id.gb2312);
		menu.removeItem(R.id.utf8);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.about:
			doAbout();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	//弹出关于框
	private void doAbout() {
		Dialog dialog = new AlertDialog.Builder(TxtReader.this).setTitle(
				R.string.aboutTitle).setMessage(R.string.aboutInfo)
				.setPositiveButton(R.string.aboutOK,
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialoginterface, int i) {
								// 按钮事件
							}
						}).create();
		dialog.show();
	}
	
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}



	class OpenFileAction implements OnClickListener
    {

		public void onClick(View v) {
			
			Intent in = new Intent(TxtReader.this, ListAllFileActivity.class);
        	startActivityForResult(in, 0);
		}
    	
    }
}