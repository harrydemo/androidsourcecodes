package com.twm ; 

import android.util.Log ; 

import android.media.AudioFormat ; 
import android.media.AudioManager ; 
import android.media.MediaRecorder ; 
import android.media.AudioRecord ; 

import java.util.LinkedList ; 

public class AudRec extends Thread
{
    protected AudioRecord m_in_rec ; 
    protected LinkedList  m_in_q ; 
    protected int         m_in_buf_size ; 
    protected byte []     m_in_bytes ; 
    protected boolean     m_keep_running ; 

    AudRec() {
        m_keep_running = true ; 
    }

    public void init(LinkedList data_q) {
        m_in_q = data_q ; 

        m_in_buf_size = android.media.AudioRecord.getMinBufferSize(8000,
                                                                   AudioFormat.CHANNEL_CONFIGURATION_MONO,
                                                                   AudioFormat.ENCODING_PCM_16BIT);

        m_in_rec = new AudioRecord(MediaRecorder.AudioSource.MIC, 
                                   8000, 
                                   AudioFormat.CHANNEL_CONFIGURATION_MONO,
                                   AudioFormat.ENCODING_PCM_16BIT,
                                   m_in_buf_size) ; 

        m_in_bytes = new byte [m_in_buf_size] ; 

        m_keep_running = true ; 
    }

    public void free() 
    {
        m_keep_running = false ; 
        try {
            sleep(1000) ; 
        } catch(Exception e) {
            logd("sleep exceptions...\n") ; 
        }
    }

    public void run() {
        int bytes_read ; 
        byte [] bytes_pkg ; 
        int i ; 
        m_in_rec.startRecording() ; 
        while(m_keep_running) {
            bytes_read = m_in_rec.read(m_in_bytes, 0, m_in_buf_size) ; 
            bytes_pkg = m_in_bytes.clone() ; 
            synchronized(m_in_q) {
                if(m_in_q.size() >= 20) {
                    m_in_q.removeFirst() ; 
                }
                m_in_q.add(bytes_pkg) ; 
            }
            yield() ; 
        }

        m_in_rec.stop() ; 
        m_in_rec = null ; 
        m_in_bytes = null ; 
    }

    public void logd(String s) {
        Log.d("AudRec", s) ; 
    }
}

