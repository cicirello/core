/*
 * Module org.cicirello.core
 * Copyright 2019-2023 Vincent A. Cicirello, <https://www.cicirello.org/>.
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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.cicirello.util.Copyable;

/**
 * An implementation of a Binary Heap. An instance of a BinaryHeapDouble contains (element,
 * priority) pairs, such that the elements are distinct. The priority values are of type double.
 *
 * <p><b>Priority order:</b> BinaryHeapDouble instances are created via factory methods with names
 * beginning with <code>create</code>. The priority order depends upon the factory method used to
 * create the BinaryHeapDouble. Methods named <code>createMinHeap</code> produce a min heap with
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
 * BinaryHeapDouble&lt;String&gt; pq = BinaryHeapDouble.createMinHeap();
 * </code></pre>
 *
 * <p><b>Method runtimes:</b> The asymptotic runtime of the methods of this class are as follows
 * (where n is the current size of the heap and m is the size of a Collection parameter where
 * relevant):
 *
 * <ul>
 *   <li><b>O(1):</b> {@link #contains}, {@link #createMaxHeap()}, {@link #createMaxHeap(int)},
 *       {@link #createMinHeap()}, {@link #createMinHeap(int)}, {@link #element}, {@link #isEmpty},
 *       {@link #iterator}, {@link #peek}, {@link #peekElement}, {@link #peekPriority()}, {@link
 *       #peekPriority(Object)}, {@link #size()}
 *   <li><b>O(lg n):</b> {@link #add(Object, double)}, {@link #add(PriorityQueueNode.Double)},
 *       {@link #change}, {@link #demote}, {@link #offer(Object, double)}, {@link
 *       #offer(PriorityQueueNode.Double)}, {@link #poll}, {@link #pollElement}, {@link
 *       #pollThenAdd(Object, double)}, {@link #pollThenAdd(PriorityQueueNode.Double)}, {@link
 *       #promote}, {@link #remove()}, {@link #remove(Object)}, {@link #removeElement()}
 *   <li><b>O(m):</b> {@link #containsAll(Collection)}, {@link #createMaxHeap(Collection)}, {@link
 *       #createMinHeap(Collection)}
 *   <li><b>O(n):</b> {@link #clear}, {@link #copy()}, {@link #ensureCapacity}, {@link #equals},
 *       {@link #hashCode}, {@link #toArray()}, {@link #toArray(Object[])}, {@link #trimToSize}
 *   <li><b>O(n + m):</b> {@link #addAll(Collection)}, {@link #merge(BinaryHeapDouble)}, {@link
 *       #removeAll(Collection)}, {@link #retainAll(Collection)}
 * </ul>
 *
 * @param <E> The type of object contained in the BinaryHeapDouble.
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, <a
 *     href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public final class BinaryHeapDouble<E>
    implements MergeablePriorityQueueDouble<E, BinaryHeapDouble<E>>, Copyable<BinaryHeapDouble<E>> {

  private PriorityQueueNode.Double<E>[] buffer;
  private int size;
  private final HashMap<E, java.lang.Integer> index;
  private final Prioritizer compare;
  private final double extreme;

  /** The default initial capacity. */
  public static final int DEFAULT_INITIAL_CAPACITY = 16;

  /* PRIVATE: Use factory methods for creation.
   *
   * Initializes an empty BinaryHeapDouble.
   *
   * @param initialCapacity The initial capacity, which must be positive.
   */
  private BinaryHeapDouble(int initialCapacity) {
    this(initialCapacity, new MinOrder());
  }

  /* PRIVATE: Use factory methods for creation.
   *
   * Initializes an empty BinaryHeapDouble.
   *
   * @param initialCapacity The initial capacity, which must be positive.
   */
  private BinaryHeapDouble(int initialCapacity, Prioritizer compare) {
    this.compare = compare;
    buffer = allocate(initialCapacity);
    index = new HashMap<E, java.lang.Integer>();
    extreme =
        compare.comesBefore(0, 1)
            ? java.lang.Double.POSITIVE_INFINITY
            : java.lang.Double.NEGATIVE_INFINITY;
  }

  /* PRIVATE: Use factory methods for creation.
   *
   * Initializes a BinaryHeapDouble from a collection of (element, priority) pairs.
   *
   * @param initialElements The initial collection of (element, priority) pairs, which must be
   * non-empty.
   *
   * @throws IllegalArgumentException if initialElements is empty, or if more than
   * one pair in initialElements contains the same element.
   */
  private BinaryHeapDouble(Collection<PriorityQueueNode.Double<E>> initialElements) {
    this(initialElements, new MinOrder());
  }

  /* PRIVATE: Use factory methods for creation.
   *
   * Initializes a BinaryHeapDouble from a collection of (element, priority) pairs.
   *
   * @param initialElements The initial collection of (element, priority) pairs, which must be
   * non-empty.
   *
   * @throws IllegalArgumentException if initialElements is empty, or if more than
   * one pair in initialElements contains the same element.
   */
  private BinaryHeapDouble(
      Collection<PriorityQueueNode.Double<E>> initialElements, Prioritizer compare) {
    this(initialElements.size(), compare);
    for (PriorityQueueNode.Double<E> element : initialElements) {
      if (index.containsKey(element.element)) {
        throw new IllegalArgumentException("initialElements contains duplicates");
      }
      buffer[size] = element.copy();
      index.put(buffer[size].element, size);
      size++;
    }
    buildHeap();
  }

  /*
   * private copy constructor to support the copy() method.
   */
  private BinaryHeapDouble(BinaryHeapDouble<E> other) {
    this(other.capacity(), other.compare);
    size = other.size;
    for (int i = 0; i < size; i++) {
      buffer[i] = other.buffer[i].copy();
      index.put(buffer[i].element, i);
    }
  }

  @Override
  public BinaryHeapDouble<E> copy() {
    return new BinaryHeapDouble<E>(this);
  }

  /**
   * Creates an empty BinaryHeapDouble with the {@link #DEFAULT_INITIAL_CAPACITY} as the initial
   * capacity, and a minimum-priority-first-out priority order.
   *
   * @param <E> The type of elements contained in the BinaryHeapDouble.
   * @return an empty BinaryHeapDouble with a minimum-priority-first-out priority order
   */
  public static <E> BinaryHeapDouble<E> createMinHeap() {
    return new BinaryHeapDouble<E>(DEFAULT_INITIAL_CAPACITY);
  }

  /**
   * Creates an empty BinaryHeapDouble with a specified initial capacity, and a
   * minimum-priority-first-out priority order.
   *
   * @param initialCapacity The initial capacity, which must be positive.
   * @param <E> The type of elements contained in the BinaryHeapDouble.
   * @return an empty BinaryHeapDouble with a minimum-priority-first-out priority order
   * @throws IllegalArgumentException if initialCapacity is less than or equal to 0.
   */
  public static <E> BinaryHeapDouble<E> createMinHeap(int initialCapacity) {
    if (initialCapacity <= 0)
      throw new IllegalArgumentException("Initial capacity must be positive.");
    return new BinaryHeapDouble<E>(initialCapacity);
  }

  /**
   * Creates a BinaryHeapDouble from a collection of (element, priority) pairs, with a
   * minimum-priority-first-out priority order.
   *
   * @param initialElements The initial collection of (element, priority) pairs, which must be
   *     non-empty.
   * @param <E> The type of elements contained in the BinaryHeapDouble.
   * @return a BinaryHeapDouble with a minimum-priority-first-out priority order
   * @throws IllegalArgumentException if initialElements is empty, or if more than one pair in
   *     initialElements contains the same element.
   */
  public static <E> BinaryHeapDouble<E> createMinHeap(
      Collection<PriorityQueueNode.Double<E>> initialElements) {
    if (initialElements.size() < 1) {
      throw new IllegalArgumentException("initialElements is empty");
    }
    return new BinaryHeapDouble<E>(initialElements);
  }

  /**
   * Creates an empty BinaryHeapDouble with the {@link #DEFAULT_INITIAL_CAPACITY} as the initial
   * capacity, and a maximum-priority-first-out priority order.
   *
   * @param <E> The type of elements contained in the BinaryHeapDouble.
   * @return an empty BinaryHeapDouble with a maximum-priority-first-out priority order
   */
  public static <E> BinaryHeapDouble<E> createMaxHeap() {
    return new BinaryHeapDouble<E>(DEFAULT_INITIAL_CAPACITY, new MaxOrder());
  }

  /**
   * Creates an empty BinaryHeapDouble with a specified initial capacity, and a
   * maximum-priority-first-out priority order.
   *
   * @param initialCapacity The initial capacity, which must be positive.
   * @param <E> The type of elements contained in the BinaryHeapDouble.
   * @return an empty BinaryHeapDouble with a maximum-priority-first-out priority order
   * @throws IllegalArgumentException if initialCapacity is less than or equal to 0.
   */
  public static <E> BinaryHeapDouble<E> createMaxHeap(int initialCapacity) {
    if (initialCapacity <= 0)
      throw new IllegalArgumentException("Initial capacity must be positive.");
    return new BinaryHeapDouble<E>(initialCapacity, new MaxOrder());
  }

  /**
   * Creates a BinaryHeapDouble from a collection of (element, priority) pairs, with a
   * maximum-priority-first-out priority order.
   *
   * @param initialElements The initial collection of (element, priority) pairs, which must be
   *     non-empty.
   * @param <E> The type of elements contained in the BinaryHeapDouble.
   * @return a BinaryHeapDouble with a maximum-priority-first-out priority order
   * @throws IllegalArgumentException if initialElements is empty, or if more than one pair in
   *     initialElements contains the same element.
   */
  public static <E> BinaryHeapDouble<E> createMaxHeap(
      Collection<PriorityQueueNode.Double<E>> initialElements) {
    if (initialElements.size() < 1) {
      throw new IllegalArgumentException("initialElements is empty");
    }
    return new BinaryHeapDouble<E>(initialElements, new MaxOrder());
  }

  /**
   * {@inheritDoc}
   *
   * <p>The runtime of this method is O(n + m) where n is current size of the heap and m is the size
   * of the Collection c. In general this is more efficient than calling add repeatedly, unless you
   * are adding a relatively small number of elements, in which case you should instead call either
   * {@link #offer(PriorityQueueNode.Double)} or {@link #add(PriorityQueueNode.Double)} for each
   * (element, priority) pair you want to add.
   *
   * @throws IllegalArgumentException if the heap already contains any of the (element, priority)
   *     pairs.
   */
  @Override
  public final boolean addAll(Collection<? extends PriorityQueueNode.Double<E>> c) {
    if (size + c.size() > buffer.length) {
      internalAdjustCapacity((size + c.size()) << 1);
    }
    boolean changed = false;
    for (PriorityQueueNode.Double<E> e : c) {
      if (index.containsKey(e.element)) {
        throw new IllegalArgumentException("heap already contains one or more of these elements");
      }
      buffer[size] = e.copy();
      index.put(buffer[size].element, size);
      size++;
      changed = true;
    }
    if (changed) {
      buildHeap();
    }
    return changed;
  }

  @Override
  public final boolean change(E element, double priority) {
    if (!offer(element, priority)) {
      int i = index.get(element);
      if (compare.comesBefore(priority, buffer[i].value)) {
        buffer[i].value = priority;
        percolateUp(i);
        return true;
      } else if (compare.comesBefore(buffer[i].value, priority)) {
        buffer[i].value = priority;
        percolateDown(i);
        return true;
      }
      return false;
    }
    return true;
  }

  @Override
  public final void clear() {
    for (int i = 0; i < size; i++) {
      buffer[i] = null;
    }
    size = 0;
    index.clear();
  }

  @Override
  public final boolean contains(Object o) {
    if (o instanceof PriorityQueueNode.Double) {
      PriorityQueueNode.Double pair = (PriorityQueueNode.Double) o;
      return index.containsKey(pair.element);
    }
    return index.containsKey(o);
  }

  @Override
  public final boolean demote(E element, double priority) {
    Integer where = index.get(element);
    if (where != null) {
      int i = where;
      if (compare.comesBefore(buffer[i].value, priority)) {
        buffer[i].value = priority;
        percolateDown(i);
        return true;
      }
    }
    return false;
  }

  /**
   * Increases the capacity if the capacity is not already at least the specified minimum. If the
   * capacity is at or above the requested minimum, then this method does nothing.
   *
   * @param minCapacity The desired minimum capacity.
   */
  public final void ensureCapacity(int minCapacity) {
    if (buffer.length < minCapacity) {
      internalAdjustCapacity(minCapacity);
    }
  }

  /**
   * Checks if this BinaryHeapDouble contains the same (element, priority) pairs as another
   * BinaryHeapDouble, including the specific order within the BinaryHeapDouble, as well as that the
   * priority order is the same.
   *
   * @param other The other BinaryHeapDouble.
   * @return true if and only if this and other contain the same (element, priority) pairs, with the
   *     same priority order.
   */
  @Override
  public boolean equals(Object other) {
    if (other instanceof BinaryHeapDouble) {
      @SuppressWarnings("unchecked")
      BinaryHeapDouble<E> casted = (BinaryHeapDouble<E>) other;
      if (size != casted.size) return false;
      if (compare.comesBefore(0, 1) != casted.compare.comesBefore(0, 1)) return false;
      for (int i = 0; i < size; i++) {
        if (!buffer[i].element.equals(casted.buffer[i].element)) return false;
        if (casted.buffer[i].value != buffer[i].value) return false;
      }
      return true;
    } else {
      return false;
    }
  }

  /**
   * Computes a hashCode for the BinaryHeapDouble.
   *
   * @return a hashCode
   */
  @Override
  public int hashCode() {
    int h = 0;
    for (int i = 0; i < size; i++) {
      h = 31 * h + java.lang.Double.hashCode(buffer[i].value);
      h = 31 * h + buffer[i].element.hashCode();
    }
    return h;
  }

  @Override
  public final boolean isEmpty() {
    return size == 0;
  }

  @Override
  public final Iterator<PriorityQueueNode.Double<E>> iterator() {
    return new BinaryHeapDoubleIterator();
  }

  /**
   * {@inheritDoc}
   *
   * @throws IllegalArgumentException if this and other have different priority-order (e.g., one is
   *     a minheap while the other is a maxheap)
   */
  @Override
  public boolean merge(BinaryHeapDouble<E> other) {
    if (compare.comesBefore(0, 1) != other.compare.comesBefore(0, 1)) {
      throw new IllegalArgumentException("this and other follow different priority-order");
    }
    if (size + other.size() > buffer.length) {
      internalAdjustCapacity((size + other.size()) << 1);
    }
    boolean changed = false;
    for (int i = 0; i < other.size; i++) {
      buffer[size] = other.buffer[i];
      index.put(buffer[size].element, size);
      size++;
      changed = true;
    }
    if (changed) {
      other.clear();
      buildHeap();
    }
    return changed;
  }

  @Override
  public final boolean offer(E element, double priority) {
    if (contains(element)) {
      return false;
    }
    return internalOffer(new PriorityQueueNode.Double<E>(element, priority));
  }

  @Override
  public final boolean offer(PriorityQueueNode.Double<E> pair) {
    if (contains(pair.element)) {
      return false;
    }
    return internalOffer(pair.copy());
  }

  @Override
  public final E peekElement() {
    return size > 0 ? buffer[0].element : null;
  }

  @Override
  public final PriorityQueueNode.Double<E> peek() {
    return size > 0 ? buffer[0] : null;
  }

  @Override
  public final double peekPriority() {
    return size > 0 ? buffer[0].value : extreme;
  }

  @Override
  public final double peekPriority(E element) {
    java.lang.Integer i = index.get(element);
    return i != null ? buffer[i].value : extreme;
  }

  @Override
  public final E pollElement() {
    PriorityQueueNode.Double<E> min = poll();
    return min != null ? min.element : null;
  }

  @Override
  public final PriorityQueueNode.Double<E> poll() {
    if (size > 0) {
      PriorityQueueNode.Double<E> min = buffer[0];
      index.remove(min.element);
      size--;
      if (size > 0) {
        buffer[0] = buffer[size];
        buffer[size] = null;
        index.put(buffer[0].element, 0);
        percolateDown(0);
      } else {
        buffer[0] = null;
      }
      return min;
    } else {
      return null;
    }
  }

  /**
   * {@inheritDoc}
   *
   * @throws IllegalArgumentException if, after the poll part of this operation, the priority queue
   *     contains the element from the pair to add.
   */
  @Override
  public final PriorityQueueNode.Double<E> pollThenAdd(PriorityQueueNode.Double<E> pair) {
    PriorityQueueNode.Double<E> min = size > 0 ? buffer[0] : null;
    if (min != null) {
      index.remove(min.element);
    }
    if (contains(pair.element)) {
      throw new IllegalArgumentException(
          "This priority queue doesn't support duplicate elements, and already contains the element.");
    }
    buffer[0] = pair.copy();
    index.put(buffer[0].element, 0);
    if (size <= 0) {
      size = 1;
    } else {
      percolateDown(0);
    }
    return min;
  }

  /**
   * {@inheritDoc}
   *
   * @throws IllegalArgumentException if, after the poll part of this operation, the priority queue
   *     contains the element.
   */
  @Override
  public final E pollThenAdd(E element, double priority) {
    E min = size > 0 ? buffer[0].element : null;
    if (min != null) {
      index.remove(min);
    }
    if (contains(element)) {
      throw new IllegalArgumentException(
          "This priority queue doesn't support duplicate elements, and already contains the element.");
    }
    buffer[0] = new PriorityQueueNode.Double<E>(element, priority);
    index.put(buffer[0].element, 0);
    if (size <= 0) {
      size = 1;
    } else {
      percolateDown(0);
    }
    return min;
  }

  @Override
  public final boolean promote(E element, double priority) {
    Integer where = index.get(element);
    if (where != null) {
      int i = where;
      if (compare.comesBefore(priority, buffer[i].value)) {
        buffer[i].value = priority;
        percolateUp(i);
        return true;
      }
    }
    return false;
  }

  @Override
  public final boolean remove(Object o) {
    java.lang.Integer i = null;
    if (o instanceof PriorityQueueNode.Double) {
      PriorityQueueNode.Double pair = (PriorityQueueNode.Double) o;
      i = index.get(pair.element);
    } else {
      i = index.get(o);
    }
    if (i == null) {
      return false;
    }
    index.remove(buffer[i].element);
    size--;
    if (size > 0 && i != size) {
      double removedElementPriority = buffer[i].value;
      buffer[i] = buffer[size];
      buffer[size] = null;
      index.put(buffer[i].element, i);
      // percolate in relevant direction
      if (compare.comesBefore(buffer[i].value, removedElementPriority)) {
        percolateUp(i);
      } else if (compare.comesBefore(removedElementPriority, buffer[i].value)) {
        percolateDown(i);
      }
    } else {
      buffer[i] = null;
    }
    return true;
  }

  /**
   * {@inheritDoc}
   *
   * <p>The runtime of this method is O(n + m) where n is current size of the heap and m is the size
   * of the Collection c. In general this is more efficient than calling remove repeatedly, unless
   * you are removing a relatively small number of elements, in which case you should instead call
   * {@link #remove(Object)} for each element you want to remove.
   */
  @Override
  public final boolean removeAll(Collection<?> c) {
    HashSet<Object> discardThese = new HashSet<Object>();
    for (Object o : c) {
      if (o instanceof PriorityQueueNode.Double) {
        PriorityQueueNode.Double pair = (PriorityQueueNode.Double) o;
        discardThese.add(pair.element);
      } else {
        discardThese.add(o);
      }
    }
    boolean changed = false;
    for (int i = size - 1; i >= 0; i--) {
      if (discardThese.contains(buffer[i].element)) {
        changed = true;
        index.remove(buffer[i].element);
        size--;
        if (i == size) {
          buffer[i] = null;
        } else {
          buffer[i] = buffer[size];
          index.put(buffer[i].element, i);
          buffer[size] = null;
        }
      }
    }
    if (changed) {
      buildHeap();
    }
    return changed;
  }

  /**
   * {@inheritDoc}
   *
   * <p>The runtime of this method is O(n + m) where n is current size of the heap and m is the size
   * of the Collection c. In general this is more efficient than calling remove repeatedly, unless
   * you are removing a relatively small number of elements, in which case you should instead call
   * {@link #remove(Object)} for each element you want to remove.
   */
  @Override
  public final boolean retainAll(Collection<?> c) {
    HashSet<Object> keepThese = new HashSet<Object>();
    for (Object o : c) {
      if (o instanceof PriorityQueueNode.Double) {
        PriorityQueueNode.Double pair = (PriorityQueueNode.Double) o;
        keepThese.add(pair.element);
      } else {
        keepThese.add(o);
      }
    }
    boolean changed = false;
    for (int i = size - 1; i >= 0; i--) {
      if (!keepThese.contains(buffer[i].element)) {
        changed = true;
        index.remove(buffer[i].element);
        size--;
        if (i == size) {
          buffer[i] = null;
        } else {
          buffer[i] = buffer[size];
          index.put(buffer[i].element, i);
          buffer[size] = null;
        }
      }
    }
    if (changed) {
      buildHeap();
    }
    return changed;
  }

  @Override
  public final int size() {
    return size;
  }

  @Override
  public final Object[] toArray() {
    Object[] array = new Object[size];
    for (int i = 0; i < size; i++) {
      array[i] = buffer[i];
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
    for (int i = 0; i < size; i++) {
      @SuppressWarnings("unchecked")
      T nextElement = (T) buffer[i];
      result[i] = nextElement;
    }
    if (result.length > size) {
      result[size] = null;
    }
    return result;
  }

  /**
   * Decreases the capacity to the current size of the BinaryHeapDouble, provided size is at least
   * 1, and otherwise decreases capacity to 1. If the size and the capacity are the same, then this
   * method does nothing.
   */
  public final void trimToSize() {
    if (size < buffer.length) {
      internalAdjustCapacity(size > 0 ? size : 1);
    }
  }

  /*
   * package-private to support testing.
   * no reason to provide public access to current capacity
   */
  int capacity() {
    return buffer.length;
  }

  private void percolateDown(int i) {
    int left;
    while ((left = (i << 1) + 1) < size) {
      int smallest = i;
      if (compare.comesBefore(buffer[left].value, buffer[i].value)) {
        smallest = left;
      }
      int right = left + 1;
      if (right < size && compare.comesBefore(buffer[right].value, buffer[smallest].value)) {
        smallest = right;
      }
      if (smallest != i) {
        PriorityQueueNode.Double<E> temp = buffer[i];
        buffer[i] = buffer[smallest];
        buffer[smallest] = temp;
        index.put(buffer[i].element, i);
        index.put(buffer[smallest].element, smallest);
        i = smallest;
      } else {
        break;
      }
    }
  }

  private void percolateUp(int i) {
    int parent;
    while (i > 0 && compare.comesBefore(buffer[i].value, buffer[parent = (i - 1) >> 1].value)) {
      PriorityQueueNode.Double<E> temp = buffer[i];
      buffer[i] = buffer[parent];
      buffer[parent] = temp;
      index.put(buffer[i].element, i);
      index.put(buffer[parent].element, parent);
      i = parent;
    }
  }

  private PriorityQueueNode.Double<E>[] allocate(int capacity) {
    @SuppressWarnings("unchecked")
    PriorityQueueNode.Double<E>[] temp = new PriorityQueueNode.Double[capacity];
    return temp;
  }

  /*
   * Used internally: ALERT that this will fail with exception if capacity < size ALERT.
   */
  private void internalAdjustCapacity(int capacity) {
    PriorityQueueNode.Double<E>[] temp = allocate(capacity);
    System.arraycopy(buffer, 0, temp, 0, size);
    buffer = temp;
  }

  /*
   * used internally: doesn't check if already contains element
   */
  private boolean internalOffer(PriorityQueueNode.Double<E> pair) {
    if (size == buffer.length) {
      internalAdjustCapacity(size << 1);
    }
    buffer[size] = pair;
    index.put(pair.element, size);
    percolateUp(size);
    size++;
    return true;
  }

  private void buildHeap() {
    for (int i = (size >> 1) - 1; i >= 0; i--) {
      percolateDown(i);
    }
  }

  private class BinaryHeapDoubleIterator implements Iterator<PriorityQueueNode.Double<E>> {

    private int index;

    public BinaryHeapDoubleIterator() {
      index = 0;
    }

    @Override
    public boolean hasNext() {
      return index < size;
    }

    @Override
    public PriorityQueueNode.Double<E> next() {
      if (index >= size) {
        throw new NoSuchElementException("No more elements remain.");
      }
      index++;
      return buffer[index - 1];
    }
  }
}
