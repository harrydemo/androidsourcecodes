package chat.client.gui;

import java.util.logging.Level;

import chat.client.agent.ChatClientInterface;
import jade.core.MicroRuntime;
import jade.util.Logger;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * This activity implement the participants interface.
 * 
 * @author Michele Izzo - Telecomitalia
 */

public class ParticipantsActivity extends ListActivity
{
	private Logger logger = Logger.getJADELogger(this.getClass().getName());

	private MyReceiver myReceiver;

	private String nickname;
	private ChatClientInterface chatClientInterface;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();
		if (extras != null)
		{
			nickname = extras.getString("nickname");
		}

		try
		{
			chatClientInterface = MicroRuntime.getAgent(nickname)
					.getO2AInterface(ChatClientInterface.class);
		} catch (StaleProxyException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ControllerException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		myReceiver = new MyReceiver();

		IntentFilter refreshParticipantsFilter = new IntentFilter();
		refreshParticipantsFilter
				.addAction("jade.demo.chat.REFRESH_PARTICIPANTS");
		registerReceiver(myReceiver, refreshParticipantsFilter);

		setContentView(R.layout.participants);

		setListAdapter(new ArrayAdapter<String>(this, R.layout.participant,
				chatClientInterface.getParticipantNames()));

		ListView listView = getListView();
		listView.setTextFilterEnabled(true);
		listView.setOnItemClickListener(listViewtListener);
	}

	private OnItemClickListener listViewtListener = new OnItemClickListener()
	{
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id)
		{
			// TODO: A partecipant was picked. Send a private message.
			finish();
		}
	};

	@Override
	protected void onDestroy()
	{
		super.onDestroy();

		unregisterReceiver(myReceiver);

		logger.log(Level.INFO, "Destroy activity!");
	}

	private class MyReceiver extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			String action = intent.getAction();
			logger.log(Level.INFO, "Received intent " + action);
			if (action.equalsIgnoreCase("jade.demo.chat.REFRESH_PARTICIPANTS"))
			{
				setListAdapter(new ArrayAdapter<String>(
						ParticipantsActivity.this, R.layout.participant,
						chatClientInterface.getParticipantNames()));
			}
		}
	}

}
