package com.cola.ui;

import java.util.Calendar;
import java.util.TimeZone;

import com.cola.ui.Frm_Editacctitem.MyExpandableListAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemLongClickListener;

public class Grid_bills extends Activity implements OnItemLongClickListener {
	BilldbHelper billdb;
	View sv;
	EditText edit;
	AbsoluteLayout alayout;
	int a = 10, b = 10;
	GridView grd;

	TextView total;

	DatePicker dp;
	Button okbtn;
	ListView lv;

	private int mYear;
	private int mMonth;
	private int mDay;

	String today;
	String[] from;
	int[] to;

	SimpleCursorAdapter mAdapter;
	Cursor cur;
	int _id;

	protected GridView listHands = null;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		setContentView(R.layout.grid_bills);

		billdb = new BilldbHelper(this);

		lv = (ListView) findViewById(R.id.listview);

		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH)+1;
		mDay = c.get(Calendar.DAY_OF_MONTH);
		today = mYear + "-" + mMonth;
		setTitle("ColaBox-账单明细(" + today + ")");
		cur = billdb.getBills(today);
		from = new String[] { "rowid", "name", "fee", "sdate", "desc" };
		to = new int[] { R.id.item1, R.id.item2, R.id.item3, R.id.item4,
				R.id.item5 };
		mAdapter = new SimpleCursorAdapter(this, R.layout.grid_items, cur,
				from, to);
		lv.setAdapter(mAdapter);

		// getBillsTotal
		total = (TextView) findViewById(R.id.totalitem);
		total.setText(billdb.getBillsTotal(today));

		lv.setOnItemLongClickListener(this);

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 1, 0, "选择月份");// .setIcon(R.drawable.editbills);
		// menu.add(0, 2, 0, " 删 除 ");//.setIcon(R.drawable.editbills);

		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		// Log.v("ColaBox", "getmenuitemid=" + item.getItemId());
		switch (item.getItemId()) {
		case 1:
			showDialog("请选择年月:", "");
			return true;
		case 2:
			// Log.v("cola",lv.getSelectedItemId()+" p");
			// OnItemLongClickListener onItemLongClick;
			// lv.setOnItemLongClickListener(this);

			return true;

		}
		return false;
	}

	private void showDialog(String title, String text) {
		final DatePickerDialog dia = new DatePickerDialog(this,
				mDateSetListener, mYear, mMonth-1, mDay);

		dia.show();
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear+1;
			mDay = dayOfMonth;
			today = mYear + "-" + mMonth;

			setTitle("ColaBox-账单明细(" + today + ")");
			cur = billdb.getBills(today);
			mAdapter.changeCursor(cur);
			//lv.setAdapter(mAdapter);
			((SimpleCursorAdapter) mAdapter).notifyDataSetChanged();
		}
	};

	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// log.e("calllogactivity", view.tostring() + "position=" + position);
		// charsequence number = ((textview) view).gettext();
		// toast t = toast.maketext(this, number + " is long clicked",
		// toast.length_long);
		// t.show();
		
		_id=(int)id;
		new AlertDialog.Builder(this).setTitle("提示").setMessage(
				"确定删除该明细?").setIcon(R.drawable.quit).setPositiveButton("确定",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
                        //Log.v("",""+_id);
						 billdb.delBills(_id);
						 mAdapter.changeCursor(cur);
						 ((SimpleCursorAdapter) mAdapter).notifyDataSetChanged();
						// finish();
					}
				}).setNegativeButton("取消",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				}).show();

		return true;

	}

	/*
	 * public boolean onKeyDown(int keyCode, KeyEvent event) {
	 * Log.v("cola","keycode="+keyCode); switch (keyCode) { case
	 * KeyEvent.KEYCODE_BACK:
	 * 
	 * return false; case 22: //edit.layout(a, b, 100+a, 100+b); a+=10;b+=10;
	 * return true; case 21: //alayout.layout(a, b, 400, 500); return true;
	 *  }
	 * 
	 * //sv.invalidate(); return false; }
	 */
}