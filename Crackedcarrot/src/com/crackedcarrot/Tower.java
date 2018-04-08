package com.crackedcarrot;

import java.util.Random;
import com.crackedcarrot.menu.R;

/**
 * Class defining a tower in the game
 */
public class Tower extends Sprite {
	private SoundManager soundManager;
	private Creature[] mCreatures;
	// different towertypes
	public final static int CANNON = 1;
	public final static int AOE = 2;
	public final static int BUNKER = 3;
	public final static int TELSA = 4;

	public enum UpgradeOption {
		upgrade_lvl, upgrade_fire, upgrade_frost, upgrade_poison, upgrade_special
	};

	// towertype
	public int towerType;
	// The current range of a tower
	private float range;
	// The current AOE range of a tower
	private float rangeAOE;
	// Price for the tower
	private int price;
	// Resell value if the tower is sold
	private int resellPrice;
	// Minimum damage that this tower can inflict
	private int minDamage;
	// Maximum damage that this tower can inflict
	private int maxDamage;
	// AOE damage
	private int aoeDamage;
	// Speed of the shots
	private int velocity;
	// If the tower have frost damage
	private boolean hasFrostDamage;
	// If the tower have frost damage. How long?
	private int frostTime;
	// If the tower have frost damage. How much will it affect?
	private float frostAmount;
	// If the tower have fire damage
	private boolean hasFireDamage;
	// If the tower have firedamage how mutch
	private float fireFactor;
	// If the tower have poison damage
	private boolean hasPoisonDamage;
	// Factor of posiondamage.
	private float poisonFactor;
	// The linked upgrade for this tower
	private int upgradeLvl;
	private int upgradeFire;
	private int upgradeFrost;
	private int upgradePoison;
	// If this tower have a super ability one of the following booelans will be
	// set
	private boolean hasSuper_teleport = false;
	private boolean hasSuper_element = false;
	// The type of shot related to this tower
	public Shot relatedShot;
	// The time existing between each fired shot
	private float coolDown;
	// The temporary variable representing the time existing between each fired
	// shot
	private float tmpCoolDown;
	// The current target creature
	private Creature targetCreature;
	// Random used to calculate damage
	private Random rand;
	// used by resume to uniquely identify this tower-type.
	private int towerTypeId;
	// Used to determine if the tower should animate impact
	private boolean ImpactdAnimate = false;
	// To determine when a creature has been teleported to spawnpoint we will
	// check his nbr of laps
	private int targetCreatureMapLap;
	// All creatures avaible for this tower
	private TrackerList startTrackerList;
	// Sound. leaves
	private int sound_l;
	// Sound. impact
	private int sound_i;

	// Testing. Used to calculate correct distance
	private float aspectRatio = 0;

	/**
	 * Constructor used when defining a new tower in the game. Needs the texture
	 * resource, subtype, list of creatures and soundmanager.
	 * 
	 * @param resourceId
	 * @param type
	 * @param mCreatures
	 * @param soundManager
	 */
	public Tower(int resourceId, int type, Creature[] mCreatures,
			SoundManager soundManager) {
		super(resourceId, TOWER, type);
		this.soundManager = soundManager;
		this.mCreatures = mCreatures;
		rand = new Random();
	}

	public void initTower(int resourceId, int type, Creature[] mCreatures,
			SoundManager soundManager) {
		this.setResourceId(resourceId);
		this.setType(TOWER, type);
		this.soundManager = soundManager;
		this.mCreatures = mCreatures;
		rand = new Random();
	}

	/**
	 * Calculates special damage. Used by all towers that have frost,posion or
	 * fire damage
	 * 
	 * @param tmpCreature
	 * @param aoeTower
	 * @return damage factor
	 */
	private float specialDamage(Creature tmpCreature, boolean aoeTower) {
		// If tower has frostdamage
		if (this.hasFrostDamage) {
			if (aoeTower)
				tmpCreature.affectWithFrost(this.frostTime / 2,
						this.frostAmount);
			else
				tmpCreature.affectWithFrost(this.frostTime, this.frostAmount);
		}
		// If tower has poison damage
		if (this.hasPoisonDamage) {
			if (this.isPoisonTower())
				tmpCreature.affectWithPoison(4, this.minDamage);
			if (this.isPoisonTower())
				tmpCreature.affectWithPoison(4, this.minDamage
						* this.poisonFactor);
		}
		// If tower has firedamage
		if (this.hasFireDamage && !tmpCreature.creatureFireResistant)
			return this.fireFactor;
		else
			return 1;
	}

	// Old tracker
	/*
	 * private Creature trackNearestEnemy(int nbrCreatures) { Creature
	 * targetCreature = null; double lastCreatureDistance = Double.MAX_VALUE;
	 * 
	 * for(int i = 0;i < nbrCreatures; i++ ){ if(mCreatures[i].draw == true &&
	 * mCreatures[i].health > 0){ // Is the creature still alive? double
	 * distance = Math.sqrt( Math.pow((this.relatedShot.x -
	 * (mCreatures[i].getScaledX())) , 2) + Math.pow((this.relatedShot.y -
	 * (mCreatures[i].getScaledY())) , 2) ); if(distance < range){ // Is the
	 * creature within tower range? if (targetCreature == null) targetCreature =
	 * mCreatures[i]; else if (lastCreatureDistance > distance) { targetCreature
	 * = mCreatures[i]; lastCreatureDistance = distance; } } } } return
	 * targetCreature; }
	 */
	/**
	 * Method that tracks a creature. It iterates over a list of creatures and
	 * picks the first creature in the list that is within the range of the
	 * tower
	 * 
	 * @param nbrCreatures
	 * @return nearest creature
	 */
	private Creature trackNearestEnemy() {
		Creature targetCreature = null;
		double lastTraveledDistance = 0;
		double lastCreatureDistanceToTower = Double.MAX_VALUE;

		TrackerList tmpList = startTrackerList;
		while (tmpList != null) {
			TrackerData tmpData = tmpList.data;
			if (tmpData != null) {
				Creature tmpCreature = tmpData.first;
				while ((tmpCreature != tmpData.last) && tmpCreature != null) {

					if (tmpCreature.draw == true
							&& tmpCreature.currentHealth > 0) { // Is the
																// creature
																// still alive?
						double distance = Math.sqrt(Math.pow((this.relatedShot
								.getX() - (tmpCreature.getScaledX())), 2)
								+ Math.pow(
										(this.relatedShot.getY() - (tmpCreature
												.getScaledY()))
												* this.aspectRatio, 2));

						if (distance < range) { // Is the creature within tower
												// range?
							if (targetCreature == null) {
								targetCreature = tmpCreature;
								lastTraveledDistance = tmpCreature.distance;
								lastCreatureDistanceToTower = distance;
							} else {
								// Incase this is a bunker we want to only fire
								// at closest enemy to
								// keep speed at highest possible
								if (this.towerType == Tower.BUNKER) {
									if (lastCreatureDistanceToTower > distance) {
										targetCreature = tmpCreature;
										lastCreatureDistanceToTower = distance;
									}
								} else if (lastTraveledDistance < tmpCreature.distance) {
									targetCreature = tmpCreature;
									lastTraveledDistance = tmpCreature.distance;
								}
							}
						}
					}
					tmpCreature = tmpCreature.nextCreature;
				}
			}
			tmpList = tmpList.next;
		}
		return targetCreature;
	}

	/**
	 * Method that tracks all creatures that are in range of tower. It iterates
	 * over a list of creatures and picks all creatures in range.
	 * 
	 * @param nbrCreatures
	 * @param doFullDamage
	 */
	private int trackAllNearbyEnemies(boolean doFullDamage) {
		int nbrOfHits = 0;
		float range;
		if (doFullDamage)
			range = this.range;
		else
			range = this.rangeAOE;

		TrackerList tmpList = startTrackerList;
		while (tmpList != null) {
			TrackerData tmpData = tmpList.data;
			if (tmpData != null) {
				Creature tmpCreature = tmpData.first;
				while ((tmpCreature != tmpData.last) && tmpCreature != null) {
					if (tmpCreature.draw == true
							&& tmpCreature.currentHealth > 0) { // Is the
																// creature
																// still alive?
						double distance = Math
								.sqrt(Math.pow(
										(this.relatedShot.x - (tmpCreature
												.getScaledX())), 2)
										+ Math.pow(
												(this.relatedShot.y - (tmpCreature
														.getScaledY()))
														* this.aspectRatio, 2));

						if (distance <= range) {
							float randomInt;
							if (doFullDamage) {
								float damageFactor = specialDamage(tmpCreature,
										false);
								randomInt = (rand.nextInt(this.maxDamage
										- this.minDamage) + this.minDamage)
										* damageFactor;
							} else {
								float damageFactor = specialDamage(tmpCreature,
										true);
								randomInt = this.aoeDamage * damageFactor;
							}
							tmpCreature.damage(randomInt, -1,
									this.hasSuper_teleport,
									this.hasSuper_element);
							nbrOfHits++;
						}
					}
					tmpCreature = tmpCreature.nextCreature;
				}
			}
			tmpList = tmpList.next;
		}
		return nbrOfHits;
	}

	// Old tracker
	/*
	 * private int trackAllNearbyEnemies(int nbrCreatures, boolean doFullDamage)
	 * { int nbrOfHits = 0; float range; if (doFullDamage) range = this.range;
	 * else range = this.rangeAOE;
	 * 
	 * for(int i = 0;i < nbrCreatures; i++ ){ if(mCreatures[i].draw == true &&
	 * mCreatures[i].health > 0){ // Is the creature still alive? double
	 * distance = Math.sqrt( Math.pow((this.relatedShot.x -
	 * (mCreatures[i].getScaledX())) , 2) + Math.pow((this.relatedShot.y -
	 * (mCreatures[i].getScaledY())) , 2) ); if(distance <= range){ float
	 * randomInt; if (doFullDamage) { float damageFactor =
	 * specialDamage(mCreatures[i],false); randomInt =
	 * (rand.nextInt(this.maxDamage-this.minDamage) + this.minDamage) *
	 * damageFactor; } else { float damageFactor =
	 * specialDamage(mCreatures[i],true); randomInt = this.aoeDamage *
	 * damageFactor; } mCreatures[i].damage(randomInt,-1); nbrOfHits++; } } }
	 * return nbrOfHits; }
	 */

	/**
	 * Method that calculates the damage for a specific tower depending on the
	 * upgrade level and a random integer so the damage wont be predictable
	 * during game play
	 * 
	 * @param timeDeltaSeconds
	 * @param nbrCreatures
	 */
	private void createProjectileDamage(float timeDeltaSeconds, int nbrCreatures) {
		// First we have to check if the tower is ready to fire
		if (!this.relatedShot.draw && (this.tmpCoolDown <= 0)) {
			// This is happens when a tower with projectile damage is ready to
			// fire.
			this.targetCreature = trackNearestEnemy();
			towerStartFireSequence(this.targetCreature);
		}
		// Creature has been teleported away. stop firing
		else if (this.relatedShot.draw
				&& targetCreatureMapLap != targetCreature.mapLap) {
			this.tmpCoolDown = this.coolDown;
			this.relatedShot.draw = false;
			this.relatedShot.resetShotCordinates();
		} else if (this.relatedShot.draw && ImpactdAnimate) {
			float size = this.relatedShot.getWidth() / 2;
			if (this.towerType == Tower.CANNON)
				size = this.rangeAOE;
			if (this.towerType == Tower.TELSA)
				size = this.targetCreature.getWidth() / 2
						* this.targetCreature.scale;

			ImpactdAnimate = relatedShot.animateShot(timeDeltaSeconds, size,
					targetCreature);
		}
		// if the tower is currently in use:
		else if (this.relatedShot.draw) {
			updateProjectile(timeDeltaSeconds, nbrCreatures);
		}
	}

	/**
	 * Method that updates the shot for the current tower
	 * 
	 * @param timeDeltaSeconds
	 * @param nbrCreatures
	 */
	private void updateProjectile(float timeDeltaSeconds, int nbrCreatures) {

		float yDistance = targetCreature.getScaledY() - this.relatedShot.y
				- (this.relatedShot.getHeight() / 2);
		float xDistance = targetCreature.getScaledX() - this.relatedShot.x
				- (this.relatedShot.getWidth() / 2);
		double xyMovement = (this.velocity * timeDeltaSeconds);

		if (this.towerType == Tower.TELSA || this.towerType == Tower.BUNKER) {
			relatedShot.animateMovingShot(timeDeltaSeconds);
		}

		// This will only happen if we have reached our destination creature
		if ((Math.abs(yDistance) <= xyMovement)
				&& (Math.abs(xDistance) <= xyMovement))
			projectileHitsTarget(nbrCreatures);
		else {
			// Travel until we reach target
			double radian = Math.atan2(yDistance, xDistance);
			this.relatedShot.x += Math.cos(radian) * xyMovement;
			this.relatedShot.y += Math.sin(radian) * xyMovement;
		}
	}

	/**
	 * This runs when a tower hits a creature
	 * 
	 * @param nbrCreatures
	 */
	private void projectileHitsTarget(int nbrCreatures) {
		this.tmpCoolDown = this.coolDown;
		float damageFactor = specialDamage(this.targetCreature, false);
		float randomInt = (rand.nextInt(this.maxDamage - this.minDamage) + this.minDamage)
				* damageFactor;
		targetCreature.damage(randomInt, sound_i, this.hasSuper_teleport,
				this.hasSuper_element);

		this.ImpactdAnimate = true;
		relatedShot.tmpAnimationTime = relatedShot.animationTime;

		if (this.towerType == Tower.CANNON) {
			this.trackAllNearbyEnemies(false);
		}
	}

	/**
	 * Method that calculates AOE damage for a specific tower depending on the
	 * upgrade level and a random integer so the damage wont be predictable
	 * during game play This method is only used by towers with direct aoe
	 * damage. Not to be confused with towers that have projectiledamage
	 */
	private void createPureAOEDamage(int nbrCreatures, float timeDeltaSeconds) {
		if (ImpactdAnimate) {
			float size = this.range;
			ImpactdAnimate = relatedShot.animateShot(timeDeltaSeconds, size,
					null);
		} else if (this.tmpCoolDown <= 0) {
			if (trackAllNearbyEnemies(true) > 0) {
				soundManager.playSound(this.sound_l);
				this.tmpCoolDown = this.coolDown;
				this.relatedShot.draw = true;
				ImpactdAnimate = true;
			}
		}
	}

	/**
	 * Main function of the tower class. Find and kill creatures
	 * 
	 * @param timeDeltaSeconds
	 * @param nbrCreatures
	 */
	public void attackCreatures(float timeDeltaSeconds, int nbrCreatures) {
		// Decrease the coolDown variable
		this.tmpCoolDown = this.tmpCoolDown - timeDeltaSeconds;

		// This code is used by towers firing pure aoe damage.
		if (this.towerType == Tower.AOE) {
			createPureAOEDamage(nbrCreatures, timeDeltaSeconds);
		}
		// If bunker, cannon or tesla
		else {
			createProjectileDamage(timeDeltaSeconds, nbrCreatures);
		}
	}

	/**
	 * Init fire sequence. Show projectile and play sound
	 * 
	 * @param targetCreature
	 */
	private void towerStartFireSequence(Creature targetCreature) {
		if (targetCreature != null) {
			if (sound_l != -1)
				soundManager.playSound(sound_l);
			this.relatedShot.draw = true;
			this.targetCreatureMapLap = targetCreature.mapLap;
		}
	}

	/**
	 * Given all variable this method will create a exaxt copy of another tower
	 */
	public void cloneTower(int resourceId, int towerType, int towerTypeId,
			float range, float rangeAOE, int price, int resellPrice,
			int minDamage, int maxDamage, int aoeDamage, int velocity,
			int upgradeLvl, float coolDown, float width, float height,
			Shot copyShot, int sound_l, int sound_i) {

		this.setResourceId(resourceId);
		this.towerType = towerType;
		this.towerTypeId = towerTypeId;
		this.range = range;
		this.rangeAOE = rangeAOE;
		this.price = price;
		this.resellPrice = resellPrice;
		this.minDamage = minDamage;
		this.maxDamage = maxDamage;
		this.aoeDamage = aoeDamage;
		this.velocity = velocity;
		this.upgradeLvl = upgradeLvl;
		this.coolDown = coolDown;
		this.setWidth(width);
		this.setHeight(height);
		this.relatedShot.setWidth(copyShot.getWidth());
		this.relatedShot.setHeight(copyShot.getHeight());
		this.relatedShot.setResourceId(copyShot.getResourceId());
		this.relatedShot.setAnimationTime(copyShot.getAnimationTime());
		this.sound_l = sound_l;
		this.sound_i = sound_i;
	}

	/**
	 * Create new tower and place on map
	 * 
	 * @param clone
	 * @param towerPlacement
	 * @param mScaler
	 */
	public void createTower(Tower clone, Coords towerPlacement, Scaler mScaler,
			Tracker tracker, boolean upgrade) {
		this.draw = false;

		// Use the textureNames that we preloaded into the towerTypes at startup
		// If this is a new created tower we have to manually reset the folowing
		// values
		this.towerTypeId = clone.towerTypeId;

		if (!upgrade) {
			this.towerType = clone.towerType;
			this.relatedShot.setResourceId(clone.relatedShot.getResourceId());
			this.sound_l = clone.sound_l;
			this.sound_i = clone.sound_i;
			this.x = towerPlacement.x;
			this.y = towerPlacement.y;
		}
		this.setResourceId(clone.getResourceId());
		this.range = clone.range;
		this.rangeAOE = clone.rangeAOE;
		this.price = clone.price;
		this.resellPrice = clone.resellPrice;
		this.minDamage = clone.minDamage;
		this.maxDamage = clone.maxDamage;
		this.aoeDamage = clone.aoeDamage;
		this.velocity = clone.velocity;
		this.upgradeLvl = clone.upgradeLvl;
		this.coolDown = clone.coolDown;
		this.relatedShot.setAnimationTime(clone.relatedShot.getAnimationTime());

		// Special abilities. If this is a new created tower we have to manually
		// reset the folowing values
		if (!upgrade) {

			if (this.towerType != Tower.AOE) {
				this.hasPoisonDamage = false;
			} else {
				this.hasPoisonDamage = true;
			}
			this.hasFrostDamage = false;
			this.frostAmount = 0;
			this.frostTime = 0;
			this.hasFireDamage = false;
			this.fireFactor = 0;
			this.setUpgradeFire(0);
			this.setUpgradeFrost(0);
			this.setUpgradePoison(0);
			this.r = 1;
			this.g = 1;
			this.b = 1;
			this.relatedShot.r = 1;
			this.relatedShot.g = 1;
			this.relatedShot.b = 1;
			this.hasSuper_element = false;
			this.hasSuper_teleport = false;
		}

		// Tracker
		int rangeGrid = mScaler.rangeGrid((int) (this.range + this.rangeAOE));
		Coords tmp = mScaler.getGridXandY((int) x, (int) y);
		int right = tmp.x + rangeGrid;
		if (right >= (mScaler.getGridWidth() + 2))
			right = mScaler.getGridWidth() + 1;
		int left = tmp.x - rangeGrid;
		if (left < 0)
			left = 0;
		int top = tmp.y + rangeGrid;
		if (top >= (mScaler.getGridHeight() + 2))
			top = mScaler.getGridHeight() + 1;
		int bottom = tmp.y - rangeGrid;
		if (bottom < 0)
			bottom = 0;

		startTrackerList = new TrackerList();
		TrackerList currentTrackerList = new TrackerList();
		startTrackerList.next = currentTrackerList;

		for (int x = left; x <= right; x++) {
			for (int y = bottom; y <= top; y++) {
				currentTrackerList.data = (tracker.getTrackerData(x, y));
				currentTrackerList.next = new TrackerList();
				currentTrackerList = currentTrackerList.next;
			}
		}

		this.aspectRatio = mScaler.aspectRatio();

		this.draw = true;
		this.relatedShot.resetShotCordinates();// Same location of Shot as
												// midpoint of Tower
		this.relatedShot.draw = false;

	}

	public int upgradeSpecialAbility(UpgradeOption opt, int money) {
		int price = 0;

		if (opt == UpgradeOption.upgrade_fire) {
			if (this.getUpgradeFire() == 0 && money >= 30) {
				this.fireFactor = 1.8f;
				this.hasFireDamage = true;
				this.r = 1;
				this.g = 0.8f;
				this.b = 0.8f;
				this.relatedShot.r = 1;
				this.relatedShot.g = 0.6f;
				this.relatedShot.b = 0.6f;
				this.setUpgradeFire(this.getUpgradeFire() + 1);
				price = 30;
			} else if (this.getUpgradeFire() == 1 && money >= 60) {
				this.r = 1;
				this.g = 0.7f;
				this.b = 0.7f;
				this.fireFactor = 3;
				this.setUpgradeFire(this.getUpgradeFire() + 1);
				price = 60;
			} else if (this.getUpgradeFire() == 2 && money >= 90) {
				this.r = 1;
				this.g = 0.6f;
				this.b = 0.6f;
				this.fireFactor = 4;
				this.setUpgradeFire(this.getUpgradeFire() + 1);
				price = 90;
			} else if (this.getUpgradeFire() == 3 && money >= 180) {
				this.r = 1;
				this.g = 0.5f;
				this.b = 0.5f;
				this.fireFactor = 8;
				this.setUpgradeFire(this.getUpgradeFire() + 1);
				price = 180;
			} else if (this.getUpgradeFire() == 4 && money >= 360) {
				this.r = 1;
				this.g = 0.4f;
				this.b = 0.4f;
				this.fireFactor = 16;
				this.setUpgradeFire(this.getUpgradeFire() + 1);
				price = 360;
			}
		} else if (opt == UpgradeOption.upgrade_frost) {
			if (this.getUpgradeFrost() == 0 && money >= 30) {
				this.relatedShot.setResourceId(R.drawable.cannonshot_ice);
				this.frostTime = 3;
				this.frostAmount = 0.7f;
				this.hasFrostDamage = true;
				this.r = 0.8f;
				this.g = 0.8f;
				this.b = 1;
				this.setUpgradeFrost(this.getUpgradeFrost() + 1);
				price = 30;
			} else if (this.getUpgradeFrost() == 1 && money >= 60) {
				this.frostTime = 4;
				this.frostAmount = 0.6f;
				this.r = 0.7f;
				this.g = 0.7f;
				this.b = 1;
				this.setUpgradeFrost(this.getUpgradeFrost() + 1);
				price = 60;
			} else if (this.getUpgradeFrost() == 2 && money >= 90) {
				this.frostTime = 5;
				this.frostAmount = 0.5f;
				this.r = 0.6f;
				this.g = 0.6f;
				this.b = 1;
				this.setUpgradeFrost(this.getUpgradeFrost() + 1);
				price = 90;
			} else if (this.getUpgradeFrost() == 3 && money >= 180) {
				this.frostTime = 6;
				this.frostAmount = 0.4f;
				this.r = 0.5f;
				this.g = 0.5f;
				this.b = 1;
				this.setUpgradeFrost(this.getUpgradeFrost() + 1);
				price = 180;
			} else if (this.getUpgradeFrost() == 4 && money >= 360) {
				this.frostTime = 7;
				this.frostAmount = 0.3f;
				this.r = 0.4f;
				this.g = 0.4f;
				this.b = 1;
				this.setUpgradeFrost(this.getUpgradeFrost() + 1);
				price = 360;
			}

		} else if (opt == UpgradeOption.upgrade_poison) {
			if (this.getUpgradePoison() == 0 && money >= 30) {
				this.hasPoisonDamage = true;
				this.poisonFactor = 0.2f;
				this.r = 0.8f;
				this.g = 1;
				this.b = 0.8f;
				this.relatedShot.r = 0.6f;
				this.relatedShot.g = 1;
				this.relatedShot.b = 0.6f;
				this.setUpgradePoison(this.getUpgradePoison() + 1);
				price = 30;
			} else if (this.getUpgradePoison() == 1 && money >= 60) {
				this.poisonFactor = 0.5f;
				this.r = 0.7f;
				this.g = 1;
				this.b = 0.7f;
				this.setUpgradePoison(this.getUpgradePoison() + 1);
				price = 60;
			} else if (this.getUpgradePoison() == 2 && money >= 90) {
				this.poisonFactor = 1;
				this.r = 0.6f;
				this.g = 1;
				this.b = 0.6f;
				this.setUpgradePoison(this.getUpgradePoison() + 1);
				price = 90;
			} else if (this.getUpgradePoison() == 3 && money >= 180) {
				this.poisonFactor = 2;
				this.r = 0.5f;
				this.g = 1;
				this.b = 0.5f;
				this.setUpgradePoison(this.getUpgradePoison() + 1);
				price = 180;
			} else if (this.getUpgradePoison() == 4 && money >= 360) {
				this.poisonFactor = 4;
				this.r = 0.4f;
				this.g = 1;
				this.b = 0.4f;
				this.setUpgradePoison(this.getUpgradePoison() + 1);
				price = 360;
			}
		}
		return price;
	}

	public int upgradeSuperAbility(int money, Sprite[] mSpecialTowers) {
		int price = 0;
		if (money >= 100) {
			if (this.towerType == Tower.AOE) {
				this.hasSuper_element = true;
				if (this.upgradeLvl == 6) {
					this.setCurrentTexture(mSpecialTowers[3]
							.getCurrentTexture());
					this.setResourceId(R.drawable.poisontower_special_1);
				} else if (this.upgradeLvl == 10) {
					this.setCurrentTexture(mSpecialTowers[4]
							.getCurrentTexture());
					this.setResourceId(R.drawable.poisontower_special_2);
				} else if (this.upgradeLvl == -1) {
					this.setCurrentTexture(mSpecialTowers[5]
							.getCurrentTexture());
					this.setResourceId(R.drawable.poisontower_special_3);
				}
				this.relatedShot.r = 0.7f;
				this.relatedShot.g = 0.7f;
				this.relatedShot.b = 1f;
				price = 100;
			}
			if (this.towerType == Tower.TELSA) {
				this.hasSuper_teleport = true;
				if (this.upgradeLvl == 7) {
					this.setCurrentTexture(mSpecialTowers[0]
							.getCurrentTexture());
					this.setResourceId(R.drawable.tesla_special_1);
				} else if (this.upgradeLvl == 11) {
					this.setCurrentTexture(mSpecialTowers[1]
							.getCurrentTexture());
					this.setResourceId(R.drawable.tesla_special_2);
				} else if (this.upgradeLvl == -1) {
					this.setCurrentTexture(mSpecialTowers[2]
							.getCurrentTexture());
					this.setResourceId(R.drawable.tesla_special_3);
				}
				this.relatedShot.r = 1f;
				this.relatedShot.g = 1f;
				this.relatedShot.b = 0.7f;
				price = 100;
			}
		}
		return price;
	}

	// ////////////////////////////////////////////
	// Getter for tower
	// ////////////////////////////////////////////

	public int getTowerType() {
		return this.towerType;
	}

	/**
	 * Given a tower this method will create a new tower with the same variables
	 * as the given one
	 * 
	 * @return
	 */

	/**
	 * Return range of this tower
	 * 
	 * @return
	 */
	public float getRange() {
		return this.range;
	}

	/**
	 * Return minimum damage
	 * 
	 * @return
	 */
	public float getMinDamage() {
		return this.minDamage;
	}

	/**
	 * Return maximum damage
	 * 
	 * @return
	 */
	public float getMaxDamage() {
		return this.maxDamage;
	}

	/**
	 * Returns the cost of this tower
	 * 
	 * @return price
	 */
	public int getPrice() {
		return price;
	}

	public int getResellPrice() {
		int resell = 0;
		if (this.upgradeFire == 1 || this.upgradeFrost == 1
				|| this.upgradePoison == 1) {
			resell = 15;
		}
		if (this.upgradeFire == 2 || this.upgradeFrost == 2
				|| this.upgradePoison == 2) {
			resell = 30;
		}
		if (this.upgradeFire == 3 || this.upgradeFrost == 3
				|| this.upgradePoison == 3) {
			resell = 45;
		}
		if (this.upgradeFire == 4 || this.upgradeFrost == 4
				|| this.upgradePoison == 4) {
			resell = 90;
		}
		if (this.upgradeFire == 5 || this.upgradeFrost == 5
				|| this.upgradePoison == 5) {
			resell = 180;
		}

		if (hasSuper_teleport || hasSuper_element)
			resell = 50;

		return resell + this.resellPrice;
	}

	public int[] getUpgradeTypeIndex(Tower[] towerTypes,
			boolean specialupgrade_teleport,
			boolean specialupgrade_element_remove) {
		int[] Upgrade = new int[11];

		if (upgradeLvl == -1)
			Upgrade[0] = -1;
		else if (upgradeLvl < 8)
			Upgrade[0] = 1;
		else if (upgradeLvl < 12)
			Upgrade[0] = 2;
		else
			Upgrade[0] = -1;

		if (Upgrade[0] != -1) {
			Upgrade[1] = towerTypes[upgradeLvl].price;
		}

		if (getUpgradePoison() == 0 && getUpgradeFrost() == 0
				&& (towerType == Tower.BUNKER || towerType == Tower.CANNON))
			if (upgradeLvl > 7 || upgradeLvl == -1) {
				Upgrade[2] = getUpgradeFire();
				if (getUpgradeFire() == 0)
					Upgrade[3] = 30;
				if (getUpgradeFire() == 1)
					Upgrade[3] = 60;
				if (getUpgradeFire() == 2)
					Upgrade[3] = 90;
				if (getUpgradeFire() == 3)
					Upgrade[3] = 180;
				if (getUpgradeFire() == 4)
					Upgrade[3] = 360;

			} else
				Upgrade[2] = -1;
		else
			Upgrade[2] = -1;

		if (getUpgradePoison() == 0 && getUpgradeFire() == 0
				&& towerType == Tower.CANNON)
			if (upgradeLvl > 7 || upgradeLvl == -1) {
				Upgrade[4] = getUpgradeFrost();
				if (getUpgradeFrost() == 0)
					Upgrade[5] = 30;
				if (getUpgradeFrost() == 1)
					Upgrade[5] = 60;
				if (getUpgradeFrost() == 2)
					Upgrade[5] = 90;
				if (getUpgradeFrost() == 3)
					Upgrade[5] = 180;
				if (getUpgradeFrost() == 4)
					Upgrade[5] = 360;

			} else
				Upgrade[4] = -1;
		else
			Upgrade[4] = -1;

		if (getUpgradeFrost() == 0 && getUpgradeFire() == 0
				&& towerType == Tower.BUNKER)
			if (upgradeLvl > 7 || upgradeLvl == -1) {
				Upgrade[6] = getUpgradePoison();
				if (getUpgradePoison() == 0)
					Upgrade[7] = 30;
				if (getUpgradePoison() == 1)
					Upgrade[7] = 60;
				if (getUpgradePoison() == 2)
					Upgrade[7] = 90;
				if (getUpgradePoison() == 3)
					Upgrade[7] = 180;
				if (getUpgradePoison() == 4)
					Upgrade[7] = 360;
			} else
				Upgrade[6] = -1;
		else
			Upgrade[6] = -1;

		if (!specialupgrade_teleport && towerType == Tower.TELSA) {
			Upgrade[8] = 1;
			Upgrade[9] = 100;
		}

		if (!specialupgrade_element_remove && towerType == Tower.AOE) {
			Upgrade[8] = 1;
			Upgrade[9] = 100;
		}

		if (specialupgrade_element_remove && this.hasSuper_element) {
			Upgrade[8] = 2;
		}
		if (specialupgrade_teleport && this.hasSuper_teleport) {
			Upgrade[8] = 3;
		}

		Upgrade[10] = this.getResellPrice();

		return Upgrade;
	}

	public boolean isPoisonTower() {
		return this.hasPoisonDamage;
	}

	public boolean isFrostTower() {
		return this.hasFrostDamage;
	}

	public boolean isFireTower() {
		return this.hasFireDamage;
	}

	public int getUpgradeTowerLvl() {
		return this.upgradeLvl;
	}

	/**
	 * @param upgradeFire
	 *            the upgradeFire to set
	 */
	public void setUpgradeFire(int upgradeFire) {
		this.upgradeFire = upgradeFire;
	}

	/**
	 * @return the upgradeFire
	 */
	public int getUpgradeFire() {
		return upgradeFire;
	}

	public int getUpgradeSuper() {
		if (hasSuper_teleport) {
			return 1;
		} else if (hasSuper_element) {
			return 2;
		}

		return 0;
	}

	/**
	 * @param upgradeFrost
	 *            the upgradeFrost to set
	 */
	public void setUpgradeFrost(int upgradeFrost) {
		this.upgradeFrost = upgradeFrost;
	}

	/**
	 * @return the upgradeFrost
	 */
	public int getUpgradeFrost() {
		return upgradeFrost;
	}

	/**
	 * @param upgradePoison
	 *            the upgradePoison to set
	 */
	public void setUpgradePoison(int upgradePoison) {
		this.upgradePoison = upgradePoison;
	}

	/**
	 * @return the upgradePoison
	 */
	public int getUpgradePoison() {
		return upgradePoison;
	}

	/**
	 * @return the towertype
	 */
	public int getTowerTypeId() {
		return this.towerTypeId;
	}

	/**
	 * @return true if this tower has a element superupgrade
	 */
	public boolean getSuperElement() {
		return this.hasSuper_element;
	}

	/**
	 * @return true if this tower has a teleport superupgrade
	 */
	public boolean getSuperTeleport() {
		return this.hasSuper_teleport;
	}

	public float getAoeDamage() {
		return this.aoeDamage;
	}

}