package bbth.game.achievements.events;

import bbth.game.BBTHSimulation;
import bbth.game.Player;

public class BeatHitEvent extends BBTHAchievementEvent {
	Player hittingPlayer;

	public BeatHitEvent(BBTHSimulation simulation, boolean singleplayer,
			float aiDifficulty) {
		super(simulation, singleplayer, aiDifficulty);
	}

	public void set(Player hittingPlayer) {
		this.hittingPlayer = hittingPlayer;
	}

	public Player getHittingPlayer() {
		return hittingPlayer;
	}

	public float getCombo() {
		return hittingPlayer.getCombo();
	}

}
