package bbth.game;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import bbth.engine.core.GameActivity;
import bbth.engine.sound.Beat;
import bbth.engine.sound.BeatTracker;
import bbth.engine.sound.MusicPlayer;
import bbth.engine.sound.MusicPlayer.OnCompletionListener;

/**
 * A complete beat track for a single song. Handles music, hit and miss sounds,
 * scoring.
 * 
 * @author jardini
 * 
 */
public class BeatTrack {

	public static final int BEAT_TRACK_WIDTH = 50;
	public static final float BEAT_LINE_X = 25;
	public static final float BEAT_LINE_Y = BBTHGame.HEIGHT - 50;
	public static final float BEAT_CIRCLE_RADIUS = 2.f + BeatTracker.TOLERANCE / 15.f;
	public static final float BEAT_LINE_TOP = BEAT_LINE_Y - BEAT_CIRCLE_RADIUS;
	public static final float BEAT_LINE_BOTTOM = BEAT_LINE_Y + BEAT_CIRCLE_RADIUS;
	
	public static final float COMBO_PULSE_TIME = 0.5f;
	public static final float COMBO_BRAG_TIME = 2;
	
	private BeatTracker beatTracker;
	private boolean isHolding;
	private int combo;
	private String comboStr;
	private MusicPlayer musicPlayer;
	private List<Beat> beatsInRange;
	private Paint paint;
	private OnCompletionListener onCompletionListener;
	
	private long last_combo_time;
	private float brag_text_pos;
	private boolean display_uber_brag;
	private String combo_brag_text;
	private long last_uber_combo_time;
	
	public BeatTrack(OnCompletionListener listener) {
		musicPlayer = new MusicPlayer();
		beatsInRange = new ArrayList<Beat>();
		onCompletionListener = listener;
		
		last_combo_time = 0;
		display_uber_brag = false;

		//soundManager = new SoundManager(GameActivity.instance, MAX_SOUNDS);
		//HIT_SOUND_ID = soundManager.addSound(R.raw.tambourine);
		//MISS_SOUND_ID = soundManager.addSound(R.raw.derp2);
		//HOLD_SOUND_ID = soundManager.addSound(R.raw.tambourine);
		isHolding = false;

		// Setup score stuff
		combo = 0;
		comboStr = GameActivity.instance.getString(R.string.combomultiplier, combo);

		// Setup paint
		paint = new Paint();
		paint.setTextSize(20.0f);
		paint.setFakeBoldText(true);
		paint.setStrokeCap(Cap.ROUND);
		paint.setAntiAlias(true);
		
		brag_text_pos = BBTHGame.WIDTH/2.0f + BEAT_TRACK_WIDTH/2.0f - paint.measureText(GameActivity.instance.getString(R.string.combouberunit, comboStr))/2.0f;
	}
	
	public void setSong(Song song) {
		loadSong(song);
	}
	
	// loads a song and an associated beat track
	public final void loadSong(Song song) {
		stopMusic();
		musicPlayer = new MusicPlayer(GameActivity.instance, song.songId);
		beatTracker = new BeatTracker(musicPlayer, song.trackId);
		musicPlayer.setOnCompletionListener(onCompletionListener);
	}

	public void startMusic() {
		musicPlayer.play();
	}

	public void stopMusic() {
		if (musicPlayer != null) {
			musicPlayer.stop();
			musicPlayer.release();
		}
	}
	
	public void setVolume(float volume) {
		musicPlayer.setVolume(volume);
	}
	
	public void setStartDelay(int delayMillis) {
		musicPlayer.setStartDelay(delayMillis);
	}

	public void draw(Canvas canvas) {

		paint.setARGB(127, 255, 255, 255);
		paint.setStrokeWidth(2.f);
		canvas.drawLine(BEAT_LINE_X, 0, BEAT_LINE_X, BEAT_LINE_Y - BEAT_CIRCLE_RADIUS, paint);
		canvas.drawLine(BEAT_LINE_X, BEAT_LINE_Y + BEAT_CIRCLE_RADIUS, BEAT_LINE_X, BBTHGame.HEIGHT, paint);

		if (beatTracker != null) {
			beatTracker.drawBeats(beatsInRange, BEAT_LINE_X, BEAT_LINE_Y, canvas, paint);
		}

		paint.setStyle(Style.STROKE);
		paint.setColor(Color.WHITE);
		paint.setStrokeWidth(2.f);
		canvas.drawLine(BEAT_LINE_X - BEAT_TRACK_WIDTH / 2, BEAT_LINE_Y - BEAT_CIRCLE_RADIUS, BEAT_LINE_X + BEAT_TRACK_WIDTH / 2, BEAT_LINE_Y - BEAT_CIRCLE_RADIUS, paint);
		canvas.drawLine(BEAT_LINE_X - BEAT_TRACK_WIDTH / 2, BEAT_LINE_Y + BEAT_CIRCLE_RADIUS, BEAT_LINE_X + BEAT_TRACK_WIDTH / 2, BEAT_LINE_Y + BEAT_CIRCLE_RADIUS, paint);
		paint.setStyle(Style.FILL);

		paint.setColor(Color.WHITE);

		float timesincecombo = (System.currentTimeMillis() - last_combo_time) / 1000.0f;
		float timesinceubercombo = (System.currentTimeMillis() - last_uber_combo_time) / 1000.0f;
		float old_text_size = paint.getTextSize();
		if (timesincecombo < COMBO_PULSE_TIME) {
			paint.setTextSize(30.0f - (timesincecombo*10.0f/COMBO_PULSE_TIME));
		}
		canvas.drawText(comboStr, 15, BBTHGame.HEIGHT - 10, paint);
		
		if (combo >= BBTHSimulation.UBER_UNIT_THRESHOLD && combo % BBTHSimulation.UBER_UNIT_THRESHOLD == 0) {
			if (display_uber_brag == false) {
				last_uber_combo_time = System.currentTimeMillis();
				if (combo_brag_text == null) {
					display_uber_brag = true;
					combo_brag_text = GameActivity.instance.getString(R.string.combouberunit, comboStr);
				}
			}
		} else if (display_uber_brag && timesinceubercombo > COMBO_BRAG_TIME) {
			display_uber_brag = false;
			combo_brag_text = null;
		}
		
		if (display_uber_brag && timesinceubercombo < COMBO_BRAG_TIME) {
			paint.setTextSize(20.0f);
			float alpha = timesinceubercombo / COMBO_BRAG_TIME;
			alpha = (alpha - alpha * alpha) * 4;
			paint.setAlpha((int)(255 * alpha));
			canvas.drawText(combo_brag_text, brag_text_pos, BBTHGame.HEIGHT/2.0f, paint);
		}
		paint.setTextSize(old_text_size);

	}

	public void refreshBeats() {
		// Get beats in range
		if (beatTracker != null) {
			beatsInRange = beatTracker.getBeatsInRange(-700, 6000);
		}
	}

	// returns the closest beat in the touch zone, Beat.REST means no beat
	public Beat.BeatType getTouchZoneBeat() {
		return beatTracker == null ? Beat.BeatType.REST : beatTracker.getTouchZoneBeat();
	}
	
	public Beat.BeatType onTouchDown(float x, float y) {
		if (beatTracker == null) return Beat.BeatType.REST;
		Beat.BeatType beatType = beatTracker.onTouchDown();
		boolean isOnBeat = (beatType != Beat.BeatType.REST);
		if (isOnBeat) {
			if (beatType == Beat.BeatType.HOLD){
				isHolding = true;
				//holdId = soundManager.play(HOLD_SOUND_ID);
			} else {
				//soundManager.play(HIT_SOUND_ID);
			}
			// NOTE: Combos should also be tracked in bbthSimulation
			if (beatType != Beat.BeatType.HOLD) {
				++combo;
				last_combo_time = System.currentTimeMillis();
				comboStr = GameActivity.instance.getString(R.string.combomultiplier, combo);
			}
		} else {
			//soundManager.play(MISS_SOUND_ID);
			combo = 0;
			comboStr = GameActivity.instance.getString(R.string.combomultiplier, combo);
		}

		return beatType;
	}
	
	public Beat[] getAllBeats() {
		return beatTracker == null ? new Beat[0] : beatTracker.getAllBeats();
	}
	
	public void onTouchUp(float x, float y) {
		if (isHolding) {
			//soundManager.stop(holdId);
			isHolding = false;
		}
	}

	public float getCombo() {
		return combo;
	}
	
	public int getSongLength() {
		return musicPlayer.getSongLength();
	}

	public boolean isPlaying() {
		return musicPlayer.isPlaying();
	}

	public int getCurrPosition() {
		return musicPlayer.getCurrentPosition();
	}
}
