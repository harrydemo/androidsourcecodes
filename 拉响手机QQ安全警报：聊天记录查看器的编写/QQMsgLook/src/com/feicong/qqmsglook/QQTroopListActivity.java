package com.feicong.qqmsglook;

import com.feicong.DBHelper.DBHelper;
import com.feicong.DBHelper.QQTroopService;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class QQTroopListActivity extends Activity {
    /** Called when the activity is first created. */
	private ListView mlistviewTroop;
	private QQTroopService mService;
	private DBHelper mDB;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.troop_list_layout);
        Intent intent = getIntent();
		String dbName = intent.getStringExtra("qqUin");
		dbName += ".db";
		mlistviewTroop = (ListView)findViewById(R.id.lvtroop_list);
        try
		{
			mDB = new DBHelper(this, dbName, null, 1);
	        mService = new QQTroopService(mDB);
			SimpleAdapter adapter = new SimpleAdapter(this,
					mService.setAdapterListData(0, -1), R.layout.troop_list,
					new String[]
					{ "troopName", "troopUin", "troopMemo" }, new int[]
					{ R.id.troop_name, R.id.troop_uin, R.id.troop_memo });
			mlistviewTroop.setAdapter(adapter);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		mlistviewTroop.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> parent, View v, int location,
					long id)
			{
				try
				{
					Intent intent = new Intent();
					TextView tv = (TextView) v
							.findViewById((int) R.id.troop_uin);
					intent.putExtra("friendorTroopUin", tv.getText());
					intent.putExtra("bQQ", false);
					String qqUin = getIntent().getStringExtra("qqUin");
					intent.putExtra("qqUin", qqUin);
					intent.setClass(QQTroopListActivity.this,
							QQMsgActivity.class);
					QQTroopListActivity.this.startActivity(intent);
				} catch (Exception e)
				{
					e.printStackTrace();
				}				
			}			
		});
    }
    
    @Override
	protected void onDestroy()
	{
		mDB.close();
		super.onDestroy();
	}
    
}