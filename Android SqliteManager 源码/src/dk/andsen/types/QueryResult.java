/**
 * Part of aSQLiteManager (http://sourceforge.net/projects/asqlitemanager/)
 * a a SQLite Manager by andsen (http://sourceforge.net/users/andsen)
 *
 * @author andsen
 *
 */
package dk.andsen.types;

public class QueryResult {
	public String[] columnNames;
	public String [][] Data;

	public String[] getColumnNames() {
		return columnNames;
	}
	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}
	public String[][] getData() {
		return Data;
	}
	public void setData(String[][] data) {
		Data = data;
	}
}
