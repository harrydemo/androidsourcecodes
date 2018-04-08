package bbth.game.achievements.impls;

import bbth.engine.achievements.AchievementInfo;
import bbth.game.achievements.BBTHAchievement;
import bbth.game.achievements.events.UnitCreatedEvent;

public class IncrementOnUnitCreateAchievement extends BBTHAchievement {

	public IncrementOnUnitCreateAchievement(AchievementInfo achievementInfo) {
		super(achievementInfo);
	}

	@Override
	public void unitCreated(UnitCreatedEvent e) {
		if (e.getLocalPlayer().getTeam() == e.getUnit().getTeam()) {
			increment();
		}
	}

	@Override
	public boolean usesUnitCreated() {
		return true;
	}

}
