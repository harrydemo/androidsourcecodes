package greendroid.util;

/**
 * Utility class containing several useful constants related to time.
 * 
 * @author Cyril Mottier
 */
public class Time {
    
    private Time() {
    }

	/**
	 * The number of milliseconds in a second.
	 */
	public static final long GD_SECOND = 1000;

	/**
	 * The number of milliseconds in a minute.
	 */
	public static final long GD_MINUTE = GD_SECOND * 60;

	/**
	 * The number of milliseconds in an hour.
	 */
	public static final long GD_HOUR = GD_MINUTE * 60;

	/**
	 * The number of milliseconds in a day.
	 */
	public static final long GD_DAY = GD_HOUR * 24;

	/**
	 * The number of milliseconds in a week.
	 */
	public static final long GD_WEEK = GD_DAY * 7;

}
