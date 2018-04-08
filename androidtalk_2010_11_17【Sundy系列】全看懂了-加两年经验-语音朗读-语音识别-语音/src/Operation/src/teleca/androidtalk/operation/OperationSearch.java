/**
 * @Andrew OperationSearch
 * @description A simple factory patterns of operation classed .
 * @version 1.0
 * @author Andrew
 * @date 2010-11-5
 */

package teleca.androidtalk.operation;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;

public class OperationSearch extends OperationBase implements IOperation {
	private Context myContext;  //current activity context
	public OpResult doOperation(Context context , String data) {
		myContext = context;
		beginSearch(data);
		return null;
	}
	private boolean beginSearch(String key) {
		//To Start A Web Search
		if (key != null) {
			Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
			intent.putExtra(SearchManager.QUERY, key);
			myContext.startActivity(intent);
			return true;
		}
		return false;
	}
}
