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
     * ���
     */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG,"TxtListOnCreate");
		
		File sdCardPath = Environment.getExternalStorageDirectory();
		GetFiles( sdCardPath );
		
	}
	/**
	 * ��ȡ�ļ��б�
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
	 * ���ļ��б�ת�����ַ���
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
	 * ��List���е��ʱ������
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
     * �����Ի���
     * @param id
     * @return
     */
    protected Dialog onCreateDialog( int id ){
    	switch(id){
    		case DIALOG_INFO:
    			return new AlertDialog.Builder(this)
    					.setIcon(android.R.drawable.ic_dialog_info)
    					.setTitle("Hello xxh")
    					.setMessage("�汾��HC�Ķ���1.0 \n֧�ָ�ʽ��txt")
    					.setPositiveButton("OK", null)
    					.create();
    		case DIALOG_ERR:
    			return new AlertDialog.Builder(this)
    					.setIcon(android.R.drawable.ic_dialog_info)
    					.setTitle("Hello xxh")
    					.setMessage("��ʱֻ֧��TXT��ʽ")
    					.setPositiveButton("OK", null)
    					.create();
    		default:
    			return null;
    	}
    }
}
