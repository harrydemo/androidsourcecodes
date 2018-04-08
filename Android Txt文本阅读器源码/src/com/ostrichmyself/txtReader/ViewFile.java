package com.ostrichmyself.txtReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * 打开文件的方法
 * @author Administrator
 *
 */
public class ViewFile extends Activity {
	
	private String filenameString;
	private static final String gb2312 = "GB2312";
	private static final String utf8 = "UTF-8";
	private static final String defaultCode = gb2312;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filebrowser);
		try {
			Bundle bunde = this.getIntent().getExtras();
			filenameString = bunde.getString("fileName");
			refreshGUI(defaultCode);
		} catch (Exception e) {
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = this.getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.gb2312:
			refreshGUI(defaultCode);
			break;
		case R.id.utf8:
			refreshGUI(utf8);
			break;
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
		Dialog dialog = new AlertDialog.Builder(ViewFile.this).setTitle(
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

	private void refreshGUI(String code)
	{
		TextView tv = (TextView) findViewById(R.id.view_contents);
		String fileContent = getStringFromFile(code);
		tv.setText(fileContent);
	}
	
	public String getStringFromFile(String code)
	{
		try {
			StringBuffer sBuffer = new StringBuffer();
			FileInputStream fInputStream = new FileInputStream(filenameString);
			InputStreamReader inputStreamReader = new InputStreamReader(fInputStream, code);
			BufferedReader in = new BufferedReader(inputStreamReader);
			if(!new File(filenameString).exists())
			{
				return null;
			}
			while (in.ready()) {
				sBuffer.append(in.readLine() + "\n");
			}
			in.close();
			return sBuffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	// 读取文件内容
	public byte[] readFile(String fileName) throws Exception {
		byte[] result = null;
		FileInputStream fis = null;
		try {
			File file = new File(fileName);
			fis = new FileInputStream(file);
			result = new byte[fis.available()];
			fis.read(result);
		} catch (Exception e) {
		} finally {
			fis.close();
		}
		return result;
	}

}
