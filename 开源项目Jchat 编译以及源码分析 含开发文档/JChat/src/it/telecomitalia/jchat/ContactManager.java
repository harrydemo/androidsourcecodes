package it.telecomitalia.jchat;

import jade.util.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.database.Cursor;
import android.location.Location;
import android.provider.Contacts.People;
import android.telephony.PhoneNumberUtils;

/**
 * Manages the list of contacts. This class is a singleton and manages a list of
 * all contacts and their location. It is responsible for adding and removing
 * contacts and also for location update in a thread safe way.
 * 管理联系人列表。
 * 这个类是单身和管理所有联系人的列表和它们的位置。
 * 它是负责添加和删除联系人和位置更新中的一个线程安全的方式。
 */

public class ContactManager
{

	/**
	 * Static instance of the contact manager.
	 * 联系人管理器的静态实例。
	 */
	private static ContactManager manager = new ContactManager();

	/**
	 * Map containing all the available contacts (except for the my contact), in
	 * particular all contacts stored on the phone (offline) and online contacts
	 * 地图包含所有提供接触（为我的联系人除外），特别是所有接触存储在手机上（离线）和在线联系人
	 */
	private final Map<String, Contact> contactsMap;

	/**
	 * Map containing all the available locations (online contacts only except
	 * the my contact).地图包含所有可用位置（只除了我接触在线联系人）。
	 */
	private final Map<String, ContactLocation> contactLocationMap;

	/**
	 * Instance of the my contact.
	 * 我接触的实例。
	 */
	private Contact myContact;

	/**
	 * 我接触的位置
	 */
	private ContactLocation myContactLocation;

	/**
	 * Instance of the Jade logger for debugging  输出桥接信息
	 */
	private final Logger myLogger = Logger.getMyLogger(this.getClass().getName());

	/**
	 * The adapter describing the contact list.
	 * 适配器描述的联系人列表。
	 */
	private ContactListAdapter contactsAdapter;

	/**
	 * Instance of {@link ContactListChanges} containing the list of newly added
	 * contacts and of recently removed contacts.
	 * {@的链接ContactListChanges}包含新添加的联系人和最近删除的联系人列表的实例。
	 */
	private ContactListChanges modifications;

	/**
	 * Id of the my Contact
	 * 对方的id
	 */
	private final String MY_CONTACT_NAME = "Me";

	/**
	 * Name of the location provider
	 * 位置提供的名称
	 */
	private String providerName;

	/**
	 * Checks if any contact is currently moving.
	 * 如有接触的检查是目前移动。
	 * @return true if any contact is moving, false otherwise
	 */
	public synchronized boolean movingContacts()
	{
		boolean moving = true;
		List<ContactLocation> locs = new ArrayList<ContactLocation>(contactLocationMap.values());
		for (ContactLocation contactLocation : locs)
		{
			moving = moving && contactLocation.hasMoved();
		}
		return moving;
	}

	/**
	 * Instantiates a new contact manager.
	 * 实例化一个新的联系人管理。
	 */
	private ContactManager()
	{
		contactsMap = new HashMap<String, Contact>();
		contactLocationMap = new HashMap<String, ContactLocation>();
		modifications = new ContactListChanges();
	}

	/**
	 * Reset tracking of modifications to contact list
	 * 重置跟踪修改连络清单
	 */
	public synchronized void resetModifications()
	{
		modifications.resetChanges();
	}

	/**
	 * Retrieves the list of modifications to contact map since last reset
	 * 检索修改的名单，联系地图最近复位
	 * @return {@link ContactListChanges}
	 */
	public synchronized ContactListChanges getModifications()
	{
		return new ContactListChanges(modifications);
	}

	/**
	 * Retrieves the adapter associated to the Contact list
	 * 检索适配器关联到联系人列表
	 * @return the adapter
	 */
	public ContactListAdapter getAdapter()
	{
		return contactsAdapter;
	}

	/**
	 * Read contacts stored on phone database and populates the contact map with
	 * all offline contacts.
	 * 阅读联系人存储在手机上的数据库，并填充所有脱机接触接触地图。
	 * @param act
	 *            reference to an activity
	 */
	public void readPhoneContacts(ContactListActivity act)
	{
		providerName = act.getText(R.string.location_provider_name).toString();

		// perform a query on contacts database returning all contacts data in
		// name ascending order
		//联系人数据库上执行的查询返回所有联系人数据名称升序
		Cursor c = act.getContentResolver().query(People.CONTENT_URI, null, null, null, People.NAME + " DESC");

		act.startManagingCursor(c);

		int nameCol = c.getColumnIndexOrThrow(People.NAME);
		int phonenumberCol = c.getColumnIndexOrThrow(People.NUMBER);

		// Let's get contacts data
		if (c.moveToFirst())
		{
			do
			{
				String phonenumber = PhoneNumberUtils.stripSeparators(c.getString(phonenumberCol));
				String name = c.getString(nameCol);

				myLogger.log(Logger.FINE, "Thread " + Thread.currentThread().getId() + ":Found contact " + name + " with numtel " + phonenumber);
				Contact cont = new Contact(name, phonenumber, true);
				contactsMap.put(phonenumber, cont);

			} while (c.moveToNext());
		}

		c.close();
	}

	/**
	 * Adds an adapter for the contact list.
	 * 添加一个联系人列表的适配器。
	 * @param cla
	 *            the {@link ContactListAdapter}
	 */
	public void addAdapter(ContactListAdapter cla)
	{
		contactsAdapter = cla;
	}

	/**
	 * Adds a new {@link Contact} with the given phoneNumber and Location or
	 * updates the Location of an existing one.
	 * 添加一个新的给定的电话号码和地点或更新现有的一个位置{@链接联系}。
	 * @param phoneNumber
	 *            of the new contact or of the contact that should be updated
	 *            新的接触或接触，应更新
	 * @param loc
	 *            current location of the new contact or new location if this is
	 *            an update当前位置新的接触或新的位置，如果这是一个更新
	 */
	public synchronized void addOrUpdateOnlineContact(String phoneNumber, Location loc)
	{
		// Is the contact already there?

		Contact cont = contactsMap.get(phoneNumber);

		// the new contact is available
		if (cont != null)
		{
			if (cont.isOnline())
			{
				ContactLocation oldloc = contactLocationMap.get(phoneNumber);
				oldloc.changeLocation(loc);
			} 
			else
			{
				ContactLocation location = new ContactLocation(providerName);
				location.changeLocation(loc);
				cont.setOnline();
				contactLocationMap.put(phoneNumber, location);
			}

		} else
		{
			cont = new Contact(phoneNumber, phoneNumber, false);
			cont.setOnline();
			ContactLocation location = new ContactLocation(providerName);
			location.changeLocation(loc);

			myLogger.log(Logger.INFO, "Thread " + Thread.currentThread().getId() + ":New contact " + cont.getName() + " was added with location " + location.toString());
			contactLocationMap.put(phoneNumber, location);
			contactsMap.put(phoneNumber, cont);
			modifications.contactsAdded.add(phoneNumber);
			myLogger.log(Logger.INFO, "Thread " + Thread.currentThread().getId() + ":Contact map is now: " + contactsMap.toString());
		}

	}

	/**
	 * Set the given contact status to offline (without removing it from the map
	 * if it is stored on the phone contacts database)
	 * 设置（如果它不从地图上删除存储在手机上的联系人数据库）的接触状态为脱机
	 * @param phoneNumber
	 *            number of the contact
	 */
	//异步
	public synchronized void setContactOffline(String phoneNumber)
	{

		// If a contact is local (It's in the phone contacts) it must be shown
		// as offline
		// If the contact is not local, remove it when it goes offline

		Contact c = contactsMap.get(phoneNumber);
		c.setOffline();
		contactLocationMap.remove(phoneNumber);
		myLogger.log(Logger.INFO, "Thread " + Thread.currentThread().getId() + ":Contact map is now: " + contactsMap.toString());

	}

	/**
	 * Retrieves a Contact given its phone number
	 * 检索给定的电话号码联络
	 * @param phoneNumber
	 *            the phone number of the contact that should be retrieved
	 * @return the contact having the given phoneNumber
	 */
	public Contact getContact(String phoneNumber)
	{
		return contactsMap.get(phoneNumber);
	}

	/**
	 * Gets the location of a contact given its phone number.
	 * 取得其电话号码的联系人的位置。
	 * @param phoneNumber
	 *            the phone number of the contact whose location should be
	 *            retrieved
	 * @return location of the required contact
	 */
	public ContactLocation getContactLocation(String phoneNumber)
	{
		return contactLocationMap.get(phoneNumber);
	}

	/**
	 * Retrieve instance of the main contact
	 * 检索实例的主触头
	 * @return the my contact
	 */
	public Contact getMyContact()
	{
		return myContact;
	}

	/**
	 * Gets the single instance of ContactManager.
	 * 获取联系人管理器的单个实例。
	 * @return single instance of ContactManager
	 */
	public static ContactManager getInstance()
	{
		return manager;
	}

	/**
	 * Cleans up both the contact map and the location map
	 * 清理接触地图和位置图
	 */
	public void shutdown()
	{
		contactsMap.clear();
		contactsAdapter.clear();
		modifications.resetChanges();
		contactLocationMap.clear();
	}

	/**
	 * Returns a map containing contact to location mapping. This map is a copy
	 * of the inner location map so this should be thread safe
	 * 返回地图，包含接触到的位置映射。
	 * 这张地图是副本内的位置图，所以这应该是线程安全的
	 * @return map with online contact locations
	 */
	public synchronized Map<String, ContactLocation> getAllContactLocations()
	{
		Map<String, ContactLocation> location = new HashMap<String, ContactLocation>(contactLocationMap.size());
		for (String s : contactLocationMap.keySet())
		{
			location.put(new String(s), new ContactLocation(contactLocationMap.get(s)));
		}
		return location;
	}

	/**
	 * Retrieves a map containing mappings between contacts and phone numbers
	 * 地图检索包含联系人和电话号码之间的映射
	 * @return copy of the inner contact map
	 */
	public synchronized Map<String, Contact> getAllContacts()
	{
		Map<String, Contact> cMap = new HashMap<String, Contact>(contactsMap.size());

		for (String s : contactsMap.keySet())
		{
			cMap.put(new String(s), new Contact(contactsMap.get(s)));
		}

		return cMap;
	}

	/**
	 * Adds the my contact, given its phone number.
	 * 增加了我的联系，其电话号码。
	 * @param phoneNumber
	 *            my contact's phone number
	 */
	public void addMyContact(String phoneNumber)
	{
		// TODO Auto-generated method stub
		myContact = new Contact(MY_CONTACT_NAME, phoneNumber, true);
		myContact.setOnline();
		myContactLocation = new ContactLocation(providerName);

	}

	/**
	 * Update location of my contact an check if it has moved
	 * 更新我接触的位置，如果它提出的检查
	 * @param loc
	 *            new location of my contact
	 * @return true if contact has moved, false otherwise
	 */
	public synchronized boolean updateMyContactLocation(Location loc)
	{
		myContactLocation.changeLocation(loc);
		myLogger.log(Logger.INFO, "After updating current location is  " + myContactLocation);
		return myContactLocation.hasMoved();
	}

	/**
	 * Gets location of my contact.
	 * 获取我接触的位置。
	 * @return the current my contact's location
	 */
	public synchronized ContactLocation getMyContactLocation()
	{
		return new ContactLocation(myContactLocation);
	}

}
