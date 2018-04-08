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
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import TorJava.Common.QueueHandler;
import TorJava.Common.TorException;

/**
 * used to be TCPStreamThreadTor2Java
 */
class QueueTor2JavaHandler implements QueueHandler {
	TCPStream stream;

	PipedInputStream sin; // read from tor and output to this stream

	PipedOutputStream fromtor; // private end of this pipe

	boolean stopped; // as stop() is deprecated we use this toggle variable

	QueueTor2JavaHandler(TCPStream stream) {
		this.stream = stream;
		try {
			sin = (PipedInputStream) new SafePipedInputStream();
			fromtor = new PipedOutputStream(sin);
		} catch (IOException e) {
			Logger.logStream(Logger.ERROR,
					"QueueTor2JavaHandler: caught IOException "
					+ e.getMessage());
		}
	}

	public void close() {
		this.stopped = true;
		/* leave data around, until no more referenced by someone else */
		//try{ sin.close(); } catch(Exception e) {}
		try{ fromtor.close();  } catch(Exception e) {}
	}

	/** return TRUE, if cell was handled */
	public boolean handleCell(Cell cell) 
		throws TorException
	{
		if(stream.closed || this.stopped) return false;
		if (cell == null) return false;
		if (!cell.isTypeRelay()) return false;

		CellRelay relay = (CellRelay) cell;
		if (relay.isTypeData()) {
			Logger.logStream(Logger.RAW_DATA,
					"QueueTor2JavaHandler.handleCell(): stream "
					+ stream.ID + " received data");
			try {
				fromtor.write(relay.data, 0, relay.length);
			} catch (IOException e) {
				Logger.logStream(Logger.ERROR,
						"QueueTor2JavaHandler.handleCell(): caught IOException "
						+ e.getMessage());
			}
			return true;
		} else if (relay.isTypeEnd()) {
			Logger.logStream(Logger.RAW_DATA,
					"QueueTor2JavaHandler.handleCell(): stream "
					+ stream.ID + " is closed: "
					+ relay.reasonForClosing());
			stream.closed_for_reason = (int) (relay.payload[0]) & 0xff;
			stream.closed = true;
			stream.close(true);
			this.stopped = true;
			return true;
		}
		return false;
	}
}
