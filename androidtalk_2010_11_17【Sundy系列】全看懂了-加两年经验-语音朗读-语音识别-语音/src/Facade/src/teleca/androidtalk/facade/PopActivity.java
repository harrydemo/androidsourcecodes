/**
 * @description : Activity that show installedApps ArrayList .
 * @version 1.0
 * @author Alex
 * @date 2010-11-10
 */
package teleca.androidtalk.facade;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class PopActivity extends Activity  
{
	private ListView lv;
	static ArrayList<String> res = new ArrayList<String>();
	private ArrayAdapter<String> adapter;
	String itemName = "";
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pop);
		lv = (ListView) findViewById(R.id.lv);
		lv.setOnItemClickListener(new OnItemClickListener() 
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,long arg3)
			{
				itemName = (String) lv.getItemAtPosition(pos);
				 Bundle b = new Bundle();
				Log.v("TAG", itemName);
				Intent intent = new Intent();
				 b.putString("Success", itemName);
				intent.putExtras(b);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		getInstalledApps(this);
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_single_choice, res);
		lv.setAdapter(adapter);
		lv.setItemsCanFocus(true);
		lv.setSelection(1);
		lv.setScrollContainer(true);
	}
	/**
	 *  get Installed Apps
	 *  
	 * @param Activity
	 * @return 
	 */

	public static void getInstalledApps(Activity activity) 
	{

		List<PackageInfo> packs = activity.getPackageManager().getInstalledPackages(0);
		for (int i = 0; i < packs.size(); i++) 
		{
			PackageInfo p = packs.get(i);
			String appname = p.applicationInfo.loadLabel(
					activity.getPackageManager()).toString();
			res.add(appname);
			Log.v("TAG", appname);
		}
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
	}
}
