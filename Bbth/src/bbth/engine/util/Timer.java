package bbth.engine.util;

public class Timer {

	private long start;
	private float time;

	public void start() {
		start = System.nanoTime();
	}

	public void stop() {
		time += ((System.nanoTime() - start) / 1000000000.0f - time) * 0.05f;
	}

	public int getMilliseconds() {
		return (int) (time * 1000);
	}
}
