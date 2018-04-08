package bbth.game.achievements.events;

import bbth.game.BBTHSimulation;

public class UpdateEvent extends BBTHAchievementEvent {

	public UpdateEvent(BBTHSimulation simulation, boolean singleplayer,
			float aiDifficulty) {
		super(simulation, singleplayer, aiDifficulty);
	}

	float seconds;

	public void set(float seconds) {
		this.seconds = seconds;
	}

	public float getSeconds() {
		return seconds;
	}

}
