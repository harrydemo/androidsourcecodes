// Copyright 2008 Google Inc.
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
//      http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.calculator.plot;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * View plotting the equation.
 * 
 * @author tulaurent@gmail.com (Laurent Tu)
 *
 */
public class EquationPlotterView extends View {

  public EquationPlotterView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public EquationPlotterView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public EquationPlotterView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

private ArrayList<PlotData> listPlotData = null; 
 
  
  public void setListPlotData(ArrayList<PlotData> listPlotData) {
    this.listPlotData = listPlotData;
    this.invalidate();
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    
    drawBackground(canvas);
    
    int screenWidth = getWidth();
    int screenHeight = getHeight();
    
    for (PlotData plotData : listPlotData) {
      Paint linePaint = new Paint();
      linePaint.setAntiAlias(true);
      linePaint.setARGB(255, 0, 0, 0);
      
      double scaleX = screenWidth/(plotData.maxX-plotData.minX);
      double scaleY = screenHeight/(plotData.maxY-plotData.minY);
      double offsetX = -plotData.minX;
      double offsetY = -plotData.minY;
      
      for (int i = 0; i < plotData.xValues.length -1; ++i) {
        canvas.drawLine(
            (float) ((plotData.xValues[i] + offsetX) * scaleX),
            (float) (1 + screenHeight - (plotData.yValues[i] + offsetY) * scaleY),
            (float) ((plotData.xValues[i+1] + offsetX) * scaleX),
            (float) (1 + screenHeight - (plotData.yValues[i+1] + offsetY) * scaleY),
            linePaint);
      }
      
      canvas.save();
    }
  }

  private void drawBackground(Canvas canvas) {
    // Draw RGB.
    canvas.drawRGB(255, 255, 255);
    
    // Draw Axis.
    int screenWidth = getWidth();
    int screenHeight = getHeight();
    Paint axisPaint = new Paint();
    axisPaint.setARGB(255, 0, 0, 255);
    
    Paint gridPaint = new Paint();
    gridPaint.setARGB(20, 0, 0, 0);
    
    // TODO(Laurent): Can do better (add grid step instead of doing multiplication).
    for (int i = 1; i < 10; i++) {
      if (i != 5) {
        canvas.drawLine(0, screenHeight/10*i, screenWidth, screenHeight/10*i, gridPaint);
        canvas.drawLine(screenWidth/10*i, 0, screenWidth/10*i, screenHeight, gridPaint);
      } else {
        canvas.drawLine(0, screenHeight/2, screenWidth, screenHeight/2, axisPaint);
        canvas.drawLine(screenWidth/2, 0, screenWidth/2, screenHeight, axisPaint);        
      }
    }
  }


}
