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

import java.util.*;

/**
 * A RangeSearchTree is a spatial data structure that supports the
 * retrieval of data associated with point keys as well as the
 * searching of data that occurs within a specified range of points.
 * 
 * <p>Note: RangeSearchTree does not implement SortedMap for range
 * searching bcause the SortedMap interface is not well-suited to
 * multi-dimensional range search trees.</p>
 */
public interface RangeSearchTree<Coord extends Comparable<? super Coord>,
                                     P extends Point<Coord>, V>
  extends Map<P, V>
{
  /**
   * Returns an iterator for mappings that are contained in the
   * rectangle defined by the given lower left-hand and upper
   * right-hand corners.  The mappings returned include those occuring
   * at points on the bounding rectangle, not just those inside.
   *
   * @param lower The lower left-hand corner of the bounding
   * rectangle.  A null value can be used to specify the region is
   * unbounded in that direction.
   * @param upper The upper right-hand corner of the bounding
   * rectangle.  A null value can be used to specify the region is
   * unbounded in that direction.
   * @return An iterator for mappings that are contained in the
   * specified rectangle.
   */
  public Iterator<Map.Entry<P,V>> iterator(P lower, P upper);

}
