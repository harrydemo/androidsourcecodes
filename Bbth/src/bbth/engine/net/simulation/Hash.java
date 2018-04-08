package bbth.engine.net.simulation;

public class Hash {

	// from the internets
	public static int mix(int a) {
		a = (a + 0x7ed55d16) + (a << 12);
		a = (a ^ 0xc761c23c) ^ (a >> 19);
		// a = (a + 0x165667b1) + (a << 5);
		// a = (a + 0xd3a2646c) ^ (a << 9);
		// a = (a + 0xfd7046c5) + (a << 3);
		// a = (a ^ 0xb55a4f09) ^ (a >> 16);
		return a;
	}

	public static int mix(int hash, int value) {
		return mix(hash ^ value);
	}

	public static int mix(int hash, float value) {
		return Hash.mix(hash, Float.floatToRawIntBits(value));
	}
}
