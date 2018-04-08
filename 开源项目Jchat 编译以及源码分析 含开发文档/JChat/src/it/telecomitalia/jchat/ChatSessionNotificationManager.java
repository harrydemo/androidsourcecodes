package it.telecomitalia.jchat;

import jade.util.Logger;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * Manager class for incoming notifications. 传入的通知管理类。
 * <p>
 * Provides support for adding, removing notifications or updating existing
 * notifications in status bar  This class should be 提供支持添加，删除通知或更新现有状态栏的通知
 * similar to the Android <code>NotificationManager</code> but provides the
 * functionality of removing 这个类应该是类似的Android的
 * <code> NotificationManager</代码>，但提供了删除功能
 * all notifications
 * 
 */

//聊天会话通知管理
class ChatSessionNotificationManager
{

	/**
	 * The instance of the ChatSessionNotificationManager 会话通知管理类的实例
	 */
	private static ChatSessionNotificationManager theInstance;

	/**
	 * Reference to an activity (needed to post notifications).引用到一个活动（张贴通知） It
	 * seems it's not really important which Activity to use 它似乎使用的活动，它不是真的很重要
	 */
	private Activity activity;

	/**
	 * Max number of notifications that can be shown to the user
	 * 可以向用户显示的最大数量的通知
	 */
	private final int MAX_NOTIFICATION_NUMBER = 10;

	/**
	 * Instance of the logger, used for debugging
	 */
	private static final Logger myLogger = Logger
			.getMyLogger(ChatSessionNotificationManager.class.getName());

	/**
	 * List of all notification displayed at a specific time.
	 * 在一个特定的时间显示所有通知的清单。
	 */
	private List<Integer> notificationList;

	/**
	 * Instance of the notification manager used to display/remove the
	 * notifications
	 * 通知经理的实例，用来显示/删除通知
	 */
	private NotificationManager manager;

	/**
	 * Instantiates a new chat session notification manager.
	 * 
	 * @param act
	 *            Instance of the activity needed to instantiate the
	 *            notification manager
	 *            活动实例需要实例的通知经理
	 */
	public ChatSessionNotificationManager(Activity act)
	{
		activity = act;
		notificationList = new ArrayList<Integer>(MAX_NOTIFICATION_NUMBER);
		manager = (NotificationManager) activity
				.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	/**
	 * Adds a session notification on the status bar. Must be called by UI
	 * thread. 增加了一个状态栏上的会议通知。必须调用UI线程。
	 * 
	 * @param sessionId
	 *            id of the session that must be notified
	 */
	//添加会话通知
	public void addNewSessionNotification(String sessionId)
	{
		int index = Integer.parseInt(sessionId);
		Notification sessionNotif = makeSessionNotification(sessionId);
		addNotification(index, sessionNotif);
	}

	/**
	 * Adds a new message notification on the status bar. Must be called by UI
	 * thread.
	 * 状态栏上增加了一个新的消息通知。必须调用UI线程。
	 * @param sessionId
	 *            Id of the session related to this notification
	 * @param msg
	 *            session message that will be notified
	 */
	public void addNewMsgNotification(String sessionId, MsnSessionMessage msg)
	{
		int index = Integer.parseInt(sessionId);
		Notification sessionNotif = makeMsgNotification(sessionId, msg);
		addNotification(index, sessionNotif);
	}

	/**
	 * Adds a <code>Notification</code> object on the status bar with the given
	 * id. It keeps track of the notification in a list.
	 * 添加一个<code>通知</ code>的给定id的状态栏上的对象。它保持的通知列表中的曲目。
	 * @param index
	 *            index for this notification ()
	 * @param notif
	 *            the notification to be added
	 * @see Notification
	 */
	private void addNotification(int index, Notification notif)
	{
		Integer indexAsInteger = Integer.valueOf(index);

		if (!notificationList.contains(indexAsInteger))
		{
			notificationList.add(indexAsInteger);
		}

		manager.notify(index, notif);
	}

	/**
	 * Utility method that creates a session notification object to be added
	 * 创建一个会议通知对象被添加的实用方法
	 * 
	 * @param sessionId
	 *            id of the session to be notified
	 * @return the Notification object
	 */
	private Notification makeSessionNotification(String sessionId)
	{
		//制作会话通知
		MsnSession session = MsnSessionManager.getInstance().retrieveSession(
				sessionId);
		
		//得到参加者的个数
		int numberOfParticipants = session.getParticipantCount();
		String title = session.toString();

		Intent viewChatIntent = new Intent(Intent.ACTION_MAIN);
		viewChatIntent.setClass(activity, ChatActivity.class);
		viewChatIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		viewChatIntent.setData(session.getSessionIdAsUri());

		PendingIntent pi = PendingIntent.getActivity(activity, 0,
				viewChatIntent, PendingIntent.FLAG_CANCEL_CURRENT);

		Notification notif = new Notification(R.drawable.incoming, "",
				System.currentTimeMillis());
		notif.setLatestEventInfo(activity, title, numberOfParticipants
				+ " participants", pi);
		
		//返回有多少人使用的会话通知
		return notif;
	}

	/**
	 * Utility method that creates a message notification object to be added.
	 * 无以复加的实用方法，创建一个消息通知对象。
	 * @param sessionId
	 *            id of the session whose message will be notified
	 * @param msg
	 *            the message to be notified
	 * 
	 * @return the <code>Notification</code> object
	 */
	//制作消息通知
	private Notification makeMsgNotification(String sessionId,
			MsnSessionMessage msg)
	{
		MsnSession session = MsnSessionManager.getInstance().retrieveSession(
				sessionId);
		String title = session.toString();

		Intent viewChatIntent = new Intent(Intent.ACTION_MAIN);
		viewChatIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		viewChatIntent.setClass(activity, ChatActivity.class);
		viewChatIntent.setData(session.getSessionIdAsUri());

		Notification notif = new Notification(R.drawable.chat,
				"A Message is arrived", System.currentTimeMillis());

		PendingIntent pi = PendingIntent.getActivity(activity, 0,
				viewChatIntent, PendingIntent.FLAG_CANCEL_CURRENT);

		notif.setLatestEventInfo(activity, title,
				"Msg from " + msg.getSenderName(), pi);

		return notif;
	}

	/**
	 * Initialize the session manager instance. Please note that the activity is
	 * only needed once: after creating the manager, it can be accessed by
	 * <code>getInstance()</code> 初始化会话管理实例
	 * 
	 * @param act
	 *            the main activity used for sending notification
	 */
	public static void create(Activity act)
	{
		if (theInstance == null)
		{
			theInstance = new ChatSessionNotificationManager(act);
		}
	}

	/**
	 * Returns the instance of the notification manager 返回一个通知管理实例
	 * 
	 * @return the {@link ChatSessionNotificationManager} instance
	 */
	public static ChatSessionNotificationManager getInstance()
	{
		return theInstance;
	}

	/**
	 * Removes the session notification. 移除会话通知
	 * 
	 * @param sessionId
	 *            id of the notification to be removed
	 */
	public void removeSessionNotification(String sessionId)
	{
		manager.cancel(Integer.parseInt(sessionId));
	}

	/**
	 * Removes all notifications.
	 * 
	 */
	public void removeAllNotifications()
	{

		for (int i = 0; i < notificationList.size(); i++)
		{
			Integer index = notificationList.get(i);
			manager.cancel(index.intValue());
			myLogger.log(Logger.FINE,
					"Removing notification with ID " + index.intValue());
		}
		notificationList.clear();
	}

}
