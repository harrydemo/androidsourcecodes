/**
 * 
 */
package com.appspot.hexagon.util;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import android.content.res.Resources;
import android.util.Log;

/**
 * @author Admin
 *
 */
public class CSVUtil {

	public static final String CSV_PATTERN = ",(?=([^\"]*\"[^\"]*\")*(?![^\"]*\"))";
	public static String TAG = "CSVUtil";
	
	/**
	 * <p>"CSVUtil.readFromFile()" takes in a String parameter which specifying 
	 * the absolute file path to let it find the csv file.</p>
	 * <p>Then it try to parse the file and return you an ArrayList<String[]> 
	 * object type. The returned object which containing a String array each line representing columns.</p>
	 * @param filename where the absolute path where "*.csv" file lies ...
	 * @return
	 */
	@SuppressWarnings("finally")
	public static ArrayList<String[]> readFromFile(InputStream is) {
        Pattern pattern = Pattern.compile(CSV_PATTERN);
        ArrayList<String[]> csvList = null;
        
        try {
        	csvList = new ArrayList<String[]>();
        	
            //final BufferedReader reader = new BufferedReader(new FileReader(filename));
        	Scanner sc = new Scanner(is);
            for (int row = 0; sc.hasNextLine(); row++) {
            	String line = sc.nextLine();
                String[] items = pattern.split(line);
                //model.setRowCount(row + 1);
                //for (int column = 0; column < items.length; column++) {
                	// nothing to do at column level
                //}
                csvList.add(items);
            }
            sc.close();
        } catch (Exception e) {
            Log.e(TAG,"Error while opening csv file",e);
        }finally{
        	return csvList;
        }
    }
}
