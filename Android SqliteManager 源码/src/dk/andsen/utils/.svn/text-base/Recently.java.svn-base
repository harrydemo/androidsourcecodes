/**
 * Part of aSQLiteManager (http://sourceforge.net/projects/asqlitemanager/)
 * a a SQLite Manager by andsen (http://sourceforge.net/users/andsen)
 *
 * Handle a list of recently opened files.
 * 
 * file1;file2;file3;...;
 * 
 * @author andsen
 *
 */
package dk.andsen.utils;

public class Recently {

	/**
	 * Generate a updated list of recently opened files based on the
	 * previous list and the newly opened file
	 * @param files a semicolon separated list of filenames
	 * @param file the newly opened files name
	 * @param length the max length of the list
	 * @return a new list of recently opened files
	 */
	public static String updateList(String files, String file, int length) {
		// split list of files into a List of String
		if (files == null)
			return file + ";";
		String[] fileList = files.split(";");
		// place file at top of list
		String ret = file + ";";
		// convert the list to semicolon separated list
		for (int i = 0; i < fileList.length; i++) {
			// don't include the file if it equals the new file	
			if (!fileList[i].equals(file)) {
				ret += fileList[i] + ";";
			} else {
				++length;
			}
			// if we have the required number of files exit loop
			if (i == length -2)
				break;
		}
		return ret;
	}
}
