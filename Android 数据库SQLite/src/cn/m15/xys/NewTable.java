package cn.m15.xys;


import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class NewTable extends Activity {
    DatabaseHelper mDbHelper = null;
    SQLiteDatabase mDb = null;
    Context mContext = null;
  
    /**����һ�ű��SQL���**/
    private static final String NAME_TABLE_CREATE = "create table gameInfo("
	    + "_id INTEGER PRIMARY KEY AUTOINCREMENT," + "name TEXT,"+ "hp INTEGER DEFAULT 100,"+ "mp INTEGER DEFAULT 100,"
	    + "number INTEGER);";
    
    
    /**ɾ��һ�ű��SQL���**/
    private static final String NAME_TABLE_DELETE = "DROP TABLE gameInfo";
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	setContentView(R.layout.create_table);
	mContext = this;
	mDbHelper = DatabaseHelper.getInstance(mContext);
	mDb= mDbHelper.getReadableDatabase();  
	
	Button button0 = (Button)findViewById(R.id.createTable);
	button0.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View arg0) {
		try {
		    mDb.execSQL(NAME_TABLE_CREATE);
		    Toast.makeText(NewTable.this, "�ɹ�������ݱ�", Toast.LENGTH_LONG).show();  
		}catch(SQLiteException e) {
		    Toast.makeText(NewTable.this, "���ݿ����Ѵ�˱�", Toast.LENGTH_LONG).show();    
		}
	    }
	});
	Button button1 = (Button)findViewById(R.id.deleteTable);
	button1.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View arg0) {
		try {
		    mDb.execSQL(NAME_TABLE_DELETE);
		    Toast.makeText(NewTable.this, "�ɹ�ɾ�����ݱ�", Toast.LENGTH_LONG).show(); 
		}catch(SQLiteException e) {
		    Toast.makeText(NewTable.this, "���ݿ������޴˱�", Toast.LENGTH_LONG).show();    
		}

	    }
	});
	
	
	
	super.onCreate(savedInstanceState);
    }

}
