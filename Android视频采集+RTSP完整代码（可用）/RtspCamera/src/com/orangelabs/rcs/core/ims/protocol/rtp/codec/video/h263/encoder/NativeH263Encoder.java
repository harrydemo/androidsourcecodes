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

package com.orangelabs.rcs.core.ims.protocol.rtp.codec.video.h263.encoder;


// Referenced classes of package com.orangelabs.rcs.core.ims.protocol.rtp.codec.video.h263.encoder:
//            NativeH263EncoderParams

public class NativeH263Encoder
{

    public NativeH263Encoder()
    {
    }

    public static native int InitEncoder(NativeH263EncoderParams nativeh263encoderparams);

    public static native byte[] EncodeFrame(byte abyte0[], long l);

    public static native int DeinitEncoder();

    static 
    {
        String libname = "H263Encoder";
        try
        {
            System.loadLibrary(libname);
        }
        catch(UnsatisfiedLinkError unsatisfiedlinkerror) { }
    }
}
