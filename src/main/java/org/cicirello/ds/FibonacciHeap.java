/*
 * Module org.cicirello.core
 * Copyright 2019-2025 Vincent A. Cicirello, <https://www.cicirello.org/>.
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import org.cicirello.util.Copyable;

/**
 * An implementation of a Fibonacci Heap. An instance of a FibonacciHeap contains (element,
 * priority) pairs, such that the elements are distinct. The priority values are of type int.
 *
 * <p><b>Origin:</b> Fibonacci heaps were first introduced in the following article: M. L. Fredman
 * and R. E. Tarjan (1987). Fibonacci Heaps and Their Uses in Improved Network Optimization
 * Algorithms. <i>Journal of the ACM</i>, 34(3): 596-615, July 1987.
 *
 * <p><b>Priority order:</b> FibonacciHeap instances are created via factory methods with names
 * beginning with <code>create</code>. The priority order depends upon the factory method used to
 * create the FibonacciHeap. Methods named <code>createMinHeap</code> produce a min heap with
 * priority order minimum-priority-first-out. Methods named <code>createMaxHeap</code> produce a max
 * heap with priority order maximum-priority-first-out.
 *
 * <p><b>Distinctness:</b> The {@link Object#hashCode} and {@link Object#equals} methods are used to
 * enforce distinctness, so be sure that the class of the elements properly implements these
 * methods, or else behavior is not guaranteed.
 *
 * <p><b>Creating instances:</b> To create an instance, use one of the factory methods, such as
 * with:
 *
 * <pre><code>
 * FibonacciHeap&lt;String&gt; pq = FibonacciHeap.createMinHeap();
 * </code></pre>
 *
 * <p><b>Method runtimes:</b> The asymptotic runtime of the methods of this class are as follows
 * (where n is the current size of the heap and m is the size of a Collection parameter where
 * relevant). Note that in many cases in this list, the runtimes are amortized time and not actual
 * time (see a reference on Fibonacci heaps for details).
 *
 * <ul>
 *   <li><b>O(1):</b> {@link #add(Object, int)}, {@link #add(PriorityQueueNode.Integer)}, {@link
 *       #contains}, {@link #createMaxHeap()}, {@link #createMinHeap()}, {@link #element}, {@link
 *       #isEmpty}, {@link #iterator}, {@link #merge}, {@link #offer(E, int)}, {@link
 *       #offer(PriorityQueueNode.Integer)}, {@link #peek}, {@link #peekElement}, {@link
 *       #peekPriority()}, {@link #peekPriority(Object)}, {@link #promote}, {@link #size()}
 *   <li><b>O(lg n):</b> {@link #demote}, {@link #poll}, {@link #pollElement}, {@link
 *       #pollThenAdd(Object, int)}, {@link #pollThenAdd(PriorityQueueNode.Integer)}, {@link
 *       #remove()}, {@link #remove(Object)}, {@link #removeElement()}
 *   <li><b>O(m):</b> {@link #addAll(Collection)}, {@link #containsAll(Collection)}, {@link
 *       #createMaxHeap(Collection)}, {@link #createMinHeap(Collection)}
 *   <li><b>O(n):</b> {@link #clear}, {@link #copy()}, {@link #equals}, {@link #hashCode}, {@link
 *       #toArray()}, {@link #toArray(Object[])}
 *   <li><b>O(n + m):</b> {@link #removeAll(Collection)}, {@link #retainAll(Collection)}
 * </ul>
 *
 * <p>The amortized runtime of {@link #change} depends on the direction of change. If the priority
 * is decreased for a min-heap or increased for a max-heap, the amortized runtime of {@link #change}
 * is O(1); but if the priority is increased for a min-heap or decreased for a max-heap, then the
 * amortized time of {@link #change} is O(lg n).
 *
 * @param <E> The type of object contained in the FibonacciHeap.
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, <a
 *     href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public final class FibonacciHeap<E>
    implements MergeablePriorityQueue<E, FibonacciHeap<E>>, Copyable<FibonacciHeap<E>> {

  private final IntegerPrioritizer compare;
  private final int extreme;

  private int size;
  private FibonacciHeapNode<E> min;

  private final FibonacciHeapNode.Consolidator<E> consolidator;

  private HashMap<E, FibonacciHeapNode<E>> index;

  /*
   * PRIVATE: Use factory methods for creation.
   *
   * Initializes an empty FibonacciHeap.
   */
  private FibonacciHeap(IntegerPrioritizer compare) {
    this.compare = compare;
    extreme = compare.comesBefore(0, 1) ? Integer.MAX_VALUE : Integer.MIN_VALUE;

    consolidator = new FibonacciHeapNode.Consolidator<E>(compare);
    index = new HashMap<E, FibonacciHeapNode<E>>();
  }

  /*
   * PRIVATE: Use factory methods for creation.
   * Initializes a FibonacciHeap from a collection of (element, priority) pairs.
   *
   * @param initialElements The initial collection of (element, priority) pairs.
   *
   * @throws IllegalArgumentException if more than one pair in initialElements contains the same element.
   */
  private FibonacciHeap(
      Collection<PriorityQueueNode.Integer<E>> initialElements, IntegerPrioritizer compare) {
    this(compare);
    for (PriorityQueueNode.Integer<E> element : initialElements) {
      if (!offer(element)) {
        throw new IllegalArgumentException("initialElements contains duplicates");
      }
    }
  }

  /*
   * private copy constructor to support the copy() method.
   */
  private FibonacciHeap(FibonacciHeap<E> other) {
    this(other.compare);
    size = other.size;
    min = other.min != null ? other.min.copy() : null;
    index = new HashMap<E, FibonacciHeapNode<E>>();
    FibonacciHeapNode.NodeIterator<E> iter = new FibonacciHeapNode.NodeIterator<E>(min);
    while (iter.hasNext()) {
      FibonacciHeapNode<E> node = iter.next();
      index.put(node.e.element, node);
    }
  }

  @Override
  public FibonacciHeap<E> copy() {
    return new FibonacciHeap<E>(this);
  }

  /**
   * Creates an empty FibonacciHeap with minimum-priority-first-out priority order.
   *
   * @param <E> The type of elements contained in the FibonacciHeap.
   * @return an empty FibonacciHeap with a minimum-priority-first-out priority order
   */
  public static <E> FibonacciHeap<E> createMinHeap() {
    return new FibonacciHeap<E>(new IntegerMinOrder());
  }

  /**
   * Creates a FibonacciHeap from a collection of (element, priority) pairs, with a
   * minimum-priority-first-out priority order.
   *
   * @param initialElements The initial collection of (element, priority) pairs.
   * @param <E> The type of elements contained in the FibonacciHeap.
   * @return a FibonacciHeap with a minimum-priority-first-out priority order
   * @throws IllegalArgumentException if more than one pair in initialElements contains the same
   *     element.
   */
  public static <E> FibonacciHeap<E> createMinHeap(
      Collection<PriorityQueueNode.Integer<E>> initialElements) {
    return new FibonacciHeap<E>(initialElements, new IntegerMinOrder());
  }

  /**
   * Creates an empty FibonacciHeap with maximum-priority-first-out priority order.
   *
   * @param <E> The type of elements contained in the FibonacciHeap.
   * @return an empty FibonacciHeap with a maximum-priority-first-out priority order
   */
  public static <E> FibonacciHeap<E> createMaxHeap() {
    return new FibonacciHeap<E>(new IntegerMaxOrder());
  }

  /**
   * Creates a FibonacciHeap from a collection of (element, priority) pairs, with a
   * maximum-priority-first-out priority order.
   *
   * @param initialElements The initial collection of (element, priority) pairs.
   * @param <E> The type of elements contained in the FibonacciHeap.
   * @return a FibonacciHeap with a maximum-priority-first-out priority order
   * @throws IllegalArgumentException if more than one pair in initialElements contains the same
   *     element.
   */
  public static <E> FibonacciHeap<E> createMaxHeap(
      Collection<PriorityQueueNode.Integer<E>> initialElements) {
    return new FibonacciHeap<E>(initialElements, new IntegerMaxOrder());
  }

  @Override
  public boolean change(E element, int priority) {
    FibonacciHeapNode<E> node = index.get(element);
    if (node != null) {
      if (compare.comesBefore(priority, node.e.value)) {
        internalPromote(node, priority);
        return true;
      } else if (compare.comesBefore(node.e.value, priority)) {
        internalDemote(node, priority);
        return true;
      }
      return false;
    }
    return offer(element, priority);
  }

  @Override
  public void clear() {
    size = 0;
    // set min to null which should cause garbage collection
    // of entire fibonacci heap (impossible to have references to Nodes
    // external from this class.
    min = null;
    // clear the index... old way: index.clear();
    // instead let garbage collector take care of it, just reinitialize:
    index = new HashMap<E, FibonacciHeapNode<E>>();
  }

  @Override
  public boolean contains(Object o) {
    if (o instanceof PriorityQueueNode.Integer) {
      PriorityQueueNode.Integer pair = (PriorityQueueNode.Integer) o;
      return index.containsKey(pair.element);
    }
    return index.containsKey(o);
  }

  /**
   * Checks if this PriorityQueue contains all elements or (element, priority) pairs from a given
   * Collection.
   *
   * @param c A Collection of elements or (element, priority) pairs to check for containment.
   * @return true if and only if this PriorityQueue contains all of the elements or (element,
   *     priority) pairs in c.
   */
  @Override
  public boolean containsAll(Collection<?> c) {
    for (Object o : c) {
      if (o instanceof PriorityQueueNode.Integer) {
        PriorityQueueNode.Integer pair = (PriorityQueueNode.Integer) o;
        if (!index.containsKey(pair.element)) {
          return false;
        }
      } else if (!index.containsKey(o)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean demote(E element, int priority) {
    FibonacciHeapNode<E> node = index.get(element);
    if (node != null && compare.comesBefore(node.e.value, priority)) {
      internalDemote(node, priority);
      return true;
    }
    return false;
  }

  /**
   * Checks if this heap is the same as another, including that they contain the same (element,
   * priority) pairs as another, including the specific structure the heap, as well as that the
   * priority order is the same.
   *
   * @param other The other heap.
   * @return true if and only if this and other contain the same (element, priority) pairs, with the
   *     same priority order.
   */
  @Override
  public boolean equals(Object other) {
    if (other instanceof FibonacciHeap) {
      @SuppressWarnings("unchecked")
      FibonacciHeap<E> casted = (FibonacciHeap<E>) other;
      if (size != casted.size) return false;
      if (compare.comesBefore(0, 1) != casted.compare.comesBefore(0, 1)) return false;
      Iterator<PriorityQueueNode.Integer<E>> iter = iterator();
      Iterator<PriorityQueueNode.Integer<E>> otherIter = casted.iterator();
      while (iter.hasNext()) {
        if (!iter.next().equals(otherIter.next())) {
          return false;
        }
      }
      return true;
    }
    return false;
  }

  /**
   * Computes a hashCode.
   *
   * @return a hashCode
   */
  @Override
  public int hashCode() {
    int h = 0;
    for (PriorityQueueNode.Integer<E> e : this) {
      h = 31 * h + e.value;
      h = 31 * h + e.element.hashCode();
    }
    return h;
  }

  @Override
  public boolean isEmpty() {
    return size == 0;
  }

  @Override
  public Iterator<PriorityQueueNode.Integer<E>> iterator() {
    return new FibonacciHeapNode.FibonacciHeapIterator<E>(min);
  }

  /**
   * {@inheritDoc}
   *
   * @throws IllegalArgumentException if this and other have different priority-order (e.g., one is
   *     a minheap while the other is a maxheap)
   */
  @Override
  public boolean merge(FibonacciHeap<E> other) {
    if (compare.comesBefore(0, 1) != other.compare.comesBefore(0, 1)) {
      throw new IllegalArgumentException("this and other follow different priority-order");
    }
    if (other.size > 0) {
      index.putAll(other.index);
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

  @Override
  public boolean offer(E element, int priority) {
    if (index.containsKey(element)) {
      return false;
    }
    internalOffer(new PriorityQueueNode.Integer<E>(element, priority));
    return true;
  }

  @Override
  public boolean offer(PriorityQueueNode.Integer<E> pair) {
    if (index.containsKey(pair.element)) {
      return false;
    }
    internalOffer(pair.copy());
    return true;
  }

  @Override
  public E peekElement() {
    return min != null ? min.e.element : null;
  }

  @Override
  public PriorityQueueNode.Integer<E> peek() {
    return min != null ? min.e : null;
  }

  @Override
  public int peekPriority() {
    return min != null ? min.e.value : extreme;
  }

  @Override
  public int peekPriority(E element) {
    FibonacciHeapNode<E> node = index.get(element);
    return node != null ? node.e.value : extreme;
  }

  @Override
  public E pollElement() {
    PriorityQueueNode.Integer<E> min = poll();
    return min != null ? min.element : null;
  }

  @Override
  public PriorityQueueNode.Integer<E> poll() {
    PriorityQueueNode.Integer<E> result = null;
    if (size == 1) {
      PriorityQueueNode.Integer<E> pair = min.e;
      min = null;
      size = 0;
      result = pair;
      index.remove(result.element);
    } else if (size > 1) {
      FibonacciHeapNode<E> z = min;
      min = min.removeSelf();
      min = consolidator.consolidate(min, size);
      size--;
      result = z.e;
      index.remove(result.element);
    }
    return result;
  }

  @Override
  public boolean promote(E element, int priority) {
    FibonacciHeapNode<E> node = index.get(element);
    if (node != null && compare.comesBefore(priority, node.e.value)) {
      internalPromote(node, priority);
      return true;
    }
    return false;
  }

  @Override
  public boolean remove(Object o) {
    FibonacciHeapNode<E> node = null;
    if (o instanceof PriorityQueueNode.Integer) {
      PriorityQueueNode.Integer pair = (PriorityQueueNode.Integer) o;
      node = index.get(pair.element);
    } else {
      node = index.get(o);
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

  /**
   * {@inheritDoc}
   *
   * <p>The runtime of this method is O(n + m) where n is current size of the heap and m is the size
   * of the Collection c. In general this is more efficient than calling remove repeatedly.
   */
  @Override
  public boolean removeAll(Collection<?> c) {
    HashSet<Object> discardThese = toSet(c);
    ArrayList<PriorityQueueNode.Integer<E>> keepList =
        new ArrayList<PriorityQueueNode.Integer<E>>();
    for (PriorityQueueNode.Integer<E> e : this) {
      if (!discardThese.contains(e.element)) {
        keepList.add(e);
      }
    }
    if (keepList.size() < size) {
      clear();
      for (PriorityQueueNode.Integer<E> e : keepList) {
        internalOffer(e);
      }
      return true;
    }
    return false;
  }

  /**
   * {@inheritDoc}
   *
   * <p>The runtime of this method is O(n + m) where n is current size of the heap and m is the size
   * of the Collection c. In general this is more efficient than calling remove repeatedly.
   */
  @Override
  public boolean retainAll(Collection<?> c) {
    HashSet<Object> keepThese = toSet(c);
    ArrayList<PriorityQueueNode.Integer<E>> keepList =
        new ArrayList<PriorityQueueNode.Integer<E>>(keepThese.size());
    for (PriorityQueueNode.Integer<E> e : this) {
      if (keepThese.contains(e.element)) {
        keepList.add(e);
      }
    }
    if (keepList.size() < size) {
      clear();
      for (PriorityQueueNode.Integer<E> e : keepList) {
        internalOffer(e);
      }
      return true;
    }
    return false;
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public Object[] toArray() {
    Object[] array = new Object[size];
    int i = 0;
    for (PriorityQueueNode.Integer<E> e : this) {
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
  public <T> T[] toArray(T[] array) {
    @SuppressWarnings("unchecked")
    T[] result =
        array.length >= size
            ? array
            : (T[]) Array.newInstance(array.getClass().getComponentType(), size);
    int i = 0;
    for (PriorityQueueNode.Integer<E> e : this) {
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

  private void internalOffer(PriorityQueueNode.Integer<E> pair) {
    if (min == null) {
      min = new FibonacciHeapNode<E>(pair);
      size = 1;
      index.put(pair.element, min);
    } else {
      FibonacciHeapNode<E> added = new FibonacciHeapNode<E>(pair, min);
      if (compare.comesBefore(pair.value, min.e.value)) {
        min = added;
      }
      size++;
      index.put(pair.element, added);
    }
  }

  private void internalPromote(FibonacciHeapNode<E> x, int priority) {
    // only called if priority decreased for a minheap (increased for a maxheap)
    // so no checks needed here.
    x.e.value = priority;
    FibonacciHeapNode<E> y = x.parent();
    if (y != null && compare.comesBefore(priority, y.e.value)) {
      x.cut(y, min);
      y.cascadingCut(min);
    }
    if (compare.comesBefore(priority, min.e.value)) {
      min = x;
    }
  }

  private void internalDemote(FibonacciHeapNode<E> x, int priority) {
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

  private HashSet<Object> toSet(Collection<?> c) {
    HashSet<Object> set = new HashSet<Object>();
    for (Object o : c) {
      if (o instanceof PriorityQueueNode.Integer) {
        PriorityQueueNode.Integer pair = (PriorityQueueNode.Integer) o;
        set.add(pair.element);
      } else {
        set.add(o);
      }
    }
    return set;
  }
}
