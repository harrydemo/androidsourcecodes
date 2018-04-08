package com.np.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.np.data.DBAdapter;
import com.np.other.Alert;
import com.np.other.ToastSeal;
import com.wb.np.R;

public class SavingList extends Activity {
	private SavingList th;
	private String strArray[];
	
	private final int RETURN = Menu.FIRST;
	private final int backgroundColor1 =  0xFF000000;
	private final int backgroundColor2 =  0xFF313031;
	
	private ListView list;
	private DBAdapter db;
	private Cursor cursor;  
	

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        th = this;
        setContentView(R.layout.look);
        init();
        loadData();
    }
    
    private void init(){
    	list = (ListView)findViewById(R.id.savingList);
    	list.setOnItemClickListener(click);
    	list.setOnItemLongClickListener(keyLong);
        db = new DBAdapter(th).open();
		strArray = th.getResources().getStringArray(R.array.code);
    }
    
    private void loadData(){
    	cursor = db.getAllTitles();
//    	if(cursor.moveToFirst()){
//    		StringBuffer sb = new StringBuffer();
//    		do{
//    			sb.append("id = "+cursor.getString(cursor.getColumnIndex(DBAdapter.KEY_ID))+"\n");
//    		}while(cursor.moveToNext());
//    		ToastSeal.toast(th, sb.toString(), 5);
//    	}
	    String[] columns = {DBAdapter.KEY_MOBILE};
	    int[] id = new int[] {R.id.savingNumber};
	    SimpleCursorAdapter adapter=new SimpleCursorAdapter(this,R.layout.savingtext,cursor,columns,id);
	    list.setAdapter(adapter);
    }
    
    OnItemClickListener click = new OnItemClickListener(){
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			showMobile(position);
		}};
		
	OnItemLongClickListener keyLong = new OnItemLongClickListener(){
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				final int position, long id){
			cursor.moveToPosition(position);
			final String phone = cursor.getString(cursor.getColumnIndex(DBAdapter.KEY_MOBILE));
			String items[] = {"呼叫"+phone,"发送短信","从记录中删除"};
			Builder builder = new AlertDialog.Builder(th);
			builder.setTitle(phone);
			builder.setSingleChoiceItems(items,-1,	
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int item) {
							if(item == 0){
								// 生成呼叫意图
								Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+ phone));
								// 开始呼叫
								startActivity(intent);
							}else if(item == 1){
								Uri smsToUri = Uri.parse("smsto:"+phone);   
								Intent mIntent = new Intent( android.content.Intent.ACTION_SENDTO, smsToUri );   
								startActivity( mIntent);
							}else if(item == 2){
								if(db.deleteTitle(cursor.getInt(cursor.getColumnIndex(DBAdapter.KEY_ID)))){
									ToastSeal.toast(th, "删除成功", 1);
									loadData();
								}else{
									ToastSeal.toast(th, "删除失败", 1);
								}
							}
							dialog.dismiss();
						}
					});
			builder.setPositiveButton("取消",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			builder.create().show();
			return true;
		}};
    
    
    public boolean onCreateOptionsMenu(Menu menu){
    	super.onCreateOptionsMenu(menu);
    	menu.add(0,RETURN,0,R.string.backtrack).setIcon(R.drawable.ic_menu_revert);
    	return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item){
    	switch (item.getItemId()){
    	case RETURN:
    		th.finish();
    		db.close();
    		break;
		}
    	return super.onOptionsItemSelected(item);
    } 
    
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if(keyCode == KeyEvent.KEYCODE_BACK){
    		db.close();
    	}
		return super.onKeyDown(keyCode, event);
	}

	private void showMobile(int position){
    	LinearLayout layoutText = new LinearLayout(th);
    	layoutText.setOrientation(LinearLayout.VERTICAL);
    	layoutText.setGravity(Gravity.CENTER_VERTICAL);
    	cursor.moveToPosition(position);
		for(int i=0;i<strArray.length;i++){
			View view = View.inflate(th, R.layout.text, null);
			LinearLayout linear = (LinearLayout)view.findViewById(R.id.layoutInfo);
			TextView text1 = (TextView)view.findViewById(R.id.text1);
			TextView text2 = (TextView)view.findViewById(R.id.text2);
			if(i % 2 == 0)
				linear.setBackgroundColor(backgroundColor1);
			else {
				linear.setBackgroundColor(backgroundColor2);
			}
			text1.setText(strArray[i]);
			if(i + 1 < DBAdapter.KEY_ALL.length)
			text2.setText(cursor.getString(cursor.getColumnIndex(DBAdapter.KEY_ALL[i + 1])));
			layoutText.addView(view);
		}
		new Alert(th).setBuilder(0, "详细信息",layoutText,null, null);
    }
}