package bbth.game.achievements.impls;

import bbth.engine.achievements.AchievementInfo;
import bbth.engine.util.Bag;
import bbth.game.Team;
import bbth.game.achievements.BBTHAchievement;
import bbth.game.achievements.events.UnitCreatedEvent;
import bbth.game.units.Unit;

public class YoDawgIHeardYouLikeUnits extends BBTHAchievement {

	public YoDawgIHeardYouLikeUnits(AchievementInfo achievementInfo) {
		super(achievementInfo);
	}

	@Override
	public void unitCreated(UnitCreatedEvent e) {
		Unit unit = e.getUnit();
		Team team = e.getLocalPlayer().getTeam();
		if (team == unit.getTeam()) {
			// getUnitsInCircle() won't return this unit because it has just
			// been created, and is not in the acceleration data structure yet
			// (it will be added on the next frame)
			Bag<Unit> otherUnits = e.getUnitsInCircle(unit.getX(), unit.getY(),
					unit.getRadius());
			for (Unit otherUnit : otherUnits) {
				if (team.isEnemy(otherUnit.getTeam())) {
					increment();
					break;
				}
			}
		}
	}

	@Override
	public boolean usesUnitCreated() {
		return true;
	}

}
