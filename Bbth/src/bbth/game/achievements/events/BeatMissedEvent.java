package bbth.game.achievements.events;

import bbth.game.BBTHSimulation;

public class BeatMissedEvent extends BBTHAchievementEvent {

	public BeatMissedEvent(BBTHSimulation simulation, boolean singleplayer,
			float aiDifficulty) {
		super(simulation, singleplayer, aiDifficulty);
	}

}
