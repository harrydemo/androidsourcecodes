package com.lianghe.caculator;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lianghe.tools.Arith;

public class CaculatorActivity extends Activity implements OnTouchListener,
		OnLongClickListener {

	private EditText et;
	private String operator = "";// 操作
	private String oldText = "";
	private boolean inputDone = true;
	private String operatorNumber = "";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		et = (EditText) findViewById(R.id.tv_result);
		Button delBtn = (Button) findViewById(R.id.btnDel);
		delBtn.setOnLongClickListener(this);
		et.setOnTouchListener(this);
	}

	public void onButtonClickHandler(View view) {
		Button button = (Button) view;
		if ("del".equals(button.getText())) {
			String s = String.valueOf(et.getText());
			if (s.length() > 0) {
				if (s.length() == 1) {
					clear("0");
				} else {
					et.setText(s.subSequence(0, s.length() - 1));
				}
				operatorNumber = et.getText().toString();
			}
		} else if ("+".equals(button.getText()) || "-".equals(button.getText())
				|| "*".equals(button.getText()) || "÷".equals(button.getText())
				|| "=".equals(button.getText())) {
			caculate();// 计算
			operator = button.getText().toString();// 操作符
			oldText = et.getText().toString();
			inputDone = true;
		} else {
			if (inputDone) {
				et.setText(button.getText().toString());
				inputDone = false;
			} else {
				et.append(button.getText().toString());
			}
			operatorNumber = et.getText().toString();
		}
		et.requestFocus(TextView.FOCUS_RIGHT);
	}

	private void caculate() {
		if ("+".equals(operator)) {
			et.setText(String.valueOf(Arith.add(oldText, operatorNumber)));
		} else if ("-".equals(operator)) {
			et.setText(String.valueOf(Arith.sub(oldText, operatorNumber)));
		} else if ("*".equals(operator)) {
			et.setText(String.valueOf(Arith.mul(oldText, operatorNumber)));
		} else if ("÷".equals(operator)) {
			if ("0".equals(operatorNumber)) {
				clear("被除数不能为零");
			} else {
				et.setText(String.valueOf(Arith.div(oldText, operatorNumber)));
			}
		}
	}

	private void clear(String text) {
		et.setText(text);
		operator = "";
		oldText = "";
		operatorNumber = "";
		inputDone = true;
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		et.setInputType(InputType.TYPE_NULL);
		return false;
	}

	@Override
	public boolean onLongClick(View v) {
		clear("0");
		return false;
	}
}