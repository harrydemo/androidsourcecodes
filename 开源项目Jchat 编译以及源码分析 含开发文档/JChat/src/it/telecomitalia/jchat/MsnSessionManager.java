package it.telecomitalia.jchat;

import jade.util.leap.Iterator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Provides conversation sessions management functionalities. In particular, it
 * allows the creation of a new conversation session and the removal of an
 * existing one, retrieving existing sessions, adding messages to a session and
 * so on.
 * 提供对话会议管理功能。特别是，它允许创建一个新的对话会议，并拆除现有检索现有的会话，添加邮件会话等。
 */

//消息会话管理
public class MsnSessionManager
{

	/**
	 * The instance of the session manager
	 */
	private static MsnSessionManager instance = new MsnSessionManager();

	/**
	 * The session map. Each session is stored using its ID as a map key.
	 */
	private Map<String, MsnSession> sessionMap;

	/**
	 * Max number of session
	 */
	public static final int MAX_MSN_SESSION_NUMBER = 10;

	private MsnSessionManager()
	{
		sessionMap = new HashMap<String, MsnSession>(MAX_MSN_SESSION_NUMBER);
	}

	/**
	 * Gets the instance of MsnSessionManager.
	 * 
	 * @return the instance of MsnSessionManager
	 */
	public static MsnSessionManager getInstance()
	{
		return instance;
	}

	/**
	 * Called when the user initiates a session with other contacts, creates a
	 * new session generating the session id from the participants id list. The
	 * session is then added to the session map and its session Id is returned.
	 * 用户启动会话与其他接触时调用，创建一个新的会话，从参与者ID列表生成的会话ID。
	 * 会话，然后添加到会话地图，并返回其会话ID。
	 * @param participantIds
	 *            the list of participants ids (phone numbers)
	 * @return String that represents the session Id for the newly created
	 *         session
	 */
	public String startMsnSession(List<String> participantIds)
	{
		String sessionIdAsString = getSessionIdFromParticipants(participantIds);
		addMsnSession(sessionIdAsString, participantIds);
		return sessionIdAsString;
	}

	/**
	 * Gets the session id from participants id list. Session id is computed by
	 * hashing each participant phone number and then by exoring them all
	 * together. This should create a fairly unique values that depends only
	 * from the participants ids and not by the order in which they appear.
	 * 获取从参与者ID列表中的会话ID。
	 * 通过散列每个参与者的电话号码，然后他们一起exoring会话ID计算。
	 * 这应该创建一个相当独特的价值，只能从参与者ID和不取决于它们出现的顺序。
	 * @param participantIds
	 *            the list of participant phone numbers
	 * 
	 * @return the session id as a string
	 */
	public String getSessionIdFromParticipants(List<String> participantIds)
	{
		String myAgentId = ContactManager.getInstance().getMyContact().getPhoneNumber();
		// The session id is computed by hashing agentNames
		int sessionIdAsInt = myAgentId.hashCode();

		for (String participantId : participantIds)
		{
			int tmp = participantId.hashCode();
			sessionIdAsInt ^= tmp;
		}

		String sessionIdAsStr = String.valueOf(sessionIdAsInt);
		return sessionIdAsStr;
	}

	/**
	 * Adds a new msn session initiated by another contact (the other contact
	 * start the communication)
	 * 添加另一个接触（其他联系方式启动通讯）发起的一个新的MSN会话
	 * 
	 * @param sessionId
	 *            the session id
	 * @param participantIds
	 *            the list of all participants'ids
	 */
	public synchronized void addMsnSession(String sessionId, List<String> participantIds)
	{
		if (!sessionMap.containsKey(sessionId))
		{
			int sessionCounter = sessionMap.size() + 1;
			MsnSession session = new MsnSession(sessionId, participantIds, sessionCounter);
			// register it
			sessionMap.put(sessionId, session);
		}
	}

	/**
	 * Adds a new conversation session initiated by another contact (the other
	 * contact start the communication)
	 * 添加另一个接触（其他联系方式启动通讯）发起的一个新的对话会议
	 * @param sessionId
	 *            the session id
	 * @param recvIt
	 *            the iterator over the receiver list
	 * @param senderPhone
	 *            the phone number of the contact that initiates the session
	 */
	//添加会话
	public synchronized void addMsnSession(String sessionId, Iterator recvIt, String senderPhone)
	{
		if (!sessionMap.containsKey(sessionId))
		{
			int sessionCounter = sessionMap.size() + 1;
			MsnSession session = new MsnSession(sessionId, recvIt, senderPhone, sessionCounter);
			// register it
			sessionMap.put(sessionId, session);
		}
	}

	/**
	 * Removes an active session given its ID.
	 * 
	 * @param msnSession
	 *            the session id
	 */
	//移除会话
	public synchronized void removeMsnSession(String msnSession)
	{
		sessionMap.remove(msnSession);
	}

	/**
	 * Retrieve a copy of a session, given its id.
	 * 
	 * @param sessionId
	 *            id of the session we would like to retrieve
	 * 
	 * @return the copy of the desired session
	 */
	//返回会话
	public MsnSession retrieveSession(String sessionId)
	{
		//返回会话
		MsnSession session = null;
		MsnSession copyOfSession = null;

		synchronized (this)
		{
			session = sessionMap.get(sessionId);
			if (session != null)
				copyOfSession = new MsnSession(session);
		}

		return copyOfSession;

	}

	/**
	 * Adds the message to a given session in a thread safe way.
	 * 将一个线程安全的方式在给定的会话的消息。
	 * @param sessionId
	 *            the session id of the session
	 * @param msg
	 *            the message that should be added
	 */
	//添加消息到会话
	public synchronized void addMessageToSession(String sessionId, MsnSessionMessage msg)
	{
		MsnSession session = sessionMap.get(sessionId);

		if (session != null)
		{
			session.addMessage(msg);
		}
	}

	/**
	 * Performs some cleanup  执行一些清理工作
	 */
	public synchronized void shutdown()
	{
		sessionMap.clear();
	}

	/**
	 * Retrieves the list of participants to all active session in a given
	 * moment ( useful for changing map icon to contacts that are in an active
	 * session)
	 * 在某一时刻的所有活动会话（用于改变地图的图标在活跃会话的联系人检索与会者名单）
	 * @return the list of ids of all contacts participating to a session.
	 * 返回所有参与会话的联系人的ID列表
	 */
	public synchronized Set<String> getAllParticipantIds()
	{
		// define a set to avoid duplicates
		Set<String> idSet = new HashSet<String>();
		ArrayList<MsnSession> c = null;

		synchronized (this)
		{
			c = new ArrayList<MsnSession>(sessionMap.values());
		}

		for (MsnSession session : c)
		{
			List<String> partIds = session.getAllParticipantIds();
			idSet.addAll(partIds);
		}

		return idSet;
	}

}
