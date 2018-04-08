package it.telecomitalia.jchat;

import jade.util.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides functionalities to create and fire events and to register listeners
 * that should handle these events. Events are defined as a public. When an
 * event is fired, its registered handler is executed, if any. Currently
 * 
 * �ṩ�Ĺ����������ʹ����¼�ע��������Ӧ�ô�����Щ�¼���������Ϊ�����¼�����һ���¼�����������ע��Ĵ������ִ�У�����еĻ���Ŀǰ
 * @see MsnEvent
 * @see IEventHandler
 */

//��Ϣ�¼�����
public class MsnEventMgr
{

	/**
	 * The event manager instance  ����ʵ��
	 */
	private static MsnEventMgr theInstance = new MsnEventMgr();
	/**
	 * Map of all registered event handlers  ע���¼�����
	 */
	private Map<String, IEventHandler> eventMap;

	/**
	 * Instance of the JADE logger for debugging
	 */
	private Logger myLogger = Logger.getMyLogger(MsnEventMgr.class.getName());

	/**
	 * Instantiates a new Event handler  ʵ����һ�����¼�����
	 */
	private MsnEventMgr()
	{
		eventMap = new HashMap<String, IEventHandler>(2);
	}

	/**
	 * Retrieves an instance of the Event Manager
	 * ����һ���¼�����ʵ��
	 * @return Event manager instance
	 */
	public static MsnEventMgr getInstance()
	{
		return theInstance;
	}

	/**
	 * Creates a new event using the given name
	 * �������ִ���һ���µ�ʵ��
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
	 * �˷����̾���
	 * ����Ϊ�������ӦΪÿһ�ֲ�ͬ�Ļ�¼�Ӧ��ע���Լ��Ĵ���������Ϊĳһ�ض��¼�����Ϊ
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
	 * �����������һ���¼����������䴦��������û�С�
	 * ������Ϊ�����߳�ÿ���¼�������ʱ��
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
	 * �˷���������ӳٵ��¼����������䴦���������еĻ���������Ϊ�����߳�ÿ���¼�������ʱ��
	 * @param event
	 *            the event to fire
	 * @param delayMillis
	 *            delay we have to wait before the event is fired
	 */
	public void fireEventDelayed(MsnEvent event, long delayMillis)
	{
		//�¼��ӳ�
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
