package com.tilltheendwjx.airplus;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.ToggleButton;

public class AirPlusActivity extends Activity implements OnItemClickListener {
	static final String PREFERENCES = "AirPlusActivity";
	private ListView mAirsList;
	private Button mSetAirMode;

	private Cursor mCursor;
	private LayoutInflater mFactory;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mFactory = LayoutInflater.from(this);
		mCursor = Airs.getAirsCursor(getContentResolver());
		updateLayout();
		setAirMode();
	}

	private void updateLayout() {

		setContentView(R.layout.main);
		mAirsList = (ListView) findViewById(R.id.airs_list);
		AirTimeAdapter adapter = new AirTimeAdapter(this, mCursor);
		mAirsList.setAdapter(adapter);
		mAirsList.setVerticalScrollBarEnabled(true);
		mAirsList.setOnItemClickListener(this);
		mAirsList.setOnCreateContextMenuListener(this);

		View addAir = findViewById(R.id.add_air);
		addAir.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				addNewAir();
			}
		});
		// Make the entire view selected when focused.
		addAir.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				v.setSelected(hasFocus);
			}
		});
		mSetAirMode = (Button) findViewById(R.id.set_air_mode);
		mSetAirMode.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AirModeController.setAirplaneMode(AirPlusActivity.this,
						!AirModeController.IsAirModeOn(AirPlusActivity.this));
				setAirMode();
			}
		});

	}

	@Override
	public void onItemClick(AdapterView parent, View v, int pos, long id) {
		final Cursor c = (Cursor) mAirsList.getAdapter().getItem(pos);
		final Air air = new Air(c);
		Intent intent = new Intent(this, SetAir.class);
		intent.putExtra(Airs.AIR_INTENT_EXTRA, air);
		startActivity(intent);
	}

	private void updateAir(boolean enabled, Air air) {
		Airs.enableAir(this, air.id, enabled);
		if (enabled) {
			SetAir.popAirSetToast(this, air.start_hour, air.start_minutes,
					air.daysOfWeek);
		}
	}

	private class AirTimeAdapter extends CursorAdapter {
		public AirTimeAdapter(Context context, Cursor cursor) {
			super(context, cursor);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			View ret = mFactory.inflate(R.layout.air_time, parent, false);
			AirDigitalClock digitalClock = (AirDigitalClock) ret
					.findViewById(R.id.digitalClock);
			digitalClock.setLive(false);
			return ret;
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			final Air air = new Air(cursor);

			View indicator = view.findViewById(R.id.indicator);

			// Set the initial state of the clock "checkbox"
			final ToggleButton airOnOff = (ToggleButton) indicator
					.findViewById(R.id.air_onoff);
			airOnOff.setChecked(air.enabled);

			// Clicking outside the "checkbox" should also change the state.
			indicator.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					airOnOff.toggle();
					updateAir(airOnOff.isChecked(), air);
				}
			});

			AirDigitalClock digitalClock = (AirDigitalClock) view
					.findViewById(R.id.digitalClock);

			// set the air text
			final Calendar startc = Calendar.getInstance();
			final Calendar endc = Calendar.getInstance();
			startc.set(Calendar.HOUR_OF_DAY, air.start_hour);
			startc.set(Calendar.MINUTE, air.start_minutes);
			endc.set(Calendar.HOUR_OF_DAY, air.end_hour);
			endc.set(Calendar.MINUTE, air.end_minutes);
			digitalClock.updateTime(startc, endc);

			// Set the repeat text or leave it blank if it does not repeat.
			TextView daysOfWeekView = (TextView) digitalClock
					.findViewById(R.id.daysOfWeek);
			final String daysOfWeekStr = air.daysOfWeek.toString(
					AirPlusActivity.this, false);
			if (daysOfWeekStr != null && daysOfWeekStr.length() != 0) {
				daysOfWeekView.setText(daysOfWeekStr);
				daysOfWeekView.setVisibility(View.VISIBLE);
			} else {
				daysOfWeekView.setVisibility(View.GONE);
			}

			// Display the label
			TextView labelView = (TextView) view.findViewById(R.id.label);
			if (air.label != null && air.label.length() != 0) {
				labelView.setText(air.label);
				labelView.setVisibility(View.VISIBLE);
			} else {
				labelView.setVisibility(View.GONE);
			}
		}
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		setAirMode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.air_list_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	private void setAirMode() {
		boolean mode = AirModeController.IsAirModeOn(AirPlusActivity.this);
		mSetAirMode.setText((mode ? R.string.air_off : R.string.air_on));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateContextMenu(android.view.ContextMenu,
	 * android.view.View, android.view.ContextMenu.ContextMenuInfo)
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		getMenuInflater().inflate(R.menu.context_menu, menu);

		// Use the current item to create a custom view for the header.
		final AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		final Cursor c = (Cursor) mAirsList.getAdapter().getItem(info.position);
		final Air air = new Air(c);

		// Construct the Calendar to compute the time.
		final Calendar startCal = Calendar.getInstance();
		startCal.set(Calendar.HOUR_OF_DAY, air.start_hour);
		startCal.set(Calendar.MINUTE, air.start_minutes);
		final String startTime = Airs.formatTime(this, startCal);

		final Calendar endCal = Calendar.getInstance();
		endCal.set(Calendar.HOUR_OF_DAY, air.end_hour);
		endCal.set(Calendar.MINUTE, air.end_minutes);
		final String endTime = Airs.formatTime(this, endCal);

		// Inflate the custom view and set each TextView's text.
		final View mv = mFactory.inflate(R.layout.context_menu_header, null);
		TextView textView = (TextView) mv.findViewById(R.id.header_time);
		textView.setText(startTime + " To " + endTime);
		textView = (TextView) mv.findViewById(R.id.header_label);
		textView.setText(air.label);

		// Set the custom view on the menu.
		menu.setHeaderView(mv);
		// Change the text based on the state of the air.
		if (air.enabled) {
			menu.findItem(R.id.enable_air).setTitle(R.string.disable_air);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onContextItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		final int ids = (int) info.id;
		// Error check just in case.
		if (ids == -1) {
			return super.onContextItemSelected(item);
		}
		switch (item.getItemId()) {
		case R.id.delete_air: {
			// Confirm that the air will be deleted.
			new AlertDialog.Builder(this)
					.setTitle(getString(R.string.delete_air))
					.setMessage(getString(R.string.delete_air_confirm))
					.setPositiveButton(android.R.string.ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface d, int w) {
									Airs.deleteAir(AirPlusActivity.this, ids);
								}
							}).setNegativeButton(android.R.string.cancel, null)
					.show();
			return true;
		}

		case R.id.enable_air: {
			final Cursor c = (Cursor) mAirsList.getAdapter().getItem(
					info.position);
			final Air air = new Air(c);
			Airs.enableAir(this, air.id, !air.enabled);
			if (!air.enabled) {
				SetAir.popAirSetToast(this, air.start_hour, air.start_minutes,
						air.daysOfWeek);
			}
			return true;
		}

		case R.id.edit_air: {
			final Cursor c = (Cursor) mAirsList.getAdapter().getItem(
					info.position);
			final Air air = new Air(c);
			Intent intent = new Intent(this, SetAir.class);
			intent.putExtra(Airs.AIR_INTENT_EXTRA, air);
			startActivity(intent);
			return true;
		}

		default:
			break;
		}
		return super.onContextItemSelected(item);
	}

	private void addNewAir() {
		startActivity(new Intent(this, SetAir.class));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_item_settings:
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		case R.id.menu_item_add_air:
			addNewAir();
			return true;
		case R.id.menu_item_done:
			finish();
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}