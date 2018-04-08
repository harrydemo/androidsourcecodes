 /*************************************************************************** 
 *              Copyright (C) 2009 Andrico Team                             * 
 *              http://code.google.com/p/andrico/                           *
 *                             												*
 * Licensed under the Apache License, Version 2.0 (the "License");			*
 * you may not use this file except in compliance with the License.			*
 * 																			*	
 * You may obtain a copy of the License at 									*
 * http://www.apache.org/licenses/LICENSE-2.0								*
 *																			*
 * Unless required by applicable law or agreed to in writing, software		*
 * distributed under the License is distributed on an "AS IS" BASIS,		*
 *																			*
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.	*
 * See the License for the specific language governing permissions and		*
 * limitations under the License.											*
 ****************************************************************************/

package org.andrico.andjax.http;

import org.apache.commons.io.IOUtils;
import org.apache.http.entity.mime.MIME;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.james.mime4j.message.AbstractBody;
import org.apache.james.mime4j.message.BinaryBody;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class ByteArrayBody extends AbstractBody implements BinaryBody, ContentBody 
{
    private static final String LOG = "ByteArrayBody";

    public interface WriteToProgressHandler 
    {
        public void setProgress(long progress, long total);
    }

    private final byte[] mBytes;
    private final String mFilename;
    private final WriteToProgressHandler mWriteToProgressHandler;

    public ByteArrayBody(final byte[] bytes, final String filename, WriteToProgressHandler writeToProgressHandler) 
    {
        Log.d(LOG, "ByteArrayBody for " + filename + " is " + String.valueOf(bytes.length)
                + " long.");
        mBytes = bytes;
        mFilename = filename;
        mWriteToProgressHandler = writeToProgressHandler;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.http.entity.mime.ContentDescriptor#getCharset()
     */
    public Charset getCharset() 
    {
        return null;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.http.entity.mime.ContentDescriptor#getContentLength()
     */
    public long getContentLength() 
    {
        return mBytes.length;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.http.entity.mime.content.ContentBody#getFilename()
     */
    public String getFilename() 
    {
        return mFilename;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.james.mime4j.message.BinaryBody#getInputStream()
     */
    public InputStream getInputStream() throws IOException 
    {
        return new ByteArrayInputStream(mBytes);
    }

    /*
     * (non-Javadoc)
     * @see org.apache.http.entity.mime.ContentDescriptor#getMimeType()
     */
    public String getMimeType() 
    {
        return "application/octet-stream";
    }

    /*
     * (non-Javadoc)
     * @see org.apache.http.entity.mime.ContentDescriptor#getTransferEncoding()
     */
    public String getTransferEncoding() 
    {
        return MIME.ENC_BINARY;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.james.mime4j.message.Body#writeTo(java.io.OutputStream)
     */
    public void writeToOld(OutputStream out) throws IOException 
    {
        if (out == null) 
        {
            throw new IllegalArgumentException("Output stream may not be null");
        }
        try 
        {
            IOUtils.copy(getInputStream(), out);
        } 
        finally 
        {
        }
    }

    /*
     * (non-Javadoc)
     * @see org.apache.james.mime4j.message.Body#writeTo(java.io.OutputStream)
     */
    public void writeTo(OutputStream out) throws IOException 
    {
        if (out == null) 
        {
            throw new IllegalArgumentException("Output stream may not be null");
        }
        try 
        {
            InputStream in = getInputStream();
            byte[] b = new byte[2048];
            int count;
            int total = 0;

            while ((count = in.read(b)) > 0) 
            {
                out.write(b, 0, count);
                total += count;
                if (mWriteToProgressHandler != null) 
                {
                    mWriteToProgressHandler.setProgress(total, getContentLength());
                }
            }
            Log.d(LOG, "Wrote a total of: " + total + " expected to write: " + getContentLength());
        } 
        finally 
        {
        }
    }

}
