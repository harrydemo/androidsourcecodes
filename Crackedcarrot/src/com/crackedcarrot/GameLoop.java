package com.crackedcarrot;

import java.util.concurrent.Semaphore;

import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.crackedcarrot.fileloader.Level;
import com.crackedcarrot.fileloader.Map;
import com.crackedcarrot.menu.R;
import com.crackedcarrot.textures.TextureData;

/**
 * A runnable that updates the position of each creature and projectile every
 * frame by applying a simple movement simulation. Also keeps track of player
 * life, level count etc.
 */
public class GameLoop implements Runnable {

	public NativeRender renderHandle;
	public SoundManager soundManager; // We need to reach this to be able to
										// turn off sound.

	protected GameLoopGUI gui;
	protected Scaler mScaler;
	protected Semaphore dialogSemaphore = new Semaphore(1);

	protected Map mGameMap;
	protected Player player;

	protected boolean run = true;

	protected long mLastTime;

	private String resumeTowers = "";

	protected Coords selectedTower;

	protected float startCreatureHealth;
	protected float currentCreatureHealth;

	protected int lvlNbr = 0;
	protected int gameSpeed;
	protected int remainingCreaturesALIVE;
	protected int remainingCreaturesALL;
	protected int totalNumberOfTowers = 0;

	protected Creature[] mCreatures;
	protected Level[] mLvl;
	protected Shot[] mShots;
	protected Tower[] mTower;
	protected Tower[][] mTowerGrid;
	protected Tower[] mTTypes;
	protected Sprite[] mSpecialTowers;
	// Tracker for finding creatures
	private Tracker gameTracker;

	public int progressbarLastSent = 0;

	protected static boolean pause = false;
	protected static Semaphore pauseSemaphore = new Semaphore(0);

	private boolean superupgrade_teleport = false;
	private boolean superupgrade_element = false;
	protected boolean survivalGame = false;
	protected int survivalCreatureCount = 0;

	public GameLoop(NativeRender renderHandle, Map gameMap, Level[] waveList,
			Tower[] tTypes, Player p, GameLoopGUI gui, SoundManager sm,
			boolean survivalGame) {
		this.renderHandle = renderHandle;
		this.mGameMap = gameMap;
		this.mTowerGrid = gameMap.get2DGrid();
		this.mTower = gameMap.getLinearGrid();
		this.mScaler = gameMap.getScaler();
		this.mTTypes = tTypes;
		this.mLvl = waveList;
		this.soundManager = sm;
		this.player = p;
		this.gui = gui;
		this.gui.setUpgradeListeners(new UpgradeTowerLvlListener(),
				new UpgradeFireListener(), new UpgradeFrostListener(),
				new UpgradePoisonListener(), new SellListener(),
				new UpgradeSpecialListener());
		gameTracker = new Tracker();
		this.survivalGame = survivalGame;
	}

	protected void initializeDataStructures() {
		this.mShots = new Shot[mTower.length];
		// Initialize the all the elements in the arrays with garbage data
		for (int i = 0; i < mTower.length; i++) {
			mTower[i].initTower(R.drawable.tesla1, 0, mCreatures, soundManager);
			mShots[i] = new Shot(R.drawable.throwingstar, 0, mTower[i]);
			mTower[i].setHeight(this.mTTypes[0].getHeight());
			mTower[i].setWidth(this.mTTypes[0].getWidth());
			mTower[i].relatedShot = mShots[i];
			mTower[i].relatedShot.setHeight(this.mTTypes[0].relatedShot
					.getHeight());
			mTower[i].relatedShot.setWidth(this.mTTypes[0].relatedShot
					.getWidth());
			mTower[i].draw = false;
			mShots[i].draw = false;
		}

		// Free all allocated data in the render
		// Not needed really.. but now we know for sure that
		// we don't have any garbage anywhere.
		try {
			renderHandle.freeSprites();
			renderHandle.preloadTextureLibrary();
			// Ok, here comes something superduper mega important.
			// The folowing looks up what names the render assigned
			// To every texture from their resource ids
			// And assigns that id to the template objects for
			// Towers shots and creatures.

			for (int i = 0; i < mTTypes.length; i++) {
				mTTypes[i].setCurrentTexture(renderHandle.getTexture(mTTypes[i]
						.getResourceId()));

				mTTypes[i].relatedShot.setCurrentTexture(renderHandle
						.getTexture(mTTypes[i].relatedShot.getResourceId()));

			}

			for (int i = 0; i < mLvl.length; i++) {
				TextureData test = renderHandle.getTexture(mLvl[i]
						.getResourceId());
				mLvl[i].setCurrentTexture(test);
				mLvl[i].setDeadTexture(renderHandle.getTexture(mLvl[i]
						.getDeadResourceId()));
			}

			mSpecialTowers = new Sprite[6];
			mSpecialTowers[0] = new Sprite();
			mSpecialTowers[0].setCurrentTexture(renderHandle
					.getTexture(R.drawable.tesla_special_1));
			mSpecialTowers[1] = new Sprite();
			mSpecialTowers[1].setCurrentTexture(renderHandle
					.getTexture(R.drawable.tesla_special_2));
			mSpecialTowers[2] = new Sprite();
			mSpecialTowers[2].setCurrentTexture(renderHandle
					.getTexture(R.drawable.tesla_special_3));
			mSpecialTowers[3] = new Sprite();
			mSpecialTowers[3].setCurrentTexture(renderHandle
					.getTexture(R.drawable.poisontower_special_1));
			mSpecialTowers[4] = new Sprite();
			mSpecialTowers[4].setCurrentTexture(renderHandle
					.getTexture(R.drawable.poisontower_special_2));
			mSpecialTowers[5] = new Sprite();
			mSpecialTowers[5].setCurrentTexture(renderHandle
					.getTexture(R.drawable.poisontower_special_3));

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// Sends an array with sprites to the renderer

		// UGLY HACK!!
		mGameMap.getBackground()[0].setType(Sprite.BACKGROUND, 0);
		// END UGLY HACK!!
		renderHandle.setSprites(mGameMap.getBackground(), Sprite.BACKGROUND);
		// renderHandle.setSprites(mCreatures);
		renderHandle.setSprites(mTower, Sprite.TOWER);
		renderHandle.setSprites(mShots, Sprite.SHOT);
		// renderHandle.setSprites(mGrid, NativeRender.HUD);

		// Now's a good time to run the GC. Since we won't do any explicit
		// allocation during the test, the GC should stay dormant and not
		// influence our results.
		Runtime r = Runtime.getRuntime();
		r.gc();

	}

	protected void initializeLvl() {

		try {
			// Free last levels sprites to clear the video mem and ram from
			// Unused creatures and settings that are no longer valid.
			renderHandle.freeSprites();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		// Set the creatures texture size and other atributes.
		remainingCreaturesALL = mLvl[lvlNbr].nbrCreatures;
		remainingCreaturesALIVE = mLvl[lvlNbr].nbrCreatures;
		currentCreatureHealth = mLvl[lvlNbr].getHealth()
				* remainingCreaturesALL;
		startCreatureHealth = mLvl[lvlNbr].getHealth() * remainingCreaturesALL;

		mCreatures = new Creature[remainingCreaturesALL];
		for (int i = 0; i < mCreatures.length; i++) {
			mCreatures[i] = new Creature(R.drawable.mrrabbit_animate, 0,
					player, soundManager, mGameMap.getWaypoints().getCoords(),
					this, i, gameTracker);
		}

		renderHandle.setSprites(mCreatures);

		// Need to reverse the list for to draw correctly.
		for (int z = 0; z < remainingCreaturesALL; z++) {
			// The following line is used to add the following wave of creatures
			// to the list of creatures.

			if (survivalGame) {
				mLvl[z].cloneCreature(mCreatures[z]);
			} else
				mLvl[lvlNbr].cloneCreature(mCreatures[z]);

			// This is defined by the scale of this current lvl
			Coords tmpCoord = mScaler.scale(14, 0);
			mCreatures[z]
					.setYOffset((int) (tmpCoord.getX() * mCreatures[z].scale));

		}

		System.gc();

		try {
			// Finally send of the sprites to the render to be allocated
			// And after that drawn.
			renderHandle.finalizeSprites();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Initialize the status, displaying the amount of currency
		gui.sendMessage(gui.GUI_PLAYERHEALTH_ID, player.getHealth(), 0);
		// Initialize the status, displaying the players health
		gui.sendMessage(gui.GUI_PLAYERMONEY_ID, player.getMoney(), 0);
		// Initialize the status, displaying the creature image
		gui.sendMessage(gui.GUI_CREATUREVIEW_ID,
				mLvl[lvlNbr].getDisplayResourceId(), 0);

		// Show the NextLevel-dialog and waits for user to click ok
		// via the semaphore.
		gui.sendMessage(gui.DIALOG_NEXTLEVEL_ID, 0, 0);

		// This is a good time to save the current progress of the game.
		// -2 = call the SaveGame-function.
		// 1 = ask SaveGame to save all data.
		// 0 = not used.
		gui.sendMessage(-2, 1, 0);

		// Initialize the status, displaying the amount of currency
		gui.sendMessage(gui.GUI_PLAYERMONEY_ID, player.getMoney(), 0);
		// Initialize the status, displaying the players health
		gui.sendMessage(gui.GUI_PLAYERHEALTH_ID, player.getHealth(), 0);
		// Initialize the status, displaying the creature image
		gui.sendMessage(gui.GUI_CREATUREVIEW_ID,
				mLvl[lvlNbr].getDisplayResourceId(), 0);

		// And set the progressbar with creature health to full again.
		gui.sendMessage(gui.GUI_PROGRESSBAR_ID, 100, 0);
		// And reset our internal counter for the creature health progress bar
		// ^^
		progressbarLastSent = 100;

		gui.sendMessage(gui.GUI_UPDATELVLNBRTEXT_ID, this.lvlNbr + 1, 0);

		// If we dont reset this variable each wave. The timeDelta will be
		// fucked up
		// And creatures will try to move to second waypoint insteed.
		mLastTime = 0;
		// Reset gamespeed between levels? <- NO!
		// gameSpeed = 1;

		// Remove healthbar until game begins.
		gui.sendMessage(gui.GUI_HIDEHEALTHBAR_ID, 0, 0);

		player.setTimeUntilNextLevel(player.getTimeBetweenLevels());

		// Initialize the status, displaying how long left until level starts
		gui.sendMessage(gui.GUI_NEXTLEVELINTEXT_ID,
				(int) player.getTimeUntilNextLevel(), 0);

		// We wait to show the status bar until everything is updated
		gui.sendMessage(gui.GUI_SHOWSTATUSBAR_ID, 0, 0);

		// Code to wait for the user to click ok on NextLevel-dialog.
		waitForDialogClick();

		int reverse = remainingCreaturesALL;
		for (int z = 0; z < remainingCreaturesALL; z++) {
			reverse--;
			float special = 1;
			if (mCreatures[z].isCreatureFast())
				special = 1.5f;
			mCreatures[z]
					.setSpawndelay((player.getTimeBetweenLevels() + ((reverse * 1.5f) / special)));
		}
	}

	public void run() {

		// Log.d("GAMELOOP","INIT GAMELOOP 1");

		initializeDataStructures();

		// Log.d("GAMELOOP", "INIT GAMELOOP 2");

		// Resuming an old game? Rebuild all the old towers.
		if ((resumeTowers != "") && (resumeTowers != null)) {

			// Log.d("GAMEINIT", "RESUME - Rebuilding towers: " + resumeTowers);

			String[] towers = resumeTowers.split("@");

			for (int i = 0; i < towers.length; i++) {
				String[] tower = towers[i].split(",");
				Coords c = mScaler.getPosFromGrid(Integer.parseInt(tower[1]),
						Integer.parseInt(tower[2]));
				// Log.d("GAMELOOP", "Resume CreateTower Type: " + tower[3]);

				// First we build the tower.
				Tower t = mTowerGrid[Integer.parseInt(tower[1])][Integer
						.parseInt(tower[2])];
				t.createTower(mTTypes[Integer.parseInt(tower[3])], c, mScaler,
						gameTracker, false);

				// Upgrade the fire.
				for (int j = 0; j < Integer.parseInt(tower[4]); j++) {
					int price = t.upgradeSpecialAbility(
							Tower.UpgradeOption.upgrade_fire, 10000);
					if (price != 0) {
						try {
							TextureData tex = renderHandle
									.getTexture(t.relatedShot.getResourceId());
							t.relatedShot.setCurrentTexture(tex);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}

				// Upgrade the frost.
				for (int j = 0; j < Integer.parseInt(tower[5]); j++) {
					int price = t.upgradeSpecialAbility(
							Tower.UpgradeOption.upgrade_frost, 10000);
					if (price != 0) {
						try {
							TextureData tex = renderHandle
									.getTexture(t.relatedShot.getResourceId());
							t.relatedShot.setCurrentTexture(tex);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}

				// Upgrade the poison.
				for (int j = 0; j < Integer.parseInt(tower[6]); j++) {
					int price = t.upgradeSpecialAbility(
							Tower.UpgradeOption.upgrade_poison, 10000);
					if (price != 0) {
						try {
							TextureData tex = renderHandle
									.getTexture(t.relatedShot.getResourceId());
							t.relatedShot.setCurrentTexture(tex);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}

				// Is the tower a Super-upgraded tower?
				// (we dont need sanity checking here, unless the game corrupts
				// itself this cannot be abused)
				if (Integer.parseInt(tower[7]) == 1) {
					superupgrade_teleport = true;
					t.upgradeSuperAbility(10000, mSpecialTowers);
				} else if (Integer.parseInt(tower[7]) == 2) {
					t.upgradeSuperAbility(10000, mSpecialTowers);
					superupgrade_element = true;
				}

			}

		}

		// Log.d("GAMELOOP","INIT GAMELOOP 3");

		gameSpeed = 1;

		while (run) {

			// It is important that ALL SIZES OF SPRITES ARE SET BEFORE! THIS!
			// OR they will be infinitely small.
			initializeLvl();

			// This is used to know when the time has changed or not
			int lastTime = (int) player.getTimeUntilNextLevel();

			// The LEVEL loop. Will run until all creatures are dead or done or
			// player is dead.
			while (remainingCreaturesALL > 0 && run) {

				// Systemclock. Used to help determine speed of the game.
				final long time = SystemClock.uptimeMillis();

				if (pause) {
					try {
						pauseSemaphore.acquire();
					} catch (InterruptedException e1) {
					}
				}

				// Used to calculate creature movement.
				long timeDelta = time - mLastTime;
				// To save some cpu we will sleep the
				// gameloop when not needed. GOAL 60fps
				if (timeDelta <= 16) {
					int naptime = (int) (16 - timeDelta);
					try {
						Thread.sleep(naptime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else if (timeDelta > 70) {
					timeDelta = 70;
					// Log.d("GAMELOOP",
					// "One lap in gameLoop is taking more than 0.07s");
				}
				final float timeDeltaSeconds = mLastTime > 0.0f ? (timeDelta / 1000.0f)
						* gameSpeed
						: 0.0f;
				mLastTime = time;

				// Displays the Countdown-to-next-wave text.
				if (player.getTimeUntilNextLevel() > 0) {

					// So we eventually reach the end of the countdown...
					player.setTimeUntilNextLevel(player.getTimeUntilNextLevel()
							- timeDeltaSeconds);

					if (player.getTimeUntilNextLevel() < 0) {
						// Show healthbar again.
						if (this.survivalGame) {
							gui.sendMessage(gui.GUI_HIDECREATUREDATA_ID, 0, 0);
						} else {
							gui.sendMessage(gui.GUI_SHOWHEALTHBAR_ID, 0, 0);
							// Force the GUI to repaint the
							// #-of-creatures-alive-counter.
							creatureDiesOnMap(0);
						}
						player.setTimeUntilNextLevel(0);
					} else {
						// Update the displayed text on the countdown.
						if (lastTime - player.getTimeUntilNextLevel() > 0.5) {
							lastTime = (int) player.getTimeUntilNextLevel();
							gui.sendMessage(gui.GUI_NEXTLEVELINTEXT_ID,
									lastTime, 0);
						}
					}
				}

				// Calls the method that moves the creature.
				for (int x = 0; x < mLvl[lvlNbr].nbrCreatures; x++) {
					mCreatures[x].update(timeDeltaSeconds);
				}
				// Calls the method that handles the monsterkilling.
				for (int x = 0; x < mTower.length; x++) {
					if (mTower[x].draw == true)
						mTower[x].attackCreatures(timeDeltaSeconds,
								mLvl[lvlNbr].nbrCreatures);
					else if (mTower[x].relatedShot.draw)
						mTower[x].relatedShot.draw = false;
				}
				// Check if the GameLoop are to run the level loop one more
				// time.
				if (player.getHealth() < 1) {
					// If you have lost all your lives then the game ends.
					run = false;
				}
			}

			player.calculateInterest();

			// Check if the GameLoop are to run the level loop one more time.
			if (player.getHealth() < 1) {
				// If you have lost all your lives then the game ends.
				// Log.d("GAMETHREAD", "You are dead");
				showYouLost();
				run = false;
			} else if (remainingCreaturesALL < 1) {
				showYouCompletedWave();
			}
		}
		// Log.d("GAMETHREAD", "dead thread");

		// If we lost or won the game, we load the GameFinished screen.
		if (!gui.quitDialogPressed) {

			gameFinished();
		}

		// Close activity/gameview.
		gui.sendMessage(-1, 0, 0); // gameInit.finish();
	}

	public void gameFinished() {
		Intent gameFinished = new Intent(gui.getGameInit(), GameFinished.class);
		if (lvlNbr >= mLvl.length) {
			gameFinished.putExtra("win", true);
		} else {
			gameFinished.putExtra("win", false);
		}
		if (survivalGame) {
			gameFinished.putExtra("survival", true);
			gameFinished.putExtra("score", this.survivalCreatureCount);
		} else {
			gameFinished.putExtra("survival", false);
			gameFinished.putExtra("score", player.getScore());
		}

		gameFinished.putExtra("difficulty", this.player.getDifficulty());
		gameFinished.putExtra("map", gui.getGameInit().mapChoice);

		if (gui.multiplayerMode)
			gameFinished.putExtra("multiplayer", true);
		else
			gameFinished.putExtra("multiplayer", false);

		// Since this is not a multiplayergame we will send 1 to gameinit
		gui.getGameInit().startActivity(gameFinished);
	}

	// Basic function to show score and failure dialog
	public void showYouLost() {
		// Show the You Lost-dialog.
		// gui.sendMessage(gui.DIALOG_LOST_ID, 0, 0);
		// This is a good time clear all savegame data.
		// -2 = call the SaveGame-function.
		// 2 = ask SaveGame to clear all data.
		// 0 = not used.
		gui.sendMessage(-2, 2, 0);

		// Play fail sound
		soundManager.playSoundLoose();

		// Code to wait for the user to click ok on YouLost-dialog.
		// waitForDialogClick();
	}

	public void showYouCompletedWave() {
		// If you have survied the entire wave without dying. Proceed to next
		// next level.
		// Log.d("GAMETHREAD", "Wave complete");
		lvlNbr++;
		if (lvlNbr >= mLvl.length) {
			// You have completed this map
			// Log.d("GAMETHREAD", "You have completed this map");

			// Show the You Won-dialog.
			// gui.sendMessage(gui.DIALOG_WON_ID, 0, 0);

			// This is a good time clear all savegame data.
			// -2 = call the SaveGame-function.
			// 2 = ask SaveGame to clear all data.
			// 0 = not used.
			gui.sendMessage(-2, 2, 0);

			// Play victory sound
			soundManager.playSoundVictory();

			// Code to wait for the user to click ok on YouWon-dialog.
			// waitForDialogClick();

			// Code to wait for the user to click ok on YouWon-dialog.
			// !!! MOVED !!! Put this before scoreninja instead!
			// Might fix some activity-focus problems we're having... /Fredrik
			// waitForDialogClick();
			run = false;

		}

	}

	public boolean createTower(Coords TowerPos, int towerType) {
		if (mTTypes.length > towerType) {
			if (!mScaler.insideGrid(TowerPos.x, TowerPos.y)) {
				// You are trying to place a tower on a spot outside the grid
				return false;
			}
			if (player.getMoney() < mTTypes[towerType].getPrice()) {
				// Not enough money to build this tower.
				return false;
			}
			Coords tmpC = mScaler.getGridXandY(TowerPos.x, TowerPos.y);
			int tmpx = tmpC.x;
			int tmpy = tmpC.y;

			Tower t = mTowerGrid[tmpx][tmpy];

			if (t != null && !t.draw) {
				Coords towerPlacement = mScaler.getPosFromGrid(tmpx, tmpy);
				t.createTower(mTTypes[towerType], towerPlacement, mScaler,
						gameTracker, false);
				player.moneyFunction(-mTTypes[towerType].getPrice());

				try {
					TextureData tex = renderHandle
							.getTexture(t.getResourceId());
					t.setCurrentTexture(tex);
					tex = renderHandle
							.getTexture(t.relatedShot.getResourceId());
					t.relatedShot.setCurrentTexture(tex);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				soundManager.playSoundCreate();
				updateCurrency();

				return true;
			} else {
				/*
				 * TODO: Maybe someone can use this to upgrade towers, comment
				 * left intact.
				 * 
				 * // User clicked on an existing tower. Show upgrade window.
				 * Message msg = new Message(); msg.what = 4;
				 * nextLevelHandler.sendMessage(msg);
				 */
			}
		}
		return false;
	}

	// When a creature is dead and have faded away we will remove it from the
	// gameloop
	public void creatureLeavesMAP(int n) {
		this.remainingCreaturesALL -= n;
	}

	// When the player decreases in health, we will notify the status bar
	public void updatePlayerHealth() {
		gui.sendMessage(gui.GUI_PLAYERHEALTH_ID, player.getHealth(), 0);
	}

	// When a creature is dead we will notify the status bar
	public void creatureDiesOnMap(int n) {
		this.remainingCreaturesALIVE -= n;
		if (remainingCreaturesALIVE <= 0)
			for (int x = 0; x < mLvl[lvlNbr].nbrCreatures; x++)
				mCreatures[x].setAllDead(true);
		gui.sendMessage(gui.GUI_CREATURELEFT_ID, remainingCreaturesALIVE, 0);
	}

	// When a creature is dead in survival we will notify the status bar
	public void creatureDiesOnMapSurvival(int n) {
		this.survivalCreatureCount += n;
		gui.sendMessage(gui.GUI_CREATURESURVIVAL_ID, survivalCreatureCount, 0);
	}

	public void updateCreatureProgress(float dmg) {
		// Update the status, displaying total health of all creatures
		this.currentCreatureHealth -= dmg;

		// Another solution, only send when the update is 1/20'th of the total
		// healthbar:
		// If we cast 1,999999 to int we will receive 1.
		// To solve that problem we use +0,5. Ex: 1,4 and 1,5 both will be
		// casted to 1 but
		// if we add 0,5 -> 1,9 and 2. Java will cast to 1 and 2 =)
		float currFl = (20 * (currentCreatureHealth / startCreatureHealth)) + 0.5f;
		int curr = ((int) currFl) * 5;

		if ((progressbarLastSent - 5) >= curr) {
			// When using a 5% step we will asume that no tower can do more than
			// 5% of total creature
			// health. Instead we will set progressbar to current total value of
			// creature health.
			progressbarLastSent = curr;
			gui.sendMessage(gui.GUI_PROGRESSBAR_ID, progressbarLastSent, 0);
		}
	}

	// Update the status when the players money increases.
	public void updateCurrency() {
		gui.sendMessage(gui.GUI_PLAYERMONEY_ID, player.getMoney(), 0);
	}

	public void stopGameLoop() {
		run = false;
	}

	public void dialogClick() {
		dialogSemaphore.release();
	}

	protected void waitForDialogClick() {
		// Code to wait for the user to click ok on a dialog.
		try {
			dialogSemaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		try {
			dialogSemaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		dialogSemaphore.release();
	}

	public Level getLevelData() {
		return mLvl[lvlNbr];
	}

	public Player getPlayerData() {
		return player;
	}

	public int getLevelNumber() {
		return lvlNbr;
	}

	// This is used by the savegame-function to remember all the towers.
	public String resumeGetTowers() {
		String s = "";
		Coords tmp;
		for (int i = 0; i < mTower.length; i++) {
			Tower t = mTower[i];
			if (t != null && t.draw) {
				tmp = mScaler.getGridXandY((int) t.x, (int) t.y);
				// int[] towerUpgrades = t.getUpgradeTypeIndex(this.mTTypes);
				s = s + t.getTowerType() + "," + (int) tmp.x + ","
						+ (int) tmp.y + "," + t.getTowerTypeId() + ","
						+ t.getUpgradeFire() + "," + t.getUpgradeFrost() + ","
						+ t.getUpgradePoison() + "," + t.getUpgradeSuper()
						+ "@";
			}
		}

		// Log.d("GAMELOOP", "DEBUG, resumeGetTowers: " + s);

		return s;
	}

	public void resume(int level, String towers) {
		this.lvlNbr = level;
		this.resumeTowers = towers;
	}

	public void setGameSpeed(int i) {
		this.gameSpeed = i;
	}

	public boolean gridOcupied(int x, int y) {
		if (!mScaler.insideGrid(x, y)) {
			// Towers do can not exist at these coordinates
			return false;
		}
		Coords tmpC = mScaler.getGridXandY(x, y);
		return mTowerGrid[tmpC.x][tmpC.y] != null
				&& mTowerGrid[tmpC.x][tmpC.y].draw == true;
	}

	public int[] getTowerCoordsAndRange(int x, int y) {
		if (!mScaler.insideGrid(x, y)) {
			// Towers do can not exist at these coordinates
			return null;
		}
		Coords tmpC = mScaler.getGridXandY(x, y);

		int[] rData = new int[5];

		rData[0] = (int) mTowerGrid[tmpC.x][tmpC.y].x;
		rData[1] = (int) mTowerGrid[tmpC.x][tmpC.y].y;
		rData[2] = (int) mTowerGrid[tmpC.x][tmpC.y].getRange();
		rData[3] = (int) mTowerGrid[tmpC.x][tmpC.y].getWidth();
		rData[4] = (int) mTowerGrid[tmpC.x][tmpC.y].getHeight();

		return rData;
	}

	public Tower getTower(int towerid) {
		return mTTypes[towerid];
	}

	public void showTowerUpgradeUI(int x, int y) {
		selectedTower = mScaler.getGridXandY(x, y);
		Tower t = mTowerGrid[selectedTower.x][selectedTower.y];
		int[] test = t.getUpgradeTypeIndex(this.mTTypes, superupgrade_teleport,
				superupgrade_element);
		int min = (int) (t.getMinDamage() + t.getAoeDamage());
		int max = (int) (t.getMaxDamage() + t.getAoeDamage());
		boolean aoeTower = t.getTowerType() == Tower.AOE;
		gui.showTowerUpgrade(test[0], test[1], test[2], test[3], test[4],
				test[5], test[6], test[7], test[8], test[9], test[10], min,
				max, aoeTower);
	}

	public static void pause() {
		pause = true;
	}

	public static void unPause() {
		pause = false;
		pauseSemaphore.release();
	}

	private class UpgradeTowerLvlListener implements OnClickListener {
		public void onClick(View v) {
			// Log.d("GUI", "Upgrade A clicked!");
			if (selectedTower != null) {
				Tower t = mTowerGrid[selectedTower.x][selectedTower.y];
				int upgradeIndex = t.getUpgradeTowerLvl();
				if (upgradeIndex != -1
						&& player.getMoney() >= mTTypes[upgradeIndex]
								.getPrice()) {
					player.moneyFunction(-mTTypes[upgradeIndex].getPrice());
					updateCurrency();
					t.createTower(mTTypes[upgradeIndex], null, mScaler,
							gameTracker, true);

					try {
						if (t.towerType == Tower.TELSA && t.getSuperTeleport()) {
							if (t.getUpgradeTowerLvl() == 11) {
								t.setCurrentTexture(mSpecialTowers[1]
										.getCurrentTexture());
								t.setResourceId(R.drawable.tesla_special_2);
							}
							if (t.getUpgradeTowerLvl() == -1) {
								t.setCurrentTexture(mSpecialTowers[2]
										.getCurrentTexture());
								t.setResourceId(R.drawable.tesla_special_3);
							}
						} else if (t.towerType == Tower.AOE
								&& t.getSuperElement()) {
							if (t.getUpgradeTowerLvl() == 10) {
								t.setCurrentTexture(mSpecialTowers[4]
										.getCurrentTexture());
								t.setResourceId(R.drawable.poisontower_special_2);
							}
							if (t.getUpgradeTowerLvl() == -1) {
								t.setCurrentTexture(mSpecialTowers[5]
										.getCurrentTexture());
								t.setResourceId(R.drawable.poisontower_special_3);
							}
						} else {
							TextureData tex = renderHandle.getTexture(t
									.getResourceId());
							t.setCurrentTexture(tex);
						}
						// tex =
						// renderHandle.getTexture(t.relatedShot.getResourceId());
						// t.relatedShot.setCurrentTexture(tex);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					int[] data = getTowerCoordsAndRange(
							(int) (t.x + t.getWidth() / 2),
							(int) (t.y + t.getHeight() / 2));
					gui.getGameInit().hudHandler.showRangeIndicator(data[0],
							data[1], data[2], data[3], data[4]);

					showTowerUpgradeUI((int) t.x, (int) t.y);
				} else {
					gui.NotEnoughMoney();
				}
			} else {
				// Log.d("GAMELOOP","Error, no tower selected, can not upgrade");
			}

		}
	}

	private class UpgradeFireListener implements OnClickListener {
		public void onClick(View v) {
			upgradeTower(Tower.UpgradeOption.upgrade_fire);
		}
	}

	private class UpgradeFrostListener implements OnClickListener {
		public void onClick(View v) {
			upgradeTower(Tower.UpgradeOption.upgrade_frost);
		}
	}

	private class UpgradePoisonListener implements OnClickListener {
		public void onClick(View v) {
			upgradeTower(Tower.UpgradeOption.upgrade_poison);
		}
	}

	private class UpgradeSpecialListener implements OnClickListener {
		public void onClick(View v) {
			upgradeSuperTower();
		}
	}

	private void upgradeTower(Tower.UpgradeOption opt) {
		if (selectedTower != null) {
			Tower t = mTowerGrid[selectedTower.x][selectedTower.y];
			int price = t.upgradeSpecialAbility(opt, player.getMoney());
			if (price != 0) {
				player.moneyFunction(-price);
				try {
					TextureData tex = renderHandle.getTexture(t.relatedShot
							.getResourceId());
					t.relatedShot.setCurrentTexture(tex);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				showTowerUpgradeUI((int) t.x, (int) t.y);
				updateCurrency();
			} else {
				gui.NotEnoughMoney();
				// Log.d("GAMELOOP","No upgrade done");
			}
		} else {
			// Log.d("GAMELOOP","Error, no tower selected, can not upgrade");
		}

	}

	private void upgradeSuperTower() {
		if (selectedTower != null) {
			Tower t = mTowerGrid[selectedTower.x][selectedTower.y];
			int price = t
					.upgradeSuperAbility(player.getMoney(), mSpecialTowers);
			if (price != 0) {
				if (t.getTowerType() == Tower.AOE)
					this.superupgrade_element = true;
				else
					this.superupgrade_teleport = true;

				player.moneyFunction(-price);
				showTowerUpgradeUI((int) t.x, (int) t.y);
				updateCurrency();
			} else {
				gui.NotEnoughMoney();
			}
		} else {
			// Log.d("GAMELOOP","Error, no tower selected, can not upgrade");
		}

	}

	private class SellListener implements OnClickListener {
		public void onClick(View v) {
			// Log.d("GameLoop", "Sell Tower clicked!");
			if (selectedTower != null) {

				gui.getGameInit().hudHandler.hideRangeIndicator();

				gui.hideTowerUpgrade();
				Tower t = mTowerGrid[selectedTower.x][selectedTower.y];
				t.draw = false;
				t.relatedShot.draw = false;
				if (t.getSuperElement()) {
					superupgrade_element = false;
				}
				if (t.getSuperTeleport()) {
					superupgrade_teleport = false;
				}
				player.moneyFunction(t.getResellPrice());
				updateCurrency();
			} else {
				// Log.d("GAMELOOP","Error, no tower selected, can not sell");
			}
		}
	}

	public void alertTeleport() {
		gui.sendMessage(gui.GUI_TELEPORTSUCCESS, 0, 0);
	}
}