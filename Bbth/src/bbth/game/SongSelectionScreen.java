package bbth.game;

import bbth.engine.net.bluetooth.Bluetooth;
import bbth.engine.net.simulation.LockStepProtocol;
import bbth.engine.ui.Anchor;
import bbth.engine.ui.UIButton;
import bbth.engine.ui.UIButtonDelegate;
import bbth.engine.ui.UILabel;
import bbth.engine.ui.UINavigationController;
import bbth.engine.ui.UIScrollView;

public class SongSelectionScreen extends UIScrollView implements
		UIButtonDelegate {
	private Team playerTeam;
	private Bluetooth bluetooth;
	private LockStepProtocol protocol;

	UINavigationController controller;
	
	boolean singlePlayer;
	
	public SongSelectionScreen(UINavigationController controller, Team playerTeam, Bluetooth bluetooth, LockStepProtocol protocol, boolean singlePlayer) {
		super(null);

		this.controller = controller;
		this.singlePlayer = singlePlayer;
		
		this.playerTeam = playerTeam;
		this.bluetooth = bluetooth;
		this.protocol = protocol;

		this.setSize(BBTHGame.WIDTH, BBTHGame.HEIGHT);

		UILabel title = new UILabel(R.string.songselection, null);
		title.setAnchor(Anchor.TOP_CENTER);
		title.setTextSize(30.f);
		title.setPosition(BBTHGame.WIDTH / 2.f, BBTHGame.TITLE_TOP);
		this.addSubview(title);
		this.setScrollsHorizontal(false);
		this.setScrollsVertical(false);

		int y = 0;
		this.addSubview(makeButton(Song.RETRO, y++));
		this.addSubview(makeButton(Song.MISTAKE_THE_GETAWAY, y++));
		this.addSubview(makeButton(Song.JAVLA_SLADDAR, y++));
		this.addSubview(makeButton(Song.ODINS_KRAFT, y++));
		this.addSubview(makeButton(Song.MIGHT_AND_MAGIC, y++));
		this.setContentRect(0, 0, BBTHGame.WIDTH, BBTHGame.CONTENT_TOP + y * 65);
	}

	private UIButton makeButton(Song song, int idx) {
		UIButton button = new UIButton(song.getNameResourceId(), song);
		button.setAnchor(Anchor.TOP_CENTER);
		button.setSize(BBTHGame.WIDTH * 0.75f, 45);
		button.setPosition(BBTHGame.WIDTH / 2, BBTHGame.CONTENT_TOP + idx * 65);
		button.setButtonDelegate(this);
		return button;
	}

	@Override
	public void onClick(UIButton button) {
		controller.pushUnder(new InGameScreen(controller, playerTeam, bluetooth, (Song) button.tag, protocol, singlePlayer));
		controller.pop(BBTHGame.FROM_RIGHT_TRANSITION);
	}
}
