package bbth.game.achievements.events;

import bbth.game.BBTHSimulation;

public class WallCreatedEvent extends BBTHAchievementEvent {
	public WallCreatedEvent(BBTHSimulation simulation, boolean singleplayer,
			float aiDifficulty) {
		super(simulation, singleplayer, aiDifficulty);
	}
}
