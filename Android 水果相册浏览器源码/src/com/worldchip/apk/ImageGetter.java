

package com.worldchip.apk;

import java.util.List;


import com.dream.gallery.IImage;
//import com.dream.gallery.IImageList;
import com.dream.hlper.BitmapManager;
import com.dream.hlper.Util;


import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.os.Process;


/*
*���ز���:�����κθ�����ͼ�������ͼ������
*���ڴ�ͺ�ص������ʾλͼ��
*���и��ɵ�����ͼ��λͼ����������
*����Ҫ�㣺

* 1��ͼ��ʧ�ܸ�������Ϊ�û������߳̾���
*�ƶ���һ����ͬ��ͼ�񡣶������ȡ�����ķ�����
*ƾ�����ͼ�������û������̹߳ر�����ʵ�ֵ�

*
* 2��ͼ����سɹ�����һ���ص����û������߳�������ʵ����ʵ��λͼ��ʾ��
*
* 3����������������Ƿ��ͼ����������Ҫ��������
* ͼ���û�����������ƶ�����һЩͼ����ʱ����Ҫ
* ���¼���λͼ����ʾ������
 */

interface ImageGetterCallback {
    public void imageLoaded(int pos, int offset, Bitmap bitmap,
                            boolean isThumb);
    public boolean wantsThumbnail(int pos, int offset);
    public boolean wantsFullImage(int pos, int offset);
    public int fullImageSizeToUse(int pos, int offset);
    public void completed();
    public int [] loadOrder();
}

class ImageGetter {

    @SuppressWarnings("unused")
    private static final String TAG = "ImageGetter";

    // ���������е��߳�
    private Thread mGetterThread;

    // ��ǰ�������š�����һ��ʵʱ�Ե��ۼӱ�����д�����߳��С�
    private int mCurrentSerial;

    // Baseλ�õļ�����-1��ʾĿǰû��������Ҫ��ɡ�
    private int mCurrentPosition = -1;

    // Ϊÿ��ͼ��ʵ�ֻص�
    private ImageGetterCallback mCB;

    // ͼ���б��е�ͼ��
   // private IImageList mImageList;
    
    private List<String> mImagePathList;

    // ʵ��һ����������Ա�ʵ�ֻص�
    private GetterHandler mHandler;

    // True if we want to cancel the current loading.
    private volatile boolean mCancel = true;

    // True if the getter thread is idle waiting.
    private boolean mIdle = false;

    // True when the getter thread should exit.
    private boolean mDone = false;

    private ContentResolver mCr;

    private class ImageGetterRunnable implements Runnable {

        private Runnable callback(final int position, final int offset,
                                  final boolean isThumb,
                                  final Bitmap bitmap,
                                  final int requestSerial) {
            return new Runnable() {
                public void run() {
                    // check for inflight callbacks that aren't applicable
                    // any longer before delivering them
                    if (requestSerial == mCurrentSerial) {
                        mCB.imageLoaded(position, offset, bitmap, isThumb);
                    } else if (bitmap != null) {
                        bitmap.recycle();
                    }
                }
            };
        }

        private Runnable completedCallback(final int requestSerial) {
            return new Runnable() {
                public void run() {
                    if (requestSerial == mCurrentSerial) {
                        mCB.completed();
                    }
                }
            };
        }

        public void run() {
            // Lower the priority of this thread to avoid competing with
            // the UI thread.
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

            while (true) {
                synchronized (ImageGetter.this) {
                    while (mCancel || mDone || mCurrentPosition == -1) {
                        if (mDone) return;
                        mIdle = true;
                        ImageGetter.this.notify();
                        try {
                            ImageGetter.this.wait();
                        } catch (InterruptedException ex) {
                            // ignore
                        }
                        mIdle = false;
                    }
                }

                executeRequest();

                synchronized (ImageGetter.this) {
                    mCurrentPosition = -1;
                }
            }
        }
        private void executeRequest() {
            //int imageCount = mImageList.getCount();
            int imageCount =mImagePathList.size();

            int [] order = mCB.loadOrder();
            for (int i = 0; i < order.length; i++) {
                if (mCancel) return;
                int offset = order[i];
                int imageNumber = mCurrentPosition + offset;
                if (imageNumber >= 0 && imageNumber < imageCount) {
                    if (!mCB.wantsThumbnail(mCurrentPosition, offset)) {
                        continue;
                    }

                    //IImage image = mImageList.getImageAt(imageNumber);
                    //if (image == null) continue;
                    if (mCancel) return;

                    //Bitmap b = image.thumbBitmap(IImage.NO_ROTATE);
                    Bitmap b =Util.getThumbBitmap(mImagePathList.get(imageNumber),IImage.NO_ROTATE);
                    if (b == null) continue;
                    if (mCancel) {
                        b.recycle();
                        return;
                    }

                    Runnable cb = callback(mCurrentPosition, offset,
                            true,
                            b,
                            mCurrentSerial);
                    mHandler.postGetterCallback(cb);
                }
            }

            for (int i = 0; i < order.length; i++) {
                if (mCancel) return;
                int offset = order[i];
                int imageNumber = mCurrentPosition + offset;
                if (imageNumber >= 0 && imageNumber < imageCount) {
                    if (!mCB.wantsFullImage(mCurrentPosition, offset)) {
                        continue;
                    }

                    //IImage image = mImageList.getImageAt(imageNumber);
                    //if (image == null) continue;
                    if (mCancel) return;

                    int sizeToUse = mCB.fullImageSizeToUse(
                            mCurrentPosition, offset);
                    //Bitmap b = image.fullSizeBitmap(sizeToUse, 3 * 1024 * 1024,
                           // IImage.NO_ROTATE, IImage.USE_NATIVE);
                    Bitmap b = Util.getFullSizeBitmap(mImagePathList.get(imageNumber), sizeToUse, 3 * 1024 * 1024, IImage.NO_ROTATE, IImage.USE_NATIVE);

                    if (b == null) continue;
                    if (mCancel) {
                        b.recycle();
                        return;
                    }

                    Runnable cb = callback(mCurrentPosition, offset,
                            false, b, mCurrentSerial);
                    mHandler.postGetterCallback(cb);
                }
            }

            mHandler.postGetterCallback(completedCallback(mCurrentSerial));
        }
    }

    public ImageGetter(ContentResolver cr) {
        mCr = cr;
        mGetterThread = new Thread(new ImageGetterRunnable());
        mGetterThread.setName("ImageGettter");
        mGetterThread.start();
    }

    // Cancels current loading (without waiting).
    public synchronized void cancelCurrent() {
        Util.Assert(mGetterThread != null);
        mCancel = true;
        BitmapManager.instance().cancelThreadDecoding(mGetterThread, mCr);
    }

    // Cancels current loading (with waiting).
    private synchronized void cancelCurrentAndWait() {
        cancelCurrent();
        while (mIdle != true) {
            try {
                wait();
            } catch (InterruptedException ex) {
                // ignore.
            }
        }
    }

    // Stops this image getter.
    public void stop() {
        synchronized (this) {
            cancelCurrentAndWait();
            mDone = true;
            notify();
        }
        try {
            mGetterThread.join();
        } catch (InterruptedException ex) {
            // Ignore the exception
        }
        mGetterThread = null;
    }

    public synchronized void setPosition(int position, ImageGetterCallback cb,
    		List<String> imageList, GetterHandler handler) {
        // Cancel the previous request.
        cancelCurrentAndWait();

        // Set new data.
        mCurrentPosition = position;
        mCB = cb;
        mImagePathList= imageList;
        mHandler = handler;
        mCurrentSerial += 1;

        // Kick-start the current request.
        mCancel = false;
        BitmapManager.instance().allowThreadDecoding(mGetterThread);
        notify();
    }
}

class GetterHandler extends Handler {
    private static final int IMAGE_GETTER_CALLBACK = 1;

    @Override
    public void handleMessage(Message message) {
        switch(message.what) {
            case IMAGE_GETTER_CALLBACK:
                ((Runnable) message.obj).run();
                break;
        }
    }

    public void postGetterCallback(Runnable callback) {
       postDelayedGetterCallback(callback, 0);
    }

    public void postDelayedGetterCallback(Runnable callback, long delay) {
        if (callback == null) {
            throw new NullPointerException();
        }
        Message message = Message.obtain();
        message.what = IMAGE_GETTER_CALLBACK;
        message.obj = callback;
        sendMessageDelayed(message, delay);
    }

    public void removeAllGetterCallbacks() {
        removeMessages(IMAGE_GETTER_CALLBACK);
    }
}
