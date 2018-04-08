package bbth.game.achievements.impls;

import bbth.engine.achievements.AchievementInfo;
import bbth.game.achievements.BBTHAchievement;
import bbth.game.achievements.events.BaseDestroyedEvent;

public class DesperateTimes extends BBTHAchievement {
	public DesperateTimes(AchievementInfo achievementInfo) {
		super(achievementInfo);
	}

	@Override
	public void baseDestroyed(BaseDestroyedEvent e) {
		if (!e.getDestroyedBaseOwner().isLocal()
				&& e.getLocalPlayer().getHealth() < 10f) {
			increment();
		}
	}

	@Override
	public boolean usesBaseDestroyed() {
		return super.usesBaseDestroyed();
	}

}
