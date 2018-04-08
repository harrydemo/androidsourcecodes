package bbth.game.achievements.impls;

import bbth.engine.achievements.AchievementInfo;
import bbth.engine.sound.Beat;
import bbth.game.BeatTrack;
import bbth.game.achievements.BBTHAchievement;
import bbth.game.achievements.events.GameEndedEvent;

public class FullComboAchievement extends BBTHAchievement {

	public FullComboAchievement(AchievementInfo achievementInfo) {
		super(achievementInfo);
	}

	@Override
	public void gameEnded(GameEndedEvent e) {
		BeatTrack track = e.getBeatTrack();
		if (track == null) {
			System.err.println("Error: no beat track."); //$NON-NLS-1$
			return;
		}
		boolean tappedall = true;
		for (Beat beat : track.getAllBeats()) {
			if (!beat.isTapped()) {
				tappedall = false;
				break;
			}
		}

		if (tappedall) {
			increment();
		}
	}

	@Override
	public boolean usesGameEnded() {
		return true;
	}

}
