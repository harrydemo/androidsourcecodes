package com.crackedcarrot.multiplayer;

import java.util.Random;
import java.util.concurrent.Semaphore;

import android.content.Intent;
import com.crackedcarrot.GameFinished;
import com.crackedcarrot.GameLoop;
import com.crackedcarrot.GameLoopGUI;
import com.crackedcarrot.NativeRender;
import com.crackedcarrot.Player;
import com.crackedcarrot.SoundManager;
import com.crackedcarrot.Tower;
import com.crackedcarrot.fileloader.Level;
import com.crackedcarrot.fileloader.Map;

public class MultiplayerGameLoop extends GameLoop {

	private static Semaphore synchLevelSemaphore = new Semaphore(1);
	private MultiplayerService mMultiplayerService;
	private boolean opponentLife = true;
	// private boolean hurtOpponent;

	// The variable representing the shield in a multiplayer game
	public boolean multiplayerShield = false;

	public MultiplayerGameLoop(NativeRender renderHandle, Map gameMap,
			Level[] waveList, Tower[] tTypes, Player p, GameLoopGUI gui,
			SoundManager sm, MultiplayerService mpS, boolean survivalGame) {
		super(renderHandle, gameMap, waveList, tTypes, p, gui, sm, survivalGame);
		this.mMultiplayerService = mpS;
	}

	/**
	 * Overriding initializeLevel from super class to control multiplayer
	 * synchronization
	 */
	protected void initializeLvl() {

		super.initializeLvl();

		// Make the five multiplayer buttons visible for the current level
		gui.sendMessage(gui.SETMULTIPLAYERVISIBLE, 0, 0);

		// Send message to opponent that the player is ready for next level
		String message2 = "synchLevel";
		byte[] send2 = message2.getBytes();
		mMultiplayerService.write(send2);

		// Show "Waiting for opponent" message
		// this.gui.sendMessage(gui.OPP_CREATURELEFT,
		// mLvl[this.lvlNbr].nbrCreatures, 0);
		if (lvlNbr == 0) {
			gui.sendMessage(gui.MULTIPLAYER_SCOREBOARD_WAITING, 0, 0);
			gui.sendMessage(gui.MULTIPLAYER_SCOREBOARD, 0, 0);
		}

		// Wait for the opponent
		try {
			synchLevelSemaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			synchLevelSemaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		synchLevelSemaphore.release();

		// Close "Waiting for opponent" message
		gui.sendMessage(gui.MULTIPLAYER_SCOREBOARD_CLOSE, 0, 0);

		this.multiplayerShield = false;

	}

	/** Overriding the run method from super class GameLoop */
	public void run() {
		super.run();
		// Log.d("MULTIGAMELOOP", "DU D�DAR DIG SJ�LV!!!");
		String stopMsg = "Dead";
		byte[] sendStop = stopMsg.getBytes();
		mMultiplayerService.write(sendStop);
	}

	// Override
	public void showYouLost() {
		// If you have lost all your lives then the game ends.
		// Log.d("GAMETHREAD", "You are dead");

		// Send info to opponent that player is dead
		String message = "Dead";
		byte[] send = message.getBytes();
		mMultiplayerService.write(send);

		// Is the opponent still alive?
		if (this.opponentLife) {
			// Send the synch message so opponent won't wait for eternity
			String lastMessage = "synchLevel";
			byte[] sendMessage = lastMessage.getBytes();
			mMultiplayerService.write(sendMessage);
			// Show the "You Lost"-dialog.
			gui.sendMessage(gui.MULTIPLAYER_SCOREBOARD_UPDATE_END, 1, 0);
			gui.sendMessage(gui.MULTIPLAYER_SCOREBOARD, 0, 0);
			waitForDialogClick();
			run = false;
		} else {
			// The one who dies first is the looser, so this player has won
			gui.sendMessage(gui.MULTIPLAYER_SCOREBOARD_UPDATE_END, 2, 0);
			gui.sendMessage(gui.MULTIPLAYER_SCOREBOARD, 0, 0);
			waitForDialogClick();
			run = false;
		}
	}

	public void showYouCompletedWave() {
		// Send players score to opponent
		// Log.d("GAMETHREAD", "Wave complete");
		lvlNbr++;
		/**
		
		*/

		// Is the opponent dead, in that case you've won the game
		// Show "Waiting for opponent" message
		gui.sendMessage(gui.MULTIPLAYER_SCOREBOARD_WAITING, 0, 0);
		gui.sendMessage(gui.MULTIPLAYER_SCOREBOARD, 0, 0);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		String me = "synchLevel";
		byte[] sendThis = me.getBytes();
		mMultiplayerService.write(sendThis);

		// Wait for the opponent
		try {
			synchLevelSemaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// Log.d("ZZZZZZZ", "Before second synchlevel");
		try {
			synchLevelSemaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		synchLevelSemaphore.release();

		// The game is not totally completed
		if (lvlNbr < mLvl.length) {
			// Do nothing here
			// Close "Waiting for opponent" message
			if (this.opponentLife)
				gui.sendMessage(gui.MULTIPLAYER_SCOREBOARD_CLOSE, 0, 0);

		} else {
			// Log.d("GAMETHREAD", "You have completed this map");
			// Both players have survived all the enemy waves
			gui.sendMessage(gui.MULTIPLAYER_SCOREBOARD, 0, 0);
			gui.sendMessage(gui.MULTIPLAYER_SCOREBOARD_UPDATE_END, 0, 0);
			waitForDialogClick();
			run = false;
		}

		// Check if the opponent has died while we waited for synching.
		if (!this.opponentLife) {
			gui.sendMessage(gui.MULTIPLAYER_SCOREBOARD, 0, 0);
			gui.sendMessage(gui.MULTIPLAYER_SCOREBOARD_UPDATE_END, 2, 0);
			waitForDialogClick();
			run = false;
		}

	}

	/** Release the synchronization semaphore from outside this class */
	// public static void synchLevelClick() {
	public void synchLevelClick() {
		synchLevelSemaphore.release();
	}

	/**
	 * When handler receives info about opponent life, update through this
	 * method
	 */
	public void setOpponentLife(boolean bool) {
		this.opponentLife = bool;
	}

	/**
	 * This method is called when the player wants to increase the speed and
	 * health of one of the opponents enemies. The method can only be called
	 * once every level
	 */
	public boolean increaseEnemySpeed() {
		if (player.getMoney() >= 40) {
			player.moneyFunction(-40);
			// send message over Bluetooth
			String increaseEnemySpeed = "incEnSp";
			byte[] sendIncEnSp = increaseEnemySpeed.getBytes();
			mMultiplayerService.write(sendIncEnSp);
			updateCurrency();
			return true;
		} else {
			// Not enough money
			return false;
		}
	}

	/**
	 * This method is called when the player wants to decrease the health of the
	 * opponent. The method can only be called once every level
	 */
	public boolean decreaseOppLife() {
		if (player.getMoney() >= 100) {
			player.moneyFunction(-100);
			// send message over Bluetooth
			String decOppLife = "decOppLife";
			byte[] sendDecOppL = decOppLife.getBytes();
			mMultiplayerService.write(sendDecOppL);
			updateCurrency();
			return true;
		} else {
			// Not enough money
			return false;
		}
	}

	/**
	 * This method is called when the player wants to destroy one of the
	 * opponents random towers The method can only be called once every level
	 */
	public boolean destroyTower() {
		if (player.getMoney() >= 200) {
			player.moneyFunction(-200);
			// send message over Bluetooth
			String desTower = "desTower";
			byte[] sendDesTow = desTower.getBytes();
			mMultiplayerService.write(sendDesTow);
			updateCurrency();
			return true;
		} else {
			// Not enough money
			return false;
		}
	}

	/**
	 * This method is called when the player wants to make all the opponents
	 * enemies gain special ability (fast or resistance)
	 */
	public boolean makeElemental() {
		if (player.getMoney() >= 20) {
			player.moneyFunction(-20);
			// send message over Bluetooth
			String mkElem = "mkElem";
			byte[] sendMkElem = mkElem.getBytes();
			mMultiplayerService.write(sendMkElem);
			updateCurrency();
			return true;
		} else {
			// Not enough money
			return false;
		}
	}

	/**
	 * This method is called when the player wants to make a shield to protect
	 * from the opponents nasty multiplayer- manipulations
	 */
	public boolean makeShield() {
		if (player.getMoney() >= 50) {
			player.moneyFunction(-50);
			mkShield();
			updateCurrency();
			return true;
		} else {
			// Not enough money
			return false;
		}
	}

	/**
	 * The five help functions for the multiplayer gameplay
	 */
	public void incEnSp(int nbr) {

		// If we fail to find a random creature in nbrCreatures tries. We will
		// try to get
		// the first living creature and add special abillites to him.
		if (nbr >= 1) {
			for (int z = 0; z < mLvl[lvlNbr].nbrCreatures; z++) {
				if (mCreatures[z].draw && mCreatures[z].getHealth() > 0) {

					if (!mCreatures[z].creatureFast) {
						mCreatures[z].creatureFast = true;
						mCreatures[z]
								.setVelocity(mCreatures[z].getVelocity() * 1.5f);
					}
					mCreatures[z]
							.setCurrentHealth(mLvl[lvlNbr].getHealth() * 4);
					updateCreatureProgress(0);
					break;
				}
			}
			return;
		}

		// Tries to find a random living creature and speed and health to him
		Random rand = new Random();
		int tmp = rand.nextInt(mLvl[lvlNbr].nbrCreatures);
		if (mCreatures[tmp].draw && mCreatures[tmp].getHealth() > 0) {
			if (!mCreatures[tmp].creatureFast) {
				mCreatures[tmp].creatureFast = true;
				mCreatures[tmp]
						.setVelocity(mCreatures[tmp].getVelocity() * 1.5f);
			}
			mCreatures[tmp].setCurrentHealth(mLvl[lvlNbr].getHealth() * 4);
			updateCreatureProgress(0);
		} else {
			incEnSp(1);
		}
	}

	public boolean decOppLife() {
		if (player.getHealth() > 5) {
			player.damage(5);
			updatePlayerHealth();
			return true;
		} else
			return false;
	}

	public void desTower(int nbr) {
		if (nbr >= 1) {
			for (int z = 0; z < mTower.length; z++) {
				if (mTower[z].draw) {
					mTower[z].draw = false;
					mTower[z].relatedShot.draw = false;
					break;
				}
			}
			return;
		}

		Random rand = new Random();
		int tmp = rand.nextInt(mTower.length);

		if (mTower[tmp].draw) {
			mTower[tmp].draw = false;
			mTower[tmp].relatedShot.draw = false;
		} else
			desTower(1);
	}

	public String mkElem() {

		boolean fast = mCreatures[0].creatureFast;
		boolean fireResistant = mCreatures[0].creatureFireResistant;
		boolean frostResistant = mCreatures[0].creatureFrostResistant;
		boolean poisonResistant = mCreatures[0].creaturePoisonResistant;

		Random rand = new Random();
		int tmp = rand.nextInt(4);

		if (tmp == 0) {
			if (fast)
				tmp++;
			else
				fast = true;
		}

		if (tmp == 1) {
			if (fireResistant)
				tmp++;
			else
				fireResistant = true;
		}

		if (tmp == 2) {
			if (frostResistant)
				tmp++;
			else
				frostResistant = true;
		}

		if (tmp == 3) {
			if (poisonResistant) {
				if (fast)
					if (fireResistant)
						frostResistant = true;
					else
						fireResistant = true;
				else
					fast = true;
			} else
				poisonResistant = true;
		}
		int tmp2 = 0;
		int tmp3 = 0;
		int tmp4 = 0;

		if (!this.survivalGame) {
			if (this.lvlNbr > 7) {
				tmp2 = rand.nextInt(4);
				if (tmp2 == 0)
					fast = true;
				if (tmp2 == 1)
					fireResistant = true;
				if (tmp2 == 2)
					frostResistant = true;
				if (tmp2 == 3)
					poisonResistant = true;
			}
			if (this.lvlNbr > 12) {
				tmp3 = rand.nextInt(4);
				if (tmp3 == 0)
					fast = true;
				if (tmp3 == 1)
					fireResistant = true;
				if (tmp3 == 2)
					frostResistant = true;
				if (tmp3 == 3)
					poisonResistant = true;
			}
			if (this.lvlNbr > 16) {
				tmp4 = rand.nextInt(4);
				if (tmp4 == 0)
					fast = true;
				if (tmp4 == 1)
					fireResistant = true;
				if (tmp4 == 2)
					frostResistant = true;
				if (tmp4 == 3)
					poisonResistant = true;
			}
		}
		for (int z = 0; z < mLvl[lvlNbr].nbrCreatures; z++) {
			if (fast && !mCreatures[z].creatureFast) {
				mCreatures[z].setVelocity(mCreatures[z].getVelocity() * 1.5f);
			}

			mCreatures[z].setCreatureSpecials(fast, fireResistant,
					frostResistant, poisonResistant);

		}

		String returnS = "";
		if (fast)
			returnS = "1";
		else
			returnS = "0";
		if (fireResistant)
			returnS += "1";
		else
			returnS += "0";
		if (frostResistant)
			returnS += "1";
		else
			returnS += "0";
		if (poisonResistant)
			returnS += "1";
		else
			returnS += "0";

		return returnS;
	}

	public void mkShield() {
		this.multiplayerShield = true;
	}

	// Override
	public static void pause() {
		// Do nothing
	}

	// Override
	public static void unPause() {
		// Do nothing
	}

	// Override
	public void creatureDiesOnMap(int n) {
		super.creatureDiesOnMap(n);
		String creDies = "CRE:" + player.getScore() + ":"
				+ remainingCreaturesALIVE;
		byte[] sendCreDies = creDies.getBytes();
		mMultiplayerService.write(sendCreDies);
	}

	// When a creature is dead in survival we will notify the status bar
	@Override
	public void creatureDiesOnMapSurvival(int n) {
		super.creatureDiesOnMapSurvival(n);
		String creDies = "CRE:" + player.getScore() + ":"
				+ this.survivalCreatureCount;
		byte[] sendCreDies = creDies.getBytes();
		mMultiplayerService.write(sendCreDies);
	}

	@Override
	public void updatePlayerHealth() {
		super.updatePlayerHealth();
		String health = "HEALTH:" + player.getScore() + ":"
				+ player.getHealth();
		byte[] sendHealth = health.getBytes();
		mMultiplayerService.write(sendHealth);
	}

	public boolean isSurvivalGame() {
		return this.survivalGame;
	}

	@Override
	public void gameFinished() {
		Intent gameFinished = new Intent(gui.getGameInit(), GameFinished.class);
		if (this.opponentLife) {
			gameFinished.putExtra("win", false);
		} else {
			gameFinished.putExtra("win", true);
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
}
