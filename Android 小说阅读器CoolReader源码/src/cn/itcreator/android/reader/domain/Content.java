package cn.itcreator.android.reader.domain;

public class Content {
	
	private long index;
	private long length;
	
	public Content(long index, long length){
		this.index = index;
		this.length = length;
	}
	
	public long getIndex() {
		return index;
	}
	public void setIndex(long index) {
		this.index = index;
	}
	public long getLength() {
		return length;
	}
	public void setLength(long length) {
		this.length = length;
	}

}
