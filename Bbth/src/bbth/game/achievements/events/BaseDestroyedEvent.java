package bbth.game.achievements.events;

import bbth.game.BBTHSimulation;
import bbth.game.BeatTrack;
import bbth.game.Player;

public class BaseDestroyedEvent extends BBTHAchievementEvent {

	public BaseDestroyedEvent(BBTHSimulation simulation, boolean singleplayer,
			float aiDifficulty) {
		super(simulation, singleplayer, aiDifficulty);
	}

	Player destroyedBaseOwner;
	BeatTrack track;

	public void set(Player destroyedBaseOwner, BeatTrack track) {
		this.destroyedBaseOwner = destroyedBaseOwner;
		this.track = track;
	}

	public Player getDestroyedBaseOwner() {
		return destroyedBaseOwner;
	}

	public BeatTrack getBeatTrack() {
		return track;
	}

}
