package bbth.game;

import android.graphics.Paint.Align;
import bbth.engine.core.GameActivity;
import bbth.engine.net.bluetooth.Bluetooth;
import bbth.engine.net.bluetooth.State;
import bbth.engine.net.simulation.LockStepProtocol;
import bbth.engine.ui.Anchor;
import bbth.engine.ui.UIButton;
import bbth.engine.ui.UIButtonDelegate;
import bbth.engine.ui.UILabel;
import bbth.engine.ui.UINavigationController;
import bbth.engine.ui.UIView;

public class GameSetupScreen extends UIView implements UIButtonDelegate {
	private LockStepProtocol protocol;
	private Bluetooth bluetooth;

	private UIButton serverButton;
	private UIButton clientButton;
	private UIButton disconnectButton;
	private UILabel statusLabel;

	private UINavigationController controller;
	private UILabel titleLabel;
	private UILabel deviceNameLabel;

	private String currentStatus = null;

	public GameSetupScreen(UINavigationController controller) {
		super(null);

		this.controller = controller;

		setSize(BBTHGame.WIDTH, BBTHGame.HEIGHT);
		setPosition(0, 0);

		protocol = new LockStepProtocol();
		bluetooth = new Bluetooth(GameActivity.instance, protocol);

		serverButton = new UIButton(R.string.createagame, null);
		serverButton.setAnchor(Anchor.TOP_CENTER);
		serverButton.setPosition(BBTHGame.WIDTH / 2, BBTHGame.CONTENT_TOP + 65);
		serverButton.setSize(BBTHGame.WIDTH * 0.75f, 45);
		serverButton.setButtonDelegate(this);
		addSubview(serverButton);

		clientButton = new UIButton(R.string.joinagame, null);
		clientButton.setAnchor(Anchor.TOP_CENTER);
		clientButton.setPosition(BBTHGame.WIDTH / 2, BBTHGame.CONTENT_TOP + 130);
		clientButton.setSize(BBTHGame.WIDTH * 0.75f, 45);
		clientButton.setButtonDelegate(this);
		addSubview(clientButton);

		disconnectButton = new UIButton(R.string.cancel, null);
		disconnectButton.setAnchor(Anchor.TOP_CENTER);
		disconnectButton.setPosition(BBTHGame.WIDTH / 2, BBTHGame.CONTENT_TOP + 195);
		disconnectButton.setSize(BBTHGame.WIDTH * 0.75f, 45);
		disconnectButton.setButtonDelegate(this);
		disconnectButton.isDisabled = true;
		addSubview(disconnectButton);

		titleLabel = new UILabel(R.string.multiplayer, null);
		titleLabel.setTextSize(30.f);
		titleLabel.setAnchor(Anchor.TOP_CENTER);
		titleLabel.setPosition(BBTHGame.WIDTH / 2, BBTHGame.TITLE_TOP);
		titleLabel.setTextAlign(Align.CENTER);
		addSubview(titleLabel);

		deviceNameLabel = new UILabel(""); //$NON-NLS-1$
		deviceNameLabel.setWrapText(true); // actually needed for newlines too...
		deviceNameLabel.setAnchor(Anchor.BOTTOM_CENTER);
		deviceNameLabel.setTextSize(15);
		deviceNameLabel.setPosition(BBTHGame.WIDTH / 2, BBTHGame.HEIGHT - 80);
		deviceNameLabel.setLineHeight(20);
		deviceNameLabel.setSize(BBTHGame.WIDTH, 0);
		addSubview(deviceNameLabel);

		statusLabel = new UILabel("", null); //$NON-NLS-1$
		statusLabel.setTextSize(15);
		statusLabel.setItalics(true);
		statusLabel.setAnchor(Anchor.TOP_CENTER);
		statusLabel.setPosition(BBTHGame.WIDTH / 2, BBTHGame.CONTENT_TOP);
		statusLabel.setSize(BBTHGame.WIDTH - 10, 10);
		statusLabel.setTextAlign(Align.CENTER);
		statusLabel.setWrapText(true);
		addSubview(statusLabel);
	}

	@Override
	public void willAppear(boolean animated) {
		super.willAppear(animated);

		// Reset the state if the ServerSelectScreen pops back to us
		bluetooth.disconnect();
	}

	@Override
	public void onUpdate(float seconds) {
		// Update status message when it changes
		String statusMessage = bluetooth.getString();
		if (statusMessage != this.currentStatus) {
			statusLabel.setText((statusMessage == null) ? "" : statusMessage); //$NON-NLS-1$
			this.currentStatus = statusMessage;
		}

		// Update button enabled state
		boolean isDisconnected = (bluetooth.getState() == State.DISCONNECTED);
		serverButton.isDisabled = !isDisconnected;
		clientButton.isDisabled = !isDisconnected;
		disconnectButton.isDisabled = isDisconnected;
		
		// If bluetooth connects, assume it was because of a click on the
		// serverButton, since we disconnect when this view is switched to
		// and the only way to get to the connected state from this class
		// is bluetooth.listen().
		if (bluetooth.getState() == State.CONNECTED) {
			controller.pushUnder(new SongSelectionScreen(controller, Team.SERVER, bluetooth, protocol, false));
			controller.pop(BBTHGame.FROM_RIGHT_TRANSITION);
		}

		// We don't have an isVisible flag for deviceNameLabel, so clear
		// the text instead
		if (bluetooth.getState() != State.LISTEN_FOR_CONNECTIONS) {
			deviceNameLabel.setText(""); //$NON-NLS-1$
		}
	}

	@Override
	public void onClick(UIButton sender) {
		if (sender == serverButton) {
			bluetooth.listen();
			deviceNameLabel.setText(GameActivity.instance.getString(R.string.tellotherplayertoconnectto, bluetooth.getLocalName()));
		} else if (sender == clientButton) {
			controller.push(new ServerSelectScreen(controller, protocol, bluetooth), BBTHGame.FROM_RIGHT_TRANSITION);
		} else if (sender == disconnectButton) {
			bluetooth.disconnect();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode) {
		bluetooth.onActivityResult(requestCode, resultCode);
	}
}
