package com.crackedcarrot;

import com.crackedcarrot.menu.R;
import com.scoreninja.adapter.ScoreNinjaAdapter;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GameFinished extends Activity {

	public ScoreNinjaAdapter scoreNinjaAdapter;

	private int score;
	private int mapChoice;
	private boolean multiplayer;
	private boolean survivalgame;
	private int difficulty;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gamefinished);

		/**
		 * Ensures that the activity is displayed only in the portrait
		 * orientation
		 */
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		Typeface typefaceSniglet = Typeface.createFromAsset(getAssets(),
				"fonts/Sniglet.ttf");

		Bundle extras = getIntent().getExtras();
		score = extras.getInt("score");
		mapChoice = extras.getInt("map");
		boolean win = extras.getBoolean("win");
		multiplayer = extras.getBoolean("multiplayer", true);
		survivalgame = extras.getBoolean("survival");
		difficulty = extras.getInt("difficulty");

		ImageView imageTitle = (ImageView) findViewById(R.id.GameFinishedImageViewTitle);
		if (win)
			imageTitle.setImageResource(R.drawable.victory);
		else
			imageTitle.setImageResource(R.drawable.defeat);

		ImageView image = (ImageView) findViewById(R.id.GameFinishedImageViewImage);
		if (win)
			image.setImageResource(R.drawable.win);
		else
			image.setImageResource(R.drawable.loose);

		TextView tvTitle = (TextView) findViewById(R.id.GameFinishedTextViewTitle);
		tvTitle.setTypeface(typefaceSniglet);

		TextView tvText = (TextView) findViewById(R.id.GameFinishedTextViewText);
		tvText.setTag(typefaceSniglet);

		if (win) {
			tvTitle.setText("Congratulations!");
			tvText.setText("You've slain all the vile rabbits and their evil companions and saved your precious carrots!");
		} else {
			tvTitle.setText("You lost...");
			tvText.setText("The vile rabbits have conquered your pitiful backgarden, and worse, the world!");
		}

		if (multiplayer && win) {
			tvText.setText("You've defeated your opponent and watched him fall in the hands of the vile rabbits and their evil companions!");
		} else if (multiplayer && !win) {
			tvText.setText("Your opponent have used his vile rabbits to conquerer your pitiful backgarden!");

		}

		TextView tvScore = (TextView) findViewById(R.id.GameFinishedTextViewScore);
		tvScore.setTypeface(typefaceSniglet);

		if (survivalgame) {
			if (!multiplayer) {
				tvTitle.setText("In survival game there is no winner...");

				tvText.setText("Training makes perfect. Try again!");
				if (score >= 300)
					tvText.setText("Good game but you can do better!");
				if (score >= 500)
					tvText.setText("Great work. But can you beat 700?");
				if (score >= 700 && difficulty == 0)
					tvText.setText("Nice! Time to kick ass on hard...");
				if (score >= 700 && difficulty == 1)
					tvText.setText("Excellent work. You are one of the best rabbit slayers in the world! Maybe it is time to try hard?");
				if (score >= 700 && difficulty == 2)
					tvText.setText("Amazing! You've proved yourself to be among the ranks of the best rabbit slayers in the world!");
			}
			tvScore.setText("Kills: " + extras.getInt("score"));
		} else
			tvScore.setText("Final score: " + extras.getInt("score"));

		// Save everything and return to mainmenu.
		Button buttonBack = (Button) findViewById(R.id.GameFinished_Button_Ok);
		buttonBack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				backButton();
			}
		});

		// Load/prepare Scoreninja if it's active and installed.

		if (!this.multiplayer && !this.survivalgame
				&& ScoreNinjaAdapter.isInstalled(this)) {

			if (mapChoice == 1) {
				scoreNinjaAdapter = new ScoreNinjaAdapter(this, "mapzeroone",
						"E70411F009D4EDFBAD53DB7BE528BFE2");
			} else if (mapChoice == 2) {
				scoreNinjaAdapter = new ScoreNinjaAdapter(this, "mapzerotwo",
						"26CCAFB5B609DEB078F18D52778FA70B");
			} else if (mapChoice == 3) {
				scoreNinjaAdapter = new ScoreNinjaAdapter(this, "mapzerothree",
						"41F4C7AEF5A4DEF7BDC050AEB3EA37FC");
			} else if (mapChoice == 4) {
				scoreNinjaAdapter = new ScoreNinjaAdapter(this, "mapzerofour",
						"EF3428A86CD2387E603C7CE41B9AAD34");
			} else if (mapChoice == 5) {
				scoreNinjaAdapter = new ScoreNinjaAdapter(this, "mapzerofive",
						"FDF504FBDF1BF8E53968ED55CA591213");
			} else if (mapChoice == 6) {
				scoreNinjaAdapter = new ScoreNinjaAdapter(this, "mapzerosix",
						"28E2D9AB8D002455400C1D93B09D9A64");
			}

			scoreNinjaAdapter.show(score);
		}

	}

	// Unfortunate API, but you must notify ScoreNinja onActivityResult.
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		scoreNinjaAdapter.onActivityResult(requestCode, resultCode, data);
	}

	private void backButton() {
		finish();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
}
