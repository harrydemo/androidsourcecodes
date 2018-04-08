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

import com.google.calculator.R;
import com.google.calculator.ScientificCalculatorApplication;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Activity Equation Editor.
 * 
 * @author tulaurent@gmail.com (Laurent Tu)
 */
public class EquationEditor extends Activity {
  private static final int ADD_EQUATION_ID = 0;
  private static final int PLOT_ID = 1;
  private EquationEditorView equationsView;

  @Override
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    setContentView(R.layout.equation_editor);
    equationsView = (EquationEditorView) findViewById(R.id.equations);
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
      super.onCreateOptionsMenu(menu);
      menu.add(0, PLOT_ID, 0, R.string.menu_eq_editor_plot);
      menu.add(0, ADD_EQUATION_ID, 0, R.string.menu_eq_editor_add_equation);
      return true;
  }
  
  @Override
  public boolean onMenuItemSelected(int featureId, MenuItem item) {
    super.onMenuItemSelected(featureId, item);
    switch(item.getItemId()) {
      case PLOT_ID:
        ArrayList<PlotData> listPlotData = equationsView.getPlotData();
        ScientificCalculatorApplication application = (ScientificCalculatorApplication) getApplication();
        application.setLastPlotData(listPlotData);
        Intent i = new Intent(this, EquationPlotter.class);
        startActivityForResult(i, 0);
        break;
      case ADD_EQUATION_ID:
        equationsView.addEquation();
        break;
      }
    return true;
  }
}
