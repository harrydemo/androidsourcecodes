package com.wangwei.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.wangwei.view.SeatsView;

public class FilmSeatsSelectActivity extends Activity {

    // 放大等级
    private int level = 7;
    private SeatsView seatsView;
    private TextView textView;
    private float t_x;
    private float t_y;
    private TableLayout tableLayout;
    final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
    final int FP = ViewGroup.LayoutParams.FILL_PARENT;
    private Button sureButton;
    private TextView timeTextView;
    private String filmDate;
    // 为了demo 认为制造的坐标,要跟电影时间长度一样，匹配的
    private int[] xxx;
    String[] filmTimes;
    String ft;
    // 存放座位的点
    public static List<Point> seatsList;
    private StringBuffer seatStringBuffer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.film_seat_activity);
        filmDate = getIntent().getStringExtra("filmDate");
        textView = (TextView) findViewById(R.id.t);
        seatsView = (SeatsView) findViewById(R.id.view);
        Button biggerButton = (Button) findViewById(R.id.bigger);
        biggerButton.setOnClickListener(new OnClickListener() {
            
            public void onClick(View v) {
                level++;
                if (level > 15) {
                    level = 15;
                } else {
                    SeatsView.width++;
                    SeatsView.heigh++;
                    SeatsView.textSize++;
                    seatsView.postInvalidate();
                }
            }

        });

        Button smallerButton = (Button) findViewById(R.id.smaller);
        smallerButton.setOnClickListener(new OnClickListener() {
            
            public void onClick(View v) {
                level--;
                if (level < 0) {
                    level = 0;
                } else {
                    SeatsView.width--;
                    SeatsView.heigh--;
                    SeatsView.textSize--;
                    seatsView.postInvalidate();
                }
            }

        });

        TextView textView = (TextView) findViewById(R.id.fsa_movice_name);
        textView.setText(getIntent().getStringExtra("dianyingyuanname"));

        timeTextView = (TextView) findViewById(R.id.fsa_time);

        sureButton = (Button) findViewById(R.id.fsa_sure);
        sureButton.setVisibility(View.INVISIBLE);
        sureButton.setOnClickListener(new OnClickListener() {
            
            public void onClick(View v) {

            }

        });
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        timeTextView.setText(simpleDateFormat.format(new java.util.Date())
                + " " + filmDate + " "
                + getIntent().getStringExtra("selecttime") + " "
                + getIntent().getStringExtra("moviesName"));
        filmTimes = getIntent().getStringArrayExtra("filmTimes");
        xxx = new int[filmTimes.length];
        for (int i = 0; i < xxx.length; i++) {
            xxx[i] = i + 1;
            if (filmTimes[i].equals(getIntent().getStringExtra("selecttime"))) {
                seatsView.x_num = (i + 1) * (xxx[i] + 20);
            }
        }

        initTableLayout();

    }

    private void initTableLayout() {
        tableLayout = (TableLayout) findViewById(R.id.fsa_tablelayout);
        tableLayout.setStretchAllColumns(true);
        TableRow tr = new TableRow(this);

        for (int j = 0; j < filmTimes.length; j++) {

            final Button button1 = new Button(this);
            button1.setWidth(40);
            button1.setHeight(30);
            if (filmTimes[j].equals("")) {
                button1.setBackgroundColor(Color.rgb(51, 51, 51));
            } else {
                button1.setBackgroundColor(Color.GREEN);

                ft = filmTimes[j];
                final int num = j;
                button1.setOnClickListener(new OnClickListener() {
                    
                    public void onClick(View v) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                                "yyyy");
                        timeTextView.setText(simpleDateFormat
                                .format(new java.util.Date())
                                + " "
                                + filmDate
                                + " "
                                + ft
                                + " "
                                + getIntent().getStringExtra("moviesName"));
                        seatsView.x_num = (num + 1) * (xxx[num] + 20);
                        seatsView.postInvalidate();

                    }

                });
            }
            TextView textView = new TextView(this);
            textView.setText(" ");
            tr.addView(button1);
            tr.addView(textView);
        }
        TableRow tr1 = new TableRow(this);
        for (int j = 0; j < filmTimes.length; j++) {

            TextView time = new TextView(this);
            time.setTextSize(15);
            time.setPadding(1, -4, 0, 0);
            if (filmTimes[j].equals("")) {
                time.setWidth(filmTimes[j].length());
            } else {
                time.setText(filmTimes[j]);
            }

            TextView textView = new TextView(this);
            textView.setText(" ");
            tr1.addView(time);
            tr1.addView(textView);
        }

        tableLayout.addView(tr, new TableLayout.LayoutParams(FP, WC));
        tableLayout.addView(tr1, new TableLayout.LayoutParams(FP, WC));
    }

    
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            t_x = event.getX();
            t_y = event.getY();
            Log.d("x:y", t_x + ":" + t_y);
            // 根据放置位置不同高不同，坐标-165转换过去
            final Point point = seatsView.screenToindex(t_x, t_y - 165);
            if (point != null) {
                // 点击的加入LIST里
                if (seatsList == null) {

                    seatsList = new ArrayList<Point>();
                }
                if (seatsList.size() != 0) {
                    for (int i = 0; i < seatsList.size(); i++) {
                        if (!(seatsList.get(i).x == point.x && seatsList.get(i).y == point.y)) {
                            seatsList.add(point);
                        }
                    }
                } else {
                    seatsList.add(point);
                }
                textView.setVisibility(View.VISIBLE);
                // 180是间隔，我认为定的，做DEMO试用 横坐标180能买
                if (t_x > seatsView.x_num + SeatsView.width / 2
                        + SeatsView.width / 4) {
                    final int size = seatsList.size();
                    seatStringBuffer = new StringBuffer();
                    for (int i = 0; i < size; i++) {
                        seatStringBuffer.append((seatsList.get(i).x + 1) + "排"
                                + (seatsList.get(i).y + 1) + "号 ");
                    }
                    textView.setText("选中:" + seatStringBuffer.toString()
                            + "确定购票?");
                    sureButton.setVisibility(View.VISIBLE);
                    sureButton.setOnClickListener(new OnClickListener() {
                        
                        public void onClick(View v) {
                            Intent intent = new Intent(
                                    FilmSeatsSelectActivity.this,
                                    LoginActivity.class);
                            intent.putExtra("fm", getIntent().getStringExtra(
                                    "moviesName"));
                            intent.putExtra("dianyingyuanname", getIntent()
                                    .getStringExtra("dianyingyuanname"));
                            intent.putExtra("fangyingting", getIntent()
                                    .getStringExtra("fangyingting"));
                            intent.putExtra("actionTime", "2010-08-07 " + ft);
                            intent.putExtra("zuoci", seatStringBuffer
                                    .toString());
                            intent.putExtra("count", size + "");

                            startActivity(intent);

                        }

                    });
                } else {
                    textView.setText("选中第" + (point.x + 1) + "排,第"
                            + (point.y + 1) + "号已被预订!");
                    sureButton.setVisibility(View.INVISIBLE);
                }

            } else {
                seatsList = null;
                seatStringBuffer = null;
                sureButton.setVisibility(View.INVISIBLE);
                textView.setVisibility(View.INVISIBLE);
            }

        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            final float xdiff = (event.getX() - t_x);
            final float ydiff = (event.getY() - t_y);
            t_x = event.getX();
            t_y = event.getY();
            // Log.d("screenCenter_y",SeatsView.screenCenter_y+"");
            if (SeatsView.screenCenter_x + xdiff > 320) {
                SeatsView.screenCenter_x = 320;
            } else if (SeatsView.screenCenter_x < 0) {
                SeatsView.screenCenter_x = 0;
            } else {
                SeatsView.screenCenter_x += xdiff;
            }
            if (SeatsView.screenCenter_y + ydiff > 160) {
                SeatsView.screenCenter_y = 160;
            } else if (SeatsView.screenCenter_y < 0) {
                SeatsView.screenCenter_y = 0;
            } else {
                SeatsView.screenCenter_y += ydiff;
            }
            seatsView.postInvalidate();
        }
        return super.onTouchEvent(event);
    }
}