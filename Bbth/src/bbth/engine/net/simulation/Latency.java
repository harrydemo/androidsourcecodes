package bbth.engine.net.simulation;

public class Latency {

	private boolean hasPrev;
	private long totalRTT;
	private long totalOffset;
	private int prevLocalTime;
	private int count;
	private int packetCount;

	public void reset() {
		hasPrev = false;
		totalRTT = 0;
		totalOffset = 0;
		prevLocalTime = 0;
		count = 0;
		packetCount = 0;
	}

	public int getAverageRTT() {
		return count > 0 ? (int) (totalRTT / count) : 0;
	}

	public int getAverageOffset() {
		return count > 0 ? (int) (totalOffset / count) : 0;
	}

	public int getPacketCount() {
		return packetCount;
	}

	public void recordTime(int remoteTime, int localTime) {
		// magic formula
		if (hasPrev) {
			totalOffset += remoteTime - localTime + (localTime - prevLocalTime)
					/ 2;
			totalRTT += localTime - prevLocalTime;
			count++;
		}

		prevLocalTime = localTime;
		hasPrev = true;
		packetCount++;
	}

	public float getForwardSimulationSeconds(int oldRemoteTime, int localTime) {
		int newRemoteTime = localTime + getAverageOffset();
		return (newRemoteTime - oldRemoteTime) * 0.001f;
	}
}
