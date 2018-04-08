package cn.sharp.android.ncr.display.domain;

public class Url extends BaseDomain {
	private static int ID = 0;

	public Url() {
		super(ID++);
	}

	public int type;
	public String value;
}
