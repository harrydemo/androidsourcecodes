package bbth.game;

import bbth.engine.core.Game;
import bbth.engine.core.GameActivity;

public class BBTHActivity extends GameActivity {

	@Override
	protected Game getGame() {
		return new BBTHGame(this);
	}
}
