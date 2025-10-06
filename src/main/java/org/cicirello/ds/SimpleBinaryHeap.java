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
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.cicirello.util.Copyable;

/**
 * An implementation of a Binary Heap. An instance of a SimpleBinaryHeap contains (element,
 * priority) pairs, such that the priority values are of type int.
 *
 * <p>Consider using the {@link BinaryHeap} class instead if your application requires any of the
 * following: distinct elements, efficient containment checks, efficient priority increases or
 * decreases, efficient arbitrary element removals. The {@link BinaryHeap} class can find an
 * arbitrary element in constant time, making all of those operations faster.
 *
 * <p><b>Priority order:</b> SimpleBinaryHeap instances are created via factory methods with names
 * beginning with <code>create</code>. The priority order depends upon the factory method used to
 * create the SimpleBinaryHeap. Methods named <code>createMinHeap</code> produce a min heap with
 * priority order minimum-priority-first-out. Methods named <code>createMaxHeap</code> produce a max
 * heap with priority order maximum-priority-first-out.
 *
 * <p><b>Creating instances:</b> To create an instance, use one of the factory methods, such as
 * with:
 *
 * <pre><code>
 * SimpleBinaryHeap&lt;String&gt; pq = SimpleBinaryHeap.createMinHeap();
 * </code></pre>
 *
 * <p><b>Method runtimes:</b> The asymptotic runtime of the methods of this class are as follows
 * (where n is the current size of the heap and m is the size of a Collection parameter where
 * relevant):
 *
 * <ul>
 *   <li><b>O(1):</b> {@link #createMaxHeap()}, {@link #createMaxHeap(int)}, {@link
 *       #createMinHeap()}, {@link #createMinHeap(int)}, {@link #element}, {@link #isEmpty}, {@link
 *       #iterator}, {@link #peek}, {@link #peekElement}, {@link #peekPriority()}, {@link #size()}
 *   <li><b>O(lg n):</b> {@link #add(Object, int)}, {@link #add(PriorityQueueNode.Integer)}, {@link
 *       #offer(Object, int)}, {@link #offer(PriorityQueueNode.Integer)}, {@link #poll}, {@link
 *       #pollElement}, {@link #pollThenAdd(Object, int)}, {@link
 *       #pollThenAdd(PriorityQueueNode.Integer)}, {@link #remove()}, {@link #removeElement()}
 *   <li><b>O(m):</b> {@link #createMaxHeap(Collection)}, {@link #createMinHeap(Collection)}
 *   <li><b>O(n):</b> {@link #change}, {@link #clear}, {@link #contains}, {@link #copy()}, {@link
 *       #demote}, {@link #ensureCapacity}, {@link #equals}, {@link #hashCode}, {@link
 *       #peekPriority(Object)}, {@link #promote}, {@link #remove(Object)}, {@link #toArray()},
 *       {@link #toArray(Object[])}, {@link #trimToSize}
 *   <li><b>O(n + m):</b> {@link #addAll(Collection)}, {@link #containsAll(Collection)}, {@link
 *       #merge(SimpleBinaryHeap)}, {@link #removeAll(Collection)}, {@link #retainAll(Collection)}
 * </ul>
 *
 * @param <E> The type of object contained in the SimpleBinaryHeap.
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, <a
 *     href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public final class SimpleBinaryHeap<E>
    implements MergeablePriorityQueue<E, SimpleBinaryHeap<E>>, Copyable<SimpleBinaryHeap<E>> {

  private PriorityQueueNode.Integer<E>[] buffer;
  private int size;
  private final IntegerPrioritizer compare;
  private final int extreme;

  /** The default initial capacity. */
  public static final int DEFAULT_INITIAL_CAPACITY = 16;

  /* PRIVATE: Use factory methods for creation.
   *
   * Initializes an empty SimpleBinaryHeap.
   *
   * @param initialCapacity The initial capacity, which must be positive.
   */
  private SimpleBinaryHeap(int initialCapacity) {
    this(initialCapacity, new IntegerMinOrder());
  }

  /* PRIVATE: Use factory methods for creation.
   *
   * Initializes an empty SimpleBinaryHeap.
   *
   * @param initialCapacity The initial capacity, which must be positive.
   */
  private SimpleBinaryHeap(int initialCapacity, IntegerPrioritizer compare) {
    this.compare = compare;
    buffer = allocate(initialCapacity);
    extreme = compare.comesBefore(0, 1) ? java.lang.Integer.MAX_VALUE : java.lang.Integer.MIN_VALUE;
  }

  /* PRIVATE: Use factory methods for creation.
   *
   * Initializes a SimpleBinaryHeap from a collection of (element, priority) pairs.
   *
   * @param initialElements The initial collection of (element, priority) pairs, which must be
   * non-empty.
   *
   * @throws IllegalArgumentException if initialElements is empty.
   */
  private SimpleBinaryHeap(Collection<PriorityQueueNode.Integer<E>> initialElements) {
    this(initialElements, new IntegerMinOrder());
  }

  /* PRIVATE: Use factory methods for creation.
   *
   * Initializes a SimpleBinaryHeap from a collection of (element, priority) pairs.
   *
   * @param initialElements The initial collection of (element, priority) pairs, which must be
   * non-empty.
   *
   * @throws IllegalArgumentException if initialElements is empty.
   */
  private SimpleBinaryHeap(
      Collection<PriorityQueueNode.Integer<E>> initialElements, IntegerPrioritizer compare) {
    this(initialElements.size(), compare);
    for (PriorityQueueNode.Integer<E> element : initialElements) {
      buffer[size] = element.copy();
      size++;
    }
    buildHeap();
  }

  /*
   * private copy constructor to support the copy() method.
   */
  private SimpleBinaryHeap(SimpleBinaryHeap<E> other) {
    this(other.capacity(), other.compare);
    size = other.size;
    for (int i = 0; i < size; i++) {
      buffer[i] = other.buffer[i].copy();
    }
  }

  @Override
  public SimpleBinaryHeap<E> copy() {
    return new SimpleBinaryHeap<E>(this);
  }

  /**
   * Creates an empty SimpleBinaryHeap with the {@link #DEFAULT_INITIAL_CAPACITY} as the initial
   * capacity, and a minimum-priority-first-out priority order.
   *
   * @param <E> The type of elements contained in the SimpleBinaryHeap.
   * @return an empty SimpleBinaryHeap with a minimum-priority-first-out priority order
   */
  public static <E> SimpleBinaryHeap<E> createMinHeap() {
    return new SimpleBinaryHeap<E>(DEFAULT_INITIAL_CAPACITY);
  }

  /**
   * Creates an empty SimpleBinaryHeap with a specified initial capacity, and a
   * minimum-priority-first-out priority order.
   *
   * @param initialCapacity The initial capacity, which must be positive.
   * @param <E> The type of elements contained in the SimpleBinaryHeap.
   * @return an empty SimpleBinaryHeap with a minimum-priority-first-out priority order
   * @throws IllegalArgumentException if initialCapacity is less than or equal to 0.
   */
  public static <E> SimpleBinaryHeap<E> createMinHeap(int initialCapacity) {
    if (initialCapacity <= 0)
      throw new IllegalArgumentException("Initial capacity must be positive.");
    return new SimpleBinaryHeap<E>(initialCapacity);
  }

  /**
   * Creates a SimpleBinaryHeap from a collection of (element, priority) pairs, with a
   * minimum-priority-first-out priority order.
   *
   * @param initialElements The initial collection of (element, priority) pairs, which must be
   *     non-empty.
   * @param <E> The type of elements contained in the SimpleBinaryHeap.
   * @return a SimpleBinaryHeap with a minimum-priority-first-out priority order
   * @throws IllegalArgumentException if initialElements is empty.
   */
  public static <E> SimpleBinaryHeap<E> createMinHeap(
      Collection<PriorityQueueNode.Integer<E>> initialElements) {
    if (initialElements.size() < 1) {
      throw new IllegalArgumentException("initialElements is empty");
    }
    return new SimpleBinaryHeap<E>(initialElements);
  }

  /**
   * Creates an empty SimpleBinaryHeap with the {@link #DEFAULT_INITIAL_CAPACITY} as the initial
   * capacity, and a maximum-priority-first-out priority order.
   *
   * @param <E> The type of elements contained in the SimpleBinaryHeap.
   * @return an empty SimpleBinaryHeap with a maximum-priority-first-out priority order
   */
  public static <E> SimpleBinaryHeap<E> createMaxHeap() {
    return new SimpleBinaryHeap<E>(DEFAULT_INITIAL_CAPACITY, new IntegerMaxOrder());
  }

  /**
   * Creates an empty SimpleBinaryHeap with a specified initial capacity, and a
   * maximum-priority-first-out priority order.
   *
   * @param initialCapacity The initial capacity, which must be positive.
   * @param <E> The type of elements contained in the SimpleBinaryHeap.
   * @return an empty SimpleBinaryHeap with a maximum-priority-first-out priority order
   * @throws IllegalArgumentException if initialCapacity is less than or equal to 0.
   */
  public static <E> SimpleBinaryHeap<E> createMaxHeap(int initialCapacity) {
    if (initialCapacity <= 0)
      throw new IllegalArgumentException("Initial capacity must be positive.");
    return new SimpleBinaryHeap<E>(initialCapacity, new IntegerMaxOrder());
  }

  /**
   * Creates a SimpleBinaryHeap from a collection of (element, priority) pairs, with a
   * maximum-priority-first-out priority order.
   *
   * @param initialElements The initial collection of (element, priority) pairs, which must be
   *     non-empty.
   * @param <E> The type of elements contained in the SimpleBinaryHeap.
   * @return a SimpleBinaryHeap with a maximum-priority-first-out priority order
   * @throws IllegalArgumentException if initialElements is empty.
   */
  public static <E> SimpleBinaryHeap<E> createMaxHeap(
      Collection<PriorityQueueNode.Integer<E>> initialElements) {
    if (initialElements.size() < 1) {
      throw new IllegalArgumentException("initialElements is empty");
    }
    return new SimpleBinaryHeap<E>(initialElements, new IntegerMaxOrder());
  }

  @Override
  public final boolean add(E element, int priority) {
    return offer(element, priority);
  }

  @Override
  public final boolean add(PriorityQueueNode.Integer<E> pair) {
    return offer(pair);
  }

  /**
   * {@inheritDoc}
   *
   * <p>The runtime of this method is O(n + m) where n is current size of the heap and m is the size
   * of the Collection c. In general this is more efficient than calling add repeatedly, unless you
   * are adding a relatively small number of elements, in which case you should instead call either
   * {@link #offer(PriorityQueueNode.Integer)} or {@link #add(PriorityQueueNode.Integer)} for each
   * (element, priority) pair you want to add.
   */
  @Override
  public final boolean addAll(Collection<? extends PriorityQueueNode.Integer<E>> c) {
    if (size + c.size() > buffer.length) {
      internalAdjustCapacity((size + c.size()) << 1);
    }
    boolean changed = false;
    for (PriorityQueueNode.Integer<E> e : c) {
      buffer[size] = e.copy();
      size++;
      changed = true;
    }
    if (changed) {
      buildHeap();
    }
    return changed;
  }

  @Override
  public final boolean change(E element, int priority) {
    int i = find(element);
    if (i >= 0) {
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
    return offer(element, priority);
  }

  @Override
  public final void clear() {
    for (int i = 0; i < size; i++) {
      buffer[i] = null;
    }
    size = 0;
  }

  @Override
  public final boolean contains(Object o) {
    if (o instanceof PriorityQueueNode.Integer) {
      PriorityQueueNode.Integer pair = (PriorityQueueNode.Integer) o;
      return find(pair.element) >= 0;
    }
    return find(o) >= 0;
  }

  /**
   * {@inheritDoc}
   *
   * <p>The runtime of this method is O(n + m) where n is current size of the heap and m is the size
   * of the Collection c. In general this is more efficient than calling {@link #contains(Object)}
   * repeatedly.
   */
  @Override
  public final boolean containsAll(Collection<?> c) {
    HashSet<E> containsThese = new HashSet<E>();
    for (int i = 0; i < size; i++) {
      containsThese.add(buffer[i].element);
    }
    for (Object o : c) {
      if (o instanceof PriorityQueueNode.Integer) {
        PriorityQueueNode.Integer pair = (PriorityQueueNode.Integer) o;
        if (!containsThese.contains(pair.element)) {
          return false;
        }
      } else if (!containsThese.contains(o)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public final boolean demote(E element, int priority) {
    int i = find(element);
    if (i >= 0) {
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
   * Checks if this SimpleBinaryHeap contains the same (element, priority) pairs as another
   * SimpleBinaryHeap, including the specific order within the SimpleBinaryHeap, as well as that the
   * priority order is the same.
   *
   * @param other The other SimpleBinaryHeap.
   * @return true if and only if this and other contain the same (element, priority) pairs, with the
   *     same priority order.
   */
  @Override
  public boolean equals(Object other) {
    if (other instanceof SimpleBinaryHeap) {
      @SuppressWarnings("unchecked")
      SimpleBinaryHeap<E> casted = (SimpleBinaryHeap<E>) other;
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
   * Computes a hashCode for the SimpleBinaryHeap.
   *
   * @return a hashCode
   */
  @Override
  public int hashCode() {
    int h = 0;
    for (int i = 0; i < size; i++) {
      h = 31 * h + buffer[i].value;
      h = 31 * h + buffer[i].element.hashCode();
    }
    return h;
  }

  @Override
  public final boolean isEmpty() {
    return size == 0;
  }

  @Override
  public final Iterator<PriorityQueueNode.Integer<E>> iterator() {
    return new SimpleBinaryHeapIterator();
  }

  /**
   * {@inheritDoc}
   *
   * @throws IllegalArgumentException if this and other have different priority-order (e.g., one is
   *     a minheap while the other is a maxheap)
   */
  @Override
  public boolean merge(SimpleBinaryHeap<E> other) {
    if (compare.comesBefore(0, 1) != other.compare.comesBefore(0, 1)) {
      throw new IllegalArgumentException("this and other follow different priority-order");
    }
    if (size + other.size() > buffer.length) {
      internalAdjustCapacity((size + other.size()) << 1);
    }
    boolean changed = false;
    for (int i = 0; i < other.size; i++) {
      buffer[size] = other.buffer[i];
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
  public final boolean offer(E element, int priority) {
    return internalOffer(new PriorityQueueNode.Integer<E>(element, priority));
  }

  @Override
  public final boolean offer(PriorityQueueNode.Integer<E> pair) {
    return internalOffer(pair.copy());
  }

  @Override
  public final E peekElement() {
    return size > 0 ? buffer[0].element : null;
  }

  @Override
  public final PriorityQueueNode.Integer<E> peek() {
    return size > 0 ? buffer[0] : null;
  }

  @Override
  public final int peekPriority() {
    return size > 0 ? buffer[0].value : extreme;
  }

  @Override
  public final int peekPriority(E element) {
    int i = find(element);
    return i >= 0 ? buffer[i].value : extreme;
  }

  @Override
  public final E pollElement() {
    PriorityQueueNode.Integer<E> min = poll();
    return min != null ? min.element : null;
  }

  @Override
  public final PriorityQueueNode.Integer<E> poll() {
    if (size > 0) {
      PriorityQueueNode.Integer<E> min = buffer[0];
      size--;
      if (size > 0) {
        buffer[0] = buffer[size];
        buffer[size] = null;
        percolateDown(0);
      } else {
        buffer[0] = null;
      }
      return min;
    } else {
      return null;
    }
  }

  @Override
  public final PriorityQueueNode.Integer<E> pollThenAdd(PriorityQueueNode.Integer<E> pair) {
    PriorityQueueNode.Integer<E> min = size > 0 ? buffer[0] : null;
    buffer[0] = pair.copy();
    if (size <= 0) {
      size = 1;
    } else {
      percolateDown(0);
    }
    return min;
  }

  @Override
  public final E pollThenAdd(E element, int priority) {
    E min = size > 0 ? buffer[0].element : null;
    buffer[0] = new PriorityQueueNode.Integer<E>(element, priority);
    if (size <= 0) {
      size = 1;
    } else {
      percolateDown(0);
    }
    return min;
  }

  @Override
  public final boolean promote(E element, int priority) {
    int i = find(element);
    if (i >= 0) {
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
    int i = -1;
    if (o instanceof PriorityQueueNode.Integer) {
      PriorityQueueNode.Integer pair = (PriorityQueueNode.Integer) o;
      i = find(pair.element);
    } else {
      i = find(o);
    }
    if (i < 0) {
      return false;
    }
    size--;
    if (size > 0 && i != size) {
      int removedElementPriority = buffer[i].value;
      buffer[i] = buffer[size];
      buffer[size] = null;
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
   * <p>Unlike the {@link remove(Object)} method, which removes one instance of the Object in cases
   * where it appears multiple times, this method removes all instances of all objects in the
   * Collection.
   *
   * <p>The runtime of this method is O(n + m) where n is current size of the heap and m is the size
   * of the Collection c. In general this is more efficient than calling remove repeatedly.
   */
  @Override
  public final boolean removeAll(Collection<?> c) {
    HashSet<Object> discardThese = new HashSet<Object>();
    for (Object o : c) {
      if (o instanceof PriorityQueueNode.Integer) {
        PriorityQueueNode.Integer pair = (PriorityQueueNode.Integer) o;
        discardThese.add(pair.element);
      } else {
        discardThese.add(o);
      }
    }
    boolean changed = false;
    for (int i = size - 1; i >= 0; i--) {
      if (discardThese.contains(buffer[i].element)) {
        changed = true;
        size--;
        if (i != size) {
          buffer[i] = buffer[size];
        }
        buffer[size] = null;
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
   * of the Collection c. In general this is more efficient than calling remove repeatedly.
   */
  @Override
  public final boolean retainAll(Collection<?> c) {
    HashSet<Object> keepThese = new HashSet<Object>();
    for (Object o : c) {
      if (o instanceof PriorityQueueNode.Integer) {
        PriorityQueueNode.Integer pair = (PriorityQueueNode.Integer) o;
        keepThese.add(pair.element);
      } else {
        keepThese.add(o);
      }
    }
    boolean changed = false;
    for (int i = size - 1; i >= 0; i--) {
      if (!keepThese.contains(buffer[i].element)) {
        changed = true;
        size--;
        if (i != size) {
          buffer[i] = buffer[size];
        }
        buffer[size] = null;
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
   * Decreases the capacity to the current size of the SimpleBinaryHeap, provided size is at least
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

  private final int find(Object element) {
    for (int i = 0; i < size; i++) {
      if (buffer[i].element.equals(element)) {
        return i;
      }
    }
    return -1;
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
        PriorityQueueNode.Integer<E> temp = buffer[i];
        buffer[i] = buffer[smallest];
        buffer[smallest] = temp;
        i = smallest;
      } else {
        break;
      }
    }
  }

  private void percolateUp(int i) {
    int parent;
    while (i > 0 && compare.comesBefore(buffer[i].value, buffer[parent = (i - 1) >> 1].value)) {
      PriorityQueueNode.Integer<E> temp = buffer[i];
      buffer[i] = buffer[parent];
      buffer[parent] = temp;
      i = parent;
    }
  }

  private PriorityQueueNode.Integer<E>[] allocate(int capacity) {
    @SuppressWarnings("unchecked")
    PriorityQueueNode.Integer<E>[] temp = new PriorityQueueNode.Integer[capacity];
    return temp;
  }

  /*
   * Used internally: ALERT that this will fail with exception if capacity < size ALERT.
   */
  private void internalAdjustCapacity(int capacity) {
    buffer = Arrays.copyOfRange(buffer, 0, capacity);
  }

  /*
   * used internally: doesn't check if already contains element
   */
  private boolean internalOffer(PriorityQueueNode.Integer<E> pair) {
    if (size == buffer.length) {
      internalAdjustCapacity(size << 1);
    }
    buffer[size] = pair;
    percolateUp(size);
    size++;
    return true;
  }

  private void buildHeap() {
    for (int i = (size >> 1) - 1; i >= 0; i--) {
      percolateDown(i);
    }
  }

  private class SimpleBinaryHeapIterator implements Iterator<PriorityQueueNode.Integer<E>> {

    private int index;

    public SimpleBinaryHeapIterator() {
      index = 0;
    }

    @Override
    public boolean hasNext() {
      return index < size;
    }

    @Override
    public PriorityQueueNode.Integer<E> next() {
      if (index >= size) {
        throw new NoSuchElementException("No more elements remain.");
      }
      index++;
      return buffer[index - 1];
    }
  }
}
