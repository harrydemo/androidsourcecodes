/**
 * @description : Identify a data model extends ArrayAdapter.
 * @version 1.0
 * @author Alex
 * @date 2010-11-10
 */
package teleca.androidtalk.facade;

import java.util.ArrayList;

import teleca.androidtalk.facade.util.Command;
import teleca.androidtalk.facade.util.Helper;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CommandListActivity extends Activity 
{
	public static final int DIAL_ID = Menu.FIRST;
	public static final int START_ID = Menu.FIRST + 1;
	public static final int GOTO_ID = Menu.FIRST + 2;
	public static final int SEARCH_ID = Menu.FIRST + 3;
	public static final int EXIT_ID = Menu.FIRST + 4;
	private ListView lv;
	private TextView tv;
	// private Button btn1,btn2;
	private CommandItemAdapter adapter;
    private ArrayList<Command> arraylist = new ArrayList<Command>();
	@Override
	public void onCreate(Bundle icicle) 
	{
		super.onCreate(icicle);
		setContentView(R.layout.commandlist);
		tv = (TextView)findViewById(R.id.text);
		tv.setTextSize(20);
		lv = (ListView) findViewById(android.R.id.list);
		//load data
		try 
		{
			 adapter = new CommandItemAdapter(this, R.layout.cmdlist_item, arraylist);
			Log.v("k", "middle");
		} catch (Exception e) 
		{
			Toast.makeText(CommandListActivity.this, "Exception", Toast.LENGTH_LONG).show();
		}
		lv.setAdapter(adapter);
		lv.setItemsCanFocus(false);
		getArrayList();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		super.onCreateOptionsMenu(menu);
		menu.add(0, DIAL_ID, 0,"Dialing");
		menu.add(1, START_ID, 1,"Start App");
		menu.add(2, GOTO_ID, 2, "Goto Web");
		menu.add(3, SEARCH_ID, 3, "Search Google");
		menu.add(4, EXIT_ID, 4, "Exit");
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
		case DIAL_ID:
			Intent dial_intent = new Intent();
			dial_intent.setClass(CommandListActivity.this, CommandDialingActivity.class);
			startActivityForResult(dial_intent,1);
			return true;
		case START_ID:
			Intent start_intent = new Intent();
			start_intent.setClass(CommandListActivity.this, CommandStartAppActivity.class);
			startActivityForResult(start_intent, 2);
			return true;
		case GOTO_ID:
			Intent web_intent = new Intent();
			web_intent.setClass(CommandListActivity.this, CommandGotoWebActivity.class);
			startActivityForResult(web_intent,3);
			return true;
		case SEARCH_ID:
			Intent search_intent = new Intent();
			search_intent.setClass(CommandListActivity.this, CommandSearchGoogleActivity.class);
			startActivityForResult(search_intent,4);
			return true;
		case EXIT_ID:
			finish();
		return true;	
		}
		return super.onOptionsItemSelected(item);
	}
	/**
	 *  accept the activity data and show it.
	 *  
	 * @param requestCode,resultCode,Intent
	 * @return 
	 */

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch(resultCode)
		{
		case RESULT_OK:
			Log.v("TAG", "!!!!!!!!!");
			if(arraylist != null)
			{
				arraylist.removeAll(arraylist);
			}
			getArrayList();			
			adapter = new CommandItemAdapter(this, R.layout.cmdlist_item, arraylist);
			lv.setAdapter(adapter);
			lv.invalidate();
		}
	}
	/**
	 *  load data to ArrayList
	 *  
	 * @param 
	 * @return 
	 */
	private void getArrayList()
	{
		Cursor cur = Helper.getCommandList(this);
		cur.moveToFirst();
		Command item;
		if(arraylist != null)
		{
			arraylist.removeAll(arraylist);
		}
		while(!cur.isAfterLast())
		{
			item = new Command(cur.getString(0),cur.getString(1),cur.getString(2),cur.getString(3));
	
			arraylist.add(item);
			cur.moveToNext();
			Log.v("TAG", item.toString());
		}
		adapter.notifyDataSetChanged();
	}
}
