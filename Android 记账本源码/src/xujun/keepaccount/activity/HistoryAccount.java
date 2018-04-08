package xujun.keepaccount.activity;

import java.util.ArrayList;

import xujun.keepaccount.R;
import xujun.keepaccount.entity.Account;
import xujun.keepaccount.entity.AccountEnum;
import xujun.keepaccount.sql.DBHelper;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class HistoryAccount extends Activity
{
	public static final String QUERY_HISTORY_ACCOUNT="query_history_account";
	public static final String DELELTE_HISTORY_ACCOUNT="delete_history_account";
	
	private float sumMoney = 0.0f;
	private DBHelper dbHelper;
	private ArrayList<Account> queryAccountList;
	private QueryAccountBroadcastReceiver queryReceiver;
	private DeleteHisAccountBroadcastReceiver deleteReceiver;
	private ListView listView;
	private TextView viewSum;
	
	//记录下上一次的查询周期，以便在删除之后刷新
	private String preStartDate;
	private String preEndDate;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history_account_activity);
		queryReceiver = new QueryAccountBroadcastReceiver();
		dbHelper = new DBHelper(this);
		listView = (ListView) findViewById(R.id.lstHistoryAccount);
		viewSum = (TextView) findViewById(R.id.viewHistorySumMoney);
		IntentFilter queryFilter = new IntentFilter(QUERY_HISTORY_ACCOUNT);
		registerReceiver(queryReceiver, queryFilter);
	}
	@Override
	protected void onResume() {
		super.onResume();
		//在Resume状态下注册一个Broadcast
		IntentFilter filter = new IntentFilter(DELELTE_HISTORY_ACCOUNT);
		deleteReceiver = new DeleteHisAccountBroadcastReceiver();
		registerReceiver(deleteReceiver, filter);
	}
	@Override
	protected void onPause() {
		unregisterReceiver(deleteReceiver);
		super.onPause();
	}
	private void getHistoryAccountList(String startDate,String endDate)
	{
		Log.d("xujun debug", "query history account");
		sumMoney = 0.0f;
		StringBuilder selectBuilder = new StringBuilder();
		selectBuilder.append("select * from ").append(DBHelper.TABLE_NAME).append(" where ")
		.append(DBHelper.COLUMN_DATE).append(" >= ?")
		.append(" and ")
		.append(DBHelper.COLUMN_DATE).append(" <= ?")
		.append(" order by ").append(DBHelper.COLUMN_DATE);
		
		queryAccountList = dbHelper.getQueryAccountList(selectBuilder.toString(), new String[]{startDate,endDate});
		for (Account account:queryAccountList) {
			sumMoney+=account.getMoney();
		}
		
		listView.setAdapter(new ListItemAdapter(this));
		viewSum.setText("总金额: "+sumMoney);
	}
	//提供一个外部Activity触发的入口
	class QueryAccountBroadcastReceiver extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			preStartDate = intent.getCharSequenceExtra("startDate").toString();
			preEndDate = intent.getCharSequenceExtra("endDate").toString();
			getHistoryAccountList(preStartDate,preEndDate);
		}
	}
	class DeleteHisAccountBroadcastReceiver extends BroadcastReceiver
	{

		@Override
		public void onReceive(Context context, Intent intent) {
			StringBuilder builder = new StringBuilder();
			boolean first = true;
			for(int i=0;i<queryAccountList.size();i++)
			{
				Account account = queryAccountList.get(i);
				if(account.isSelected())
				{
					if(!first)
					{
						builder.append(" or ");
					}
					builder.append("account_id = ");
					builder.append(account.getAccountId());
					
					first = false;
				}
			}
			String deleteAccountIdStr = builder.toString();
			//如果为空字符串就是没有选择删除条目
			if(!deleteAccountIdStr.equals(""))
			{
				dbHelper.open();
				dbHelper.delete(deleteAccountIdStr, null);
				dbHelper.close();
				//再次查询，以刷新ListView
				getHistoryAccountList(preStartDate,preEndDate);
			}
			
		}
		
	}
	class ListItemAdapter extends BaseAdapter 
	{
		private LayoutInflater  layoutInflater;

		ListItemAdapter(Context context)
		{
			this.layoutInflater = LayoutInflater.from(context);
		}
		@Override
		public int getCount() {
			return queryAccountList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ListItemView itemView = null;
			if(convertView == null)
			{
				itemView = new ListItemView();
				convertView = layoutInflater.inflate(R.layout.history_account_listview_item, null);
				itemView.image = (ImageView) convertView.findViewById(R.id.imgHistoryItemType);
				itemView.viewDate = (TextView) convertView.findViewById(R.id.viewHistoryItemDate);
				itemView.viewMoney = (TextView)convertView.findViewById(R.id.viewHistoryItemMoney);
				itemView.viewTypeName = (TextView) convertView.findViewById(R.id.viewHistoryItemTypeName);
				itemView.checkBox = (CheckBox) convertView.findViewById(R.id.chkHistoryAccountItem);
				
				itemView.image.setImageResource(AccountEnum.getAccountEnumImage(queryAccountList.get(position).getType()));
				itemView.viewMoney.setText(String.valueOf(queryAccountList.get(position).getMoney()));
				itemView.viewTypeName.setText(queryAccountList.get(position).getType().toString());
				itemView.viewDate.setText(queryAccountList.get(position).getDate());
				itemView.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						queryAccountList.get(position).setSelected(isChecked);
					}});
				convertView.setTag(itemView);
			}
			else
			{
				itemView = (ListItemView) convertView.getTag();
			}
			return convertView;
		}
		class ListItemView 
		{
			public ImageView image;
			public TextView viewDate;
			public TextView viewMoney;
			public TextView viewTypeName;
			public CheckBox checkBox;
		}
	}
}
