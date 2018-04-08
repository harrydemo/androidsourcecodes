package chat.client.agent;

import java.util.List;
import java.util.logging.Level;

import jade.content.ContentManager;
import jade.content.Predicate;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.Logger;
import jade.util.leap.Iterator;
import jade.util.leap.Set;
import jade.util.leap.SortedSetImpl;
import chat.ontology.ChatOntology;
import chat.ontology.Joined;
import chat.ontology.Left;
import android.content.Intent;
import android.content.Context;

/**
 * This agent implements the logic of the chat client running on the user
 * terminal. User interactions are handled by the ChatGui in a
 * terminal-dependent way. The ChatClientAgent performs 3 types of behaviours: -
 * ParticipantsManager. A CyclicBehaviour that keeps the list of participants up
 * to date on the basis of the information received from the ChatManagerAgent.
 * This behaviour is also in charge of subscribing as a participant to the
 * ChatManagerAgent. - ChatListener. A CyclicBehaviour that handles messages
 * from other chat participants. - ChatSpeaker. A OneShotBehaviour that sends a
 * message conveying a sentence written by the user to other chat participants.
 * 该代理实现的聊天客户端，在用户终端上运行的逻辑。
 * 用户交互由ChatGui处理终端依赖的方式。
 * ChatClientAgent执行3种类型的行为： - ParticipantsManager人。
 * 使与会者名单最新信息的基础上获得一个CyclicBehaviour从ChatManagerAgent。
 * 这种行为也作为一个参与者​​的ChatManagerAgent订阅费。 
 * - ChatListener。一个CyclicBehaviour处理来自其他聊天参与者的消息。
 *  - ChatSpeaker。一个OneShotBehaviour发送消息输送到其他聊天参与者的用户写一个句子。
 * @author Giovanni Caire - TILAB
 */
public class ChatClientAgent extends Agent implements ChatClientInterface
{
	private static final long serialVersionUID = 1594371294421614291L;

	private Logger logger = Logger.getJADELogger(this.getClass().getName());

	private static final String CHAT_ID = "__chat__";
	private static final String CHAT_MANAGER_NAME = "manager";

	private Set participants = new SortedSetImpl();
	private Codec codec = new SLCodec();
	private Ontology onto = ChatOntology.getInstance();
	private ACLMessage spokenMsg;

	private Context context;

	protected void setup()
	{
		Object[] args = getArguments();
		if (args != null && args.length > 0)
		{
			if (args[0] instanceof Context)
			{
				context = (Context) args[0];
			}
		}

		// Register language and ontology
		ContentManager cm = getContentManager();
		cm.registerLanguage(codec);
		cm.registerOntology(onto);
		cm.setValidationMode(false);

		// Add initial behaviours
		addBehaviour(new ParticipantsManager(this));
		addBehaviour(new ChatListener(this));

		// Initialize the message used to convey spoken sentences
		spokenMsg = new ACLMessage(ACLMessage.INFORM);
		spokenMsg.setConversationId(CHAT_ID);

		// Activate the GUI
		registerO2AInterface(ChatClientInterface.class, this);

		Intent broadcast = new Intent();
		broadcast.setAction("jade.demo.chat.SHOW_CHAT");
		logger.log(Level.INFO, "Sending broadcast " + broadcast.getAction());
		context.sendBroadcast(broadcast);
	}

	protected void takeDown()
	{
	}

	private void notifyParticipantsChanged()
	{
		Intent broadcast = new Intent();
		broadcast.setAction("jade.demo.chat.REFRESH_PARTICIPANTS");
		logger.log(Level.INFO, "Sending broadcast " + broadcast.getAction());
		context.sendBroadcast(broadcast);
	}

	private void notifySpoken(String speaker, String sentence)
	{
		Intent broadcast = new Intent();
		broadcast.setAction("jade.demo.chat.REFRESH_CHAT");
		broadcast.putExtra("sentence", speaker + ": " + sentence + "\n");
		logger.log(Level.INFO, "Sending broadcast " + broadcast.getAction());
		context.sendBroadcast(broadcast);
	}

	/**
	 * Inner class ParticipantsManager. This behaviour registers as a chat
	 * participant and keeps the list of participants up to date by managing the
	 * information received from the ChatManager agent.
	 * 内部类ParticipantsManager。
	 * 这种行为，注册为一个聊天的参与者，使与会者名单管理从ChatManager代理接收到的信息。
	 */
	class ParticipantsManager extends CyclicBehaviour
	{
		private static final long serialVersionUID = -4845730529175649756L;
		private MessageTemplate template;

		ParticipantsManager(Agent a)
		{
			super(a);
		}

		public void onStart()
		{
			// Subscribe as a chat participant to the ChatManager agent
			ACLMessage subscription = new ACLMessage(ACLMessage.SUBSCRIBE);
			subscription.setLanguage(codec.getName());
			subscription.setOntology(onto.getName());
			String convId = "C-" + myAgent.getLocalName();
			subscription.setConversationId(convId);
			subscription
					.addReceiver(new AID(CHAT_MANAGER_NAME, AID.ISLOCALNAME));
			myAgent.send(subscription);
			// Initialize the template used to receive notifications
			// from the ChatManagerAgent
			template = MessageTemplate.MatchConversationId(convId);
		}

		public void action()
		{
			// Receives information about people joining and leaving
			// the chat from the ChatManager agent
			ACLMessage msg = myAgent.receive(template);
			if (msg != null)
			{
				if (msg.getPerformative() == ACLMessage.INFORM)
				{
					try
					{
						Predicate p = (Predicate) myAgent.getContentManager()
								.extractContent(msg);
						if (p instanceof Joined)
						{
							Joined joined = (Joined) p;
							List<AID> aid = (List<AID>) joined.getWho();
							for (AID a : aid)
								participants.add(a);
							notifyParticipantsChanged();
						}
						if (p instanceof Left)
						{
							Left left = (Left) p;
							List<AID> aid = (List<AID>) left.getWho();
							for (AID a : aid)
								participants.remove(a);
							notifyParticipantsChanged();
						}
					} catch (Exception e)
					{
						Logger.println(e.toString());
						e.printStackTrace();
					}
				} else
				{
					handleUnexpected(msg);
				}
			} else
			{
				block();
			}
		}
	} // END of inner class ParticipantsManager

	/**
	 * Inner class ChatListener. This behaviour registers as a chat participant
	 * and keeps the list of participants up to date by managing the information
	 * received from the ChatManager agent.
	 * 内部类ChatListener。
	 * 这种行为，注册为一个聊天的参与者，使与会者名单管理从ChatManager代理接收到的信息。
	 */
	class ChatListener extends CyclicBehaviour
	{
		private static final long serialVersionUID = 741233963737842521L;
		private MessageTemplate template = MessageTemplate
				.MatchConversationId(CHAT_ID);

		ChatListener(Agent a)
		{
			super(a);
		}

		public void action()
		{
			ACLMessage msg = myAgent.receive(template);
			if (msg != null)
			{
				if (msg.getPerformative() == ACLMessage.INFORM)
				{
					notifySpoken(msg.getSender().getLocalName(),
							msg.getContent());
				} else
				{
					handleUnexpected(msg);
				}
			} else
			{
				block();
			}
		}
	} // END of inner class ChatListener

	/**
	 * Inner class ChatSpeaker. INFORMs other participants about a spoken
	 * sentence
	 * 内部类ChatSpeaker。通知口语句子的其他参与者
	 */
	private class ChatSpeaker extends OneShotBehaviour
	{
		private static final long serialVersionUID = -1426033904935339194L;
		private String sentence;

		private ChatSpeaker(Agent a, String s)
		{
			super(a);
			sentence = s;
		}

		public void action()
		{
			spokenMsg.clearAllReceiver();
			Iterator it = participants.iterator();
			while (it.hasNext())
			{
				spokenMsg.addReceiver((AID) it.next());
			}
			spokenMsg.setContent(sentence);
			notifySpoken(myAgent.getLocalName(), sentence);
			send(spokenMsg);
		}
	} // END of inner class ChatSpeaker

	// ///////////////////////////////////////
	// Methods called by the interface
	// ///////////////////////////////////////
	public void handleSpoken(String s)
	{
		// Add a ChatSpeaker behaviour that INFORMs all participants about
		// the spoken sentence
		addBehaviour(new ChatSpeaker(this, s));
	}

	public String[] getParticipantNames()
	{
		String[] pp = new String[participants.size()];
		Iterator it = participants.iterator();
		int i = 0;
		while (it.hasNext())
		{
			AID id = (AID) it.next();
			pp[i++] = id.getLocalName();
		}
		return pp;
	}

	// ///////////////////////////////////////
	// Private utility method
	// ///////////////////////////////////////
	private void handleUnexpected(ACLMessage msg)
	{
		if (logger.isLoggable(Logger.WARNING))
		{
			logger.log(Logger.WARNING, "Unexpected message received from "
					+ msg.getSender().getName());
			logger.log(Logger.WARNING, "Content is: " + msg.getContent());
		}
	}

}
