package cn.com.karl.dida;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;


import cn.com.karl.domain.Dict;
import cn.com.karl.domain.Sent;
import cn.com.karl.utils.MyAdapter;
import cn.com.karl.utils.SearchWords;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class DidaActivity extends Activity {

	private ImageButton btn_voice; // ��������
	private EditText edit_search; // �༭����
	private Button btn_search; // ��������
	private TextView text_word;
	private TextView text_pron;
	private TextView btn_aduio; // ���ʷ���
	private TextView btn_add; // ��ӵ���
	private TextView text_def;
	private ListView didaListview;
	private MyAdapter adapter;
	private String aduioPath;
	private AudioManager audioManager;// ����������
	private int maxVolume;// �������
	
	private static Context context;
	private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;  
	public static StringBuffer sb=new StringBuffer();
	private ProgressDialog mProgressDialog;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			mProgressDialog.dismiss();
			Dict dict = (Dict) msg.obj;
			if (dict != null && !"".equals(dict)) {
				if (dict.getKey() == null || "".equals(dict.getKey())) {
					text_word.setText(edit_search.getText());
				} else {
					text_word.setText(dict.getKey());
				}
				String ps = dict.getPs();
				if (ps == null || "".equals(ps)) {
					// ���ķ����Ӣ��
					btn_aduio.setVisibility(View.INVISIBLE);
					btn_add.setVisibility(View.VISIBLE);
					text_def.setText(dict.getAcceptation());
					if (dict.getSents() != null) {
						adapter = new MyAdapter(getApplicationContext());
						adapter.setSents(dict.getSents());
						didaListview.setAdapter(adapter);
					}
				} else {
					// Ӣ�ķ��������
					text_pron.setText("[" + ps + "]");
					aduioPath = dict.getPron();
					btn_aduio.setVisibility(View.VISIBLE);
					btn_add.setVisibility(View.VISIBLE);
					text_def.setText(dict.getAcceptation());
					if (dict.getSents() != null) {
						adapter = new MyAdapter(getApplicationContext());
						adapter.setSents(dict.getSents());
						didaListview.setAdapter(adapter);
					}
					for(int i=0;i<dict.getSents().size();i++){
						Sent sent=dict.getSents().get(i);
						sb.append(sent.getOrig()).append(":").append(sent.getTrans()).append(",");
					}
				}
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dida);

		context = this;

		init();
		
		audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);// ����������
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
				maxVolume-2, AudioManager.FLAG_ALLOW_RINGER_MODES);
		
	}

	private void init() {
		btn_voice = (ImageButton) this.findViewById(R.id.btn_voice);
		edit_search = (EditText) this.findViewById(R.id.edit_search);
		btn_search = (Button) this.findViewById(R.id.btn_search);
		text_word = (TextView) this.findViewById(R.id.text_word);
		// edit_search.setText("%C4%E3%BA%C3");
		text_pron = (TextView) this.findViewById(R.id.text_pron);
		btn_aduio = (TextView) this.findViewById(R.id.btn_aduio);
		btn_add = (TextView) this.findViewById(R.id.btn_add);
		text_def = (TextView) this.findViewById(R.id.text_def);
		didaListview = (ListView) this.findViewById(R.id.didaListview);
		// ��������
		btn_voice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				voice();
			}
		});
		// ��������
		btn_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (CheckNet()) {
					final String word = edit_search.getText().toString().trim();
					try {
						final String words = URLEncoder.encode(word, "GBK");
						if (words != null && !words.equals("")) {
							mProgressDialog = ProgressDialog.show(
									DidaActivity.this, null, " ���ڲ�ѯ . . . ");
							new Thread(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									System.out
											.println("--------------" + words);
									Dict dict = SearchWords.tansWord(words);

									Message msg = handler.obtainMessage();
									msg.obj = dict;
									handler.sendMessage(msg);
								}
							}).start();
						}
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				else {
					Toast.makeText(getApplicationContext(), "�����޷�����", 1).show();
				}
			}
		});
		// ���ʷ���
		btn_aduio.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				 playAudio(aduioPath);
				 
			}
		});
		// ��ӵ���
		btn_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
	}

	public static void playAudio(String path) {
		MediaPlayer mp = null;
		try {
			mp = MediaPlayer.create(context, Uri.parse(path));
			mp.start();
		} finally {
			mp = null;
		}
	}
	 private void voice(){
		   try{  
	           //ͨ��Intent��������ʶ���ģʽ����������  
	           Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);  
	           //����ģʽ������ģʽ������ʶ��  
	           intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);  
	           //��ʾ������ʼ  
	           intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "��ʼ����");  
	           //��ʼ����ʶ��  
	           startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);  
	           }catch (Exception e) {  
	               // TODO: handle exception  
	               e.printStackTrace();  
	               Toast.makeText(getApplicationContext(), "�Ҳ��������豸", 1).show();  
	           }  
	       
	      
	   }
	   
	   @Override  
	   protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
	       // TODO Auto-generated method stub  
		   super.onActivityResult(requestCode, resultCode, data);  
	       //�ص���ȡ�ӹȸ�õ�������   
	       if(requestCode==VOICE_RECOGNITION_REQUEST_CODE && resultCode==RESULT_OK){  
	           //ȡ���������ַ�  
	           ArrayList<String> results=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);  
	           
	           String resultString=""; 
	           StringBuffer sb=new StringBuffer();
	           for(int i=0;i<results.size();i++){  
	             
	               sb.append(results.get(i)).append(",");
	               
	           }
	           String str=sb.substring(0, sb.lastIndexOf(","));
	           final String[] items=str.split(",");
	           
	            AlertDialog.Builder builder = new AlertDialog.Builder(this);
	            builder.setTitle("��ѡ��");
	            builder.setItems(items, new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int item) {
	                    //Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
	                    edit_search.setText(items[item]);
	                }
	            });
	           AlertDialog alert = builder.create();
	            alert.show();
	            
	           //Toast.makeText(this, items.toString(), 1).show();  
	       }  
	       
	   }  
	   @Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub

			if (keyCode == KeyEvent.KEYCODE_BACK) {
				if(mProgressDialog!=null){
				mProgressDialog.dismiss();
				}
				AlertDialog.Builder builder = new AlertDialog.Builder(
						DidaActivity.this);
				builder.setIcon(R.drawable.bee);
				builder.setTitle("��ȷ���˳���");
				builder.setPositiveButton("ȷ��",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								DidaActivity.this.finish();
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

}
