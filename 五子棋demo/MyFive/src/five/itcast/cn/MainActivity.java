package five.itcast.cn;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

//³ÌÐòÈë¿ÚActivity
public class MainActivity extends Activity {
    
	private Chessboard gameView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.five_game_view);
        gameView = (Chessboard) findViewById(R.id.snake);
        gameView.setTextView((TextView)findViewById(R.id.text));
    }
    
}