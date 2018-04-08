package bbth.engine.util;

import android.content.Context;
import android.os.Vibrator;
import bbth.engine.core.GameActivity;

public class Vibrate {

	public static void vibrate(float seconds) {
		((Vibrator) GameActivity.instance
				.getSystemService(Context.VIBRATOR_SERVICE))
				.vibrate((long) (seconds * 1000));
	}
}
