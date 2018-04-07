package TorJava;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;


public class DefaultHiddenServiceRequestHandler implements HiddenServiceRequestHandler {

	private int mLocalPort = 0;
	private boolean closed = false;
	
	ArrayList<StreamFerry> mThreads = new ArrayList<StreamFerry>();
	
	public DefaultHiddenServiceRequestHandler(int port) {
		this.mLocalPort = port;
	}
	
	/**
	 * accept an incoming hidden service connection
	 */
	@Override
	public boolean accept(TCPStream incomingConnection) {
		try {
			synchronized(this) {
				if (closed) return false;
				Socket localServer = new Socket("127.0.0.1", mLocalPort);
				mThreads.add(new StreamFerry(incomingConnection, localServer, true));
				mThreads.add(new StreamFerry(incomingConnection, localServer, false));
			}
		} catch (IOException e) {
			// Try to notify the remote client
			incomingConnection.close();
		}
		return true;
	}
	
	/**
	 * StreamFerry ferries data between a hidden service connection and a local server
	 * @author cmg47
	 *
	 */
	private class StreamFerry extends Thread {
		
		public static final int mBufferSize = 498;
		
		private TCPStream remoteClient = null;
		private Socket localServer = null;
		private InputStream in = null;
		private OutputStream out = null;
		
		private boolean stopped = false;
		
		public StreamFerry(TCPStream remoteClient, Socket localServer, boolean leaving) {
			this.remoteClient = remoteClient;
			this.localServer = localServer;
			try {
				if (leaving) {
					this.in = localServer.getInputStream();
					this.out = remoteClient.getOutputStream();
				} else {
					this.in = remoteClient.getInputStream();
					this.out = localServer.getOutputStream();
				}
				this.start();
			} catch (IOException e) {
				// IOException occurs on local server connection
				remoteClient.close();
			}
		}
		
		@Override
		public void run() {
			super.run();
			byte[] buffer = new byte[mBufferSize];
			int read;
			while (!stopped) {
				try {
					read = in.read(buffer);
					if (read <= 0) close();
					else {
						out.write(buffer, 0, read);
						out.flush();
					}
				} catch (IOException e) {
					close();
				}
			}
		}
		
		/**
		 * close this connection
		 */
		public void close() {
			stopped = true;
			this.interrupt();
			try {
				if (!remoteClient.closed) remoteClient.close();
				if (!localServer.isClosed()) localServer.close();
			} catch (IOException e) {
				// Oh well, we tried.
			}
			threadFinished(this);
		}
	}

	/**
	 * for StreamFerrys when they have finished ferrying 
	 * @param s
	 */
	private void threadFinished(StreamFerry s) {
		mThreads.remove(s);
	}
	
	/**
	 * stop all threads
	 */
	@Override
	public void close() {
		synchronized(this) {
			closed = true;
			StreamFerry[] ferries = new StreamFerry[mThreads.size()];
			mThreads.toArray(ferries);
			for (int i=0; i<ferries.length; i++) {
				if (ferries[i] != null) ferries[i].close();
			}
			mThreads.clear();
		}
		
	}
}
