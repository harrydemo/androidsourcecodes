package wordroid.activitys;

import java.io.File;
import java.util.StringTokenizer;

import wordroid.business.OperationOfBooks;
import wordroid.database.DataAccess;
import wordroid.model.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class ImportBook extends ListActivity {
	private ProgressDialog myDialog = null;
	private String[] fileNames;
	private TextView filename ;
	private EditText numOfEachList ;
	private Button confirm ;
	private Button cancel ;
	public static String result="";
	protected static final int IMPORT_SUCCEED =0x108;
	protected static final int IMPORT_FAIL =0x109;
	RadioButton shunxu;
	RadioGroup radioGroup;
	String order;
	
	private Handler mHandler = new Handler(){ 
        public void handleMessage(Message msg) {
        	myDialog.dismiss();
            switch (msg.what) { 
                case IMPORT_SUCCEED: {  
                	Dialog dialog = new AlertDialog.Builder(ImportBook.this)
        	        .setIcon(R.drawable.dialog_icon)
        	        .setTitle("导入新词库")
        	        .setMessage("导入已完成！")
        	        .setPositiveButton("返回主菜单", new DialogInterface.OnClickListener() {
        	            public void onClick(DialogInterface dialog, int whichButton) {
        	                /* User clicked OK so do some stuff */
        	            	finish();
        	            }
        	        })
        	       .create();
        			dialog.show();
                    break; 
                } 
                default: {
                	DataAccess data = new DataAccess(ImportBook.this);
                	DataAccess.bookID="";
                	DataAccess.bookID=result;
                	if (!DataAccess.bookID.equals(""))
                	data.DeleteBook();
                	DataAccess.bookID="";
                	Toast.makeText(ImportBook.this, "导入词库失败", Toast.LENGTH_LONG)
        			.show();
                    break;
                    }
                
            } 
         } 
     }; 
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setTitle("导入新词库");
		this.setContentView(R.layout.import_book);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
		File f = new File("/sdcard/");
		File[] files=f.listFiles();
		fileNames = new String[files.length];
		for (int i=0;i<fileNames.length;i++){
			fileNames[i]=files[i].getName();
		}
		this.setListAdapter(new ArrayAdapter<String>(this, R.layout.file_row, fileNames));
		 
	}

	@Override
	protected void onListItemClick(ListView l, View v, final int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		final Dialog dialog = new Dialog(this);
		dialog.setTitle("导入新词库");
		dialog.setContentView(R.layout.import_book_dialog);
		dialog.show();
		filename = (TextView) dialog.findViewById(R.id.filename);
		 final EditText newname = (EditText) dialog.findViewById(R.id.newname);
		 numOfEachList = (EditText) dialog.findViewById(R.id.numOfEachList);
		 confirm = (Button) dialog.findViewById(R.id.import_confirm_button);
		 cancel = (Button) dialog.findViewById(R.id.import_cancel_button);
		 shunxu = (RadioButton)dialog.findViewById(R.id.radioButtonshunxu);
		 radioGroup =(RadioGroup) dialog.findViewById(R.id.radioGroup);
		 radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				
				if (checkedId==shunxu.getId()) order ="shunxu";
				else order="luanxu";
			}
			 
		 });
		filename.setText("词库文件："+fileNames[position]);
		cancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				dialog.cancel();
				
			}
			
		});
		 
		   
		StringTokenizer st = new StringTokenizer(fileNames[position], ".");
		newname.setText(st.nextToken());
		confirm.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
						if (newname.getText().toString().equals("")){
							dialog.cancel();
							Toast.makeText(ImportBook.this, "词库名称不能为空", Toast.LENGTH_LONG)
							.show();
						}
				try{
					if (Integer.parseInt(numOfEachList.getText().toString())<=0)
						throw new Exception();
				}catch(Exception e){
					dialog.cancel();
					Toast.makeText(ImportBook.this, "list容量格式不正确（请输入大于0的整数）", Toast.LENGTH_LONG)
					.show();
				}
				dialog.cancel();
				 
				final Thread thread =new Thread(new Runnable(){
					public void run(){
						OperationOfBooks OOB = new OperationOfBooks();
						try {
							DataAccess.ifContinue=true;
							result="";
							if(OOB.ImportBook(ImportBook.this, fileNames[position],newname.getText().toString(), Integer.parseInt(numOfEachList.getText().toString()),order)){
								Message m = new Message();
								m.what=ImportBook.IMPORT_SUCCEED;
								ImportBook.this.mHandler.sendMessage(m);
							}
							else{
								Message m = new Message();
								m.what=ImportBook.IMPORT_FAIL;
								ImportBook.this.mHandler.sendMessage(m);
							}
							
						} catch (Exception e) {
							Message m = new Message();
							m.what=ImportBook.IMPORT_FAIL;
							ImportBook.this.mHandler.sendMessage(m);
						}
					}

					});
				thread.start();
				myDialog = new ProgressDialog(ImportBook.this);
				 myDialog.setTitle("导入新词库");
				 myDialog.setMessage("请稍等，这可能需要几分钟的时间");
				 myDialog.setCancelable(false);
                myDialog.setButton("取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						DataAccess.ifContinue=false;
						OperationOfBooks.ifContinue=false;
					}
				});
                myDialog.show();
			}
			
		});
	}


}
