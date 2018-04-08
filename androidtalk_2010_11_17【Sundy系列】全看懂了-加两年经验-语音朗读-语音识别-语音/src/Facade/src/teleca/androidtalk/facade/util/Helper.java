package teleca.androidtalk.facade.util;

import teleca.androidtalk.speechengine.OperationType;
import teleca.androidtalk.speechengine.SpeechCommandResult;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

/**
 * @author Jacky Zhang
 * @version 0.1
 *  Tt's a util class used to operation database
 */
public class Helper {
	/*
	 * the path which saved data
	 */
	private static final String AUTHORITY = "teleca.androidtalk.facade.util";
	private static final Uri CONTENNT_URI = Uri.parse("content://"+AUTHORITY+"/AndroidTalk");
	/*
	 * the different type for define command
	 */
	public static final String TYPE_GOTOWEB = "Goto Web";
	public static final String TYPE_DIALING = "Dialing";
	public static final String TYPE_STARTAPP = "Start App";
	public static final String TYPE_SEARCHGOOGLE = "Search Google";
	
	/*
	 * add a command to database
	 */
	public static void addCommand(Activity activity, String name, String category, String relation){
		ContentValues cv = new ContentValues();
		cv.put("cmd_name", name);
		cv.put("cmd_category", category);
		cv.put("relation", relation);
		activity.getContentResolver().insert(CONTENNT_URI, cv);
	}
	
	/*
	 * edit a command to database
	 */
	public static void editCommand(Activity activity, String id, String name, String category, String relation){
		ContentValues cv = new ContentValues();
		cv.put("cmd_name", name);
		cv.put("cmd_category", category);
		cv.put("relation", relation);
		activity.getContentResolver().update(CONTENNT_URI, cv, "id='"+id+"'", null);
	}
	
	/*
	 * delete a command from database
	 */
	public static void deleteCommand(Activity activity, String id){
		activity.getContentResolver().delete(CONTENNT_URI, "id='"+id+"'", null);
	}
	
	/*
	 * check whether a command name has exist in database
	 */
	public static boolean isNameExist(String cmdName, Activity activity){
		Cursor cur = activity.getContentResolver().query(CONTENNT_URI, null, "cmd_name='"+cmdName+"'", null, null);
		if(cur.getCount()>0) return true;
		return false;
	}
	
	/*
	 * return SpeechCommandResult according to command name
	 */
	public static SpeechCommandResult getCommand(String cmdName, Activity activity){
		String[] PROJECTION = new String[] {"cmd_category", "relation"};
		Cursor cur = activity.getContentResolver().query(CONTENNT_URI, PROJECTION, "cmd_name='"+cmdName+"'", null, null);
		cur.moveToFirst();
		SpeechCommandResult sc = new SpeechCommandResult();
		if(cur.getCount()==0) {
			sc.setOpType(OperationType.Unknow);
			return sc;
		}
		sc.setOpType(getCmdType(cur.getString(0)));
		sc.setOpData(cur.getString(1));
		return sc;
	}
	
	/*
	 * according to command type return OperationType
	 */
	private static OperationType getCmdType(String cmdType){
		if(cmdType.equalsIgnoreCase(TYPE_GOTOWEB)) return OperationType.GotoWeb;
		if(cmdType.equalsIgnoreCase(TYPE_DIALING)) return OperationType.Dialing;
		if(cmdType.equalsIgnoreCase(TYPE_STARTAPP)) return OperationType.StartApp;
		if(cmdType.equalsIgnoreCase(TYPE_SEARCHGOOGLE)) return OperationType.SearchGoogle;
		return null;
	}
	
	/*
	 * get command list 
	 */
	public static Cursor getCommandList(Activity activity){
		String[] PROJECTION = new String[] {"id", "cmd_name", "cmd_category","relation"};
		Cursor cur = activity.getContentResolver().query(CONTENNT_URI, PROJECTION, null, null, null);
		return cur;
    }
}
