package bbth.game.achievements.impls;

import bbth.engine.achievements.AchievementInfo;
import bbth.engine.achievements.Achievements;
import bbth.game.achievements.BBTHAchievement;
import bbth.game.achievements.events.GameEndedEvent;

public class StrategistAchievement extends BBTHAchievement {

	public StrategistAchievement(AchievementInfo achievementInfo) {
		super(achievementInfo);
	}

	@Override
	public void gameEnded(GameEndedEvent e) {
		if (e.getLocalPlayer() == e.getWinningPlayer()
				&& e.getLocalPlayer().getTotalUnitsCreated() < e
						.getRemotePlayer().getTotalUnitsCreated()) {
			Achievements.INSTANCE.increment(achievementInfo);
		}
	}

	@Override
	public boolean usesGameEnded() {
		return true;
	}

}
