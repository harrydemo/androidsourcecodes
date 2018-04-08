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

import com.google.calculator.calc.InputTextView;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * View representing an equation.
 * 
 * @author tulaurent@gmail.com (Laurent Tu)
 */
public class EquationView extends LinearLayout {

  private CheckBox checkBox;
  private EditText editText;
  private TextView prefixText;
  
  private Equation equation = null;
  
  public EquationView(Context context) {
    super(context);
    this.setOrientation(HORIZONTAL);
    
    checkBox = new CheckBox(context);
    checkBox.setChecked(true);
    checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
      public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
        updateEquationEnabled(isChecked);
      }
    });
    prefixText = new TextView(context);
    editText = new InputTextView(context) {
      @Override
      protected void onTextChanged(CharSequence text, int start, int before, int after) {
        updateEquation(getText());
      }
    };
    editText.setHint("Enter your equation...");
    
    this.addView(checkBox, new LinearLayout.LayoutParams(
        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    this.addView(prefixText, new LinearLayout.LayoutParams(
        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    this.addView(editText, new LinearLayout.LayoutParams(
        LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
  }
  
  protected void updateEquationEnabled(boolean isEnabled) {
    if (equation != null) {
      equation.setEquationEnabled(isEnabled);
    }
  }

  public void updateEquation(CharSequence equationText) {
    if (equation != null) {
      equation.setEquation(equationText.toString());
    }
  }

  public void setEquationText(CharSequence text) {
    editText.setText(text);
  }

  private void setPrefixText(CharSequence text) {
    prefixText.setText(text);
  }

  public void setIndexInEquationEditor(int equationEditorIndex) {
    setPrefixText("y" + equationEditorIndex + "(x) = ");
  }

  public void setEquation(Equation equation) {
    this.equation = equation;
  }
}
