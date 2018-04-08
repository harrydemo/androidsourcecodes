package com.xiexh.hcreader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TxtList extends ListActivity {
	
	private final int DIALOG_INFO = 0;
	private final int DIALOG_ERR = 1;
	private final String TAG = "[HcReader]";
    private List<File> txtList = new ArrayList<File>(); ;
    /**
     * 入口
     */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG,"TxtListOnCreate");
		
		File sdCardPath = Environment.getExternalStorageDirectory();
		GetFiles( sdCardPath );
		
	}
	/**
	 * 获取文件列表
	 * @param filePath
	 */
	public void GetFiles( File filePath ){
	    int i;
	    //
	    File[] files = filePath.listFiles();
	    for(i=0;i<files.length;i++){
	    	if(files[i].isDirectory()){
	    		GetFiles( files[i] );
	    	}
	    	else if(files[i].getName().toLowerCase().endsWith(".txt")){ 
	    		txtList.add(files[i]);
	    	}
	    }
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,FileToStr(txtList));
		setListAdapter(adapter);
	}
	/**
	 * 把文件列表转换成字符串
	 * @param f
	 * @return
	 */
	public String[] FileToStr( List<File> f ){
		ArrayList<String> listStr = new ArrayList<String>();
		int i;
		for (i = 0; i < f.size(); i++) {
			String nameString = f.get(i).getName();
			listStr.add(nameString);
		}
		return listStr.toArray(new String[0]);
	}
	/**
	 * 当List中有点击时被激活
	 */
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Log.d(TAG, "listOnClick:"+txtList.get(position).toString() );
		File file = txtList.get(position); 
		
		Intent i = new Intent(TxtList.this,TxtView.class);
		i.putExtra("file", file.toString());
		startActivity(i);
		/*if(file.isDirectory()){
			GetFiles(file);
		}else if( file.getName().toLowerCase().endsWith(".txt") ){
			Intent i = new Intent(TxtList.this,TxtView.class);
			i.putExtra("file", file.toString());
			startActivity(i);
		}else{
			showDialog(DIALOG_ERR);
		}*/
	}
	/**
     * 创建对话框
     * @param id
     * @return
     */
    protected Dialog onCreateDialog( int id ){
    	switch(id){
    		case DIALOG_INFO:
    			return new AlertDialog.Builder(this)
    					.setIcon(android.R.drawable.ic_dialog_info)
    					.setTitle("Hello xxh")
    					.setMessage("版本：HC阅读器1.0 \n支持格式：txt")
    					.setPositiveButton("OK", null)
    					.create();
    		case DIALOG_ERR:
    			return new AlertDialog.Builder(this)
    					.setIcon(android.R.drawable.ic_dialog_info)
    					.setTitle("Hello xxh")
    					.setMessage("暂时只支持TXT格式")
    					.setPositiveButton("OK", null)
    					.create();
    		default:
    			return null;
    	}
    }
}
