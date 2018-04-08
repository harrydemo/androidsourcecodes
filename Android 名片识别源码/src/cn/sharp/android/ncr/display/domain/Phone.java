package cn.sharp.android.ncr.display.domain;

public class Phone extends BaseDomain {
	private static int ID = 0;

	public Phone() {
		super(ID++);
	}

	public int type;
	public String value;
}
