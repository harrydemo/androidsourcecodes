package bbth.engine.achievements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Singleton that handles achievement locking and unlocking
 * 
 * @author Justin
 * 
 */
public enum Achievements {
	INSTANCE;

	// tracks number of activations for each achievement
	private Map<Integer, Integer> _achievementActivations;
	private ArrayList<UnlockAnimation> _unlocks;
	private SharedPreferences _settings;
	private Paint _paint;

	private Achievements() {
		_achievementActivations = new HashMap<Integer, Integer>();
		_unlocks = new ArrayList<UnlockAnimation>();
		_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	}

	// Should be called before checking achievement statuses or going to an
	// achievement screen
	@SuppressWarnings("unchecked")
	public void initialize(Context context) {
		_settings = context.getSharedPreferences(
				"achievements", Activity.MODE_PRIVATE); //$NON-NLS-1$
		_achievementActivations = new HashMap<Integer, Integer>();
		Map<String, Integer> activations = (HashMap<String, Integer>) _settings
				.getAll();
		for (Map.Entry<String, Integer> entry : activations.entrySet()) {
			_achievementActivations.put(Integer.valueOf(entry.getKey()),
					entry.getValue());
		}
	}

	// locks an achievement, also adding it if it doesn't yet exist
	public void lock(AchievementInfo info) {
		_achievementActivations.put(info.id, 0);
		;
	}

	// locks all achievements
	@SuppressWarnings("unchecked")
	public void lockAll() {
		_achievementActivations = new HashMap<Integer, Integer>();
		Map<String, Integer> activations = (HashMap<String, Integer>) _settings
				.getAll();
		for (Map.Entry<String, Integer> entry : activations.entrySet()) {
			_achievementActivations.put(Integer.valueOf(entry.getKey()), 0);
		}
		commit();
	}

	// increment an achievement, but don't commit the changes to
	// sharedPreferences
	public boolean increment(AchievementInfo info) {
		Integer activeTmp = _achievementActivations.get(info.id);
		int activations = activeTmp == null ? 0 : activeTmp;
		if (activations == info.maxActivations) {
			// already unlocked, no need to do anything
			return true;
		}
		_achievementActivations.put(info.id, activations + 1);
		if (activations + 1 == info.maxActivations) {
			_unlocks.add(new UnlockAnimation(info.name));
			return true;
		}
		return false;
	}

	// commit all achievement data to sharedPreferences
	public void commit() {
		Editor editor = _settings.edit();
		for (Map.Entry<Integer, Integer> entry : _achievementActivations
				.entrySet()) {
			editor.putInt(String.valueOf(entry.getKey()), entry.getValue());
		}
		editor.commit();
	}

	// return the status of an achievement
	public boolean isUnlocked(AchievementInfo info) {
		Integer activations = _achievementActivations.get(info.id);
		// binary numeric promotion applied to 'activations'
		return activations != null && activations == info.maxActivations;
	}

	public void tick(float seconds) {
		int n = _unlocks.size();
		for (int i = n - 1; i >= 0; --i) {
			UnlockAnimation u = _unlocks.get(i);
			if (u.isOver()) {
				_unlocks.remove(u);
			} else {
				u.tick(seconds);
			}
		}
	}

	// draw the unlock event animations
	public void draw(Canvas canvas, float animationWidth, float animationHeight) {
		int n = _unlocks.size();
		float top = 0;
		for (int i = 0; i < n; ++i) {
			top = _unlocks.get(i).draw(canvas, _paint, animationWidth,
					animationHeight, top);
		}
	}

	// get all achievements, useful for displaying all of them in one menu
	public Map<Integer, Integer> getAll() {
		return Collections.unmodifiableMap(_achievementActivations);
	}

}
