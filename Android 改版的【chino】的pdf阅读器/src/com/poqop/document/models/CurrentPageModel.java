package com.poqop.document.models;

import com.poqop.document.events.CurrentPageListener;
import com.poqop.document.events.EventDispatcher;

/*
 * 当前页面
 */
public class CurrentPageModel extends EventDispatcher
{
    private int currentPageIndex;

    /*
     * 设置当前页
     */
    public void setCurrentPageIndex(int currentPageIndex)
    {
        if (this.currentPageIndex != currentPageIndex)
        {
            this.currentPageIndex = currentPageIndex;
            dispatch(new CurrentPageListener.CurrentPageChangedEvent(currentPageIndex));
        }
    }
}
