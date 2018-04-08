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

import android.app.Activity;
import android.app.Dialog;
import android.location.LocationManager;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;


/**
 * Custom dialog employed to show to the user current host and port for JADE main container
 * making him able to change default settings (read from strings.xml) if he wishes to.
 * 
 * @author Cristina Cucè
 * @author Marco Ughetti 
 * @author Stefano Semeria
 * @author Tiziana Trucco
 * @version 1.0 
 */
public class JadeParameterDialog extends Dialog {

	/** 
	 * The JADE main container host address. 
	 */
	private String jadeAddress;
	
	/**
	 * The JADE main container host port.
	 */
	private String jadePort;

	/**
	 * The location provider to be used (can be GPS or NETWORK)
	 */
	private String locationProvider;
	
	/**
	 * the phone number
	 */
	private String phoneNumber;
	
	/** 
	 * GUI element containing the JADE address value
	 */
	private EditText jadeAddressEdt;
	
	/** 
	 * GUI element containing the JADE port value
	 */
	private EditText jadePortEdt;
	
	/**
	 * Spinner for selecting provider
	 */
	private Spinner providerSpinner;
	/** 
	 * GUI element containing the JADE port value
	 */
	private EditText phoneNbrEdt;

	/**
	 * Main activity
	 */
	private Activity activity;
	
	/**
	 * Instantiates a new jade parameter dialog.
	 * 
	 * @param act the current application context
	 */
	public JadeParameterDialog(Activity act) {
		super(act);
		activity = act;
		View v = initUI();
		this.setTitle(act.getString(R.string.label_params_title));
		this.setCancelable(false);
		this.setContentView(v);
		fillWithDefaults();
	}
	
	/**
	 * Retrieve default values for JADE host/port from strings.xml file
	 * 
	 * @param ctx the application context
	 */
	private void fillWithDefaults(){
		
		JChatApplication app = (JChatApplication) activity.getApplication();
		jadeAddress = app.getProperty(JChatApplication.JADE_DEFAULT_HOST);
		jadePort = app.getProperty(JChatApplication.JADE_DEFAULT_PORT); 
		locationProvider = app.getProperty(JChatApplication.LOCATION_PROVIDER); 
		phoneNumber = app.getProperty(JChatApplication.PREFERENCE_PHONE_NUMBER);
		providerSpinner.setSelection((locationProvider.equals(LocationManager.GPS_PROVIDER)? 0: 1));
		jadeAddressEdt.setText(jadeAddress);
		jadePortEdt.setText(jadePort);
		phoneNbrEdt.setText(phoneNumber);
	}
	

	/**
	 * Gets the current location provider.
	 * 
	 * @return the current location provider.
	 */
	public String getLocationProvider() {
		return locationProvider;
	}
	
	/**
	 * Gets the JADE main container host address.
	 * 
	 * @return the JADE main container host address.
	 */
	public String getJadeAddress(){
		return jadeAddress;
	}
	
	

	
	
	/**
	 * Gets the JADE main container host port.
	 * 
	 * @return the JADE main container host port
	 */
	public String getJadePort(){
		return jadePort;
	}
	
	
	/**
	 * Initializes the dialog UI, preparing the parent view containing view hierarchy 
	 * Layout is hardcoded here, no xml.
	 * 
	 * @param ctx the application context
	 * @return the parent view
	 */
	private View initUI(){
		RelativeLayout layout = new RelativeLayout(activity);
		
		TextView jadeAddress = new TextView(activity);
		jadeAddress.setText("Jade platform address");
		jadeAddress.setId(1);
		layout.addView(jadeAddress,new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		
		jadeAddressEdt = new EditText(activity);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.BELOW, 1);
		jadeAddressEdt.setId(2);
		jadeAddressEdt.setSingleLine();
		layout.addView(jadeAddressEdt,params);
		
		TextView jadePort = new TextView(activity);
		jadePort.setText("Jade platform port");
		params = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.BELOW, 2);
		jadePort.setId(3);
		layout.addView(jadePort,params);
		
		jadePortEdt = new EditText(activity);
		jadePortEdt.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		params = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.BELOW, 3);
		jadePortEdt.setId(4);
		jadePortEdt.setSingleLine();
		layout.addView(jadePortEdt,params);
	
		TextView phoneNbr = new TextView(activity);
		phoneNbr.setText("Phone Number");
		params = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.BELOW, 4);
		phoneNbr.setId(5);
		layout.addView(phoneNbr,params);
		
		phoneNbrEdt = new EditText(activity);
		phoneNbrEdt.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		phoneNbrEdt.setSingleLine();
		params = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.BELOW, 5);
		phoneNbrEdt.setId(6);
		layout.addView(phoneNbrEdt,params);
		
		
		TextView locProviderName = new TextView(activity);
		locProviderName.setText("Location Provider");
		locProviderName.setId(7);
		params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.BELOW, 6);
		layout.addView(locProviderName, params);
		
		providerSpinner = new Spinner(activity);
		providerSpinner.setId(8);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,android.R.layout.simple_spinner_item,new String[]{LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER}); 
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		providerSpinner.setAdapter(adapter);
		providerSpinner.setSelection(0);
		params = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.BELOW, 7);
		layout.addView(providerSpinner, params);

				
		Button closeButton = new Button(activity);
		params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params.addRule(RelativeLayout.BELOW, 8);
		closeButton.setText("Close");
		closeButton.setOnClickListener(new View.OnClickListener(){
			/**
			 * Handles clicking on close button 
			 */
			public void onClick(View arg0) {
					String tmpVar = JadeParameterDialog.this.jadeAddressEdt.getText().toString();
					
					if (tmpVar.length() > 0){
						JadeParameterDialog.this.jadeAddress = tmpVar;
						JChatApplication app = (JChatApplication) activity.getApplication();
						app.setProperty(JChatApplication.JADE_DEFAULT_HOST, tmpVar);
					}
					
					tmpVar = JadeParameterDialog.this.jadePortEdt.getText().toString();
					if (tmpVar.length() > 0){
						JadeParameterDialog.this.jadePort = tmpVar;
						JChatApplication app = (JChatApplication) activity.getApplication();
						app.setProperty(JChatApplication.JADE_DEFAULT_PORT, tmpVar);
						
					}
					tmpVar = JadeParameterDialog.this.phoneNbrEdt.getText().toString();
					if (tmpVar.length() > 0){
						JadeParameterDialog.this.phoneNumber = tmpVar;
						JChatApplication app = (JChatApplication) activity.getApplication();
						app.setProperty(JChatApplication.PREFERENCE_PHONE_NUMBER, tmpVar);
					}
					tmpVar = (String) JadeParameterDialog.this.providerSpinner.getSelectedItem();
					if (tmpVar.length() > 0){
						JadeParameterDialog.this.locationProvider = tmpVar;
						JChatApplication app = (JChatApplication) activity.getApplication();
						app.setProperty(JChatApplication.LOCATION_PROVIDER, tmpVar);
					}
					
					JadeParameterDialog.this.dismiss();
			}
			
		});
		
		layout.addView(closeButton,params);
		
		ScrollView scrollingView = new ScrollView(activity);
		scrollingView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.FILL_PARENT));
		scrollingView.addView(layout);
		
		return scrollingView;
	}

	
	
}
