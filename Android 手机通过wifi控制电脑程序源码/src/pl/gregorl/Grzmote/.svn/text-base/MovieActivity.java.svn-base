package pl.gregorl.Grzmote;

import pl.gregorl.Grzmote.Common.MovieCommands;
import android.os.Bundle;
import android.widget.ImageButton;

public class MovieActivity extends CommandSendingActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.movie);
    	
        ImageButton playpause_musicButton = (ImageButton) findViewById(R.id.movie_pause);
        adjustImageButton(playpause_musicButton,MovieCommands.PlayPause);

        ImageButton prev_musicButton = (ImageButton) findViewById(R.id.movie_seek_rew);
        adjustImageButton(prev_musicButton, MovieCommands.SeekBackward);
        
        ImageButton stop_musicButton = (ImageButton) findViewById(R.id.movie_stop);
        adjustImageButton(stop_musicButton, MovieCommands.Stop);

        ImageButton forward_musicButton = (ImageButton) findViewById(R.id.movie_seek_fwd);
        adjustImageButton(forward_musicButton, MovieCommands.SeekForward);
    }
}
