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

package com.google.calculator.tree;

import com.google.calculator.evaluation.NotPredefinedFunction;
import com.google.calculator.evaluation.PredefinedFunctions;

import java.util.ArrayList;

/**
 * 
 * @author tulaurent@gmail.com (Laurent Tu)
 *
 */
public class Function extends InternalNode {

  public String functionName;
  
  public Function(String name, ArrayList<Tree> arguments) {
    super(arguments);
    functionName = name;
  }

  public Function(String name) {
    super();
    functionName = name;
  }

  @Override
  public double evaluate(EvaluationContext context) {
    try {
      return PredefinedFunctions.evaluate(this, context);
    } catch (NotPredefinedFunction e) {
      if (functionName.equals("history")) {
        // TODO(Laurent): return ResultDisplayView.historyResult.get(arguments.get(0));
      }
      // TODO(Laurent): exception.
      return 0;
    }
  }
  
  @Override
  public String toString() {
    if (children != null) {
      return functionName + "("+children.toString()+")";
    } else {
      return functionName + "()";
    }
  }
}
