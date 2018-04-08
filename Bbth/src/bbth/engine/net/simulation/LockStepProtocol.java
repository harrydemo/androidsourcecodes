package bbth.engine.net.simulation;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import bbth.engine.net.bluetooth.Protocol;

/**
 * Reads and writes LockSteps and Events in a compressed format:
 * 
 * <pre>
 * struct Event {
 *     byte type; // high order bits for flags
 *     int time; // the number of fine timesteps from the start of the game
 *     float x;
 *     float y;
 *     byte code;
 * }
 * </pre>
 */
public class LockStepProtocol implements Protocol {

	private BlockingQueue<LockStep> incoming = new PriorityBlockingQueue<LockStep>();
	private BlockingQueue<LockStep> outgoing = new PriorityBlockingQueue<LockStep>();

	public LockStep readLockStep() {
		return incoming.poll();
	}

	public void writeLockStep(LockStep step) {
		outgoing.add(step);
	}

	@Override
	public void readFrom(DataInputStream in) throws IOException,
			InterruptedException {
		LockStep step = new LockStep();
		int count = in.readByte();
		step.hash = in.readInt();
		step.coarseTime = in.readInt();

		for (int i = 0; i < count; i++) {
			Event event = new Event();

			// Deserialize the event
			event.id = in.readInt();
			int type = in.readByte();
			event.type = type & 0x03;
			event.flags = type & 0xF8;
			event.fineTime = in.readInt();
			event.x = in.readFloat();
			event.y = in.readFloat();
			event.code = in.readByte();

			step.events.add(event);
		}

		incoming.put(step);
	}

	@Override
	public void writeTo(DataOutputStream out) throws IOException,
			InterruptedException {
		LockStep step = outgoing.take();
		out.writeByte(step.events.size());
		out.writeInt(step.hash);
		out.writeInt(step.coarseTime);

		for (int i = 0, count = step.events.size(); i < count; i++) {
			Event event = step.events.get(i);

			// Serialize the event
			out.writeInt(event.id);
			out.writeByte(event.type | event.flags);
			out.writeInt(event.fineTime);
			out.writeFloat(event.x);
			out.writeFloat(event.y);
			out.writeByte(event.code);
		}
	}
}
