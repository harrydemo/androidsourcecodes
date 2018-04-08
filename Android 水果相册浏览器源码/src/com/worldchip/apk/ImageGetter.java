

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
*加载策略:对于任何给定的图像的缩略图，负荷
*到内存和后回调结果显示位图。
*进行负荷的完整图像位图。共有以下
*三个要点：

* 1。图像失败负荷是因为用户界面线程决定
*移动到一个不同的图像。而这个“取消”的发生是
*凭借包含图像解码的用户界面线程关闭流来实现的

*
* 2。图像加载成功。后一个回调到用户界面线程真正在实际上实现位图显示。
*
* 3。当后，运行它检查是否该图像是我们想要截下来的
* 图像。用户界面可能有移动其他一些图像，这时便需要
* 把新加载位图的显示区域上
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

    // 正在运作中的线程
    private Thread mGetterThread;

    // 当前请求的序号。这是一个实时性的累加变量，写在主线程中。
    private int mCurrentSerial;

    // Base位置的检索。-1表示目前没有请求需要完成。
    private int mCurrentPosition = -1;

    // 为每个图像实现回调
    private ImageGetterCallback mCB;

    // 图像列表中的图像
   // private IImageList mImageList;
    
    private List<String> mImagePathList;

    // 实例一个处理程序，以便实现回调
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
