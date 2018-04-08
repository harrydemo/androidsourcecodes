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

package com.google.calculator;

import com.google.calculator.calc.CalculatorInputTextView;
import com.google.calculator.calc.CalculatorResultListView;
import com.google.calculator.plot.EquationEditor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.widget.Button;

/**
 * Main activity of the application.
 * 
 * @author tulaurent@gmail.com (Laurent Tu)
 */
public class ScientificCalculator extends Activity {
  
  private static final int EQ_EDITOR_ID = 0;
  private CalculatorInputTextView calculatorInputTextView;
  private Button submitButton;
  private CalculatorResultListView resultDisplay;
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    setContentView(R.layout.main);
    
    calculatorInputTextView = (CalculatorInputTextView) findViewById(R.id.edition);
    submitButton = (Button) findViewById(R.id.submit_button);
    resultDisplay = (CalculatorResultListView) findViewById(R.id.results);
    
    calculatorInputTextView.setDisplay(resultDisplay);
    resultDisplay.setCalculatorTextView(calculatorInputTextView);
    
    submitButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View arg0) {  
        calculatorInputTextView.performComputation();
      }
    });
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
      super.onCreateOptionsMenu(menu);
      menu.add(0, EQ_EDITOR_ID, 0, R.string.menu_eq_editor);
      return true;
  }
  
  @Override
  public boolean onMenuItemSelected(int featureId, MenuItem item) {
    super.onMenuItemSelected(featureId, item);
    switch(item.getItemId()) {
    case EQ_EDITOR_ID:
      Intent i = new Intent(this, EquationEditor.class);
      startActivityForResult(i, 0);
      break;
    }
    return true;
  }
  
}