package cn.m.xys;

import android.app.Activity;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

public class FiveChessActivity extends Activity 
{
    GameView gameView = null;    
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
    	// ���ر�����
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	// ȫ����ʾ
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
    		WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	// ��ȡ��Ļ���
    	Display display = getWindowManager().getDefaultDisplay();
    	// ��ʵGameView
    	GameView.init(this, display.getWidth(), display.getHeight());
    	gameView = GameView.getInstance();
    	setContentView(gameView);
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
	return super.onKeyDown(keyCode, event);
    }    
}