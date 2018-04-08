package com.test;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MenuActivity extends ListActivity {
    
	private static final String[] cities = new String[] {
        "����", "����", "�Ϻ�" 
    }; 
	
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        
        setListAdapter(new ArrayAdapter<String>(this,
        		android.R.layout.simple_list_item_single_choice,cities));
        
        final ListView listView = getListView();

        listView.setItemsCanFocus(true);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        
    }
    

}