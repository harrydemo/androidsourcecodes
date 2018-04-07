package it.telecomitalia.jchat;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.Logger;
import jade.wrapper.gateway.GatewayAgent;

/**
 * Agent running all behaviours. It resides on the phone and it is responsible
 * for DF registration/subscription and for behaviour execution.
 * <p>
 * It extends GatewayAgent as requested by JADE Android add-on and is therefore
 * able to process commands sent through JadeGateway.execute(). Provides as an
 * inner class the behaviour responsible for receiving messages.
 * 代理运行的所有行为。它驻留在手机上，它是东风登记/订阅和负责执行行为。
 * 它延伸GatewayAgent白玉Android的附加要求，因此能够通过JadeGateway.execute（）发送进程的命令。
 * 提供一个内部类的行为负责接收消息。
 */

//Msn 代理
public class MsnAgent extends GatewayAgent
{

	/**
	 * Name of service description to be registered on DF (allowing us to filter
	 * out the modifications performed by our application from others)
	 * 服务描述要在DF（使我们能够筛选出我们的应用程序进行修改别人的注册名称）
	 */
	public static final String msnDescName = "android-msn-service";

	/**
	 * Type of the description of the service服务描述的类型
	 */
	public static final String msnDescType = "android-msn";

	/**
	 * Name of the latitude property registered on the DF纬度的属性的名称登记上的DF
	 */
	public static final String PROPERTY_NAME_LOCATION_LAT = "Latitude";

	/**
	 * Name of the longitude property registered on the DF经度属性的名称登记上的DF
	 */
	public static final String PROPERTY_NAME_LOCATION_LONG = "Longitude";

	/**
	 * Name of the altitude property registered on the DF高度属性的名称登记上的DF
	 */
	public static final String PROPERTY_NAME_LOCATION_ALT = "Altitude";

	/**
	 * Ontology used for sending message 发送消息用于本体论
	 */
	public static final String CHAT_ONTOLOGY = "chat_ontology";

	/**
	 * Description of Msn service
	 */
	private DFAgentDescription myDescription;

	/**
	 * DF Subscription message
	 */
	private ACLMessage subscriptionMessage;

	/**
	 * Instance of {@link ContactsUpdaterBehaviour}
	 */
	private ContactsUpdaterBehaviour contactsUpdaterB;

	/**
	 * Message receiver behaviour instance消息接收器的行为实例
	 */
	private MessageReceiverBehaviour messageRecvB;

	/**
	 * Instance of JADE Logger for debugging
	 */
	private final Logger myLogger = Logger.getMyLogger(this.getClass().getName());

	/**
	 * Overrides the Agent.setup() method, performing registration on DF,
	 * prepares the DF subscription message, and adds the
	 * {@link ContactsUpdaterBehaviour}.
	 * 覆盖Agent.setup（）方法，执行东风登记，准备DF订阅消息，添加的{@ link ContactsUpdaterBehaviour}。
	 */
	protected void setup()
	{
		super.setup();
		Thread.currentThread().getId();
		myLogger.log(Logger.INFO, "setup() called: My currentThread has this ID: " + Thread.currentThread().getId());
		myDescription = new DFAgentDescription();
		// fill a msn service description
		ServiceDescription msnServiceDescription = new ServiceDescription();
		msnServiceDescription.setName(msnDescName);
		msnServiceDescription.setType(msnDescType);
		myDescription.addServices(msnServiceDescription);

		// subscribe to DF
		subscriptionMessage = DFService.createSubscriptionMessage(this, this.getDefaultDF(), myDescription, null);

		ContactLocation curLoc = ContactManager.getInstance().getMyContactLocation();

		Property p = new Property(PROPERTY_NAME_LOCATION_LAT, new Double(curLoc.getLatitude()));
		msnServiceDescription.addProperties(p);
		p = new Property(PROPERTY_NAME_LOCATION_LONG, new Double(curLoc.getLongitude()));
		msnServiceDescription.addProperties(p);
		p = new Property(PROPERTY_NAME_LOCATION_ALT, new Double(curLoc.getAltitude()));
		msnServiceDescription.addProperties(p);
		myDescription.setName(this.getAID());

		try
		{
			myLogger.log(Logger.INFO, "Registering to DF!");
			DFService.register(this, myDescription);
		} 
		catch (FIPAException e)
		{
			e.printStackTrace();
		}

		// added behaviour to dispatch chat messages 添加行为，派遣聊天消息
		messageRecvB = new MessageReceiverBehaviour();
		addBehaviour(messageRecvB);
		String[] args = (String[]) getArguments();
		myLogger.log(Logger.INFO, "UPDATE TIME: " + args[0]);
		contactsUpdaterB = new ContactsUpdaterBehaviour(Long.parseLong(args[0]), curLoc);
		addBehaviour(contactsUpdaterB);
	}

	/**
	 * Gets the DF subscription message.
	 * 获取DF订阅消息。
	 * @return the DF subscription message
	 */
	public ACLMessage getSubscriptionMessage()
	{
		return subscriptionMessage;
	}

	/**
	 * Gets the agent description.
	 * 获取代理的说明。
	 * @return the agent description
	 */
	public DFAgentDescription getAgentDescription()
	{
		return myDescription;
	}

	/**
	 * Overrides agent takeDown() method
	 */
	protected void takeDown()
	{
		myLogger.log(Logger.INFO, "Doing agent takeDown() ");
	}

	/**
	 * Overrides GatewayAgent.processCommand(). Receives a command from
	 * JadeGateway.execute() The behaviour for sending a message in particular
	 * is received in this way
	 * 覆盖GatewayAgent.processCommand（）。
	 * 收到从JadeGateway.execute（）命令，尤其是发送消息的行为，以这种方式收到
	 * @param command
	 *            a generic command that this agent shall execute.
	 */
	protected void processCommand(final Object command)
	{
		if (command instanceof Behaviour)
		{
			addBehaviour((Behaviour) command);

		}
		releaseCommand(command);
	}

	/**
	 * Defines the behaviour for receiving chat messages. Each time a message is
	 * received, a UI feedback is required (adding message to message window or
	 * adding a notification). The {@link CyclicBehaviour} continuously executes
	 * its action method and does something as soon as a message arrives.
	 * 定义用于接收聊天消息的行为。
	 * 每一个消息被接收时，需要一个UI反馈（添加消息到消息窗口或添加通知）。
	 * {@链接CyclicBehaviour}连续执行它的操作方法，并做一些事情尽快消息到达。
	 */
	private class MessageReceiverBehaviour extends CyclicBehaviour
	{

		/**
		 * Overrides the Behaviour.action() method and receives messages. After
		 * a message is received the following operations take place:
		 * <ul>
		 * <li>If the message is related to a new conversation (it is the first
		 * message received) a new session is created, otherwise the session is
		 * retrieved
		 * <li>The message is added to the conversation
		 * <li>an event for a new message is sent to update the GUI in th
		 * appropriate way
		 * </ul>
		 * 覆盖Behaviour.action（）方法和接收消息。
		 * 在收到消息后，下面的操作发生：
		 * 如果消息是涉及到一个新的对话（这是第一次收到的消息）创建一个新的会话，否则会话中检索
		 * 该消息被添加到事件被发送到一个新的消息更新GUI日适当的方式谈话
		 */
		public void action()
		{
			try
			{
				MessageTemplate mt = MessageTemplate.MatchOntology(CHAT_ONTOLOGY);
				ACLMessage msg = myAgent.receive(mt);
			
				// If a message is received  如果收到消息
				if (msg != null)
				{
					myLogger.log(Logger.FINE, msg.toString());
					MsnSessionManager sessionManager = MsnSessionManager.getInstance();

					// retrieve the session id 返回消息会话id
					String sessionId = msg.getConversationId();
					myLogger.log(Logger.FINE, "Received Message... session ID is " + sessionId);
					String senderPhoneNum = msg.getSender().getLocalName();

					// Create a session Message from the received ACLMessage
					//从收到的消息中创建会话消息
					MsnSessionMessage sessionMessage = new MsnSessionMessage(msg);

					// Check if we can retrieve a session. If so we should have
					// got a copy检查如果我们可以检索会话。如果是这样，我们应该有一个副本
					MsnSession currentSession = MsnSessionManager.getInstance().retrieveSession(sessionId);

					// If we have a new session
					if (currentSession == null)
					{
						// Create a new session with the specified ID
						//创建具有指定ID的一个新的会话
						sessionManager.addMsnSession(sessionId, msg.getAllReceiver(), senderPhoneNum);
					}

					// prepare an "IncomingMessage"
					MsnEvent event = MsnEventMgr.getInstance().createEvent(MsnEvent.INCOMING_MESSAGE_EVENT);
					event.addParam(MsnEvent.INCOMING_MESSAGE_PARAM_SESSIONID, sessionId);
					event.addParam(MsnEvent.INCOMING_MESSAGE_PARAM_MSG, sessionMessage);
					// Add message to session
					sessionManager.addMessageToSession(sessionId, sessionMessage);
					MsnEventMgr.getInstance().fireEvent(event);
				} else
				{
					block();
				}

			} catch (Throwable t)
			{
				myLogger.log(Logger.SEVERE, "***  Uncaught Exception for agent " + myAgent.getLocalName() + "  ***", t);
			}

		}
	}
}
