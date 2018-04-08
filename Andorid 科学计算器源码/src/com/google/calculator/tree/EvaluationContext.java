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

import java.util.HashMap;

/**
 * Evaluation context.
 * 
 * @author tulaurent@gmail.com (Laurent Tu)
 */
public class EvaluationContext {
  /**
   * The variables and their values.
   */
  public HashMap<String, Double> variables;
  
  public EvaluationContext() {
    variables = new HashMap<String, Double>();
  }

  /**
   * Returns the default evaluation context.
   * 
   * @return The default EvaluationContext object.
   */
  public static EvaluationContext getDefaultContext() {
    EvaluationContext context = new EvaluationContext();
    context.variables.put("PI", Math.PI);
    context.variables.put("E", Math.E);
    
    return context;
  }
}
