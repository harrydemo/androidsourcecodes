package com.test;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListViewActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ListView listView = (ListView) findViewById(R.id.List);
        listView.setAdapter(new ArrayAdapter<String>(this, 
        	android.R.layout.simple_list_item_1, 
            new String[] {"兰州","北京","上海"}));
    }
}