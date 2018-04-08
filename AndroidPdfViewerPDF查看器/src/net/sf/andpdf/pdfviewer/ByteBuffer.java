package net.sf.andpdf.pdfviewer;

public class ByteBuffer {

	private byte[] buf;
	private int ofs;
	private int pos;
	private int siz;
	private int mrk;
	
	public ByteBuffer(byte[] buf) {
		this(buf,0,0,(buf==null?0:buf.length));
	}

	public ByteBuffer(byte[] buf, int ofs, int pos, int siz) {
		this.buf = buf;
		this.ofs = ofs;
		this.pos = pos;
		this.siz = siz;
	}

	public final int position() {
		return pos-ofs;
	}

	public final void position(int position) {
		// TODO: check range 0..length-1
		pos = position+ofs;
	}

	public final byte get() {
		// TODO: check range 
		return buf[pos++];
	}

	public final int remaining() {
		return siz-pos;
	}

	public final byte get(int position) {
		return buf[position+ofs];
	}

	public final static ByteBuffer allocate(int size) {
		return new ByteBuffer(new byte[size]);
	}

	public final ByteBuffer slice() {
		return new ByteBuffer(buf, pos, pos, siz);
	}

	public final void limit(int length) {
		// TODO: check range 0..buf.length
		siz = ofs+length;
	}

	public final void get(byte[] outBuf) {
		System.arraycopy(buf, pos, outBuf, 0, outBuf.length);
		pos += outBuf.length;
	}

	public final void rewind() {
		pos = ofs;
	}

	public final int limit() {
		return siz-ofs;
	}

	public final boolean hasArray() {
		return true;
	}

	public final int arrayOffset() {
		return ofs;
	}

	public final byte[] array() {
		return buf;
	}

	public final void flip() {
		siz = pos;
		pos = ofs;
	}

	public final ByteBuffer duplicate() {
		return new ByteBuffer(buf, ofs, pos, siz);
	}

	public final static ByteBuffer wrap(byte[] bytes) {
		return new ByteBuffer(bytes);
	}

	public final char getChar(int position) {
		// TODO: check current byteorder, assume BIG_ENDIAN
		int result = get(position++)&0xff;
		result = (result<<8)+(get(position)&0xff);
		return (char)result;
	}
	
	public final int getInt() {
		// TODO: check current byteorder, assume BIG_ENDIAN
		int result = get()&0xff;
		result = (result<<8)+(get()&0xff);
		result = (result<<8)+(get()&0xff);
		result = (result<<8)+(get()&0xff);
		return result;
	}

	public final long getLong() {
		// TODO: check current byteorder, assume BIG_ENDIAN
		long result = get()&0xff;
		result = (result<<8)+(get()&0xff);
		result = (result<<8)+(get()&0xff);
		result = (result<<8)+(get()&0xff);
		result = (result<<8)+(get()&0xff);
		result = (result<<8)+(get()&0xff);
		result = (result<<8)+(get()&0xff);
		result = (result<<8)+(get()&0xff);
		return result;
	}

	
	public final char getChar() {
		// TODO: check current byteorder, assume BIG_ENDIAN
		int result = get()&0xff;
		result = (result<<8)+(get()&0xff);
		return (char)result;
	}
	
	public final short getShort() {
		int result = get()&0xff;
		result = (result<<8)+(get()&0xff);
		return (short)result;
	}

	public final void put(int index, byte b) {
		buf[index+ofs] = b;
	}

	public final void put(byte b) {
		buf[pos++] = b;
	}

	public final void putInt(int i) {
		put((byte)((i>>24)&0xff));
		put((byte)((i>>16)&0xff));
		put((byte)((i>>8) &0xff));
		put((byte)( i     &0xff));
	}

	public final void putShort(short s) {
		put((byte)((s>>8) &0xff));
		put((byte)( s     &0xff));
	}

	public final void mark() {
		mrk = pos;
	}

	public final void put(ByteBuffer data) {
		int len = data.remaining();
		System.arraycopy(data.array(), data.position()+data.arrayOffset(), buf, pos, len);
		pos += len;
	}

	public final void reset() {
		// TODO: check for mark set
		pos = mrk;
	}

	public final void putInt(int index, int value) {
		put(index++,(byte)((value>>24)&0xff));
		put(index++,(byte)((value>>16)&0xff));
		put(index++,(byte)((value>>8) &0xff));
		put(index  ,(byte)( value     &0xff));
	}

	public final void putLong(long value) {
		put((byte)((value>>56)&0xff));
		put((byte)((value>>48)&0xff));
		put((byte)((value>>40)&0xff));
		put((byte)((value>>32)&0xff));
		put((byte)((value>>24)&0xff));
		put((byte)((value>>16)&0xff));
		put((byte)((value>>8) &0xff));
		put((byte)( value     &0xff));
	}

	public final void putChar(char value) {
		put((byte)((value>>8) &0xff));
		put((byte)( value     &0xff));
	}
	public final void put(byte[] data) {
		int len = data.length;
		System.arraycopy(data, 0, buf, pos, len);
		pos += len;
	}

	public final void get(byte[] outBuf, int outOffset, int length) {
		System.arraycopy(buf, pos, outBuf, outOffset, length);
		pos += length;
	}

	public final java.nio.ByteBuffer toNIO() {
		return java.nio.ByteBuffer.wrap(buf, pos, siz-pos); 
	}

	public final boolean hasRemaining() {
		return pos < siz;
	}

	public final static ByteBuffer fromNIO(java.nio.ByteBuffer nioBuf) {
		return new ByteBuffer(nioBuf.array(), nioBuf.arrayOffset(), nioBuf.arrayOffset()+nioBuf.position(), nioBuf.arrayOffset()+nioBuf.position()+nioBuf.remaining());
	}



}
