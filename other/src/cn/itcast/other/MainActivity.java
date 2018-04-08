package cn.itcast.other;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Uri uri = Uri.parse("content://cn.itcast.provides.personprovider/person");
        getContentResolver().registerContentObserver(uri, true, new PersonContentObserver(new Handler()));
    }
    
    private final class PersonContentObserver extends ContentObserver{

		public PersonContentObserver(Handler handler) {
			super(handler);
		}

		@Override
		public void onChange(boolean selfChange) {
			ContentResolver contentResolver = getContentResolver();
			Uri uri = Uri.parse("content://cn.itcast.provides.personprovider/person");
			Cursor cursor = contentResolver.query(uri, null, null, null, "personid asc");
			while(cursor.moveToNext()){
				int personid = cursor.getInt(cursor.getColumnIndex("personid"));
				String name = cursor.getString(cursor.getColumnIndex("name"));
				String phone = cursor.getString(cursor.getColumnIndex("phone"));
				int amount = cursor.getInt(cursor.getColumnIndex("amount"));
				Log.i(TAG, "personid="+ personid + ",name="+ name+ ",phone="+ phone+ ",amount="+ amount);
			}
			cursor.close();
		}
    	
    }
}