package com.wangwei.activity;

import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * @author 作者 E-mail:
 * @version 创建时间：Aug 11, 2010 7:44:23 PM 类说明
 */
public class FilmTimeSelectActivity extends Activity {
    final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
    final int FP = ViewGroup.LayoutParams.FILL_PARENT;
    private TableLayout tableLayout;
    private Spinner spinner;
    private String[] timeStrings = { "星期一 8月7日 数字 国语", "星期二 8月8日 3DMAX 粤语",
            "星期三 8月9日 数字 粤语", "星期四 8月10日 3D 粤语", "星期五 8月11日 3D 国语",
            "星期六 8月12日 3D 国语", "星期天 8月13日 数字 粤语" };

    private String[][] filmTimes = {
            { "9:30", "", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00",
                    "13:30" },
            { "9:30", "", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00",
                    "13:30" },
            { "", "", "10:30", "", "11:30", "12:00", "12:30", "", "13:30" },
            { "9:30", "", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00",
                    "13:30" },
            { "9:30", "10:00", "", "11:00", "11:30", "12:00", "12:30", "13:00",
                    "13:30" },
            { "9:30", "10:00", "10:30", "", "11:30", "12:00", "12:30", "13:00",
                    "13:30" },
            { "9:30", "", "10:30", "", "11:30", "12:00", "12:30", "13:00",
                    "13:30" },
            { "9:30", "", "10:30", "11:00", "", "12:00", "", "13:00", "13:30" }, };

    private String[] fangyingting = { "1号厅", "2号厅", "3号厅", "4号厅", "5号厅", "6号厅",
            "7号厅", "8号厅", };
    private TextView cinemaNameTextView;
    private String filmDate;
    private TextView filmNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.film_time_select_activity);
        initViews();
        initTableLayout();

    }

    private void initViews() {
        filmNameTextView = (TextView) findViewById(R.id.ftsa_filmname);
        filmNameTextView.setText(getIntent().getStringExtra("filmname"));
        spinner = (Spinner) findViewById(R.id.ftsa_select_time);
        ArrayAdapter<String> areaAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, timeStrings);
        areaAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(areaAdapter);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                    int arg2, long arg3) {
                filmDate = timeStrings[arg2];
                tableLayout.removeAllViews();
                initTableLayout();
            }

            
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });

        cinemaNameTextView = (TextView) findViewById(R.id.ftsa_moviename);
        cinemaNameTextView.setText("大地数字影院");

        Button button = (Button) findViewById(R.id.ftsa_selectcity);
        button.setOnClickListener(new OnClickListener() {
            
            public void onClick(View v) {
                Intent intent = new Intent(FilmTimeSelectActivity.this,
                        SelectCinemaActivity.class);
                startActivityForResult(intent, 1);
            }

        });

    }

    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            cinemaNameTextView.setText(data.getStringExtra("cinemaName"));
        }

    }

    private void initTableLayout() {
        Random random = new Random();
        tableLayout = (TableLayout) findViewById(R.id.ftsa_tablelayout);
        tableLayout.setStretchAllColumns(true);
        for (int i = 0; i < filmTimes.length; i++) {
            TableRow tr = new TableRow(this);
            for (int j = 0; j < filmTimes[0].length; j++) {
                final Button button = new Button(this);
                if (j == 0) {
                    Button button1 = new Button(this);
                    button1.setWidth(60);
                    button1.setHeight(35);
                    button1.setTextSize(15);
                    button1.setText(fangyingting[i]);
                    button1.setBackgroundColor(Color.BLUE);
                    tr.addView(button1);
                }
                button.setWidth(60);
                button.setHeight(35);
                button.setTextSize(15);
                button.setText(filmTimes[i][j]);
                if (filmTimes[i][j].equals("")) {
                    button.setBackgroundColor(Color.rgb(51, 51, 51));
                    button.setClickable(false);
                } else {

                    int r = random.nextInt(9);
                    if (r > 2 && r < 7) {
                        button.setBackgroundColor(Color.GREEN);
                        final String selectTime = filmTimes[i][j];
                        final int n = i;
                        button.setOnClickListener(new OnClickListener() {
                            
                            public void onClick(View v) {
                                Intent intent = new Intent(
                                        FilmTimeSelectActivity.this,
                                        FilmSeatsSelectActivity.class);
                                intent.putExtra("filmTimes", filmTimes[n]);
                                intent.putExtra("filmDate", filmDate);
                                intent.putExtra("moviesName", filmNameTextView
                                        .getText());

                                intent
                                        .putExtra("dianyingyuanname",
                                                cinemaNameTextView.getText()
                                                        .toString());
                                intent.putExtra("selecttime", selectTime);
                                intent
                                        .putExtra("fangyingting",
                                                fangyingting[n]);
                                startActivity(intent);
                            }

                        });
                    } else {
                        button.setBackgroundColor(Color.RED);
                        button.setClickable(false);
                    }
                }

                TextView textView = new TextView(this);
                textView.setText(" ");
                tr.addView(textView);
                tr.addView(button);
                if (j == filmTimes[0].length - 1) {
                    TextView textView1 = new TextView(this);
                    textView1.setText("  ");
                    tr.addView(textView1);
                }

            }
            tableLayout.addView(tr, new TableLayout.LayoutParams(FP, WC));
            TableRow tr1 = new TableRow(this);
            TextView textView = new TextView(this);
            tr1.addView(textView);
            tableLayout.addView(tr1, new TableLayout.LayoutParams(FP, WC));
        }
    }
}
