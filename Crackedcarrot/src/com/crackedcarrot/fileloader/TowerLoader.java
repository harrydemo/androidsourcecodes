package com.crackedcarrot.fileloader;

import java.io.IOException;
import java.io.InputStream;
import android.content.Context;
import com.crackedcarrot.Coords;
import com.crackedcarrot.Scaler;
import com.crackedcarrot.Shot;
import com.crackedcarrot.SoundManager;
import com.crackedcarrot.Tower;
import com.crackedcarrot.menu.R;

/**
 * A class that reads the requested towerConf and returns an list of Towers.
 */
public class TowerLoader {

	private SoundManager soundManager;
	private Context context;
	private InputStream in;
	private Tower[] towerList;
	private Scaler scaler;
	private int mResourceId;
	private int ShotResID;
	private int towerType;
	private float range;
	private float rangeAOE;
	private int level;
	private int price;
	private int resellPrice;
	private int minDamage;
	private int maxDamage;
	private int aoeDamage;
	private int velocity;
	private int upgrade1;
	private float coolDown;
	private Shot relatedShot;
	private float width;
	private float height;
	private float animationTime;
	private int sound_l;
	private int sound_i;

	/**
	 * Constructor
	 * 
	 * @param Context
	 *            The context of the activity that requested the loader.
	 * @param Scaler
	 *            A scaler.
	 */
	public TowerLoader(Context context, Scaler scaler, SoundManager soundManager) {
		this.context = context;
		this.scaler = scaler;
		this.soundManager = soundManager;
	}

	/**
	 * Will read a file and turn all the data from the file to list of towers.
	 * <p>
	 * This method is called from GameInit.
	 * 
	 * @param String
	 *            The filename of the requested file
	 * @return Tower[] A list of Tower objects
	 */
	public Tower[] readTowers(String towerFile) {
		int resID = context.getResources().getIdentifier(towerFile, "raw",
				context.getPackageName());
		in = context.getResources().openRawResource(resID);
		int i = 0;
		int lineNo = 0;
		int tmpCount = 0;
		int nbrTwr = 0;
		int twrNbr = 0;
		String tmpStr[] = null;

		try {
			String buf = "";
			while ((i = in.read()) != -1) {
				char c = (char) i;
				if (c != '\n') {
					buf += c;
				} else if (c == '\n') {
					lineNo++;
					switch (lineNo) {
					case 1:
						// Contains info about the file. Do nothing here.
						break;
					case 2:
						tmpStr = buf.split("::");
						nbrTwr = Integer.parseInt(tmpStr[1].trim());
						towerList = new Tower[nbrTwr];
						break;
					default:
						tmpCount++;
						if (tmpCount >= 4) {
							tmpStr = buf.split("::");
						}
						switch (tmpCount) {
						case 4:
							// Tower level
							level = Integer.parseInt(tmpStr[1].trim());
							break;
						case 5:
							// Tower price
							price = Integer.parseInt(tmpStr[1].trim());
							break;
						case 6:
							// Tower resell value
							resellPrice = Integer.parseInt(tmpStr[1].trim());
							break;
						case 7:
							// Tower minimum damage
							minDamage = Integer.parseInt(tmpStr[1].trim());
							break;
						case 8:
							// Tower maximum damage
							maxDamage = Integer.parseInt(tmpStr[1].trim());
							break;
						case 9:
							// Tower velocity of bullets
							Coords recalc = scaler.scale(
									Integer.parseInt(tmpStr[1].trim()), 0);
							velocity = recalc.getX();
							break;
						case 10:
							// Cooldown between each shot
							coolDown = Float.valueOf(tmpStr[1].trim());
							break;
						case 11:
							// Towertype
							towerType = Integer.parseInt(tmpStr[1].trim());
							break;
						case 12:
							// upgrade
							upgrade1 = Integer.parseInt(tmpStr[1].trim());
							break;
						case 13:
							// Tower range
							recalc = scaler.scale(
									Integer.parseInt(tmpStr[1].trim()), 0);
							range = recalc.getX();
							break;
						case 14:
							// AOE range
							recalc = scaler.scale(
									Integer.parseInt(tmpStr[1].trim()), 0);
							rangeAOE = recalc.getX();
							break;
						case 15:
							// AOE damage
							aoeDamage = Integer.parseInt(tmpStr[1].trim());
							break;
						case 16:
							// Shot animation time
							animationTime = Float.parseFloat(tmpStr[1].trim());
							break;
						case 17:
							// Shot sound. Projectile leaves
							String soundfile = tmpStr[1].trim();
							if (!soundfile.equals("none")) {
								sound_l = context.getResources().getIdentifier(
										soundfile, "raw",
										context.getPackageName());
								sound_l = soundManager.addSound(1.0f, 300,
										sound_l);
							} else
								sound_l = -1;
							break;
						case 18:
							// Shot sound. impact
							String soundfile2 = tmpStr[1].trim();
							if (!soundfile2.equals("none")) {
								sound_i = context.getResources().getIdentifier(
										soundfile2, "raw",
										context.getPackageName());
								sound_i = soundManager.addSound(1.0f, 300,
										sound_i);
							} else
								sound_i = -1;

							ShotResID = 0;

							// Load tower texture
							if (this.towerType == Tower.BUNKER) {
								if (level == 1) {
									mResourceId = R.drawable.bunker1;
									ShotResID = R.drawable.throwingstar;
								} else if (level == 2) {
									mResourceId = R.drawable.bunker2;
									ShotResID = R.drawable.throwingstar;
								} else if (level == 3) {
									mResourceId = R.drawable.bunker3;
									ShotResID = R.drawable.throwingstar;
								}
							}
							if (this.towerType == Tower.CANNON) {
								if (level == 1) {
									mResourceId = R.drawable.cannontower1;
									ShotResID = R.drawable.cannonshot;
								} else if (level == 2) {
									mResourceId = R.drawable.cannontower2;
									ShotResID = R.drawable.cannonshot;
								} else if (level == 3) {
									mResourceId = R.drawable.cannontower3;
									ShotResID = R.drawable.cannonshot;
								}
							}
							if (this.towerType == Tower.AOE) {
								if (level == 1) {
									mResourceId = R.drawable.poisontower1;
									ShotResID = R.drawable.poisoncloud;
								} else if (level == 2) {
									mResourceId = R.drawable.poisontower2;
									ShotResID = R.drawable.poisoncloud;
								} else if (level == 3) {
									mResourceId = R.drawable.poisontower3;
									ShotResID = R.drawable.poisoncloud;
								}

							}
							if (this.towerType == Tower.TELSA) {
								if (level == 1) {
									mResourceId = R.drawable.tesla1;
									ShotResID = R.drawable.lightbolt;
								} else if (level == 2) {
									mResourceId = R.drawable.tesla2;
									ShotResID = R.drawable.lightbolt;
								} else if (level == 3) {
									mResourceId = R.drawable.tesla3;
									ShotResID = R.drawable.lightbolt;
								}
							}

							// Tower size ALWAYS 60
							recalc = scaler.scale(60, 60);
							width = recalc.getX();
							height = recalc.getY();

							// Shot size
							recalc = scaler.scale(24, 24);
							relatedShot = new Shot(ShotResID, 0,
									towerList[twrNbr]);
							relatedShot.setHeight(recalc.getY());
							relatedShot.setWidth(recalc.getX());
							relatedShot.setAnimationTime(animationTime);

							towerList[twrNbr] = new Tower(mResourceId, 0, null,
									null);
							towerList[twrNbr].relatedShot = relatedShot;
							towerList[twrNbr].cloneTower(mResourceId,
									towerType, twrNbr, range, rangeAOE, price,
									resellPrice, minDamage, maxDamage,
									aoeDamage, velocity, upgrade1, coolDown,
									width, height, relatedShot, sound_l,
									sound_i);

							twrNbr++;
							tmpCount = 0;
							break;
						default:
							break;
						}
						buf = "";
						break;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return towerList;
	}
}
