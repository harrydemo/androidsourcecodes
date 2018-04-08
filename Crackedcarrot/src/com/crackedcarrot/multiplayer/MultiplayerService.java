package com.crackedcarrot.multiplayer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.bluetooth.BluetoothSocket;
import com.crackedcarrot.GameLoopGUI;

public class MultiplayerService extends Thread {

	private final BluetoothSocket mmSocket;
	private final InputStream mmInStream;
	private final OutputStream mmOutStream;

	// Message types sent to the MultiplayerService Handler
	public static final int MESSAGE_READ = 10;
	public static final int MESSAGE_SYNCH_LEVEL = 1;
	public static final int MESSAGE_PLAYER_SCORE = 2;
	public static final int MESSAGE_PLAYER_DEAD = 3;
	public static final int MESSAGE_DEVICE_NAME = 30;
	public static final int MESSAGE_BT_KILLED = 40;

	private boolean runBluetooth = true;

	public MultiplayerHandler mpHandler;
	public GameLoopGUI gameLoopGui;

	public MultiplayerService(BluetoothSocket socket) {
		// Log.d("MultiplayerService", "create ConnectedThread");
		mmSocket = socket;
		InputStream tmpIn = null;
		OutputStream tmpOut = null;

		// Get the BluetoothSocket input and output streams
		try {
			tmpIn = socket.getInputStream();
			tmpOut = socket.getOutputStream();
		} catch (IOException e) {
		}

		mmInStream = tmpIn;
		mmOutStream = tmpOut;
		mpHandler = new MultiplayerHandler();
		mpHandler.start();
	}

	/** This run method constantly read from the input stream */
	public void run() {
		// Log.d("MultiplayerService", "BEGIN MultiplayerService");
		byte[] buffer = new byte[1024];
		int bytes;

		// Keep listening to the InputStream while connected
		while (runBluetooth) {
			try {
				// Read from the InputStream
				bytes = mmInStream.read(buffer);

				if (bytes > 0) {
					// Log.d("MPSERVICE__", "read bytes: " + bytes);
					mpHandler.mMultiplayerHandler.obtainMessage(MESSAGE_READ,
							0, bytes, buffer).sendToTarget();
				}
				// Log.d("MPSERVICE LOOP", "Send to handler");
			} catch (IOException e) {
				// Log.d("MPSERVICE LOOP", "Connection lost", e);
				connectionLost();
				break;
			}
		}
	}

	public synchronized void connected() {
		this.start();
	}

	/**
	 * Write to the connected OutStream.
	 * 
	 * @param buffer
	 *            The bytes to write
	 */
	public void write(byte[] buffer) {
		try {
			// Log.d("MPSERVICE Write", "Write to OutputStream");
			mmOutStream.write(buffer);
		} catch (IOException e) {
			// Log.e("MultiplayerService", "Exception during write", e);
		}
	}

	/**
	 * Indicate that the connection was lost and notify the UI Activity.
	 */
	private void connectionLost() {
		// Send a failure message back to the Activity
		mpHandler.mMultiplayerHandler.obtainMessage(MESSAGE_BT_KILLED)
				.sendToTarget();
	}

	/** Closes the Bluetooth socket and streams */
	public void endBluetooth() {
		this.runBluetooth = false;
		try {
			this.mmSocket.close();
			this.mmInStream.close();
			this.mmOutStream.close();
		} catch (Exception e) {
			// Log.e("MultiplayerService",
			// "Exception when closing socket and streams", e);
		}
	}

	public void setLoopAndGUI(MultiplayerGameLoop gLoop, GameLoopGUI glGui) {

		mpHandler.setGameLoop(gLoop);
		mpHandler.setGameLoopGui(glGui);
		gameLoopGui = glGui;
	}

}