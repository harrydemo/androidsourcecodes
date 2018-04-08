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

import java.util.PriorityQueue;
import java.util.Map;
import java.util.Arrays;
import java.util.Comparator;

/**
 * NearestNeighbors implements an algorithm for finding the k-nearest
 * neighbors to a query point within the set of points contained by a
 * {@link KDTree} instance.  The algorithm can be specialized with a custom
 * distance-finding function by passing a {@link Distance} instance to its
 * constructor.
 */
public class NearestNeighbors<Coord extends Number & Comparable<? super Coord>,
                               P extends Point<Coord>, V>
{
  /**
   * The Entry interface makes accessible the results of a
   * {@link NearestNeighbors} search.  An Entry exposes both the
   * point-value mapping and its distance from the query point.
   */
  public interface Entry<Coord extends Number & Comparable<? super Coord>,
                         P extends Point<Coord>, V>
  {
    /**
     * Returns the distance from result to the query point.  This
     * will usually be implemented by dynamically taking the square root
     * of {@link #getDistance2}.  Therefore, repeated calls may be
     * expensive.
     *
     * @return The distance from result to the query point.
     */
    public double getDistance();

    /**
     * Returns the square of the distance from result to the query point.
     * This will usually be implemented as returning a cached value used
     * during the nearest neighbors search.
     *
     * @return The square of the distance from result to the query point.
     */
    public double getDistance2();

    /**
     * Returns the point-value mapping stored in this query result.
     *
     * @return The point-value mapping stored in this query result.
     */
    public Map.Entry<P,V> getNeighbor();
  }

  private final class NNEntry
    implements Entry<Coord, P, V>, Comparable<Entry<Coord, P, V>>
  {
    double _distance2;
    Map.Entry<P,V> _neighbor;

    NNEntry(double distance2, Map.Entry<P,V> neighbor) {
      _distance2 = distance2;
      _neighbor = neighbor;
    }

    public double getDistance() {
      return StrictMath.sqrt(_distance2);
    }

    public double getDistance2() {
      return _distance2;
    }

    public Map.Entry<P,V> getNeighbor() {
      return _neighbor;
    }

    public int compareTo(Entry<Coord, P, V> obj) {
      final double d = obj.getDistance2();

      if(_distance2 < d)
        return -1;
      else if(_distance2 > d)
        return 1;

      return 0;
    }
  }

  private final class EntryComparator
    implements Comparator<Entry<Coord, P, V>>
  {
    // Invert relationship so priority queue keeps highest on top.
    public int compare(Entry<Coord, P, V> n1, Entry<Coord, P, V> n2) {
      final double d1 = n1.getDistance2();
      final double d2 = n2.getDistance2();

      if(d1 < d2)
        return 1;
      else if(d1 > d2)
        return -1;

      return 0;
    }

    public boolean equals(Object obj) {
      return (obj != null && obj == this);
    }
  }

  private boolean __omitQueryPoint;
  private int __numNeighbors;
  private double __minDistance;
  private Distance<Coord, P> __distance;
  private PriorityQueue<Entry<Coord, P, V>> __pq;
  private P __query;
private KDTree<Coord, P, V> tree;

  private void find(KDTree<Coord,P,V>.KDNode node) {
    if(node == null)
      return;

    final int discriminator = node._discriminator;
    final P point = node.getKey();
    double d2 = __distance.distance2(__query, point);

    if(d2 < __minDistance && (d2 != 0.0 || !__omitQueryPoint)) {
      if(__pq.size() == __numNeighbors) {
        __pq.poll();
        __pq.add(new NNEntry(d2, node));
        __minDistance = __pq.peek().getDistance2();
      } else {
        __pq.add(new NNEntry(d2, node));
        if(__pq.size() == __numNeighbors) {
          __minDistance = __pq.peek().getDistance2();
        }
      }
    }

    double dp =
      __query.getCoord(discriminator).doubleValue() -
      point.getCoord(discriminator).doubleValue();

    d2 = dp*dp;

    if(dp < 0) {
      find(node._low);
      if(d2 < __minDistance) {
        find(node._high);
      }
    } else {
      find(node._high);
      if(d2 < __minDistance) {
        find(node._low);
      }
    }
  }

  /**
   * Constructs a new NearestNeighbors instance, using the specified
   * distance-finding functor to calculate distances during searches.
   *
   * @param distance A distance-finding functor implementing
   *                 the {@link Distance} interface.
   */
  public NearestNeighbors(Distance<Coord, P> distance) {
    __distance = distance;
  }

  /**
   * Constructs a NearestNeighbors instance using a {@link EuclideanDistance}
   * instance to calculate distances between points.
   */
  public NearestNeighbors() {
    this(new EuclideanDistance<Coord, P>());
  }
  
  public NearestNeighbors(KDTree<Coord,P,V> tree) {
	  this();
	  this.tree = tree;
  }

  /**
   * Sets the distance-finding functor used to calculate distances during
   * searches.
   *
   * @param distance The distance-finding functor to use for distance
   *                 calculations.
   */
  public void setDistance(Distance<Coord, P> distance) {
    __distance = distance;
  }

  /**
   * Finds the k-nearest neighbors to a query point withina KDTree instance.
   * The neighbors are returned as an array of {@link Entry} instances, sorted
   * from nearest to farthest.
   *
   * @param tree The KDTree to search.
   * @param queryPoint The query point.
   * @param numNeighbors The number of nearest neighbors to find.  This should
   *        be a positive value.  Non-positive values result in no neighbors
   *        being found.
   * @param omitQueryPoint If true, point-value mappings at a distance of
   *        zero are omitted from the result.  If false, mappings at a
   *        distance of zero are included.
   * @return An array containing the nearest neighbors and their distances
   *         sorted by least distance to greatest distance.  If no neighbors
   *         are found, the array will have a length of zero.
   */
  public Entry<Coord,P,V>[] get(
                                P queryPoint,
                                int numNeighbors,
                                boolean omitQueryPoint)
  {
    __omitQueryPoint = omitQueryPoint;
    __numNeighbors = numNeighbors;
    __query = queryPoint;
    __minDistance = Double.POSITIVE_INFINITY;

    __pq = new PriorityQueue<Entry<Coord, P, V>>(tree.size(),
                                                 new EntryComparator());

    if(numNeighbors > 0) {
      find(tree._root);
    }

    Entry<Coord,P,V>[] neighbors = new Entry[__pq.size()];

    __pq.toArray(neighbors);
    Arrays.sort(neighbors);

    __pq = null;
    __query = null;

    return neighbors;
  }

  /**
   * Same as {@link #get get(tree, queryPoint, numNeighbors, true)}.
   */
  public Entry<Coord,P,V>[]
    get(KDTree<Coord,P,V> tree, P queryPoint, int numNeighbors)
  {
    return get(queryPoint, numNeighbors, true);
  }
}
