 /*************************************************************************** 
 *              Copyright (C) 2009 Andrico Team                             * 
 *              http://code.google.com/p/andrico/                           *
 *                             												*
 * Licensed under the Apache License, Version 2.0 (the "License");			*
 * you may not use this file except in compliance with the License.			*
 * 																			*	
 * You may obtain a copy of the License at 									*
 * http://www.apache.org/licenses/LICENSE-2.0								*
 *																			*
 * Unless required by applicable law or agreed to in writing, software		*
 * distributed under the License is distributed on an "AS IS" BASIS,		*
 *																			*
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.	*
 * See the License for the specific language governing permissions and		*
 * limitations under the License.											*
 ****************************************************************************/

package org.andrico.andrico;


import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import org.andrico.andrico.content.Contact;
import org.andrico.andrico.content.DBContact;
import org.andrico.andrico.facebook.LoginActivity;

import com.google.gdata.data.Feed;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ExpandableListActivity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import 	android.widget.ExpandableListView;
import android.widget.ExpandableListAdapter;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ExpandableListView.OnChildClickListener;



public class ContactList extends ListActivity
{
	private LinkedList<Contact> contacts = null;
	final static String TAG = "ContactList";
	private static int CONFIG_ORDER = 0;
	private SimpleAdapter listAdapter = null;
	protected ListView list;
	TextView selection;
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	    Window  w = getWindow(); 
	    w.requestFeature(Window.FEATURE_LEFT_ICON);   
	    setContentView(R.layout.contacts);
	    w.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_andrico);
	    
	    DBContact db = new DBContact();
     	
	    /*Contact contact = new Contact();
	    contact.setAdress("Moscow");
	    contact.setDateOfBirth("4.06.90");
	    contact.setFBid("1");
	    contact.setName("Mike");
	    contact.setPage("www.mike.com");
	    contact.setSecondName("Lanin");
	    
	    db.insert(ContactList.this, contact);
	    */
	    contacts = db.getContactList(ContactList.this);

	    LinkedList<Map<String, String>> conts = new LinkedList<Map<String, String>>();
	    
	    if (contacts != null)
		{
	    	for(int j = 0; j < contacts.size(); j++) 
         	{
        		TreeMap<String, String> cont = new TreeMap<String, String> ();
                
        		cont.put("contact", contacts.get(j).getName() + " " + contacts.get(j).getSecondName());
                cont.put("fbid", contacts.get(j).getFBid());
        		conts.add(cont);
            }
	    	
	    	listAdapter = new SimpleAdapter(this, conts, R.layout.group_row, 
         					new String[] {"contact", "fbid"},
         					new int[] {R.id.NameOfGroup, R.id.FBID});
        }  
	    
	    this.list = (ListView) this.findViewById(android.R.id.list);
    	list.setAdapter(listAdapter);
    	getListView().setTextFilterEnabled(true);
    	
    	
    	    	
    	this.findViewById(R.id.Synchronize).setOnClickListener(new OnClickListener()
        {
			public void onClick(View v)
			{   
				Intent i = new Intent(ContactList.this, Synchronize.class);
				startActivity(i);
				finish();
       		}
		});
        
        
        this.findViewById(R.id.BackToMenu).setOnClickListener(new OnClickListener()
        {
			public void onClick(View v)
			{
				Intent i = new Intent(ContactList.this,MainActivity.class);
                startActivity(i);
                finish();
       		}
		});
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		super.onListItemClick(l, v, position, id);
		
		TextView faceBookID = (TextView)v.findViewById(R.id.FBID );
		String fbid = (String) faceBookID.getText();
		
		Intent i = new Intent(ContactList.this, ContactInfo.class);
		i.putExtra("fbid", fbid);
		
		startActivity(i);
        finish();
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) 
    { 
    	if(keyCode==KeyEvent.KEYCODE_BACK)
    	{
    		Intent i = new Intent(ContactList.this,MainActivity.class);
    		startActivity(i);
            finish();
            return true;
    	}
		return false; 
	}
	
 }



