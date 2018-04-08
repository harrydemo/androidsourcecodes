
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
 *这个类提供了一些公用方法来取消位图解码。
 *
 * 功能decodefiledescriptor()用来解码位图。在
 *解码如果另一个线程要取消它，它调用续函数
 * cancelthreaddecoding()指定的线程来进行解码。
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
     * 创建一个指定的线程并将其初始化
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
   * 以下方法被用来跟踪
   * bitmapfaction选项的解码和取消。
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
     * 以下方法被用来跟踪哪些线程
     *被禁用的位图解码。
     */
    public synchronized boolean canThreadDecoding(Thread t) {
        ThreadStatus status = mThreadStatus.get(t);
        if (status == null) {
            // 默认允许解码
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

        // 唤醒正处于等待状态中的线程
        notifyAll();

        // 由于取消请求，可以到达mediaprovider早于getthumbnail！
        //使用mthumbrequesting标志，确保请求取消请求。
        try {
            synchronized (status) {
                while (status.mThumbRequesting) {
                    Images.Thumbnails.cancelThumbnailRequest(cr, -1, t.getId());
                    Video.Thumbnails.cancelThumbnailRequest(cr, -1, t.getId());
                    status.wait(200);
                }
            }
        } catch (InterruptedException ex) {
            // 忽略异常
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
     * 解码bitmapfactory位图的实现
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
