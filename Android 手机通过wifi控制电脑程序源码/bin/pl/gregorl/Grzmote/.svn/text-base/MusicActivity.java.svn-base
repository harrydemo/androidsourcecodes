package pl.gregorl.Grzmote;

import pl.gregorl.Grzmote.Common.MusicCommands;
import android.os.Bundle;
import android.widget.ImageButton;

public class MusicActivity extends CommandSendingActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        
    	setContentView(R.layout.music);
    	
        ImageButton playpause_musicButton = (ImageButton) findViewById(R.id.music_playpause);
        adjustImageButton(playpause_musicButton,MusicCommands.PlayPause);

        ImageButton prev_musicButton = (ImageButton) findViewById(R.id.music_prev);
        adjustImageButton(prev_musicButton, MusicCommands.Previous);
        
        ImageButton stop_musicButton = (ImageButton) findViewById(R.id.music_stop);
        adjustImageButton(stop_musicButton, MusicCommands.Stop);

        ImageButton forward_musicButton = (ImageButton) findViewById(R.id.music_next);
        adjustImageButton(forward_musicButton, MusicCommands.Next);    
    }


}