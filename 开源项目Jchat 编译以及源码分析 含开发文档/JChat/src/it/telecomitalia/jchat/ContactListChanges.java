package it.telecomitalia.jchat;

import jade.util.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains a list of new contacts added and a list of removed contacts
 * <p>
 * Used by the agent to store all modifications to the contacts lists that
 * should be done also to the GUI
 * 包含一个新的联系人列表中添加和删除联系人名单，由代理用于存储所有修改的联系人列表也应该做的GUI
 */

//接触者变化
public class ContactListChanges
{

	/**
	 * Instance of the Jade Logger for debugging 输出桥接信息
	 */
	private Logger myLogger = Logger.getMyLogger(ContactListChanges.class.getName());

	/**
	 * List of the contacts phone number that have been added since last update
	 * 自上次更新已经添加的联系人的电话号码列表
	 */
	public List<String> contactsAdded;

	/**
	 * List of the contacts phone number that have been removed since last
	 * update自上次更新已经删除的联系人的电话号码列表
	 */
	public List<String> contactsDeleted;

	/**
	 * Copy constructor.
	 * <p>
	 * Makes a deep copy of the two inner lists
	 * 使得两个内部列表的深拷贝
	 * @param changes
	 *            the {@link ContactListChanges} that should be copied
	 */
	public ContactListChanges(ContactListChanges changes)
	{
		contactsAdded = new ArrayList<String>(changes.contactsAdded);
		contactsDeleted = new ArrayList<String>(changes.contactsDeleted);
		myLogger.log(Logger.FINE, "Thread " + Thread.currentThread().getId() + ": Copy constructor of ContactListChanges called!");
	}

	/**
	 * Instantiates a new contact list changes.
	 * 实例化一个新的联系人列表中的变化。
	 */
	public ContactListChanges()
	{
		contactsAdded = new ArrayList<String>();
		contactsDeleted = new ArrayList<String>();
		myLogger.log(Logger.FINE, "Thread " + Thread.currentThread().getId() + ":Main constructor of ContactListChanges called!");
	}

	/**
	 * Clears both the lists of changes
	 * 清除名单的变化
	 */
	public void resetChanges()
	{
		contactsAdded.clear();
		contactsDeleted.clear();
		myLogger.log(Logger.FINE, "Thread " + Thread.currentThread().getId() + ":Reset changes of ContactListChanges was called!");
	}

	/**
	 * Overrides Object.toString() and provides a representation of a
	 * {@link ContactListChanges} by printing both the list of added and removed
	 * contacts
	 *覆盖Object.toString（），并提供打印的添加和删除的联系人列表{@链接ContactListChanges}表示
	 */
	public String toString()
	{
		return "Thread " + Thread.currentThread().getId() + ":ContactListChanges: ADDED => " + contactsAdded.toString() + "\n REMOVED => " + contactsDeleted.toString();
	}
}
