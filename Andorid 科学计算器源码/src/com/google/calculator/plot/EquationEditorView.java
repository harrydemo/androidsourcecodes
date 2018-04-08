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
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * View for the equation editor. It is a ListView of EquationView elements, adapted
 * from equations elements, via EquationAdapter.
 * 
 * @author tulaurent@gmail.com (Laurent Tu)
 *
 */
public class EquationEditorView extends ListView {

  public EquationEditorView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	    initializeEquationEditorView();
	}

	public EquationEditorView(Context context, AttributeSet attrs) {
		super(context, attrs);
	    initializeEquationEditorView();
	}

	public EquationEditorView(Context context) {
		super(context);
	    initializeEquationEditorView();
	}

private ArrayList<Equation> listEquations;
  private EquationAdapter adapter;
  

  private void initializeEquationEditorView() {
    listEquations = new ArrayList<Equation>();
    listEquations.add(new Equation());
    adapter = new EquationAdapter(listEquations);
    this.setAdapter(adapter);
  }

  public void addEquation() {
    listEquations.add(new Equation());
    adapter.notifyDataSetChanged();
  }

  public ArrayList<PlotData> getPlotData() {
    // TODO(Laurent): Put this in a Service / asynchronous call.
    ArrayList<PlotData> listPlotData = new ArrayList<PlotData>();
    for (Equation equation : listEquations) {
      PlotData plotData = equation.getPlotData();
      if (plotData != null) {
        listPlotData.add(plotData);
      }
    }
    
    return listPlotData;
  }

}
