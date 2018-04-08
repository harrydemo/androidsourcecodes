package chat.client.gui;

import java.util.logging.Level;

import jade.util.Logger;
import android.app.Application;
import android.content.SharedPreferences;

/**
 * This is the Android Chat Demo Application.
 * 
 * @author Michele Izzo - Telecomitalia
 */

public class ChatApplication extends Application
{
	private Logger logger = Logger.getJADELogger(this.getClass().getName());

	@Override
	public void onCreate()
	{
		super.onCreate();

		SharedPreferences settings = getSharedPreferences("jadeChatPrefsFile",
				0);

		String defaultHost = settings.getString("defaultHost", "");
		String defaultPort = settings.getString("defaultPort", "");
		if (defaultHost.length()==0 || defaultPort.length()==0)
		{
			logger.log(Level.INFO, "Create default properties");
			SharedPreferences.Editor editor = settings.edit();
			editor.putString("defaultHost", "10.0.2.2");
			editor.putString("defaultPort", "1099");
			editor.commit();
		}
	}
}
