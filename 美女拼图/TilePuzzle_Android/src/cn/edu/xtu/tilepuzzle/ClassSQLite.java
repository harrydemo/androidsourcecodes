package cn.edu.xtu.tilepuzzle;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ClassSQLite {

	private static Context context;// Ӧ�û��������� Activity ��������
	private final static String DBName=ClassGameDB.DATABASE_TILEPUZZLE_NAME;
	private final static String userTableName=ClassGameDB.TABLE_TILEPUZZLE_PEOPLEINFO;
	private final static String gameSetTableName=ClassGameDB.TABLE_TILEPUZZLE_GAMEDATA;
	
	public ClassSQLite(Context _context) {
		context = _context;
	}

	/**
	 * �����ݿ�
	 * 
	 * @DBName ���ݿ�����
	 */
	public SQLiteDatabase openDBByName(String DBName) {
		SQLiteDatabase db;
		//deleteDBByName(DBName);
		db = context.openOrCreateDatabase(DBName, Context.MODE_PRIVATE, null);
		return db;
	}
	
	/**
	 * �����ݿ�
	 * 
	 * @DBName ���ݿ�����
	 */
	public void createDBByName(String DBName) {
		SQLiteDatabase db;
		//deleteDBByName(DBName);
		db = context.openOrCreateDatabase(DBName, Context.MODE_PRIVATE, null);
		db.close();
	}

	/**
	 * ɾ�����ݿ�
	 * 
	 * @DBName ���ݿ�����
	 */
	public boolean deleteDBByName(String DBName) {
		context.deleteDatabase(DBName);
		Log.d("DB", "had deleted database:" + DBName);
		return false;
	}

	/**
	 * ɾ�����ݿ��б������
	 * @TableName ������
	 */
	public boolean deleteTableByName(String TableName) {
		SQLiteDatabase dbDatabase = openDBByName(DBName);
		dbDatabase.delete(TableName, null, null);
		close(dbDatabase);		
		Log.d("DB", "had deleted table:" + DBName + "->" + TableName);
		return false;
	}
	
	public boolean isExistTableByName(String TableName) {
		SQLiteDatabase db_ = openDBByName(DBName);
		String sqlString = "select * from " + TableName;		
		try {
			db_.execSQL(sqlString);
			//Log.d("DB", "Create Table:" + TableName);
			return true;
		} catch (Exception e) {
			//Log.d("DB", TableName + " is exists.");			
			return false;
		} finally {
			close(db_);
		}
		
	}
	

	/**
	 * ������ݿ��еı��Ƿ����
	 * @TableName ������
	 */
	public boolean checkTableIsExistByName(String TableName) {
		SQLiteDatabase db_ = openDBByName(DBName);
		String sqlString = "";
		if (TableName.equals(ClassGameDB.TABLE_TILEPUZZLE_GAMEDATA)) {
			sqlString = "CREATE TABLE " + TableName + ClassGameDB.GAMEDATA_STRUCTURE;
		} else if (TableName.equals(ClassGameDB.TABLE_TILEPUZZLE_PEOPLEINFO)) {
			sqlString = "CREATE TABLE " + TableName + ClassGameDB.PEOPLEINFO_STRUCTURE;
		} else
			;
		try {
			db_.execSQL(sqlString);
			//Log.d("DB", "Create Table:" + TableName);
			if (TableName.equals(ClassGameDB.TABLE_TILEPUZZLE_GAMEDATA))
				this.addGameData( ClassGameDB.gameSetData);
			return true;
		} catch (Exception e) {
			//Log.d("DB", TableName + " is exists.");
			if (TableName.equals(ClassGameDB.TABLE_TILEPUZZLE_GAMEDATA)) {
				Cursor cursor_ = getCursorByTableName(TableName);
				if (cursor_ == null || cursor_.getCount() == 0)
					addGameData(ClassGameDB.gameSetData);
			}
			return false;
		} finally {
			close(db_);
		}
		
	}
	/**
	 * �����ݿ��д�����
	 * @TableName ������
	 */
	public boolean createTableByName(String TableName){
		SQLiteDatabase db_ = openDBByName(DBName);
		String sqlString = "";
		if (TableName.equals(ClassGameDB.TABLE_TILEPUZZLE_GAMEDATA)) {
			sqlString = "CREATE TABLE " + TableName + ClassGameDB.GAMEDATA_STRUCTURE;
		} else if (TableName.equals(ClassGameDB.TABLE_TILEPUZZLE_PEOPLEINFO)) {
			sqlString = "CREATE TABLE " + TableName + ClassGameDB.PEOPLEINFO_STRUCTURE;
		} else
			;
		
		try {
			db_.execSQL(sqlString);	
			//Log.d("DB ", TableName + " had created.");	
			return true;
		} catch (Exception e) {
			//Log.d("DB ", TableName + " is exists. don't create.");	
			return false;
		} finally {
			close(db_);
		}
	}
	
	/**
	 * �÷��������� �������ݵ����ݿ����Ϸ���ò�������
	 * @gameSetData_ ��Ϸ����
	 * 
	 */
	public boolean addGameData(String[] gameSetData_) {
		SQLiteDatabase db_=openDBByName(DBName);
		String TableName=gameSetTableName; 
		String sql = "insert into " + TableName + " values(";
		int i = 0;
		for (i = 0; i < gameSetData_.length - 1; i++)
			sql += "'" + gameSetData_[i] + "'" + ",";
		sql += "'" + gameSetData_[i] + "');";
		Log.d("DB", sql);
		try {
			db_.execSQL(sql);
			//Log.d("DB", "insert Table ok");
			return true;
		} catch (Exception e) {
			//Log.d("DB", "insert Table  err: " + sql);
			return false;
		} finally {
			close(db_);
		}
	}
	/**
	 * �÷��������� �������ݵ����ݿ����Ϸ��ұ���
	 * 
	 * @DBName ���ݿ�����
	 * @TableName ������
	 * @peopleInfo ��������
	 */
	public boolean addUserInfo(String[][] peopleInfo) {
		SQLiteDatabase db_=openDBByName(DBName);		
		
		ContentValues values=new ContentValues();
		try {
			for (int i = 0; i < peopleInfo.length; i++) {
				values=new ContentValues();
				values.put("NAME", peopleInfo[i][0]);
				values.put("TIME", peopleInfo[i][1]);				
				db_.insert(userTableName, null, values);
			}
			Log.d("DB", "insert Table ok");
			return true;
		} catch (Exception e) {
			Log.d("DB", "insert Table  err: ???");
			return false;
		} finally {
			close(db_);
		}
	}

	/**
	 * �÷��������� �������ݵ����ݿ�������Ϣ����
	 * 
	 * @DBName ���ݿ�����
	 * @TableName ������
	 * @peopleName ��������
	 * @time ��Ϸ�õ�ʱ��
	 */
	/*public boolean addUserInfoTable(SQLiteDatabase db_, String TableName, String peopleName, int time) {
		ContentValues values = new ContentValues();
		try {
			values.put("NAME", peopleName);
			values.put("TIME", time);
			db_.insert(TableName, null, values);
			return true;
		} catch (Exception e) {
			Log.d("DB", "insert Table  err:?? ");
			return false;
		} finally {
			close(db_);
		}
	}
	*/
	/**
	 * �÷��������� �������ݵ����ݿ�������Ϣ����
	 * 
	 * @DBName ���ݿ�����
	 * @TableName ������
	 * @peopleName ��������
	 * @time ��Ϸ�õ�ʱ��
	 */
	public  boolean addUserInfo(String peopleName, long time) {
		SQLiteDatabase db_=openDBByName(DBName);		
		ContentValues values = new ContentValues();
		try {
			values.put("NAME", peopleName);
			values.put("TIME", time);
			db_.insert(userTableName, null, values);
			return true;
		} catch (Exception e) {
			Log.d("DB", "insert Table  err:?? ");
			return false;
		} finally {
			close(db_);
		}
	}

	/**
	 * ��ȡ���ݿ�ı�������
	 * @DBName ���ݿ�����
	 * @TableName ������
	 * @return ����Cursor
	 */
	/*public Cursor getDataByTableName(SQLiteDatabase db_, String TableName) {
		// Cursor cursor = db_.query(TableName, null, null, null, null, null, null);
		String sql = "";
		if (TableName.equals(GameDB.TABLE_TILEPUZZLE_GAMEDATA))
			sql = "select * from " + TableName+";";
		else if (TableName.equals(GameDB.TABLE_TILEPUZZLE_PEOPLEINFO))
			sql = "select * from " + TableName + " order by TIME ASC"+";";
		Cursor cursor = db_.rawQuery(sql, null);
		Log.d("DB", "cursor count:"+cursor.getCount());
		int mycount = cursor.getCount();
		close(db_);
		if (mycount == 0)
			return null;
		else
			return cursor;
	}*/
	
	/**
	 * ��ȡ���ݿ�ı�������
	 * @DBName ���ݿ�����
	 * @TableName ������
	 * @return ����Cursor
	 */
	public Cursor getCursorByTableName(String TableName) {
		SQLiteDatabase db_=openDBByName(DBName) ;
		// Cursor cursor = db_.query(TableName, null, null, null, null, null, null);
		String sql = "";
		if (TableName.equals(ClassGameDB.TABLE_TILEPUZZLE_GAMEDATA))
			sql = "select * from " + TableName+";";
		else if (TableName.equals(ClassGameDB.TABLE_TILEPUZZLE_PEOPLEINFO))
			sql = "select * from " + TableName + " order by TIME ASC"+";";
		Cursor cursor = db_.rawQuery(sql, null);
		Log.d("DB", "cursor count:"+cursor.getCount());
		int mycount = cursor.getCount();
		close(db_);
		if (mycount == 0)
			return null;
		else
			return cursor;
	}

	/**
	 * �������ݿ��еĸ�����Ϣ
	 * @DBName ���ݿ�����
	 * @TableName ������
	 * @peopleName ��������
	 * @time ��Ϸ�õ�ʱ��
	 */
	/*public boolean updateTableByName(String DBName, String TableName, String peopleName, int time) {
		SQLiteDatabase db_ = openDBByName(DBName);
		ContentValues values = new ContentValues();
		values.put("NAME", peopleName);
		values.put("TIME", time);
		db_.update(TableName, values, "NAME='" + peopleName + "'", null);
		close(db_);
		return false;
	}*/
	
	
	/**
	 * �������ݿ��еĸ�����Ϣ
	 * 
	 * @DBName ���ݿ�����
	 * @TableName ������
	 * @peopleName ��������
	 * @time ��Ϸ�õ�ʱ��
	 */
	public boolean updateUserInfoByNameAndTime(String peopleName, int time) {	
		SQLiteDatabase db_=openDBByName(DBName);
		
		ContentValues values = new ContentValues();
		values.put("NAME", peopleName);
		values.put("TIME", time);
		db_.update(userTableName, values, "NAME='" + peopleName + "';", null);
		close(db_);
		return false;
	}

	/**
	 * �������ݿ��е���Ϸ���ò���
	 * 
	 * @DBName ���ݿ�����
	 * @TableName ������
	 * @gameData ��Ϸ����
	 */
	public boolean updateGameSetData(String gameData[]) {
		//SQLiteDatabase db_ = openDBByName(DBName);
		SQLiteDatabase db_=openDBByName(DBName);
		String TableName=gameSetTableName; 
		ContentValues values = new ContentValues();
		values.put("orgImageID", gameData[ClassGameDB.IndexInGameSetDatat_orgImageID]);
		values.put("reversed", gameData[ClassGameDB.IndexInGameSetDatat_reversed]);
		values.put("funny", gameData[ClassGameDB.IndexInGameSetDatat_funny]);
		values.put("addString", gameData[ClassGameDB.IndexInGameSetDatat_addString]);
		values.put("hard", gameData[ClassGameDB.IndexInGameSetDatat_hard]);
		values.put("rows", gameData[ClassGameDB.IndexInGameSetDatat_rows]);
		values.put("columns", gameData[ClassGameDB.IndexInGameSetDatat_columns]);
		try {
			Log.d("DB", "��ʼ������Ϸ��Ϣ��" +TableName);			
			db_.update(TableName, values, null,null);
			Log.d("DB", "������Ϸ��Ϣ�ɹ�->" +TableName+":"+ gameData.toString());
			return true;
		} catch (Exception e) {
			Log.d("DB", "������Ϸ��Ϣʧ��->" +TableName);
		}finally{
			close(db_);
		}
		return false;
	}
	
	/**
	 * ��ȡ���������Ϣ
	 */
	public String[][] getUserInfo(){
		Cursor cursor=getCursorByTableName(userTableName);
		int mycount = cursor.getCount();
		if (mycount == 0)
			return null;
		String[][] peopleInfo = new String[mycount][2];
		// Ҫ�������еķ���ֵ�ã�������Ҫ��ָ���Ƶ���һ����
		cursor.moveToFirst();
		// ͬ���������Ҫ�ƶ�������ֵ��ĳ���ϣ�����������²���
		// cursor.moveToPosition(position);
		int i = 0;
		for (i = 0; i < mycount; i++) {
			peopleInfo[i][0] = cursor.getString(cursor.getColumnIndex("NAME"));
			peopleInfo[i][1] = cursor.getString(cursor.getColumnIndex("TIME"));
			// ��ȡĳ��ֵmyCursor.getString();
			cursor.moveToNext();
			// myCursor.moveToPrevious();
		}
		cursor.close();
		return peopleInfo;
	}
	
	/**
	 * ��ȡ��Ϸ������Ϣ
	 * @Cursor Cursor
	 * @return String[]
	 */
	public String[] getGameData() {
		Cursor cursor=getCursorByTableName(gameSetTableName);
		int mycount = cursor.getCount();
		if (mycount == 0)
			return null;
		String[] gameData = new String[ClassGameDB.gameSetData.length];
		// Ҫ�������еķ���ֵ�ã�������Ҫ��ָ���Ƶ���һ����
		cursor.moveToFirst();
		//"orgImageID", "reversed", "funny", "addStringg", "hard", "rows","columns" 
		int orgImageID = cursor.getColumnIndex(ClassGameDB.gameSetDataName[ClassGameDB.IndexInGameSetDatat_orgImageID]);
		gameData[ClassGameDB.IndexInGameSetDatat_orgImageID]=cursor.getString(orgImageID);
		Log.d("DB", orgImageID+"->orgImageID:"+cursor.getString(orgImageID));
		
		int reversed = cursor.getColumnIndex(ClassGameDB.gameSetDataName[ClassGameDB.IndexInGameSetDatat_reversed]);
		gameData[ClassGameDB.IndexInGameSetDatat_reversed]=cursor.getString(reversed);
		Log.d("DB", reversed+"->reversed:"+cursor.getString(reversed));
		
		int funny = cursor.getColumnIndex(ClassGameDB.gameSetDataName[ClassGameDB.IndexInGameSetDatat_funny]);
		gameData[ClassGameDB.IndexInGameSetDatat_funny]=cursor.getString(funny);
		Log.d("DB", funny+"->funny:"+cursor.getString(funny));
		
		int addString = cursor.getColumnIndex(ClassGameDB.gameSetDataName[ClassGameDB.IndexInGameSetDatat_addString]);
		gameData[ClassGameDB.IndexInGameSetDatat_addString]=cursor.getString(addString);
		Log.d("DB", addString+"->addString:"+cursor.getString(addString));
		
		int hard = cursor.getColumnIndex(ClassGameDB.gameSetDataName[ClassGameDB.IndexInGameSetDatat_hard]);
		gameData[ClassGameDB.IndexInGameSetDatat_hard]=cursor.getString(hard);
		Log.d("DB", hard+"->hard:"+cursor.getString(hard));
		
		int rows = cursor.getColumnIndex(ClassGameDB.gameSetDataName[ClassGameDB.IndexInGameSetDatat_rows]);
		gameData[ClassGameDB.IndexInGameSetDatat_rows]=cursor.getString(rows);
		Log.d("DB", rows+"->rows:"+cursor.getString(rows));
		
		int columns = cursor.getColumnIndex(ClassGameDB.gameSetDataName[ClassGameDB.IndexInGameSetDatat_columns]);
		gameData[ClassGameDB.IndexInGameSetDatat_columns]=cursor.getString(columns);
		Log.d("DB", columns+"->columns:"+cursor.getString(columns));
		
		cursor.close();
		return gameData;
	}
	/**
	 * ɾ�����ݿ��б�ĸ�����Ϣ
	 * @peopleName ��������
	 */
	public boolean deletePeopleInfoByPeopleName(String peopleName) {
		SQLiteDatabase db_ = openDBByName(DBName);
		db_.delete(userTableName, "NAME='" + peopleName + "'", null);
		close(db_);
		return false;
	}
	public static void close(SQLiteDatabase db_) {
		db_.close();
	}
	public void SqliteTest() {
		String gameData[]=getGameData();		
		String resultString="";
		for(int i=0;i<7;i++){
			resultString+=gameData[i]+";";
		}		
		Log.d("DB", "SqliteTest:��ѯ->TABLE_TILEPUZZLE_GAMEDATA:"+resultString);
		
		String temp[]=ClassGameDB.gameSetData;
		temp[1]="Y";
		temp[2]="Y";
		temp[3]="Y";
		temp[4]="Y";
		
		resultString="";
		for(int i=0;i<7;i++)
			resultString+=temp[i]+";";
		Log.d("DB", "SqliteTest:������Ϸ����Ϊ->TABLE_TILEPUZZLE_GAMEDATA:"+resultString);
		
		updateGameSetData(temp);
		gameData=getGameData();	
		resultString="";
		for(int i=0;i<7;i++){
			resultString+=gameData[i]+";";
		}		
		Log.d("DB", "SqliteTest:���º�->TABLE_TILEPUZZLE_GAMEDATA:"+resultString);
		
		//Log.d("DB", "SqliteTest:ɾ����->TABLE_TILEPUZZLE_GAMEDATA:"+resultString);
		
		String tempP[][]=new String[2][2];
		tempP[0][0]="LIUYAOHUA";
		tempP[0][1]="100";
		tempP[1][0]="DAIQING";
		tempP[1][1]="500";
		addUserInfo(tempP);
		Log.d("DB", "SqliteTest:����->TABLE_TILEPUZZLE_PEOPLEINFO:"+tempP);		
		
		String peopleInfo[][]=getUserInfo();		
		for(int i=0;i<peopleInfo.length;i++){			
			Log.d("DB", "SqliteTest:��ѯ->TABLE_TILEPUZZLE_PEOPLEINFO:"+peopleInfo[i][0]+" : "+peopleInfo[i][1]);
		}
		
		updateUserInfoByNameAndTime("LIUYAOHUA", 50);
		updateUserInfoByNameAndTime("DAIQING", 50);		
		String peopleInfoUP[][]=getUserInfo();		
		for(int i=0;i<peopleInfoUP.length;i++){			
			Log.d("DB", "SqliteTest:���º�->TABLE_TILEPUZZLE_PEOPLEINFO:"+peopleInfoUP[i][0]+" : "+peopleInfoUP[i][1]);
		}
		
		deletePeopleInfoByPeopleName("DAIQING");
		String peopleInfoD[][]=getUserInfo();		
		for(int i=0;i<peopleInfoD.length;i++){			
			Log.d("DB", "SqliteTest:ɾ����->TABLE_TILEPUZZLE_PEOPLEINFO:"+peopleInfoD[i][0]+" : "+peopleInfoD[i][1]);
		}
		
		addUserInfo("DAIQING",100);
		 peopleInfo=getUserInfo();		
		for(int i=0;i<peopleInfo.length;i++){
			Log.d("DB", "SqliteTest:ɾ���ٴ������û���Ϣ��ѯ->TABLE_TILEPUZZLE_PEOPLEINFO:"+peopleInfo[i][0]+" : "+peopleInfo[i][1]);
		}
		
	}
}

	/**
	 * ͨ���û�����ѯ�����Ϣ��¼
	 * @Cursor Cursor
	 * @peopleName �������
	 * @return String ָ������¼��ָ�룬������JDBC �� ResultSet
	 */
/*	public String[] getPeopleInfoFromCursor(Cursor cursor, String peopleName) {
		int mycount = cursor.getCount();
		if (mycount == 0)
			return null;
		String[] peopleInfo = new String[2];
		// Ҫ�������еķ���ֵ�ã�������Ҫ��ָ���Ƶ���һ����
		cursor.moveToFirst();
		// ͬ���������Ҫ�ƶ�������ֵ��ĳ���ϣ�����������²���
		// cursor.moveToPosition(position);
		int i = 0;
		for (i = 0; i < mycount; i++) {
			if (cursor.getString(1).endsWith(peopleName)) {
				peopleInfo[0] = cursor.getString(0);
				peopleInfo[1] = cursor.getString(1);
				break;
			}
			// ��ȡĳ��ֵmyCursor.getString();
			cursor.moveToNext();
			// myCursor.moveToPrevious();
		}
		cursor.close();
		if (i >= mycount)
			return null;
		return peopleInfo;
	}
*/
	
	/**
	 * ��ȡ���������Ϣ
	 * @Cursor Cursor
	 * @return String[][]
	 */
/*	public String[][] getUserInfoFromCursor(Cursor cursor) {
		int mycount = cursor.getCount();
		if (mycount == 0)
			return null;
		String[][] peopleInfo = new String[mycount][2];
		// Ҫ�������еķ���ֵ�ã�������Ҫ��ָ���Ƶ���һ����
		cursor.moveToFirst();
		// ͬ���������Ҫ�ƶ�������ֵ��ĳ���ϣ�����������²���
		// cursor.moveToPosition(position);
		int i = 0;
		for (i = 0; i < mycount; i++) {
			peopleInfo[i][0] = cursor.getString(cursor.getColumnIndex("NAME"));
			peopleInfo[i][1] = cursor.getString(cursor.getColumnIndex("TIME"));
			// ��ȡĳ��ֵmyCursor.getString();
			cursor.moveToNext();
			// myCursor.moveToPrevious();
		}
		cursor.close();
		return peopleInfo;
	}
	*/
	
	/**
	 * ��ȡ��Ϸ������Ϣ
	 * @Cursor Cursor
	 * @return String[]
	 */
	/*public String[] getGameDataFromCursor(Cursor cursor) {
		int mycount = cursor.getCount();
		if (mycount == 0)
			return null;
		String[] gameData = new String[GameDB.gameSetData.length];
		// Ҫ�������еķ���ֵ�ã�������Ҫ��ָ���Ƶ���һ����
		cursor.moveToFirst();
		//"orgImageID", "reversed", "funny", "addStringg", "hard", "rows","columns" 
		int orgImageID = cursor.getColumnIndex(GameDB.gameSetDataName[GameDB.IndexInGameSetDatat_orgImageID]);
		gameData[GameDB.IndexInGameSetDatat_orgImageID]=cursor.getString(orgImageID);
		Log.d("DB", orgImageID+"->orgImageID:"+cursor.getString(orgImageID));
		
		int reversed = cursor.getColumnIndex(GameDB.gameSetDataName[GameDB.IndexInGameSetDatat_reversed]);
		gameData[GameDB.IndexInGameSetDatat_reversed]=cursor.getString(reversed);
		Log.d("DB", reversed+"->reversed:"+cursor.getString(reversed));
		
		int funny = cursor.getColumnIndex(GameDB.gameSetDataName[GameDB.IndexInGameSetDatat_funny]);
		gameData[GameDB.IndexInGameSetDatat_funny]=cursor.getString(funny);
		Log.d("DB", funny+"->funny:"+cursor.getString(funny));
		
		int addString = cursor.getColumnIndex(GameDB.gameSetDataName[GameDB.IndexInGameSetDatat_addString]);
		gameData[GameDB.IndexInGameSetDatat_addString]=cursor.getString(addString);
		Log.d("DB", addString+"->addString:"+cursor.getString(addString));
		
		int hard = cursor.getColumnIndex(GameDB.gameSetDataName[GameDB.IndexInGameSetDatat_hard]);
		gameData[GameDB.IndexInGameSetDatat_hard]=cursor.getString(hard);
		Log.d("DB", hard+"->hard:"+cursor.getString(hard));
		
		int rows = cursor.getColumnIndex(GameDB.gameSetDataName[GameDB.IndexInGameSetDatat_rows]);
		gameData[GameDB.IndexInGameSetDatat_rows]=cursor.getString(rows);
		Log.d("DB", rows+"->rows:"+cursor.getString(rows));
		
		int columns = cursor.getColumnIndex(GameDB.gameSetDataName[GameDB.IndexInGameSetDatat_columns]);
		gameData[GameDB.IndexInGameSetDatat_columns]=cursor.getString(columns);
		Log.d("DB", columns+"->columns:"+cursor.getString(columns));
		
		cursor.close();
		return gameData;
	}
	*/
	/**
	 * ɾ�����ݿ��б�ĸ�����Ϣ
	 * @DBName ���ݿ�����
	 * @TableName ������
	 * @peopleName ��������
	 */
/*	public boolean deletePeopleInfoByPeopleName(String DBName, String TableName, String peopleName) {
		SQLiteDatabase db_ = openDBByName(DBName);
		db_.delete(TableName, "NAME='" + peopleName + "'", null);
		close(db_);
		return false;
	}
	*/
	
	/**
	 * ɾ�����ݿ��б�ĸ�����Ϣ
	 * @db_ ���ݿ�
	 * @TableName ������
	 * @peopleName ��������
	 */
	/*public boolean deletePeopleInfoByPeopleName(SQLiteDatabase db_, String TableName, String peopleName) {
		db_.delete(TableName, "NAME='" + peopleName + "'", null);
		close(db_);
		return false;
	}*/
	
	/**
	 * ɾ�����ݿ��б�ĸ�����Ϣ
	 * @db_ ���ݿ�
	 * @TableName ������
	 * @peopleName ��������
	 */
	/*public boolean deletePeopleInfoByPeopleName(SQLiteDatabase db_, String peopleName) {
		db_.delete(this.UserTableName, "NAME='" + peopleName + "'", null);
		close(db_);
		return false;
	}*/
	



	/**
	 * ɾ�����ݿ��б������
	 * @DBName ���ݿ�����
	 * @TableName ������
	 */
	/*public boolean deleteTableByDBName(String DBName, String TableName) {
		SQLiteDatabase dbDatabase = openDBByName(DBName);
		dbDatabase.delete(TableName, null, null);
		close(dbDatabase);
		// checkDBByName(DBName);
		// db.delete(table, whereClause,
		// whereArgs).deleteDatabase(GameDB.DATABASE_TILEPUZZLE_NAME);
		Log.d("DB", "had deleted table:" + DBName + "->" + TableName);
		return false;
	}*/
	
	
	/**
	 * �������ݿ��еĸ�����Ϣ
	 * 
	 * @DBName ���ݿ�����
	 * @TableName ������
	 * @peopleName ��������
	 * @time ��Ϸ�õ�ʱ��
	 */
	/*public boolean updateTableByName(SQLiteDatabase db_, String TableName, String peopleName, int time) {	
		ContentValues values = new ContentValues();
		values.put("NAME", peopleName);
		values.put("TIME", time);
		db_.update(TableName, values, "NAME='" + peopleName + "';", null);
		close(db_);
		return false;
	}*/

	/**
	 * �������ݿ��е���Ϸ���ò���
	 * 
	 * @DBName ���ݿ�����
	 * @TableName ������
	 * @gameData ��Ϸ����
	 */
	/*public boolean updateTableByName(String DBName, String TableName, String gameData[]) {
		SQLiteDatabase db_ = openDBByName(DBName);
		ContentValues values = new ContentValues();
		values.put("orgImageID", gameData[GameDB.IndexInGameSetDatat_orgImageID]);
		values.put("reversed", gameData[GameDB.IndexInGameSetDatat_reversed]);
		values.put("funny", gameData[GameDB.IndexInGameSetDatat_funny]);
		values.put("addString", gameData[GameDB.IndexInGameSetDatat_addString]);
		values.put("hard", gameData[GameDB.IndexInGameSetDatat_hard]);
		values.put("rows", gameData[GameDB.IndexInGameSetDatat_rows]);
		values.put("columns", gameData[GameDB.IndexInGameSetDatat_columns]);
		try {
			db_.update(TableName, values, null, null);
			Log.d("DB", "������Ϸ��Ϣ�ɹ���" + gameData);
			return true;
		} catch (Exception e) {
			Log.d("DB", "������Ϸ��Ϣʧ�ܣ�" + gameData);
		}finally{
			close(db_);
		}
		return false;
	}*/

	/**
	 * �÷��������� �������ݵ����ݿ����Ϸ���ò�������
	 * 
	 * @DBName ���ݿ�����
	 * @TableName ������
	 * @gameSetData_ ��Ϸ����
	 * 
	 */
	/*public boolean addDataToGameTable(SQLiteDatabase db_, String TableName, String[] gameSetData_) {
		String sql = "insert into " + TableName + " values(";
		int i = 0;
		for (i = 0; i < gameSetData_.length - 1; i++)
			sql += "'" + gameSetData_[i] + "'" + ",";
		sql += "'" + gameSetData_[i] + "');";
		Log.d("DB", sql);
		try {
			db_.execSQL(sql);
			Log.d("DB", "insert Table ok");
			return true;
		} catch (Exception e) {
			Log.d("DB", "insert Table  err: " + sql);
			return false;
		} finally {
			close(db_);
		}
	}*/

	/**
	 * �÷��������� �������ݵ����ݿ����Ϸ��ұ���
	 * 
	 * @DBName ���ݿ�����
	 * @TableName ������
	 * @peopleInfo ��������
	 */
/*	public boolean addUserInfoTable(SQLiteDatabase db_, String TableName, String[][] peopleInfo) {
		
		ContentValues values=new ContentValues();
		try {
			for (int i = 0; i < peopleInfo.length; i++) {
				values=new ContentValues();
				values.put("NAME", peopleInfo[i][0]);
				values.put("TIME", peopleInfo[i][1]);				
				db_.insert(TableName, null, values);
			}
			Log.d("DB", "insert Table ok");
			return true;
		} catch (Exception e) {
			Log.d("DB", "insert Table  err: ???");
			return false;
		} finally {
			close(db_);
		}
	}
	*/
	