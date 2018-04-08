package com.orange.gpstest;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * This class represents the settings window
 * This window displays settings for output test file
 * @see Activity
 * @author Alan Sowamber
 */
public class SettingsUI extends Activity implements OnCheckedChangeListener{
	
	Settings settings;
	MainApp mainApp;
	EditText fileName;
	EditText directory;
	CheckBox checkBox;
	Button applyButton;
	int iCheckFileRet=MainApp.OK;
	
	static final String DialogMsgArray[]={
			"New settings applied!",
			"Can't create this directory!",
			"The directory has been created!",
			"Can't open the directory!"
	};
	
	
	static final int dialogMsgErrorId=0;
	static final int dialogMsgCreateId=1;

	/** Called when the activity is first created. 
	 * Create the window from the application settings
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    mainApp=(MainApp)getApplication();
	    settings=mainApp.settings;

	    // TODO Auto-generated method stub
	    setContentView(R.layout.settings);
	    
	    TextView filnamelabel=(TextView)findViewById(R.id.TextView01);
	    filnamelabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, mainApp.GetFontSize());
	    
	    fileName=(EditText)findViewById(R.id.EditText01);
	    fileName.setTextSize(TypedValue.COMPLEX_UNIT_PX, mainApp.GetFontSize());
		
	    TextView directorylabel=(TextView)findViewById(R.id.TextView02);
	    directorylabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, mainApp.GetFontSize());
	    directory=(EditText)findViewById(R.id.testdirectory);
	    directory.setTextSize(TypedValue.COMPLEX_UNIT_PX, mainApp.GetFontSize());
	    
	    
	    checkBox=(CheckBox)findViewById(R.id.CheckBox01);
	    checkBox.setTextSize(TypedValue.COMPLEX_UNIT_PX, mainApp.GetFontSize());
	    
	    checkBox.setChecked(settings.appendTime);
	    checkBox.setOnCheckedChangeListener(this);
	    
	    fileName.setText(settings.TestFile);
	    directory.setText(settings.TestDirectory);
	    
	    applyButton=(Button)findViewById(R.id.ApplyButton01);
	    applyButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, mainApp.GetFontSize());
	    applyButton.setOnClickListener(new OnClickListener(){
	    	public void  onClick(View v)  {
	    		onApplyButton();
	    	}
	    }
	    );
	   
	    
	   

	}
	
	/**
	 * Apply new settings from the UI
	 */
	void onApplyButton(){
		iCheckFileRet=mainApp.checkTestFile(directory.getText().toString());
		showDialog(iCheckFileRet);
	}
	
	/**
	 * Implements from Activity
	 * Save last changes before destroy window
	 * @see Activity
	 */
	protected void  onDestroy  ()
	{
		super.onDestroy();
		settings.TestFile=fileName.getText().toString();
		
		if((iCheckFileRet==MainApp.OK)||(iCheckFileRet==MainApp.FILECREATED))
		{
			settings.TestDirectory=directory.getText().toString();
		}
		
		settings.SaveData();
	}
	

	/**
	 * Implements from OnCheckedChangeListener
	 * Save checkbox value in settings
	 * @see OnCheckedChangeListener
	 */
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		settings.appendTime=isChecked;
		
		
	}
	
	/**
	 * Implements from Activity
	 * Create dialogs
	 * @param id The dialog identifier
	 * @see Activity
	 */
	public Dialog onCreateDialog (int id){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(DialogMsgArray[id]);
		builder.setCancelable(false);
		builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	  // MainApp.this.finish();
		        	   dialog.dismiss();
		           }
		       });
		AlertDialog alert = builder.create();
		return alert;
	}

}
