/**
 * Part of aSQLiteManager (http://sourceforge.net/projects/asqlitemanager/)
 * a a SQLite Manager by andsen (http://sourceforge.net/users/andsen)
 *
 * Query builder and result viewer
 *
 * @author andsen
 *
 */
package dk.andsen.asqlitemanager;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import dk.andsen.types.QueryResult;
import dk.andsen.utils.Utils;

/**
 * @author os
 *
 */
public class QueryViewer extends Activity implements OnClickListener{

	private static final int MENU_TABLES = 0;
	private static final int MENU_FIELDS = 1;
	private static final int MENU_QUERYTYPE = 2;
	private static final int MENU_RESENT_SQL = 3;
	private static final int MENU_TRANSACTION = 4;
	private static final int MENU_EXPORT = 5;
	private static final int TRANSACTION_BEGIN = 0;
	private static final int TRANSACTION_ROLLBACK = 1;
	private static final int TRANSACTION_COMMIT = 2;
	private boolean inTransaction = false;
	private int _transactionLevel = 0;
	private List<String> saveSQL = new ArrayList<String>();
	private static final int QUERYTYPE_SELECT = 0;
	private static final int QUERYTYPE_CREATEVIEW = 1;
	private static final int QUERYTYPE_CREATETABLE = 2;
	private static final int QUERYTYPE_DROPTABLE = 3;
	private static final int QUERYTYPE_DROPVIEW = 4;
	private static final int QUERYTYPE_DELETE = 5;
	private static final int QUERYTYPE_INSERT_INTO = 6;
	private String[] _queryTypes = new String[]
      {"Select", "Create view" ,"Create table", "Drop table", "Drop view", "Delete from", "Insert into"};
	private String[] _transaction = new String[]
      {"Begin transaction", "Rollback" ,"Commit"};
	private EditText _tvQ;
	private Button _btR;
	private TextView _tvTransaction;
	private Context _cont;
	private String _dbPath;
	private Database _db;
	private int _offset = 0;
	private int _limit;
	private TableLayout _aTable;
	private boolean _save;
	private boolean[] listOfTables_selected;
	private String[] listOfTables;
	private String[] listOfSQL;
	private boolean[] listOfFields_selected;
	private String[] listOfFields;
	private Button bUp;
	private Button bDwn;
	private int _queryType = 0;
	boolean _rebuildMenu = false;
	private String _tableDialogString;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.query_viewer);
		_tvQ = (EditText) this.findViewById(R.id.SQLStm);
		_tvTransaction = (TextView)this.findViewById(R.id.Transaction);
		_btR = (Button) this.findViewById(R.id.Run);
		_btR.setOnClickListener(this);
		_cont = _tvQ.getContext();
		_save = Prefs.getSaveSQL(_cont);
		_limit = Prefs.getPageSize(_cont);
		bUp = (Button) this.findViewById(R.id.PgUp);
		bDwn = (Button) this.findViewById(R.id.PgDwn);
		bUp.setOnClickListener(this);
		bDwn.setOnClickListener(this);
		Bundle extras = getIntent().getExtras();
		if(extras !=null)
		{
			_cont = _tvQ.getContext();
			_dbPath = extras.getString("db");
			Utils.logD("Opening database");
			_db = new Database(_dbPath, _cont);
			if (!_db.isDatabase) {
				Utils.logD("Not a database!");
				Utils.showException(_dbPath + " is not a database!", _cont);
				finish();
			} else {
				Utils.logD("Database open");
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	public void onClick(View v) {
		int key = v.getId();
		String sql = _tvQ.getText().toString();
		Utils.logD("Offset: " + _offset);
		Utils.logD("Limit: " + _limit);
		if (!sql.equals(""))
		if (key == R.id.Run) {
			QueryResult result = _db.getSQLQueryPage(sql, _offset, _limit);
			if (_save) {
				// If in transaction store SQL is List for later writing to table
				if(_db.inTransaction()) {
					saveSQL.add(new String(_tvQ.getText().toString()));
					// Also write to database for use during transaction
					_db.saveSQL(_tvQ.getText().toString());
				}
				_db.saveSQL(_tvQ.getText().toString());
				// New SQL -> menu must be rebuild
				_rebuildMenu = true;
			}
			_aTable=(TableLayout)findViewById(R.id.datagrid);
			setTitles(_aTable, result.getColumnNames());
			appendRows(_aTable, result.getData());			
		}  else if (key == R.id.PgDwn) {
			int childs = _aTable.getChildCount();
			Utils.logD("Table childs: " + childs);
			if (childs >= _limit) {  //  No more data on to display - no need to PgDwn
				_offset += _limit;
				String [] nn = {};
				setTitles(_aTable, nn);
				QueryResult result = _db.getSQLQueryPage(sql, _offset, _limit);
				setTitles(_aTable, result.getColumnNames());
				appendRows(_aTable, result.getData());
			}
			Utils.logD("PgDwn:" + _offset);
		} else if (key == R.id.PgUp) {
			_offset -= _limit;
			if (_offset < 0)
				_offset = 0;
			QueryResult result = _db.getSQLQueryPage(sql, _offset, _limit);
			setTitles(_aTable, result.getColumnNames());
			appendRows(_aTable, result.getData());
			Utils.logD("PgUp: " + _offset);
		}
	}

	@Override
	public void onBackPressed() {
		// Disable back key if in transaction
		if(inTransaction) {
			Utils.showMessage("Warning", "In transaction:\nCommit or Rollback before exiting", _cont);
			return;
		}
		super.onBackPressed();
	}

	/**
	 * Add a String[] as titles
	 * @param table
	 * @param titles
	 */
	private void setTitles(TableLayout table, String[] titles) {
		int rowSize=titles.length;
		table.removeAllViews();
		TableRow row = new TableRow(this);
		row.setBackgroundColor(Color.BLUE);
		for(int i=0; i<rowSize; i++){
			TextView c = new TextView(this);
			c.setText(titles[i]);
			c.setPadding(3, 3, 3, 3);
			row.addView(c);
		}
		table.addView(row, new TableLayout.LayoutParams());
	}

	/**
	 * Add a String[][] list to the table layout as rows
	 * @param table
	 * @param data
	 */
	private void appendRows(TableLayout table, String[][] data) {
		if (data == null)
			return;
		int rowSize=data.length;
		int colSize=(data.length>0)?data[0].length:0;
		for(int i=0; i<rowSize; i++){
			TableRow row = new TableRow(this);
			row.setOnClickListener(new OnClickListener() {
			   public void onClick(View v) {
			      // button 1 was clicked!
			  	 Utils.logD("OnClick: " + v.getId());
			   }
			  });

			if (i%2 == 1)
				row.setBackgroundColor(Color.DKGRAY);
			for(int j=0; j<colSize; j++){
				TextView c = new TextView(this);
				c.setText(data[i][j]);
				c.setPadding(3, 3, 3, 3);
				//				if (j%2 == 1)
				//					if (i%2 == 1)
				//						c.setBackgroundColor(Color.BLUE);
				//					else
				//						c.setBackgroundColor(Color.BLUE & Color.GRAY);
				c.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
				      // button 1 was clicked!
				  	 Utils.logD("OnClick: " + v.getId());
				  	 String text = (String)((TextView)v).getText();
				  	 ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
				  	 clipboard.setText(text);
				  	 Utils.toastMsg(_cont, "Text copied to clip board");
				   }
				  });
				row.addView(c);
			}
			table.addView(row, new TableLayout.LayoutParams());
		}
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_TABLES:
			showDialog(MENU_TABLES );
			break;
		case MENU_FIELDS:
			showDialog(MENU_FIELDS );
			break;
		case MENU_QUERYTYPE:
			showDialog(MENU_QUERYTYPE );
			break;
		case MENU_RESENT_SQL:
			showDialog(MENU_RESENT_SQL);
			break;
		case MENU_TRANSACTION:
			showDialog(MENU_TRANSACTION);
			break;
		case MENU_EXPORT:
			// give a message about where the result is written to
			String sql= _tvQ.getText().toString();
			if (_tvQ == null || _tvQ.getText().toString().equals("")) {
				Utils.showMessage(getString(R.string.Error), getString(R.string.ErrorNothingToExport), this);
			} else {
				_db.exportQueryResult(sql);
				Utils.toastMsg(this, getString(R.string.DataExported));
			}
			break;
		}
		return false;
	}

	public boolean onPrepareOptionsMenu(Menu menu) {
		if (_rebuildMenu) {
			Utils.logD("Preparing OptionMenu");
			menu.clear();
			//removeDialog(MENU_TABLES);
			removeDialog(MENU_FIELDS);
			//removeDialog(MENU_QUERYTYPE);
			menu.add(0, MENU_TABLES, 0, R.string.DBTables);
			menu.add(0, MENU_FIELDS, 0, R.string.DBFields);
			menu.add(0, MENU_QUERYTYPE, 0, R.string.DBQueryType);
			menu.add(0, MENU_TRANSACTION, 0, R.string.Transaction);
			menu.add(0, MENU_EXPORT, 0, R.string.ExportData);
			// Only create "Recent SQL menu ef history table exists
			if (_db.historyExists())
				menu.add(0, MENU_RESENT_SQL, 0, R.string.RecentSQL);
			_rebuildMenu = false;
		}
		return true;
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_TABLES, 0, R.string.DBTables);
		menu.add(0, MENU_FIELDS, 0, R.string.DBFields);
		menu.add(0, MENU_QUERYTYPE, 0, R.string.DBQueryType);
		menu.add(0, MENU_TRANSACTION, 0, R.string.Transaction);
		menu.add(0, MENU_EXPORT, 0, R.string.ExportData);
		// Only create "Recent SQL menu ef history table exists
		if (_db.historyExists())
			menu.add(0, MENU_RESENT_SQL, 0, R.string.RecentSQL);
		return true;
	}
	
	@Override
	protected Dialog onCreateDialog(int id) 
	{
		String title = "";
		switch (id) {
		case MENU_TABLES:
			Utils.logD("Creating MENU_TABLES");
			title = getText(R.string.DBTables).toString();
			listOfTables = _db.getTables();
			listOfTables_selected = new boolean[listOfTables.length];
			Dialog test = new AlertDialog.Builder(this)
			.setTitle(title)
			.setMultiChoiceItems(listOfTables, listOfTables_selected, new DialogSelectionClickHandler())
			.setPositiveButton(getText(R.string.OK), new DialogButtonClickHandler())
			.create();
			_tableDialogString = test.toString();
			return test;
		case MENU_FIELDS:
			Utils.logD("Creating MENU_FIELDS");
			title = getText(R.string.DBFields).toString();
			//count selected tables
			int selTables = 0;
			for (boolean sel: listOfTables_selected) {
				if (sel)
				  selTables++;
			}
			String[] tables = new String[selTables];
			selTables = 0;
			for (int i = 0; i < listOfTables.length; i++) {
				if (listOfTables_selected[i]) {
					tables[selTables] = listOfTables[i];
				  selTables++;
				}
			}
			listOfFields = _db.getTablesFieldsNames(tables);
			listOfFields_selected = new boolean[listOfFields.length];
			return new AlertDialog.Builder(this)
			.setTitle(title)
			.setMultiChoiceItems( listOfFields, listOfFields_selected, new DialogSelectionClickHandler())
			.setPositiveButton(getText(R.string.OK), new DialogButtonClickHandler())
			.create();
		case MENU_RESENT_SQL:		
			Utils.logD("Creating MENU_RESENT_SQL");
			listOfSQL = _db.getListOfSQL();
			return new AlertDialog.Builder(this)
			.setTitle(title) 
			.setSingleChoiceItems(listOfSQL, 0, new ResentSQLOnClickHandler() )
			.create();
		case MENU_TRANSACTION:
			Utils.logD("Creating MENU_TRANSACTION");
			return new AlertDialog.Builder(this)
			.setTitle(title)
			.setSingleChoiceItems(_transaction, 0, new TransactionOnClickHandler())
			.create();
		default: //case MENU_QUERYTYPE:
			Utils.logD("Creating MENU_QUERYTYPE");
			//posts = _queryTypes; 
			return new AlertDialog.Builder(this)
			.setTitle(title)
			.setSingleChoiceItems(_queryTypes, 0, new QueryTypeOnClickHandler())
			.create();
		}
	}

	/**
	 * @author os
	 *
	 */
	public class DialogSelectionClickHandler implements DialogInterface.OnMultiChoiceClickListener {
		public void onClick( DialogInterface dialog, int clicked, boolean selected )
		{
			Utils.logD("Dialog: " + dialog.getClass().getSimpleName());
			// Clear selected fields to remove them from the sql
			// but only if it a change in Tables dialog 
			if (dialog.toString().equals(_tableDialogString)) {
				_rebuildMenu = true;
				if (listOfFields_selected != null) {
					for (int i = 0; i < listOfFields_selected.length; i ++)
						listOfFields_selected[i] = false;
				}
			}
		}
	}
	
	/**
	 * @author os
	 *
	 */
	public class DialogButtonClickHandler implements DialogInterface.OnClickListener {
		public void onClick( DialogInterface dialog, int clicked )
		{
			Utils.logD("Dialog: " + dialog.getClass().getName());
			switch(clicked)
			{
			case DialogInterface.BUTTON_POSITIVE:
				String sql = buildSQL();
				_tvQ.setText(sql);
				//printSelectedGrapes();
				break;
			}
		}
	}
	
	/**
	 * Handles the click on the query type dialog
	 * @author andsen
	 *
	 */
	public class QueryTypeOnClickHandler implements DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {
			_queryType = which;
			//Utils.showMessage("qtype", "" + _queryType, _cont);
			dialog.dismiss();
			String sql = buildSQL();
			_tvQ.setText(sql);
		}
	}

	/**
	 * Handles the click on the transaction dialog
	 * @author andsen
	 *
	 */
	public class TransactionOnClickHandler implements DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int id) {
			// Handle transactions
			Utils.logD("Transaction menu clicked");
			switch(id) {
			case TRANSACTION_BEGIN:
				_transactionLevel++;
				_tvTransaction.setText(getText(R.string.InTransaction) + "(" + _transactionLevel + ")");
				_tvTransaction.setVisibility(View.VISIBLE);
				if (!inTransaction && saveSQL != null)
					saveSQL.clear();
				inTransaction = _db.beginTransaction();
				break;
			case TRANSACTION_COMMIT:
				// only commit if in transaction
				if (inTransaction) {
					_transactionLevel--;
					inTransaction = _db.commit();
					if (_transactionLevel == 0) {
						_tvTransaction.setText(getText(R.string.UpdatesCommitted));
					} else {
						_tvTransaction.setText(getText(R.string.UpdatesCommitted) + " "
								+ getText(R.string.InTransaction) + "(" + _transactionLevel + ")");
					}
					// save the SQL executed before rollback 
					if (!inTransaction && saveSQL != null)
						for (String str: saveSQL) {
							_db.saveSQL(str);
						}
				} else {
					_tvTransaction.setVisibility(View.VISIBLE);
					_tvTransaction.setText(getText(R.string.NothingToCommit));
				}
				break;
			case TRANSACTION_ROLLBACK:
				// only roll back if in transaction
				if (inTransaction) {
					_transactionLevel--;
					inTransaction = _db.rollback();
					if (_transactionLevel == 0) {
						_tvTransaction.setText(getText(R.string.UpdatesRolledback));
					} else {
						_tvTransaction.setText(getText(R.string.UpdatesRolledback) + " - "
								+ getText(R.string.InTransaction) + "(" + _transactionLevel + ")");
					}
					// save the SQL executed before rollback 
					if (!inTransaction && saveSQL != null)
						for (String str: saveSQL) {
							_db.saveSQL(str);
						}
				} else {
					_tvTransaction.setVisibility(View.VISIBLE);
					_tvTransaction.setText(getText(R.string.NothingToRolledback));
				}
				break;
			default:
				//Newer reached
				break;
			}
			dialog.dismiss();
		}
	}
	
	/**
	 * Handles the click on the rwsent SQL menu
	 * @author andsen
	 *
	 */
	public class ResentSQLOnClickHandler implements DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {
			_queryType = which;
			//Utils.showMessage("qtype", "" + _queryType, _cont);
			dialog.dismiss();
			_tvQ.setText(listOfSQL[which]);
		}
	}

	
	/**
	 * Build the SQL statement
	 * @return
	 */
	private String buildSQL() {
		String sql = "";
		switch (_queryType) {
		case QUERYTYPE_SELECT:
			sql = buildSelectSQL();
			break;
		case QUERYTYPE_CREATETABLE:
			sql = buildCreateTableSQL();
			break;
		case QUERYTYPE_CREATEVIEW:
			sql = buildCreateViewSQL();
			break;
		case QUERYTYPE_DELETE:
			sql = buildDeleteSQL();
			break;
		case QUERYTYPE_DROPTABLE:
			sql = buildDropTableSQL();
			break;
		case QUERYTYPE_DROPVIEW:
			sql = buildDropViewSQL();
			break;
		case QUERYTYPE_INSERT_INTO:
			sql = buildInsertIntoSQL();
			break;
		default:
			sql = "";
			break;
		}
		return sql;
	}

	/**
	 * Build a Insert into statement
	 * @return the sql
	 */
	private String buildInsertIntoSQL() {
		int noOfSelectedTables = 0;
		if (!(listOfTables != null))
			return "Insert Into TableName (field1, field2)\nValues ('Value1', 'Value2')";
		for (int i= 0; i < listOfTables.length; i++) 
			if (listOfTables_selected[i])
				noOfSelectedTables++;
		if (noOfSelectedTables > 1) {
			Utils.showException("Insert mode only works with one selected table", _cont);
			return "";
		}
		// create the field list with out the user having to open the fields menu
		onCreateDialog(MENU_FIELDS);
		//String sql = "Insert Into TableName (field1, field2) Values ('Value1', 'Value2')";
		String sql = "Insert Into ";
		if(listOfTables != null)
			if (listOfTables.length > 0)
				for (int i= 0; i < listOfTables.length; i++) {
					if (listOfTables_selected[i]) {
						sql += listOfTables[i] + " (";
						break;
					}
				}
		if(listOfFields != null)
			if (listOfFields.length > 0)
				for (int i= 0; i < listOfFields.length; i++) {
						sql += listOfFields[i].substring(listOfFields[i].indexOf(".") + 1) + ", ";
					}
		sql = sql.substring(0, sql.length()-2) + ")\nvalues (";
		if(listOfFields != null)
			if (listOfFields.length > 0)
				for (int i= 0; i < listOfFields.length; i++) {
						sql += "'value" + (i + 1) + "', ";
					}
		sql = sql.substring(0, sql.length()-2) + ")";
		return sql;
	}

	/**
	 * Build a select statement
	 * @return the sql
	 */
	private String buildSelectSQL() {
		int i = 0;
		String sql = "";
		boolean del2chars = false;
		if (listOfFields == null || noSelected(listOfFields_selected) == 0)
			sql = "select * \nfrom ";
		else {
			sql = "select ";
			Utils.logD("List of fields: " + listOfFields.length);
			for (i= 0; i < listOfFields.length; i++) {
				if (listOfFields_selected[i]) {
					Utils.logD("Selected field: " + listOfFields[i]);
					sql += listOfFields[i]+ ", ";
					del2chars = true;
				}
			}
			if (del2chars)
				sql = sql.substring(0, sql.length() - 2);
			sql += "\nfrom ";
		}
		if (listOfTables != null && listOfTables.length > 0) {
			for (i = 0; i < listOfTables.length; i++) {
				if (listOfTables_selected[i]) {
					sql += listOfTables[i] + ", ";
				}
			}
			sql = sql.substring(0, sql.length() - 2);
		}
		return sql;
	}

	/**
	 * find the number of true entries in a list of boolean
	 * @param listOfFieldsSelected
	 * @return
	 */
	private int noSelected(boolean[] listOfFieldsSelected) {
		int res = 0;
		for (int i = 0; i < listOfFieldsSelected.length; i++) {
			if (listOfFieldsSelected[i])
				++res;
		}
		return res;
	}

	/**
	 * Build a drop table statement
	 * @return the sql
	 */
	private String buildDropTableSQL() {
		String sql = "Drop table ";
		// Drop first of the selected tables
		if(listOfTables != null)
			if (listOfTables.length > 0)
				for (int i= 0; i < listOfTables.length; i++) {
					if (listOfTables_selected[i]) {
						sql += listOfTables[i];
						break;
					}
				}
		return sql;
	}

	/**
	 * build a drop view statement
	 * @return the sql statement
	 */
	private String buildDropViewSQL() {
		String sql = "Drop view viewName";
		return sql;
	}

	/**
	 * Build a delete statement 
	 * @return the sql statement
	 */
	private String buildDeleteSQL() {
		String sql = "Delete from  ";
		if(listOfTables != null)
			if (listOfTables.length > 0)
				for (int i= 0; i < listOfTables.length; i++) {
					if (listOfTables_selected[i]) {
						sql += listOfTables[i] + " where ";
						break;
					}
				}
		if(listOfFields != null)
			if (listOfFields.length > 0)
				for (int i= 0; i < listOfFields.length; i++) {
					if (listOfFields_selected[i]) {
						sql += listOfFields[i]+ " = 'xxx'";
						break;
					}
				}
		return sql;
	}

	/**
	 * Buil a create view statement
	 * @return the sql statement
	 */
	private String buildCreateViewSQL() {
		String sql = "Create view ViewName as \n";
		sql += buildSelectSQL();
		return sql;
	}

	/**
	 * Build a create table statement
	 * @return the sql statement
	 */
	private String buildCreateTableSQL() {
		String sql = "Create table TableName (feild1 f1type, feild2 f2type)";
		return sql;
	}

}
