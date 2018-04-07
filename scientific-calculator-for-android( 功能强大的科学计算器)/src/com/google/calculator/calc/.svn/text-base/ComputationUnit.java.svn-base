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

/**
 * Unit containing computation information:
 *  - The query.
 *  - The result.
 *  
 * @author tulaurent@gmail.com (Laurent Tu)
 *
 */
public class ComputationUnit {
  /**
   * Query.
   * eg. (1 + sqrt(5)) / 2
   */
  CharSequence query;
  /**
   * Result.
   * eg. 1.618
   */
  ComputationResult result;
  /**
   * Index of this computation unit in the result display.
   */
  int index;
  
  /**
   * Constructor.
   * 
   * @param index
   */
  ComputationUnit(int index) {
    this.index = index;
  }
  
  /**
   * Constructor.
   * 
   * @param index
   * @param query
   */
  ComputationUnit(int index, CharSequence query) {
    this.index = index;
    this.query = query;
  }
  
  /**
   * Setter for query.
   * @param query
   */
  public void setQuery(CharSequence query) {
    this.query = query;
  }
  
  /**
   * Setter for result.
   * 
   * @param result
   */
  public void setComputationResult(ComputationResult result) {
    this.result = result;
  }
  
  @Override
  public String toString() {
    String displayString = String.format("%d > %s", index, query);
    if (result != null) {
      for (CharSequence line : result.lines) {
        displayString += String.format("%n%s = %s", getWhiteSpaces(index), line);
      }
    }
    return displayString;
  }
  
  /**
   * Gets white spaces to align the display of the query and the display of the result. 
   * 
   * @param number The index of the query.
   * @return A string of white spaces.
   */
  private static CharSequence getWhiteSpaces(double number) {
    String result = "";
    do {
      result += "  ";
      number /= 10;
    } while (number >= 1);
    
    return result;
  }
}
