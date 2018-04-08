/*
 * Copyright 2001-2005 Daniel F. Savarese
 * Copyright 2006-2009 Savarese Software Research Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.savarese.com/software/ApacheLicense-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nanosheep.bikeroute.utility.route.kd;

/**
 * The Point interface represents a point in a k-dimensional space.
 * It is used to specify point keys that index into spatial data
 * structures.
 */
public interface Point<Coord extends Comparable<? super Coord>> {
  /**
   * Returns the value of the coordinate of the given dimension.
   *
   * @return The value of the coordinate of the given dimension.
   * @exception IllegalArgumentException if the Point does not
   *            support the dimension.
   */
  public Coord getCoord(int dimension);

  /**
   * Returns the number of dimensions in the point.
   *
   * @return The number of dimensions in the point.
   */
  public int getDimensions();
}
