package com.zhuyan.dialog;

import com.zhuyan.R;
import com.zhuyan.adapter.DialogListViewAdapter;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import com.zhuyan.model.*;

public class LongClickDialog extends Dialog {
	
	private static final String TAG="MainLongClickDialog";
	private ListView dialogListView=null;
	private DialogListViewAdapter dialogListViewAdapter=null;
	private EditText edit;

	public LongClickDialog(Context context,EditText editText) {
		super(context);
		// TODO Auto-generated constructor stub
		edit=editText;
		setContentView(R.layout.dialog_listview_view);
		dialogListView=(ListView)findViewById(R.id.dialog_listview_id);
		dialogListViewAdapter= new DialogListViewAdapter(context);
		dialogListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Log.i(TAG,"Click");
				edit.setText(((Person)dialogListViewAdapter.getItem(position)).getPhoneNumber());
			}
		});
		dialogListView.setAdapter(dialogListViewAdapter);
		
	}
}
