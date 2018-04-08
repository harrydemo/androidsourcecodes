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

package com.google.calculator.calc;

import com.google.calculator.evaluation.PredefinedFunctions;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

/**
 * Input text view with autocompletion by word.
 * 
 * @author tulaurent@gmail.com (Laurent Tu)
 */
public class InputTextView extends AutoCompleteTextView {

  public InputTextView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    setSingleLine();
    setupAutoComplete();
	}

	public InputTextView(Context context, AttributeSet attrs) {
    super(context, attrs);
    setSingleLine();
    setupAutoComplete();
	}

	public InputTextView(Context context) {
    super(context);
    setSingleLine();
    setupAutoComplete();
	}

  private static final String[] kKeywords = PredefinedFunctions.getFunctionsName();
  private int lastWordStartIndex;
  private int lastCursorPosition;

  /**
   * Setup autocomplete.
   */
  protected void setupAutoComplete() {
    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
        this.getContext(), android.R.layout.simple_dropdown_item_1line, kKeywords);
    setAdapter(arrayAdapter);
    setThreshold(1);
  }

  protected void resetText() {
    setText("", BufferType.EDITABLE);
  }

  @Override
  protected void performFiltering(CharSequence text, int keyCode) {
    // Detect the beginning of the last word in the line.
    Log.i("filtering", "starting");
    lastCursorPosition = getSelectionEnd();
    lastWordStartIndex = 0;
    for (int i = lastCursorPosition-1; i >= 0; i--) {
      char c = text.charAt(i);
      if (c == ' ' ||
          c == '(' ||
          c == ')' ||
          c == '+' ||
          c == '-' ||
          c == '*' ||
          c == '/' ||
          c == '^') {
        lastWordStartIndex = i+1;
        break;
      }
    }
    
    // Extract last word.
    CharSequence lastWord = null;
    lastWord = text.subSequence(lastWordStartIndex, lastCursorPosition);
    
    // Perform filtering if last word length is bigger than filtering threshold.
    if (lastWord.length() >= this.getThreshold()) {
      super.performFiltering(lastWord, keyCode);
    }
  }

  @Override
  protected void replaceText(CharSequence text) {
    CharSequence currentText = getText();
    
    // Replace currently auto-completed word.
    CharSequence textKeptOnLeft = currentText.subSequence(0, lastWordStartIndex);
    CharSequence textKeptOnRight = currentText.subSequence(lastCursorPosition, currentText.length());
    super.replaceText(textKeptOnLeft + text.toString() + textKeptOnRight);
    
    // Set selection just after replaced word.
    setSelection(lastWordStartIndex + text.length());
  }

}
