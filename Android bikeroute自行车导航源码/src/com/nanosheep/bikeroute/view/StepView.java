package com.nanosheep.bikeroute.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * This file is part of BikeRoute.
 * 
 * Copyright (C) 2011  Jonathan Gray
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * @author jono@nanosheep.net
 * @version Jan 21, 2011
 */

public class StepView extends LinearLayout 
{ 
	private Paint paint;
    
	public StepView(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public StepView(final Context context) {
		super(context);
		init();
	}

	private void init() {
		paint = new Paint();
		paint.setARGB(200, 75, 75, 75);
		paint.setAntiAlias(true);

	}
	
	public void setInnerPaint(final Paint paint) {
		this.paint = paint;
	}


    @Override
    protected void dispatchDraw(final Canvas canvas) {
    	
    	final RectF drawRect = new RectF();
    	drawRect.set(0,0, getMeasuredWidth(), getMeasuredHeight());
    	
    	canvas.drawRoundRect(drawRect, 5, 5, paint);
		
		super.dispatchDraw(canvas);
    }
}