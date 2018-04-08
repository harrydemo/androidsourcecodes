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

    
    /**���ݿ��ֶ�**/
   
    public final static String TABLE_NAME ="test";
    public final static String ID ="_id";
    public final static String NAME ="name";
    public final static String HP ="hp";
    public final static String MP ="mp";
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	setContentView(R.layout.create_date);
	mContext = this;
	//����DatabaseHelper����      
	mDbHelper = DatabaseHelper.getInstance(mContext);
	//����getReadableDatabase����������ݿⲻ���� �򴴽�  ����������
	mDb= mDbHelper.getReadableDatabase();  
	//��ʼ�� �����ݿ��д��һЩ��Ϣ
	for(int i=0; i<10; i++) {
	    insert(NAME,"����MOMO" + i); 
	}
	   
	
	
	//����
	Button button0 = (Button)findViewById(R.id.add);
	button0.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View arg0) {
		insert(NAME,"�����С�ɰ�"); 
		 Toast.makeText(Newdate.this, "���һ����������ΪС�ɰ�", Toast.LENGTH_LONG).show();  
	    }
	});
	//ɾ��
	Button button1 = (Button)findViewById(R.id.delete);
	button1.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View arg0) {
		delete(ID,"1");
		Toast.makeText(Newdate.this, "ɾ��һ��_id=1������", Toast.LENGTH_LONG).show();  
	    }
	});
	//�޸�
	Button button2 = (Button)findViewById(R.id.modify);
	button2.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View arg0) {
		update(NAME,"����MOMO3","С�ɰ�3");
		Toast.makeText(Newdate.this, "������������MOMO3 ΪС�ɰ�3", Toast.LENGTH_LONG).show();  
	    }
	});
	//����
	Button button3 = (Button)findViewById(R.id.find);
	button3.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View arg0) {
		Cursor cursor = find(ID,"5");
		String name = cursor.getString(cursor.getColumnIndex(NAME));
		Toast.makeText(Newdate.this, "����IDΪ5���ݵ������� " + name, Toast.LENGTH_LONG).show();  
	    }
	});
	super.onCreate(savedInstanceState);
    }

    
    /**
     * ����һ������
     * @param key
     * @param date
     */
    public void insert(String key, String date) {
	ContentValues values = new ContentValues();
	values.put(key, date);
	mDb.insert(TABLE_NAME, null, values);
    }
    
    
    /**
     * ɾ��һ������
     * @param key
     * @param date
     */
    public void delete(String key, String date) {
	mDb.delete(TABLE_NAME, key+"=?", new String[] {date});
    }
    
    /**
     * ����һ������
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
     * ����һ������
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
