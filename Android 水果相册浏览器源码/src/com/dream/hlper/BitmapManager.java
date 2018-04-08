
package com.dream.hlper;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Video;
import android.util.Log;

import java.io.FileDescriptor;
import java.util.WeakHashMap;

/**
 *������ṩ��һЩ���÷�����ȡ��λͼ���롣
 *
 * ����decodefiledescriptor()��������λͼ����
 *���������һ���߳�Ҫȡ������������������
 * cancelthreaddecoding()ָ�����߳������н��롣
 */
public class BitmapManager {
    private static final String TAG = "BitmapManager";
    private static enum State {CANCEL, ALLOW}
    private static class ThreadStatus {
        public State mState = State.ALLOW;
        public BitmapFactory.Options mOptions;
        public boolean mThumbRequesting;
        @Override
        public String toString() {
            String s;
            if (mState == State.CANCEL) {
                s = "Cancel";
            } else if (mState == State.ALLOW) {
                s = "Allow";
            } else {
                s = "?";
            }
            s = "thread state = " + s + ", options = " + mOptions;
            return s;
        }
    }

    private final WeakHashMap<Thread, ThreadStatus> mThreadStatus =
            new WeakHashMap<Thread, ThreadStatus>();

    private static BitmapManager sManager = null;

    private BitmapManager() {
    }

    /**
     * ����һ��ָ�����̲߳������ʼ��
     */
    private synchronized ThreadStatus getOrCreateThreadStatus(Thread t) {
        ThreadStatus status = mThreadStatus.get(t);
        if (status == null) {
            status = new ThreadStatus();
            mThreadStatus.put(t, status);
        }
        return status;
    }

    /**
   * ���·�������������
   * bitmapfactionѡ��Ľ����ȡ����
     */
    private synchronized void setDecodingOptions(Thread t,
            BitmapFactory.Options options) {
        getOrCreateThreadStatus(t).mOptions = options;
    }

    synchronized void removeDecodingOptions(Thread t) {
        ThreadStatus status = mThreadStatus.get(t);
        status.mOptions = null;
    }

    /**
     * ���·���������������Щ�߳�
     *�����õ�λͼ���롣
     */
    public synchronized boolean canThreadDecoding(Thread t) {
        ThreadStatus status = mThreadStatus.get(t);
        if (status == null) {
            // Ĭ���������
            return true;
        }

        boolean result = (status.mState != State.CANCEL);
        return result;
    }

    public synchronized void allowThreadDecoding(Thread t) {
        getOrCreateThreadStatus(t).mState = State.ALLOW;
    }

    public synchronized void cancelThreadDecoding(Thread t, ContentResolver cr) {
        ThreadStatus status = getOrCreateThreadStatus(t);
        status.mState = State.CANCEL;
        if (status.mOptions != null) {
            status.mOptions.requestCancelDecode();
        }

        // ���������ڵȴ�״̬�е��߳�
        notifyAll();

        // ����ȡ�����󣬿��Ե���mediaprovider����getthumbnail��
        //ʹ��mthumbrequesting��־��ȷ������ȡ������
        try {
            synchronized (status) {
                while (status.mThumbRequesting) {
                    Images.Thumbnails.cancelThumbnailRequest(cr, -1, t.getId());
                    Video.Thumbnails.cancelThumbnailRequest(cr, -1, t.getId());
                    status.wait(200);
                }
            }
        } catch (InterruptedException ex) {
            // �����쳣
        }
    }

    public Bitmap getThumbnail(ContentResolver cr, long origId, int kind,
            BitmapFactory.Options options, boolean isVideo) {
        Thread t = Thread.currentThread();
        ThreadStatus status = getOrCreateThreadStatus(t);

        if (!canThreadDecoding(t)) {
            Log.d(TAG, "Thread " + t + " is not allowed to decode.");
            return null;
        }

        try {
            synchronized (status) {
                status.mThumbRequesting = true;
            }
            if (isVideo) {
                return Video.Thumbnails.getThumbnail(cr, origId, t.getId(),
                        kind, null);
            } else {
                return Images.Thumbnails.getThumbnail(cr, origId, t.getId(),
                        kind, null);
            }
        } finally {
            synchronized (status) {
                status.mThumbRequesting = false;
                status.notifyAll();
            }
        }
    }

    public static synchronized BitmapManager instance() {
        if (sManager == null) {
            sManager = new BitmapManager();
        }
        return sManager;
    }

    /**
     * ����bitmapfactoryλͼ��ʵ��
     */
    public Bitmap decodeFileDescriptor(FileDescriptor fd,
                                       BitmapFactory.Options options) {
        if (options.mCancel) {
            return null;
        }

        Thread thread = Thread.currentThread();
        if (!canThreadDecoding(thread)) {
            Log.d(TAG, "Thread " + thread + " is not allowed to decode.");
            return null;
        }

        setDecodingOptions(thread, options);
        Bitmap b = BitmapFactory.decodeFileDescriptor(fd, null, options);

        removeDecodingOptions(thread);
        return b;
    }
}
