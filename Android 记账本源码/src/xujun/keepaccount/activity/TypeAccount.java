package xujun.keepaccount.activity;

import xujun.keepaccount.R;
import xujun.keepaccount.dialog.DateSelectorDialog;
import xujun.keepaccount.entity.AccountEnum;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class TypeAccount extends Activity implements OnItemClickListener
{
	private static final int REQUEST_QUERYDATE = 2;
	private AccountEnum[] enums;
	private GridView gridView;
	private AccountEnum curSelEnum;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.type_account_activity);
		initEnums();
		gridView = (GridView) findViewById(R.id.typeAccount_grid);
		gridView.setAdapter(new GridItemAdapter(this));
		gridView.setNumColumns(3);
		gridView.setOnItemClickListener(this);
	}
	@Override
	public void onItemClick(AdapterView<?> gridView, View gridItemView, int position, long row) {
		curSelEnum = enums[position];
		//弹出时间选择对话框
		Intent intent = new Intent(this,DateSelectorDialog.class);
		startActivityForResult(intent, REQUEST_QUERYDATE);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUEST_QUERYDATE)
		{
			if(resultCode == RESULT_OK)
			{
				Intent intent = new Intent(this,TypeAccountList.class);
				Bundle bundle = new Bundle();
				bundle.putCharSequence("startDate", data.getCharSequenceExtra("startDate"));
				bundle.putCharSequence("endDate", data.getCharSequenceExtra("endDate"));
				bundle.putInt("type", curSelEnum.getValue());
				intent.putExtras(bundle);
				startActivity(intent);
			}
		}
	}
	
	private void initEnums()
	{
		enums = new AccountEnum[]{AccountEnum.Daily,AccountEnum.Eatery,AccountEnum.Shirt,
								  AccountEnum.Traffic,AccountEnum.Electricity,AccountEnum.Amusement,
								  AccountEnum.Sport,AccountEnum.Company,AccountEnum.Other};
	}
	
	class GridItemAdapter extends BaseAdapter
	{
		private LayoutInflater inflater;
		public GridItemAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}
		@Override
		public int getCount() {
			return enums.length;
		}

		@Override
		public Object getItem(int position) {
			return enums[position];
		}

		@Override
		public long getItemId(int position) {
			return enums[position].getValue();
		}

		@Override
		public View getView(int position, View view, ViewGroup arg2) {
			if(view == null)
			{
				view = inflater.inflate(R.layout.type_account_grid_item, null);
				ImageView img = (ImageView)view.findViewById(R.id.typeAccount_typeIcon);
				TextView text = (TextView)view.findViewById(R.id.typeAccount_typeName);
				
				img.setImageResource(AccountEnum.getAccountEnumImage(enums[position]));
				text.setText(enums[position].toString());
			}
			return view;
		}
	}

	
}
