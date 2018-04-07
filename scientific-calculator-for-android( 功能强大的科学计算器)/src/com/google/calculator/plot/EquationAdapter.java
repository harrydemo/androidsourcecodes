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

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Adapter for EquationView.
 * 
 * @author tulaurent@gmail.com (Laurent Tu)
 */
public class EquationAdapter extends BaseAdapter {
  
  private ArrayList<Equation> equationsList;

  public EquationAdapter(ArrayList<Equation> listEquations) {
    this.equationsList = listEquations;
  }

  public int getCount() {
    return equationsList.size();
  }

  public Object getItem(int position) {
    return position;
  }

  public long getItemId(int position) {
    return position;
  }

  public View getView(int position, View convertView, ViewGroup parent) {
    EquationView equationView;
    if (convertView == null) {
      equationView = new EquationView(parent.getContext());
      equationView.setEquation(equationsList.get(position));
      equationView.setIndexInEquationEditor(position);
      //equationView.setEquationText(equationsList.get(position).getEquationText());
    } else {
      equationView = (EquationView) convertView;
    }
    
    return equationView;
  }
  
}
