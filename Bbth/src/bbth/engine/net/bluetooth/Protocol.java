package bbth.engine.net.bluetooth;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface Protocol {

	/**
	 * After the Bluetooth socket gets a chunk of data, attempt to parse that
	 * chunk. Will throw an IOException if the stream reads past the end of the
	 * chunk. Called repeatedly by the Bluetooth read thread.
	 */
	public void readFrom(DataInputStream in) throws IOException,
			InterruptedException;

	/**
	 * Write the next chunk of data to the Bluetooth socket. Called repeatedly
	 * by the Bluetooth write thread.
	 */
	public void writeTo(DataOutputStream out) throws IOException,
			InterruptedException;
}
