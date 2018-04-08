package bbth.engine.net.bluetooth;

import bbth.game.R;

public enum State {
	DISCONNECTED(R.string.disconnected), ENABLE_BLUETOOTH(
			R.string.enablebluetooth), CHECK_PREVIOUS_CONNECTION(
			R.string.checkpreviousconnection), GET_NEARBY_DEVICES(
			R.string.getnearbydevices), CONNECT_TO_DEVICE(R.string.connecting), LISTEN_FOR_CONNECTIONS(
			R.string.listenforconnections), CONNECTED(R.string.connected);

	private final int message;

	private State(int message) {
		this.message = message;
	}

	public int getMessageResourceId() {
		return message;
	}
}
