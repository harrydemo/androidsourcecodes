package cn.com.karl.dida;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.com.karl.domain.Dict;
import cn.com.karl.utils.SearchWords;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class TransActivity extends Activity implements OnClickListener {

	private ImageButton btn_type;
	private Button btn_tran;
	private Button btn_voice;
	private EditText et_value;
	private EditText et_result;
	private ProgressDialog mProgressDialog = null;
	private static Boolean isTrue = false;
	private static final int VOICE_RECOGNITION_REQUEST_CODE = 12345;
	Matcher m = null;

	private Handler handler = new Handler() {
		public void handleMessage(Message message) {

			mProgressDialog.dismiss();
			Dict dict = (Dict) message.obj;
			if (dict == null || "".equals(dict)) {
				Toast.makeText(getApplicationContext(), "û���ҵ���Ӧ������", 1).show();
			} else {
				if (dict.getSents().size() <= 0) {
					Toast.makeText(getApplicationContext(), "û���ҵ���Ӧ������", 1)
							.show();
				} else {
					if (m.matches()) {
						// ���ķ����Ӣ��
						et_result.setText(dict.getSents().get(0).getTrans());
						btn_type.setBackgroundResource(R.drawable.ecbtn);
						btn_type.invalidate();
						isTrue = false;
					} else {
						// Ӣ�ķ��������
						et_result.setText(dict.getSents().get(0).getOrig());

						btn_type.setBackgroundResource(R.drawable.cebtn);
						btn_type.invalidate();
						isTrue = true;
					}

				}
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trans);

		init();
	}

	private void init() {
		btn_tran = (Button) this.findViewById(R.id.btn_tran);
		btn_tran.setOnClickListener(this);
		btn_voice = (Button) this.findViewById(R.id.btn_voice);
		btn_voice.setOnClickListener(this);
		et_value = (EditText) this.findViewById(R.id.et_value);
		et_result = (EditText) this.findViewById(R.id.et_result);
		//
		btn_type = (ImageButton) this.findViewById(R.id.btn_type);
		btn_type.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isTrue == true) {
					btn_type.setBackgroundResource(R.drawable.ecbtn);
					isTrue = false;

				} else {
					btn_type.setBackgroundResource(R.drawable.cebtn);
					isTrue = true;

				}

			}
		});
	}

	/**
	 * ��ѯ�����Ƿ�����
	 * 
	 * @return
	 */
	private Boolean CheckNet() {
		ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		if (info != null && info.isAvailable()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_tran:
			if (CheckNet()) {
				search();

			} else {
				Toast.makeText(getApplicationContext(), "�����޷�����", 1).show();
			}
			break;
		case R.id.btn_voice:
			voice();
			break;
		default:
			break;
		}
	}

	public void search() {
		try {
			final String words = et_value.getText().toString().trim();
			if (words != null && !words.equals("")) {
				final String word = URLEncoder.encode(words, "GBK");
				mProgressDialog = ProgressDialog.show(TransActivity.this, null,
						"���ڲ�ѯ  . . . ");
				new Thread() {
					@Override
					public void run() {
						Dict dict = SearchWords.tansWord(word);
						Message msg = handler.obtainMessage();
						msg.obj = dict;
						handler.sendMessage(msg);
					}
				}.start();

				String regEx = "^[a-zA-Z]{3}.*$";
				Pattern p = Pattern.compile(regEx);
				m = p.matcher(words);

			}
		} catch (Exception e) {
		}
	}

	private void voice() {
		try {
			// ͨ��Intent��������ʶ���ģʽ����������
			Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			// ����ģʽ������ģʽ������ʶ��
			intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
					RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
			// ��ʾ������ʼ
			intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "��ʼ����");
			// ��ʼ����ʶ��
			startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "�Ҳ��������豸", 1).show();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		// �ص���ȡ�ӹȸ�õ�������
		if (requestCode == VOICE_RECOGNITION_REQUEST_CODE
				&& resultCode == RESULT_OK) {
			// ȡ���������ַ�
			ArrayList<String> results = data
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

			String resultString = "";
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < results.size(); i++) {

				sb.append(results.get(i)).append(",");

			}
			String str = sb.substring(0, sb.lastIndexOf(","));
			final String[] items = str.split(",");

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("��ѡ��");
			builder.setItems(items, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					// Toast.makeText(getApplicationContext(), items[item],
					// Toast.LENGTH_SHORT).show();
					et_value.setText(items[item]);
				}
			});
			AlertDialog alert = builder.create();
			alert.show();

			// Toast.makeText(this, items.toString(), 1).show();
		}

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK) {

			AlertDialog.Builder builder = new AlertDialog.Builder(
					TransActivity.this);
			builder.setIcon(R.drawable.bee);
			builder.setTitle("��ȷ���˳���");
			builder.setPositiveButton("ȷ��",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							TransActivity.this.finish();
							android.os.Process.killProcess(android.os.Process
									.myPid());
							android.os.Process.killProcess(android.os.Process
									.myTid());
							android.os.Process.killProcess(android.os.Process
									.myUid());
						}
					});
			builder.setNegativeButton("����",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							dialog.cancel();
						}
					});
			builder.show();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
}
