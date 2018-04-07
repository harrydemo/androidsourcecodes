package it.telecomitalia.jchat;

/**
 * Generic interface for an event handler
 * �¼���������ͨ�ýӿ�
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