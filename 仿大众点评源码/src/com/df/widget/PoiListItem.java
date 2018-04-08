package com.df.widget;

import com.df.dianping.R;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PoiListItem extends LinearLayout
{

	private static int CARD_PADDING = 0;
	private static int CHECKIN_PADDING = 0;
	public static float DISTANCE_FACTOR = 1.0F;
	private static int PROMO_PADDING = 0;
	private static int TUAN_PADDING = 0;
	
	View card;
	View checkin;
	PoiStar star;
	View promo;
	TextView name;
	TextView price;
	TextView addr;
	TextView distance;
	View tuan;

	public PoiListItem(Context context) {
		super(context);
	}

	public PoiListItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setPoiData(String name, String price, String addr, int stars,
			boolean isTuan, boolean isCard, boolean isPromo, boolean isCheckIn) {
		
		int m = 0;
		int i = 0;
		int j = 0;
		int k = 0;
		
		this.addr.setText(addr);
		this.price.setText(price);
		this.star.setStar(stars);
		this.name.setText(name);
		
		if(isTuan)// have group purchase("TuanGou")
		{
			if(TUAN_PADDING == 0)
			{
				TUAN_PADDING = BitmapFactory.decodeResource(getResources(), R.drawable.ic_tuan_small).getWidth();
				TUAN_PADDING += (int)(6.0F * getResources().getDisplayMetrics().density);
			}
			m = TUAN_PADDING;
			this.tuan.setVisibility(View.VISIBLE);
		}
		else
		{
			this.tuan.setVisibility(View.INVISIBLE);
		}
		
		if(isPromo)
		{
			if(PROMO_PADDING == 0)
			{
				PROMO_PADDING = BitmapFactory.decodeResource(getResources(), R.drawable.ic_promo_small).getWidth();
				PROMO_PADDING += (int)(6.0F * getResources().getDisplayMetrics().density);
			}
			m += PROMO_PADDING;
			i += PROMO_PADDING;
			this.promo.setVisibility(View.VISIBLE);
		}
		else
		{
			this.promo.setVisibility(View.INVISIBLE);
		}
		
		if(isCheckIn)
		{
			if(CHECKIN_PADDING == 0)
			{
				CHECKIN_PADDING = BitmapFactory.decodeResource(getResources(), R.drawable.ic_checkin_small).getWidth();
				CHECKIN_PADDING += (int)(6.0F * getResources().getDisplayMetrics().density);
			}
			m += CHECKIN_PADDING;
			i += CHECKIN_PADDING;
			j += CHECKIN_PADDING;
			this.checkin.setVisibility(View.VISIBLE);
		}
		else
		{
			this.checkin.setVisibility(View.INVISIBLE);
		}
		
		if(isCard)
		{
			if(CARD_PADDING == 0)
			{
				CARD_PADDING = BitmapFactory.decodeResource(getResources(), R.drawable.ic_card_small).getWidth();
				CARD_PADDING += (int)(6.0F * getResources().getDisplayMetrics().density);
			}
			m += CARD_PADDING;
			i += CARD_PADDING;
			j += CARD_PADDING;
			k += CARD_PADDING;
			this.card.setVisibility(View.VISIBLE);
		}
		else
		{
			this.card.setVisibility(View.INVISIBLE);
		}
		
		this.tuan.setPadding(this.tuan.getPaddingLeft(), this.tuan.getPaddingTop(), i, this.tuan.getPaddingBottom());
		this.promo.setPadding(this.promo.getPaddingLeft(), this.promo.getPaddingTop(), j, this.promo.getPaddingBottom());
		this.checkin.setPadding(this.checkin.getPaddingLeft(), this.checkin.getPaddingTop(), k, this.checkin.getPaddingBottom());
		this.name.setPadding(this.name.getPaddingLeft(), this.name.getPaddingTop(), m, this.name.getPaddingBottom());
	}
	
	protected void onFinishInflate() {
		super.onFinishInflate();
		this.star = ((PoiStar) findViewById(R.id.poistar));
		this.tuan = findViewById(R.id.tuan);
		this.promo = findViewById(R.id.promo);
		this.checkin = findViewById(R.id.checkin);
		this.card = findViewById(R.id.card);
		this.name = ((TextView) findViewById(R.id.name));
		this.price = ((TextView) findViewById(R.id.price));
		this.addr = ((TextView) findViewById(R.id.addr));
		this.distance = ((TextView) findViewById(R.id.distance));
	}
	
	public void setDistanceText(String dis)
	{
		this.distance.setText(dis);
	}
	
}