package bbth.game.achievements;

import bbth.engine.achievements.Achievement;
import bbth.engine.achievements.AchievementInfo;
import bbth.engine.achievements.Achievements;
import bbth.game.achievements.events.BaseDestroyedEvent;
import bbth.game.achievements.events.BeatHitEvent;
import bbth.game.achievements.events.BeatMissedEvent;
import bbth.game.achievements.events.GameEndedEvent;
import bbth.game.achievements.events.UnitCreatedEvent;
import bbth.game.achievements.events.UnitDeadEvent;
import bbth.game.achievements.events.UpdateEvent;
import bbth.game.achievements.events.WallCreatedEvent;

public abstract class BBTHAchievement extends Achievement {

	public BBTHAchievement(AchievementInfo achievementInfo) {
		super(achievementInfo);
	}

	protected void increment() {
		if (Achievements.INSTANCE.increment(achievementInfo))
			BBTHAchievementManager.INSTANCE
					.unregisterAchievementFromEvents(this);
	}

	public void baseDestroyed(BaseDestroyedEvent e) {
	}

	public void gameEnded(GameEndedEvent e) {
	}

	public void unitDead(UnitDeadEvent e) {
	}

	public void unitCreated(UnitCreatedEvent e) {
	}

	public void beatHit(BeatHitEvent e) {
	}

	public void beatMissed(BeatMissedEvent e) {
	}

	public void wallCreated(WallCreatedEvent e) {
	}

	public void update(UpdateEvent e) {
	}

	public boolean usesBaseDestroyed() {
		return false;
	}

	public boolean usesGameEnded() {
		return false;
	}

	public boolean usesUnitDead() {
		return false;
	}

	public boolean usesUnitCreated() {
		return false;
	}

	public boolean usesBeatHit() {
		return false;
	}

	public boolean usesBeatMissed() {
		return false;
	}

	public boolean usesWallCreated() {
		return false;
	}

	public boolean usesUpdate() {
		return false;
	}
}
