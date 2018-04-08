package jardini;

import static bbth.game.BBTHGame.HEIGHT;
import static bbth.game.BBTHGame.WIDTH;
import kvgishen.TitleScreen;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.FloatMath;
import bbth.engine.core.GameScreen;
import bbth.engine.particles.ParticleSystem;
import bbth.engine.sound.Beat;
import bbth.engine.sound.MusicPlayer;
import bbth.engine.sound.MusicPlayer.OnCompletionListener;
import bbth.engine.util.ColorUtils;
import bbth.engine.util.MathUtils;
import bbth.game.BeatTrack;
import bbth.game.Song;

/**
 * A simple test screen for music playback and tapping
 * 
 * @author jardini
 */
public class MusicTestScreen extends GameScreen {
	
	private static final String MAIN_MENU_TEXT = "Menu";
	private static final String MINIMAP_TEXT = "Minimap";
	
	private static final float BEAT_LINE_Y = 135;
	
	private Paint _paint;
	private ParticleSystem _particles;
	private RectF _comboBox;
	
	private BeatTrack _beatTrack;
	private int _combo;
	private String _comboStr;
	
	public MusicTestScreen(Context context) {
		_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		_paint.setTextAlign(Align.CENTER);
		_paint.setTextSize(10);
		
		_combo = 0;
		_comboStr = "x" + String.valueOf(_combo);
		
		_comboBox = new RectF(0, 0, 0, 0);
		_particles = new ParticleSystem(100, 0.5f);
		
		_beatTrack = new BeatTrack(new OnCompletionListener() {
			@Override
			public void onCompletion(MusicPlayer mp) {
				nextScreen = new TitleScreen(null);
			}
		});
		_beatTrack.setSong(Song.RETRO);
		_beatTrack.startMusic();
	}
	
	@Override
	public void onUpdate(float seconds) {
		_beatTrack.refreshBeats();
		_particles.tick(seconds);
	}
	
	@Override
	public void onTouchDown(float x, float y) {
		if (_comboBox.contains(x, y)) {
			float xPos = (_comboBox.left + _comboBox.right) / 2;
			float yPos = (_comboBox.top + _comboBox.bottom) / 2;
			for (int i = 0; i < 90; ++i) {
				float angle = MathUtils.randInRange(0, 2 * MathUtils.PI);
				float xVel = MathUtils.randInRange(5.f, 25.f) * FloatMath.cos(angle);
				float yVel = MathUtils.randInRange(5.f, 25.f) * FloatMath.sin(angle);
				_particles.createParticle().circle().color(ColorUtils.randomHSV(0, 360, 0, 1, 0.5f, 1)).velocity(xVel, yVel).shrink(0.3f, 0.4f).radius(2.0f).position(xPos, yPos);
				_comboBox.bottom = _comboBox.top;
				_comboBox.left = _comboBox.right;
			}
		} else {
			_beatTrack.onTouchDown(x, y);
		}
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		// draw beats section
		_beatTrack.draw(canvas);
		_paint.setColor(Color.WHITE);
		canvas.drawLine(0, BEAT_LINE_Y - Beat.RADIUS, 50, BEAT_LINE_Y - Beat.RADIUS, _paint);
		canvas.drawLine(0, BEAT_LINE_Y + Beat.RADIUS, 50, BEAT_LINE_Y + Beat.RADIUS, _paint);
		canvas.drawText(_comboStr, 25, HEIGHT - 10, _paint);
		// canvas.drawText(_scoreStr, 25, HEIGHT - 2, _paint);
		canvas.drawLine(50, 0, 50, HEIGHT, _paint);
		
		// draw map / creatures section
		_paint.setColor(Color.YELLOW);
		canvas.drawArc(_comboBox, 0, 360, true, _paint);
		
		_paint.setColor(Color.WHITE);
		canvas.drawLine(50, HEIGHT - 20, WIDTH - 50, HEIGHT - 20, _paint);
		_paint.setStyle(Style.STROKE);
		canvas.drawCircle(100, HEIGHT - 10, 8, _paint);
		canvas.drawRect(WIDTH / 2 - 8, HEIGHT - 18, WIDTH / 2 + 8, HEIGHT - 2, _paint);
		canvas.drawLine(212, HEIGHT - 2, 228, HEIGHT - 2, _paint);
		canvas.drawLine(212, HEIGHT - 2, 220, HEIGHT - 18, _paint);
		canvas.drawLine(220, HEIGHT - 18, 228, HEIGHT - 2, _paint);
		
		// draw minimap section
		_paint.setStyle(Style.FILL);
		canvas.drawText(MAIN_MENU_TEXT, WIDTH - 25, 14, _paint);
		canvas.drawLine(WIDTH - 50, 20, WIDTH, 20, _paint);
		canvas.drawLine(WIDTH - 50, 0, WIDTH - 50, HEIGHT, _paint);
		canvas.drawText(MINIMAP_TEXT, WIDTH - 25, HEIGHT / 2, _paint);
		
		// draw particles
		_particles.draw(canvas, _paint);
	}
	
	@Override
	public void onStart() {
		_beatTrack.startMusic();
	}
	
	@Override
	public void onStop() {
		_beatTrack.stopMusic();
	}
}
