package bbth.game.achievements.impls;

import bbth.engine.achievements.AchievementInfo;
import bbth.game.achievements.BBTHAchievement;
import bbth.game.achievements.events.GameEndedEvent;

public class AnythingYouCanDo extends BBTHAchievement {

	public AnythingYouCanDo(AchievementInfo achievementInfo) {
		super(achievementInfo);
	}

	@Override
	public void gameEnded(GameEndedEvent e) {
		if (e.isSingleplayer() && e.getLocalPlayer() == e.getWinningPlayer()
				&& e.getAiDifficulty() >= 1f) {
			increment();
		}
	}

	@Override
	public boolean usesGameEnded() {
		return true;
	}

}
