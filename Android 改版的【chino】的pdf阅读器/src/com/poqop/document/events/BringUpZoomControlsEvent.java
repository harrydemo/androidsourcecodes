package com.poqop.document.events;

public class BringUpZoomControlsEvent extends SafeEvent<BringUpZoomControlsListener>
{
    @Override
    public void dispatchSafely(BringUpZoomControlsListener listener)
    {
        listener.toggleZoomControls();
    }
}
