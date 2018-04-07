package com.poqop.document.models;

import com.poqop.document.events.DecodingProgressListener;
import com.poqop.document.events.EventDispatcher;

public class DecodingProgressModel extends EventDispatcher
{
    private int currentlyDecoding;

    public void increase()
    {
        currentlyDecoding++;
        dispatchChanged();
    }

    private void dispatchChanged()
    {
        dispatch(new DecodingProgressListener.DecodingProgressEvent(currentlyDecoding));
    }

    public void decrease()
    {
        currentlyDecoding--;
        dispatchChanged();
    }
}
