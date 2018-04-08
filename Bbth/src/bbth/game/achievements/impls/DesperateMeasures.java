package bbth.game.achievements.impls;

import bbth.engine.achievements.AchievementInfo;
import bbth.engine.achievements.Achievements;
import bbth.game.achievements.BBTHAchievement;
import bbth.game.achievements.events.BaseDestroyedEvent;

public class DesperateMeasures extends BBTHAchievement {

	public DesperateMeasures(AchievementInfo achievementInfo) {
		super(achievementInfo);
	}

	@Override
	public void baseDestroyed(BaseDestroyedEvent e) {
		if (e.getLocalPlayer() != e.getDestroyedBaseOwner()
				&& e.getBeatTrack().getSongLength() < e.getBeatTrack()
						.getCurrPosition() + 10000) {
			Achievements.INSTANCE.increment(achievementInfo);
		}
	}

	@Override
	public boolean usesBaseDestroyed() {
		return true;
	}

}
