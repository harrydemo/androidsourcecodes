package com.android.flypigeon.home;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.android.flypigeon.R;
import com.android.flypigeon.service.MainService;
import com.android.flypigeon.util.Constant;
import com.android.flypigeon.util.FileName;
import com.android.flypigeon.util.FileState;
import com.android.flypigeon.util.Message;
import com.android.flypigeon.util.Person;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class ChartMsgActivity extends Activity implements OnClickListener{
	private Person person = null;
	private Person me = null;
	private EditText chartMsg = null;
	private Button chartMsgSend = null;
	private Button chartMsgFile = null;
	private LinearLayout chartMsgPanel = null;
	private MainService mService = null;
	private Intent mMainServiceIntent = null;
	private MyBroadcastRecv broadcastRecv = null;
	private IntentFilter bFilter = null;
	private ScrollView chartMsgScroll = null;
	private boolean isPaused = false;//�жϱ����Ƿ�ɼ�
	private boolean isRemoteUserClosed = false; //�Ƿ�Զ���û��Ѿ��ر���ͨ���� 
	private ArrayList<FileState> receivedFileNames = null;//���յ��ĶԷ����������ļ���
	private ArrayList<FileState> beSendFileNames = null;//���͵��Է����ļ�����Ϣ
	private ReceiveSendFileListAdapter receiveFileListAdapter = new ReceiveSendFileListAdapter(this);
	private ReceiveSendFileListAdapter sendFileListAdapter = new ReceiveSendFileListAdapter(this);
	
	//�����ͻ��յ���Ϣ�����Ļ�������һ��
	private final Handler mHandler = new Handler();
    private Runnable scrollRunnable= new Runnable() {
	    @Override
	    public void run() {
            int offset = chartMsgPanel.getMeasuredHeight() - chartMsgScroll.getHeight();//�жϸ߶� 
            if (offset > 0) {
	            chartMsgScroll.scrollBy(0, 100);//ÿ�ι�100����λ
	        }
	    }
    };
    
	
	/**
	 * MainService�����뵱ǰActivity�İ�������
	 */
	private ServiceConnection sConnection = new ServiceConnection(){
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mService = ((MainService.ServiceBinder)service).getService();
			showMsg(person.personId);//����������ӳɹ����ȡ���û�����Ϣ����ʾ
			System.out.println("Service connected to activity...");
		}
		@Override
		public void onServiceDisconnected(ComponentName name) {
			mService = null;
			System.out.println("Service disconnected to activity...");
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_chart_layout);
		Intent intent = getIntent();
		person = (Person)intent.getExtras().getSerializable("person");
		me = (Person)intent.getExtras().getSerializable("me");
		
		((ImageView)findViewById(R.id.my_head_icon)).setImageResource(person.personHeadIconId);
		((TextView)findViewById(R.id.my_nickename)).setText(person.personNickeName);
		chartMsg = (EditText)findViewById(R.id.chart_msg);
		chartMsgSend = (Button)findViewById(R.id.chart_msg_send);
		chartMsgSend.setOnClickListener(this);
		chartMsgFile = (Button)findViewById(R.id.chart_msg_file);
		chartMsgFile.setOnClickListener(this);
		chartMsgPanel = (LinearLayout)findViewById(R.id.chart_msg_panel);
		chartMsgScroll = (ScrollView)findViewById(R.id.chart_msg_scroll);
		
		//��ǰActivity���̨MainService���а�
        mMainServiceIntent = new Intent(this,MainService.class);
        bindService(mMainServiceIntent, sConnection, BIND_AUTO_CREATE);
        regBroadcastRecv();
        
	}

	@Override
	public void onClick(View vi) {
		switch(vi.getId()){
		case R.id.chart_msg_send:
			String msg = chartMsg.getText().toString();
			if(null==msg || msg.length()<=0){
				Toast.makeText(this, getString(R.string.content_is_empty), Toast.LENGTH_SHORT).show();
				return;
			}
			chartMsg.setText("");
			View view = getLayoutInflater().inflate(R.layout.send_msg_layout, null);
			ImageView iView = (ImageView)view.findViewById(R.id.send_head_icon);
			TextView smcView = (TextView)view.findViewById(R.id.send_msg_content);
			TextView smtView = (TextView)view.findViewById(R.id.send_msg_time);
			TextView nView = (TextView)view.findViewById(R.id.send_nickename);
			iView.setImageResource(me.personHeadIconId);
			smcView.setText(msg);
			smtView.setText(new Date().toLocaleString());
			nView.setText(me.personNickeName);
			chartMsgPanel.addView(view);
			
			mService.sendMsg(person.personId, msg);
			mHandler.post(scrollRunnable);
			break;
		case R.id.chart_msg_file:
			Intent intent = new Intent(this,MyFileManager.class);
			intent.putExtra("selectType", Constant.SELECT_FILES);
			startActivityForResult(intent, Constant.FILE_RESULT_CODE);
			break;
		}
	}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.chart_menu, menu);
    	return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getOrder()){
    	case 1:
    		Intent intent = new Intent(this,MyFileManager.class);
			intent.putExtra("selectType", Constant.SELECT_FILES);
			startActivityForResult(intent, Constant.FILE_RESULT_CODE);
    		break;
    	case 2:
    		AlertDialog.Builder  builder = new AlertDialog.Builder(this);
			builder.setTitle(me.personNickeName);
			String title = String.format(getString(R.string.talk_with), person.personNickeName);
			builder.setMessage(title);
			builder.setIcon(me.personHeadIconId);
			builder.setNegativeButton(getString(R.string.close), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface tdialog, int which) {
					tdialog.dismiss();
				}
			});
			final AlertDialog talkDialog = builder.show();
			talkDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface arg0) {
					mService.stopTalk(person.personId);
				}
			});
			mService.startTalk(person.personId);
    		break;
    	}
    	return super.onOptionsItemSelected(item);
    }
    
	//�����û�id��service���ø��û���Ϣ������ʾ����Ϣ
	private void showMsg(int userId){
		List<Message> msgs = mService.getMessagesById(userId);
		if(null!=msgs){
			while(msgs.size()>0){
				View view = getLayoutInflater().inflate(R.layout.received_msg_layout, null);
				ImageView iView = (ImageView)view.findViewById(R.id.received_head_icon);
				TextView smcView = (TextView)view.findViewById(R.id.received_msg_content);
				TextView smtView = (TextView)view.findViewById(R.id.received_msg_time);
				TextView nView = (TextView)view.findViewById(R.id.received_nickename);
				iView.setImageResource(person.personHeadIconId);
				Message msg = msgs.remove(0);
				smcView.setText(msg.msg);
				smtView.setText(msg.receivedTime);
				nView.setText(person.personNickeName);
				chartMsgPanel.addView(view);
				mHandler.post(scrollRunnable);
			}
		}
	}
	
	boolean finishedSendFile = false;//��¼��ǰ��Щ�ļ��ǲ��Ǳ����Ѿ����չ���
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK){
			if(null!=data){
				int selectType = data.getExtras().getInt("selectType");
				if(selectType == Constant.SELECT_FILE_PATH){//����յ������ļ���ѡ��ģʽ��˵��������Ҫ����Է����������ļ�����ѵ�ǰѡ����ļ���·�����ط����
					String fileSavePath = data.getExtras().getString("fileSavePath");
					if(null!=fileSavePath){
						mService.receiveFiles(fileSavePath);
						finishedSendFile = true;//�ѱ��ν���״̬��Ϊtrue
					}else{
						Toast.makeText(this, getString(R.string.folder_can_not_write), Toast.LENGTH_SHORT).show();
					}
				}else if(selectType == Constant.SELECT_FILES){//����յ������ļ�ѡ��ģʽ��˵��������Ҫ�����ļ�����ѵ�ǰѡ��������ļ����ظ�����㡣
					@SuppressWarnings("unchecked")
					final ArrayList<FileName> files = (ArrayList<FileName>)data.getExtras().get("files");
					mService.sendFiles(person.personId, files);//�ѵ�ǰѡ��������ļ����ظ������
					
					//��ʾ�ļ������б�
					beSendFileNames = mService.getBeSendFileNames();//�ӷ������������Ҫ���յ��ļ����ļ���
					if(beSendFileNames.size()<=0)return;
					sendFileListAdapter.setResources(beSendFileNames);
					AlertDialog.Builder  builder = new AlertDialog.Builder(this);
					builder.setTitle(me.personNickeName);
					builder.setMessage(R.string.start_to_send_file);
					builder.setIcon(me.personHeadIconId);
					View vi = getLayoutInflater().inflate(R.layout.request_file_popupwindow_layout, null);
					builder.setView(vi);
					final AlertDialog fileListDialog = builder.show();
					fileListDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
						@Override
						public void onDismiss(DialogInterface arg0) {
							beSendFileNames.clear();
				        	files.clear();
						}
					});
					ListView lv = (ListView)vi.findViewById(R.id.receive_file_list);//��Ҫ���յ��ļ��嵥
					lv.setAdapter(sendFileListAdapter);
					Button btn_ok = (Button)vi.findViewById(R.id.receive_file_okbtn);
					btn_ok.setVisibility(View.GONE);
			        Button btn_cancle = (Button)vi.findViewById(R.id.receive_file_cancel);
			      //����ð�ť���������ļ�ѡ�����������ó��ļ���ѡ��ģʽ��ѡ��һ���������նԷ��ļ����ļ���
			        btn_ok.setOnClickListener(new View.OnClickListener() {   
			            @Override  
			            public void onClick(View v) { 
			            	if(!finishedSendFile){//��������ļ��Ѿ����չ������ٴ��ļ���ѡ����
				            	Intent intent = new Intent(ChartMsgActivity.this,MyFileManager.class);
				    			intent.putExtra("selectType", Constant.SELECT_FILE_PATH);
				    			startActivityForResult(intent, 0);
			            	}
			            }   
				     });   
				    //����ð�ť������������㷢���û��ܾ������ļ��Ĺ㲥       
			        btn_cancle.setOnClickListener(new View.OnClickListener() {   
				        @Override  
				        public void onClick(View v) { 
				        	fileListDialog.dismiss();
				        }   
			        });
				}
			}
		}
	}
	
    //�㲥������
    private class MyBroadcastRecv extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(Constant.hasMsgUpdatedAction)){
				showMsg(person.personId);
			}else if(intent.getAction().equals(Constant.dataReceiveErrorAction) 
					|| intent.getAction().equals(Constant.dataSendErrorAction)){
				Toast.makeText(ChartMsgActivity.this, intent.getExtras().getString("msg"), Toast.LENGTH_SHORT).show();
			}else if(intent.getAction().equals(Constant.fileSendStateUpdateAction)){//�յ����Է������ļ�����״̬֪ͨ
				beSendFileNames = mService.getBeSendFileNames();//��õ�ǰ�����ļ�����״̬
				sendFileListAdapter.setResources(beSendFileNames);
				sendFileListAdapter.notifyDataSetChanged();//�����ļ������б�
			}else if(intent.getAction().equals(Constant.fileReceiveStateUpdateAction)){//�յ����Է������ļ�����״̬֪ͨ
				receivedFileNames = mService.getReceivedFileNames();//��õ�ǰ�����ļ�����״̬
				receiveFileListAdapter.setResources(receivedFileNames);
				receiveFileListAdapter.notifyDataSetChanged();//�����ļ������б�
			}else if(intent.getAction().equals(Constant.receivedTalkRequestAction)){
				if(!isPaused){
					isRemoteUserClosed = false;
					final Person psn = (Person)intent.getExtras().get("person");
					String title = String.format(getString(R.string.talk_with), psn.personNickeName);
					AlertDialog.Builder  builder = new AlertDialog.Builder(ChartMsgActivity.this);
					builder.setTitle(psn.personNickeName);
					builder.setMessage(title);
					builder.setIcon(psn.personHeadIconId);
					View vi = getLayoutInflater().inflate(R.layout.request_talk_layout, null);
					builder.setView(vi);
					final AlertDialog revTalkDialog = builder.show();
					revTalkDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
						@Override
						public void onDismiss(DialogInterface arg0) {
							mService.stopTalk(psn.personId);
						}
					});
					Button talkOkBtn = (Button)vi.findViewById(R.id.receive_talk_okbtn);
					talkOkBtn.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View okBtn) {
							if(!isRemoteUserClosed){//���Զ���û�δ�ر�ͨ��������Է�����ͬ�����ͨ��ָ��
								mService.acceptTalk(psn.personId);
								okBtn.setEnabled(false);
							}
						}
					});
					Button talkCancelBtn = (Button)vi.findViewById(R.id.receive_talk_cancel);
					talkCancelBtn.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View cancelBtn) {
							revTalkDialog.dismiss();
						}
					});
				}
			}else if(intent.getAction().equals(Constant.remoteUserRefuseReceiveFileAction)){
				Toast.makeText(ChartMsgActivity.this, getString(R.string.refuse_receive_file), Toast.LENGTH_SHORT).show();
			}else if(intent.getAction().equals(Constant.receivedSendFileRequestAction)){
				if(!isPaused){//��������ڿɼ�״̬����Ӧ�㲥,����һ����ʾ���Ƿ�Ҫ���շ��������ļ�
					receivedFileNames = mService.getReceivedFileNames();//�ӷ������������Ҫ���յ��ļ����ļ���
					receiveFileListAdapter.setResources(receivedFileNames);
					AlertDialog.Builder  builder = new AlertDialog.Builder(context);
					builder.setTitle(person.personNickeName);
					builder.setMessage(R.string.sending_file_to_you);
					builder.setIcon(person.personHeadIconId);
					View vi = getLayoutInflater().inflate(R.layout.request_file_popupwindow_layout, null);
					builder.setView(vi);
					final AlertDialog revFileDialog = builder.show();
					revFileDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
						@Override
						public void onDismiss(DialogInterface arg0) {
							receivedFileNames.clear();
				        	if(!finishedSendFile){//��������ļ���δ���վ͹رս��մ��ڣ�˵���������ν��գ�ͬʱ��Զ�̷���һ���ܾ����յ�ָ�
					        	Intent intent = new Intent();
								intent.setAction(Constant.refuseReceiveFileAction);
								sendBroadcast(intent);
				        	}
				        	finishedSendFile = false;//�ر��ļ����նԻ��򣬱���ʾ�����ļ�������ɣ��ѱ����ļ�����״̬��Ϊfalse
						}
					});
					ListView lv = (ListView)vi.findViewById(R.id.receive_file_list);//��Ҫ���յ��ļ��嵥
					lv.setAdapter(receiveFileListAdapter);
					Button btn_ok = (Button)vi.findViewById(R.id.receive_file_okbtn);
			        Button btn_cancle = (Button)vi.findViewById(R.id.receive_file_cancel);
			        
			        //����ð�ť���������ļ�ѡ�����������ó��ļ���ѡ��ģʽ��ѡ��һ���������նԷ��ļ����ļ���
			        btn_ok.setOnClickListener(new View.OnClickListener() {   
			            @Override  
			            public void onClick(View v) { 
			            	if(!finishedSendFile){//��������ļ��Ѿ����չ������ٴ��ļ���ѡ����
				            	Intent intent = new Intent(ChartMsgActivity.this,MyFileManager.class);
				    			intent.putExtra("selectType", Constant.SELECT_FILE_PATH);
				    			startActivityForResult(intent, 0);
			            	}
			            }   
				     });   
			        
				    //����ð�ť������������㷢���û��ܾ������ļ��Ĺ㲥       
			        btn_cancle.setOnClickListener(new View.OnClickListener() {   
				        @Override  
				        public void onClick(View v) { 
				        	revFileDialog.dismiss();
				        }   
			        });
				}
			}
		}
    }
    
	//�㲥������ע��
	private void regBroadcastRecv(){
        broadcastRecv = new MyBroadcastRecv();
        bFilter = new IntentFilter();
        bFilter.addAction(Constant.hasMsgUpdatedAction);
        bFilter.addAction(Constant.receivedSendFileRequestAction);
        bFilter.addAction(Constant.fileReceiveStateUpdateAction);
        bFilter.addAction(Constant.fileSendStateUpdateAction);
        bFilter.addAction(Constant.receivedTalkRequestAction);
        registerReceiver(broadcastRecv, bFilter);
	}	
	
	@Override
	protected void onResume() {
		super.onResume();
		isPaused = false;
	}
	@Override
	protected void onPause() {
		super.onPause();
		isPaused = true;
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(sConnection);
		unregisterReceiver(broadcastRecv);
	}
}
