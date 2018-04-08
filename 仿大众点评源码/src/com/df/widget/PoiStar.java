package com.df.widget;

import com.df.dianping.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;


public class PoiStar extends ImageView {

	  private static Drawable DRAWABLE00;
	  private static Drawable DRAWABLE05;
	  private static Drawable DRAWABLE10;
	  private static Drawable DRAWABLE15;
	  private static Drawable DRAWABLE20;
	  private static Drawable DRAWABLE25;
	  private static Drawable DRAWABLE30;
	  private static Drawable DRAWABLE35;
	  private static Drawable DRAWABLE40;
	  private static Drawable DRAWABLE45;
	  private static Drawable DRAWABLE50;
	 
	  private int star = -1;
	
	public PoiStar(Context context) {
		super(context);
	}

	public PoiStar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PoiStar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public int getStar()
	{
		return this.star;
	}
	
	/**
	 * 
	 * @param paramInt the valid value only contain 
	 *  0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50
	 */
	public void setStar(int paramInt)
	{
		star = paramInt;
		switch(paramInt)
		{
			case 0:
			{	
				if(DRAWABLE00 == null)
				{
					DRAWABLE00 = getResources().getDrawable(R.drawable.star00);
				}
				setImageDrawable(DRAWABLE00);
				break;
			}
			
			case 5:
			{
				if(DRAWABLE05 == null)
				{
					DRAWABLE05 = getResources().getDrawable(R.drawable.star05);
				}
				setImageDrawable(DRAWABLE05);
				break;
			}
			
			case 10:
			{
				if(DRAWABLE10 == null)
				{
					DRAWABLE10 = getResources().getDrawable(R.drawable.star10);
				}
				setImageDrawable(DRAWABLE10);
				break;
			}
			
			case 15:
			{
				if(DRAWABLE15 == null)
				{
					DRAWABLE15 = getResources().getDrawable(R.drawable.star15);
				}
				setImageDrawable(DRAWABLE15);
				break;
			}
			
			case 20:
			{
				if(DRAWABLE20 == null)
				{
					DRAWABLE20 = getResources().getDrawable(R.drawable.star20);
				}
				setImageDrawable(DRAWABLE20);
				break;
			}
			
			case 25:
			{
				if(DRAWABLE25 == null)
				{
					DRAWABLE25 = getResources().getDrawable(R.drawable.star25);
				}
				setImageDrawable(DRAWABLE25);
				break;
			}
			
			case 30:
			{
				if(DRAWABLE30 == null)
				{
					DRAWABLE30 = getResources().getDrawable(R.drawable.star30);
				}
				setImageDrawable(DRAWABLE30);
				break;
			}
			case 35:
			{
				if(DRAWABLE35 == null)
				{
					DRAWABLE35 = getResources().getDrawable(R.drawable.star35);
				}
				setImageDrawable(DRAWABLE35);
				break;
			}
			
			case 40:
			{
				if(DRAWABLE40 == null)
				{
					DRAWABLE40 = getResources().getDrawable(R.drawable.star40);
				}
				setImageDrawable(DRAWABLE40);
				break;
			}
			case 45:
			{
				if(DRAWABLE45 == null)
				{
					DRAWABLE45 = getResources().getDrawable(R.drawable.star45);
				}
				setImageDrawable(DRAWABLE45);
				break;
			}
			
			case 50:
			{
				if(DRAWABLE50 == null)
				{
					DRAWABLE50 = getResources().getDrawable(R.drawable.star50);
				}
				setImageDrawable(DRAWABLE50);
				break;
			}
			default:
			{
				setImageDrawable(null);
			}
		}
	}
}