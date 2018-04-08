package com.android.flypigeon.home;

import java.util.ArrayList;
import java.util.Map;

import com.android.flypigeon.R;
import com.android.flypigeon.service.MainService;
import com.android.flypigeon.util.Constant;
import com.android.flypigeon.util.FileName;
import com.android.flypigeon.util.FileState;
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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FlyPigeonMainActivity extends Activity implements View.OnClickListener{
	private ExpandableListView ev = null;
	private String[] groupIndicatorLabeles = null;
	private SettingDialog settingDialog = null;
	private MyBroadcastRecv broadcastRecv = null;
	private IntentFilter bFilter = null;
	private ArrayList<Map<Integer,Person>> children = null;
	private ArrayList<Integer> personKeys = null;
	private MainService mService = null;
	private Intent mMainServiceIntent = null;
	private ExListAdapter adapter = null;
	private Person me = null;
	private Person person = null;
	private AlertDialog dialog = null;
	private boolean isPaused = false;//判断本身是不是可见
	private boolean isRemoteUserClosed = false; //是否远程用户已经关闭了通话。 
	private ArrayList<FileState> receivedFileNames = null;//接收到的对方传过来的文件名
	private ArrayList<FileState> beSendFileNames = null;//发送到对方的文件名信息
	/**
	 * MainService服务与当前Activity的绑定连接器
	 */
	private ServiceConnection sConnection = new ServiceConnection(){
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mService = ((MainService.ServiceBinder)service).getService();
			System.out.println("Service connected to activity...");
		}
		@Override
		public void onServiceDisconnected(ComponentName name) {
			mService = null;
			System.out.println("Service disconnected to activity...");
		}
	};
	
	private ReceiveSendFileListAdapter receiveFileListAdapter = new ReceiveSendFileListAdapter(this);
	private ReceiveSendFileListAdapter sendFileListAdapter = new ReceiveSendFileListAdapter(this);
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        groupIndicatorLabeles = getResources().getStringArray(R.array.groupIndicatorLabeles);
        
        //当前Activity与后台MainService进行绑定
        mMainServiceIntent = new Intent(this,MainService.class);
        bindService(mMainServiceIntent, sConnection, BIND_AUTO_CREATE);
        startService(mMainServiceIntent);
        
        ev = (ExpandableListView)findViewById(R.id.main_list);
        regBroadcastRecv();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.main_menu, menu);
    	return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getOrder()){
    	case 1:
    		showSettingDialog();
    		break;
    	case 2:
    		break;
    	}
    	return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	isPaused = false;
    	getMyInfomation();
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	isPaused = true;
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	unregisterReceiver(broadcastRecv);
    	stopService(mMainServiceIntent);
		unbindService(sConnection);
    }    
//==============================ExpandableListView数据适配器===================================
    private class ExListAdapter extends BaseExpandableListAdapter implements OnLongClickListener{
    	private Context context = null;
    	
    	public ExListAdapter(Context context){
    		this.context = context;
    	}

        //获得某个用户对象
		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return children.get(groupPosition).get(personKeys.get(childPosition));
		}
		//获得用户在用户列表中的序号
		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return personKeys.get(childPosition);
		}
		//生成用户布局View
		@Override
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,ViewGroup parentView) {
			View view = null;
			if(groupPosition<children.size()){//如果groupPosition的序号能从children列表中获得一个children对象
				Person person = children.get(groupPosition).get(personKeys.get(childPosition));//获得当前用户实例
				view = getLayoutInflater().inflate(R.layout.person_item_layout, null);//生成List用户条目布局对象
				view.setOnLongClickListener(this);//添加长按事件
				view.setOnClickListener(FlyPigeonMainActivity.this);
				view.setTag(person);//添加一个tag标记以便在长按事件和点击事件中根据该标记进行相关处理
				view.setPadding(30, 0, 0, 0);//设置左边填充空白距离
				ImageView headIconView = (ImageView)view.findViewById(R.id.person_head_icon);//头像
				TextView nickeNameView = (TextView)view.findViewById(R.id.person_nickename);//昵称
				TextView loginTimeView = (TextView)view.findViewById(R.id.person_login_time);//登录时间
				TextView msgCountView = (TextView)view.findViewById(R.id.person_msg_count);//未读信息计数
			//	TextView ipaddressView = (TextView)view.findViewById(R.id.person_ipaddress);//IP地址
				headIconView.setImageResource(person.personHeadIconId);
				nickeNameView.setText(person.personNickeName);
				loginTimeView.setText(person.loginTime);
				String msgCountStr = getString(R.string.init_msg_count);
				//根据用户id从service层获得该用户的消息数量
				msgCountView.setText(String.format(msgCountStr, mService.getMessagesCountById(person.personId)));
			//	ipaddressView.setText(person.ipAddress);
			}
			return view;
		}
		//获得某个用户组中的用户数
		@Override
		public int getChildrenCount(int groupPosition) {
			int childrenCount = 0;
			if(groupPosition<children.size())childrenCount=children.get(groupPosition).size();
			return childrenCount;
		}
		//获得媒个用户组对象
		@Override
		public Object getGroup(int groupPosition) {
			return children.get(groupPosition);
		}
		//获得用户组数量,该处的用户组数量返回的是组名称的数量
		@Override
		public int getGroupCount() {
			return groupIndicatorLabeles.length;
		}
		//获得用户组序号
		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}
		//生成用户组布局View
		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView,ViewGroup parent) {
			AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 60);
            TextView textView = new TextView(context);
            textView.setLayoutParams(lp);
            textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            textView.setPadding(50, 0, 0, 0);
            int childrenCount = 0;
            if(groupPosition<children.size()){//如果groupPosition序号能从children列表中获得children对象，则获得该children对象中的用户数量
            	childrenCount = children.get(groupPosition).size();
            }
			textView.setText(groupIndicatorLabeles[groupPosition]+"("+childrenCount+")");
			return textView;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

		/*当用户列表被长时间按下时会触发该事件*/
		@Override
		public boolean onLongClick(View view) {
			person = (Person)view.getTag();
			AlertDialog.Builder  builder = new AlertDialog.Builder(context);
			builder.setTitle(person.personNickeName);
			builder.setMessage(R.string.pls_select_opr);
			builder.setIcon(person.personHeadIconId);
			View vi = getLayoutInflater().inflate(R.layout.person_long_click_layout, null);
			builder.setView(vi);
			dialog = builder.show();

			Button sendMsgBtn = (Button)vi.findViewById(R.id.long_send_msg);
			sendMsgBtn.setTag(person);
			sendMsgBtn.setOnClickListener(FlyPigeonMainActivity.this);
			
			Button sendFileBtn = (Button)vi.findViewById(R.id.long_send_file);
			sendFileBtn.setTag(person);
			sendFileBtn.setOnClickListener(FlyPigeonMainActivity.this);
			
			Button callBtn = (Button)vi.findViewById(R.id.long_click_call);
			callBtn.setTag(person);
			callBtn.setOnClickListener(FlyPigeonMainActivity.this);
			
			Button cancelBtn = (Button)vi.findViewById(R.id.long_click_cancel);
			cancelBtn.setTag(person);
			cancelBtn.setOnClickListener(FlyPigeonMainActivity.this);
		
			return true;
		}
    }
    //=================================ExpandableListView数据适配器结束===================================================
    
    //获得自已的相关信息
    private void getMyInfomation(){
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
    	int iconId = pre.getInt("headIconId", R.drawable.black_bird);
    	String nickeName = pre.getString("nickeName", "输入你的昵称");
    	ImageView myHeadIcon = (ImageView)findViewById(R.id.my_head_icon);
    	myHeadIcon.setImageResource(iconId);
    	TextView myNickeName = (TextView)findViewById(R.id.my_nickename);
    	myNickeName.setText(nickeName);
    	me = new Person();
    	me.personHeadIconId = iconId;
    	me.personNickeName = nickeName;
    }

	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.myinfo_panel://弹出系统设置窗口
			showSettingDialog();
			break;
		case R.id.person_item_layout://转到发信息页面
			person = (Person)view.getTag();//用户列表的childView被点击时
			openChartPage(person);
			break;
		case R.id.long_send_msg://长按列表的childView时在弹出的窗口中点击"发送信息"按钮时
			person = (Person)view.getTag();
			openChartPage(person);
			if(null!=dialog)dialog.dismiss();
			break;
		case R.id.long_send_file:
			Intent intent = new Intent(this,MyFileManager.class);
			intent.putExtra("selectType", Constant.SELECT_FILES);
			startActivityForResult(intent, 0);
			dialog.dismiss();
			break;
		case R.id.long_click_call:
			person = (Person)view.getTag();
			AlertDialog.Builder  builder = new AlertDialog.Builder(this);
			builder.setTitle(me.personNickeName);
			String title = String.format(getString(R.string.talk_with), person.personNickeName);
			builder.setMessage(title);
			builder.setIcon(me.personHeadIconId);
			builder.setNegativeButton(getString(R.string.close), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface cdialog, int which) {
					cdialog.dismiss();
				}
			});
			final AlertDialog callDialog = builder.show();
			callDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface arg0) {
					mService.stopTalk(person.personId);
				}
			});
			mService.startTalk(person.personId);
			break;
		case R.id.long_click_cancel:
			dialog.dismiss();
			break;
		}
	}
	
	boolean finishedSendFile = false;//记录当前这些文件是不是本次已经接收过了
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK){
			if(null!=data){
				int selectType = data.getExtras().getInt("selectType");
				if(selectType == Constant.SELECT_FILE_PATH){//如果收到的是文件夹选择模式，说明现在是要保存对方传过来的文件，则把当前选择的文件夹路径返回服务层
					String fileSavePath = data.getExtras().getString("fileSavePath");
					if(null!=fileSavePath){
						mService.receiveFiles(fileSavePath);
						finishedSendFile = true;//把本次接收状态置为true
						System.out.println("over save file ...");
					}else{
						Toast.makeText(this, getString(R.string.folder_can_not_write), Toast.LENGTH_SHORT).show();
					}
				}else if(selectType == Constant.SELECT_FILES){//如果收到的是文件选择模式，说明现在是要发送文件，则把当前选择的所有文件返回给服务层。
					@SuppressWarnings("unchecked")
					final ArrayList<FileName> files = (ArrayList<FileName>)data.getExtras().get("files");
					mService.sendFiles(person.personId, files);//把当前选择的所有文件返回给服务层
					
					//显示文件发送列表
					beSendFileNames = mService.getBeSendFileNames();//从服务层获得所有需要接收的文件的文件名
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
					ListView lv = (ListView)vi.findViewById(R.id.receive_file_list);//需要接收的文件清单
					lv.setAdapter(sendFileListAdapter);
					Button btn_ok = (Button)vi.findViewById(R.id.receive_file_okbtn);
					btn_ok.setVisibility(View.GONE);
			        Button btn_cancle = (Button)vi.findViewById(R.id.receive_file_cancel);
			      //如果该按钮被点击则打开文件选择器，并设置成文件夹选择模式，选择一个用来接收对方文件的文件夹
			        btn_ok.setOnClickListener(new View.OnClickListener() {   
			            @Override  
			            public void onClick(View v) { 
			            	if(!finishedSendFile){//如果本次文件已经接收过了则不再打开文件夹选择器
				            	Intent intent = new Intent(FlyPigeonMainActivity.this,MyFileManager.class);
				    			intent.putExtra("selectType", Constant.SELECT_FILE_PATH);
				    			startActivityForResult(intent, 0);
			            	}
			            }   
				     });   
				    //如果该按钮被点击则向服务层发送用户拒绝接收文件的广播       
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
	
	//显示信息设置对话框
	private void showSettingDialog(){
		if(null==settingDialog)settingDialog = new SettingDialog(this);
		settingDialog.show();
	}
    
    //=========================广播接收器==========================================================
    private class MyBroadcastRecv extends BroadcastReceiver{
    	
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(Constant.updateMyInformationAction)){
				getMyInfomation();
			}else if(intent.getAction().equals(Constant.dataReceiveErrorAction) 
					|| intent.getAction().equals(Constant.dataSendErrorAction)){
				Toast.makeText(FlyPigeonMainActivity.this, intent.getExtras().getString("msg"), Toast.LENGTH_SHORT).show();
			}else if(intent.getAction().equals(Constant.fileReceiveStateUpdateAction)){//收到来自服务层的文件接收状态通知
				if(!isPaused){
					receivedFileNames = mService.getReceivedFileNames();//获得当前所有文件接收状态
					receiveFileListAdapter.setResources(receivedFileNames);
					receiveFileListAdapter.notifyDataSetChanged();//更新文件接收列表
				}
			}else if(intent.getAction().equals(Constant.fileSendStateUpdateAction)){//收到来自服务层的文件接收状态通知
				if(!isPaused){
					beSendFileNames = mService.getBeSendFileNames();//获得当前所有文件接收状态
					sendFileListAdapter.setResources(beSendFileNames);
					sendFileListAdapter.notifyDataSetChanged();//更新文件接收列表
				}
			}else if(intent.getAction().equals(Constant.receivedTalkRequestAction)){
				if(!isPaused){
					isRemoteUserClosed = false;
					final Person psn = (Person)intent.getExtras().get("person");
					String title = String.format(getString(R.string.talk_with), psn.personNickeName);
					AlertDialog.Builder  builder = new AlertDialog.Builder(FlyPigeonMainActivity.this);
					builder.setTitle(me.personNickeName);
					builder.setMessage(title);
					builder.setIcon(me.personHeadIconId);
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
							if(!isRemoteUserClosed){//如果远程用户未关闭通话，则向对方发送同意接收通话指令
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
			}else if(intent.getAction().equals(Constant.remoteUserClosedTalkAction)){
				isRemoteUserClosed = true;//如果接收到远程用户关闭通话指令则把该标记置为true
			}else if(intent.getAction().equals(Constant.remoteUserRefuseReceiveFileAction)){
				Toast.makeText(FlyPigeonMainActivity.this, getString(R.string.refuse_receive_file), Toast.LENGTH_SHORT).show();
			}else if(intent.getAction().equals(Constant.personHasChangedAction)){
				children = mService.getChildren();
				personKeys = mService.getPersonKeys();
				if(null==adapter){
					adapter = new ExListAdapter(FlyPigeonMainActivity.this);
			        ev.setAdapter(adapter);
			        ev.expandGroup(0);
			        ev.setGroupIndicator(getResources().getDrawable(R.drawable.all_bird));
		        }
		        adapter.notifyDataSetChanged();
			}else if(intent.getAction().equals(Constant.hasMsgUpdatedAction)){
				adapter.notifyDataSetChanged();
			}else if(intent.getAction().equals(Constant.receivedSendFileRequestAction)){//接收到文件发送请求，请求接收文件
				if(!isPaused){//如果自身处于可见状态则响应广播,弹出一个提示框是否要接收发过来的文件
					receivedFileNames = mService.getReceivedFileNames();//从服务层获得所有需要接收的文件的文件名
					if(receivedFileNames.size()<=0)return;
					receiveFileListAdapter.setResources(receivedFileNames);
					Person psn = (Person)intent.getExtras().get("person");
					AlertDialog.Builder  builder = new AlertDialog.Builder(context);
					builder.setTitle(psn.personNickeName);
					builder.setMessage(R.string.sending_file_to_you);
					builder.setIcon(psn.personHeadIconId);
					View vi = getLayoutInflater().inflate(R.layout.request_file_popupwindow_layout, null);
					builder.setView(vi);
					final AlertDialog recDialog = builder.show();
					recDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
						@Override
						public void onDismiss(DialogInterface arg0) {
							receivedFileNames.clear();
				        	if(!finishedSendFile){//如果本次文件并未接收就关闭接收窗口，说明放弃本次接收，同时向远程发送一个拒绝接收的指令。
					        	Intent intent = new Intent();
								intent.setAction(Constant.refuseReceiveFileAction);
								sendBroadcast(intent);
				        	}
				        	finishedSendFile = false;//关闭文件接收对话框，本表示本次文件接收完成，把本次文件接收状态置为false
						}
					});
					ListView lv = (ListView)vi.findViewById(R.id.receive_file_list);//需要接收的文件清单
					lv.setAdapter(receiveFileListAdapter);
					Button btn_ok = (Button)vi.findViewById(R.id.receive_file_okbtn);
			        Button btn_cancle = (Button)vi.findViewById(R.id.receive_file_cancel);
			      //如果该按钮被点击则打开文件选择器，并设置成文件夹选择模式，选择一个用来接收对方文件的文件夹
			        btn_ok.setOnClickListener(new View.OnClickListener() {   
			            @Override  
			            public void onClick(View v) { 
			            	if(!finishedSendFile){//如果本次文件已经接收过了则不再打开文件夹选择器
				            	Intent intent = new Intent(FlyPigeonMainActivity.this,MyFileManager.class);
				    			intent.putExtra("selectType", Constant.SELECT_FILE_PATH);
				    			startActivityForResult(intent, 0);
			            	}
			    		//	dialog.dismiss();
			            }   
				     });   
				    //如果该按钮被点击则向服务层发送用户拒绝接收文件的广播       
			        btn_cancle.setOnClickListener(new View.OnClickListener() {   
				        @Override  
				        public void onClick(View v) { 
				        	recDialog.dismiss();
				        }   
			        });
				}
			}
		}
    }
    //=========================广播接收器结束==========================================================
    
    
	//广播接收器注册
	private void regBroadcastRecv(){
        broadcastRecv = new MyBroadcastRecv();
        bFilter = new IntentFilter();
        bFilter.addAction(Constant.updateMyInformationAction);
        bFilter.addAction(Constant.personHasChangedAction);
        bFilter.addAction(Constant.hasMsgUpdatedAction);
        bFilter.addAction(Constant.receivedSendFileRequestAction);
        bFilter.addAction(Constant.remoteUserRefuseReceiveFileAction);
        bFilter.addAction(Constant.dataReceiveErrorAction);
        bFilter.addAction(Constant.dataSendErrorAction);
        bFilter.addAction(Constant.fileReceiveStateUpdateAction);
        bFilter.addAction(Constant.fileSendStateUpdateAction);
        bFilter.addAction(Constant.receivedTalkRequestAction);
        bFilter.addAction(Constant.remoteUserClosedTalkAction);
        registerReceiver(broadcastRecv, bFilter);
	}
	//打开发短信页面
	private void openChartPage(Person person){
		Intent intent = new Intent(this,ChartMsgActivity.class);
		intent.putExtra("person", person);
		intent.putExtra("me", me);
		startActivity(intent);
	}
}