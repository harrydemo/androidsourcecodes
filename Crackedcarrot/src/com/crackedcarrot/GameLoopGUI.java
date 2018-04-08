package com.crackedcarrot;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.crackedcarrot.UI.UIHandler;
import com.crackedcarrot.fileloader.Level;
import com.crackedcarrot.menu.R;

/*
 * 
 * This class builds on the GameInit-class with the purpose of containing all the
 * required GUI elements for the GameLoop-class, for cleanliness/readability.
 * 
 */

public class GameLoopGUI {

	private GameInit gameInit;

	private Dialog dialog = null;
	private Dialog dialogNextLevel = null;
	private Dialog dialogPause = null;
	private Dialog dialogQuit = null;
	private Dialog dialogHelp = null;

	// //////////////////////////////////////
	// MULTIPLAYER SCOREBOARD
	// //////////////////////////////////////
	private Dialog dialogSCOREBOARD = null;
	private TextView o_waiting;
	private TextView o_health;
	private TextView o_score;
	private TextView o_enemies_left;
	private TextView y_health;
	private TextView y_score;
	private TextView y_enemies_left;
	private Button ScoreBoardButton;
	private int opponentScore;
	private int opponentEnLeft;
	private int opponentHealthLeft;
	public boolean multiplayerMode;
	// //////////////////////////////////////
	// MULTIPLAYER SCOREBOARD end
	// //////////////////////////////////////

	public boolean quitDialogPressed = false;

	private int healthBarState = 3;
	private int healthProgress = 100;
	// private int resume;
	private int playerScore;
	public int towerInfo;
	private boolean showFullHelp = false;

	private WebView mWebView; // used by TowerInfo-dialog to display html-pages.
	private Drawable healthBarDrawable;
	private ImageView enImView;
	private LinearLayout statusBar;
	private LinearLayout creatureBar;
	private LinearLayout counterBar;
	private ProgressBar healthProgressBar;
	private TextView currencyView;
	private TextView nrCreText;
	private TextView counterText;
	private TextView playerHealthView;
	private TextView scoreCounter;
	private TextView lvlNbr;
	private UIHandler hud;

	// Used when we ask for the instruction view
	private int currentSelectedTower;

	// For readability-reasons.
	public final int DIALOG_NEXTLEVEL_ID = 1;
	final int DIALOG_QUIT_ID = 5;
	public final int DIALOG_HELP = 6;
	final int DIALOG_PAUSE_ID = 7;
	public final int MULTIPLAYER_SCOREBOARD = 8;
	public final int MULTIPLAYER_SCOREBOARD_UPDATE_ENEMIES = 9;
	public final int MULTIPLAYER_SCOREBOARD_UPDATE_HEALTH = 10;
	public final int MULTIPLAYER_SCOREBOARD_UPDATE_END = 11;
	public final int MULTIPLAYER_SCOREBOARD_WAITING = 12;
	public final int MULTIPLAYER_SCOREBOARD_CLOSE = 13;
	public final int MULTIPLAYER_SCOREBOARD_UPDATE_ENEMIES_SURVIVAL = 14;
	public final int GUI_PLAYERMONEY_ID = 20;
	public final int GUI_PLAYERHEALTH_ID = 21;
	public final int GUI_CREATUREVIEW_ID = 22;
	final int GUI_CREATURELEFT_ID = 23;
	public final int GUI_PROGRESSBAR_ID = 24;
	public final int GUI_NEXTLEVELINTEXT_ID = 25;
	public final int GUI_SHOWSTATUSBAR_ID = 26;
	public final int GUI_SHOWHEALTHBAR_ID = 27;
	public final int GUI_HIDEHEALTHBAR_ID = 28;
	public final int GUI_UPDATELVLNBRTEXT_ID = 29;
	public final int SETMULTIPLAYERVISIBLE = 30;
	public final int GUI_HIDECREATUREDATA_ID = 31;
	public final int GUI_SHOWSHIELDBUTTON = 32;
	public final int GUI_TELEPORTSUCCESS = 33;
	public final int GUI_CREATURESURVIVAL_ID = 34;

	final Button towerbutton1;
	final Button towerbutton2;
	final Button towerbutton3;
	final Button towerbutton4;
	final LinearLayout towertext;

	final LinearLayout towerUpgrade;
	final Button sellTower;
	final Button closeUpgrade;
	final Button upgradeLvl2;
	final Button upgradeLvl3;
	final Button upgradeFire1;
	final Button upgradeFire2;
	final Button upgradeFire3;
	final Button upgradeFire4;
	final Button upgradeFire5;
	final Button upgradeFrost1;
	final Button upgradeFrost2;
	final Button upgradeFrost3;
	final Button upgradeFrost4;
	final Button upgradeFrost5;
	final Button upgradePoison1;
	final Button upgradePoison2;
	final Button upgradePoison3;
	final Button upgradePoison4;
	final Button upgradePoison5;
	final Button upgradeSpecial;

	final LinearLayout towerstats;
	final TextView towerDamage;
	final TextView towerLvl;
	final TextView towerSpecial;
	final ImageView icon_lvl;
	final ImageView icon_fire;
	final ImageView icon_frost;
	final ImageView icon_poison;
	final ImageView icon_special;

	final Button tower1Information;
	final Button tower2Information;
	final Button tower3Information;
	final Button tower4Information;

	final Button forward;
	final Button play;

	final ExpandMenu expandMenu;
	final Button lessHealthButton;
	final Button enemyFastButton;
	final Button destroyTowerButton;
	final Button makeElementalButton;
	final Button makeShieldButton;

	// Constructor. A good place to initiate all our different GUI-components.
	public GameLoopGUI(GameInit gi, final UIHandler hud, boolean multiplayerMode) {
		gameInit = gi;
		this.hud = hud;
		this.multiplayerMode = multiplayerMode;

		towerUpgrade = (LinearLayout) gameInit
				.findViewById(R.id.upgrade_layout);
		upgradeLvl2 = (Button) gameInit.findViewById(R.id.upgrade_lvl2);
		upgradeLvl3 = (Button) gameInit.findViewById(R.id.upgrade_lvl3);
		upgradeFire1 = (Button) gameInit.findViewById(R.id.upgrade_fire1);
		upgradeFire2 = (Button) gameInit.findViewById(R.id.upgrade_fire2);
		upgradeFire3 = (Button) gameInit.findViewById(R.id.upgrade_fire3);
		upgradeFire4 = (Button) gameInit.findViewById(R.id.upgrade_fire4);
		upgradeFire5 = (Button) gameInit.findViewById(R.id.upgrade_fire5);
		upgradeFrost1 = (Button) gameInit.findViewById(R.id.upgrade_frost1);
		upgradeFrost2 = (Button) gameInit.findViewById(R.id.upgrade_frost2);
		upgradeFrost3 = (Button) gameInit.findViewById(R.id.upgrade_frost3);
		upgradeFrost4 = (Button) gameInit.findViewById(R.id.upgrade_frost4);
		upgradeFrost5 = (Button) gameInit.findViewById(R.id.upgrade_frost5);
		upgradePoison1 = (Button) gameInit.findViewById(R.id.upgrade_poison1);
		upgradePoison2 = (Button) gameInit.findViewById(R.id.upgrade_poison2);
		upgradePoison3 = (Button) gameInit.findViewById(R.id.upgrade_poison3);
		upgradePoison4 = (Button) gameInit.findViewById(R.id.upgrade_poison4);
		upgradePoison5 = (Button) gameInit.findViewById(R.id.upgrade_poison5);
		upgradeSpecial = (Button) gameInit.findViewById(R.id.upgrade_special);

		towerstats = (LinearLayout) gameInit.findViewById(R.id.towerstats);
		towerDamage = (TextView) gameInit.findViewById(R.id.towerDamage);
		towerLvl = (TextView) gameInit.findViewById(R.id.towerLvl);
		towerSpecial = (TextView) gameInit.findViewById(R.id.towerSpecial);

		icon_lvl = (ImageView) gameInit.findViewById(R.id.icon_lvl);
		icon_fire = (ImageView) gameInit.findViewById(R.id.icon_fire);
		icon_frost = (ImageView) gameInit.findViewById(R.id.icon_frost);
		icon_poison = (ImageView) gameInit.findViewById(R.id.icon_poison);
		icon_special = (ImageView) gameInit.findViewById(R.id.icon_special);

		sellTower = (Button) gameInit.findViewById(R.id.sell);
		closeUpgrade = (Button) gameInit.findViewById(R.id.close_upgrade);

		towertext = (LinearLayout) gameInit.findViewById(R.id.ttext);
		towerbutton1 = (Button) gameInit.findViewById(R.id.t1);
		towerbutton2 = (Button) gameInit.findViewById(R.id.t2);
		towerbutton3 = (Button) gameInit.findViewById(R.id.t3);
		towerbutton4 = (Button) gameInit.findViewById(R.id.t4);

		// Tower information. Clicking this will open information about this
		// tower
		tower1Information = (Button) gameInit.findViewById(R.id.t1info);
		tower1Information.setOnClickListener(new InfoListener());
		// Tower information. Clicking this will open information about this
		// tower
		tower2Information = (Button) gameInit.findViewById(R.id.t2info);
		tower2Information.setOnClickListener(new InfoListener());
		// Tower information. Clicking this will open information about this
		// tower
		tower3Information = (Button) gameInit.findViewById(R.id.t3info);
		tower3Information.setOnClickListener(new InfoListener());
		// Tower information. Clicking this will open information about this
		// tower
		tower4Information = (Button) gameInit.findViewById(R.id.t4info);
		tower4Information.setOnClickListener(new InfoListener());

		// Create an pointer to the statusbar
		statusBar = (LinearLayout) gameInit.findViewById(R.id.status_menu);
		// Create an pointer to creatureBar
		creatureBar = (LinearLayout) gameInit.findViewById(R.id.creature_part);
		// Create an pointer to counterBar
		counterBar = (LinearLayout) gameInit.findViewById(R.id.counter);

		// Create the TextView showing number of enemies left
		nrCreText = (TextView) gameInit.findViewById(R.id.nrEnemyLeft);
		Typeface MuseoSans = Typeface.createFromAsset(gameInit.getAssets(),
				"fonts/MuseoSans_500.otf");
		nrCreText.setTypeface(MuseoSans);

		// Create the TextView showing counter
		counterText = (TextView) gameInit.findViewById(R.id.countertext);
		counterText.setTypeface(MuseoSans);

		// Create the TextView showing current level
		lvlNbr = (TextView) gameInit.findViewById(R.id.lvlNumber);
		lvlNbr.setTypeface(MuseoSans);

		// And the score Counter.
		scoreCounter = (TextView) gameInit.findViewById(R.id.scoreCounter);
		scoreCounter.setTypeface(MuseoSans);

		// Create the progress bar, showing the enemies total health
		healthProgressBar = (ProgressBar) gameInit
				.findViewById(R.id.health_progress);
		healthProgressBar.setMax(healthProgress);
		healthProgressBar.setProgress(healthProgress);
		healthBarDrawable = healthProgressBar.getProgressDrawable();
		healthBarDrawable.setColorFilter(Color.parseColor("#339900"),
				PorterDuff.Mode.MULTIPLY);

		// Create the ImageView showing current creature
		enImView = (ImageView) gameInit.findViewById(R.id.enemyImVi);

		// Create the text view showing the amount of currency
		currencyView = (TextView) gameInit.findViewById(R.id.currency);
		currencyView.setTypeface(MuseoSans);

		// Create the text view showing a players health
		playerHealthView = (TextView) gameInit.findViewById(R.id.playerHealth);
		playerHealthView.setTypeface(MuseoSans);

		// Create the expandable menu in multiplayer mode
		expandMenu = (ExpandMenu) gameInit.findViewById(R.id.expandable_menu);
		lessHealthButton = (Button) gameInit.findViewById(R.id.less_health);
		enemyFastButton = (Button) gameInit.findViewById(R.id.enemy_fast);
		destroyTowerButton = (Button) gameInit.findViewById(R.id.destroy_tower);
		makeElementalButton = (Button) gameInit
				.findViewById(R.id.make_elemental);
		makeShieldButton = (Button) gameInit.findViewById(R.id.make_shield);

		forward = (Button) gameInit.findViewById(R.id.forward);
		play = (Button) gameInit.findViewById(R.id.play);
		final Button expandMenuButton = (Button) gameInit
				.findViewById(R.id.expand_menu);

		if (multiplayerMode) {
			expandMenuButton.setVisibility(View.VISIBLE);
		} else {
			forward.setVisibility(View.VISIBLE);
		}

		/**
		 * Listeners for the nine icons in the in-game menu. When clicked on,
		 * it's possible to place a tower on an empty space on the map. The
		 * first button is the normal/fast switcher.
		 */
		expandMenuButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				expandMenu.switchMenu();
			}
		});

		lessHealthButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (gameInit.gLoop.decreaseOppLife()) {
					CharSequence text = "Item purchased.";
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(getGameInit(), text, duration);
					toast.show();
					expandMenu.switchMenu();
					if (!gameInit.gLoop.survivalGame)
						lessHealthButton.setVisibility(View.INVISIBLE); // set
																		// visible
																		// when
																		// new
																		// level
				} else {
					// Not enough money, show in the menu below
					CharSequence text = "Not enough money";
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(getGameInit(), text, duration);
					toast.show();
				}

			}
		});

		enemyFastButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (gameInit.gLoop.increaseEnemySpeed()) {
					CharSequence text = "Item purchased.";
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(getGameInit(), text, duration);
					toast.show();
					expandMenu.switchMenu();
					if (!gameInit.gLoop.survivalGame)
						enemyFastButton.setVisibility(View.INVISIBLE); // set
																		// visible
																		// when
																		// new
																		// level
				} else {
					// Not enough money, show in the menu below
					CharSequence text = "Not enough money";
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(getGameInit(), text, duration);
					toast.show();
				}

			}
		});

		destroyTowerButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (gameInit.gLoop.destroyTower()) {
					CharSequence text = "Item purchased.";
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(getGameInit(), text, duration);
					toast.show();
					expandMenu.switchMenu();
					if (!gameInit.gLoop.survivalGame)
						destroyTowerButton.setVisibility(View.INVISIBLE); // set
																			// visible
																			// when
																			// new
																			// level
				} else {
					// Not enough money, show in the menu below
					CharSequence text = "Not enough money";
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(getGameInit(), text, duration);
					toast.show();
				}

			}
		});

		makeElementalButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (gameInit.gLoop.makeElemental()) {
					CharSequence text = "Item purchased.";
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(getGameInit(), text, duration);
					toast.show();
					expandMenu.switchMenu();
					if (!gameInit.gLoop.survivalGame)
						makeElementalButton.setVisibility(View.INVISIBLE); // set
																			// visible
																			// when
																			// new
																			// level
				} else {
					// Not enough money, show in the menu below
					CharSequence text = "Not enough money";
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(getGameInit(), text, duration);
					toast.show();
				}

			}
		});

		makeShieldButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (gameInit.gLoop.makeShield()) {
					CharSequence text = "Item purchased.";
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(getGameInit(), text, duration);
					toast.show();
					expandMenu.switchMenu();
					makeShieldButton.setVisibility(View.INVISIBLE); // set
																	// visible
																	// when new
																	// level
				} else {
					// Not enough money, show in the menu below
					CharSequence text = "Not enough money";
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(getGameInit(), text, duration);
					toast.show();
				}

			}
		});

		forward.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				forward.setVisibility(View.GONE);
				gameInit.gameLoop.setGameSpeed(3);
				play.setVisibility(View.VISIBLE);
			}
		});

		play.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				play.setVisibility(View.GONE);
				gameInit.gameLoop.setGameSpeed(1);
				forward.setVisibility(View.VISIBLE);
			}
		});

		closeUpgrade.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// Log.d("GUI", "Close Upgrade clicked!");
				gameInit.hudHandler.hideRangeIndicator();
				hideTowerUpgrade();
			}
		});

		towerbutton1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// A tower of type 1 has been chosen, where to put it?
				gameInit.mGLSurfaceView.setTowerType(0);
				openTowerBuildMenu(0);
				hud.showGrid();
			}
		});
		towerbutton2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// A tower of type 2 has been chosen, where to put it?
				gameInit.mGLSurfaceView.setTowerType(1);
				openTowerBuildMenu(1);
				hud.showGrid();
			}
		});
		towerbutton3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// A tower of type 3 has been chosen, where to put it?
				gameInit.mGLSurfaceView.setTowerType(2);
				openTowerBuildMenu(2);
				hud.showGrid();
			}
		});
		towerbutton4.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// A tower of type 4 has been chosen, where to put it?
				gameInit.mGLSurfaceView.setTowerType(3);
				openTowerBuildMenu(3);
				hud.showGrid();
			}
		});

		// Button that removes towerInformation
		final Button inMenu6 = (Button) gameInit.findViewById(R.id.inmenu6);
		inMenu6.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				gameInit.hudHandler.hideRangeIndicator();

				gameInit.mGLSurfaceView.setTowerType(-1);
				towertext.setVisibility(View.GONE);
				towerbutton1.setVisibility(View.VISIBLE);
				towerbutton2.setVisibility(View.VISIBLE);
				towerbutton3.setVisibility(View.VISIBLE);
				towerbutton4.setVisibility(View.VISIBLE);
				hud.hideGrid();
			}
		});

		if (this.multiplayerMode) {
			// This needs to be initalized before we try using any of it.
			dialogSCOREBOARD = new Dialog(gameInit, R.style.InGameMenu);
			dialogSCOREBOARD.setContentView(R.layout.multiplayer_scoreboard);
			// dialogWait.setCancelable(false);

			o_waiting = (TextView) dialogSCOREBOARD
					.findViewById(R.id.o_waiting);
			o_health = (TextView) dialogSCOREBOARD.findViewById(R.id.o_health);
			o_score = (TextView) dialogSCOREBOARD.findViewById(R.id.o_score);
			o_enemies_left = (TextView) dialogSCOREBOARD
					.findViewById(R.id.o_enemies_left);
			y_health = (TextView) dialogSCOREBOARD.findViewById(R.id.y_health);
			y_score = (TextView) dialogSCOREBOARD.findViewById(R.id.y_score);
			y_enemies_left = (TextView) dialogSCOREBOARD
					.findViewById(R.id.y_enemies_left);
			ScoreBoardButton = (Button) dialogSCOREBOARD
					.findViewById(R.id.ScoreBoardButton);
		}

	}

	/*
	 * Creates all of our dialogs.
	 * 
	 * Note: This functions is only called ONCE for each dialog. If you need a
	 * dynamic dialog this code does NOT go here!
	 */
	protected Dialog onCreateDialog(int id) {

		WindowManager.LayoutParams lp;

		switch (id) {

		case DIALOG_NEXTLEVEL_ID:
			dialogNextLevel = new Dialog(gameInit, R.style.NextlevelTheme);
			dialogNextLevel.setContentView(R.layout.nextlevel);
			dialogNextLevel.setCancelable(true);
			// Info button
			Button infoButton = (Button) dialogNextLevel
					.findViewById(R.id.infobutton2);
			infoButton.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					showFullHelp = true;
					gameInit.showDialog(DIALOG_HELP);
				}
			});

			// A button
			Button butt = (Button) dialogNextLevel
					.findViewById(R.id.NextLevelButton);
			Typeface face = Typeface.createFromAsset(gameInit.getAssets(),
					"fonts/Sniglet.ttf");
			butt.setTypeface(face);
			butt.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					dialogNextLevel.dismiss();

				}
			});

			// Title of each level:
			TextView title = (TextView) dialogNextLevel
					.findViewById(R.id.NextLevelTitle);
			title.setTypeface(face);

			// Text for next level will be placed here.
			TextView text = (TextView) dialogNextLevel
					.findViewById(R.id.NextLevelText);
			face = Typeface.createFromAsset(gameInit.getAssets(),
					"fonts/MuseoSans_500.otf");
			text.setTypeface(face);

			dialogNextLevel
					.setOnDismissListener(new DialogInterface.OnDismissListener() {
						public void onDismiss(DialogInterface dialog) {
							gameInit.gameLoop.dialogClick();
						}
					});
			return dialogNextLevel;
			// break;

		case DIALOG_HELP:
			dialogHelp = new Dialog(gameInit, R.style.NextlevelTheme);
			dialogHelp.setContentView(R.layout.webinstruction);
			dialogHelp.setCancelable(true);

			Button close = (Button) dialogHelp
					.findViewById(R.id.closewebdialog);
			close.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					// nothing else. handled by onDismissListener instead, it's
					// better.
					dialogHelp.dismiss();
				}
			});

			final Button back = (Button) dialogHelp
					.findViewById(R.id.backwebdialog);
			back.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					mWebView.goBack();
				}
			});

			dialogHelp
					.setOnDismissListener(new DialogInterface.OnDismissListener() {
						public void onDismiss(DialogInterface dialog) {
							// Done with this window, unpause stuff.
							mWebView.clearView();
							if (!showFullHelp)
								GameLoop.unPause();
							showFullHelp = false;
						}
					});

			mWebView = (WebView) dialogHelp.findViewById(R.id.webview);
			mWebView.setBackgroundColor(0);

			WebSettings webSettings = mWebView.getSettings();
			webSettings.setSavePassword(false);
			webSettings.setSaveFormData(false);
			webSettings.setJavaScriptEnabled(false);
			webSettings.setSupportZoom(false);

			mWebView.setWebViewClient(new WebViewClient() {
				public void onPageFinished(WebView view, String url) {
					if (!showFullHelp
							|| mWebView.getUrl().equals(
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
			return dialogHelp;

		case DIALOG_QUIT_ID:
			dialogQuit = new Dialog(gameInit, R.style.NextlevelTheme);
			dialogQuit.setContentView(R.layout.levelquit);
			dialogQuit.setCancelable(true);

			Typeface faceSniglet = Typeface.createFromAsset(
					gameInit.getAssets(), "fonts/Sniglet.ttf");

			// Text
			TextView quitTv = (TextView) dialogQuit
					.findViewById(R.id.LevelQuit_Text);
			quitTv.setTypeface(faceSniglet);

			// First button
			Button quitYes = (Button) dialogQuit
					.findViewById(R.id.LevelQuit_Yes);
			quitYes.setTypeface(faceSniglet);
			quitYes.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					quitDialogPressed = true;
					gameInit.finish();
				}
			});

			// Second button
			Button quitNo = (Button) dialogQuit.findViewById(R.id.LevelQuit_No);
			quitNo.setTypeface(faceSniglet);
			quitNo.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					dialogQuit.dismiss();
				}
			});

			// Dismiss-listener
			dialogQuit
					.setOnDismissListener(new DialogInterface.OnDismissListener() {
						public void onDismiss(DialogInterface dialog) {
							// do nothing.
						}
					});

			lp = dialogQuit.getWindow().getAttributes();
			dialogQuit.getWindow().setAttributes(lp);
			dialogQuit.getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_DIM_BEHIND);

			return dialogQuit;
			// break;

		case DIALOG_PAUSE_ID:
			dialogPause = new Dialog(gameInit, R.style.InGameMenu);
			dialogPause.setContentView(R.layout.levelpause);
			dialogPause.setCancelable(true);

			// Continue button
			Button buttonPauseContinue = (Button) dialogPause
					.findViewById(R.id.LevelPause_Continue);
			face = Typeface.createFromAsset(gameInit.getAssets(),
					"fonts/Sniglet.ttf");
			buttonPauseContinue.setTypeface(face);

			buttonPauseContinue.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dialogPause.dismiss();
				}
			});

			// Sound on/off button
			final Button buttonPauseSound = (Button) dialogPause
					.findViewById(R.id.LevelPause_Sound);
			face = Typeface.createFromAsset(gameInit.getAssets(),
					"fonts/Sniglet.ttf");
			buttonPauseSound.setTypeface(face);
			buttonPauseSound.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					String pausetext = "Sound: ";
					if (gameInit.gameLoop.soundManager.playSound) {
						gameInit.gameLoop.soundManager.playSound = false;
						pausetext += "<font color=red>off</font>";
					} else {
						gameInit.gameLoop.soundManager.playSound = true;
						pausetext += "<font color=green>on</font>";
					}
					CharSequence chS = Html.fromHtml(pausetext);
					buttonPauseSound.setText(chS);

				}
			});
			// And update the text to match the current setting.
			String pausetext = "Sound: ";
			if (gameInit.gameLoop.soundManager.playSound)
				pausetext += "<font color=green>on</font>";
			else
				pausetext += "<font color=red>off</font>";
			CharSequence chS = Html.fromHtml(pausetext);
			buttonPauseSound.setText(chS);

			// Help button
			Button buttonPauseHelp = (Button) dialogPause
					.findViewById(R.id.LevelPause_Help);
			face = Typeface.createFromAsset(gameInit.getAssets(),
					"fonts/Sniglet.ttf");
			buttonPauseHelp.setTypeface(face);

			if (!multiplayerMode) {
				buttonPauseHelp.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						showFullHelp = true;
						gameInit.showDialog(DIALOG_HELP);
					}
				});
			} else {
				buttonPauseHelp.setText("Scoreboard");
				buttonPauseHelp.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						dialogPause.dismiss();
						dialogSCOREBOARD.show();
					}
				});
			}

			// Quit button
			Button buttonPauseQuit = (Button) dialogPause
					.findViewById(R.id.LevelPause_Quit);
			face = Typeface.createFromAsset(gameInit.getAssets(),
					"fonts/Sniglet.ttf");
			buttonPauseQuit.setTypeface(face);
			buttonPauseQuit.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					gameInit.showDialog(DIALOG_QUIT_ID);
				}
			});

			// Dismiss-listener
			dialogPause
					.setOnDismissListener(new DialogInterface.OnDismissListener() {
						public void onDismiss(DialogInterface dialog) {
							GameLoop.unPause();
						}
					});

			// Makes the background of the dialog blurred.
			if (!multiplayerMode) {
				lp = dialogPause.getWindow().getAttributes();
				dialogPause.getWindow().setAttributes(lp);
				dialogPause.getWindow().setFlags(
						WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
						WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
			}

			return dialogPause;

		case MULTIPLAYER_SCOREBOARD:

			o_health.setText("Health: " + gameInit.gameLoop.player.getHealth());
			o_score.setText("Score: 0");
			y_score.setText("Score: 0");
			y_health.setText("Health: " + gameInit.gameLoop.player.getHealth());

			if (gameInit.gLoop.survivalGame) {
				o_enemies_left.setText("Kills: 0");
				y_enemies_left.setText("Kills: 0");
			} else {
				o_enemies_left.setText("Enemies left: "
						+ gameInit.gLoop.mLvl[0].nbrCreatures);
				y_enemies_left.setText("Enemies left: "
						+ gameInit.gLoop.mLvl[0].nbrCreatures);
			}

			dialogSCOREBOARD
					.setOnDismissListener(new DialogInterface.OnDismissListener() {
						public void onDismiss(DialogInterface dialog) {
							dialogSCOREBOARD.dismiss();
						}
					});

			// End game
			ScoreBoardButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					quitDialogPressed = false;
					gameInit.gameLoop.dialogClick();
					gameInit.gameLoop.stopGameLoop();
				}
			});

			return dialogSCOREBOARD;

		default:
			// Log.d("GAMEINIT", "onCreateDialog got unknown dialog id: " + id);
			dialog = null;
		}
		return dialog;
	}

	/*
	 * Creates our NextLevel-dialog.
	 * 
	 * This is called every time a dialog is presented. If you want dynamic
	 * dialogs, put your code here.
	 */
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case DIALOG_NEXTLEVEL_ID:

			int currLvlnbr = gameInit.gameLoop.getLevelNumber() + 1;

			Level currLvl = gameInit.gameLoop.getLevelData();

			// Title:
			TextView title = (TextView) dialog
					.findViewById(R.id.NextLevelTitle);
			String titleText = "Level " + currLvlnbr + "<br>"
					+ currLvl.creepTitle + "<br>";
			CharSequence styledText = Html.fromHtml(titleText);
			title.setText(styledText);

			// And an icon.
			ImageView image = (ImageView) dialog
					.findViewById(R.id.NextLevelImage);
			image.setImageResource(currLvl.getDisplayResourceId());

			// Text for next level goes here.
			TextView text = (TextView) dialog.findViewById(R.id.NextLevelText);

			Player currPlayer = gameInit.gameLoop.getPlayerData();
			String lvlText = "Number of creeps: " + currLvl.nbrCreatures
					+ "<br>";
			lvlText += "Bounty: " + currLvl.goldValue + "g/creep<br>";
			lvlText += "Health: " + (int) currLvl.getHealth() + "hp/creep<br>";
			lvlText += "<br>";
			lvlText += "Special abilities:<br>";
			int tmpAbil = 0;
			if (currLvl.creatureFast) {
				lvlText += "<font color=0xFF00FF>Fast level</font><br>";
				tmpAbil++;
			}
			if (currLvl.creatureFireResistant) {
				lvlText += "<font color=red>Fire resistant</font><br>";
				tmpAbil++;
			}
			if (currLvl.creatureFrostResistant) {
				lvlText += "<font color=blue>Frost resistant</font><br>";
				tmpAbil++;
			}
			if (currLvl.creaturePoisonResistant) {
				lvlText += "<font color=green>Poision resistant</font><br>";
				tmpAbil++;
			}
			if (tmpAbil == 0)
				lvlText += "No special abilities<br>";

			if (currLvlnbr > 1) {
				lvlText += "<br>Previous level:<br>";
				lvlText += "Interest gained:"
						+ currPlayer.getInterestGainedThisLvl() + "<br>";
				lvlText += "Health lost:" + currPlayer.getHealthLostThisLvl();
			} else {
				lvlText += "<br>Tip:<br>";
				lvlText += "If you have trouble<br>playing the game<br>use the information<br>button below or ingame.";
			}
			styledText = Html.fromHtml(lvlText);
			text.setText(styledText);

			if (currLvl.creaturePoisonResistant
					&& !currLvl.creatureFireResistant
					&& !currLvl.creatureFrostResistant) {
				image.setColorFilter(Color.rgb(178, 255, 178),
						PorterDuff.Mode.MULTIPLY);
			} else if (!currLvl.creaturePoisonResistant
					&& currLvl.creatureFireResistant
					&& !currLvl.creatureFrostResistant) {
				image.setColorFilter(Color.rgb(255, 178, 178),
						PorterDuff.Mode.MULTIPLY);
			} else if (!currLvl.creaturePoisonResistant
					&& !currLvl.creatureFireResistant
					&& currLvl.creatureFrostResistant) {
				image.setColorFilter(Color.rgb(178, 178, 255),
						PorterDuff.Mode.MULTIPLY);
			} else if (currLvl.creaturePoisonResistant
					&& currLvl.creatureFrostResistant
					&& !currLvl.creatureFireResistant) {
				image.setColorFilter(Color.rgb(178, 255, 255),
						PorterDuff.Mode.MULTIPLY);
			} else if (currLvl.creaturePoisonResistant
					&& !currLvl.creatureFrostResistant
					&& currLvl.creatureFireResistant) {
				image.setColorFilter(Color.rgb(255, 255, 178),
						PorterDuff.Mode.MULTIPLY);
			} else if (!currLvl.creaturePoisonResistant
					&& currLvl.creatureFrostResistant
					&& currLvl.creatureFireResistant) {
				image.setColorFilter(Color.rgb(255, 178, 255),
						PorterDuff.Mode.MULTIPLY);
			} else
				image.setColorFilter(Color.rgb(255, 255, 255),
						PorterDuff.Mode.MULTIPLY);
			break;
		case DIALOG_PAUSE_ID:
			final Button buttonPauseSound = (Button) dialogPause
					.findViewById(R.id.LevelPause_Sound);
			// And update the image to match the current setting.
			String pausetext = "Sound: ";
			if (gameInit.gameLoop.soundManager.playSound)
				pausetext += "<font color=green>on</font>";
			else
				pausetext += "<font color=red>off</font>";
			CharSequence chS;
			chS = Html.fromHtml(pausetext);
			buttonPauseSound.setText(chS);
			break;

		case DIALOG_HELP:
			// Fetch information from previous intent. The information will
			// contain the
			// tower decided by the player.
			String url = null;
			int tower = towerInfo;

			if (showFullHelp)
				url = "file:///android_asset/instructions.html";
			else {
				if (tower == 0)
					url = "file:///android_asset/t1.html";
				if (tower == 1)
					url = "file:///android_asset/t2.html";
				if (tower == 2)
					url = "file:///android_asset/t3.html";
				if (tower == 3)
					url = "file:///android_asset/t4.html";
			}

			mWebView.loadUrl(url);
			break;

		default:
			// Log.d("GAMEINIT", "onPrepareDialog got unknown dialog id: " +
			// id);
			dialog = null;
		}
	}

	// This is used to handle calls from the GameLoop to show
	// our dialogs.
	public Handler guiHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case DIALOG_NEXTLEVEL_ID:
				SharedPreferences settings1 = gameInit.getSharedPreferences(
						"Options", 0);
				if (settings1.getBoolean("optionsNextLevel", true)
						&& !GameInit.multiplayergame) {
					// Log.d("GAMELOOPGUI", "Start next level dialog");
					gameInit.showDialog(DIALOG_NEXTLEVEL_ID);
				} else {
					// Simulate clicking the dialog.
					// Log.d("GAMELOOPGUI", "Simulate next level dialog");
					gameInit.gameLoop.dialogClick();
				}
				break;

			case GUI_PLAYERMONEY_ID:
				// Update currencyView (MONEY) and score.
				scoreCounter.setText(""
						+ String.format("%08d",
								gameInit.gameLoop.player.getScore()));
				currencyView.setText("" + msg.arg1);
				if (multiplayerMode) {
					y_score.setText("Score: "
							+ gameInit.gameLoop.player.getScore());
				}
				break;
			case GUI_PLAYERHEALTH_ID:
				// Update player-health. and score.
				scoreCounter.setText(""
						+ String.format("%08d",
								gameInit.gameLoop.player.getScore()));
				playerHealthView.setText("" + msg.arg1);
				if (multiplayerMode) {
					y_health.setText("Health: " + msg.arg1);
					y_score.setText("Score: "
							+ gameInit.gameLoop.player.getScore());
				}
				break;
			case GUI_CREATUREVIEW_ID:
				// Update Enemy-ImageView
				enImView.setImageResource(msg.arg1);
				break;
			case GUI_CREATURELEFT_ID:
				// update number of creatures still alive on GUI.
				String tt = String.valueOf(msg.arg1);
				nrCreText.setText("" + tt);
				if (multiplayerMode) {
					y_enemies_left.setText("Enemies left: " + tt);
				}
				break;
			case GUI_PROGRESSBAR_ID: // update progressbar with creatures
										// health.
				// The code below is used to change color of healthbar when
				// health drops
				if (msg.arg1 >= 66 && healthBarState == 1) {
					healthBarDrawable.setColorFilter(
							Color.parseColor("#339900"),
							PorterDuff.Mode.MULTIPLY);
					healthBarState = 3;
				}
				if (msg.arg1 <= 66 && healthBarState == 3) {
					healthBarDrawable.setColorFilter(
							Color.parseColor("#FFBB00"),
							PorterDuff.Mode.MULTIPLY);
					healthBarState = 2;
				}
				if (msg.arg1 <= 33 && healthBarState == 2) {
					healthBarDrawable.setColorFilter(
							Color.parseColor("#CC0000"),
							PorterDuff.Mode.MULTIPLY);
					healthBarState = 1;
				}
				healthProgressBar.setProgress(msg.arg1);
				break;

			case GUI_NEXTLEVELINTEXT_ID: // This is used to show how long time
											// until next lvl.
				tt = String.valueOf(msg.arg1);
				counterText.setText("Next level in: " + tt);
				break;
			case GUI_UPDATELVLNBRTEXT_ID: // This is used to show level number.
				tt = String.valueOf(msg.arg1);
				lvlNbr.setText("Level: " + tt);
				break;
			case GUI_SHOWSTATUSBAR_ID:
				// Show statusbar
				statusBar.setVisibility(View.VISIBLE);
				break;
			case GUI_SHOWHEALTHBAR_ID:
				// If we want to switch back to healthbar
				counterBar.setVisibility(View.GONE);
				creatureBar.setVisibility(View.VISIBLE);
				break;
			case GUI_HIDEHEALTHBAR_ID:
				// If we want to use space in statusbar to show time to next
				// level counter
				creatureBar.setVisibility(View.GONE);
				counterBar.setVisibility(View.VISIBLE);
				break;
			case GUI_HIDECREATUREDATA_ID:
				// If we play a survival game nonen of the following bars are of
				// use
				creatureBar.setVisibility(View.GONE);
				counterText.setText("");
				break;
			case GUI_CREATURESURVIVAL_ID:
				tt = String.valueOf(msg.arg1);
				counterText.setText("Kills: " + tt);
				if (multiplayerMode) {
					y_enemies_left.setText("Kills: " + tt);
					y_score.setText("Score: "
							+ gameInit.gameLoop.player.getScore());
				}
				break;
			case MULTIPLAYER_SCOREBOARD_UPDATE_ENEMIES:
				opponentScore = msg.arg1;
				opponentEnLeft = msg.arg2;
				o_score.setText("Score: " + opponentScore);
				o_enemies_left.setText("Enemies left: " + opponentEnLeft);
				break;
			case MULTIPLAYER_SCOREBOARD_UPDATE_ENEMIES_SURVIVAL:
				opponentScore = msg.arg1;
				opponentEnLeft = msg.arg2;
				o_score.setText("Score: " + opponentScore);
				o_enemies_left.setText("Kills: " + opponentEnLeft);
				break;
			case MULTIPLAYER_SCOREBOARD_UPDATE_HEALTH:
				opponentScore = msg.arg1;
				opponentHealthLeft = msg.arg2;
				o_score.setText("Score: " + opponentScore);
				o_health.setText("Health: " + opponentHealthLeft);
				break;

			case MULTIPLAYER_SCOREBOARD_UPDATE_END:
				int tmp = msg.arg1;
				if (tmp == 2) {
					o_waiting.setText("You win!");
				} else if (tmp == 1) {
					o_waiting.setText("You lose!");
				} else {
					String winLoose;
					// Is player score better than opponents, if so player is
					// the winner
					if (playerScore > opponentScore)
						winLoose = "You Win!";
					else if (playerScore < opponentScore)
						winLoose = "You Lose!";
					else
						winLoose = "It's a tie!";
					o_waiting.setText(winLoose);
				}

				ScoreBoardButton.setVisibility(View.VISIBLE);

				break;

			case MULTIPLAYER_SCOREBOARD_WAITING:
				o_waiting.setText("Waiting for opponent...");
				break;

			case MULTIPLAYER_SCOREBOARD_CLOSE:
				o_waiting.setText("");
				dialogSCOREBOARD.dismiss();
				break;

			case MULTIPLAYER_SCOREBOARD:
				gameInit.showDialog(MULTIPLAYER_SCOREBOARD);
				break;

			case SETMULTIPLAYERVISIBLE:
				lessHealthButton.setVisibility(View.VISIBLE);
				enemyFastButton.setVisibility(View.VISIBLE);
				destroyTowerButton.setVisibility(View.VISIBLE);
				makeElementalButton.setVisibility(View.VISIBLE);
				makeShieldButton.setVisibility(View.VISIBLE);
				break;
			case GUI_SHOWSHIELDBUTTON:
				makeShieldButton.setVisibility(View.VISIBLE);
				break;
			case GUI_TELEPORTSUCCESS:
				CharSequence text = "A enemy has been teleported back to spawnpoint";
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(getGameInit(), text, duration);
				toast.show();
				break;
			case -1: // GAME IS DONE, CLOSE ACTIVITY.
				gameInit.finish();
				break;
			case -2: // SAVE THE GAME.
						// arg 1 = save game, 2 = remove saved game.
				gameInit.saveGame(msg.arg1);
				break;

			default:
				// Log.e("GAMELOOPGUI", "guiHandler error! msg.what: " +
				// msg.what);
				break;
			}
		}
	};

	public void sendMessage(int i, int j, int k) {

		// TODO: remove this when done debugging msgs.
		// // Log.d("GAMELOOPGUI", "sendMessage: " + i);

		Message msg = Message.obtain();
		msg.what = i;
		msg.arg1 = j;
		msg.arg2 = k;
		guiHandler.sendMessage(msg);
	}

	private void openTowerBuildMenu(int towerId) {
		if (towerId == 0) {
			tower1Information.setVisibility(View.VISIBLE);
			tower2Information.setVisibility(View.GONE);
			tower3Information.setVisibility(View.GONE);
			tower4Information.setVisibility(View.GONE);
		} else if (towerId == 1) {
			tower1Information.setVisibility(View.GONE);
			tower2Information.setVisibility(View.VISIBLE);
			tower3Information.setVisibility(View.GONE);
			tower4Information.setVisibility(View.GONE);
		} else if (towerId == 2) {
			tower1Information.setVisibility(View.GONE);
			tower2Information.setVisibility(View.GONE);
			tower3Information.setVisibility(View.VISIBLE);
			tower4Information.setVisibility(View.GONE);
		} else if (towerId == 3) {
			tower1Information.setVisibility(View.GONE);
			tower2Information.setVisibility(View.GONE);
			tower3Information.setVisibility(View.GONE);
			tower4Information.setVisibility(View.VISIBLE);
		}
		this.currentSelectedTower = towerId;
		towerbutton1.setVisibility(View.GONE);
		towerbutton2.setVisibility(View.GONE);
		towerbutton3.setVisibility(View.GONE);
		towerbutton4.setVisibility(View.GONE);
		towertext.setVisibility(View.VISIBLE);
	}

	/** Method used to get the GameInit object from the multiplayer handler */
	public GameInit getGameInit() {
		return this.gameInit;
	}

	public void showTowerUpgrade(int showLevelUpgrade, int LevelPrice,
			int showFireUpgrade, int FirePrice, int showFrostUpgrade,
			int FrostPrice, int showPoisonUpgrade, int PoisonPrice,
			int showSpecialUpgrade, int SpecialPrice, int recellValue,
			int minDamage, int maxDamage, boolean aoeTower) {

		this.upgradeFire1.setVisibility(View.GONE);
		this.upgradeFire2.setVisibility(View.GONE);
		this.upgradeFire3.setVisibility(View.GONE);
		this.upgradeFire4.setVisibility(View.GONE);
		this.upgradeFire5.setVisibility(View.GONE);
		this.upgradeFrost1.setVisibility(View.GONE);
		this.upgradeFrost2.setVisibility(View.GONE);
		this.upgradeFrost3.setVisibility(View.GONE);
		this.upgradeFrost4.setVisibility(View.GONE);
		this.upgradeFrost5.setVisibility(View.GONE);
		this.upgradePoison1.setVisibility(View.GONE);
		this.upgradePoison2.setVisibility(View.GONE);
		this.upgradePoison3.setVisibility(View.GONE);
		this.upgradePoison4.setVisibility(View.GONE);
		this.upgradePoison5.setVisibility(View.GONE);
		this.upgradeLvl2.setVisibility(View.GONE);
		this.upgradeLvl3.setVisibility(View.GONE);
		this.upgradeSpecial.setVisibility(View.GONE);

		this.sellTower.setText("+" + recellValue);

		switch (showLevelUpgrade) {
		case (1):
			this.upgradeLvl2.setText("-" + LevelPrice);
			this.upgradeLvl2.setVisibility(View.VISIBLE);
			break;
		case (2):
			this.upgradeLvl3.setText("-" + LevelPrice);
			this.upgradeLvl3.setVisibility(View.VISIBLE);
			break;
		}

		switch (showFireUpgrade) {
		case (0):
			this.upgradeFire1.setText("-" + FirePrice);
			this.upgradeFire1.setVisibility(View.VISIBLE);
			break;
		case (1):
			this.upgradeFire2.setText("-" + FirePrice);
			this.upgradeFire2.setVisibility(View.VISIBLE);
			break;
		case (2):
			this.upgradeFire3.setText("-" + FirePrice);
			this.upgradeFire3.setVisibility(View.VISIBLE);
			break;
		case (3):
			this.upgradeFire4.setText("-" + FirePrice);
			this.upgradeFire4.setVisibility(View.VISIBLE);
			break;
		case (4):
			this.upgradeFire5.setText("-" + FirePrice);
			this.upgradeFire5.setVisibility(View.VISIBLE);
			break;
		}

		switch (showFrostUpgrade) {
		case (0):
			this.upgradeFrost1.setText("-" + FrostPrice);
			this.upgradeFrost1.setVisibility(View.VISIBLE);
			break;
		case (1):
			this.upgradeFrost2.setText("-" + FrostPrice);
			this.upgradeFrost2.setVisibility(View.VISIBLE);
			break;
		case (2):
			this.upgradeFrost3.setText("-" + FrostPrice);
			this.upgradeFrost3.setVisibility(View.VISIBLE);
			break;
		case (3):
			this.upgradeFrost4.setText("-" + FrostPrice);
			this.upgradeFrost4.setVisibility(View.VISIBLE);
			break;
		case (4):
			this.upgradeFrost5.setText("-" + FrostPrice);
			this.upgradeFrost5.setVisibility(View.VISIBLE);
			break;
		}

		switch (showPoisonUpgrade) {
		case (0):
			this.upgradePoison1.setText("-" + PoisonPrice);
			this.upgradePoison1.setVisibility(View.VISIBLE);
			break;
		case (1):
			this.upgradePoison2.setText("-" + PoisonPrice);
			this.upgradePoison2.setVisibility(View.VISIBLE);
			break;
		case (2):
			this.upgradePoison3.setText("-" + PoisonPrice);
			this.upgradePoison3.setVisibility(View.VISIBLE);
			break;
		case (3):
			this.upgradePoison4.setText("-" + PoisonPrice);
			this.upgradePoison4.setVisibility(View.VISIBLE);
			break;
		case (4):
			this.upgradePoison5.setText("-" + PoisonPrice);
			this.upgradePoison5.setVisibility(View.VISIBLE);
			break;
		}

		if (showSpecialUpgrade == 1) {
			this.upgradeSpecial.setText("-" + SpecialPrice);
			this.upgradeSpecial.setVisibility(View.VISIBLE);
		}

		towerDamage.setText(minDamage + "-" + maxDamage);
		towerLvl.setText("" + showLevelUpgrade);
		if (showLevelUpgrade == -1)
			towerLvl.setText("3");
		else
			towerLvl.setText("" + showLevelUpgrade);

		this.icon_fire.setVisibility(View.GONE);
		this.icon_frost.setVisibility(View.GONE);
		this.icon_poison.setVisibility(View.GONE);
		this.icon_special.setVisibility(View.GONE);
		this.towerSpecial.setText("");

		if (showFrostUpgrade > 0) {
			this.icon_frost.setVisibility(View.VISIBLE);
			this.towerSpecial.setText("" + showFrostUpgrade);
		} else if (showPoisonUpgrade > 0) {
			this.icon_poison.setVisibility(View.VISIBLE);
			this.towerSpecial.setText("" + showPoisonUpgrade);
		} else if (showFireUpgrade > 0) {
			this.icon_fire.setVisibility(View.VISIBLE);
			this.towerSpecial.setText("" + showFireUpgrade);
		} else if (showSpecialUpgrade == 2 || showSpecialUpgrade == 3) {
			this.icon_special.setVisibility(View.VISIBLE);
		}

		if (aoeTower)
			this.icon_poison.setVisibility(View.VISIBLE);

		gameInit.mGLSurfaceView.setTowerType(-1);
		hud.hideGrid();
		this.towertext.setVisibility(View.GONE);
		towerbutton1.setVisibility(View.GONE);
		towerbutton2.setVisibility(View.GONE);
		towerbutton3.setVisibility(View.GONE);
		towerbutton4.setVisibility(View.GONE);
		play.setVisibility(View.GONE);
		forward.setVisibility(View.GONE);

		towerUpgrade.setVisibility(View.VISIBLE);
	}

	public void hideTowerUpgrade() {

		towerUpgrade.setVisibility(View.GONE);

		if (gameInit.gameLoop.gameSpeed == 1) {
			forward.setVisibility(View.VISIBLE);
		} else {
			play.setVisibility(View.VISIBLE);
		}

		towerbutton1.setVisibility(View.VISIBLE);
		towerbutton2.setVisibility(View.VISIBLE);
		towerbutton3.setVisibility(View.VISIBLE);
		towerbutton4.setVisibility(View.VISIBLE);

	}

	public void setUpgradeListeners(OnClickListener upgradeTowerLvlListener,
			OnClickListener upgradeFireListener,
			OnClickListener upgradeFrostListener,
			OnClickListener upgradePoisonListener,
			OnClickListener sellListener, OnClickListener upgradeSpecialListener) {

		this.upgradeLvl2.setOnClickListener(upgradeTowerLvlListener);
		this.upgradeLvl3.setOnClickListener(upgradeTowerLvlListener);
		this.upgradeFire1.setOnClickListener(upgradeFireListener);
		this.upgradeFire2.setOnClickListener(upgradeFireListener);
		this.upgradeFire3.setOnClickListener(upgradeFireListener);
		this.upgradeFire4.setOnClickListener(upgradeFireListener);
		this.upgradeFire5.setOnClickListener(upgradeFireListener);
		this.upgradeFrost1.setOnClickListener(upgradeFrostListener);
		this.upgradeFrost2.setOnClickListener(upgradeFrostListener);
		this.upgradeFrost3.setOnClickListener(upgradeFrostListener);
		this.upgradeFrost4.setOnClickListener(upgradeFrostListener);
		this.upgradeFrost5.setOnClickListener(upgradeFrostListener);
		this.upgradePoison1.setOnClickListener(upgradePoisonListener);
		this.upgradePoison2.setOnClickListener(upgradePoisonListener);
		this.upgradePoison3.setOnClickListener(upgradePoisonListener);
		this.upgradePoison4.setOnClickListener(upgradePoisonListener);
		this.upgradePoison5.setOnClickListener(upgradePoisonListener);
		this.sellTower.setOnClickListener(sellListener);
		this.upgradeSpecial.setOnClickListener(upgradeSpecialListener);
	}

	public void NotEnoughMoney() {
		CharSequence text = "Not enough money";
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(this.getGameInit(), text, duration);
		toast.show();
		hud.blinkRedRange();
	}

	private class InfoListener implements OnClickListener {
		public void onClick(View v) {
			if (GameLoop.pause == false) {
				if (!multiplayerMode)
					GameLoop.pause();
				towerInfo = currentSelectedTower;
				gameInit.showDialog(DIALOG_HELP);
			}
		}
	}

}