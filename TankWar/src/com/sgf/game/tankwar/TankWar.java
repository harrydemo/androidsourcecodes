package com.sgf.game.tankwar;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.TableLayout;

public class TankWar extends Activity {

	@Override
	 public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.game_layout);
		GamePanel gp=(GamePanel)findViewById(R.id.tankwar);
		gp.setDialog(new AlertDialog.Builder(this).setMessage("GAME OVER!").setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if(keyCode==KeyEvent.KEYCODE_BACK){
					Log.i("System.out", "game is over!");
					finish();
				}
				return false;
			}
		}).create());
		gp.setMsgPanel((TableLayout)findViewById(R.id.msg));
		gp.setFocusable(true);
	}

}
