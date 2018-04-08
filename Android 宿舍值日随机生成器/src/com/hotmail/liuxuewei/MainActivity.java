package com.hotmail.liuxuewei;


import com.work.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private ListView listview;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_activity);
        
        listview = (ListView)findViewById(R.id.ListView01);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, dutyname);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	@Override
    		public void onItemClick(AdapterView<?> parent, View v, int pos,
    				long id) {
        		Toast.makeText(MainActivity.this, dutyname[pos],
						Toast.LENGTH_SHORT).show();
    		}
    	});
    }
	private final static String [] dutyname={"ÊŒ ÊŒ","»Ÿ √√","–€ √√","¥® √√","√ﬁ ∑Â","–° πÍ","±Ã ±Ã","«Â “Ø"};
}
