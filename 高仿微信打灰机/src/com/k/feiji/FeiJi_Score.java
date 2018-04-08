package com.k.feiji;

import com.baidu.mobstat.StatService;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class FeiJi_Score extends FeiJi_BaseAc {

	private SharedPreferences _Share;
	private ListView _ListView;
	private ScoreListAda _Adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feiji_score);
		Init();
	}

	private void Init() {
		// TODO Auto-generated method stub
		_Share = getSharedPreferences("Share", Context.MODE_PRIVATE);
		String _Score = _Share.getString("SCORE", "0;0;0;0;0;0;0;0;0;0");
		String[] _Scores = _Score.split(";");

		_ListView = (ListView) findViewById(R.id.feiji_score_list);
		_Adapter = new ScoreListAda(FeiJi_Score.this, _Scores);
		_ListView.setAdapter(_Adapter);
	}

	private class ScoreListAda extends BaseAdapter {

		private Item _Item;
		private LayoutInflater _Inflater;
		private String[] _Lists;

		public ScoreListAda(Context c, String[] lists) {
			_Inflater = (LayoutInflater) c
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			_Lists = lists;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return _Lists.length;
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
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (convertView == null) {
				convertView = _Inflater
						.inflate(R.layout.feiji_score_item, null);
				_Item = new Item();
				_Item.tv1 = (TextView) convertView
						.findViewById(R.id.feiji_score_list_item1);
				_Item.tv2 = (TextView) convertView
						.findViewById(R.id.feiji_score_list_item2);
				_Item.bu = (Button) convertView
						.findViewById(R.id.feiji_score_list_return);
				convertView.setTag(_Item);
			} else {
				_Item = (Item) convertView.getTag();
			}
			if (_Item != null) {
				_Item.tv1.setText("" + (position + 1));
				_Item.tv2.setText("" + _Lists[position]);
				if (position >= _Lists.length - 1) {
					_Item.bu.setVisibility(View.VISIBLE);
					_Item.bu.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							finish();
						}
					});
				}
			}

			return convertView;
		}

		public class Item {
			public TextView tv1;
			public TextView tv2;
			public Button bu;
		}
	}

	public void onResume() {
		super.onResume();
		StatService.onResume(this);
	}

	public void onPause() {
		super.onPause();
		StatService.onPause(this);
	}
}
