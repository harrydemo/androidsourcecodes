/*
 * Copyright 2010 Savarese Software Research Corporation
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
 * The Distance interface encapsulates an algorithm for determining
 * the distance between two points.
 */
public interface Distance<Coord extends Number & Comparable<? super Coord>,
                              P extends Point<Coord>>
{
  /**
   * Returns the distance between two points.
   *
   * @param from The first end point.
   * @param to The second end point.
   * @return The distance between from and to.
   */
  public double distance(P from, P to);

  /**
   * Returns the square of the distance between two points.
   *
   * @param from The first end point.
   * @param to The second end point.
   * @return The square of the distance between from and to.
   */
  public double distance2(P from, P to);
}
