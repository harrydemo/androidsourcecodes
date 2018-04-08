/**
 * Part of aSQLiteManager (http://sourceforge.net/projects/asqlitemanager/)
 * a Android SQLite Manager by andsen (http://sourceforge.net/users/andsen)
 *
 *	This class contains all all database functions
 *
 * @author andsen
 *
 */
package dk.andsen.asqlitemanager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import dk.andsen.RecordEditor.types.TableField;
import dk.andsen.types.Field;
import dk.andsen.types.FieldDescr;
import dk.andsen.types.QueryResult;
import dk.andsen.utils.Utils;

/**
 * @author Andsen
 *
 */
public class Database {
	public boolean isDatabase = false;
	private SQLiteDatabase _db = null;
	private String _dbPath;
	private Context _cont;
	private String nl = "\n"; 
	
	/**
	 * Open a existing database at the given path
	 * @param dbPath Path to the database
	 */
	public Database(String dbPath, Context cont) {
		_dbPath = dbPath;
		try {
			// Must find a way to check if it is a SQLite file!
			if (testDBFile(dbPath)) {
				// Here we know it is a SQLite 3 file
				Utils.logD("Trying to open (RW): " + dbPath);
				_db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
				_cont = cont;
				isDatabase = true;
			}
		} catch (Exception e) {
			Utils.logD("Trying to open Exception: " + e.getMessage());
			// It is not a database
			isDatabase = false;
		}
	}
	
	/**
	 * Test if a file is a SQLite database
	 * @param dbPath path to the database file
	 * @return true if it is a SQLite file
	 */
	private boolean testDBFile(String dbPath) {
		// File must start with the following 16 bytes
		// 0x53 0x51 0x4c 0x69 0x74 0x65 0x20 0x66 0x6f 0x72 0x6d 0x61 0x74 0x20 0x33 0x00
		// to be a SQLite 3 database
		File backupFile = new File(dbPath);
		FileReader f = null;
		if (backupFile.canRead()) {
			try {
				f = new FileReader(backupFile);
				char buffer[] = new char[16];
				f.read(buffer, 0, 16);
				if (buffer[0] == 0x53 && 
						buffer[1] == 0x51 &&
						buffer[2] == 0x4c &&
						buffer[3] == 0x69 &&
						buffer[4] == 0x74 &&
						buffer[5] == 0x65 &&
						buffer[6] == 0x20 &&
						buffer[7] == 0x66 &&
						buffer[8] == 0x6f &&
						buffer[9] == 0x72 &&
						buffer[10] == 0x6d && 
						buffer[11] == 0x61 &&
						buffer[12] == 0x74 &&
						buffer[13] == 0x20 &&
						buffer[14] == 0x33 &&
						buffer[15] == 0x00) {
					f.close();
					return true; 
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				f.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}
		return false;
	}

	/**
	 * Close the database
	 */
	public void close() {
		_db.close();
	}

	/**
	 * Test the database if not open open it
	 */
	private void testDB() {
		if (_db == null) {
			_db = SQLiteDatabase.openDatabase(_dbPath, null, SQLiteDatabase.OPEN_READWRITE);
		}
		if (!_db.isOpen()) {
			_db = SQLiteDatabase.openDatabase(_dbPath, null, SQLiteDatabase.OPEN_READWRITE);
		}
	}
	
	/**
	 * Retrieve all the table names of the database
	 * @return
	 */
	public String[] getTables() {
		testDB();
		String sql ="select name from sqlite_master where type = 'table' order by name";
		Cursor res = _db.rawQuery(sql, null);
		int recs = res.getCount();
		String[] tables = new String[recs + 1];
		int i = 1;
		tables[0] = "sqlite_master";
		//Utils.logD("Tables: " + recs);
		while(res.moveToNext()) {
			tables[i] = res.getString(0);
			i++;
		}
		res.close();
		return tables;
	}

	/**
	 * Retrieve all views from a database
	 * @return
	 */
	public String[] getViews() {
		testDB();
		String sql ="select name from sqlite_master where type = 'view'";
		Cursor res = _db.rawQuery(sql, null);
		int recs = res.getCount();
		String[] views = new String[recs];
		int i = 0;
		//Utils.logD("Views: " + recs);
		while(res.moveToNext()) {
			views[i] = res.getString(0);
			i++;
		}
		res.close();
		return views;
	}

	/**
	 * Retrieve all views from a database
	 * @return
	 */
	public String[] getIndex() {
		testDB();
		String sql ="select name from sqlite_master where type = 'index'";
		Cursor res = _db.rawQuery(sql, null);
		int recs = res.getCount();
		String[] index = new String[recs];
		int i = 0;
		//Utils.logD("Index: " + recs);
		while(res.moveToNext()) {
			index[i] = res.getString(0);
			i++;
		}
		res.close();
		return index;
	}

	/**
	 * Retrieve a list of field names from a table
	 * @param table
	 * @return
	 */
	public Field[] getFields(String table) {
		// Get field type
		// SELECT typeof(sql) FROM sqlite_master where typeof(sql) <> "null" limit 1
		testDB();
		String sql = "select * from " + table + " limit 1";
		sql = "pragma table_info(" + table + ")";
		Cursor res = _db.rawQuery(sql, null);
		int cols = res.getCount();
		Field[] fields = new Field[cols];
		int i = 0;
		// getting field names
		while(res.moveToNext()) {
			Field field = new Field();
			field.setFieldName(res.getString(1));
			field.setFieldType(res.getString(2));
			field.setNotNull(res.getInt(3));
			field.setDef(res.getString(4));
			field.setPk(res.getInt(5));
			fields[i] = field;
			i++;
		}
		res.close();
		return fields;
	}

	/** 
	 * Return a String list with all field names of the table
	 * @param table
	 * @return
	 */
	public String[] getFieldsNames(String table) {
		testDB();
		String sql = "pragma table_info(" + table + ")";
		Cursor res = _db.rawQuery(sql, null);
		int cols = res.getCount();
		String[] fields = new String[cols];
		int i = 0;
		// getting field names
		while(res.moveToNext()) {
			fields[i] = res.getString(1);
			i++;
		}
		res.close();
		return fields;
	}
	
	/**
	 * Retrieve the number of columns in a table
	 * @param table
	 * @return
	 */
	public int getNumCols(String table) {
		testDB();
		String sql = "select * from " + table + " limit 1";
		Cursor cursor = _db.rawQuery(sql, null);
		int cols = cursor.getColumnCount();
		cursor.close();
		return cols;
	}

	/**
	 * Retrieve all data form the tables and return it as two dimentional string list
	 * @param table
	 * @return
	 */
	public String[][] getTableData(String table, int offset, int limit, boolean view) {
		/*
		 * If not a query or view include rowid in data if no single field
		 * primary key exists
		 */
		//TODO implement sorting on single column asc / des
		// first time a coulms is clicked sort asc if it is clicked again sort des
		testDB();
		String sql = "";
		if (view)
			sql = "select * from " + table + " limit " + limit + " offset " + offset;
		else
			sql = "select rowid as rowid, * from " + table + " limit " + limit + " offset " + offset;
		Utils.logD("SQL = " + sql);
		Cursor cursor = _db.rawQuery(sql, null);
		int cols = cursor.getColumnCount();
		int rows = cursor.getCount();
		String[][] res = new String[rows][cols];
		int i = 0;
		//int j = 0;
		while(cursor.moveToNext()) {
			for (int k=0; k<cols; k++) {
				res[i][k] = cursor.getString(k);
			}
			i++;
		}
		return res;
	}

	/**
	 * Return the SQL that defines the table
	 * @param table
	 * @return a String[] with sql needed to create the table
	 */
	public String[][] getSQL(String table) {
		testDB();
		String sql = "select sql from sqlite_master where tbl_name = '" + table +"'";
		Cursor cursor = _db.rawQuery(sql, null);
		int i = 0;
		String[][] res = new String[cursor.getCount()][1];
		// Split SQL in lines
		while(cursor.moveToNext()) {
				res[i][0] = cursor.getString(0);
			i++;
		}
		cursor.close();
		return res;
	}

	/**
	 * Return the headings for a tables structure
	 * @param table
	 * @return
	 */
	public String[] getTableStructureHeadings(String table) {
		String[] ret = {"id", "name","type","notnull","dflt_value","pk"};
		return ret;
	}
	
	/**
	 * Return table structure i two dimentional string list
	 * @param table
	 * @return
	 */
	public String[][] getTableStructure(String table) {
		testDB();
		String sql = "pragma table_info ("+table+")";
		Cursor cursor = _db.rawQuery(sql, null);
		int cols = cursor.getColumnCount();
		int rows = cursor.getCount();
		String[][] res = new String[rows][cols];
		int i = 0;
		while(cursor.moveToNext()) {
			for (int k=0; k<cols; k++) {
				res[i][k] = cursor.getString(k);
			}
			i++;
		}
		return res;
	}

	/**
	 * Retrieve a list of FieldDescr to describe all fields of a table
	 * @param tableName
	 * @return
	 */
	public FieldDescr[] getTableStructureDef(String tableName) {
		testDB();
		String sql = "pragma table_info ("+tableName+")";
		Cursor cursor = _db.rawQuery(sql, null);
		int rows = cursor.getCount();
		FieldDescr[] flds = new FieldDescr[rows]; 
		int i = 0;
		while(cursor.moveToNext()) {
			FieldDescr fld = new FieldDescr();
			fld.setCid(cursor.getInt(0));
			fld.setName(cursor.getString(1));
			fld.setType(fieldType2Int(cursor.getString(2)));
			fld.setNotNull(int2boolean(cursor.getInt(3)));
			fld.setDefaultValue(cursor.getString(4));
			fld.setPk(int2boolean(cursor.getInt(5)));
			flds[i] = fld;
			i++;
			//Utils.logD("getTableStructureDef: " + fld.getName());
		}
		return flds;
	}
	
	/**
	 * Convert a field type retrieved by a pragma table_info (tableName)
	 * to a RecordEditorBuilder editor type
	 * @param fieldType
	 * @return
	 */
	private int fieldType2Int(String fieldType) {
		if (fieldType.equalsIgnoreCase("STRING")
				|| fieldType.equalsIgnoreCase("TEXT"))
			return TableField.TYPE_STRING;
		else if (fieldType.equalsIgnoreCase("INTEGER"))
			return TableField.TYPE_INTEGER;
		else if (fieldType.equalsIgnoreCase("REAL") 
				|| fieldType.equalsIgnoreCase("FLOAT")
				|| fieldType.equalsIgnoreCase("DOUBLE"))
			return TableField.TYPE_FLOAT;
		else if (fieldType.equalsIgnoreCase("BOOLEAN")
				|| fieldType.equalsIgnoreCase("BOOL"))
			return TableField.TYPE_BOOLEAN;
		else if (fieldType.equalsIgnoreCase("DATE"))
			return TableField.TYPE_DATE;
		else if (fieldType.equalsIgnoreCase("TIME"))
			return TableField.TYPE_TIME;
		else if (fieldType.equalsIgnoreCase("DATETIME"))
			return TableField.TYPE_DATETIME;
		else if (fieldType.equalsIgnoreCase("PHONENO"))
			return TableField.TYPE_PHONENO;
		else
			return TableField.TYPE_STRING;
	}
	
	/**
	 * Convert the SQLite 0 / 1 boolean to Java boolean 
	 * @param intBool
	 * @return
	 */
	private boolean int2boolean(int intBool) {
		boolean res = false;
		if (intBool == 1)
			res = true;
		return res;
	}
	
	/**
	 * Return the result of the query as a comma separated test in String list
	 * @param sql
	 * @return
	 */
	public String[] getSQLQuery(String sql) {
		testDB();
		String[] tables = {_cont.getText(R.string.NoResult).toString()};
		try {
			Cursor res = _db.rawQuery(sql, null);
			int recs = res.getCount();
			tables = new String[recs];
			int i = 0;
			Utils.logD("Views: " + recs);
			while(res.moveToNext()) {
				for(int j = 0; j < res.getColumnCount(); j++) {
					if (j == 0)
						tables[i] = res.getString(j);
					else 
						tables[i] += ", " + res.getString(j);
				}
				i++;
			}
			res.close();
		} catch (Exception e) {
			tables = new String [] {"Error: " + e.toString()};
		}
		return tables;
	}

	/**
	 * Return a string list with the field names of one ore more tables
	 * @param tables
	 * @return
	 */
	public String[] getTablesFieldsNames(String[] tables) {
		testDB();
		Cursor res;
		List<String> tList = new ArrayList<String>();
		int i = 0;
		for (int j = 0; j < tables.length; j++) {
			String sql = "pragma table_info(" + tables[j] + ")";
			Utils.logD("getTablesFieldsNames: " + sql);
			res = _db.rawQuery(sql, null);
			i = 0;
			// getting field names
			while(res.moveToNext()) {
				tList.add(tables[j] + "." + res.getString(1));
				//fields[i] = res.getString(1);
				i++;
			}
			res.close();
		}
		String[] fieldList = new String[tList.size()];
		i = 0;
		for (String str: tList) {
			fieldList[i] = str;
			i++;
		}
		return fieldList;
	}

	/**
	 * Save a SQL statement in a aSQLiteManager table in the current database.
	 * @param saveSql statement to save
	 */
	public void saveSQL(String saveSql) {
		testDB();
		testHistoryTable();
		String sql = "insert into aSQLiteManager (sql) values (\"" + saveSql +"\")";
		try {
			_db.execSQL(sql);
			Utils.logD("SQL save");
		} catch (SQLException e) {
			// All duplicate SQL ends here
			Utils.logD(e.toString());
		}
	}

	/**
	 * Test for a aSQLiteManager table in the current database. If it does not
	 * exists create it.
	 */
	private void testHistoryTable() {
		testDB();
		Cursor res = _db.rawQuery("select name from sqlite_master where type = \"table\" and name = \"aSQLiteManager\"", null);
		int recs = res.getCount();
		res.close();
		if (recs > 0) {
			return;
		} else {
			// create the aSQLiteManager table
			String sql = "create table aSQLiteManager (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, sql TEXT NOT NULL UNIQUE)";
			_db.execSQL(sql);
			Utils.logD("aSQLiteManager table created");
			saveSQL("delete from aSQLiteManager where 1=1");
			saveSQL("drop table aSQLiteManager");
		}
	}

	/**
	 * Retrieve a number of rows based on a sql query
	 * @param sqlStatement the statement
	 * @param offset number of rows to skip
	 * @param limit max number of rows to retrieve
	 * @return a QueryResult object
	 */
	public QueryResult getSQLQueryPage(String sqlStatement, int offset, int limit) {
		testDB();
		String sql;
		if (sqlStatement.startsWith("select"))
			sql = sqlStatement + " limit " + limit + " offset " + offset;
		else 
			sql = sqlStatement;
		//String[][] res;
		Utils.logD("SQL = " + sql);
		Cursor cursor = null;
		QueryResult nres = new QueryResult();
		try {
			cursor = _db.rawQuery(sql, null);
			int cols = cursor.getColumnCount();
			int rows = cursor.getCount();
			nres.columnNames = cursor.getColumnNames();
			if (rows == 0) {
				nres.Data = new String[1][1];
				//res = new String[1][1];
				//res[0][0] = "No result";
				nres.setColumnNames(new String[] {""});
				nres.Data[0][0] = _cont.getText(R.string.NoResult).toString();
				return nres;
			} else {
				//TOD get column names
				nres.Data = new String[rows][cols];
				//res = new String[rows][cols];
				int i = 0;
				while(cursor.moveToNext()) {
					for (int k=0; k<cols; k++) {
						//res[i][k] = cursor.getString(k);
						nres.Data[i][k] = cursor.getString(k);
					}
					i++;
				}
			}
			return nres;
		} catch (Exception e) {
			nres.setColumnNames(new String[] {_cont.getText(R.string.Error).toString()});
			nres.Data = new String[1][1];
			nres.Data[0][0] = e.toString();			
			//res = new String[1][1];
			//res[0][0] = "Error:\n" + e.toString();
			if (cursor != null)
				cursor.close();
			return nres;
		}
	}

	/**
	 * Return a index definition from its name
	 * @param indexName name of index
	 * @return the sql to create the index
	 */
	public String getIndexDef(String indexName) {
		testDB();
		String res = "";
		String sql;
		sql = "select sql from sqlite_master where type = \"index\" and name = \"" + indexName + "\"";
		Utils.logD("get indexef: "+ sql);
		Cursor cursor = _db.rawQuery(sql, null);
		int rows = cursor.getCount();
		if (rows > 0) {
			while(cursor.moveToNext()) {
				res = cursor.getString(0);
			}
		}
	  cursor.close();
		return res;
	}

	/**
	 * Return a list of recent executed SQL statements from current database
	 * ordered by latest first
	 * @return a String[] with SQL statements
	 */
	public String[] getListOfSQL() {
		testDB();
		// TODO problems if aSQLiteManager don't exists
		String sql = "select * from aSQLiteManager order by _id desc";
		String[] list = null;
		try {
			Cursor res = _db.rawQuery(sql, null);
			int cols = res.getCount();
			list = new String[cols];
			int i = 0;
			// getting field names
			while(res.moveToNext()) {
				String str = new String();
				str = res.getString(1);
				list[i] = str;
				i++;
			}
			res.close();
			return list;
		} catch (Exception e) {
			Utils.logD(e.toString());
			return list;
		}
	}
	
	/**
	 * Backup current database
	 * @return true on success
	 */
	public boolean exportDatabase() {
		testDB();
		String backupName = _dbPath + ".sql";
		File backupFile = new File(backupName);
		FileWriter f;
		BufferedWriter out;
    try {
			f = new FileWriter(backupFile);
			out = new BufferedWriter(f);
			Utils.logD("Exporting to; " + backupFile);
      Utils.logD("-- Database export made by aSQLiteManager");
			out.write("--\n");
      out.write("-- Database export made by aSQLiteManager\n");
			out.write("--\n");
      // export table definitions
      exportTableDefinitions(out);
      // export data
      exportData(out);
      // export index definitions
      exportIndexDefinitions(out);
      // export view definitions
      exportViews(out);
      // export constraints -- how and i which order?

      // export triggers, procedures, ...
      
      //Close the output stream
      out.close();
      f.close();

    } catch (IOException e) {
    	Utils.showException(e.getMessage(), _cont);
    	e.printStackTrace();
    	return false;
	  }
		Utils.logD("Exportet to; " + backupFile.getAbsolutePath());
		// backupFile.
		return true;
	}
	
	/**
	 * Export all data from current database
	 * @param out
	 */
	private void exportData(BufferedWriter out) {
		String sql = "select name from sqlite_master where type = 'table'"; 
		Cursor res = _db.rawQuery(sql, null);
		try {
			while(res.moveToNext()) {
				String tabName = res.getString(0);   //  || tabName.equals("sqlite_sequence") set sequence as it was
				if(!(tabName.equals("sqlite_master") || tabName.equals("android_metadata")
						|| tabName.equals("sqlite_sequence"))) {
					out.write("--\n");
					out.write("-- Exporting data for  " + tabName+ nl);
					out.write("--\n");
					// retrieve table informations
					sql = "PRAGMA table_info (" + tabName + ")";
					Cursor tabInf = _db.rawQuery(sql, null);
					// retrieve data
					sql = "select * from " + res.getString(0);
					Cursor data = _db.rawQuery(sql, null);
					while (data.moveToNext()) {
						// build value list based on result and field types
						String fields = "";
						for(int i = 0; i < data.getColumnCount(); i++) {
							String val = data.getString(i);
							tabInf.moveToPosition(i);
							String type = tabInf.getString(2);
							if (val == null){
								fields += "null";
								if (i != data.getColumnCount()-1)
									fields += ", ";
							} else if (type.equals("INTEGER") || type.equals("REAL")) {
								fields += val;
								if (i != data.getColumnCount()-1)
									fields += ", ";
							} else {  // it must be string or blob(?) so quote it
								fields += "\"" + val + "\"";
								if (i != data.getColumnCount()-1)
									fields += ", ";
							}
						}
						out.write("insert into " + tabName + " values (" + fields + ");" + nl);
					}
				}
			}
		} catch (IOException e) {
			Utils.logE(e.getMessage());
		}
	}

	/**
	 * Export all index definitions from current database
	 * @param out
	 */
	private void exportIndexDefinitions(BufferedWriter out) {
		String sql = "select name, sql from sqlite_master where type = 'index'"; 
		Cursor res = _db.rawQuery(sql, null);
		try {
			while(res.moveToNext()) {
				// for auto index SQL is null
				if (res.getString(1) != null) {
					out.write("--\n");
					out.write("-- Exporting index definitions for " + res.getString(0) + nl);
					out.write("--\n");
					out.write(res.getString(1) + ";" + nl);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Export all view definitions from current database
	 * @param out
	 */
	private void exportViews(BufferedWriter out) {
		String sql = "select name, sql from sqlite_master where type = 'view'"; 
		Cursor res = _db.rawQuery(sql, null);
		try {
			while(res.moveToNext()) {
				out.write("--\n");
				out.write("-- Exporting view definitions for " + res.getString(0) + nl);
				out.write("--\n");
				out.write(res.getString(1) + ";" + nl);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Export all table definitions of current database
	 * @param out
	 */
	private void exportTableDefinitions(BufferedWriter out) {
		String sql = "select name, sql from sqlite_master where type = 'table'"; 
		Cursor res = _db.rawQuery(sql, null);
		try {
			while(res.moveToNext()) {
				String table = res.getString(0);
				if(!(table.equals("sqlite_master") || table.equals("sqlite_sequence") || 
						table.equals("android_metadata"))) {
					out.write("--\n");
					out.write("-- Exporting table definitions for " + table + nl);
					out.write("--\n");
					out.write(res.getString(1) + ";" + nl);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Restore current database from a file of same name but with .sql extension
	 * @return true on success
	 */
	public boolean restoreDatabase() {
		// Just delete all user data and then run the exported file as
		// a script
		testDB();
		String backupName = _dbPath + ".sql";
		File backupFile = new File(backupName);
		if (!backupFile.exists())
			Utils.showMessage(_cont.getText(R.string.Restore).toString(),
					_cont.getText(R.string.NoExportToRestore).toString(), _cont);
		// drop all views
		Utils.logD("Dropping all views");
		dropAllViews();
		// drop all user tables
		Utils.logD("Dropping all tables");
		dropAllTables();
		return runScript(backupFile);
	}
	
	/**
	 * Execute a single line of SQL
	 * 
	 * @param sql the SQL statement to execute
	 */
	public void executeStatement(String sql) {
		Utils.logD("Executing statement:" + sql);
		testDB();
		try {
			_db.execSQL(sql);
		} catch (SQLException e) {
			Utils.showException(e.toString(), _cont);
			e.printStackTrace();
		}
	}

	/**
	 * Run a SQL script from a file
	 * @param scriptFile
	 * @return true upon success
	 */
	public boolean runScript(File scriptFile) {
		testDB();
		FileReader f;
		BufferedReader in;
		String line = "";
    try {
			f = new FileReader(scriptFile);
			in = new BufferedReader(f);
			Utils.logD("Importing from; " + scriptFile);
			String nline = "";
			while ((nline = in.readLine()) != null) {
				line += nline;
				// if more of statement coming append newline
				if (!(line.endsWith(";") || line.equals("")))
					line += nl;
	      if(line.startsWith("--")) {
	        // It a comment just empty line
	      	line = "";
	      } else if(line.endsWith(";")) {
	        // If line ends with ; we have a statement ready to execute
	      	line = line.substring(0, line.length() - 1);
	      	Utils.logD("SQL: " + line);
	      	// execute SQL
	      	_db.execSQL(line);
	      	line = "";
	      }
			}
			in.close();
			f.close();
    } catch (Exception e) {
    	Utils.showException(e.toString(), _cont);
    	return false;
    }
    return true;
	}

	/**
	 * Drop all user tables in current database
	 */
	private void dropAllTables() {
		String sql = "select name, sql from sqlite_master where type = 'table'"; 
		Cursor res = _db.rawQuery(sql, null);
		try {
			while(res.moveToNext()) {
				String table = res.getString(0);
				if(!(table.equals("sqlite_master") || table.equals("sqlite_sequence") || 
						table.equals("android_metadata"))) {
					sql = "drop table " + table;
					_db.execSQL(sql);
				}
			}
		} catch (Exception e) {
			Utils.logE(e.getMessage());
		}
	}

	/**
	 * Drop all views in current database
	 */
	private void dropAllViews() {
		String sql = "select name, sql from sqlite_master where type = 'view'"; 
		Cursor res = _db.rawQuery(sql, null);
		try {
			while(res.moveToNext()) {
				String view = res.getString(0);
				sql = "drop view " + view;
				_db.execSQL(sql);
			}
		} catch (Exception e) {
			Utils.logE(e.getMessage());
		}
	}

	/**
	 * Starts a transaction. Transaction can be nested as the SQLite savepoints
	 * @return true if transaction started
	 */
	public boolean beginTransaction() {
		testDB();
		_db.beginTransaction();
		return _db.inTransaction();
	}
	
	/**
	 * Commit updates back to last begin transaction / savepoint
	 * @return true if still in transaction
	 */
	public boolean commit() {
		testDB();
		_db.setTransactionSuccessful();
		_db.endTransaction();
		return _db.inTransaction();
	}
	
	/**
	 * Roll back updates back to last transaction / savepoint 
	 * @return true if still in transaction
	 */
	public boolean rollback() {
		testDB();
		_db.endTransaction();
		return _db.inTransaction();
	}
	
	/**
	 * Return true i a transaction has not been commitet / rolled back
	 * @return
	 */
	public boolean inTransaction() {
		return _db.inTransaction();
	}

	/**
	 * Export the current query to a file named after the database with the 
	 * extension .export
	 * @param sql The SQL to query
	 */
	public void exportQueryResult(String sql) {
		testDB();
		Cursor data = _db.rawQuery(sql, null);
		String backupName = _dbPath + ".export";
		File backupFile = new File(backupName);
		FileWriter f;
		BufferedWriter out;
		try {
			f = new FileWriter(backupFile);
			out = new BufferedWriter(f);
			while(data.moveToNext()) {
				// write export
				String fields = "";
				for(int i = 0; i < data.getColumnCount(); i++) {
					String val = data.getString(i);
					//tabInf.moveToPosition(i);
					//String type = tabInf.getString(2);
					if (val == null){
						fields += "null";
						if (i != data.getColumnCount()-1)
							fields += "; ";
//					} else if (type.equals("INTEGER") || type.equals("REAL")) {
//						fields += val;
//						if (i != data.getColumnCount()-1)
//							fields += ", ";
					} else {  // it must be string or blob(?) so quote it
						fields += "\"" + val + "\"";
						if (i != data.getColumnCount()-1)
							fields += "; ";
					}
				}
				out.write(fields + nl);
			}
			out.close();
			f.close();
		} catch (Exception e) {
			Utils.logE(e.getMessage());
		}
	}

	/**
	 * @return Return true if the history table aSQLiteManager exists
	 */
	public boolean historyExists() {
		String sql = "select * from aSQLiteManager order by _id desc";
		try {
			Cursor res = _db.rawQuery(sql, null);
			res.close();
			return true;
		} catch (Exception e) {
			Utils.logD(e.toString());
			return false;
		}
	}

	/**
	 * Retrieve a record based on table name and rowid
	 * @param tableName
	 * @param rowId
	 * @return a list of TableFields one for each field the first contains
	 * the rowid for the record
	 */
	public TableField[] getRecord(String tableName, int rowId) {
		String sql = "select rowid as rowid, * from " + tableName + " where rowid = " + rowId;
		Utils.logD(sql);
		// retrieves field types, pk, ... from database
		FieldDescr[] tabledef = getTableStructureDef(tableName);
		Cursor curs = _db.rawQuery(sql, null);
		TableField[] tfs = new TableField[curs.getColumnCount()];
		curs.moveToNext(); 
		int fields = curs.getColumnCount();
		for (int j = 0; j < fields; j++) {
			TableField tf = new TableField();
			tf.setName(curs.getColumnName(j));
			tf.setDisplayName(curs.getColumnName(j));
			// The extra field rowid
			if (j == 0) {
				// Don't allow updating of rowid
				tf.setUpdateable(false);
				tf.setType(TableField.TYPE_INTEGER);
			} else {
				//Utils.logD("QName " + tf.getName());
				//Utils.logD("DName " +  tabledef[j-1].getName());
				tf.setUpdateable(true);
				tf.setType(tabledef[j-1].getType());
				tf.setNotNull(tabledef[j-1].isNotNull());
				tf.setPrimaryKey(tabledef[j-1].isPk());
				tf.setDefaultValue(tabledef[j-1].getDefaultValue());
				Utils.logD("Name - type: " + tf.getName() + " - " + tabledef[j-1].getType());
			}
			tf.setValue(curs.getString(j));
			tfs[j] = tf;
		}
		curs.close();
		return tfs;
	}

	/**
	 * Retrieve a list of TableFields to match a empty record for the
	 * database
	 * @param tableName
	 * @return
	 */
	public TableField[] getEmptyRecord(String tableName) {
		FieldDescr[] fd = getTableStructureDef(tableName);
		TableField[] tfs = new TableField[fd.length];
		for (int i = 0; i < fd.length; i++) {
			TableField tf = new TableField();
			tf.setName(fd[i].getName());
			tf.setType(fd[i].getType());
			tf.setPrimaryKey(fd[i].isPk());
			tf.setUpdateable(true);
			tf.setValue(null);
			tf.setNotNull(fd[i].isNotNull());
			tfs[i] = tf;
		}
		return tfs;
	}
	/**
	 * Update a record in tableName based on it rowId with the fields
	 * in  
	 * @param tableName
	 * @param rowId
	 */
	public void updateRecord(String tableName, int rowId, TableField[] fields) {
		String sql = "update " + tableName + " set ";
		for (TableField fld: fields) {
			if (!fld.getName().equals("rowid")) {
				sql += fld.getName() + " = " + quoteStrings(fld) + ", ";
			}
		}
		sql = sql.substring(0, sql.length() - 2);
		sql += " where rowid = " + rowId;
		Utils.logD("Update SQL = " + sql);
		_db.execSQL(sql);
	}
	
	/**
	 * @param fld
	 * @return
	 */
	private String quoteStrings(TableField fld) {
		boolean quete = true;
		if (fld.getValue() == null || fld.getValue().equals(""))
			return "null";
		switch (fld.getType()) {
		case TableField.TYPE_BOOLEAN:
		case TableField.TYPE_FLOAT:
		case TableField.TYPE_INTEGER:
			quete = false;
			break;
		}
		if (quete)
			return "\"" + fld.getValue()+"\"";
		else
			if (fld.getType() == TableField.TYPE_BOOLEAN)
				if (fld.getValue().equalsIgnoreCase("true"))
					return "1";
				else
					return "0";
			return fld.getValue();
	}

	/**
	 * @param tableName
	 * @param fields
	 */
	public void insertRecord(String tableName, TableField[] fields) {
		String sql = "insert into " + tableName + " (";
		for (TableField fld: fields) {
				sql += fld.getName() + ", ";
		}
		sql = sql.substring(0, sql.length() - 2) + ") values (";
		for (TableField fld: fields) {
			sql += quoteStrings(fld) + ", ";
		}
		sql = sql.substring(0, sql.length() - 2) + ")";
		Utils.logD("Insert SQL = " + sql);
		_db.execSQL(sql);
	}

	/**
	 * Export a single table to a file in the same catalog as the database named
	 * database_tablename.sql
	 * @param tableName
	 * @return true on success
	 */
	public boolean exportTable(String table) {
		Utils.logD("Dumping table: " + table);
		String backupName = _dbPath + "." + table + ".sql";
		File backupFile = new File(backupName);
		FileWriter f;
		BufferedWriter out;
    try {
			f = new FileWriter(backupFile);
			out = new BufferedWriter(f);
			exportSingleTableDefinition(table, f);
			Utils.logD("Def exported");
			exportSingleTableData(table, f);
			Utils.logD("Data exported");

			out.close();
      f.close();
    } catch (IOException e) {
    	Utils.showException(e.getMessage(), _cont);
    	e.printStackTrace();
    	return false;
    }
		return true;
	}

	/**
	 * @param tableName
	 * @param out
	 * @return
	 */
	private boolean exportSingleTableData(String tableName, FileWriter out) {
		String sql = "select name from sqlite_master where type = 'table' and name = '" + tableName + "'"; 
		Cursor res = _db.rawQuery(sql, null);
		try {
			while(res.moveToNext()) {
				String tabName = res.getString(0);   //  || tabName.equals("sqlite_sequence") set sequence as it was
				if(!(tabName.equals("sqlite_master") || tabName.equals("android_metadata")
						|| tabName.equals("sqlite_sequence"))) {
					out.write("--\n");
					out.write("-- Exporting data for  " + tabName+ nl);
					out.write("--\n");
					// retrieve table informations
					sql = "PRAGMA table_info (" + tabName + ")";
					Cursor tabInf = _db.rawQuery(sql, null);
					// retrieve data
					sql = "select * from " + res.getString(0);
					Cursor data = _db.rawQuery(sql, null);
					while (data.moveToNext()) {
						// build value list based on result and field types
						String fields = "";
						for(int i = 0; i < data.getColumnCount(); i++) {
							String val = data.getString(i);
							tabInf.moveToPosition(i);
							String type = tabInf.getString(2);
							if (val == null){
								fields += "null";
								if (i != data.getColumnCount()-1)
									fields += ", ";
							} else if (type.equals("INTEGER") || type.equals("REAL")) {
								fields += val;
								if (i != data.getColumnCount()-1)
									fields += ", ";
							} else {  // it must be string or blob(?) so quote it
								fields += "\"" + val + "\"";
								if (i != data.getColumnCount()-1)
									fields += ", ";
							}
						}
						out.write("insert into " + tabName + " values (" + fields + ");" + nl);
					}
				}
			}
		} catch (IOException e) {
			Utils.logE(e.getMessage());
		}
		
		return false;
	}

	/**
	 * @param tableName
	 * @param out
	 * @return
	 */
	private boolean exportSingleTableDefinition(String tableName, FileWriter out) {
		String sql = "select name, sql from sqlite_master where type = 'table' and name = '" + tableName +"'"; 
		Cursor res = _db.rawQuery(sql, null);
		try {
			while(res.moveToNext()) {
				String table = res.getString(0);
				if(!(table.equals("sqlite_master") || table.equals("sqlite_sequence") || 
						table.equals("android_metadata"))) {
					out.write("--\n");
					out.write("-- Exporting table definitions for " + table + nl);
					out.write("--\n");
					out.write(res.getString(1) + ";" + nl);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}