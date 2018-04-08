package it.telecomitalia.jchat;

import jade.util.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides functionalities to create and fire events and to register listeners
 * that should handle these events. Events are defined as a public. When an
 * event is fired, its registered handler is executed, if any. Currently
 * 
 * 提供的功能来创建和触发事件注册侦听器应该处理这些事件。被定义为公共事件。当一个事件被激发，其注册的处理程序执行，如果有的话。目前
 * @see MsnEvent
 * @see IEventHandler
 */

//消息事件管理
public class MsnEventMgr
{

	/**
	 * The event manager instance  管理实例
	 */
	private static MsnEventMgr theInstance = new MsnEventMgr();
	/**
	 * Map of all registered event handlers  注册事件处理
	 */
	private Map<String, IEventHandler> eventMap;

	/**
	 * Instance of the JADE logger for debugging
	 */
	private Logger myLogger = Logger.getMyLogger(MsnEventMgr.class.getName());

	/**
	 * Instantiates a new Event handler  实例化一个新事件处理
	 */
	private MsnEventMgr()
	{
		eventMap = new HashMap<String, IEventHandler>(2);
	}

	/**
	 * Retrieves an instance of the Event Manager
	 * 返回一个事件管理实例
	 * @return Event manager instance
	 */
	public static MsnEventMgr getInstance()
	{
		return theInstance;
	}

	/**
	 * Creates a new event using the given name
	 * 根据名字创建一个新的实例
	 * @param eventName
	 *            the name of the event to be created
	 * @return the new event
	 */
	public MsnEvent createEvent(String eventName)
	{
		return new MsnEvent(eventName);
	}

	/**
	 * This methods teaches the manager which handler should be called for each
	 * kind of event Different activities should register their own handler to
	 * change the behaviour for a given event
	 * 此方法教经理，
	 * 被称为处理程序应为每一种不同的活动事件应该注册自己的处理程序更改为某一特定事件的行为
	 * @param eventName
	 *            name of the event that could occur
	 * @param updater
	 *            the handler that should be called
	 */
	public synchronized void registerEvent(String eventName, IEventHandler updater)
	{
		eventMap.put(eventName, updater);
	}

	/**
	 * This method issues an event and called its handler if any. It is called
	 * by the agent thread each time an event takes place
	 * 这个方法发出一个事件，并呼吁其处理程序，如果没有。
	 * 它被称为代理线程每个事件发生的时间
	 * @param event
	 *            the event to fire
	 */
	public synchronized void fireEvent(MsnEvent event)
	{
		String eventName = event.getName();
		myLogger.log(Logger.FINE, "Firing event " + event.getName());
		IEventHandler handler = eventMap.get(eventName);

		if (handler != null)
		{
			handler.handleEvent(event);
		} 
		else
		{
			myLogger.log(Logger.SEVERE, "WARNING: an event was fired but no handler was registered!");
		}

	}

	/**
	 * This method issues an event after a delay and call its handler if any. It
	 * is called by the agent thread each time an event takes place
	 * 此方法问题后延迟的事件，并调用其处理程序，如果有的话。它被称为代理线程每个事件发生的时间
	 * @param event
	 *            the event to fire
	 * @param delayMillis
	 *            delay we have to wait before the event is fired
	 */
	public void fireEventDelayed(MsnEvent event, long delayMillis)
	{
		//事件延迟
		String eventName = event.getName();
		myLogger.log(Logger.FINE, "Firing event delayed " + event.getName());
		IEventHandler handler = eventMap.get(eventName);

		try
		{
			Thread.sleep(delayMillis);
			handler.handleEvent(event);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
