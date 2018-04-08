/**
 * OnionCoffee - Anonymous Communication through TOR Network
 * Copyright (C) 2005-2007 RWTH Aachen University, Informatik IV
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * version 2 as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 */
package TorJava;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import TorJava.Common.Encoding;

/**
 * 
 * @author Lexi Pimenidis
 */
class TCPStreamOutputStream extends OutputStream {
	TCPStream stream;

	PipedOutputStream sout; // read from this, and forward to tor

	PipedInputStream fromjava; // private end of this pipe

	boolean stopped; // as stop() is depreacated we use this toggle variable

	byte[] buffer;
	int bufferFilled;

	TCPStreamOutputStream(TCPStream stream) {
		this.stream = stream;
		buffer = new byte[CellRelay.RELAY_DATA_SIZE];
		bufferFilled = 0;
		try {
			sout = new PipedOutputStream();
			fromjava = new PipedInputStream(sout);
		} catch (IOException e) {
			Logger.logStream(Logger.ERROR,
					"TCPStreamThreadJava2Tor: caught IOException "
					+ e.getMessage());
		}
	}

	public void close() {
		this.stopped = true;
	}

	public void write(int b) 
		throws IOException
	{
		write(Encoding.intToNByteArray(b,4));
	}
	
	public synchronized void flush() 
		throws IOException
	{
		if (stopped) 
			throw new IOException("TCPStreamOutputStream.flush(): output closed");
		if (bufferFilled<1) return;
		if (bufferFilled>buffer.length) 
			throw new IOException("TCPStreamOutputStream.flush(): there must be an error somewhere else");

		CellRelayData cell = new CellRelayData(stream);
		cell.length = bufferFilled;
		if (cell.length > cell.data.length)
			cell.length = cell.data.length;
		System.arraycopy(buffer,0, cell.data,0, bufferFilled);
		stream.send_cell(cell);
		bufferFilled=0;
	}

	public void write(byte[] b,int off,int len)
		throws IOException
	{
		if (len==0) return;
		/*if (len<0) throw new IOException("TCPStreamOutputStream.write(): len = "+len);
		if (bufferFilled > buffer.length) throw new IOException("TCPStreamOutputStream.write(): filled = "+bufferFilled); */

		int bytesFree = buffer.length;
		if (bufferFilled == buffer.length) 
			flush();
		else
			bytesFree = buffer.length-bufferFilled;

		if(len>bytesFree) {
			write(b,off,bytesFree);
			write(b,off+bytesFree,len-bytesFree);
		} else {
			System.arraycopy(b,off, buffer, bufferFilled,len);
			bufferFilled+=len;
			if (bufferFilled==buffer.length) flush();
		};
	}

	public void write(byte[] b)
		throws IOException
	{
		write(b,0,b.length);
	}
	
}
