// Created by plusminus on 19:14:08 - 20.10.2008
package org.andnav.osm.tileprovider;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StreamUtils {
	
	// ===========================================================
	// Constants
	// ===========================================================
	
	private static final Logger logger = LoggerFactory.getLogger(StreamUtils.class);
	
	public static final int IO_BUFFER_SIZE = 8 * 1024;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * This is a utility class with only static members.
	 */
	private StreamUtils() {
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	
	/**
	 * Copy the content of the input stream into the output stream, using a temporary
	 * byte array buffer whose size is defined by {@link #IO_BUFFER_SIZE}.
	 *
	 * @param in The input stream to copy from.
	 * @param out The output stream to copy to.
	 *
	 * @throws IOException If any error occurs during the copy.
	 */
	public static void copy(InputStream in, OutputStream out) throws IOException {
		byte[] b = new byte[IO_BUFFER_SIZE];
		int read;
		while ((read = in.read(b)) != -1) {
			out.write(b, 0, read);
		}
	}

	/**
	 * Closes the specified stream.
	 *
	 * @param stream The stream to close.
	 */
	public static void closeStream(Closeable stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {
				logger.error("IO", "Could not close stream", e);
			}
		}
	} 

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
