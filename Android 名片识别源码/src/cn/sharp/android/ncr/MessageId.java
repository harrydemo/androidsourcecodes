package cn.sharp.android.ncr;

public class MessageId {
	private final static int INTERVAL = 1000;
	public final static int NAMECARD_REC_SUCCESS = 0;
	public final static int NAMECARD_REC_FAILURE = NAMECARD_REC_SUCCESS
			+ INTERVAL;

	// public final static int DECODE_IMAGE_DATA_SUCCESS = 1;
	// public final static int DECODE_IMAGE_DATA_FAILURE =
	// DECODE_IMAGE_DATA_SUCCESS
	// + INTERVAL;

	public final static int SAVE_CONTACT_SUCCESS = 2;

	public final static int REC_THREAD_STOPPED = 3;
	public final static int DECODE_THREAD_STOPPED=4;
}
