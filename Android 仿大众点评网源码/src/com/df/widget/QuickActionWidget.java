package com.df.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.WindowManager;
import android.widget.PopupWindow;
import java.util.ArrayList;
import java.util.List;

import com.df.dianping.R;

public abstract class QuickActionWidget extends PopupWindow
{
  private static final int MEASURE_AND_LAYOUT_DONE = 2;
  private int mArrowOffsetY;
  private Context mContext;
  private boolean mDismissOnClick;
  private boolean mIsDirty;
  private boolean mIsOnTop;
  private final int[] mLocation = new int[2];
  private OnQuickActionClickListener mOnQuickActionClickListener;
  private int mPopupY;
  private int mPrivateFlags;
  private ArrayList<QuickAction> mQuickActions = new ArrayList();
  private final Rect mRect = new Rect();
  private int mScreenHeight;
  private int mScreenWidth;

  public QuickActionWidget(Context paramContext)
  {
    super(paramContext);
    this.mContext = paramContext;
    initializeDefault();
    setFocusable(true);
    setTouchable(true);
    setOutsideTouchable(true);
    setWidth(-2);
    setHeight(-2);
    WindowManager localWindowManager = (WindowManager)this.mContext.getSystemService("window");
    this.mScreenWidth = localWindowManager.getDefaultDisplay().getWidth();
    this.mScreenHeight = localWindowManager.getDefaultDisplay().getHeight();
  }

  private void initializeDefault()
  {
    this.mDismissOnClick = true;
    this.mArrowOffsetY = this.mContext.getResources().getDimensionPixelSize(R.dimen.arrow_offset);
  }

  private void prepareAnimationStyle()
  {
//    int i = this.mScreenWidth;
//    boolean bool = this.mIsOnTop;
//    int j = this.mRect.centerX();
//    
//    if (j <= i / 4)
//    {
//        setAnimationStyle(R.anim.);
//        return;
//    }
//    
//    if (j >= i * 3 / 4)
//    {
//       setAnimationStyle(m);
//       return;
//    }
    
    //setAnimationStyle(k);

  }

  private void showArrow()
  {
    View localView1 = getContentView();
    View localView3 = localView1.findViewById(R.id.gdi_arrow_up);
    View localView4 = localView1.findViewById(R.id.gdi_arrow_down);
    if (this.mIsOnTop)
    {
      localView3.setVisibility(0);
      localView4.setVisibility(4);
    }
    else
    {
    	localView3.setVisibility(4);
        localView4.setVisibility(0);
    }
  }

  public void addQuickAction(QuickAction paramQuickAction)
  {
    if (paramQuickAction != null)
    {
      this.mQuickActions.add(paramQuickAction);
      this.mIsDirty = true;
    }
  }

  public void clearAllQuickActions()
  {
    if (!this.mQuickActions.isEmpty())
    {
      this.mQuickActions.clear();
      this.mIsDirty = true;
    }
  }

  protected void clearQuickActions()
  {
    if (!this.mQuickActions.isEmpty())
      onClearQuickActions();
  }

  public int getArrowOffsetY()
  {
    return this.mArrowOffsetY;
  }

  protected Context getContext()
  {
    return this.mContext;
  }

  public boolean getDismissOnClick()
  {
    return this.mDismissOnClick;
  }

  protected OnQuickActionClickListener getOnQuickActionClickListener()
  {
    return this.mOnQuickActionClickListener;
  }

  protected int getScreenHeight()
  {
    return this.mScreenHeight;
  }

  protected int getScreenWidth()
  {
    return this.mScreenWidth;
  }

  protected void onClearQuickActions()
  {
  }

  protected abstract void onMeasureAndLayout(Rect paramRect, View paramView);

  protected abstract void populateQuickActions(List<QuickAction> paramList);

  public void setArrowOffsetY(int paramInt)
  {
    this.mArrowOffsetY = paramInt;
  }

  public void setContentView(int paramInt)
  {
    setContentView(LayoutInflater.from(this.mContext).inflate(paramInt, null));
  }

  public void setDismissOnClick(boolean paramBoolean)
  {
    this.mDismissOnClick = paramBoolean;
  }

  public void setOnQuickActionClickListener(OnQuickActionClickListener paramOnQuickActionClickListener)
  {
    this.mOnQuickActionClickListener = paramOnQuickActionClickListener;
  }

  protected void setWidgetSpecs(int paramInt, boolean paramBoolean)
  {
    this.mPopupY = paramInt;
    this.mIsOnTop = paramBoolean;
    this.mPrivateFlags = (0x2 | this.mPrivateFlags);
  }

  public void show(View paramView)
  {
    View localView = getContentView();
    if (localView == null)
      throw new IllegalStateException("You need to set the content view using the setContentView method");
    setBackgroundDrawable(new ColorDrawable(0));
    int[] arrayOfInt = this.mLocation;
    paramView.getLocationOnScreen(arrayOfInt);
    this.mRect.set(arrayOfInt[0], arrayOfInt[1], arrayOfInt[0] + paramView.getWidth(), arrayOfInt[1] + paramView.getHeight());
    if (this.mIsDirty)
    {
      clearQuickActions();
      populateQuickActions(this.mQuickActions);
    }
    onMeasureAndLayout(this.mRect, localView);
    if ((0x2 & this.mPrivateFlags) != 2)
      throw new IllegalStateException("onMeasureAndLayout() did not set the widget specification by calling setWidgetSpecs()");
    showArrow();
    prepareAnimationStyle();
    showAtLocation(paramView, 0, 0, this.mPopupY);
	  
  }

  public static abstract interface OnQuickActionClickListener
  {
    public abstract void onQuickActionClicked(QuickActionWidget paramQuickActionWidget, int paramInt);
  }
}
