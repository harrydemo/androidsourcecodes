/*
 *  http://www.guet.edu.cn/
 *  by hmg25 20111212
 *  Just For Learning
 */
package com.guet.SiriCN;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.guet.SiriCN.SiriAction.VoiceAction;
import com.hmg.SiriCN.R;
import com.iflytek.speech.RecognizerResult;
import com.iflytek.speech.SpeechError;
import com.iflytek.ui.RecognizerDialog;
import com.iflytek.ui.RecognizerDialogListener;
import com.iflytek.ui.UploadDialogListener;

public class MainActivity extends Activity implements OnItemClickListener ,OnClickListener{
	public static final int DIALOG_DETAILS = 0;
	public static final int DIALOG_NONETWORK = 1;
	public static final int DIALOG_DOWNLOAD = 2;
	protected static final int CHAT_MODE = 1;
	protected static final int QA_MODE = 2;
	
	protected static final int MENU_MODE = Menu.FIRST;
	protected static final int MENU_ABOUT = Menu.FIRST+1;
	protected static final int MENU_EXIT = Menu.FIRST+2;
	protected static final int MENU_CLEAR = Menu.FIRST+3;
	protected static final int MENU_SWITCH_INPUT = Menu.FIRST+4;
	
	private ListView mListView;
	private ArrayList<SiriListItem> list;
	private ImageButton speakButton;
	private ImageButton sendButton;
	private ImageButton voiceButton;
	private EditText editMsgView;
    private String mProvider;
	RecognizerDialog isrDialog;
	VoiceActionListener mVoiceListener;
	SiriEngine mSiriEngine;
	ProgressDialog mProgressDialog;
	RelativeLayout mContainer,mEditBar,mVoiceBar;
	SiriAction mSiriAction;
	MediaPlayer player;
	ChatMsgViewAdapter mAdapter;
	Context mContext;
	boolean isChatMode = true;
	boolean isHasTTS=true;
	boolean isVoiceBarMode=true;
	boolean isXiaoIcanUse=true;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.main);

		mContext = this;
		if (Util.CheckNetwork(mContext)){ 
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setMessage("正在初始化，请稍候…… ^_^");
		mProgressDialog.setCancelable(false);
		mProgressDialog.show();
		init();
		}else{
			showDialog(DIALOG_NONETWORK);
		}
	}

	private void init() {
		   
			mSiriEngine = new SiriEngine(this);
			mSiriAction = new SiriAction();
			list = new ArrayList<SiriListItem>();
			mAdapter = new ChatMsgViewAdapter(this, list);
			mListView = (ListView) findViewById(R.id.list);
			mListView.setAdapter(mAdapter);
			mListView.setOnItemClickListener(this);
			mListView.setFastScrollEnabled(true);
			registerForContextMenu(mListView);

			mContainer=(RelativeLayout)findViewById(R.id.container);
			mEditBar=(RelativeLayout)findViewById(R.id.edit_bottombar);
			mEditBar.setVisibility(View.GONE);
			mVoiceBar=(RelativeLayout)findViewById(R.id.speak_bottombar);
			
			sendButton= (ImageButton)findViewById(R.id.btn_msg_send);
			sendButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					String msgText =editMsgView.getText().toString();
					if (msgText.length()>0) {
						MessageHandle(msgText);	
						editMsgView.setText("");
						editMsgView.clearFocus();
						//close ime
						InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
						imm.hideSoftInputFromWindow(editMsgView.getWindowToken(), 0);
					}else
					Toast.makeText(MainActivity.this, "发送内容不能为空！", Toast.LENGTH_SHORT).show();
				}
			});
			voiceButton = (ImageButton)findViewById(R.id.speak_input);
			voiceButton.setOnClickListener(this);
			editMsgView= (EditText)findViewById(R.id.MessageText);	
			editMsgView.clearFocus();
			speakButton = (ImageButton)findViewById(R.id.voice_input);
			speakButton.setOnClickListener(this);
			new InitThread().start();		
			
	}	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		mSiriEngine.SiriStopSpeak();
		player = MediaPlayer.create(MainActivity.this, R.raw.begin);
		player.start();
		isrDialog.show();
	}
	
	
	private void switchChatMode(boolean chatmode) {
		isChatMode = chatmode;
		if(isChatMode){
			if(isXiaoIcanUse){
		   mContainer.setBackgroundResource(R.drawable.bg_chat);
		   Toast.makeText(mContext, "当前为聊天模式", Toast.LENGTH_SHORT).show();
			}else{
			Toast.makeText(mContext, "聊天模式异常，不能切换", Toast.LENGTH_SHORT).show();	
			}
		}else{
		mContainer.setBackgroundResource(R.drawable.bg_alice);
		Toast.makeText(mContext, "当前为问答模式", Toast.LENGTH_SHORT).show();
		}
		mSiriEngine.setSiriMode(chatmode);
	}

	public void registerVoiceListener(VoiceActionListener listener) {
		mVoiceListener = listener;
	}

	public void unregisterVoiceListener() {
		mVoiceListener = null;
	}

	RecognizerDialogListener recoListener = new RecognizerDialogListener() {
		String text = "";

		@Override
		public void onResults(ArrayList<RecognizerResult> results,
				boolean isLast) {
			text = results.get(0).text;
		}

		@Override
		public void onEnd(SpeechError error) {
			if (error == null) {
				if (text != "")
					MessageHandle(text);
				else
					speak("我没有听清楚 你再说一遍好吗？", true);
			} else
				isrDialog.show();
		}
	};

	

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 2011:			 
				 if((Integer)msg.obj==SiriEngine.ERROR_XIAOI_INIT) {
					isXiaoIcanUse=false;
					switchChatMode(false);
					Toast.makeText(MainActivity.this, "聊天模式网络异常，将自己切换至问答模式",
							Toast.LENGTH_LONG).show();
				 }
				 
				if((Integer)msg.obj==SiriEngine.ERROR_NO_TTS){					
					isHasTTS=false;
					mProgressDialog.dismiss();					
					showDialog(DIALOG_DOWNLOAD);				    
				}
				
				//以下appid需要自己去科大讯飞网站申请，请勿使用默认的进行商业用途
					isrDialog = new RecognizerDialog(MainActivity.this,
							"appid=4eb35803");
					isrDialog.setEngine("sms", null, null);
					isrDialog.setListener(recoListener);
					

					/*Window w = isrDialog.getWindow();
					WindowManager.LayoutParams lp = w.getAttributes();
					lp.alpha = 0.9f;
					w.setAttributes(lp);

					ArrayList<String> list = getContactName();
					String keys = "";
					if (list != null) {
						UploadDialog uploadDialog = new UploadDialog(
								MainActivity.this, "appid=4eb35803");
						uploadDialog.setListener(uploadListener);

						for (String key : list) {
							if (keys != "")
								keys = keys + ",";
							keys = keys + key;
						}

						try {
							uploadDialog.setContent(keys.getBytes("UTF-8"),
									"dtt=keylist", "contact");
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						uploadDialog.show();
					}
*/
					mProgressDialog.dismiss();
					player = MediaPlayer.create(MainActivity.this, R.raw.lock);
					player.start();
					speak("初始化完成 欢迎使用", true);
				break;
			case 2012:
				String spkMsg = (String)msg.obj;
				if (isChatMode&&spkMsg.length()>0) {
					spkMsg = spkMsg.replaceAll("(小i|xiaoi)", "Siri");
				}
				speak(spkMsg, true);
				break;
			case 2013:
				speak((String) msg.obj, true);
				break;
			}
			super.handleMessage(msg);
		}
	};

	private void interceptVoiceAction() {
		if (mSiriAction.action == VoiceAction.CALL) {
			CallAction action = new CallAction(mSiriAction.parameter[0],
					MainActivity.this);
			action.makeCall();
		} else if (mSiriAction.action == VoiceAction.OPENAPP) {
			OpenAppAction action = new OpenAppAction(mSiriAction.parameter[0],
					MainActivity.this);
			action.runApp();
		} else if (mSiriAction.action == VoiceAction.PLAY) {
			PlayAction action = new PlayAction(mSiriAction.parameter[0],
					MainActivity.this);
			action.Play();
		} else if (mSiriAction.action == VoiceAction.SEARCH) {
			SearchAction action = new SearchAction(mSiriAction.parameter[0],
					mSiriAction.parameter[1], MainActivity.this);
			action.Search();
		}
	}

	private void MessageHandle(String msg) {
		addToList(msg, false);
		msg = msg.replaceAll("。$", "");
		if (mVoiceListener == null) {
		//	Log.e("hmg", msg);
			if (mSiriAction.doAction(msg.trim())) {
				interceptVoiceAction();
			} else
				mSiriEngine.handlerAnswer(msg, handler);
		} else {
			mVoiceListener.onVoiceResult(msg);
		}
	}

	public void onDestroy() {
		super.onDestroy();
		if(mSiriEngine!=null)
		mSiriEngine.SiriStop();
	}

	public ArrayList getContactName() {
		// 取得ContentResolver
		ContentResolver content = getContentResolver();
		Uri uri = ContactsContract.Contacts.CONTENT_URI; // 联系人的URI
		Cursor cursor = content.query(uri, null, null, null, null);
		int contactCount = cursor.getCount(); // 获得联系人数		
		ArrayList contacts = new ArrayList(contactCount);
		if (cursor.moveToFirst()) {
			String disPlayName;
			// 循环遍历
			for (; !cursor.isAfterLast(); cursor.moveToNext()) {
				int displayNameColumn = cursor
						.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
				disPlayName = cursor.getString(displayNameColumn);
				contacts.add(disPlayName);
			}
			return contacts;
		}
		return null;
	}

	public String contact = null;
	private UploadDialogListener uploadListener = new UploadDialogListener() {
		@Override
		public void onDataUploaded(String contentID, String extendID) {
			contact = extendID;
		}

		@Override
		public void onEnd(SpeechError error) {
		}
	};

	class InitThread extends Thread {
		public void run() {
			Message message = new Message();
			message.what = 2011;
			message.obj = mSiriEngine.SiriInit();
			handler.sendMessage(message);
		}
	}

	public void speak(String msg, boolean isSiri) {
		addToList(msg, isSiri);
		if(isHasTTS)
		mSiriEngine.SiriSpeak(msg);
	}

	private void addToList(String msg, boolean isSiri) {
		list.add(new SiriListItem(msg, isSiri));
		mAdapter.notifyDataSetChanged();
		mListView.setSelection(list.size() - 1);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		// TODO Auto-generated method stub
		//if (!isChatMode) {
			SiriListItem item = list.get(pos);
			if (item.isSiri) {
				new CustomDialog(MainActivity.this,
						CustomDialog.DIALOG_DETAILS,"详细",item.message).show();
			}
	//	}
	}

	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, CHAT_MODE, 0, "聊天模式");
		menu.add(0, QA_MODE, 0, "问答模式");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case CHAT_MODE:
			switchChatMode(true);
			break;
		case QA_MODE:
			switchChatMode(false);
			break;
		default:
			return super.onContextItemSelected(item);
		}
		return true;
	}

	public boolean onCreateOptionsMenu(Menu menu) {
       menu.add(0, MENU_MODE, 0,  "切换到问答模式");    
       menu.add(0, MENU_CLEAR, 0, "清空聊天列表");
       menu.add(0,MENU_SWITCH_INPUT,0,"切换为手写输入");
       menu.add(0, MENU_ABOUT, 0, "帮助");
       menu.add(0, MENU_EXIT, 0,  "退出");
       return true;
    }

	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onPrepareOptionsMenu(menu);
		if(isChatMode)
			menu.getItem(0).setTitle("切换到问答模式");
		else
			menu.getItem(0).setTitle("切换到聊天模式");
		if(isVoiceBarMode)
			menu.getItem(2).setTitle("切换为文字输入");
		else
			menu.getItem(2).setTitle("切换为语音输入");
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		int item_id=item.getItemId();
		switch(item_id)
		{
		case MENU_MODE:
			switchChatMode(!isChatMode);
			break;
		case MENU_CLEAR:
			list.clear();
			mAdapter.notifyDataSetChanged();
			break;
		case MENU_ABOUT:
			String showtxt="帮助文件丢失！";
			InputStream is;
			try {
				is = getAssets().open("readme.txt");
				int size = is.available(); 
	            byte[] buffer = new byte[size];   
	             is.read(buffer);   
	             is.close();   
	             showtxt = new String(buffer, "GB2312"); 
			} catch (IOException e) {     
				    throw new RuntimeException(e);   
	       }   
			new CustomDialog(MainActivity.this,
					CustomDialog.DIALOG_DETAILS,"关于",showtxt).show();			
			break;
		case MENU_EXIT:
			MainActivity.this.finish();
			break;
		case MENU_SWITCH_INPUT:
			if(isVoiceBarMode)
			{
				mVoiceBar.setVisibility(View.GONE);
				mEditBar.setVisibility(View.VISIBLE);
			}else{
				mVoiceBar.setVisibility(View.VISIBLE);
				mEditBar.setVisibility(View.GONE);
			}
			isVoiceBarMode=!isVoiceBarMode;
		    break;
		default:
            return super.onOptionsItemSelected(item);
		}
		return true;
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		 Dialog dialog = null;
		    switch (id) {
		    
		    case DIALOG_NONETWORK:
		    	return new AlertDialog.Builder(this).setTitle("注意").setMessage("网络没有连接，请检查您的网络！")
				.setIcon(android.R.drawable.ic_dialog_alert)
		    	.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						MainActivity.this.finish();					
					}
				}).setOnKeyListener(new DialogInterface.OnKeyListener() {
					public boolean onKey(DialogInterface dialog, int KeyCode,
							KeyEvent event) {
						switch (KeyCode) {
						case KeyEvent.KEYCODE_BACK:
							if(isHasTTS)
							MainActivity.this.finish();
							break;
						default:
							break;
						}
						return true;
					}
				}).create();
		    	
		    case DIALOG_DOWNLOAD:
		    return new AlertDialog.Builder(mContext)
				.setTitle("提示").setMessage("您没有安装捷通语音TTS，不能正常朗读，请自行下载安装！")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setPositiveButton("下载", new  DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					    Uri uri=Uri.parse("market://search?q=pname:com.sinovoice.sharedtts");
						Intent i=new Intent(Intent.ACTION_VIEW,uri);
					    mContext.startActivity(i);
					}
				}).setNegativeButton("取消",null).create();
			default:
		return super.onCreateDialog(id);
	   }
	}
	
	public class SiriListItem {
		String message;
		boolean isSiri;

		public SiriListItem(String msg, boolean siri) {
			message = msg;
			isSiri = siri;
		}
	}
}