package com.crackedcarrot.menu;

import java.io.IOException;
import java.io.InputStream;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView.ScaleType;
import android.widget.ViewSwitcher.ViewFactory;

import com.crackedcarrot.GameInit;

/**
 * This class/activity consists of three clickable objects, two buttons that
 * switches between the existing maps with belonging description, and the map
 * image that starts the game loop when clicked on. It sends a level variable to
 * the game loop the game loads the data depending on which level the user has
 * chosen.
 */
public class MapOp extends Activity implements ViewFactory {

	// If this is set to 0 let the player play on Normal difficulty. Will read
	// data from integers.xml to set this
	int fullversion = 0;

	/** The index for our "maps" array */
	private int difficulty = 1;
	private int mapSelected = 1;
	private int gameMode = 0;

	private TextView tv;

	private ImageView mBackground;

	private ImageView easy;
	private ImageView hard;
	private ImageView normal;

	private RadioButton radioEasy;
	private RadioButton radioNormal;
	private RadioButton radioHard;
	private RadioButton radioSurvivalGame;
	private RadioButton radioNormalGame;

	private Button StartGameButton;

	private Gallery gallery;

	private Bitmap bitmap1;
	private Bitmap bitmap2;
	private Bitmap bitmap3;
	private Bitmap bitmap4;
	private Bitmap bitmap5;
	private Bitmap bitmap6;

	/** References to our images */
	private Bitmap[] mmaps = { bitmap1, bitmap2, bitmap3, bitmap4, bitmap5,
			bitmap6, };

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainmenu_startgame);

		Resources r = getResources();
		fullversion = r.getInteger(R.integer.app_type);

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 8;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		InputStream is = this.getResources().openRawResource(R.drawable.map1);

		try {
			mmaps[0] = BitmapFactory.decodeStream(is, null, options);
			is = this.getResources().openRawResource(R.drawable.map2);
			mmaps[1] = BitmapFactory.decodeStream(is, null, options);
			if (fullversion == 0)
				options.inSampleSize = 1;
			is = this.getResources().openRawResource(R.drawable.map3);
			mmaps[2] = BitmapFactory.decodeStream(is, null, options);
			is = this.getResources().openRawResource(R.drawable.map4);
			mmaps[3] = BitmapFactory.decodeStream(is, null, options);
			is = this.getResources().openRawResource(R.drawable.map5);
			mmaps[4] = BitmapFactory.decodeStream(is, null, options);
			is = this.getResources().openRawResource(R.drawable.map6);
			mmaps[5] = BitmapFactory.decodeStream(is, null, options);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				// Skip
			}
		}

		/**
		 * Ensures that the activity is displayed only in the portrait
		 * orientation
		 */
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		/**
		 * identifying the image views and text view, these are the ones that
		 * will be set.
		 */
		mBackground = (ImageView) findViewById(R.id.mBackground);

		tv = (TextView) this.findViewById(R.id.maptext);
		Typeface face = Typeface.createFromAsset(this.getAssets(),
				"fonts/MuseoSans_500.otf");
		tv.setTypeface(face);

		gallery = (Gallery) findViewById(R.id.gallery1);
		gallery.setAdapter(new ImageAdapter(this));
		gallery.setOnItemSelectedListener(gItemSelectedHandler);
		gallery.setSelection((gallery.getCount() / 2) - 2, true);

		StartGameButton = (Button) findViewById(R.id.startmap);
		StartGameButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// Send the level variable to the game loop and start it

				StartGameButton.setVisibility(View.INVISIBLE);

				tv.setVisibility(View.INVISIBLE);

				radioEasy.setVisibility(View.INVISIBLE);
				radioNormal.setVisibility(View.INVISIBLE);
				radioHard.setVisibility(View.INVISIBLE);
				radioNormalGame.setVisibility(View.INVISIBLE);
				radioSurvivalGame.setVisibility(View.INVISIBLE);

				easy.setVisibility(View.INVISIBLE);
				normal.setVisibility(View.INVISIBLE);
				hard.setVisibility(View.INVISIBLE);

				gallery.setVisibility(View.INVISIBLE);

				mBackground.setImageResource(R.drawable.loadimage);
				mBackground.setScaleType(ScaleType.CENTER_INSIDE);

				Intent StartGame = new Intent(v.getContext(), GameInit.class);
				StartGame.putExtra("com.crackedcarrot.menu.map", mapSelected);
				StartGame.putExtra("com.crackedcarrot.menu.difficulty",
						difficulty);
				StartGame.putExtra("com.crackedcarrot.menu.wave", gameMode);
				startActivity(StartGame);
				finish();
			}
		});

		// Difficulty listeners.
		face = Typeface.createFromAsset(this.getAssets(),
				"fonts/MuseoSans_500.otf");
		radioEasy = (RadioButton) findViewById(R.id.radioEasy);
		radioEasy.setTypeface(face);
		radioNormal = (RadioButton) findViewById(R.id.radioNormal);
		radioNormal.setTypeface(face);
		radioHard = (RadioButton) findViewById(R.id.radioHard);
		radioHard.setTypeface(face);
		radioNormalGame = (RadioButton) findViewById(R.id.radioNormalGame);
		radioNormalGame.setTypeface(face);
		radioSurvivalGame = (RadioButton) findViewById(R.id.radioSurvivalGame);
		radioSurvivalGame.setTypeface(face);

		radioEasy.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				difficulty = 0;
				setRadioButtons(0);
			}

		});

		radioNormal.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				difficulty = 1;
				setRadioButtons(1);
			}
		});

		radioHard.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				difficulty = 2;
				setRadioButtons(2);
			}
		});

		radioNormalGame.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				gameMode = 0;
				radioNormalGame.setChecked(true);
				radioSurvivalGame.setChecked(false);
			}

		});

		radioSurvivalGame.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (fullversion == 0) {
					CharSequence text = "Survival is not available in this version.";
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(getBaseContext(), text,
							duration);
					toast.show();
					gameMode = 0;
					radioNormalGame.setChecked(true);
					radioSurvivalGame.setChecked(false);
				} else {
					gameMode = 3;
					radioNormalGame.setChecked(false);
					radioSurvivalGame.setChecked(true);
				}
			}

		});

		easy = (ImageView) findViewById(R.id.StartGameImageViewEasy);
		easy.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				difficulty = 0;
				setRadioButtons(0);
			}
		});

		normal = (ImageView) findViewById(R.id.StartGameImageViewNormal);
		normal.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				difficulty = 1;
				setRadioButtons(1);
			}
		});

		hard = (ImageView) findViewById(R.id.StartGameImageViewHard);
		hard.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				difficulty = 2;
				setRadioButtons(2);
			}
		});

	}

	private void setRadioButtons(int i) {
		radioEasy.setChecked(false);
		radioNormal.setChecked(false);
		radioHard.setChecked(false);

		switch (i) {
		case 0:
			radioEasy.setChecked(true);
			break;
		case 1:
			radioNormal.setChecked(true);
			break;
		case 2:
			radioHard.setChecked(true);
			break;
		}
	}

	// Called when we get focus again (after a game has ended).
	@Override
	public void onRestart() {
		super.onRestart();

		// TODO: Ta bort allt detta?
		// Reset the selected map?
		// mapSelected = 1;
		// tv.setText("Map 1: The field of grass.");
	}

	public View makeView() {
		ImageView imageView = new ImageView(this);
		imageView.setBackgroundResource(R.drawable.xml_gallery);
		imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		imageView.setLayoutParams(new ImageSwitcher.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		return imageView;
	}

	public OnItemSelectedListener gItemSelectedHandler = new OnItemSelectedListener() {
		// @Override
		public void onItemSelected(AdapterView<?> parent, View v,
				int _position, long id) {
			_position = _position % 6;
			switch (_position) {
			case 0:
				tv.setText("Map 1: The field of long grass.");
				mapSelected = 1;
				break;
			case 1:
				mapSelected = 2;
				tv.setText("Map 2: The field of cold grass.");
				break;
			case 2:
				if (fullversion == 0) {
					mapSelected = 1;
					tv.setText("Map 3: Not available in this version.");
				} else {
					mapSelected = 3;
					tv.setText("Map 3: The field of no grass.");
				}
				break;
			case 3:
				if (fullversion == 0) {
					mapSelected = 1;
					tv.setText("Map 4: Not available in this version.");
				} else {
					mapSelected = 4;
					tv.setText("Map 4: The field of long grass v2.");
				}
				break;
			case 4:
				if (fullversion == 0) {
					mapSelected = 1;
					tv.setText("Map 5: Not available in this version.");
				} else {
					mapSelected = 5;
					tv.setText("Map 5: The field of cold grass v2.");
				}
				break;
			case 5:
				if (fullversion == 0) {
					mapSelected = 1;
					tv.setText("Map 6: Not available in this version.");
				} else {
					mapSelected = 6;
					tv.setText("Map 6: The field of no grass v2.");
				}
				break;
			}

		}

		// @Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
		}
	};

	private class ImageAdapter extends BaseAdapter {
		private Context context;
		// private int itemBackground;
		public int position;
		private int x;
		private int y;

		public ImageAdapter(Context c) {
			context = c;
			x = (int) (110 * getResources().getDisplayMetrics().density);
			y = (int) (165 * getResources().getDisplayMetrics().density);
		}

		// ---returns the number of images---
		public int getCount() {
			return 1000;
		}

		public Object getItem(int position) {
			return position % 6;
		}

		public long getItemId(int position) {
			return position % 5;
		}

		// ---returns an ImageView view---
		public View getView(int position, View convertView, ViewGroup parent) {
			this.position = position % 6;
			ImageView imageView = new ImageView(context);
			imageView.setImageBitmap(mmaps[this.position]);
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			imageView.setLayoutParams(new Gallery.LayoutParams(x, y));
			imageView.setBackgroundResource(R.drawable.xml_gallery);
			return imageView;
		}
	}

}
