/*
 * Copyright (C) 2008 The Android Open Source Project
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

package com.android.email.core.internet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

import com.android.email.core.Base64OutputStream;
import com.android.email.core.mail.Body;
import com.android.email.core.mail.MessagingException;



/**
 * A Body that is backed by a temp file. The Body exposes a getOutputStream method that allows
 * the user to write to the temp file. After the write the body is available via getInputStream
 * and writeTo one time. After writeTo is called, or the InputStream returned from
 * getInputStream is closed the file is deleted and the Body should be considered disposed of.
 */
public class BinaryTempFileBody implements Body {
    private static File mTempDirectory;

    private File mFile;

    public static void setTempDirectory(File tempDirectory) {
        mTempDirectory = tempDirectory;
    }

    public BinaryTempFileBody() throws IOException {
        if (mTempDirectory == null) {
            throw new
                RuntimeException("setTempDirectory has not been called on BinaryTempFileBody!");
        }
    }

    public OutputStream getOutputStream() throws IOException {
        mFile = File.createTempFile("body", null, mTempDirectory);
        mFile.deleteOnExit();
        return new FileOutputStream(mFile);
    }

    public InputStream getInputStream() throws MessagingException {
        try {
            return new BinaryTempFileBodyInputStream(new FileInputStream(mFile));
        }
        catch (IOException ioe) {
            throw new MessagingException("Unable to open body", ioe);
        }
    }

    public void writeTo(OutputStream out) throws IOException, MessagingException {
        InputStream in = getInputStream();
        Base64OutputStream base64Out = new Base64OutputStream(out);
        IOUtils.copy(in, base64Out);
        base64Out.close();
        mFile.delete();
    }

    class BinaryTempFileBodyInputStream extends FilterInputStream {
        public BinaryTempFileBodyInputStream(InputStream in) {
            super(in);
        }

        @Override
        public void close() throws IOException {
            super.close();
            mFile.delete();
        }
    }
}
