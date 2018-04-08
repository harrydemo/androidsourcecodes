package cn.itcast.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.itcast.domain.Person;
import cn.itcast.service.PersonService;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
    private ListView listView;
    private PersonService service;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button button = (Button)this.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				ContentResolver contentResolver = getContentResolver();
				Uri insertUri = Uri.parse("content://cn.itcast.provides.personprovider/person");
				ContentValues values = new ContentValues();
				values.put("name", "laofang");
				values.put("phone", "13800029333");
				values.put("amount", "1000");
				contentResolver.insert(insertUri, values);
			}
		});
        
        listView = (ListView)this.findViewById(R.id.listView);
        service = new PersonService(this);
      /*  List<Person> persons = service.getScrollData(0, 5);       
        List<HashMap<String, Object>> data = new ArrayList<HashMap<String,Object>>();
        for(Person person : persons){
        	HashMap<String, Object> item = new HashMap<String, Object>();
        	item.put("name", person.getName());
        	item.put("phone", person.getPhone());
        	item.put("amount", person.getAmount());
        	data.add(item);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.item,
        		new String[]{"name","phone", "amount"}, new int[]{R.id.name, R.id.phone, R.id.amount});
        listView.setAdapter(adapter);
        
        listView.setOnItemClickListener(new ItemClickListener());
        */
        Cursor cursor = service.getCursorScrollData(0, 5);
        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this, R.layout.item, cursor,
        		new String[]{"name","phone","amount"}, new int[]{R.id.name, R.id.phone, R.id.amount});        
        listView.setAdapter(cursorAdapter);
        listView.setOnItemClickListener(new ItemClickListener());
    }
    
    private final class ItemClickListener implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			ListView lView = (ListView)parent;
			/*HashMap<String, Object> item = (HashMap<String, Object>)lView.getItemAtPosition(position);
			Log.i(TAG, item.get("name").toString());
			Toast.makeText(MainActivity.this, item.get("name").toString(), 1).show();*/
			Cursor cursor = (Cursor)lView.getItemAtPosition(position);
			String name = cursor.getString(cursor.getColumnIndex("name"));
			Toast.makeText(MainActivity.this, name, 1).show();
		}
    	
    }
}