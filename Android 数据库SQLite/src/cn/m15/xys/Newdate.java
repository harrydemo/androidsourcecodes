package cn.m15.xys;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Newdate extends Activity {
    DatabaseHelper mDbHelper = null;
    SQLiteDatabase mDb = null;
    Context mContext = null;

    
    /**数据库字段**/
   
    public final static String TABLE_NAME ="test";
    public final static String ID ="_id";
    public final static String NAME ="name";
    public final static String HP ="hp";
    public final static String MP ="mp";
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	setContentView(R.layout.create_date);
	mContext = this;
	//创建DatabaseHelper对象      
	mDbHelper = DatabaseHelper.getInstance(mContext);
	//调用getReadableDatabase方法如果数据库不存在 则创建  如果存在则打开
	mDb= mDbHelper.getReadableDatabase();  
	//初始化 给数据库表写入一些信息
	for(int i=0; i<10; i++) {
	    insert(NAME,"雨松MOMO" + i); 
	}
	   
	
	
	//增加
	Button button0 = (Button)findViewById(R.id.add);
	button0.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View arg0) {
		insert(NAME,"新添加小可爱"); 
		 Toast.makeText(Newdate.this, "添加一条数据名称为小可爱", Toast.LENGTH_LONG).show();  
	    }
	});
	//删除
	Button button1 = (Button)findViewById(R.id.delete);
	button1.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View arg0) {
		delete(ID,"1");
		Toast.makeText(Newdate.this, "删除一条_id=1的数据", Toast.LENGTH_LONG).show();  
	    }
	});
	//修改
	Button button2 = (Button)findViewById(R.id.modify);
	button2.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View arg0) {
		update(NAME,"雨松MOMO3","小可爱3");
		Toast.makeText(Newdate.this, "更新名称雨松MOMO3 为小可爱3", Toast.LENGTH_LONG).show();  
	    }
	});
	//查找
	Button button3 = (Button)findViewById(R.id.find);
	button3.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View arg0) {
		Cursor cursor = find(ID,"5");
		String name = cursor.getString(cursor.getColumnIndex(NAME));
		Toast.makeText(Newdate.this, "查找ID为5数据的名称是 " + name, Toast.LENGTH_LONG).show();  
	    }
	});
	super.onCreate(savedInstanceState);
    }

    
    /**
     * 插入一条数据
     * @param key
     * @param date
     */
    public void insert(String key, String date) {
	ContentValues values = new ContentValues();
	values.put(key, date);
	mDb.insert(TABLE_NAME, null, values);
    }
    
    
    /**
     * 删除一掉数据
     * @param key
     * @param date
     */
    public void delete(String key, String date) {
	mDb.delete(TABLE_NAME, key+"=?", new String[] {date});
    }
    
    /**
     * 更新一条数据
     * @param key
     * @param oldDate
     * @param newDate
     */
    public void update(String key, String oldDate,String newDate) {
	ContentValues values = new ContentValues();
	values.put(key, newDate);
	mDb.update(TABLE_NAME, values, key+"=?", new String[] {oldDate});
    }
    
    /**
     * 查找一条数据
     * @param key
     * @param date
     * @return
     */
    public Cursor find(String key ,String date) {
	
	Cursor cursor = mDb.query(TABLE_NAME, null, key+"=?", new String[] {date}, null, null, null);
	if(cursor != null) {
	    cursor.moveToFirst();
	}
	return cursor;
    }

}
