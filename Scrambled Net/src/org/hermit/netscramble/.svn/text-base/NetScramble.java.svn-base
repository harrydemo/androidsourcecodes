
/**
 * NetScramble: unscramble a network and connect all the terminals.
 * 
 * This is an Android implementation of the KDE game "knetwalk".
 * The player is given a network diagram with the parts of the network
 * randomly rotated; he/she must rotate them to connect all the terminals
 * to the server.
 * 
 * Original author:
 *   QNetwalk, Copyright (C) 2004, Andi Peredri <andi@ukr.net>
 *
 * Ported to kde by:
 *   Thomas Nagy <tnagyemail-mail@yahoo@fr>
 *
 * Cell-locking implemented by:
 *   Reinhold Kainhofer <reinhold@kainhofer.com>
 *
 * Ported to Android by:
 *   Ian Cameron Smith <johantheghost@yahoo.com>
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License version 2
 *   as published by the Free Software Foundation (see COPYING).
 * 
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 */


package org.hermit.netscramble;


import java.util.Formatter;

import org.hermit.android.AppUtils;
import org.hermit.android.InfoBox;
import org.hermit.netscramble.BoardView.Skill;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewAnimator;


/**
 * Main NetScramble activity.
 */
public class NetScramble
	extends Activity
{

	// ******************************************************************** //
	// Public Types.
	// ******************************************************************** //

    /**
     * Current state of the game.
     */
    static enum State { NEW, RESTORED, INIT,
    					PAUSED, HELP, RUNNING, SOLVED, ABORTED }

    /**
     * The sounds that we make.
     */
    static enum Sound {
    	START(R.raw.start),
    	CLICK(R.raw.click),
    	TURN(R.raw.turn),
    	CONNECT(R.raw.connect),
    	WIN(R.raw.win);
    	
    	private Sound(int res) {
    		soundRes = res;
    	}
    	
     	int soundRes;			// Resource ID for the sound file.
    }

    /**
     * Sound play mode.
     */
    static enum SoundMode {
    	NONE(R.id.sounds_off),
    	QUIET(R.id.sounds_qt),
    	FULL(R.id.sounds_on);
    	
    	private SoundMode(int res) {
    		menuId = res;
    	}
    	
    	int menuId;				// ID of the corresponding menu item.
    }

    
	// ******************************************************************** //
    // Activity Setup.
    // ******************************************************************** //

	/**
	 * Called when the activity is starting.  This is where most
	 * initialization should go: calling setContentView(int) to inflate
	 * the activity's UI, etc.
	 * 
	 * You can call finish() from within this function, in which case
	 * onDestroy() will be immediately called without any of the rest of
	 * the activity lifecycle executing.
	 * 
	 * Derived classes must call through to the super class's implementation
	 * of this method.  If they do not, an exception will be thrown.
	 * 
	 * @param	icicle			If the activity is being re-initialized
	 * 							after previously being shut down then this
	 * 							Bundle contains the data it most recently
	 * 							supplied in onSaveInstanceState(Bundle).
	 * 							Note: Otherwise it is null.
	 */
    @Override
    public void onCreate(Bundle icicle) {
        Log.i(TAG, "onCreate(): " +
        			(icicle == null ? "clean start" : "restart"));
        
        super.onCreate(icicle);
        
        appResources = getResources();
        
        gameTimer = new GameTimer();

        // We don't want a title bar.
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Load all the pixmaps for the game tiles etc.
        Cell.initPixmaps(appResources);

        // Restore our preferences.
    	SharedPreferences prefs = getPreferences(0);
    	
        // See if sounds are enabled and how.  The old way was boolean
    	// soundMode, so look for that as a fallback.
        soundMode = SoundMode.FULL;
        {
        	String smode = prefs.getString("soundMode", null);
        	if (smode != null)
        		soundMode = SoundMode.valueOf(smode);
        	else {
        		String son = prefs.getString("soundEnable", null);
        		if (son != null)
        			soundMode = Boolean.valueOf(son) ?
        									SoundMode.FULL : SoundMode.NONE;
        	}
        }
        
        // Create the GUI for the game.
        mainView = createGui();
        setContentView(mainView);

        AppUtils autils = new AppUtils(this);
        messageDialog = new InfoBox(this, R.string.button_close);
		messageDialog.setLinkButtons(new int[] {
				R.string.button_homepage,
				R.string.button_license
			},
			new int[] {
    			R.string.url_homepage,
    			R.string.url_license
		});
        String version = autils.getVersionString(AppUtils.Detail.SIMPLE);
		messageDialog.setTitle(version);

        // If we have a previous state to restore, try to do so.
        boolean restored = false;
        if (icicle != null)
        	restored = restoreState(icicle);
        
        // Get the current game skill level from the preferences, if we didn't
        // get a saved game.  Default to NOVICE if it's not there.
        if (!restored) {
        	gameSkill = null;
        	String skill = prefs.getString("skillLevel", null);
        	if (skill != null)
        		gameSkill = Skill.valueOf(skill);
        	if (gameSkill == null)
        		gameSkill = Skill.NOVICE;
        	gameState = State.NEW;
        } else {
        	// Save our restored game state.
        	restoredGameState = gameState;
        	gameState = State.RESTORED;
        }
    }

    
    /**
     * Called when the current activity is being re-displayed
     * to the user (the user has navigated back to it). It will be followed
     * by onStart(). 
     *
     * For activities that are using raw Cursor objects (instead of creating
     * them through managedQuery(android.net.Uri, String[], String, String[],
     * String), this is usually the place where the cursor should be requeried
     * (because you had deactivated it in onStop(). 
	 * 
	 * Derived classes must call through to the super class's implementation
	 * of this method.  If they do not, an exception will be thrown.
     */
    @Override
	protected void onRestart() {
        Log.i(TAG, "onRestart()");
        
    	super.onRestart();
    }
    
    
    /**
     * Called after onCreate(Bundle) or onStop() when the current activity is
     * now being displayed to the user.  It will be followed by onResume()
     * if the activity comes to the foreground, or onStop() if it becomes
     * hidden.
	 * 
	 * Derived classes must call through to the super class's implementation
	 * of this method.  If they do not, an exception will be thrown.
     */
    @Override
	protected void onStart() {
        Log.i(TAG, "onStart()");

    	super.onStart();
    }
    

    /**
     * This method is called after onStart() when the activity is being
     * re-initialized from a previously saved state, given here in state.
     * Most implementations will simply use onCreate(Bundle) to restore
     * their state, but it is sometimes convenient to do it here after
     * all of the initialization has been done or to allow subclasses
     * to decide whether to use your default implementation.  The default
     * implementation of this method performs a restore of any view
     * state that had previously been frozen by onSaveInstanceState(Bundle).
     * 
     * This method is called between onStart() and onPostCreate(Bundle).
     * 
	 * @param	omState			The data most recently supplied in
	 * 							onSaveInstanceState(Bundle).
     */
    @Override
    protected void onRestoreInstanceState(Bundle inState) {
        Log.i(TAG, "onRestoreInstanceState()");
        
        super.onRestoreInstanceState(inState);

        // Save the state.
        // saveState(outState);
    }
    
    
    /**
     * Called after onRestoreInstanceState(Bundle), onRestart(), or onPause(),
     * for your activity to start interacting with the user.  This is a good
     * place to begin animations, open exclusive-access devices (such as the
     * camera), etc.
     * 
     * Keep in mind that onResume is not the best indicator that your
     * activity is visible to the user; a system window such as the
     * keyguard may be in front.  Use onWindowFocusChanged(boolean) to know
     * for certain that your activity is visible to the user (for example,
     * to resume a game).
	 * 
	 * Derived classes must call through to the super class's implementation
	 * of this method.  If they do not, an exception will be thrown.
     */
    @Override
    protected void onResume() {
        Log.i(TAG, "onResume()");
        
        super.onResume();
        
        // Display the skill level.
		statusMid.setText(gameSkill.label);

        // If we restored a state, go to that state.  Otherwise start
        // at the welcome screen.
        if (gameState == State.NEW) {
            Log.d(TAG, "onResume() NEW: init");
        	setState(State.INIT);
        } else if (gameState == State.RESTORED) {
            Log.d(TAG, "onResume() RESTORED: set " + restoredGameState);
        	setState(restoredGameState);
        	
        	// If we restored an aborted state, that means we were starting
        	// a game.  Kick it off again.
        	if (restoredGameState == State.ABORTED) {
                Log.d(TAG, "onResume() RESTORED ABORTED: start");
        		startGame(null);
        	}
        } else if (gameState == State.PAUSED) {
        	// We just paused without closing down.  Resume.
    	    setState(State.RUNNING);
        } else {
            Log.d(TAG, "onResume() !!" + gameState + "!!: init");
    	    // setState(State.INIT);		// Shouldn't get here.
        }
    }


    /**
     * Called to retrieve per-instance state from an activity before being
     * killed so that the state can be restored in onCreate(Bundle) or
     * onRestoreInstanceState(Bundle) (the Bundle populated by this method
     * will be passed to both).
     * 
     * This method is called before an activity may be killed so that when
     * it comes back some time in the future it can restore its state.
     * 
     * Do not confuse this method with activity lifecycle callbacks such as
     * onPause(), which is always called when an activity is being placed in
     * the background or on its way to destruction, or onStop() which is
     * called before destruction.
     * 
     * The default implementation takes care of most of the UI per-instance
     * state for you by calling onSaveInstanceState() on each view in the
     * hierarchy that has an id, and by saving the id of the currently focused
     * view (all of which is restored by the default implementation of
     * onRestoreInstanceState(Bundle)).  If you override this method to save
     * additional information not captured by each individual view, you will
     * likely want to call through to the default implementation, otherwise
     * be prepared to save all of the state of each view yourself.
     * 
     * If called, this method will occur before onStop().  There are no
     * guarantees about whether it will occur before or after onPause().
	 * 
	 * @param	outState		A Bundle in which to place any state
	 * 							information you wish to save.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.i(TAG, "onSaveInstanceState()");
        
        super.onSaveInstanceState(outState);

        // Save the state.
        saveState(outState);
    }

    
    /**
     * Called as part of the activity lifecycle when an activity is going
     * into the background, but has not (yet) been killed.  The counterpart
     * to onResume(). 
     * 
     * When activity B is launched in front of activity A, this callback
     * will be invoked on A.  B will not be created until A's onPause()
     * returns, so be sure to not do anything lengthy here.
     * 
     * This callback is mostly used for saving any persistent state the
     * activity is editing, to present a "edit in place" model to the user
     * and making sure nothing is lost if there are not enough resources to
     * start the new activity without first killing this one.  This is also
     * a good place to do things like stop animations and other things
     * that consume a noticeable mount of CPU in order to make the
     * switch to the next activity as fast as possible, or to close
     * resources that are exclusive access such as the camera.
     * 
     * In situations where the system needs more memory it may kill paused
     * processes to reclaim resources.  Because of this, you should be sure
     * that all of your state is saved by the time you return from this
     * function.  In general onSaveInstanceState(Bundle) is used to save
     * per-instance state in the activity and this method is used to store
     * global persistent data (in content providers, files, etc.).
     * 
     * After receiving this call you will usually receive a following call
     * to onStop() (after the next activity has been resumed and displayed),
     * however in some cases there will be a direct call back to onResume()
     * without going through the stopped state. 
	 * 
	 * Derived classes must call through to the super class's implementation
	 * of this method.  If they do not, an exception will be thrown.
     */
    @Override
    protected void onPause() {
        Log.i(TAG, "onPause()");
        
        super.onPause();
        
        // Pause the game.
        if (gameState == State.RUNNING)
        	setState(State.PAUSED);
    }

    
    /**
     * Called when you are no longer visible to the user.  You will next
     * receive either onStart(), onDestroy(), or nothing, depending on
     * later user activity.
     * 
     * Note that this method may never be called, in low memory situations
     * where the system does not have enough memory to keep your activity's
     * process running after its onPause() method is called.
	 * 
	 * Derived classes must call through to the super class's implementation
	 * of this method.  If they do not, an exception will be thrown.
     */
    @Override
	protected void onStop() {
        Log.i(TAG, "onStop()");
        
    	super.onStop();
    }

    
    /**
     * Perform any final cleanup before an activity is destroyed.  This can
     * happen either because the activity is finishing (someone called
     * finish() on it, or because the system is temporarily destroying this
     * instance of the activity to save space.  You can distinguish between
     * these two scenarios with the isFinishing() method.
     * 
     * Note: do not count on this method being called as a place for saving
     * data!  For example, if an activity is editing data in a content
     * provider, those edits should be committed in either onPause() or
     * onSaveInstanceState(Bundle), not here.  This method is usually
     * implemented to free resources like threads that are associated with
     * an activity, so that a destroyed activity does not leave such things
     * around while the rest of its application is still running.  There
     * are situations where the system will simply kill the activity's
     * hosting process without calling this method (or any others) in it,
     * so it should not be used to do things that are intended to remain
     * around after the process goes away. 
	 * 
	 * Derived classes must call through to the super class's implementation
	 * of this method.  If they do not, an exception will be thrown.
     */
    @Override
	protected void onDestroy() {
        Log.i(TAG, "onDestroy()");
        
    	super.onDestroy();
    }
    
    
    // ******************************************************************** //
    // GUI Creation.
    // ******************************************************************** //

    /**
     * Create the GUI for the game.  We basically create a board which
     * fills the screen; but we also create a text display for status
     * messages, which covers the board when visible.
     * 
     * @return					The game GUI's top-level view.
     */
    private ViewGroup createGui() {
    	WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
    	Display disp = wm.getDefaultDisplay();
    	int width = disp.getWidth();
    	int height = disp.getHeight();
        
    	viewSwitcher = new ViewAnimator(this);
    	final int FPAR = LinearLayout.LayoutParams.FILL_PARENT;

    	// Make the animations for the switcher.
    	animSlideInLeft =
    			new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f,
				   			   		   Animation.RELATIVE_TO_SELF, 0.0f,
				   			   		   Animation.RELATIVE_TO_SELF, 0.0f,
				   			   		   Animation.RELATIVE_TO_SELF, 0.0f);
    	animSlideInLeft.setDuration(500);
    	animSlideOutLeft =
    			new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
    				   			   	   Animation.RELATIVE_TO_SELF, -1.0f,
    				   			   	   Animation.RELATIVE_TO_SELF, 0.0f,
    				   			   	   Animation.RELATIVE_TO_SELF, 0.0f);
    	animSlideOutLeft.setDuration(500);
    	animSlideInRight =
				new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.0f,
			   			   		   	   Animation.RELATIVE_TO_SELF, 0.0f,
			   			   		   	   Animation.RELATIVE_TO_SELF, 0.0f,
			   			   		   	   Animation.RELATIVE_TO_SELF, 0.0f);
    	animSlideInRight.setDuration(500);
    	animSlideOutRight =
				new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				   			   	   	   Animation.RELATIVE_TO_SELF, 1.0f,
				   			   	   	   Animation.RELATIVE_TO_SELF, 0.0f,
				   			   	   	   Animation.RELATIVE_TO_SELF, 0.0f);
    	animSlideOutRight.setDuration(500);

        // Add the board and status to the layout, filling all the space.
    	View playView = createPlayView(width > height);
        viewSwitcher.addView(playView, 0,
					   new ViewGroup.LayoutParams(FPAR, FPAR));

        // Add the splash screen to the top-level layout, filling the space.
        // This will hide the board when it is visible.
        splashText = createSplashScreen();
        viewSwitcher.addView(splashText, 1,
				       new ViewGroup.LayoutParams(FPAR, FPAR));

    	return viewSwitcher;
    }


    /**
     * Create the play view, containing the board and status bar.
     * 
     * @param	landscape		True if the layout is in landscape
     * 							orientation, false for portrait.
     * @return					The play view's top-level view.
     */
    private View createPlayView(boolean landscape) {
    	final int WCON = LinearLayout.LayoutParams.WRAP_CONTENT;
    	final int FPAR = LinearLayout.LayoutParams.FILL_PARENT;
    	final int HORI = LinearLayout.HORIZONTAL;
    	final int VERT = LinearLayout.VERTICAL;
    	
        // Set up our orientation and fill modes.
    	final int orient = landscape ? HORI : VERT;
    	final int orient2 = landscape ? VERT : HORI;
    	final int hfill = landscape ? WCON : FPAR;
    	final int vfill = landscape ? FPAR : WCON;

        // Create a layout to hold the board and status bar.
    	LinearLayout boardWrapper = new LinearLayout(this);
        boardWrapper.setOrientation(orient);

        // Construct the board, and add it to the layout.
        boardView = new BoardView(this);
        boardView.setBackgroundColor(Color.BLACK);
        boardWrapper.addView(boardView,
        					 new LinearLayout.LayoutParams(FPAR, FPAR, 1));
        
        // Add a status bar after the board.
        boardWrapper.addView(createStatusBar(orient2),
        					 new LinearLayout.LayoutParams(hfill, vfill));
        
        return boardWrapper;
    }
    
    
    /**
     * Create the status bar, containing two status fields.
     * 
     * @return					The status bar's top-level view.
     */
    private View createStatusBar(int orientation) {
        // Add a status bar under the board.
        LinearLayout statusWrapper = new LinearLayout(this);
        statusWrapper.setBackgroundColor(Color.BLACK);
        statusWrapper.setOrientation(orientation);
        
        // Create the left status field.
        statusLeft = new TextView(this);
        statusLeft.setGravity(Gravity.LEFT);
        statusLeft.setText("00");
        LinearLayout.LayoutParams lpl = new LinearLayout.LayoutParams(
        		LinearLayout.LayoutParams.WRAP_CONTENT,
        		LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        statusWrapper.addView(statusLeft, lpl);

        // Create the center status field.
        statusMid = new TextView(this);
        statusMid.setGravity(Gravity.CENTER_HORIZONTAL);
        statusMid.setText("");
        LinearLayout.LayoutParams lpm = new LinearLayout.LayoutParams(
        		LinearLayout.LayoutParams.WRAP_CONTENT,
        		LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        statusWrapper.addView(statusMid, lpm);
        
        // Create the right status field.
        statusRight = new TextView(this);
        statusRight.setGravity(Gravity.RIGHT);
        clicksText = new StringBuilder(10);
        timeText = new StringBuilder(10);
        timeFormatter = new Formatter(timeText);
        LinearLayout.LayoutParams lpr = new LinearLayout.LayoutParams(
        		LinearLayout.LayoutParams.WRAP_CONTENT,
        		LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        statusWrapper.addView(statusRight, lpr);

        return statusWrapper;
    }


    /**
     * Create the splash screen, which is just a big text view.
     * 
     * @return					The splash screen's top-level view.
     */
    private TextView createSplashScreen() {
        // Create the splash text view.  Set it up to call wakeUp() when
        // the user presses a key or taps the screen.
    	TextView text = new TextView(this) {
        	// Handle taps/clicks.
        	@Override
            public boolean onTouchEvent(MotionEvent event) {
            	if (event.getAction() == MotionEvent.ACTION_DOWN) {
    				wakeUp();
            		return true;
            	} else
            		return false;
            }
        	// Handle key presses.
        	@Override
            public boolean onKeyDown(int keyCode, KeyEvent event) {
        		wakeUp();
        		return true;
        	}
        };
        text.setGravity(Gravity.CENTER);
        text.setBackgroundColor(Color.WHITE);
        text.setTextColor(Color.argb(255, 100, 100, 255));
        text.setTextSize(14f);
        text.setText("Wait...");

        return text;
    }
    
        
    // ******************************************************************** //
    // Menu Management.
    // ******************************************************************** //
    
    /**
     * Initialize the contents of the game's options menu by adding items
     * to the given menu.
     * 
     * This is only called once, the first time the options menu is displayed.
     * To update the menu every time it is displayed, see
     * onPrepareOptionsMenu(Menu).
     * 
     * When we add items to the menu, we can either supply a Runnable to
     * receive notification of selection, or we can implement the Activity's
     * onOptionsItemSelected(Menu.Item) method to handle them there.
     * 
     * @param	menu			The options menu in which we should
     * 							place our items.  We can safely hold on this
     * 							(and any items created from it), making
     * 							modifications to it as desired, until the next
     * 							time onCreateOptionsMenu() is called.
     * @return					true for the menu to be displayed; false
     * 							to suppress showing it.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	mainMenu = menu;
    	
    	// We must call through to the base implementation.
    	super.onCreateOptionsMenu(menu);
    	
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        // GUI is created, state is restored (if any) -- now is a good time
        // to re-sync the options menus.
        selectCurrentSkill();
        selectSoundMode();

        return true;
    }
    
    
    private void selectCurrentSkill() {
        // Set the selected skill menu item to the current skill.
    	if (mainMenu != null) {
    		MenuItem skillItem = mainMenu.findItem(gameSkill.id);
    		if (skillItem != null)
    			skillItem.setChecked(true);
    	}
    }


    private void selectSoundMode() {
        // Set the sound enable menu item to the current state.
    	if (mainMenu != null) {
    		int id = soundMode.menuId;
    		MenuItem soundItem = mainMenu.findItem(id);
    		if (soundItem != null)
    			soundItem.setChecked(true);
    	}
    }

    
    /**
     * This hook is called whenever an item in your options menu is selected.
     * Derived classes should call through to the base class for it to
     * perform the default menu handling.  (True?)
     *
     * @param	item			The menu item that was selected.
     * @return					false to have the normal processing happen.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case R.id.menu_new:
    		startGame(null);
    		break;
    	case R.id.menu_pause:
    		setState(State.PAUSED);
    		break;
    	case R.id.menu_help:
    		setState(State.HELP);
    		break;
    	case R.id.menu_about:
 			messageDialog.show(R.string.about_text);
    		break;
    	case R.id.skill_novice:
    		startGame(Skill.NOVICE);
    		break;
    	case R.id.skill_normal:
    		startGame(Skill.NORMAL);
    		break;
    	case R.id.skill_expert:
    		startGame(Skill.EXPERT);
    		break;
    	case R.id.skill_master:
    		startGame(Skill.MASTER);
    		break;
    	case R.id.skill_insane:
    		startGame(Skill.INSANE);
    		break;
    	case R.id.sounds_off:
    		setSoundMode(SoundMode.NONE);
    		break;
    	case R.id.sounds_qt:
    		setSoundMode(SoundMode.QUIET);
    		break;
    	case R.id.sounds_on:
    		setSoundMode(SoundMode.FULL);
    		break;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    	
    	return true;
    }
    
	
    private void setSoundMode(SoundMode mode) {
		soundMode = mode;
		
		// Save the new setting to prefs.
        SharedPreferences prefs = getPreferences(0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("soundMode", "" + soundMode);
        editor.commit();
        
        selectSoundMode();
    }
    
    
    // ******************************************************************** //
    // Game progress.
    // ******************************************************************** //

    /**
     * This method is called each time the user clicks a cell.
     */
    void cellClicked(Cell cell) {
    	// Count the click, but only if this isn't a repeat click on the
    	// same cell.  This is because the tap interface only rotates
    	// clockwise, and it's not fair to count an anti-clockwise
    	// turn as 3 clicks.
    	if (cell != prevClickedCell) {
    		++clickCount;
    		updateStatus();
    		prevClickedCell = cell;
    	}
    }
    
    
    // ******************************************************************** //
    // Game Control Functions.
    // ******************************************************************** //

    /**
     * Wake up: the user has clicked the splash screen, so continue.
     */
    private void wakeUp() {
    	// If we are paused, just go to running.  Otherwise (in the
    	// welcome or game over screen), start a new game.
        if (gameState == State.PAUSED || gameState == State.HELP)
        	setState(State.RUNNING);
        else
        	startGame(null);
    }


	// Create a listener for the user starting the game.
	private final DialogInterface.OnClickListener startGameListener =
								new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface arg0, int arg1) {
			setState(State.RUNNING);
		}
	};
	
	
    /**
     * Start a game at a given skill level, or the previous skill level.
     * The skill level chosen is saved to the preferences and becomes
     * the default for next time.
     * 
     * @param	sk			Skill level to start at; if null, use the
     * 						previous skill from the preferences.
     */
    public void startGame(BoardView.Skill sk) {
    	// Abort any existing game, so we know we're not just continuing.
		setState(State.ABORTED);

    	// Sort out the previous and new skills.  If we aren't
    	// given a new skill, default to previous.
    	BoardView.Skill prevSkill = gameSkill;
    	gameSkill = sk != null ? sk : prevSkill;
    	
    	// Save the new skill setting in the prefs.
        SharedPreferences prefs = getPreferences(0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("skillLevel", gameSkill.toString());
        editor.commit();

        // Set the selected skill menu item to the current skill.
        selectCurrentSkill();
		statusMid.setText(gameSkill.label);

        // OK, now get going!
        Log.i(TAG, "startGame: " + gameSkill + " (was " + prevSkill + ")");

		// If we're going up to master or insane level, set up a message
        // to display the user to show him/her the new rules.
    	int msg = 0;
		if (prevSkill != BoardView.Skill.INSANE) {
	        if (gameSkill == BoardView.Skill.INSANE)
	        	msg = R.string.help_insane;
	        else if (gameSkill == BoardView.Skill.MASTER &&
	        		 prevSkill != BoardView.Skill.MASTER)
	        	msg = R.string.help_master;
		}
		
		// If we have a help message to show, show it; the dialog will
		// start the game (and hence the clock) when the user is ready.
		// Otherwise, start the game now.
		if (msg != 0)
			new AlertDialog.Builder(this)
				.setMessage(msg)
				.setPositiveButton(R.string.button_ok, startGameListener)
				.show();
		else
			setState(State.RUNNING);
    }

    
    /**
     * This method is called by the board view when the network is
     * solved.
     * 
     * If we allow the user to tinker after finishing, this may be called
     * each time the user gets to a connected net.
     */
    void gameSolved() {
		setState(State.SOLVED);

        makeSound(Sound.WIN);
        
		// TODO: log the score: board size, 3600 - gameClock / 1000, clickCount
    }

    
	// ******************************************************************** //
	// Game State.
	// ******************************************************************** //

    /**
     * Set the game state.  Set the screen display and start/stop the
     * clock as appropriate.
     * 
     * @param	state		The state to go into.
     */
    void setState(State state) {
        Log.i(TAG, "setState: " + state + " (was " + gameState + ")");

    	// If we're not changing state, don't bother.
    	if (state == gameState)
    		return;
    	
        // Save the previous state, and change.
        State prev = gameState;
        gameState = state;

        // Handle the state change.
        switch (gameState) {
        case NEW:
        case RESTORED:
        	// Should never get these.
            break;
        case INIT:
            gameTimer.stop();
        	showSplashText(R.string.splash_text);
            break;
        case SOLVED:
            gameTimer.stop();
            boardView.setSolved();

        	// We allow the user to keep playing after it's over, but
        	// don't keep reporting wins.
        	if (prev != State.SOLVED)
        		reportWin(boardView.unconnectedCells());
        	break;
        case ABORTED:
        	// Aborted is followed by something else,
        	// so don't display anything.
            gameTimer.stop();
        	break;
        case PAUSED:
            gameTimer.stop();
        	showSplashText(R.string.pause_text);
            break;
        case HELP:
            gameTimer.stop();
        	showSplashText(R.string.help_text);
            break;
        case RUNNING:
            // Set us going, if this is a new game.
        	if (prev != State.RESTORED &&
        					prev != State.PAUSED && prev != State.HELP) {
                boardView.setupBoard(gameSkill);
        		clickCount = 0;
        		prevClickedCell = null;
        		gameTimer.reset();
        		updateStatus();
        		makeSound(Sound.START);
        	}
        	hideSplashText();
            gameTimer.start();
            break;
        }
    }


	// Create a listener for the user starting a new game.
	private final DialogInterface.OnClickListener newGameListener =
								new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface arg0, int arg1) {
			startGame(null);
		}
	};
	
	
	/**
	 * Report that the user has won the game.  Let the user continue to
	 * play with the layout, or start a new game.
     * 
     * @param	unused			The number of unused cells.  Normally zero,
     * 							but it's sometimes possible to solve the
     * 							board without using all the cable bits.
 	 */
    private void reportWin(int unused) {
    	// Format the win message.
		long time = gameTimer.getTime();
		String msg;
		
		if (unused != 0) {
			CharSequence fmt = appResources.getText(R.string.win_spares_text);
			msg = String.format((String) fmt,
							    time / 60000, time / 1000 % 60,
							    clickCount, unused);
		} else {
			CharSequence fmt = appResources.getText(R.string.win_text);
			msg = String.format((String) fmt, time / 60000,
								time / 1000 % 60, clickCount);
		}
		
		// Display the dialog.
		new AlertDialog.Builder(this)
			.setTitle(R.string.win_title)
			.setMessage(msg)
			.setPositiveButton(R.string.win_new, newGameListener)
			.setNegativeButton(R.string.win_continue, null)
			.show();
    }

    
    // ******************************************************************** //
    // Status Display.
    // ******************************************************************** //

	/**
     * Update the status line to the current game state.
     */
	void updateStatus() {
		// Use StringBuilders and a Formatter to avoid allocating new
		// String objects every time -- this function is called often!
		
		clicksText.setLength(0);
		clicksText.append(clickCount);
		statusLeft.setText(clicksText);
		
		timeText.setLength(0);
		long time = gameTimer.getTime();
		timeFormatter.format("%02d:%02d", time / 60000, time / 1000 % 60);
		statusRight.setText(timeText);
	}


    /**
     * Set the status text to the given text message.  This hides the
     * game board.
     * 
     * @param	msgId			Resource ID of the message to set.
     */
	void showSplashText(int msgId) {
		splashText.setText(msgId);
		if (viewSwitcher.getDisplayedChild() != 1) {
	    	viewSwitcher.setInAnimation(animSlideInRight);
	    	viewSwitcher.setOutAnimation(animSlideOutRight);
			viewSwitcher.setDisplayedChild(1);
		}
		
		// Any key dismisses it, so we need focus.  TODO: doesn't work.
		splashText.requestFocus();
	}


    /**
     * Hide the status text, revealing the board.
     */
	void hideSplashText() {
		if (viewSwitcher.getDisplayedChild() != 0) {
	    	viewSwitcher.setInAnimation(animSlideInLeft);
	    	viewSwitcher.setOutAnimation(animSlideOutLeft);
			viewSwitcher.setDisplayedChild(0);
		}
	}
	

	/**
	 * Make a sound.
	 * 
	 * @param	which			ID of the sound to play.
     */
	void makeSound(Sound which) {
		if (soundMode == SoundMode.NONE)
			return;

		try {
			MediaPlayer mp = MediaPlayer.create(this, which.soundRes);
			mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
				public void onPrepared(MediaPlayer mp) { mp.start(); }
			});
			mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
				public void onCompletion(MediaPlayer mp) { mp.release(); }
			});
			
			float vol = 1.0f;
			if (soundMode == SoundMode.QUIET)
				vol = 0.3f;
			mp.setVolume(vol, vol);
			
			mp.prepareAsync();
		} catch (Exception e) {
			Log.d(TAG, e.toString());
		}
	}
	

    // ******************************************************************** //
    // State Save/Restore.
    // ******************************************************************** //

    /**
     * Save game state so that the user does not lose anything
     * if the game process is killed while we are in the 
     * background.
     * 
	 * @param	outState		A Bundle in which to place any state
	 * 							information we wish to save.
     */
    private void saveState(Bundle outState) {
    	// Save the skill level and game state.
    	outState.putString("gameSkill", gameSkill.toString());
    	outState.putString("gameState", gameState.toString());
    	
    	// Save the game state of the board.
        boardView.saveState(outState);

    	// Restore the game timer and click count.
    	gameTimer.saveState(outState);
    	outState.putInt("clickCount", clickCount);
    }

    
    /**
     * Restore our game state from the given Bundle.
     * 
     * @param	map			A Bundle containing the saved state.
     * @return				true if the state was restored OK; false
     * 						if the saved state was incompatible with the
     * 						current configuration.
     */
    private boolean restoreState(Bundle map) {
    	// Get the skill level and game state.
    	gameSkill = Skill.valueOf(map.getString("gameSkill"));
    	gameState = State.valueOf(map.getString("gameState"));

    	// Restore the state of the game board.
    	boolean restored = boardView.restoreState(map, gameSkill);

    	// Restore the game timer and click count.
    	if (restored) {
    		restored = gameTimer.restoreState(map, false);
    		clickCount = map.getInt("clickCount");
    	}

        return restored;
    }
    
    
    // ******************************************************************** //
    // Private Types.
    // ******************************************************************** //

	// This class implements the game clock.  All it does is update the
    // status each tick.
	private final class GameTimer extends Timer {
		
		GameTimer() {
    		// Tick every 0.25 s.
    		super(250);
    	}
		
    	@Override
		protected boolean step(int count, long time) {
    		updateStatus();
            
            // Run until explicitly stopped.
            return false;
        }
        
	}
	
	
    // ******************************************************************** //
    // Class Data.
    // ******************************************************************** //

    // Debugging tag.
	private static final String TAG = "netscramble";

    
    // ******************************************************************** //
    // Member Data.
    // ******************************************************************** //

	// The app's resources.
	private Resources appResources;
	
	// The top-level view.
	private ViewGroup mainView;
	
    // The game board.
    private BoardView boardView = null;

    // The status bar, consisting of 3 status fields.
    private TextView statusLeft;
    private TextView statusMid;
    private TextView statusRight;

    // Text buffers used to format the click count and time.  We allocate
    // these here, so we don't allocate new String objects every time
    // we update the status -- which is very often.
    private StringBuilder clicksText;
    private StringBuilder timeText;
    private Formatter timeFormatter;
    
    // The text widget used to display status messages.  When visible,
    // it covers the board.
	private TextView splashText = null;
	
	// View switcher used to switch between the splash text and
	// board view.
	private ViewAnimator viewSwitcher = null;

	// Animations for the view switcher.
	private TranslateAnimation animSlideInLeft;
	private TranslateAnimation animSlideOutLeft;
	private TranslateAnimation animSlideInRight;
	private TranslateAnimation animSlideOutRight;

	// The menu used to select the skill level.  We keep this so we can
	// set the selected item.
	private Menu mainMenu;
	
	// The state of the current game.
	private State gameState;

	// When gameState == State.RESTORED, this is our restored game state.
	private State restoredGameState;

	// The currently selected skill level.
	private BoardView.Skill gameSkill;
	
	// Timer used to time the game.
	private GameTimer gameTimer;

	// True to enable sounds.
	private SoundMode soundMode;
	
	// Number of times the user has clicked.
	private int clickCount = 0;
	
	// The previous cell that was clicked.  Used to detect multiple clicks
	// on the same cell.
	private Cell prevClickedCell = null;

	// Dialog used to display about etc.
	private InfoBox messageDialog;

}

