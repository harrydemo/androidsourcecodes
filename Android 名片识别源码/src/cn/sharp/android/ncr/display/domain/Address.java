package cn.sharp.android.ncr.display.domain;

public class Address extends BaseDomain {
	private static int ID = 0;

	public Address() {
		super(ID++);
	}

	public int type;
	public String value;
	/**
	 * this field is appended to the field <i>value</i> above, with the
	 * character '\' separated
	 */
	public String postcode;

}
