package it.telecomitalia.jchat;

import jade.util.Logger;
import android.os.Handler;

/**
 * Provides an implementation of an handler that needs to post stuff on the UI
 * thread to modify the GUI Subclasses simply needs to implement the abstract
 * method processEvent() that shall be automatically executed on the GUI thread.
 * Please note that this object has an handler inside and to work correctly MUST
 * be created inside the UI thread (if it is created in a different thread,
 * messages shall be posted on its message queue with unpredictable results).
 * �ṩ��һ�����������Ҫ��UI�߳��Ϸ���Ķ����޸ĵ�GUI����ֻ��Ҫʵ�ֵĳ��󷽷���processEvent����Ӧ�Զ�ִ��GUI�̵߳�ִ�С�
 * ��ע�������������һ��������������������봴��UI�߳��ڣ�������ڲ�ͬ���̴߳����ģ�
 * ��ϢӦ�ڲ���Ԥ֪�Ľ����������Ϣ���У���
 */

public abstract class GuiEventHandler implements IEventHandler
{

	/**
	 * Instance of JADE Logger for debugging
	 */
	private Logger myLogger = Logger.getMyLogger(GuiEventHandler.class.getName());

	/**
	 * Handler used for posting Runnables on the UI thread
	 */
	protected Handler localHandler;

	/**
	 * Instantiates a new contacts {@link GuiEventHandler}
	 * 
	 */
	public GuiEventHandler()
	{
		localHandler = new Handler();
		myLogger.log(Logger.FINE, "Handler created by thread " + Thread.currentThread().getId());
	}

	/**
	 * Implements the IEventHandler.handleEvent() by posting a runnable on UI
	 * thread. The event is passed to the Runnable for further processing.
	 * ʵ������UI�߳����е�IEventHandler.handleEvent���������¼������ݵ�Runnable����һ������
	 * @param event
	 *            event that needs to be processed
	 * 
	 */
	public void handleEvent(MsnEvent event)
	{
		localHandler.postDelayed((new MyRunnable(event)), 500);
	}

	/**
	 * This abstract method provides the code that shall be executed on the UI
	 * thread inside the Runnable. Subclasses simply needs to implements this to
	 * handle UI events
	 * ������󷽷��ṩ�ģ�Ӧ�����ڲ�UI�̵߳�Runnable��ִ�д��롣����ֻ��Ҫʵ���������UI�¼�
	 * @param event
	 *            the event that should be handled
	 */
	protected abstract void processEvent(MsnEvent event);

	/**
	 * Provides the Runnable object that shall be posted on UI thread by
	 * handleEvent() using template method pattern
	 * �ṩ��Runnable����Ӧ������UI�̵߳�handleEvent����ʹ��ģ�巽��ģʽ
	 */
	private class MyRunnable implements Runnable
	{
		/**
		 * Event to be stored
		 */
		private MsnEvent parameter;

		/**
		 * Instantiates a new my runnable.
		 * 
		 * @param event
		 *            the event to be handled
		 */
		public MyRunnable(MsnEvent event)
		{
			parameter = event;

		}

		/**
		 * Code to be executed on the UI thread (simply calls the abstract
		 * method defined by subclasses)
		 * ��UI�߳��ϣ�ֻ����������ж���ĳ��󷽷���ִ�еĴ��룩
		 */
		public void run()
		{
			GuiEventHandler.this.processEvent(parameter);
		}

	}
}
