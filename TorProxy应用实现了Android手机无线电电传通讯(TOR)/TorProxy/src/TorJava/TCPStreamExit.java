package TorJava;

import java.io.IOException;

import TorJava.Common.Queue;

public class TCPStreamExit extends TCPStream {

	TCPStreamExit(Circuit c, CellRelay begin) throws IOException {
		super(c);
		
		this.ID = begin.streamID;
		
		c.registerEstablishedStream(this.ID, this);
		
		queue = new Queue(queue_timeout);
		
		qhFC = new QueueFlowControlHandler(this,streamLevelFlowControl,streamLevelFlowControlIncrement);
        this.queue.addHandler(qhFC);
        qhT2J = new QueueTor2JavaHandler(this);
        this.queue.addHandler(qhT2J);
        outputStream = new TCPStreamOutputStream(this);
		
		send_cell(new CellRelay(this, CellRelay.RELAY_CONNECTED));
		
	}

}
