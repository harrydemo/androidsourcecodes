package com.df.widget;

import android.content.Context;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.List;

import com.df.dianping.R;

public class QuickActionBar extends QuickActionWidget
{
  private View.OnClickListener mClickHandlerInternal = new View.OnClickListener()
  {
    public void onClick(View paramView)
    {
//      QuickActionWidget.OnQuickActionClickListener localOnQuickActionClickListener = QuickActionBar.this.getOnQuickActionClickListener();
//      int i;
//      if (localOnQuickActionClickListener != null)
//        i = QuickActionBar.this.mQuickActions.size();
//      for (int j = 0; ; j++)
//      {
//        if (j >= i);
//        while (true)
//        {
//          if (QuickActionBar.this.getDismissOnClick())
//            QuickActionBar.this.dismiss();
//          return;
//          if (paramView != ((QuickAction)QuickActionBar.this.mQuickActions.get(j)).mView.get())
//            break;
//          localOnQuickActionClickListener.onQuickActionClicked(QuickActionBar.this, j);
//        }
//      }
    }
  };
  private ViewGroup mQuickActionItems;
  private List<QuickAction> mQuickActions;
  private ViewGroup mRack;
  private Animation mRackAnimation;
  private HorizontalScrollView mScrollView;

  public QuickActionBar(Context paramContext)
  {
    super(paramContext);
    this.mRackAnimation = AnimationUtils.loadAnimation(paramContext, R.anim.rack);
    this.mRackAnimation.setInterpolator(new Interpolator()
    {
      public float getInterpolation(float paramFloat)
      {
        float f = 1.55F * paramFloat - 1.1F;
        return 1.2F - f * f;
      }
    });
    setContentView(R.layout.quick_action_bar);
    View localView = getContentView();
    this.mRack = ((ViewGroup)localView.findViewById(R.id.gdi_rack));
    this.mQuickActionItems = ((ViewGroup)localView.findViewById(R.id.gdi_quick_action_items));
    this.mScrollView = ((HorizontalScrollView)localView.findViewById(R.id.gdi_scroll));
  }

  protected void onClearQuickActions()
  {
    super.onClearQuickActions();
    this.mQuickActionItems.removeAllViews();
  }

  protected void onMeasureAndLayout(Rect paramRect, View paramView)
  {
    paramView.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
    paramView.measure(View.MeasureSpec.makeMeasureSpec(getScreenWidth(), View.MeasureSpec.AT_MOST), -2);
    int i = paramView.getMeasuredHeight();
    int j = getArrowOffsetY();
    boolean bool;
    int k = j + (paramRect.top - i);
    
  setWidgetSpecs(k, false);
    
  }

  protected void populateQuickActions(List<QuickAction> paramList)
  {
    this.mQuickActions = paramList;
    LayoutInflater localLayoutInflater = LayoutInflater.from(getContext());
    Iterator localIterator = paramList.iterator();
    while (true)
    {
      if (!localIterator.hasNext())
        return;
      QuickAction localQuickAction = (QuickAction)localIterator.next();
      TextView localTextView = (TextView)localLayoutInflater.inflate(R.layout.quick_action_bar_item, this.mQuickActionItems, false);
      localTextView.setText(localQuickAction.mTitle);
      localTextView.setCompoundDrawablesWithIntrinsicBounds(null, localQuickAction.mDrawable, null, null);
      localTextView.setOnClickListener(this.mClickHandlerInternal);
      this.mQuickActionItems.addView(localTextView);
      localQuickAction.mView = new WeakReference(localTextView);
    }
  }

  public void show(View paramView)
  {
    super.show(paramView);
    //this.mScrollView.scrollTo(0, 0);
    this.mRack.startAnimation(this.mRackAnimation);
  }
}
