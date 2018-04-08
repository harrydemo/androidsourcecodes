package bbth.engine.net.simulation;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class LockStep implements Comparable<LockStep> {

	public int hash;
	public int coarseTime;
	public ArrayList<Event> events = new ArrayList<Event>();

	@Override
	public int compareTo(LockStep other) {
		return coarseTime - other.coarseTime;
	}

	public void addEventsToQueue(PriorityQueue<Event> queue) {
		for (int i = 0, n = events.size(); i < n; i++) {
			queue.add(events.get(i));
		}
	}
}
