package com.poqop.document.codec;

import android.graphics.Bitmap;
import android.graphics.RectF;

public interface CodecPage
{
    boolean isDecoding();

    void waitForDecode();

    int getWidth();

    int getHeight();

    Bitmap renderBitmap(int width, int height, RectF pageSliceBounds);

    void recycle();
}
