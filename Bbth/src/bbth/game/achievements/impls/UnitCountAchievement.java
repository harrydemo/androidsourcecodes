package bbth.game.achievements.impls;

import bbth.engine.achievements.AchievementInfo;
import bbth.engine.achievements.Achievements;
import bbth.game.achievements.BBTHAchievement;
import bbth.game.achievements.events.UnitCreatedEvent;

public class UnitCountAchievement extends BBTHAchievement {

	private int m_unit_threshold;

	public UnitCountAchievement(AchievementInfo achievementInfo,
			int unit_threshold) {
		super(achievementInfo);

		m_unit_threshold = unit_threshold;
	}

	@Override
	public void unitCreated(UnitCreatedEvent e) {
		if (e.getLocalPlayer().getUnits().size() >= m_unit_threshold) {
			Achievements.INSTANCE.increment(achievementInfo);
		}
	}

	@Override
	public boolean usesUnitCreated() {
		return true;
	}

}
