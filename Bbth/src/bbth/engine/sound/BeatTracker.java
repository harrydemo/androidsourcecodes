package bbth.engine.sound;

import java.util.ArrayList;
import java.util.List;

import bbth.game.BBTHGame;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Tracks the beats of a song given a music player and a beat pattern
 * 
 * @author jardini
 * 
 */
public class BeatTracker {

	public static final int TOUCH_RESULT_MISS = 0;
	public static final int TOUCH_RESULT_TAP = 1;
	public static final int TOUCH_RESULT_HOLD = 2;

	public static final float TOLERANCE = 100; // millisecond difference in what
												// is still considered a valid
												// touch

	private MusicPlayer _musicPlayer;
	private Beat[] _allBeats;
	private List<Beat> _nearbyBeats;
	private int _currentBeatIndex;

	public BeatTracker(MusicPlayer player, int beatPatternXml) {
		_musicPlayer = player;
		_currentBeatIndex = 0;
		_allBeats = BeatPatternParser.parse(beatPatternXml);
		_nearbyBeats = new ArrayList<Beat>();
	}

	public int getCurrentPosition() {
		return _musicPlayer.getCurrentPosition() + BBTHGame.SOUND_CALIBRATION;
	}

	// returns whether a beat was successfully tapped
	public Beat.BeatType onTouchDown() {
		updateCurrentBeatIndex();
		Beat beat = _allBeats[_currentBeatIndex];
		if (beat.onTouchDown(getCurrentPosition())) {
			return beat.type;
		}
		return Beat.BeatType.REST;
	}

	// returns whether a beat was successfully tapped
	public Beat.BeatType getTouchZoneBeat() {
		updateCurrentBeatIndex();
		Beat beat = _allBeats[_currentBeatIndex];
		if (beat.inTouchZone(getCurrentPosition())) {
			return beat.type;
		}
		return Beat.BeatType.REST;
	}

	// handle a release
	public void onTouchUp() {
		// does nothing for now
	}

	// return index into BeatPattern for closest beat
	public final void updateCurrentBeatIndex() {
		int currTime = getCurrentPosition();

		if (_currentBeatIndex >= _allBeats.length - 1) {
			return;
		}
		Beat beat = _allBeats[_currentBeatIndex];
		Beat nextBeat = _allBeats[_currentBeatIndex + 1];

		while (Math.abs(currTime - nextBeat._startTime) < Math.abs(currTime
				- beat._startTime)) {
			++_currentBeatIndex;
			if (_currentBeatIndex >= _allBeats.length - 1) {
				return;
			}
			beat = _allBeats[_currentBeatIndex];
			nextBeat = _allBeats[_currentBeatIndex + 1];
		}
	}

	// get all the beats in a time window relative to the current time in the
	// song being played
	public final List<Beat> getBeatsInRange(int lowerOffset, int upperOffset) {
		_nearbyBeats.clear();

		updateCurrentBeatIndex();
		int i = _currentBeatIndex;
		int currTime = getCurrentPosition();
		int lowerBound = currTime + lowerOffset;
		int upperBound = currTime + upperOffset;

		while (i >= 0 && _allBeats[i].getEndTime() > lowerBound) {
			_nearbyBeats.add(_allBeats[i]);
			--i;
		}

		i = _currentBeatIndex + 1;
		while (i < _allBeats.length && _allBeats[i]._startTime < upperBound) {
			_nearbyBeats.add(_allBeats[i]);
			++i;
		}

		return _nearbyBeats;
	}

	public final Beat[] getAllBeats() {
		return _allBeats;
	}

	// draw a list of beats, likely obtained from getBeatsInRange
	public void drawBeats(List<Beat> beats, float xMid, float yMid,
			Canvas canvas, Paint paint) {
		int time = getCurrentPosition();
		for (int i = 0; i < beats.size(); ++i) {
			_nearbyBeats.get(i).draw(time, xMid, yMid, canvas, paint);
		}
	}
}
