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
 * A Point implementation supporting k dimensions.
 */
public class GenericPoint<Coord extends Comparable<? super Coord>>
  implements Point<Coord>
{
  private Comparable<? super Coord>[] __coordinates;

  /**
   * Constructs a GenericPoint with the specified dimensions.
   *
   * @param dimensions The number of dimensions in the point.  Must be
   * greater than 0.
   */
  public GenericPoint(int dimensions) {
    assert(dimensions > 0);
    __coordinates  = new Comparable[dimensions];
  }

  /**
   * Two-dimensional convenience constructor.
   *
   * @param x The coordinate value of the first dimension.
   * @param y The coordinate value of the second dimension.
   */
  public GenericPoint(Coord x, Coord y) {
    this(2);
    setCoord(0, x);
    setCoord(1, y);
  }

  /**
   * Three-dimensional convenience constructor.
   *
   * @param x The coordinate value of the first dimension.
   * @param y The coordinate value of the second dimension.
   * @param z The coordinate value of the third dimension.
   */
  public GenericPoint(Coord x, Coord y, Coord z) {
    this(3);
    setCoord(0, x);
    setCoord(1, y);
    setCoord(2, z);
  }

  /**
   * Four-dimensional convenience constructor.
   *
   * @param x The coordinate value of the first dimension.
   * @param y The coordinate value of the second dimension.
   * @param z The coordinate value of the third dimension.
   * @param w The coordinate value of the fourth dimension.
   */
  public GenericPoint(Coord x, Coord y, Coord z, Coord w) {
    this(4);
    setCoord(0, x);
    setCoord(1, y);
    setCoord(2, z);
    setCoord(3, w);
  }

  /**
   * Sets the value of the coordinate for the specified dimension.
   *
   * @param dimension The dimension (starting from 0) of the
   * coordinate value to set.
   * @param value The new value of the coordinate.
   * @exception ArrayIndexOutOfBoundsException If the dimension is
   * outside of the range [0,getDimensions()-1].
   */
  public void setCoord(int dimension, Coord value)
    throws ArrayIndexOutOfBoundsException
  {
    __coordinates[dimension] = value;
  }

  /**
   * Returns the value of the coordinate for the specified dimension.
   *
   * @param dimension The dimension (starting from 0) of the
   * coordinate value to retrieve.
   * @return The value of the coordinate for the specified dimension.
   * @exception ArrayIndexOutOfBoundsException If the dimension is
   * outside of the range [0,getDimensions()-1].
   */
  public Coord getCoord(int dimension) {
    return (Coord)__coordinates[dimension];
  }

  /**
   * Returns the number of dimensions of the point.
   *
   * @return The number of dimensions of the point.
   */
  public int getDimensions() { return __coordinates.length; }

  /**
   * Returns the hash code value for this point.
   *
   * @return The hash code value for this point.
   */
  public int hashCode() {
    int hash = 0;
    for(Comparable<? super Coord> c : __coordinates)
      hash+=c.hashCode();
    return hash;
  }

  /**
   * Returns true if the specified object is equal to the GenericPoint.
   *
   * @param obj The object to test for equality.
   * @return true if the specified object is equal to the
   * GenericPoint, false if not.
   */
  public boolean equals(Object obj) {
    if(!(obj instanceof GenericPoint)) 
      return false;

    GenericPoint point = (GenericPoint)obj;

    for(int i = 0; i < __coordinates.length; ++i)
      if(!__coordinates[i].equals(point.getCoord(i)))
        return false;

    return true;
  }

  /**
   * Returns a copy of the point.
   *
   * @return A copy of the point.
   */
  public Object clone() {
    GenericPoint<Coord> point =
      new GenericPoint<Coord>(__coordinates.length);
    for(int i = 0; i < __coordinates.length; ++i)
      point.setCoord(i, (Coord)__coordinates[i]);
    return point;
  }

  /**
   * Returns a string representation of the point, listing its
   * coordinate values in order.
   *
   * @return A string representation of the point.
   */
  public String toString() {
    StringBuffer buffer = new StringBuffer();

    buffer.append("[ ");
    buffer.append(__coordinates[0].toString());

    for(int i = 1; i < __coordinates.length; ++i) {
      buffer.append(", ");
      buffer.append(__coordinates[i].toString());
    }    

    buffer.append(" ]");

    return buffer.toString();
  }
}
