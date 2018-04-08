package it.telecomitalia.jchat;

import android.location.Location;

/**
 * Specialization of {@link Location} that also contains a flag that checks if
 * the location has moved
 * <p>
 * Used by the agent to store all modifications to the contacts lists that
 * should be done also to the GUI
 * <p>
 * This object is immutable by design.
 * 专业化的{@ link位置}也包含一个标志，检查位置移动，
 * 使用代理来存储所有修改的接触，也应该做的GUI对象名单是由设计一成不变。
 */

//接触者位置信息
public class ContactLocation extends Location
{

	/**
	 * true if contact location has changed, false otherwise
	 * 如果接触的位置已更改，否则返回false
	 */
	private boolean hasMoved;

	/**
	 * Instantiates a new empty contact location. Each component is initialized
	 * to positive infinity
	 * 实例化一个新的空接触的位置。每个组件都初始化为正无穷大
	 * @param providerName
	 *            name of the location provider (gps/network)
	 */
	public ContactLocation(String providerName)
	{
		super(providerName);
		hasMoved = false;
		setLatitude(Double.POSITIVE_INFINITY);
		setLongitude(Double.POSITIVE_INFINITY);
		setAltitude(Double.POSITIVE_INFINITY);

	}

	/**
	 * Creates a new copy of contact location.
	 * 接触的位置创建一个新副本。
	 * @param toBeCopied
	 *            the to be copied
	 */
	public ContactLocation(ContactLocation toBeCopied)
	{
		this(toBeCopied, toBeCopied.hasMoved);
	}

	/**
	 * Instantiates a new contact location given location and boolean flag
	 * 实例化一个新的接触位置，给定的位置和布尔标志
	 * @param loc
	 *            the contact location
	 * @param moved
	 *            the initial value of the boolean flag
	 */
	private ContactLocation(Location loc, boolean moved)
	{
		super(loc);
		hasMoved = moved;
	}

	/**
	 * Changes the location of this contact and sets its internal state to
	 * moving
	 * 改变这种接触的位置，并设置其内部状态到移动
	 * @param loc
	 *            the new contact location
	 */
	public void changeLocation(Location loc)
	{
		this.hasMoved = !this.equals(loc);
		if (hasMoved)
		{
			setLatitude(loc.getLatitude());
			setLongitude(loc.getLongitude());
			setAltitude(loc.getAltitude());
		}
	}

	/**
	 * Checks if the {@link ContactLocation} is changed.
	 * 如果{@的链接ContactLocation}改变的检查。
	 * @return true, if successful
	 */
	public boolean hasMoved()
	{
		return hasMoved;
	}

	/**
	 * Two Contact location are the same if they match their altitude, longitude
	 * and latitude
	 * 两个接触位置是相同的，如果它们匹配它们的海拔高度，经度和纬度
	 * @param l
	 *            location we have to compare
	 * @return true if equals, false otherwise
	 */
	public boolean equals(Location l)
	{
		return ((this.getAltitude() == l.getAltitude()) && (this.getLatitude() == l.getLatitude()) && (this.getLongitude() == l.getLongitude()));
	}

}
