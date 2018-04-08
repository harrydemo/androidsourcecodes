package it.telecomitalia.jchat;

/**
 * Generic interface for an event handler
 * 事件处理程序的通用接口
 */
public interface IEventHandler
{

	/**
	 * Handles the event
	 * 
	 * @param event
	 *            the event that should be handled
	 */
	public abstract void handleEvent(MsnEvent event);

}