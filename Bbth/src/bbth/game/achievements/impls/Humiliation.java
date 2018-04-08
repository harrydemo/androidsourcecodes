package bbth.game.achievements.impls;

import bbth.engine.achievements.AchievementInfo;
import bbth.game.BBTHSimulation;
import bbth.game.Base;
import bbth.game.Team;
import bbth.game.achievements.BBTHAchievement;
import bbth.game.achievements.events.UnitCreatedEvent;
import bbth.game.units.Unit;

public class Humiliation extends BBTHAchievement {

	public Humiliation(AchievementInfo achievementInfo) {
		super(achievementInfo);
	}

	private static final float dist = 10f;

	@Override
	public void unitCreated(UnitCreatedEvent e) {
		Unit unit = e.getUnit();
		Team team = e.getLocalPlayer().getTeam();
		if (team == unit.getTeam()) {
			if ((team == Team.SERVER && unit.getY() > BBTHSimulation.GAME_HEIGHT
					- Base.BASE_HEIGHT - dist)
					|| (team == Team.CLIENT && unit.getY() < Base.BASE_HEIGHT
							+ dist)) {
				increment();
			}
		}
	}

	@Override
	public boolean usesUnitCreated() {
		return true;
	}

}
