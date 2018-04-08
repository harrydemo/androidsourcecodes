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

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Displays the list of ComputationUnit objects representing computation query and results
 * in a ListView.
 * It also manages the history.
 * 
 * @author tulaurent@gmail.com (Laurent Tu)
 */
public class CalculatorResultListView extends ListView {

  public CalculatorResultListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	    initializeResultListView(context);
	}

	public CalculatorResultListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	    initializeResultListView(context);
	}

	public CalculatorResultListView(Context context) {
		super(context);
	    initializeResultListView(context);
	}

/**
   * Represents the computations.
   */
  private ArrayList<ComputationUnit> history = null;
  
  /**
   * Number of history entries.
   */
  private int numEntriesInHistory = 0;
  /**
   * Index of history entry currently displayed.
   */
  private int currentHistoryEntryIndex = 0;
  /**
   * Adapter for displaying ComputationUnit objects in the ListView.
   */ 
  private ComputationUnitAdapter adapter;
  
  /**
   * Reference on CalculatorInputTextView when using previous entries. 
   */
  private InputTextView calculatorInputTextView;

  /**
   * Initializes the view. Constructor helper.
   * 
   * @param context
   */
  private void initializeResultListView(Context context) {
    history = new ArrayList<ComputationUnit>();
    adapter = new ComputationUnitAdapter(history);
    this.setAdapter(adapter);
    // Does it work?
    // mLayoutMode = ListView.LAYOUT_FORCE_BOTTOM;
    
    // When an item is clicked, fill the input calculatorTextView with this item.
    this.setOnItemClickListener(new OnItemClickListener() {
      public void onItemClick(AdapterView arg0, View arg1, int position, long arg3) {
        calculatorInputTextView.append(history.get(position).query);
      }
    });
  }
  
  /**
   * Adds computation unit to the view with the given query.
   * 
   * @param query The computation query.
   * @return The index of the new ComputationUnit.
   */
  public int addComputationUnit(CharSequence query) {
    ComputationUnit unit = new ComputationUnit(numEntriesInHistory, query);
    history.add(unit); 
    ajustDisplay();
    
    numEntriesInHistory++;
    currentHistoryEntryIndex = numEntriesInHistory;
    
    return unit.index;
  }

  /**
   * Adjusts the display, tries to do autoscrolling.
   */
  private void ajustDisplay() {
    adapter.notifyDataSetChanged();
    requestLayout();
    
    // TODO(Laurent): autoscrolling here.
  }
  
  /**
   * Updates the ComputationUnit result indexed by given index.
   * 
   * @param computationUnitIndex The index of the ComputationUnit.
   * @param result The result.
   */
  public void updateComputationUnitResult(int computationUnitIndex, ComputationResult result) {
    history.get(computationUnitIndex).setComputationResult(result);
    ajustDisplay();
  }
  
  /**
   * Sets the current history element to be displayed to the previous one, and return the value of the entry.
   * 
   * @return The history element to be displayed.
   */
  public CharSequence goPreviousEntry() {
    if (currentHistoryEntryIndex > 0) {
      currentHistoryEntryIndex --;
    }
    return history.get(currentHistoryEntryIndex).query;
  }

  /**
   * Sets the current history element to be displayed to the next one, and return the value of the entry.
   * 
   * @return The history element to be displayed.
   */
  public CharSequence goNextEntry() {
    if (currentHistoryEntryIndex < numEntriesInHistory -1) {
      currentHistoryEntryIndex ++;
    } else {
      currentHistoryEntryIndex = numEntriesInHistory;
      return "";
    }
    return history.get(currentHistoryEntryIndex).query;
  }

  /**
   * Injects the calculator text view for the input.
   * 
   * @param calculatorInputTextView
   */
  public void setCalculatorTextView(InputTextView calculatorInputTextView) {
    this.calculatorInputTextView = calculatorInputTextView;
  }

}
