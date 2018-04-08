package com.wangwei.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author 作者 E-mail:
 * @version 创建时间：Aug 12, 2010 5:26:07 PM 类说明
 */
public class AllCinemasActivity extends Activity {
    private List<HashMap<String, Object>> cinemasListItem;
    private String[] cinemaNames = { "青春电影大世界", "西湖电影院", "xxxxxxx大世界",
            "xxxxx电影院", "xxxx大世界", "xxxxx电影院" };
    private String[] cinemaAdd = { "杭州xxxxxxx", "杭州xxxxxxx", "杭州xxxxxxx",
            "杭州xxxxxxx", "杭州xxxxxxx", "杭州xxxxxxx", };
    private String[] cinemaPhone = { "0571xxxxxx", "0571xxxxxxx", "0571xxxxxx",
            "0571xxxxxxx", "0571xxxxxx", "0571xxxxxxx", };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_cinemas_activity);
        initList();
        initButtons();
    }
    
    private void initButtons() {
        final Button button = (Button) findViewById(R.id.all_films);
        button.setOnClickListener(new OnClickListener() {
            
            public void onClick(View v) {
                Intent intent = new Intent(AllCinemasActivity.this,
                        AllCinemasActivity.class);
                startActivity(intent);
            }

        });
    }

    private void initList() {
        cinemasListItem = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < cinemaNames.length; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("cinema_name", cinemaNames[i]);
            map.put("ciname_add", "地址:" + cinemaAdd[i]);
            map.put("cinema_dianhua", "电话:" + cinemaPhone[i]);
            cinemasListItem.add(map);
        }
        SimpleAdapter listItemAdapter = new SimpleAdapter(this,
                cinemasListItem, R.layout.cinema_list_listview, new String[] {
                        "cinema_name", "ciname_add", "cinema_dianhua" },
                new int[] { R.id.cinema_name, R.id.ciname_add,
                        R.id.cinema_dianhua });
        ListView cinemaListView = (ListView) findViewById(R.id.cinemaListView);
        cinemaListView.setAdapter(listItemAdapter);
        cinemaListView.setOnItemClickListener(new OnItemClickListener() {
            
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                Intent intent = new Intent(AllCinemasActivity.this,
                        FilmTimeSelectActivity.class);
                intent.putExtra("filmInfo", cinemaNames[arg2]);
                startActivity(intent);
            }

        });

    }
}
