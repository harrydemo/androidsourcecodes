package xujun.keepaccount.activity;

import java.util.ArrayList;
import java.util.HashMap;

import xujun.keepaccount.Main;
import xujun.keepaccount.R;
import xujun.keepaccount.entity.AccountEnum;
import xujun.keepaccount.sql.DBHelper;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class TypeAccountList extends Activity implements OnClickListener
{
	private ListView list;
	private TextView viewSum;
	private ImageButton btnBack;
	
	private ArrayList<HashMap<String, Object>> adapterData;
	private String startDate,endDate;
	private int type;
	private float sum = 0.0f;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.type_account_list_activity);
		list = (ListView) findViewById(R.id.lstTypeAccount);
		viewSum = (TextView) findViewById(R.id.viewTypeAccountSumMoney);
		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		
		Bundle bundle = getIntent().getExtras();
		startDate = bundle.getString("startDate");
		endDate = bundle.getString("endDate");
		type = bundle.getInt("type");
		getData();
		//生成适配器的Item和动态数组对应的元素
		SimpleAdapter listItemAdapter = new SimpleAdapter(this, adapterData,// 数据源
				R.layout.type_account_list_item,// ListItem的XML实现
				//动态数组与ImageItem对应的子项
				new String[] {"date", "type","money" },
				//Item的XML文件里面的一个ImageView,一个TextView ID
				new int[] {R.id.viewTypeAccountDate, R.id.viewTypeAccountName,R.id.viewTypeAccountMoeny });
		list.setAdapter(listItemAdapter);  
		viewSum.setText("总金额: "+sum);
	}
	private void getData()
	{
		sum=0.0f;
		adapterData = new ArrayList<HashMap<String, Object>>();
		StringBuilder selectBuilder = new StringBuilder();
		selectBuilder.append("select account_date,account_type,account_money from ").append(DBHelper.TABLE_NAME).append(" where ")
		.append(DBHelper.COLUMN_DATE).append(" >= ?")
		.append(" and ")
		.append(DBHelper.COLUMN_DATE).append(" <= ?")
		.append(" and ")
		.append(DBHelper.COLUMN_TYPE).append(" = ?")
		.append(" order by ").append(DBHelper.COLUMN_DATE);
		
		DBHelper dbHelper = new DBHelper(this);
		dbHelper.open();
		Cursor cursor = null;
		try {
			cursor = dbHelper.query(selectBuilder.toString(), new String[]{startDate,endDate,""+type});
			while (cursor.moveToNext()) 
			{
				HashMap<String, Object> record = new HashMap<String, Object>();
				record.put("date", cursor.getString(0));
				record.put("type", AccountEnum.getAccountEnum(cursor.getInt(1)).toString());
				record.put("money", cursor.getFloat(2));
				adapterData.add(record);
				sum+=cursor.getFloat(2);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally
		{
			if(cursor != null && !cursor.isClosed())
			{
				cursor.close();
			}
			dbHelper.close();
		}
		
	}
	@Override
	public void onClick(View v) {
		//等于按下了返回按钮
		this.onBackPressed();
	}
}
