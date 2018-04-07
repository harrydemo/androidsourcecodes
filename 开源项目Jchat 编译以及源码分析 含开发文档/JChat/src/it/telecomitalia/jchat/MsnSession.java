package it.telecomitalia.jchat;

import jade.core.AID;
import jade.util.Logger;
import jade.util.leap.Iterator;

import java.util.ArrayList;
import java.util.List;

import android.net.Uri;

/**
 * Represents a IM conversation between a set of participants. It keeps an
 * unique ID, the list of the participants and the list of already sent
 * messages. Provides functionalities to add messages to the session and to
 * retrieve the session data and to retrieve a representation of this session's
 * ID as an URI, to be set as Intent data.
 * 代表了一组参与者之间的IM对话。它使一个唯一的ID，参与者和已发送的邮件列表清单。
 * 提供消息添加到会话和检索会话数据和本次会议的ID表示，作为一个URI，将作为意向数据检索功能。
 */

//消息会话
public class MsnSession
{

	/**
	 * Instance of the JADE logger for debugging
	 */
	private final Logger myLogger = Logger.getMyLogger(this.getClass().getName());

	/**
	 * List of ids (phone numbers) of all conversation participants
	 * 所有谈话参与者的ID名单（电话号码）
	 */
	private List<String> participantIdList;

	/**
	 * The message list.
	 */
	private ArrayList<MsnSessionMessage> messageList;

	/**
	 * Scheme that shall be used when representing session id as an URI
	 * 计划代表URI作为一个会话ID时，应使用
	 */
	private static final String SESSION_ID_URI_SCHEME = "content";

	/**
	 * SSP that shall be used when representing session id as an URI
	 * SSP的代表URI作为一个会话ID时，应使用
	 */
	private static final String SESSION_ID_URI_SSP = "sessionId";

	/**
	 * Prefix used for building notification title
	 * 前缀建设的通知标题
	 */
	private static final String NOTIFICATION_TITLE = "Conversation ";

	/**
	 * Title that will be shown in chat activity when showing this session's
	 * contents
	 * 标题显示本次会议的内容时，将显示在聊天活动
	 */
	private String sessionTitle;

	/**
	 * The session id.
	 */
	private String sessionId;

	/**
	 * Instantiates a new conversation session.
	 * 实例化一个新的对话会议。
	 * 
	 * @param sessionId
	 *            the session id
	 * @param recvIt
	 *            iterator on the participant list
	 * @param senderPhone
	 *            the sender phone
	 * @param sessionCounter
	 *            unique integer assigned to this session
	 */
	MsnSession(String sessionId, Iterator recvIt, String senderPhone, int sessionCounter)
	{
		this.sessionId = sessionId;
		this.participantIdList = new ArrayList<String>();
		this.messageList = new ArrayList<MsnSessionMessage>();
		fillParticipantList(recvIt, senderPhone);
		// prepare the session title
		StringBuffer buffer = new StringBuffer(NOTIFICATION_TITLE);
		buffer.append(sessionCounter);
		this.sessionTitle = buffer.toString();
	}

	/**
	 * Instantiates a new conversation session.
	 * 
	 * @param sessionId
	 *            the session id
	 * @param participantsIds
	 *            the list of participants' ids
	 *            与会者的IDS列表
	 * @param sessionCounter
	 *            unique integer assigned to this session
	 *            分配给本次会议的唯一的整数
	 */
	MsnSession(String sessionId, List<String> participantsIds, int sessionCounter)
	{
		this.sessionId = sessionId;
		this.participantIdList = participantsIds;
		this.messageList = new ArrayList<MsnSessionMessage>();
		// prepare the session title
		StringBuffer buffer = new StringBuffer(NOTIFICATION_TITLE);
		buffer.append(sessionCounter);
		this.sessionTitle = buffer.toString();
	}

	/**
	 * Performs a copy of a conversation session.
	 * 
	 * @param session
	 *            the session to copy
	 */
	MsnSession(MsnSession session)
	{
		this.sessionTitle = session.sessionTitle;
		this.sessionId = session.sessionId;
		this.messageList = new ArrayList<MsnSessionMessage>();

		for (MsnSessionMessage msg : session.messageList)
		{
			this.messageList.add(new MsnSessionMessage(msg));
		}

		this.participantIdList = new ArrayList<String>(session.participantIdList);
	}

	/**
	 * Fill the list of participants to this conversation (my contact is not
	 * included)
	 * 填写的参与者的至此的谈话的列表（不包括在内我的接触被）
	 * @param receiversIt
	 *            iterator over the list of receivers
	 *            迭代超过​​接收机列表
	 * @param senderPhoneNum
	 *            the phone number of the contact that initiates the session
	 *            发起会话的联系人的电话号码
	 */
	private void fillParticipantList(Iterator receiversIt, String senderPhoneNum)
	{
		while (receiversIt.hasNext())
		{
			AID contactAID = (AID) receiversIt.next();
			// In this application the agent local name is the contact phone
			// number
			String contactPhoneNum = contactAID.getLocalName();
			String myPhoneNum = ContactManager.getInstance().getMyContact().getPhoneNumber();

			// Check that this is not me
			if (!myPhoneNum.equals(contactPhoneNum))
			{
				// add as a new participant
				participantIdList.add(contactPhoneNum);
			}
		}

		participantIdList.add(senderPhoneNum);
	}

	/**
	 * Gets the session id as an URI in the form of
	 * "SESSION_ID_URI_SCHEME://SESSION_ID_URI_SSP#sessionId" Used for putting
	 * the sessionId inside Intent as data.
	 * 作为URI的形式获取会话ID“SESSION_ID_URI_SCHEME:
	 * // SESSION_ID_URI_SSP的＃SessionID的”用于原意，
	 * 把里面的数据的SessionID。
	 * @return the session id as an URI
	 */
	public Uri getSessionIdAsUri()
	{
		return Uri.fromParts(SESSION_ID_URI_SCHEME, SESSION_ID_URI_SSP, sessionId);
	}

	/**
	 * Overrides Object.equals(). Two sessions are considered equals if and only
	 * if their session ids are equal
	 * 覆盖的Object.Equals（）。两会被视为等于当且仅当他们的会话ID都是平等的
	 * @param o
	 *            object to compare to
	 * @return true if the sessions have same id, false otherwise
	 */
	public boolean equals(Object o)
	{
		// TODO Auto-generated method stub

		boolean retval = false;

		if (o instanceof MsnSession)
		{
			MsnSession otherSession = (MsnSession) o;
			retval = this.sessionId.equals(otherSession.sessionId);
		}

		return retval;
	}

	/**
	 * Adds the message to this session's message list
	 * 本次会议的消息列表添加消息
	 * @param msg
	 *            message to be added
	 */
	public void addMessage(MsnSessionMessage msg)
	{
		messageList.add(msg);
	}

	/**
	 * Gets the message list.
	 * 
	 * @return the message list
	 */
	public ArrayList<MsnSessionMessage> getMessageList()
	{
		ArrayList<MsnSessionMessage> list = null;
		list = new ArrayList<MsnSessionMessage>(messageList);
		return list;
	}

	/**
	 * Gets the the list of participants'ids
	 * 得到与会者的IDS的清单
	 * @return the participant ids'list
	 */
	public List<String> getAllParticipantIds()
	{
		return participantIdList;
	}

	/**
	 * Gets all participant names.
	 * 获取所有参与者的名字。
	 * @return the list of all participants' names
	 */
	public List<String> getAllParticipantNames()
	{
		ArrayList<String> participantNameList = new ArrayList<String>();
		for (String id : participantIdList)
		{
			participantNameList.add(ContactManager.getInstance().getContact(id).getName());
		}
		return participantNameList;
	}

	/**
	 * Gets the participant count.
	 * 获取参赛者计数。
	 * @return the participant count
	 */
	public int getParticipantCount()
	{
		return participantIdList.size() + 1;
	}

	/**
	 * Gets the session id.
	 * 
	 * @return the session id
	 */
	public String getSessionId()
	{
		return sessionId;
	}

	/**
	 * String representation (the session title)
	 * 字符串表示形式（会议的名称）
	 * @return the session title
	 */
	public String toString()
	{
		return sessionTitle;
	}
}