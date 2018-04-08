package cn.sharp.android.ncr.display.domain;

public class Email extends BaseDomain {
	private static int ID = 0;
	public int type;
	public String value;

	public Email() {
		super(ID++);
	}
}
