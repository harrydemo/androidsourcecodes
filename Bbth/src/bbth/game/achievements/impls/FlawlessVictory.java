package bbth.game.achievements.impls;

import bbth.engine.achievements.AchievementInfo;
import bbth.game.achievements.BBTHAchievement;
import bbth.game.achievements.events.BaseDestroyedEvent;

public class FlawlessVictory extends BBTHAchievement {
	public FlawlessVictory(AchievementInfo achievementInfo) {
		super(achievementInfo);
	}

	@Override
	public void baseDestroyed(BaseDestroyedEvent e) {
		if (!e.getDestroyedBaseOwner().isLocal()
				&& e.getLocalPlayer().getHealth() >= 100f) {
			increment();
		}
	}

	@Override
	public boolean usesBaseDestroyed() {
		return super.usesBaseDestroyed();
	}

}
