package com.wangwei.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * @author 作者 E-mail:
 * @version 创建时间：Aug 11, 2010 11:34:26 PM 类说明
 */
public class SelectCinemaActivity extends Activity {
    private String[] provinces = { "浙江", "广东" };

    private String[][] cities = { { "杭州", "温州" }, { "广州", "珠海" } };

    private String[][] cinemas = { {"青春电影大世界", "西湖影院"},{"青春电影大世界", "西湖影院"} };
    private Context context;

    private String cinemaName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cinema_select_activity);
        context = this;
        init();
    }

    private void init() {
        Spinner proviceSpinner = (Spinner) findViewById(R.id.csa_select_province);
        final Spinner citySpinner = (Spinner) findViewById(R.id.csa_select_city);
        final Spinner cinemaSpinner = (Spinner) findViewById(R.id.csa_select_cinema);

        ArrayAdapter<String> areaAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, provinces);
        areaAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        proviceSpinner.setAdapter(areaAdapter);
        proviceSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                    int arg2, long arg3) {

                ArrayAdapter<String> areaAdapter1 = new ArrayAdapter<String>(
                        context, android.R.layout.simple_spinner_item,
                        cities[arg2]);
                areaAdapter1
                        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                citySpinner.setAdapter(areaAdapter1);
                citySpinner
                        .setOnItemSelectedListener(new OnItemSelectedListener() {
                            
                            public void onItemSelected(AdapterView<?> arg0,
                                    View arg1, int arg2, long arg3) {
                                ArrayAdapter<String> areaAdapter2 = new ArrayAdapter<String>(
                                        context,
                                        android.R.layout.simple_spinner_item,
                                        cinemas[arg2]);
                                areaAdapter2
                                        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                cinemaSpinner.setAdapter(areaAdapter2);
                                cinemaSpinner
                                        .setOnItemSelectedListener(new OnItemSelectedListener() {
                                            
                                            public void onItemSelected(
                                                    AdapterView<?> arg0,
                                                    View arg1, int arg2,
                                                    long arg3) {
                                                cinemaName = cinemas[0][arg2];
                                            }

                                            
                                            public void onNothingSelected(
                                                    AdapterView<?> arg0) {

                                            }

                                        });
                            }

                            
                            public void onNothingSelected(AdapterView<?> arg0) {

                            }

                        });
            }

            
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });

        Button sureButton = (Button) findViewById(R.id.cinema_sure);
        sureButton.setOnClickListener(new OnClickListener() {
            
            public void onClick(View v) {
                Toast.makeText(context, cinemaName, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context,
                        FilmTimeSelectActivity.class);
                intent.putExtra("cinemaName", cinemaName);
                setResult(RESULT_OK, intent);
                finish();
            }

        });

        Button retButton = (Button) findViewById(R.id.cinema_return);
        retButton.setOnClickListener(new OnClickListener() {
            
            public void onClick(View v) {
                finish();
            }

        });

    }
}
