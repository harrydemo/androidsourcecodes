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
 * Manager class for incoming notifications. �����֪ͨ�����ࡣ
 * <p>
 * Provides support for adding, removing notifications or updating existing
 * notifications in status bar  This class should be �ṩ֧����ӣ�ɾ��֪ͨ���������״̬����֪ͨ
 * similar to the Android <code>NotificationManager</code> but provides the
 * functionality of removing �����Ӧ�������Ƶ�Android��
 * <code> NotificationManager</����>�����ṩ��ɾ������
 * all notifications
 * 
 */

//����Ự֪ͨ����
class ChatSessionNotificationManager
{

	/**
	 * The instance of the ChatSessionNotificationManager �Ự֪ͨ�������ʵ��
	 */
	private static ChatSessionNotificationManager theInstance;

	/**
	 * Reference to an activity (needed to post notifications).���õ�һ���������֪ͨ�� It
	 * seems it's not really important which Activity to use ���ƺ�ʹ�õĻ����������ĺ���Ҫ
	 */
	private Activity activity;

	/**
	 * Max number of notifications that can be shown to the user
	 * �������û���ʾ�����������֪ͨ
	 */
	private final int MAX_NOTIFICATION_NUMBER = 10;

	/**
	 * Instance of the logger, used for debugging
	 */
	private static final Logger myLogger = Logger
			.getMyLogger(ChatSessionNotificationManager.class.getName());

	/**
	 * List of all notification displayed at a specific time.
	 * ��һ���ض���ʱ����ʾ����֪ͨ���嵥��
	 */
	private List<Integer> notificationList;

	/**
	 * Instance of the notification manager used to display/remove the
	 * notifications
	 * ֪ͨ�����ʵ����������ʾ/ɾ��֪ͨ
	 */
	private NotificationManager manager;

	/**
	 * Instantiates a new chat session notification manager.
	 * 
	 * @param act
	 *            Instance of the activity needed to instantiate the
	 *            notification manager
	 *            �ʵ����Ҫʵ����֪ͨ����
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
	 * thread. ������һ��״̬���ϵĻ���֪ͨ���������UI�̡߳�
	 * 
	 * @param sessionId
	 *            id of the session that must be notified
	 */
	//��ӻỰ֪ͨ
	public void addNewSessionNotification(String sessionId)
	{
		int index = Integer.parseInt(sessionId);
		Notification sessionNotif = makeSessionNotification(sessionId);
		addNotification(index, sessionNotif);
	}

	/**
	 * Adds a new message notification on the status bar. Must be called by UI
	 * thread.
	 * ״̬����������һ���µ���Ϣ֪ͨ���������UI�̡߳�
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
	 * ���һ��<code>֪ͨ</ code>�ĸ���id��״̬���ϵĶ��������ֵ�֪ͨ�б��е���Ŀ��
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
	 * ����һ������֪ͨ������ӵ�ʵ�÷���
	 * 
	 * @param sessionId
	 *            id of the session to be notified
	 * @return the Notification object
	 */
	private Notification makeSessionNotification(String sessionId)
	{
		//�����Ự֪ͨ
		MsnSession session = MsnSessionManager.getInstance().retrieveSession(
				sessionId);
		
		//�õ��μ��ߵĸ���
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
		
		//�����ж�����ʹ�õĻỰ֪ͨ
		return notif;
	}

	/**
	 * Utility method that creates a message notification object to be added.
	 * ���Ը��ӵ�ʵ�÷���������һ����Ϣ֪ͨ����
	 * @param sessionId
	 *            id of the session whose message will be notified
	 * @param msg
	 *            the message to be notified
	 * 
	 * @return the <code>Notification</code> object
	 */
	//������Ϣ֪ͨ
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
	 * <code>getInstance()</code> ��ʼ���Ự����ʵ��
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
	 * Returns the instance of the notification manager ����һ��֪ͨ����ʵ��
	 * 
	 * @return the {@link ChatSessionNotificationManager} instance
	 */
	public static ChatSessionNotificationManager getInstance()
	{
		return theInstance;
	}

	/**
	 * Removes the session notification. �Ƴ��Ự֪ͨ
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
