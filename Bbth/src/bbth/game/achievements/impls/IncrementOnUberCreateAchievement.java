package bbth.game.achievements.impls;

import bbth.engine.achievements.AchievementInfo;
import bbth.game.achievements.BBTHAchievement;
import bbth.game.achievements.events.UnitCreatedEvent;
import bbth.game.units.UnitType;

public class IncrementOnUberCreateAchievement extends BBTHAchievement {

	public IncrementOnUberCreateAchievement(AchievementInfo achievementInfo) {
		super(achievementInfo);
	}

	@Override
	public void unitCreated(UnitCreatedEvent e) {
		if (e.getUnitType() == UnitType.UBER
				&& e.getLocalPlayer().getTeam() == e.getUnit().getTeam()) {
			increment();
		}
	}

	@Override
	public boolean usesUnitCreated() {
		return true;
	}

}
