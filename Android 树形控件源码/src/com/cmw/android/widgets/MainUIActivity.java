package com.cmw.android.widgets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemSelectedListener;

import com.cmw.android.data.ListDataProvider;

public class MainUIActivity extends ListActivity {
	private ListDataProvider provider = null;
	private static List<Map<String,String>> dataSource = new ArrayList<Map<String,String>>();
	ListView listView = null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);  
        setContentView(R.layout.main);  
        provider = new ListDataProvider(this);
        listView = this.getListView();
        provider.loadData();
        dataSource = provider.getDataSource();
        ListAdapter adapter = new SimpleAdapter(this, dataSource, R.layout.my_listitem,
        		new String[]{"caption","src"}, new int[]{R.id.itemCaption,R.id.itemActivityCls});
        setListAdapter(adapter);
        listView.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				System.out.println("onItemSelected--------------");
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				System.out.println("onNothingSelected--------------");
			}
        });
    }
    
    
    
    @SuppressWarnings("unchecked")
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
    	HashMap<String, String> selRecord = (HashMap<String, String>)l.getItemAtPosition(position);
		String caption = selRecord.get("caption");
		try {
			provider.foward(caption);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}