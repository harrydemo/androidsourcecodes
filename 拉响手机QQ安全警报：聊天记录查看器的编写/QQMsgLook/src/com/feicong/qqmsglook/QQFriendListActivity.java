package com.feicong.qqmsglook;

import com.feicong.DBHelper.DBHelper;
import com.feicong.DBHelper.QQFriendService;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class QQFriendListActivity extends Activity {
    /** Called when the activity is first created. */
	private ListView mlistviewFriend;
	private QQFriendService mService;
	private DBHelper mDB;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_list_layout);
        Intent intent = getIntent();
        String dbName = intent.getStringExtra("qqUin");
		dbName += ".db";
		mlistviewFriend = (ListView)findViewById(R.id.lvfriend_list);
		mDB = new DBHelper(this, dbName, null, 1);
        mService = new QQFriendService(mDB);
        try
		{
			SimpleAdapter adapter = new SimpleAdapter(this,
					mService.setAdapterListData(0, -1), R.layout.friend_list,
					new String[]{
				"memberLevel", "name", "qquin"}, 
				new int[]{
				R.id.uin_memberLevel, R.id.uin_name, R.id.uin_number });
			mlistviewFriend.setAdapter(adapter);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		mlistviewFriend.setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> parent, View v, int location,
					long id)
			{
				try
				{
					Intent intent = new Intent();
					TextView tv = (TextView) v
							.findViewById((int) R.id.uin_number);
					intent.putExtra("friendorTroopUin", tv.getText());
					intent.putExtra("bQQ", true);
					;
					String qqUin = getIntent().getStringExtra("qqUin");
					intent.putExtra("qqUin", qqUin);
					intent.setClass(QQFriendListActivity.this,
							QQMsgActivity.class);
					QQFriendListActivity.this.startActivity(intent);
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