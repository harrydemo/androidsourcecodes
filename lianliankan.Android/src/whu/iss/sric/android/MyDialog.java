package whu.iss.sric.android;

import whu.iss.sric.view.GameView;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class MyDialog extends Dialog implements OnClickListener{

	private GameView gameview;
	private Context context;
	
	public MyDialog(Context context, GameView gameview, String msg, int time) {
		super(context,R.style.dialog);
		this.gameview = gameview;
		this.context = context;
		this.setContentView(R.layout.dialog_view);
		TextView text_msg = (TextView) findViewById(R.id.text_message);
		TextView text_time = (TextView) findViewById(R.id.text_time);
		ImageButton btn_menu = (ImageButton) findViewById(R.id.menu_imgbtn);
		ImageButton btn_next = (ImageButton) findViewById(R.id.next_imgbtn);
		ImageButton btn_replay = (ImageButton) findViewById(R.id.replay_imgbtn);
		
		text_msg.setText(msg);
		text_time.setText(text_time.getText().toString().replace("$", String.valueOf(time)));
		btn_menu.setOnClickListener(this);
		btn_next.setOnClickListener(this);
		btn_replay.setOnClickListener(this);
		this.setCancelable(false);
	}

	@Override
	public void onClick(View v) {
		this.dismiss();
		switch(v.getId()){
		case R.id.menu_imgbtn:
			Dialog dialog = new AlertDialog.Builder(context)
            .setIcon(R.drawable.buttons_bg20)
            .setTitle(R.string.quit)
            .setMessage(R.string.sure_quit)
            .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	((WelActivity)context).quit();
                }
            })
            .setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            })
            .create();
			dialog.show();
//			gameview.startPlay();
//			Toast.makeText(context, text, duration);
			break;
		case R.id.replay_imgbtn:
			gameview.startPlay();
			break;
		case R.id.next_imgbtn:
			gameview.startNextPlay();
			break;
		}
	}
}
