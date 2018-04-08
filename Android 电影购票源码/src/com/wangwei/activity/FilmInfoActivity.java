package com.wangwei.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wangwei.vo.FilmInfo;

/**
 * @author 作者 E-mail:
 * @version 创建时间：Aug 11, 2010 3:22:58 PM 类说明
 */
public class FilmInfoActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.film_info_activity);
        initFileInfo();
    }

    private void initFileInfo() {
        final FilmInfo filmInfo = getIntent().getParcelableExtra("filmInfo");
        ImageView imageView = (ImageView) findViewById(R.id.info_imgUrl);
        imageView.setBackgroundResource(Integer.valueOf(filmInfo.getImgUrl()));

        TextView filmNameTextView = (TextView) findViewById(R.id.info_filmName);
        filmNameTextView.setText(filmInfo.getFilmName());

        TextView directorTextView = (TextView) findViewById(R.id.info_director);
        directorTextView.setText(getString(R.string.daoyan)
                + filmInfo.getDirector());

        TextView starrTextView = (TextView) findViewById(R.id.info_starring);
        starrTextView.setText(getString(R.string.zhuyan)
                + filmInfo.getStarring());

        TextView produceTextView = (TextView) findViewById(R.id.info_produce);
        produceTextView.setText(getString(R.string.chupin)
                + filmInfo.getProduce());

        TextView actionTimeTextView = (TextView) findViewById(R.id.info_actionTime);
        actionTimeTextView.setText(getString(R.string.actionTime)
                + filmInfo.getActionTime());

        TextView summaryTextView = (TextView) findViewById(R.id.info_summary);
        summaryTextView.setText(getString(R.string.zhaiyao)
                + filmInfo.getSummary());

        Button button = (Button) findViewById(R.id.info_goupiao);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(FilmInfoActivity.this,
                        FilmTimeSelectActivity.class);
                intent.putExtra("filmname", filmInfo.getFilmName());
                startActivity(intent);
            }

        });
    }
}
