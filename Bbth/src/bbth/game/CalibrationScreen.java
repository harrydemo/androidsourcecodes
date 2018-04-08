package bbth.game;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import bbth.engine.core.GameActivity;
import bbth.engine.sound.MusicPlayer;
import bbth.engine.sound.MusicPlayer.OnCompletionListener;
import bbth.engine.ui.Anchor;
import bbth.engine.ui.UILabel;
import bbth.engine.ui.UINavigationController;
import bbth.engine.ui.UISlider;
import bbth.engine.ui.UIView;

public class CalibrationScreen extends UIView implements OnCompletionListener {
	
	private static final int Y_OFFSET = 65;
	
	private UISlider _calibrationSlider;
	private UILabel _explanationLabel, _calibrationLabel, _title;
	private SharedPreferences _settings;
	private SharedPreferences.Editor _editor;
	private int _calibration; // In milliseconds
	private BeatTrack _beatTrack;
	
	public CalibrationScreen(UINavigationController controller) {
		_settings = BBTHActivity.instance.getSharedPreferences("game_settings", 0); //$NON-NLS-1$
		_editor = _settings.edit();
		setSize(BBTHGame.WIDTH, BBTHGame.HEIGHT);
				
		_title = new UILabel(GameActivity.instance.getString(R.string.calibratesound));
		_title.setAnchor(Anchor.TOP_CENTER);
		_title.setTextSize(30);
		_title.sizeToFit();
		_title.setPosition(BBTHGame.WIDTH / 2, BBTHGame.TITLE_TOP);
		
		final float CONTENT_CENTER = BBTHGame.CONTENT_TOP + 8;
		
		_explanationLabel = new UILabel(GameActivity.instance.getString(R.string.calibrationexplanation));
		_explanationLabel.setAnchor(Anchor.CENTER_CENTER);
		_explanationLabel.setTextSize(16);
		_explanationLabel.sizeToFit();
		_explanationLabel.setPosition(BBTHGame.WIDTH / 2, CONTENT_CENTER);

		_calibrationSlider = new UISlider(-400, 400, 0);
		_calibrationSlider.setAnchor(Anchor.CENTER_CENTER);
		_calibrationSlider.setSize(200, 24);
		_calibrationSlider.setPosition(BBTHGame.WIDTH / 2 + 20, CONTENT_CENTER + 2 * Y_OFFSET);
		_calibrationSlider.setValue(BBTHGame.SOUND_CALIBRATION);
		
		_calibration = (int) _calibrationSlider.getValue();
		
		// Set up sound stuff
		_beatTrack = new BeatTrack(this);
		_beatTrack.setSong(Song.MISTAKE_THE_GETAWAY);
		_beatTrack.setStartDelay(1000);

		addSubview(_title);
		addSubview(_explanationLabel);
		addSubview(_calibrationSlider);
	}
	
	@Override
	public void onStop() {
		_beatTrack.stopMusic();
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		_beatTrack.draw(canvas);
		super.onDraw(canvas);
	}
	
	@Override
	public void willHide(boolean animated) {
		super.willHide(animated);
		_beatTrack.stopMusic();
	}
	
	@Override
	public void onUpdate(float seconds) {
		super.onUpdate(seconds);

		_beatTrack.refreshBeats();
		
		if (_beatTrack.getCurrPosition() >= 0) {
			_beatTrack.startMusic();
		}
		
		if (_calibration != (int) _calibrationSlider.getValue())
		{
			_calibration = (int) _calibrationSlider.getValue();
		    _editor.putInt("soundCalibration", _calibration); //$NON-NLS-1$
		    BBTHGame.SOUND_CALIBRATION = _calibration;
		    _editor.commit();
		}
	}
	
	@Override
	public boolean shouldPlayMenuMusic() {
		return false;
	}

	@Override
	public void onCompletion(MusicPlayer mp) {
		mp.stop();
		mp.release();
	}
	
	@Override
	public void onTouchDown(float x, float y) {
		super.onTouchDown(x, y);
		_beatTrack.onTouchDown(x, y);
	}
	
	@Override
	public void onTouchUp(float x, float y) {
		super.onTouchUp(x, y);
		_beatTrack.onTouchUp(x, y);
	}

}