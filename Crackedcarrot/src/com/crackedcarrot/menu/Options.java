package com.crackedcarrot.menu;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.scoreninja.adapter.ScoreNinjaAdapter;

public class Options extends Activity {

	private boolean optionsHighscore;
	private boolean optionsNextLevel;
	private boolean optionsSound;

	private Button button1;
	private Button button2;
	private Button button3;

	public ScoreNinjaAdapter scoreNinjaAdapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainmenu_options);

		/**
		 * Ensures that the activity is displayed only in the portrait
		 * orientation
		 */
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		scoreNinjaAdapter = new ScoreNinjaAdapter(this, "mapzeroone",
				"E70411F009D4EDFBAD53DB7BE528BFE2");
		optionsHighscore = ScoreNinjaAdapter.isInstalled(this);

		// Restore preferences
		SharedPreferences settings = getSharedPreferences("Options", 0);
		optionsNextLevel = settings.getBoolean("optionsNextLevel", true);
		optionsSound = settings.getBoolean("optionsSound", true);

		Typeface face = Typeface.createFromAsset(getAssets(),
				"fonts/Sniglet.ttf");

		// Enable/disable sounds during the game.
		button1 = (Button) findViewById(R.id.MainMenuOptionsButton1);
		button1.setTypeface(face);
		button1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// Toggles sound on or off
				if (optionsSound) {
					setSound(false);
				} else {
					setSound(true);
				}
			}
		});
		// Update the sound-text
		setSound(optionsSound);

		// Enable/disable Highscore.
		button2 = (Button) findViewById(R.id.MainMenuOptionsButton2);
		button2.setTypeface(face);
		button2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// Toggles highscore on or off
				if (optionsHighscore) {
					setHighscore(false);
				} else {
					setHighscore(true);
				}
			}
		});
		// Update highscore-text
		setHighscore(optionsHighscore);

		// Enable/disable NextLevel-dialog.
		button3 = (Button) findViewById(R.id.MainMenuOptionsButton3);
		button3.setTypeface(face);
		button3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// Toggles highscore on or off
				if (optionsNextLevel) {
					setNextLevel(false);
				} else {
					setNextLevel(true);
				}
			}
		});
		// Update nextlevel-text
		setNextLevel(optionsNextLevel);

		// Show the highscore-activity.
		Button HighscoreButton = (Button) findViewById(R.id.MainMenuOptionsButton4);
		HighscoreButton.setTypeface(face);
		HighscoreButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent Highscore = new Intent(Options.this, Highscore.class);
				startActivity(Highscore);
			}
		});

		// Save everything and return to mainmenu.
		Button buttonSave = (Button) findViewById(R.id.MainMenuOptionsButtonOk);
		buttonSave.setTypeface(face);
		buttonSave.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				setSave();
			}
		});

	}

	@Override
	protected void onStop() {
		super.onStop();

		SharedPreferences settings = getSharedPreferences("Options", 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("optionsNextLevel", optionsNextLevel);
		editor.putBoolean("optionsSound", optionsSound);
		editor.commit();
	}

	public void setHighscore(boolean b) {
		this.optionsHighscore = b;

		if (b) {
			if (ScoreNinjaAdapter.isInstalled(this) == false) {
				if (ScoreNinjaAdapter.neverAskAgain(this)) {
					CharSequence text = "You have clicked never ask again and therefore ScoreNinja will not be installed.";
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(this, text, duration);
					toast.show();
					button2.setText("ScoreNinja: Off");
				} else
					button2.setText("ScoreNinja: On");

				// If ScoreNinja is enabled but not installed we try to install
				// it:
				scoreNinjaAdapter.show();
			} else
				button2.setText("ScoreNinja: On");
		} else {
			if (ScoreNinjaAdapter.isInstalled(this) == true) {
				button2.setText("ScoreNinja: On");

				CharSequence text = "You have to manually unistall ScoreNinja in android settings";
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(this, text, duration);
				toast.show();

			} else {
				button2.setText("ScoreNinja: Off");
			}
		}
	}

	public void setNextLevel(boolean b) {
		this.optionsNextLevel = b;

		if (b) {
			button3.setText("NextLevel: On");
		} else {
			button3.setText("NextLevel: Off");
		}
	}

	public void setSave() {
		this.finish();
	}

	public void setSound(boolean b) {
		this.optionsSound = b;

		if (b) {
			button1.setText("Sounds: On");
		} else {
			button1.setText("Sounds: Off");
		}
	}

}
