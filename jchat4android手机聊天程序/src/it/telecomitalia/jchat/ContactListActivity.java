/*****************************************************************
 jChat is a  chat application for Android based on JADE
  Copyright (C) 2008 Telecomitalia S.p.A. 
 
 GNU Lesser General Public License

 This is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation, 
 version 2.1 of the License. 

 This software is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this software; if not, write to the
 Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 Boston, MA  02111-1307, USA.
 *****************************************************************/

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
 * The main activity. Shows two tabs: one with contact list (with distance from current contact)
 * and the second showing contacts locations on map.
 * <p>
 * It basically manages application initialization and shutdown, UI, receivers and prepares Jade Android add-on sending data to the
 * agent for GUI update
 * 
 * @author Cristina Cuccè
 * @author Marco Ughetti 
 * @author Stefano Semeria
 * @author Tiziana Trucco
 * @version 1.0 
 */
public class ContactListActivity extends MapActivity implements
		ConnectionListener {

	
	/** 
	 * Id of the context menu chat item. 
	 */
	private final int CONTEXT_MENU_ITEM_CHAT_LIST = Menu.FIRST ;

	/** 
	 * Id of the context menu call item. 
	 */
	private final int CONTEXT_MENU_ITEM_CALL_LIST = Menu.FIRST + 1;

	/**
	 *  Id of the context menu sms item. 
	 */
	private final int CONTEXT_MENU_ITEM_SMS_LIST = Menu.FIRST + 2;

	/**
	 *  Id of the map context menu chat item. 
	 */
	private final int CONTEXT_MENU_ITEM_CHAT_MAP = Menu.FIRST + 3;

	/** 
	 * Id of the map context menu call item. 
	 */
	private final int CONTEXT_MENU_ITEM_CALL_MAP = Menu.FIRST + 4;

	/** 
	 * Id of the map context menu sms item. 
	 */
	private final int CONTEXT_MENU_ITEM_SMS_MAP = Menu.FIRST + 5;

	/** 
	 * Id of contact tab
	 */
	private final String CONTACTS_TAB_TAG = "ContTab";

	/** 
	 * Id of mapview tab 
	 */
	private final String MAPVIEW_TAB_TAG = "MapViewTab";

	/**
	 *  Value returned by the <code>ChatActivity</code> when closed. 
	 */
	public static final int CHAT_ACTIVITY_CLOSED = 777;

	/** 
	 * Key for retrieving the ActivityPendingResults from Intent
	 */
	public static final String ID_ACTIVITY_PENDING_RESULT = "ID_ACTIVITY_PENDING_RESULT";

	
	/** 
	 * The Jade gateway instance (retrieved after call to <code>JadeGateway.connect()</code>)
	 */
	private JadeGateway gateway;

	/** 
	 * Jade Logger for logging purpose 
	 */
	private static final Logger myLogger = Logger
			.getMyLogger(ContactListActivity.class.getName());

	/** 
	 * The main tab host. 
	 */
	private TabHost mainTabHost;

	/**
	 * Alpha transformation performed when connecting to Jade
	 */
	private AlphaAnimation backDissolve;
	
	/**
	 * The button for switching map/satellite mode in map view
	 */
	private Button switchButton;
	
	/** 
	 * The customized contacts list view. 
	 */
	private MultiSelectionListView contactsListView;



	/** 
	 * The customized overlay we use to draw on the map. 
	 */
	private ContactsPositionOverlay overlay;

	/** 
	 * The map view 
	 */
	private MapView mapView;

	
	/**
	 *  Custom dialog containing Jade connection parameters entered by the user. 
	 */
	private JadeParameterDialog parameterDialog;


	/** 
	 * Updater for this activity. 
	 */
	private GuiEventHandler activityHandler;

	
	/**
	 * Initializes the activity's UI interface.
	 */
	private void initUI() {

		//Setup the main tabhost
		setContentView(R.layout.homepage);
		mainTabHost = (TabHost) findViewById(R.id.main_tabhost);
		mainTabHost.setup();

		//Fill the contacts tab
		TabSpec contactsTabSpecs = mainTabHost.newTabSpec(CONTACTS_TAB_TAG);
		TabSpec mapTabSpecs = mainTabHost.newTabSpec(MAPVIEW_TAB_TAG);

		contactsTabSpecs.setIndicator(getText(R.string.label_contacts_tab_indicator), getResources().getDrawable(R.drawable.contact));
		contactsTabSpecs.setContent(R.id.contactstabview);
		
		mapTabSpecs.setIndicator(getText(R.string.label_map_tab_indicator), getResources().getDrawable(R.drawable.globemap));
		mapTabSpecs.setContent(R.id.maptabview);
		
		
		mainTabHost.addTab(contactsTabSpecs);
		mainTabHost.addTab(mapTabSpecs);
		Resources res = getResources();

		int[] colors = new int[] { res.getColor(R.color.white),
				res.getColor(R.color.blue) };
		
	
		GradientDrawable contentGradient = new GradientDrawable(
				Orientation.LEFT_RIGHT, colors);


		View homeTab = (View) findViewById(R.id.contactstabview);
		homeTab.setBackgroundDrawable(contentGradient);
		View homeTab1 = (View) findViewById(R.id.maptabview);
		homeTab1.setBackgroundDrawable(contentGradient);

		
		//init the map view
		mapView = (MapView) findViewById(R.id.myMapView);

		mapView.setSatellite(false);
	
		mapView.setOnTouchListener(new View.OnTouchListener(){

			public boolean onTouch(View v, MotionEvent event) {
				//User has touched the screen, no action just take the time
				if ( event.getAction() == MotionEvent.ACTION_DOWN ) {
						int touchedX = (int) event.getX();
						int touchedY = (int) event.getY();
						overlay.checkClickedPosition(new Point(touchedX, touchedY));
				}
				
				return true;
			}
			
		});
		
		mapView
				.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
					public void  onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)  {
						
						//Let the menu appear
						menu.add(Menu.NONE, CONTEXT_MENU_ITEM_CHAT_MAP,Menu.NONE, R.string.menu_item_chat);
						menu.add(Menu.NONE, CONTEXT_MENU_ITEM_CALL_MAP,Menu.NONE, R.string.menu_item_call);
						menu.add(Menu.NONE, CONTEXT_MENU_ITEM_SMS_MAP,Menu.NONE, R.string.menu_item_sms);
					}
				});

		
		List<Overlay> overlayList = mapView.getOverlays();
		overlay = new ContactsPositionOverlay(this, mapView, getResources());
		overlayList.add(overlay);

		//Button for switching map mode
		switchButton = (Button) findViewById(R.id.switchMapBtn);
		switchButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				Button clickedBtn = (Button) arg0;
				
				if (mapView.isSatellite()) {
					clickedBtn.setText(ContactListActivity.this
							.getText(R.string.label_toggle_satellite));
					mapView.setSatellite(false);
				} else {
					clickedBtn.setText(ContactListActivity.this
							.getText(R.string.label_toggle_map));
					mapView.setSatellite(true);
				}
			}

		});

		//Create the updater array

		//Select default tab
		mainTabHost.setCurrentTabByTag(CONTACTS_TAB_TAG);

		contactsListView = (MultiSelectionListView) findViewById(R.id.contactsList);
		int[] selectorColors = new int[] { res.getColor(R.color.light_green),
				res.getColor(R.color.dark_green) };
		GradientDrawable selectorDrawable = new GradientDrawable(
				Orientation.TL_BR, selectorColors);
		contactsListView.setSelector(selectorDrawable);
	
		registerForContextMenu(contactsListView);

		registerForContextMenu(mapView);

		
		
		contactsListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView parent, View v,
							int position, long id) {
						CheckBox cb = (CheckBox) v
								.findViewById(R.id.contact_check_box);
						cb.setChecked(!cb.isChecked());
					}
				});

		ContactManager.getInstance().readPhoneContacts(this);
		contactsListView.setAdapter(ContactManager.getInstance().getAdapter());
		parameterDialog = new JadeParameterDialog(this);
		//initializeContactList();

	}

	@Override	 
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		
		
		if(v instanceof MultiSelectionListView){
			AdapterView.AdapterContextMenuInfo adaptMenuInfo = (AdapterContextMenuInfo) menuInfo;
			MultiSelectionListView myLv = (MultiSelectionListView) v;
			myLv.setSelection(adaptMenuInfo.position);
			
			List<String> checkedContacts = myLv.getAllSelectedItems();
		
			if(checkedContacts.size() > 0){
				//Let the menu appear
				if(checkedContacts.size() == 1){
					//only one contact selected
					Contact cc = ContactManager.getInstance().getContact(checkedContacts.get(0));
					//verify is he is online --> add Chat 
					if(cc.isOnline()){
						menu.add(Menu.NONE, CONTEXT_MENU_ITEM_CHAT_LIST, Menu.NONE, R.string.menu_item_chat);
						}
					//add always sms and call
					menu.add(Menu.NONE, CONTEXT_MENU_ITEM_CALL_LIST, Menu.NONE, R.string.menu_item_call);
					menu.add(Menu.NONE, CONTEXT_MENU_ITEM_SMS_LIST, Menu.NONE, R.string.menu_item_sms);
				}else{
					//more elements selected
					boolean allOnline = true;
					for(String contactId : checkedContacts){
						Contact cc = ContactManager.getInstance().getContact(contactId);
						if(!cc.isOnline()){
							allOnline = false;
							break;
						}
					}
					if(allOnline)
						menu.add(Menu.NONE, CONTEXT_MENU_ITEM_CHAT_LIST, Menu.NONE, R.string.menu_item_chat);
					//otherwise only sms
					menu.add(Menu.NONE, CONTEXT_MENU_ITEM_SMS_LIST, Menu.NONE, R.string.menu_item_sms);
				}
			}	
		}else if (v instanceof MapView){
			menu.add(Menu.NONE, CONTEXT_MENU_ITEM_CHAT_MAP,Menu.NONE, R.string.menu_item_chat);
			menu.add(Menu.NONE, CONTEXT_MENU_ITEM_CALL_MAP,Menu.NONE, R.string.menu_item_call);
			menu.add(Menu.NONE, CONTEXT_MENU_ITEM_SMS_MAP,Menu.NONE, R.string.menu_item_sms);
		}
	}
	 

	/** 
	 * Executed at activity creation. Performs UI initialization, ContactManager initialization, 
	 * start of contact location update.
	 * 
	 * @param icicle Saved state if the application has been frozen (not used)
	 */
	public void onCreate(Bundle icicle) {

		myLogger.log(Logger.FINE,
				"onReceiveIntent called: My currentThread has this ID: "
						+ Thread.currentThread().getId());
		super.onCreate(icicle);
		
		ContactListAdapter cla = new ContactListAdapter(this);
		ContactManager.getInstance().addAdapter(cla);
		ChatSessionNotificationManager.create(this);

		initUI();
	
		
		//register an event for this activity to handle refresh of the views in this activity
		activityHandler = new ContactListActivityUpdateHandler();
		
		//register a generic disconnection handler
		MsnEventMgr.getInstance().registerEvent(MsnEvent.CONTACT_DISCONNECT_EVENT, new ContactDisconnectionHandler());
		//Initialize the UI
		
		disableUI();
		

	}
	

	/**
	 * Called any time this activity is shown.
	 * Resets contact list and map view removing all checked contacts
	 * Registers handlers for both the views.
	 */
	protected void onResume() {
		super.onResume();	
		myLogger.log(Logger.FINE, "On Resume was called!!!: setting event handler in ContactListActivity");
		MsnEventMgr.getInstance().registerEvent(MsnEvent.VIEW_REFRESH_EVENT, activityHandler);
		MsnEventMgr.getInstance().registerEvent(MsnEvent.INCOMING_MESSAGE_EVENT, activityHandler);
		this.overlay.uncheckAllContacts();
		this.contactsListView.uncheckAllSelectedItems();
	}

	/**
	 * Enables the main view after the application is connected to JADE
	 */
	private void enableUI() {
		
		View mainView = findViewById(R.id.main_view);
		backDissolve = new AlphaAnimation(0.0f,1.0f);
		backDissolve.setDuration(3000);
	
		mainView.setAnimation(backDissolve);
		mainView.setVisibility(View.VISIBLE);
		
		TabWidget widget = mainTabHost.getTabWidget();
		widget.setEnabled(true);
	}

	/**
	 * Disables the UI at the beginning
	 */
	private void disableUI() {
		
		View mainView = findViewById(R.id.main_view);
		mainView.setVisibility(View.INVISIBLE);
		
		TabWidget widget = mainTabHost.getTabWidget();
		widget.setEnabled(false);
	}

	//Retrieve the JADE properties from Dialog or configuration file
	/**
	 * Retrieve the jade properties, needed to connect to the JADE main container. 
	 * <p>
	 * These properties are:
	 * <ul>
	 * 	<li> <code>MAIN_HOST</code>: hostname or IP address of the machine on which the main container is running. 
	 * 			   Taken from resource file or settings dialog.
	 *   <li> <code>MAIN_PORT</code>: port used by the JADE main container. Taken from resource file or settings dialog.
	 *   <li> <code>MSISDN_KEY</code>: name of the JADE agent (the phone number)
	 * </ul>
	 * 
	 * @return the jade properties
	 */
	private Properties getJadeProperties() {
		//fill Jade connection properties
		Properties jadeProperties = new Properties();
		JChatApplication app = (JChatApplication)getApplication();
		jadeProperties.setProperty(Profile.MAIN_HOST, app.getProperty(JChatApplication.JADE_DEFAULT_HOST));
		jadeProperties.setProperty(Profile.MAIN_PORT, app.getProperty(JChatApplication.JADE_DEFAULT_PORT));
		jadeProperties.setProperty(JICPProtocol.MSISDN_KEY, app.getProperty(JChatApplication.PREFERENCE_PHONE_NUMBER));
		return jadeProperties;
	}


	/**
	 * Handles the shutdown of the application when exiting. Stops location update, 
	 * removes all notifications from status bar, shuts Jade down and clears managers  
	 */
	protected void onDestroy() {

	
		GeoNavigator.getInstance(this).stopLocationUpdate();
		myLogger.log(Logger.INFO, "onDestroy called ...");
	
		
		ChatSessionNotificationManager.getInstance().removeAllNotifications();

		if (gateway != null) {
			try {
				gateway.shutdownJADE();
			} catch (ConnectException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			gateway.disconnect(this);
		}

		MsnSessionManager.getInstance().shutdown();
		ContactManager.getInstance().shutdown();
		//Debug.stopMethodTracing();
		super.onDestroy();
	}

	/**
	 * Callback methods called when connection to Android add-on's MicroRuntimeService is completed
	 * Provides an instance of the {@link JadeGateway} that is stored and sends the updater to the agent using 
	 * JadeGateway.execute() making it able to update the GUI.
	 * 
	 * @param gw instance of the JadeGateway returned after the call to <code>JadeGateway.connect()</code>
	 */
	public void onConnected(JadeGateway gw) {
		this.gateway = gw;

		enableUI();
		try {
			this.gateway.execute("");
		} catch (StaleProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		myLogger.log(Logger.INFO, "onConnected(): SUCCESS!");

	}

	/**
	 * Method of {@link ConnectionListener} interface, called in case the service got disconnected.
	 * Currently not implemented.
	 */
	public void onDisconnected() {
		// TODO Auto-generated method stub
	}

	/**
	 * Creates the application main menu
	 * 
	 * @see Activity
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		 // Inflate the currently selected menu XML resource.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.title_icon, menu);
		return true;
	}
	
	

	/**
	 * Called any time the application main menu is displayed
	 * 
	 * @see Activity
	 */
	public boolean onPrepareOptionsMenu(Menu menu) {
		
		MenuItem menuItemConnect = menu.findItem(R.id.connect);
		menuItemConnect.setVisible(gateway == null);
		MenuItem menuItemSettings = menu.findItem(R.id.settings);
		menuItemSettings.setVisible((gateway == null));
		return super.onPrepareOptionsMenu(menu);
		
	}

	/**
	 * Handles the selection on items in main menu
	 * 
	 * @see Activity
	 */
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		super.onMenuItemSelected(featureId, item);
		switch (item.getItemId()) {
		case R.id.exit:
			finish();
			break;
		case R.id.settings:
			parameterDialog.show();
			break;
		case R.id.connect:
			//try to get a JadeGateway
			try {
				JChatApplication app = (JChatApplication)getApplication();
				ContactManager.getInstance().addMyContact(app.getProperty(JChatApplication.PREFERENCE_PHONE_NUMBER));

				GeoNavigator.setLocationProviderName(app.getProperty(JChatApplication.LOCATION_PROVIDER));
				GeoNavigator.getInstance(this).startLocationUpdate();

				initializeContactList();
				
				//fill Jade connection properties
				Properties jadeProperties = getJadeProperties();
				JadeGateway.connect(MsnAgent.class.getName(),
						new String[] { getText(R.string.contacts_update_time)
								.toString() }, jadeProperties, this, this);
			} catch (Exception e) {
				//troubles during connection
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
	 * 
	 * @see Activity
	 */
	public boolean onContextItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case CONTEXT_MENU_ITEM_CALL_LIST: {
			List<String> selectedIds = contactsListView.getAllSelectedItems();
			if (selectedIds.size() == 1) {
				callContact(selectedIds.get(0));
			} else {
				Toast.makeText(this, R.string.error_msg_multiple_phonecalls,
						2000).show();
			}

		}
			break;

		case CONTEXT_MENU_ITEM_CALL_MAP: {
			List<String> selectedIds = overlay.getSelectedItems();
			if (selectedIds.size() == 1) {
				callContact(selectedIds.get(0));
			} else {
				Toast.makeText(this, R.string.error_msg_multiple_phonecalls,
						2000);
			}
		}
			break;

		case CONTEXT_MENU_ITEM_CHAT_LIST: {
			List<String> participantIds = contactsListView
					.getAllSelectedItems();
			launchChatSession(participantIds);
		}
			break;

		case CONTEXT_MENU_ITEM_CHAT_MAP: {
			List<String> participantIds = overlay.getSelectedItems();
			launchChatSession(participantIds);
		}
			break;

		case CONTEXT_MENU_ITEM_SMS_LIST:{
			ArrayList<String> participantIds =  (ArrayList<String>) contactsListView
			.getAllSelectedItems();
			Intent i = new Intent();
			i.setClass(this, SendSMSActivity.class);
			i.putExtra(SendSMSActivity.SMS_ADDRESS_LIST, participantIds);
			startActivity(i);
		}
			
			break;
			
		case CONTEXT_MENU_ITEM_SMS_MAP:{
			ArrayList<String> participantIds =  (ArrayList<String>) overlay.getSelectedItems();
			//SMSDialog smsDialog = new SMSDialog(this,participantIds);
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
	 * Performs a fake phone call to a given contact.
	 * Android phone service is used 
	 * 
	 * @param selectedCPhoneNumber phone number of desired contact
	 */
	
	private void callContact(String selectedCPhoneNumber) {
		
		Intent i = new Intent(Intent.ACTION_CALL);
		Uri phoneUri = Uri.parse("tel:" +  selectedCPhoneNumber);
		i.setData(phoneUri);
		startActivity(i);
		
	}

	/**
	 * Start a new chat session with the given participants. The session is registered with session manager
	 * and a new chat activity is launched giving the sessionId of the session as a parameter
	 * 
	 * @param participantIds list of phone numbers of the participants to this conversation
	 */
	private void launchChatSession(List<String> participantIds) {
		//start a new session or retrieve it If the session already exists. its Id is retrieved
		String sessionId = MsnSessionManager.getInstance().startMsnSession(
				participantIds);

		//retrieve a copy of the session
		MsnSession session = MsnSessionManager.getInstance().retrieveSession(
				sessionId);
		//Add a notification for the new session
		ChatSessionNotificationManager.getInstance().addNewSessionNotification(sessionId);

		//packet an intent. We'll try to add the session ID in the intent data in URI form
		//We use intent resolution here, cause the ChatActivity should be selected matching ACTION and CATEGORY
		Intent it = new Intent(Intent.ACTION_MAIN);
		//set the data as an URI (content://sessionId#<sessionIdValue>)
		it.setData(session.getSessionIdAsUri());
		it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		it.setClass(this, ChatActivity.class);
		startActivity(it);

	}

	/**
	 * Updates the adapter associated with this contact list view.
	 * This method is run on the main thread but called periodically by the agent, giving a list of changes 
	 * (new contacts or removed ones).
	 * 
	 * @param changes the list of changes (new contacts or contacts that went offline and must be removed)
	 * @param contactMap copy of the contact map that shall be used for this update (to avoid racing conditions issues)
	 * @param contactLocMap copy of the location map that shall be used for this update (to avoid racing conditions issues)
	 */
	private void updateListAdapter(ContactListChanges changes, Map<String,Contact> contactMap, Map<String,ContactLocation> contactLocMap) {
		ContactListAdapter adapter = ContactManager.getInstance().getAdapter();
		adapter.update(changes, contactMap, contactLocMap);
	}

	/**
	 * Initialize the list filling up its adapter with all the available data.
	 * After initialization all changes shall be incremental (contacts added, contacts removed)
	 */
	private void initializeContactList() {
		ContactListAdapter adapter = ContactManager.getInstance().getAdapter();
		adapter.initialize();
		contactsListView.setAdapter(adapter);
		overlay.initializePositions();
	}
	


	/**
	 * Defines an handler to show  a toast when a contact disconnects
	 * @author s.semeria
	 *
	 */
	private  class ContactDisconnectionHandler extends GuiEventHandler {
		
		/**
		 * Handles the disconnection event
		 * 
		 * @param event the disconnection event to be handled
		 */ 
		protected void processEvent(MsnEvent event) {
			String eventName = event.getName();
			
			if (eventName.equals(MsnEvent.CONTACT_DISCONNECT_EVENT)){
				String discContactName = (String) event.getParam(MsnEvent.CONTACT_DISCONNECT_PARAM_CONTACTNAME);
				Toast.makeText(ContactListActivity.this, discContactName + " went offline!", 3000).show();
			}
		}
		
		
	}

	

	
	
	/**
	 * Handler for all events handled by the main {@link ContactListActivity}
	 */
	private class ContactListActivityUpdateHandler extends GuiEventHandler {

	
		
		/**
		 * Handles both an incoming message and a refresh of the screen
		 * 
		 * @param event the event that should be handled
		 */
		protected void processEvent(MsnEvent event) {
			String eventName = event.getName();
			
			
			if (eventName.equals(MsnEvent.VIEW_REFRESH_EVENT)){
				ContactListChanges changes = (ContactListChanges) event.getParam(MsnEvent.VIEW_REFRESH_PARAM_LISTOFCHANGES);
				Map<String,Contact> cMap = (Map<String,Contact>) event.getParam(MsnEvent.VIEW_REFRESH_CONTACTSMAP);
				Map<String,ContactLocation> cLocMap = (Map<String,ContactLocation>) event.getParam(MsnEvent.VIEW_REFRESH_PARAM_LOCATIONMAP);
				myLogger.log(Logger.INFO, "Thread "+ Thread.currentThread().getId() + ":GUI thread retrieves this list of changes from the event: " +  changes.toString());
				
				updateListAdapter(changes, cMap, cLocMap);
				overlay.update(changes, cMap, cLocMap);
				mapView.invalidate();
				int selPos = contactsListView.getSelectedItemPosition();
				ContactListAdapter adapter = ContactManager.getInstance()
						.getAdapter();
				contactsListView.setAdapter(adapter);
				contactsListView.setSelection(selPos);
			} else if (eventName.equals(MsnEvent.INCOMING_MESSAGE_EVENT)){
				myLogger.log(Logger.FINE, "Thread "+ Thread.currentThread().getId() + ":Contact List activity received an INCOMING MSG EVENT!!!");
				MsnSessionMessage msnMsg = (MsnSessionMessage) event.getParam(MsnEvent.INCOMING_MESSAGE_PARAM_MSG);
				String sessionId = (String) event.getParam(MsnEvent.INCOMING_MESSAGE_PARAM_SESSIONID);	
				//if the incoming msg is not for our session, post a notification
				ChatSessionNotificationManager.getInstance().addNewMsgNotification(sessionId, msnMsg);
				Toast.makeText(ContactListActivity.this, msnMsg.getSenderName() + " says: " + msnMsg.getMessageContent(), 3000).show();
			}			
		}

	}

	/* 
	 * This method is needed to tell the Google server if we're showing info ???? 
	 * @see com.google.android.maps.MapActivity#isRouteDisplayed()
	 */
	protected boolean isRouteDisplayed() {
		return false;
	}

}
