package com.crackedcarrot.menu;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

import com.crackedcarrot.GameInit;
import com.crackedcarrot.GameLoop;
import com.crackedcarrot.multiplayer.MultiplayerOp;
import com.scoreninja.adapter.ScoreNinjaAdapter;

public class MainMenu extends Activity {

	Dialog dialog;
	private int resumes;
	private ImageView mBackground;
	private TableLayout mTable;
	private Dialog dialogInfo = null;
	private WebView mWebView; // used to display html-pages.
	private SharedPreferences mPrefs;
	private String url = "";

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// Log.d("MAINMENU", "onKeyDown KEYCODE_BACK");
			// Log.d("MAINMENU", "Calling System.exit(0)");
			System.exit(0);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	// Shows the "New Game or Resume old game?"-dialog.
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 1:
			dialog = new Dialog(this, R.style.NextlevelTheme);
			dialog.setContentView(R.layout.levelresume);
			dialog.setCancelable(true);

			TextView textView = (TextView) dialog
					.findViewById(R.id.LevelResume_Text);
			textView.setText("Resume last game? You have " + (3 - resumes)
					+ " resume(s) left.");

			Button buttonStartGame = (Button) dialog
					.findViewById(R.id.Resume_StartGame);
			buttonStartGame.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dialog.dismiss();
					Intent StartGame = new Intent(v.getContext(), MapOp.class);
					startActivity(StartGame);
				}
			});

			Button buttonResume = (Button) dialog
					.findViewById(R.id.Resume_Resume);
			buttonResume.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					// Send the level variable to the game loop and start it
					dialog.dismiss();

					mBackground.setImageResource(R.drawable.loadimage);
					mBackground.setScaleType(ScaleType.CENTER_INSIDE);
					mTable.setVisibility(View.INVISIBLE);

					Intent StartGame = new Intent(v.getContext(),
							GameInit.class);
					StartGame.putExtra("com.crackedcarrot.menu.map", 0);
					StartGame.putExtra("com.crackedcarrot.menu.difficulty", 0);
					// Since this is not a multiplayergame we will send 0 to
					// gameinit
					StartGame.putExtra("com.crackedcarrot.menu.wave", 0);
					startActivity(StartGame);
				}
			});

			dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
				public void onDismiss(DialogInterface dialog) {
					// nothing. dialog is already closed.
				}
			});

			break;

		case 2:
			dialogInfo.setCancelable(true);

			Button close = (Button) dialogInfo
					.findViewById(R.id.closewebdialog);
			close.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					// nothing else. handled by onDismissListener instead, it's
					// better.
					dialogInfo.dismiss();
				}
			});

			final Button back = (Button) dialogInfo
					.findViewById(R.id.backwebdialog);
			back.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					// We'll never have a Back button in this dialog.
					mWebView.goBack();
				}
			});

			dialogInfo
					.setOnDismissListener(new DialogInterface.OnDismissListener() {
						public void onDismiss(DialogInterface dialog) {
							// Done with this window, unpause stuff.
							mWebView.clearView();
							GameLoop.unPause();
						}
					});

			mWebView.setBackgroundColor(0);

			WebSettings webSettings = mWebView.getSettings();
			webSettings.setSavePassword(false);
			webSettings.setSaveFormData(false);
			webSettings.setJavaScriptEnabled(false);
			webSettings.setSupportZoom(false);

			mWebView.setWebViewClient(new WebViewClient() {
				public void onPageFinished(WebView view, String url) {
					if (mWebView.getUrl().equals(
							"file:///android_asset/instructions.html")) {
						mWebView.clearHistory();
					}

					if (mWebView.canGoBack()) {
						back.setVisibility(View.VISIBLE);
					} else {
						back.setVisibility(View.INVISIBLE);
					}
				}
			});

			return dialogInfo;

		default:
			// Log.d("MAINMENU", "onCreateDialog got unknown dialog id: " + id);
			dialog = null;
		}
		return dialog;
	}

	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case 1:
			TextView textView = (TextView) dialog
					.findViewById(R.id.LevelResume_Text);
			textView.setText("Resume last game? You have " + (3 - resumes)
					+ " resume(s) left.");
		case 2:
			mWebView.clearHistory();
			mWebView.loadUrl(url);
			break;
		default:
			// Log.d("MAINMENU", "onPrepareDialog got unknown dialog id: " +
			// id);
			dialog = null;
		}
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainmenu);

		/**
		 * Ensures that the activity is displayed only in the portrait
		 * orientation
		 */
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		mBackground = (ImageView) findViewById(R.id.mainmenuBackground);
		mTable = (TableLayout) findViewById(R.id.mainmenutable);

		Button StartGameButton = (Button) findViewById(R.id.StartGame);
		StartGameButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// See if there's any old game saved that can be resumed.
				SharedPreferences resume = getSharedPreferences("resume", 0);
				resumes = resume.getInt("resumes", -1);
				// Log.d("MAINMENU", "resumes: " + resumes);

				if (resumes > -1 && resumes < 3) {
					showDialog(1);
				} else {
					Intent StartGame = new Intent(v.getContext(), MapOp.class);
					startActivity(StartGame);
				}
			}
		});

		Button OptionsButton = (Button) findViewById(R.id.Options);
		OptionsButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent Options = new Intent(MainMenu.this, Options.class);
				startActivity(Options);
			}
		});

		Button HelpButton = (Button) findViewById(R.id.Help);
		HelpButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				url = "file:///android_asset/instructions.html";
				showDialog(2);
			}
		});

		Button MultiPlayerButton = (Button) findViewById(R.id.Multiplayer);
		MultiPlayerButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent Multiplayer = new Intent(MainMenu.this,
						MultiplayerOp.class);
				startActivity(Multiplayer);
			}
		});

		dialogInfo = new Dialog(this, R.style.NextlevelTheme);
		dialogInfo.setContentView(R.layout.webinstruction);
		mWebView = (WebView) dialogInfo.findViewById(R.id.webview);

		Context mContext = this.getApplicationContext();
		mPrefs = mContext.getSharedPreferences("myAppPrefs", 0); // 0 = mode
																	// private.
																	// only this
																	// app can
																	// read
																	// these
																	// preferences

		String versionNo = "";
		PackageInfo pInfo = null;
		try {
			pInfo = getPackageManager().getPackageInfo("com.crackedcarrot",
					PackageManager.GET_META_DATA);
		} catch (NameNotFoundException e) {
			pInfo = null;
		}
		if (pInfo != null)
			versionNo = "" + pInfo.versionCode;

		if (getFirstRun(versionNo)) {
			url = "file:///android_asset/changelog.html";
			showDialog(2);
			setRunned(versionNo);
		}
	}

	// Called when we get focus again (after a game has ended).
	@Override
	public void onResume() {
		super.onResume();

		mBackground.setImageResource(R.drawable.mainmenu);
		mBackground.setScaleType(ScaleType.FIT_XY);
		mTable.setVisibility(View.VISIBLE);

		// Update the resumes variable in case it's changed.
		// If you touch this please tell fredrik about it.
		SharedPreferences resume = getSharedPreferences("resume", 0);
		resumes = resume.getInt("resumes", -1);
	}

	// Called when we get focus again (after a game has ended).
	@Override
	public void onRestart() {
		super.onRestart();

		mBackground.setImageResource(R.drawable.mainmenu);
		mBackground.setScaleType(ScaleType.FIT_XY);
		mTable.setVisibility(View.VISIBLE);

		// Update the resumes variable in case it's changed.
		// If you touch this please tell fredrik about it.
		SharedPreferences resume = getSharedPreferences("resume", 0);
		resumes = resume.getInt("resumes", -1);
	}

	/**
	 * get if this is the first run
	 * 
	 * @return returns true, if this is the first run
	 */
	public boolean getFirstRun(String versionNo) {
		String tmp = mPrefs.getString("firstRun", "1.00");
		return (!tmp.equals(versionNo));
	}

	/**
	 * store the first run
	 */
	public void setRunned(String versionNo) {
		SharedPreferences.Editor edit = mPrefs.edit();
		edit.putString("firstRun", versionNo);
		edit.commit();
	}

}