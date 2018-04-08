package com.df.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import java.lang.ref.WeakReference;

public class QuickAction
{
  public Drawable mDrawable;
  public CharSequence mTitle;
  WeakReference<View> mView;

  public QuickAction(Context paramContext, int paramInt1, int paramInt2)
  {
    this.mDrawable = paramContext.getResources().getDrawable(paramInt1);
    this.mTitle = paramContext.getResources().getString(paramInt2);
  }

  public QuickAction(Context paramContext, int paramInt, CharSequence paramCharSequence)
  {
    this.mDrawable = paramContext.getResources().getDrawable(paramInt);
    this.mTitle = paramCharSequence;
  }

  public QuickAction(Context paramContext, Drawable paramDrawable, int paramInt)
  {
    this.mDrawable = paramDrawable;
    this.mTitle = paramContext.getResources().getString(paramInt);
  }

  public QuickAction(Drawable paramDrawable, CharSequence paramCharSequence)
  {
    this.mDrawable = paramDrawable;
    this.mTitle = paramCharSequence;
  }
}
