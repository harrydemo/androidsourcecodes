package cn.m15.xys;


import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class NewSQLite extends Activity {
    DatabaseHelper mDbHelper = null;
    SQLiteDatabase mDb = null;
    Context mContext = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	setContentView(R.layout.create_sql);
	mContext = this;
	//创建DatabaseHelper对象      
	mDbHelper = DatabaseHelper.getInstance(mContext);
	//调用getReadableDatabase方法如果数据库不存在 则创建  如果存在则打开
	mDb= mDbHelper.getReadableDatabase();  

	Button button0 = (Button)findViewById(R.id.createDateBase);
	button0.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View arg0) {

		Toast.makeText(NewSQLite.this, "成功创建数据库", Toast.LENGTH_LONG).show();  
	    }
	});
	Button button1 = (Button)findViewById(R.id.deleteDateBase);
	button1.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View arg0) {
		mDbHelper = DatabaseHelper.getInstance(mContext);
		// 调用getReadableDatabase方法如果数据库不存在 则创建 如果存在则打开
		mDb = mDbHelper.getReadableDatabase();
		// 关闭数据库
		mDbHelper.close();
		// 删除数据库
		mDbHelper.deleteDatabase(mContext);
		Toast.makeText(NewSQLite.this, "成功删除数据库", Toast.LENGTH_LONG).show();  
	    }
	});
	
	
	
	super.onCreate(savedInstanceState);
    }

}
