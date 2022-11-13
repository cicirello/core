/*
 * Module org.cicirello.core
 * Copyright 2019-2022 Vincent A. Cicirello, <https://www.cicirello.org/>.
 *
 * This file is part of module org.cicirello.core.
 *
 * Module org.cicirello.core is free software: you can
 * redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Module org.cicirello.core is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License
 * along with module org.cicirello.core.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.cicirello.ds;

import java.lang.reflect.Array;
import java.util.Iterator;

/**
 * Package access abstract base class for Fibonacci heaps with double-valued priorities.
 *
 * @param <E> The type of object contained within.
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, <a
 *     href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
abstract class AbstractFibonacciHeapDouble<E>
    implements MergeablePriorityQueueDouble<E, SimpleFibonacciHeapDouble<E>> {

  private final Prioritizer compare;
  private final double extreme;

  private int size;
  private FibonacciHeapDoubleNode<E> min;

  private final FibonacciHeapDoubleNode.Consolidator<E> consolidator;

  /*
   * package private for use by subclass: Use factory methods for creation otherwise.
   *
   * Initializes an empty AbstractFibonacciHeapDouble.
   */
  AbstractFibonacciHeapDouble(Prioritizer compare) {
    this.compare = compare;
    extreme =
        compare.comesBefore(0, 1)
            ? java.lang.Double.POSITIVE_INFINITY
            : java.lang.Double.NEGATIVE_INFINITY;

    consolidator = new FibonacciHeapDoubleNode.Consolidator<E>(compare);
  }

  /*
   * package private copy constructor to support the copy() method, including in subclass.
   */
  AbstractFibonacciHeapDouble(AbstractFibonacciHeapDouble<E> other) {
    this(other.compare);
    size = other.size;
    min = other.min != null ? other.min.copy() : null;
  }

  @Override
  public void clear() {
    size = 0;
    // set min to null which should cause garbage collection
    // of entire fibonacci heap (impossible to have references to Nodes
    // external from this class.
    min = null;
  }

  @Override
  public final boolean demote(E element, double priority) {
    FibonacciHeapDoubleNode<E> node = find(element);
    if (node != null && compare.comesBefore(node.e.value, priority)) {
      internalDemote(node, priority);
      return true;
    }
    return false;
  }

  /**
   * Checks if this heap is the same as another, including the same class, and that they contain the
   * same (element, priority) pairs as another, including the specific structure the heap, as well
   * as that the priority order is the same.
   *
   * @param other The other heap.
   * @return true if and only if this and other contain the same (element, priority) pairs, with the
   *     same priority order.
   */
  @Override
  public boolean equals(Object other) {
    if (other instanceof AbstractFibonacciHeapDouble) {
      @SuppressWarnings("unchecked")
      AbstractFibonacciHeapDouble<E> casted = (AbstractFibonacciHeapDouble<E>) other;
      if (size != casted.size) return false;
      if (compare.comesBefore(0, 1) != casted.compare.comesBefore(0, 1)) return false;
      Iterator<PriorityQueueNode.Double<E>> iter = iterator();
      Iterator<PriorityQueueNode.Double<E>> otherIter = casted.iterator();
      while (iter.hasNext()) {
        if (!iter.next().equals(otherIter.next())) {
          return false;
        }
      }
      return getClass() == other.getClass();
    } else {
      return false;
    }
  }

  /**
   * Computes a hashCode.
   *
   * @return a hashCode
   */
  @Override
  public int hashCode() {
    int h = 0;
    for (PriorityQueueNode.Double<E> e : this) {
      h = 31 * h + java.lang.Double.hashCode(e.value);
      h = 31 * h + e.element.hashCode();
    }
    return h;
  }

  @Override
  public final boolean isEmpty() {
    return size == 0;
  }

  @Override
  public final Iterator<PriorityQueueNode.Double<E>> iterator() {
    return new FibonacciHeapDoubleNode.FibonacciHeapDoubleIterator<E>(min);
  }

  @Override
  public final E peekElement() {
    return min != null ? min.e.element : null;
  }

  @Override
  public final PriorityQueueNode.Double<E> peek() {
    return min != null ? min.e : null;
  }

  @Override
  public final double peekPriority() {
    return min != null ? min.e.value : extreme;
  }

  @Override
  public final double peekPriority(E element) {
    FibonacciHeapDoubleNode<E> node = find(element);
    return node != null ? node.e.value : extreme;
  }

  @Override
  public final E pollElement() {
    PriorityQueueNode.Double<E> min = poll();
    return min != null ? min.element : null;
  }

  @Override
  public PriorityQueueNode.Double<E> poll() {
    if (size == 1) {
      PriorityQueueNode.Double<E> pair = min.e;
      min = null;
      size = 0;
      return pair;
    } else if (size > 1) {
      FibonacciHeapDoubleNode<E> z = min;
      min = min.removeSelf();
      min = consolidator.consolidate(min, size);
      size--;
      return z.e;
    }
    return null;
  }

  @Override
  public final boolean promote(E element, double priority) {
    FibonacciHeapDoubleNode<E> node = find(element);
    if (node != null && compare.comesBefore(priority, node.e.value)) {
      internalPromote(node, priority);
      return true;
    }
    return false;
  }

  @Override
  public final boolean remove(Object o) {
    FibonacciHeapDoubleNode<E> node = null;
    if (o instanceof PriorityQueueNode.Double) {
      PriorityQueueNode.Double pair = (PriorityQueueNode.Double) o;
      node = find(pair.element);
    } else {
      node = find(o);
    }
    if (node == null) {
      return false;
    }
    internalPromote(
        node,
        compare.comesBefore(min.e.value - 1, min.e.value) ? min.e.value - 1 : min.e.value + 1);
    poll();
    return true;
  }

  @Override
  public final int size() {
    return size;
  }

  @Override
  public final Object[] toArray() {
    Object[] array = new Object[size];
    int i = 0;
    for (PriorityQueueNode.Double<E> e : this) {
      array[i] = e;
      i++;
    }
    return array;
  }

  /**
   * {@inheritDoc}
   *
   * @throws ArrayStoreException if the runtime component type of array is not compatible with the
   *     type of the (element, priority) pairs.
   * @throws NullPointerException if array is null
   */
  @Override
  public final <T> T[] toArray(T[] array) {
    @SuppressWarnings("unchecked")
    T[] result =
        array.length >= size
            ? array
            : (T[]) Array.newInstance(array.getClass().getComponentType(), size);
    int i = 0;
    for (PriorityQueueNode.Double<E> e : this) {
      @SuppressWarnings("unchecked")
      T nextElement = (T) e;
      result[i] = nextElement;
      i++;
    }
    if (result.length > size) {
      result[size] = null;
    }
    return result;
  }

  /*
   * package access to enable sublcass overriding with simple index check/
   */
  FibonacciHeapDoubleNode<E> find(Object element) {
    return min == null ? null : min.find(element);
  }

  /*
   * used internally: doesn't check if already contains element.
   * package access to enable subclass overriding.
   */
  FibonacciHeapDoubleNode<E> internalOffer(PriorityQueueNode.Double<E> pair) {
    if (min == null) {
      min = new FibonacciHeapDoubleNode<E>(pair);
      size = 1;
      return min;
    } else {
      FibonacciHeapDoubleNode<E> added = new FibonacciHeapDoubleNode<E>(pair, min);
      if (compare.comesBefore(pair.value, min.e.value)) {
        min = added;
      }
      size++;
      return added;
    }
  }

  final void internalPromote(FibonacciHeapDoubleNode<E> x, double priority) {
    // only called if priority decreased for a minheap (increased for a maxheap)
    // so no checks needed here.
    x.e.value = priority;
    FibonacciHeapDoubleNode<E> y = x.parent();
    if (y != null && compare.comesBefore(priority, y.e.value)) {
      x.cut(y, min);
      y.cascadingCut(min);
    }
    if (compare.comesBefore(priority, min.e.value)) {
      min = x;
    }
  }

  final boolean internalMerge(AbstractFibonacciHeapDouble<E> other) {
    if (compare.comesBefore(0, 1) != other.compare.comesBefore(0, 1)) {
      throw new IllegalArgumentException("this and other follow different priority-order");
    }
    if (other.size > 0) {
      other.min.insertListInto(min);
      if (compare.comesBefore(other.min.e.value, min.e.value)) {
        min = other.min;
      }
      size += other.size;
      other.clear();
      return true;
    }
    return false;
  }

  final void internalDemote(FibonacciHeapDoubleNode<E> x, double priority) {
    // only called if priority increased for a minheap (decreased for a maxheap)
    // so no checks needed here.

    // 1. promote (opposite) to front
    internalPromote(
        x, compare.comesBefore(min.e.value - 1, min.e.value) ? min.e.value - 1 : min.e.value + 1);
    // 2. poll() to remove
    poll();
    // 3. reinsert with new priority
    x.e.value = priority;
    internalOffer(x.e);
  }

  final boolean internalChange(FibonacciHeapDoubleNode<E> node, double priority) {
    if (compare.comesBefore(priority, node.e.value)) {
      internalPromote(node, priority);
      return true;
    } else if (compare.comesBefore(node.e.value, priority)) {
      internalDemote(node, priority);
      return true;
    }
    return false;
  }

  final FibonacciHeapDoubleNode.NodeIterator<E> nodeIterator() {
    return new FibonacciHeapDoubleNode.NodeIterator<E>(min);
  }
}
