/**
 * 
 */
package com.ty.weather;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.ty.weather.ForecastProvider.WeatherWidgets;
import com.ty.weather.util.WidgetEntity;

/**
 * @author 088926
 *
 */
public class ConfigureActivity extends Activity implements View.OnClickListener{
	private static String TAG = "ConfigureActivity";
	
	private EditText editCity, editUpdatetime;
	private Button btnSave;
	private String city;
	private int updatetime;

    private int widgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "ty-weather configure started");
        
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);        
        setContentView(R.layout.configure);
        
        editCity = (EditText)findViewById(R.id.editCity);
        editUpdatetime = (EditText)findViewById(R.id.editUpdatetime);
        btnSave = (Button)findViewById(R.id.btnSave);
        
        btnSave.setOnClickListener(this);        
        
        // Read the appWidgetId to configure from the incoming intent
        widgetId = getIntent().getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        setConfigureResult(Activity.RESULT_CANCELED);
        if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }
        
        if (savedInstanceState != null){
        	city = savedInstanceState.getString("city");
        	updatetime = savedInstanceState.getInt("updatetime");
        	
        	editCity.setText(city);
        	editUpdatetime.setText(updatetime);
        }
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	// TODO Auto-generated method stub
    	super.onSaveInstanceState(outState);
    	
    	outState.putString("city", city);
    	outState.putInt("updatetime", updatetime);    	

		Log.d(TAG, "ty-weather onSaveInstanceState!");
    }

	public void onClick(View v) {
		// TODO Auto-generated method stub
		Log.d(TAG, "ty-weather configure save!");
		switch (v.getId()) {
		case R.id.btnSave: {
			city = editCity.getText().toString();
			updatetime = Integer.parseInt(editUpdatetime.getText().toString());
			
			ContentValues values = new ContentValues();
			values.put(BaseColumns._ID, widgetId);
			values.put(WidgetEntity.POSTALCODE, city);
			values.put(WidgetEntity.UPDATE_MILIS, updatetime);
			values.put(WidgetEntity.LAST_UPDATE_TIME, -1);
			values.put(WidgetEntity.IS_CONFIGURED, 1);
			
			ContentResolver resolver = getContentResolver();
			resolver.insert(WeatherWidgets.CONTENT_URI, values);
			
			// start service
			Log.d(TAG, "ty-weather start Service!");
			ForecastService.addWidgetIDs(new int[]{widgetId});
			startService(new Intent(this, ForecastService.class));
			
			setConfigureResult(Activity.RESULT_OK);
            finish();
            
			break;
		}
		}
	}
	
	/**
     * Convenience method to always include {@link #widgetId} when setting
     * the result {@link Intent}.
     */
    public void setConfigureResult(int resultCode) {
        final Intent data = new Intent();
        data.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        setResult(resultCode, data);
    }
}
