package ninjarush.mainactivity;

import ninjarush.mainsurfaceview.NinjaRushSurfaceView;
import ninjarush.music.GameMusic;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MyninjarushActivity extends Activity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //��ʼ������������ 
        GameMusic.inIt(this);
        //��ʼ�����ֲ�����
        GameMusic.inItMusic(R.raw.bg);
        //��ʼ����Ч������
        GameMusic.inItSound();
        //��ʼ������������
        GameMusic.windMediaplayer();
        //��ʼ�� run ������
        GameMusic.runMediaplayer();
        
    }
  
}