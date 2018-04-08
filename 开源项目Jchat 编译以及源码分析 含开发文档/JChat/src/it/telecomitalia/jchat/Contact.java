package it.telecomitalia.jchat;

/**
 * This class represents a generic contact that can be online or offline. Each
 * contact has a name and a mandatory phone number. Please note that the Agent
 * shall be identified with the phone number. It seems that the emulator does
 * not support a way to customize the phone number. So for now the application
 * reads the number from a property file. Each contact has non null location:
 * online contacts have a location that is updated while offline contacts have a
 * fixed location (null location)(they should not be drawn).
 * <p>
 * This class is almost immutable. The <code>isOnline</code> flag is the only
 * mutable field 
 * 这个类表示一个通用的接触，可以联机或脱机。  
 * 每个联系人都有一个名称和一个强制性的电话号码。请注意，代理应确定电话号码。
 * 看来，模拟器不支持的方式来定制的电话号码。因此，对于现在的应用程序读取的数目  
 * 从一个属性文件。
 *  每个联系人都有非空的位置：在线联系人有一个更新的位置  
 * 离线联系人，同时有一个固定的位置（空位置）（他们不应该被画）。  
 * 这个类几乎是一成不变的。在<code> isOnline</ code>的标志是唯一的可变领域
 */

//接触者
public class Contact
{
	/**
	 * The contact phone number.联系电话号码。
	 */
	private final String phoneNumber;

	/**
	 * Status of the contact. This flag is mutable接触的状态。这个标志是可变
	 */
	private volatile boolean isOnline;

	/**
	 * The contact name. It is the one on the local phone contacts if the
	 * contact is known, the phone number otherwise.
	 * 
	 * *联系人姓名。如果知道联系人，它是一个本地电话接触，否则被称为其它。
	 */
	private final String name;

	/**
	 * True if the contact is present in local phone contacts, false otherwise.
	 * 真正接触是目前在本地电话联系，否则为false。
	 * 是否在电话本上
	 */
	private final boolean storedOnPhone;

	/**
	 * Instantiates a new contact.
	 * 
	 * @param name
	 *            Contact name
	 * @param phoneNumber
	 *            contact phone number
	 * @param stored
	 *            true if contact is stored on phone, false otherwise
	 *            构造
	 */
	public Contact(String name, String phoneNumber, boolean stored)
	{
		this.name = name;
		this.phoneNumber = phoneNumber;
		isOnline = false;
		storedOnPhone = stored;
	}

	/**
	 * Performs a deep copy of a contact.
	 * 执行一个接触的深拷贝。
	 * @param c
	 *            the contact to be copied
	 */
	public Contact(Contact c)
	{
		this.name = c.name;
		this.phoneNumber = c.phoneNumber;
		this.isOnline = c.isOnline;
		this.storedOnPhone = c.storedOnPhone;
	}

	/**
	 * Gets the contact phone number.
	 * 取得联系的电话号码。
	 * @return the phone number
	 */
	public String getPhoneNumber()
	{
		return phoneNumber;
	}

	/**
	 * Gets the contact name.
	 * 得到接触者的名字
	 * @return the contact name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Checks if a contact is stored on phone.
	 * 如果检查接触存储在手机上。
	 * @return true if it is stored on phone, false otherwise
	 */
	public boolean isStoredOnPhone()
	{
		return storedOnPhone;
	}

	/**
	 * Prints out the contact name
	 * 输出接触者的名字
	 * @return the contact name
	 */
	public String toString()
	{
		return name;
	}

	/**
	 * Change contact status to online.
	 * 改变接触状态在线。
	 */
	public void setOnline()
	{
		isOnline = true;
	}

	/**
	 * Change contact status to offline.
	 * 改变接触状态为脱机。
	 */
	public void setOffline()
	{
		isOnline = false;
		;
	}

	/**
	 * Checks if contact is online.
	 * 确认接触者是否在线
	 * @return true if online, false otherwise
	 */
	public boolean isOnline()
	{
		return isOnline;
	}

	/**
	 * Checks for contacts equality
	 * 检查联系人是否同一个
	 * @param o
	 *            the object to be checked
	 * @return true if contacts have the same phone number, false otherwise
	 */
	public boolean equals(Object o)
	{

		boolean res = false;

		if (o instanceof Contact) //同一实例
		{
			Contact other = (Contact) o;
			res = phoneNumber.equals(other.phoneNumber);
		}

		return res;
	}
}
