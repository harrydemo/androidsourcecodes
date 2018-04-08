package xujun.keepaccount.activity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class TodayAccount extends Activity implements OnItemClickListener
{
	private DBHelper dbHelper;
	private ArrayList<Account> todayAccounts;
	private float sumMoney = 0.0f;
	private DeleteAaccountBroadcastReceiver receiver;
	public static final String ACTION_DELETE_TODAY_ACCOUNT = "xujun.keepaccount.activity.delteTodayAccount";
	private ListView list;
	private TextView viewSumMoney;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.today_account_activity);

		list = (ListView) findViewById(R.id.lstTodayAccount);
		list.setOnItemClickListener(this);
		viewSumMoney = (TextView) findViewById(R.id.viewSumMoney);
		dbHelper = new DBHelper(this);
	}
	//在create－start之后，调用，并且，每次再回到该页面都会被调用，所以，在这里查询数据库
	@Override
	protected void onResume() {
		getTodayAccount();
		//在Resume状态下注册一个Broadcast
		IntentFilter filter = new IntentFilter(ACTION_DELETE_TODAY_ACCOUNT);
		receiver = new DeleteAaccountBroadcastReceiver();
		registerReceiver(receiver, filter);
		
		super.onResume();
	}
	@Override
	public void onPause()
    {
    	unregisterReceiver(receiver);
    	super.onPause();
    }
	private void queryAccount()
	{
//		ArrayList<HashMap<String, Object>> listData = new ArrayList<HashMap<String, Object>>();
//		for(Account account:todayAccounts)
//		{
//			HashMap<String, Object> map = new HashMap<String, Object>();
//			map.put("imgItemType", AccountEnum.getAccountEnumImage(account.getType()) );
//			map.put("viewItemMoney", account.getMoney());
//			listData.add(map);
//		}
		//生成适配器的Item和动态数组对应的元素
//		SimpleAdapter listItemAdapter = new SimpleAdapter(this, listData,// 数据源
//				R.layout.today_account_listview_item,// ListItem的XML实现
//				//动态数组与ImageItem对应的子项
//				new String[] {"imgItemType", "viewItemMoney" },
//				//Item的XML文件里面的一个ImageView,一个TextView ID
//				new int[] {R.id.imgItemType, R.id.viewItemMoney, });
//		list.setAdapter(listItemAdapter);  
	}
	
	private void getTodayAccount()
	{
		Log.d("xujun debug", "query today account");
		//每次查询开始都要清零
		sumMoney = 0.0f;
		Calendar calendar = Calendar.getInstance();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String today = format.format(calendar.getTime());
		
		StringBuilder selectBuilder = new StringBuilder();
		selectBuilder.append("select * from ").append(DBHelper.TABLE_NAME).append(" where ")
		.append(DBHelper.COLUMN_DATE).append(" =?").append(" order by ").append(DBHelper.COLUMN_TYPE);
		
		todayAccounts =  dbHelper.getQueryAccountList(selectBuilder.toString(), new String[]{today});
		for (Account account:todayAccounts) {
			sumMoney+=account.getMoney();
		}
		list.setAdapter(new ListItemAdapter(this));
		viewSumMoney.setText("总金额: "+sumMoney);
	}
	
	
	@Override
	public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
		Log.d("xujun debug", "list item click");
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
			return todayAccounts.size();
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
				convertView = layoutInflater.inflate(R.layout.today_account_listview_item, null);
				itemView.image = (ImageView) convertView.findViewById(R.id.imgItemType);
				itemView.view = (TextView) convertView.findViewById(R.id.viewItemMoney);
				itemView.typeNameView = (TextView)convertView.findViewById(R.id.viewItemTypeName);
				itemView.checkBox = (CheckBox) convertView.findViewById(R.id.chkTodayAccountItem);
				
				itemView.image.setImageResource(AccountEnum.getAccountEnumImage(todayAccounts.get(position).getType()));
				itemView.view.setText(String.valueOf(todayAccounts.get(position).getMoney()));
				itemView.typeNameView.setText(todayAccounts.get(position).getType().toString());
				itemView.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						todayAccounts.get(position).setSelected(isChecked);
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
			public TextView view;
			public TextView typeNameView;
			public CheckBox checkBox;
		}
	}
	
	//提供一个外部Activity触发的入口
	class DeleteAaccountBroadcastReceiver extends BroadcastReceiver
	{
		
		@Override
		public void onReceive(Context context, Intent intent)
		{
			StringBuilder builder = new StringBuilder();
			boolean first = true;
			for(int i=0;i<todayAccounts.size();i++)
			{
				Account account = todayAccounts.get(i);
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
			Log.d("xujun debug", builder.toString());
			//如果为空字符串就是没有选择删除条目
			if(!deleteAccountIdStr.equals(""))
			{
				dbHelper.open();
				dbHelper.delete(deleteAccountIdStr, null);
				dbHelper.close();
				//再次查询，以刷新ListView
				getTodayAccount();
			}
			
		}
	}
	
}

