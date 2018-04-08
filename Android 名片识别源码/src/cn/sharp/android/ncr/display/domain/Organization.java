package cn.sharp.android.ncr.display.domain;

public class Organization extends BaseDomain {
	private static int ID = 0;

	public Organization() {
		super(ID++);
	}

	public int type;
	public String company;
	public String title;
}
