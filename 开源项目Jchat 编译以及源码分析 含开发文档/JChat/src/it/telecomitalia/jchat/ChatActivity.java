package it.telecomitalia.jchat;

import jade.android.ConnectionListener;
import jade.android.JadeGateway;
import jade.core.AID;
import jade.core.Profile;
import jade.core.behaviours.OneShotBehaviour;
import jade.imtp.leap.JICP.JICPProtocol;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;
import jade.util.leap.Properties;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Represents the activity that allows sending and receiving messages to other
 * contacts. It is launched when a user clicks a notification on the status bar
 * (for reading a message sent by a contact) or when starts a conversation
 * himself.
 * <p>
 * Please note that only a single activity is used also for managing multiple
 * conversation at a time, that is the user always sees a single activity also
 * when he switches from one to another: activity is simply redrawn.
 * <p>
 * Implements the ConnectionListener interface to be able to connect to the
 * MicroRuntime service for communication with agent.
 * 
 * 表示允许发送和接收消息到其他的活动，它推出当用户单击状态栏上的通知  联系人  *（读取一个联系人发送消息），或当启动一个对话自己。
 * 请注意，在一次谈话只有一个单一的活动也被用于管理多个 ，当他切换从一个到另一个活动是简单地重绘,那就是用户总是能看到一个单一的活动也  。
 * 实现ConnectionListener接口能够连接到  * MicroRuntime与代理通信服务。
 * 
 */

//聊天界面
public class ChatActivity extends Activity implements ConnectionListener
{
	/** Instance of Jade Logger, for debugging purpose.用于调试 */
	private final Logger myLogger = Logger.getMyLogger(this.getClass().getName());

	/** ListView showing participants to this chat session. ListView中显示此聊天会话的参与者 */
	private ListView partsList;

	/** Button for sending data. 用于发送数据的按钮 */
	private ImageButton sendButton;

	/** Button for closing this activity and session.按钮关闭这个活动和会议 */
	private ImageButton closeButton;

	/** List of already sent messages. 已发送的邮件名单 */
	private ListView messagesSentList;

	/** Edit text for editing the message that must be sent. 编辑文本编辑的邮件，必须发送 */
	private EditText messageToBeSent;

	/** Instance of jade gateway necessary to work with Jade add-on. 实例所必需的工作 网关 */
	private JadeGateway gateway;

	/** Id of the session this activity is related to 这项活动是与会话ID */
	private String sessionId;

	/** Adapter used to fill up the message list 适配器用于填补了邮件列表 */
	private MsnSessionAdapter sessionAdapter;

	private ChatActivityHandler activityHandler;

	/**
	 * Retrieves the id of the chat session this activity refers to.
	 * 检索聊天会话的ID，这个活动是指
	 * 
	 * @return Id of the session 会话ID
	 */
	public String getMsnSession()
	{
		return sessionId;
	}

	/**
	 * Initializes basic GUI components and listeners. Also performs connection
	 * 初始化基本的GUI组件和听众。还进行连接 to add-on's Jade Gateway. 添加网关
	 * 
	 * @param icicle
	 *            Bundle of data if we are resuming a frozen state (not
	 *            used)捆绑的数据，如果我们继续冻结状态（未使用）
	 */
	protected void onCreate(Bundle icicle)
	{
		Thread.currentThread().getId(); //获得当前进程id

		myLogger.log(Logger.FINE, "onReceiveIntent called: My currentThread has this ID: " + Thread.currentThread().getId());
		super.onCreate(icicle);

		//启用窗体的扩展特性
		requestWindowFeature(Window.FEATURE_LEFT_ICON);

		setContentView(R.layout.chat);
		
		//设置特征的图标
		setFeatureDrawable(Window.FEATURE_LEFT_ICON, getResources().getDrawable(R.drawable.chat));

		myLogger.log(Logger.FINE, "onCreate called ...");
		//会话适配器
		sessionAdapter = new MsnSessionAdapter(getWindow().getLayoutInflater(), getResources());

		//发送消息
		sendButton = (ImageButton) findViewById(R.id.sendBtn);
		sendButton.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View view)
			{
				String msgContent = messageToBeSent.getText().toString().trim();
				myLogger.log(Logger.FINE, "onClick(): send message:" + msgContent);
				
				if (msgContent.length() > 0)
				{
					//本次会议的所有参与者发送消息。 
					sendMessageToParticipants(msgContent);
				}
				messageToBeSent.setText("");
			}
		});

		// retrieve the list
		partsList = (ListView) findViewById(R.id.partsList);
		messageToBeSent = (EditText) findViewById(R.id.edit);
		messagesSentList = (ListView) findViewById(R.id.messagesListView);

		//关闭会话按钮
		closeButton = (ImageButton) findViewById(R.id.closeBtn);
		closeButton.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View view)
			{
				ChatSessionNotificationManager.getInstance().removeSessionNotification(sessionId);
				MsnSessionManager.getInstance().removeMsnSession(sessionId);
				finish();
			}
		});

		activityHandler = new ChatActivityHandler();

		// fill Jade connection properties 填补桥接属性
		Properties jadeProperties = getJadeProperties();

		// try to get a JadeGateway 获得网关
		try
		{
			//桥接
			JadeGateway.connect(MsnAgent.class.getName(), jadeProperties, this, this);
		} 
		catch (Exception e)
		{
			// troubles during connection 连接失败提示
			Toast.makeText(this, getString(R.string.error_msg_jadegw_connection), Integer.parseInt(getString(R.string.toast_duration))).show();
			
			myLogger.log(Logger.SEVERE, "Error in chatActivity", e);
			e.printStackTrace();
		}
	}

	private Properties getJadeProperties()
	{
		// fill Jade connection properties 填补桥接属性
		Properties jadeProperties = new Properties();
		JChatApplication app = (JChatApplication) getApplication();
		
		jadeProperties.setProperty(Profile.MAIN_HOST, app.getProperty(JChatApplication.JADE_DEFAULT_HOST));
		jadeProperties.setProperty(Profile.MAIN_PORT, app.getProperty(JChatApplication.JADE_DEFAULT_PORT));
		jadeProperties.setProperty(JICPProtocol.MSISDN_KEY, app.getProperty(JChatApplication.PREFERENCE_PHONE_NUMBER));
		return jadeProperties;
	}

	/**
	 * Populates the GUI retrieving the sessionId from the intent that initiates
	 * the activity itself. The session Id is saved in the intent as an URI,
	 * whose fragment is the part we are interested in.
	 * <p>
	 * Please note that this method shall be called both when the activity is
	 * created for the first time and when it is resumed from the background
	 * (that is, when it is in the foreground and the user switches to a new
	 * session by clicking the status bar notifications) 
	 * 填充的图形用户界面，检索会话ID发起的意图
	 * 活动本身。会话ID被保存在一个URI的意图， 我们感兴趣的部分是的片段 请注意，
	 * 它是从此方法应被称为活动是既当首次创建和恢复的背景时
	 * （也就是说，当它是在前台和用户切换到一个新的会议通过点击状态栏的通知）
	 */
	@Override
	protected void onResume()
	{
		//会话被恢复
		myLogger.log(Logger.FINE, "onResume() was called!");
		
		//连接链
		Intent i = getIntent();
		Uri sessionIdUri = i.getData();
		
		//会话id
		sessionId = sessionIdUri.getFragment();

		//会话消息
		MsnSession session = MsnSessionManager.getInstance().retrieveSession(sessionId);
		setTitle(session.toString());
		
		//得到会话所有的参与者
		List<String> participants = session.getAllParticipantNames();
		ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(this, R.layout.participant_layout, R.id.participantName, participants);
		partsList.setAdapter(aAdapter);
		ChatSessionNotificationManager.getInstance().addNewSessionNotification(sessionId);
		messageToBeSent.setText("");

		// Retrieve messages if the session already contains data如果会议已经包含数据检索消息
		sessionAdapter.setNewSession(session);
		messagesSentList.setAdapter(sessionAdapter);
		MsnEventMgr.getInstance().registerEvent(MsnEvent.INCOMING_MESSAGE_EVENT, activityHandler);

		super.onResume();
	}

	/**
	 * 状态栏 Called only when resuming an activity by clicking the status bar,
	 * just before <code> onResume() </code>
	 * <p>
	 * Sets the retrieved intent (containing info about the new session selected
	 * by the user) as the current one, to make <code> onResume() </code> able
	 * to populate the GUI with the new data.
	 * 
	 * @param intent
	 *            the intent launched when clicking on status bar notification
	 *            (no new activity is created but the new intent is passed
	 *            anyway)
	 */
	@Override
	protected void onNewIntent(Intent intent)
	{
		myLogger.log(Logger.FINE, "onNewIntent was called!! \n Intent received was: " + intent.toString());
		setIntent(intent);
		super.onNewIntent(intent);
	}

	/**
	 * Called only when destroying the chat activity when closing the
	 * chat只有当破坏聊天活动结束聊天时，被称为 window (both when clicking the close button or
	 * when going back in 窗口（当点击关闭按钮或回去 activity stack with the back
	 * arrow).活动堆栈后退箭头）
	 * <p>
	 * It basically performs a disconnection from the service, sends the
	 * closing它基本上执行从服务断开，发送闭幕 message to the main activity and resets the
	 * ChatActivityUpdater to null信息的主要活动和复位ChatActivityUpdater空 (so the agent
	 * is aware that the chat activity is not visible).（代理知道聊天活动是不可见的）
	 * 
	 * @param intent
	 *            the intent launched when clicking on status bar notification
	 *            (no new activity is created but the new intent is passed
	 *            anyway)
	 */
	@Override
	protected void onDestroy()
	{
		super.onDestroy();

		//需要关闭桥接
		if (gateway != null)
		{
			gateway.disconnect(this);
			myLogger.log(Logger.INFO, "ChatActivity.onDestroy() : disconnected from MicroRuntimeService");
		}

	}

	/**
	 * Gets the instance to the add-on's JadeGateway to be able to send messages
	 * to be sent to the Jade agent. It's a callback, called after the
	 * connection to add-on's <code>MicroRuntimeService</code>
	 * 得到实例使用网关去发送消息针对桥代理
	 * 
	 * @param gw
	 *            Instance of the JadeGateway retrieved after the connection
	 * @see ConnectionListener
	 */
	public void onConnected(JadeGateway gw)
	{
		this.gateway = gw;
		myLogger.log(Logger.INFO, "onConnected(): SUCCESS!");
	}

	/**
	 * Dummy implementation for the ConnectionListener's onDisconnected
	 * 虚拟实现连接侦听的失去连接
	 * 
	 * @see ConnectionListener
	 */
	public void onDisconnected()
	{
	}

	/**
	 * Sends a message to all participants of this session.本次会议的所有参与者发送消息。
	 * <p>
	 * Instantiates a new SenderBehaviour object and sends it to the agent,
	 * together with message contents and receiver list, then updates the
	 * message ListView.
	 * 
	 * 实例一个发送对象去发送接受列表以及消息代理，然后更新消息列表
	 * 
	 * @param msgContent
	 *            content of the message to be sent
	 */
	private void sendMessageToParticipants(String msgContent)
	{
		// set all participants as receivers设置为接收所有参与者
		//获得当前会话
		MsnSession session = MsnSessionManager.getInstance().retrieveSession(sessionId);
		
		//返回会话参与者
		List<String> receivers = session.getAllParticipantIds();

		try
		{
			//网关执行 发送行为
			gateway.execute(new SenderBehaviour(session.getSessionId(), msgContent, receivers));
			
			//返回接触者
			Contact myContact = ContactManager.getInstance().getMyContact();
			
			//会话消息
			MsnSessionMessage message = new MsnSessionMessage(msgContent, myContact.getName(), myContact.getPhoneNumber());
			
			//添加消息到会话
			MsnSessionManager.getInstance().addMessageToSession(session.getSessionId(), message);
			
			// Add a new view to the adapter
			//添加消息到视图
			sessionAdapter.addMessageView(message);
			// refresh the list 刷新列表
			messagesSentList.setAdapter(sessionAdapter);
		}
		catch (Exception e)
		{
			myLogger.log(Logger.WARNING, e.getMessage());
		}
	}

	/**
	 * Contains the actual code executed by the agent to send the
	 * message.包含实际的代码执行代理发送消息。
	 */
	private class SenderBehaviour extends OneShotBehaviour
	{
		//发送者行为
		/** ACLMessage to be sent */
		private ACLMessage theMsg;

		/**
		 * Instantiates a new sender behaviour. Fills up the ACLMessage with
		 * data provided.
		 * 实例化一个新的发送者的行为。提供的数据，填补了ACLMessage。
		 * @param convId
		 *            the conv id
		 * @param content
		 *            the content
		 * @param receivers
		 *            the receivers
		 */
		public SenderBehaviour(String convId, String content, List<String> receivers)
		{
			theMsg = new ACLMessage(ACLMessage.INFORM);
			theMsg.setContent(content);
			theMsg.setOntology(MsnAgent.CHAT_ONTOLOGY);
			theMsg.setConversationId(convId);

			for (int i = 0; i < receivers.size(); i++)
			{
				String cId = receivers.get(i);
				theMsg.addReceiver(new AID(cId, AID.ISLOCALNAME));
			}
		}

		/**
		 * Sends the message. Executed by JADE agent. 发送一个消息，JADE代理执行
		 */
		public void action()
		{
			myLogger.log(Logger.FINE, "Sending msg " + theMsg.toString());
			myAgent.send(theMsg);
		}
	}

	/**
	 * Defines an handler for UI events. 定义一个UI事件
	 */
	private class ChatActivityHandler extends GuiEventHandler
	{

		/**
		 * Performs the update of the GUI. It handles the arrival of a new
		 * message.执行GUI的更新。它可以处理新邮件的到来。
		 * <p>
		 * Two cases are possible:
		 * <ol>
		 * <li>incoming message is related to the current session and should be
		 * added to message list
		 * <li>incoming message is related to another session and a notification
		 * is to be shown 传入的消息是有关本届会议，并应加入到邮件列表中传入的消息，涉及到另一个会议，是要显示通知
		 * </ol>
		 * 
		 * @param event
		 *            the event that shall be notified to this listener to be
		 *            handled
		 *            应当通知被处理的事件，这个监听器
		 */
		protected void processEvent(MsnEvent event)
		{
			//事件名
			String eventName = event.getName();

			// Handle case of new message
			if (eventName.equals(MsnEvent.INCOMING_MESSAGE_EVENT))
			{
				//获得消息
				MsnSessionMessage msnMsg = (MsnSessionMessage) event.getParam(MsnEvent.INCOMING_MESSAGE_PARAM_MSG);
				//获得会话id
				String sessionId = (String) event.getParam(MsnEvent.INCOMING_MESSAGE_PARAM_SESSIONID);

				// check if the message is related to the same session we are
				// currently in.检查涉及到同一个会话的消息，我们目前所处 如果是这样，添加一个新的消息会话更新
				// If so, add a new message to session udapter and update it
				
				if (sessionId.equals(ChatActivity.this.sessionId))
				{
					//原会话
					sessionAdapter.addMessageView(msnMsg);
					messagesSentList.setAdapter(sessionAdapter);
				} 
				else
				{
					//新会话
					// if the incoming msg is not for our session, post a
					// notification如果传入的味精是不是我们的会议，发布通知
					ChatSessionNotificationManager.getInstance().addNewMsgNotification(sessionId, msnMsg);
					Toast.makeText(ChatActivity.this, msnMsg.getSenderName() + " says: " + msnMsg.getMessageContent(), 3000).show();
				}
			}
		}

	}
}
