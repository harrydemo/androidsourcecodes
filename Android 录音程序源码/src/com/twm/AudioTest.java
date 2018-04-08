package com.twm;

import android.app.Activity ; 
import android.os.Bundle ;
import android.view.Menu ;
import android.view.MenuItem ;
import android.widget.TextView ;

import android.util.Log ; 

import java.util.LinkedList ; 

public class AudioTest extends Activity 
{
    /* menu id define */
    public static final int MENU_START_ID = Menu.FIRST ; 
    public static final int MENU_STOP_ID = Menu.FIRST + 1 ; 
    public static final int MENU_EXIT_ID = Menu.FIRST + 2 ; 

    protected AudPly     m_player ; 
    protected AudRec     m_recorder ; 
    protected LinkedList<byte []> m_pkg_q ; 

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        m_pkg_q = new LinkedList<byte []>() ; 
    }

    /* create menu */
    public boolean onCreateOptionsMenu(Menu aMenu) 
    {
        boolean res = super.onCreateOptionsMenu(aMenu) ; 

        aMenu.add(0, MENU_START_ID, 0, R.string.menu_start) ; 
        aMenu.add(0, MENU_STOP_ID, 0, R.string.menu_stop) ; 
        aMenu.add(0, MENU_EXIT_ID, 0, R.string.menu_exit) ; 

        return res ; 
    }

    /* menu select handler */
    public boolean onOptionsItemSelected(MenuItem aMenuItem) 
    {
        switch (aMenuItem.getItemId()) {
        case MENU_START_ID:
            {
                m_player = new AudPly() ; 
                m_recorder = new AudRec() ;

                m_pkg_q.clear() ; 

                m_player.init(m_pkg_q) ; 
                m_recorder.init(m_pkg_q) ; 

                m_recorder.start() ; 
                m_player.start() ; 
            }
            break ;
        case MENU_STOP_ID:
            {   
                m_recorder.free() ; 
                m_player.free() ;

                m_player = null ; 
                m_recorder = null ; 
            }
            break ; 
        case MENU_EXIT_ID:
            {
                int pid = android.os.Process.myPid() ; 
                android.os.Process.killProcess(pid) ; 
            }
            break ;
        default:
            break ; 
        }

        return super.onOptionsItemSelected(aMenuItem);
    }

}
