package com.wangwei.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author 作者 E-mail:
 * @version 创建时间：Aug 12, 2010 2:00:16 PM 类说明
 */
public class SureActivity extends Activity {
    TextView phonenumTextView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sure_activity);

        TextView yingyuanmingziTextView = (TextView) findViewById(R.id.yingyuanmingzi);
        yingyuanmingziTextView.setText(getIntent().getStringExtra(
                "dianyingyuanname"));

        TextView fmTextView = (TextView) findViewById(R.id.fm);
        fmTextView.setText(getIntent().getStringExtra("fm"));

        TextView yydzTextView = (TextView) findViewById(R.id.yydz);
        yydzTextView.setText(getIntent().getStringExtra("dizhi"));

        TextView ytTextView = (TextView) findViewById(R.id.yt);
        ytTextView.setText(getIntent().getStringExtra("fangyingting"));

        TextView fysjTextView = (TextView) findViewById(R.id.fysj);
        fysjTextView.setText(getIntent().getStringExtra("actionTime"));

        TextView zcTextView = (TextView) findViewById(R.id.zc);
        zcTextView.setText(getIntent().getStringExtra("zuoci"));

        TextView pjTextView = (TextView) findViewById(R.id.pj);
        pjTextView.setText("20元");

        TextView psTextView = (TextView) findViewById(R.id.ps);
        psTextView.setText(getIntent().getStringExtra("count"));

        TextView zjTextView = (TextView) findViewById(R.id.zj);
        zjTextView.setText(Integer.valueOf(getIntent().getStringExtra("count"))
                * 20 + "元");

        phonenumTextView = (TextView) findViewById(R.id.phonenum);
        phonenumTextView.setOnClickListener(new OnClickListener() {

            
            public void onClick(View v) {
                final EditText input = new EditText(SureActivity.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                new AlertDialog.Builder(SureActivity.this).setTitle("购票手机号")
                        .setView(input).setPositiveButton(R.string.sure,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                            int which) {
                                        String value = input.getText()
                                                .toString();
                                        if (!value.trim().equals("")) {
                                            phonenumTextView.setText(value);
                                        }

                                    }
                                }).show();
            }
        });

        Button cancelButton = (Button) findViewById(R.id.cancel_gp);
        cancelButton.setOnClickListener(new OnClickListener() {
            
            public void onClick(View v) {
                finish();
            }

        });

        Button sureButton = (Button) findViewById(R.id.sure_gp);
        sureButton.setOnClickListener(new OnClickListener() {
            
            public void onClick(View v) {

                Intent intent = new Intent(SureActivity.this,
                        SearchMapAcitivity.class);
                intent.putExtra("yingyuanmingzi", getIntent().getStringExtra(
                        "dianyingyuanname"));
                startActivity(intent);
                finish();
            }

        });
    }
}
