package com.itensen.test;

import android.R.string;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class MenuActivity extends Activity {
	ListView list;
	RelativeLayout newNameView;
	EditText newName;
	Button finishButton;
	Button cancelButton;
	String[] menus;
	String exec;
	String[] x=new String[]{"\\","//",":","*","?","\"","<",">","|"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog);
		menus=getIntent().getStringArrayExtra("menus");
		
		if(menus.length>0){
			list=(ListView)findViewById(R.id.list_dialog_list);
			newNameView =(RelativeLayout)findViewById(R.id.layout_dialog_newname);
			newName =(EditText)findViewById(R.id.edit_dialog_newname);
			finishButton =(Button)findViewById(R.id.button_dialog_finish);
			cancelButton =(Button)findViewById(R.id.button_dialog_cansel);
			
			finishButton.setOnClickListener(new ButtonOnClick());
			cancelButton.setOnClickListener(new ButtonOnClick());
			
			ArrayAdapter<String> adapter=new ArrayAdapter<String>(MenuActivity.this, R.layout.dialog_listview_item, menus);
		
			list.setAdapter(adapter);
			list.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					exec=menus[arg2];
					
					if(exec.equals("重命名")){
						newNameView.setVisibility(View.VISIBLE);
						list.setVisibility(View.GONE);
					}else{
						Intent intent=new Intent();
						intent.putExtra("exec", exec);
						MenuActivity.this.setResult(RESULT_OK, intent);
						MenuActivity.this.finish();
					}
					
				}
			});
		}
		
	}
	
	class ButtonOnClick implements android.view.View.OnClickListener{
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.button_dialog_finish:
				String name=newName.getText().toString()+"";
				if(name.length()>0){
					//去除文件名中的非法字符
					for(int i=0;i<x.length;i++){
						name.replace(x[i], "");
					}
					Intent intent=new Intent();
					intent.putExtra("exec",exec);
					intent.putExtra("name",name);
					MenuActivity.this.setResult(RESULT_OK, intent);
					MenuActivity.this.finish();
				}
				break;
			case R.id.button_dialog_cansel:
				MenuActivity.this.setResult(RESULT_CANCELED);
				MenuActivity.this.finish();
				break;

			default:
				break;
			}
			
		}
		
		
	}

}
