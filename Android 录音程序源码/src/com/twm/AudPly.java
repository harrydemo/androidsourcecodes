package com.twm ; 

import android.util.Log ; 

import android.media.AudioFormat ; 
import android.media.AudioManager ; 
import android.media.AudioTrack ;  

import java.util.LinkedList ; 
import java.lang.Integer ; 

public class AudPly extends Thread 
{
    protected AudioTrack m_out_trk ; 
    protected LinkedList m_out_q ; 
    protected int        m_out_buf_size ; 
    protected byte []    m_out_bytes ; 
    protected boolean    m_keep_running ; 
    
    AudPly() {
        m_keep_running = true ; 
    }

    public void init(LinkedList data_q) {
        m_out_q = data_q ; 

        m_out_buf_size = android.media.AudioTrack.getMinBufferSize(8000,
                                                                   AudioFormat.CHANNEL_CONFIGURATION_MONO,
                                                                   AudioFormat.ENCODING_PCM_16BIT);

        m_out_trk = new AudioTrack(AudioManager.STREAM_VOICE_CALL, 8000,
                                   AudioFormat.CHANNEL_CONFIGURATION_MONO,
                                   AudioFormat.ENCODING_PCM_16BIT, 
                                   m_out_buf_size,
                                   AudioTrack.MODE_STREAM);
        
        m_keep_running = true ; 
    }

    public void free() {
        m_keep_running = false ; 
        try {
            sleep(1000) ; 
        } catch(Exception e) {
            logd("sleep exceptions...\n") ; 
        }
    }

    public void run() {
        byte [] bytes_pkg = null ; 

        m_out_trk.play() ; 
        while(m_keep_running) {
            synchronized(m_out_q) {
                if(!m_out_q.isEmpty()) {
                    m_out_bytes = (byte [])(m_out_q.removeFirst()) ; 
                    bytes_pkg = m_out_bytes.clone() ; 
                    m_out_bytes = null ; 
                }
            }

            if(bytes_pkg != null) {
                m_out_trk.write(bytes_pkg, 0, bytes_pkg.length) ; 
            }

            bytes_pkg = null ; 
            yield() ; 
        }

        m_out_trk.stop() ; 
        m_out_trk = null ; 
    }

    public void logd(String s) {
        Log.d("AudPly", s) ; 
    }
}
