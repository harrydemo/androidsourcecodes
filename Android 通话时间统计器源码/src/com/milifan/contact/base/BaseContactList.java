package com.milifan.contact.base;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.milifan.R;
import com.milifan.adapter.CallCursorAdapter;

/**
 * ListActivity基类，实现了每行单击事件和查询数据方法
 * @author Snake
 *
 */
public class BaseContactList extends ListActivity {

	protected String sort = "DESC";
	
	/**
	 * 根据查询条件或排序方式，进行数据查询并将查询到的结果填充到list的行
	 * @param where
	 * @param order
	 */
	protected void setListAdapter(String where, String order){
		if (order == null){
			order = CallLog.Calls.DEFAULT_SORT_ORDER;
		}
		Cursor cursor = getContentResolver().query(CallLog.Calls.CONTENT_URI,
				null, where, null, order);
		//将cursor生命周期交由activity来管理Activity.startManagingCursor(cursor)
		startManagingCursor(cursor);
		CallCursorAdapter adapter = new CallCursorAdapter(this,
				R.layout.callinfo, cursor,
				new String[] { "number", "name", "date", "duration"},
				new int[] { R.id.TextNumber,R.id.TextName, R.id.TextDate, R.id.TextDuration});
		setListAdapter(adapter);
	}
	
	/* (non-Javadoc)
	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		
		LinearLayout layout = (LinearLayout)v;
		TextView numberText = (TextView)layout.findViewById(R.id.TextNumber);
		Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel://"+numberText.getText()));
		startActivity(callIntent);
	}
}
