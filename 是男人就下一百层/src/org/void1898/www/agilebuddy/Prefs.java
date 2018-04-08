package org.void1898.www.agilebuddy;

import org.void1898.www.agilebuddy.util.ConstantInfo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author void1898@gmail.com
 * 
 */
public class Prefs extends Activity implements OnClickListener,
		SeekBar.OnSeekBarChangeListener {

	private SharedPreferences mBaseSettings;

	private SharedPreferences mRankingSettings;

	private EditText mUserNameEditText;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.options);

		mBaseSettings = PreferenceManager.getDefaultSharedPreferences(this);

		CheckBox vibrateCheckbox = (CheckBox) findViewById(R.id.options_vibrate_checkbox);
		vibrateCheckbox.setChecked(mBaseSettings.getBoolean(
				ConstantInfo.PREFERENCE_KEY_VIBRATE, true));
		vibrateCheckbox
				.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							mBaseSettings.edit().putBoolean(
									ConstantInfo.PREFERENCE_KEY_VIBRATE, true)
									.commit();
						} else {
							mBaseSettings.edit().putBoolean(
									ConstantInfo.PREFERENCE_KEY_VIBRATE, false)
									.commit();
						}
					}
				});

		CheckBox soundsCheckbox = (CheckBox) findViewById(R.id.options_sounds_checkbox);
		soundsCheckbox.setChecked(mBaseSettings.getBoolean(
				ConstantInfo.PREFERENCE_KEY_SOUNDS, true));
		soundsCheckbox
				.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							mBaseSettings.edit().putBoolean(
									ConstantInfo.PREFERENCE_KEY_SOUNDS, true)
									.commit();
						} else {
							mBaseSettings.edit().putBoolean(
									ConstantInfo.PREFERENCE_KEY_SOUNDS, false)
									.commit();
						}
					}
				});

		CheckBox showTipsCheckbox = (CheckBox) findViewById(R.id.options_showtips_checkbox);
		showTipsCheckbox.setChecked(mBaseSettings.getBoolean(
				ConstantInfo.PREFERENCE_KEY_SHOWTIPS, true));
		showTipsCheckbox
				.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							mBaseSettings.edit().putBoolean(
									ConstantInfo.PREFERENCE_KEY_SHOWTIPS, true)
									.commit();
						} else {
							mBaseSettings
									.edit()
									.putBoolean(
											ConstantInfo.PREFERENCE_KEY_SHOWTIPS,
											false).commit();
						}
					}
				});

		SeekBar seekBar = (SeekBar) findViewById(R.id.velocityController);
		seekBar.setProgress(mBaseSettings.getInt(
				ConstantInfo.PREFERENCE_KEY_POWER, 40));
		seekBar.setOnSeekBarChangeListener(this);

		mRankingSettings = getSharedPreferences(
				ConstantInfo.PREFERENCE_RANKING_INFO, 0);

		mUserNameEditText = (EditText) findViewById(R.id.options_username_edittext);
		mUserNameEditText.setText(mRankingSettings.getString(
				ConstantInfo.PREFERENCE_KEY_RANKING_NAME, ""));

		TextView bestRecordTextView = (TextView) findViewById(R.id.options_best_record_textview);
		bestRecordTextView.setText(""
				+ bestRecordTextView.getText()
				+ mRankingSettings.getInt(
						ConstantInfo.PREFERENCE_KEY_RANKING_SCORE, 0));

		Button okayButton = (Button) findViewById(R.id.options_okay_button);
		okayButton.setOnClickListener(this);

		Button uploadScoreButton = (Button) findViewById(R.id.options_upload_score_button);
		uploadScoreButton.setOnClickListener(this);

		Button tipsButton = (Button) findViewById(R.id.options_tips_button);
		tipsButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		String userName = null;
		if (mUserNameEditText.getText() != null) {
			userName = mUserNameEditText.getText().toString()
					.replace("\n", " ").trim();
		}
		if (userName == null || "".equals(userName)) {
			showToast(R.string.options_toast_username_null);
			return;
		} else if (userName.length() > 20) {
			showToast(R.string.options_toast_username_too_long);
			return;
		}
		mRankingSettings.edit().putString(
				ConstantInfo.PREFERENCE_KEY_RANKING_NAME, userName).commit();
		switch (v.getId()) {
		case R.id.options_okay_button:
			finish();
			break;
		case R.id.options_upload_score_button:
			mRankingSettings.edit().putBoolean(
					ConstantInfo.PREFERENCE_KEY_RANKING_FLAG,
					!mRankingSettings.getBoolean(
							ConstantInfo.PREFERENCE_KEY_RANKING_FLAG, false))
					.commit();
			break;
		case R.id.options_tips_button:
			Intent i = new Intent(this, TipsActivity.class);
			startActivity(i);
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		mBaseSettings.edit().putInt(ConstantInfo.PREFERENCE_KEY_POWER,
				seekBar.getProgress()).commit();
	}

	private void showToast(int strId) {
		Toast toast = Toast.makeText(this, strId, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.TOP, 0, 220);
		toast.show();
	}
}
