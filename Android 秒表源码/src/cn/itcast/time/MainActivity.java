package cn.itcast.time;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
    private TextView minText;
    private TextView secText;
    private boolean paused = false;
    private String timeUsed;
    private int timeUsedInSec;
    private Handler uiHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				if (!paused)
				{
					addTimeUsed();
					updateClockUI();
				}
				uiHandler.sendEmptyMessageDelayed(1, 1000);
				break;
			default:
				break;
			}
		}
    	
    };
       
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
               minText = (TextView) this.findViewById(R.id.min);
		secText = (TextView) this.findViewById(R.id.sec);
		
		Button button = (Button) this.findViewById(R.id.button);
		Button  sbutton=(Button)this.findViewById(R.id.stop);
		sbutton.setOnClickListener(new View.OnClickListener() {
		    
		    @Override
		    public void onClick(View v) {
			paused = true;
			timeUsedInSec=0;
		    }
		});
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			    uiHandler.removeMessages(1);
				startGame();	
				paused = false;		
				}

		});
    }
    @Override
	protected void onPause() {
		super.onPause();
		paused = true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		paused = false;
	}
	
	private void startGame() {
		uiHandler.sendEmptyMessageDelayed(1, 1000);
	}
	
    // 更新时间的显示
	private void updateClockUI() {
		minText.setText(getMin()+":");
		secText.setText(getSec());
	}
	
	public void addTimeUsed() {
		timeUsedInSec=timeUsedInSec+1;
		timeUsed = this.getMin() + ":" + this.getSec();
	}
	
	public CharSequence getMin() {
		return String.valueOf(timeUsedInSec / 60);
	}

	public CharSequence getSec() {
		int sec = timeUsedInSec % 60;
		return sec < 10 ? "0" + sec : String.valueOf(sec);
	}
}