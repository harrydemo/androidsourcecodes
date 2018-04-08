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
 * רҵ����{@ linkλ��}Ҳ����һ����־�����λ���ƶ���
 * ʹ�ô������洢�����޸ĵĽӴ���ҲӦ������GUI���������������һ�ɲ��䡣
 */

//�Ӵ���λ����Ϣ
public class ContactLocation extends Location
{

	/**
	 * true if contact location has changed, false otherwise
	 * ����Ӵ���λ���Ѹ��ģ����򷵻�false
	 */
	private boolean hasMoved;

	/**
	 * Instantiates a new empty contact location. Each component is initialized
	 * to positive infinity
	 * ʵ����һ���µĿսӴ���λ�á�ÿ���������ʼ��Ϊ�������
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
	 * �Ӵ���λ�ô���һ���¸�����
	 * @param toBeCopied
	 *            the to be copied
	 */
	public ContactLocation(ContactLocation toBeCopied)
	{
		this(toBeCopied, toBeCopied.hasMoved);
	}

	/**
	 * Instantiates a new contact location given location and boolean flag
	 * ʵ����һ���µĽӴ�λ�ã�������λ�úͲ�����־
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
	 * �ı����ֽӴ���λ�ã����������ڲ�״̬���ƶ�
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
	 * ���{@������ContactLocation}�ı�ļ�顣
	 * @return true, if successful
	 */
	public boolean hasMoved()
	{
		return hasMoved;
	}

	/**
	 * Two Contact location are the same if they match their altitude, longitude
	 * and latitude
	 * �����Ӵ�λ������ͬ�ģ��������ƥ�����ǵĺ��θ߶ȣ����Ⱥ�γ��
	 * @param l
	 *            location we have to compare
	 * @return true if equals, false otherwise
	 */
	public boolean equals(Location l)
	{
		return ((this.getAltitude() == l.getAltitude()) && (this.getLatitude() == l.getLatitude()) && (this.getLongitude() == l.getLongitude()));
	}

}
