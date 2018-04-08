package bbth.game.achievements.impls;

import bbth.engine.achievements.AchievementInfo;
import bbth.engine.achievements.Achievements;
import bbth.game.achievements.BBTHAchievement;
import bbth.game.achievements.events.GameEndedEvent;

public class UltimateStalemate extends BBTHAchievement {

	public UltimateStalemate(AchievementInfo achievementInfo) {
		super(achievementInfo);
	}

	@Override
	public void gameEnded(GameEndedEvent e) {
		if (e.isTie() && e.getBeatTrack().getSongLength() > 120000) {
			Achievements.INSTANCE.increment(achievementInfo);
		}
	}

	@Override
	public boolean usesGameEnded() {
		return true;
	}

}
