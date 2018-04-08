package com.crackedcarrot;

/**
 * Class defining a shot in the game. This can be both a projectile and a aoen
 * */
public class Shot extends Sprite {
	// The tower object which the shot belongs to
	public Tower tower;
	// Varibles used to calculate animation time for a shot
	public float animationTime;
	public float tmpAnimationTime;

	public Shot(int resourceId, int type, Tower tower) {
		super(resourceId, SHOT, type);
		this.tower = tower;
		super.draw = false;
	}

	/**
	 * Method that places a shot back to the start position
	 */
	public void resetShotCordinates() {
		x = tower.x + tower.getWidth() / 2 - this.getWidth() / 2;
		y = tower.y + tower.getHeight() / 2 - this.getHeight() / 2;
	}

	public float getX() {
		return x + this.getWidth() / 2;
	}

	public float getY() {
		return y + this.getHeight() / 2;
	}

	/**
	 * Shows the animation for a shot. Requiers system time, size of animation
	 * and target creature if any.
	 * 
	 * @param timeDeltaSeconds
	 * @param size
	 * @param targetCreature
	 * @return false if animateShot dont need to be run again
	 */
	public boolean animateShot(float timeDeltaSeconds, float size,
			Creature targetCreature) {
		tmpAnimationTime -= timeDeltaSeconds;

		if (tmpAnimationTime <= 0) {
			this.draw = false;
			this.resetShotCordinates();
			this.scale = 1;
			this.cFrame = 0;
			tmpAnimationTime = this.animationTime;
			return false;
		} else {
			if (targetCreature != null) {
				this.x = targetCreature.getScaledX() - this.getWidth() / 2;
				this.y = targetCreature.getScaledY() - this.getHeight() / 2;
			} else
				this.resetShotCordinates();
			if (this.tower.towerType == Tower.BUNKER
					|| this.tower.towerType == Tower.TELSA)
				cFrame = (int) (((1 - (tmpAnimationTime / animationTime)) * (this
						.getNbrOfFrames() / 2))) + (this.getNbrOfFrames() / 2);
			else {
				cFrame = (int) (((1 - (tmpAnimationTime / animationTime)) * (this
						.getNbrOfFrames() - 1))) + 1;
				if (cFrame == 0) {
					cFrame = 1;
				}
			}
			scaleSprite(size);
			return true;
		}
	}

	/**
	 * Shows the moving animation for a shot. Requires system time,
	 * 
	 * @param timeDeltaSeconds
	 */
	public void animateMovingShot(float timeDeltaSeconds) {
		tmpAnimationTime -= timeDeltaSeconds * 5;
		if (tmpAnimationTime <= 0) {
			this.cFrame = 0;
			tmpAnimationTime = this.animationTime;
		} else {
			cFrame = (int) (((1 - (tmpAnimationTime / animationTime)) * (this
					.getNbrOfFrames() / 2)));
		}
	}

	/**
	 * Will return how long time a animation from this shot runs
	 * 
	 * @return time(float)
	 */
	public float getAnimationTime() {
		return animationTime;
	}

	/**
	 * Set how long time a animation from this shot runs
	 * 
	 * @param animationTime
	 */
	public void setAnimationTime(float animationTime) {
		this.animationTime = animationTime;
		this.tmpAnimationTime = animationTime;
	}

}