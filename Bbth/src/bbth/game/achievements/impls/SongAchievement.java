package bbth.game.achievements.impls;

import bbth.engine.achievements.AchievementInfo;
import bbth.game.Song;
import bbth.game.achievements.BBTHAchievement;
import bbth.game.achievements.events.GameEndedEvent;

public class SongAchievement extends BBTHAchievement {
	Song song;

	public SongAchievement(AchievementInfo achievementInfo, Song song) {
		super(achievementInfo);
		this.song = song;
	}

	@Override
	public void gameEnded(GameEndedEvent e) {
		if (e.getSong() == song && !e.isTie() && e.getWinningPlayer().isLocal()) {
			increment();
		}
	}

	@Override
	public boolean usesGameEnded() {
		return true;
	}

}
