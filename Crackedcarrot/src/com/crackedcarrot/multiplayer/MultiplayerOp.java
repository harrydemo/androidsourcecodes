package com.crackedcarrot.multiplayer;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crackedcarrot.menu.R;

public class MultiplayerOp extends Activity {

	// If this is set to 0 let the player play on Normal difficulty. Will read
	// data from integers.xml to set this
	int fullversion = 0;

	private Button HostButton;
	private Button JoinButton;
	private Button StartButton;
	private Spinner mapChooser;
	private Spinner difChooser;
	private Spinner modeChooser;
	private TextView tv;
	private LinearLayout hostoptions;
	private int mapId = 0;
	private int difId = 1;
	private int modeId = 0;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.multiplayer);

		/**
		 * Ensures that the activity is displayed only in the portrait
		 * orientation
		 */
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		tv = (TextView) findViewById(R.id.MpInfo);

		Resources r = getResources();
		fullversion = r.getInteger(R.integer.app_type);

		HostButton = (Button) findViewById(R.id.host);
		JoinButton = (Button) findViewById(R.id.join);
		hostoptions = (LinearLayout) findViewById(R.id.hostOpt);

		mapChooser = (Spinner) findViewById(R.id.mapChooser);
		ArrayAdapter adapter = ArrayAdapter.createFromResource(this,
				R.array.maps, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mapChooser.setAdapter(adapter);
		mapChooser.setOnItemSelectedListener(mapListener);
		if (fullversion != 0)
			mapChooser.setSelection(6);

		difChooser = (Spinner) findViewById(R.id.difChooser);
		ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this,
				R.array.difficulty, android.R.layout.simple_spinner_item);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		difChooser.setAdapter(adapter2);
		difChooser.setOnItemSelectedListener(difListener);
		difChooser.setSelection(1);

		modeChooser = (Spinner) findViewById(R.id.modeChooser);
		ArrayAdapter adapter3 = ArrayAdapter.createFromResource(this,
				R.array.gamemode, android.R.layout.simple_spinner_item);
		adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		modeChooser.setAdapter(adapter3);
		modeChooser.setOnItemSelectedListener(modeListener);

		HostButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				JoinButton.setVisibility(View.GONE);
				HostButton.setVisibility(View.GONE);
				hostoptions.setVisibility(View.VISIBLE);
				tv.setVisibility(View.GONE);
			}
		});

		JoinButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent StartClient = new Intent(v.getContext(), Client.class);
				startActivity(StartClient);
			}
		});

		Button BackButton = (Button) findViewById(R.id.back);
		BackButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});

		StartButton = (Button) findViewById(R.id.startHost);
		StartButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent StartServer = new Intent(v.getContext(), Server.class);
				StartServer.putExtra("com.crackedcarrot.multiplayer.map",
						mapId + 1);
				StartServer.putExtra(
						"com.crackedcarrot.multiplayer.difficulty", difId);
				StartServer.putExtra("com.crackedcarrot.multiplayer.gamemode",
						modeId + 1);
				startActivity(StartServer);
			}
		});
	}

	private Spinner.OnItemSelectedListener mapListener = new Spinner.OnItemSelectedListener() {
		public void onItemSelected(AdapterView parent, View v, int position,
				long id) {
			mapId = parent.getSelectedItemPosition();
			if (fullversion == 0 && mapId != 0 && mapId != 1) {
				mapId = 0;
				parent.setSelection(0);
				CharSequence text = "This map is not avaible in this version.";
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(getBaseContext(), text, duration);
				toast.show();
			}
		}

		public void onNothingSelected(AdapterView parent) {
		}
	};

	private Spinner.OnItemSelectedListener difListener = new Spinner.OnItemSelectedListener() {
		public void onItemSelected(AdapterView parent, View v, int position,
				long id) {
			difId = parent.getSelectedItemPosition();
		}

		public void onNothingSelected(AdapterView parent) {
		}
	};

	private Spinner.OnItemSelectedListener modeListener = new Spinner.OnItemSelectedListener() {
		public void onItemSelected(AdapterView parent, View v, int position,
				long id) {
			modeId = parent.getSelectedItemPosition();
		}

		public void onNothingSelected(AdapterView parent) {
		}
	};
}
