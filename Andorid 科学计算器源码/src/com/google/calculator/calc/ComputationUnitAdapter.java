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

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Adapter for ComputationUnit.
 * 
 * @author tulaurent@gmail.com (Laurent Tu)
 *
 */
public class ComputationUnitAdapter extends BaseAdapter {

  private ArrayList<ComputationUnit> listComputationUnit;

  public ComputationUnitAdapter(ArrayList<ComputationUnit> listComputationUnit) {
    this.listComputationUnit = listComputationUnit;
  }

  public int getCount() {
    return listComputationUnit.size();
  }

  public Object getItem(int position) {
    return position;
  }

  public long getItemId(int position) {
    return position;
  }

  public View getView(int position, View convertView, ViewGroup parent) {
    TextView textView;
    if (convertView == null) {
      textView = new TextView(parent.getContext());
      textView.setLines(2);
    } else {
      textView = (TextView) convertView;
    }
    
    if (position % 2 == 1) {
      // TODO(laurentt): this does not work anymore.
      textView.setBackgroundColor(android.R.color.background_light);
    }
    textView.setText(listComputationUnit.get(position).toString());
    return textView;
  }

}
