/**
 * Part of aSQLiteManager (http://sourceforge.net/projects/asqlitemanager/)
 * a a SQLite Manager by andsen (http://sourceforge.net/users/andsen)
 *
 * Show informations and data for tables and views
 *
 * @author andsen
 *
 */

/*
 * Use SQL like this
 * SELECT rowid as rowid, * FROM programs
 * to get unique id for each record this might be a primary key but only
 * if this is a single field
 */
package dk.andsen.asqlitemanager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import dk.andsen.RecordEditor.RecordEditorBuilder;
import dk.andsen.RecordEditor.types.TableField;
import dk.andsen.types.Types;
import dk.andsen.utils.Utils;
public class TableViewer extends Activity implements OnClickListener {
	private String _dbPath;
	private Database _db = null;
	private String _table;
	Context _cont;
	//private String _type = "Fields";
	private TableLayout _aTable;
	private int offset = 0;
	private int limit = 15;
	private boolean _updateTable;
	private Button bUp;
	private Button bDwn;
	private int sourceType;
	private static final int MENU_DUMP_TABLE = 0;

	/*
	 * What is needed to allow editing form  table viewer 
	 * 
	 * When displaying records
	 * select rowid, t.* form table as t
	 * 
	 * to include the sqlite rowid
	 * 
	 * But only if a single field primary key does not exists
	 * If there does only
	 * select * from table
	 * 
	 * Then it is possible to update ... where rowid = x
	 * 
	 */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.table_viewer);
		TextView tvDB = (TextView)this.findViewById(R.id.TableToView);
		Button bTab = (Button) this.findViewById(R.id.Fields);
		Button bVie = (Button) this.findViewById(R.id.Data);
		Button sVie = (Button) this.findViewById(R.id.SQL);
		bUp = (Button) this.findViewById(R.id.PgUp);
		bDwn = (Button) this.findViewById(R.id.PgDwn);
		bUp.setOnClickListener(this);
		bDwn.setOnClickListener(this);
		bUp.setVisibility(View.GONE);
		bDwn.setVisibility(View.GONE);
		_cont = this;
		limit = Prefs.getPageSize(this);
		bTab.setOnClickListener(this);
		bVie.setOnClickListener(this);
		sVie.setOnClickListener(this);
		Bundle extras = getIntent().getExtras();
		if(extras !=null)
		{
			_cont = tvDB.getContext();
			sourceType = extras.getInt("type");
			_dbPath = extras.getString("db");
			Utils.logD("Opening database");
			_table = extras.getString("Table");
			if (sourceType == Types.TABLE)
				tvDB.setText(getString(R.string.DBTable) + " " + _table);
			else if (sourceType == Types.VIEW)
				tvDB.setText(getString(R.string.DBView) + " " + _table);
			_db = new Database(_dbPath, _cont);
			Utils.logD("Database open");
			onClick(bTab);
		}
	}

	@Override
	protected void onPause() {
		_db.close();
		super.onPause();
	}

	@Override
	protected void onRestart() {
		_db = new Database(_dbPath, _cont);
		super.onRestart();
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	public void onClick(View v) {
		int key = v.getId();
		_aTable=(TableLayout)findViewById(R.id.datagrid);
		boolean isView = false;
		if (sourceType == Types.VIEW)
			isView = true;

		if (key == R.id.Fields) {
			offset = 0;
			String[] fieldNames = _db.getTableStructureHeadings(_table);
			setTitles(_aTable, fieldNames, false);
			String [][] data = _db.getTableStructure(_table);
			updateButtons(false);
			appendRows(_aTable, data, false);
		} else if (key == R.id.Data) {
			/*
			 * If not a query include rowid in data if no single field
			 * primary key exists
			 */
			//list.
			offset = 0;
			String [] fieldNames = _db.getFieldsNames(_table);
			setTitles(_aTable, fieldNames, !isView);
			String [][] data = _db.getTableData(_table, offset, limit, isView);
			updateButtons(true);
			appendRows(_aTable, data, !isView);
		} else if (key == R.id.SQL) {
			offset = 0;
			String [] fieldNames = {"SQL"};
			setTitles(_aTable, fieldNames, false);
			String [][] data = _db.getSQL(_table);
			updateButtons(false);
			appendRows(_aTable, data, false);
		} else if (key == R.id.PgDwn) {
			//TODO copy methods from .Data to solve paging problems for views
			int childs = _aTable.getChildCount();
			Utils.logD("Table childs: " + childs);
			if (childs >= limit) {  //  No more data on to display - no need to PgDwn
				offset += limit;
				String [] fieldNames = _db.getFieldsNames(_table);
				setTitles(_aTable, fieldNames, !isView);
				String [][] data = _db.getTableData(_table, offset, limit, isView);
				appendRows(_aTable, data, !isView);
			}
			Utils.logD("PgDwn:" + offset);
		} else if (key == R.id.PgUp) {
			//TODO copy methods from .Data to solve paging problems for views
			offset -= limit;
			if (offset < 0)
				offset = 0;
			String [] fieldNames = _db.getFieldsNames(_table);
			setTitles(_aTable, fieldNames, !isView);
			String [][] data = _db.getTableData(_table, offset, limit, isView);
			appendRows(_aTable, data, !isView);
			Utils.logD("PgUp: " + offset);
		}
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (_updateTable) {
			String [] fieldNames = _db.getFieldsNames(_table);
			setTitles(_aTable, fieldNames, true);
			boolean isView = false;
			if (sourceType == Types.VIEW)
				isView = true;

			String [][] data = _db.getTableData(_table, offset, limit, isView);
			updateButtons(true);
			appendRows(_aTable, data, true);
		}
	}

	/**
	 * If paging = true show paging buttons otherwise not
	 * @param paging
	 */
	private void updateButtons(boolean paging) {
		if (paging) {
			bUp.setVisibility(View.VISIBLE);
			bDwn.setVisibility(View.VISIBLE);
		} else {
			bUp.setVisibility(View.GONE);
			bDwn.setVisibility(View.GONE);
		}
	}

	/**
	 * Add a row to the table
	 * @param table
	 * @param data
	 */
	private void appendRows(TableLayout table, String[][] data, boolean allowEdit) {
		int rowSize=data.length;
		int colSize=(data.length>0)?data[0].length:0;
		for(int i=0; i<rowSize; i++){
			TableRow row = new TableRow(this);
			row.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					// Edit button was clicked!
					Utils.logD("OnClick: " + v.getId());
				}
			});

			if (i%2 == 1)
				row.setBackgroundColor(Color.DKGRAY);
			// TODO change rows to ConvertView
			// Adding all columns as TextView's should be changed to a ConvertView
			// as described here:
			// http://android-er.blogspot.com/2010/06/using-convertview-in-getview-to-make.html
			// or in android41cv dk.andsen.utils.MyArrayAdapter

			for(int j=0; j<colSize; j++){
				if (j==0 && allowEdit) {
					
					TextView c = new TextView(this);
					// TODO use this ?  c.setTextColor(StateColorList);
					//c.setBackgroundColor(R.color.yellow);
					c.setText("Edit");
					//c.setTextColor(R.color.yellow);
					int id = new Integer(data[i][j]).intValue();
					c.setId(id);
					c.setPadding(3, 3, 3, 3);
					// TODO More efficient to make one OnClickListener and assign this to all records edit field?
					c.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							final RecordEditorBuilder re;
							final int rowid = v.getId();
							Utils.logD("Ready to edit rowid " +v.getId() + " in table " + _table);
							TableField[] rec = _db.getRecord(_table, rowid);
							final Dialog dial = new Dialog(_cont);
							dial.setContentView(R.layout.line_editor);
							dial.setTitle("Edit row " + rowid);
							LinearLayout ll = (LinearLayout)dial.findViewById(R.id.LineEditor);
							re = new RecordEditorBuilder(rec, _cont);
							re.setFieldNameWidth(200);
							re.setTreatEmptyFieldsAsNull(true);
							final ScrollView sv = re.getScrollView();
							final Button btnOK = new Button(_cont);
							btnOK.setText("OK");
							btnOK.setLayoutParams(new LinearLayout.LayoutParams(
									LinearLayout.LayoutParams.FILL_PARENT,
									LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
							btnOK.setOnClickListener(new OnClickListener() {
								public void onClick(View v) {
									if (v == btnOK) {
										String msg = re.checkInput(sv); 
										if (msg == null) {
											//Utils.logD("Record edited; " + rowid);
											TableField[] res = re.getEditedData(sv);
											_db.updateRecord(_table, rowid, res);
											dial.dismiss();
											_updateTable = true;
										}
										else
											Utils.showException(msg, sv.getContext());
									} 
								}
							});
							final Button btnCancel = new Button(_cont);
							btnCancel.setText("Cancel");
							btnCancel.setLayoutParams(new LinearLayout.LayoutParams(
									LinearLayout.LayoutParams.FILL_PARENT,
									LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
							btnCancel.setOnClickListener(new OnClickListener() {
								public void onClick(View v) {
									if (v == btnCancel) {
										dial.dismiss();
									}
								}
							});
							LinearLayout llButtons = new LinearLayout(_cont);
							llButtons.setOrientation(LinearLayout.HORIZONTAL);
							llButtons.setLayoutParams(new LinearLayout.LayoutParams(
									LinearLayout.LayoutParams.FILL_PARENT,
									LinearLayout.LayoutParams.WRAP_CONTENT));
							llButtons.addView(btnOK);
							llButtons.addView(btnCancel);
							ll.addView(llButtons);
							ll.addView(sv);
							dial.show();
						}
					});
					row.addView(c);
				} else {
					TextView c = new TextView(this);
					c.setText(data[i][j]);
					c.setPadding(3, 3, 3, 3);
					c.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							String text = (String)((TextView)v).getText();
							ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
							clipboard.setText(text);
							Utils.toastMsg(_cont, "Text copied to clip board");
						}
					});
					row.addView(c);
				}

			}
			table.addView(row, new TableLayout.LayoutParams());
		}
	}

	/**
	 * Add titles to the columns
	 * @param table
	 * @param titles
	 */
	private void setTitles(TableLayout table, String[] titles, boolean allowEdit) {
		int rowSize=titles.length;
		table.removeAllViews();
		TableRow row = new TableRow(this);
		row.setBackgroundColor(Color.BLUE);
		if (allowEdit) {
			TextView c = new TextView(this);
			c.setText("New");
			c.setPadding(3, 3, 3, 3);
			c.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					final RecordEditorBuilder re;
					TableField[] rec = _db.getEmptyRecord(_table);
					final Dialog dial = new Dialog(_cont);
					dial.setContentView(R.layout.line_editor);
					dial.setTitle("Insert new row");
					LinearLayout ll = (LinearLayout)dial.findViewById(R.id.LineEditor);
					re = new RecordEditorBuilder(rec, _cont);
					re.setFieldNameWidth(200);
					re.setTreatEmptyFieldsAsNull(true);
					final ScrollView sv = re.getScrollView();
					final Button btnOK = new Button(_cont);
					btnOK.setText("OK");
					btnOK.setLayoutParams(new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.FILL_PARENT,
							LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
					btnOK.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							if (v == btnOK) {
								String msg = re.checkInput(sv); 
								if (msg == null) {
									//Utils.logD("Record edited; " + rowid);
									TableField[] res = re.getEditedData(sv);
									_db.insertRecord(_table, res);
									dial.dismiss();
									_updateTable = true;
								}
								else
									Utils.showException(msg, sv.getContext());
							} 
						}
					});
					final Button btnCancel = new Button(_cont);
					btnCancel.setText("Cancel");
					btnCancel.setLayoutParams(new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.FILL_PARENT,
							LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
					btnCancel.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							if (v == btnCancel) {
								dial.dismiss();
							}
						}
					});
					LinearLayout llButtons = new LinearLayout(_cont);
					llButtons.setOrientation(LinearLayout.HORIZONTAL);
					llButtons.setLayoutParams(new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.FILL_PARENT,
							LinearLayout.LayoutParams.WRAP_CONTENT));
					llButtons.addView(btnOK);
					llButtons.addView(btnCancel);
					ll.addView(llButtons);
					ll.addView(sv);
					dial.show();
				}
			});
			row.addView(c);
		}
		for(int i=0; i<rowSize; i++){
			//TODO add onClickListener to sort based on the column
			TextView c = new TextView(this);
			c.setText(titles[i]);
			c.setPadding(3, 3, 3, 3);
			row.addView(c);
		}
		table.addView(row, new TableLayout.LayoutParams());
	}

	/*
	 *  Creates the menu items
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_DUMP_TABLE, 0, R.string.DumpTable);
		return true;
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
    case MENU_DUMP_TABLE:
    	// Dump table to .sql file
    	if (_db.exportTable(_table)) {
    		Utils.toastMsg(this, String.format(this.getString(R.string.TableDumpep) , _table));
      	return true;
    	}
    	else
    	  Utils.toastMsg(this, this.getString(R.string.DumpFailed));
		}
		return false;
	}
	
	
}