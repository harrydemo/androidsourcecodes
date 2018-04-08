package com.poqop.document.models;

import com.poqop.document.events.BringUpZoomControlsEvent;
import com.poqop.document.events.EventDispatcher;
import com.poqop.document.events.ZoomChangedEvent;
import com.poqop.document.events.ZoomListener;

public class ZoomModel extends EventDispatcher
{
    private float zoom = 1.0f;
    private static final float INCREMENT_DELTA = 0.1f;
    private boolean horizontalScrollEnabled;
    private boolean isCommited;

    public void setZoom(float zoom)
    {
        zoom = Math.max(zoom, 1.0f);
        if (this.zoom != zoom)
        {
            float oldZoom = this.zoom;
            this.zoom = zoom;
            isCommited = false;
            dispatch(new ZoomChangedEvent(zoom, oldZoom));
        }
    }

    public float getZoom()
    {
        return zoom;
    }

    //增加缩放
    public void increaseZoom()
    {
        setZoom(getZoom() + INCREMENT_DELTA);
    }
    //减少缩放
    public void decreaseZoom()
    {
        setZoom(getZoom() - INCREMENT_DELTA);
    }

    public void toggleZoomControls()
    {
        dispatch(new BringUpZoomControlsEvent());
    }

    public void setHorizontalScrollEnabled(boolean horizontalScrollEnabled)
    {
        this.horizontalScrollEnabled = horizontalScrollEnabled;
    }

    public boolean isHorizontalScrollEnabled()
    {
        return horizontalScrollEnabled;
    }

    public boolean canDecrement()
    {
        return zoom > 1.0f;
    }

    public void commit()
    {
        if (!isCommited)
        {
            isCommited = true;
            dispatch(new ZoomListener.CommitZoomEvent());
        }
    }
}
