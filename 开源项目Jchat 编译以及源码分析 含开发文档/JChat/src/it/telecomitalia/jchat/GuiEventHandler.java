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
 * 提供了一个处理程序，需要在UI线程上发表的东西修改的GUI子类只需要实现的抽象方法的processEvent（）应自动执行GUI线程的执行。
 * 请注意对象，这里面有一个处理和正常工作，必须创建UI线程内（如果是在不同的线程创建的，
 * 消息应在不可预知的结果发布的消息队列）。
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
	 * 实现张贴UI线程运行的IEventHandler.handleEvent（）。该事件被传递的Runnable作进一步处理。
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
	 * 这个抽象方法提供的，应当对内部UI线程的Runnable的执行代码。子类只需要实现这个处理UI事件
	 * @param event
	 *            the event that should be handled
	 */
	protected abstract void processEvent(MsnEvent event);

	/**
	 * Provides the Runnable object that shall be posted on UI thread by
	 * handleEvent() using template method pattern
	 * 提供了Runnable对象，应张贴在UI线程的handleEvent（）使用模板方法模式
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
		 * 在UI线程上（只需调用子类中定义的抽象方法被执行的代码）
		 */
		public void run()
		{
			GuiEventHandler.this.processEvent(parameter);
		}

	}
}
