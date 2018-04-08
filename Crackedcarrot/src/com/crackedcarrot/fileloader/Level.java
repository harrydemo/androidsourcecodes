package com.crackedcarrot.fileloader;

import com.crackedcarrot.Creature;
import com.crackedcarrot.Sprite;
import com.crackedcarrot.menu.R;

// Class contains level information. Number of creatures creature type etc.
public class Level extends Creature {
	public int nbrCreatures;
	public String creepTitle;

	// Constructor
	public Level(int resourceId) {
		// Change subtype and numer of frames during load
		super(resourceId, 0, null, null, null, null, 0, null);
	}

	public float getHealth() {
		return startHealth;
	}

	public void cloneCreature(Creature clone) {
		clone.setResourceId(this.getResourceId());
		clone.setDeadResourceId(this.getDeadResourceId());
		clone.setDeadTexture(this.getDeadTexture());
		clone.creatureFast = this.isCreatureFast();
		clone.creatureFireResistant = this.creatureFireResistant;
		clone.creatureFrostResistant = this.creatureFrostResistant;
		clone.creaturePoisonResistant = this.creaturePoisonResistant;

		clone.setCurrentHealth(this.startHealth);
		clone.setStartHealth(this.startHealth);
		clone.setVelocity(this.velocity);
		clone.setBaseVelocity(this.basevelocity);

		clone.setWidth(this.getWidth());
		clone.setHeight(this.getHeight());

		Sprite healthBar = clone.getHealthBar();
		healthBar.draw = false;
		healthBar.x = 0;
		healthBar.y = 0;
		healthBar.setHeight(8);
		healthBar.setWidth(this.getWidth() * this.scale);
		healthBar.r = 0;
		healthBar.g = 1;
		healthBar.b = 0;

		clone.setGoldValue(this.goldValue);
		clone.draw = false;
		clone.opacity = 1;

		clone.creatureFrozenTime = 0;
		clone.creaturePoisonTime = 0;
		clone.setRGB(this.rDefault, this.gDefault, this.bDefault);
		clone.setDead(false);
		clone.setAllDead(false);
		clone.scale = this.scale;
		clone.setDisplayResourceId(this.getDisplayResourceId());
		clone.setAnimationTime(this.isCreatureFast());
		clone.moveToWaypoint(0);
		clone.setSpawnPoint();
		clone.setNextWayPoint(1);
		clone.setDamagePerCreep(this.getDamagePerCreep());
		clone.setSurvivalMode(this.getSurvivalMode());
	}
}