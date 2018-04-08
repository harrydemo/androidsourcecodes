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
 * �������е�������Ϊ����פ�����ֻ��ϣ����Ƕ���Ǽ�/���ĺ͸���ִ����Ϊ��
 * ������GatewayAgent����Android�ĸ���Ҫ������ܹ�ͨ��JadeGateway.execute�������ͽ��̵����
 * �ṩһ���ڲ������Ϊ���������Ϣ��
 */

//Msn ����
public class MsnAgent extends GatewayAgent
{

	/**
	 * Name of service description to be registered on DF (allowing us to filter
	 * out the modifications performed by our application from others)
	 * ��������Ҫ��DF��ʹ�����ܹ�ɸѡ�����ǵ�Ӧ�ó�������޸ı��˵�ע�����ƣ�
	 */
	public static final String msnDescName = "android-msn-service";

	/**
	 * Type of the description of the service��������������
	 */
	public static final String msnDescType = "android-msn";

	/**
	 * Name of the latitude property registered on the DFγ�ȵ����Ե����ƵǼ��ϵ�DF
	 */
	public static final String PROPERTY_NAME_LOCATION_LAT = "Latitude";

	/**
	 * Name of the longitude property registered on the DF�������Ե����ƵǼ��ϵ�DF
	 */
	public static final String PROPERTY_NAME_LOCATION_LONG = "Longitude";

	/**
	 * Name of the altitude property registered on the DF�߶����Ե����ƵǼ��ϵ�DF
	 */
	public static final String PROPERTY_NAME_LOCATION_ALT = "Altitude";

	/**
	 * Ontology used for sending message ������Ϣ���ڱ�����
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
	 * Message receiver behaviour instance��Ϣ����������Ϊʵ��
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
	 * ����Agent.setup����������ִ�ж���Ǽǣ�׼��DF������Ϣ����ӵ�{@ link ContactsUpdaterBehaviour}��
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

		// added behaviour to dispatch chat messages �����Ϊ����ǲ������Ϣ
		messageRecvB = new MessageReceiverBehaviour();
		addBehaviour(messageRecvB);
		String[] args = (String[]) getArguments();
		myLogger.log(Logger.INFO, "UPDATE TIME: " + args[0]);
		contactsUpdaterB = new ContactsUpdaterBehaviour(Long.parseLong(args[0]), curLoc);
		addBehaviour(contactsUpdaterB);
	}

	/**
	 * Gets the DF subscription message.
	 * ��ȡDF������Ϣ��
	 * @return the DF subscription message
	 */
	public ACLMessage getSubscriptionMessage()
	{
		return subscriptionMessage;
	}

	/**
	 * Gets the agent description.
	 * ��ȡ�����˵����
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
	 * ����GatewayAgent.processCommand������
	 * �յ���JadeGateway.execute������������Ƿ�����Ϣ����Ϊ�������ַ�ʽ�յ�
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
	 * �������ڽ���������Ϣ����Ϊ��
	 * ÿһ����Ϣ������ʱ����Ҫһ��UI�����������Ϣ����Ϣ���ڻ����֪ͨ����
	 * {@����CyclicBehaviour}����ִ�����Ĳ�������������һЩ���龡����Ϣ���
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
		 * ����Behaviour.action���������ͽ�����Ϣ��
		 * ���յ���Ϣ������Ĳ���������
		 * �����Ϣ���漰��һ���µĶԻ������ǵ�һ���յ�����Ϣ������һ���µĻỰ������Ự�м���
		 * ����Ϣ����ӵ��¼������͵�һ���µ���Ϣ����GUI���ʵ��ķ�ʽ̸��
		 */
		public void action()
		{
			try
			{
				MessageTemplate mt = MessageTemplate.MatchOntology(CHAT_ONTOLOGY);
				ACLMessage msg = myAgent.receive(mt);
			
				// If a message is received  ����յ���Ϣ
				if (msg != null)
				{
					myLogger.log(Logger.FINE, msg.toString());
					MsnSessionManager sessionManager = MsnSessionManager.getInstance();

					// retrieve the session id ������Ϣ�Ựid
					String sessionId = msg.getConversationId();
					myLogger.log(Logger.FINE, "Received Message... session ID is " + sessionId);
					String senderPhoneNum = msg.getSender().getLocalName();

					// Create a session Message from the received ACLMessage
					//���յ�����Ϣ�д����Ự��Ϣ
					MsnSessionMessage sessionMessage = new MsnSessionMessage(msg);

					// Check if we can retrieve a session. If so we should have
					// got a copy���������ǿ��Լ����Ự�����������������Ӧ����һ������
					MsnSession currentSession = MsnSessionManager.getInstance().retrieveSession(sessionId);

					// If we have a new session
					if (currentSession == null)
					{
						// Create a new session with the specified ID
						//��������ָ��ID��һ���µĻỰ
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
