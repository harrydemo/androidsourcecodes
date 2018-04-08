package bbth.game.achievements.impls;

import bbth.engine.achievements.AchievementInfo;
import bbth.game.achievements.BBTHAchievement;
import bbth.game.achievements.events.BeatHitEvent;

public class ComboCounterAchievement extends BBTHAchievement {

	private int m_threshold;

	public ComboCounterAchievement(AchievementInfo achievementInfo,
			int threshold) {
		super(achievementInfo);
		m_threshold = threshold;
	}

	@Override
	public void beatHit(BeatHitEvent e) {
		if (e.getCombo() >= m_threshold
				&& e.getHittingPlayer().getTeam() == e.getLocalPlayer()
						.getTeam()) {
			increment();
		}
	}

	@Override
	public boolean usesBeatHit() {
		return true;
	}

}
