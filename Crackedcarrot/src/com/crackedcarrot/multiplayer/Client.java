package com.crackedcarrot.multiplayer;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.Semaphore;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.crackedcarrot.GameInit;
import com.crackedcarrot.menu.R;

public class Client extends Activity {

	// If this is set to 0 this game is a LITE game. Will read data from
	// integers.xml to set this
	int fullversion = 0;

	// Local Bluetooth adapter
	private BluetoothAdapter mBluetoothAdapter = null;
	// The client thread
	private ConnectThread mConnectThread;
	// The Universally Unique Identifier (UUID) for this application
	private static final UUID MY_UUID = UUID
			.fromString("9a8aa173-eaf0-4370-80e1-3a13ed5efae9");
	// The request codes for startActivity and onActivityResult
	private static final int REQUEST_ENABLE_BLUETOOTH = 1;
	private static final int REQUEST_CONNECT_DEVICE = 2;
	private static final int REQUEST_DISCOVERABLE = 3;

	public BluetoothSocket mmClientSocket = null;
	private MultiplayerService mMultiplayerService;

	private int DIFFICULTY = 1;
	private int MAP = 1;
	private int GAMEMODE = 0;
	protected static Semaphore handshakeSemaphore = new Semaphore(0);

	// Return Intent extra
	public static String EXTRA_DEVICE_ADDRESS = "device_address";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.client);

		/**
		 * Ensures that the activity is displayed only in the portrait
		 * orientation
		 */
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		Resources r = getResources();
		fullversion = r.getInteger(R.integer.app_type);

		Button ScanButton = (Button) findViewById(R.id.scan);
		ScanButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent serverIntent = new Intent(Client.this, ScanDevices.class);
				startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
			}
		});

		// Get the local Bluetooth adapter
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		// If the adapter is null, then Bluetooth is not supported
		if (mBluetoothAdapter == null) {
			Toast.makeText(this, "Bluetooth is not available on this device",
					Toast.LENGTH_LONG).show();
			finish();
			return;
		}
	}

	/** When the activity first starts, do following */
	@Override
	public void onStart() {
		super.onStart();

		/**
		 * Request that the device will be discoverable for 300 seconds Only
		 * need to do this for the server side of the connection
		 */
		/*
		 * Intent discoverableIntent = new
		 * Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		 * discoverableIntent
		 * .putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
		 * startActivityForResult(discoverableIntent, REQUEST_DISCOVERABLE);
		 */

		/**
		 * Request that Bluetooth will be activated if not on. setupClient()
		 * will then be called during onActivityResult
		 */
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BLUETOOTH);
			// Log.d("CLIENT", "Request enable Bluetooth");
		}
	}

	/**
	 * This synchronized method starts the thread that does the actual
	 * connection with the available server
	 * 
	 * @param device
	 */
	private synchronized void connect(BluetoothDevice device) {
		if (mConnectThread == null) {
			mConnectThread = new ConnectThread(device);
			mConnectThread.start();
			// Log.d("CLIENT", "Start connect thread");
		}
	}

	/**
	 * This method is called after the startActivityForResult() is called with
	 * parameters containing activity id and user choice
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_CONNECT_DEVICE:
			// When ScanDevices returns with a device to connect
			if (resultCode == Activity.RESULT_OK) {
				// Get the MAC address of the device
				String address = data.getExtras().getString(
						ScanDevices.EXTRA_DEVICE_ADDRESS);
				// Get the BLuetoothDevice object
				BluetoothDevice device = mBluetoothAdapter
						.getRemoteDevice(address);

				// Show connecting-progress-dialog.
				showDialog(1);

				// Try to connect to the device
				connect(device);
			}
			break;
		case REQUEST_DISCOVERABLE:
			if (resultCode == 300) {
				// The device is made discoverable and bluetooth is activated
			} else {
				// User did not accept the request or an error occured
				Toast.makeText(
						this,
						"The device was not made discoverable. Leaving multiplayer",
						Toast.LENGTH_SHORT).show();
				finish();
			}
			break;
		case REQUEST_ENABLE_BLUETOOTH:
			// When the request to enable Bluetooth returns
			if (resultCode == Activity.RESULT_OK) {
				// Bluetooth is now enabled, so do nothing
			} else {
				// User did not enable Bluetooth or an error occured
				Toast.makeText(this,
						"Bluetooth was not enabled. Leaving multiplayer.",
						Toast.LENGTH_SHORT).show();
				finish();
			}
		}
	}

	private class ConnectThread extends Thread {

		public ConnectThread(BluetoothDevice device) {
			// mmClientSocket is final so use a temporary object first
			BluetoothSocket tmp = null;
			// Log.d("CLIENT", "Connect thread constructor");
			// Get a BluetoothSocket to connect with the given BluetoothDevice
			try {
				// MY_UUID is the app's UUID string, also used by the server
				// code
				tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
			} catch (IOException e) {
			}
			mmClientSocket = tmp;
		}

		public void run() {

			Looper.prepare();

			Toast.makeText(getBaseContext(),
					"Connection to server failed...leaving", Toast.LENGTH_LONG)
					.show();

			// Cancel discovery because it will slow down the connection
			mBluetoothAdapter.cancelDiscovery();
			// Log.d("CLIENT", "Connectthread runs");
			try {
				// Connect through the socket. This will block until it succeeds
				// or throws an exception
				// Log.d("CLIENT", "Connectthread call connect");
				mmClientSocket.connect();
			} catch (IOException connectException) {
				// Unable to connect; close the socket and get out
				// Send a message that connection failed
				Toast.makeText(getBaseContext(),
						"Connection to server failed...leaving",
						Toast.LENGTH_LONG).show();

				finish();

				try {
					mmClientSocket.close();
				} catch (IOException closeException) {
					// Log.e("CLIENT", "Can't close socket", closeException);
				}

				return;
			}
			// Log.d("CLIENT", "Ansluten!!!");
			startGame();
		}
	}

	private void startGame() {
		// Log.d("CLIENT", "Start game");

		mMultiplayerService = new MultiplayerService(mmClientSocket);
		mMultiplayerService.start();

		try {
			handshakeSemaphore.acquire();
		} catch (InterruptedException e1) {
		}

		MAP = mMultiplayerService.mpHandler.MAP;
		DIFFICULTY = mMultiplayerService.mpHandler.DIFFICULTY;
		GAMEMODE = mMultiplayerService.mpHandler.GAMEMODE;

		// The following is used to determine if the Server is trying to start a
		// fullversion game while the
		// client only can run a lite version game.
		boolean serverSettingsOK = true;
		if (fullversion == 0) {
			if (MAP != 1 && MAP != 2) {
				MAP = 1;
			}
			serverSettingsOK = false;
		}

		String mapMsg = "CLIENT:" + serverSettingsOK;
		byte[] sendMsg = mapMsg.getBytes();
		mMultiplayerService.write(sendMsg);

		GameInit.setMultiplayer(mMultiplayerService);
		Intent StartGame = new Intent(this, GameInit.class);
		StartGame.putExtra("com.crackedcarrot.menu.map", MAP);
		StartGame.putExtra("com.crackedcarrot.menu.difficulty", DIFFICULTY);
		StartGame.putExtra("com.crackedcarrot.menu.wave", GAMEMODE);
		startActivity(StartGame);
		// Cancel the thread that completed the connection
		mConnectThread = null;
		finish();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 1: {
			ProgressDialog dialog = new ProgressDialog(this);
			dialog.setMessage("Connecting to server. Press back button to cancel connection.");
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

				public void onCancel(DialogInterface dialog) {
					dialog.dismiss();
				}
			});
			return dialog;
		}
		}
		return null;
	}

}