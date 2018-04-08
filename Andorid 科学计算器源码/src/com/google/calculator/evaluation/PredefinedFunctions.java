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

package com.google.calculator.evaluation;

import com.google.calculator.tree.EvaluationContext;
import com.google.calculator.tree.Function;
import com.google.calculator.tree.Tree;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;


/**
 * Manages predefined functions, ie. the functions of the Java math library.
 * 
 * @author tulaurent@gmail.com (Laurent Tu)
 */
public class PredefinedFunctions {
  
  /**
   * Evaluate result of pre-defined function according to context.
   * 
   * @param function The function we want to evaluate.
   * @param context The context of the evaluation.
   * @return The result of the evaluation.
   * @throws NotPredefinedFunction Whenever the function is not a predefined one.
   */
  public static double evaluate(Function function, EvaluationContext context) throws NotPredefinedFunction {
    String functionName = function.functionName;
    ArrayList<Tree> arguments = function.children;
    int numArguments;
    if (arguments == null) numArguments = 0;
    else numArguments = arguments.size();
    
    Method[] methods = Math.class.getMethods();
    for (Method method : methods) {
      Class<?>[] parametersType = method.getParameterTypes();
      int numParameters;
      if (parametersType != null) {
        numParameters = parametersType.length;
      } else {
        numParameters = 0;
      }
      
      boolean parametersAreDouble = true;
      for (Class<?> paramType : parametersType) {
        if (paramType != double.class) {
          parametersAreDouble = false;
          break;
        }
      }
      if (!parametersAreDouble) continue;
      
      if (method.getName().equals(functionName) && numArguments == numParameters) {
        Object args[] = new Object[numArguments];
        for (int i = 0; i < numArguments; i++) {
          Tree tree = arguments.get(i);
          double value = tree.evaluate(context);
          args[i] = value;
        }
        Object result = null;
        try {
          result = method.invoke(null, args);
        } catch (IllegalArgumentException e) {
          return 0;
        } catch (IllegalAccessException e) {
          return 0;
        } catch (InvocationTargetException e) {
          return 0;
        }
        
        return (Double) result;
      }
    }
    throw new NotPredefinedFunction(functionName);
  }
  
  /**
   * Return a String array of all predefined functions of the Java Math library.
   * 
   * @return an array of String.
   */
  public static String[] getFunctionsName() {
    ArrayList<String> methodsName = new ArrayList<String>();
    
    Method[] methods = Math.class.getMethods();
    for (Method method : methods) {
      Class<?>[] parametersType = method.getParameterTypes();
      int numParameters = parametersType.length;
      
      boolean parametersAreDouble = true;
      for (Class<?> paramType : parametersType) {
        if (paramType != double.class) {
          parametersAreDouble = false;
          break;
        }
      }
      if (!parametersAreDouble) continue;
      
      methodsName.add(method.getName() + "(");
    }
    
    String[] result = new String[methodsName.size()];
    return methodsName.toArray(result);
  }
}
