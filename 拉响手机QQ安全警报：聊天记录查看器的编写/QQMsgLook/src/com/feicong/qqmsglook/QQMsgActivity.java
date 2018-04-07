package com.feicong.qqmsglook;

import com.feicong.DBHelper.DBHelper;
import com.feicong.DBHelper.PageUtil;
import com.feicong.DBHelper.QQMessageService;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class QQMsgActivity extends Activity implements OnClickListener{
    /** Called when the activity is first created. */
	private DBHelper mDB;
	private QQMessageService mService;
	private ListView listviewmsg;
	private Button buttonFirst;
	private Button buttonPrev;
	private Button buttonNext;
	private Button buttonEnd;
	private TextView pageInfo;
	private PageUtil pageUtil;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.msg_layout);
        listviewmsg = (ListView)findViewById(R.id.lvmsg);
        try	{
            Intent intent = getIntent();
            String qqUin = intent.getStringExtra("qqUin");
    		String dbName =qqUin + ".db";
    		String friendroTroopUin = intent.getStringExtra("friendorTroopUin");
    		Boolean bQQ = intent.getBooleanExtra("bQQ", true);
			mDB = new DBHelper(this, dbName, null, 1);
			mService = new QQMessageService(mDB, friendroTroopUin, bQQ);
            pageUtil = new PageUtil(mService);
			SimpleAdapter adapter = new SimpleAdapter(this,
					mService.setAdapterListData(0, PageUtil.pagesize),
					R.layout.msg, new String[]
					{ "uin", "msg" }, new int[]
					{ R.id.uin, R.id.uin_msg });			
			listviewmsg.setAdapter(adapter);
		} catch (Exception e){
			e.printStackTrace();
		}
		buttonFirst = (Button) findViewById(R.id.first);
        buttonFirst.setOnClickListener(this);
        buttonPrev = (Button) findViewById(R.id.prev);
        buttonPrev.setOnClickListener(this);
        buttonPrev.setEnabled(false);
        buttonNext = (Button) findViewById(R.id.next);
        buttonNext.setOnClickListener(this);
        buttonEnd = (Button) findViewById(R.id.end);
        buttonEnd.setOnClickListener(this);
        pageInfo = (TextView) findViewById(R.id.pageInfo);
    	int index = 1;
        try	{
        	int pageCount = pageUtil.getPagecount();
        	if ((pageCount == 0) || (pageCount == 1)){        		
        		buttonFirst.setEnabled(false);	
        		buttonPrev.setEnabled(false);			
				buttonNext.setEnabled(false);
				buttonEnd.setEnabled(false);
				if (pageCount == 0) {
	        		index = 0;
				}
        	}
			pageInfo.setText("(" + String.valueOf(index) + "/" + pageCount + ")");
		} catch (Exception e){
			buttonFirst.setEnabled(false);
			buttonPrev.setEnabled(false);
			buttonNext.setEnabled(false);
			buttonEnd.setEnabled(false);
			pageInfo.setText("(0/0)");
		}
    }
    
	@Override
	protected void onDestroy(){
		mDB.close();
		super.onDestroy();
	}

	public void onClick(View v){
		Button button = (Button) v;
		SimpleAdapter adapter = null;
		switch (button.getId()) {
			case R.id.first:
				pageUtil.firstPage();
				break;
			case R.id.prev:
				pageUtil.prevPage();
				break;
			case R.id.next:
				pageUtil.nextPage();
				break;
			case R.id.end:
				pageUtil.endPage();
				break;
		}
		try
		{
			adapter = new SimpleAdapter(this, 
					pageUtil.getList(), 
					R.layout.msg,
					new String[]{ "uin", "msg" },
					new int[]{ R.id.uin, R.id.uin_msg });
			listviewmsg.setAdapter(adapter);
			pageInfo.setText("(" + pageUtil.getCurrentpage() + "/" + pageUtil.getPagecount() + ")");		
			if (pageUtil.getCurrentpage() == 1 && pageUtil.getPagecount() != 0) { //Ê×Ò³
				buttonPrev.setEnabled(false);
				buttonNext.setEnabled(true);
			} else if (pageUtil.getCurrentpage() == pageUtil.getPagecount() 
					&& pageUtil.getPagecount() != 0) {  //Î²Ò³
				buttonNext.setEnabled(false);
				buttonPrev.setEnabled(true);
			} else if ((pageUtil.getPagecount() == 0) || (pageUtil.getPagecount() == 1)) {
				buttonFirst.setEnabled(false);
				buttonPrev.setEnabled(false);
				buttonNext.setEnabled(false);
				buttonEnd.setEnabled(false);
			} else {
				buttonPrev.setEnabled(true);
				buttonNext.setEnabled(true);
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}