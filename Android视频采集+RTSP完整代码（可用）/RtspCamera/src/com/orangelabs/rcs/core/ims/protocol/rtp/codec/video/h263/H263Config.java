/*******************************************************************************
 * Software Name : RCS IMS Stack
 *
 * Copyright (C) 2010 France Telecom S.A.
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
 ******************************************************************************/

package com.orangelabs.rcs.core.ims.protocol.rtp.codec.video.h263;

/**
 * Default H263 settings.
 *
 * @author hlxn7157
 */
public class H263Config {
    /**
     * H263 Codec Name
     */
    public final static String CODEC_NAME = "h263-2000";

    /**
     * Default clock rate
     */
    public final static int CLOCK_RATE = 90000;

    /**
     * Default codec params
     */
    public final static String CODEC_PARAMS = "profile=0;level=10";
//    public final static String CODEC_PARAMS = "profile=0;level=20";

    /**
     * Default video width
     */
//    public final static int VIDEO_WIDTH = 176;
    public final static int VIDEO_WIDTH = 352;

    /**
     * Default video height
     */
//    public final static int VIDEO_HEIGHT = 144;
    public final static int VIDEO_HEIGHT = 288;

    /**
     * Default video frame rate
     */
    public final static int FRAME_RATE = 21;

    /**
     * Default video bit rate
     */
//      public final static int BIT_RATE = 64000;
    public final static int BIT_RATE = 128000;
}
