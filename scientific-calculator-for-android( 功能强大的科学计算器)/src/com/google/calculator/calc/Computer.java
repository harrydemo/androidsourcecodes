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

import com.google.calculator.parser.parser;
import com.google.calculator.plot.PlotData;
import com.google.calculator.tree.EvaluationContext;
import com.google.calculator.tree.Tree;

import java.io.StringBufferInputStream;

/**
 * Place holder for computation performing methods.
 * 
 * @author tulaurent@gmail.com (Laurent Tu)
 *
 */
public class Computer {

  /**
   * Returns a ComputationResult containing the result of computation of lineToCompute.
   * 
   * @param lineToCompute The query, eg. "3+5".
   * @return A ComputationResult object.
   */
  public static ComputationResult compute(CharSequence lineToCompute) {
    // Parsing
    Tree tree;
    try {
      tree = parseLine(lineToCompute);
    } catch (Exception e) {
      return new ComputationResult("Expression not correct", e.toString());
    }
    
    // Evaluation
    EvaluationContext context = EvaluationContext.getDefaultContext();
    if (tree != null) {
      return new ComputationResult(tree.evaluate(context));
    } else {
      return new ComputationResult("Cannot evaluate tree", "Tree is null");
    }
  }
  
  /**
   * Computes the plotting data for an equation right hand side.
   * For example, it computes the plotting data for y = sin(x), where sin(x) is the RHS.
   * 
   * @param equationRhs Contains the equation right hand side to be plotted, eg. sin(x).
   * @param plotData Contains the plotting information.
   * @return True if computation of plotting data succeeded.
   */
  public static boolean computePlot(CharSequence equationRhs, PlotData plotData) {
    // Parsing
    Tree tree;
    try {
      tree = parseLine(equationRhs);
    } catch (Exception e) {
      return false;
    }
    
    // Evaluation
    if (tree == null) {
      return false;
    }
    
    // TODO(Laurent): Not too bad from a memory point of view, it might be tremendously
    // faster to pass an array of [xmin:xstep:xmax].
    EvaluationContext context = EvaluationContext.getDefaultContext();
    context.variables.put("x", new Double(0));
    int i = 0;
    for (double x = plotData.minX; x < plotData.maxX; x += plotData.step) {
      context.variables.put("x", x);
      plotData.xValues[i] = x;
      plotData.yValues[i] = tree.evaluate(context);
      i++;
    }
    
    return true;    
  }

  private static Tree parseLine(CharSequence lineToCompute) throws Exception {
    StringBufferInputStream stream = new StringBufferInputStream(lineToCompute.toString());
    Tree tree = null;
    tree = parser.getTree(stream);
    return tree;
  }

}
