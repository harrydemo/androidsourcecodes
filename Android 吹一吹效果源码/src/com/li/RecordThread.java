package com.li;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Message;
 
//import com.bshark.letmessage.ui.BlowActivity.MyHandler;
//import com.bshark.letmessage.util.Parameter;

import com.li.BlowActivity.MyHandler;
 
public class RecordThread extends Thread {
        private AudioRecord ar;
        private int bs = 100;
        private static int SAMPLE_RATE_IN_HZ = 8000;
        private Message msg;
        private int number = 1;
        private int tal = 1;
        private MyHandler handler;
        private long currenttime;
        private long endtime;
        private long time = 1;
        
        //�����ֵ֮�� �����¼�
        private static int BLOW_ACTIVI=3000;
 
        public RecordThread(MyHandler myHandler) {
                super();
                bs = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ,
                                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                                AudioFormat.ENCODING_PCM_16BIT);
                ar = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE_IN_HZ,
                                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                                AudioFormat.ENCODING_PCM_16BIT, bs);
                handler = myHandler;
        }
 
        @Override
        public void run() {
                try {
                        ar.startRecording();
                        Parameter.isblow = true;
                        // ���ڶ�ȡ�� buffer
                        byte[] buffer = new byte[bs];
                        while (Parameter.isblow) {
                                number++;
                                sleep(8);
                                currenttime = System.currentTimeMillis();
                                int r = ar.read(buffer, 0, bs) + 1;
                                int v = 0;
                                for (int i = 0; i < buffer.length; i++) {
                                        v += (buffer[i] * buffer[i]);
                                }
                                int value = Integer.valueOf(v / (int) r);
                                tal = tal + value;
                                endtime = System.currentTimeMillis();
                                time = time + (endtime - currenttime);
                 
                                if (time >= 500 || number > 5) {
 
                                        int total = tal / number;
                                        if (total > BLOW_ACTIVI) {
                                                //������Ϣ֪ͨ������ ��������
                                                 
                                                //���ô����handler �����淢��֪ͨ
                                        		BlowActivity.i+=1;
                                        		handler.sendEmptyMessage(0); //�ı�i��ֵ�󣬷���һ����message�����߳�
 
                                                //
                                                number = 1;
                                                tal = 1;
                                                time = 1;
                                        }
                                }
 
                        }
                        ar.stop();
                        ar.release();
                        bs=100;
                	
                         
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }
}