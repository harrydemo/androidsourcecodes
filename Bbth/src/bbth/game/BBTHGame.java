package bbth.game;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import bbth.engine.achievements.Achievements;
import bbth.engine.core.Game;
import bbth.engine.core.GameActivity;
import bbth.engine.particles.ParticleSystem;
import bbth.engine.sound.MusicPlayer;
import bbth.engine.ui.UINavigationController;
import bbth.engine.ui.UINavigationEventListener;
import bbth.engine.ui.UISwipeTransition;
import bbth.engine.ui.UISwipeTransition.Direction;
import bbth.engine.ui.UIView;
import bbth.game.achievements.BBTHAchievementManager;

public class BBTHGame extends Game implements UINavigationEventListener {
	// This is the viewport width and height
	public static final float WIDTH = 320;
	public static final float HEIGHT = 530;
	public static final float TITLE_TOP = 40;
	public static final float CONTENT_TOP = 110;
	public static final boolean DEBUG = false;
	public static boolean SHOW_TUTORIAL = true;
	public static boolean TITLE_SCREEN_MUSIC = true;
	public static float AI_DIFFICULTY = 0.75f;
	public static int SOUND_CALIBRATION = 0;
	public static final int AWESOME_GREEN = Color.rgb(159, 228, 74);
	private UINavigationController navController;
	private static MusicPlayer musicPlayer;
	
	private static final int NUM_PARTICLES = 1000;
	private static final float PARTICLE_THRESHOLD = 0.5f;

	public static final ParticleSystem PARTICLES = new ParticleSystem(NUM_PARTICLES, PARTICLE_THRESHOLD);
	public static final Paint PARTICLE_PAINT = new Paint();
	static {
		PARTICLE_PAINT.setStrokeWidth(2.f);
		PARTICLE_PAINT.setAntiAlias(true);
	}

	public BBTHGame(Activity activity) {
		stopTitleMusic();
		navController = new UINavigationController();
		currentScreen = navController;
		
		SharedPreferences sharedPreferences = activity.getSharedPreferences("game_settings", 0); //$NON-NLS-1$
		SHOW_TUTORIAL = sharedPreferences.getBoolean("showTutorial", true); //$NON-NLS-1$
		TITLE_SCREEN_MUSIC = sharedPreferences.getBoolean("titleScreenMusic", true); //$NON-NLS-1$
		AI_DIFFICULTY = sharedPreferences.getFloat("aiDifficulty", 0.75f); //$NON-NLS-1$
		SOUND_CALIBRATION = sharedPreferences.getInt("soundCalibration", 0); //$NON-NLS-1$
		
		Achievements.INSTANCE.initialize(activity);
		BBTHAchievementManager.INSTANCE.initialize();
		navController.push(new TitleScreen(navController));
		
		navController.setNavListener(this);
		
		startTitleMusic();
	}
	
	@Override
	public void onUpdate(float seconds) {
		BBTHGame.PARTICLES.tick(seconds);
		super.onUpdate(seconds);
	}

	@Override
	public float getDrawDelay() {
		return 1.0f / 60.0f;
	}

	@Override
	public float getWidth() {
		return WIDTH;
	}

	@Override
	public float getHeight() {
		return HEIGHT;
	}
	

	@Override
	public void onBackPressed() {
		if (!navController.pop(FROM_LEFT_TRANSITION)) {
			stopTitleMusic();
			GameActivity.instance.finish();
		}
	}
	
	@Override
	public void onStop() {
		Achievements.INSTANCE.commit();
		stopTitleMusic();
		navController.onStop();
	}
	
	public static void startTitleMusic() {
		if (musicPlayer == null) {
			musicPlayer = new MusicPlayer(GameActivity.instance, R.raw.mistakethegetaway);
			musicPlayer.loop();
		} else if (!musicPlayer.isLooping()) {
			musicPlayer.loop();
		}
	}
	
	public static void stopTitleMusic() {
		if(musicPlayer != null) {
			musicPlayer.stop();
			musicPlayer.release();
			musicPlayer = null;
		}
	}

	public static final UISwipeTransition FROM_RIGHT_TRANSITION = new UISwipeTransition(WIDTH, Direction.FROM_RIGHT, 0.3f);
	public static final UISwipeTransition FROM_LEFT_TRANSITION = new UISwipeTransition(WIDTH, Direction.FROM_LEFT, 0.3f);

	@Override
	public void onScreenShown(UIView newscreen) {
		if (newscreen == null)
		{
			stopTitleMusic();
			return;
		}
		
		if (BBTHGame.TITLE_SCREEN_MUSIC && newscreen.shouldPlayMenuMusic())
		{
			startTitleMusic();
		}
		else
		{
			stopTitleMusic();
		}
	}

	@Override
	public void onScreenHidden(UIView oldscreen) {
		// Do nothing.
	}
}
