package com.wgs.jiesuo;

import java.util.List;

import android.R.integer;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.wgs.jiesuo.LockPatternView.Cell;
import com.wgs.jiesuo.LockPatternView.DisplayMode;
import com.wgs.jiesuo.LockPatternView.OnPatternListener;

public class MainActivity extends Activity implements OnClickListener {

	// private OnPatternListener onPatternListener;

	private LockPatternView lockPatternView;

	private LockPatternUtils lockPatternUtils;

	private Button btn_set_pwd;

	private Button btn_reset_pwd;

	private Button btn_check_pwd;
	
	private boolean opFLag = true;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		lockPatternView = (LockPatternView) findViewById(R.id.lpv_lock);
		btn_reset_pwd = (Button) findViewById(R.id.btn_reset_pwd);
		btn_set_pwd = (Button) findViewById(R.id.btn_set_pwd);
		btn_check_pwd = (Button) findViewById(R.id.btn_check_pwd);
		btn_reset_pwd.setOnClickListener(this);
		btn_set_pwd.setOnClickListener(this);
		btn_check_pwd.setOnClickListener(this);

		lockPatternUtils = new LockPatternUtils(this);
		lockPatternView.setOnPatternListener(new OnPatternListener() {

			public void onPatternStart() {

			}

			public void onPatternDetected(List<Cell> pattern) {
				if(opFLag){
					int result = lockPatternUtils.checkPattern(pattern);
					if (result!= 1) {
						if(result==0){
							lockPatternView.setDisplayMode(DisplayMode.Wrong);
							Toast.makeText(MainActivity.this, "√‹¬Î¥ÌŒÛ", Toast.LENGTH_LONG)
							.show();
						}else{
							lockPatternView.clearPattern();
							Toast.makeText(MainActivity.this, "«Î…Ë÷√√‹¬Î", Toast.LENGTH_LONG)
							.show();
						}
						
					} else {
						Toast.makeText(MainActivity.this, "√‹¬Î’˝»∑", Toast.LENGTH_LONG)
								.show();
					}
				}else{
					lockPatternUtils.saveLockPattern(pattern);
					Toast.makeText(MainActivity.this, "√‹¬Î“—æ≠…Ë÷√", Toast.LENGTH_LONG)
					.show();
					lockPatternView.clearPattern();
				}
			
			}

			public void onPatternCleared() {

			}

			public void onPatternCellAdded(List<Cell> pattern) {

			}
		});
	}

	public void onClick(View v) {
		if (v == btn_reset_pwd) {
			lockPatternView.clearPattern();
			lockPatternUtils.clearLock();
		} else if (v == btn_check_pwd) {
			opFLag = true;
		} else {
			opFLag = false;
		}
	}

}
