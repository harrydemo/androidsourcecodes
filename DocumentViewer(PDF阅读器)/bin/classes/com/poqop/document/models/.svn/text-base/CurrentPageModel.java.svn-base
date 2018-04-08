package com.poqop.document.models;

import com.poqop.document.events.CurrentPageListener;
import com.poqop.document.events.EventDispatcher;

public class CurrentPageModel extends EventDispatcher
{
    private int currentPageIndex;

    public void setCurrentPageIndex(int currentPageIndex)
    {
        if (this.currentPageIndex != currentPageIndex)
        {
            this.currentPageIndex = currentPageIndex;
            dispatch(new CurrentPageListener.CurrentPageChangedEvent(currentPageIndex));
        }
    }
}
