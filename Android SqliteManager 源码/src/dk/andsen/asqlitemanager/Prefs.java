/**
 * Part of aSQLiteManager (http://sourceforge.net/projects/asqlitemanager/)
 * a a SQLite Manager by andsen (http://sourceforge.net/users/andsen)
 *
 * The preferences screen
 *
 * @author andsen
 *
 */
package dk.andsen.asqlitemanager;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * Part of aSQLiteManager (http://sourceforge.net/projects/asqlitemanager/)
 * a a SQLite Manager by andsen (http://sourceforge.net/users/andsen)
 *
 * This class contains the preference functionalities.
 *
 * @author andsen
 *
 */
public class Prefs extends PreferenceActivity {
   // Option names and default values
   private static final String OPT_PAGESIZE = "PageSize";
   private static final String OPT_PAGESIZE_DEF = "20";
   private static final String OPT_SAVESQL = "SaveSQL";
   private static final boolean OPT_SAVESQL_DEF = false;
   private static final String OPT_FILENO = "RecentFiles";
   private static final String OPT_FILENO_DEF = "20";

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      addPreferencesFromResource(R.xml.settings);
   }

   /**
    * Return the numbers of records to retrieve when paging data
    * @param context
    * @return page size
    */
  public static int getPageSize(Context context) {
     return new Integer( PreferenceManager.getDefaultSharedPreferences(context)
           .getString(OPT_PAGESIZE, OPT_PAGESIZE_DEF)).intValue();
  }

  /**
   * Return true if executed statements are stored in database
   * @param context
   * @return
   */
  public static boolean getSaveSQL(Context context) {
  	return PreferenceManager.getDefaultSharedPreferences(context)
  		.getBoolean(OPT_SAVESQL, OPT_SAVESQL_DEF);
  }

	public static int getNoOfFiles(Context context) {
    return new Integer( PreferenceManager.getDefaultSharedPreferences(context)
        .getString(OPT_FILENO, OPT_FILENO_DEF)).intValue();
	}
}
