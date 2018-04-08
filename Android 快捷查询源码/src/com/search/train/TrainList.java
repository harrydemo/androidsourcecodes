package com.search.train;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.search.R;
import com.search.common.ActivityUtils;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/**
 * 
 * @author chenjie
 *
 */
public class TrainList extends ListActivity {
	
	private TextView trainTitle;
	
	private Resources res;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.train_list);
		
		trainTitle = (TextView)this.findViewById(R.id.train_list_title);
		
		List<Map<String, Object>> datas = this.getDatas();
		String[] from = new String[]{
				"item_image",
				"item_text1",
				"item_text2"
		};
		int[] to = new int[]{
				R.id.item_train_image,
				R.id.item_train_text1,
				R.id.item_train_text2
		};
		
		
		Bundle bundle = this.getIntent().getExtras();
		trainTitle.setText(bundle.getString("from")+"到"+bundle.getString("to")+"的火车信息");
		SimpleAdapter adapter = new SimpleAdapter(this, datas, R.layout.list_item_train, from, to);
		this.getListView().setAdapter(adapter);
		
		res = this.getResources();
	}
	
	public void onListItemClick(ListView l, View v, int position, long id){
		super.onListItemClick(l, v, position, id);
		
		Intent intent = new Intent();
		
		Bundle bundle = this.getIntent().getExtras();
		Train t = (Train)bundle.getSerializable("train"+position);
		
		if(t != null){
			Bundle b = new Bundle();
			b.putSerializable("trainDetail", t);
			intent.putExtras(b);
			
			intent.setClass(this, TrainDetail.class);
			
			startActivity(intent);			
		}else{
			ActivityUtils.showDialog(this, res.getString(R.string.ok), res.getString(R.string.tip), "Train 为空");
		}

	}
	

	private List<Map<String, Object>> getDatas() {
		
		List<Map<String, Object>> result =new ArrayList<Map<String,Object>>();

		Bundle bundle = this.getIntent().getExtras();
		int i = 0;
		Train train = null;
		while((train = (Train)bundle.getSerializable("train"+i)) != null){
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("item_image", R.drawable.icon);
			StringBuilder sb = new StringBuilder();
			sb.append("车次：").append(train.getCls()).append("  ")
			  .append("车型：").append(train.getType());
			item.put("item_text1", sb.toString());
			
			StringBuilder sb2 = new StringBuilder();
			sb2.append("发车时间：").append(train.getLeaveTime()).append("  ")
			  .append("到达时间：").append(train.getReachTime());
			item.put("item_text2", sb2.toString());
			
			result.add(item);
			i++;
		}
		
		return result;
	}
	
	
}
