/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dream.gallery;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.InputStream;

/**
 * The interface of all images used in gallery.
 */
public interface IImage {
    static final int THUMBNAIL_TARGET_SIZE = 320;
    static final int MINI_THUMB_TARGET_SIZE = 96;
    static final int THUMBNAIL_MAX_NUM_PIXELS = 512 * 384;
    static final int MINI_THUMB_MAX_NUM_PIXELS = 128 * 128;
    static final int UNCONSTRAINED = -1;

    /** Get the image list which contains this image. */
    public abstract IImageList getContainer();

    /** Get the bitmap for the full size image. */
    public abstract Bitmap fullSizeBitmap(int minSideLength,
            int maxNumberOfPixels);
    public abstract Bitmap fullSizeBitmap(int minSideLength,
            int maxNumberOfPixels, boolean rotateAsNeeded, boolean useNative);
    public abstract int getDegreesRotated();
    public static final boolean ROTATE_AS_NEEDED = true;
    public static final boolean NO_ROTATE = false;
    public static final boolean USE_NATIVE = true;
    public static final boolean NO_NATIVE = false;

    /** 获取输入流与一个给定的全尺寸图像. */
    public abstract InputStream fullSizeImageData();
    public abstract Uri fullSizeImageUri();

    /**获取的图像数据（全尺寸） */
    public abstract String getDataPath();

    // 获取图像的标题
    public abstract String getTitle();

    // 获取原图像
    public abstract long getDateTaken();

    public abstract String getMimeType();

    public abstract int getWidth();

    public abstract int getHeight();

    // 获取图像属性
    public abstract boolean isReadonly();
    public abstract boolean isDrm();

    // 得到位图中的缩略图
    public abstract Bitmap thumbBitmap(boolean rotateAsNeeded);

    // 获得位图的迷你缩图
    public abstract Bitmap miniThumbBitmap();

    // 位图的旋转
    public abstract boolean rotateImageBy(int degrees);

}
