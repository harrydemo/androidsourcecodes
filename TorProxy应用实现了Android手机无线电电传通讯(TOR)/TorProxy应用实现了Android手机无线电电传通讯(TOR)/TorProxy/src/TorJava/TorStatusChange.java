package TorJava;

/**
 * Interface by which the controller of the Tor system can be notified of
 * changes to Tor's status.
 * 
 * @author Connell Gauld
 * 
 */
public interface TorStatusChange {
	public void statusChanged();
}
