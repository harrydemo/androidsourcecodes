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

import java.util.ArrayList;

/**
 * Internal node of the tree.
 * 
 * @author tulaurent@gmail.com (Laurent Tu)
 */
public abstract class InternalNode implements Tree {
  /**
   * Children.
   */
  public ArrayList<Tree> children = null;
  
  /**
   * Constructor for two children node.
   * 
   * @param left
   * @param right
   */
  public InternalNode(Tree left, Tree right) {
    if (children == null) {
      children = new ArrayList<Tree>();
    }
    children.add(left);
    children.add(right);
  }
  
  /**
   * Constructor.
   * 
   * @param tree
   */
  public InternalNode(Tree tree) {
    if (children == null) {
      children = new ArrayList<Tree>();
    }
    children.add(tree);
  }

  /**
   * Constructor.
   * 
   * @param children
   */
  public InternalNode(ArrayList<Tree> children) {
    this.children = children;
  }

  /**
   * Default constructor.
   */
  public InternalNode() {
  }

  public abstract double evaluate(EvaluationContext context);
}
