package z.s.test;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class T9 extends Activity implements OnClickListener {

	private EditText phone;
	private Button delete;
	private Map<Integer, Integer> map = new HashMap<Integer, Integer>();
	private SoundPool spool;
	private AudioManager am = null;
	
	private ListView listView;
	private T9Adapter adapter;
	private MyApplication application;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.t9text);

		application = (MyApplication)getApplication();
		listView = (ListView) findViewById(R.id.contactList);
		am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

		spool = new SoundPool(11, AudioManager.STREAM_SYSTEM, 5);
		map.put(0, spool.load(this, R.raw.dtmf0, 0));
		map.put(1, spool.load(this, R.raw.dtmf1, 0));
		map.put(2, spool.load(this, R.raw.dtmf2, 0));
		map.put(3, spool.load(this, R.raw.dtmf3, 0));
		map.put(4, spool.load(this, R.raw.dtmf4, 0));
		map.put(5, spool.load(this, R.raw.dtmf5, 0));
		map.put(6, spool.load(this, R.raw.dtmf6, 0));
		map.put(7, spool.load(this, R.raw.dtmf7, 0));
		map.put(8, spool.load(this, R.raw.dtmf8, 0));
		map.put(9, spool.load(this, R.raw.dtmf9, 0));
		map.put(11, spool.load(this, R.raw.dtmf11, 0));
		map.put(12, spool.load(this, R.raw.dtmf12, 0));

		phone = (EditText) findViewById(R.id.phone);
		phone.setInputType(InputType.TYPE_NULL);
		phone.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(null == application.getContactInfo() || application.getContactInfo().size()<1){
				}else{
					if(null == adapter){
						adapter = new T9Adapter(T9.this);
						adapter.assignment(application.getContactInfo());
						listView.setAdapter(adapter);
						listView.setTextFilterEnabled(true);
					}else{
						adapter.getFilter().filter(s);
					}
				}
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			public void afterTextChanged(Editable s) {
			}
		});

		for (int i = 0; i < 12; i++) {
			View v = findViewById(R.id.dialNum1 + i);
			v.setOnClickListener(this);
		}

		delete = (Button) findViewById(R.id.delete);
		delete.setOnClickListener(this);
		delete.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View v) {
				phone.setText("");
				return false;
			}
		});
	}


	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialNum0:
			if (phone.getText().length() < 12) {
				play(1);
				input(v.getTag().toString());
			}
			break;
		case R.id.dialNum1:
			if (phone.getText().length() < 12) {
				play(1);
				input(v.getTag().toString());
			}
			break;
		case R.id.dialNum2:
			if (phone.getText().length() < 12) {
				play(2);
				input(v.getTag().toString());
			}
			break;
		case R.id.dialNum3:
			if (phone.getText().length() < 12) {
				play(3);
				input(v.getTag().toString());
			}
			break;
		case R.id.dialNum4:
			if (phone.getText().length() < 12) {
				play(4);
				input(v.getTag().toString());
			}
			break;
		case R.id.dialNum5:
			if (phone.getText().length() < 12) {
				play(5);
				input(v.getTag().toString());
			}
			break;
		case R.id.dialNum6:
			if (phone.getText().length() < 12) {
				play(6);
				input(v.getTag().toString());
			}
			break;
		case R.id.dialNum7:
			if (phone.getText().length() < 12) {
				play(7);
				input(v.getTag().toString());
			}
			break;
		case R.id.dialNum8:
			if (phone.getText().length() < 12) {
				play(8);
				input(v.getTag().toString());
			}
			break;
		case R.id.dialNum9:
			if (phone.getText().length() < 12) {
				play(9);
				input(v.getTag().toString());
			}
			break;
		case R.id.dialx:
			if (phone.getText().length() < 12) {
				play(11);
				input(v.getTag().toString());
			}
			break;
		case R.id.dialj:
			if (phone.getText().length() < 12) {
				play(12);
				input(v.getTag().toString());
			}
			break;
		case R.id.delete:
			delete();
			break;
		case R.id.callp:
			if (phone.getText().toString().length() >= 4) {
				call(phone.getText().toString());
			}
			break;
		default:
			break;
		}
	}
	
	private void play(int id) {
		int max = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		int current = am.getStreamVolume(AudioManager.STREAM_MUSIC);

		float value = (float)0.7 / max * current;
		spool.setVolume(spool.play(id, value, value, 0, 0, 1f), value, value);
	}
	private void input(String str) {
		int c = phone.getSelectionStart();
		String p = phone.getText().toString();
		phone.setText(p.substring(0, c) + str + p.substring(phone.getSelectionStart(), p.length()));
		phone.setSelection(c + 1, c + 1);
	}
	private void delete() {
		int c = phone.getSelectionStart();
		if (c > 0) {
			String p = phone.getText().toString();
			phone.setText(p.substring(0, c - 1) + p.substring(phone.getSelectionStart(), p.length()));
			phone.setSelection(c - 1, c - 1);
		}
	}
	private void call(String phone) {
		Uri uri = Uri.parse("tel:" + phone);
		Intent it = new Intent(Intent.ACTION_CALL, uri);
		startActivity(it);
	}










}
