package pl.gregorl.Grzmote;

import pl.gregorl.Grzmote.R.id;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
		EditText serverAddressEdit = (EditText)findViewById(R.id.address_field);
		serverAddressEdit.setText(Settings.getServerAddress());
		
		Button saveButton = (Button) findViewById(R.id.save_button);
		
		final BaseActivity a = this;
		saveButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				setSettings();
				Settings.Save();
				a.finish();
			}
		});
	}
		
	private void setSettings(){
		Settings.setServerAddress(((EditText)findViewById(id.address_field)).getText().toString());
	}
	
}
