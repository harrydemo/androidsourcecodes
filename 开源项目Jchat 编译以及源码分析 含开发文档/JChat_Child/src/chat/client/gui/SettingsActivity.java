package chat.client.gui;

import jade.util.leap.Properties;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * This activity implement the settings interface.
 * 
 * @author Michele Izzo - Telecomitalia
 */

public class SettingsActivity extends Activity
{
	Properties properties;
	EditText hostField;
	EditText portField;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		SharedPreferences settings = getSharedPreferences("jadeChatPrefsFile",
				0);

		String host = settings.getString("defaultHost", "");
		String port = settings.getString("defaultPort", "");

		setContentView(R.layout.settings);

		hostField = (EditText) findViewById(R.id.edit_host);
		hostField.setText(host);

		portField = (EditText) findViewById(R.id.edit_port);
		portField.setText(port);

		Button button = (Button) findViewById(R.id.button_use);
		button.setOnClickListener(buttonUseListener);
	}

	private OnClickListener buttonUseListener = new OnClickListener()
	{
		public void onClick(View v)
		{
			SharedPreferences settings = getSharedPreferences(
					"jadeChatPrefsFile", 0);

			// TODO: Verify that edited parameters was formally correct
			SharedPreferences.Editor editor = settings.edit();
			editor.putString("defaultHost", hostField.getText().toString());
			editor.putString("defaultPort", portField.getText().toString());
			editor.commit();

			finish();
		}
	};
}
