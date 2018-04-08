package pl.gregorl.Grzmote;

import pl.gregorl.Grzmote.Common.VolumeCommands;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TabHost;

public class GrzmoteActivity extends CommandSendingTabActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		prepareTabs();
		adjustVolumeButtons();

	}

	private void adjustVolumeButtons() {
		ImageButton muteButton = (ImageButton) findViewById(R.id.mute);
		adjustImageButton(muteButton, VolumeCommands.Mute);

		ImageButton volumeupButton = (ImageButton) findViewById(R.id.volume_up);
		adjustImageButton(volumeupButton, VolumeCommands.Up);

		ImageButton volumedownButton = (ImageButton) findViewById(R.id.volume_down);
		adjustImageButton(volumedownButton, VolumeCommands.Down);
	}

	private void prepareTabs() {
		Resources res = getResources();
		TabHost tabHost = getTabHost();

		TabHost.TabSpec spec; // Reusable TabSpec for each tab
		Intent intent; // Reusable Intent for each tab

		intent = new Intent().setClass(this, MusicActivity.class);

		spec = tabHost.newTabSpec("music").setIndicator("Music",
				res.getDrawable(R.drawable.icon_music)).setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, MovieActivity.class);
		spec = tabHost.newTabSpec("movie").setIndicator("Movie",
				res.getDrawable(R.drawable.icon_video)).setContent(intent);
		tabHost.addTab(spec);

		tabHost.setCurrentTabByTag("music");

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	/* Handles item selections */
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.settings_option) {
			startActivity(new Intent().setClass(this, SettingsActivity.class));
		}
		return false;
	}

}
