/**
 * Part of aSQLiteManager (http://sourceforge.net/projects/asqlitemanager/)
 * a a SQLite Manager by andsen (http://sourceforge.net/users/andsen)
 *
 * Not used
 *
 * @author andsen
 *
 */
package dk.andsen.asqlitemanager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import dk.andsen.utils.Utils;

public class DataGrid extends Activity  {
	private String _dbPath;
	private String _table;
	private Context _cont;
	private Database _db = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.datagrid);
		TableLayout aTable=(TableLayout)findViewById(R.id.datagrid);
		Bundle extras = getIntent().getExtras();
		if(extras ==null)
		{
			//throw Exception NoArgumentSupplied; 
		} else {
			_dbPath = extras.getString("db");
			Utils.logD("Opening database");
			_table = extras.getString("Table");
			_db = new Database(_dbPath, _cont);
			Utils.logD("Database open");
			String [] fieldNames = _db.getFieldsNames(_table);
			appendTitles(aTable, fieldNames);
			String [][] data = _db.getTableData(_table, 0, 20, false);
			appendRows(aTable, data);
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
	
	private void appendRows(TableLayout table, String[][] data) {
		int rowSize=data.length;
		int colSize=(data.length>0)?data[0].length:0;
		for(int i=0; i<rowSize; i++){
			TableRow row = new TableRow(this);
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
				row.addView(c);
			}
			table.addView(row, new TableLayout.LayoutParams());
		}
	}

	private void appendTitles(TableLayout table, String[] amortization) {
		int rowSize=amortization.length;
		TableRow row = new TableRow(this);
		row.setBackgroundColor(Color.BLUE);
		for(int i=0; i<rowSize; i++){
				TextView c = new TextView(this);
				c.setText(amortization[i]);
				c.setPadding(3, 3, 3, 3);
				row.addView(c);
		}
		table.addView(row, new TableLayout.LayoutParams());
	}
}
