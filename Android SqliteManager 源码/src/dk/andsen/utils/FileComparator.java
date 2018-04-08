/**
 * Part of one of andsens open source project (a41cv / aSQLiteManager) 
 *
 * @author andsen
 *
 */
package dk.andsen.utils;

import java.io.File;
import java.util.Comparator;

/**
 * Sort files first directories then files	
 * @author andsen
 *
 */
class FileComparator implements Comparator<File> {
   
    public int compare(File file1, File file2){
    	String f1 = ((File)file1).getName();
    	String f2 = ((File)file2).getName();
    	int f1Length = f1.length();
    	int f2Length = f2.length();
    	boolean f1Dir = (((File)file1).isDirectory()) ? true: false;
    	boolean f2Dir = (((File)file2).isDirectory()) ? true: false;
    	int shortest = (f1Length > f2Length) ? f2Length : f1Length;
    	// one of the files is a directory
    	if (f1Dir && !f2Dir)
    		return -1;
    	if (f2Dir && !f1Dir)
    		return 1;
    	// sort alphabetically
    	for (int i = 0; i < shortest; i++) {
    		if (f1.charAt(i) > f2.charAt(i))
    			return 1;
    		else if (f1.charAt(i) < f2.charAt(i))
    			return -1;
    	}
    	if (f1Length > f2Length)
    		return 1;
    	else
    		return 0; 
    }
}
