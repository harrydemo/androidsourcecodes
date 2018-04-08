package org.vudroid.pdfdroid.codec;

import android.content.ContentResolver;
import com.poqop.document.VuDroidLibraryLoader;
import com.poqop.document.codec.CodecContext;
import com.poqop.document.codec.CodecDocument;

public class PdfContext implements CodecContext
{
    static
    {
        VuDroidLibraryLoader.load();
    }

    public CodecDocument openDocument(String fileName)
    {
        return PdfDocument.openDocument(fileName, "");
    }

    public void setContentResolver(ContentResolver contentResolver)
    {
        //TODO
    }

    public void recycle() {
    }
}
