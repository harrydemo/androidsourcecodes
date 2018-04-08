package org.iSun.td;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iSun.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
/**
 * 选择地图的界面
 * @author iSun
 *
 */
public class MapSelectActivity extends Activity implements OnItemClickListener {
	ListView mapList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main);

		init();

	}

	private void init() {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> map;

		map = new HashMap<String, String>();
		map.put("mapName", "Snake");
		list.add(map);
		map = new HashMap<String, String>();
		map.put("mapName", "MStyle");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("mapName", "Circle");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("mapName", "LingStyle");
		list.add(map);

		map = new HashMap<String, String>();
		map.put("mapName", "Revenge");
		list.add(map);

		mapList = (ListView) findViewById(R.id.lv_mapList);
		mapList.setOnItemClickListener(this);
		mapList.setCacheColorHint(0);
		mapList.setAdapter(new SimpleAdapter(this, list, R.layout.mapitem,
				new String[] { "mapName" }, new int[] { R.id.mapItem }));
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setClass(this, TurrentDefenseActivity.class);
		intent.putExtra("mapId", position);
		startActivity(intent);
	}
}
