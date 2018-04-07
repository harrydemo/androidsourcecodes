package it.telecomitalia.jchat;

import jade.util.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * This is an event. An event is basically a container object that carries
 * arbitrary data and has an unique name. It is filled by the entity that fires
 * the event.
 * 这是一个事件。事件基本上是一个容器对象，进行任意数据，并有一个独特的名称。它填补了触发事件的实体。
 */

//消息事件自定义结构类
public class MsnEvent
{

	private Logger myLogger = Logger.getMyLogger(MsnEvent.class.getName());

	/**
	 * Event that is fired each time an update should be performed
	 * 事件被激发每个更新，应执行的时间
	 */
	public static final String TICK_EVENT = "TICK_EVENT";
	/**
	 * Event that is fired when a new message arrives
	 * 事件的新邮件到达时，将触发
	 */
	public static final String INCOMING_MESSAGE_EVENT = "INCOMING_MESSAGE_EVENT";
	/**
	 * Event that is fired when a refresh of the view is needed ()
	 * 事件被解雇时需要刷新视图（）
	 */
	public static final String VIEW_REFRESH_EVENT = "VIEW_REFRESH_EVENT";
	/**
	 * Event that is fired when a contact disconnects
	 * 事件被触发时，触点断开
	 */
	public static final String CONTACT_DISCONNECT_EVENT = "CONTACT_DISCONNECT_EVENT";

	// Parameters defined for INCOMING MSG EVENT
	/**
	 * Name for the incoming message parameter
	 * 传入的消息参数名称
	 */
	public static final String INCOMING_MESSAGE_PARAM_MSG = "INCOMING_MESSAGE_PARAM_MSG";
	/**
	 * Name for the session id parameter for an incoming message
	 * 传入消息的会话ID参数名称
	 */
	public static final String INCOMING_MESSAGE_PARAM_SESSIONID = "INCOMING_MESSAGE_PARAM_SESSIONID";

	// Parameters defined for VIEW REFRESH EVENT
	/**
	 * Name for the list of changes parameter at each refresh
	 * 名称的变化参数在每次刷新列表
	 */
	public static final String VIEW_REFRESH_PARAM_LISTOFCHANGES = "VIEW_REFRESH_PARAM_LISTOFCHANGES";
	/**
	 * Name of contacts map parameter at each refresh
	 * 在每次刷新接触map参数名称
	 */
	public static final String VIEW_REFRESH_CONTACTSMAP = "VIEW_REFRESH_CONTACTSMAP";
	/**
	 * Name of location map parameter at each refresh
	 * 在每次刷新位置图参数的名称
	 */
	public static final String VIEW_REFRESH_PARAM_LOCATIONMAP = "VIEW_REFRESH_PARAM_LOCATIONMAP";

	// Parameters defined for CONTACT DISCONNECT EVENT
	/**
	 * Name of parameter
	 */
	public static final String CONTACT_DISCONNECT_PARAM_CONTACTNAME = "CONTACT_DISCONNECT_PARAM_CONTACTNAME";

	/**
	 * Name of the event
	 */
	private String name;

	/**
	 * Maps that stores event parameters
	 * 地图，存储事件参数
	 */
	private Map<String, Object> paramsMap;

	/**
	 * Returns the name of the event
	 * 返回事件的名称。
	 * @return event name
	 */
	public final String getName()
	{
		return name;

	}

	/**
	 * Builds a new event
	 * 
	 * @param name
	 *            name of the event
	 */
	public MsnEvent(String name)
	{
		this.name = name;
	}

	/**
	 * Adds a parameter to the event using the given name
	 * 添加一个事件参数的使用给定的名称
	 * @param name
	 *            name of the parameter
	 * @param value
	 *            value to be added
	 */
	public void addParam(String name, Object value)
	{
		if (paramsMap == null)
		{
			paramsMap = new HashMap<String, Object>();
		}

		paramsMap.put(name, value);
		myLogger.log(Logger.FINE, "putting in event map parameter " + name + " having value " + value.toString());
	}

	/**
	 * Retrieves a parameter from an event
	 * 检索从事件参数
	 * @param name
	 *            of the parameter to retrieve
	 * @return value of the parameter
	 */
	public Object getParam(String name)
	{
		return paramsMap.get(name);
	}
}
