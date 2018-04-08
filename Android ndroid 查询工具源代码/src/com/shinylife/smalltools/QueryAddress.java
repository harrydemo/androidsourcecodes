package com.shinylife.smalltools;

import com.shinylife.smalltools.api.ApiImpl;
import com.shinylife.smalltools.entity.AddressInfo;
import com.shinylife.smalltools.helper.InternetHelper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class QueryAddress extends BaseActivity {
	private Button query_btn;
	private EditText query_q;
	private TextView query_result;
	private ProgressDialog pBar;
	private final int QUERY_MSG = 102;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setView(R.layout.query_add);
		SetTitle(getString(R.string.number_add_info));
		setTitleBar(R.drawable.title_back, "", R.drawable.title_home, "");
		findViews();
		setListener();
	}

	private void findViews() {
		query_btn = (Button) findViewById(R.id.query_btn);
		query_q = (EditText) findViewById(R.id.query_q);
		query_result = (TextView) findViewById(R.id.query_result);
	}

	private void setListener() {
		query_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

				inputMethodManager.hideSoftInputFromWindow(
						query_q.getWindowToken(),
						InputMethodManager.RESULT_UNCHANGED_SHOWN);
				boolean hasNet = new InternetHelper(QueryAddress.this)
						.getNetworkIsAvailable();
				if (!hasNet) {
					showToast(getString(R.string.no_internet));
					return;
				}
				String q = query_q.getText().toString();
				if (q == null || q.length() == 0) {
					showToast(getString(R.string.input_zip_no_desc));
				} else {
					queryData(q);
				}
			}
		});
	}

	private void queryData(final String no) {
		pBar = new ProgressDialog(this);
		pBar.setMessage(getString(R.string.querying));
		pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pBar.show();
		new Thread() {
			@Override
			public void run() {
				ApiImpl impl = new ApiImpl();
				Message msg = new Message();
				msg.what = QUERY_MSG;
				msg.obj = impl.getAddressInfo(no);
				hander.sendMessage(msg);
			}
		}.start();
	}

	Handler hander = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.obj == null) {
				if (pBar.isShowing()) {
					pBar.dismiss();
				}
				query_result
						.setText(Html
								.fromHtml(getString(R.string.query_no_result_template)));
				return;
			}
			switch (msg.what) {
			case QUERY_MSG:
				AddressInfo pi = (AddressInfo) msg.obj;
				String ret = getString(R.string.query_zip_result_template,
						pi.getProvince(), pi.getCity(), pi.getPhone(),
						pi.getZipcode(), pi.getLocation());
				query_result.setText(ret);
				break;
			default:
				break;
			}
			if (pBar.isShowing()) {
				pBar.dismiss();
			}
		}
	};

	@Override
	protected void HandleTitleBarEvent(int buttonId) {
		// TODO Auto-generated method stub
		switch (buttonId) {
		case 0:
			finish();
			break;
		case 1:
			Intent intent = new Intent(getApplicationContext(),
					SmallToolsActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
}
