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

import com.google.calculator.calc.Computer;

/**
 * Equation to be plotted.
 * The equation word here is misused and denotes the right hand side (RHS) part of
 * an equation of the type y(x) = rhs(x).
 * 
 * @author tulaurent@gmail.com (Laurent Tu)
 */
public class Equation {
  /**
   * The RHS of the equation, eg. "sin(x)".
   */
  private String equation = null;
  /**
   * Is the equation enabled?
   */
  private boolean enabled = true;
  /**
   * Has the equation been changed since last computation of the plot data?
   */
  private boolean hasBeenChanged = true;
  /**
   * The PlotData of the equation.
   */
  private PlotData plotData = new PlotData();
  
  /**
   * Constructor.
   * 
   * @param equation The equation.
   */
  public Equation(String equation) {
    this.equation = equation;
  }
  
  /**
   * Constructor.
   */
  public Equation() {
    equation = "";
  }

  /**
   * Gets the Plot Data.
   * @return The PlotData.
   */
  public PlotData getPlotData() {
    if (!enabled) return null;
    
    if (hasBeenChanged) {
      Computer.computePlot(equation, plotData);
      hasBeenChanged = false;
    }

    return plotData;
  }

  /**
   * Set the equation.
   * @param equationText
   */
  public void setEquation(String equationText) {
    hasBeenChanged = true;
    equation = equationText;
  }

  /**
   * Get equation.
   * @return A CharSequence of the equation.
   */
  public CharSequence getEquationText() {
    return equation;
  }

  /**
   * 
   * @param isEnabled
   */
  public void setEquationEnabled(boolean isEnabled) {
    enabled = isEnabled;
  }
}
