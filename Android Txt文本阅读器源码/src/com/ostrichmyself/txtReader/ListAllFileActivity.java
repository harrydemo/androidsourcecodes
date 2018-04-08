package com.ostrichmyself.txtReader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/*展现全部文件的Activity*/
public class ListAllFileActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filelist);
		initFileList();
	}
	
	private void initFileList()
	{
		File path = android.os.Environment.getExternalStorageDirectory();
		File[] f = path.listFiles();
		fill(f);
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
		Dialog dialog = new AlertDialog.Builder(ListAllFileActivity.this).setTitle(
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
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(ListAllFileActivity.this, ViewFile.class);
		bundle = new Bundle();
		File file = fileNameList.get(position);
		if (file.isDirectory())
		{
			File[] f = file.listFiles();
			fill(f);
		}
		else {
			bundle.putString(fileNameKey, file.getAbsolutePath());
			intent.putExtras(bundle);
        	startActivityForResult(intent, 0);
		}
	}
	

	// 读取文件列表,并设置listview
	private void fill(File[] files) {
		fileNameList = new ArrayList<File>();
		for (File file : files) {
			if (isValidFileOrDir(file)) {
				fileNameList.add(file);
			}
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fileToStrArr(fileNameList));
		setListAdapter(adapter);
	}
	
	/*检查是否为合法的文件名，或者是否为路径*/
	private boolean isValidFileOrDir(File file)
	{
		if (file.isDirectory()) {
			return true;
		}
		else {
			String fileName = file.getName().toLowerCase();
			if (fileName.endsWith(".txt")) {
				return true;
			}
		}
		return false;
	}
	

	private String[] fileToStrArr(List<File> fl)
	{
		ArrayList<String> fnList = new ArrayList<String>();
		for (int i = 0; i < fl.size(); i++) {
			String nameString = fl.get(i).getName();
			fnList.add(nameString);
		}
		return fnList.toArray(new String[0]);
	}
	
	
	/*文件列表*/
	private List<File> fileNameList;
	private Bundle bundle;
	private String fileNameKey = "fileName";

}
