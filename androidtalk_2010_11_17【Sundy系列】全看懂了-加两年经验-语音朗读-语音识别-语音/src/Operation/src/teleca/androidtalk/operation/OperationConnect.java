/**
 * @Andrew OperationConnect
 * @description A simple factory patterns of operation classed .
 * @version 1.0
 * @author Andrew
 * @date 2010-11-5
 */
package teleca.androidtalk.operation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

public class OperationConnect extends OperationBase implements IOperation {
	
	
	private String HTTPPROTO = "http://"; //proto name
	private Context myContext = null;     //current activity context
	
	public OpResult doOperation(Context context , String data) {
		myContext = context;
		String urlFromString = getUrlFromBaseData(data);
		String urlFromKey = getUrlByName(data);
		String url="";
		if (urlFromKey != null) {
			url = urlFromKey;
		} else {
			url = urlFromString;
		}
		openUrl(url);
		return null;
	}

//	private boolean customerAddrDefine(String key, String addr) {
//		if (key != null && addr != null) {
//			SharedPreferences keyTable = myContext.getSharedPreferences("keyTable", 0);
//			keyTable.edit().putString(key, addr).commit();
//			return true;
//		}
//		return false;
//	}

	private String getUrlByName(String name) {
		//To find if the name exist in the keyTable,if exist , get the url address
		SharedPreferences keyTable = myContext.getSharedPreferences("keyTable", 0);
		return keyTable.getString(name, null);
	}

	private String getUrlFromBaseData(String data) {
		
		//To make up a url from Base argruments
		data = data.replace(" ", ".");
		String formatUrlString = HTTPPROTO;
		if (!data.startsWith(HTTPPROTO)) {
			formatUrlString += data;
		} else {
			formatUrlString = data;
		}
		String url = getUrlByName(data);
		if (url != null) {
			formatUrlString = "";
			if (!url.startsWith(HTTPPROTO))
				formatUrlString = HTTPPROTO;
			formatUrlString += url;
		}
		return formatUrlString;
	}

	private boolean openUrl(String url) {
		//To Start webClient to open a url
		if (url != null) {
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(url));
			myContext.startActivity(i);
			return true;
		}
		return false;
	}
}