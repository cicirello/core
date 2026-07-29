/*
 * Module org.cicirello.core
 * Copyright 2019-2026 Vincent A. Cicirello, <https://www.cicirello.org/>.
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

import java.util.Arrays;
import org.cicirello.util.Copyable;

/**
 * An implementation of a Binary Heap of (element, priority) pairs, such that the elements are
 * distinct integers in the interval [0, n), and with priority values of type double.
 *
 * <p><b>Priority order:</b> IntBinaryHeapDouble instances are created via factory methods with
 * names beginning with <code>create</code>. The priority order depends upon the factory method used
 * to create the IntBinaryHeapDouble. Methods named <code>createMinHeap</code> produce a min heap
 * with priority order minimum-priority-first-out. Methods named <code>createMaxHeap</code> produce
 * a max heap with priority order maximum-priority-first-out.
 *
 * <p><b>Creating instances:</b> To create an instance, use one of the factory methods. In this
 * example an IntBinaryHeapDouble with an element domain of [0,100) is created:
 *
 * <pre><code>
 * IntBinaryHeapDouble pq = IntBinaryHeapDouble.createMinHeap(100);
 * </code></pre>
 *
 * <p>In the above example, the element domain is [0,100) and the IntBinaryHeapDouble is initially
 * empty.
 *
 * <p><b>Purpose:</b> The purpose of such an IntBinaryHeapDouble is to support implementations of
 * algorithms that require such a specialized case. For example, some graph algorithms such as
 * Dijkstra's algorithm for single-source shortest paths, and Prim's algorithm for minimum spanning
 * tree, rely on a priority queue of the vertex ids, which are usually ints in some finite range.
 * Although such applications could use the classes that instead implement the {@link
 * PriorityQueueDouble} interface, using Java's wrapper type {@link Integer}, the classes that
 * implement {@link IntPriorityQueueDouble} that specialize the element type to int are optimized
 * for this special case.
 *
 * <p>For a more general purpose binary heap, see the {@link BinaryHeapDouble} class.
 *
 * <p><b>Method runtimes:</b> The asymptotic runtime of the methods of this class are as follows
 * (where n is the current size of the heap):
 *
 * <ul>
 *   <li><b>O(1):</b> {@link #contains(int)}, {@link #createMaxHeap(int)}, {@link
 *       #createMinHeap(int)}, {@link #domain()}, {@link #isEmpty()}, {@link #peek()}, {@link
 *       #peekPriority()}, {@link #peekPriority(int)}, {@link #size()}
 *   <li><b>O(lg n):</b> {@link #change(int,double)}, {@link #demote(int,double)}, {@link
 *       #offer(int, double)}, {@link #poll()}, {@link #pollThenOffer(int,double)}, {@link
 *       #promote(int,double)}
 *   <li><b>O(n):</b> {@link #clear()}, {@link #copy()}, {@link #toArray()}
 * </ul>
 *
 * @author <a href=https://www.cicirello.org/ target=_top>Vincent A. Cicirello</a>, <a
 *     href=https://www.cicirello.org/ target=_top>https://www.cicirello.org/</a>
 */
public final class IntBinaryHeapDouble
    implements IntPriorityQueueDouble, Copyable<IntBinaryHeapDouble> {

  private final int[] heap;
  private final int[] index;
  private final double[] value;
  private final boolean[] in;
  private int size;
  private final DoublePrioritizer prioritizer;

  /**
   * Initializes an empty min-heap of (int, priority) pairs, such that the domain of the elements
   * are the integers in [0, n).
   *
   * @param n The size of the domain of the elements of the min-heap.
   */
  private IntBinaryHeapDouble(int n, DoublePrioritizer prioritizer) {
    heap = new int[n];
    index = new int[n];
    value = new double[n];
    in = new boolean[n];
    this.prioritizer = prioritizer;
  }

  /*
   * private copy constructor to support copy() nethod
   */
  private IntBinaryHeapDouble(IntBinaryHeapDouble other) {
    heap = other.heap.clone();
    index = other.index.clone();
    value = other.value.clone();
    in = other.in.clone();
    size = other.size;
    prioritizer = other.prioritizer;
  }

  @Override
  public IntBinaryHeapDouble copy() {
    return new IntBinaryHeapDouble(this);
  }

  /**
   * {@inheritDoc}
   *
   * @throws IndexOutOfBoundsException if element is negative, or if element is greater than or
   *     equal to the domain n.
   */
  @Override
  public boolean change(int element, double priority) {
    if (!in[element]) {
      internalOffer(element, priority);
      return true;
    }
    return internalPromote(element, priority) || internalDemote(element, priority);
  }

  @Override
  public final void clear() {
    if (size > 0) {
      Arrays.fill(in, false);
      size = 0;
    }
  }

  /**
   * {@inheritDoc}
   *
   * @throws IndexOutOfBoundsException if element is negative, or if element is greater than or
   *     equal to the domain n.
   */
  @Override
  public final boolean contains(int element) {
    return in[element];
  }

  /**
   * Initializes an empty max-heap of (int, priority) pairs, such that the domain of the elements
   * are the integers in [0, n).
   *
   * @param n The size of the domain of the elements of the max-heap.
   * @return an empty max-heap
   */
  public static IntBinaryHeapDouble createMaxHeap(int n) {
    if (n < 1) {
      throw new IllegalArgumentException("domain must be positive");
    }
    return new IntBinaryHeapDouble(n, new DoubleMaxOrder());
  }

  /**
   * Initializes an empty min-heap of (int, priority) pairs, such that the domain of the elements
   * are the integers in [0, n).
   *
   * @param n The size of the domain of the elements of the min-heap.
   * @return an empty min-heap
   */
  public static IntBinaryHeapDouble createMinHeap(int n) {
    if (n < 1) {
      throw new IllegalArgumentException("domain must be positive");
    }
    return new IntBinaryHeapDouble(n, new DoubleMinOrder());
  }

  /**
   * {@inheritDoc}
   *
   * @throws IndexOutOfBoundsException if element is negative, or if element is greater than or
   *     equal to the domain n.
   */
  @Override
  public boolean demote(int element, double priority) {
    return in[element] && internalDemote(element, priority);
  }

  @Override
  public final int domain() {
    return index.length;
  }

  @Override
  public final boolean isEmpty() {
    return size == 0;
  }

  /**
   * {@inheritDoc}
   *
   * @throws IndexOutOfBoundsException if element is negative, or if element is greater than or
   *     equal to the domain n.
   */
  @Override
  public boolean offer(int element, double priority) {
    if (in[element]) {
      return false;
    }
    internalOffer(element, priority);
    return true;
  }

  @Override
  public final int peek() {
    return heap[0];
  }

  @Override
  public double peekPriority() {
    return value[heap[0]];
  }

  /**
   * {@inheritDoc}
   *
   * @throws IndexOutOfBoundsException if element is negative, or if element is greater than or
   *     equal to the domain n.
   */
  @Override
  public double peekPriority(int element) {
    return value[element];
  }

  @Override
  public final int poll() {
    int min = heap[0];
    in[min] = false;
    size--;
    if (size > 0) {
      index[heap[0] = heap[size]] = 0;
      percolateDown(0);
    }
    return min;
  }

  /**
   * Performs the equivalent of a combination of a {@link #poll} and {@link #offer}. Like {@link
   * #poll}, the return value is undefined if the priority queue is empty at the time that this
   * method is called. Thus, you should not call this method on empty priority queues. This method
   * adds an (element, priority) pair to the priority queue with a specified priority provided that
   * the element is not already present. If the element is already present in the priority queue and
   * is at the root of binary heap, then this method will update its priority and return the element
   * (the equivalent behavior if you had instead explicitly called poll followed by offer). If the
   * element is already present in the priority queue but somewhere other than the root, then this
   * method just performs the poll. Unlike the {@link #offer} method, this method does not provide
   * an explicit confirmation of success. If such confirmation is required, then you should instead
   * directly use a combination of {@link #poll} and {@link #offer}.
   *
   * <p>This implementation is more efficient than separately calling {@link #poll} and {@link
   * #offer} as it exploits the structure of a binary heap. However, its asymptotic runtime is no
   * better than a combination of {@link #poll} and {@link #offer}.
   *
   * @param element The element to add.
   * @param priority The priority of the element.
   * @return the next element in priority order. The return value is undefined if the priority queue
   *     is empty at the time that this method is called.
   * @throws IndexOutOfBoundsException if element is negative, or if element is greater than or
   *     equal to the domain n.
   */
  @Override
  public final int pollThenOffer(int element, double priority) {
    if (in[element]) {
      if (index[element] == 0) {
        // case: element is at root of heap
        // action: update priority, percolate, and return element
        value[element] = priority;
        percolateDown(0);
        return element;
      }
      // case: element is somewhere in PQ other than root
      // action: just do the poll
      return poll();
    }
    int min = heap[0];
    in[min] = false;
    index[heap[0] = element] = 0;
    value[element] = priority;
    in[element] = true;
    percolateDown(0);
    return min;
  }

  /**
   * {@inheritDoc}
   *
   * @throws IndexOutOfBoundsException if element is negative, or if element is greater than or
   *     equal to the domain n.
   */
  @Override
  public boolean promote(int element, double priority) {
    return in[element] && internalPromote(element, priority);
  }

  @Override
  public final int size() {
    return size;
  }

  @Override
  public final int[] toArray() {
    return Arrays.copyOf(heap, size);
  }

  private void internalOffer(int element, double priority) {
    index[heap[size] = element] = size;
    value[element] = priority;
    in[element] = true;
    percolateUp(size);
    size++;
  }

  private boolean internalPromote(int element, double priority) {
    if (prioritizer.comesBefore(priority, value[element])) {
      value[element] = priority;
      percolateUp(index[element]);
      return true;
    }
    return false;
  }

  private boolean internalDemote(int element, double priority) {
    if (prioritizer.comesBefore(value[element], priority)) {
      value[element] = priority;
      percolateDown(index[element]);
      return true;
    }
    return false;
  }

  private void percolateDown(int i) {
    int left;
    while ((left = (i << 1) + 1) < size) {
      int smallest = i;
      if (prioritizer.comesBefore(value[heap[left]], value[heap[i]])) {
        smallest = left;
      }
      int right = left + 1;
      if (right < size && prioritizer.comesBefore(value[heap[right]], value[heap[smallest]])) {
        smallest = right;
      }
      if (smallest != i) {
        int temp = heap[i];
        index[heap[i] = heap[smallest]] = i;
        index[heap[smallest] = temp] = smallest;
        i = smallest;
      } else {
        break;
      }
    }
  }

  private void percolateUp(int i) {
    int parent;
    while (i > 0 && prioritizer.comesBefore(value[heap[i]], value[heap[parent = (i - 1) >> 1]])) {
      int temp = heap[i];
      index[heap[i] = heap[parent]] = i;
      index[heap[parent] = temp] = parent;
      i = parent;
    }
  }
}
