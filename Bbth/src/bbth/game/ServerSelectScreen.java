package bbth.game;

import java.util.ArrayList;
import java.util.List;

import android.bluetooth.BluetoothDevice;
import android.graphics.Paint.Align;
import bbth.engine.net.bluetooth.Bluetooth;
import bbth.engine.net.bluetooth.State;
import bbth.engine.net.simulation.LockStepProtocol;
import bbth.engine.ui.Anchor;
import bbth.engine.ui.UIButton;
import bbth.engine.ui.UIButtonDelegate;
import bbth.engine.ui.UILabel;
import bbth.engine.ui.UINavigationController;
import bbth.engine.ui.UIScrollView;

public class ServerSelectScreen extends UIScrollView implements UIButtonDelegate {

	private UINavigationController controller;
	private LockStepProtocol protocol;
	private Bluetooth bluetooth;
	private UIButton refreshButton;
	private UILabel statusLabel;
	private String currentStatus;
	private int lastCount;
	private List<UIButton> buttons = new ArrayList<UIButton>();

	public ServerSelectScreen(UINavigationController controller, LockStepProtocol protocol, Bluetooth bluetooth) {
		super(null);
		setScrollsHorizontal(false);
		setSize(BBTHGame.WIDTH, BBTHGame.HEIGHT);

		this.controller = controller;
		this.protocol = protocol;
		this.bluetooth = bluetooth;

		UILabel titleLabel = new UILabel(R.string.gamebrowser, null);
		titleLabel.setTextSize(30.f);
		titleLabel.setAnchor(Anchor.TOP_CENTER);
		titleLabel.setPosition(BBTHGame.WIDTH / 2, BBTHGame.TITLE_TOP);
		titleLabel.setTextAlign(Align.CENTER);
		addSubview(titleLabel);

		refreshButton = new UIButton(R.string.refresh);
		refreshButton.setAnchor(Anchor.TOP_CENTER);
		refreshButton.setPosition(BBTHGame.WIDTH / 2, BBTHGame.CONTENT_TOP + 25);
		refreshButton.setSize(100, 30);
		refreshButton.setButtonDelegate(this);
		addSubview(refreshButton);

		statusLabel = new UILabel("", null); //$NON-NLS-1$
		statusLabel.setTextSize(15);
		statusLabel.setItalics(true);
		statusLabel.setAnchor(Anchor.TOP_CENTER);
		statusLabel.setPosition(BBTHGame.WIDTH / 2, BBTHGame.CONTENT_TOP);
		statusLabel.setSize(BBTHGame.WIDTH - 10, 10);
		statusLabel.setTextAlign(Align.CENTER);
		statusLabel.setWrapText(true);
		addSubview(statusLabel);

		bluetooth.findNearbyDevices();
	}

	@Override
	public void onUpdate(float seconds) {
		// Add new devices as they appear
		List<BluetoothDevice> devices = bluetooth.getDevices();
		while (lastCount < devices.size()) {
			BluetoothDevice device = devices.get(lastCount);
			if (device == null || device.getName() == null) {
				// Log.d("Error", "Wtf, device name is null?"); //$NON-NLS-1$ //$NON-NLS-2$
				continue;
			}
			UIButton button = new UIButton(device.getName());
			button.setAnchor(Anchor.TOP_CENTER);
			button.setPosition(BBTHGame.WIDTH/2, BBTHGame.CONTENT_TOP + 25 + 65  + lastCount * 50);
			button.setSize(BBTHGame.WIDTH * 0.75f, 40);
			button.setButtonDelegate(this);
			button.tag = lastCount;
			button.isDisabled = false;
			buttons.add(button);
			addSubview(button);
			layoutSubviews(false);
			lastCount++;
		}
		
		// Disable the refresh button while refreshing.
		refreshButton.isDisabled = bluetooth.getState() == State.GET_NEARBY_DEVICES;

		// Update the current status label
		String statusMessage = bluetooth.getString();
		if (statusMessage != this.currentStatus) {
			statusLabel.setText((statusMessage == null) ? "" : statusMessage); //$NON-NLS-1$
			this.currentStatus = statusMessage;
		}

		// Start the game as the client when bluetooth connects
		if (bluetooth.getState() == State.CONNECTED) {
			controller.pushUnder(new InGameScreen(controller, Team.CLIENT, bluetooth, null, protocol, false));
			controller.pop(BBTHGame.FROM_RIGHT_TRANSITION);
		}
		
		super.onUpdate(seconds);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode) {
		bluetooth.onActivityResult(requestCode, resultCode);
	}

	@Override
	public void onClick(UIButton sender) {
		if (sender == refreshButton) {
			bluetooth.findNearbyDevices();
			for (UIButton button : buttons) {
				removeSubview(button);
			}
			layoutSubviews(false);
			buttons.clear();
			lastCount = 0;
		} else {
			List<BluetoothDevice> devices = bluetooth.getDevices();
			int i = (Integer) sender.tag;
			if (i >= 0 && i < devices.size()) {
				bluetooth.connectToDevice(devices.get(i));
			}
		}
	}
}
