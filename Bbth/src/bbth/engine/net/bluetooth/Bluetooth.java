package bbth.engine.net.bluetooth;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import bbth.engine.core.GameActivity;

public final class Bluetooth implements Runnable {

	private static final UUID GAME_SPECIFIC_UUID = new UUID(3985320, 837498234);
	private static final int BLUETOOTH_ENABLED = Activity.RESULT_FIRST_USER;
	private static final int BLUETOOTH_DISCOVERABLE = Activity.RESULT_FIRST_USER + 1;
	private static final int LISTEN_DELAY_IN_SECONDS = 300;
	private static final String PREV_DEVICE_ADDRESS = "prevDeviceAddress"; //$NON-NLS-1$

	private static abstract class StateBase {
		public abstract State getState();

		// only called when constructed, try to pre-cache localized state string
		protected String getStateString() {
			return GameActivity.instance.getString(getState()
					.getMessageResourceId());
		}

		String stateString = getStateString();

		// called every update, avoid localization calls wherever possible (i.e.
		// only override if absolutely necessary)
		public String getString() {
			return stateString;
		}

		public abstract void run() throws InterruptedException;
	}

	private final DisconnectedState DISCONNECTED = new DisconnectedState();
	private final EnableBluetoothState ENABLE_BLUETOOTH = new EnableBluetoothState();
	private final ListenForConnectionsState LISTEN_FOR_CONNECTIONS = new ListenForConnectionsState();
	private final GetNearbyDevicesState GET_NEARBY_DEVICES = new GetNearbyDevicesState();
	private final ConnectToDeviceState CONNECT_TO_DEVICE = new ConnectToDeviceState();
	private final CheckPreviousConnectionState CHECK_PREVIOUS_CONNECTION = new CheckPreviousConnectionState();
	private final ConnectedState CONNECTED = new ConnectedState();

	private GameActivity context;
	private StateBase currentState = DISCONNECTED;
	private StateBase nextState = DISCONNECTED;
	private final SharedPreferences settings;
	private BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
	private List<BluetoothDevice> devices = new ArrayList<BluetoothDevice>();
	private Thread thread;
	private boolean isDiscoverable;
	private Protocol protocol;

	public Bluetooth(GameActivity context, Protocol protocol) {
		this.context = context;
		this.protocol = protocol;
		settings = context.getPreferences(Context.MODE_PRIVATE);

		// Start the state machine
		thread = new Thread(this);
		thread.start();
	}

	public String getLocalName() {
		return bluetooth.getName();
	}

	public State getState() {
		return currentState.getState();
	}

	public String getString() {
		return currentState.getString();
	}

	public List<BluetoothDevice> getDevices() {
		return devices;
	}

	@Override
	public void run() {
		while (true) {
			// If currentState.run() returns without transitioning, transition
			// to the DISCONNECTED state
			nextState = DISCONNECTED;

			// Run the current state
			try {
				currentState.run();
			} catch (InterruptedException e) {
			}

			// Update to the next state
			currentState = nextState;
		}
	}

	private class EnableBluetoothState extends StateBase {
		@Override
		public State getState() {
			return State.ENABLE_BLUETOOTH;
		}

		public StateBase nextState;

		@Override
		public void run() throws InterruptedException {
			if (bluetooth != null) {
				if (!bluetooth.isEnabled()) {
					context.startActivityForResult(new Intent(
							BluetoothAdapter.ACTION_REQUEST_ENABLE),
							BLUETOOTH_ENABLED);
					Thread.sleep(60 * 1000);
				} else {
					transition(nextState);
				}
			}
		}
	}

	private class CheckPreviousConnectionState extends StateBase {
		@Override
		public State getState() {
			return State.CHECK_PREVIOUS_CONNECTION;
		}

		@Override
		public void run() throws InterruptedException {
			String prevDeviceAddress = settings.getString(PREV_DEVICE_ADDRESS,
					null);
			if (prevDeviceAddress != null) {
				BluetoothDevice prevDevice = bluetooth
						.getRemoteDevice(prevDeviceAddress);
				if (prevDevice != null) {
					try {
						CONNECTED.socket = prevDevice
								.createRfcommSocketToServiceRecord(GAME_SPECIFIC_UUID);
						CONNECTED.socket.connect();
						transition(CONNECTED);
					} catch (IOException e) {
					}
				}
			}
			transition(DISCONNECTED);
		}
	}

	private class GetNearbyDevicesState extends StateBase {
		@Override
		public State getState() {
			return State.GET_NEARBY_DEVICES;
		}

		// TODO: This overrides getString instead of getStateString because the
		// number of devices changes while the state doesn't.
		// At some point, we should have something just re-calculate stateString
		// and make getString final in StateBase, but
		// I'm too lazy to do that right now. Since it's just the connecting
		// screen, performance isn't really an issue. -ZD
		@Override
		public String getString() {
			return GameActivity.instance.getString(getState()
					.getMessageResourceId(), devices.size());
		}

		private BroadcastReceiver receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if (BluetoothDevice.ACTION_FOUND.equals(action)) {
					devices.add((BluetoothDevice) intent
							.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE));
				} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
						.equals(action)) {
					try {
						transition(DISCONNECTED);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};

		@Override
		public void run() throws InterruptedException {
			try {
				devices.clear();
				context.registerReceiver(receiver, new IntentFilter(
						BluetoothDevice.ACTION_FOUND));
				context.registerReceiver(receiver, new IntentFilter(
						BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
				bluetooth.startDiscovery();
				Thread.sleep(120 * 1000);
			} finally {
				bluetooth.cancelDiscovery();
				context.unregisterReceiver(receiver);
				transition(DISCONNECTED);
			}
		}
	}

	private class ConnectToDeviceState extends StateBase {
		@Override
		public State getState() {
			return State.CONNECT_TO_DEVICE;
		}

		boolean needToRegenerate = true;

		private void regenerateStateString() {
			BluetoothDevice device = currentDevice;
			stateString = GameActivity.instance.getString(getState()
					.getMessageResourceId(),
					device == null ? "" : device.getName()); //$NON-NLS-1$
			if (device != null) {
				needToRegenerate = false;
			}
		}

		@Override
		public String getStateString() {
			regenerateStateString();
			return stateString;
		}

		@Override
		public String getString() {
			if (needToRegenerate)
				regenerateStateString();
			return stateString;
		}

		public BluetoothDevice currentDevice;

		@Override
		public void run() throws InterruptedException {
			BluetoothSocket socket = null;
			try {
				socket = currentDevice
						.createRfcommSocketToServiceRecord(GAME_SPECIFIC_UUID);
				socket.connect();
				CONNECTED.socket = socket;
				socket = null;
				transition(CONNECTED);
			} catch (IOException e) {
				transition(DISCONNECTED);
			} finally {
				if (socket != null) {
					try {
						socket.close();
					} catch (IOException e) {
					}
				}
			}
		}
	}

	private class ConnectedState extends StateBase {
		@Override
		public State getState() {
			return State.CONNECTED;
		}

		public BluetoothSocket socket;

		@Override
		public void run() throws InterruptedException {
			Thread readThread = null;
			try {
				// Save the address of the connected device for next time
				Editor editor = settings.edit();
				editor.putString(PREV_DEVICE_ADDRESS, socket.getRemoteDevice()
						.getAddress());
				editor.commit();

				// Start the read thread
				readThread = new Thread() {
					@Override
					public void run() {
						try {
							DataInputStream in = new DataInputStream(
									socket.getInputStream());
							while (!Thread.interrupted()) {
								protocol.readFrom(in);
							}
						} catch (IOException e) {
							e.printStackTrace();

							// Disconnect on IOExceptions because they will
							// probably cause a
							// lock-step desync otherwise, not sure what causes
							// this but we
							// got one once (not reproducible)
							try {
								transition(DISCONNECTED);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				};
				readThread.start();

				// Run the write thread
				ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
				DataOutputStream out = new DataOutputStream(byteArrayOut);
				OutputStream socketOut = socket.getOutputStream();
				while (!Thread.interrupted()) {
					// Write an outgoing object to a temporary byte buffer
					byteArrayOut.reset();
					protocol.writeTo(out);

					// Make sure we only write complete packets in one chunk,
					// not sure if this will help fix the Android bug or not
					byteArrayOut.writeTo(socketOut);
					socketOut.flush();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (readThread != null) {
					readThread.interrupt();
				}
				if (socket != null) {
					try {
						socket.close();
					} catch (IOException e) {
					}
				}
			}
		}
	}

	private class ListenForConnectionsState extends StateBase {
		@Override
		public State getState() {
			return State.LISTEN_FOR_CONNECTIONS;
		}

		private BroadcastReceiver receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if (BluetoothAdapter.ACTION_SCAN_MODE_CHANGED.equals(action)
						&& intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE,
								BluetoothAdapter.ERROR) != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
					try {
						transition(DISCONNECTED);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};

		@Override
		public void run() throws InterruptedException {
			// Make sure other clients can connect to us
			if (!isDiscoverable) {
				Intent intent = new Intent(
						BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
				intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,
						LISTEN_DELAY_IN_SECONDS);
				context.registerReceiver(receiver, new IntentFilter(
						BluetoothAdapter.ACTION_SCAN_MODE_CHANGED));
				context.startActivityForResult(intent, BLUETOOTH_DISCOVERABLE);
			}

			// Listen for connections and try to accept one
			BluetoothServerSocket socket = null;
			try {
				socket = bluetooth
						.listenUsingRfcommWithServiceRecord(
								"THIS POINTLESS STRING DOESN'T DO ANYTHING", GAME_SPECIFIC_UUID); //$NON-NLS-1$
				while (true) {
					try {
						// Don't block too long on accept so this thread can be
						// interrupted and stopped
						CONNECTED.socket = socket.accept(100);
						transition(CONNECTED);
					} catch (IOException e) {
					}

					// Let this thread be interrupted
					Thread.sleep(0);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				context.unregisterReceiver(receiver);
				if (socket != null) {
					try {
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private class DisconnectedState extends StateBase {
		@Override
		public State getState() {
			return State.DISCONNECTED;
		}

		@Override
		public void run() throws InterruptedException {
			Thread.sleep(60 * 1000);
		}
	}

	protected void transition(StateBase state) throws InterruptedException {
		nextState = state;
		thread.interrupt();

		// Make sure we give Java the chance to interrupt ourselves
		Thread.sleep(0);
	}

	public void listen() {
		disconnect();
		ENABLE_BLUETOOTH.nextState = LISTEN_FOR_CONNECTIONS;
		try {
			transition(ENABLE_BLUETOOTH);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void connectToPreviousDevice() {
		disconnect();
		ENABLE_BLUETOOTH.nextState = CHECK_PREVIOUS_CONNECTION;
		try {
			transition(ENABLE_BLUETOOTH);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void findNearbyDevices() {
		disconnect();
		ENABLE_BLUETOOTH.nextState = GET_NEARBY_DEVICES;
		try {
			transition(ENABLE_BLUETOOTH);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void connectToDevice(BluetoothDevice device) {
		disconnect();
		CONNECT_TO_DEVICE.currentDevice = device;
		ENABLE_BLUETOOTH.nextState = CONNECT_TO_DEVICE;
		try {
			transition(ENABLE_BLUETOOTH);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void disconnect() {
		try {
			transition(DISCONNECTED);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Stupid method necessary because of android's weird context/activity mess.
	 */
	public void onActivityResult(int requestCode, int resultCode) {
		if (currentState == ENABLE_BLUETOOTH
				&& requestCode == BLUETOOTH_ENABLED
				&& resultCode == Activity.RESULT_OK) {
			try {
				transition(ENABLE_BLUETOOTH.nextState);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else if (currentState == LISTEN_FOR_CONNECTIONS
				&& requestCode == BLUETOOTH_DISCOVERABLE
				&& resultCode == Activity.RESULT_CANCELED) {
			try {
				transition(DISCONNECTED);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
