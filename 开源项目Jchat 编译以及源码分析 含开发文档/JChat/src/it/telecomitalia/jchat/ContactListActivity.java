package it.telecomitalia.jchat;

import jade.android.ConnectionListener;
import jade.android.JadeGateway;
import jade.core.Profile;
import jade.imtp.leap.JICP.JICPProtocol;
import jade.util.Logger;
import jade.util.leap.Properties;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.TabHost.TabSpec;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

/**
 * The main activity. Shows two tabs: one with contact list (with distance from
 * current contact) and the second showing contacts locations on map.
 * <p>
 * It basically manages application initialization and shutdown, UI, receivers
 * and prepares Jade Android add-on sending data to the agent for GUI update
 * 主要活动。显示了两个标签：联系人列表（距离  *目前接触）和第二接触地图上的位置。  *
 *  它基本上是管理应用程序的初始化和关机，用户界面​​，接收器  *和准备玉Android的GUI更新的数据发送到代理
 */

//接触者列表  启动的首页
public class ContactListActivity extends MapActivity implements
		ConnectionListener
{

	/**
	 * Id of the context menu chat item.上下文菜单聊天项目的ID。
	 */
	private final int CONTEXT_MENU_ITEM_CHAT_LIST = Menu.FIRST;

	/**
	 * Id of the context menu call item.上下文菜单中调用项目的ID。
	 */
	private final int CONTEXT_MENU_ITEM_CALL_LIST = Menu.FIRST + 1;

	/**
	 * Id of the context menu sms item.消息的上下文菜单的ID。
	 */
	private final int CONTEXT_MENU_ITEM_SMS_LIST = Menu.FIRST + 2;

	/**
	 * Id of the map context menu chat item.地图上下文菜单聊天项目的ID
	 */
	private final int CONTEXT_MENU_ITEM_CHAT_MAP = Menu.FIRST + 3;

	/**
	 * Id of the map context menu call item.地图上下文菜单中调用项目的ID。
	 */
	private final int CONTEXT_MENU_ITEM_CALL_MAP = Menu.FIRST + 4;

	/**
	 * Id of the map context menu sms item.地图上下文菜单中消息的ID。
	 */
	private final int CONTEXT_MENU_ITEM_SMS_MAP = Menu.FIRST + 5;

	/**
	 * Id of contact tab接触标签的ID
	 */
	private final String CONTACTS_TAB_TAG = "ContTab";

	/**
	 * Id of mapview tab
	 */
	private final String MAPVIEW_TAB_TAG = "MapViewTab";

	/**
	 * Value returned by the <code>ChatActivity</code> when closed.
	 */
	public static final int CHAT_ACTIVITY_CLOSED = 777;

	/**
	 * Key for retrieving the ActivityPendingResults from Intent 取回
	 */
	public static final String ID_ACTIVITY_PENDING_RESULT = "ID_ACTIVITY_PENDING_RESULT";

	/**
	 * The Jade gateway instance (retrieved after call to
	 * <code>JadeGateway.connect()</code>) 桥接实例
	 */
	private JadeGateway gateway;

	/**
	 * Jade Logger for logging purpose 针对桥接的目标输出
	 */
	private static final Logger myLogger = Logger
			.getMyLogger(ContactListActivity.class.getName());

	/**
	 * The main tab host. 多标签
	 */
	private TabHost mainTabHost;

	/**
	 * Alpha transformation performed when connecting to Jade 当桥接时 变换执行
	 */
	private AlphaAnimation backDissolve;

	/**
	 * The button for switching map/satellite mode in map view 地图或卫星模式方式
	 */
	private Button switchButton;

	/**
	 * The customized contacts list view. 自定义联系人列表视图。 多选列表
	 */
	private MultiSelectionListView contactsListView;

	/**
	 * The customized overlay we use to draw on the map. 在地图上显示的自定义的障碍物
	 */
	private ContactsPositionOverlay overlay;

	/**
	 * The map view  地图视图
	 */
	private MapView mapView;

	/**
	 * Custom dialog containing Jade connection parameters entered by the user.
	 * 桥连接参数的自定义对话框，由用户输入。
	 */
	private JadeParameterDialog parameterDialog;

	/**
	 * Updater for this activity.
	 */
	private GuiEventHandler activityHandler;

	/**
	 * Initializes the activity's UI interface. 初始化视图会话接口
	 */
	private void initUI()
	{
		//初始化界面
		// Setup the main tabhost
 		setContentView(R.layout.homepage);
		mainTabHost = (TabHost) findViewById(R.id.main_tabhost);
		mainTabHost.setup();

		// Fill the contacts tab
		TabSpec contactsTabSpecs = mainTabHost.newTabSpec(CONTACTS_TAB_TAG);
		TabSpec mapTabSpecs = mainTabHost.newTabSpec(MAPVIEW_TAB_TAG);

		contactsTabSpecs.setIndicator(
				getText(R.string.label_contacts_tab_indicator), getResources()
						.getDrawable(R.drawable.contact));
		contactsTabSpecs.setContent(R.id.contactstabview);

		mapTabSpecs.setIndicator(getText(R.string.label_map_tab_indicator),
				getResources().getDrawable(R.drawable.globemap));
		mapTabSpecs.setContent(R.id.maptabview);

		mainTabHost.addTab(contactsTabSpecs);
		mainTabHost.addTab(mapTabSpecs);
		Resources res = getResources();

		int[] colors = new int[]
		{ res.getColor(R.color.white), res.getColor(R.color.blue) };

		GradientDrawable contentGradient = new GradientDrawable(
				Orientation.LEFT_RIGHT, colors);

		View homeTab = (View) findViewById(R.id.contactstabview);
		homeTab.setBackgroundDrawable(contentGradient);
		View homeTab1 = (View) findViewById(R.id.maptabview);
		homeTab1.setBackgroundDrawable(contentGradient);

		// init the map view
		mapView = (MapView) findViewById(R.id.myMapView);

		mapView.setSatellite(false);

		mapView.setOnTouchListener(new View.OnTouchListener()
		{

			public boolean onTouch(View v, MotionEvent event)
			{
				// User has touched the screen, no action just take the time
				if (event.getAction() == MotionEvent.ACTION_DOWN)
				{
					int touchedX = (int) event.getX();
					int touchedY = (int) event.getY();
					overlay.checkClickedPosition(new Point(touchedX, touchedY));
				}

				return true;
			}

		});

		//菜单
		mapView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener()
		{
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenu.ContextMenuInfo menuInfo)
			{

				// Let the menu appear
				menu.add(Menu.NONE, CONTEXT_MENU_ITEM_CHAT_MAP, Menu.NONE,
						R.string.menu_item_chat);
				menu.add(Menu.NONE, CONTEXT_MENU_ITEM_CALL_MAP, Menu.NONE,
						R.string.menu_item_call);
				menu.add(Menu.NONE, CONTEXT_MENU_ITEM_SMS_MAP, Menu.NONE,
						R.string.menu_item_sms);
			}
		});

		List<Overlay> overlayList = mapView.getOverlays();
		overlay = new ContactsPositionOverlay(this, mapView, getResources());
		overlayList.add(overlay);

		// Button for switching map mode  切换地图模式
		switchButton = (Button) findViewById(R.id.switchMapBtn);
		switchButton.setOnClickListener(new View.OnClickListener()
		{

			public void onClick(View arg0)
			{
				Button clickedBtn = (Button) arg0;

				if (mapView.isSatellite())
				{
					clickedBtn.setText(ContactListActivity.this
							.getText(R.string.label_toggle_satellite));
					mapView.setSatellite(false);
				} 
				else
				{
					clickedBtn.setText(ContactListActivity.this
							.getText(R.string.label_toggle_map));
					mapView.setSatellite(true);
				}
			}

		});

		// Create the updater array

		// Select default tab  创建更新数组 选择默认的标签
		mainTabHost.setCurrentTabByTag(CONTACTS_TAB_TAG);

		contactsListView = (MultiSelectionListView) findViewById(R.id.contactsList);
		int[] selectorColors = new int[]
		{ res.getColor(R.color.light_green), res.getColor(R.color.dark_green) };
		GradientDrawable selectorDrawable = new GradientDrawable(
				Orientation.TL_BR, selectorColors);
		contactsListView.setSelector(selectorDrawable);

		registerForContextMenu(contactsListView);

		registerForContextMenu(mapView);

		contactsListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener()
				{
					public void onItemClick(AdapterView parent, View v,
							int position, long id)
					{
						CheckBox cb = (CheckBox) v
								.findViewById(R.id.contact_check_box);
						cb.setChecked(!cb.isChecked());
					}
				});

		//读取电话联系人
		ContactManager.getInstance().readPhoneContacts(this);
		contactsListView.setAdapter(ContactManager.getInstance().getAdapter());
		
		//自定义桥接参数赋值对话框
		parameterDialog = new JadeParameterDialog(this);
		// initializeContactList();

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo)
	{
		//菜单
		
		if (v instanceof MultiSelectionListView) //列表菜单
		{
			AdapterView.AdapterContextMenuInfo adaptMenuInfo = (AdapterContextMenuInfo) menuInfo;
			MultiSelectionListView myLv = (MultiSelectionListView) v;
			myLv.setSelection(adaptMenuInfo.position);

			List<String> checkedContacts = myLv.getAllSelectedItems();

			if (checkedContacts.size() > 0)
			{
				// Let the menu appear
				if (checkedContacts.size() == 1)
				{
					// only one contact selected
					Contact cc = ContactManager.getInstance().getContact(
							checkedContacts.get(0));
					// verify is he is online --> add Chat
					if (cc.isOnline())
					{
						menu.add(Menu.NONE, CONTEXT_MENU_ITEM_CHAT_LIST,
								Menu.NONE, R.string.menu_item_chat);
					}
					// add always sms and call
					menu.add(Menu.NONE, CONTEXT_MENU_ITEM_CALL_LIST, Menu.NONE,
							R.string.menu_item_call);
					menu.add(Menu.NONE, CONTEXT_MENU_ITEM_SMS_LIST, Menu.NONE,
							R.string.menu_item_sms);
				} 
				else
				{
					// more elements selected
					boolean allOnline = true;
					for (String contactId : checkedContacts)
					{
						Contact cc = ContactManager.getInstance().getContact(
								contactId);
						if (!cc.isOnline())
						{
							allOnline = false;
							break;
						}
					}
					if (allOnline)
						menu.add(Menu.NONE, CONTEXT_MENU_ITEM_CHAT_LIST,
								Menu.NONE, R.string.menu_item_chat);
					// otherwise only sms
					menu.add(Menu.NONE, CONTEXT_MENU_ITEM_SMS_LIST, Menu.NONE,
							R.string.menu_item_sms);
				}
			}
		} 
		else if (v instanceof MapView)  //地图菜单
		{
			menu.add(Menu.NONE, CONTEXT_MENU_ITEM_CHAT_MAP, Menu.NONE,
					R.string.menu_item_chat);
			menu.add(Menu.NONE, CONTEXT_MENU_ITEM_CALL_MAP, Menu.NONE,
					R.string.menu_item_call);
			menu.add(Menu.NONE, CONTEXT_MENU_ITEM_SMS_MAP, Menu.NONE,
					R.string.menu_item_sms);
		}
	}

	/**
	 * Executed at activity creation. Performs UI initialization, ContactManager
	 * initialization, start of contact location update.
	 * 执行活动的创造。执行用户界面初始化的ContactManager初始化，开始接触的位置更新。
	 * @param icicle
	 *            Saved state if the application has been frozen (not used)
	 */
	public void onCreate(Bundle icicle)
	{

		myLogger.log(Logger.FINE,
				"onReceiveIntent called: My currentThread has this ID: "
						+ Thread.currentThread().getId());
		super.onCreate(icicle);

		try
		{
			// 新建接触者的适配器
			ContactListAdapter cla = new ContactListAdapter(this);
			ContactManager.getInstance().addAdapter(cla);

			// 会话通知创建
			ChatSessionNotificationManager.create(this);

			// 初始化UI
			initUI();

			// register an event for this activity to handle refresh of the
			// views in
			// this activity
			// 注册事件为这项活动在此活动的意见处理刷新
			activityHandler = new ContactListActivityUpdateHandler();

			// register a generic disconnection handler 注册一个生成失去连接的处理
			MsnEventMgr.getInstance().registerEvent(
					MsnEvent.CONTACT_DISCONNECT_EVENT,
					new ContactDisconnectionHandler());
			// Initialize the UI

			//禁止某些界面
			//disableUI();
		} 
		catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * Called any time this activity is shown. Resets contact list and map view
	 * removing all checked contacts Registers handlers for both the views.
	 * 调用任何显示此活动的时间。复位联系人列表和地图视图中删除所有选中的联系人寄存器双方意见的处理程序。
	 */
	protected void onResume()
	{
		super.onResume();
		myLogger.log(Logger.FINE,
				"On Resume was called!!!: setting event handler in ContactListActivity");
		
		//注册一个刷新事件 消息事件
		MsnEventMgr.getInstance().registerEvent(MsnEvent.VIEW_REFRESH_EVENT,
				activityHandler);
		MsnEventMgr.getInstance().registerEvent(
				MsnEvent.INCOMING_MESSAGE_EVENT, activityHandler);
		
		//解除所有接触者
		this.overlay.uncheckAllContacts();
		this.contactsListView.uncheckAllSelectedItems();
	}

	/**
	 * Enables the main view after the application is connected to JADE 主视图能够连接桥
	 */
	//使界面可用
	private void enableUI()
	{
		View mainView = findViewById(R.id.main_view);
		backDissolve = new AlphaAnimation(0.0f, 1.0f);
		backDissolve.setDuration(3000);

		mainView.setAnimation(backDissolve);
		mainView.setVisibility(View.VISIBLE);

		TabWidget widget = mainTabHost.getTabWidget();
		widget.setEnabled(true);
	}

	/**
	 * Disables the UI at the beginning 刚开始时 禁止这UI
	 */
	//使界面不可用
	private void disableUI()
	{
		View mainView = findViewById(R.id.main_view);
		mainView.setVisibility(View.INVISIBLE);

		TabWidget widget = mainTabHost.getTabWidget();
		widget.setEnabled(false);
	}

	// Retrieve the JADE properties from Dialog or configuration file
	// 从对话或配置文件中检索桥属性
	/**
	 * Retrieve the jade properties, needed to connect to the JADE main
	 * container.
	 * <p>
	 * These properties are:
	 * <ul>
	 * <li> <code>MAIN_HOST</code>: hostname or IP address of the machine on
	 * which the main container is running. Taken from resource file or settings
	 * dialog.
	 * <li> <code>MAIN_PORT</code>: port used by the JADE main container. Taken
	 * from resource file or settings dialog.
	 * <li> <code>MSISDN_KEY</code>: name of the JADE agent (the phone number)
	 * </ul>
	 * 
	 * @return the jade properties
	 */
	//返回桥接的属性
	private Properties getJadeProperties()
	{
		// fill Jade connection properties 填充桥接属性
		Properties jadeProperties = new Properties();
		
		JChatApplication app = (JChatApplication) getApplication();
		jadeProperties.setProperty(Profile.MAIN_HOST,
				app.getProperty(JChatApplication.JADE_DEFAULT_HOST));
		jadeProperties.setProperty(Profile.MAIN_PORT,
				app.getProperty(JChatApplication.JADE_DEFAULT_PORT));
		jadeProperties.setProperty(JICPProtocol.MSISDN_KEY,
				app.getProperty(JChatApplication.PREFERENCE_PHONE_NUMBER));
		return jadeProperties;
	}

	/**
	 * Handles the shutdown of the application when exiting. Stops location
	 * update, removes all notifications from status bar, shuts Jade down and
	 * clears managers 
	 * 处理退出时关闭的应用程序。停止位置更新，删除所有的通知，从状态栏，桥接关闭，并清除管理
	 */
	protected void onDestroy()
	{
		//程序关闭时 触发
		GeoNavigator.getInstance(this).stopLocationUpdate();
		myLogger.log(Logger.INFO, "onDestroy called ...");

		ChatSessionNotificationManager.getInstance().removeAllNotifications();

		if (gateway != null)
		{
			try
			{
				gateway.shutdownJADE();
			} 
			catch (ConnectException e)
			{
				e.printStackTrace();
			} 
			catch (Exception e)
			{
				e.printStackTrace();
			}
			gateway.disconnect(this);
		}

		MsnSessionManager.getInstance().shutdown();
		ContactManager.getInstance().shutdown();
		// Debug.stopMethodTracing();
		super.onDestroy();
	}

	/**
	 * Callback methods called when connection to Android add-on's
	 * MicroRuntimeService is completed Provides an instance of the
	 * {@link JadeGateway} that is stored and sends the updater to the agent
	 * using JadeGateway.execute() making it able to update the GUI.
	 * 
	 * *回调方法被称为连接到Android时附加上的 MicroRuntimeService完成提供的实例 {@链接JadeGateway
	 * }存储和发送的更新代理 的JadeGateway.execute（）使它能够更新GUI。
	 * 
	 * @param gw
	 *            instance of the JadeGateway returned after the call to
	 *            <code>JadeGateway.connect()</code>
	 */
	//JadeGateWay里面的回调方法
	public void onConnected(JadeGateway gw)
	{
		this.gateway = gw;

		enableUI();
		try
		{
			this.gateway.execute("");
		} 
		catch (StaleProxyException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ControllerException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		myLogger.log(Logger.INFO, "onConnected(): SUCCESS!");
	}

	/**
	 * Method of {@link ConnectionListener} interface, called in case the
	 * service got disconnected. Currently not implemented. *方法
	 * {@链接ConnectionListener接口，
	 * 以防被称为服务断开了。目前尚未实现。
	 */
	public void onDisconnected()
	{
		myLogger.log(Logger.INFO, "onDisconnected(): SUCCESS!");
	}

	/**
	 * Creates the application main menu
	 * 
	 * @see Activity
	 */
	//创建选项菜单
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		// Inflate the currently selected menu XML resource.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.title_icon, menu);
		return true;
	}

	/**
	 * Called any time the application main menu is displayed
	 * 调用任何应用程序的主菜单显示时间
	 * @see Activity
	 */
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		MenuItem menuItemConnect = menu.findItem(R.id.connect);
		menuItemConnect.setVisible(gateway == null);
		MenuItem menuItemSettings = menu.findItem(R.id.settings);
		menuItemSettings.setVisible((gateway == null));
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Handles the selection on items in main menu
	 *	处理菜单消息
	 * @see Activity
	 */
	public boolean onMenuItemSelected(int featureId, MenuItem item)
	{
		super.onMenuItemSelected(featureId, item);
		switch (item.getItemId())
		{
		case R.id.exit:
			finish();
			break;
		case R.id.settings:
			parameterDialog.show();
			break;
		case R.id.connect:
			// try to get a JadeGateway 连接
			try
			{
				JChatApplication app = (JChatApplication) getApplication();
				ContactManager
						.getInstance()
						.addMyContact(
								app.getProperty(JChatApplication.PREFERENCE_PHONE_NUMBER));

				GeoNavigator.setLocationProviderName(app
						.getProperty(JChatApplication.LOCATION_PROVIDER));
				GeoNavigator.getInstance(this).startLocationUpdate();

				initializeContactList();

				// fill Jade connection properties
				Properties jadeProperties = getJadeProperties();
				JadeGateway.connect(MsnAgent.class.getName(), new String[]
				{ getText(R.string.contacts_update_time).toString() },
						jadeProperties, this, this);
			} 
			catch (Exception e)
			{
				// troubles during connection
				Toast.makeText(this,
						getString(R.string.error_msg_jadegw_connection),
						Integer.parseInt(getString(R.string.toast_duration)))
						.show();
				myLogger.log(Logger.SEVERE, "Error in onCreate", e);
				
				e.printStackTrace();
			}
			break;
		}
		return true;
	}

	/**
	 * Handles the context menu selection both in map and in contact view
	 * 处理地图和联系人视图中的上下文菜单中选择
	 * 
	 * @see Activity
	 */
	//列表选项菜单事件
	public boolean onContextItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case CONTEXT_MENU_ITEM_CALL_LIST:
		{
			List<String> selectedIds = contactsListView.getAllSelectedItems();
			if (selectedIds.size() == 1)
			{
				callContact(selectedIds.get(0));
			} else
			{
				Toast.makeText(this, R.string.error_msg_multiple_phonecalls,
						2000).show();
			}

		}
			break;

		case CONTEXT_MENU_ITEM_CALL_MAP:
		{
			List<String> selectedIds = overlay.getSelectedItems();
			if (selectedIds.size() == 1)
			{
				callContact(selectedIds.get(0));
			} 
			else
			{
				Toast.makeText(this, R.string.error_msg_multiple_phonecalls,
						2000);
			}
		}
			break;

		case CONTEXT_MENU_ITEM_CHAT_LIST:
		{
			List<String> participantIds = contactsListView
					.getAllSelectedItems();
			launchChatSession(participantIds);
		}
			break;

		case CONTEXT_MENU_ITEM_CHAT_MAP:
		{
			List<String> participantIds = overlay.getSelectedItems();
			launchChatSession(participantIds);
		}
			break;

		case CONTEXT_MENU_ITEM_SMS_LIST:
		{
			ArrayList<String> participantIds = (ArrayList<String>) contactsListView
					.getAllSelectedItems();
			Intent i = new Intent();
			i.setClass(this, SendSMSActivity.class);
			i.putExtra(SendSMSActivity.SMS_ADDRESS_LIST, participantIds);
			startActivity(i);
		}

			break;

		case CONTEXT_MENU_ITEM_SMS_MAP:
		{
			ArrayList<String> participantIds = (ArrayList<String>) overlay
					.getSelectedItems();
			// SMSDialog smsDialog = new SMSDialog(this,participantIds);
			Intent i = new Intent();
			i.setClass(this, SendSMSActivity.class);
			i.putExtra(SendSMSActivity.SMS_ADDRESS_LIST, participantIds);
			startActivity(i);
		}

			break;
		default:
		}
		return false;
	}

	/**
	 * Performs a fake phone call to a given contact. Android phone service is
	 * used 执行一个给定的接触者电话。使用Android手机服务
	 * 
	 * @param selectedCPhoneNumber
	 *            phone number of desired contact
	 */
	//打电话
	private void callContact(String selectedCPhoneNumber)
	{
		Intent i = new Intent(Intent.ACTION_CALL);
		Uri phoneUri = Uri.parse("tel:" + selectedCPhoneNumber);
		i.setData(phoneUri);
		startActivity(i);
	}

	/**
	 * Start a new chat session with the given participants. The session is
	 * registered with session manager and a new chat activity is launched
	 * giving the sessionId of the session as a parameter
	 * 
	 * 一个给定的参与者开始新的聊天会话。
	 * 会议注册与会话管理，并推出一个新的聊天活动作为一个参数给会话的SessionID
	 * 
	 * @param participantIds
	 *            list of phone numbers of the participants to this conversation
	 *            这次谈话的参与者的电话号码清单
	 */
	
	//运行聊天会话
	private void launchChatSession(List<String> participantIds)
	{
		// start a new session or retrieve it If the session already exists. its
		// Id is retrieved
		//启动一个新的会话，或检索它，如果已经存在的会话。它的ID检索
		String sessionId = MsnSessionManager.getInstance().startMsnSession(
				participantIds);

		// retrieve a copy of the session
		MsnSession session = MsnSessionManager.getInstance().retrieveSession(
				sessionId);
		// Add a notification for the new session
		ChatSessionNotificationManager.getInstance().addNewSessionNotification(
				sessionId);

		// packet an intent. We'll try to add the session ID in the intent data
		// in URI form
		// We use intent resolution here, cause the ChatActivity should be
		// selected matching ACTION and CATEGORY
		//包的意图。我们会尽量添加的URI形式的意图数据的会话ID，
		//我们在这里使用意图的决议，导致ChatActivity应选择匹配的行动和类别
		Intent it = new Intent(Intent.ACTION_MAIN);
		// set the data as an URI (content://sessionId#<sessionIdValue>)
		it.setData(session.getSessionIdAsUri());
		it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		it.setClass(this, ChatActivity.class);
		startActivity(it);
	}

	/**
	 * Updates the adapter associated with this contact list view. This method
	 * is run on the main thread but called periodically by the agent, giving a
	 * list of changes (new contacts or removed ones).
	 * 更新与此联系人的列表视图关联的适配器。
	 * 主线程上运行，但这种方法称为代理，定期提供名单的变化（新的接触或删除）。
	 * 
	 * @param changes
	 *            the list of changes (new contacts or contacts that went
	 *            offline and must be removed)变更清单（脱机，必须拆除的新联系人或联系人）
	 * @param contactMap
	 *            copy of the contact map that shall be used for this update (to
	 *            avoid racing conditions issues)副本，应使用此更新（避免赛车条件的问题接触图）
	 * @param contactLocMap
	 *            copy of the location map that shall be used for this update
	 *            (to avoid racing conditions issues)副本的位置图，应使用此更新（避免赛车条件的问题）
	 */
	private void updateListAdapter(ContactListChanges changes,
			Map<String, Contact> contactMap,
			Map<String, ContactLocation> contactLocMap)
	{
		ContactListAdapter adapter = ContactManager.getInstance().getAdapter();
		adapter.update(changes, contactMap, contactLocMap);
	}

	/**
	 * Initialize the list filling up its adapter with all the available data.
	 * After initialization all changes shall be incremental (contacts added,
	 * contacts removed) 
	 * 初始化列表，填补了所有可用的数据适配器。
	 * 初始化后，所有的变化应是渐进的（接触的增加，接触删除）
	 */
	private void initializeContactList()
	{
		ContactListAdapter adapter = ContactManager.getInstance().getAdapter();
		adapter.initialize();
		contactsListView.setAdapter(adapter);
		overlay.initializePositions();
	}

	/**
	 * Defines an handler to show a toast when a contact disconnects
	 * 定义一个处理程序，以显示时，触点断开
	 * 
	 * @author s.semeria
	 * 
	 */
	private class ContactDisconnectionHandler extends GuiEventHandler
	{

		/**
		 * Handles the disconnection event
		 * 
		 * @param event
		 *            the disconnection event to be handled
		 */
		protected void processEvent(MsnEvent event)
		{
			String eventName = event.getName();

			if (eventName.equals(MsnEvent.CONTACT_DISCONNECT_EVENT))
			{
				String discContactName = (String) event
						.getParam(MsnEvent.CONTACT_DISCONNECT_PARAM_CONTACTNAME);
				Toast.makeText(ContactListActivity.this,
						discContactName + " went offline!", 3000).show();
			}
		}

	}

	/**
	 * Handler for all events handled by the main {@link ContactListActivity}
	 * 处理所有的事件处理程序主要的 @ link ContactListActivity}
	 */
	private class ContactListActivityUpdateHandler extends GuiEventHandler
	{

		/**
		 * Handles both an incoming message and a refresh of the screen
		 * 处理传入消息和屏幕刷新
		 * 
		 * @param event
		 *            the event that should be handled
		 */
		protected void processEvent(MsnEvent event)
		{
			String eventName = event.getName();

			//事件匹配  以及处理
			if (eventName.equals(MsnEvent.VIEW_REFRESH_EVENT))
			{
				ContactListChanges changes = (ContactListChanges) event
						.getParam(MsnEvent.VIEW_REFRESH_PARAM_LISTOFCHANGES);
				Map<String, Contact> cMap = (Map<String, Contact>) event
						.getParam(MsnEvent.VIEW_REFRESH_CONTACTSMAP);
				Map<String, ContactLocation> cLocMap = (Map<String, ContactLocation>) event
						.getParam(MsnEvent.VIEW_REFRESH_PARAM_LOCATIONMAP);
				myLogger.log(
						Logger.INFO,
						"Thread "
								+ Thread.currentThread().getId()
								+ ":GUI thread retrieves this list of changes from the event: "
								+ changes.toString());

				updateListAdapter(changes, cMap, cLocMap);
				overlay.update(changes, cMap, cLocMap);
				mapView.invalidate();
				int selPos = contactsListView.getSelectedItemPosition();
				ContactListAdapter adapter = ContactManager.getInstance()
						.getAdapter();
				contactsListView.setAdapter(adapter);
				contactsListView.setSelection(selPos);
			} 
			else if (eventName.equals(MsnEvent.INCOMING_MESSAGE_EVENT))
			{
				myLogger.log(
						Logger.FINE,
						"Thread "
								+ Thread.currentThread().getId()
								+ ":Contact List activity received an INCOMING MSG EVENT!!!");
				MsnSessionMessage msnMsg = (MsnSessionMessage) event
						.getParam(MsnEvent.INCOMING_MESSAGE_PARAM_MSG);
				String sessionId = (String) event
						.getParam(MsnEvent.INCOMING_MESSAGE_PARAM_SESSIONID);
				// if the incoming msg is not for our session, post a
				// notification
				ChatSessionNotificationManager.getInstance()
						.addNewMsgNotification(sessionId, msnMsg);
				Toast.makeText(
						ContactListActivity.this,
						msnMsg.getSenderName() + " says: "
								+ msnMsg.getMessageContent(), 3000).show();
			}
		}

	}

	/*
	 * This method is needed to tell the Google server if we're showing info
	 * ????
	 * 这种方法需要告诉Google的服务器上，如果我们显示的信息
	 * @see com.google.android.maps.MapActivity#isRouteDisplayed()
	 */
	protected boolean isRouteDisplayed()
	{
		return false;
	}

}
