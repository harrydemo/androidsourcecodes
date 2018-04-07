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

/**
 * Represents plotting data.
 * 
 * @author tulaurent@gmail.com (Laurent Tu)
 *
 */
public class PlotData {
  public double minX = -5;
  public double maxX = 5;
  
  public double minY = -5;
  public double maxY = 5;
  
  public double step = 0.1;
  
  public double xValues[];
  public double yValues[];
  
  PlotData() {
    createArrays();
  }
  
  public void createArrays() {
    int numElements = (int) ((maxX - minX)/step) + 1;
    xValues = new double[numElements];
    yValues = new double[numElements];
  }
}
