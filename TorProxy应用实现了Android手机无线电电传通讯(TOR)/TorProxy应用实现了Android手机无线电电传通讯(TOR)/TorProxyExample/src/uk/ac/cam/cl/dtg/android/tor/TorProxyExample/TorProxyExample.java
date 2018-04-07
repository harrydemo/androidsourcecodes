package uk.ac.cam.cl.dtg.android.tor.TorProxyExample;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import uk.ac.cam.cl.dtg.android.tor.TorProxyLib.ITorProxyControl;
import uk.ac.cam.cl.dtg.android.tor.TorProxyLib.SocksProxy;
import uk.ac.cam.cl.dtg.android.tor.TorProxyLib.TorProxyLib;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TorProxyExample extends Activity implements Runnable, OnClickListener {
	
	private static final String HOST = "www.something.com";
	private static final int PORT = 80;
	
	private TextView mStatusView = null;
	private TextView mHttpResult = null;
	private Button mOpenTorProxy = null;
	
	private boolean mFetching = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		mStatusView = (TextView)findViewById(R.id.statusView);
		mHttpResult = (TextView)findViewById(R.id.httpResult);
		mOpenTorProxy = (Button)findViewById(R.id.openTorProxy);
		mOpenTorProxy.setOnClickListener(this);
	}

	// Keep track of the control service
	private ITorProxyControl mControlService = null;
	private final IntentFilter torStatusFilter = new IntentFilter(
			TorProxyLib.STATUS_CHANGE_INTENT);

	// Service connection to TorProxy service
	private ServiceConnection mSvcConn = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {

			mControlService = ITorProxyControl.Stub.asInterface(service);
			// Connected to Control Service
			// Perhaps check Tor status here
			updateTorStatus();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {

			mControlService = null;
			// Connection to Control Service lost
		}

	};

	@Override
	protected void onResume() {

		super.onResume();
		
		// Register to receive Tor status update broadcasts
		registerReceiver(mBroadcastReceiver, torStatusFilter);

		// Bind to the TorProxy control service
		bindService(new Intent().setComponent(new ComponentName(
				TorProxyLib.CONTROL_SERVICE_PACKAGE,
				TorProxyLib.CONTROL_SERVICE_CLASS)), mSvcConn, BIND_AUTO_CREATE);
	}

	@Override
	protected void onPause() {
		// Registered in onResume so unregister here
		unregisterReceiver(mBroadcastReceiver);
		unbindService(mSvcConn);

		super.onPause();
	}

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			if (TorProxyLib.STATUS_CHANGE_INTENT.equals(intent.getAction())) {

				// TorProxy has broadcast a Tor status update
				// Check Tor status here
				updateTorStatus();
			}
		}
	};

	private void updateTorStatus() {
		
		try {
			int torStatus = mControlService.getStatus();
			int torProfile = mControlService.getProfile();
			switch (torStatus) {
			case TorProxyLib.STATUS_ON:
				setStatus("available");
				startHttpRequest();
				break;
			case TorProxyLib.STATUS_REQUIRES_DEMAND:
				setStatus("requires demand");
				mControlService.registerDemand();
				break;
			case TorProxyLib.STATUS_CONNECTING:
				setStatus("connecting...");
				break;
			case TorProxyLib.STATUS_UNAVAILABLE:
				if (torProfile == TorProxyLib.PROFILE_OFF)
					setStatus("anonymous connection is turned off");
				else
					setStatus("anonymous connection is unavailable (no network?)");
				break;
			}
		} catch (RemoteException e) {
			setStatus("Error communicating with control service");
		}
	}
	
	private void setStatus(final String s) {
		// Update the UI with a status
		// Post to the view since we are probably being called from
		// another thread.
		mStatusView.post(new Runnable() {
			@Override
			public void run() {
				mStatusView.setText("Status: " + s);
			}
		});
	}
	
	private void setHttpResult(final String s) {
		// Update the UI with an HttpResult
		// Post to the view since we are probably being called from
		// another thread.
		mHttpResult.post(new Runnable() {
			@Override
			public void run() {
				mHttpResult.setText(s);
			}
		});
	}
	
	private void startHttpRequest() {
		new Thread(this).start();
	}
	
	@Override
	public void run() {
	
		// Only allow one fetch at a time
		synchronized(this) {
			if (mFetching == true) return;
			mFetching = true;
		}
		
		try {
			// Get the proxy port
			int proxyPort = mControlService.getSOCKSPort();
			SocksProxy proxy = new SocksProxy(proxyPort);
			
			try {
				setHttpResult("Getting " + HOST);
				// Create a socket to the destination through the
				// anonymous proxy
				Socket s = proxy.connectSocksProxy(null, HOST, PORT, 0);
				//Socket s = new Socket(HOST, PORT);
				
				PrintWriter writer = new PrintWriter(s.getOutputStream());
				InputStreamReader reader = new InputStreamReader(s.getInputStream());
				
				// Very simple HTTP GET
				writer.println("GET / HTTP/1.1");
				writer.println("Host: " + HOST);
				writer.println("Connection: close");
				writer.println();
				writer.flush();
				
				// Get the result
				StringBuilder result = new StringBuilder();
				char[] buffer = new char[1024];
				int read = 0;
				do {
					result.append(buffer, 0, read);
					read = reader.read(buffer, 0, buffer.length);
				} while (read > -1);
				
				// Update the UI
				setHttpResult(result.toString());
				
			} catch (UnknownHostException e) {
				setHttpResult("Unknown host: " + HOST);
			} catch (IOException e) {
				setHttpResult("IOException");
			}
			
		} catch (RemoteException e1) {
			setHttpResult("Unable to communicate with control service");
		}
		
		synchronized(this) {
			mFetching = false;
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.openTorProxy) {
			try {
				Intent i = new Intent().setComponent(new ComponentName(
						TorProxyLib.SETTINGS_ACTIVITY_PACKAGE,
						TorProxyLib.SETTINGS_ACTIVITY_CLASS));
				startActivity(i);
			} catch (ActivityNotFoundException a) {
				AlertDialog.Builder b = new AlertDialog.Builder(this);
				b.setMessage("TorProxy must be installed for this application to work");
				b.setPositiveButton("OK", null);
				b.show();
			}
		}
	}
}
